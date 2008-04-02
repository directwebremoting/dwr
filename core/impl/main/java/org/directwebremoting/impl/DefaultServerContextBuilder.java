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

import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;
import org.directwebremoting.Container;
import org.directwebremoting.ServerContext;
import org.directwebremoting.ServerContextFactory.ServerContextBuilder;

/**
 * A ServerContextBuilder that creates DefaultServerContexts.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class DefaultServerContextBuilder implements ServerContextBuilder
{
    /* (non-Javadoc)
     * @see ServerContextBuilder#set(javax.servlet.ServletConfig, javax.servlet.ServletContext, org.directwebremoting.Container)
     */
    public void set(ServletConfig config, ServletContext context, Container container)
    {
        try
        {
            ServerContext ec = new DefaultServerContext(config, context, container);
            context.setAttribute(ATTRIBUTE_SERVER_CONTEXT, ec);
        }
        catch (Exception ex)
        {
            log.fatal("Failed to create an ExecutionContext", ex);
        }
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.ServerContextBuilder#get()
     */
    public ServerContext get(ServletContext context)
    {
        if (context == null)
        {
            throw new NullPointerException("context");
        }

        ServerContext reply = (ServerContext) context.getAttribute(ATTRIBUTE_SERVER_CONTEXT);
        if (reply == null)
        {
            log.warn("ServerContextFactory.get(ServletContext) returns null when DWR has not been initialized in the given ServletContext");
        }

        return reply;
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.ServerContextBuilder#get()
     */
    public ServerContext get()
    {
        ServerContext serverContext = ContainerUtil.getSingletonServerContext();
        if (serverContext == null)
        {
            log.fatal("Error initializing Hub because singleton ServerContext == null.");
            log.fatal("This probably means that either DWR has not been properly initialized (in which case you should delay the current action until it has)");
            log.fatal("or that there is more than 1 DWR servlet is configured in this classloader, in which case you should provide a ServletContext to the Hub yourself.");
            throw new IllegalStateException("No singleton ServerContext see logs for possible causes and solutions.");
        }

        return serverContext;
    }

    /**
     * The attribute under which we publish the ServerContext
     */
    private static final String ATTRIBUTE_SERVER_CONTEXT = "org.directwebremoting.impl.ServerContext";

    /**
     * The log stream
     */
    private static final Log log = LogFactory.getLog(DefaultServerContextBuilder.class);
}
