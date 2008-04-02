package uk.ltd.getahead.testdwr;

import nu.xom.Attribute;
import nu.xom.Document;
import nu.xom.Element;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class XOMDemo
{
    /**
     * @param text some text for the embedded push button.
     * @return a new sample XML Document
     */
    public Document getDocument(String text)
    {
        Element div = new Element("div");
        Document doc = new Document(div);

        div.addAttribute(new Attribute("id", "parentXOM"));
        div.appendChild("This is text from XOMDemo. ");

        Element button = new Element("input");
        button.addAttribute(new Attribute("id", "buttonXOM"));
        button.addAttribute(new Attribute("type", "button"));
        button.addAttribute(new Attribute("value", text));
        button.addAttribute(new Attribute("onclick", "alert('Events are enabled')"));

        div.appendChild(button);

        return doc;
    }

    /**
     * @param doc
     * @return The number of chars in the serialized document.
     */
    public int debugDocument(Document doc)
    {
        String debug = doc.toXML();

        log.debug(debug);

        return debug.length();
    }

    /**
     * The log stream
     */
    private static final Log log = LogFactory.getLog(XOMDemo.class);
}
