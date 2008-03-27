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

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * A specialization of {@link AbstractContextScope} using a concurrent map
 * to hold registered instance providers. Concrete implementations must
 * define {@code get()} to return the current context, and they must call
 * {@code super(C.class)} in constructors.
 * @author Tim Peierls [tim at peierls dot net]
 */
public abstract class AbstractMapContextScope<C> 
    extends AbstractContextScope<C, ConcurrentMap>
{
    protected AbstractMapContextScope(Class<C> type, String scopeName)
    {
        super(type, scopeName);
    }
    
    public abstract C get();
    
    
    //
    // ContextRegistry methods
    //
    
    public ConcurrentMap registryFor(C context)
    {
        ConcurrentMap instanceMap = map.get(context);

        if (instanceMap == null) 
        {
            ConcurrentMap emptyMap = new ConcurrentHashMap();
            instanceMap = map.putIfAbsent(context, emptyMap);
            if (instanceMap == null) 
            {
                instanceMap = emptyMap;
            }
        }

        return instanceMap;
    }

    public <T> InstanceProvider<T> get(ConcurrentMap registry, Key<T> key, String keyString)
    {
        @SuppressWarnings("unchecked")
        ConcurrentMap<Key<T>, InstanceProvider<T>> instanceMap =
            (ConcurrentMap<Key<T>, InstanceProvider<T>>) registry;
        return instanceMap.get(key);
    }
    
    public <T> InstanceProvider<T> putIfAbsent(ConcurrentMap registry, 
                                               Key<T> key, String keyString, 
                                               InstanceProvider<T> creator)
    {                                               
        @SuppressWarnings("unchecked")
        ConcurrentMap<Key<T>, InstanceProvider<T>> instanceMap =
            (ConcurrentMap<Key<T>, InstanceProvider<T>>) registry;
        
        return instanceMap.putIfAbsent(key, creator);
    }
    
    public <T> boolean remove(ConcurrentMap registry, Key<T> key, String keyString, 
                              InstanceProvider<T> creator)
    {
        @SuppressWarnings("unchecked")
        ConcurrentMap<Key<T>, InstanceProvider<T>> instanceMap =
            (ConcurrentMap<Key<T>, InstanceProvider<T>>) registry;
        
        return instanceMap.remove(key, creator);
    }

    private final ConcurrentMap<C, ConcurrentMap> map =
          new ConcurrentHashMap<C, ConcurrentMap>();
}
