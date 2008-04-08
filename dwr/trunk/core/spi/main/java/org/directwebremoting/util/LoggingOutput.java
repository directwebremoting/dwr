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
package org.directwebremoting.util;

/**
 * We don't want to force users to use commons-logging, but there are no
 * logging APIs available at 1.3 so this lets us use Servlet.log if
 * commons-logging is not available.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public interface LoggingOutput
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

    /**
     * Logger a debug message
     * @param message The text to log
     */
    public void debug(String message);

    /**
     * Logger an info message
     * @param message The text to log
     */
    public void info(String message);

    /**
     * Logger a warning message
     * @param message The text to log
     */
    public void warn(String message);

    /**
     * Logger a warning message
     * @param message The text to log
     * @param th An optional stack trace
     */
    public void warn(String message, Throwable th);

    /**
     * Logger an error message
     * @param message The text to log
     */
    public void error(String message);

    /**
     * Logger an error message
     * @param message The text to log
     * @param th An optional stack trace
     */
    public void error(String message, Throwable th);

    /**
     * Logger a fatal error message
     * @param message The text to log
     */
    public void fatal(String message);

    /**
     * Logger a fatal error message
     * @param message The text to log
     * @param th An optional stack trace
     */
    public void fatal(String message, Throwable th);

    /**
     * Save CPU time when we are not debugging
     * @return true if debugging is enabled
     */
    public boolean isDebugEnabled();
}
