package org.directwebremoting.json.types;

import java.util.Locale;

import org.directwebremoting.util.JavascriptUtil;

/**
 * The Json version of a String
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class JsonString extends JsonValue
{
    /**
     * All JsonStrings wrap a Java string
     */
    public JsonString(String value)
    {
        this.value = value;
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.json.JsonValue#getString()
     */
    @Override
    public String getString()
    {
        return value;
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.json.JsonValue#toExternalRepresentation()
     */
    @Override
    public String toExternalRepresentation()
    {
        return "'" + JavascriptUtil.escapeJavaScript(value, false) + "'";
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        return getString();
    }

    /**
     * <p>Returns an upper case hexadecimal <code>String</code> for the given
     * character.</p>
     * @param ch The character to convert.
     * @return An upper case hexadecimal <code>String</code>
     */
    private static String hex(char ch)
    {
        return Integer.toHexString(ch).toUpperCase(Locale.ENGLISH);
    }

    /**
     * The string value that we wrap
     */
    private final String value;
}
