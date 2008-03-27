package uk.ltd.getahead.dwr;

/**
 * Various constants for type conversion
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class ConversionConstants
{
    /**
     * The name for reference types from javascript
     */
    protected static final String TYPE_REFERENCE = "reference"; //$NON-NLS-1$

    /**
     * The name for string types from javascript
     */
    protected static final String TYPE_STRING = "string"; //$NON-NLS-1$

    /**
     * The character that we use to separate types for values in inbound
     * variables
     */
    protected static final String INBOUND_TYPE_SEPARATOR = ":"; //$NON-NLS-1$

    /**
     * What prefix do we put to the input paramters
     */
    protected static final String PARAM_PREFIX = "param"; //$NON-NLS-1$

    /**
     * The character to use to distinguish between the variable name and the
     * variable value
     */
    protected static final String INBOUND_DECL_SEPARATOR = "="; //$NON-NLS-1$

    /**
     * The inbound key to define the method to be called
     */
    protected static final String INBOUND_KEY_METHODNAME = "methodname"; //$NON-NLS-1$

    /**
     * The inbound key to define the class to be used. This will be the
     * javascript version of the full java package name
     */
    protected static final String INBOUND_KEY_CLASSNAME = "classname"; //$NON-NLS-1$

    /**
     * Used to define the id of this call
     */
    protected static final String INBOUND_KEY_ID = "id"; //$NON-NLS-1$

    /**
     * The key to define if we are in XMLRPC mode
     */
    protected static final String INBOUND_KEY_XMLMODE = "xml"; //$NON-NLS-1$

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
}
