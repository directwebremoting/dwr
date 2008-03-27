package uk.ltd.getahead.dwr;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import uk.ltd.getahead.dwr.util.LogErrorHandler;

/**
 * The Configuration class has responsibility for reading all config data from
 * web.xml and dwr.xml
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class Configuration
{
    /**
     * Examine the current configuration
     * @param debug Are we in debug mode?
     */
    public Configuration(boolean debug)
    {
        this.debug = debug;

        converterManager = new ConverterManager();
        creatorManager = new CreatorManager(debug);
    }

    /**
     * Add to the current configuration by reading a DOM tree from a IO stream.
     * @param in The InputStream to parse from
     * @throws SAXException If the parse fails
     */
    public void addConfig(InputStream in) throws SAXException
    {
        try
        {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setValidating(true);

            DocumentBuilder db = dbf.newDocumentBuilder();
            db.setEntityResolver(new DTDEntityResolver());
            db.setErrorHandler(new LogErrorHandler());

            Document doc = db.parse(in);

            addConfig(doc);
        }
        catch (ParserConfigurationException ex)
        {
            throw new SAXException("Parser exception", ex);
        }
        catch (IOException ex)
        {
            throw new SAXException("IO Error reading from dwr.xml", ex);
        }
    }

    /**
     * Add to the current configuration by reading a DOM tree directly
     * @param doc The DOM tree
     * @throws SAXException If the parse fails
     */
    public void addConfig(Document doc) throws SAXException
    {
        Element root = doc.getDocumentElement();
        NodeList rootChildren = root.getChildNodes();
        for (int i=0; i<rootChildren.getLength(); i++)
        {
            Node node = rootChildren.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE)
            {
                Element child = (Element) node;

                if (child.getNodeName().equals("init"))
                {
                    loadInits(child);
                }
                else if (child.getNodeName().equals("allow"))
                {
                    loadAllows(child);
                }
            }
        }
    }

    /**
     * Internal method to load the inits element
     * @param child The element to read
     * @throws SAXException If the parse fails
     */
    private void loadInits(Element child) throws SAXException
    {
        NodeList inits = child.getChildNodes();
        for (int j=0; j<inits.getLength(); j++)
        {
            if (inits.item(j).getNodeType() == Node.ELEMENT_NODE)
            {
                Element initer = (Element) inits.item(j);

                if (initer.getNodeName().equals("creator"))
                {
                    loadCreator(initer);
                }
                else if (initer.getNodeName().equals("converter"))
                {
                    loadConverter(initer);
                }
            }
        }
    }

    /**
     * Internal method to load the creator element
     * @param initer The element to read
     * @throws SAXException If the parse fails
     */
    private void loadCreator(Element initer) throws SAXException
    {
        String id = initer.getAttribute("id");
        String classname = initer.getAttribute("class");

        try
        {
            Class clazz = Class.forName(classname);
            creatorManager.addCreatorType(id, clazz);
        }
        catch (ClassNotFoundException ex)
        {
            throw new SAXException("Failed to find class: "+classname+", for creator with id="+id, ex);
        }
    }

    /**
     * Internal method to load the converter element
     * @param initer The element to read
     * @throws SAXException If the parse fails
     */
    private void loadConverter(Element initer) throws SAXException
    {
        String id = initer.getAttribute("id");
        String classname = initer.getAttribute("class");

        try
        {
            Class clazz = Class.forName(classname);
            converterManager.addConverterType(id, clazz);
        }
        catch (ClassNotFoundException ex)
        {
            throw new SAXException("Failed to find class: " + classname + ", for converter with id=" + id, ex);
        }
    }

    /**
     * Internal method to load the create/convert elements
     * @param child The element to read
     * @throws SAXException If the parse fails
     */
    private void loadAllows(Element child) throws SAXException
    {
        NodeList allows = child.getChildNodes();
        for (int j=0; j<allows.getLength(); j++)
        {
            if (allows.item(j).getNodeType() == Node.ELEMENT_NODE)
            {
                Element allower = (Element) allows.item(j);
                if (allower.getNodeName().equals("create"))
                {
                    loadCreate(allower);
                }
                else if (allower.getNodeName().equals("convert"))
                {
                    loadConvert(allower);
                }
            }
        }
    }

    /**
     * Internal method to load the convert element
     * @param allower The element to read
     * @throws SAXException If the parse fails
     */
    private void loadConvert(Element allower) throws SAXException
    {
        String match = allower.getAttribute("match");
        String type = allower.getAttribute("converter");

        try
        {
            converterManager.addConverter(match, type);
        }
        catch (IllegalArgumentException ex)
        {
            throw new SAXException(ex.getMessage(), ex);
        }
        catch (Exception ex)
        {
            throw new SAXException("Error instansiating: "+type, ex);
        }
    }

    /**
     * Internal method to load the create element
     * @param allower The element to read
     * @throws SAXException If the parse fails
     */
    private void loadCreate(Element allower) throws SAXException
    {
        String type = allower.getAttribute("creator");
        String javascript = allower.getAttribute("javascript");

        try
        {
            creatorManager.addCreator(type, javascript, allower);
        }
        catch (IllegalArgumentException ex)
        {
            throw new SAXException(ex.getMessage(), ex);
        }
        catch (Exception ex)
        {
            throw new SAXException("Error instansiating: "+type, ex);
        }
    }

    /**
     * Are we in debug mode?
     * @return true if the debug switch is on in web.xml
     */
    public boolean isDebugMode()
    {
        return debug;
    }

    /**
     * Check to see if the given word is reserved or a bad idea in any known
     * version of JavaScript.
     * @param name The word to check
     * @return false if the word is not reserved
     */
    public boolean isReservedWord(String name)
    {
        return reserved.contains(name);
    }

    /**
     * Accessor for the CreatorManager that we configure
     * @return the configured CreatorManager
     */
    public CreatorManager getCreatorManager()
    {
        return creatorManager;
    }

    /**
     * Accessor for the ConverterManager that we configure
     * @return the configured ConverterManager
     */
    public ConverterManager getConverterManager()
    {
        return converterManager;
    }

    /**
     * The ConverterManager to which we delegate conversion of parameters
     */
    private final ConverterManager converterManager;

    /**
     * The CreatorManager to which we delegate creation of new objects.
     */
    private final CreatorManager creatorManager;

    /**
     * Are we in debug mode?
     */
    private final boolean debug;

    private static SortedSet reserved = new TreeSet();
    private static final String[] RESERVED_ARRAY =  new String[]
    {
        // Reserved and used at ECMAScript 4
        "as", "break", "case", "catch", "class", "const", "continue", "default",
        "delete", "do", "else", "export", "extends", "false", "finally", "for",
        "function", "if", "import", "in", "instanceof", "is", "namespace",
        "new", "null", "package", "private", "public", "return", "super",
        "switch", "this", "throw", "true", "try", "typeof", "use", "var",
        "void", "while", "with",
        // Reserved for future use at ECMAScript 4
        "abstract", "debugger", "enum", "goto", "implements", "interface",
        "native", "protected", "synchronized", "throws", "transient",
        "volatile",
        // Reserved in ECMAScript 3, unreserved at 4 best to avoid anyway
        "boolean", "byte", "char", "double", "final", "float", "int", "long",
        "short", "static",
        // I have seen the folowing list as 'best avoided for function names'
        // but it seems way to all encompassing, so I'm not going to include it
        /*
        "alert", "anchor", "area", "arguments", "array", "assign", "blur",
        "boolean", "button", "callee", "caller", "captureevents", "checkbox",
        "clearinterval", "cleartimeout", "close", "closed", "confirm",
        "constructor", "date", "defaultstatus", "document", "element", "escape",
        "eval", "fileupload", "find", "focus", "form", "frame", "frames",
        "getclass", "hidden", "history", "home", "image", "infinity",
        "innerheight", "isfinite", "innerwidth", "isnan", "java", "javaarray",
        "javaclass", "javaobject", "javapackage", "length", "link", "location",
        "locationbar", "math", "menubar", "mimetype", "moveby", "moveto",
        "name", "nan", "navigate", "navigator", "netscape", "number", "object",
        "onblur", "onerror", "onfocus", "onload", "onunload", "open", "opener",
        "option", "outerheight", "outerwidth", "packages", "pagexoffset",
        "pageyoffset", "parent", "parsefloat", "parseint", "password",
        "personalbar", "plugin", "print", "prompt", "prototype", "radio", "ref",
        "regexp", "releaseevents", "reset", "resizeby", "resizeto",
        "routeevent", "scroll", "scrollbars", "scrollby", "scrollto", "select",
        "self", "setinterval", "settimeout", "status", "statusbar", "stop",
        "string", "submit", "sun", "taint",  "text", "textarea", "toolbar",
        "top", "tostring", "unescape", "untaint", "unwatch", "valueof", "watch",
        "window",
        */
    };

    /**
     * For easy access ...
     */
    static
    {
        // The Javascript reserved words array so we don't generate illegal javascript
        reserved.addAll(Arrays.asList(RESERVED_ARRAY));
    }
}
