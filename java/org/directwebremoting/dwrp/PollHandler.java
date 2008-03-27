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
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.directwebremoting.WebContext;
import org.directwebremoting.WebContextFactory;
import org.directwebremoting.extend.ConverterManager;
import org.directwebremoting.extend.EnginePrivate;
import org.directwebremoting.extend.Handler;
import org.directwebremoting.extend.PageNormalizer;
import org.directwebremoting.extend.RealScriptSession;
import org.directwebremoting.extend.ScriptConduit;
import org.directwebremoting.extend.ScriptSessionManager;
import org.directwebremoting.extend.ServerLoadMonitor;
import org.directwebremoting.extend.WaitController;
import org.directwebremoting.util.Continuation;
import org.directwebremoting.util.Logger;
import org.directwebremoting.util.Messages;
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
        // We must parse the parameters before we setup the conduit because it's
        // only after doing this that we know the scriptSessionId
        WebContext webContext = WebContextFactory.get();

        boolean isGet = request.getMethod().equals("GET");
        Map parameters = (Map) request.getAttribute(ATTRIBUTE_PARAMETERS);
        if (parameters == null)
        {
            try
            {
                if (isGet)
                {
                    parameters = ParseUtil.parseGet(request);
                }
                else
                {
                    parameters = ParseUtil.parsePost(request);
                }
                request.setAttribute(ATTRIBUTE_PARAMETERS, parameters);
            }
            catch (Exception ex)
            {
                // Send a batch exception to the server because the parse failed
                String script = EnginePrivate.getRemoteHandleBatchExceptionScript(null, ex);
                sendErrorScript(response, script);
                return;
            }
        }

        String batchId = extractParameter(request, parameters, ATTRIBUTE_CALL_ID, ProtocolConstants.INBOUND_KEY_BATCHID);
        String scriptId = extractParameter(request, parameters, ATTRIBUTE_SESSION_ID, ProtocolConstants.INBOUND_KEY_SCRIPT_SESSIONID);
        String page = extractParameter(request, parameters, ATTRIBUTE_PAGE, ProtocolConstants.INBOUND_KEY_PAGE);
        String prString = extractParameter(request, parameters, ATTRIBUTE_PARTIAL_RESPONSE, ProtocolConstants.INBOUND_KEY_PARTIAL_RESPONSE);
        int partialResponse = Integer.valueOf(prString).intValue();

        // We might need to complain that reverse ajax is not enabled.
        if (!activeReverseAjaxEnabled)
        {
            log.error("Polling and Comet are disabled. To enable them set the init-param activeReverseAjaxEnabled to true. See http://getahead.org/dwr/server/servlet for more.");
            String script = EnginePrivate.getRemotePollCometDisabledScript(batchId);
            sendErrorScript(response, script);
            return;
        }

        if (!allowGetForSafariButMakeForgeryEasier && isGet)
        {
            // Send a batch exception to the server because the parse failed
            String script = EnginePrivate.getRemoteHandleBatchExceptionScript(batchId, new SecurityException("GET Disallowed"));
            sendErrorScript(response, script);
            return;
        }

        // Various bits of parseResponse need to be stashed away places
        String normalizedPage = pageNormalizer.normalizePage(page);

        webContext.setCurrentPageInformation(normalizedPage, scriptId);
        RealScriptSession scriptSession = (RealScriptSession) webContext.getScriptSession();

        long maxConnectedTime = serverLoadMonitor.getConnectedTime();
        long endTime = System.currentTimeMillis() + maxConnectedTime;

        // If we are going to be doing any waiting then check for other threads
        // from the same browser that are already waiting, and send them on
        // their way
        if (maxConnectedTime > 0)
        {
            notifyThreadsFromSameBrowser(request, scriptId);
        }

        ScriptConduit notifyConduit = new NotifyOnlyScriptConduit(scriptSession.getScriptLock());

        // The pre-stream wait. A wait before we open any output stream
        // Don't wait if we would wait for 0s or if there are queued scripts
        boolean canWaitMore = true;
        if (maxConnectedTime > 0 && !scriptSession.hasWaitingScripts())
        {
            canWaitMore = streamWait(request, notifyConduit, scriptSession, maxConnectedTime);
        }

        BaseScriptConduit conduit;
        if (plain)
        {
            conduit = new PlainScriptConduit(response, batchId, converterManager);
        }
        else
        {
            //conduit = new Html4kScriptConduit(response, partialResponse, batchId, converterManager);
            conduit = new HtmlScriptConduit(response, batchId, converterManager);
        }

        // How much longer do we wait now the stream is open?
        long extraWait = endTime - System.currentTimeMillis();

        // We might need to cut out waiting short to force proxies to flush
        if (maxWaitAfterWrite != -1 && extraWait > maxWaitAfterWrite)
        {
            extraWait = maxWaitAfterWrite;
        }

        // Short-circut if we are not waiting at all
        if (extraWait <= 0)
        {
            canWaitMore = false;
        }

        if (canWaitMore && partialResponse != PARTIAL_RESPONSE_NO)
        {
            streamWait(request, conduit, scriptSession, extraWait);
        }
        else
        {
            scriptSession.writeScripts(conduit);
        }

        int timeToNextPoll = serverLoadMonitor.getDisconnectedTime();

        conduit.close(timeToNextPoll);
    }

    /**
     * Perform a wait.
     * @param request The HTTP request, needed to start a Jetty continuation
     * @param conduit A conduit if there is an open stream or null if not
     * @param scriptSession The script that we lock against
     * @param wait How long do we wait for?
     * @return True if the wait did not end in a shutdown request
     * @throws IOException If an IO error occurs
     */
    protected boolean streamWait(HttpServletRequest request, ScriptConduit conduit, RealScriptSession scriptSession, long wait) throws IOException
    {
        Object lock = scriptSession.getScriptLock();
        WaitController controller = new NotifyWaitController(lock);

        try
        {
            serverLoadMonitor.threadWaitStarting(controller);
            if (conduit != null)
            {
                scriptSession.addScriptConduit(conduit);
            }

            synchronized (lock)
            {
                // If this is Jetty then we can use Continuations
                Continuation continuation = new Continuation(request);
                if (continuation.isAvailable())
                {
                    if (!sleepWithContinuation(scriptSession, continuation, wait))
                    {
                        lock.wait(wait);
                    }
                }
                else
                {
                    lock.wait(wait);
                }

                if (conduit != null)
                {
                    scriptSession.removeScriptConduit(conduit);
                }
                serverLoadMonitor.threadWaitEnding(controller);
            }
        }
        catch (InterruptedException ex)
        {
            log.warn("Interupted", ex);

            if (conduit != null)
            {
                scriptSession.removeScriptConduit(conduit);
            }
            serverLoadMonitor.threadWaitEnding(controller);
        }

        return !controller.isShutdown();
    }

    /**
     * Use a {@link ResumeContinuationScriptConduit} to wait
     * @param scriptSession The session that we add the conduit to
     * @param continuation The Jetty continuation object
     * @param preStreamWaitTime The length of time to wait
     * @return True if the continuation wait worked
     */
    protected boolean sleepWithContinuation(RealScriptSession scriptSession, Continuation continuation, long preStreamWaitTime)
    {
        ScriptConduit listener = null;

        try
        {
            // create a listener
            listener = (ScriptConduit) continuation.getObject();
            if (listener == null)
            {
                listener = new ResumeContinuationScriptConduit(continuation);
                continuation.setObject(listener);
            }
            scriptSession.addScriptConduit(listener);

            // JETTY: throws a RuntimeException that must propogate to the container!
            continuation.suspend(preStreamWaitTime);

            scriptSession.removeScriptConduit(listener);
        }
        catch (Exception ex)
        {
            Continuation.rethrowIfContinuation(ex);

            if (listener != null)
            {
                scriptSession.removeScriptConduit(listener);
            }

            log.warn("Exception", ex);
            return false;
        }

        return true;
    }

    /**
     * Make other threads from the same browser stop waiting and continue
     * @param request The HTTP request
     * @param scriptId The session id of the current page
     */
    protected void notifyThreadsFromSameBrowser(HttpServletRequest request, String scriptId)
    {
        // First we check to see if there is already a connection from the
        // current browser to this servlet
        String otherScriptSessionId = (String) request.getSession().getAttribute(ATTRIBUTE_LONGPOLL_SESSION_ID);
        if (otherScriptSessionId != null)
        {
            RealScriptSession previousSession = scriptSessionManager.getScriptSession(otherScriptSessionId);
            Object lock = previousSession.getScriptLock();

            // Unlock previous script session (request will be automatically finished)
            synchronized (lock)
            {
                lock.notifyAll();
            }
        }

        request.getSession().setAttribute(ATTRIBUTE_LONGPOLL_SESSION_ID, scriptId);
    }

    /**
     * Extract a parameter and ensure it is in the request.
     * This is needed to cope with Jetty continuations that are not real
     * continuations.
     * @param request The HTTP request
     * @param parameters The parameter list parsed out of the request
     * @param attrName The name of the request attribute
     * @param paramName The name of the parameter sent
     * @return The found value
     */
    protected String extractParameter(HttpServletRequest request, Map parameters, String attrName, String paramName)
    {
        String id = (String) request.getAttribute(attrName);

        if (id == null)
        {
            id = (String) parameters.remove(paramName);
            request.setAttribute(attrName, id);
        }

        if (id == null)
        {
            throw new IllegalArgumentException(Messages.getString("PollHandler.MissingParameter", paramName));
        }

        return id;
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
     * How we stash away the results of the request parse
     */
    public static final String ATTRIBUTE_PARAMETERS = "org.directwebremoting.dwrp.parameters";

    /**
     * How we stash away the results of the request parse
     */
    public static final String ATTRIBUTE_CALL_ID = "org.directwebremoting.dwrp.callId";

    /**
     * How we stash away the results of the request parse
     */
    public static final String ATTRIBUTE_SESSION_ID = "org.directwebremoting.dwrp.sessionId";

    /**
     * How we stash away the results of the request parse
     */
    public static final String ATTRIBUTE_PAGE = "org.directwebremoting.dwrp.page";

    /**
     * How we stash away the results of the request parse
     */
    public static final String ATTRIBUTE_PARTIAL_RESPONSE = "org.directwebremoting.dwrp.partialResponse";

    /**
     * We remember people that are in a long poll so we can kick them out
     */
    public static final String ATTRIBUTE_LONGPOLL_SESSION_ID = "org.directwebremoting.dwrp.longPollSessionId";

    /**
     * The client can not handle partial responses
     */
    protected static final int PARTIAL_RESPONSE_NO = 0;

    /**
     * The client can handle partial responses
     */
    protected static final int PARTIAL_RESPONSE_YES = 1;

    /**
     * The client can only handle partial responses with a 4k data post
     * (can be whitespace) - we're talking IE here.
     */
    protected static final int PARTIAL_RESPONSE_FLUSH = 2;

    /**
     * The log stream
     */
    private static final Logger log = Logger.getLogger(PollHandler.class);
}
