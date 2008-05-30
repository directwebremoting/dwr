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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.directwebremoting.ConversionException;
import org.directwebremoting.ScriptSession;
import org.directwebremoting.WebContextFactory;
import org.directwebremoting.datasync.Directory;
import org.directwebremoting.datasync.MatchedItems;
import org.directwebremoting.datasync.StoreProvider;
import org.directwebremoting.datasync.StoreRegion;
import org.directwebremoting.io.Item;
import org.directwebremoting.io.JavascriptFunction;
import org.directwebremoting.io.RawData;

/**
 * External interface to the set of {@link StoreProvider}s that have been
 * registered.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class Data
{
    /**
     * Notes that there is a region of a page that wishes to subscribe to server
     * side data, and registers a callback function to receive the data.
     * @param clientId Since multiple widgets on a page may be interested
     * in viewing a single data store, widgets must provide a distinguishing ID.
     * Subsequent calls to <code>subscribe()</code> with the same ID will cancel
     * updates to the original subscription set. Calls to subscribe() with null
     * in the <code>storeId</code> field will be taken to be an unsubscribe.
     * The subscriptionId only needs to be unique within a page, and not within
     * a website, so using the element ID is probably good enough.
     * @param storeId The ID of a store as registered on the server using
     * {@link Directory#register}. If no store has been registered
     * on the server using this passed <code>storeId</code>, then this call will
     * act as if there was a store registered, but that it was empty. This may
     * make it harder to scan the server for exposed stores, and could be useful
     * if the store was known to be in-progress. If storeId is null then this
     * is taken as being an unsubscribe request.
     * @param callback See documentation for {@link #callback}
     * @param region For field documentation see {@link StoreRegion}.
     */
    public void view(String clientId, String storeId, JavascriptFunction callback, StoreRegion region)
    {
        StoreProvider provider = Directory.getRegistration(storeId);
        if (provider == null)
        {
            // Fake a 'no-data' response
            callback(callback, clientId, REASON_INITIAL, new ArrayList<Item>(), 0);
        }
        else
        {
            MatchedItems matches = provider.view(region);
            callback(callback, clientId, REASON_INITIAL, matches.getViewedMatches(), matches.getTotalMatchCount());
        }
    }

    /**
     * Notes that there is a region of a page that wishes to subscribe to server
     * side data, and registers a callback function to receive the data.
     * @param clientId Since multiple widgets on a page may be interested
     * in viewing a single data store, widgets must provide a distinguishing ID.
     * Subsequent calls to <code>subscribe()</code> with the same ID will cancel
     * updates to the original subscription set. Calls to subscribe() with null
     * in the <code>storeId</code> field will be taken to be an unsubscribe.
     * The subscriptionId only needs to be unique within a page, and not within
     * a website, so using the element ID is probably good enough.
     * @param storeId The ID of a store as registered on the server using
     * {@link Directory#register}. If no store has been registered
     * on the server using this passed <code>storeId</code>, then this call will
     * act as if there was a store registered, but that it was empty. This may
     * make it harder to scan the server for exposed stores, and could be useful
     * if the store was known to be in-progress. If storeId is null then this
     * is taken as being an unsubscribe request.
     * @param callback See documentation for {@link #callback}
     * @param region For field documentation see {@link StoreRegion}.
     */
    public void subscribe(String clientId, String storeId, JavascriptFunction callback, StoreRegion region)
    {
        StoreProvider provider = Directory.getRegistration(storeId);
        if (provider == null)
        {
            // Fake a 'no-data' response
            callback(callback, clientId, REASON_INITIAL, new ArrayList<Item>(), 0);
            return;
        }

        Map<String, Subscription> subscriptions = getSubscriptions();
        Subscription subscription = subscriptions.get(clientId);
        if (subscription == null)
        {
            subscription = new Subscription(storeId, callback, region);
            subscriptions.put(clientId, subscription);
        }
        else
        {
            provider.unsubscribe(subscription);
        }

        MatchedItems matches = provider.subscribe(region, subscription);

        callback(callback, clientId, REASON_INITIAL, matches.getViewedMatches(), matches.getTotalMatchCount());
    }

    /**
     * Remove a subscription from the list of people that we are remembering
     * to keep updated
     * @param clientId The client's identifier for the subscription
     */
    public void unsubscribe(String clientId)
    {
        Map<String, Subscription> subscriptions = getSubscriptions();
        Subscription subscription = subscriptions.get(clientId);
        if (subscription != null)
        {
            subscription.getCallback().close();
            subscriptions.remove(clientId);
        }
    }

    /**
     * Update server side data.
     * 
     * @param storeId The store into which data is to be altered/inserted. If
     * a store by the given name has not been registered then this method is
     * a no-op, however a message will be written to the log detailing the error
     * @param itemId The key for the store that we are inserting into. It is an
     * error for itemId to be null, and it will result in an NPE.
     * @param data Data from the outside world that we need to convert ourselves
     * using the type information stored in the directory. If there is no
     * registered converter for this type, a ConversionException will be thrown.
     * if data == null then the data will be removed from the store.
     * @throws ConversionException If the convert can't happen
     */
    public <T> void update(String storeId, String itemId, RawData data) throws ConversionException
    {
        StoreProvider store = Directory.getRegistration(storeId);
        if (store == null)
        {
            log.error("update() can't find any stores with storeId=" + storeId);
            throw new NullPointerException("Invalid store");
        }

        store.put(itemId, data);
    }

    /**
     * Used by {@link #callback} to denote the initial response to a
     * {@link Data#view} or {@link Data#subscribe}.
     */
    public static final int REASON_INITIAL = 0;

    /**
     * Used by {@link #callback} to denote an insert into to a data set
     * previously highlighted in a call to {@link Data#subscribe}.
     */
    public static final int REASON_INSERT = 1;

    /**
     * Used by {@link #callback} to denote an update to to a data set
     * previously highlighted in a call to {@link Data#subscribe}.
     */
    public static final int REASON_UPDATE = 2;

    /**
     * Used by {@link #callback} to denote a delete from a data set
     * previously highlighted in a call to {@link Data#subscribe}.
     */
    public static final int REASON_DELETE = 3;

    /**
     * Execute the client-side callback function.
     * This is a trivial proxy to a {@link JavascriptFunction} that provides
     * a small amount of data typing value, but more importantly provides some
     * scope for documentation of the ways in which the callback is used.
     * @param clientId The ID passed in in the call to {@link Data#view} or
     * {@link Data#subscribe}.
     * @param reason The reason for the call. Must be one of the REASON_*
     * codes.
     * {@link Data#REASON_INITIAL} (={@value Data#REASON_INITIAL})
     * {@link Data#REASON_INSERT} (={@value Data#REASON_INSERT})
     * {@link Data#REASON_UPDATE} (={@value Data#REASON_UPDATE})
     * {@link Data#REASON_DELETE} (={@value Data#REASON_DELETE})
     * The function will be called once with a value of
     * {@link Data#REASON_INITIAL} in response to the original call to
     * {@link Data#view} or {@link Data#subscribe}. The call will be made
     * even if there is no data to transfer (<code>items</code> will be an
     * empty array).
     * Values of {@link Data#REASON_INSERT}, {@link Data#REASON_UPDATE} or
     * {@link Data#REASON_DELETE} will be used when a change is detected on
     * the server. This could be via the #update function.
     * @param items An array of objects containing the keys and data
     * <pre>[{ itemId:key1, data:data1 }, { itemId:key2, data:data2 },...]</pre>
     * When the reason code is 'delete' the data property will be missing.
     * @param matchCount
     */
    private void callback(JavascriptFunction function, String clientId, int reason, List<Item> items, int matchCount)
    {
        function.execute(clientId, reason, items, matchCount);
    }

    /**
     * Find the list of subscriptions from the current ScriptSession
     * @return The current list of subscriptions
     */
    private Map<String, Subscription> getSubscriptions()
    {
        // Create the list of subscriptions in the script session
        ScriptSession session = WebContextFactory.get().getScriptSession();
        @SuppressWarnings("unchecked")
        Map<String, Subscription> subscriptions = (Map<String, Subscription>) session.getAttribute(ATTRIBUTE_SUBSCRIPTIONS);
        if (subscriptions == null)
        {
            subscriptions = new HashMap<String, Subscription>();
            session.setAttribute(ATTRIBUTE_SUBSCRIPTIONS, subscriptions);
        }

        return subscriptions;
    }

    /**
     * The attribute under which we store subscriptions
     */
    private static final String ATTRIBUTE_SUBSCRIPTIONS = "org.directwebremoting.export.Data.subscriptions";

    /**
     * The log stream
     */
    private static final Log log = LogFactory.getLog(Data.class);
}
