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
package org.directwebremoting.export;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.directwebremoting.Browser;
import org.directwebremoting.datasync.Directory;
import org.directwebremoting.datasync.StoreProvider;
import org.directwebremoting.io.DwrConvertedException;
import org.directwebremoting.io.Item;
import org.directwebremoting.io.ItemUpdate;
import org.directwebremoting.io.MatchedItems;
import org.directwebremoting.io.StoreChangeListener;
import org.directwebremoting.io.StoreRegion;

/**
 * External interface to the set of {@link StoreProvider}s that have been
 * registered.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class Data
{
    /**
     * Provide access to a single item of data given its ID.
     * @param storeId The ID of the store into which we look for the item
     * @param itemId The ID of the item to retrieve from the store
     * @param receiver The client side interface to pass async updates to.
     * Will be <code>null</code> if no async updates are required
     * @return The found item, or null if one was not found.
     */
    public Item viewItem(String storeId, String itemId, StoreChangeListener<Object> receiver)
    {
        StoreProvider<Object> provider = Directory.getRegistration(storeId, Object.class);
        if (provider == null)
        {
            throw new DwrConvertedException("clientId not found");
        }

        if (receiver != null)
        {
            // TODO: implement
            throw new DwrConvertedException("subscribe to a single object not supported yet");
        }

        return provider.get(itemId);
    }

    /**
     * Notes that there is a region of a page that wishes to subscribe to server
     * side data, and registers a callback function to receive the data.
     * @param storeId The ID of a store as registered on the server using
     * {@link Directory#register}. If no store has been registered
     * on the server using this passed <code>storeId</code>, then this call will
     * act as if there was a store registered, but that it was empty. This may
     * make it harder to scan the server for exposed stores. Internally however
     * the call will be ignored. This behavior may change in the future and
     * should <strong>not</strong> be relied upon.
     * @param region For field documentation see {@link StoreRegion}.
     * @param receiver The client side interface to pass async updates to.
     * Will be <code>null</code> if no async updates are required
     */
    public MatchedItems viewRegion(String storeId, StoreRegion region, StoreChangeListener<Object> receiver)
    {
        StoreProvider<Object> provider = Directory.getRegistration(storeId, Object.class);
        if (provider == null)
        {
            throw new DwrConvertedException("clientId not found");
        }

        if (region == null)
        {
            region = new StoreRegion();
        }

        if (receiver != null)
        {
            provider.unsubscribe(receiver);
            return provider.subscribe(region, receiver);
        }
        else
        {
            return provider.view(region);
        }
    }

    /**
     * Remove a subscription from the list of people that we are remembering
     * to keep updated
     * @param receiver The client side interface to pass async updates to.
     * Will be <code>null</code> if no async updates are required
     */
    public void unsubscribe(String storeId, StoreChangeListener<Object> receiver)
    {
        StoreProvider<Object> provider = Directory.getRegistration(storeId, Object.class);
        provider.unsubscribe(receiver);
        Browser.close(receiver);
    }

    /**
     * Update server side data.
     * @param storeId The store into which data is to be altered/inserted. If
     * a store by the given name has not been registered then this method is
     * a no-op, however a message will be written to the log detailing the error
     * @param changes A list of changes to make to the objects in the store
     */
    public <T> void update(String storeId, List<ItemUpdate> changes)
    {
        StoreProvider<Object> store = Directory.getRegistration(storeId, Object.class);
        if (store == null)
        {
            log.error("update() can't find any stores with storeId=" + storeId);
            throw new NullPointerException("Invalid store");
        }

        store.update(changes);
    }

    /**
     * The log stream
     */
    private static final Log log = LogFactory.getLog(Data.class);
}
