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

import org.directwebremoting.Container;
import org.directwebremoting.ScriptBuffer;
import org.directwebremoting.WebContext;
import org.directwebremoting.WebContextFactory;
import org.directwebremoting.extend.ConverterManager;
import org.directwebremoting.extend.Handler;
import org.directwebremoting.extend.MarshallException;
import org.directwebremoting.extend.PageNormalizer;
import org.directwebremoting.extend.RealScriptSession;
import org.directwebremoting.extend.EnginePrivate;
import org.directwebremoting.extend.ScriptBufferUtil;
import org.directwebremoting.extend.ScriptConduit;
import org.directwebremoting.extend.ScriptSessionManager;
import org.directwebremoting.extend.ServerLoadMonitor;
import org.directwebremoting.extend.WaitController;
import org.directwebremoting.util.Continuation;
import org.directwebremoting.util.DebuggingPrintWriter;
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
        Container container = webContext.getContainer();

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
                sendBatchExceptionResponse(response, null, ex);
                return;
            }
        }

        String batchId = extractParameter(request, parameters, ATTRIBUTE_CALL_ID, ProtocolConstants.INBOUND_KEY_BATCHID);
        String scriptId = extractParameter(request, parameters, ATTRIBUTE_SESSION_ID, ProtocolConstants.INBOUND_KEY_SCRIPT_SESSIONID);
        String page = extractParameter(request, parameters, ATTRIBUTE_PAGE, ProtocolConstants.INBOUND_KEY_PAGE);
        String prString = extractParameter(request, parameters, ATTRIBUTE_PARTIAL_RESPONSE, ProtocolConstants.INBOUND_KEY_PARTIAL_RESPONSE);
        int partialResponse = Integer.valueOf(prString).intValue();

        // TODO: remove (Hack while the 4k-buffer-flush this is broken)
        /*
        if (partialResponse == PARTIAL_RESPONSE_FLUSH)
        {
            partialResponse = PARTIAL_RESPONSE_NO;
        }
        //*/

        if (!activeReverseAjaxEnabled)
        {
            sendNoPollingResponse(response, batchId);
            return;
        }

        if (!allowGetForSafariButMakeForgeryEasier && isGet)
        {
            sendBatchExceptionResponse(response, batchId, new SecurityException("GET Disallowed"));
            return;
        }

        // Various bits of parseResponse need to be stashed away places
        String normalizedPage = pageNormalizer.normalizePage(page);

        webContext.setCurrentPageInformation(normalizedPage, scriptId);
        RealScriptSession scriptSession = (RealScriptSession) webContext.getScriptSession();
        ServerLoadMonitor monitor = (ServerLoadMonitor) container.getBean(ServerLoadMonitor.class.getName());

        long maxConnectedTime = monitor.getMaxConnectedTime();
        long endTime = System.currentTimeMillis() + maxConnectedTime;

        // If we are going to be doing any waiting then check for other threads
        // from the same browser that are already waiting, and send them on
        // their way
        if (maxConnectedTime > 0)
        {
            notifyThreadsFromSameBrowser(request, scriptId);
        }

        ScriptConduit notifyConduit = new NotifyOnlyScriptConduit(scriptSession.getScriptLock());

        // Don't wait if we would wait for 0s or if there are queued scripts
        boolean shutdown = false;
        if (maxConnectedTime > 0 && !scriptSession.hasWaitingScripts())
        {
            shutdown = streamWait(request, notifyConduit, scriptSession, maxConnectedTime);
        }

        PollScriptConduit conduit = openStream(response, batchId, partialResponse);

        // How much longer do we wait now the stream is open?
        long extraWait = endTime - System.currentTimeMillis();
        if (extraWait > 0 && partialResponse != PARTIAL_RESPONSE_NO && !shutdown)
        {
            streamWait(request, conduit, scriptSession, extraWait);
        }
        else
        {
            scriptSession.writeScripts(conduit);
        }

        closeStream(batchId, partialResponse, conduit);
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
            }
        }
        catch (InterruptedException ex)
        {
            log.warn("Interupted", ex);
        }
        finally
        {
            if (conduit != null)
            {
                scriptSession.removeScriptConduit(conduit);
            }
            serverLoadMonitor.threadWaitEnding(controller);
        }

        return controller.isShutdown();
    }

    /**
     * Open an output stream, set the mime type etc, and create a ScriptConduit
     * for writing to that stream.
     * @param response The HTTP response from which we create a {@link ScriptConduit}
     * @param batchId The batch id that we are priming
     * @param partialResponse Do we do the IE 4k flush hack
     * @return For writing to the HTTP response
     * @throws IOException
     */
    protected PollScriptConduit openStream(HttpServletResponse response, String batchId, int partialResponse) throws IOException
    {
        // Get the output stream and setup the mimetype
        if (plain)
        {
            response.setContentType(MimeConstants.MIME_JS);
        }
        else
        {
            response.setContentType(MimeConstants.MIME_HTML);
        }

        PrintWriter out;
        if (log.isDebugEnabled())
        {
            // This might be considered evil - altering the program flow
            // depending on the log status, however DebuggingPrintWriter is
            // very thin and only about logging
            out = new DebuggingPrintWriter("", response.getWriter());
        }
        else
        {
            out = response.getWriter();
        }

        // The conduit to pass on reverse ajax scripts
        PollScriptConduit conduit = new PollScriptConduit(out, response, partialResponse);

        // Setup a debugging prefix
        if (out instanceof DebuggingPrintWriter)
        {
            DebuggingPrintWriter dpw = (DebuggingPrintWriter) out;
            dpw.setPrefix("out(" + conduit.hashCode() + "): ");
        }

        if (!plain)
        {
            out.println(ProtocolConstants.POLL_SCRIPT_PREFIX);
            out.println(ProtocolConstants.SCRIPT_START_MARKER);
            out.println(EnginePrivate.remoteBeginIFrameResponse(batchId, false));
            out.println(ProtocolConstants.SCRIPT_END_MARKER);
        }

        return conduit;
    }

    /**
     * A poll has finished, get the client to call us back
     * @param batchId The id of the batch that we are responding to
     * @param partialResponse Do we do the IE 4k flush hack
     * @param conduit A conduit if there is an open stream or null if not
     * @throws IOException
     */
    protected void closeStream(String batchId, int partialResponse, PollScriptConduit conduit) throws IOException
    {
        try
        {
            int wait = serverLoadMonitor.getTimeToNextPoll();
            // If the client can handle partial responses, then there is
            // no need to stagger return callbacks (which we do to
            // prevent massed reconnects in chat type apps.)
            if (partialResponse != PARTIAL_RESPONSE_NO)
            {
                wait = 0;
            }

            EnginePrivate.remoteHandleCallback(conduit, batchId, "0", new Integer(wait));
        }
        catch (Exception ex)
        {
            EnginePrivate.remoteHandleException(conduit, batchId, "0", ex);
            log.warn("--Erroring: batchId[" + batchId + "] message[" + ex.toString() + ']', ex);
        }

        if (!plain)
        {
            PrintWriter out = conduit.getPrintWriter();

            out.println(EnginePrivate.remoteEndIFrameResponse(batchId, false));
            out.println(ProtocolConstants.SCRIPT_START_MARKER);
            out.println(ProtocolConstants.POLL_SCRIPT_POSTFIX);
            out.println(ProtocolConstants.SCRIPT_END_MARKER);
        }
    }

    /**
     * We might need to complain that reverse ajax is not enabled.
     * @param batchId The identifier of the batch that we are handling a response for
     * @param response The http response to write to
     * @throws IOException if writing fails.
     */
    protected void sendNoPollingResponse(HttpServletResponse response, String batchId) throws IOException
    {
        log.error("Polling and Comet are disabled. To enable them set the init-param activeReverseAjaxEnabled to true. See http://getahead.ltd.uk/dwr/server/servlet for more.");
        String script = EnginePrivate.getRemotePollCometDisabledScript(batchId);
        sendScript(response, script);
    }

    /**
     * If we need to send a batch exception to the server because the parse
     * failed, then this is how we do it.
     * @param response The http response to write to
     * @param batchId The identifier of the batch that we are handling a response for
     * @param ex The exception to write
     * @throws IOException if writing fails.
     */
    protected void sendBatchExceptionResponse(HttpServletResponse response, String batchId, Exception ex) throws IOException
    {
        String script = EnginePrivate.getRemoteHandleBatchExceptionScript(batchId, ex);
        sendScript(response, script);
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
    protected void sendScript(HttpServletResponse response, String script) throws IOException
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
        sendScript(out, script);
    }

    /**
     * Write a script out in a synchronized manner to avoid thread clashes
     * @param out The servlet output stream
     * @param script The script to write
     */
    protected void sendScript(PrintWriter out, String script)
    {
        synchronized (out)
        {
            out.println(ProtocolConstants.SCRIPT_START_MARKER);
            out.println(script);
            out.println(ProtocolConstants.SCRIPT_END_MARKER);

            // I'm not totally sure if this is the right thing to do.
            // A PrintWriter that encounters an error never recovers so maybe
            // we could be more robust by using a lower level object and
            // working out what to do if something goes wrong. Annoyingly
            // PrintWriter also throws the original exception away.
            if (out.checkError())
            {
                log.warn("Error writing to stream");
                // throw new IOException("Error writing to stream");
            }
        }
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
        }
        catch (Exception ex)
        {
            Continuation.rethrowIfContinuation(ex);
            log.warn("Exception", ex);
            return false;
        }
        finally
        {
            if (listener != null)
            {
                scriptSession.removeScriptConduit(listener);
            }
        }

        return true;
    }

    /**
     * A {@link WaitController} that works with {@link Object#wait(long)}
     * @author Joe Walker [joe at getahead dot ltd dot uk]
     */
    public static class NotifyWaitController implements WaitController
    {
        /**
         * @param lock
         */
        public NotifyWaitController(Object lock)
        {
            this.lock = lock;
        }

        /* (non-Javadoc)
         * @see org.directwebremoting.extend.WaitController#shutdown()
         */
        public void shutdown()
        {
            try
            {
                synchronized (lock)
                {
                    lock.notifyAll();
                }
            }
            catch (Exception ex)
            {
                log.warn("Failed to notify all ScriptSession users", ex);
            }

            shutdown = true;
        }

        /* (non-Javadoc)
         * @see org.directwebremoting.extend.WaitController#isShutdown()
         */
        public boolean isShutdown()
        {
            return shutdown;
        }

        /**
         * The object that is being {@link Object#wait(long)} on so we can
         * move it on with {@link Object#notifyAll()}.
         */
        private Object lock;

        /**
         * Has {@link #shutdown()} been called on this object?
         */
        private boolean shutdown = false;
    }

    /**
     * A ScriptConduit that works with the parent Marshaller.
     * In some ways this is nasty because it has access to essentially private parts
     * of PollHandler, however there is nowhere sensible to store them
     * within that class, so this is a hacky simplification.
     * @author Joe Walker [joe at getahead dot ltd dot uk]
     */
    protected class PollScriptConduit extends ScriptConduit
    {
        /**
         * Simple ctor
         * @param out The stream to write to
         * @param response Used to flush output
         * @param partialResponse Do we do the IE 4k flush hack
         */
        protected PollScriptConduit(PrintWriter out, HttpServletResponse response, int partialResponse)
        {
            super(RANK_FAST);
            this.out = out;
            this.response = response;
            this.partialResponse = partialResponse;
        }

        /* (non-Javadoc)
         * @see org.directwebremoting.ScriptConduit#addScript(org.directwebremoting.ScriptBuffer)
         */
        public boolean addScript(ScriptBuffer script) throws IOException, MarshallException
        {
            sendScript(out, ScriptBufferUtil.createOutput(script, converterManager));
            return true;
        }

        /* (non-Javadoc)
         * @see org.directwebremoting.ScriptConduit#flush()
         */
        public void flush() throws IOException
        {
            synchronized (out)
            {
                if (partialResponse == PARTIAL_RESPONSE_FLUSH)
                {
                    out.print(fourKFlushData);
                }

                out.flush();
                response.flushBuffer();

                if (out.checkError())
                {
                    throw new IOException("Error flushing buffered stream");
                }
            }
        }

        /**
         * PollHandler.closeStream() needs to be able to write closing HTML
         * @return The underlying output stream
         */
        public PrintWriter getPrintWriter()
        {
            return out;
        }

        /**
         * Do we need to do the IE 4k flush thing?
         */
        private int partialResponse;

        /**
         * Used to flush data to the output stream
         */
        private final HttpServletResponse response;

        /**
         * The PrintWriter to send output to, and that we should synchronize against
         */
        private final PrintWriter out;
    }

    /**
     * Implementation of ScriptConduit that simply calls <code>notifyAll()</code>
     * if a script is added.
     * No actual script adding is done here.
     * Useful in conjunction with a streamWait()
     */
    protected static final class NotifyOnlyScriptConduit extends ScriptConduit
    {
        /**
         * @param lock Object to wait and notifyAll with
         */
        protected NotifyOnlyScriptConduit(Object lock)
        {
            super(RANK_PROCEDURAL);
            this.lock = lock;
        }

        /* (non-Javadoc)
         * @see org.directwebremoting.ScriptConduit#addScript(org.directwebremoting.ScriptBuffer)
         */
        public boolean addScript(ScriptBuffer script)
        {
            try
            {
                synchronized (lock)
                {
                    lock.notifyAll();
                }
            }
            catch (Exception ex)
            {
                log.warn("Failed to notify all ScriptSession users", ex);
            }

            // We have not done anything with the script, so
            return false;
        }

        /* (non-Javadoc)
         * @see org.directwebremoting.ScriptConduit#flush()
         */
        public void flush()
        {
        }

        private final Object lock;
    }

    /**
     * Implementaion of ScriptConduit that just resumes a continuation.
     */
    protected static final class ResumeContinuationScriptConduit extends ScriptConduit
    {
        /**
         * @param continuation
         */
        protected ResumeContinuationScriptConduit(Continuation continuation)
        {
            super(RANK_PROCEDURAL);
            this.continuation = continuation;
        }

        /* (non-Javadoc)
         * @see org.directwebremoting.ScriptConduit#addScript(org.directwebremoting.ScriptBuffer)
         */
        public boolean addScript(ScriptBuffer script)
        {
            try
            {
                continuation.resume();
            }
            catch (Exception ex)
            {
                log.warn("Exception in continuation.resume()", ex);
            }

            // never actually handle the script!
            return false;
        }

        /* (non-Javadoc)
         * @see org.directwebremoting.ScriptConduit#flush()
         */
        public void flush()
        {
        }

        /**
         * The Jetty continuation
         */
        private final Continuation continuation;
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
     * Are we doing full reverse ajax
     */
    protected boolean activeReverseAjaxEnabled = false;

    /**
     * By default we disable GET, but this hinders old Safaris
     */
    protected boolean allowGetForSafariButMakeForgeryEasier = false;

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
     * The slab of data we send to IE to get it to stream
     */
    protected static final String fourKFlushData;
    static
    {
        StringBuffer buffer = new StringBuffer(409600);
        for (int i = 0; i < 4096; i++)
        {
            buffer.append(" ");
        }
        fourKFlushData = buffer.toString();
    }

    /**
     * The log stream
     */
    protected static final Logger log = Logger.getLogger(PollHandler.class);
}
