package org.directwebremoting.json.parse.impl;

import org.directwebremoting.json.parse.JsonDecoder;
import org.directwebremoting.json.parse.JsonParseException;

/**
 * A {@link JsonDecoder} that doesn't do anything, which is useful for
 * validations that don't need to get any data, just check it's validity.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class IgnoreJsonDecoder implements JsonDecoder
{
    /* (non-Javadoc)
     * @see org.directwebremoting.json.parse.JsonDecoder#getRoot()
     */
    public Object getRoot()
    {
        return null;
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.json.parse.JsonDecoder#addBoolean(java.lang.String, boolean)
     */
    public void addBoolean(String propertyName, boolean value) throws JsonParseException
    {
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.json.parse.JsonDecoder#addNull(java.lang.String)
     */
    public void addNull(String propertyName) throws JsonParseException
    {
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.json.parse.JsonDecoder#addNumber(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
     */
    public void addNumber(String propertyName, String intPart, String floatPart, String expPart) throws JsonParseException
    {
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.json.parse.JsonDecoder#addString(java.lang.String, java.lang.String)
     */
    public void addString(String propertyName, String value) throws JsonParseException
    {
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.json.parse.JsonDecoder#beginArray(java.lang.String)
     */
    public void beginArray(String propertyName) throws JsonParseException
    {
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.json.parse.JsonDecoder#beginObject(java.lang.String)
     */
    public void beginObject(String propertyName) throws JsonParseException
    {
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.json.parse.JsonDecoder#endArray(java.lang.String)
     */
    public void endArray(String propertyName) throws JsonParseException
    {
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.json.parse.JsonDecoder#endObject(java.lang.String)
     */
    public void endObject(String propertyName) throws JsonParseException
    {
    }
}
