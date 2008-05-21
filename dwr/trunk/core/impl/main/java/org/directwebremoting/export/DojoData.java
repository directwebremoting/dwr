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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.directwebremoting.ScriptSession;
import org.directwebremoting.ServerContextFactory;
import org.directwebremoting.WebContextFactory;
import org.directwebremoting.datasync.Directory;
import org.directwebremoting.datasync.SortCriteria;
import org.directwebremoting.extend.ConverterManager;
import org.directwebremoting.extend.MarshallException;
import org.directwebremoting.io.JavascriptFunction;
import org.directwebremoting.io.RawData;

/**
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class DojoData
{
    /**
     * 
     */
    private static final String ATTRIBUTE_SUBSCRIPTIONS = "org.directwebremoting.export.DojoData.subscriptions";

    public static class Item
    {
        int itemId;
        Object data;
    }

    public static class Subscription
    {
        /**
         * @param callback The client side function to call with updates
         */
        public Subscription(String storeId, JavascriptFunction callback)
        {
            this.callback = callback;
            this.storeId = storeId;
        }

        /**
         * Accessor for the start of the range of interest
         */
        public int getStart()
        {
            return start;
        }

        /**
         * Accessor for the start of the range of interest
         */
        public void setStart(int start)
        {
            this.start = start;
        }

        /**
         * Accessor for the length of the range of interest
         */
        public int getCount()
        {
            return count;
        }

        /**
         * Accessor for the length of the range of interest
         */
        public void setCount(int count)
        {
            this.count = count;
        }

        /**
         * Accessor for the directory subscription
         */
        public StoreView getStoreView()
        {
            return storeView;
        }

        /**
         * Accessor for the directory subscription
         */
        public void setStoreView(StoreView storeView)
        {
            this.storeView = storeView;
        }

        /**
         * Accessor for the directory subscription
         */
        public JavascriptFunction getCallback()
        {
            return callback;
        }

        private String storeId;
        private int start;
        private int count;
        private StoreView storeView;
        private JavascriptFunction callback;

        /*
            List<Item> store;
            store = Directory.getViewOfStore(storeId, sort, query, Item.class);
         */

        /**
         * @return
         */
        public Set<Item> getInitialItems()
        {
            // TODO Auto-generated method stub
            return null;
        }
    }

    public static class StoreView
    {
        public StoreView(List<SortCriteria> sort, Map<String, String> query)
        {
            this.sort = sort;
            this.query = query;
        }

        /**
         * @param testSort
         * @param testQuery
         */
        public boolean matches(List<SortCriteria> testSort, Map<String, String> testQuery)
        {
            if (!sort.equals(testSort))
            {
                return false;
            }

            if (!query.equals(testQuery))
            {
                return false;
            }

            return true;
        }

        /**
         * 
         */
        public void close()
        {
        }

        List<SortCriteria> sort;
        Map<String, String> query;
    }

    /**
     * Notes that there is a region of a page that wishes to subscribe to server
     * side data, and registers a callback function to receive the data.
     * @param subscriptionId Since multiple widgets on a page may be interested
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
     * if the store was known to be in-progress.
     * @param callback A client-side function to be called to be updated on data
     * from the server. The function will have the following signature:
     * <pre>function(reason:String, data:Object[]) { ... }</pre>
     * The first parameter is the reason for the call and will be one of
     * 'initial', 'insert', 'update' or 'delete'. The function will be called
     * once with a value of 'initial' in response to each subscribe. The call
     * will be made even if there is no data to transfer (data will be an empty
     * array). The other values will be used when a change is detected on the
     * server. This could be via the #update function. The second parameter will
     * be an array of objects which will appear in Javascript along these lines:
     * <pre>[{ itemId:key1, data:data1 }, { itemId:key2, data:data2 },...]</pre>
     * When the reason code is 'delete' the data property will be missing.
     * TODO: the reason codes are effectively constants. Maybe we should have
     * an enumeration with int values for speed? OTOH keeping 2 APIs in sync
     * might be easier with them being strings
     * TODO: the reason for wanting to separate insert and update was so we
     * could differentiate add-at-end, add-in-middle and update, but since the
     * server data is effectively a Map maybe we can do without this
     */
    public void init(String subscriptionId, String storeId, JavascriptFunction callback)
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

        Subscription subscription = subscriptions.get(subscriptionId);
        if (subscription == null)
        {
            subscription = new Subscription(storeId, callback);
            subscriptions.put(subscriptionId, subscription);
        }
    }

    /**
     * Declares that the current page is no longer interested in viewing data
     * on the given subscriptionId, and that we can close server resources.
     * If the page is about to be closed, then the server will timeout the
     * ScriptSession, and all data will be purged anyway. This function could be
     * used with a large single-page site to note that a table has been deleted.
     * @param subscriptionId The ID previously passed to {@link #init}.
     */
    public void destroy(String subscriptionId)
    {
        ScriptSession session = WebContextFactory.get().getScriptSession();
        @SuppressWarnings("unchecked")
        Map<String, Subscription> subscriptions = (Map<String, Subscription>) session.getAttribute(ATTRIBUTE_SUBSCRIPTIONS);
        if (subscriptions != null)
        {
            subscriptions.remove(subscriptionId);
        }
    }

    /**
     * Notes that a page is interested in viewing a (portion of a) server-side
     * data store.
     * 
     * @param subscriptionId Since multiple widgets on a page may be interested
     * in viewing a single data store, widgets must provide a distinguishing ID.
     * Subsequent calls to <code>subscribe()</code> with the same ID will cancel
     * updates to the original subscription set. Calls to subscribe() with null
     * in the <code>storeId</code> field will be taken to be an unsubscribe.
     * The subscriptionId only needs to be unique within a page, and not within
     * a website, so using the element ID is probably good enough.
     * @param start The beginning of the region of interest to the viewer. The
     * initial index is generally 0.
     * @param count The length of the region of interest to the viewer. If the
     * subscriber wishes to subscribe to 'the rest of the data', they should
     * use count = -1. Thus using start = 0, count = -1 is the entire data set.
     * If start + count > size of data set, then less data will be sent than was
     * requested. If at a later date the store grows such that it is bigger than
     * the requested data set, the extra items will not be transmitted.
     * @param sort Sort criteria to be used before the range is extracted.
     * Using <code>null</code> as a sort criteria means 'no sorting'. Sort
     * criteria can be expensive to server resources, so using no sorting could
     * be much faster. The default sort order is that supplied to the server in
     * the {@link Directory#register} method.
     * @param query Filter criteria to be used before the range is extracted.
     * This functions much like the sort criteria, except, clearly this is
     * filtering and not sorting.
     */
    public void subscribe(String subscriptionId, int start, int count, List<SortCriteria> sort, Map<String, String> query)
    {
        // Get the list of things that this page subscribes to
        ScriptSession session = WebContextFactory.get().getScriptSession();
        @SuppressWarnings("unchecked")
        Map<String, Subscription> subscriptions = (Map<String, Subscription>) session.getAttribute(ATTRIBUTE_SUBSCRIPTIONS);
        if (subscriptions == null)
        {
            log.error("subscribe() called before init() with subscriptionId=" + subscriptionId);
            throw new IllegalStateException("subscribe() called before init()");
        }

        // Get the subscription in question
        Subscription subscription = subscriptions.get(subscriptionId);
        if (subscription == null)
        {
            log.error("Missing subscription when finding subscriptionId=" + subscriptionId);
            throw new IllegalStateException("Missing subscription");
        }

        StoreView storeView = subscription.getStoreView();
        if (!storeView.matches(sort, query))
        {
            storeView.close();

            subscription.setStoreView(new StoreView(sort, query));
            subscriptions.put(subscriptionId, subscription);
        }
        
        subscription.setStart(start);
        subscription.setCount(count);

        JavascriptFunction callback = subscription.getCallback();
        Set<Item> items = subscription.getInitialItems();
        callback.execute("initial", items);
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
     * registered converter for this type, a MarshallException will be thrown.
     * if data == null then the data will be removed from the store.
     * @throws MarshallException If the convert can't happen
     */
    public <T> void update(String storeId, String itemId, RawData data) throws MarshallException
    {
        @SuppressWarnings("unchecked")
        Class<T> type = (Class<T>) Directory.getValueType(storeId);
        Map<String, T> store = Directory.getStore(storeId, type);
        if (store == null)
        {
            log.error("update() can't find any stores with storeId=" + storeId);
            throw new NullPointerException("Invalid store");
        }

        ConverterManager converterManager = ServerContextFactory.get().getContainer().getBean(ConverterManager.class);
        T value = converterManager.convertInbound(type, data);

        store.put(itemId, value);
    }

    /**
     * The log stream
     */
    private static final Log log = LogFactory.getLog(DojoData.class);
}
