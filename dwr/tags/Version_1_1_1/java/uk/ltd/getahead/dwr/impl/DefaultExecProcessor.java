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
package uk.ltd.getahead.dwr.impl;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import uk.ltd.getahead.dwr.AccessControl;
import uk.ltd.getahead.dwr.Call;
import uk.ltd.getahead.dwr.Calls;
import uk.ltd.getahead.dwr.ConverterManager;
import uk.ltd.getahead.dwr.CreatorManager;
import uk.ltd.getahead.dwr.OutboundVariable;
import uk.ltd.getahead.dwr.Processor;
import uk.ltd.getahead.dwr.util.Logger;

/**
 * Execute a remote Javascript request.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class DefaultExecProcessor implements Processor
{
    /* (non-Javadoc)
     * @see uk.ltd.getahead.dwr.Processor#handle(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    public void handle(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        try
        {
            ExecuteQuery eq = new ExecuteQuery(creatorManager, converterManager, accessControl);
            Calls calls = eq.execute(req);

            for (int i = 0; i < calls.getCallCount(); i++)
            {
                Call call = calls.getCall(i);
                if (call.getThrowable() != null)
                {
                    log.warn("Erroring: id[" + call.getId() + "] message[" + call.getThrowable().toString() + ']'); //$NON-NLS-1$ //$NON-NLS-2$
                }
                else
                {
                    log.debug("Returning: id[" + call.getId() + "] assign[" + call.getReply().getAssignCode() + "] xhr[" + calls.isXhrMode() + ']'); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                }
            }

            // We build the answer up in a StringBuffer because that makes is easier
            // to debug, and because that's only what the compiler does anyway.
            StringBuffer buffer = new StringBuffer();

            // if we are in html (iframe mode) we need to direct script to the parent
            String prefix = calls.isXhrMode() ? "" : "window.parent."; //$NON-NLS-1$ //$NON-NLS-2$

            // iframe mode starts as HTML, so get into script mode
            if (!calls.isXhrMode())
            {
                buffer.append("<script type='text/javascript'>\n"); //$NON-NLS-1$
            }

            // Now pass on the executed method responses
            for (int i = 0; i < calls.getCallCount(); i++)
            {
                Call call = calls.getCall(i);
                if (call.getThrowable() != null)
                {
                    OutboundVariable ov = call.getThrowable();

                    buffer.append(ov.getInitCode());
                    buffer.append('\n');
                    buffer.append(prefix);
                    buffer.append("DWREngine._handleServerError('"); //$NON-NLS-1$
                    buffer.append(call.getId());
                    buffer.append("', "); //$NON-NLS-1$
                    buffer.append(ov.getAssignCode());
                    buffer.append(");\n"); //$NON-NLS-1$
                }
                else
                {
                    OutboundVariable ov = call.getReply();

                    buffer.append(ov.getInitCode());
                    buffer.append('\n');
                    buffer.append(prefix);
                    buffer.append("DWREngine._handleResponse('"); //$NON-NLS-1$
                    buffer.append(call.getId());
                    buffer.append("', "); //$NON-NLS-1$
                    buffer.append(ov.getAssignCode());
                    buffer.append(");\n"); //$NON-NLS-1$
                }
            }

            // iframe mode needs to get out of script mode
            if (!calls.isXhrMode())
            {
                buffer.append("</script>\n"); //$NON-NLS-1$
            }

            String reply = buffer.toString();
            log.debug(reply);

            // LocalUtil.addNoCacheHeaders(resp);
            resp.setContentType(calls.isXhrMode() ? HtmlConstants.MIME_PLAIN : HtmlConstants.MIME_HTML);
            PrintWriter out = resp.getWriter();
            out.print(reply);
            out.flush();
        }
        catch (Exception ex)
        {
            // This only catches exceptions parsing the request. All execution
            // exceptions are returned inside the Call POJOs.
            if (log.isDebugEnabled())
            {
                log.warn("Error: ", ex); //$NON-NLS-1$
                log.debug("- User Agent: " + req.getHeader(HtmlConstants.HEADER_USER_AGENT)); //$NON-NLS-1$
                log.debug("- Remote IP:  " + req.getRemoteAddr()); //$NON-NLS-1$
                log.debug("- Request URL:" + req.getRequestURL()); //$NON-NLS-1$
                log.debug("- Query:      " + req.getQueryString()); //$NON-NLS-1$
                log.debug("- Method:     " + req.getMethod()); //$NON-NLS-1$

                /* This is borken whenever we use POST, so it's probably not worth it
                log.debug("- Body: {"); //$NON-NLS-1$
                int lines = 0;
                BufferedReader in = req.getReader();
                while (in != null && lines < 100)
                {
                    String line = in.readLine();
                    if (line == null)
                    {
                        break;
                    }

                    log.debug("-   " + line); //$NON-NLS-1$
                    lines++;
                }
                log.debug("- }" + req.getMethod()); //$NON-NLS-1$
                */
            }

            resp.setContentType(HtmlConstants.MIME_HTML);
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            PrintWriter out = resp.getWriter();
            out.println("//<script type='text/javascript'>"); //$NON-NLS-1$
            out.println("alert('Error. This may be due to an unsupported browser.\\nSee the mailing lists at http://www.getahead.ltd.uk/dwr/ for more information.');"); //$NON-NLS-1$
            out.println("//</script>"); //$NON-NLS-1$
            out.flush();
            return;
        }
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
     * Accessor for the DefaultCreatorManager that we configure
     * @param creatorManager The new DefaultConverterManager
     */
    public void setCreatorManager(CreatorManager creatorManager)
    {
        this.creatorManager = creatorManager;
    }

    /**
     * Accessor for the security manager
     * @param accessControl The accessControl to set.
     */
    public void setAccessControl(AccessControl accessControl)
    {
        this.accessControl = accessControl;
    }

    /**
     * How we convert parameters
     */
    protected ConverterManager converterManager = null;

    /**
     * How we create new beans
     */
    protected CreatorManager creatorManager = null;

    /**
     * The security manager
     */
    protected AccessControl accessControl = null;

    /**
     * The log stream
     */
    private static final Logger log = Logger.getLogger(DefaultExecProcessor.class);
}
