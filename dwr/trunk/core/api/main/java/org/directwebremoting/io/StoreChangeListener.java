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

import java.util.Collection;
import java.util.EventListener;

import org.directwebremoting.datasync.StoreProvider;

/**
 * We sometimes need to monitor what is happening to a store and the items it
 * contains
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public interface StoreChangeListener<T> extends EventListener
{
    /**
     * Something has removed an item from the store
     * @param source The store from which it was moved
     * @param itemId The ID of the item
     */
    public void itemRemoved(StoreProvider<T> source, String itemId);

    /**
     * Something has added an item to the store
     * @param source The store from which it was added
     * @param item The thing that has changed
     */
    public void itemAdded(StoreProvider<T> source, Item item);

    /**
     * Something has updated an item in the store
     * @param source The store from which it was updated
     * @param item The thing that has changed
     * @param changedAttributes A list of changed attributes. If null then
     * you should assume that everything has changed
     */
    public void itemChanged(StoreProvider<T> source, Item item, Collection<String> changedAttributes);
}
