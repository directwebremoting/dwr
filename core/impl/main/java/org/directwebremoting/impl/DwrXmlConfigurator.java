package org.directwebremoting.impl;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.servlet.ServletContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.directwebremoting.AjaxFilter;
import org.directwebremoting.Container;
import org.directwebremoting.extend.AccessControl;
import org.directwebremoting.extend.AjaxFilterManager;
import org.directwebremoting.extend.Configurator;
import org.directwebremoting.extend.ConverterManager;
import org.directwebremoting.extend.Creator;
import org.directwebremoting.extend.CreatorManager;
import org.directwebremoting.extend.MethodDeclaration;
import org.directwebremoting.extend.OverrideProperty;
import org.directwebremoting.extend.ParameterProperty;
import org.directwebremoting.extend.Property;
import org.directwebremoting.util.LocalUtil;
import org.directwebremoting.util.LogErrorHandler;
import org.directwebremoting.util.Loggers;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * A configurator that gets its configuration by reading a dwr.xml file.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class DwrXmlConfigurator implements Configurator
{
    /**
     * Setter for the resource name that we can use to read a file from the
     * servlet context
     * @param servletResourceName The name to lookup
     * @throws IOException On file read failure
     * @throws ParserConfigurationException On XML setup failure
     * @throws SAXException On XML parse failure
     */
    public void setServletResourceName(ServletContext servletContext, String servletResourceName) throws IOException, ParserConfigurationException, SAXException
    {
        this.servletResourceName = servletResourceName;

        InputStream in = null;
        try
        {
            in = servletContext.getResourceAsStream(servletResourceName);
            if (in == null)
            {
                throw new IOException("Missing config file: '" + servletResourceName + "'");
            }

            Loggers.STARTUP.debug("Configuring from servlet resource: " + servletResourceName);
            setInputStream(in);
        }
        finally
        {
            LocalUtil.close(in);
        }
    }

    /**
     * Setter for a classpath based lookup
     * @param classResourceName The resource to lookup in the classpath
     * @throws IOException On file read failure
     * @throws ParserConfigurationException On XML setup failure
     * @throws SAXException On XML parse failure
     */
    public void setClassResourceName(String classResourceName) throws IOException, ParserConfigurationException, SAXException
    {
        this.classResourceName = classResourceName;

        InputStream in = LocalUtil.getInternalResourceAsStream(classResourceName);
        if (in == null)
        {
            throw new IOException("Missing config file: '" + classResourceName + "'");
        }

        Loggers.STARTUP.debug("Configuring from class resource: " + classResourceName);
        setInputStream(in);
    }

    /**
     * Setter for a direct input stream to configure from
     * @param in The input stream to read from.
     * @throws IOException On file read failure
     * @throws ParserConfigurationException On XML setup failure
     * @throws SAXException On XML parse failure
     */
    public void setInputStream(InputStream in) throws ParserConfigurationException, SAXException, IOException
    {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setValidating(true);

        DocumentBuilder db = dbf.newDocumentBuilder();
        db.setEntityResolver(new DTDEntityResolver());
        db.setErrorHandler(new LogErrorHandler());

        document = db.parse(in);
    }

    /**
     * To set the configuration document directly
     * @param document The new configuration document
     */
    public void setDocument(Document document)
    {
        this.document = document;
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.Configurator#configure(org.directwebremoting.Container)
     */
    public void configure(Container container)
    {
        accessControl = container.getBean(AccessControl.class);
        ajaxFilterManager = container.getBean(AjaxFilterManager.class);
        converterManager = container.getBean(ConverterManager.class);
        creatorManager = container.getBean(CreatorManager.class);

        Element root = document.getDocumentElement();

        NodeList rootChildren = root.getChildNodes();
        for (int i = 0; i < rootChildren.getLength(); i++)
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
                else if (child.getNodeName().equals(ELEMENT_SIGNATURES))
                {
                    loadSignature(child);
                }
            }
        }
    }

    /**
     * Internal method to load the init element
     * @param child The element to read
     */
    private void loadInits(Element child)
    {
        NodeList inits = child.getChildNodes();
        for (int j = 0; j < inits.getLength(); j++)
        {
            if (inits.item(j).getNodeType() == Node.ELEMENT_NODE)
            {
                Element initer = (Element) inits.item(j);

                if (initer.getNodeName().equals(ATTRIBUTE_CREATOR))
                {
                    String id = initer.getAttribute(ATTRIBUTE_ID);
                    String className = initer.getAttribute(ATTRIBUTE_CLASS);
                    creatorManager.addCreatorType(id, className);
                }
                else if (initer.getNodeName().equals(ATTRIBUTE_CONVERTER))
                {
                    String id = initer.getAttribute(ATTRIBUTE_ID);
                    String className = initer.getAttribute(ATTRIBUTE_CLASS);
                    converterManager.addConverterType(id, className);
                }
            }
        }
    }

    /**
     * Internal method to load the create/convert elements
     * @param child The element to read
     */
    private void loadAllows(Element child)
    {
        NodeList allows = child.getChildNodes();
        for (int j = 0; j < allows.getLength(); j++)
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
                else if (allower.getNodeName().equals(ELEMENT_FILTER))
                {
                    loadFilter(allower);
                }
            }
        }
    }

    /**
     * Internal method to load the convert element
     * @param allower The element to read
     */
    private void loadConvert(Element allower)
    {
        String match = allower.getAttribute(ATTRIBUTE_MATCH);
        String type = allower.getAttribute(ATTRIBUTE_CONVERTER);

        try
        {
            Map<String, String> params = createSettingMap(allower);
            converterManager.addConverter(match, type, params);
        }
        catch (NoClassDefFoundError ex)
        {
            Loggers.STARTUP.info("Convertor '" + type + "' not loaded due to NoClassDefFoundError. (match='" + match + "'). Cause: " + ex.getMessage());
        }
        catch (Exception ex)
        {
            Loggers.STARTUP.error("Failed to add convertor: match=" + match + ", type=" + type, ex);
        }
    }

    /**
     * Internal method to load the create element
     * @param allower The element to read
     */
    private void loadCreate(Element allower)
    {
        String type = allower.getAttribute(ATTRIBUTE_CREATOR);
        String javascript = allower.getAttribute(ATTRIBUTE_JAVASCRIPT);

        try
        {
            Map<String, String> params = createSettingMap(allower);
            creatorManager.addCreator(type, params);

            processPermissions(javascript, allower);
            processAuth(javascript, allower);
            processParameters(javascript, allower);
            processAjaxFilters(javascript, allower);
        }
        catch (NoClassDefFoundError ex)
        {
            Loggers.STARTUP.info("Creator '" + type + "' not loaded due to NoClassDefFoundError. (javascript='" + javascript + "'). Cause: " + ex.getMessage());
        }
        catch (Exception ex)
        {
            Loggers.STARTUP.error("Failed to add creator: type=" + type + ", javascript=" + javascript, ex);
        }
    }

    /**
     * Internal method to load the convert element
     * @param allower The element to read
     */
    private void loadFilter(Element allower)
    {
        String type = allower.getAttribute(ATTRIBUTE_CLASS);

        try
        {
            Class<?> impl = LocalUtil.classForName(type);
            AjaxFilter object = (AjaxFilter) impl.newInstance();

            LocalUtil.setParams(object, createSettingMap(allower), ignore);

            ajaxFilterManager.addAjaxFilter(object);
        }
        catch (ClassCastException ex)
        {
            Loggers.STARTUP.error(type + " does not implement " + AjaxFilter.class.getName(), ex);
        }
        catch (NoClassDefFoundError ex)
        {
            Loggers.STARTUP.info("Missing class for filter (class='" + type + "'). Cause: " + ex.getMessage());
        }
        catch (Exception ex)
        {
            Loggers.STARTUP.error("Failed to add filter: class=" + type, ex);
        }
    }

    /**
     * Create a parameter map from nested <param name="foo" value="blah"/>
     * elements
     * @param parent The parent element
     * @return A map of parameters
     */
    private static Map<String, String> createSettingMap(Element parent)
    {
        Map<String, String> params = new HashMap<String, String>();

        // Go through the attributes in the allower element, adding to the param map
        NamedNodeMap attrs = parent.getAttributes();
        for (int i = 0; i < attrs.getLength(); i++)
        {
            Node node = attrs.item(i);
            String name = node.getNodeName();
            String value = node.getNodeValue();
            params.put(name, value);
        }

        // Go through the param elements in the allower element, adding to the param map
        NodeList locNodes = parent.getElementsByTagName(ELEMENT_PARAM);
        for (int i = 0; i < locNodes.getLength(); i++)
        {
            // Since this comes from getElementsByTagName we can assume that
            // all the nodes are elements.
            Element element = (Element) locNodes.item(i);

            // But getElementsByTagName(ELEMENT_PARAM) includes param nodes that
            // are nested down inside filters, so we need to check that the
            // parent node is 'parent'. $&*?! DOM!
            if (element.getParentNode() != parent)
            {
                continue;
            }

            String name = element.getAttribute(ATTRIBUTE_NAME);
            if (name != null)
            {
                String value = element.getAttribute(ATTRIBUTE_VALUE);
                if (value == null || value.length() == 0)
                {
                    StringBuffer buffer = new StringBuffer();
                    NodeList textNodes = element.getChildNodes();

                    for (int j = 0; j < textNodes.getLength(); j++)
                    {
                        buffer.append(textNodes.item(j).getNodeValue());
                    }

                    value = buffer.toString();
                }

                params.put(name, value);
            }
        }

        return params;
    }

    /**
     * Process the include and exclude elements, passing them on to the creator
     * manager.
     * @param javascript The name of the creator
     * @param parent The container of the include and exclude elements.
     */
    private void processPermissions(String javascript, Element parent)
    {
        NodeList incNodes = parent.getElementsByTagName(ELEMENT_INCLUDE);
        for (int i = 0; i < incNodes.getLength(); i++)
        {
            Element include = (Element) incNodes.item(i);
            String method = include.getAttribute(ATTRIBUTE_METHOD);
            accessControl.addIncludeRule(javascript, method);

            if (include.hasAttribute(ATTRIBUTE_ROLE))
            {
                String role = include.getAttribute(ATTRIBUTE_ROLE);
                accessControl.addRoleRestriction(javascript, method, role);
            }
        }

        NodeList excNodes = parent.getElementsByTagName(ELEMENT_EXCLUDE);
        for (int i = 0; i < excNodes.getLength(); i++)
        {
            Element include = (Element) excNodes.item(i);
            String method = include.getAttribute(ATTRIBUTE_METHOD);
            accessControl.addExcludeRule(javascript, method);
        }
    }

    /**
     * J2EE role based method level security added here.
     * @param javascript The name of the creator
     * @param parent The container of the include and exclude elements.
     */
    private void processAuth(String javascript, Element parent)
    {
        NodeList nodes = parent.getElementsByTagName(ELEMENT_AUTH);
        for (int i = 0; i < nodes.getLength(); i++)
        {
            Element include = (Element) nodes.item(i);

            String method = include.getAttribute(ATTRIBUTE_METHOD);
            String role = include.getAttribute(ATTRIBUTE_ROLE);

            accessControl.addRoleRestriction(javascript, method, role);
        }
    }

    /**
     * J2EE role based method level security added here.
     * @param javascript The name of the creator
     * @param parent The container of the include and exclude elements.
     */
    private void processAjaxFilters(String javascript, Element parent)
    {
        NodeList nodes = parent.getElementsByTagName(ELEMENT_FILTER);
        for (int i = 0; i < nodes.getLength(); i++)
        {
            Element include = (Element) nodes.item(i);

            String type = include.getAttribute(ATTRIBUTE_CLASS);
            AjaxFilter filter = LocalUtil.classNewInstance(javascript, type, AjaxFilter.class);
            if (filter != null)
            {
                LocalUtil.setParams(filter, createSettingMap(include), ignore);
                ajaxFilterManager.addAjaxFilter(filter, javascript);
            }
        }
    }

    /**
     * Parse and extra type info from method signatures
     * @param element The element to read
     */
    private void loadSignature(Element element)
    {
        StringBuffer sigtext = new StringBuffer();

        // This coagulates text nodes, not sure if we need to do this?
        element.normalize();

        NodeList nodes = element.getChildNodes();
        for (int i = 0; i < nodes.getLength(); i++)
        {
            Node node = nodes.item(i);
            short type = node.getNodeType();
            if (type != Node.TEXT_NODE && type != Node.CDATA_SECTION_NODE)
            {
                Loggers.STARTUP.warn("Ignoring illegal node type: " + type);
                continue;
            }

            sigtext.append(node.getNodeValue());
        }

        SignatureParser sigp = new SignatureParser(converterManager, creatorManager);
        sigp.parse(sigtext.toString());
    }

    /**
     * Collections often have missing information. This helps fill the missing
     * data in.
     * @param javascript The name of the creator
     * @param parent The container of the include and exclude elements.
     * @throws ClassNotFoundException If the type attribute can't be converted into a Class
     */
    private void processParameters(String javascript, Element parent) throws ClassNotFoundException
    {
        NodeList nodes = parent.getElementsByTagName(ELEMENT_PARAMETER);
        for (int i = 0; i < nodes.getLength(); i++)
        {
            Element include = (Element) nodes.item(i);

            String methodName = include.getAttribute(ATTRIBUTE_METHOD);

            // Try to find the method that we are annotating
            Creator creator = creatorManager.getCreator(javascript, true);
            Class<?> dest = creator.getType();

            Method method = null;
            for (Method test : dest.getMethods())
            {
                if (test.getName().equals(methodName))
                {
                    if (method == null)
                    {
                        method = test;
                    }
                    else
                    {
                        Loggers.STARTUP.warn("Setting extra type info to overloaded methods may fail with <parameter .../>");
                    }
                }
            }

            if (method == null)
            {
                Loggers.STARTUP.error("Unable to find method called: " + methodName + " on type: " + dest.getName() + " from creator: " + javascript);
                continue;
            }

            String number = include.getAttribute(ATTRIBUTE_NUMBER);
            int paramNo = Integer.parseInt(number);

            String types = include.getAttribute(ATTRIBUTE_TYPE);
            StringTokenizer st = new StringTokenizer(types, ",");

            int j = 0;
            while (st.hasMoreTokens())
            {
                String type = st.nextToken();
                Class<?> clazz = LocalUtil.classForName(type.trim());
                ParameterProperty parentProperty = new ParameterProperty(new MethodDeclaration(method), paramNo);
                Property child = parentProperty.createChild(j);
                child = converterManager.checkOverride(child);
                Property replacement = new OverrideProperty(clazz);
                converterManager.setOverrideProperty(child, replacement);
                j++;
            }
        }
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        if (servletResourceName != null)
        {
            return "DwrXmlConfigurator[ServletResource:" + servletResourceName + "]";
        }
        else
        {
            return "DwrXmlConfigurator[ClassResource:" + classResourceName + "]";
        }
    }

    /**
     * The parsed document
     */
    private Document document;

    /**
     * The properties that we don't warn about if they don't exist.
     */
    private static final List<String> ignore = Arrays.asList("class");

    /**
     * What AjaxFilters apply to which Ajax calls?
     */
    private AjaxFilterManager ajaxFilterManager = null;

    /**
     * The converter manager that decides how parameters are converted
     */
    private ConverterManager converterManager = null;

    /**
     * The DefaultCreatorManager to which we delegate creation of new objects.
     */
    private CreatorManager creatorManager = null;

    /**
     * The security manager
     */
    private AccessControl accessControl = null;

    /**
     * For debug purposes, the classResourceName that we were configured with.
     * Either this or {@link #servletResourceName} will be null
     */
    private String classResourceName;

    /**
     * For debug purposes, the servletResourceName that we were configured with
     * Either this or {@link #classResourceName} will be null
     */
    private String servletResourceName;

    /*
     * The element names
     */
    private static final String ELEMENT_INIT = "init";

    private static final String ELEMENT_ALLOW = "allow";

    private static final String ELEMENT_CREATE = "create";

    private static final String ELEMENT_CONVERT = "convert";

    private static final String ELEMENT_PARAM = "param";

    private static final String ELEMENT_INCLUDE = "include";

    private static final String ELEMENT_EXCLUDE = "exclude";

    private static final String ELEMENT_PARAMETER = "parameter";

    private static final String ELEMENT_AUTH = "auth";

    private static final String ELEMENT_SIGNATURES = "signatures";

    private static final String ELEMENT_FILTER = "filter";

    /*
     * The attribute names
     */
    private static final String ATTRIBUTE_ID = "id";

    private static final String ATTRIBUTE_CLASS = "class";

    private static final String ATTRIBUTE_CONVERTER = "converter";

    private static final String ATTRIBUTE_MATCH = "match";

    private static final String ATTRIBUTE_JAVASCRIPT = "javascript";

    private static final String ATTRIBUTE_CREATOR = "creator";

    private static final String ATTRIBUTE_NAME = "name";

    private static final String ATTRIBUTE_VALUE = "value";

    private static final String ATTRIBUTE_METHOD = "method";

    private static final String ATTRIBUTE_ROLE = "role";

    private static final String ATTRIBUTE_NUMBER = "number";

    private static final String ATTRIBUTE_TYPE = "type";
}
