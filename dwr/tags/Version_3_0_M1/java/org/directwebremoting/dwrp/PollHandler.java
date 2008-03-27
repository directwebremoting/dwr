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
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.directwebremoting.WebContextFactory;
import org.directwebremoting.extend.Alarm;
import org.directwebremoting.extend.ContainerAbstraction;
import org.directwebremoting.extend.ConverterManager;
import org.directwebremoting.extend.EnginePrivate;
import org.directwebremoting.extend.Handler;
import org.directwebremoting.extend.PageNormalizer;
import org.directwebremoting.extend.RealScriptSession;
import org.directwebremoting.extend.RealWebContext;
import org.directwebremoting.extend.ScriptSessionManager;
import org.directwebremoting.extend.ServerException;
import org.directwebremoting.extend.ServerLoadMonitor;
import org.directwebremoting.extend.Sleeper;
import org.directwebremoting.impl.JettyContinuationSleeper;
import org.directwebremoting.impl.OutputAlarm;
import org.directwebremoting.impl.ShutdownAlarm;
import org.directwebremoting.impl.TimedAlarm;
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
    @SuppressWarnings({"ThrowableInstanceNeverThrown"})
    public void handle(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        // If you're new to understanding this file, you may wish to skip this
        // step and come back to it later ;-)
        // So Jetty does something a bit weird with Ajax Continuations. You
        // suspend a request (which works via an exception) while keeping hold
        // of a continuation object. There are methods on this continuation
        // object to restart the request. Also you can write to the output at
        // any time the request is suspended. When the continuation is
        // restarted, rather than restart the thread from where is was
        // suspended, it starts it from the beginning again. Since we are able
        // to write to the response outside of the servlet thread, there is no
        // need for us to do anything if we have been restarted. So we ignore
        // all Jetty continuation restarts.
        if (JettyContinuationSleeper.isRestart(request))
        {
            JettyContinuationSleeper.restart(request);
            return;
        }

        // A PollBatch is the information that we expect from the request.
        // if the parse fails we can do little more than tell the browser that
        // something went wrong.
        final PollBatch batch;
        try
        {
            batch = new PollBatch(request);
        }
        catch (ServerException ex)
        {
            // Send a batch exception to the server because the parse failed
            String script = EnginePrivate.getRemoteHandleBatchExceptionScript(null, ex);
            sendErrorScript(response, script);
            return;
        }

        // Check to see that the page and script session id are valid
        RealWebContext webContext = (RealWebContext) WebContextFactory.get();
        String normalizedPage = pageNormalizer.normalizePage(batch.getPage());
        webContext.checkPageInformation(normalizedPage, batch.getScriptSessionId(), batch.getWindowName());

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

        // A script conduit is some route from a ScriptSession back to the page
        // that belongs to the session. There may be zero or many of these
        // conduits (although if there are more than 2, something is strange)
        // All scripts destined for a page go to a ScriptSession and then out
        // via a ScriptConduit.
        final RealScriptSession scriptSession = (RealScriptSession) webContext.getScriptSession();

        // Create a conduit depending on the type of request (from the URL)
        final BaseScriptConduit conduit = createScriptConduit(batch, response);

        // So we're going to go to sleep. How do we wake up?
        Sleeper sleeper = containerAbstraction.createSleeper(request);

        // There are various reasons why we want to wake up and carry on ...
        final List<Alarm> alarms = new ArrayList<Alarm>();

        // If the conduit has an error flushing data, it needs to give up
        alarms.add(conduit.getErrorAlarm());

        // Set the system up to resume on output (perhaps with delay)
        if (batch.getPartialResponse() == PartialResponse.NO || maxWaitAfterWrite != -1)
        {
            // add an output listener to the script session that calls the
            // "wake me" method on whatever is putting us to sleep
            alarms.add(new OutputAlarm(scriptSession, maxWaitAfterWrite));
        }

        // Set the system up to resume anyway after maxConnectedTime
        long maxConnectedTime = serverLoadMonitor.getConnectedTime();
        alarms.add(new TimedAlarm(maxConnectedTime));

        // We also need to wake-up if the server is being shut down
        // WARNING: This code has a non-obvious side effect - The server load
        // monitor (which hands out shutdown messages) also monitors usage by
        // looking at the number of connected alarms.
        alarms.add(new ShutdownAlarm(serverLoadMonitor));

        // Make sure that all the alarms know what to wake
        for (Alarm alarm : alarms)
        {
            alarm.setAlarmAction(sleeper);
        }

        // We need to do something sensible when we wake up ...
        Runnable onAwakening = new Runnable()
        {
            public void run()
            {
                // Cancel all the alarms
                for (Alarm alarm : alarms)
                {
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

        // Register the conduit with a script session so messages can get out.
        // This must happen late on in this method because this will cause any
        // scripts cached in the script session (because there was no conduit
        // available when they were written) to be sent to the conduit.
        // We need any AlarmScriptConduits to be notified so they can make
        // maxWaitWfterWrite work for all cases
        scriptSession.addScriptConduit(conduit);

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
     * @throws IOException If the response can't be interrogated
     */
    private BaseScriptConduit createScriptConduit(PollBatch batch, HttpServletResponse response) throws IOException
    {
        BaseScriptConduit conduit;

        if (plain)
        {
            conduit = new PlainScriptConduit(response, batch.getBatchId(), converterManager, jsonOutput);
        }
        else
        {
            if (batch.getPartialResponse() == PartialResponse.FLUSH)
            {
                conduit = new Html4kScriptConduit(response, batch.getBatchId(), converterManager, jsonOutput);
            }
            else
            {
                conduit = new HtmlScriptConduit(response, batch.getBatchId(), converterManager, jsonOutput);
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
    @Deprecated
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
     * @param containerAbstraction the containerAbstraction to set
     */
    public void setContainerAbstraction(ContainerAbstraction containerAbstraction)
    {
        this.containerAbstraction = containerAbstraction;
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
     * @return Are we outputting in JSON mode?
     */
    public boolean isJsonOutput()
    {
        return jsonOutput;
    }

    /**
     * @param jsonOutput Are we outputting in JSON mode?
     */
    public void setJsonOutput(boolean jsonOutput)
    {
        this.jsonOutput = jsonOutput;
    }

    /**
     * Are we outputting in JSON mode?
     */
    protected boolean jsonOutput = false;

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
     * How we abstract away container specific logic
     */
    protected ContainerAbstraction containerAbstraction = null;

    /**
     * The log stream
     */
    protected static final Log log = LogFactory.getLog(PollHandler.class);
}
