package org.directwebremoting.servlet;

/**
 * Various constant for dealing with HTTP traffic.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public interface HttpConstants
{
    /**
     * HTTP etag header
     */
    public static final String HEADER_ETAG = "ETag";

    /**
     * HTTP etag equivalent of HEADER_IF_MODIFIED
     */
    public static final String HEADER_IF_NONE = "If-None-Match";

    /**
     * HTTP header for when a file was last modified
     */
    public static final String HEADER_LAST_MODIFIED = "Last-Modified";

    /**
     * HTTP header to request only modified data
     */
    public static final String HEADER_IF_MODIFIED = "If-Modified-Since";

    /**
     * The name of the user agent HTTP header
     */
    public static final String HEADER_USER_AGENT = "User-Agent";
}
