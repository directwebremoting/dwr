/*
 * Copyright 2006 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.directwebremoting.spring;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.directwebremoting.create.NewCreator;
import org.directwebremoting.filter.ExtraLatencyAjaxFilter;
import org.directwebremoting.util.Logger;
import org.springframework.beans.FatalBeanException;
import org.springframework.beans.PropertyValue;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.HierarchicalBeanFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.ChildBeanDefinition;
import org.springframework.beans.factory.support.ManagedList;
import org.springframework.beans.factory.support.ManagedMap;
import org.springframework.beans.factory.xml.BeanDefinitionDecorator;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.BeanDefinitionParserDelegate;
import org.springframework.beans.factory.xml.NamespaceHandlerSupport;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;
import org.springframework.util.xml.DomUtils;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * The Spring namespace handler which handles all elements that are defined as
 * part of the DWR namespace. <br/>
 * The DWR namespace is defined in the <code>spring-dwr-X.X.xsd</code> file. All
 * elements that are encountered in Spring configuration files are automatically
 * converted to their actual bean representation in the Spring bean registry.
 *
 * @author Erik Wiersma
 * @author Bram Smeets
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class DwrNamespaceHandler extends NamespaceHandlerSupport
{
    /* (non-Javadoc)
     * @see org.springframework.beans.factory.xml.NamespaceHandler#init()
     */
    public void init()
    {
        // register bean definition parsers and decorators for all dwr namespace elements
        registerBeanDefinitionParser("configuration", new ConfigurationBeanDefinitionParser());
        registerBeanDefinitionParser("controller", new ControllerBeanDefinitionParser());

        registerBeanDefinitionDecorator("init", new InitDefinitionDecorator());
        registerBeanDefinitionDecorator("create", new CreatorBeanDefinitionDecorator());
        registerBeanDefinitionDecorator("convert", new ConverterBeanDefinitionDecorator());
        registerBeanDefinitionDecorator("signatures", new SignaturesBeanDefinitionDecorator());
        registerBeanDefinitionDecorator("remote", new RemoteBeanDefinitionDecorator());
    }
    
    /*
     *
     */
    protected BeanDefinition registerSpringConfiguratorIfNecessary(BeanDefinitionRegistry registry)
    {
        if (!registry.containsBeanDefinition(DEFAULT_SPRING_CONFIGURATOR_ID))
        {
            BeanDefinitionBuilder builder = BeanDefinitionBuilder.rootBeanDefinition(SpringConfigurator.class);
            builder.addPropertyValue("creators", new ManagedMap());
            builder.addPropertyValue("converters", new ManagedMap());
            registry.registerBeanDefinition(DEFAULT_SPRING_CONFIGURATOR_ID, builder.getBeanDefinition());
        }
        return registry.getBeanDefinition(DEFAULT_SPRING_CONFIGURATOR_ID);
    }

    /**
     * Registers a new {@link org.directwebremoting.extend.Creator} in the registry using name <code>javascript</code>.
     * @param registry The definition of all the Beans
     * @param javascript The name of the bean in the registry.
     * @param beanCreator The {@link org.directwebremoting.extend.Creator} to register.
     * @param children The node list to check for nested elements
     */
    protected void registerCreator(BeanDefinitionRegistry registry, String javascript, BeanDefinitionBuilder creatorConfig, Map params,  NodeList children)
    {
        registerSpringConfiguratorIfNecessary(registry);

        List includes = new ArrayList();
        creatorConfig.addPropertyValue("includes", includes);

        List excludes = new ArrayList();
        creatorConfig.addPropertyValue("excludes", excludes);

        Properties auth = new Properties();
        creatorConfig.addPropertyValue("auth", auth);

        // check to see if there are any nested elements here
        for (int i = 0; i < children.getLength(); i++)
        {
            Node node = children.item(i);

            if (node.getNodeType() == Node.TEXT_NODE || node.getNodeType() == Node.COMMENT_NODE)
            {
                continue;
            }

            Element child = (Element) node;

            if (node.getNodeName().equals("dwr:latencyfilter"))
            {
                BeanDefinitionBuilder beanFilter = BeanDefinitionBuilder.rootBeanDefinition(ExtraLatencyAjaxFilter.class);
                beanFilter.addPropertyValue("delay", child.getAttribute("delay"));
                BeanDefinitionHolder holder2 = new BeanDefinitionHolder(beanFilter.getBeanDefinition(), "__latencyFilter_" + javascript);
                BeanDefinitionReaderUtils.registerBeanDefinition(holder2, registry);

                ManagedList filterList = new ManagedList();
                filterList.add(new RuntimeBeanReference("__latencyFilter_" + javascript));
                creatorConfig.addPropertyValue("filters", filterList);
            }
            else if (node.getNodeName().equals("dwr:include"))
            {
                includes.add(child.getAttribute("method"));
            }
            else if (node.getNodeName().equals("dwr:exclude"))
            {
                excludes.add(child.getAttribute("method"));
            }
            else if (node.getNodeName().equals("dwr:auth"))
            {
                auth.setProperty(child.getAttribute("method"), child.getAttribute("role"));
            }
            else if (node.getNodeName().equals("dwr:convert"))
            {
                Element element = (Element) node;
                String type = element.getAttribute("type");
                String className = element.getAttribute("class");

                ConverterConfig converterConfig = new ConverterConfig();
                converterConfig.setType(type);
                parseConverterSettings(converterConfig, element);
                lookupConverters(registry).put(className, converterConfig);
            }
            else if (node.getNodeName().equals("dwr:filter"))
            {
                Element element = (Element) node;
                String filterClass = element.getAttribute("class");
                BeanDefinitionBuilder beanFilter;
                try
                {
                    beanFilter = BeanDefinitionBuilder.rootBeanDefinition(ClassUtils.forName(filterClass));
                }
                catch (ClassNotFoundException e)
                {
                    throw new IllegalArgumentException("DWR filter class '" + filterClass + "' was not found. " + 
                                                       "Check the class name specified in <dwr:filter class=\"" + filterClass + 
                                                       "\" /> exists");
                }
                BeanDefinitionHolder holder2 = new BeanDefinitionHolder(beanFilter.getBeanDefinition(), "__filter_" + filterClass + "_" + javascript);
                BeanDefinitionReaderUtils.registerBeanDefinition(holder2, registry);

                ManagedList filterList = new ManagedList();
                filterList.add(new RuntimeBeanReference("__filter_" + filterClass + "_" + javascript));
                creatorConfig.addPropertyValue("filters", filterList);
            }
            else if (node.getNodeName().equals("dwr:param"))
            {
                Element element = (Element) node;
                String name = element.getAttribute("name");
                String value = element.getAttribute("value");
                params.put(name, value);
            }
            else
            {
                throw new RuntimeException("an unknown dwr:remote sub node was fouund: " + node.getNodeName());
            }
        }
        creatorConfig.addPropertyValue("params", params);
        
        String creatorConfigName = "__" + javascript;
        BeanDefinitionHolder holder3 = new BeanDefinitionHolder(creatorConfig.getBeanDefinition(), creatorConfigName);
        BeanDefinitionReaderUtils.registerBeanDefinition(holder3, registry);

        lookupCreators(registry).put(javascript, new RuntimeBeanReference(creatorConfigName));
    }

    protected class ConfigurationBeanDefinitionParser implements BeanDefinitionParser
    {
        
        public BeanDefinition parse(Element element, ParserContext parserContext)
        {
            BeanDefinitionRegistry registry = parserContext.getRegistry();
            BeanDefinition beanDefinition = registerSpringConfiguratorIfNecessary(registry);

            Element initElement = DomUtils.getChildElementByTagName(element, "init");
            if (initElement != null)
            {
                decorate(initElement, new BeanDefinitionHolder(beanDefinition, DEFAULT_SPRING_CONFIGURATOR_ID), parserContext);    
            }
            
            List createElements = DomUtils.getChildElementsByTagName(element, "create");
            Iterator iter = createElements.iterator();
            while (iter.hasNext())
            {
                Element createElement = (Element) iter.next();
                decorate(createElement, new BeanDefinitionHolder(beanDefinition, DEFAULT_SPRING_CONFIGURATOR_ID), parserContext);
            }

            List convertElements = DomUtils.getChildElementsByTagName(element, "convert");
            iter = convertElements.iterator();
            while (iter.hasNext())
            {
                Element convertElement = (Element) iter.next();
                decorate(convertElement, new BeanDefinitionHolder(beanDefinition, DEFAULT_SPRING_CONFIGURATOR_ID), parserContext);
            }
            
            List signatureElements = DomUtils.getChildElementsByTagName(element, "signatures");
            for (Iterator i = signatureElements.iterator(); i.hasNext();)
            {
                Element signatureElement = (Element) i.next();
                decorate(signatureElement, new BeanDefinitionHolder(beanDefinition, DEFAULT_SPRING_CONFIGURATOR_ID), parserContext);
            }

            return beanDefinition;
        }
    }

    protected class ControllerBeanDefinitionParser implements BeanDefinitionParser
    {
        public BeanDefinition parse(Element element, ParserContext parserContext)
        {
            BeanDefinitionBuilder dwrController = BeanDefinitionBuilder.rootBeanDefinition(DwrController.class);
            List configurators = new ManagedList();
            configurators.add(new RuntimeBeanReference(DEFAULT_SPRING_CONFIGURATOR_ID));
            dwrController.addPropertyValue("configurators", configurators);

            String debug = element.getAttribute("debug");
            if (StringUtils.hasText(debug))
            {
                dwrController.addPropertyValue("debug", debug);
            }

            String beanName = element.getAttribute(BeanDefinitionParserDelegate.ID_ATTRIBUTE);
            String nameAttr = element.getAttribute(BeanDefinitionParserDelegate.NAME_ATTRIBUTE);
            String[] aliases = null;
            if (!StringUtils.hasText(beanName))
            {
                beanName = element.getAttribute("name");
            }
            else
            {
                String aliasName = element.getAttribute("name");
                if (StringUtils.hasText(aliasName))
                {
                    aliases = StringUtils.tokenizeToStringArray(nameAttr, BeanDefinitionParserDelegate.BEAN_NAME_DELIMITERS);
                }
            }

            parseControllerParameters(dwrController, element);
            
            BeanDefinitionHolder holder = new BeanDefinitionHolder(dwrController.getBeanDefinition(), beanName, aliases);
            BeanDefinitionReaderUtils.registerBeanDefinition(holder, parserContext.getRegistry());

            return dwrController.getBeanDefinition();
        }
        
        protected void parseControllerParameters(BeanDefinitionBuilder dwrControllerDefinition, Element parent)
        {
            NodeList children = parent.getChildNodes();
            Map params = new HashMap();
            for (int i = 0; i < children.getLength(); i++)
            {
                Node node = children.item(i);

                if (node.getNodeType() == Node.TEXT_NODE || node.getNodeType() == Node.COMMENT_NODE)
                {
                    continue;
                }

                Element child = (Element) node;
                if (child.getNodeName().equals("dwr:config-param"))
                {
                    String paramName = child.getAttribute("name");
                    String value = child.getAttribute("value");
                    params.put(paramName, value);
                }
                else
                {
                    throw new RuntimeException("an unknown dwr:controller sub node was found: " + node.getNodeName());
                }
            }
            dwrControllerDefinition.addPropertyValue("configParams", params);
        }
    }

    protected class RemoteBeanDefinitionDecorator implements BeanDefinitionDecorator
    {
        /**
         * Registers an &lt;dwr:remote ... /&gt; element.
         */
        public BeanDefinitionHolder decorate(Node node, BeanDefinitionHolder definition, ParserContext parserContext)
        {
            Element element = (Element) node;

            String javascript = element.getAttribute("javascript");

            BeanDefinitionBuilder beanCreator = BeanDefinitionBuilder.rootBeanDefinition(BeanCreator.class);

            try
            {
                String beanClassName = resolveBeanClassname(definition.getBeanDefinition(), parserContext.getRegistry());
                if (beanClassName == null) 
                {
                    throw new FatalBeanException("Unabled to find type for beanName '" + definition.getBeanName() + 
                                                 "'. " + "Check your bean has a correctly configured parent or provide a class for " + 
                                                 " the bean definition");
                }
                beanCreator.addPropertyValue("beanClass", ClassUtils.forName(beanClassName));
            }
            catch (ClassNotFoundException e)
            {
                throw new FatalBeanException("Unable to create DWR bean creator for '" + definition.getBeanName() + "'.", e);
            }

            String name = definition.getBeanName();
            if (name.startsWith("scopedTarget."))
            {
                name = name.substring(name.indexOf(".") + 1);
            }
            beanCreator.addPropertyValue("beanId", name);
            beanCreator.addPropertyValue("javascript", javascript);

            BeanDefinitionBuilder creatorConfig = BeanDefinitionBuilder.rootBeanDefinition(CreatorConfig.class);
            creatorConfig.addPropertyValue("creator", beanCreator.getBeanDefinition());            
            registerCreator(parserContext.getRegistry(), javascript, creatorConfig, new HashMap(), node.getChildNodes());

            return definition;
        }
        
        /**
         *  Try getting the beanClassName from the definition and if that fails try to get it from 
         *  the parent (and even parent BeanFactory if we have to).
         *  
         *  @param definition 
         *  @param registry
         *  @return class name or null if not found
         */
        private String resolveBeanClassname(BeanDefinition definition, BeanDefinitionRegistry registry) 
        {
            String beanClassName = definition.getBeanClassName();    
            if (!StringUtils.hasText(beanClassName)) 
            {
                while (definition instanceof ChildBeanDefinition )
                {
                    String parentName = ((ChildBeanDefinition)definition).getParentName();                    
                    BeanDefinition parentDefinition = findParentDefinition(parentName, registry);
                    if (parentDefinition == null)
                    {
                        if (log.isDebugEnabled()) 
                        {
                            log.debug("No parent bean named '" + parentName + "' could be found in the " + 
                                      "hierarchy of BeanFactorys. Check you've defined a bean called '" + parentName + "'");
                        }
                        break;
                    }
                    beanClassName = parentDefinition.getBeanClassName();
                    if (StringUtils.hasText(beanClassName ))
                    {
                        // found the class name we were looking for
                        break;
                    }
                    definition = parentDefinition;
                }
            }
           
            return beanClassName;
        }
        
        private BeanDefinition findParentDefinition(String parentName, BeanDefinitionRegistry registry)
        {   
            if (registry != null) 
            {
                if (registry.containsBeanDefinition(parentName)) 
                {
                    return registry.getBeanDefinition(parentName);
                } 
                else if (registry instanceof HierarchicalBeanFactory) 
                {
                    // Try to get parent definition from the parent BeanFactory. This could return null
                    BeanFactory parentBeanFactory = ((HierarchicalBeanFactory)registry).getParentBeanFactory();
                    return findParentDefinition(parentName, (BeanDefinitionRegistry)parentBeanFactory);
                } 
            }
            
            // we've exhausted all possibilities        
            return null;
        }
    }

    protected class ConverterBeanDefinitionDecorator implements BeanDefinitionDecorator
    {

        public BeanDefinitionHolder decorate(Node node, BeanDefinitionHolder definition, ParserContext parserContext)
        {
            Element element = (Element) node;
            String type = element.getAttribute("type");
            String className = element.getAttribute("class");
            String javascriptClassName = element.getAttribute("javascript");
            
            BeanDefinitionRegistry registry = parserContext.getRegistry();

            ConverterConfig converterConfig = new ConverterConfig();
            converterConfig.setType(type);
            converterConfig.setJavascriptClassName(javascriptClassName);
            parseConverterSettings(converterConfig, element);
            lookupConverters(registry).put(className, converterConfig);

            return definition;
        }
    }

    protected void parseConverterSettings(ConverterConfig converterConfig, Element parent)
    {
        NodeList children = parent.getChildNodes();

        // check to see if there are any nested elements here
        for (int i = 0; i < children.getLength(); i++)
        {
            Node node = children.item(i);

            if (node.getNodeType() == Node.TEXT_NODE || node.getNodeType() == Node.COMMENT_NODE)
            {
                continue;
            }

            Element child = (Element) node;
            if (child.getNodeName().equals("dwr:include"))
            {
                converterConfig.addInclude(child.getAttribute("method"));
            }
            else if (child.getNodeName().equals("dwr:exclude"))
            {
                converterConfig.addExclude(child.getAttribute("method"));
            }
            /* TODO Why is this only a property of ObjectConverter?
             else if (child.getNodeName().equals("dwr:force"))
             {
             converterConfig.setForce(Boolean.parseBoolean(child.getAttribute("value")));
             }
             */
            else
            {
                throw new RuntimeException("an unknown dwr:remote sub node was found: " + node.getNodeName());
            }
        }

    }

    /**
     * Parse the <code>&lt;dwr:init&gt;</code> elements
     */
    protected class InitDefinitionDecorator implements BeanDefinitionDecorator
    {
        public BeanDefinitionHolder decorate(Node parent, BeanDefinitionHolder definition, ParserContext parserContext)
        {           
            Map converters = new HashMap();
            Map creators = new HashMap();
            NodeList inits = parent.getChildNodes();
            for (int j = 0; j < inits.getLength(); j++)
            {
                Node node = inits.item(j);
                if (node.getNodeType() == Node.TEXT_NODE || node.getNodeType() == Node.COMMENT_NODE)
                {
                    continue;
                }

                Element child = (Element)inits.item(j);
                if (child.getNodeName().equals(ELEMENT_CREATOR))
                {
                    String id = child.getAttribute(ATTRIBUTE_ID);
                    String className = child.getAttribute(ATTRIBUTE_CLASS);
                    creators.put(id, className);
                }
                else if (child.getNodeName().equals(ELEMENT_CONVERTER))
                {
                    String id = child.getAttribute(ATTRIBUTE_ID);
                    String className = child.getAttribute(ATTRIBUTE_CLASS);
                    converters.put(id, className);
                }
                else
                {
                    throw new RuntimeException("An unknown sub node '" + child.getNodeName() + 
                            "' was found while parsing dwr:init");
                }
            }

            
            BeanDefinition configurator = registerSpringConfiguratorIfNecessary(parserContext.getRegistry());
            configurator.getPropertyValues().addPropertyValue("creatorTypes", creators);
            configurator.getPropertyValues().addPropertyValue("converterTypes", converters);

            return definition;
        }
    }

    
    /**
     * Uses the BeanDefinitionDecorator since we need access to the name of the parent definition??
     * Register the creatores: spring, new, null, scripted, jsf, struts, pageflow
     */
    protected class CreatorBeanDefinitionDecorator implements BeanDefinitionDecorator
    {
        
        public BeanDefinitionHolder decorate(Node node, BeanDefinitionHolder definition, ParserContext parserContext)
        {
            Element element = (Element) node;
            String javascript = element.getAttribute("javascript");
            String creatorType = element.getAttribute("type");

            BeanDefinitionBuilder creatorConfig = BeanDefinitionBuilder.rootBeanDefinition(CreatorConfig.class);
            
            // Configure "known" creators in the CreatorConfig. If unknown then just create the configuration
            // and leave it up DWR itself to decide if it's a valid creator type
            BeanDefinitionBuilder creator;
            Map params = new HashMap();
            
            if ("spring".equals(creatorType))
            {
                // TODO Refactor so that both spring creators use the same code...
                BeanDefinitionBuilder springCreator = BeanDefinitionBuilder.rootBeanDefinition(BeanCreator.class);
                
                springCreator.addPropertyValue("javascript", javascript);
                
                NodeList children = element.getChildNodes();
                for (int i = 0; i < children.getLength(); i++)
                {
                    Node childNode = children.item(i);
                    if (childNode.getNodeType() == Node.TEXT_NODE || childNode.getNodeType() == Node.COMMENT_NODE)
                    {
                        continue;
                    }
                    
                    Element child = (Element) childNode;
                    String paramName = child.getAttribute("name");
                    String value = child.getAttribute("value");
                    if ("beanName".equals(paramName) || "beanId".equals(paramName)) {
                        springCreator.addPropertyValue("beanId", value);
                    } else {
                        params.put(paramName, value);    
                    }
                }
                
                creatorConfig.addPropertyValue("creator", springCreator.getBeanDefinition());         
            }
            else if ("new".equals(creatorType))
            {
                creator = BeanDefinitionBuilder.rootBeanDefinition(NewCreator.class);
                creator.addPropertyValue("className", node.getAttributes().getNamedItem("class").getNodeValue());
                creator.addPropertyValue("javascript", javascript);
                creatorConfig.addPropertyValue("creator", creator.getBeanDefinition());
            }
            else if ("null".equals(creatorType))
            {
                creatorConfig.addPropertyValue("creatorType", "none");
                String className = element.getAttribute("class");
                if (className == null || "".equals(className)) 
                {
                    throw new BeanInitializationException("'class' is a required attribute for the declaration <dwr:creator type=\"null\"" + 
                            " javascript=\"" + javascript + "\" ... />");
                }
                params.put("class", className);
            }
            else if ("pageflow".equals(creatorType))
            {
                creatorConfig.addPropertyValue("creatorType", creatorType);
            }
            else if ("jsf".equals(creatorType) || "scripted".equals(creatorType) || "struts".equals(creatorType))
            {
                creatorConfig.addPropertyValue("creatorType", creatorType);
            }
            else
            {
                if (log.isDebugEnabled())
                {
                    log.debug("Looking up creator type '" + creatorType + "'");
                }
                // TODO We should delay the initialization of the creatorClass until after the bean 
                // definitions have been parsed.  
                BeanDefinition configurator = registerSpringConfiguratorIfNecessary(parserContext.getRegistry());
                PropertyValue registeredCreators = configurator.getPropertyValues().getPropertyValue("creatorTypes");
                Map registeredCreatorMap = (Map)registeredCreators.getValue();
                String creatorClass = (String)registeredCreatorMap.get(creatorType);
                if (creatorClass == null)
                {
                    // the creator type should have been registered
                    throw new UnsupportedOperationException("Type " + creatorType + " is not supported " + 
                            " or the custom creator has not been registered dwr:init");                    
                } 
                else 
                {
                    try 
                    {
                        Class clazz = Class.forName(creatorClass);
                        creator = BeanDefinitionBuilder.rootBeanDefinition(clazz);
                        creatorConfig.addPropertyValue("creator", creator.getBeanDefinition());
                        String className = element.getAttribute("class");
                        if (StringUtils.hasText(className))
                        {
                            params.put("class", className);
                        }
                    } 
                    catch (ClassNotFoundException ex) 
                    {
                        throw new FatalBeanException("ClassNotFoundException trying to register " +  
                                " creator '" + creatorClass +  "' for javascript type '" + javascript +"'. Check the " + 
                                " class in the classpath and that the creator is register in dwr:init", ex);
                    }
                }
            }

            registerCreator(parserContext.getRegistry(), javascript, creatorConfig, params, node.getChildNodes());

            return definition;
        }
    }

    protected class SignaturesBeanDefinitionDecorator implements BeanDefinitionDecorator
    {

        public BeanDefinitionHolder decorate(Node node, BeanDefinitionHolder definition, ParserContext parserContext)
        {
            BeanDefinitionRegistry registry = parserContext.getRegistry();
            BeanDefinition config = registerSpringConfiguratorIfNecessary(registry);

            StringBuffer sigtext = new StringBuffer();
            NodeList children = node.getChildNodes();
            for (int i = 0; i < children.getLength(); i++)
            {
                Node child = children.item(i);
                if (child.getNodeType() != Node.TEXT_NODE && child.getNodeType() != Node.CDATA_SECTION_NODE)
                {
                    log.warn("Ignoring illegal node type: " + child.getNodeType());
                    continue;
                }
                sigtext.append(child.getNodeValue());
            }

            config.getPropertyValues().addPropertyValue("signatures", sigtext.toString());

            return definition;
        }

    }

    protected Map lookupCreators(BeanDefinitionRegistry registry)
    {
        BeanDefinition config = registerSpringConfiguratorIfNecessary(registry);
        return (Map) config.getPropertyValues().getPropertyValue("creators").getValue();
    }

    protected Map lookupConverters(BeanDefinitionRegistry registry)
    {
        BeanDefinition config = registerSpringConfiguratorIfNecessary(registry);
        return (Map) config.getPropertyValues().getPropertyValue("converters").getValue();
    }

    protected final static String DEFAULT_SPRING_CONFIGURATOR_ID = "__dwrConfiguration";

    /**
     * The log stream
     */
    protected static final Logger log = Logger.getLogger(DwrNamespaceHandler.class);
    
    /*
     * The element names
     */
    private static final String ELEMENT_CONVERTER = "dwr:converter";
    
    private static final String ELEMENT_CREATOR = "dwr:creator";
    
    /*
     * The attribute names
     */
    private static final String ATTRIBUTE_ID = "id";

    private static final String ATTRIBUTE_CLASS = "class";
    
    
}
