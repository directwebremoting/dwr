package org.directwebremoting.util;

/**
 * Various constants from generating output.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public interface MimeConstants
{
    /**
     * MIME constant for plain text.
     * If we need to do more advanced char processing then we should consider
     * adding "; charset=utf-8" to the end of these 3 strings and altering the
     * marshalling to assume utf-8, which it currently does not.
     */
    public static final String MIME_PLAIN = "text/plain";

    /**
     * MIME constant for HTML
     */
    public static final String MIME_HTML = "text/html";

    /**
     * MIME constant for Javascript
     */
    public static final String MIME_JS = "text/javascript; charset=utf-8";

    /**
     * MIME constant for JSON
     */
    public static final String MIME_JSON = "application/json";
}
