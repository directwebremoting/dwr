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


/**
 * Register a concrete subclass of this as a servlet context listener;
 * it uses {@link #createInjector} to obtain a custom {@code Injector}
 * and then stashes it in the servlet context.
 * @author Tim Peierls [tim at peierls dot net]
 */
public abstract class CustomInjectorServletContextListener extends AbstractDwrGuiceServletContextListener
{
    /**
     * Define to return a custom {@link Injector}.
     */
    @Override
    protected abstract Injector createInjector();

    /**
     * Does nothing, since this is not being used as a {@code Module}.
     */
    @Override
    protected final void configure()
    {
    }
}
