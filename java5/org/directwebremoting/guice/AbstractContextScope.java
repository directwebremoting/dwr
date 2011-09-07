/*
 * Copyright 2007 Tim Peierls
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.directwebremoting.guice;

import com.google.inject.Key;
import com.google.inject.Provider;
import com.google.inject.Scope;
import com.google.inject.util.ToStringBuilder;

import java.util.Collection;
import java.util.ArrayList;
import java.util.List;
import static java.util.Collections.synchronizedList;
import static java.util.Collections.unmodifiableList;

import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

import org.directwebremoting.util.Logger;
import static org.directwebremoting.guice.AbstractContextScope.State.*;

/**
 * Partial implementation of {@link ContextScope}. Concrete implementations
 * must pass the context identifier type to the super constructor and define 
 * {@code get()} to return the current context identifier (and to return null 
 * or throw an exception if there is no current context). They must also implement
 * the {@link ContextRegistry} interface.
 * @author Tim Peierls [tim at peierls dot net]
 */
public abstract class AbstractContextScope<C, R> 
    implements ContextScope<C>, ContextRegistry<C, R>
{        
    protected AbstractContextScope(Class<C> type, String scopeName) 
    {
        this.type = type;
        this.scopeName = scopeName;
    }
    
    public String toString()
    {
        return scopeName;
    }
    
    public List<Key<?>> getKeysInScope()
    {
        synchronized (scopedKeys)
        {
            return new ArrayList<Key<?>>(scopedKeys);
        }
    }
    
    public <T> Provider<T> scope(final Key<T> key, final Provider<T> creator) 
    {
        if (log.isDebugEnabled())
        {
            log.debug(String.format(
                "scope %s: adding key %s with creator %s", 
                scopeName, key, creator
            ));
        }
        
        scopedKeys.add(key);
        final String name = key.toString();
        return new Provider<T>() 
        {
            public T get() 
            {
                if (log.isDebugEnabled())
                {
                    log.debug(String.format(
                        "scope %s: getting key %s with creator %s", 
                        scopeName, key, creator
                    ));
                }
        
                C context = getContext(key);
                R registry = registryFor(context);
                InstanceProvider<T> future = 
                    AbstractContextScope.this.get(registry, key, name);
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


    public Class<C> type() 
    {
        return type;
    }
    
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
    
    public void closeAll(ContextCloseHandler<?>... closeHandlers)
    {
        for (C context : getOpenContexts())
        {
            close(context, closeHandlers);
        }
    }
    
    private <T> void handleClose(ContextCloseHandler<T> closeHandler, Object value)
    {
        Class<T> type = closeHandler.type();
        if (type.isInstance(value))
        {
            try
            {
                closeHandler.close(type.cast(value));
            }
            catch (Exception e)
            {
                // Ignore exceptions when closing,
                // the closeHandler should have taken
                // appropriate action before rethrowing.
            }
        }
    }
    
    private C getContext(Key<?> key)
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
    
    private final Class<C> type;
    
    private final String scopeName;
    
    /* @GuardedBy("self") */
    private final List<Key<?>> scopedKeys = synchronizedList(new ArrayList<Key<?>>());
    
    private final ConcurrentMap<C, State> contexts = new ConcurrentHashMap<C, State>();

    /**
     * The log stream
     */
    private static final Logger log = Logger.getLogger(AbstractContextScope.class);
}
