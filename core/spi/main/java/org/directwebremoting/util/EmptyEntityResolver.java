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
