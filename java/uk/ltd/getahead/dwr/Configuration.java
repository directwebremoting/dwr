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
package uk.ltd.getahead.dwr;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 * The DefaultConfiguration class has responsibility for reading all config data from
 * web.xml and dwr.xml
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public interface Configuration
{
    /**
     * Add to the current configuration by reading a DOM tree from a IO stream.
     * @param in The InputStream to parse from
     * @throws ParserConfigurationException If there are XML setup problems
     * @throws IOException Error parsing dwr.xml
     * @throws SAXException Error parsing dwr.xml
     */
    void addConfig(InputStream in) throws ParserConfigurationException, IOException, SAXException;

    /**
     * Add to the current configuration by reading a DOM tree directly
     * @param doc The DOM tree
     */
    void addConfig(Document doc);
}
