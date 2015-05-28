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
 * A way to find {@link StoreProvider}s that people wish to expose to the
 * outside world.
 * Warning. This API may well get wrapped in a Factory like the other DWR
 * services.
 * TODO: decide if we want to wrap this
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class Directory
{
    /**
     * Register a StoreProvider for access by the outside world
     * @param storeId The id by which the store can be reached
     * @param provider The store provider that holds the Map of data
     */
    public static void register(String storeId, StoreProvider<?> provider)
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
    @SuppressWarnings("unchecked")
    public static <T> StoreProvider<T> getRegistration(String storeId, @SuppressWarnings("unused") Class<T> type)
    {
        return (StoreProvider<T>) providers.get(storeId);
    }

    /**
     * Internal map of stores
     */
    private static Map<String, StoreProvider<?>> providers = new HashMap<String, StoreProvider<?>>();
}
