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
package org.directwebremoting.impl;

import java.io.IOException;
import java.io.StringWriter;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.directwebremoting.Container;
import org.directwebremoting.ScriptSession;
import org.directwebremoting.WebContext;
import org.directwebremoting.extend.RealScriptSession;
import org.directwebremoting.extend.RealWebContext;
import org.directwebremoting.extend.ScriptSessionManager;
import org.directwebremoting.util.SwallowingHttpServletResponse;

/**
 * A default implementation of WebContext.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class DefaultWebContext extends DefaultServerContext implements RealWebContext
{
    /**
     * Create a new DefaultWebContext
     * @param request The incoming http request
     * @param response The outgoing http reply
     * @param config The servlet configuration
     * @param context The servlet context
     * @param container The IoC container
     * @see org.directwebremoting.WebContextFactory.WebContextBuilder#set(HttpServletRequest, HttpServletResponse, ServletConfig, ServletContext, Container)
     */
    public DefaultWebContext(HttpServletRequest request, HttpServletResponse response, ServletConfig config, ServletContext context, Container container)
    {
        super(config, context, container);

        this.request = request;
        this.response = response;
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.extend.RealWebContext#checkPageInformation(java.lang.String, java.lang.String, boolean)
     */
    public void checkPageInformation(String sentPage, String sentScriptId, boolean checkScriptId)
    {
        if (checkScriptId)
        {
            ScriptSessionManager manager = getScriptSessionManager();
            RealScriptSession scriptSession = manager.getScriptSession(sentScriptId);
            if (scriptSession == null)
            {
                log.error("Invalid ScriptSessionId: " + sentScriptId);
                throw new SecurityException("Invalid ScriptSessionId");
            }

            String storedPage = scriptSession.getPage();
            if (!storedPage.equals(sentPage))
            {
                log.error("Invalid Page: Passed page=" + sentPage + ", but page in script session=" + storedPage);
                throw new SecurityException("Invalid Page");
            }
        }

        this.page = sentPage;
        this.scriptSessionId = sentScriptId;
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.WebContext#getCurrentPage()
     */
    public String getCurrentPage()
    {
        return page;
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.WebContext#getScriptSession()
     */
    public ScriptSession getScriptSession()
    {
        ScriptSessionManager manager = getScriptSessionManager();

        RealScriptSession scriptSession = manager.getScriptSession(scriptSessionId);
        manager.setPageForScriptSession(scriptSession, page);

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
        realRequest.setAttribute(WebContext.ATTRIBUTE_DWR, Boolean.TRUE);

        getServletContext().getRequestDispatcher(url).forward(realRequest, fakeResponse);

        return buffer.toString();
    }

    /**
     * The unique ID (like a session ID) assigned to the current page
     */
    private String scriptSessionId = null;

    /**
     * The URL of the current page
     */
    private String page = null;

    /**
     * The HttpServletRequest associated with the current request
     */
    private HttpServletRequest request = null;

    /**
     * The HttpServletResponse associated with the current request
     */
    private HttpServletResponse response = null;

    /**
     * The log stream
     */
    private static final Log log = LogFactory.getLog(DefaultWebContext.class);
}
