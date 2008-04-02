package uk.ltd.getahead.testdwr;

import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

/**
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class DOMDemo
{
    /**
     * @param text some text for the embedded push button.
     * @return a new sample XML Document
     * @throws ParserConfigurationException If DOM breaks
     */
    public Document getDocument(String text) throws ParserConfigurationException
    {
        if (buildFactory == null)
        {
            buildFactory = DocumentBuilderFactory.newInstance();
        }

        if (builder == null)
        {
            builder = buildFactory.newDocumentBuilder();
        }

        Document doc = builder.newDocument();
        Element div = doc.createElement("div");
        doc.appendChild(div);

        div.setAttribute("id", "parentDOM");
        Text tn = doc.createTextNode("This is text from DOMDemo. ");
        div.appendChild(tn);

        Element button = doc.createElement("input");
        button.setAttribute("id", "buttonDOM");
        button.setAttribute("type", "button");
        button.setAttribute("value", text);
        button.setAttribute("onclick", "alert('Events are enabled')");

        div.appendChild(button);

        return doc;
    }
    
    /**
     * @param doc
     * @return The number of chars in the serialized document.
     * @throws TransformerException If DOM breaks
     */
    public int debugDocument(Document doc) throws TransformerException
    {
        if (transformer == null)
        {
            transformer = xslFact.newTransformer();
        }

        Source source = new DOMSource(doc);
        StringWriter xml = new StringWriter();
        StreamResult result = new StreamResult(xml);

        transformer.transform(source, result);

        xml.flush();

        String debug = xml.toString();

        log.debug(debug);

        return debug.length();
    }

    private TransformerFactory xslFact = TransformerFactory.newInstance();

    private Transformer transformer = null;

    private DocumentBuilderFactory buildFactory = null;

    private DocumentBuilder builder = null;

    /**
     * The log stream
     */
    private static final Log log = LogFactory.getLog(DOMDemo.class);
}
