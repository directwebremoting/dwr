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
