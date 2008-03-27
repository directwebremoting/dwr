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

/**
 * Various constants for type conversion
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 * @noinspection InterfaceNeverImplemented
 */
public interface ProtocolConstants
{
    /**
     * The name for reference types from javascript
     */
    public static final String TYPE_REFERENCE = "reference";

    /**
     * The name for string types from javascript
     */
    public static final String TYPE_STRING = "string";

    /**
     * How many calls are there in this request?
     */
    public static final String INBOUND_CALL_COUNT = "callCount";

    /**
     * The prefix to the call number on inbound calls
     */
    public static final String INBOUND_CALLNUM_PREFIX = "c";

    /**
     * The suffix to the call number on inbound calls
     */
    public static final String INBOUND_CALLNUM_SUFFIX = "-";

    /**
     * Used to define the id of this call
     */
    public static final String INBOUND_KEY_ID = "id";

    /**
     * The parameter that specifies the http session identifier
     */
    public static final String INBOUND_KEY_HTTP_SESSIONID = "httpSessionId";

    /**
     * The parameter that specifies the batch identifier
     */
    public static final String INBOUND_KEY_BATCHID = "batchId";

    /**
     * The parameter that specifies the script session identifier
     */
    public static final String INBOUND_KEY_SCRIPT_SESSIONID = "scriptSessionId";

    /**
     * The parameter that specifies the current page
     */
    public static final String INBOUND_KEY_PAGE = "page";

    /**
     * The inbound key to define the class to be used.
     * This will be the javascript version of the full java package name.
     */
    public static final String INBOUND_KEY_SCRIPTNAME = "scriptName";

    /**
     * The inbound key to define the method to be called
     */
    public static final String INBOUND_KEY_METHODNAME = "methodName";

    /**
     * What prefix do we put to the input paramters
     */
    public static final String INBOUND_KEY_PARAM = "param";

    /**
     * What prefix do we put to the environment paramters
     */
    public static final String INBOUND_KEY_ENV = "e";

    /**
     * What prefix do we put to the meta-data paramters
     */
    public static final String INBOUND_KEY_METADATA = "p-";

    /**
     * The inbound key to declare if the XHR.responseText is readable when half filled
     */
    public static final String INBOUND_KEY_PARTIAL_RESPONSE = "partialResponse";

    /**
     * The character to use to distinguish between the variable name and the
     * variable value
     */
    public static final String INBOUND_DECL_SEPARATOR = "=";

    /**
     * The character that we use to separate types for values in inbound
     * variables
     */
    public static final String INBOUND_TYPE_SEPARATOR = ":";

    /**
     * How are javascript arrays begun
     */
    public static final String INBOUND_ARRAY_END = "]";

    /**
     * How are javascript arrays ended
     */
    public static final String INBOUND_ARRAY_START = "[";

    /**
     * How are javascript array elements separated
     */
    public static final String INBOUND_ARRAY_SEPARATOR = ",";

    /**
     * How javascript associative arrays (maps) are started
     */
    public static final String INBOUND_MAP_START = "{";

    /**
     * How javascript associative arrays (maps) are ended
     */
    public static final String INBOUND_MAP_END = "}";

    /**
     * How javascript map entries are split from each other
     */
    public static final String INBOUND_MAP_SEPARATOR = ",";

    /**
     * How javascript map entries are split into name/value pairs
     */
    public static final String INBOUND_MAP_ENTRY = ":";

    /**
     * null is sometimes needed in a javascript map
     */
    public static final String INBOUND_NULL = "null";

    /**
     * The marker to indicate that the output is from data inserted into the call
     */
    public static final String SCRIPT_CALL_INSERT = "//#DWR-INSERT";

    /**
     * The marker to indicate that the output is a reply to the call
     */
    public static final String SCRIPT_CALL_REPLY = "//#DWR-REPLY";

    /**
     * The marker to indicate the start of a evalable script block
     */
    public static final String SCRIPT_START_MARKER = "//#DWR-START#";

    /**
     * The marker to indicate the end of a evalable script block
     */
    public static final String SCRIPT_END_MARKER = "//#DWR-END#";
}
