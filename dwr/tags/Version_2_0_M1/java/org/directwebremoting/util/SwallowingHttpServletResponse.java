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
package org.directwebremoting.util;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

/**
 * Used by ExecutionContext to forward results back via javascript
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public final class SwallowingHttpServletResponse extends HttpServletResponseWrapper implements HttpServletResponse
{
    /**
     * @param response The real HttpServletResponse
     * @param sout The place we copy responses to
     */
    public SwallowingHttpServletResponse(HttpServletResponse response, Writer sout)
    {
        super(response);

        pout = new PrintWriter(sout);
        oout = new WriterOutputStream(sout);

        // Ignored, but we might as well start with a realistic value in case
        // anyone wants to work with the buffer size.
        bufferSize = response.getBufferSize();
    }

    /* (non-Javadoc)
     * @see javax.servlet.ServletResponseWrapper#getOutputStream()
     */
    public ServletOutputStream getOutputStream()
    {
        return oout;
    }

    /* (non-Javadoc)
     * @see javax.servlet.ServletResponseWrapper#getWriter()
     */
    public PrintWriter getWriter()
    {
        return pout;
    }

    /* (non-Javadoc)
     * @see javax.servlet.ServletResponseWrapper#flushBuffer()
     */
    public void flushBuffer() throws IOException
    {
        pout.flush();
    }

    /* (non-Javadoc)
     * @see javax.servlet.http.HttpServletResponse#sendError(int, java.lang.String)
     */
    public void sendError(int sc, String msg)
    {
        log.warn("Ignoring call to sendError(" + sc + ", " + msg + ')'); //$NON-NLS-1$ //$NON-NLS-2$
    }

    /* (non-Javadoc)
     * @see javax.servlet.http.HttpServletResponse#sendError(int)
     */
    public void sendError(int sc)
    {
        log.warn("Ignoring call to sendError(" + sc + ')'); //$NON-NLS-1$
    }

    /* (non-Javadoc)
     * @see javax.servlet.http.HttpServletResponse#sendRedirect(java.lang.String)
     */
    public void sendRedirect(String location)
    {
        log.warn("Ignoring call to sendRedirect(" + location + ')'); //$NON-NLS-1$
    }

    /* (non-Javadoc)
     * @see javax.servlet.http.HttpServletResponse#setStatus(int)
     */
    public void setStatus(int sc)
    {
        log.warn("Ignoring call to setStatus(" + sc + ')'); //$NON-NLS-1$
    }

    /**
     * @see javax.servlet.http.HttpServletResponse#setStatus(int, java.lang.String)
     * @deprecated
     */
    public void setStatus(int sc, String sm)
    {
        log.warn("Ignoring call to setStatus(" + sc + ", " + sm + ')'); //$NON-NLS-1$ //$NON-NLS-2$
    }

    /* (non-Javadoc)
     * @see javax.servlet.ServletResponse#setContentLength(int)
     */
    public void setContentLength(int i)
    {
        // The content length of the original document is not likely to be the
        // same as the content length of the new document.
    }

    /* (non-Javadoc)
     * @see javax.servlet.ServletResponseWrapper#isCommitted()
     */
    public boolean isCommitted()
    {
        return false;
    }

    /* (non-Javadoc)
     * @see javax.servlet.ServletResponseWrapper#reset()
     */
    public void reset()
    {
    }

    /* (non-Javadoc)
     * @see javax.servlet.ServletResponseWrapper#resetBuffer()
     */
    public void resetBuffer()
    {
    }

    /* (non-Javadoc)
     * @see javax.servlet.ServletResponseWrapper#setBufferSize(int)
     */
    public void setBufferSize(int bufferSize)
    {
        // We're not writing data to the original source so setting the buffer
        // size on it isn't really important.
        this.bufferSize = bufferSize;
    }

    /* (non-Javadoc)
     * @see javax.servlet.ServletResponseWrapper#getBufferSize()
     */
    public int getBufferSize()
    {
        return bufferSize;
    }

    /**
     * The log stream
     */
    private static final Logger log = Logger.getLogger(SwallowingHttpServletResponse.class);

    /**
     * The forwarding output stream
     */
    private final ServletOutputStream oout;

    /**
     * The forwarding output stream
     */
    private final PrintWriter pout;

    /**
     * The ignored buffer size
     */
    private int bufferSize;
}
