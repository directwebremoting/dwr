package uk.ltd.getahead.testdwr;

import java.io.IOException;
import java.io.StringWriter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentFactory;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

/**
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class DOM4JDemo
{
    /**
     * @param text some text for the embedded push button.
     * @return a new sample XML Document
     */
    public Document getDocument(String text)
    {
        DocumentFactory df = new DocumentFactory();

        Element div = df.createElement("div");
        Document doc = df.createDocument(div);

        div.addAttribute("id", "parentDOM4J");
        div.addText("This is text from DOM4JDemo. ");

        Element button = df.createElement("input");
        button.addAttribute("id", "buttonDOM4J");
        button.addAttribute("type", "button");
        button.addAttribute("value", text);
        button.addAttribute("onclick", "alert('Events are enabled')");

        div.add(button);

        return doc;
    }

    /**
     * @param doc
     * @return The number of chars in the serialized document.
     * @throws IOException If DOM4J breaks
     */
    public int debugDocument(Document doc) throws IOException
    {
        OutputFormat outformat = OutputFormat.createCompactFormat();
        outformat.setEncoding("UTF-8"); //$NON-NLS-1$

        // Setup the destination
        StringWriter xml = new StringWriter();

        XMLWriter writer = new XMLWriter(xml, outformat);
        writer.write(doc);
        writer.flush();
        xml.flush();

        String debug = xml.toString();

        log.debug(debug);

        return debug.length();
    }

    /**
     * The log stream
     */
    private static final Log log = LogFactory.getLog(DOM4JDemo.class);
}
