/*
 * Copyright 2007 Tim Peierls
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.directwebremoting.guice.spring;

import com.google.inject.AbstractModule;
import com.google.inject.Provider;

import static org.directwebremoting.guice.DwrScopes.GLOBAL;

import org.springframework.beans.factory.BeanFactory;

/**
 * Ties {@code SpringIntegration.fromSpring} providers
 * to a BeanFactory whose provider is a {@link java.io.Closeable} in global
 * application scope, which means that it will be destroyed 
 * when the servlet context is destroyed.
 */
public class SpringModule extends AbstractModule 
{
    public SpringModule(BeanFactoryLoader loader)
    {
        this.provider = new CloseableBeanFactoryProvider(loader);
    }
    
    /* (non-Javadoc)
     * @see com.google.inject.AbstractModule#configure()
     */
    @Override
    protected void configure() 
    {
        bind(BeanFactory.class)
            .toProvider(CloseableBeanFactoryProvider.class)
            .asEagerSingleton();
 
        bind(CloseableBeanFactoryProvider.class)
            .toProvider(providerOfProvider)
            .in(GLOBAL);
    }
    
    protected final CloseableBeanFactoryProvider provider;
    
    private final Provider<CloseableBeanFactoryProvider> providerOfProvider = 
        new Provider<CloseableBeanFactoryProvider>()
        {
            public CloseableBeanFactoryProvider get()
            {
                return provider;
            }
        };
}
