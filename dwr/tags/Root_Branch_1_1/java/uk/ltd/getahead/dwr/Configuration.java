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
