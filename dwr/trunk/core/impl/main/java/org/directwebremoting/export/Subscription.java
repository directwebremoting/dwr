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

import org.directwebremoting.datasync.StoreChangeListener;
import org.directwebremoting.datasync.StoreRegion;
import org.directwebremoting.io.JavascriptFunction;

/**
 * A Subscription links a widget on a page to some data shared through a
 * {@link org.directwebremoting.datasync.StoreProvider} by
 * {@link org.directwebremoting.datasync.Directory}
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public final class Subscription implements StoreChangeListener
{
    /**
     * All Subscriptions need a store and callback to work from.
     */
    public Subscription(String storeId, JavascriptFunction callback, StoreRegion storeRegion)
    {
        this.callback = callback;
        this.storeId = storeId;
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

        Subscription that = (Subscription) obj;

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

    /**
     * @see #getStoreId
     */
    private String storeId;

    /**
     * @see #getCallback
     */
    private JavascriptFunction callback;

    /**
     * @see #getStoreRegion
     */
    private StoreRegion storeRegion;
}
