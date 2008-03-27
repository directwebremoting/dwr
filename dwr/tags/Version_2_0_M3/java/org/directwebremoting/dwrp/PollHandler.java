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
import org.directwebremoting.ConverterManager;
import org.directwebremoting.MarshallException;
import org.directwebremoting.ScriptBuffer;
import org.directwebremoting.ScriptConduit;
import org.directwebremoting.ScriptSession;
import org.directwebremoting.ServerLoadMonitor;
import org.directwebremoting.WebContext;
import org.directwebremoting.WebContextFactory;
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
public class PollHandler
{
    /**
     * Handle a poll request
     * @param request The http request
     * @param response The http response
     * @param plain Are we using plain javascript or html wrapped javascript
     * @throws IOException Thrown if we can't read or write
     */
    public void doPoll(HttpServletRequest request, HttpServletResponse response, boolean plain) throws IOException
    {
        // We must parse the parameters before we setup the conduit because it's
        // only after doing this that we know the scriptSessionId

        WebContext webContext = WebContextFactory.get();
        Container container = webContext.getContainer();

        Map parameters = (Map) request.getAttribute(ATTRIBUTE_PARAMETERS);
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

        String callId = extractParameter(request, parameters, ATTRIBUTE_CALL_ID, ConversionConstants.INBOUND_KEY_ID);
        String scriptId = extractParameter(request, parameters, ATTRIBUTE_SESSION_ID, ConversionConstants.INBOUND_KEY_SCRIPT_SESSIONID);
        String page = extractParameter(request, parameters, ATTRIBUTE_PAGE, ConversionConstants.INBOUND_KEY_PAGE);
        String prString = extractParameter(request, parameters, ATTRIBUTE_PARTIAL_RESPONSE, ConversionConstants.INBOUND_KEY_PARTIAL_RESPONSE);
        boolean partialResponse = Boolean.valueOf(prString).booleanValue();

        // Various bits of parseResponse need to be stashed away places
        webContext.setCurrentPageInformation(page, scriptId);
        ScriptSession scriptSession = webContext.getScriptSession();
        ServerLoadMonitor monitor = (ServerLoadMonitor) container.getBean(ServerLoadMonitor.class.getName());

        try
        {
            serverLoadMonitor.threadWaitStarting();

            try
            {
                // The first wait - before we do any output
                long preStreamWaitTime = monitor.getPreStreamWaitTime();

                // If the browser can't handle partial responses then we'll need to do
                // all our waiting in the pre-stream phase where we plan to be interupted
                if (!partialResponse)
                {
                    long postStreamWaitTime = monitor.getPostStreamWaitTime();
                    preStreamWaitTime += postStreamWaitTime;
                }

                Object lock = scriptSession.getScriptLock();
                synchronized (lock)
                {
                    // Don't wait if there are queued scripts
                    if (scriptSession.hasWaitingScripts())
                    {
                        return;
                    }

                    // If this is Jetty then we can use Continuations
                    boolean useSleep = true;
                    Continuation continuation = new Continuation(request);
                    if (continuation.isAvailable())
                    {
                        useSleep = false;
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
                            useSleep = true;
                        }
                        finally
                        {
                            if (listener != null)
                            {
                                scriptSession.removeScriptConduit(listener);
                            }
                        }
                    }

                    if (useSleep)
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
                }
            }
            catch (Exception ex)
            {
                // Allow Jetty RequestRetry exception to propogate to container
                Continuation.rethrowIfContinuation(ex);
                log.warn("Error calling pollWait()", ex);
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
            ScriptConduit conduit = new PollScriptConduit(out, response, plain);
    
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
                long postStreamWaitTime = monitor.getPostStreamWaitTime();
                if (postStreamWaitTime > 0)
                {
                    // Flush any scripts already written.
                    // This is a bit of a broad brush: We only really need to flush the
                    // conduit that is part of this response, but is there any harm
                    // in flushing too many?
                    //ScriptSession scriptSession = context.getScriptSession();
                    //scriptSession.flushConduits();

                    // This is the same as Thread.sleep() except that this allows us to
                    // keep track of how many people are waiting to control server load
                    try
                    {
                        // If the browser can't handle partialResponses then we need to
                        // reply quickly. 100ms should be enough for any work being done
                        // on other threads to complete. If not it can wait.
                        if (!partialResponse)
                        {
                            postStreamWaitTime = 100;
                        }

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

                ScriptBuffer script = new ScriptBuffer(converterManager);
                try
                {
                    int wait = serverLoadMonitor.getTimeToNextPoll();
                    Integer data = new Integer(wait);

                    script.appendScript("DWREngine._handleResponse(")
                          .appendData(callId)
                          .appendScript(',')
                          .appendData(data)
                          .appendScript(");");
                }
                catch (Exception ex)
                {
                    script.appendScript("DWREngine._handleServerWarning(")
                          .appendData(callId)
                          .appendScript(", 'Error handling reverse ajax');");

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
    private String extractParameter(HttpServletRequest request, Map parameters, String attrName, String paramName)
    {
        String id = (String) request.getAttribute(attrName);

        if (id == null)
        {
            id = (String) parameters.remove(paramName);
            request.setAttribute(attrName, id);
        }

        if (id == null)
        {
            throw new MarshallException(Messages.getString("PollHandler.MissingParameter", paramName));
        }

        return id;
    }

    /**
     * Write a script out in a synchronized manner to avoid thread clashes
     * @param out The servlet output stream
     * @param script The script to write
     * @param plain Are we using plain javascript or html wrapped javascript
     * @throws IOException If a write error occurs
     */
    protected void sendScript(PrintWriter out, String script, boolean plain) throws IOException
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
         * @param plain Are we using plain javascript or html wrapped javascript
         */
        protected PollScriptConduit(PrintWriter out, HttpServletResponse response, boolean plain)
        {
            super(RANK_FAST);
            this.out = out;
            this.plain = plain;
            this.response = response;
        }

        /* (non-Javadoc)
         * @see org.directwebremoting.ScriptConduit#addScript(org.directwebremoting.ScriptBuffer)
         */
        public boolean addScript(ScriptBuffer script) throws IOException
        {
            sendScript(out, script.createOutput(), plain);
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
         * Are we using plain javascript or html wrapped javascript
         */
        private boolean plain;

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
