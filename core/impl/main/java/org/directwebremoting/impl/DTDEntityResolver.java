package org.directwebremoting.impl;

import java.io.InputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.directwebremoting.extend.DwrConstants;
import org.directwebremoting.util.LocalUtil;
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
    public InputSource resolveEntity(String publicId, String systemId) throws SAXException
    {
        for (int i = 0; i < MAPPINGS.length; i++)
        {
            if (publicId.equals(MAPPINGS[i][0]))
            {
                if (i != MAPPINGS.length - 1)
                {
                    String doctype = "<!DOCTYPE dwr PUBLIC \"" +
                        MAPPINGS[MAPPINGS.length - 1][0] + "\" \"http://getahead.org/dwr/" +
                        MAPPINGS[MAPPINGS.length - 1][1] + "\">";

                    log.warn("Deprecated public id in dwr.xml. Use: " + doctype);
                }

                String dtdname = DwrConstants.PACKAGE_PATH + MAPPINGS[i][1];
                InputStream raw = LocalUtil.getInternalResourceAsStream(dtdname);
                return new InputSource(raw);
            }
        }

        throw new SAXException("Failed to resolve: publicId=" + publicId + " systemId=" + systemId);
    }

    /**
     * An array of mappings from public ids to files to read from.
     * The last one of these is the "only true DTD" the others are deprecated
     * so be careful if you re-order them.
     */
    private static final String[][] MAPPINGS =
    {
        { "-//GetAhead Limited//DTD Direct Web Remoting 0.4//EN", "/dwr10.dtd"},
        { "-//GetAhead Limited//DTD Direct Web Remoting 1.0//EN", "/dwr10.dtd"},
        { "-//GetAhead Limited//DTD Direct Web Remoting 2.0//EN", "/dwr20.dtd"},
        { "-//GetAhead Limited//DTD Direct Web Remoting 3.0//EN", "/dwr30.dtd"},
    };

    /**
     * The log stream
     */
    private static final Log log = LogFactory.getLog(DTDEntityResolver.class);
}
