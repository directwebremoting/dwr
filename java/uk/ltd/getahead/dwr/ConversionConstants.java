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
package uk.ltd.getahead.dwr;

/**
 * Various constants for type conversion
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class ConversionConstants
{
    /**
     * Is this an instance of a broken copy of safari v2
     */
    public static final String BROKEN_SAFARI2 = "isBrokenSafari2"; //$NON-NLS-1$

    /**
     * The name for reference types from javascript
     */
    public static final String TYPE_REFERENCE = "reference"; //$NON-NLS-1$

    /**
     * The name for string types from javascript
     */
    public static final String TYPE_STRING = "string"; //$NON-NLS-1$

    /**
     * How many calls are there in this request?
     */
    public static final String INBOUND_CALL_COUNT = "callCount"; //$NON-NLS-1$

    /**
     * The prefix to the call number on inbound calls
     */
    public static final String INBOUND_CALLNUM_PREFIX = "c"; //$NON-NLS-1$

    /**
     * The suffix to the call number on inbound calls
     */
    public static final String INBOUND_CALLNUM_SUFFIX = "-"; //$NON-NLS-1$

    /**
     * Used to define the id of this call
     */
    public static final String INBOUND_KEY_ID = "id"; //$NON-NLS-1$

    /**
     * The inbound key to define the class to be used.
     * This will be the javascript version of the full java package name.
     */
    public static final String INBOUND_KEY_SCRIPTNAME = "scriptName"; //$NON-NLS-1$

    /**
     * The inbound key to define the method to be called
     */
    public static final String INBOUND_KEY_METHODNAME = "methodName"; //$NON-NLS-1$

    /**
     * What prefix do we put to the input paramters
     */
    public static final String INBOUND_KEY_PARAM = "param"; //$NON-NLS-1$

    /**
     * What prefix do we put to the environment paramters
     */
    public static final String INBOUND_KEY_ENV = "e"; //$NON-NLS-1$

    /**
     * The character to use to distinguish between the variable name and the
     * variable value
     */
    public static final String INBOUND_DECL_SEPARATOR = "="; //$NON-NLS-1$

    /**
     * The character that we use to separate types for values in inbound
     * variables
     */
    public static final String INBOUND_TYPE_SEPARATOR = ":"; //$NON-NLS-1$

    /**
     * The key to define if we are in XMLRPC mode
     */
    public static final String INBOUND_KEY_XMLMODE = "xml"; //$NON-NLS-1$

    /**
     * How are javascript arrays begun
     */
    public static final String INBOUND_ARRAY_END = "]"; //$NON-NLS-1$

    /**
     * How are javascript arrays ended
     */
    public static final String INBOUND_ARRAY_START = "["; //$NON-NLS-1$

    /**
     * How are javascript array elements separated
     */
    public static final String INBOUND_ARRAY_SEPARATOR = ","; //$NON-NLS-1$

    /**
     * How javascript associative arrays (maps) are started
     */
    public static final String INBOUND_MAP_START = "{"; //$NON-NLS-1$

    /**
     * How javascript associative arrays (maps) are ended
     */
    public static final String INBOUND_MAP_END = "}"; //$NON-NLS-1$

    /**
     * How javascript map entries are split from each other
     */
    public static final String INBOUND_MAP_SEPARATOR = ","; //$NON-NLS-1$

    /**
     * How javascript map entries are split into name/value pairs
     */
    public static final String INBOUND_MAP_ENTRY = ":"; //$NON-NLS-1$

    /**
     * null is sometimes needed in a javascript map
     */
    public static final String INBOUND_NULL = "null"; //$NON-NLS-1$

    /**
     * Prevent instansiation
     */
    private ConversionConstants()
    {
    }
}
