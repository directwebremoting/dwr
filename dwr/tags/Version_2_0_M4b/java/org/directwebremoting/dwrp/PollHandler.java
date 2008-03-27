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
import org.directwebremoting.extend.RemoteDwrEngine;
import org.directwebremoting.extend.ScriptBufferUtil;
import org.directwebremoting.extend.ScriptConduit;
import org.directwebremoting.extend.ServerLoadMonitor;
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

        Map parameters = (Map) request.getAttribute(ATTRIBUTE_PARAMETERS);
        try
        {
            if (parameters == null)
            {
                if (request.getMethod().equals("GET"))
                {
                    parameters = ParseUtil.parseGet(request);
                }
                else
                {
                    parameters = ParseUtil.parsePost(request);
                }
                request.setAttribute(ATTRIBUTE_PARAMETERS, parameters);
            }
        }
        catch (Exception ex)
        {
            RemoteDwrEngine.remoteHandleExceptionWithoutCallId(response, ex);
            return;
        }

        String batchId = extractParameter(request, parameters, ATTRIBUTE_CALL_ID, ConversionConstants.INBOUND_KEY_BATCHID);
        String callId = extractParameter(request, parameters, ATTRIBUTE_CALL_ID, ConversionConstants.INBOUND_KEY_ID);
        String scriptId = extractParameter(request, parameters, ATTRIBUTE_SESSION_ID, ConversionConstants.INBOUND_KEY_SCRIPT_SESSIONID);
        String page = extractParameter(request, parameters, ATTRIBUTE_PAGE, ConversionConstants.INBOUND_KEY_PAGE);
        String prString = extractParameter(request, parameters, ATTRIBUTE_PARTIAL_RESPONSE, ConversionConstants.INBOUND_KEY_PARTIAL_RESPONSE);
        boolean partialResponse = Boolean.valueOf(prString).booleanValue();

        // Various bits of parseResponse need to be stashed away places
        String normalizedPage = pageNormalizer.normalizePage(page);

        webContext.setCurrentPageInformation(normalizedPage, scriptId);
        RealScriptSession scriptSession = (RealScriptSession) webContext.getScriptSession();
        ServerLoadMonitor monitor = (ServerLoadMonitor) container.getBean(ServerLoadMonitor.class.getName());

        long postStreamWaitTime = monitor.getPostStreamWaitTime();
        long preStreamWaitTime = monitor.getPreStreamWaitTime();

        // If the browser can't handle partial responses then we'll need to do
        // all our waiting in the pre-stream phase where we plan to be
        // interupted, and then reply quickly.
        // 100ms should be enough work being done on other threads to complete.
        // If not it can wait.
        if (!partialResponse)
        {
            postStreamWaitTime = 100;
            preStreamWaitTime += postStreamWaitTime;
        }

        try
        {
            serverLoadMonitor.threadWaitStarting();

            // Don't wait if we would wait for 0s or if there are queued scripts
            if (preStreamWaitTime > 0 && !scriptSession.hasWaitingScripts())
            {
                // The first wait - before we do any output
                Object lock = scriptSession.getScriptLock();
                synchronized (lock)
                {
                    // If this is Jetty then we can use Continuations
                    Continuation continuation = new Continuation(request);
                    if (continuation.isAvailable())
                    {
                        if (!sleepWithContinuation(scriptSession, continuation, preStreamWaitTime))
                        {
                            sleepWithNotify(scriptSession, lock, preStreamWaitTime);
                        }
                    }
                    else
                    {
                        sleepWithNotify(scriptSession, lock, preStreamWaitTime);
                    }
                }
            }

            // Get the output stream and setup the mimetype
            response.setContentType(MimeConstants.MIME_PLAIN);
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
            ScriptConduit conduit = new PollScriptConduit(out, response);
    
            // Setup a debugging prefix
            if (out instanceof DebuggingPrintWriter)
            {
                DebuggingPrintWriter dpw = (DebuggingPrintWriter) out;
                dpw.setPrefix("out(" + conduit.hashCode() + "): ");
            }

            try
            {
                // From the call to addScriptConduit() there could be 2 threads writing
                // to 'out' so we synchronize on 'out' to make sure there are no
                // clashes
                scriptSession.addScriptConduit(conduit);

                // The second wait - after we've started to do the output
                if (postStreamWaitTime > 0)
                {
                    try
                    {
                        Thread thread = Thread.currentThread();
                        String oldName = thread.getName();
                        thread.setName("DWR:Poll:PostStreamWait:" + postStreamWaitTime);

                        Thread.sleep(postStreamWaitTime);

                        thread.setName(oldName);
                    }
                    catch (InterruptedException ex)
                    {
                        log.warn("Interupted", ex);
                    }
                }

                ScriptBuffer script = new ScriptBuffer();
                try
                {
                    int wait = serverLoadMonitor.getTimeToNextPoll();
                    Integer data = new Integer(wait);

                    RemoteDwrEngine.remoteHandleCallback(conduit, batchId, callId, data);
                }
                catch (Exception ex)
                {
                    RemoteDwrEngine.remoteHandleException(conduit, batchId, callId, ex);
                    log.warn("--Erroring: id[" + callId + "] message[" + ex.toString() + ']', ex);
                }

                scriptSession.addScript(script);
            }
            finally
            {
                scriptSession.removeScriptConduit(conduit);
            }
        }
        finally
        {
            serverLoadMonitor.threadWaitEnding();
        }
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
     * Write a script out in a synchronized manner to avoid thread clashes
     * @param out The servlet output stream
     * @param script The script to write
     * @throws IOException If a write error occurs
     */
    protected void sendScript(PrintWriter out, String script) throws IOException
    {
        synchronized (out)
        {
            if (!plain)
            {
                out.println("<html><body><script type='text/javascript'>");
            }

            out.println(ConversionConstants.SCRIPT_START_MARKER);
            out.println(script);
            out.println(ConversionConstants.SCRIPT_END_MARKER);

            if (!plain)
            {
                out.println("</script></body></html>");
            }

            if (out.checkError())
            {
                throw new IOException("Error flushing buffered stream");
            }
        }
    }

    /**
     * Use a {@link NotifyOnlyScriptConduit} to wait on a lock
     * @param scriptSession The session that we add the conduit to
     * @param lock The object that we wait on
     * @param preStreamWaitTime The length of time to wait
     * @throws IOException If the write to the browser fails
     */
    protected void sleepWithNotify(RealScriptSession scriptSession, Object lock, long preStreamWaitTime) throws IOException
    {
        ScriptConduit listener = new NotifyOnlyScriptConduit(lock);

        // The comet part of a poll request
        try
        {
            scriptSession.addScriptConduit(listener);

            try
            {
                Thread thread = Thread.currentThread();
                String oldName = thread.getName();
                thread.setName("DWR:Poll:PreStreamWait:" + preStreamWaitTime);

                lock.wait(preStreamWaitTime);

                thread.setName(oldName);
            }
            catch (InterruptedException ex)
            {
                log.warn("Interupted", ex);
            }
        }
        finally
        {
            scriptSession.removeScriptConduit(listener);
        }
    }

    /**
     * Use a {@link ResumeContinuationScriptConduit} to wait
     * @param scriptSession The session that we add the conduit to
     * @param continuation The Jetty continuation object
     * @param preStreamWaitTime The length of time to wait
     * @return True if the continuation wait worked
     */
    private boolean sleepWithContinuation(RealScriptSession scriptSession, Continuation continuation, long preStreamWaitTime)
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
     * A ScriptConduit that works with the parent Marshaller.
     * In some ways this is nasty because it has access to essentially private parts
     * of PollHandler, however there is nowhere sensible to store them
     * within that class, so this is a hacky simplification.
     * @author Joe Walker [joe at getahead dot ltd dot uk]
     */
    private class PollScriptConduit extends ScriptConduit
    {
        /**
         * Simple ctor
         * @param out The stream to write to
         * @param response Used to flush output
         */
        protected PollScriptConduit(PrintWriter out, HttpServletResponse response)
        {
            super(RANK_FAST);
            this.out = out;
            this.response = response;
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
            out.flush();
            response.flushBuffer();

            if (out.checkError())
            {
                throw new IOException("Error flushing buffered stream");
            }
        }

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
     * Useful in conjunction with a preStreamWait to
     */
    private static final class NotifyOnlyScriptConduit extends ScriptConduit
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
    private static final class ResumeContinuationScriptConduit extends ScriptConduit
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
     * Accessfor for the PageNormalizer.
     * @param pageNormalizer The new PageNormalizer
     */
    public void setPageNormalizer(PageNormalizer pageNormalizer)
    {
        this.pageNormalizer = pageNormalizer;
    }

    /**
     * Are we using plain javascript or html wrapped javascript
     */
    private boolean plain;

    /**
     * How we turn pages into the canonical form.
     */
    private PageNormalizer pageNormalizer;

    /**
     * We need to tell the system that we are waiting so it can load adjust
     */
    protected ServerLoadMonitor serverLoadMonitor = null;

    /**
     * How we convert parameters
     */
    protected ConverterManager converterManager = null;

    /**
     * How we stash away the results of the request parse
     */
    protected static final String ATTRIBUTE_PARAMETERS = "org.directwebremoting.dwrp.parameters";

    /**
     * How we stash away the results of the request parse
     */
    protected static final String ATTRIBUTE_CALL_ID = "org.directwebremoting.dwrp.callId";

    /**
     * How we stash away the results of the request parse
     */
    protected static final String ATTRIBUTE_SESSION_ID = "org.directwebremoting.dwrp.sessionId";

    /**
     * How we stash away the results of the request parse
     */
    protected static final String ATTRIBUTE_PAGE = "org.directwebremoting.dwrp.page";

    /**
     * How we stash away the results of the request parse
     */
    protected static final String ATTRIBUTE_PARTIAL_RESPONSE = "org.directwebremoting.dwrp.partialResponse";

    /**
     * The log stream
     */
    protected static final Logger log = Logger.getLogger(PollHandler.class);
}
