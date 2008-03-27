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

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Class to enable us to access servlet parameters.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
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
    public Browser getBrowser()
    {
        throw new UnsupportedOperationException("Use WebContextFactory.get().getBrowser()"); //$NON-NLS-1$
    }

    /* (non-Javadoc)
     * @see uk.ltd.getahead.dwr.WebContext#getContainer()
     */
    public Container getContainer()
    {
        throw new UnsupportedOperationException("Use WebContextFactory.get().getContainer()"); //$NON-NLS-1$
    }

    /**
     * Accessor for the current ExecutionContext.
     * @return The current ExecutionContext
     */
    public static ExecutionContext get()
    {
        return new ExecutionContext(WebContextFactory.get());
    }

    /**
     * The real WebContext to proxy to
     */
    private WebContext proxy = null;
}
