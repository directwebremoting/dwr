package org.directwebremoting.guice;

import org.directwebremoting.Container;
import org.directwebremoting.extend.Configurator;

import com.google.inject.Inject;

import static org.directwebremoting.guice.DwrGuiceUtil.*;

/**
 * Delegates to an injected configurator. This class only exists to provide an
 * publicly accessible named class with a parameterless constructor.
 * @author Tim Peierls [tim at peierls dot net]
 */
public class InternalConfigurator implements Configurator
{
    public InternalConfigurator()
    {
        getInjector().injectMembers(this);
    }

    public void configure(Container container)
    {
        configurator.configure(container);
    }

    @Inject private volatile Configurator configurator;
}
