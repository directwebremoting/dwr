/*
 * Copyright 2007 Tim Peierls
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
package org.directwebremoting.guice;

import com.google.inject.Injector;

import java.util.LinkedList;

import javax.servlet.ServletContext;

import org.directwebremoting.WebContext;
import org.directwebremoting.WebContextFactory;
import static org.directwebremoting.guice.DwrGuiceServletContextListener.getPublishedInjector;


/**
 * Utilities for making Injector and ServletContext instances available.
 * @author Tim Peierls [tim at peierls dot net]
 */
class DwrGuiceUtil
{
    /**
     * Returns the Injector instance published in the current servlet context.
     */
    static Injector getInjector()
    {
        return getPublishedInjector(getServletContext());
    }

    /**
     * Gets the servlet context from the current web context, if one exists,
     * otherwise gets it from the thread-local stash.
     */
    static ServletContext getServletContext()
    {
        WebContext webcx = WebContextFactory.get();
        if (webcx != null)
        {
            return webcx.getServletContext();
        }
        else
        {
            return servletContexts.get().getFirst();
        }
    }
    
    /**
     * Thread-locally pushes a servlet context. Call {@link #popServletContext}
     * in a finally block when calling this method.
     */
    static void pushServletContext(ServletContext context)
    {
        servletContexts.get().addFirst(context);
    }
    
    /**
     * Pops a thread-locally stashed servlet context. Call this in
     * a finally block when {@link #pushServletContext} is called.
     */
    static void popServletContext()
    {
        servletContexts.get().removeFirst();
    }
    
    private static final ThreadLocal<LinkedList<ServletContext>> servletContexts = 
        new ThreadLocal<LinkedList<ServletContext>>()
        {
            protected LinkedList<ServletContext> initialValue()
            {
                return new LinkedList<ServletContext>();
            }
        };
}
