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

import org.directwebremoting.Container;
import org.directwebremoting.extend.Handler;

/**
 * Various constants from generating output.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class PathConstants
{
    /**
     * When we prime the {@link Container} with URLs for {@link Handler}s we use
     * a prefix so the {@link UrlProcessor} knows that this is a URL and not
     * some other property
     */
    public static final String URL_PREFIX = "url:";

    /**
     * Help page name
     */
    public static final String FILE_HELP = "/help.html";

    /**
     * Extension for javascript files
     */
    public static final String EXTENSION_JS = ".js";

    /**
     * The position of web.xml
     */
    public static final String RESOURCE_WEB_XML = "/WEB-INF/web.xml";
}
