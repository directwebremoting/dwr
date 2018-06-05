package org.directwebremoting.jsonp;

/**
 * Called when a JSON request is not formatted properly
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class JsonpCallException extends RuntimeException
{
    /**
     * All JsonCallExceptions must have a reason
     */
    public JsonpCallException(String reason)
    {
        super(reason);
    }
}
