package org.directwebremoting.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.directwebremoting.Container;
import org.directwebremoting.ServerContext;
import org.directwebremoting.ServerContextFactory;
import org.directwebremoting.extend.Builder;

/**
 * A Builder that creates DefaultHubs.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class DefaultBuilder<T> implements Builder<T>
{
    /**
     * This method calls created.getConstructor(constructorParameters) in order
     * to find a constructor which the Builder can use
     * @param created The type we wish to create
     */
    public DefaultBuilder(Class<? extends T> created)
    {
        this.created = created;
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.Builder#get()
     */
    public T get()
    {
        ServerContext serverContext = ServerContextFactory.get();
        return get(serverContext);
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.Builder#get()
     */
    @SuppressWarnings("unchecked")
    public T get(ServerContext serverContext)
    {
        Container container = serverContext.getContainer();
        Map<Class<?>, Object> contextObjects = lookup.get(container);
        if (contextObjects == null)
        {
            throw new IllegalStateException("The passed ServerContext in unknown to the DefaultBuilder");
        }

        T t = (T) contextObjects.get(created);
        if (t == null)
        {
            throw new IllegalStateException("The DefaultBuilder knows nothing about objects of type " + created.getName());
        }

        return t;
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.extend.Builder#attach(org.directwebremoting.Container)
     */
    public T attach(Container container)
    {
        try
        {
            T t = container.newInstance(created);
            Map<Class<?>, Object> contextObjects = lookup.get(container);
            if (contextObjects == null)
            {
                contextObjects = new HashMap<Class<?>, Object>();
                lookup.put(container, contextObjects);
            }
            contextObjects.put(created, t);
            return t;
        }
        catch (Exception ex)
        {
            log.warn("Failed to create an instance of " + created.getName());
            throw new RuntimeException(ex);
        }
    }

    /**
     * Our cache of created objects.
     */
    private static final Map<Container, Map<Class<?>, Object>> lookup = new HashMap<Container, Map<Class<?>, Object>>();

    /**
     * How we create objects of the given type
     */
    private final Class<? extends T> created;

    /**
     * The log stream
     */
    private static final Log log = LogFactory.getLog(DefaultBuilder.class);
}
