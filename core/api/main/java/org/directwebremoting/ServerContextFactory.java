package org.directwebremoting;

import javax.servlet.ServletContext;

import org.directwebremoting.extend.Builder;
import org.directwebremoting.extend.Factory;

/**
 * Accessor for the current ServerContext.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class ServerContextFactory
{
    /**
     * Accessor for the current ServerContext.
     * @return The current ServerContext.
     */
    public static ServerContext get()
    {
        return factory.get();
    }

    /**
     * This method was designed to support more complex DWR setups, although
     * it is unlikely that it ever did this properly. If you have multiple
     * DWR servlets in a single ServletContext or wish to do cross-context
     * access to DWR then please contact the DWR mailing list.
     * @param ctx The servlet context to allow us to bootstrap
     * @return The current ServerContext.
     * @deprecated Use the plain ServerContextFactory.get() version
     * @see #get()
     */
    @Deprecated
    public static ServerContext get(ServletContext ctx)
    {
        return factory.get();
    }

    /**
     * Internal method to allow us to get the Builder from which we
     * will get ServerContext objects.
     * Do NOT call this method from outside of DWR.
     */
    public static ServerContext attach(Container container)
    {
        return factory.attach(container);
    }

    /**
     * The factory helper class
     */
    private static Factory<ServerContext> factory = Factory.create(ServerContextBuilder.class);

    /**
     * Hack to get around Generics not being implemented by erasure
     */
    public interface ServerContextBuilder extends Builder<ServerContext>
    {
    }
}
