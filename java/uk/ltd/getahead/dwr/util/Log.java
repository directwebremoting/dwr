package uk.ltd.getahead.dwr.util;

/**
 * A very quick and dirty logging implementation.
 * <code>java.util.logging</code> is out because we work with JDK 1.3 and we
 * don't want to force users to import log4j or commons-logging.
 * Don't use this outside of DWR - it's just a quick hack to keep things simple. 
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public final class Log
{
    /**
     * Prevent instansiation
     */
    private Log()
    {
    }

    /**
     * The logging implementation
     */
    private static LoggingOutput output;

    /**
     * 
     */
    static
    {
        try
        {
            output = new CommonsLoggingOutput();
            output.info("Logging using commons-logging."); //$NON-NLS-1$
        }
        catch (NoClassDefFoundError ex)
        {
            output = new ServletLoggingOutput();
            output.info("Logging using servlet.log."); //$NON-NLS-1$
        }
    }

    /**
     * Log a debug message
     * @param message The text to log
     */
    public static void debug(String message)
    {
        output.debug(message);
    }

    /**
     * Log an info message
     * @param message The text to log
     */
    public static void info(String message)
    {
        output.info(message);
    }

    /**
     * Log a warning message
     * @param message The text to log
     */
    public static void warn(String message)
    {
        output.warn(message);
    }

    /**
     * Log a warning message
     * @param message The text to log
     * @param th An optional stack trace
     */
    public static void warn(String message, Throwable th)
    {
        output.warn(message, th);
    }

    /**
     * Log an error message
     * @param message The text to log
     */
    public static void error(String message)
    {
        output.error(message);
    }

    /**
     * Log an error message
     * @param message The text to log
     * @param th An optional stack trace
     */
    public static void error(String message, Throwable th)
    {
        output.error(message, th);
    }

    /**
     * Log a fatal error message
     * @param message The text to log
     */
    public static void fatal(String message)
    {
        output.fatal(message);
    }

    /**
     * Log a fatal error message
     * @param message The text to log
     * @param th An optional stack trace
     */
    public static void fatal(String message, Throwable th)
    {
        output.fatal(message, th);
    }
}
