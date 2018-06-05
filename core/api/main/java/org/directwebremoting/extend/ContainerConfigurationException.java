package org.directwebremoting.extend;

/**
 * Something has gone wrong while configuring a Container
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class ContainerConfigurationException extends RuntimeException
{
    /**
     * Constructs a new runtime exception with the specified detail message.
     * The cause is not initialized.
     * @param message the detail message.
     */
    public ContainerConfigurationException(String message)
    {
        super(message);
    }

    /**
     * Constructs a new runtime exception with the specified detail message and
     * cause.
     * Note that the detail message associated with <code>cause</code> is
     * not automatically incorporated in this runtime exception's detail message.
     * @param message the detail message (which is saved for later retrieval
     *        by the {@link #getMessage()} method).
     * @param cause the cause
     */
    public ContainerConfigurationException(String message, Throwable cause)
    {
        super(message, cause);
    }

    /**
     * Constructs a new runtime exception with the specified cause and a
     * detail message of <tt>(cause==null ? null : cause.toString())</tt>
     * (which typically contains the class and detail message of
     * <tt>cause</tt>).  This constructor is useful for runtime exceptions
     * that are little more than wrappers for other throwables.
     * @param cause the cause
     */
    public ContainerConfigurationException(Throwable cause)
    {
        super(cause);
    }
}
