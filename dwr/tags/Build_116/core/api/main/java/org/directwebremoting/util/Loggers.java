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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * A collection of standard log destinations
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public interface Loggers
{
    /**
     * Log destination for startup messages
     */
    public static final Log STARTUP = LogFactory.getLog("org.directwebremoting.log.startup");

    /**
     * Log destination for session change messages
     */
    public static final Log SESSION = LogFactory.getLog("org.directwebremoting.log.session");

    /**
     * Log destination for script debug messages
     */
    public static final Log SCRIPTS = LogFactory.getLog("org.directwebremoting.log.scripts");
}
