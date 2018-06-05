package org.directwebremoting.dwrp;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.directwebremoting.Container;
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
    public void handle(final HttpServletRequest request, final HttpServletResponse response) throws IOException
    {
        // If you're new to understanding this file, you may wish to skip this
        // step and come back to it later ;-)
        // So Jetty does something a bit weird with Ajax Continuations. You
        // suspend a request while keeping hold
        // of a continuation object. There are methods on this continuation
        // object to resume the request. Also you can write to the output at
        // any time the request is suspended. When the continuation is
        // resumed, rather than resume the thread from where is was
        // suspended, it starts it from the beginning again and will arrive
        // here. Below we detect this and bail out after doing the work
        // associated with the resumed request.
        if (containerAbstraction.handleResumedRequest(request))
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

            // Send a HTTP 400 to signal Bad Request
            // (we have no possibility to reply with a DWR handle* script call as we don't know
            // the instanceId, and this is required for script tag remoting
            response.sendError(400, "Failed to parse request");
            return;
        }

        // Security checks first, once we've parsed the input
        checkGetAllowed(batch);
        checkNotCsrfAttack(request, batch);

        // Initialize WebContext stuff
        String normalizedPage = pageNormalizer.normalizePage(batch.getPage());
        final RealScriptSession scriptSession = scriptSessionManager.getOrCreateScriptSession(batch.getScriptSessionId(), normalizedPage, request.getSession(false));
        RealWebContext webContext = (RealWebContext) WebContextFactory.get();
        webContext.initialize(normalizedPage, scriptSession);

        // We might need to complain that reverse ajax is not enabled.
        if (!activeReverseAjaxEnabled)
        {
            log.error("Polling and Comet are disabled. To enable them set the init-param activeReverseAjaxEnabled to true.");
            String script = EnginePrivate.getRemotePollCometDisabledScript(batch.getBatchId());
            sendErrorScript(response, batch, script);
            return;
        }

        // A script conduit is some route from a ScriptSession back to the page
        // that belongs to the session. There may be zero or many of these
        // conduits (although if there are more than 2, something is strange)
        // All scripts destined for a page go to a ScriptSession and then out
        // via a ScriptConduit.
        scriptSession.confirmScripts(batch.getNextReverseAjaxIndex() - 1);

        // Create a conduit depending on the type of request (from the URL)
        final BaseScriptConduit conduit = createScriptConduit(response.getWriter(), batch);

        // So we're going to go to sleep. How do we wake up?
        final Sleeper sleeper = containerAbstraction.createSleeper(request, response, scriptSession, conduit);
        container.initializeBean(sleeper);

        // There are various reasons why we want to wake up and carry on ...
        final List<Alarm> alarms = new ArrayList<Alarm>();

        // Use of comet depends on the type of browser and the number of current
        // connections from this browser (detected by cookies)
        boolean clientSupportsLongRequests = BrowserDetect.supportsComet(request);
        boolean clientSupportsStreamingUpdates = (batch.getPartialResponse() != PartialResponse.NO);
        boolean configurationSaysFullStreaming = streamingEnabled || (maxWaitAfterWrite == -1);
        boolean canWeHaveFullStreaming = clientSupportsLongRequests && clientSupportsStreamingUpdates && configurationSaysFullStreaming;
        // For early closing mode add an output listener to the script session that calls the
        // "wake me" method on whatever is putting us to sleep - if the client
        // does not support streaming or streaming has not been configured.
        final Sleeper proxiedSleeper;
        if (!canWeHaveFullStreaming) {
    		final int earlyCloseTimeout = (maxWaitAfterWrite == -1) ? ProtocolConstants.FALLBACK_MAX_WAIT_AFTER_WRITE : maxWaitAfterWrite;
    		proxiedSleeper = new Sleeper()
            {
                public void enterSleep(String batchId, Runnable onClose, int disconnectedTime) throws IOException
                {
                    sleeper.enterSleep(batchId, onClose, disconnectedTime);
                }
                public void wakeUpForData()
                {
                    executor.schedule(new Runnable()
                    {
                        public void run()
                        {
                            sleeper.wakeUpToClose();
                        }
                    }, earlyCloseTimeout, TimeUnit.MILLISECONDS);
                    sleeper.wakeUpForData();
                }
                public int wakeUpToClose()
                {
                    return sleeper.wakeUpToClose();
                }
            };
        } else {
            proxiedSleeper = sleeper;
        }

        // Set the system up to resume anyway after maxConnectedTime
        alarms.add(new TimedAlarm(proxiedSleeper, serverLoadMonitor.getConnectedTime(), executor));

        // We also need to wake-up if the server is being shut down
        // WARNING: This code has a non-obvious side effect - The server load
        // monitor (which hands out shutdown messages) also monitors usage by
        // looking at the number of connected alarms.
        alarms.add(new ShutdownAlarm(proxiedSleeper, serverLoadMonitor));

        // Register the sleeper with a script session so messages can get out.
        scriptSession.setSleeper(proxiedSleeper);

        // We need to do some stuff when time has come to close sleeper...
        final int disconnectedTime = serverLoadMonitor.getDisconnectedTime();
        Runnable onClose = new Runnable()
        {
            public void run()
            {
                // Cancel all the alarms
                for (Alarm alarm : alarms)
                {
                    alarm.cancel();
                }

                // We can't be used as a sleeper for this session any longer
                scriptSession.clearSleeper(proxiedSleeper);

                updateCsrfState(request, batch);
            }
        };

        // Flush any queued scripts
        if (scriptSession.getScript(0) != null) {
            proxiedSleeper.wakeUpForData();
        }

        // Actually go to sleep. This *must* be the last thing in this method to
        // cope with all the methods of affecting Threads.
        proxiedSleeper.enterSleep(batch.getBatchId(), onClose, disconnectedTime);
    }

    /**
     * Create the correct type of ScriptConduit depending on the request.
     * @param batch The parsed request
     * @return A correctly configured conduit
     * @throws IOException If the response can't be interrogated
     */
    private BaseScriptConduit createScriptConduit(PrintWriter out, PollBatch batch) throws IOException
    {
        BaseScriptConduit conduit;

        if (plain)
        {
            conduit = new PlainScriptConduit(out, batch.getInstanceId(), null);
        }
        else
        {
            conduit = new HtmlScriptConduit(out, batch.getInstanceId(), batch.getBatchId(), batch.getDocumentDomain());
        }

        return conduit;
    }

    /**
     * Send a script to the browser and wrap it in the required prefixes etc.
     * @param response The http response to write to
     * @param script The script to write
     * @throws IOException if writing fails.
     */
    protected void sendErrorScript(HttpServletResponse response, Batch batch, String script) throws IOException
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
        out.println(EnginePrivate.remoteBeginWrapper(batch.getInstanceId(), !plain, null));
        out.println(script);
        out.println(EnginePrivate.remoteEndWrapper(batch.getInstanceId(), !plain));
        out.println(ProtocolConstants.SCRIPT_END_MARKER);
    }

    /**
     * @param container DI container
     */
    public void setContainer(Container container)
    {
        this.container = container;
    }

    /**
     * DI container
     */
    protected Container container;

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
     * make the flush work. A value of -1 indicates that we do not do early
     * closing after writes IF the client supports streaming.  If the client
     * does not support streaming FALLBACK_MAX_WAIT_AFTER_WRITE is used.
     * @param maxWaitAfterWrite the maxWaitAfterWrite to set
     */
    public void setMaxWaitAfterWrite(int maxWaitAfterWrite)
    {
        this.maxWaitAfterWrite = maxWaitAfterWrite;
    }

    /**
     * Sometimes with proxies, you need to close the stream all the time to
     * make the flush work. A value of -1 indicates that we do not do early
     * closing after writes IF the client supports streaming.  If the client
     * does not support streaming FALLBACK_MAX_WAIT_AFTER_WRITE is used.
     */
    protected int maxWaitAfterWrite = -1;

    /**
     * Do we support streaming for clients that allow it?
     *
     * @param streamingEnabled
     */
    public void setStreamingEnabled(boolean streamingEnabled)
    {
    	this.streamingEnabled = streamingEnabled;
    }

    /**
     * Do we support streaming for clients that allow it?
     */
    private boolean streamingEnabled;

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
     * Accessor for the DefaultConverterManager that we configure
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
