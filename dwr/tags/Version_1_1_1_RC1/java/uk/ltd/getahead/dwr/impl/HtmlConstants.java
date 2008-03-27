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
package uk.ltd.getahead.dwr.impl;

/**
 * Various constants from generating output.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public interface HtmlConstants
{
    /**
     * MIME constant for plain text.
     * If we need to do more advanced char processing then we should consider
     * adding "; charset=utf-8" to the end of these 3 strings and altering the
     * marshalling to assume utf-8, which it currently does not.
     */
    static final String MIME_PLAIN = "text/plain"; //$NON-NLS-1$

    /**
     * MIME constant for HTML
     */
    static final String MIME_HTML = "text/html"; //$NON-NLS-1$

    /**
     * MIME constant for Javascript
     */
    static final String MIME_JS = "text/javascript"; //$NON-NLS-1$

    /**
     * Empty string
     */
    static final String BLANK = ""; //$NON-NLS-1$

    /**
     * Path to the root of the web app
     */
    static final String PATH_ROOT = "/"; //$NON-NLS-1$

    /**
     * Path to the execution handler
     */
    static final String PATH_EXEC = "/exec"; //$NON-NLS-1$

    /**
     * Path to the interface creator
     */
    static final String PATH_INTERFACE = "/interface/"; //$NON-NLS-1$

    /**
     * Path to the generated test pages
     */
    static final String PATH_TEST = "/test/"; //$NON-NLS-1$

    /**
     * Path upwards
     */
    static final String PATH_UP = ".."; //$NON-NLS-1$

    /**
     * Index page name
     */
    static final String FILE_INDEX = "/index.html"; //$NON-NLS-1$

    /**
     * Util script name
     */
    static final String FILE_UTIL = "/util.js"; //$NON-NLS-1$

    /**
     * Engine helper name
     */
    static final String FILE_ENGINE = "/engine.js"; //$NON-NLS-1$

    /**
     * Deprecated script name
     */
    static final String FILE_DEPRECATED = "/deprecated.js"; //$NON-NLS-1$

    /**
     * Help page name
     */
    static final String FILE_HELP = "/help.html"; //$NON-NLS-1$

    /**
     * Extension for javascript files
     */
    static final String EXTENSION_JS = ".js"; //$NON-NLS-1$

    /**
     * HTTP etag header
     */
    static final String HEADER_ETAG = "ETag"; //$NON-NLS-1$

    /**
     * HTTP etag equivalent of HEADER_IF_MODIFIED
     */
    static final String HEADER_IF_NONE = "If-None-Match"; //$NON-NLS-1$

    /**
     * HTTP header for when a file was last modified
     */
    static final String HEADER_LAST_MODIFIED = "Last-Modified"; //$NON-NLS-1$

    /**
     * HTTP header to request only modified data
     */
    static final String HEADER_IF_MODIFIED = "If-Modified-Since"; //$NON-NLS-1$

    /**
     * The name of the user agent HTTP header
     */
    static final String HEADER_USER_AGENT = "User-Agent"; //$NON-NLS-1$
}
