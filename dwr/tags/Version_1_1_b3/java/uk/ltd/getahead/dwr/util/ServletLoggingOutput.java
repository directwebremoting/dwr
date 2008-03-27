/*
 * Copyright 2005 Joe Walker
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package uk.ltd.getahead.dwr.util;

import javax.servlet.http.HttpServlet;

/**
 * An implementation of LoggingOutput that sends stuff to the Servlet.log
 * stream.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class ServletLoggingOutput implements LoggingOutput
{
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

    /* (non-Javadoc)
     * @see uk.ltd.getahead.dwr.util.LoggingOutput#debug(java.lang.String)
     */
    public void debug(String message)
    {
        log(LEVEL_DEBUG, message, null);
    }

    /* (non-Javadoc)
     * @see uk.ltd.getahead.dwr.util.LoggingOutput#info(java.lang.String)
     */
    public void info(String message)
    {
        log(LEVEL_INFO, message, null);
    }

    /* (non-Javadoc)
     * @see uk.ltd.getahead.dwr.util.LoggingOutput#warn(java.lang.String)
     */
    public void warn(String message)
    {
        log(LEVEL_WARN, message, null);
    }

    /* (non-Javadoc)
     * @see uk.ltd.getahead.dwr.util.LoggingOutput#warn(java.lang.String, java.lang.Throwable)
     */
    public void warn(String message, Throwable th)
    {
        log(LEVEL_WARN, message, th);
    }

    /* (non-Javadoc)
     * @see uk.ltd.getahead.dwr.util.LoggingOutput#error(java.lang.String)
     */
    public void error(String message)
    {
        log(LEVEL_ERROR, message, null);
    }

    /* (non-Javadoc)
     * @see uk.ltd.getahead.dwr.util.LoggingOutput#error(java.lang.String, java.lang.Throwable)
     */
    public void error(String message, Throwable th)
    {
        log(LEVEL_ERROR, message, th);
    }

    /* (non-Javadoc)
     * @see uk.ltd.getahead.dwr.util.LoggingOutput#fatal(java.lang.String)
     */
    public void fatal(String message)
    {
        log(LEVEL_FATAL, message, null);
    }

    /* (non-Javadoc)
     * @see uk.ltd.getahead.dwr.util.LoggingOutput#fatal(java.lang.String, java.lang.Throwable)
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
            HttpServlet servlet = (HttpServlet) servlets.get();
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
        if (logLevel.equalsIgnoreCase("FATAL")) //$NON-NLS-1$
        {
            setLevel(LEVEL_FATAL);
        }
        else if (logLevel.equalsIgnoreCase("ERROR")) //$NON-NLS-1$
        {
            setLevel(LEVEL_ERROR);
        }
        else if (logLevel.equalsIgnoreCase("WARN")) //$NON-NLS-1$
        {
            setLevel(LEVEL_WARN);
        }
        else if (logLevel.equalsIgnoreCase("INFO")) //$NON-NLS-1$
        {
            setLevel(LEVEL_INFO);
        }
        else if (logLevel.equalsIgnoreCase("DEBUG")) //$NON-NLS-1$
        {
            setLevel(LEVEL_DEBUG);
        }
        else
        {
            throw new IllegalArgumentException("Unknown log level: " + logLevel); //$NON-NLS-1$
        }
    }

    /* (non-Javadoc)
     * @see uk.ltd.getahead.dwr.util.LoggingOutput#isDebugEnabled()
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
    private static final ThreadLocal servlets = new ThreadLocal();

    /**
     * What is the current debug level?
     */
    private static int level = LEVEL_WARN;
}
