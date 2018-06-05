package org.directwebremoting.extend;

/**
 * TODO: Work out if this exception actually helps.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class AccessDeniedException extends SecurityException
{
    /**
     * Basic constructor - ensure all exceptions have messages
     * @param s The exception message
     */
    public AccessDeniedException(String s)
    {
        super(s);
    }
}
