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
 * A class to help with the use of {@link Callback}s
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class CallbackHelperFactory
{
    public static <T> String saveCallback(Callback<T> callback, Class<T> type)
    {
        return get().saveCallback(callback, type);
    }

    /**
     * Accessor for the current CallbackHelper.
     * @return The current CallbackHelper or null if the current thread was not
     * started by DWR.
     */
    public static CallbackHelper get()
    {
        if (builder == null)
        {
            return null;
        }

        return builder.get();
    }

    /**
     * Class to enable us to access servlet parameters.
     */
    public interface CallbackHelperBuilder
    {
        /**
         * @return The CallbackHelper that is associated with this thread
         */
        CallbackHelper get();
    }

    /**
     * The CallbackHelperBuilder from which we will get CallbackHelper objects
     */
    private static CallbackHelperBuilder builder = null;

    /**
     * Internal method to allow us to get the CallbackHelperBuilder from which we
     * will get CallbackHelper objects.
     * Do not call this method from outside of DWR.
     * @param builder The factory object (from DwrServlet)
     */
    public static void setCallbackHelperBuilder(CallbackHelperBuilder builder)
    {
        CallbackHelperFactory.builder = builder;
    }
}
