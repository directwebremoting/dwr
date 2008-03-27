package uk.ltd.getahead.dwr;

/**
 * Something has gone wrong when we were doing some conversion.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class ConversionException extends Exception
{
    /**
     * Default ctor
     */
    public ConversionException()
    {
    }

    /**
     * Construct a ConversionException with a description message
     * @param message error description
     */
    public ConversionException(String message)
    {
        super(message);
    }

    /**
     * Construct a ConversionException with a description message and exception
     * @param message error description
     * @param ex error stack trace
     */
    public ConversionException(String message, Throwable ex)
    {
        super(message);
        this.ex = ex;
    }

    /**
     * Construct a ConversionException with an exception
     * @param ex error stack trace
     */
    public ConversionException(Throwable ex)
    {
        super(ex.getMessage());
        this.ex = ex;
    }

    /**
     * @return The cause of this exception (if any)
     */
    public Throwable getCause()
    {
        return ex;
    }

    /**
     * Stored exception cause
     */
    private Throwable ex = null;
}
