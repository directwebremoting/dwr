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

import org.directwebremoting.ConverterManager;
import org.directwebremoting.MarshallException;
import org.directwebremoting.OutboundContext;
import org.directwebremoting.OutboundVariable;
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
        Map parameters = (Map) request.getAttribute(ATTRIBUTE_PARAMETERS);
        if (parameters == null)
        {
            if (request.getMethod().equals("GET")) //$NON-NLS-1$
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

        try
        {
            serverLoadMonitor.threadWaitStarting();

            try
            {
                // The first wait - before we do any output
                PollUtil.preStreamWait(partialResponse);
            }
            catch (Exception ex)
            {
                // Allow Jetty RequestRetry exception to propogate to container
                Continuation.rethrowIfContinuation(ex);
                log.warn("Error calling pollWait()", ex); //$NON-NLS-1$
            }

            // Get the output stream and setup the mimetype
            response.setContentType(MimeConstants.MIME_PLAIN);
            PrintWriter out;
            if (log.isDebugEnabled())
            {
                // This might be considered evil - altering the program flow
                // depending on the log status, however DebuggingPrintWriter is
                // very thin and only about logging
                out = new DebuggingPrintWriter("", response.getWriter()); //$NON-NLS-1$
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
                dpw.setPrefix("out(" + conduit.hashCode() + "): "); //$NON-NLS-1$ //$NON-NLS-2$
            }

            try
            {
                // From the call to addScriptConduit() there could be 2 threads writing
                // to 'out' so we synchronize on 'out' to make sure there are no
                // clashes
                scriptSession.addScriptConduit(conduit);

                // The second wait - after we've started to do the output
                PollUtil.postStreamWait(partialResponse);

                OutboundContext converted = new OutboundContext();

                String script;
                try
                {
                    int wait = serverLoadMonitor.getTimeToNextPoll();
                    Integer data = new Integer(wait);

                    OutboundVariable ov = converterManager.convertOutbound(data, converted);
                    script = ov.getInitCode() + "DWREngine._handleResponse('" + callId + "', " + ov.getAssignCode() + ");"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                }
                catch (Exception ex)
                {
                    script = "DWREngine._handleServerWarning('" + callId + "', 'Error handling reverse ajax');"; //$NON-NLS-1$ //$NON-NLS-2$
                    log.warn("--Erroring: id[" + callId + "] message[" + ex.toString() + ']', ex); //$NON-NLS-1$ //$NON-NLS-2$
                }

                sendScript(out, script, plain);
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
            throw new MarshallException(Messages.getString("PollHandler.MissingParameter", paramName)); //$NON-NLS-1$
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
                out.println("<html><body><script type='text/javascript'>"); //$NON-NLS-1$
            }

            out.println(ConversionConstants.SCRIPT_START_MARKER);
            out.println(script);
            out.println(ConversionConstants.SCRIPT_END_MARKER);

            if (!plain)
            {
                out.println("</script></body></html>"); //$NON-NLS-1$
            }

            if (out.checkError())
            {
                throw new IOException("Error flushing buffered stream"); //$NON-NLS-1$
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
    protected class PollScriptConduit extends ScriptConduit
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
         * @see org.directwebremoting.ScriptConduit#addScript(java.lang.String)
         */
        public boolean addScript(String script) throws IOException
        {
            sendScript(out, script, plain);
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
                throw new IOException("Error flushing buffered stream"); //$NON-NLS-1$
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
    protected static final String ATTRIBUTE_PARAMETERS = "org.directwebremoting.dwrp.parameters"; //$NON-NLS-1$

    /**
     * How we stash away the results of the request parse
     */
    protected static final String ATTRIBUTE_CALL_ID = "org.directwebremoting.dwrp.callId"; //$NON-NLS-1$

    /**
     * How we stash away the results of the request parse
     */
    protected static final String ATTRIBUTE_SESSION_ID = "org.directwebremoting.dwrp.sessionId"; //$NON-NLS-1$

    /**
     * How we stash away the results of the request parse
     */
    protected static final String ATTRIBUTE_PAGE = "org.directwebremoting.dwrp.page"; //$NON-NLS-1$

    /**
     * How we stash away the results of the request parse
     */
    protected static final String ATTRIBUTE_PARTIAL_RESPONSE = "org.directwebremoting.dwrp.partialResponse"; //$NON-NLS-1$

    /**
     * The log stream
     */
    protected static final Logger log = Logger.getLogger(PollHandler.class);
}
