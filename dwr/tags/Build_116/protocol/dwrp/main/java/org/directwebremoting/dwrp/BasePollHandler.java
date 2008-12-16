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
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.directwebremoting.WebContextFactory;
import org.directwebremoting.extend.Alarm;
import org.directwebremoting.extend.ContainerAbstraction;
import org.directwebremoting.extend.ConverterManager;
import org.directwebremoting.extend.EnginePrivate;
import org.directwebremoting.extend.PageNormalizer;
import org.directwebremoting.extend.ProtocolConstants;
import org.directwebremoting.extend.RealScriptSession;
import org.directwebremoting.extend.RealWebContext;
import org.directwebremoting.extend.ScriptSessionManager;
import org.directwebremoting.extend.ServerLoadMonitor;
import org.directwebremoting.extend.Sleeper;
import org.directwebremoting.impl.OutputAlarm;
import org.directwebremoting.impl.PollingServerLoadMonitor;
import org.directwebremoting.impl.ShutdownAlarm;
import org.directwebremoting.impl.TimedAlarm;
import org.directwebremoting.util.BrowserDetect;
import org.directwebremoting.util.MimeConstants;

/**
 * A Marshaller that output plain Javascript.
 * This marshaller can be tweaked to output Javascript in an HTML context.
 * This class works in concert with CallScriptConduit, they should be
 * considered closely related and it is important to understand what one does
 * while editing the other.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class BasePollHandler extends BaseDwrpHandler
{
    /**
     * @param plain Are we using plain javascript or html wrapped javascript
     */
    public BasePollHandler(boolean plain)
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
        if (containerAbstraction.isResponseCompleted(request))
        {
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
        catch (Exception ex)
        {
            log.debug("Failed to parse request", ex);

            // Send a batch exception to the server because the parse failed
            String script = EnginePrivate.getRemoteHandleBatchExceptionScript(null, ex);
            sendErrorScript(response, script);
            return;
        }

        // Security checks first, once we've parsed the input
        checkGetAllowed(batch);
        checkNotCsrfAttack(request, batch);

        // Check to see that the page and script session id are valid
        String normalizedPage = pageNormalizer.normalizePage(batch.getPage());
        RealWebContext webContext = (RealWebContext) WebContextFactory.get();
        webContext.checkPageInformation(normalizedPage, batch.getScriptSessionId(), batch.getWindowName());

        // We might need to complain that reverse ajax is not enabled.
        if (!activeReverseAjaxEnabled)
        {
            log.error("Polling and Comet are disabled. To enable them set the init-param activeReverseAjaxEnabled to true. See http://getahead.org/dwr/server/servlet for more.");
            String script = EnginePrivate.getRemotePollCometDisabledScript(batch.getBatchId());
            sendErrorScript(response, script);
            return;
        }

        // A script conduit is some route from a ScriptSession back to the page
        // that belongs to the session. There may be zero or many of these
        // conduits (although if there are more than 2, something is strange)
        // All scripts destined for a page go to a ScriptSession and then out
        // via a ScriptConduit.
        final RealScriptSession scriptSession = (RealScriptSession) webContext.getScriptSession();

        // So we're going to go to sleep. How do we wake up?
        Sleeper sleeper = containerAbstraction.createSleeper(request);

        // Create a conduit depending on the type of request (from the URL)
        final BaseScriptConduit conduit = createScriptConduit(sleeper, batch, response);

        // There are various reasons why we want to wake up and carry on ...
        final List<Alarm> alarms = new ArrayList<Alarm>();

        // If the conduit has an error flushing data, it needs to give up
        alarms.add(conduit);

        // Set the system up to resume on output (perhaps with delay)
        if (batch.getPartialResponse() == PartialResponse.NO || maxWaitAfterWrite != -1)
        {
            // add an output listener to the script session that calls the
            // "wake me" method on whatever is putting us to sleep
            alarms.add(new OutputAlarm(sleeper, scriptSession, maxWaitAfterWrite, executor));
        }

        // Use of comet depends on the type of browser and the number of current
        // connections from this browser (detected by cookies)
        boolean comet = BrowserDetect.supportsComet(request);
        if (comet)
        {
            // Nasty 2 connection limit hack. How many times is this browser connected?
            String httpSessionId = webContext.getSession(true).getId();
            Collection<RealScriptSession> sessions = scriptSessionManager.getScriptSessionsByHttpSessionId(httpSessionId);
            int persistentConnections = 0;
            for (RealScriptSession session : sessions)
            {
                persistentConnections += session.countPersistentConnections();
            }

            int connectionLimit = BrowserDetect.getConnectionLimit(request);
            if (persistentConnections + 1 >= connectionLimit)
            {
                comet = false;

                if (log.isDebugEnabled())
                {
                    String uaStr = BrowserDetect.getUserAgentDebugString(request);
                    log.debug("Persistent connections=" + persistentConnections + ". (limit=" + connectionLimit + " in " + uaStr + "). Polling");
                }
            }
        }
        else
        {
            log.debug("Browser does not support comet, polling");
        }

        // Set the system up to resume anyway after maxConnectedTime
        ServerLoadMonitor slm = comet ? serverLoadMonitor : pollingServerLoadMonitor;
        long connectedTime = slm.getConnectedTime();
        final int disconnectedTime = slm.getDisconnectedTime();

        alarms.add(new TimedAlarm(sleeper, connectedTime, executor));

        // We also need to wake-up if the server is being shut down
        // WARNING: This code has a non-obvious side effect - The server load
        // monitor (which hands out shutdown messages) also monitors usage by
        // looking at the number of connected alarms.
        alarms.add(new ShutdownAlarm(sleeper, serverLoadMonitor));

        // Register the conduit with a script session so messages can get out.
        // This must happen late on in this method because this will cause any
        // scripts cached in the script session (because there was no conduit
        // available when they were written) to be sent to the conduit.
        // We need any AlarmScriptConduits to be notified so they can make
        // maxWaitWfterWrite work for all cases
        scriptSession.addScriptConduit(conduit);

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
                    conduit.close(disconnectedTime);
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
     * @throws IOException If the response can't be interrogated
     */
    private BaseScriptConduit createScriptConduit(Sleeper sleeper, PollBatch batch, HttpServletResponse response) throws IOException
    {
        BaseScriptConduit conduit;

        if (plain)
        {
            conduit = new PlainScriptConduit(sleeper, response, batch.getBatchId(), converterManager, jsonOutput);
        }
        else
        {
            if (batch.getPartialResponse() == PartialResponse.FLUSH)
            {
                conduit = new Html4kScriptConduit(sleeper, response, batch.getBatchId(), converterManager, jsonOutput);
            }
            else
            {
                conduit = new HtmlScriptConduit(sleeper, response, batch.getBatchId(), converterManager, jsonOutput);
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
     * Are we doing full reverse ajax
     */
    protected boolean activeReverseAjaxEnabled = false;

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
     * Sometimes with proxies, you need to close the stream all the time to
     * make the flush work. A value of -1 indicated that we do not do early
     * closing after writes.
     */
    protected int maxWaitAfterWrite = -1;

    /**
     * Accessor for the PageNormalizer.
     * @param pageNormalizer The new PageNormalizer
     */
    public void setPageNormalizer(PageNormalizer pageNormalizer)
    {
        this.pageNormalizer = pageNormalizer;
    }

    /**
     * How we turn pages into the canonical form.
     */
    protected PageNormalizer pageNormalizer;

    /**
     * Accessor for the server load monitor
     * @param serverLoadMonitor the new server load monitor
     */
    public void setServerLoadMonitor(ServerLoadMonitor serverLoadMonitor)
    {
        this.serverLoadMonitor = serverLoadMonitor;
    }

    /**
     * We need to tell the system that we are waiting so it can load adjust
     */
    protected ServerLoadMonitor serverLoadMonitor = null;

    /**
     * There is only one polling ServerLoadMonitor, so we're not bothering
     * with setters for now
     */
    protected ServerLoadMonitor pollingServerLoadMonitor = new PollingServerLoadMonitor();

    /**
     * Accessor for the DefaultCreatorManager that we configure
     * @param converterManager The new DefaultConverterManager
     */
    public void setConverterManager(ConverterManager converterManager)
    {
        this.converterManager = converterManager;
    }

    /**
     * How we convert parameters
     */
    protected ConverterManager converterManager = null;

    /**
     * @param scriptSessionManager the scriptSessionManager to set
     */
    public void setScriptSessionManager(ScriptSessionManager scriptSessionManager)
    {
        this.scriptSessionManager = scriptSessionManager;
    }

    /**
     * The owner of script sessions
     */
    protected ScriptSessionManager scriptSessionManager = null;

    /**
     * @param containerAbstraction the containerAbstraction to set
     */
    public void setContainerAbstraction(ContainerAbstraction containerAbstraction)
    {
        this.containerAbstraction = containerAbstraction;
    }

    /**
     * How we abstract away container specific logic
     */
    protected ContainerAbstraction containerAbstraction = null;

    /**
     * How often do we check for script sessions that need timing out
     */
    public void setScheduledThreadPoolExecutor(ScheduledThreadPoolExecutor executor)
    {
        this.executor = executor;
    }

    /**
     * @see #setScheduledThreadPoolExecutor(ScheduledThreadPoolExecutor)
     */
    protected ScheduledThreadPoolExecutor executor;

    /**
     * Are we using plain javascript or html wrapped javascript.
     * This is set by the constructor
     */
    protected boolean plain;

    /**
     * The log stream
     */
    private static final Log log = LogFactory.getLog(BasePollHandler.class);
}
