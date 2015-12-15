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
package org.directwebremoting.io;

import java.util.Collections;
import java.util.List;

/**
 * MatchedItems is simply a holder for a list of matched {@link Item}s and a
 * total match count, used by:
 * {@link org.directwebremoting.datasync.StoreProvider#viewRegion(StoreRegion)} and
 * {@link org.directwebremoting.datasync.StoreProvider#viewRegion(StoreRegion, StoreChangeListener)}
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class MatchedItems
{
    /**
     * If there is data to return, then both of these must be set.
     * Note that totalMatchCount == viewedMatches.size() ONLY when there is
     * no filtering going on.
     */
    public MatchedItems(List<Item> viewedMatches, int totalMatchCount)
    {
        if (viewedMatches == null)
        {
            throw new NullPointerException("viewedMatches may not be null. Use the default constructor if you have no data");
        }
        this.viewedMatches = viewedMatches;

        this.totalMatchCount = totalMatchCount;
    }

    /**
     * The constructor to use of there are no items to transfer.
     */
    public MatchedItems()
    {
        this.viewedMatches = Collections.emptyList();
        this.totalMatchCount = 0;
    }

    /**
     * Accessor for the total number of matches (before start/count filtering)
     */
    public int getTotalMatchCount()
    {
        return totalMatchCount;
    }

    /**
     * Accessor for the matched items (after start/count filtering)
     */
    public List<Item> getViewedMatches()
    {
        return viewedMatches;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        return "MatchedItems[viewedMatches=" + viewedMatches + ",totalMatchCount=" + totalMatchCount + "]";
    }

    /**
     * @see #getTotalMatchCount()
     */
    private final int totalMatchCount;

    /**
     * @see #getViewedMatches()
     */
    private final List<Item> viewedMatches;
}
