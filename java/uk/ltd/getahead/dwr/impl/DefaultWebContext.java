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
import java.io.InputStream;
import java.io.StringWriter;
import java.util.Properties;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import uk.ltd.getahead.dwr.Container;
import uk.ltd.getahead.dwr.WebContext;
import uk.ltd.getahead.dwr.util.SwallowingHttpServletResponse;

/**
 * A default implementation of WebContext
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class DefaultWebContext implements WebContext
{
    /**
     * Create a new DefaultWebContext
     * @param request The incoming http request
     * @param response The outgoing http reply
     * @param config The servlet configuration
     * @param context The servlet context
     * @param container The IoC container
     * @see uk.ltd.getahead.dwr.WebContextBuilder#set(HttpServletRequest, HttpServletResponse, ServletConfig, ServletContext, Container)
     */
    public DefaultWebContext(HttpServletRequest request, HttpServletResponse response, ServletConfig config, ServletContext context, Container container)
    {
        this.request = request;
        this.response = response;
        this.config = config;
        this.context = context;
        this.container = container;
    }

    /* (non-Javadoc)
     * @see uk.ltd.getahead.dwr.WebContext#getFactory()
     */
    public Container getContainer()
    {
        return container;
    }

    /* (non-Javadoc)
     * @see uk.ltd.getahead.dwr.WebContext#getSession()
     */
    public HttpSession getSession()
    {
        return request.getSession();
    }

    /* (non-Javadoc)
     * @see uk.ltd.getahead.dwr.WebContext#getSession(boolean)
     */
    public HttpSession getSession(boolean create)
    {
        return request.getSession(create);
    }

    /* (non-Javadoc)
     * @see uk.ltd.getahead.dwr.WebContext#getServletConfig()
     */
    public ServletConfig getServletConfig()
    {
        return config;
    }

    /* (non-Javadoc)
     * @see uk.ltd.getahead.dwr.WebContext#getServletContext()
     */
    public ServletContext getServletContext()
    {
        return context;
    }

    /* (non-Javadoc)
     * @see uk.ltd.getahead.dwr.WebContext#getHttpServletRequest()
     */
    public HttpServletRequest getHttpServletRequest()
    {
        return request;
    }

    /* (non-Javadoc)
     * @see uk.ltd.getahead.dwr.WebContext#getHttpServletResponse()
     */
    public HttpServletResponse getHttpServletResponse()
    {
        return response;
    }

    /* (non-Javadoc)
     * @see uk.ltd.getahead.dwr.WebContext#forwardToString(java.lang.String)
     */
    public String forwardToString(String url) throws ServletException, IOException
    {
        StringWriter sout = new StringWriter();
        StringBuffer buffer = sout.getBuffer();

        HttpServletResponse fakeResponse = new SwallowingHttpServletResponse(sout, getHttpServletResponse().getCharacterEncoding());

        getServletContext().getRequestDispatcher(url).forward(getHttpServletRequest(), fakeResponse);

        return buffer.toString();
    }

    /* (non-Javadoc)
     * @see uk.ltd.getahead.dwr.WebContext#getVersion()
     */
    public String getVersion()
    {
        synchronized (propLock)
        {
            if (props == null)
            {
                loadProperties();
            }

            return props.getProperty(KEY_VERSION);
        }
    }

    /**
     * Fish the version number out of the dwr.properties file.
     * @return The current version number.
     */
    protected String getSourceControlInfo()
    {
        synchronized (propLock)
        {
            if (props == null)
            {
                loadProperties();
            }

            return props.getProperty(KEY_SCCINFO);
        }
    }

    /**
     * Load the properties from the internal properties file.
     */
    private void loadProperties()
    {
        synchronized (propLock)
        {
            props = new Properties();

            try
            {
                InputStream in = getClass().getResourceAsStream(FILENAME_VERSION);
                props.load(in);
            }
            catch (Exception ex)
            {
                props.put(KEY_VERSION, VALUE_UNKNOWN);
                props.put(KEY_SCCINFO, VALUE_UNKNOWN);
                props.put(KEY_ERROR, ex.toString());
            }
        }
    }

    private HttpServletRequest request = null;
    private HttpServletResponse response = null;
    private ServletConfig config = null;
    private ServletContext context = null;
    private Container container;

    private static final String FILENAME_VERSION = "/dwr-version.properties"; //$NON-NLS-1$
    private static final String KEY_VERSION = "version"; //$NON-NLS-1$
    private static final String KEY_SCCINFO = "scc-info"; //$NON-NLS-1$
    private static final String KEY_ERROR = "error"; //$NON-NLS-1$
    private static final String VALUE_UNKNOWN = "unknown"; //$NON-NLS-1$

    private static Properties props = null;
    private static final Object propLock = new Object();
}
