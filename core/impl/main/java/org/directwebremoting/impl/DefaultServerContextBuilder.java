package org.directwebremoting.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.directwebremoting.Container;
import org.directwebremoting.ServerContext;
import org.directwebremoting.WebContextFactory;
import org.directwebremoting.ServerContextFactory.ServerContextBuilder;

/**
 * A ServerContextBuilder that creates DefaultServerContexts.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class DefaultServerContextBuilder implements ServerContextBuilder
{
    /* (non-Javadoc)
     * @see org.directwebremoting.extend.Builder#get()
     */
    public ServerContext get()
    {
        ServerContext serverContext = WebContextFactory.get();
        if (serverContext == null)
        {
            // If not see if there is a singleton
            serverContext = StartupUtil.getSingletonServerContext();
            if (serverContext == null)
            {
                log.fatal("Error initializing ServerContext because this is not a DWR thread and there is more than one DWR servlet in the current classloader.");
                log.fatal("This probably means that either DWR has not been properly initialized (in which case you should delay the current action until it has)");
                log.fatal("or that there is more than 1 DWR servlet is configured in this classloader, in which case you should provide a ServletContext to the get() yourself.");
                throw new IllegalStateException("No singleton ServerContext see logs for possible causes and solutions.");
            }
        }

        return serverContext;
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.extend.Builder#get(org.directwebremoting.ServerContext)
     */
    public ServerContext get(ServerContext context)
    {
        return context;
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.extend.Builder#attach(org.directwebremoting.ServerContext)
     */
    public ServerContext attach(Container container)
    {
        try
        {
            return container.newInstance(DefaultServerContext.class);
        }
        catch (RuntimeException ex)
        {
            throw ex;
        }
        catch (Exception ex)
        {
            throw new RuntimeException(ex);
        }
    }

    /**
     * The log stream
     */
    private static final Log log = LogFactory.getLog(DefaultServerContextBuilder.class);
}
