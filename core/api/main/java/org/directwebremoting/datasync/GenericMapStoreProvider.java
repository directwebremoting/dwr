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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.event.EventListenerList;

import org.directwebremoting.ServerContextFactory;
import org.directwebremoting.extend.ConverterManager;
import org.directwebremoting.io.Item;
import org.directwebremoting.io.RawData;

/**
 * A simple implementation of StoreProvider that uses a Map<String, ?>.
 * TODO: Allow access to a (wrapper implementation) of the Map to allow its use
 * outside this class.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class GenericMapStoreProvider<T> extends AbstractStoreProvider implements StoreProvider
{
    /**
     * Initialize the GenericMapStoreProvider from an existing map
     */
    @SuppressWarnings("unchecked")
    public GenericMapStoreProvider(Map<String, T> datamap)
    {
        this();
        this.datamap = (Map<String, Object>) datamap;
        this.type = (Class<?>) datamap.getClass().getGenericInterfaces()[1];
    }

    /**
     * Setup the converterManager. All constructors should call this
     */
    protected GenericMapStoreProvider()
    {
        converterManager = ServerContextFactory.get().getContainer().getBean(ConverterManager.class);
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.datasync.StoreProvider#put(java.lang.String, org.directwebremoting.io.RawData)
     */
    public void put(String itemId, RawData data)
    {
        // TODO: update to mutate the object rather than create
        // TODO: call the listeners
        Object value = converterManager.convertInbound(type, data);
        datamap.put(itemId, value);
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.datasync.StoreProvider#view(org.directwebremoting.datasync.StoreRegion)
     */
    public MatchedItems view(StoreRegion region)
    {
        List<Item> matches = new ArrayList<Item>();

        for (Map.Entry<String, Object> entry : datamap.entrySet())
        {
            Object value = entry.getValue();
            if (passesFilter(value, region.getFilter()))
            {
                matches.add(new Item(entry));
            }
        }

        sort(matches, region.getSort());
        List<Item> subList = matches.subList(region.getStart(), region.getStart() + region.getCount());

        return new MatchedItems(subList, matches.size());
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.datasync.StoreProvider#subscribe(org.directwebremoting.datasync.StoreRegion, org.directwebremoting.datasync.StoreChangeListener)
     */
    public MatchedItems subscribe(StoreRegion region, StoreChangeListener li)
    {
        storeChangeListeners.add(StoreChangeListener.class, li);
        return view(region);
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.datasync.StoreProvider#unsubscribe(org.directwebremoting.datasync.StoreChangeListener)
     */
    public void unsubscribe(StoreChangeListener li)
    {
        storeChangeListeners.remove(StoreChangeListener.class, li);
    }

    /**
     * The list of current {@link StoreChangeListener}s
     */
    protected EventListenerList storeChangeListeners = new EventListenerList();

    /**
     * The type that this StoreProvider uses
     */
    protected Class<?> type;

    /**
     * The internal map of objects
     */
    protected Map<String, Object> datamap;

    /**
     * Cached converterManager so we don't look it up every time
     */
    protected ConverterManager converterManager;
}
