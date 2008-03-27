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
public final class Configuration
{
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
            throw new SAXException(Messages.getString("Configuration.ParseError"), ex); //$NON-NLS-1$
        }
        catch (IOException ex)
        {
            throw new SAXException(Messages.getString("Configuration.FileError"), ex); //$NON-NLS-1$
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

                if (child.getNodeName().equals(ELEMENT_INIT))
                {
                    loadInits(child);
                }
                else if (child.getNodeName().equals(ELEMENT_ALLOW))
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

                if (initer.getNodeName().equals(ATTRIBUTE_CREATOR))
                {
                    loadCreator(initer);
                }
                else if (initer.getNodeName().equals(ATTRIBUTE_CONVERTER))
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
        String id = initer.getAttribute(ATTRIBUTE_ID);
        String classname = initer.getAttribute(ATTRIBUTE_CLASS);

        try
        {
            Class clazz = Class.forName(classname);
            creatorManager.addCreatorType(id, clazz);
        }
        catch (ClassNotFoundException ex)
        {
            throw new SAXException(Messages.getString("Configuration.CreatorNotFound", classname, id), ex); //$NON-NLS-1$
        }
    }

    /**
     * Internal method to load the converter element
     * @param initer The element to read
     * @throws SAXException If the parse fails
     */
    private void loadConverter(Element initer) throws SAXException
    {
        String id = initer.getAttribute(ATTRIBUTE_ID);
        String classname = initer.getAttribute(ATTRIBUTE_CLASS);

        try
        {
            Class clazz = Class.forName(classname);
            converterManager.addConverterType(id, clazz);
        }
        catch (ClassNotFoundException ex)
        {
            throw new SAXException(Messages.getString("Configuration.ConverterNotFound", classname, id), ex); //$NON-NLS-1$
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
                if (allower.getNodeName().equals(ELEMENT_CREATE))
                {
                    loadCreate(allower);
                }
                else if (allower.getNodeName().equals(ELEMENT_CONVERT))
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
        String match = allower.getAttribute(ATTRIBUTE_MATCH);
        String type = allower.getAttribute(ATTRIBUTE_CONVERTER);

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
            throw new SAXException(Messages.getString("Configuration.ErrorInstansiating", type), ex); //$NON-NLS-1$
        }
    }

    /**
     * Internal method to load the create element
     * @param allower The element to read
     * @throws SAXException If the parse fails
     */
    private void loadCreate(Element allower) throws SAXException
    {
        String type = allower.getAttribute(ATTRIBUTE_CREATOR);
        String javascript = allower.getAttribute(ATTRIBUTE_JAVASCRIPT);

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
            throw new SAXException(Messages.getString("Configuration.ErrorInstansiating", type), ex); //$NON-NLS-1$
        }
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
     * @param creatorManager The new ConverterManager
     */
    public void setCreatorManager(CreatorManager creatorManager)
    {
        this.creatorManager = creatorManager;
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
     * Accessor for the CreatorManager that we configure
     * @param converterManager The new ConverterManager
     */
    public void setConverterManager(ConverterManager converterManager)
    {
        this.converterManager = converterManager;
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
    private ConverterManager converterManager;

    /**
     * The CreatorManager to which we delegate creation of new objects.
     */
    private CreatorManager creatorManager;

    private static final String ELEMENT_INIT = "init"; //$NON-NLS-1$
    private static final String ELEMENT_ALLOW = "allow"; //$NON-NLS-1$
    private static final String ELEMENT_CREATE = "create"; //$NON-NLS-1$
    private static final String ELEMENT_CONVERT = "convert"; //$NON-NLS-1$
    private static final String ATTRIBUTE_ID = "id"; //$NON-NLS-1$
    private static final String ATTRIBUTE_CLASS = "class"; //$NON-NLS-1$
    private static final String ATTRIBUTE_CONVERTER = "converter"; //$NON-NLS-1$
    private static final String ATTRIBUTE_MATCH = "match"; //$NON-NLS-1$
    private static final String ATTRIBUTE_JAVASCRIPT = "javascript"; //$NON-NLS-1$
    private static final String ATTRIBUTE_CREATOR = "creator"; //$NON-NLS-1$

    private static SortedSet reserved = new TreeSet();
    private static final String[] RESERVED_ARRAY =  new String[]
    {
        // Reserved and used at ECMAScript 4
        "as", //$NON-NLS-1$
        "break", //$NON-NLS-1$
        "case", //$NON-NLS-1$
        "catch", //$NON-NLS-1$
        "class", //$NON-NLS-1$
        "const", //$NON-NLS-1$
        "continue", //$NON-NLS-1$
        "default", //$NON-NLS-1$
        "delete", //$NON-NLS-1$
        "do", //$NON-NLS-1$
        "else", //$NON-NLS-1$
        "export", //$NON-NLS-1$
        "extends", //$NON-NLS-1$
        "false", //$NON-NLS-1$
        "finally", //$NON-NLS-1$
        "for", //$NON-NLS-1$
        "function", //$NON-NLS-1$
        "if", //$NON-NLS-1$
        "import", //$NON-NLS-1$
        "in", //$NON-NLS-1$
        "instanceof", //$NON-NLS-1$
        "is", //$NON-NLS-1$
        "namespace", //$NON-NLS-1$
        "new", //$NON-NLS-1$
        "null", //$NON-NLS-1$
        "package", //$NON-NLS-1$
        "private", //$NON-NLS-1$
        "public", //$NON-NLS-1$
        "return", //$NON-NLS-1$
        "super", //$NON-NLS-1$
        "switch", //$NON-NLS-1$
        "this", //$NON-NLS-1$
        "throw", //$NON-NLS-1$
        "true", //$NON-NLS-1$
        "try", //$NON-NLS-1$
        "typeof", //$NON-NLS-1$
        "use", //$NON-NLS-1$
        "var", //$NON-NLS-1$
        "void",  //$NON-NLS-1$
        "while", //$NON-NLS-1$
        "with", //$NON-NLS-1$
        // Reserved for future use at ECMAScript 4
        "abstract", //$NON-NLS-1$
        "debugger", //$NON-NLS-1$
        "enum", //$NON-NLS-1$
        "goto", //$NON-NLS-1$
        "implements", //$NON-NLS-1$
        "interface", //$NON-NLS-1$
        "native", //$NON-NLS-1$
        "protected", //$NON-NLS-1$
        "synchronized", //$NON-NLS-1$
        "throws", //$NON-NLS-1$
        "transient", //$NON-NLS-1$
        "volatile", //$NON-NLS-1$
        // Reserved in ECMAScript 3, unreserved at 4 best to avoid anyway
        "boolean", //$NON-NLS-1$
        "byte", //$NON-NLS-1$
        "char", //$NON-NLS-1$
        "double", //$NON-NLS-1$
        "final", //$NON-NLS-1$
        "float", //$NON-NLS-1$
        "int", //$NON-NLS-1$
        "long", //$NON-NLS-1$
        "short", //$NON-NLS-1$
        "static", //$NON-NLS-1$
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
