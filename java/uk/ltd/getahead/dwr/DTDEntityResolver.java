package uk.ltd.getahead.dwr;

import java.io.IOException;
import java.io.InputStream;

import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * We want to read the config files in validating mode, and keep the DTD within
 * the dwr.jar file so we need to be able to help the parser find the DTD.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public final class DTDEntityResolver implements EntityResolver
{
    /* (non-Javadoc)
     * @see org.xml.sax.EntityResolver#resolveEntity(java.lang.String, java.lang.String)
     */
    public InputSource resolveEntity(String arg0, String arg1) throws SAXException, IOException
    {
        if (arg0.equals(XML_PUBLIC_ID))
        {
            InputStream raw = getClass().getResourceAsStream(RESOURCE_DTD);
            return new InputSource(raw);
        }
        else
        {
            throw new SAXException(Messages.getString("DTDEntityResolver.ResolveFailed", arg0, arg1)); //$NON-NLS-1$
        }
    }

    private static final String XML_PUBLIC_ID = "-//GetAhead Limited//DTD Direct Web Remoting 0.4//EN"; //$NON-NLS-1$
    private static final String RESOURCE_DTD = "dwr.dtd"; //$NON-NLS-1$
}
