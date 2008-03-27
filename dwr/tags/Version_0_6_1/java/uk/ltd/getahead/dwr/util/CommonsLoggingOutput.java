package uk.ltd.getahead.dwr.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * This class is intended to be used by Log when commons-logging is available,
 * but to not force Log itself to depend on commons-logging so Log can catch
 * the ClassDefNotFoundError and use other methods.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class CommonsLoggingOutput implements LoggingOutput
{
    /* (non-Javadoc)
     * @see uk.ltd.getahead.dwr.util.LoggingOutput#debug(java.lang.String)
     */
    public void debug(String message)
    {
        log.debug(message);
    }

    /* (non-Javadoc)
     * @see uk.ltd.getahead.dwr.util.LoggingOutput#info(java.lang.String)
     */
    public void info(String message)
    {
        log.info(message);
    }

    /* (non-Javadoc)
     * @see uk.ltd.getahead.dwr.util.LoggingOutput#warn(java.lang.String)
     */
    public void warn(String message)
    {
        log.warn(message);
    }

    /* (non-Javadoc)
     * @see uk.ltd.getahead.dwr.util.LoggingOutput#warn(java.lang.String, java.lang.Throwable)
     */
    public void warn(String message, Throwable th)
    {
        log.warn(message, th);
    }

    /* (non-Javadoc)
     * @see uk.ltd.getahead.dwr.util.LoggingOutput#error(java.lang.String)
     */
    public void error(String message)
    {
        log.error(message);
    }

    /* (non-Javadoc)
     * @see uk.ltd.getahead.dwr.util.LoggingOutput#error(java.lang.String, java.lang.Throwable)
     */
    public void error(String message, Throwable th)
    {
        log.error(message, th);
    }

    /* (non-Javadoc)
     * @see uk.ltd.getahead.dwr.util.LoggingOutput#fatal(java.lang.String)
     */
    public void fatal(String message)
    {
        log.fatal(message);
    }

    /* (non-Javadoc)
     * @see uk.ltd.getahead.dwr.util.LoggingOutput#fatal(java.lang.String, java.lang.Throwable)
     */
    public void fatal(String message, Throwable th)
    {
        log.fatal(message, th);
    }

    private static Log log = LogFactory.getLog(uk.ltd.getahead.dwr.util.Log.class);
}
