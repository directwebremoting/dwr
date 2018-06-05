package org.directwebremoting.util;

import java.io.IOException;
import java.io.StringReader;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * An EntityResolver for use when you don't want to do any entity resolving.
 * <p>I think this is technically a violation of all sorts of things because the
 * DTD affects how a document is parsed, and this just dumps all DTDs. However
 * when you are not interested in validity, you just want to get information
 * when you know that the DTD won't affect the document, this could be useful.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class EmptyEntityResolver implements EntityResolver
{
    /* (non-Javadoc)
     * @see org.xml.sax.EntityResolver#resolveEntity(java.lang.String, java.lang.String)
     */
    public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException
    {
        log.debug("resolveEntity(publicId=" + publicId + ", systemId=" + systemId + ") returning null");

        InputSource is = new InputSource(new StringReader(""));
        is.setPublicId(publicId);
        is.setSystemId(systemId);

        return is;
    }

    /**
     * The log stream
     */
    private static final Log log = LogFactory.getLog(EmptyEntityResolver.class);
}
