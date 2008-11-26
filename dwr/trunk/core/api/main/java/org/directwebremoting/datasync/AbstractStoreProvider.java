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
package org.directwebremoting.datasync;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.directwebremoting.ServerContextFactory;
import org.directwebremoting.extend.ConverterManager;
import org.directwebremoting.extend.InboundVariable;
import org.directwebremoting.extend.RealRawData;
import org.directwebremoting.io.Item;
import org.directwebremoting.io.MatchedItems;
import org.directwebremoting.io.RawData;
import org.directwebremoting.io.StoreChangeListener;
import org.directwebremoting.util.LocalUtil;
import org.directwebremoting.util.Pair;

/**
 * Some methods to help implementors create {@link StoreProvider}s. It is
 * strongly recommended that all implementors of {@link StoreProvider} inherit
 * from this class in case it can provide some form of backwards compatibility.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public abstract class AbstractStoreProvider<T> implements StoreProvider<T>
{
    public AbstractStoreProvider(Class<T> type)
    {
        this.type = type;
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.datasync.StoreProvider#put(java.lang.String, org.directwebremoting.io.RawData)
     */
    public void put(String itemId, RawData rawData)
    {
        T value = convert(rawData);
        put(itemId, value);
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.datasync.StoreProvider#get(java.lang.String)
     */
    public Item viewItem(String itemId)
    {
        T object = getObject(itemId);
        return new Item(itemId, object);
    }

    /**
     * Get an object from the store without an Item wrapper.
     * @param itemId The ID of the item to fetch
     * @return The item, or null if one was not found.
     */
    protected abstract T getObject(String itemId);

    /**
     * Return true iff the <code>value</code> passed in contains a property
     * by the name of each and every key in the <code>query</code>, and where
     * the string value (using {@link #toString()}) of the property is equal to
     * the value from the <code>filter</code> map.
     * @param pojo The object to be tested to see if it matches
     * @param query The set of property/matches to test the value against
     * @return True if the value contains properties that match the filter
     */
    protected static boolean passesFilter(Object pojo, Map<String, String> query)
    {
        if (query == null || query.isEmpty())
        {
            return true;
        }

        try
        {
            for (Map.Entry<String, String> entry : query.entrySet())
            {
                String testProperty = entry.getKey();
                String testValue = entry.getValue();

                String realValue = LocalUtil.getProperty(pojo, testProperty, String.class);
                if (!testValue.equals(realValue))
                {
                    return false;
                }
            }
        }
        catch (Exception ex)
        {
            log.warn("Failed to introspect: " + pojo.getClass() + ". Filters will fail.");
            return false;
        }

        return true;
    }

    /**
     * Chop out the part we are interested in and return the data converted to
     * a MatchedItems data set destined for the web.
     * @param sortedData The complete set of data we're extracting from
     * @param start The initial index to start from.
     * @param count The number of data items to return (-1 means, to the end)
     * @return Data for the web
     */
    protected static <U> MatchedItems selectMatchedItems(SortedSet<Pair<String, U>> sortedData, int start, int count)
    {
        log.debug("Selecting data from " + sortedData.size() + " items, starting at " + start + " for " + count + " items.");

        List<Item> matches = new ArrayList<Item>();
        int i = 0;
        for (Pair<String, U> pair : sortedData)
        {
            if (i >= (start + count) && count != -1)
            {
                break;
            }

            if (i >= start)
            {
                matches.add(new Item(pair.left, pair.right));
            }

            i++;
        }

        return new MatchedItems(matches, sortedData.size());
    }

    /**
     * Convert from {@link RawData} to the type that this {@link StoreProvider}
     * supports.
     * @param rawData The data from the Internet
     * @return An object of the type supported by this store
     */
    protected T convert(RawData rawData)
    {
        if (rawData == null)
        {
            return null;
        }

        if (converterManager == null)
        {
            converterManager = ServerContextFactory.get().getContainer().getBean(ConverterManager.class);
        }

        RealRawData realRawData = (RealRawData) rawData;
        InboundVariable inboundVariable = realRawData.getInboundVariable();
        T value = converterManager.convertInbound(type, inboundVariable, null);
        return value;
    }

    /**
     * Convert from {@link RawData} to the type that this {@link StoreProvider}
     * supports.
     * @param rawData The data from the Internet
     * @return An object of the type supported by this store
     */
    protected Object convert(RawData rawData, Class<?> toType)
    {
        if (rawData == null)
        {
            return null;
        }

        if (converterManager == null)
        {
            converterManager = ServerContextFactory.get().getContainer().getBean(ConverterManager.class);
        }

        RealRawData realRawData = (RealRawData) rawData;
        InboundVariable inboundVariable = realRawData.getInboundVariable();
        Object value = converterManager.convertInbound(toType, inboundVariable, null);
        return value;
    }

    /**
     *
     * @param listener
     * @param itemIds
     */
    protected synchronized void setWatchedSet(StoreChangeListener<T> listener, Collection<String> itemIds)
    {
        clearWatchedSet(listener);
        addWatchedSet(listener, itemIds);
    }

    /**
     *
     * @param listener
     * @param itemIds
     */
    protected synchronized void addWatchedSet(StoreChangeListener<T> listener, Collection<String> itemIds)
    {
        if (itemIds == null || itemIds.isEmpty())
        {
            return;
        }

        watched.put(listener, itemIds);

        for (String itemId : itemIds)
        {
            Collection<StoreChangeListener<T>> listeners = watchers.get(itemId);
            if (listeners == null)
            {
                listeners = new HashSet<StoreChangeListener<T>>();
                watchers.put(itemId, listeners);
            }
            listeners.add(listener);
        }
    }

    /**
     * Unsubscribe from anything the listener was previously subscribed to
     * @param listener
     */
    protected synchronized void clearWatchedSet(StoreChangeListener<T> listener)
    {
        Collection<String> itemIds = watched.get(listener);
        if (itemIds != null)
        {
            for (String itemId : itemIds)
            {
                Collection<StoreChangeListener<T>> listeners = watchers.get(itemId);
                if (listeners != null)
                {
                    listeners.remove(listener);
                    if (listeners.isEmpty())
                    {
                        watchers.remove(itemId);
                    }
                }
            }

            watched.remove(listener);
        }
    }

    /**
     * Cause a listener update
     */
    protected synchronized void fireItemChanged(Item item, Collection<String> attributes)
    {
        Collection<StoreChangeListener<T>> listeners = watchers.get(item.getItemId());
        if (listeners != null)
        {
            log.debug("Firing item changed to " + listeners.size() + " listeners. Changed: " + item);

            for (StoreChangeListener<T> listener : listeners)
            {
                listener.itemChanged(this, item, attributes);
            }
        }
    }

    /**
     * Cause a listener update
     */
    protected synchronized void fireItemAdded(Item item)
    {
        Collection<StoreChangeListener<T>> listeners = watchers.get(item.getItemId());
        if (listeners != null)
        {
            log.debug("Firing item added to " + listeners.size() + " listeners. Changed: " + item);

            for (StoreChangeListener<T> listener : listeners)
            {
                listener.itemAdded(this, item);
            }
        }
    }

    /**
     * Cause a listener update
     */
    protected synchronized void fireItemRemoved(String itemId)
    {
        Collection<StoreChangeListener<T>> listeners = watchers.get(itemId);
        if (listeners != null)
        {
            log.debug("Firing item added to " + listeners.size() + " listeners. Changed: " + itemId);

            for (StoreChangeListener<T> listener : listeners)
            {
                listener.itemRemoved(this, itemId);
            }
        }
    }

    /**
     * A PairComparator is a way to proxy comparisons to the 'value' of a
     * String, Object paring.
     * @author Joe Walker [joe at getahead dot ltd dot uk]
     */
    protected static class PairComparator<T> implements Comparator<Pair<String, T>>
    {
        /**
         * @param proxy The value object comparator
         */
        protected PairComparator(Comparator<T> proxy)
        {
            this.proxy = proxy;
        }

        /* (non-Javadoc)
         * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
         */
        public int compare(Pair<String, T> p1, Pair<String, T> p2)
        {
            return proxy.compare(p1.right, p2.right);
        }

        /**
         * The comparator that we proxy to
         */
        private final Comparator<T> proxy;
    }

    /**
     * A map of the itemIds in this store along with who is watching them.
     * @protectedBy(this)
     */
    private final Map<String, Collection<StoreChangeListener<T>>> watchers = new HashMap<String, Collection<StoreChangeListener<T>>>();

    /**
     * A map of the itemIds in this store along with who is watching them.
     * @protectedBy(this)
     */
    private final Map<StoreChangeListener<T>, Collection<String>> watched = new HashMap<StoreChangeListener<T>, Collection<String>>();

    /**
     * The type that this StoreProvider uses
     */
    protected final Class<T> type;

    /**
     * Cached converterManager so we don't look it up every time
     */
    protected ConverterManager converterManager;

    /**
     * The log stream
     */
    private static final Log log = LogFactory.getLog(AbstractStoreProvider.class);
}
