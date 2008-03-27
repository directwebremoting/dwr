/*
 * Copyright 2005 Joe Walker
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.directwebremoting.dwrp;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.directwebremoting.extend.ConverterManager;
import org.directwebremoting.extend.EnginePrivate;
import org.directwebremoting.extend.Handler;
import org.directwebremoting.extend.PageNormalizer;
import org.directwebremoting.extend.RealScriptSession;
import org.directwebremoting.extend.ScriptSessionManager;
import org.directwebremoting.extend.ServerException;
import org.directwebremoting.extend.ServerLoadMonitor;
import org.directwebremoting.util.Continuation;
import org.directwebremoting.util.MimeConstants;

/**
 * A Marshaller that output plain Javascript.
 * This marshaller can be tweaked to output Javascript in an HTML context.
 * This class works in concert with CallScriptConduit, they should be
 * considered closely related and it is important to understand what one does
 * while editing the other.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class PollHandler implements Handler
{
    /**
     * @param plain Are we using plain javascript or html wrapped javascript
     */
    public PollHandler(boolean plain)
    {
        this.plain = plain;
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.Handler#handle(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    public void handle(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        // If we are a restarted jetty continuation then we need to finish off
        if (JettyContinuationSleeper.isRestart(request))
        {
            JettyContinuationSleeper.restart(request);
            return;
        }

        // The information that we can extract from the input parameters
        final PollBatch batch;
        try
        {
            batch = new PollBatch(request, pageNormalizer);
        }
        catch (ServerException ex)
        {
            // Send a batch exception to the server because the parse failed
            String script = EnginePrivate.getRemoteHandleBatchExceptionScript(null, ex);
            sendErrorScript(response, script);
            return;
        }

        // We might need to complain that reverse ajax is not enabled.
        if (!activeReverseAjaxEnabled)
        {
            log.error("Polling and Comet are disabled. To enable them set the init-param activeReverseAjaxEnabled to true. See http://getahead.org/dwr/server/servlet for more.");
            String script = EnginePrivate.getRemotePollCometDisabledScript(batch.getBatchId());
            sendErrorScript(response, script);
            return;
        }

        // Complain if GET is disallowed
        if (batch.isGet() && !allowGetForSafariButMakeForgeryEasier)
        {
            // Send a batch exception to the server because the parse failed
            String script = EnginePrivate.getRemoteHandleBatchExceptionScript(batch.getBatchId(), new SecurityException("GET Disallowed"));
            sendErrorScript(response, script);
            return;
        }

        // If we are going to be doing any waiting then check for other threads
        // from the same browser that are already waiting, and send them on
        // their way
        final long maxConnectedTime = serverLoadMonitor.getConnectedTime();
        if (maxConnectedTime > 0)
        {
            // Make other threads from the same browser stop waiting and continue
            // First we check to see if there is already a connection from the
            // current browser to this servlet
            Sleeper otherThread = (Sleeper) request.getSession().getAttribute(ATTRIBUTE_SLEEPER);
            if (otherThread != null)
            {
                otherThread.wakeUp();
            }
        }

        // Create a conduit depending on the type of request (from the URL)
        final BaseScriptConduit conduit = createScriptConduit(batch, response);

        // Register the conduit with a script session so messages can get out
        final RealScriptSession scriptSession = batch.getScriptSession();
        scriptSession.addScriptConduit(conduit);

        // So we're going to go to sleep. How do we wake up?
        final Sleeper sleeper;
        // If this is Jetty then we can use Continuations
        if (Continuation.isJetty())
        {
            sleeper = new JettyContinuationSleeper(request);
        }
        else
        {
            sleeper = new ThreadWaitSleeper();
        }

        // There are various reasons why we want to wake up and carry on ...
        final List alarms = new ArrayList();

        // The conduit might want to say 'I give up'
        alarms.add(conduit.getErrorAlarm());

        // Set the system up to resume on output (perhaps with delay)
        if (batch.getPartialResponse() == PartialResponse.NO || maxWaitAfterWrite != -1)
        {
            // add an output listener to the script session that calls the
            // "wake me" method on whatever is putting us to sleep
            alarms.add(new OutputAlarm(scriptSession, maxWaitAfterWrite));
        }

        // Set the system up to resume anyway after maxConnectedTime
        alarms.add(new TimedAlarm(maxConnectedTime));

        // We also need to wakeup if the server is being shut down
        // WARNING: This code has a non-obvious side effect - The server load
        // monitor (which hands out shutdown messages) also monitors usage by
        // looking at the number of connected alarms.
        alarms.add(new ShutdownAlarm(serverLoadMonitor));

        // Make sure that all the alarms know what to wake
        for (Iterator it = alarms.iterator(); it.hasNext();)
        {
            Alarm alarm = (Alarm) it.next();
            alarm.setAlarmAction(sleeper);
        }

        // Allow other threads to notice more than one poll thread and to send
        // this one on it's way
        final HttpSession session = request.getSession();
        session.setAttribute(ATTRIBUTE_SLEEPER, sleeper);

        // We need to do something sensible when we wake up ...
        Runnable onAwakening = new Runnable()
        {
            public void run()
            {
                // There is no point in letting other threads try to move us on
                session.removeAttribute(ATTRIBUTE_SLEEPER);

                // Cancel all the alarms
                for (Iterator it = alarms.iterator(); it.hasNext();)
                {
                    Alarm alarm = (Alarm) it.next();
                    alarm.cancel();
                }

                // We can't be used as a conduit to the browser any more
                scriptSession.removeScriptConduit(conduit);

                // Tell the browser to come back at the right time
                try
                {
                    int timeToNextPoll = serverLoadMonitor.getDisconnectedTime();
                    conduit.close(timeToNextPoll);
                }
                catch (IOException ex)
                {
                    log.warn("Failed to write reconnect info to browser");
                }
            }
        };

        // Actually go to sleep. This *must* be the last thing in this method to
        // cope with all the methods of affecting Threads. Jetty throws,
        // Weblogic continues, others wait().
        sleeper.goToSleep(onAwakening);
    }

    /**
     * Create the correct type of ScriptConduit depending on the request.
     * @param batch The parsed request
     * @param response Conduits need a response to write to
     * @return A correctly configured conduit
     * @throws IOException If the response can't be interogated
     */
    private BaseScriptConduit createScriptConduit(PollBatch batch, HttpServletResponse response) throws IOException
    {
        BaseScriptConduit conduit;

        if (plain)
        {
            conduit = new PlainScriptConduit(response, batch.getBatchId(), converterManager);
        }
        else
        {
            if (batch.getPartialResponse() == PartialResponse.FLUSH)
            {
                conduit = new Html4kScriptConduit(response, batch.getBatchId(), converterManager);
            }
            else
            {
                conduit = new HtmlScriptConduit(response, batch.getBatchId(), converterManager);
            }
        }

        return conduit;
    }

    /**
     * Send a script to the browser and wrap it in the required prefixes etc.
     * @param response The http response to write to
     * @param script The script to write
     * @throws IOException if writing fails.
     */
    protected void sendErrorScript(HttpServletResponse response, String script) throws IOException
    {
        PrintWriter out = response.getWriter();
        if (plain)
        {
            response.setContentType(MimeConstants.MIME_PLAIN);
        }
        else
        {
            response.setContentType(MimeConstants.MIME_HTML);
        }

        out.println(ProtocolConstants.SCRIPT_START_MARKER);
        out.println(script);
        out.println(ProtocolConstants.SCRIPT_END_MARKER);
    }

    /**
     * Accessor for the DefaultCreatorManager that we configure
     * @param converterManager The new DefaultConverterManager
     */
    public void setConverterManager(ConverterManager converterManager)
    {
        this.converterManager = converterManager;
    }

    /**
     * Accessor for the server load monitor
     * @param serverLoadMonitor the new server load monitor
     */
    public void setServerLoadMonitor(ServerLoadMonitor serverLoadMonitor)
    {
        this.serverLoadMonitor = serverLoadMonitor;
    }

    /**
     * Accessor for the PageNormalizer.
     * @param pageNormalizer The new PageNormalizer
     */
    public void setPageNormalizer(PageNormalizer pageNormalizer)
    {
        this.pageNormalizer = pageNormalizer;
    }

    /**
     * @param scriptSessionManager the scriptSessionManager to set
     */
    public void setScriptSessionManager(ScriptSessionManager scriptSessionManager)
    {
        this.scriptSessionManager = scriptSessionManager;
    }

    /**
     * Use {@link #setActiveReverseAjaxEnabled(boolean)}
     * @param pollAndCometEnabled Are we doing full reverse ajax
     * @deprecated Use {@link #setActiveReverseAjaxEnabled(boolean)}
     */
    public void setPollAndCometEnabled(boolean pollAndCometEnabled)
    {
        this.activeReverseAjaxEnabled = pollAndCometEnabled;
    }

    /**
     * Are we doing full reverse ajax
     * @param activeReverseAjaxEnabled Are we doing full reverse ajax
     */
    public void setActiveReverseAjaxEnabled(boolean activeReverseAjaxEnabled)
    {
        this.activeReverseAjaxEnabled = activeReverseAjaxEnabled;
    }

    /**
     * @param allowGetForSafariButMakeForgeryEasier Do we reduce security to help Safari
     */
    public void setAllowGetForSafariButMakeForgeryEasier(boolean allowGetForSafariButMakeForgeryEasier)
    {
        this.allowGetForSafariButMakeForgeryEasier = allowGetForSafariButMakeForgeryEasier;
    }

    /**
     * Sometimes with proxies, you need to close the stream all the time to
     * make the flush work. A value of -1 indicated that we do not do early
     * closing after writes.
     * @param maxWaitAfterWrite the maxWaitAfterWrite to set
     */
    public void setMaxWaitAfterWrite(int maxWaitAfterWrite)
    {
        this.maxWaitAfterWrite = maxWaitAfterWrite;
    }

    /**
     * Are we doing full reverse ajax
     */
    protected boolean activeReverseAjaxEnabled = false;

    /**
     * By default we disable GET, but this hinders old Safaris
     */
    protected boolean allowGetForSafariButMakeForgeryEasier = false;

    /**
     * Sometimes with proxies, you need to close the stream all the time to
     * make the flush work. A value of -1 indicated that we do not do early
     * closing after writes.
     * See also: org.directwebremoting.servlet.FileHandler.maxWaitAfterWrite
     */
    protected int maxWaitAfterWrite = -1;

    /**
     * Are we using plain javascript or html wrapped javascript
     */
    protected boolean plain;

    /**
     * How we turn pages into the canonical form.
     */
    protected PageNormalizer pageNormalizer;

    /**
     * We need to tell the system that we are waiting so it can load adjust
     */
    protected ServerLoadMonitor serverLoadMonitor = null;

    /**
     * How we convert parameters
     */
    protected ConverterManager converterManager = null;

    /**
     * The owner of script sessions
     */
    protected ScriptSessionManager scriptSessionManager = null;

    /**
     * We remember people that are in a long poll so we can kick them out
     */
    private static final String ATTRIBUTE_SLEEPER = "org.directwebremoting.dwrp.sleeper";

    /**
     * The log stream
     */
    protected static final Log log = LogFactory.getLog(PollHandler.class);
}
