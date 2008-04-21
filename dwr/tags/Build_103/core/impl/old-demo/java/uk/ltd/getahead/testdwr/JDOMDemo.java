package uk.ltd.getahead.testdwr;

import java.io.IOException;
import java.io.StringWriter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

/**
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class JDOMDemo
{
    /**
     * @param text some text for the embedded push button.
     * @return a new sample XML Document
     */
    public Document getDocument(String text)
    {
        Element div = new Element("div");
        Document doc = new Document(div);

        div.setAttribute(new Attribute("id", "parentJDOM"));
        div.addContent("This is text from JDOMDemo. ");

        Element button = new Element("input");
        button.setAttribute(new Attribute("id", "buttonJDOM"));
        button.setAttribute(new Attribute("type", "button"));
        button.setAttribute(new Attribute("value", text));
        button.setAttribute(new Attribute("onclick", "alert('Events are enabled')"));

        div.addContent(button);

        return doc;
    }
    
    /**
     * @param doc
     * @return The number of chars in the serialized document.
     */
    public int debugDocument(Document doc)
    {
        Format outformat = Format.getCompactFormat();
        outformat.setEncoding("UTF-8"); //$NON-NLS-1$

        // Setup the destination
        StringWriter xml = new StringWriter();

        try
        {
            XMLOutputter writer = new XMLOutputter(outformat);
            writer.output(doc, xml);
            xml.flush();    
        }
        catch (IOException ex)
        {
            log.error("Failed to serialize document", ex);
        }

        String debug = xml.toString();

        log.debug(debug);

        return debug.length();
    }

    /**
     * The log stream
     */
    private static final Log log = LogFactory.getLog(JDOMDemo.class);
}
