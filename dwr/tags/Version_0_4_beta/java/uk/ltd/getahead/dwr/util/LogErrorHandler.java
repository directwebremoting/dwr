package uk.ltd.getahead.dwr.util;

import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
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
    public void fatalError(SAXParseException ex) throws SAXException
    {
        Log.fatal(getMessage(ex), ex);
    }

    /* (non-Javadoc)
     * @see org.xml.sax.ErrorHandler#error(org.xml.sax.SAXParseException)
     */
    public void error(SAXParseException ex) throws SAXException
    {
        Log.error(getMessage(ex), ex);
    }

    /* (non-Javadoc)
     * @see org.xml.sax.ErrorHandler#warning(org.xml.sax.SAXParseException)
     */
    public void warning(SAXParseException ex) throws SAXException
    {
        Log.warn(getMessage(ex), ex);
    }

    /**
     * @param ex The exception to create a message from
     * @return A summary of what went wrong.
     */
    private String getMessage(SAXParseException ex)
    {
        if (ex.getSystemId() != null)
        {
            return "SystemID=" + ex.getSystemId() + " Line=" + ex.getLineNumber() + ' ' + ex.getMessage(); //$NON-NLS-1$ //$NON-NLS-2$
        }

        if (ex.getPublicId() != null)
        {
            return "PublicID=" + ex.getPublicId() + " Line=" + ex.getLineNumber() + ' ' + ex.getMessage(); //$NON-NLS-1$ //$NON-NLS-2$
        }

        return "Line=" + ex.getLineNumber() + ' ' + ex.getMessage(); //$NON-NLS-1$
    }
}
