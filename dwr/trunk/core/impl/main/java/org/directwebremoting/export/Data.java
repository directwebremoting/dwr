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
import org.directwebremoting.ScriptSession;
import org.directwebremoting.WebContextFactory;
import org.directwebremoting.datasync.Directory;
import org.directwebremoting.datasync.MatchedItems;
import org.directwebremoting.datasync.StoreChangeListener;
import org.directwebremoting.datasync.StoreProvider;
import org.directwebremoting.io.Item;
import org.directwebremoting.io.JavascriptFunction;
import org.directwebremoting.io.RawData;
import org.directwebremoting.io.StoreRegion;

/**
 * External interface to the set of {@link StoreProvider}s that have been
 * registered.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class Data
{
    /**
     * Notes that there is a region of a page that wishes to subscribe to server
     * side data, and registers a callback function to receive the data. The
     * data is one-shot, see {@link #subscribe} to receive updates.
     * @param clientId Since multiple widgets on a page may be interested
     * in viewing a single data store, widgets must provide a distinguishing ID.
     * The subscriptionId only needs to be unique within a page, and not within
     * a website, so using the element ID is acceptable.
     * @param storeId The ID of a store as registered on the server using
     * {@link Directory#register}. If no store has been registered
     * on the server using this passed <code>storeId</code>, then this call will
     * act as if there was a store registered, but that it was empty. This may
     * make it harder to scan the server for exposed stores, and could be useful
     * if the store was known to be in-progress.
     * @param callback See documentation for {@link #callInitial}
     * @param region For field documentation see {@link StoreRegion}.
     * @see #subscribe
     */
    public void view(String clientId, String storeId, JavascriptFunction callback, StoreRegion region)
    {
        StoreProvider<Object> provider = Directory.getRegistration(storeId, Object.class);
        if (provider == null)
        {
            // Fake a 'no-data' response
            callInitial(callback, clientId, new ArrayList<Item>(), 0);
        }
        else
        {
            if (region == null)
            {
                region = new StoreRegion();
            }

            MatchedItems matches = provider.view(region);
            callInitial(callback, clientId, matches.getViewedMatches(), matches.getTotalMatchCount());
        }
        callback.close();
    }

    /**
     * Notes that there is a region of a page that wishes to subscribe to server
     * side data, and registers a callback function to receive the data.
     * @param clientId Since multiple widgets on a page may be interested
     * in viewing a single data store, widgets must provide a distinguishing ID.
     * Subsequent calls to <code>subscribe()</code> with the same ID will cancel
     * updates to the original subscription set.
     * The subscriptionId only needs to be unique within a page, and not within
     * a website, so using the element ID is probably good enough.
     * @param storeId The ID of a store as registered on the server using
     * {@link Directory#register}. If no store has been registered
     * on the server using this passed <code>storeId</code>, then this call will
     * act as if there was a store registered, but that it was empty. This may
     * make it harder to scan the server for exposed stores. Internally however
     * the call will be ignored. This behavior may change in the future and
     * should <strong>not</strong> be relied upon.
     * @param callback See documentation for {@link #callInitial}
     * @param region For field documentation see {@link StoreRegion}.
     */
    public void subscribe(String clientId, String storeId, JavascriptFunction callback, StoreRegion region)
    {
        StoreProvider<Object> provider = Directory.getRegistration(storeId, Object.class);
        if (provider == null)
        {
            // Fake a 'no-data' response
            callInitial(callback, clientId, new ArrayList<Item>(), 0);
            return;
        }

        Map<String, Subscription<Object>> subscriptions = getSubscriptions();
        Subscription<Object> subscription = subscriptions.get(clientId);
        if (subscription == null)
        {
            subscription = new Subscription<Object>(clientId, storeId, callback, region);
            subscriptions.put(clientId, subscription);
        }
        else
        {
            provider.unsubscribe(subscription.getStoreRegion(), subscription);
        }

        MatchedItems matches = provider.subscribe(region, subscription);

        callInitial(callback, clientId, matches.getViewedMatches(), matches.getTotalMatchCount());
    }

    /**
     * Remove a subscription from the list of people that we are remembering
     * to keep updated
     * @param clientId The client's identifier for the subscription
     */
    public void unsubscribe(String clientId)
    {
        Map<String, Subscription<Object>> subscriptions = getSubscriptions();
        Subscription<Object> subscription = subscriptions.get(clientId);
        if (subscription != null)
        {
            StoreProvider<Object> provider = Directory.getRegistration(subscription.getStoreId(), Object.class);
            provider.unsubscribe(subscription.getStoreRegion(), subscription);

            subscription.getCallback().close();
            subscriptions.remove(clientId);
        }
    }

    /**
     * Update server side data.
     * @param storeId The store into which data is to be altered/inserted. If
     * a store by the given name has not been registered then this method is
     * a no-op, however a message will be written to the log detailing the error
     * @param itemId The key for the store that we are inserting into. It is an
     * error for itemId to be null, and it will result in an NPE.
     * @param data Data from the outside world that we need to convert ourselves
     * using the type information stored in the directory. If there is no
     * registered converter for this type, a ConversionException will be thrown.
     * if data == null then the data will be removed from the store.
     */
    public <T> void update(String storeId, String itemId, RawData data)
    {
        StoreProvider<Object> store = Directory.getRegistration(storeId, Object.class);
        if (store == null)
        {
            log.error("update() can't find any stores with storeId=" + storeId);
            throw new NullPointerException("Invalid store");
        }

        store.put(itemId, data);
    }

    /**
     * Used by the call*() functions to denote the initial response to a
     * {@link Data#view} or {@link Data#subscribe}.
     */
    protected static final int REASON_INITIAL = 0;

    /**
     * Used by call*() functions to denote an insert into to a data set
     * previously highlighted in a call to {@link Data#subscribe}.
     */
    protected static final int REASON_INSERT = 1;

    /**
     * Used by call*() functions to denote an update to to a data set
     * previously highlighted in a call to {@link Data#subscribe}.
     */
    protected static final int REASON_UPDATE = 2;

    /**
     * Used by call*() functions to denote a delete from a data set
     * previously highlighted in a call to {@link Data#subscribe}.
     */
    protected static final int REASON_DELETE = 3;

    /**
     * Execute the client-side callback function.
     * This is a trivial proxy to a {@link JavascriptFunction} that provides
     * a small amount of data typing value, but more importantly provides some
     * scope for documentation of the ways in which the callback is used.
     * @param clientId The ID passed in in the call to {@link Data#view} or
     * {@link Data#subscribe}.
     * @param items An array of objects containing the keys and data
     * <pre>[{ itemId:key1, data:data1 }, { itemId:key2, data:data2 },...]</pre>
     * When the reason code is 'delete' the data property will be missing.
     * @param matchCount The total number of matches (before start/count filtering)
     */
    protected static void callInitial(JavascriptFunction function, String clientId, List<Item> items, int matchCount)
    {
        function.execute(clientId, REASON_INITIAL, items, matchCount);
    }

    /**
     * Execute the client-side callback function.
     * This is a trivial proxy to a {@link JavascriptFunction} that provides
     * a small amount of data typing value, but more importantly provides some
     * scope for documentation of the ways in which the callback is used.
     * @param clientId The ID passed in in the call to {@link Data#view} or
     * {@link Data#subscribe}.
     * @param items An array of objects containing the keys and data
     * <pre>[{ itemId:key1, data:data1 }, { itemId:key2, data:data2 },...]</pre>
     * When the reason code is 'delete' the data property will be missing.
     */
    protected static void callInsert(JavascriptFunction function, String clientId, List<Item> items)
    {
        function.execute(clientId, REASON_INSERT, items);
    }

    /**
     * Execute the client-side callback function.
     * This is a trivial proxy to a {@link JavascriptFunction} that provides
     * a small amount of data typing value, but more importantly provides some
     * scope for documentation of the ways in which the callback is used.
     * @param clientId The ID passed in in the call to {@link Data#view} or
     * {@link Data#subscribe}.
     * @param items An array of objects containing the keys and data
     * <pre>[{ itemId:key1, data:data1 }, { itemId:key2, data:data2 },...]</pre>
     * When the reason code is 'delete' the data property will be missing.
     */
    protected static void callUpdate(JavascriptFunction function, String clientId, List<Item> items)
    {
        function.execute(clientId, REASON_UPDATE, items);
    }

    /**
     * Execute the client-side callback function.
     * This is a trivial proxy to a {@link JavascriptFunction} that provides
     * a small amount of data typing value, but more importantly provides some
     * scope for documentation of the ways in which the callback is used.
     * @param clientId The ID passed in in the call to {@link Data#view} or
     * {@link Data#subscribe}.
     * @param items An array of objects containing the keys and data
     * <pre>[{ itemId:key1, data:data1 }, { itemId:key2, data:data2 },...]</pre>
     * When the reason code is 'delete' the data property will be missing.
     */
    protected static void callDelete(JavascriptFunction function, String clientId, List<Item> items)
    {
        function.execute(clientId, REASON_DELETE, items);
    }

    /**
     * Find the list of subscriptions from the current ScriptSession
     * @return The current list of subscriptions
     */
    private Map<String, Subscription<Object>> getSubscriptions()
    {
        // Create the list of subscriptions in the script session
        ScriptSession session = WebContextFactory.get().getScriptSession();
        @SuppressWarnings("unchecked")
        Map<String, Subscription<Object>> subscriptions = (Map<String, Subscription<Object>>) session.getAttribute(ATTRIBUTE_SUBSCRIPTIONS);
        if (subscriptions == null)
        {
            subscriptions = new HashMap<String, Subscription<Object>>();
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

    /**
     * A Subscription links a widget on a page to some data shared through a
     * {@link org.directwebremoting.datasync.StoreProvider} by
     * {@link org.directwebremoting.datasync.Directory}
     * @author Joe Walker [joe at getahead dot ltd dot uk]
     */
    static class Subscription<T> implements StoreChangeListener<T>
    {
        /**
         * All Subscriptions need a store and callback to work from.
         */
        protected Subscription(String clientId, String storeId, JavascriptFunction callback, StoreRegion storeRegion)
        {
            this.clientId = clientId;
            this.storeId = storeId;
            this.callback = callback;
            this.storeRegion = storeRegion;
        }

        /**
         * Accessor for the directory subscription
         */
        public StoreRegion getStoreRegion()
        {
            return storeRegion;
        }

        /**
         * Accessor for the directory subscription
         */
        public JavascriptFunction getCallback()
        {
            return callback;
        }

        /**
         * Accessor for the ID of the {@link org.directwebremoting.datasync.StoreProvider}
         */
        public String getStoreId()
        {
            return storeId;
        }

        /* (non-Javadoc)
         * @see org.directwebremoting.datasync.StoreChangeListener#itemAdded(org.directwebremoting.datasync.StoreProvider, java.lang.String, java.lang.Object)
         */
        public void itemAdded(StoreProvider<T> source, String itemId, T t)
        {
            List<Item> items = new ArrayList<Item>();
            items.add(new Item(itemId, t));
            callInsert(callback, clientId, items);
        }

        /* (non-Javadoc)
         * @see org.directwebremoting.datasync.StoreChangeListener#itemRemoved(org.directwebremoting.datasync.StoreProvider, java.lang.String, java.lang.Object)
         */
        public void itemRemoved(StoreProvider<T> source, String itemId, T t)
        {
            List<Item> items = new ArrayList<Item>();
            items.add(new Item(itemId, t));
            callDelete(callback, clientId, items);
        }

        /* (non-Javadoc)
         * @see org.directwebremoting.datasync.StoreChangeListener#itemChanged(org.directwebremoting.datasync.StoreProvider, java.lang.String, java.lang.Object)
         */
        public void itemChanged(StoreProvider<T> source, String itemId, T t)
        {
            List<Item> items = new ArrayList<Item>();
            items.add(new Item(itemId, t));
            callUpdate(callback, clientId, items);
        }

        /* (non-Javadoc)
         * @see java.lang.Object#hashCode()
         */
        @Override
        public int hashCode()
        {
            return 9325 + storeId.hashCode() + callback.hashCode() + storeRegion.hashCode();
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

            @SuppressWarnings("unchecked")
            Subscription<T> that = (Subscription<T>) obj;

            if (!this.storeId.equals(that.storeId))
            {
                return false;
            }

            if (!this.callback.equals(that.callback))
            {
                return false;
            }

            if (!this.storeRegion.equals(that.storeRegion))
            {
                return false;
            }

            return true;
        }

        private final String clientId;

        /**
         * @see #getStoreId
         */
        private final String storeId;

        /**
         * @see #getCallback
         */
        private final JavascriptFunction callback;

        /**
         * @see #getStoreRegion
         */
        private final StoreRegion storeRegion;
    }
}
