package org.directwebremoting;

import org.directwebremoting.extend.Builder;
import org.directwebremoting.extend.Factory;

/**
 * An accessor for the current Hub.
 * The nested {@link Builder} will only be of use to system implementors.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class HubFactory
{
    /**
     * Accessor for the current Hub.
     * @return The current Hub.
     */
    public static Hub get()
    {
        return factory.get();
    }

    /**
     * Accessor for the current Hub in more complex setups.
     * For some setups DWR may not be able to discover the correct environment
     * (i.e. ServerContext), so we need to tell it. This generally happens if
     * you have DWR configured twice in a single context. Unless you are writing
     * code that someone else will configure, it is probably safe to use the
     * simpler {@link #get()} method.
     * @param ctx The servlet context to allow us to bootstrap
     * @return The current Hub.
     */
    public static Hub get(ServerContext ctx)
    {
        return factory.get(ctx);
    }

    /**
     * Internal method to allow us to get the Builder from which we
     * will get Hub objects.
     * Do NOT call this method from outside of DWR.
     */
    public static Hub attach(Container container)
    {
        return factory.attach(container);
    }

    /**
     * The factory helper class
     */
    private static Factory<Hub> factory = Factory.create(HubBuilder.class);

    /**
     * Hack to get around Generics not being implemented by erasure
     */
    public interface HubBuilder extends Builder<Hub>
    {
    }
}
