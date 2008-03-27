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

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.Module;
import com.google.inject.Stage;
import com.google.inject.TypeLiteral;
import static com.google.inject.name.Names.named;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.prefs.Preferences;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.directwebremoting.impl.DefaultContainer;
import org.directwebremoting.util.Logger;

import static org.directwebremoting.guice.DwrGuiceUtil.popServletContext;
import static org.directwebremoting.guice.DwrGuiceUtil.pushServletContext;

/**
 * Register a concrete subclass of this as a servlet context listener to
 * configure an {@link Injector} and stash it in the {@link ServletContext}.
 * @author Tim Peierls [tim at peierls dot net]
 */
public abstract class DwrGuiceServletContextListener
    extends AbstractDwrModule
    implements ServletContextListener
{
    public void contextInitialized(ServletContextEvent servletContextEvent)
    {
        ServletContext servletContext = servletContextEvent.getServletContext();
        pushServletContext(servletContext);
        try
        {
            Stage stage = getStage();
            Injector injector = Guice.createInjector(stage, this);
            publishInjector(servletContext, injector);
        }
        finally
        {
            popServletContext();
        }
    }
   
    public void contextDestroyed(ServletContextEvent servletContextEvent)
    {
        List<Exception> exceptions = new ArrayList<Exception>();
        DwrScopes.GLOBAL.closeAll(new ExceptionLoggingCloseableHandler(exceptions));
        for (Exception e : exceptions)
        {
            log.warn("During context destroy, closing globally-scoped Closeables: " + e, e);
        }
    }
    
    
    /**
     * Define this method to configure bindings at servlet context initialization.
     * Call {@link AbstractModule#install AbstractModule.install(Module)} within
     * this method to use binding code from other modules.
     */
    protected abstract void configure();

    /**
     * Override this method to specify which stage to run Guice in.
     * Default behavior is to look first in user preferences and then
     * in system preferences for node "org/directwebremoting/guice"
     * with a value for key "stage". If not found, the default is
     * Stage.PRODUCTION.
     */
    protected Stage getStage()
    {
        Stage stage = Stage.PRODUCTION;

        try
        {
            Preferences userNode = Preferences.userNodeForPackage(PACKAGE);
            String userStage = userNode.get(STAGE_KEY, null);
            if (userStage != null)
            {
                stage = Stage.valueOf(userStage);
            }
            else
            {
                Preferences systemNode = Preferences.systemNodeForPackage(PACKAGE);
                String systemStage = systemNode.get(STAGE_KEY, null);
                if (systemStage != null)
                {
                    stage = Stage.valueOf(systemStage);
                }
            }
        }
        catch (Exception e)
        {
            // ignore errors reading Preferences
        }

        return stage;
    }
    
    /**
     * Subclasses can use this during stage determination and binding to
     * read values from the current servlet context.
     */
    protected ServletContext getServletContext()
    {
        return DwrGuiceUtil.getServletContext();
    }


    /**
     * Returns the Injector instance installed in the given ServletContext.
     */
    static Injector getPublishedInjector(ServletContext servletContext)
    {
        Injector injector = (Injector) servletContext.getAttribute(INJECTOR);

        if (injector == null)
        {
            throw new IllegalStateException("Cannot find Injector in servlet context."
                + " You need to register a concrete extension of "
                + DwrGuiceServletContextListener.class.getName()
                + " as a servlet context listener in your web.xml.");
        }

        return injector;
    }

    static void publishInjector(ServletContext servletContext, Injector injector)
    {
        servletContext.setAttribute(INJECTOR, injector);
    }

    /**
     * The key under which a provided Injector is stashed in a ServletContext.
     * The name is prefixed by the package to avoid conflicting with other
     * listeners using the same technique.
     */
    private static final String INJECTOR =
        DwrGuiceServletContextListener.class.getPackage().getName() + ".Injector";

    /** The name of the node to examine for a STAGE property. */
    private static final Class<?> PACKAGE = DwrGuiceServletContextListener.class;

    /** The node property to examine for a value for Stage. */
    private static final String STAGE_KEY = "stage";

    /**
     * The log stream
     */
    private static final Logger log = Logger.getLogger(DwrGuiceServletContextListener.class);
}
