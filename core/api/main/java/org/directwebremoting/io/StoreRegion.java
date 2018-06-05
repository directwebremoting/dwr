package org.directwebremoting.io;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.directwebremoting.datasync.StoreProvider;
import org.directwebremoting.util.LocalUtil;

/**
 * A zone of data within a {@link StoreProvider}. Useful for either requesting
 * a block of data, or for declaring a subscription. The StoreProvider will
 * first apply the {@link #getQuery query} and the
 * {@link #getSort sort criteria} and then
 * restrict the data according to the {@link #getStart} and {@link #getCount}.
 * The latter 2 denote the viewed region.
 * TODO: Support setter injection, remove the setters, make fields final
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public final class StoreRegion
{
    /**
     * Create a StoreRegion that uses the default values of start=0, count=-1
     * (i.e. everything), sort=null (i.e. the default store sorting) and
     * filter=null (i.e. no filtering)
     */
    public StoreRegion()
    {
        this.start = 0;
        this.count = -1;
        this.sort = new ArrayList<SortCriterion>();
        this.query = new HashMap<String, String>();
        this.queryOptions = new QueryOptions();
    }

    /**
     * Create a StoreRegion with non default values
     * @param start The start of the range of interest
     * @param count The length of the region of interest to the viewer
     * @param sort The sort criteria to be used before the range is extracted
     * @param query The filter criteria to be used before the range is extracted
     */
    public StoreRegion(int start, int count, List<SortCriterion> sort, Map<String, String> query, QueryOptions queryOptions)
    {
        this.start = start;
        this.count = count;
        this.queryOptions = queryOptions == null ? new QueryOptions() : queryOptions;

        this.sort = new ArrayList<SortCriterion>();
        if (sort != null)
        {
            this.sort.addAll(sort);
        }

        this.query = new HashMap<String, String>();
        // A query for foo:* is says nothing and confuses equals() so we check
        // before we add them in
        if (query != null)
        {
            for (Map.Entry<String, String> entry : query.entrySet())
            {
                String value = entry.getValue();
                if (!"*".equals(value))
                {
                    this.query.put(entry.getKey(), value);
                }
            }
        }

    }

    /**
     * Accessor for the start of the range of interest. The initial index is
     * always 0. This refers to the post filter/sort ordering
     * @return The start of the range of interest
     */
    public int getStart()
    {
        return start;
    }

    /**
     * Accessor for the length of the region of interest to the viewer. If the
     * subscriber wishes to subscribe to 'the rest of the data', they should
     * use count = -1. Thus using start = 0, count = -1 is the entire data set.
     * If start + count > size of data set, then less data will be sent than was
     * requested. If at a later date the store grows such that it is bigger than
     * the requested data set, the extra items will be included.
     * @return The length of the region of interest to the viewer
     */
    public int getCount()
    {
        return count;
    }

    /**
     * Accessor for the sort criteria to be used before the range is extracted.
     * Using <code>null</code> as a sort criteria means 'no sorting'. Sort
     * criteria can be expensive to server resources, so using no sorting could
     * be much faster. StoreProviders must define a default sort order
     * @return The sort criteria to be used before the range is extracted
     */
    public List<SortCriterion> getSort()
    {
        return Collections.unmodifiableList(sort);
    }

    /**
     * Accessor for the filter criteria to be used before the range is extracted.
     * This functions much like the sort criteria, except, clearly this is
     * filtering and not sorting.
     * @return Filter criteria to be used before the range is extracted.
     */
    public Map<String, String> getQuery()
    {
        return Collections.unmodifiableMap(query);
    }

    /**
     * @deprecated For DWR internal use only. Use constructor injection
     */
    @Deprecated
    public void setQuery(Map<String, String> query)
    {
        this.query.clear();

        // A query for foo:* is says nothing and confuses equals() so we check
        // before we add them in
        if (query != null)
        {
            for (Map.Entry<String, String> entry : query.entrySet())
            {
                String value = entry.getValue();
                if (!"*".equals(value))
                {
                    this.query.put(entry.getKey(), value);
                }
            }
        }
    }

    /**
     * Partially supported by DWR (ignoreCase attribute only).
     *
     * @return Filter criteria to be used before the range is extracted.
     */
    public QueryOptions getQueryOptions()
    {
        return queryOptions;
    }

    /**
     * @deprecated For DWR internal use only. Use constructor injection
     */
    @Deprecated
    public void setQueryOptions(QueryOptions queryOptions)
    {
        throw new IllegalAccessError();
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        return "StoreRegion[start=" + start + ",count=" + count + ",sort=" + sort + ",query=" + query + ", options=" + queryOptions + "]";
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode()
    {
        int hash = 342;
        hash += sort != null ? sort.hashCode() : 4835;
        hash += query != null ? query.hashCode() : 5239;
        hash += queryOptions != null ? queryOptions.hashCode() : 3982;
        return hash;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
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

        StoreRegion that = (StoreRegion) obj;

        if (!LocalUtil.equals(this.sort, that.sort))
        {
            return false;
        }

        if (!LocalUtil.equals(this.query, that.query))
        {
            return false;
        }

        if (!LocalUtil.equals(this.queryOptions, that.queryOptions))
        {
            return false;
        }
        // Testing for equality within search queries is complex because
        // { name:"*" } is equivalent to {}.

        // We clone so we can remove matches and then return false if anything
        // significant remains
        /*
        Map<String, String> thatQuery = new HashMap<String, String>(that.query);

        for (Map.Entry<String, String> thisEntry : this.query.entrySet())
        {
            String key = thisEntry.getKey();
            String thisValue = thisEntry.getValue();

            String thatValue = thatQuery.get(key);

            if (thisValue.equals("*") && (thatValue == null || thatValue.equals("*")))
            {
                thatQuery.remove(key);
            }
            else if (thisValue.equals(thatValue))
            {
                thatQuery.remove(key);
            }
            else
            {
                return false;
            }
        }

        // So we've got a match for everything in this.query, but what if there
        // is something extra that is significant in that.query?
        for (String value : thatQuery.values())
        {
            if (value != null && !value.equals("*"))
            {
                return false;
            }
        }
        */

        return true;
    }

    /**
     * @see #getStart
     */
    private final int start;

    /**
     * @see #getCount
     */
    private final int count;

    /**
     * @see #getSort
     */
    private final List<SortCriterion> sort;

    /**
     * @see #getQuery
     */
    private final Map<String, String> query;

    /**
     * @see #getQueryOptions
     */
    private final QueryOptions queryOptions;

}
