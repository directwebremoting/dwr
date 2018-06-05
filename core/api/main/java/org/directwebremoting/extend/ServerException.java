package org.directwebremoting.extend;

/**
 * Something has gone wrong when we were doing some conversion.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class ServerException extends Exception
{
    /**
     * Default ctor
     */
    public ServerException()
    {
    }

    /**
     * Construct a ServerException with a description message and exception
     * @param message error description
     */
    public ServerException(String message)
    {
        super(message);
    }

    /**
     * Construct a ServerException with a description message and exception
     * @param message error description
     * @param ex error stack trace
     */
    public ServerException(String message, Throwable ex)
    {
        super(message, ex);
    }

    /**
     * Construct a ServerException with an exception
     * @param ex error stack trace
     */
    public ServerException(Throwable ex)
    {
        super(ex.getMessage(), ex);
    }
}
