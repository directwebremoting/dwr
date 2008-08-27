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

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.directwebremoting.Container;
import org.directwebremoting.ServerContext;
import org.directwebremoting.WebContextFactory;
import org.directwebremoting.ServerContextFactory.ServerContextBuilder;

/**
 * A ServerContextBuilder that creates DefaultServerContexts.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class DefaultServerContextBuilder implements ServerContextBuilder
{
    /* (non-Javadoc)
     * @see org.directwebremoting.ServerContextFactory.ServerContextBuilder#get()
     */
    public ServerContext get()
    {
        // Try to get the context from the thread
        ServerContext serverContext = WebContextFactory.get();
        if (serverContext != null)
        {
            return serverContext;
        }

        // If not see if there is a singleton
        serverContext = StartupUtil.getSingletonServerContext();
        if (serverContext != null)
        {
            return serverContext;
        }

        log.fatal("Error initializing ServerContext because this is not a DWR thread and there is more than one DWR servlet in the current classloader.");
        log.fatal("This probably means that either DWR has not been properly initialized (in which case you should delay the current action until it has)");
        log.fatal("or that there is more than 1 DWR servlet is configured in this classloader, in which case you should provide a ServletContext to the get() yourself.");

        throw new IllegalStateException("No singleton ServerContext see logs for possible causes and solutions.");
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.ServerContextFactory.ServerContextBuilder#get(javax.servlet.ServletContext)
     */
    public ServerContext get(ServletContext servletContext)
    {
        if (servletContext == null)
        {
            throw new NullPointerException("servletContext");
        }

        ServerContext reply = (ServerContext) servletContext.getAttribute(DefaultServerContext.class.getName());
        if (reply == null)
        {
            log.warn("get(ServletContext) returns null when DWR has not been initialized in the given ServletContext");
        }

        return reply;
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.ServerContextFactory.ServerContextBuilder#set(javax.servlet.ServletContext, javax.servlet.ServletConfig, org.directwebremoting.Container)
     */
    public void set(ServletContext context, ServletConfig config, Container container)
    {
        try
        {
            ServerContext serverContext = new DefaultServerContext(config, context, container);
            context.setAttribute(DefaultServerContext.class.getName(), serverContext);
        }
        catch (Exception ex)
        {
            log.fatal("Failed to create an ExecutionContext", ex);
        }
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.extend.Builder#set(javax.servlet.ServletContext, java.lang.Object[])
     */
    public void set(ServletContext context, Object... constructorParameters)
    {
        set(context, (ServletConfig) constructorParameters[0], (Container) constructorParameters[1]);
    }

    /**
     * The log stream
     */
    private static final Log log = LogFactory.getLog(DefaultBuilder.class);
}
