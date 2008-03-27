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

import java.util.ArrayList;
import java.util.Collection;

/**
 * A specialization of {@link AbstractContextScope} for the case when
 * the context identifier itself can serve as a string-keyed instance registry
 * using synchronization on the context to provide atomic put-if-absent
 * and remove-specific-value behavior.
 * @author Tim Peierls [tim at peierls dot net]
 */
public abstract class AbstractSimpleContextScope<C> extends AbstractContextScope<C, C>
{
    protected AbstractSimpleContextScope(Class<C> type, String scopeName)
    {
        super(type, scopeName);
    }

    public abstract C get();
    
    
    //
    // These methods are restricted to String lookup of plain Objects.
    //

    public abstract Object get(C registry, String keyString);
    
    public abstract void put(C registry, String keyString, Object creator);
    
    public abstract Iterable<Object> asIterable(C registry);
    
    
    //
    // ContextRegistry methods
    //
    
    public C registryFor(C context)
    {
        return context;
    }

    @SuppressWarnings("unchecked")
    public <T> InstanceProvider<T> get(C registry, Key<T> key, String keyString)
    {
        return (InstanceProvider<T>) get(registry, keyString);
    }
    
    public <T> void put(C registry, Key<T> key, String keyString, InstanceProvider<T> creator)
    {
        put(registry, keyString, creator);
    }
    
    public <T> InstanceProvider<T> putIfAbsent(C registry, Key<T> key, String keyString, 
                                               InstanceProvider<T> creator)
    {
        synchronized (registry)
        {
            InstanceProvider<T> t = get(registry, key, keyString);
            if (t != null)
            {
                return t;
            }
            else
            {
                put(registry, key, keyString, creator);
                return null;
            }
        }
    }
    
    public <T> boolean remove(C registry, Key<T> key, String keyString, 
                              InstanceProvider<T> creator)
    {
        synchronized (registry)
        {
            InstanceProvider<T> t = get(registry, key, keyString);
            if (t == creator)
            {
                // Assumes put(..., null) is equivalent to remove(...)
                put(registry, keyString, null);
                return true;
            }
            else
            {
                return false;
            }
        }
    }
    
    @SuppressWarnings("unchecked")
    public Collection<InstanceProvider<?>> values(C registry)
    {
        Collection<InstanceProvider<?>> values = new ArrayList<InstanceProvider<?>>();
        for (Object object : asIterable(registry))
        {
            if (object instanceof InstanceProvider)
            {
                values.add((InstanceProvider<?>) object);
            }
        }
        return values;
    }
}
