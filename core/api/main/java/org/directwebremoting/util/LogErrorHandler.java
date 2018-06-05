package org.directwebremoting.util;

import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXParseException;

/**
 * An ErrorHandler that writes to the Log class
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public final class LogErrorHandler implements ErrorHandler
{
    /* (non-Javadoc)
     * @see org.xml.sax.ErrorHandler#fatalError(org.xml.sax.SAXParseException)
     */
    public void fatalError(SAXParseException ex)
    {
        log.fatal(getMessage(ex));
    }

    /* (non-Javadoc)
     * @see org.xml.sax.ErrorHandler#error(org.xml.sax.SAXParseException)
     */
    public void error(SAXParseException ex)
    {
        log.error(getMessage(ex));
    }

    /* (non-Javadoc)
     * @see org.xml.sax.ErrorHandler#warning(org.xml.sax.SAXParseException)
     */
    public void warning(SAXParseException ex)
    {
        log.warn(getMessage(ex));
    }

    /**
     * @param ex The exception to create a message from
     * @return A summary of what went wrong.
     */
    private static String getMessage(SAXParseException ex)
    {
        if (ex.getSystemId() != null)
        {
            return "SystemID=" + ex.getSystemId() + " Line=" + ex.getLineNumber() + ' ' + ex.getMessage();
        }

        if (ex.getPublicId() != null)
        {
            return "PublicID=" + ex.getPublicId() + " Line=" + ex.getLineNumber() + ' ' + ex.getMessage();
        }

        return "Line=" + ex.getLineNumber() + ' ' + ex.getMessage();
    }

    /**
     * The log stream
     */
    private static final Log log = LogFactory.getLog(LogErrorHandler.class);
}
