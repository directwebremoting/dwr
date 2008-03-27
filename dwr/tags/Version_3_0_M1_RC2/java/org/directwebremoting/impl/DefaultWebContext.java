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
import org.directwebremoting.ScriptBuffer;
import org.directwebremoting.ScriptSession;
import org.directwebremoting.WebContext;
import org.directwebremoting.extend.EnginePrivate;
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
    public void checkPageInformation(String sentPage, String sentScriptId)
    {
        ScriptSessionManager scriptSessionManager = getScriptSessionManager();

        // Get the httpSessionId if it exists, but don't create one if it doesn't
        String httpSessionId = null;
        HttpSession httpSession = request.getSession(false);
        if (httpSession != null)
        {
            httpSessionId = httpSession.getId();
        }

        // Check validity to the script session id. It could be invalid due to
        // to a server re-start, a timeout, a back-button, just because the user
        // is new to this page, or because someone is hacking
        RealScriptSession scriptSession = scriptSessionManager.getScriptSession(sentScriptId, sentPage, httpSessionId);
        if (scriptSession == null)
        {
            // Force creation of a new script session
            scriptSession = scriptSessionManager.createScriptSession(sentPage, httpSessionId);
            String newSessionId = scriptSession.getId();

            // Inject a (new) script session id into the page
            ScriptBuffer handleNewSession = EnginePrivate.remoteHandleNewScriptSession(newSessionId);
            scriptSession.addScript(handleNewSession);

            // Use the new script session id not the one passed in
            log.debug("ScriptSession re-sync: " + sentScriptId + " has become " + newSessionId);
            this.scriptSessionId = newSessionId;
            this.page = sentPage;
        }
        else
        {
            // This could be called from a poll or an rpc call, so this is a
            // good place to update the session access time
            scriptSession.updateLastAccessedTime();

            String storedPage = scriptSession.getPage();
            if (!storedPage.equals(sentPage))
            {
                log.error("Invalid Page: Passed page=" + sentPage + ", but page in script session=" + storedPage);
                throw new SecurityException("Invalid Page");
            }

            // The passed script session id passed the test, use it
            this.scriptSessionId = sentScriptId;
            this.page = sentPage;
        }
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

        RealScriptSession scriptSession = manager.getScriptSession(scriptSessionId, null, null);
        if (scriptSession == null)
        {
            throw new SecurityException("Expected script session to exist");
        }

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
