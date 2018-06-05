package org.directwebremoting.json.types;

/**
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class JsonNull extends JsonValue
{
    /* (non-Javadoc)
     * @see org.directwebremoting.json.JsonValue#toExternalRepresentation()
     */
    @Override
    public String toExternalRepresentation()
    {
        return "null";
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        return toExternalRepresentation();
    }
}
