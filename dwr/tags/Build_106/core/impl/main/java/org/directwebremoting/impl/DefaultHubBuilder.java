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

import javax.servlet.ServletContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.directwebremoting.Hub;
import org.directwebremoting.ServerContext;
import org.directwebremoting.WebContextFactory;
import org.directwebremoting.HubFactory.HubBuilder;

/**
 * A HubBuilder that creates DefaultHubs.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class DefaultHubBuilder implements HubBuilder
{
    /* (non-Javadoc)
     * @see HubBuilder#set(javax.servlet.ServletContext)
     */
    public void set(ServletContext context)
    {
        try
        {
            Hub hub = new DefaultHub();
            context.setAttribute(ATTRIBUTE_SERVER_CONTEXT, hub);
        }
        catch (Exception ex)
        {
            log.fatal("Failed to create an ExecutionContext", ex);
        }
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.HubBuilder#get()
     */
    public Hub get()
    {
        ServerContext serverContext;

        // Try to get the context from the thread
        serverContext = WebContextFactory.get();
        if (serverContext == null)
        {
            // If not see if there is a singleton
            serverContext = StartupUtil.getSingletonServerContext();
            if (serverContext == null)
            {
                log.fatal("Error initializing Hub because singleton ServerContext == null.");
                log.fatal("This probably means that either DWR has not been properly initialized (in which case you should delay the current action until it has)");
                log.fatal("or that there is more than 1 DWR servlet is configured in this classloader, in which case you should provide a ServletContext to the Hub yourself.");
                throw new IllegalStateException("No singleton ServerContext see logs for possible causes and solutions.");
            }
        }

        ServletContext servletContext = serverContext.getServletContext();
        Hub reply = (Hub) servletContext.getAttribute(ATTRIBUTE_SERVER_CONTEXT);
        if (reply == null)
        {
            log.warn("HubFactory.get() returns null when DWR has not been initialized in the given ServletContext");
        }

        return reply;
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.HubBuilder#get()
     */
    public Hub get(ServletContext servletContext)
    {
        if (servletContext == null)
        {
            throw new NullPointerException("servletContext");
        }

        Hub reply = (Hub) servletContext.getAttribute(ATTRIBUTE_SERVER_CONTEXT);
        if (reply == null)
        {
            log.warn("HubFactory.get(ServletContext) returns null when DWR has not been initialized in the given ServletContext");
        }

        return reply;
    }

    /**
     * The attribute under which we publish the Hub
     */
    private static final String ATTRIBUTE_SERVER_CONTEXT = "org.directwebremoting.impl.Hub";

    /**
     * The log stream
     */
    private static final Log log = LogFactory.getLog(DefaultHubBuilder.class);
}
