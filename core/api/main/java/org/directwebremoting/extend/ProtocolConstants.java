package org.directwebremoting.extend;

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
     * The name for file types from javascript
     */
    public static final String TYPE_FILE = "file";

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
     * The parameter that specifies DWR's session identifier
     */
    public static final String INBOUND_KEY_DWR_SESSIONID = "dwrSessionId";

    /**
     * The parameter that specifies the batch identifier
     */
    public static final String INBOUND_KEY_BATCHID = "batchId";

    /**
     * The parameter that specifies the instance identifier
     */
    public static final String INBOUND_KEY_INSTANCEID = "instanceId";

    /**
     * The parameter that specifies the script session identifier
     */
    public static final String INBOUND_KEY_SCRIPT_SESSIONID = "scriptSessionId";

    /**
     * The parameter that specifies the next reverse ajax index
     */
    public static final String INBOUND_KEY_NEXT_REVERSE_AJAX_INDEX = "nextReverseAjaxIndex";

    /**
     * The parameter that specifies the client's document.domain setting if modified
     */
    public static final String INBOUND_KEY_DOCUMENT_DOMAIN = "documentDomain";

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
     * What prefix do we put to the input parameters
     */
    public static final String INBOUND_KEY_PARAM = "param";

    /**
     * What prefix do we put to the environment parameters
     */
    public static final String INBOUND_KEY_ENV = "e";

    /**
     * What prefix do we put to the meta-data parameters
     */
    public static final String INBOUND_KEY_METADATA = "a-";

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
     * A special value for handling varargs
     */
    public static final String INBOUND_VARARGS = "varargs";

    /**
     * The marker to indicate that the output is from data inserted into the call
     */
    public static final String SCRIPT_CALL_INSERT = "//#DWR-INSERT";

    /**
     * The marker to indicate that the output is a reply to the call
     */
    public static final String SCRIPT_CALL_REPLY = "//#DWR-REPLY";

    /**
     * The marker to indicate the start of a 'eval'able script block
     */
    public static final String SCRIPT_START_MARKER = "//#DWR-START#";

    /**
     * The marker to indicate the end of a 'eval'able script block
     */
    public static final String SCRIPT_END_MARKER = "//#DWR-END#";

    /**
     * How long do we wait  before closing the connection after a write for clients that
     * don't support streaming.
     */
    public static final int FALLBACK_MAX_WAIT_AFTER_WRITE = 500;
}
