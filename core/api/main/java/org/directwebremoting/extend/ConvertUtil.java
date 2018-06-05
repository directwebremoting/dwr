package org.directwebremoting.extend;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class ConvertUtil
{
    /**
     * splitInbound() returns the type info in this parameter
     */
    public static final int INBOUND_INDEX_TYPE = 0;

    /**
     * splitInbound() returns the value info in this parameter
     */
    public static final int INBOUND_INDEX_VALUE = 1;

    /**
     * The javascript outbound marshaller prefixes the toString value with a
     * colon and the original type information. This undoes that.
     * @param data The string to be split up
     * @return A string array containing the split data
     */
    public static String[] splitInbound(String data)
    {
        String[] reply = new String[2];
    
        int colon = data.indexOf(ProtocolConstants.INBOUND_TYPE_SEPARATOR);
        if (colon == -1)
        {
            log.error("Missing : in conversion data (" + data + ')');
            reply[INBOUND_INDEX_TYPE] = ProtocolConstants.TYPE_STRING;
            reply[INBOUND_INDEX_VALUE] = data;
        }
        else
        {
            reply[INBOUND_INDEX_TYPE] = data.substring(0, colon);
            reply[INBOUND_INDEX_VALUE] = data.substring(colon + 1);
        }
    
        return reply;
    }

    /**
     * The log stream
     */
    private static final Log log = LogFactory.getLog(ConvertUtil.class);
}
