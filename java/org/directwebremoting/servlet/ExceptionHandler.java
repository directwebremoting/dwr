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
package org.directwebremoting.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.directwebremoting.extend.Handler;
import org.directwebremoting.util.Continuation;
import org.directwebremoting.util.Logger;
import org.directwebremoting.util.MimeConstants;

/**
 * Handles an exception occuring during the request disptaching.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class ExceptionHandler implements Handler
{
    /* (non-Javadoc)
     * @see org.directwebremoting.Handler#handle(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    public void handle(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        // Allow Jetty RequestRetry exception to propogate to container
        Continuation.rethrowIfContinuation(cause);

        log.warn("Error: " + cause);
        if (cause instanceof SecurityException && log.isDebugEnabled())
        {
            log.debug("- User Agent: " + request.getHeader(HttpConstants.HEADER_USER_AGENT));
            log.debug("- Remote IP:  " + request.getRemoteAddr());
            log.debug("- Request URL:" + request.getRequestURL());
            log.debug("- Query:      " + request.getQueryString());
            log.debug("- Method:     " + request.getMethod());
        }

        // We are going to act on this in engine.js so we are hoping that
        // that SC_NOT_IMPLEMENTED (501) is not something that the servers
        // use that much. I would have used something unassigned like 506+
        // But that could cause future problems and might not get through
        // proxies and the like
        response.setStatus(HttpServletResponse.SC_NOT_IMPLEMENTED);
        response.setContentType(MimeConstants.MIME_HTML);
        PrintWriter out = response.getWriter();
        out.println(cause.getMessage());

        log.warn("Sent 501", cause);
    }

    /**
     * @param cause The cause of the failure
     */
    public void setException(Exception cause)
    {
        this.cause = cause;
    }

    /**
     * The cause of the failure
     */
    private Exception cause;

    /**
     * The log stream
     */
    private static final Logger log = Logger.getLogger(ExceptionHandler.class);
}
