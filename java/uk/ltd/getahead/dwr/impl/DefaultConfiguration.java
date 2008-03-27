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
package uk.ltd.getahead.dwr.impl;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import uk.ltd.getahead.dwr.AccessControl;
import uk.ltd.getahead.dwr.Configuration;
import uk.ltd.getahead.dwr.ConverterManager;
import uk.ltd.getahead.dwr.Creator;
import uk.ltd.getahead.dwr.CreatorManager;
import uk.ltd.getahead.dwr.util.LogErrorHandler;
import uk.ltd.getahead.dwr.util.Logger;

/**
 * The DefaultConfiguration class has responsibility for reading all config data from
 * web.xml and dwr.xml
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class DefaultConfiguration implements Configuration
{
    /* (non-Javadoc)
     * @see uk.ltd.getahead.dwr.Configuration#addConfig(java.io.InputStream)
     */
    public void addConfig(InputStream in) throws ParserConfigurationException, IOException, SAXException
    {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setValidating(true);

        DocumentBuilder db = dbf.newDocumentBuilder();
        db.setEntityResolver(new DTDEntityResolver());
        db.setErrorHandler(new LogErrorHandler());

        Document doc = db.parse(in);

        addConfig(doc);
    }

    /* (non-Javadoc)
     * @see uk.ltd.getahead.dwr.Configuration#addConfig(org.w3c.dom.Document)
     */
    public void addConfig(Document doc)
    {
        Element root = doc.getDocumentElement();

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
     * Internal method to load the inits element
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
     */
    private void loadCreator(Element initer)
    {
        String id = initer.getAttribute(ATTRIBUTE_ID);
        String classname = initer.getAttribute(ATTRIBUTE_CLASS);

        try
        {
            Class clazz = Class.forName(classname);
            creatorManager.addCreatorType(id, clazz);
        }
        catch (NoClassDefFoundError ex)
        {
            log.info("Missing classdef for creator '" + id + "'. Failed to load " + classname + ". Cause: " + ex.getMessage()); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        }
        catch (ClassNotFoundException ex)
        {
            log.info("Missing class for creator '" + id + "'. Failed to load " + classname + ". Cause: " + ex.getMessage()); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        }
        catch (Exception ex)
        {
            log.warn("Failed to load creator '" + id + "', classname=" + classname + ": ", ex); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        }
    }

    /**
     * Internal method to load the converter element
     * @param initer The element to read
     */
    private void loadConverter(Element initer)
    {
        String id = initer.getAttribute(ATTRIBUTE_ID);
        String classname = initer.getAttribute(ATTRIBUTE_CLASS);

        try
        {
            Class clazz = Class.forName(classname);
            converterManager.addConverterType(id, clazz);
        }
        catch (NoClassDefFoundError ex)
        {
            log.info("Missing classdef for converter '" + id + "'. Failed to load " + classname + ". Cause: " + ex.getMessage()); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        }
        catch (ClassNotFoundException ex)
        {
            log.info("Missing class for converter '" + id + "'. Failed to load " + classname + ". Cause: " + ex.getMessage()); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        }
        catch (Exception ex)
        {
            log.warn("Failed to load converter '" + id + "', classname=" + classname + ": " + ex); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
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
            Map params = createSettingMap(allower);
            converterManager.addConverter(match, type, params);
        }
        catch (NoClassDefFoundError ex)
        {
            log.info("Missing class for convertor '" + type + "'. (match='" + match + "'). Cause: " + ex.getMessage()); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        }
        catch (Exception ex)
        {
            log.error("Failed to add convertor: match=" + match + ", type=" + type, ex); //$NON-NLS-1$ //$NON-NLS-2$
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

        // DEPRECATED: Remove when confusion about creators has gone away
        if (type.equals("session") || type.equals("static")) //$NON-NLS-1$ //$NON-NLS-2$
        {
            log.error("The 'session' and 'static' creators are deprecated. Use the 'new' creator"); //$NON-NLS-1$
            log.error("  For more information see the DWR website"); //$NON-NLS-1$
            type = "new"; //$NON-NLS-1$

            if (type.equals("session")) //$NON-NLS-1$
            {
                allower.setAttribute("scope", "session"); //$NON-NLS-1$ //$NON-NLS-2$
            }
        }

        try
        {
            Map params = createSettingMap(allower);
            creatorManager.addCreator(type, javascript, params);

            processPermissions(javascript, allower);
            processAuth(javascript, allower);
            processParameters(javascript, allower);
        }
        catch (NoClassDefFoundError ex)
        {
            log.info("Missing class for creator '" + type + "'. (javascript='" + javascript + "'). Cause: " + ex.getMessage()); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        }
        catch (Exception ex)
        {
            log.error("Failed to add creator: type=" + type + ", javascript=" + javascript, ex); //$NON-NLS-1$ //$NON-NLS-2$
        }
    }

    /**
     * Create a parameter map from nested <param name="foo" value="blah"/>
     * elements
     * @param parent The parent element
     * @return A map of parameters
     */
    private static Map createSettingMap(Element parent)
    {
        Map params = new HashMap();

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
            Creator creator = creatorManager.getCreator(javascript);
            Class dest = creator.getType();

            Method method = null;
            Method[] methods = dest.getMethods();
            for (int j = 0; j < methods.length; j++)
            {
                Method test = methods[j];
                if (test.getName().equals(methodName))
                {
                    if (method == null)
                    {
                        method = test;
                    }
                    else
                    {
                        log.warn("Setting extra type info to overloaded methods may fail with <parameter .../>"); //$NON-NLS-1$
                    }
                }
            }

            if (method == null)
            {
                log.error("Unable to find method called: " + methodName + " on type: " + dest.getName() + " from creator: " + javascript); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                continue;
            }

            String number = include.getAttribute(ATTRIBUTE_NUMBER);
            int paramNo = Integer.parseInt(number);

            String types = include.getAttribute(ATTRIBUTE_TYPE);
            StringTokenizer st = new StringTokenizer(types, ","); //$NON-NLS-1$

            int j = 0;
            while (st.hasMoreTokens())
            {
                String type = st.nextToken();
                Class clazz = Class.forName(type.trim());
                converterManager.setExtraTypeInfo(method, paramNo, j++, clazz);
            }
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
                log.warn("Ignoring illegal node type: " + type); //$NON-NLS-1$
                continue;
            }

            sigtext.append(node.getNodeValue());
        }

        SignatureParser sigp = new SignatureParser(converterManager);
        sigp.parse(sigtext.toString());
    }

    /**
     * Accessor for the DefaultCreatorManager that we configure
     * @param creatorManager The new DefaultConverterManager
     */
    public void setCreatorManager(CreatorManager creatorManager)
    {
        this.creatorManager = creatorManager;
    }

    /**
     * Accessor for the DefaultCreatorManager that we configure
     * @return the configured DefaultCreatorManager
     */
    public CreatorManager getCreatorManager()
    {
        return creatorManager;
    }

    /**
     * Accessor for the DefaultCreatorManager that we configure
     * @param converterManager The new DefaultConverterManager
     */
    public void setConverterManager(ConverterManager converterManager)
    {
        this.converterManager = converterManager;
    }

    /**
     * Accessor for the DefaultConverterManager that we configure
     * @return the configured DefaultConverterManager
     */
    public ConverterManager getConverterManager()
    {
        return converterManager;
    }

    /**
     * Accessor for the security manager
     * @return Returns the accessControl.
     */
    public AccessControl getAccessControl()
    {
        return accessControl;
    }

    /**
     * Accessor for the security manager
     * @param accessControl The accessControl to set.
     */
    public void setAccessControl(AccessControl accessControl)
    {
        this.accessControl = accessControl;
    }

    /**
     * The log stream
     */
    private static final Logger log = Logger.getLogger(DefaultConfiguration.class);

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

    /*
     * The element names
     */
    private static final String ELEMENT_INIT = "init"; //$NON-NLS-1$

    private static final String ELEMENT_ALLOW = "allow"; //$NON-NLS-1$

    private static final String ELEMENT_CREATE = "create"; //$NON-NLS-1$

    private static final String ELEMENT_CONVERT = "convert"; //$NON-NLS-1$

    private static final String ELEMENT_PARAM = "param"; //$NON-NLS-1$

    private static final String ELEMENT_INCLUDE = "include"; //$NON-NLS-1$

    private static final String ELEMENT_EXCLUDE = "exclude"; //$NON-NLS-1$

    private static final String ELEMENT_PARAMETER = "parameter"; //$NON-NLS-1$

    private static final String ELEMENT_AUTH = "auth"; //$NON-NLS-1$

    private static final String ELEMENT_SIGNATURES = "signatures"; //$NON-NLS-1$

    /*
     * The attribute names
     */
    private static final String ATTRIBUTE_ID = "id"; //$NON-NLS-1$

    private static final String ATTRIBUTE_CLASS = "class"; //$NON-NLS-1$

    private static final String ATTRIBUTE_CONVERTER = "converter"; //$NON-NLS-1$

    private static final String ATTRIBUTE_MATCH = "match"; //$NON-NLS-1$

    private static final String ATTRIBUTE_JAVASCRIPT = "javascript"; //$NON-NLS-1$

    private static final String ATTRIBUTE_CREATOR = "creator"; //$NON-NLS-1$

    private static final String ATTRIBUTE_NAME = "name"; //$NON-NLS-1$

    private static final String ATTRIBUTE_VALUE = "value"; //$NON-NLS-1$

    private static final String ATTRIBUTE_METHOD = "method"; //$NON-NLS-1$

    private static final String ATTRIBUTE_ROLE = "role"; //$NON-NLS-1$

    private static final String ATTRIBUTE_NUMBER = "number"; //$NON-NLS-1$

    private static final String ATTRIBUTE_TYPE = "type"; //$NON-NLS-1$
}
