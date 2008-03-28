package uk.ltd.getahead.dwr.util;

import javax.servlet.http.HttpServlet;

/**
 * A very quick and dirty logging implementation.
 * <code>java.util.logging</code> is out because we work with JDK 1.3 and we
 * don't want to force users to import log4j or commons-logging.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class Log
{
    /**
     * Prevent instansiation
     */
    private Log()
    {
    }

    /**
     * Something has gone very badly wrong.
     * Processing is likely to stop.
     */
    public static final int LEVEL_FATAL = 5;

    /**
     * Something has gone wrong with the current request.
     * The user will notice that we've broken something.
     */
    public static final int LEVEL_ERROR = 4;

    /**
     * Something has gone wrong, but it could well be the users fault.
     * No need to panic yet.
     */
    public static final int LEVEL_WARN = 3;

    /**
     * An event happened that we might need to keep track of.
     */
    public static final int LEVEL_INFO = 2;

    /**
     * Testing information.
     */
    public static final int LEVEL_DEBUG = 1;

    /**
     * Log a debug message
     * @param message The text to log
     */
    public static void debug(String message)
    {
        log(LEVEL_DEBUG, message, null);
    }

    /**
     * Log an info message
     * @param message The text to log
     */
    public static void info(String message)
    {
        log(LEVEL_INFO, message, null);
    }

    /**
     * Log a warning message
     * @param message The text to log
     */
    public static void warn(String message)
    {
        log(LEVEL_WARN, message, null);
    }

    /**
     * Log a warning message
     * @param message The text to log
     * @param th An optional stack trace
     */
    public static void warn(String message, Throwable th)
    {
        log(LEVEL_WARN, message, th);
    }

    /**
     * Log an error message
     * @param message The text to log
     */
    public static void error(String message)
    {
        log(LEVEL_ERROR, message, null);
    }

    /**
     * Log an error message
     * @param message The text to log
     * @param th An optional stack trace
     */
    public static void error(String message, Throwable th)
    {
        log(LEVEL_ERROR, message, th);
    }

    /**
     * Log a fatal error message
     * @param message The text to log
     */
    public static void fatal(String message)
    {
        log(LEVEL_FATAL, message, null);
    }

    /**
     * Log a fatal error message
     * @param message The text to log
     * @param th An optional stack trace
     */
    public static void fatal(String message, Throwable th)
    {
        log(LEVEL_FATAL, message, th);
    }

    /**
     * Internal log implementation.
     * @param loglevel The level to log at
     * @param message The (optional) message to log
     * @param th The (optional) exception
     */
    private static void log(int loglevel, String message, Throwable th)
    {
        if (loglevel >= level)
        {
            HttpServlet servlet = (HttpServlet) servlets.get();
            if (servlet != null)
            {
                servlet.log(message, th);
            }
            else
            {
                if (message != null)
                {
                    System.out.println(message);
                }
    
                if (th != null)
                {
                    th.printStackTrace();
                }
            }
        }
    }

    /**
     * @return Returns the logging level.
     */
    public static int getLevel()
    {
        return level;
    }

    /**
     * @param level The logging level to set.
     */
    public static void setLevel(int level)
    {
        Log.level = level;
    }

    /**
     * String version of setLevel.
     * @param logLevel One of FATAL, ERROR, WARN, INFO, DEBUG
     */
    public static void setLevel(String logLevel)
    {
        if (logLevel.equalsIgnoreCase("FATAL"))
        {
            setLevel(LEVEL_FATAL);
        }
        else if (logLevel.equalsIgnoreCase("ERROR"))
        {
            setLevel(LEVEL_ERROR);
        }
        else if (logLevel.equalsIgnoreCase("WARN"))
        {
            setLevel(LEVEL_WARN);
        }
        else if (logLevel.equalsIgnoreCase("INFO"))
        {
            setLevel(LEVEL_INFO);
        }
        else if (logLevel.equalsIgnoreCase("DEBUG"))
        {
            setLevel(LEVEL_DEBUG);
        }
        else
        {
            throw new IllegalArgumentException("Unknown log level: " + logLevel);
        }
    }

    /**
     * Associate a servlet with this thread for logging purposes.
     * @param servlet The servlet to use for logging in this thread
     */
    public static void setExecutionContext(HttpServlet servlet)
    {
        servlets.set(servlet);
    }

    /**
     * Remove the servlet from this thread for logging purposes
     */
    public static void unsetExecutionContext()
    {
        servlets.set(null);
    }

    /**
     * The container for all known threads
     */
    private static final ThreadLocal servlets = new ThreadLocal();

    /**
     * What is the current debug level?
     */
    private static int level = LEVEL_WARN;
}
