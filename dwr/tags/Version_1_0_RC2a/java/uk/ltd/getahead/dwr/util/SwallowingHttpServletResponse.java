package uk.ltd.getahead.dwr.util;

import java.io.PrintWriter;
import java.io.Writer;
import java.lang.reflect.Method;
import java.util.Locale;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

/**
 * Used by ExecutionContext to forward results back via javascript
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public final class SwallowingHttpServletResponse implements HttpServletResponse
{
    /**
     * @param response The real HttpServletResponse
     * @param sout The place we copy responses to
     * @param url The thing we are reading
     */
    public SwallowingHttpServletResponse(HttpServletResponse response, Writer sout, String url)
    {
        this.response = response;
        this.url = url;

        pout = new PrintWriter(sout);
        oout = new WriterOutputStream(sout);
    }

    /* (non-Javadoc)
     * @see javax.servlet.ServletResponseWrapper#getOutputStream()
     */
    public ServletOutputStream getOutputStream()
    {
        log.warn("Potential errors in forwardingRequest converting binary data to string for url: " + url); //$NON-NLS-1$
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
    public void flushBuffer()
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

    /* (non-Javadoc)
     * @see javax.servlet.http.HttpServletResponse#setStatus(int, java.lang.String)
     */
    public void setStatus(int sc, String sm)
    {
        log.warn("Ignoring call to setStatus(" + sc + ", " + sm + ')'); //$NON-NLS-1$ //$NON-NLS-2$
    }

    /* (non-Javadoc)
     * @see javax.servlet.http.HttpServletResponse#addCookie(javax.servlet.http.Cookie)
     */
    public void addCookie(Cookie cookie)
    {
        response.addCookie(cookie);
    }

    /* (non-Javadoc)
     * @see javax.servlet.http.HttpServletResponse#containsHeader(java.lang.String)
     */
    public boolean containsHeader(String name)
    {
        return response.containsHeader(name);
    }

    /* (non-Javadoc)
     * @see javax.servlet.http.HttpServletResponse#encodeURL(java.lang.String)
     */
    public String encodeURL(String encurl)
    {
        return response.encodeURL(encurl);
    }

    /* (non-Javadoc)
     * @see javax.servlet.http.HttpServletResponse#encodeRedirectURL(java.lang.String)
     */
    public String encodeRedirectURL(String encurl)
    {
        return response.encodeRedirectURL(encurl);
    }

    /**
     * @deprecated
     * @see javax.servlet.http.HttpServletResponse#encodeUrl(java.lang.String)
     */
    public String encodeUrl(String encurl)
    {
        return response.encodeUrl(encurl);
    }

    /**
     * @deprecated
     * @see javax.servlet.http.HttpServletResponse#encodeRedirectUrl(java.lang.String)
     */
    public String encodeRedirectUrl(String encurl)
    {
        return response.encodeRedirectUrl(encurl);
    }

    /* (non-Javadoc)
     * @see javax.servlet.http.HttpServletResponse#setDateHeader(java.lang.String, long)
     */
    public void setDateHeader(String name, long date)
    {
        response.setDateHeader(name, date);
    }

    /* (non-Javadoc)
     * @see javax.servlet.http.HttpServletResponse#addDateHeader(java.lang.String, long)
     */
    public void addDateHeader(String name, long date)
    {
        response.addDateHeader(name, date);
    }

    /* (non-Javadoc)
     * @see javax.servlet.http.HttpServletResponse#setHeader(java.lang.String, java.lang.String)
     */
    public void setHeader(String name, String value)
    {
        response.setHeader(name, value);
    }

    /* (non-Javadoc)
     * @see javax.servlet.http.HttpServletResponse#addHeader(java.lang.String, java.lang.String)
     */
    public void addHeader(String name, String value)
    {
        response.addHeader(name, value);
    }

    /* (non-Javadoc)
     * @see javax.servlet.http.HttpServletResponse#setIntHeader(java.lang.String, int)
     */
    public void setIntHeader(String name, int value)
    {
        response.setIntHeader(name, value);
    }

    /* (non-Javadoc)
     * @see javax.servlet.http.HttpServletResponse#addIntHeader(java.lang.String, int)
     */
    public void addIntHeader(String name, int value)
    {
        response.addIntHeader(name, value);
    }

    /**
     * Servlet 2.4 spec method, with some dancing so it will work under 2.3 as
     * well.
     * @param enc The new encoding
     */
    public void setCharacterEncoding(String enc)
    {
        if (!characterEncodingSearched)
        {
            try
            {
                characterEncodingMethod = response.getClass().getMethod("setCharacterEncoding", new Class[] { String.class }); //$NON-NLS-1$
            }
            catch (Exception ex)
            {
                log.warn("Failed to find setCharacterEncoding method", ex); //$NON-NLS-1$
            }

            characterEncodingSearched = true;
        }

        if (characterEncodingMethod != null)
        {
            try
            {
                characterEncodingMethod.invoke(response, new Object[] { enc });
            }
            catch (Exception ex)
            {
                log.warn("Error setting CharacterEncoding", ex); //$NON-NLS-1$
            }
        }
    }

    private Method characterEncodingMethod = null;
    private boolean characterEncodingSearched = false;

    /* (non-Javadoc)
     * @see javax.servlet.ServletResponse#getCharacterEncoding()
     */
    public String getCharacterEncoding()
    {
        return response.getCharacterEncoding();
    }

    /* (non-Javadoc)
     * @see javax.servlet.ServletResponse#setContentLength(int)
     */
    public void setContentLength(int len)
    {
        response.setContentLength(len);
    }

    /* (non-Javadoc)
     * @see javax.servlet.ServletResponse#setContentType(java.lang.String)
     */
    public void setContentType(String type)
    {
        contentTypeCache = type;
        response.setContentType(type);
    }

    /**
     * Servlet 2.4 spec method.
     * @return The content type
     */
    public String getContentType()
    {
        if (!contentTypeSearched)
        {
            try
            {
                contentTypeMethod = response.getClass().getMethod("getContentType", null); //$NON-NLS-1$
            }
            catch (Exception ex)
            {
                log.warn("Failed to find getContentType method", ex); //$NON-NLS-1$
            }

            contentTypeSearched = true;
        }

        if (contentTypeMethod != null)
        {
            try
            {
                return (String) contentTypeMethod.invoke(response, null);
            }
            catch (Exception ex)
            {
                log.warn("Error getting ContentType", ex); //$NON-NLS-1$
            }
        }

        return contentTypeCache;
    }

    private Method contentTypeMethod = null;
    private boolean contentTypeSearched = false;
    private String contentTypeCache = "text/html"; //$NON-NLS-1$

    /* (non-Javadoc)
     * @see javax.servlet.ServletResponse#setBufferSize(int)
     */
    public void setBufferSize(int size)
    {
        response.setBufferSize(size);
    }

    /* (non-Javadoc)
     * @see javax.servlet.ServletResponse#getBufferSize()
     */
    public int getBufferSize()
    {
        return response.getBufferSize();
    }

    /* (non-Javadoc)
     * @see javax.servlet.ServletResponse#isCommitted()
     */
    public boolean isCommitted()
    {
        return response.isCommitted();
    }

    /* (non-Javadoc)
     * @see javax.servlet.ServletResponse#reset()
     */
    public void reset()
    {
        response.reset();
    }

    /* (non-Javadoc)
     * @see javax.servlet.ServletResponse#resetBuffer()
     */
    public void resetBuffer()
    {
        response.resetBuffer();
    }

    /* (non-Javadoc)
     * @see javax.servlet.ServletResponse#setLocale(java.util.Locale)
     */
    public void setLocale(Locale loc)
    {
        response.setLocale(loc);
    }

    /* (non-Javadoc)
     * @see javax.servlet.ServletResponse#getLocale()
     */
    public Locale getLocale()
    {
        return response.getLocale();
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
     * The url that we are grabbing the output from
     */
    private final String url;

    /**
     * The forwarding output stream
     */
    private final PrintWriter pout;

    /**
     * The real response that we grab data from
     */
    private HttpServletResponse response;
}
