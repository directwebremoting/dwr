package org.directwebremoting.guice.spring;

import com.google.inject.AbstractModule;
import com.google.inject.Provider;

import static org.directwebremoting.guice.DwrScopes.GLOBAL;

import org.springframework.beans.factory.BeanFactory;

/**
 * Ties {@code SpringIntegration.fromSpring} providers
 * to a BeanFactory whose provider is a Closeable in global
 * application scope, which means that it will be destroyed 
 * when the servlet context is destroyed.
 */
public class SpringModule extends AbstractModule 
{
    public SpringModule(BeanFactoryLoader loader)
    {
        this.provider = new CloseableBeanFactoryProvider(loader);
    }
    
    protected void configure() 
    {
        bind(BeanFactory.class)
            .toProvider(CloseableBeanFactoryProvider.class)
            .asEagerSingleton();
 
        bind(CloseableBeanFactoryProvider.class)
            .toProvider(providerOfProvider)
            .in(GLOBAL);
    }
    
    private final CloseableBeanFactoryProvider provider;
    
    private final Provider<CloseableBeanFactoryProvider> providerOfProvider = 
        new Provider<CloseableBeanFactoryProvider>()
        {
            public CloseableBeanFactoryProvider get()
            {
                return provider;
            }
        };
}
