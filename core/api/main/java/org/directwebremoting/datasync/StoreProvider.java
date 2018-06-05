package org.directwebremoting.datasync;

import java.util.List;

import org.directwebremoting.io.Item;
import org.directwebremoting.io.ItemUpdate;
import org.directwebremoting.io.MatchedItems;
import org.directwebremoting.io.RawData;
import org.directwebremoting.io.StoreChangeListener;
import org.directwebremoting.io.StoreRegion;

/**
 * A StoreProvider is something like a {@link java.util.Map} where the API
 * revolves around getting blocks of entries rather than individual entries and
 * it gives you the ability to subscribe to those blocks of data to ask for
 * change updates. These are the types of changes that you would expect from an
 * interface designed for remote access.
 * <p>Implementing the {@link StoreProvider} interface is likely to be hard for
 * many applications. You are advised to begin by extending the
 * {@link AbstractStoreProvider} helper class.
 * <p>In place of {@link java.util.Map.Entry}, StoreProvider uses
 * {@link org.directwebremoting.io.Item}s as it's way to connect objects with
 * their primary keys.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public interface StoreProvider<T>
{
    /**
     * Similar to {@link java.util.Map#get} in fetching items from a Store.
     * @param itemId The ID of the item to fetch
     * @return The matched item, or null if it was not found
     */
    Item viewItem(String itemId);

    /**
     * Similar to {@link java.util.Map#get} in fetching items from a Store, and
     * request to stay updated to changes in the region.
     * @param itemId The ID of the item to fetch
     * @param listener The listener to be notified of changes
     * @return The matched item, or null if it was not found
     */
    Item viewItem(String itemId, StoreChangeListener<T> listener);

    /**
     * Extract the data referred to by the given region.
     * @param region A set of filter and sort criteria to restrict the fetched data
     */
    MatchedItems viewRegion(StoreRegion region);

    /**
     * Extract the data referred to by the given region, and request to stay
     * updated to changes in the region.
     * @param region A set of filter and sort criteria to restrict the fetched data
     * @param listener The listener to be notified of changes
     * @return Data that matches the filtering specified in the region.
     */
    MatchedItems viewRegion(StoreRegion region, StoreChangeListener<T> listener);

    /**
     * Remove the declaration of interest previously expressed.
     * @param listener The listener that should no longer be notified
     */
    void unsubscribe(StoreChangeListener<T> listener);

    /**
     * Similar to {@link java.util.Map#put} in adding items to a Store.
     * A value of null is equivalent to removing the item from the store.
     * Part of the function of put is to convert the data stored in
     * {@link RawData} into alterations to data in the store. This boiler plate
     * code is implemented in {@link AbstractStoreProvider}.
     * <p>See notes on {@link org.directwebremoting.io.Item#getItemId}
     * @param itemId The key (or some mapping) to it.
     * @param rawData Data from the web to be converted and added to the store
     */
    void put(String itemId, RawData rawData);

    /**
     * Similar to {@link java.util.Map#put} in adding items to a Store.
     * A value of null is equivalent to removing the item from the store.
     * @param itemId The key (or some mapping) to it.
     * @param value The new object to be added to the store
     */
    void put(String itemId, T value);

    /**
     * Make a series of updates to items in the store.
     * @param changes A list of item IDs along with the attributes to change
     * and the values to change to
     */
    void update(List<ItemUpdate> changes);
}
