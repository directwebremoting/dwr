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
package com.example.dwr.address;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class AddressLookup
{

    private static final String LINE4 = "line4";

    private static final String LINE3 = "line3";

    private static final String LINE2 = "line2";

    /**
     * @param origpostcode the code to lookup
     * @return a map of postcode data
     */
    public Map<String, String> fillAddress(String origpostcode)
    {
        Map<String, String> reply = new HashMap<String, String>();
        String postcode = origpostcode.replace(" ", "");

        if ("LE167TR".equalsIgnoreCase(postcode))
        {
            reply.put(LINE2, "Church Lane");
            reply.put(LINE3, "Thorpe Langton");
            reply.put(LINE4, "MARKET HARBOROUGH");
        }
        else if ("NR147SL".equalsIgnoreCase(postcode))
        {
            reply.put(LINE2, "Rectory Lane");
            reply.put(LINE3, "Poringland");
            reply.put(LINE4, "NORWICH");
        }
        else if ("B927TT".equalsIgnoreCase(postcode))
        {
            reply.put(LINE2, "Olton Mere");
            reply.put(LINE3, "Warwick Road");
            reply.put(LINE4, "SOLIHULL");
        }
        else if ("E178YT".equalsIgnoreCase(postcode))
        {
            reply.put(LINE2, "");
            reply.put(LINE3, "PO Box 43108 ");
            reply.put(LINE4, "LONDON");
        }
        else if ("SN48QS".equalsIgnoreCase(postcode))
        {
            reply.put(LINE2, "Binknoll");
            reply.put(LINE3, "Wootton Bassett");
            reply.put(LINE4, "SWINDON");
        }
        else if ("NN57HT".equalsIgnoreCase(postcode))
        {
            reply.put(LINE2, "Heathville");
            reply.put(LINE3, "");
            reply.put(LINE4, "NORTHAMPTON");
        }
        else
        {
            reply.put(LINE2, "Postcode not found");
            reply.put(LINE3, "");
            reply.put(LINE4, "");
        }

        return reply;
    }
}
