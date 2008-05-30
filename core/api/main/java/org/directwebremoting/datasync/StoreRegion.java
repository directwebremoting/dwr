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

import java.util.List;
import java.util.Map;

/**
 * A zone of data within a {@link StoreProvider}. Useful for either requesting
 * a block of data, or for declaring a subscription. The StoreProvider will
 * first apply the {@link #getFilter} and {@link #getSort} criteria and then
 * restrict the data according to the {@link #getStart} and {@link #getCount}.
 * The latter 2 denote the viewed region.
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
    }

    /**
     * Create a StoreRegion with non default values
     * @param start The start of the range of interest
     * @param count The length of the region of interest to the viewer
     * @param sort The sort criteria to be used before the range is extracted
     * @param filter The filter criteria to be used before the range is extracted
     */
    public StoreRegion(int start, int count, List<SortCriteria> sort, Map<String, String> filter)
    {
        this.start = start;
        this.count = count;
        this.sort = sort;
        this.filter = filter;
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
    public List<SortCriteria> getSort()
    {
        return sort;
    }

    /**
     * Accessor for the filter criteria to be used before the range is extracted.
     * This functions much like the sort criteria, except, clearly this is
     * filtering and not sorting.
     * @return Filter criteria to be used before the range is extracted.
     */
    public Map<String, String> getFilter()
    {
        return filter;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode()
    {
        return 46712 + start + count + sort.hashCode() + filter.hashCode();
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

        if (this.start != that.start)
        {
            return false;
        }

        if (this.count != that.count)
        {
            return false;
        }

        if (!this.sort.equals(that.sort))
        {
            return false;
        }

        if (!this.filter.equals(that.filter))
        {
            return false;
        }

        return true;
    }

    /**
     * @see #getStart
     */
    private int start = 0;

    /**
     * @see #getCount
     */
    private int count = -1;

    /**
     * @see #getSort
     */
    private List<SortCriteria> sort = null;

    /**
     * @see #getFilter
     */
    private Map<String, String> filter = null;
}
