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

import org.directwebremoting.Container;
import org.directwebremoting.ServerContext;
import org.directwebremoting.extend.ConverterManager;
import org.directwebremoting.extend.ScriptSessionManager;
import org.directwebremoting.util.VersionUtil;

/**
 * The Default implementation of ServerContext
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class DefaultServerContext implements ServerContext
{
    /**
     * Build a new DefaultServerContext
     * @param config The servlet configuration
     * @param context The servlet context
     * @param container The IoC container
     */
    public DefaultServerContext(ServletConfig config, ServletContext context, Container container)
    {
        this.config = config;
        this.context = context;
        this.container = container;
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.ServerContext#getAllScriptSessions()
     */
    public Collection getAllScriptSessions()
    {
        return getScriptSessionManager().getAllScriptSessions();
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.ServerContext#getContainer()
     */
    public Container getContainer()
    {
        return container;
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.ServerContext#getScriptSessionsByPage(java.lang.String)
     */
    public Collection getScriptSessionsByPage(String otherPage)
    {
        return getScriptSessionManager().getScriptSessionsByPage(otherPage);
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.ServerContext#getServletConfig()
     */
    public ServletConfig getServletConfig()
    {
        return config;
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.ServerContext#getServletContext()
     */
    public ServletContext getServletContext()
    {
        return context;
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.WebContext#getVersion()
     */
    public String getVersion()
    {
        return VersionUtil.getVersion();
    }

    /**
     * Internal helper for getting at a ScriptSessionManager
     * @return Our ScriptSessionManager
     */
    protected ScriptSessionManager getScriptSessionManager()
    {
        if (sessionManager == null)
        {
            sessionManager = (ScriptSessionManager) container.getBean(ScriptSessionManager.class.getName());
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
            converterManager = (ConverterManager) container.getBean(ConverterManager.class.getName());
        }

        return converterManager;
    }

    /**
     * The ServletConfig associated with the current request
     */
    private ServletConfig config = null;

    /**
     * The ServletContext associated with the current request
     */
    private ServletContext context = null;

    /**
     * The Ioc container implementation
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
}
