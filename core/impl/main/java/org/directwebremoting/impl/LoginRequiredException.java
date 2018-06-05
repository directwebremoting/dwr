package org.directwebremoting.impl;

/**
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class LoginRequiredException extends SecurityException
{
    /**
     * @param s The exception message
     */
    public LoginRequiredException(String s)
    {
        super(s);
    }
}
