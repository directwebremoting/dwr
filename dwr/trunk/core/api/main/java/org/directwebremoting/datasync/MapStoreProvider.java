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

import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.directwebremoting.io.Item;
import org.directwebremoting.io.ItemUpdate;
import org.directwebremoting.io.MatchedItems;
import org.directwebremoting.io.SortCriterion;
import org.directwebremoting.io.StoreChangeListener;
import org.directwebremoting.io.StoreRegion;
import org.directwebremoting.util.LocalUtil;
import org.directwebremoting.util.Pair;

/**
 * A simple implementation of StoreProvider that uses a Map<String, ?>.
 * TODO: Allow access to a (wrapper implementation) of the Map to allow its use
 * outside this class.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class MapStoreProvider<T> extends AbstractStoreProvider<T> implements StoreProvider<T>
{
    /**
     * Initialize the MapStoreProvider from an existing map + specified type.
     */
    public MapStoreProvider(Map<String, T> datamap, Class<T> type)
    {
        this(datamap, type, new ArrayList<SortCriterion>());
    }

    /**
     * Initialize an empty MapStoreProvider from the specified type.
     */
    public MapStoreProvider(Class<T> type)
    {
        this(new HashMap<String, T>(), type, new ArrayList<SortCriterion>());
    }

    /**
     * Initialize the MapStoreProvider from an existing map + specified type
     * along with some sort criteria to be used when the client does not specify
     * sorting.
     */
    public MapStoreProvider(Map<String, T> map, Class<T> type, List<SortCriterion> defaultCriteria)
    {
        super(type);
        this.baseRegion = new StoreRegion(0, -1, defaultCriteria, null);

        Index index = new Index(baseRegion, map);
        data.put(baseRegion, index);
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.datasync.StoreProvider#put(java.lang.String, java.lang.Object)
     */
    public synchronized void put(String itemId, T value)
    {
        for (Index index : data.values())
        {
            index.put(itemId, value);
        }
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.datasync.AbstractStoreProvider#getObject(java.lang.String)
     */
    @Override
    protected synchronized T getObject(String itemId)
    {
        return data.get(baseRegion).index.get(itemId);
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.datasync.StoreProvider#view(org.directwebremoting.datasync.StoreRegion)
     */
    public synchronized MatchedItems view(StoreRegion region)
    {
        Index index = getIndex(region);
        return selectMatchedItems(index.getSortedData(), region.getStart(), region.getCount());
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.datasync.StoreProvider#subscribe(org.directwebremoting.datasync.StoreRegion, org.directwebremoting.datasync.StoreChangeListener)
     */
    public synchronized MatchedItems subscribe(StoreRegion region, StoreChangeListener<T> li)
    {
        Index index = getIndex(region);
        MatchedItems matchedItems = selectMatchedItems(index.getSortedData(), region.getStart(), region.getCount());
        Collection<String> itemIds = new HashSet<String>();
        for (Item item : matchedItems.getViewedMatches())
        {
            itemIds.add(item.getItemId());
        }
        setWatchedSet(li, itemIds);
        return matchedItems;
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.datasync.StoreProvider#unsubscribe(org.directwebremoting.datasync.StoreChangeListener)
     */
    public synchronized void unsubscribe(StoreChangeListener<T> li)
    {
        setWatchedSet(li, null);
    }

    /**
     * Get an Index from a StoreRegion by defaulting the sort criteria if
     * needed, and by creating a new index if needed.
     * @param region The region to be viewed (we ignore start/end)
     * @return An index that we can use to get a sorted data cache
     */
    protected synchronized Index getIndex(StoreRegion region)
    {
        if (region == null)
        {
            region = baseRegion;
        }

        if (region.getSort() == null)
        {
            // we need to change to the default sorting
            region = new StoreRegion(region.getStart(), region.getCount(), baseRegion.getSort(), region.getQuery());
        }

        Index index = data.get(region);

        if (index == null)
        {
            // So there is no index that looks like we want
            Index original = data.get(baseRegion);

            index = new Index(region, original);
            data.put(region, index);
        }

        return index;
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.datasync.StoreProvider#update(java.util.List)
     */
    public synchronized void update(List<ItemUpdate> changes) throws SecurityException
    {
        // First off group the changes by ID so we can fire updates together
        Map<String, List<ItemUpdate>> groupedChanges = new HashMap<String, List<ItemUpdate>>();
        for (ItemUpdate itemUpdate : changes)
        {
            List<ItemUpdate> itemChanges = groupedChanges.get(itemUpdate.getItemId());
            if (itemChanges == null)
            {
                itemChanges = new ArrayList<ItemUpdate>();
                groupedChanges.put(itemUpdate.getItemId(), itemChanges);
            }
            itemChanges.add(itemUpdate);
        }

        // Make the changes to each item in one go
        for (Map.Entry<String, List<ItemUpdate>> entry : groupedChanges.entrySet())
        {
            T t = getObject(entry.getKey());
            Collection<String> changedAttributes = new HashSet<String>();

            for (ItemUpdate itemUpdate : changes)
            {
                String attribute = itemUpdate.getAttribute();
                Class<?> convertTo = LocalUtil.getPropertyType(t.getClass(), attribute);
                Object value = convert(itemUpdate.getNewValue(), convertTo);

                try
                {
                    LocalUtil.setProperty(t, attribute, value);
                    changedAttributes.add(attribute);
                }
                catch (SecurityException ex)
                {
                    throw ex;
                }
                catch (Exception ex)
                {
                    throw new SecurityException(ex);
                }
            }

            Item item = new Item(entry.getKey(), t);
            fireItemChanged(item, changedAttributes);
        }
    }

    /**
     * Get access to the contained data as a {@link Map}.
     * TODO: This is probably very inefficient - optimize
     * @return An implementation of Map that affect this {@link StoreProvider}
     */
    public synchronized Map<String, T> asMap()
    {
        final Index original = data.get(baseRegion);

        return new AbstractMap<String, T>()
        {
            /* (non-Javadoc)
             * @see java.util.AbstractMap#put(K, V)
             */
            @Override
            public T put(String itemId, T value)
            {
                T old = getObject(itemId);
                MapStoreProvider.this.put(itemId, value);
                return old;
            }

            /* (non-Javadoc)
             * @see java.util.AbstractMap#remove(java.lang.Object)
             */
            @Override
            public T remove(Object itemId)
            {
                T old = MapStoreProvider.this.getObject((String) itemId);
                MapStoreProvider.this.put((String) itemId, (T) null);
                return old;
            }

            /* (non-Javadoc)
             * @see java.util.AbstractMap#entrySet()
             */
            @Override
            public Set<Entry<String, T>> entrySet()
            {
                return new AbstractSet<Entry<String, T>>()
                {
                    /* (non-Javadoc)
                     * @see java.util.AbstractCollection#iterator()
                     */
                    @Override
                    public Iterator<Entry<String, T>> iterator()
                    {
                        return original.index.entrySet().iterator();
                    }

                    /* (non-Javadoc)
                     * @see java.util.AbstractCollection#size()
                     */
                    @Override
                    public int size()
                    {
                        return original.sortedData.size();
                    }

                    /* (non-Javadoc)
                     * @see java.util.AbstractCollection#add(java.lang.Object)
                     */
                    @Override
                    public boolean add(Entry<String, T> entry)
                    {
                        T t = getObject(entry.getKey());
                        MapStoreProvider.this.put(entry.getKey(), entry.getValue());
                        return t != null;
                    }

                    /* (non-Javadoc)
                     * @see java.util.AbstractCollection#remove(java.lang.Object)
                     */
                    @Override
                    public boolean remove(Object o)
                    {
                        @SuppressWarnings("unchecked")
                        Entry<String, T> entry = (Entry<String, T>) o;
                        T old = getObject(entry.getKey());
                        MapStoreProvider.this.put(entry.getKey(), (T) null);
                        return old != null;
                    }
                };
            }
        };
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public synchronized String toString()
    {
        Index original = data.get(baseRegion);
        return "MapStoreProvider[type=" + type.getSimpleName() + ",entries=" + original.index.size() + ",indexes=" + data.size() + "]";
    }

    /**
     * An Index represents the data in a {@link MapStoreProvider} sorted
     * according to a certain {@link #sort} and {@link #query}
     */
    protected class Index
    {
        /**
         * This constructor should only be used from the constructor of our
         * parent MapStoreProvider.
         * @param baseRegion The portion of the data that we are looking at
         * @param map The data to filter and copy for this baseRegion
         */
        public Index(StoreRegion baseRegion, Map<String, T> map)
        {
            sort = baseRegion.getSort();
            query = baseRegion.getQuery();
            sortedData = createEmptySortedData();

            for (Map.Entry<String, T> entry : map.entrySet())
            {
                put(entry.getKey(), entry.getValue());
            }
        }

        /**
         * Constructor for use to copy data from an existing Index
         * @param region The portion of the data that we are looking at
         * @param original The data to filter and copy for this baseRegion
         */
        public Index(StoreRegion region, Index original)
        {
            sort = region.getSort();
            query = region.getQuery();
            sortedData = createEmptySortedData();

            for (Pair<String, T> pair : original.sortedData)
            {
                put(pair);
            }
        }

        /**
         * For use only by the constructor. Sets up the comparators.
         */
        private SortedSet<Pair<String, T>> createEmptySortedData()
        {
            // This is really how we sort - according to the defaultSearchCriteria
            Comparator<T> criteriaComparator = new SortCriteriaComparator<T>(sort, new PojoAttributeValueExtractor());

            // However we need to store a the data along with a key so we need a
            // proxy comparator to be a Comparator<Pair<T, String>> but to call
            // the real comparator above.
            Comparator<Pair<String, T>> pairComparator = new PairComparator<T>(criteriaComparator);

            // Copy all the data from the original map into pairs in a sorted set
            return new TreeSet<Pair<String, T>>(pairComparator);
        }

        /**
         * Accessor for the sorted data
         */
        public SortedSet<Pair<String, T>> getSortedData()
        {
            return sortedData;
        }

        /**
         * Remove an item from this cache of data
         */
        public void remove(String itemId)
        {
            T t = index.remove(itemId);
            sortedData.remove(new Pair<String, T>(itemId, t));
            fireItemRemoved(itemId);
        }

        /**
         * Add an item thats already in a pair
         */
        public void put(Pair<String, T> pair)
        {
            if (pair.right == null)
            {
                remove(pair.left);
                return;
            }

            if (isRelevant(pair.right))
            {
                boolean existing = index.containsKey(pair.left);
                sortedData.add(pair);
                index.put(pair.left, pair.right);

                if (existing)
                {
                    fireItemChanged(new Item(pair.left, pair.right), null);
                }
                else
                {
                    fireItemAdded(new Item(pair.left, pair.right));
                }
            }
        }

        /**
         * Add an entry by separate objects
         */
        public void put(String itemId, T t)
        {
            if (t == null)
            {
                remove(itemId);
                return;
            }

            if (isRelevant(t))
            {
                boolean existing = index.containsKey(itemId);
                sortedData.add(new Pair<String, T>(itemId, t));
                index.put(itemId, t);

                if (existing)
                {
                    fireItemChanged(new Item(itemId, t), null);
                }
                else
                {
                    fireItemAdded(new Item(itemId, t));
                }
            }
        }

        /**
         * Is this item one that will appear in this Index?
         */
        private boolean isRelevant(T t)
        {
            return query == null || query.isEmpty() || passesFilter(t, query);
        }

        /* (non-Javadoc)
         * @see java.lang.Object#toString()
         */
        @Override
        public String toString()
        {
            return "Map.Index[sortedData.size=" + sortedData.size() + ",index.size=" + index.size() + ",sort=" + sort + ",query=" + query + "]";
        }

        /**
         * The data sorted by object according to our sort criteria
         */
        private final SortedSet<Pair<String, T>> sortedData;

        /**
         * The data in a standard hash so we can lookup by itemId
         */
        private final Map<String, T> index = new HashMap<String, T>();

        /**
         * The criteria by which we are sorting
         */
        private final List<SortCriterion> sort;

        /**
         * The way we are filtering the data
         */
        private final Map<String, String> query;
    }

    /**
     *
     * @param li
     * @param itemIds
     */
    protected synchronized void setWatchedSet(StoreChangeListener<T> li, Collection<String> itemIds)
    {
        // Unsubscribe from anything the listener was previously subscribed to
        for (String itemId : watched.get(li))
        {
            Collection<StoreChangeListener<T>> listeners = watchers.get(itemId);
            if (listeners != null)
            {
                listeners.remove(li);
                if (listeners.isEmpty())
                {
                    watchers.remove(itemId);
                }
            }
        }

        if (itemIds == null || itemIds.isEmpty())
        {
            watched.remove(li);
        }
        else
        {
            watched.put(li, itemIds);
            for (String itemId : itemIds)
            {
                Collection<StoreChangeListener<T>> listeners = watchers.get(itemId);
                if (listeners != null)
                {
                    listeners = new HashSet<StoreChangeListener<T>>();
                    watchers.put(itemId, listeners);
                }
                listeners.add(li);
            }
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
            for (StoreChangeListener<T> listener : listeners)
            {
                listener.itemRemoved(this, itemId);
            }
        }
    }

    /**
     * A map of the itemIds in this store along with who is watching them.
     * @protectedBy(this)
     */
    protected final Map<String, Collection<StoreChangeListener<T>>> watchers = new HashMap<String, Collection<StoreChangeListener<T>>>();

    /**
     * A map of the itemIds in this store along with who is watching them.
     * @protectedBy(this)
     */
    protected final Map<StoreChangeListener<T>, Collection<String>> watched = new HashMap<StoreChangeListener<T>, Collection<String>>();

    /**
     * There will always be at least one entry in the {@link #data} map with
     * this key.
     * @protectedBy(this)
     */
    protected final StoreRegion baseRegion;

    /**
     * We actually store a number of indexes to the real data.
     * @protectedBy(this)
     */
    protected final Map<StoreRegion, Index> data = new HashMap<StoreRegion, Index>();
}
