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
package org.directwebremoting.extend;

import org.directwebremoting.ui.Callback;

/**
 * TODO: can we make this work without a factory?
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public interface CallbackHelper
{
    /**
     * Used by the many proxy classes to record a callback for later calling
     * @param callback The callback that acts on the type
     * @param type The type of the data that we are recording
     * @return A key under which the data is stored
     */
    public <T> String saveCallback(Callback<T> callback, Class<T> type);
}
