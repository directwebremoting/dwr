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
package org.directwebremoting.servlet;

/**
 * Various constants from generating output.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class PathConstants
{
    /**
     * Path to plain Javascript Marshalled execution
     */
    public static final String PATH_PLAIN_CALL = "/plaincall"; //$NON-NLS-1$

    /**
     * Path to HTML wrapped Javascript Marshalled execution
     */
    public static final String PATH_HTML_CALL = "/htmlcall"; //$NON-NLS-1$

    /**
     * Path to plain Javascript Marshalled execution
     */
    public static final String PATH_PLAIN_POLL = "/plainpoll"; //$NON-NLS-1$

    /**
     * Path to HTML wrapper Marshalled execution
     */
    public static final String PATH_HTML_POLL = "/htmlpoll"; //$NON-NLS-1$

    /**
     * Path to the interface creator
     */
    public static final String PATH_INTERFACE = "/interface/"; //$NON-NLS-1$

    /**
     * Path to the generated test pages
     */
    public static final String PATH_TEST = "/test/"; //$NON-NLS-1$

    /**
     * Path to the generated status pages
     */
    public static final String PATH_STATUS = "/status/"; //$NON-NLS-1$

    /**
     * Path to the root of the web app
     */
    public static final String PATH_ROOT = "/"; //$NON-NLS-1$

    /**
     * Index page name
     */
    public static final String FILE_INDEX = "/index.html"; //$NON-NLS-1$

    /**
     * Util script name
     */
    public static final String FILE_UTIL = "/util.js"; //$NON-NLS-1$

    /**
     * Engine helper name
     */
    public static final String FILE_ENGINE = "/engine.js"; //$NON-NLS-1$

    /**
     * Help page name
     */
    public static final String FILE_HELP = "/help.html"; //$NON-NLS-1$

    /**
     * Extension for javascript files
     */
    public static final String EXTENSION_JS = ".js"; //$NON-NLS-1$
}
