package org.directwebremoting.json.types;

/**
 * The Json version of a boolean
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class JsonBoolean extends JsonValue
{
    /**
     * All JsonBoolean wrap a Java boolean value
     */
    public JsonBoolean(boolean value)
    {
        this.value = value;
    }

    /**
     * All JsonBoolean wrap a Java boolean value
     */
    public JsonBoolean(String text)
    {
        value = Boolean.parseBoolean(text);
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.json.JsonValue#getString()
     */
    @Override
    public boolean getBoolean()
    {
        return value;
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.json.JsonValue#toExternalRepresentation()
     */
    @Override
    public String toExternalRepresentation()
    {
        return Boolean.toString(value);
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        return toExternalRepresentation();
    }

    /**
     * The string value that we wrap
     */
    private final boolean value;
}
