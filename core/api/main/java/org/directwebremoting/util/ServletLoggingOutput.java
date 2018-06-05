package org.directwebremoting.util;

import javax.servlet.http.HttpServlet;

/**
 * An implementation of LoggingOutput that sends stuff to the Servlet.log
 * stream.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
@SuppressWarnings({"UseOfSystemOutOrSystemErr", "CallToPrintStackTrace"})
public class ServletLoggingOutput implements LoggingOutput
{
    /**
     * @param base All LoggingOutput must have a constructor like this
     */
    public ServletLoggingOutput(Class<?> base)
    {
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.util.LoggingOutput#debug(java.lang.String)
     */
    public void debug(String message)
    {
        log(LEVEL_DEBUG, message, null);
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.util.LoggingOutput#info(java.lang.String)
     */
    public void info(String message)
    {
        log(LEVEL_INFO, message, null);
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.util.LoggingOutput#warn(java.lang.String)
     */
    public void warn(String message)
    {
        log(LEVEL_WARN, message, null);
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.util.LoggingOutput#warn(java.lang.String, java.lang.Throwable)
     */
    public void warn(String message, Throwable th)
    {
        log(LEVEL_WARN, message, th);
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.util.LoggingOutput#error(java.lang.String)
     */
    public void error(String message)
    {
        log(LEVEL_ERROR, message, null);
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.util.LoggingOutput#error(java.lang.String, java.lang.Throwable)
     */
    public void error(String message, Throwable th)
    {
        log(LEVEL_ERROR, message, th);
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.util.LoggingOutput#fatal(java.lang.String)
     */
    public void fatal(String message)
    {
        log(LEVEL_FATAL, message, null);
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.util.LoggingOutput#fatal(java.lang.String, java.lang.Throwable)
     */
    public void fatal(String message, Throwable th)
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
            HttpServlet servlet = servlets.get();
            if (servlet != null)
            {
                // Tomcat 4 NPEs is th is null
                if (th == null)
                {
                    servlet.log(message);
                }
                else
                {
                    servlet.log(message, th);
                }
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
     * String version of setLevel.
     * @param logLevel One of FATAL, ERROR, WARN, INFO, DEBUG
     */
    public static void setLevel(String logLevel)
    {
        if ("FATAL".equalsIgnoreCase(logLevel))
        {
            setLevel(LEVEL_FATAL);
        }
        else if ("ERROR".equalsIgnoreCase(logLevel))
        {
            setLevel(LEVEL_ERROR);
        }
        else if ("WARN".equalsIgnoreCase(logLevel))
        {
            setLevel(LEVEL_WARN);
        }
        else if ("INFO".equalsIgnoreCase(logLevel))
        {
            setLevel(LEVEL_INFO);
        }
        else if ("DEBUG".equalsIgnoreCase(logLevel))
        {
            setLevel(LEVEL_DEBUG);
        }
        else
        {
            throw new IllegalArgumentException("Unknown log level: " + logLevel);
        }
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.util.LoggingOutput#isDebugEnabled()
     */
    public boolean isDebugEnabled()
    {
        return level == LEVEL_DEBUG;
    }

    /**
     * @param level The logging level to set.
     */
    public static void setLevel(int level)
    {
        ServletLoggingOutput.level = level;
    }

    /**
     * @return Returns the logging level.
     */
    public static int getLevel()
    {
        return level;
    }

    /**
     * The container for all known threads
     */
    private static final ThreadLocal<HttpServlet> servlets = new ThreadLocal<HttpServlet>();

    /**
     * What is the current debug level?
     */
    private static int level = LEVEL_WARN;
}
