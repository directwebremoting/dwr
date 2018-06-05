package org.directwebremoting.util;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Backing class for Servlet 2.4 fake requests.
 */
class FakeHttpServletRequestObject24 // Note: does not implement interface as we are mapping versions in runtime
{
    /* (non-Javadoc)
     * @see javax.servlet.http.HttpServletRequest#getAuthType()
     */
    public String getAuthType()
    {
        return null;
    }

    /* (non-Javadoc)
     * @see javax.servlet.http.HttpServletRequest#getCookies()
     */
    public Cookie[] getCookies()
    {
        return null;
    }

    /* (non-Javadoc)
     * @see javax.servlet.http.HttpServletRequest#getDateHeader(java.lang.String)
     */
    public long getDateHeader(String name)
    {
        return -1;
    }

    /* (non-Javadoc)
     * @see javax.servlet.http.HttpServletRequest#getHeader(java.lang.String)
     */
    public String getHeader(String name)
    {
        return null;
    }

    /* (non-Javadoc)
     * @see javax.servlet.http.HttpServletRequest#getHeaders(java.lang.String)
     */
    public Enumeration<String> getHeaders(String name)
    {
        return Collections.enumeration(Collections.<String>emptySet());
    }

    /* (non-Javadoc)
     * @see javax.servlet.http.HttpServletRequest#getHeaderNames()
     */
    public Enumeration<String> getHeaderNames()
    {
        return Collections.enumeration(Collections.<String>emptySet());
    }

    /* (non-Javadoc)
     * @see javax.servlet.http.HttpServletRequest#getIntHeader(java.lang.String)
     */
    public int getIntHeader(String name)
    {
        return -1;
    }

    /* (non-Javadoc)
     * @see javax.servlet.http.HttpServletRequest#getMethod()
     */
    public String getMethod()
    {
        return method;
    }

    /**
     * @see #getMethod()
     */
    public void setMethod(String method)
    {
        this.method = method;
    }

    /**
     * @see #getMethod()
     */
    private String method = "GET";

    /* (non-Javadoc)
     * @see javax.servlet.http.HttpServletRequest#getPathInfo()
     */
    public String getPathInfo()
    {
        return pathInfo;
    }

    /**
     * @see #getPathInfo()
     */
    public void setPathInfo(String pathInfo)
    {
        this.pathInfo = pathInfo;
    }

    /**
     * @see #getPathInfo()
     */
    private String pathInfo;

    /* (non-Javadoc)
     * @see javax.servlet.http.HttpServletRequest#getPathTranslated()
     */
    public String getPathTranslated()
    {
        return null;
    }

    /* (non-Javadoc)
     * @see javax.servlet.http.HttpServletRequest#getContextPath()
     */
    public String getContextPath()
    {
        log.warn("Inventing data in FakeHttpServletRequest.getContextPath() to remain plausible.");
        return "";
    }

    /* (non-Javadoc)
     * @see javax.servlet.http.HttpServletRequest#getQueryString()
     */
    public String getQueryString()
    {
        return null;
    }

    /* (non-Javadoc)
     * @see javax.servlet.http.HttpServletRequest#getRemoteUser()
     */
    public String getRemoteUser()
    {
        return null;
    }

    /* (non-Javadoc)
     * @see javax.servlet.http.HttpServletRequest#isUserInRole(java.lang.String)
     */
    public boolean isUserInRole(String role)
    {
        return roles.contains(role);
    }

    /**
     * @see #isUserInRole(String)
     */
    public void addUserRole(String role)
    {
        roles.add(role);
    }

    /**
     * @see #isUserInRole(String)
     */
    private final Set<String> roles = new HashSet<String>();

    /* (non-Javadoc)
     * @see javax.servlet.http.HttpServletRequest#getUserPrincipal()
     */
    public Principal getUserPrincipal()
    {
        return null;
    }

    /* (non-Javadoc)
     * @see javax.servlet.http.HttpServletRequest#getRequestedSessionId()
     */
    public String getRequestedSessionId()
    {
        return null;
    }

    /* (non-Javadoc)
     * @see javax.servlet.http.HttpServletRequest#getRequestURI()
     */
    public String getRequestURI()
    {
        log.warn("Inventing data in FakeHttpServletRequest.getRequestURI() to remain plausible.");
        return "/";
    }

    /* (non-Javadoc)
     * @see javax.servlet.http.HttpServletRequest#getRequestURL()
     */
    public StringBuffer getRequestURL()
    {
        log.warn("Inventing data in FakeHttpServletRequest.getRequestURL() to remain plausible.");
        return new StringBuffer("http://localhost/");
    }

    /* (non-Javadoc)
     * @see javax.servlet.http.HttpServletRequest#getServletPath()
     */
    public String getServletPath()
    {
        log.warn("Inventing data in FakeHttpServletRequest.getServletPath() to remain plausible.");
        return "";
    }

    /* (non-Javadoc)
     * @see javax.servlet.http.HttpServletRequest#getSession(boolean)
     */
    public HttpSession getSession(boolean create)
    {
        if (!create)
        {
            return null;
        }

        return new FakeHttpSession();
    }

    /* (non-Javadoc)
     * @see javax.servlet.http.HttpServletRequest#getSession()
     */
    public HttpSession getSession()
    {
        return null;
    }

    /* (non-Javadoc)
     * @see javax.servlet.http.HttpServletRequest#isRequestedSessionIdValid()
     */
    public boolean isRequestedSessionIdValid()
    {
        return false;
    }

    /* (non-Javadoc)
     * @see javax.servlet.http.HttpServletRequest#isRequestedSessionIdFromCookie()
     */
    public boolean isRequestedSessionIdFromCookie()
    {
        return false;
    }

    /* (non-Javadoc)
     * @see javax.servlet.http.HttpServletRequest#isRequestedSessionIdFromURL()
     */
    public boolean isRequestedSessionIdFromURL()
    {
        return false;
    }

    /**
     * @see javax.servlet.http.HttpServletRequest#isRequestedSessionIdFromUrl()
     * @deprecated
     */
    @Deprecated
    public boolean isRequestedSessionIdFromUrl()
    {
        return false;
    }

    /* (non-Javadoc)
     * @see javax.servlet.ServletRequest#getAttribute(java.lang.String)
     */
    public Object getAttribute(String name)
    {
        return attributes.get(name);
    }

    /* (non-Javadoc)
     * @see javax.servlet.ServletRequest#getAttributeNames()
     */
    public Enumeration<String> getAttributeNames()
    {
        return Collections.enumeration(attributes.keySet());
    }

    /* (non-Javadoc)
     * @see javax.servlet.ServletRequest#getCharacterEncoding()
     */
    public String getCharacterEncoding()
    {
        return characterEncoding;
    }

    /* (non-Javadoc)
     * @see javax.servlet.ServletRequest#setCharacterEncoding(java.lang.String)
     */
    public void setCharacterEncoding(String characterEncoding) throws UnsupportedEncodingException
    {
        this.characterEncoding = characterEncoding;
    }

    /* (non-Javadoc)
     * @see javax.servlet.ServletRequest#getContentLength()
     */
    public int getContentLength()
    {
        return content.length;
    }

    /* (non-Javadoc)
     * @see javax.servlet.ServletRequest#getContentType()
     */
    public String getContentType()
    {
        return contentType;
    }

    /**
     * @see #getContentType()
     */
    public void setContentType(String contentType)
    {
        this.contentType = contentType;
    }

    /**
     * @see #getContentType()
     */
    private String contentType = "text/plain";

    /* (non-Javadoc)
     * @see javax.servlet.ServletRequest#getInputStream()
     */
    public ServletInputStream getInputStream() throws IOException
    {
        return new ServletInputStream()
        {
            private final ByteArrayInputStream proxy = new ByteArrayInputStream(content);

            /* (non-Javadoc)
             * @see java.io.InputStream#read()
             */
            @Override
            public int read() throws IOException
            {
                return proxy.read();
            }

            /* (non-Javadoc)
             * @see java.io.InputStream#available()
             */
            @Override
            public int available() throws IOException
            {
                return proxy.available();
            }

            /* (non-Javadoc)
             * @see java.io.InputStream#mark(int)
             */
            @Override
            public synchronized void mark(int readlimit)
            {
                proxy.mark(readlimit);
            }

            /* (non-Javadoc)
             * @see java.io.InputStream#markSupported()
             */
            @Override
            public boolean markSupported()
            {
                return proxy.markSupported();
            }

            /* (non-Javadoc)
             * @see java.io.InputStream#read(byte[], int, int)
             */
            @Override
            public int read(byte[] b, int off, int len) throws IOException
            {
                return proxy.read(b, off, len);
            }

            /* (non-Javadoc)
             * @see java.io.InputStream#close()
             */
            @Override
            public void close() throws IOException
            {
                proxy.close();
            }

            /* (non-Javadoc)
             * @see java.io.InputStream#read(byte[])
             */
            @Override
            public int read(byte[] b) throws IOException
            {
                return proxy.read(b);
            }

            /* (non-Javadoc)
             * @see java.io.InputStream#reset()
             */
            @Override
            public synchronized void reset() throws IOException
            {
                proxy.reset();
            }

            /* (non-Javadoc)
             * @see java.io.InputStream#skip(long)
             */
            @Override
            public long skip(long n) throws IOException
            {
                return proxy.skip(n);
            }
        };
    }

    /**
     * @see #getInputStream()
     */
    public void setContent(byte[] content)
    {
        this.content = content;
    }

    /**
     * @see #getInputStream()
     */
    public void setContent(String content)
    {
        this.content = content.getBytes();
    }

    /**
     * @see #getInputStream()
     */
    protected byte[] content;

    /**
     * @return "127.0.0.1"
     */
    public String getLocalAddr()
    {
        log.warn("Inventing data in FakeHttpServletRequest.getLocalAddr() to remain plausible.");
        return "127.0.0.1";
    }

    /**
     * @return "localhost"
     */
    public String getLocalName()
    {
        log.warn("Inventing data in FakeHttpServletRequest.getLocalName() to remain plausible.");
        return "localhost";
    }

    /**
     * @return 80
     */
    public int getLocalPort()
    {
        log.warn("Inventing data in FakeHttpServletRequest.getLocalPort() to remain plausible.");
        return 80;
    }

    /* (non-Javadoc)
     * @see javax.servlet.ServletRequest#getParameter(java.lang.String)
     */
    public String getParameter(String name)
    {
        return null;
    }

    /* (non-Javadoc)
     * @see javax.servlet.ServletRequest#getParameterNames()
     */
    public Enumeration<String> getParameterNames()
    {
        return Collections.enumeration(Collections.<String>emptySet());
    }

    /* (non-Javadoc)
     * @see javax.servlet.ServletRequest#getParameterValues(java.lang.String)
     */
    public String[] getParameterValues(String name)
    {
        return null;
    }

    /* (non-Javadoc)
     * @see javax.servlet.ServletRequest#getParameterMap()
     */
    public Map<String, String[]> getParameterMap()
    {
        return Collections.emptyMap();
    }

    /* (non-Javadoc)
     * @see javax.servlet.ServletRequest#getProtocol()
     */
    public String getProtocol()
    {
        log.warn("Inventing data in FakeHttpServletRequest.getProtocol() to remain plausible.");
        return "HTTP/1.1";
    }

    /* (non-Javadoc)
     * @see javax.servlet.ServletRequest#getScheme()
     */
    public String getScheme()
    {
        log.warn("Inventing data in FakeHttpServletRequest.getScheme() to remain plausible.");
        return "http";
    }

    /* (non-Javadoc)
     * @see javax.servlet.ServletRequest#getServerName()
     */
    public String getServerName()
    {
        log.warn("Inventing data in FakeHttpServletRequest.getServerName() to remain plausible.");
        return "localhost";
    }

    /* (non-Javadoc)
     * @see javax.servlet.ServletRequest#getServerPort()
     */
    public int getServerPort()
    {
        log.warn("Inventing data in FakeHttpServletRequest.getServerPort() to remain plausible.");
        return 80;
    }

    /* (non-Javadoc)
     * @see javax.servlet.ServletRequest#getReader()
     */
    public BufferedReader getReader() throws IOException
    {
        return new BufferedReader(new InputStreamReader(getInputStream()));
    }

    /* (non-Javadoc)
     * @see javax.servlet.ServletRequest#getRemoteAddr()
     */
    public String getRemoteAddr()
    {
        log.warn("Inventing data in FakeHttpServletRequest.getRemoteAddr() to remain plausible.");
        return "localhost";
    }

    /* (non-Javadoc)
     * @see javax.servlet.ServletRequest#getRemoteHost()
     */
    public String getRemoteHost()
    {
        log.warn("Inventing data in FakeHttpServletRequest.getRemoteHost() to remain plausible.");
        return "localhost";
    }

    /**
     * @return 80
     */
    public int getRemotePort()
    {
        log.warn("Inventing data in FakeHttpServletRequest.getRemotePort() to remain plausible.");
        return 80;
    }

    /* (non-Javadoc)
     * @see javax.servlet.ServletRequest#setAttribute(java.lang.String, java.lang.Object)
     */
    public void setAttribute(String name, Object o)
    {
        attributes.put(name, o);
    }

    /* (non-Javadoc)
     * @see javax.servlet.ServletRequest#removeAttribute(java.lang.String)
     */
    public void removeAttribute(String name)
    {
        attributes.remove(name);
    }

    /* (non-Javadoc)
     * @see javax.servlet.ServletRequest#getLocale()
     */
    public Locale getLocale()
    {
        return Locale.getDefault();
    }

    /* (non-Javadoc)
     * @see javax.servlet.ServletRequest#getLocales()
     */
    public Enumeration<Locale> getLocales()
    {
        return Collections.enumeration(Arrays.asList(Locale.getDefault()));
    }

    /* (non-Javadoc)
     * @see javax.servlet.ServletRequest#isSecure()
     */
    public boolean isSecure()
    {
        return false;
    }

    /* (non-Javadoc)
     * @see javax.servlet.ServletRequest#getRequestDispatcher(java.lang.String)
     */
    public RequestDispatcher getRequestDispatcher(String path)
    {
        return new RequestDispatcher()
        {
            /* (non-Javadoc)
             * @see javax.servlet.RequestDispatcher#include(javax.servlet.ServletRequest, javax.servlet.ServletResponse)
             */
            public void include(ServletRequest request, ServletResponse response) throws ServletException, IOException
            {
            }

            /* (non-Javadoc)
             * @see javax.servlet.RequestDispatcher#forward(javax.servlet.ServletRequest, javax.servlet.ServletResponse)
             */
            public void forward(ServletRequest request, ServletResponse response) throws ServletException, IOException
            {
            }
        };
    }

    /**
     * @see javax.servlet.ServletRequest#getRealPath(java.lang.String)
     * @deprecated
     */
    @Deprecated
    public String getRealPath(String path)
    {
        return null;
    }

    /**
     * The character encoding in the supposed request
     */
    private String characterEncoding = null;

    /**
     * The list of attributes
     */
    private final Map<String, Object> attributes = new HashMap<String, Object>();

    /**
     * The log stream
     */
    private static final Log log = LogFactory.getLog(FakeHttpServletRequestObject24.class);
}
