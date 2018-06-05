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
    YES("1");

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
     * PARTIAL_RESPONSE_NO = "0", PARTIAL_RESPONSE_YES = "1"
     * @param lookupid The PartialResponse to look-up
     * @return a matching PartialResponse or null if one was not found
     */
    public static PartialResponse fromOrdinal(String lookupid)
    {
        return ids.get(lookupid);
    }
}
