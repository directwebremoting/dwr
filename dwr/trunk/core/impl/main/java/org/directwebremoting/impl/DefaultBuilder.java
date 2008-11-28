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

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.directwebremoting.Container;
import org.directwebremoting.ServerContext;
import org.directwebremoting.WebContextFactory;
import org.directwebremoting.extend.Builder;
import org.directwebremoting.extend.InitializingBean;

/**
 * A Builder that creates DefaultHubs.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class DefaultBuilder<T> implements Builder<T>, InitializingBean
{
    /**
     * This method calls created.getConstructor(constructorParameters) in order
     * to find a constructor which the Builder can use
     * @param created The type we wish to create
     */
    public DefaultBuilder(Class<? extends T> created)
    {
        this.created = created;
        this.attributeName = created.getName();
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.Builder#get()
     */
    public T get()
    {
        return get(getServerContext());
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.Builder#get()
     */
    @SuppressWarnings("unchecked")
    public T get(ServerContext serverContext)
    {
        // This might be a builder for ServletContexts in which case we're done
        if (ServerContext.class.isAssignableFrom(created))
        {
            return (T) serverContext;
        }

        Map<Class<?>, Object> contextObjects = lookup.get(serverContext);
        if (contextObjects == null)
        {
            throw new IllegalStateException("The passed ServerContext in unknown to the DefaultBuilder");
        }

        T t = (T) contextObjects.get(created);
        if (t == null)
        {
            throw new IllegalStateException("The DefaultBuilder knows nothing about objects of type " + created.getName());
        }

        return t;
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.extend.InitializingBean#afterContainerSetup(org.directwebremoting.Container)
     */
    public void afterContainerSetup(Container container)
    {
        try
        {
            T t = container.newInstance(created);
            // It might seem better to do getServerContext() at this point, but
            // since this is called during setup, that does not work, so we ask
            // the container for it instead.
            ServerContext serverContext = container.getBean(ServerContext.class);
            Map<Class<?>, Object> contextObjects = lookup.get(serverContext);
            if (contextObjects == null)
            {
                contextObjects = new HashMap<Class<?>, Object>();
                lookup.put(serverContext, contextObjects);
            }
            contextObjects.put(created, t);
        }
        catch (Exception ex)
        {
            log.fatal("Failed to create an ExecutionContext", ex);
        }
    }

    /**
     * Try to get the context from the thread, or from the singleton
     */
    private ServerContext getServerContext()
    {
        ServerContext serverContext = WebContextFactory.get();
        if (serverContext == null)
        {
            // If not see if there is a singleton
            serverContext = StartupUtil.getSingletonServerContext();
            if (serverContext == null)
            {
                log.fatal("Error initializing " + created.getName() + " because this is not a DWR thread and there is more than one DWR servlet in the current classloader.");
                log.fatal("This probably means that either DWR has not been properly initialized (in which case you should delay the current action until it has)");
                log.fatal("or that there is more than 1 DWR servlet is configured in this classloader, in which case you should provide a ServletContext to the get() yourself.");
                throw new IllegalStateException("No singleton ServerContext see logs for possible causes and solutions.");
            }
        }

        return serverContext;
    }

    /**
     * Our cache of created objects.
     */
    private static final Map<ServerContext, Map<Class<?>, Object>> lookup = new HashMap<ServerContext, Map<Class<?>, Object>>();

    /**
     * How we create objects of the given type
     */
    private final Class<? extends T> created;

    /**
     * The attribute under which we publish the T
     */
    protected final String attributeName;

    /**
     * The log stream
     */
    private static final Log log = LogFactory.getLog(DefaultBuilder.class);
}
