/*
 * Copyright 2005 Joe Walker
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package uk.ltd.getahead.dwr.impl;

import java.io.InputStream;

import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import uk.ltd.getahead.dwr.AbstractDWRServlet;
import uk.ltd.getahead.dwr.Messages;
import uk.ltd.getahead.dwr.util.Logger;

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
    public InputSource resolveEntity(String arg0, String arg1) throws SAXException
    {
        for (int i = 0; i < MAPPINGS.length; i++)
        {
            if (arg0.equals(MAPPINGS[i][0]))
            {
                if (i != MAPPINGS.length - 1)
                {
                    String doctype = "<!DOCTYPE dwr PUBLIC \"" + //$NON-NLS-1$
                        MAPPINGS[MAPPINGS.length - 1][0] + "\" \"http://www.getahead.ltd.uk/dwr/" + //$NON-NLS-1$
                        MAPPINGS[MAPPINGS.length - 1][1] + "\">"; //$NON-NLS-1$

                    log.warn("Deprecated public id in dwr.xml. Use: " + doctype); //$NON-NLS-1$
                }

                String dtdname = AbstractDWRServlet.PACKAGE + MAPPINGS[i][1];
                InputStream raw = getClass().getResourceAsStream(dtdname);
                return new InputSource(raw);
            }
        }

        throw new SAXException(Messages.getString("DTDEntityResolver.ResolveFailed", arg0, arg1)); //$NON-NLS-1$
    }

    /**
     * An array of mappings from public ids to files to read from.
     * The last one of these is the "only true DTD" the others are deprecated
     * so be careful if you re-order them.
     */
    private static final String[][] MAPPINGS =
    {
        { "-//GetAhead Limited//DTD Direct Web Remoting 0.4//EN", "/dwr10.dtd"}, //$NON-NLS-1$ //$NON-NLS-2$
        { "-//GetAhead Limited//DTD Direct Web Remoting 1.0//EN", "/dwr10.dtd"}, //$NON-NLS-1$ //$NON-NLS-2$
    };

    /**
     * The log stream
     */
    private static final Logger log = Logger.getLogger(DTDEntityResolver.class);
}
