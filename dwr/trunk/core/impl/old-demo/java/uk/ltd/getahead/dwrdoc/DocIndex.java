package uk.ltd.getahead.dwrdoc;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXParseException;

/**
 * 
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class DocIndex
{
    /**
     * 
     * @param rootName
     * @throws ParserConfigurationException
     * @throws IOException 
     */
    public void setRoot(String rootName) throws ParserConfigurationException, IOException
    {
        log.debug("Setting root=" + rootName);

        DocumentBuilderFactory buildFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = buildFactory.newDocumentBuilder();
        XPath xpath = XPathFactory.newInstance().newXPath();

        FileFilter filter = new FileFilter()
        {
            public boolean accept(File pathname)
            {
                return pathname.isFile() && pathname.getName().endsWith(".html");
            }
        };

        File root = new File(rootName);
        rootName = root.getCanonicalPath() + File.separator;

        File[] matches = FileUtil.recursiveListFiles(root, filter , false, -1);
        for (int i = 0; i < matches.length; i++)
        {
            File match = matches[i];

            try
            {
                String key = match.getCanonicalPath();
                if (!key.startsWith(rootName))
                {
                    throw new IOException("File path not part of canonical root. root=" + rootName + " current=" + key);
                }
                key = key.substring(rootName.length());

                Document doc = builder.parse(new FileInputStream(match));

                String title = (String) xpath.evaluate("/html/head/meta[@name='title']/@content", doc, XPathConstants.STRING);
                if (title == null || title.trim().length() == 0)
                {
                    title = (String) xpath.evaluate("/html/head/title", doc, XPathConstants.STRING);
                }

                String flat = flatten(doc, match.getCanonicalPath());

                log.debug("Adding document id=" + key + " title=" + title);
                WebDocument wd = new WebDocument(key, title, flat);
                index.put(key, wd);

                map.put(key, title);
            }
            catch (SAXParseException ex)
            {
                log.error("Parse error: " + match + ":" + ex.getLineNumber() + " - " + ex.getMessage());
            }
            catch (Exception ex)
            {
                log.error("Failed to add: " + match, ex);
            }
        }
    }

    /**
     * Find a WebDocument by its id.
     * @param id The id of the document to find
     * @return The found document or null if none exists
     */
    public WebDocument getWebDocument(String id)
    {
        return index.get(id);
    }

    /**
     * Get a sitemap containing IDs and titles
     * @return The sitemap
     */
    public Map<String, String> getSiteMap()
    {
        return map;
    }

    /**
     * @param doc
     * @param match
     * @throws IOException
     * @throws DOMException
     * @throws TransformerException 
     */
    private String flatten(Document doc, String match) throws IOException, DOMException, TransformerException
    {
        // Check this is html
        Element html = doc.getDocumentElement();
        if (!html.getNodeName().equals("html"))
        {
            throw new IOException("Root node is not <html ...> in " + match);
        }

        Element div = doc.createElement("div");
        div.setAttribute("class", "root");

        // Flatten
        NodeList htmlChildren = html.getChildNodes();
        for (int n = 0; n < htmlChildren.getLength(); n++)
        {
            Node node = htmlChildren.item(n);
            if (node.getNodeType() == Node.ELEMENT_NODE)
            {
                if ("head".equals(node.getNodeName()))
                {
                    NodeList headNodes = node.getChildNodes();
                    for (int c = 0; c < headNodes.getLength(); c++)
                    {
                        Node headNode = headNodes.item(c);
                        if (headNode.getNodeType() == Node.ELEMENT_NODE)
                        {
                            if ("isindex".equals(headNode.getNodeName()) ||
                                "link".equals(headNode.getNodeName()) ||
                                "meta".equals(headNode.getNodeName()) ||
                                "title".equals(headNode.getNodeName()) ||
                                "object".equals(headNode.getNodeName()))
                            {
                                // ignore
                            }
                            else if ("base".equals(headNode.getNodeName()))
                            {
                                log.warn("base element will probably break links in " + match);
                            }
                            else if ("style".equals(headNode.getNodeName()) ||
                                "script".equals(headNode.getNodeName()))
                            {
                                div.appendChild(headNode.cloneNode(true));
                            }
                            else
                            {
                                log.warn("Ignoring unexpected node called " + headNode.getNodeName() + " in <html ...>");
                            }
                        }
                    }
                }
                else if ("body".equals(node.getNodeName()))
                {
                    NodeList bodyNodes = node.getChildNodes();
                    for (int c = 0; c < bodyNodes.getLength(); c++)
                    {
                        Node bodyNode = bodyNodes.item(c);
                        div.appendChild(bodyNode.cloneNode(true));
                    }
                }
                else
                {
                    log.warn("Found node called " + node.getNodeName() + " in <html ...>");
                    div.appendChild(node.cloneNode(true));
                }
            }
            else
            {
                div.appendChild(node.cloneNode(true));
            }
        }

        doc.replaceChild(div, html);

        Source source = new DOMSource(doc);

        TransformerFactory xslFact = TransformerFactory.newInstance();
        Transformer transformer = xslFact.newTransformer();

        // Setup the destination
        StringWriter xml = new StringWriter();
        StreamResult result = new StreamResult(xml);

        transformer.transform(source, result);

        xml.flush();
        return xml.toString();
    }

    /**
     * The log stream
     */
    private static final Log log = LogFactory.getLog(DocIndex.class);

    private Map<String, WebDocument> index = new HashMap<String, WebDocument>();
    private Map<String, String> map = new HashMap<String, String>();
}
