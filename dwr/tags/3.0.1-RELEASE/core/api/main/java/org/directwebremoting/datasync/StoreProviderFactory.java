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

import javax.servlet.http.HttpSession;

import org.directwebremoting.ScriptSession;

/**
 * The PerXStoreProviders (like {@link PerHttpSessionStoreProvider} and
 * {@link PerScriptSessionStoreProvider}) need a way to create
 * {@link StoreProvider}s for a given Y (in the above example Y is either
 * {@link HttpSession} or {@link ScriptSession}).
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public interface StoreProviderFactory<X, Y>
{
    /**
     * Create a new StoreProvider for a given Y.
     * For container classes like HttpSession etc, this method does NOT need
     * to store the created object. That is done by the PerXStoreProvider
     * implementation.
     */
    StoreProvider<X> create(Y y);
}
