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
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.directwebremoting.io.Item;
import org.directwebremoting.io.SortCriterion;
import org.directwebremoting.io.StoreRegion;
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
    @SuppressWarnings("unchecked")
    public MapStoreProvider(Map<String, T> map, Class<T> type, List<SortCriterion> defaultCriteria)
    {
        super(type);
        this.baseRegion = new StoreRegion(0, -1, defaultCriteria, null);

        Index index = new Index(baseRegion, map);
        data.put(baseRegion, index);

        // We add a dummy subscriber to make sure the data does not get GCed
        index.addSubscriber(new StoreChangeListener()
        {
            public void itemAdded(StoreProvider source, String itemId, Object t) { }
            public void itemRemoved(StoreProvider source, String itemId, Object t) { }
            public void itemChanged(StoreProvider source, String itemId, Object t) { }
        });
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.datasync.StoreProvider#put(java.lang.String, java.lang.Object)
     */
    public void put(String itemId, T value)
    {
        // TODO: update to mutate the object rather than create
        if (value == null)
        {
            for (Index index : data.values())
            {
                index.remove(itemId);
            }
        }
        else
        {
            for (Index index : data.values())
            {
                index.add(itemId, value);
            }
        }
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.datasync.StoreProvider#get(java.lang.String)
     */
    public T get(String itemId)
    {
        return data.get(baseRegion).index.get(itemId);
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.datasync.StoreProvider#view(org.directwebremoting.datasync.StoreRegion)
     */
    @SuppressWarnings("unchecked")
    public MatchedItems view(StoreRegion region)
    {
        Index index = getIndex(region);
        return selectMatchedItems(index.getSortedData(), region.getStart(), region.getCount());
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.datasync.StoreProvider#subscribe(org.directwebremoting.datasync.StoreRegion, org.directwebremoting.datasync.StoreChangeListener)
     */
    public MatchedItems subscribe(StoreRegion region, StoreChangeListener<T> li)
    {
        Index index = getIndex(region);
        index.addSubscriber(li);
        return selectMatchedItems(index.getSortedData(), region.getStart(), region.getCount());
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.datasync.StoreProvider#unsubscribe(org.directwebremoting.datasync.StoreChangeListener)
     */
    public void unsubscribe(StoreRegion region, StoreChangeListener<T> li)
    {
        Index index = getIndex(region);
        index.removeSubscriber(li);
    }

    /**
     * Chop out the part we are interested in and return the data converted to
     * a MatchedItems data set destined for the web.
     * @param sortedData The complete set of data we're extracting from
     * @param start The initial index to start from.
     * @param count The number of data items to return (-1 means, to the end)
     * @return Data for the web
     */
    private MatchedItems selectMatchedItems(SortedSet<Pair<String, T>> sortedData, int start, int count)
    {
        log.debug("Selecting data from " + sortedData.size() + " items, starting at " + start + " for " + count + " items.");

        List<Item> matches = new ArrayList<Item>();
        int i = 0;
        for (Pair<String, T> pair : sortedData)
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
     * Get an Index from a StoreRegion by defaulting the sort criteria if
     * needed, and by creating a new index if needed.
     * @param region The region to be viewed (we ignore start/end)
     * @return An index that we can use to get a sorted data cache
     */
    protected Index getIndex(StoreRegion region)
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

    /**
     * Get access to the contained data as a {@link Map}.
     * TODO: This is probably very inefficient - optimize
     * @return An implementation of Map that affect this {@link StoreProvider}
     */
    public Map<String, T> asMap()
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
                T old = MapStoreProvider.this.get(itemId);
                MapStoreProvider.this.put(itemId, value);
                return old;
            }

            /* (non-Javadoc)
             * @see java.util.AbstractMap#remove(java.lang.Object)
             */
            @Override
            public T remove(Object itemId)
            {
                T old = MapStoreProvider.this.get((String) itemId);
                MapStoreProvider.this.put((String) itemId, (T) null);
                return old;
            }

            /* (non-Javadoc)
             * @see java.util.AbstractMap#entrySet()
             */
            @Override
            public Set<Map.Entry<String, T>> entrySet()
            {
                return new AbstractSet<Entry<String, T>>()
                {
                    /* (non-Javadoc)
                     * @see java.util.AbstractCollection#iterator()
                     */
                    @Override
                    public Iterator<Map.Entry<String, T>> iterator()
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
                        T t = MapStoreProvider.this.get(entry.getKey());
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
                        T old = MapStoreProvider.this.get(entry.getKey());
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
    public String toString()
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
                add(entry.getKey(), entry.getValue());
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
                add(pair);
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

            for (StoreChangeListener<T> li : subscribers)
            {
                li.itemRemoved(MapStoreProvider.this, itemId, t);
            }
        }

        /**
         * Add an item thats already in a pair
         */
        public void add(Pair<String, T> pair)
        {
            if (isRelevant(pair.right))
            {
                sortedData.add(pair);
                index.put(pair.left, pair.right);

                for (StoreChangeListener<T> li : subscribers)
                {
                    li.itemAdded(MapStoreProvider.this, pair.left, pair.right);
                }
            }
        }

        /**
         * Add a entry by separate objects
         */
        public void add(String itemId, T t)
        {
            if (isRelevant(t))
            {
                sortedData.add(new Pair<String, T>(itemId, t));
                index.put(itemId, t);

                for (StoreChangeListener<T> li : subscribers)
                {
                    li.itemAdded(MapStoreProvider.this, itemId, t);
                }
            }
        }

        /**
         * Add a change subscriber
         */
        public void addSubscriber(StoreChangeListener<T> li)
        {
            subscribers.add(li);
        }

        /**
         * Remove a change subscriber
         */
        public void removeSubscriber(StoreChangeListener<T> li)
        {
            subscribers.remove(li);
        }

        /**
         * Is this item one that will appear in this Index?
         */
        private boolean isRelevant(T t)
        {
            if (query == null || query.size() == 0)
            {
                return true;
            }

            return passesFilter(t, query);
        }

        /* (non-Javadoc)
         * @see java.lang.Object#toString()
         */
        @Override
        public String toString()
        {
            return "Map.Index[sortedData.size=" + sortedData.size() + ",index.size=" + index.size() + ",subscribers.size=" + subscribers.size() + ",sort=" + sort + ",query=" + query + "]";
        }

        /**
         * The people that want to know about changes
         */
        private final List<StoreChangeListener<T>> subscribers = new ArrayList<StoreChangeListener<T>>();

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
     * There will always be at least one entry in the {@link #data} map with
     * this key
     */
    protected final StoreRegion baseRegion;

    /**
     * We actually store a number of indexes to the real data.
     */
    protected final Map<StoreRegion, Index> data = new HashMap<StoreRegion, Index>();
}
