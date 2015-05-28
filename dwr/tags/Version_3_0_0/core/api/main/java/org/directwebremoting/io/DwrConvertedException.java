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

import java.util.Map;

/**
 * DwrConvertedException are automatically exported by DWR along with the
 * message and (optionally) a map of data. It is the developers job to ensure
 * that all the objects in the data map are marked for conversion.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class DwrConvertedException extends RuntimeException
{
    /**
     * @param message The message to pass to the exceptionHandler in JavaScript
     */
    public DwrConvertedException(String message)
    {
        super(message);
    }

    /**
     * @param message The message to pass to the exceptionHandler in JavaScript
     */
    public DwrConvertedException(String message, Map<?, ?> data)
    {
        super(message);
        this.data = data;
    }

    /**
     * @return the data
     */
    public Map<?, ?> getData()
    {
        return data;
    }

    private Map<?, ?> data;
}
