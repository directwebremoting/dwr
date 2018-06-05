package org.directwebremoting.json.parse;

/**
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class JsonParseException extends Exception
{
    /**
     *
     */
    public JsonParseException()
    {
    }

    /**
     * @param message
     */
    public JsonParseException(String message)
    {
        super(message);
    }

    /**
     * @param cause
     */
    public JsonParseException(Throwable cause)
    {
        super(cause);
    }

    /**
     * @param message
     * @param cause
     */
    public JsonParseException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
