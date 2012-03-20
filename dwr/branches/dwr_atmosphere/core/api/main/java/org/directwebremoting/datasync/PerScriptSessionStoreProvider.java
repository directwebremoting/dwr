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

import org.directwebremoting.ScriptSession;
import org.directwebremoting.WebContextFactory;

/**
 * A StoreProvider that allows us to have a number of StoreProviders for each
 * {@link ScriptSession}. New {@link StoreProvider}s are created by a
 * {@link StoreProviderFactory} and stored in the ScriptSession under a given
 * attribute name.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class PerScriptSessionStoreProvider<T> extends AbstractPerXStoreProvider<T>
{
    public PerScriptSessionStoreProvider(StoreProviderFactory<T, ScriptSession> factory, String attributeName)
    {
        this.factory = factory;
        this.attributeName = attributeName;
    }

    /**
     * Internal method to find or create a StoreProvider for a given user.
     */
    @Override
    protected StoreProvider<T> getStoreProvider()
    {
        ScriptSession session = WebContextFactory.get().getScriptSession();

        @SuppressWarnings("unchecked")
        StoreProvider<T> storeProvider = (StoreProvider<T>) session.getAttribute(attributeName);

        if (storeProvider == null)
        {
            storeProvider = factory.create(session);
            session.setAttribute(attributeName, storeProvider);
        }

        return storeProvider;
    }

    /**
     * The attribute name that we are storing created StoreProviders under in
     * the ScriptSession
     */
    private final String attributeName;

    /**
     * The factory to enable us to create factories per ScriptSession
     */
    private final StoreProviderFactory<T, ScriptSession> factory;
}
