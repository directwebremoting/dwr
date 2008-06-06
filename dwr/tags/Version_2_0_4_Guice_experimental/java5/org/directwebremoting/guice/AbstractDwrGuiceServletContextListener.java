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

import static org.directwebremoting.guice.DwrGuiceUtil.INJECTOR;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import static org.directwebremoting.guice.util.ContextCloseHandlers.newExceptionLoggingCloseableHandler;

import com.google.inject.Injector;


/**
 * Not for subclassing directly; this is a common base for two different approaches
 * to {@code Injector} creation and configuration, {@link DwrGuiceServletContextListener}
 * and {@link CustomInjectorServletContextListener}.
 * @author Tim Peierls [tim at peierls dot net]
 */
public abstract class AbstractDwrGuiceServletContextListener extends AbstractDwrModule implements ServletContextListener
{
    /* (non-Javadoc)
     * @see javax.servlet.ServletContextListener#contextInitialized(javax.servlet.ServletContextEvent)
     */
    public void contextInitialized(ServletContextEvent servletContextEvent)
    {
        final ServletContext servletContext = servletContextEvent.getServletContext();
        DwrGuiceUtil.withServletContext(servletContext, new Runnable()
        {
            public void run()
            {
                publishInjector(servletContext, createInjector());
            }
        });
    }

    /* (non-Javadoc)
     * @see javax.servlet.ServletContextListener#contextDestroyed(javax.servlet.ServletContextEvent)
     */
    public void contextDestroyed(ServletContextEvent servletContextEvent)
    {
        List<Exception> exceptions = new ArrayList<Exception>();

        DwrScopes.GLOBAL.closeAll(newExceptionLoggingCloseableHandler(exceptions));

        for (Exception e : exceptions)
        {
            log.warn("During context destroy, closing GLOBAL-scoped Closeables: " + e, e);
        }
    }


    /**
     * Abstract subclasses define either this method or {@link #configure},
     * but not both.
     */
    protected abstract Injector createInjector();

    /**
     * Abstract subclasses define either this method or {@link #createInjector},
     * but not both.
     */
    @Override
    protected abstract void configure();


    /**
     * Subclasses can use this during stage determination and binding to
     * read values from the current servlet context.
     */
    protected final ServletContext getServletContext()
    {
        return DwrGuiceUtil.getServletContext();
    }


    /**
     * Returns the Injector instance installed in the given ServletContext.
     * @param servletContext the servlet context from which to get the injector
     */
    protected static Injector getPublishedInjector(ServletContext servletContext)
    {
        Injector injector = (Injector) servletContext.getAttribute(INJECTOR);

        if (injector == null)
        {
            throw new IllegalStateException("Cannot find Injector in servlet context."
                + " You need to register a concrete extension of either "
                + DwrGuiceServletContextListener.class.getName()
                + " or "
                + CustomInjectorServletContextListener.class.getName()
                + " as a servlet context listener in your web.xml.");
        }

        return injector;
    }

    /**
     * Stores the Injector instance in the given ServletContext.
     * @param servletContext the servlet context in which to store the injector
     * @param injector the injector to store
     */
    protected static void publishInjector(ServletContext servletContext, Injector injector)
    {
        servletContext.setAttribute(INJECTOR, injector);
    }


    /**
     * The log stream
     */
    private static final Log log = LogFactory.getLog(AbstractDwrGuiceServletContextListener.class);
}
