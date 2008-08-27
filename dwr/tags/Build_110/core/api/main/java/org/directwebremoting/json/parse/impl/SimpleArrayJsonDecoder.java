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
package org.directwebremoting.json.parse.impl;

import java.util.ArrayList;
import java.util.List;

import org.directwebremoting.json.parse.JsonParseException;

/**
 * Convert JSON to simple Java types, Map&lt;String, Object&gt;, List&lt;Object&gt;
 * String, BigDecimal, int, etc. This expects that the top level type in the JSON
 * will be an array
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class SimpleArrayJsonDecoder extends SimpleJsonDecoder<List<Object>>
{
    /* (non-Javadoc)
     * @see org.directwebremoting.json.parse.impl.AbstractJsonDecoder#createArrayForRoot(java.lang.Object)
     */
    @Override
    protected List<Object> createArrayForRoot(Object parent) throws JsonParseException
    {
        return new ArrayList<Object>();
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.json.parse.impl.AbstractJsonDecoder#createObjectForRoot()
     */
    @Override
    protected List<Object> createObjectForRoot() throws JsonParseException
    {
        throw new JsonParseException("Expected root to be an array");
    }
}
