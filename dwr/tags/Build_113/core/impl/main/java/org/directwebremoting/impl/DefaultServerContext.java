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

import java.util.Collection;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.directwebremoting.Container;
import org.directwebremoting.ScriptSession;
import org.directwebremoting.ServerContext;
import org.directwebremoting.extend.ConverterManager;
import org.directwebremoting.extend.ScriptSessionManager;
import org.directwebremoting.servlet.UrlProcessor;
import org.directwebremoting.util.VersionUtil;

/**
 * The Default implementation of ServerContext
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class DefaultServerContext implements ServerContext
{
    /* (non-Javadoc)
     * @see org.directwebremoting.ServerContext#getAllScriptSessions()
     */
    public Collection<ScriptSession> getAllScriptSessions()
    {
        return getScriptSessionManager().getAllScriptSessions();
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.ServerContext#getScriptSessionsByPage(java.lang.String)
     */
    public Collection<ScriptSession> getScriptSessionsByPage(String url)
    {
        return getScriptSessionManager().getScriptSessionsByPage(url);
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.ServerContext#getScriptSessionById(java.lang.String)
     */
    public ScriptSession getScriptSessionById(String sessionId)
    {
        // ScriptSessionManager().getScriptSession() can take a page and
        // httpSessionId so it can associate them, but we will have already done
        // that if we can as a result of work done creating a WebContext. For
        // use in a ServerContext we won't know, so we can pass in null
        return getScriptSessionManager().getScriptSession(sessionId, null, null);
    }

    /**
     * Injection point for Container
     */
    public void setContainer(Container container)
    {
        this.container = container;
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.ServerContext#getContainer()
     */
    public Container getContainer()
    {
        return container;
    }

    /**
     * Injection point for ServletConfig
     */
    public void setServletConfig(ServletConfig servletConfig)
    {
        this.servletConfig = servletConfig;
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.ServerContext#getServletConfig()
     */
    public ServletConfig getServletConfig()
    {
        return servletConfig;
    }

    /**
     * Injection point for ServletContext
     */
    public void setServletContext(ServletContext servletContext)
    {
        this.servletContext = servletContext;
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.ServerContext#getServletContext()
     */
    public ServletContext getServletContext()
    {
        return servletContext;
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.WebContext#getVersion()
     */
    public String getVersion()
    {
        return VersionUtil.getLabel();
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.ServerContext#getContextPath()
     */
    public String getContextPath()
    {
        UrlProcessor urlProcessor = container.getBean(UrlProcessor.class);
        return urlProcessor.getContextPath();
    }

    /**
     * Internal helper for getting at a ScriptSessionManager
     * @return Our ScriptSessionManager
     */
    protected ScriptSessionManager getScriptSessionManager()
    {
        if (sessionManager == null)
        {
            sessionManager = container.getBean(ScriptSessionManager.class);
        }

        return sessionManager;
    }

    /**
     * Internal helper for getting at a ConverterManager
     * @return Our ConverterManager
     */
    protected ConverterManager getConverterManager()
    {
        if (converterManager == null)
        {
            converterManager = container.getBean(ConverterManager.class);
        }

        return converterManager;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        return "DefaultServerContext[path=" + servletConfig.getServletName() + ", container=" + container.getClass().getSimpleName() + "]";
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj)
    {
        log.warn("Comparing ServerContexts is dangerous because DefaultWebContext inherits from DefaultServerContext. It is probably better to compare ServletContexts instead.");
        return super.equals(obj);
    }

    /**
     * The ServletConfig associated with the current request
     */
    private ServletConfig servletConfig = null;

    /**
     * The ServletContext associated with the current request
     */
    private ServletContext servletContext = null;

    /**
     * The IOC container implementation
     */
    private Container container = null;

    /**
     * The session manager for sessions keyed off script ids
     */
    private ScriptSessionManager sessionManager = null;

    /**
     * How we convert to Javascript objects
     */
    private ConverterManager converterManager = null;

    /**
     * The log stream
     */
    private static final Log log = LogFactory.getLog(DefaultServerContext.class);
}
