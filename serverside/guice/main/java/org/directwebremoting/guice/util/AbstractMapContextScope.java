package org.directwebremoting.guice.util;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.google.inject.Key;

/**
 * A specialization of {@link AbstractContextScope} using a concurrent map
 * to hold registered instance providers. Concrete implementations must
 * define {@code get()} to return the current context, and they must call
 * {@code super(C.class)} in constructors.
 * @author Tim Peierls [tim at peierls dot net]
 */
public abstract class AbstractMapContextScope<C> extends AbstractContextScope<C, InstanceMap<?>>
{
    protected AbstractMapContextScope(Class<C> type, String scopeName)
    {
        super(type, scopeName);
    }

    @Override
    public abstract C get();

    //
    // ContextRegistry methods
    //

    /* (non-Javadoc)
     * @see org.directwebremoting.guice.ContextRegistry#registryFor(java.lang.Object)
     */
    public InstanceMap<?> registryFor(C context)
    {
        InstanceMap<?> instanceMap = map.get(context);

        if (instanceMap == null)
        {
            InstanceMap<?> emptyMap = new InstanceMapImpl<Object>();
            instanceMap = map.putIfAbsent(context, emptyMap);
            if (instanceMap == null)
            {
                instanceMap = emptyMap;
            }
        }

        return instanceMap;
    }

    public <T> InstanceProvider<T> get(InstanceMap<?> registry, Key<T> key, String keyString)
    {
        return castToT(registry, key).get(key);
    }

    public <T> InstanceProvider<T> putIfAbsent(InstanceMap<?> registry, Key<T> key, String keyString, InstanceProvider<T> creator)
    {
        return castToT(registry, key).putIfAbsent(key, creator);
    }

    public <T> boolean remove(InstanceMap<?> registry, Key<T> key, String keyString, InstanceProvider<T> creator)
    {
        return castToT(registry, key).remove(key, creator);
    }

    @SuppressWarnings("unused")
    private <T> InstanceMap<T> castToT(InstanceMap<?> instanceMap, Key<T> key)
    {
        @SuppressWarnings("unchecked")
        InstanceMap<T> result = (InstanceMap<T>) instanceMap;
        return result;
    }

    private final ConcurrentMap<C, InstanceMap<?>> map = new ConcurrentHashMap<C, InstanceMap<?>>();
}
