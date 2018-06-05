package org.directwebremoting.extend;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.directwebremoting.Container;
import org.directwebremoting.ServerContext;

/**
 * Factory is not a user facing object, it is designed for system implementors.
 * Factory objects are generally use as helper classes by Factory classes
 * whose methods reflect the methods of Factory, but which are static and
 * proxy the call to the contained Factory instance.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class Factory<T>
{
    /**
     * Make it easy for Factories to create a Factory instance.
     */
    public static <T> Factory<T> create(Class<? extends Builder<T>> created)
    {
        return new Factory<T>(created);
    }

    /**
     * We need to know what type of builder to extract from the Container
     */
    public Factory(Class<? extends Builder<T>> created)
    {
        this.created = created;
    }

    /**
     * Accessor for the current object managed by this factory instance.
     */
    public T get()
    {
        Builder<T> b = this.builder;
        if (b == null)
        {
            log.warn("DWR has not been initialized properly");
            return null;
        }

        return b.get();
    }

    /**
     * Accessor for the current object in more complex setups.
     * For some setups DWR may not be able to discover the correct environment
     * (i.e. ServletContext), so we need to tell it. This generally happens if
     * you have DWR configured twice in a single context. Unless you are writing
     * code that someone else will configure, it is probably safe to use the
     * simpler {@link #get()} method.
     * @param ctx The servlet context to allow us to bootstrap
     * @return The current object accessed by this Factory.
     */
    public T get(ServerContext ctx)
    {
        Builder<T> b = this.builder;
        if (b == null)
        {
            log.warn("DWR has not been initialized properly");
            return null;
        }

        return b.get(ctx);
    }

    /**
     * Internal method to allow us to get the Builder from which we will get
     * created objects. Do NOT call this method from outside of DWR.
     * This function should <em>only</em> be called during startup.
     * our Container Builder from.
     */
    public T attach(Container container)
    {
        this.builder = container.getBean(created);
        return builder.attach(container);
    }

    /**
     * The type of builder that we get out of the container
     */
    private final Class<? extends Builder<T>> created;

    /**
     * The Builder from which we will get created objects
     */
    private volatile Builder<T> builder = null;

    /**
     * The log stream
     */
    private static final Log log = LogFactory.getLog(Factory.class);
}
