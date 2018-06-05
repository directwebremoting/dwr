package org.directwebremoting.datasync;

import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Arrays;
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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.directwebremoting.io.Item;
import org.directwebremoting.io.ItemUpdate;
import org.directwebremoting.io.MatchedItems;
import org.directwebremoting.io.QueryOptions;
import org.directwebremoting.io.SortCriterion;
import org.directwebremoting.io.StoreChangeListener;
import org.directwebremoting.io.StoreRegion;
import org.directwebremoting.util.LocalUtil;
import org.directwebremoting.util.Pair;

/**
 * A simple implementation of StoreProvider that uses a Map<String, ?>.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class MapStoreProvider<T> extends AbstractStoreProvider<T> implements StoreProvider<T>
{
    /**
     * Initialize the MapStoreProvider from an existing map + specified type.
     */
    public MapStoreProvider(Map<String, T> datamap, Class<T> type)
    {
        this(datamap, type, new ArrayList<SortCriterion>(), new DefaultComparatorFactory<T>());
    }

    /**
     * Initialize the MapStoreProvider from an existing map + specified type.
     */
    public MapStoreProvider(Map<String, T> datamap, Class<T> type, ComparatorFactory<T> comparatorFactory)
    {
        this(datamap, type, new ArrayList<SortCriterion>(), comparatorFactory);
    }

    /**
     * Initialize an empty MapStoreProvider from the specified type.
     */
    public MapStoreProvider(Class<T> type)
    {
        this(new HashMap<String, T>(), type, new ArrayList<SortCriterion>(), new DefaultComparatorFactory<T>());
    }

    /**
     * Initialize the MapStoreProvider from an existing map + specified type
     * along with some sort criteria to be used when the client does not specify
     * sorting.
     */
    public MapStoreProvider(Map<String, T> map, Class<T> type, List<SortCriterion> defaultCriteria, ComparatorFactory<T> comparatorFactory)
    {
        super(type);
        this.baseRegion = new StoreRegion(0, -1, defaultCriteria, null, new QueryOptions());
        this.comparatorFactory = comparatorFactory;

        Index index = new Index(baseRegion, map);
        data.put(baseRegion, index);
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.datasync.StoreProvider#viewRegion(org.directwebremoting.datasync.StoreRegion)
     */
    public synchronized MatchedItems viewRegion(StoreRegion region)
    {
        Index index = getIndex(region);
        return selectMatchedItems(index.getSortedData(), region.getStart(), region.getCount());
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.datasync.StoreProvider#viewRegion(org.directwebremoting.datasync.StoreRegion, org.directwebremoting.datasync.StoreChangeListener)
     */
    public synchronized MatchedItems viewRegion(StoreRegion region, StoreChangeListener<T> listener)
    {
        MatchedItems matchedItems = viewRegion(region);

        Collection<String> itemIds = new HashSet<String>();
        for (Item item : matchedItems.getViewedMatches())
        {
            itemIds.add(item.getItemId());
        }
        setWatchedSet(listener, itemIds);

        return matchedItems;
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.datasync.StoreProvider#viewItem(java.lang.String, org.directwebremoting.io.StoreChangeListener)
     */
    public Item viewItem(String itemId, StoreChangeListener<T> listener)
    {
        Item item = viewItem(itemId);

        if (item != null)
        {
            addWatchedSet(listener, Arrays.asList(item.getItemId()));
        }

        return item;
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.datasync.StoreProvider#unsubscribe(org.directwebremoting.datasync.StoreChangeListener)
     */
    public synchronized void unsubscribe(StoreChangeListener<T> listener)
    {
        setWatchedSet(listener, null);
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.datasync.StoreProvider#put(java.lang.String, java.lang.Object)
     */
    public synchronized void put(String itemId, T value)
    {
        put(itemId, value, true);
    }

    public synchronized void put(String itemId, T value, boolean notify)
    {
        for (Index index : data.values())
        {
            index.put(itemId, value, notify);
        }
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
            boolean newItem = t == null;
            Collection<String> changedAttributes = new HashSet<String>();
            if (newItem)
            {
                try
                {
                    t = type.newInstance();
                }
                catch (Exception ex)
                {
                    throw new SecurityException(ex);
                }
            }

            for (ItemUpdate itemUpdate : entry.getValue())
            {
                String attribute = itemUpdate.getAttribute();
                if (attribute.equals("$delete"))
                {
                    put(itemUpdate.getItemId(), (T) null);
                }
                else
                {
                    try
                    {
                        Class<?> convertTo = LocalUtil.getPropertyType(type, attribute);
                        Object value = convert(itemUpdate.getNewValue(), convertTo);
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
            }

            if (!changedAttributes.isEmpty())
            {
                Item item = new Item(entry.getKey(), t);
                if (newItem)
                {
                    put(entry.getKey(), t, false);
                    fireItemAdded(item);
                }
                else
                {
                    fireItemChanged(item, changedAttributes);
                }
            }
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

    /**
     * Get access to the contained data as a {@link Map}.
     * @return An implementation of Map that affects this {@link StoreProvider}
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
            region = new StoreRegion(region.getStart(), region.getCount(), baseRegion.getSort(), region.getQuery(), region.getQueryOptions());
        }

        Index index = data.get(region);

        if (index == null)
        {
            // So there is no index that looks like we want
            Index original = data.get(baseRegion);

            index = new Index(region, original);
            data.put(region, index);

            log.debug("Creating new Index: " + index);
        }
        else
        {
            log.debug("Using existing Index: " + index);
        }

        return index;
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
            options = baseRegion.getQueryOptions();

            for (Map.Entry<String, T> entry : map.entrySet())
            {
                put(entry.getKey(), entry.getValue(), false);
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
            options = region.getQueryOptions();
            sortedData = createEmptySortedData();

            for (Pair<String, T> pair : original.sortedData)
            {
                put(pair, false);
            }
        }

        /**
         * For use only by the constructor. Sets up the comparators.
         */
        private SortedSet<Pair<String, T>> createEmptySortedData()
        {
            // This is really how we sort - according to the defaultSearchCriteria
            Comparator<T> criteriaComparator = new SortCriteriaComparator<T>(sort, comparatorFactory);

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
        public void put(Pair<String, T> pair, boolean notify)
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

                if (notify)
                {
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
        }

        /**
         * Add an entry by separate objects
         */
        public void put(String itemId, T t, boolean notify)
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

                if (notify)
                {
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
        }

        /**
         * Is this item one that will appear in this Index?
         */
        private boolean isRelevant(T t)
        {
            return query == null || query.isEmpty() || passesFilter(t, query, options);
        }

        /* (non-Javadoc)
         * @see java.lang.Object#toString()
         */
        @Override
        public String toString()
        {
            return "Map.Index[sortedData.size=" + sortedData.size() + ",index.size=" + index.size() + ",sort=" + sort + ",query=" + query + ", options=" + options + "]";
        }

        @Override
        public boolean equals(Object obj)
        {
            if (obj == null)
            {
                return false;
            }

            if (obj == this)
            {
                return true;
            }

            if (!this.getClass().equals(obj.getClass()))
            {
                return false;
            }

            Index that = (Index) obj;
            if (!LocalUtil.equals(this.options, that.options))
            {
                return false;
            }

            if (!LocalUtil.equals(this.query, that.query))
            {
                return false;
            }

            if (!LocalUtil.equals(this.sort, that.sort))
            {
                return false;
            }

            return true;
        }

        @Override
        public int hashCode()
        {
            int hash = 99211;
            hash += sort != null ? sort.hashCode() : 42835;
            hash += query != null ? query.hashCode() : 52339;
            hash += options != null ? options.hashCode() : 39832;
            return hash;
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

        /**
         * Criteria to filter the query matches.
         */
        private final QueryOptions options;

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
     * How we find Comparators to compare Ts based on a given attribute
     */
    protected final ComparatorFactory<T> comparatorFactory;

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

    /**
     * The log stream
     */
    private static final Log log = LogFactory.getLog(MapStoreProvider.class);
}
