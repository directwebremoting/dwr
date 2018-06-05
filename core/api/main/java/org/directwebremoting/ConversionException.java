package org.directwebremoting;

/**
 * Something has gone wrong when we were doing some conversion.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class ConversionException extends RuntimeException
{
    /**
     * Default ctor
     * @param paramType The type we were trying to marshall
     */
    public ConversionException(Class<?> paramType)
    {
        this(paramType, null, null);
    }

    /**
     * Construct a ConversionException with an exception and a destination type
     * @param paramType The type we were trying to marshall
     * @param ex error stack trace
     */
    public ConversionException(Class<?> paramType, Throwable ex)
    {
        this(paramType, null, ex);
    }

    /**
     * Construct a ConversionException with a description message and exception
     * @param paramType The type we were trying to marshall
     * @param message error description
     */
    public ConversionException(Class<?> paramType, String message)
    {
        this(paramType, message, null);
    }

    /**
     * Construct a ConversionException with a description message and exception
     * @param paramType The type we were trying to marshall
     * @param message error description
     */
    public ConversionException(Class<?> paramType, String message, Throwable ex)
    {
        super(message != null ? message : "Error marshalling data. See the logs for more details.", ex);
        this.paramType = paramType;
    }

    /**
     * Accessor for the type we are converting to/from
     * @return The type we are converting to/from
     */
    public Class<?> getConversionType()
    {
        return paramType;
    }

    /**
     * The type we are converting to/from
     */
    private Class<?> paramType;
}
