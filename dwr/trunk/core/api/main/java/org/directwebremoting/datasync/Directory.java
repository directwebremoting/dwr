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

import java.util.HashMap;
import java.util.Map;

/**
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class Directory
{
    /**
     * Register a StoreProvider for access by the outside world
     * @param storeId The id by which the store can be reached
     * @param provider The store provider that holds the Map of data
     */
    public static void register(String storeId, StoreProvider provider)
    {
        providers.put(storeId, provider);
    }

    /**
     * Remove the registration of a StoreProvider
     * @param storeId The id by which the store can be reached
     */
    public static void unregister(String storeId)
    {
        providers.remove(storeId);
    }

    /**
     * Look up the StoreProvider by ID.
     * @param storeId The id by which the store can be reached
     * @return The found StoreProvider or null if one is not found.
     */
    public static StoreProvider getRegistration(String storeId)
    {
        return providers.get(storeId);
    }

    /**
     * Internal map of stores
     */
    private static Map<String, StoreProvider> providers = new HashMap<String, StoreProvider>();

    /*
    public static <V> Map<String, V> register(String storeId, Map<String, V> data, List<SortCriteria> defaultSort, Class<V> valueType)
    {
        stores.put(storeId, data);
        return data;
    }

    public static <V> Map<String, V> getViewOfStore(String storeId, List<SortCriteria> sort, Map<String, String> query)
    {
        @SuppressWarnings("unchecked")
        Map<String, V> store = (Map<String, V>) stores.get(storeId);

        for (SortCriteria criteria : sort)
        {
            Collections.sort(store, new Comparator()
            {
                public int compare(Object o1, Object o2)
                {
                    return 0;
                }
            });
        }

        List<Item> reply = new ArrayList<Item>();

        loopForMoreData:
        while (reply.size() <= count)
        {
            Object data = store.get(index);

            for (Map.Entry<String, String> entry : query.entrySet())
            {
                if (!matches(data, entry.getKey(), entry.getValue()))
                {
                    break loopForMoreData;
                }
            }

            reply.add(data);
        }

        return store;
    }

    @SuppressWarnings("unchecked")
    public static <V> Map<String, V> getStore(String storeId, Class<V> valueType)
    {
        return (Map<String, V>) stores.get(storeId);
    }

    private boolean matches(Object data, String key, String value)
    {
        // TODO Auto-generated method stub
        return false;
    }

    public static class StoreKey<V>
    {
        private String storeId;
        private V valueType;
    }

    public static Map<String, Map<String, ?>> stores = new HashMap<String, Map<String, ?>>();
    public static Map<String, Class<?>> valueTypes = new HashMap<String, Class<?>>();

    public static Class<?> getValueType(String storeId)
    {
        return valueTypes.get(storeId);
    }
    */
}
