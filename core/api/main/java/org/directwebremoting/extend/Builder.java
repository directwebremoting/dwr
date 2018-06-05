package org.directwebremoting.extend;

import org.directwebremoting.Container;
import org.directwebremoting.ServerContext;
import org.directwebremoting.ServerContextFactory;

/**
 * Class to enable us to build 'singleton' interface implementations.
 * It is assumed that there is one 'singleton' per {@link ServerContext}.
 * Both this class and implementations of it are generally for use by DWR
 * developers. If you are a DWR user then unless you're doing something very
 * deep, you're probably digging too far.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public interface Builder<T>
{
    /**
     * Get the object that is associated with this thread, assuming that there
     * is no confusion over the current {@link ServerContext}.
     * There is more than one {@link ServerContext} then you need to use
     * {@link #get(ServerContext)} to ensure that the correct one is used.
     * @return The object associated with this DWR instance
     */
    T get();

    /**
     * Get the object that is associated with this thread, whilst specifying
     * the correct {@link ServerContext}.
     * @param context The web application environment
     * @return The object that is associated with this web application
     */
    T get(ServerContext context);

    /**
     * This method should be called during setup only.
     * This is a bit like {@link InitializingBean#afterContainerSetup}
     * except that it is called by {@link Factory#attach} which is
     * called after the container has finished setting itself up so we can
     * rely on {@link ServerContextFactory#get()} working.
     */
    T attach(Container container);
}
