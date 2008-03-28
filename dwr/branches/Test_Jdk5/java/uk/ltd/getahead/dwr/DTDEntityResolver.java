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
        if (arg0.equals("-//GetAhead Limited//DTD Direct Web Remoting 0.4//EN"))
        {
            InputStream raw = getClass().getResourceAsStream("dwr.dtd");
            return new InputSource(raw);
        }
        else
        {
            throw new SAXException("Failed to resolve: arg0="+arg0+" arg1="+arg1);
        }
    }
}
