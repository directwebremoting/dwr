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

import java.util.prefs.Preferences;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Stage;

import org.directwebremoting.guice.util.AbstractModule;


/**
 * Register a concrete subclass of this as a servlet context listener to
 * configure an {@link Injector} with this as the only {@link com.google.inject.Module}
 * and stash it in the servlet context.
 * @author Tim Peierls [tim at peierls dot net]
 */
public abstract class DwrGuiceServletContextListener extends AbstractDwrGuiceServletContextListener
{
    /**
     * Creates an Injector built from this module, with the Stage value returned by {@link #getStage}.
     */
    @Override
    protected final Injector createInjector()
    {
        return Guice.createInjector(getStage(), new DwrScopeBinder(this));
    }

    /**
     * Define this method to configure bindings at servlet context initialization.
     * Call {@link AbstractModule#install AbstractModule.install(Module)} within
     * this method to use binding code from other modules.
     */
    @Override
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
     * Copies the Boolean that determines the behavior of {@link #bindDwrScopes()}
     * from another module and calls that method during configuration.
     */
    private static class DwrScopeBinder extends AbstractDwrModule
    {
        DwrScopeBinder(AbstractDwrModule module)
        {
            this.bindPotentiallyConflictingTypes = module.bindPotentiallyConflictingTypes;
            this.module = module;
        }

        @Override
        protected void configure()
        {
            bindDwrScopes();
            install(module);
        }

        final AbstractDwrModule module;
    }


    /** The name of the node to examine for a STAGE property. */
    private static final Class<?> PACKAGE = DwrGuiceServletContextListener.class;

    /** The node property to examine for a value for Stage. */
    private static final String STAGE_KEY = "stage";
}
