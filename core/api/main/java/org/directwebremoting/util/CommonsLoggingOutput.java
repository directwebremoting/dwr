package org.directwebremoting.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * This class is intended to be used by Logger when commons-logging is
 * available, but to not force Logger itself to depend on commons-logging so
 * Logger can catch the ClassDefNotFoundError and use other methods.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class CommonsLoggingOutput implements LoggingOutput
{
    /**
     * Create a logger specific to commons-logging
     * @param base The class to log against.
     */
    public CommonsLoggingOutput(Class<?> base)
    {
        log = LogFactory.getLog(base);
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.util.LoggingOutput#debug(java.lang.String)
     */
    public void debug(String message)
    {
        log.debug(message);
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.util.LoggingOutput#info(java.lang.String)
     */
    public void info(String message)
    {
        log.info(message);
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.util.LoggingOutput#warn(java.lang.String)
     */
    public void warn(String message)
    {
        log.warn(message);
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.util.LoggingOutput#warn(java.lang.String, java.lang.Throwable)
     */
    public void warn(String message, Throwable th)
    {
        log.warn(message, th);
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.util.LoggingOutput#error(java.lang.String)
     */
    public void error(String message)
    {
        log.error(message);
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.util.LoggingOutput#error(java.lang.String, java.lang.Throwable)
     */
    public void error(String message, Throwable th)
    {
        log.error(message, th);
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.util.LoggingOutput#fatal(java.lang.String)
     */
    public void fatal(String message)
    {
        log.fatal(message);
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.util.LoggingOutput#fatal(java.lang.String, java.lang.Throwable)
     */
    public void fatal(String message, Throwable th)
    {
        log.fatal(message, th);
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.util.LoggingOutput#isDebugEnabled()
     */
    public boolean isDebugEnabled()
    {
        return log.isDebugEnabled();
    }

    private final Log log;
}
