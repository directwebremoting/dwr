package uk.ltd.getahead.dwr.util;

/**
 * We don't want to force users to use commons-logging, but there are no
 * logging APIs available at 1.3 so this lets us use Servlet.log if
 * commons-logging is not available.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public interface LoggingOutput
{
    /**
     * Log a debug message
     * @param message The text to log
     */
    public void debug(String message);

    /**
     * Log an info message
     * @param message The text to log
     */
    public void info(String message);

    /**
     * Log a warning message
     * @param message The text to log
     */
    public void warn(String message);

    /**
     * Log a warning message
     * @param message The text to log
     * @param th An optional stack trace
     */
    public void warn(String message, Throwable th);

    /**
     * Log an error message
     * @param message The text to log
     */
    public void error(String message);

    /**
     * Log an error message
     * @param message The text to log
     * @param th An optional stack trace
     */
    public void error(String message, Throwable th);

    /**
     * Log a fatal error message
     * @param message The text to log
     */
    public void fatal(String message);

    /**
     * Log a fatal error message
     * @param message The text to log
     * @param th An optional stack trace
     */
    public void fatal(String message, Throwable th);
}
