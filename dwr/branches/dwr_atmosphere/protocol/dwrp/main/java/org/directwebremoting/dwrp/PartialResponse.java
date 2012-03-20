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
package org.directwebremoting.dwrp;

import java.util.HashMap;
import java.util.Map;

/**
 * A set of constants that represent how browsers need data flushed to them
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public enum PartialResponse
{
    /**
     * The client can not handle partial responses
     */
    NO("0"),

    /**
     * The client can handle partial responses
     */
    YES("1"),

    /**
     * The client can only handle partial responses with a 4k data post
     * (can be whitespace) - we're talking IE here.
     */
    FLUSH("2");

    /**
     * @param id The string that the browser passes for this value
     */
    private PartialResponse(String id)
    {
        this.id = id;
    }

    /**
     * The browser version of this value
     */
    private final String id;

    /**
     * A lookup table of browser strings to enum constants
     */
    private static final Map<String, PartialResponse> ids = new HashMap<String, PartialResponse>();

    /**
     * Setup the lookup table
     */
    static
    {
        for (PartialResponse partialResponse : PartialResponse.values())
        {
            ids.put(partialResponse.id, partialResponse);
        }
    }

    /**
     * Convert a string from the web into a PartialResponse. The values are
     * PARTIAL_RESPONSE_NO = "0", PARTIAL_RESPONSE_YES = "1" and
     * PARTIAL_RESPONSE_FLUSH = "2"
     * @param lookupid The PartialResponse to look-up
     * @return a matching PartialResponse or null if one was not found
     */
    public static PartialResponse fromOrdinal(String lookupid)
    {
        return ids.get(lookupid);
    }
}
