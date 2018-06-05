package org.directwebremoting.impl;

import java.util.AbstractMap;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.directwebremoting.Container;

/**
 * A {@link Map} that uses a {@link Container} as it's source of data.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class ContainerMap extends AbstractMap<String, Object> implements Map<String, Object>
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
            proxy = new HashMap<String, Object>();
            for (String name : container.getBeanNames())
            {
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
    @Override
    public Object get(Object key)
    {
        init();
        return proxy.get(key);
    }

    /* (non-Javadoc)
     * @see java.util.AbstractMap#entrySet()
     */
    @Override
    public Set<Entry<String, Object>> entrySet()
    {
        init();
        return proxy.entrySet();
    }

    /* (non-Javadoc)
     * @see java.util.AbstractMap#containsKey(java.lang.Object)
     */
    @Override
    public boolean containsKey(Object key)
    {
        init();
        return proxy.containsKey(key);
    }

    /* (non-Javadoc)
     * @see java.util.AbstractMap#containsValue(java.lang.Object)
     */
    @Override
    public boolean containsValue(Object value)
    {
        init();
        return proxy.containsValue(value);
    }

    /* (non-Javadoc)
     * @see java.util.AbstractMap#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object o)
    {
        if (null == o)
        {
            return false;
        }
        if (this == o)
        {
            return true;
        }

        init();

        if (o.getClass() != this.getClass())
        {
            return false;
        }

        return proxy.equals(o);
    }

    /* (non-Javadoc)
     * @see java.util.AbstractMap#hashCode()
     */
    @Override
    public int hashCode()
    {
        init();
        return proxy.hashCode();
    }

    /* (non-Javadoc)
     * @see java.util.AbstractMap#isEmpty()
     */
    @Override
    public boolean isEmpty()
    {
        init();
        return proxy.isEmpty();
    }

    /* (non-Javadoc)
     * @see java.util.AbstractMap#keySet()
     */
    @Override
    public Set<String> keySet()
    {
        init();
        return proxy.keySet();
    }

    /* (non-Javadoc)
     * @see java.util.AbstractMap#size()
     */
    @Override
    public int size()
    {
        init();
        return proxy.size();
    }

    /* (non-Javadoc)
     * @see java.util.AbstractMap#values()
     */
    @Override
    public Collection<Object> values()
    {
        init();
        return proxy.values();
    }

    /* (non-Javadoc)
     * @see java.util.AbstractMap#toString()
     */
    @Override
    public String toString()
    {
        init();
        return proxy.toString();
    }

    /* (non-Javadoc)
     * @see java.util.AbstractMap#put(java.lang.Object, java.lang.Object)
     */
    @Override
    public Object put(String key, Object value)
    {
        throw new UnsupportedOperationException("ContainerMaps are read only");
    }

    /* (non-Javadoc)
     * @see java.util.AbstractMap#putAll(java.util.Map)
     */
    @Override
    public void putAll(Map<? extends String, ?> t)
    {
        throw new UnsupportedOperationException("ContainerMaps are read only");
    }

    /* (non-Javadoc)
     * @see java.util.AbstractMap#remove(java.lang.Object)
     */
    @Override
    public Object remove(Object key)
    {
        throw new UnsupportedOperationException("ContainerMaps are read only");
    }

    /* (non-Javadoc)
     * @see java.util.AbstractMap#clear()
     */
    @Override
    public void clear()
    {
        throw new UnsupportedOperationException("ContainerMaps are read only");
    }

    /**
     * Are we filtering non-string values from the container
     */
    private final boolean filterNonStringValues;

    /**
     * The Container that we proxy to.
     */
    private final Container container;

    /**
     * The cache of filtered values
     */
    protected Map<String, Object> proxy;
}
