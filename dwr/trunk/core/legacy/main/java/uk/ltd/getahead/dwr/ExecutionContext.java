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
package uk.ltd.getahead.dwr;

import java.io.IOException;
import java.util.Collection;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.directwebremoting.Container;
import org.directwebremoting.ScriptSession;
import org.directwebremoting.WebContext;
import org.directwebremoting.WebContextFactory;

/**
 * Class to enable us to access servlet parameters.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 * @deprecated Use WebContext / WebContextFactory for better results
 */
@Deprecated
public class ExecutionContext implements WebContext
{
    /**
     * Create an ExecutionContext for compatibility purposes with a real
     * WebContext to proxy to.
     * @param proxy The WebContext to proxy to.
     */
    private ExecutionContext(WebContext proxy)
    {
        this.proxy = proxy;
    }

    /* (non-Javadoc)
     * @see uk.ltd.getahead.dwr.WebContext#getSession()
     */
    public HttpSession getSession()
    {
        return proxy.getSession();
    }

    /* (non-Javadoc)
     * @see uk.ltd.getahead.dwr.WebContext#getSession(boolean)
     */
    public HttpSession getSession(boolean create)
    {
        return proxy.getSession(create);
    }

    /* (non-Javadoc)
     * @see uk.ltd.getahead.dwr.WebContext#getServletConfig()
     */
    public ServletConfig getServletConfig()
    {
        return proxy.getServletConfig();
    }

    /* (non-Javadoc)
     * @see uk.ltd.getahead.dwr.WebContext#getServletContext()
     */
    public ServletContext getServletContext()
    {
        return proxy.getServletContext();
    }

    /* (non-Javadoc)
     * @see uk.ltd.getahead.dwr.WebContext#getHttpServletRequest()
     */
    public HttpServletRequest getHttpServletRequest()
    {
        return proxy.getHttpServletRequest();
    }

    /* (non-Javadoc)
     * @see uk.ltd.getahead.dwr.WebContext#getHttpServletResponse()
     */
    public HttpServletResponse getHttpServletResponse()
    {
        return proxy.getHttpServletResponse();
    }

    /* (non-Javadoc)
     * @see uk.ltd.getahead.dwr.WebContext#forwardToString(java.lang.String)
     */
    public String forwardToString(String url) throws ServletException, IOException
    {
        return proxy.forwardToString(url);
    }

    /* (non-Javadoc)
     * @see uk.ltd.getahead.dwr.WebContext#getVersion()
     */
    public String getVersion()
    {
        return proxy.getVersion();
    }

    /* (non-Javadoc)
     * @see uk.ltd.getahead.dwr.WebContext#getBrowser()
     */
    public ScriptSession getScriptSession()
    {
        throw new UnsupportedOperationException("Use WebContextFactory.get().getPage()");
    }

    /* (non-Javadoc)
     * @see uk.ltd.getahead.dwr.WebContext#getAllScriptSessions()
     */
    public Collection<ScriptSession> getAllScriptSessions()
    {
        throw new UnsupportedOperationException("Use WebContextFactory.get().getAllScriptSessions()");
    }

    /* (non-Javadoc)
     * @see uk.ltd.getahead.dwr.WebContext#getScriptSessionsByPage(java.lang.String)
     */
    public Collection<ScriptSession> getScriptSessionsByPage(String page)
    {
        throw new UnsupportedOperationException("Use WebContextFactory.get().getScriptSessionsByPage()");
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.ServerContext#getScriptSessionById(java.lang.String)
     */
    public ScriptSession getScriptSessionById(String sessionId)
    {
        throw new UnsupportedOperationException("Use WebContextFactory.get().getScriptSessionById()");
    }

    /* (non-Javadoc)
     * @see uk.ltd.getahead.dwr.WebContext#getContainer()
     */
    public Container getContainer()
    {
        throw new UnsupportedOperationException("Use WebContextFactory.get().getContainer()");
    }

    /* (non-Javadoc)
     * @see uk.ltd.getahead.dwr.WebContext#getCurrentPage()
     */
    public String getCurrentPage()
    {
        throw new UnsupportedOperationException("Use WebContextFactory.get().toJavascript()");
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.ServerContext#getContextPath()
     */
    public String getContextPath()
    {
        throw new UnsupportedOperationException("Use WebContextFactory.get().toJavascript()");
    }

    /**
     * Accessor for the current ExecutionContext.
     * @return The current ExecutionContext or null if the current thread was
     * not started by DWR.
     * @deprecated Use WebContextFactory.get() for better results
     */
    @Deprecated
    public static ExecutionContext get()
    {
        WebContext context = WebContextFactory.get();
        if (context == null)
        {
            return null;
        }

        return new ExecutionContext(context);
    }

    /**
     * The real WebContext to proxy to
     */
    private WebContext proxy = null;
}
