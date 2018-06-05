package org.directwebremoting.guice.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.google.inject.Key;
import com.google.inject.Provider;
import com.google.inject.util.ToStringBuilder;

import static java.util.Collections.*;

import static org.directwebremoting.guice.util.AbstractContextScope.State.*;


/**
 * Partial implementation of {@link ContextScope}. Concrete implementations
 * must pass the context identifier type to the super constructor and define
 * {@code get()} to return the current context identifier (and to return null
 * or throw an exception if there is no current context). They must also implement
 * the {@link ContextRegistry} interface.
 * @author Tim Peierls [tim at peierls dot net]
 */
public abstract class AbstractContextScope<C, R> implements ContextScope<C>, ContextRegistry<C, R>
{
    protected AbstractContextScope(Class<C> type, String scopeName)
    {
        this.type = type;
        this.scopeName = scopeName;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        return scopeName;
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.guice.ContextScope#getKeysInScope()
     */
    public List<Key<?>> getKeysInScope()
    {
        synchronized (scopedKeys)
        {
            return new ArrayList<Key<?>>(scopedKeys);
        }
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.guice.ContextScope#scope(com.google.inject.Key, com.google.inject.Provider)
     */
    public <T> Provider<T> scope(final Key<T> key, final Provider<T> creator)
    {
        scopedKeys.add(key);
        final String name = key.toString();
        return new Provider<T>()
        {
            public T get()
            {
                C context = getContext(key);
                R registry = registryFor(context);
                InstanceProvider<T> future = AbstractContextScope.this.get(registry, key, name);
                if (future == null)
                {
                    InstanceProvider<T> futureTask = new FutureTaskProvider<T>(creator);
                    future = putIfAbsent(registry, key, name, futureTask);
                    if (future == null)
                    {
                        future = futureTask;
                        futureTask.run();
                        if (Thread.currentThread().isInterrupted())
                        {
                            remove(registry, key, name, futureTask);
                        }
                    }
                }
                return future.get();
            }

            @Override
            public String toString()
            {
                return new ToStringBuilder(this.getClass())
                    .add("scopeName", scopeName)
                    .add("type", type)
                    .add("key", key)
                    .add("creator", creator)
                    .toString();
            }
        };
    }

    public abstract C get();

    /* (non-Javadoc)
     * @see org.directwebremoting.guice.ContextScope#type()
     */
    public Class<C> type()
    {
        return type;
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.guice.ContextScope#getOpenContexts()
     */
    public Collection<C> getOpenContexts()
    {
        Collection<C> openContexts = new ArrayList<C>();
        for (C context : contexts.keySet())
        {
            if (contexts.get(context) == OPEN)
            {
                openContexts.add(context);
            }
        }
        return openContexts;
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.guice.ContextScope#close(java.lang.Object, org.directwebremoting.guice.ContextCloseHandler<?>[])
     */
    public void close(C context, ContextCloseHandler<?>... closeHandlers)
    {
        if (!contexts.replace(context, OPEN, CLOSED))
        {
            // Context hadn't been opened or was already closed.
            return;
        }

        for (InstanceProvider<?> provider : registeredProviders(registryFor(context)))
        {
            Object value = null;
            try
            {
                value = provider.get();
            }
            catch (RuntimeException e)
            {
                // Ignore runtime exceptions: they were thrown when
                // attempting creation and mean that no object was
                // created.
            }

            if (value == null)
            {
                // No instance was created by this provider, so we ignore.
                continue;
            }

            for (ContextCloseHandler<?> closeHandler : closeHandlers)
            {
                handleClose(closeHandler, value);
            }
        }
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.guice.ContextScope#closeAll(org.directwebremoting.guice.ContextCloseHandler<?>[])
     */
    public void closeAll(ContextCloseHandler<?>... closeHandlers)
    {
        for (C context : getOpenContexts())
        {
            close(context, closeHandlers);
        }
    }

    private <T> void handleClose(ContextCloseHandler<T> closeHandler, Object value)
    {
        Class<T> closeType = closeHandler.type();
        if (closeType.isInstance(value))
        {
            try
            {
                closeHandler.close(closeType.cast(value));
            }
            catch (Exception e)
            {
                // Ignore exceptions when closing,
                // the closeHandler should have taken
                // appropriate action before re-throwing.
            }
        }
    }

    protected C getContext(Key<?> key)
    {
        C context = null;
        RuntimeException caught = null;
        try
        {
            context = get();
            if (contexts.putIfAbsent(context, OPEN) == CLOSED)
            {
                // Context is closed.
                context = null;
            }
        }
        catch (RuntimeException ex)
        {
            caught = ex;
        }
        if (context == null)
        {
            throw new OutOfScopeException(this, key, caught);
        }

        return context;
    }

    private Collection<InstanceProvider<?>> registeredProviders(R registry)
    {
        List<InstanceProvider<?>> providers = new ArrayList<InstanceProvider<?>>();
        for (Key<?> key : getKeysInScope())
        {
            InstanceProvider<?> provider = get(registry, key, key.toString());
            if (provider != null)
            {
                providers.add(provider);
            }
        }
        return providers;
    }

    enum State
    {
        OPEN,
        CLOSED
    }

    protected final Class<C> type;

    protected final String scopeName;

    /* @GuardedBy("self") */
    private final List<Key<?>> scopedKeys = synchronizedList(new ArrayList<Key<?>>());

    private final ConcurrentMap<C, State> contexts = new ConcurrentHashMap<C, State>();
}
