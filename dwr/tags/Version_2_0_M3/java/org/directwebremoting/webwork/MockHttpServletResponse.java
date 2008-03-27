/*
 * Copyright 2002-2006 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.directwebremoting.webwork;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.Assert;

/**
 * Mock implementation of the HttpServletResponse interface.
 *
 * <p>Used for testing the web framework; also useful
 * for testing application controllers.
 *
 * @author Rod Johnson
 * @author Juergen Hoeller
 * @since 1.0.2
 */
public class MockHttpServletResponse implements HttpServletResponse
{
    private static final String CHARSET_PREFIX = "charset=";

    //---------------------------------------------------------------------
    // ServletResponse properties
    //---------------------------------------------------------------------

    private String characterEncoding = "ISO-8859-1";

    private final ByteArrayOutputStream content = new ByteArrayOutputStream();

    private final DelegatingServletOutputStream outputStream = new DelegatingServletOutputStream(this.content);

    private PrintWriter writer;

    private int contentLength = 0;

    private String contentType;

    private int bufferSize = 4096;

    private boolean committed;

    private Locale locale = Locale.getDefault();

    //---------------------------------------------------------------------
    // HttpServletResponse properties
    //---------------------------------------------------------------------

    private final List cookies = new ArrayList();

    private final Map headers = new HashMap();

    private int status = HttpServletResponse.SC_OK;

    private String errorMessage;

    private String redirectedUrl;

    private String forwardedUrl;

    private String includedUrl;

    //---------------------------------------------------------------------
    // ServletResponse interface
    //---------------------------------------------------------------------

    /**
     * Compliment to {@link javax.servlet.ServletResponse#getCharacterEncoding()}
     * @param characterEncoding See link above
     */
    public void setCharacterEncoding(String characterEncoding)
    {
        this.characterEncoding = characterEncoding;
    }

    /* (non-Javadoc)
     * @see javax.servlet.ServletResponse#getCharacterEncoding()
     */
    public String getCharacterEncoding()
    {
        return characterEncoding;
    }

    /* (non-Javadoc)
     * @see javax.servlet.ServletResponse#getOutputStream()
     */
    public ServletOutputStream getOutputStream()
    {
        return this.outputStream;
    }

    /* (non-Javadoc)
     * @see javax.servlet.ServletResponse#getWriter()
     */
    public PrintWriter getWriter() throws UnsupportedEncodingException
    {
        if (this.writer == null)
        {
            Writer targetWriter = (this.characterEncoding != null ? new OutputStreamWriter(this.content, this.characterEncoding) : new OutputStreamWriter(this.content));
            this.writer = new PrintWriter(targetWriter);
        }
        return writer;
    }

    /**
     * Fetch the contents written to this stream
     * @return contents written to this stream
     */
    public byte[] getContentAsByteArray()
    {
        flushBuffer();
        return this.content.toByteArray();
    }

    /**
     * Fetch the contents written to this stream
     * @return contents written to this stream
     * @throws UnsupportedEncodingException If the encoding is not valid
     */
    public String getContentAsString() throws UnsupportedEncodingException
    {
        flushBuffer();
        return (this.characterEncoding != null) ? this.content.toString(this.characterEncoding) : this.content.toString();
    }

    /* (non-Javadoc)
     * @see javax.servlet.ServletResponse#setContentLength(int)
     */
    public void setContentLength(int contentLength)
    {
        this.contentLength = contentLength;
    }

    /**
     * Compliment to {@link javax.servlet.ServletResponse#setContentLength(int)}
     * @return See link above
     */
    public int getContentLength()
    {
        return contentLength;
    }

    /* (non-Javadoc)
     * @see javax.servlet.ServletResponse#setContentType(java.lang.String)
     */
    public void setContentType(String contentType)
    {
        this.contentType = contentType;
        if (contentType != null)
        {
            int charsetIndex = contentType.toLowerCase().indexOf(CHARSET_PREFIX);
            if (charsetIndex != -1)
            {
                String encoding = contentType.substring(charsetIndex + CHARSET_PREFIX.length());
                setCharacterEncoding(encoding);
            }
        }
    }

    /**
     * Compliment to {@link javax.servlet.ServletResponse#setContentType(java.lang.String)}
     * @return See link above
     */
    public String getContentType()
    {
        return contentType;
    }

    /* (non-Javadoc)
     * @see javax.servlet.ServletResponse#setBufferSize(int)
     */
    public void setBufferSize(int bufferSize)
    {
        this.bufferSize = bufferSize;
    }

    /* (non-Javadoc)
     * @see javax.servlet.ServletResponse#getBufferSize()
     */
    public int getBufferSize()
    {
        return bufferSize;
    }

    /* (non-Javadoc)
     * @see javax.servlet.ServletResponse#flushBuffer()
     */
    public void flushBuffer()
    {
        if (this.writer != null)
        {
            this.writer.flush();
        }
        if (this.outputStream != null)
        {
            try
            {
                this.outputStream.flush();
            }
            catch (IOException ex)
            {
                throw new IllegalStateException("Could not flush OutputStream: " + ex.getMessage());
            }
        }
        this.committed = true;
    }

    /* (non-Javadoc)
     * @see javax.servlet.ServletResponse#resetBuffer()
     */
    public void resetBuffer()
    {
        if (this.committed)
        {
            throw new IllegalStateException("Cannot reset buffer - response is already committed");
        }
        this.content.reset();
    }

    /**
     * Compilment to {@link javax.servlet.ServletResponse#isCommitted()}
     * @param committed See link above
     */
    public void setCommitted(boolean committed)
    {
        this.committed = committed;
    }

    /* (non-Javadoc)
     * @see javax.servlet.ServletResponse#isCommitted()
     */
    public boolean isCommitted()
    {
        return committed;
    }

    /* (non-Javadoc)
     * @see javax.servlet.ServletResponse#reset()
     */
    public void reset()
    {
        resetBuffer();
        this.characterEncoding = null;
        this.contentLength = 0;
        this.contentType = null;
        this.locale = null;
        this.cookies.clear();
        this.headers.clear();
        this.status = HttpServletResponse.SC_OK;
        this.errorMessage = null;
    }

    /* (non-Javadoc)
     * @see javax.servlet.ServletResponse#setLocale(java.util.Locale)
     */
    public void setLocale(Locale locale)
    {
        this.locale = locale;
    }

    /* (non-Javadoc)
     * @see javax.servlet.ServletResponse#getLocale()
     */
    public Locale getLocale()
    {
        return locale;
    }

    //---------------------------------------------------------------------
    // HttpServletResponse interface
    //---------------------------------------------------------------------

    /* (non-Javadoc)
     * @see javax.servlet.http.HttpServletResponse#addCookie(javax.servlet.http.Cookie)
     */
    public void addCookie(Cookie cookie)
    {
        Assert.notNull(cookie, "Cookie must not be null");
        this.cookies.add(cookie);
    }

    /**
     * Compliment to {@link javax.servlet.http.HttpServletResponse#addCookie(javax.servlet.http.Cookie)}
     * @return See link above
     */
    public Cookie[] getCookies()
    {
        return (Cookie[]) this.cookies.toArray(new Cookie[this.cookies.size()]);
    }

    /**
     * Compliment to {@link javax.servlet.http.HttpServletResponse#addCookie(javax.servlet.http.Cookie)}
     * @param name See link above
     * @return See link above
     */
    public Cookie getCookie(String name)
    {
        Assert.notNull(name, "Cookie name must not be null");
        for (Iterator it = this.cookies.iterator(); it.hasNext();)
        {
            Cookie cookie = (Cookie) it.next();
            if (name.equals(cookie.getName()))
            {
                return cookie;
            }
        }
        return null;
    }

    /* (non-Javadoc)
     * @see javax.servlet.http.HttpServletResponse#containsHeader(java.lang.String)
     */
    public boolean containsHeader(String name)
    {
        Assert.notNull(name, "Header name must not be null");
        return this.headers.containsKey(name);
    }

    /**
     * Compliment to {@link javax.servlet.http.HttpServletResponse#containsHeader(java.lang.String)}
     * @return See link above
     */
    public Set getHeaderNames()
    {
        return this.headers.keySet();
    }

    /**
     * Compliment to {@link javax.servlet.http.HttpServletResponse#containsHeader(java.lang.String)}
     * @param name See link above
     * @return See link above
     */
    public Object getHeader(String name)
    {
        Assert.notNull(name, "Header name must not be null");
        return this.headers.get(name);
    }

    /**
     * Compliment to {@link javax.servlet.http.HttpServletResponse#containsHeader(java.lang.String)}
     * @param name See link above
     * @return See link above
     */
    public List getHeaders(String name)
    {
        Assert.notNull(name, "Header name must not be null");
        Object value = this.headers.get(name);
        if (value instanceof List)
        {
            return (List) value;
        }
        else if (value != null)
        {
            return Collections.singletonList(value);
        }
        else
        {
            return Collections.EMPTY_LIST;
        }
    }

    /* (non-Javadoc)
     * @see javax.servlet.http.HttpServletResponse#encodeURL(java.lang.String)
     */
    public String encodeURL(String url)
    {
        return url;
    }

    /* (non-Javadoc)
     * @see javax.servlet.http.HttpServletResponse#encodeRedirectURL(java.lang.String)
     */
    public String encodeRedirectURL(String url)
    {
        return url;
    }

    /* (non-Javadoc)
     * @see javax.servlet.http.HttpServletResponse#encodeUrl(java.lang.String)
     */
    public String encodeUrl(String url)
    {
        return url;
    }

    /* (non-Javadoc)
     * @see javax.servlet.http.HttpServletResponse#encodeRedirectUrl(java.lang.String)
     */
    public String encodeRedirectUrl(String url)
    {
        return url;
    }

    /* (non-Javadoc)
     * @see javax.servlet.http.HttpServletResponse#sendError(int, java.lang.String)
     */
    public void sendError(int myStatus, String message) throws IOException
    {
        if (this.committed)
        {
            throw new IllegalStateException("Cannot set error status - response is already committed");
        }
        this.status = myStatus;
        this.errorMessage = message;
        this.committed = true;
    }

    /* (non-Javadoc)
     * @see javax.servlet.http.HttpServletResponse#sendError(int)
     */
    public void sendError(int myStatus) throws IOException
    {
        if (this.committed)
        {
            throw new IllegalStateException("Cannot set error status - response is already committed");
        }
        this.status = myStatus;
        this.committed = true;
    }

    /* (non-Javadoc)
     * @see javax.servlet.http.HttpServletResponse#sendRedirect(java.lang.String)
     */
    public void sendRedirect(String url) throws IOException
    {
        if (this.committed)
        {
            throw new IllegalStateException("Cannot send redirect - response is already committed");
        }
        Assert.notNull(url, "Redirect URL must not be null");
        this.redirectedUrl = url;
        this.committed = true;
    }

    /**
     * Compliment to {@link javax.servlet.http.HttpServletResponse#sendRedirect(java.lang.String)}
     * @return See link above
     */
    public String getRedirectedUrl()
    {
        return redirectedUrl;
    }

    /* (non-Javadoc)
     * @see javax.servlet.http.HttpServletResponse#setDateHeader(java.lang.String, long)
     */
    public void setDateHeader(String name, long value)
    {
        Assert.notNull(name, "Header name must not be null");
        this.headers.put(name, new Long(value));
    }

    /* (non-Javadoc)
     * @see javax.servlet.http.HttpServletResponse#addDateHeader(java.lang.String, long)
     */
    public void addDateHeader(String name, long value)
    {
        doAddHeader(name, new Long(value));
    }

    /* (non-Javadoc)
     * @see javax.servlet.http.HttpServletResponse#setHeader(java.lang.String, java.lang.String)
     */
    public void setHeader(String name, String value)
    {
        Assert.notNull(name, "Header name must not be null");
        this.headers.put(name, value);
    }

    /* (non-Javadoc)
     * @see javax.servlet.http.HttpServletResponse#addHeader(java.lang.String, java.lang.String)
     */
    public void addHeader(String name, String value)
    {
        doAddHeader(name, value);
    }

    /* (non-Javadoc)
     * @see javax.servlet.http.HttpServletResponse#setIntHeader(java.lang.String, int)
     */
    public void setIntHeader(String name, int value)
    {
        Assert.notNull(name, "Header name must not be null");
        this.headers.put(name, new Integer(value));
    }

    /* (non-Javadoc)
     * @see javax.servlet.http.HttpServletResponse#addIntHeader(java.lang.String, int)
     */
    public void addIntHeader(String name, int value)
    {
        doAddHeader(name, new Integer(value));
    }

    /**
     * @param name
     * @param value
     */
    private void doAddHeader(String name, Object value)
    {
        Assert.notNull(name, "Header name must not be null");
        Assert.notNull(value, "Header value must not be null");
        Object oldValue = this.headers.get(name);
        if (oldValue instanceof List)
        {
            List list = (List) oldValue;
            list.add(value);
        }
        else if (oldValue != null)
        {
            List list = new LinkedList();
            list.add(oldValue);
            list.add(value);
            this.headers.put(name, list);
        }
        else
        {
            this.headers.put(name, value);
        }
    }

    /* (non-Javadoc)
     * @see javax.servlet.http.HttpServletResponse#setStatus(int)
     */
    public void setStatus(int status)
    {
        this.status = status;
    }

    /* (non-Javadoc)
     * @see javax.servlet.http.HttpServletResponse#setStatus(int, java.lang.String)
     */
    public void setStatus(int status, String errorMessage)
    {
        this.status = status;
        this.errorMessage = errorMessage;
    }

    /**
     * Compliment to {@link javax.servlet.http.HttpServletResponse#setStatus(int)}
     * @return See link above
     */
    public int getStatus()
    {
        return status;
    }

    /**
     * Compliment to {@link javax.servlet.http.HttpServletResponse#setStatus(int, java.lang.String)}
     * @deprecated
     * @return See link above
     */
    public String getErrorMessage()
    {
        return errorMessage;
    }

    //---------------------------------------------------------------------
    // Methods for MockRequestDispatcher
    //---------------------------------------------------------------------

    /**
     * Accessor for the forwarded URL
     * @param forwardedUrl the forwarded URL
     */
    public void setForwardedUrl(String forwardedUrl)
    {
        this.forwardedUrl = forwardedUrl;
    }

    /**
     * Accessor for the forwarded URL
     * @return the forwarded URL
     */
    public String getForwardedUrl()
    {
        return forwardedUrl;
    }

    /**
     * Accessor for the included URL
     * @param includedUrl the included URL
     */
    public void setIncludedUrl(String includedUrl)
    {
        this.includedUrl = includedUrl;
    }

    /**
     * Accessor for the included URL
     * @return the included URL
     */
    public String getIncludedUrl()
    {
        return includedUrl;
    }

}
