package org.directwebremoting.servlet;

/**
 * Various constant for dealing with HTTP traffic.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 * @noinspection InterfaceNeverImplemented
 */
public class HttpConstants
{
    /**
     * HTTP etag header
     */
    public static final String HEADER_ETAG = "ETag"; //$NON-NLS-1$

    /**
     * HTTP etag equivalent of HEADER_IF_MODIFIED
     */
    public static final String HEADER_IF_NONE = "If-None-Match"; //$NON-NLS-1$

    /**
     * HTTP header for when a file was last modified
     */
    public static final String HEADER_LAST_MODIFIED = "Last-Modified"; //$NON-NLS-1$

    /**
     * HTTP header to request only modified data
     */
    public static final String HEADER_IF_MODIFIED = "If-Modified-Since"; //$NON-NLS-1$

    /**
     * The name of the user agent HTTP header
     */
    public static final String HEADER_USER_AGENT = "User-Agent"; //$NON-NLS-1$
}
