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

import java.lang.reflect.Constructor;

import javax.servlet.ServletContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.directwebremoting.ServerContext;
import org.directwebremoting.WebContextFactory;
import org.directwebremoting.extend.Builder;

/**
 * A Builder that creates DefaultHubs.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class DefaultBuilder<T> implements Builder<T>
{
    /**
     * This method calls created.getConstructor(constructorParameters) in order
     * to find a constructor which the Builder can use
     * @param created The type we wish to create
     * @param constructorParameters The parameter types needed by the chosen constructor
     */
    public DefaultBuilder(Class<? extends T> created, Class<?>...constructorParameters)
    {
        try
        {
            this.constructor = created.getConstructor(constructorParameters);
        }
        catch (Exception ex)
        {
            throw new RuntimeException(ex);
        }

        this.attributeName = created.getName();
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.Builder#get()
     */
    public T get()
    {
        // Try to get the context from the thread
        ServerContext serverContext = WebContextFactory.get();
        if (serverContext == null)
        {
            // If not see if there is a singleton
            serverContext = StartupUtil.getSingletonServerContext();
            if (serverContext == null)
            {
                log.fatal("Error initializing " + constructor.getDeclaringClass().getName() + " because singleton ServerContext == null.");
                log.fatal("This probably means that either DWR has not been properly initialized (in which case you should delay the current action until it has)");
                log.fatal("or that there is more than 1 DWR servlet is configured in this classloader, in which case you should provide a ServletContext to the get() yourself.");
                throw new IllegalStateException("No singleton ServerContext see logs for possible causes and solutions.");
            }
        }

        ServletContext servletContext = serverContext.getServletContext();
        @SuppressWarnings("unchecked")
        T reply = (T) servletContext.getAttribute(attributeName);
        if (reply == null)
        {
            log.warn("get() returns null when DWR has not been initialized in the given ServletContext");
        }

        return reply;
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.Builder#get()
     */
    public T get(ServletContext servletContext)
    {
        if (servletContext == null)
        {
            throw new NullPointerException("servletContext");
        }

        @SuppressWarnings("unchecked")
        T reply = (T) servletContext.getAttribute(attributeName);
        if (reply == null)
        {
            log.warn("get(ServletContext) returns null when DWR has not been initialized in the given ServletContext");
        }

        return reply;
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.extend.Builder#set(javax.servlet.ServletContext, java.lang.Object[])
     */
    public void set(ServletContext context, Object... constructorParameters)
    {
        try
        {
            T t = constructor.newInstance(constructorParameters);
            context.setAttribute(attributeName, t);
        }
        catch (Exception ex)
        {
            log.fatal("Failed to create an ExecutionContext", ex);
        }
    }

    /**
     * How we create objects of the given type
     */
    private final Constructor<? extends T> constructor;

    /**
     * The attribute under which we publish the T
     */
    private final String attributeName;

    /**
     * The log stream
     */
    private static final Log log = LogFactory.getLog(DefaultBuilder.class);
}
