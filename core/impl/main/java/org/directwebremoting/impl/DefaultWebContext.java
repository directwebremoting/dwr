package org.directwebremoting.impl;

import java.io.IOException;
import java.io.StringWriter;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.directwebremoting.Container;
import org.directwebremoting.ScriptSession;
import org.directwebremoting.extend.RealScriptSession;
import org.directwebremoting.extend.RealWebContext;
import org.directwebremoting.util.SwallowingHttpServletResponse;

/**
 * A default implementation of WebContext.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class DefaultWebContext extends DefaultServerContext implements RealWebContext
{
    /**
     * Create a new DefaultWebContext
     * @param container The IoC container
     * @param request The incoming http request
     * @param response The outgoing http reply
     * @param servletConfig The servlet configuration
     * @param servletContext The servlet context
     * @see org.directwebremoting.WebContextFactory.WebContextBuilder#engageThread(Container, HttpServletRequest, HttpServletResponse)
     */
    protected DefaultWebContext(Container container, HttpServletRequest request, HttpServletResponse response, ServletConfig servletConfig, ServletContext servletContext)
    {
        setServletConfig(servletConfig);
        setServletContext(servletContext);
        setContainer(container);

        this.request = request;
        this.response = response;
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.extend.RealWebContext#initialize(java.lang.String, org.directwebremoting.extend.RealScriptSession)
     */
    public void initialize(final String sentPage, RealScriptSession scriptSession)
    {
        this.scriptSession = scriptSession;
        this.page = sentPage;
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.WebContext#getCurrentPage()
     */
    public String getCurrentPage()
    {
        if (page == null)
        {
            throw new UnsupportedOperationException("CurrentPage is not supported from a JSON call.");
        }

        return page;
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.WebContext#getScriptSession()
     */
    public ScriptSession getScriptSession()
    {
        return scriptSession;
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.WebContext#getSession()
     */
    public HttpSession getSession()
    {
        return request.getSession();
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.WebContext#getSession(boolean)
     */
    public HttpSession getSession(boolean create)
    {
        return request.getSession(create);
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.WebContext#getHttpServletRequest()
     */
    public HttpServletRequest getHttpServletRequest()
    {
        return request;
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.WebContext#getHttpServletResponse()
     */
    public HttpServletResponse getHttpServletResponse()
    {
        return response;
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.WebContext#forwardToString(java.lang.String)
     */
    public String forwardToString(String url) throws ServletException, IOException
    {
        StringWriter sout = new StringWriter();
        StringBuffer buffer = sout.getBuffer();

        HttpServletResponse realResponse = getHttpServletResponse();
        HttpServletResponse fakeResponse = new SwallowingHttpServletResponse(realResponse, sout, realResponse.getCharacterEncoding());

        HttpServletRequest realRequest = getHttpServletRequest();
        realRequest.setAttribute(ATTRIBUTE_DWR, Boolean.TRUE);

        getServletContext().getRequestDispatcher(url).include(realRequest, fakeResponse);

        return buffer.toString();
    }

    /**
     * ScriptSession IDs are too long to be useful to humans. We shorten them
     * to the first 4 characters.
     */
    private String simplifyId(String id)
    {
        if (id == null)
        {
            return "[null]";
        }

        if (id.length() == 0)
        {
            return "[blank]";
        }

        if (id.length() < 4)
        {
            return id;
        }

        return id.substring(0, 4);
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        return "DefaultWebContext[id=" + simplifyId(scriptSession.getId()) + ", page=" + page + "]";
    }

    /**
     * The unique ID (like a session ID) assigned to the current page
     */
    private RealScriptSession scriptSession;

    /**
     * The URL of the current page
     */
    private String page;

    /**
     * The HttpServletRequest associated with the current request
     */
    private final HttpServletRequest request;

    /**
     * The HttpServletResponse associated with the current request
     */
    private final HttpServletResponse response;
}
