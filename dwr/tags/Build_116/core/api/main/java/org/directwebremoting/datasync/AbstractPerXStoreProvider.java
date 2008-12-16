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

import org.directwebremoting.io.Item;
import org.directwebremoting.io.ItemUpdate;
import org.directwebremoting.io.MatchedItems;
import org.directwebremoting.io.RawData;
import org.directwebremoting.io.StoreChangeListener;
import org.directwebremoting.io.StoreRegion;

/**
 * A raw StoreProvider works on a global basis, so all users share the same
 * data. We envisage a number of ways to sub-divide this. For example, one
 * store per HttpSession, one store per ScriptSession, etc. (The PerX versions
 * of the StoreProvider class). This provides an abstract implementation of the
 * PerX behavior.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public abstract class AbstractPerXStoreProvider<T> implements StoreProvider<T>
{
    /**
     * Internal method to find or create a StoreProvider for a given user.
     */
    protected abstract StoreProvider<T> getStoreProvider();

    /* (non-Javadoc)
     * @see org.directwebremoting.datasync.StoreProvider#put(java.lang.String, org.directwebremoting.io.RawData)
     */
    public void put(String itemId, RawData rawData)
    {
        getStoreProvider().put(itemId, rawData);
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.datasync.StoreProvider#put(java.lang.String, java.lang.Object)
     */
    public void put(String itemId, T value)
    {
        getStoreProvider().put(itemId, value);
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.datasync.StoreProvider#unsubscribe(org.directwebremoting.io.StoreChangeListener)
     */
    public void unsubscribe(StoreChangeListener<T> listener)
    {
        getStoreProvider().unsubscribe(listener);
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.datasync.StoreProvider#update(java.util.List)
     */
    public void update(List<ItemUpdate> changes)
    {
        getStoreProvider().update(changes);
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.datasync.StoreProvider#viewItem(java.lang.String)
     */
    public Item viewItem(String itemId)
    {
        return getStoreProvider().viewItem(itemId);
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.datasync.StoreProvider#viewItem(java.lang.String, org.directwebremoting.io.StoreChangeListener)
     */
    public Item viewItem(String itemId, StoreChangeListener<T> listener)
    {
        return getStoreProvider().viewItem(itemId, listener);
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.datasync.StoreProvider#viewRegion(org.directwebremoting.io.StoreRegion)
     */
    public MatchedItems viewRegion(StoreRegion region)
    {
        return getStoreProvider().viewRegion(region);
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.datasync.StoreProvider#viewRegion(org.directwebremoting.io.StoreRegion, org.directwebremoting.io.StoreChangeListener)
     */
    public MatchedItems viewRegion(StoreRegion region, StoreChangeListener<T> listener)
    {
        return getStoreProvider().viewRegion(region, listener);
    }
}
