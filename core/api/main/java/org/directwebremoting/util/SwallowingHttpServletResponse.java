package org.directwebremoting.util;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.Collection;
import java.util.Locale;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Used by ExecutionContext to forward results back via javascript.
 * <p>We could like to implement {@link HttpServletResponse}, but there is a bug
 * in WebLogic where it casts to a {@link HttpServletResponseWrapper} so we
 * need to extend that.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public final class SwallowingHttpServletResponse implements HttpServletResponse
{
    /**
     * Create a new HttpServletResponse that allows you to catch the body
     * @param response The original HttpServletResponse
     * @param sout The place we copy responses to
     * @param characterEncoding The output encoding
     */
    public SwallowingHttpServletResponse(HttpServletResponse response, Writer sout, String characterEncoding)
    {
        pout = new PrintWriter(sout);
        outputStream = new WriterOutputStream(sout, characterEncoding);

        this.characterEncoding = characterEncoding;
    }

    /* (non-Javadoc)
     * @see javax.servlet.http.HttpServletResponseWrapper#addCookie(javax.servlet.http.Cookie)
     */
    public void addCookie(Cookie cookie)
    {
    }

    /* (non-Javadoc)
     * @see javax.servlet.http.HttpServletResponseWrapper#addDateHeader(java.lang.String, long)
     */
    public void addDateHeader(String name, long value)
    {
    }

    /* (non-Javadoc)
     * @see javax.servlet.http.HttpServletResponseWrapper#addHeader(java.lang.String, java.lang.String)
     */
    public void addHeader(String name, String value)
    {
    }

    /* (non-Javadoc)
     * @see javax.servlet.http.HttpServletResponseWrapper#addIntHeader(java.lang.String, int)
     */
    public void addIntHeader(String name, int value)
    {
    }

    /* (non-Javadoc)
     * @see javax.servlet.http.HttpServletResponseWrapper#containsHeader(java.lang.String)
     */
    public boolean containsHeader(String name)
    {
        return false;
    }

    /* (non-Javadoc)
     * @see javax.servlet.ServletResponseWrapper#flushBuffer()
     */
    public void flushBuffer() throws IOException
    {
        pout.flush();
    }

    /* (non-Javadoc)
     * @see javax.servlet.ServletResponseWrapper#getBufferSize()
     */
    public int getBufferSize()
    {
        return bufferSize;
    }

    /* (non-Javadoc)
     * @see javax.servlet.ServletResponseWrapper#getCharacterEncoding()
     */
    public String getCharacterEncoding()
    {
        return characterEncoding;
    }

    /**
     * @return The MIME type of the content
     * @see javax.servlet.ServletResponse#setContentType(String)
     */
    public String getContentType()
    {
        return contentType;
    }

    /**
     * Accessor for any error messages set using {@link #sendError(int)} or
     * {@link #sendError(int, String)}
     * @return The current error message
     */
    public String getErrorMessage()
    {
        return errorMessage;
    }

    /* (non-Javadoc)
     * @see javax.servlet.ServletResponseWrapper#getLocale()
     */
    public Locale getLocale()
    {
        return locale;
    }

    /* (non-Javadoc)
     * @see javax.servlet.ServletResponseWrapper#getOutputStream()
     */
    public ServletOutputStream getOutputStream()
    {
        return outputStream;
    }

    /**
     * Accessor for the redirect URL set using {@link #sendRedirect(String)}
     * @return The redirect URL
     */
    public String getRedirectedUrl()
    {
        return redirectedUrl;
    }

    /**
     * What HTTP status code should be returned?
     * @return The current http status code
     */
    public int getStatus()
    {
        return status;
    }

    /* (non-Javadoc)
     * @see javax.servlet.ServletResponseWrapper#getWriter()
     */
    public PrintWriter getWriter()
    {
        return pout;
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
     * @see javax.servlet.http.HttpServletResponseWrapper#sendError(int)
     */
    public void sendError(int newStatus)
    {
        if (committed)
        {
            throw new IllegalStateException("Cannot set error status - response is already committed");
        }

        log.warn("Ignoring call to sendError(" + newStatus + ')');

        status = newStatus;
        committed = true;
    }

    /* (non-Javadoc)
     * @see javax.servlet.http.HttpServletResponseWrapper#sendError(int, java.lang.String)
     */
    public void sendError(int newStatus, String newErrorMessage)
    {
        if (committed)
        {
            throw new IllegalStateException("Cannot set error status - response is already committed");
        }

        log.warn("Ignoring call to sendError(" + newStatus + ", " + newErrorMessage + ')');

        status = newStatus;
        errorMessage = newErrorMessage;
        committed = true;
    }

    /* (non-Javadoc)
     * @see javax.servlet.http.HttpServletResponseWrapper#sendRedirect(java.lang.String)
     */
    public void sendRedirect(String location)
    {
        if (committed)
        {
            throw new IllegalStateException("Cannot send redirect - response is already committed");
        }

        log.warn("Ignoring call to sendRedirect(" + location + ')');

        redirectedUrl = location;
        committed = true;
    }

    /* (non-Javadoc)
     * @see javax.servlet.ServletResponseWrapper#setBufferSize(int)
     */
    public void setBufferSize(int bufferSize)
    {
        this.bufferSize = bufferSize;
    }

    /**
     * @param characterEncoding The new encoding to use for response strings
     * @see javax.servlet.ServletResponseWrapper#getCharacterEncoding()
     */
    public void setCharacterEncoding(String characterEncoding)
    {
        this.characterEncoding = characterEncoding;
    }

    /* (non-Javadoc)
     * @see javax.servlet.ServletResponseWrapper#setContentLength(int)
     */
    public void setContentLength(int i)
    {
        // The content length of the original document is not likely to be the
        // same as the content length of the new document.
    }

    /* (non-Javadoc)
     * @see javax.servlet.ServletResponseWrapper#setContentType(java.lang.String)
     */
    public void setContentType(String contentType)
    {
        this.contentType = contentType;
    }

    /* (non-Javadoc)
     * @see javax.servlet.http.HttpServletResponseWrapper#setDateHeader(java.lang.String, long)
     */
    public void setDateHeader(String name, long value)
    {
    }

    /* (non-Javadoc)
     * @see javax.servlet.http.HttpServletResponseWrapper#setHeader(java.lang.String, java.lang.String)
     */
    public void setHeader(String name, String value)
    {
    }

    /* (non-Javadoc)
     * @see javax.servlet.http.HttpServletResponseWrapper#setIntHeader(java.lang.String, int)
     */
    public void setIntHeader(String name, int value)
    {
    }

    /* (non-Javadoc)
     * @see javax.servlet.ServletResponseWrapper#setLocale(java.util.Locale)
     */
    public void setLocale(Locale locale)
    {
        this.locale = locale;
    }

    /* (non-Javadoc)
     * @see javax.servlet.http.HttpServletResponseWrapper#setStatus(int)
     */
    public void setStatus(int status)
    {
        this.status = status;
        log.warn("Ignoring call to setStatus(" + status + ')');
    }

    /**
     * @see javax.servlet.http.HttpServletResponse#setStatus(int, java.lang.String)
     * @deprecated
     */
    @Deprecated
    public void setStatus(int newStatus, String newErrorMessage)
    {
        status = newStatus;
        errorMessage = newErrorMessage;
        log.warn("Ignoring call to setStatus(" + newStatus + ", " + newErrorMessage + ')');
    }

    /* (non-Javadoc)
     * @see javax.servlet.http.HttpServletResponse#encodeURL(java.lang.String)
     */
    public String encodeURL(String paramString)
    {
        return null;
    }

    /* (non-Javadoc)
     * @see javax.servlet.http.HttpServletResponse#encodeRedirectURL(java.lang.String)
     */
    public String encodeRedirectURL(String paramString)
    {
        return null;
    }

    /* (non-Javadoc)
     * @see javax.servlet.http.HttpServletResponse#encodeUrl(java.lang.String)
     */
    public String encodeUrl(String paramString)
    {
        return null;
    }

    /* (non-Javadoc)
     * @see javax.servlet.http.HttpServletResponse#encodeRedirectUrl(java.lang.String)
     */
    public String encodeRedirectUrl(String paramString)
    {
        return null;
    }

    /* (non-Javadoc)
     * @see javax.servlet.http.HttpServletResponse#getHeader(java.lang.String)
     */
    public String getHeader(String paramString)
    {
        return null;
    }

    /* (non-Javadoc)
     * @see javax.servlet.http.HttpServletResponse#getHeaders(java.lang.String)
     */
    public Collection<String> getHeaders(String paramString)
    {
        return null;
    }

    /* (non-Javadoc)
     * @see javax.servlet.http.HttpServletResponse#getHeaderNames()
     */
    public Collection<String> getHeaderNames()
    {
        return null;
    }

    /**
     * The ignored buffer size
     */
    private int bufferSize = 0;

    /**
     * The character encoding used
     */
    private String characterEncoding;

    /**
     * Has the response been committed
     */
    private boolean committed = false;

    /**
     * The MIME type of the output body
     */
    private String contentType = null;

    /**
     * The error message sent with a status != HttpServletResponse.SC_OK
     */
    private String errorMessage = null;

    /**
     * Locale setting: defaults to platform default
     */
    private Locale locale = Locale.getDefault();

    /**
     * The forwarding output stream
     */
    private final ServletOutputStream outputStream;

    /**
     * The forwarding output stream
     */
    private final PrintWriter pout;

    /**
     * Where are we to redirect the user to?
     */
    private String redirectedUrl = null;

    /**
     * The HTTP status
     */
    private int status = HttpServletResponse.SC_OK;

    /**
     * The log stream
     */
    private static final Log log = LogFactory.getLog(SwallowingHttpServletResponse.class);
}
