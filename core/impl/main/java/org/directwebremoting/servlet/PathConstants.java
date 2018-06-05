package org.directwebremoting.servlet;

import org.directwebremoting.Container;
import org.directwebremoting.extend.Handler;

/**
 * Various constants from generating output.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public interface PathConstants
{
    /**
     * When we prime the {@link Container} with URLs for {@link Handler}s we use
     * a prefix so the {@link UrlProcessor} knows that this is a URL and not
     * some other property
     */
    public static final String PATH_PREFIX = "url:";

    /**
     * When we prime the {@link Container} with ResponseHandlers for {@link Handler}s we use
     * a prefix so the {@link UrlProcessor} knows what to look for.
     */
    public static final String RESPONSE_PREFIX = "response:";

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
