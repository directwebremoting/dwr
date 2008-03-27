package uk.ltd.getahead.dwr.util;

/**
 * A very quick and dirty logging implementation.
 * <code>java.util.logging</code> is out because we work with JDK 1.3 and we
 * don't want to force users to import log4j or commons-logging.
 * Don't use this outside of DWR - it's just a quick hack to keep things simple. 
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public final class Logger
{
    /**
     * @param base The class to log against
     * @return A new logger
     */
    public static Logger getLogger(Class base)
    {
        return new Logger(base);
    }

    /**
     * Prevent instansiation
     * @param base The class to log against
     */
    private Logger(Class base)
    {
        if (!commonsLoggingTried)
        {
            try
            {
                LoggingOutput internal = new CommonsLoggingOutput(Logger.class);
                internal.info("Logging using commons-logging."); //$NON-NLS-1$
                commonsLoggingAvailable = true;
            }
            catch (NoClassDefFoundError ex)
            {
                LoggingOutput internal = new ServletLoggingOutput();
                internal.info("Logging using servlet.log."); //$NON-NLS-1$
                commonsLoggingAvailable = false;
            }

            commonsLoggingTried = true;
        }

        if (commonsLoggingAvailable)
        {
            output = new CommonsLoggingOutput(base);
        }
        else
        {
            output = new ServletLoggingOutput();
        }
    }

    /**
     * The logging implementation
     */
    private LoggingOutput output;

    private static boolean commonsLoggingTried = false;
    private static boolean commonsLoggingAvailable = false;

    /**
     * Logger a debug message
     * @param message The text to log
     */
    public void debug(String message)
    {
        output.debug(message);
    }

    /**
     * Logger an info message
     * @param message The text to log
     */
    public void info(String message)
    {
        output.info(message);
    }

    /**
     * Logger a warning message
     * @param message The text to log
     */
    public void warn(String message)
    {
        output.warn(message);
    }

    /**
     * Logger a warning message
     * @param message The text to log
     * @param th An optional stack trace
     */
    public void warn(String message, Throwable th)
    {
        output.warn(message, th);
    }

    /**
     * Logger an error message
     * @param message The text to log
     */
    public void error(String message)
    {
        output.error(message);
    }

    /**
     * Logger an error message
     * @param message The text to log
     * @param th An optional stack trace
     */
    public void error(String message, Throwable th)
    {
        output.error(message, th);
    }

    /**
     * Logger a fatal error message
     * @param message The text to log
     */
    public void fatal(String message)
    {
        output.fatal(message);
    }

    /**
     * Logger a fatal error message
     * @param message The text to log
     * @param th An optional stack trace
     */
    public void fatal(String message, Throwable th)
    {
        output.fatal(message, th);
    }

    /**
     * Save CPU time when we are not debugging
     * @return true if debugging is enabled
     */
    public boolean isDebugEnabled()
    {
        return output.isDebugEnabled();
    }
}
