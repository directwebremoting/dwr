package org.directwebremoting.json;

/**
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class InvalidJsonException extends Exception
{
    /**
     * 
     */
    public InvalidJsonException()
    {
    }

    /**
     * @param reason The reason for the failure
     */
    public InvalidJsonException(String reason)
    {
        super(reason);
    }

    /**
     * @param ex The cause of the failure
     */
    public InvalidJsonException(Throwable ex)
    {
        super(ex);
    }

    /**
     * @param reason The reason for the failure
     * @param ex The cause of the failure
     */
    public InvalidJsonException(String reason, Throwable ex)
    {
        super(reason, ex);
    }
}
