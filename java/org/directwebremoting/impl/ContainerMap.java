/*
 * Copyright 2005 Joe Walker
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.directwebremoting.impl;

import java.util.AbstractMap;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.directwebremoting.Container;

/**
 * A {@link Map} that uses a {@link Container} as it's source of data.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class ContainerMap extends AbstractMap implements Map
{
    /**
     * Create a ContainerMap with a Container to proxy requests to
     * @param container The Container that we proxy to
     * @param filterNonStringValues Does the map include created beans?
     */
    public ContainerMap(Container container, boolean filterNonStringValues)
    {
        this.container = container;
        this.filterNonStringValues = filterNonStringValues;
    }

    /**
     * Read the {@link Container} and cache the values
     */
    private void init()
    {
        if (proxy == null)
        {
            proxy = new HashMap();
            for (Iterator it = container.getBeanNames().iterator(); it.hasNext();)
            {
                String name = (String) it.next();
                Object value = container.getBean(name);

                if (!filterNonStringValues || value instanceof String)
                {
                    proxy.put(name, value);
                }
            }
        }
    }

    /* (non-Javadoc)
     * @see java.util.AbstractMap#get(java.lang.Object)
     */
    public Object get(Object key)
    {
        init();
        return proxy.get(key);
    }

    /* (non-Javadoc)
     * @see java.util.AbstractMap#entrySet()
     */
    public Set entrySet()
    {
        init();
        return proxy.entrySet();
    }

    /* (non-Javadoc)
     * @see java.util.AbstractMap#containsKey(java.lang.Object)
     */
    public boolean containsKey(Object key)
    {
        init();
        return proxy.containsKey(key);
    }

    /* (non-Javadoc)
     * @see java.util.AbstractMap#containsValue(java.lang.Object)
     */
    public boolean containsValue(Object value)
    {
        init();
        return proxy.containsValue(value);
    }

    /* (non-Javadoc)
     * @see java.util.AbstractMap#equals(java.lang.Object)
     */
    public boolean equals(Object o)
    {
        init();
        return proxy.equals(o);
    }

    /* (non-Javadoc)
     * @see java.util.AbstractMap#hashCode()
     */
    public int hashCode()
    {
        init();
        return proxy.hashCode();
    }

    /* (non-Javadoc)
     * @see java.util.AbstractMap#isEmpty()
     */
    public boolean isEmpty()
    {
        init();
        return proxy.isEmpty();
    }

    /* (non-Javadoc)
     * @see java.util.AbstractMap#keySet()
     */
    public Set keySet()
    {
        init();
        return proxy.keySet();
    }

    /* (non-Javadoc)
     * @see java.util.AbstractMap#size()
     */
    public int size()
    {
        init();
        return proxy.size();
    }

    /* (non-Javadoc)
     * @see java.util.AbstractMap#values()
     */
    public Collection values()
    {
        init();
        return proxy.values();
    }

    /* (non-Javadoc)
     * @see java.util.AbstractMap#toString()
     */
    public String toString()
    {
        init();
        return proxy.toString();
    }

    /* (non-Javadoc)
     * @see java.util.AbstractMap#put(java.lang.Object, java.lang.Object)
     */
    public Object put(Object key, Object value)
    {
        throw new UnsupportedOperationException("ContainerMaps are read only");
    }

    /* (non-Javadoc)
     * @see java.util.AbstractMap#putAll(java.util.Map)
     */
    public void putAll(Map t)
    {
        throw new UnsupportedOperationException("ContainerMaps are read only");
    }

    /* (non-Javadoc)
     * @see java.util.AbstractMap#remove(java.lang.Object)
     */
    public Object remove(Object key)
    {
        throw new UnsupportedOperationException("ContainerMaps are read only");
    }

    /* (non-Javadoc)
     * @see java.util.AbstractMap#clear()
     */
    public void clear()
    {
        throw new UnsupportedOperationException("ContainerMaps are read only");
    }

    /**
     * Are we filtering non-string values from the container
     */
    private boolean filterNonStringValues;

    /**
     * The Container that we proxy to.
     */
    private Container container;

    /**
     * The cache of filtered values
     */
    protected Map proxy;
}
