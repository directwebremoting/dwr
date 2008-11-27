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

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.directwebremoting.create.NewCreator;
import org.directwebremoting.filter.ExtraLatencyAjaxFilter;
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
 * part of the DWR namespace, except <dwr:annotation-config />. <br/>
 * The DWR namespace is defined in the <code>spring-dwr-X.X.xsd</code> file. All
 * elements that are encountered in Spring configuration files are automatically
 * converted to their actual bean representation in the Spring bean registry.
 *
 * @author Erik Wiersma
 * @author Bram Smeets
 * @author Jose Noheda
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public abstract class DwrNamespaceHandler extends NamespaceHandlerSupport
{
    /* (non-Javadoc)
     * @see org.springframework.beans.factory.xml.NamespaceHandler#init()
     */
    public void init()
    {
        // register bean definition parsers and decorators for all dwr namespace elements
        registerBeanDefinitionParser("configuration", new ConfigurationBeanDefinitionParser());
        registerBeanDefinitionParser("controller", new ControllerBeanDefinitionParser());
        registerBeanDefinitionParser("url-mapping", new UrlMappingBeanDefinitionParser());
        registerBeanDefinitionParser("proxy-ref", new ProxyBeanDefinitionParser());

        registerBeanDefinitionDecorator("init", new InitDefinitionDecorator());
        registerBeanDefinitionDecorator("create", new CreatorBeanDefinitionDecorator());
        registerBeanDefinitionDecorator("convert", new ConverterBeanDefinitionDecorator());
        registerBeanDefinitionDecorator("signatures", new SignaturesBeanDefinitionDecorator());
        registerBeanDefinitionDecorator("remote", new RemoteBeanDefinitionDecorator());
    }

    protected static BeanDefinition registerSpringConfiguratorIfNecessary(BeanDefinitionRegistry registry)
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
     * @param creatorConfig
     * @param params
     * @param children The node list to check for nested elements
     */
    @SuppressWarnings("unchecked")
    protected void registerCreator(BeanDefinitionRegistry registry, String javascript, BeanDefinitionBuilder creatorConfig, Map<String, String> params, NodeList children)
    {
        registerSpringConfiguratorIfNecessary(registry);

        List<String> includes = new ArrayList<String>();
        creatorConfig.addPropertyValue("includes", includes);

        List<String> excludes = new ArrayList<String>();
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

            if ("dwr:latencyfilter".equals(node.getNodeName()))
            {
                BeanDefinitionBuilder beanFilter = BeanDefinitionBuilder.rootBeanDefinition(ExtraLatencyAjaxFilter.class);
                beanFilter.addPropertyValue("delay", child.getAttribute("delay"));
                BeanDefinitionHolder holder2 = new BeanDefinitionHolder(beanFilter.getBeanDefinition(), "__latencyFilter_" + javascript);
                BeanDefinitionReaderUtils.registerBeanDefinition(holder2, registry);

                ManagedList filterList = new ManagedList();
                filterList.add(new RuntimeBeanReference("__latencyFilter_" + javascript));
                creatorConfig.addPropertyValue("filters", filterList);
            }
            else if ("dwr:include".equals(node.getNodeName()))
            {
                includes.add(child.getAttribute("method"));
            }
            else if ("dwr:exclude".equals(node.getNodeName()))
            {
                excludes.add(child.getAttribute("method"));
            }
            else if ("dwr:auth".equals(node.getNodeName()))
            {
                auth.setProperty(child.getAttribute("method"), child.getAttribute("role"));
            }
            else if ("dwr:convert".equals(node.getNodeName()))
            {
                Element element = (Element) node;
                String type = element.getAttribute("type");
                String className = element.getAttribute("class");

                ConverterConfig converterConfig = new ConverterConfig();
                converterConfig.setType(type);
                parseConverterSettings(converterConfig, element);
                lookupConverters(registry).put(className, converterConfig);
            }
            else if ("dwr:filter".equals(node.getNodeName()))
            {
                Element element = (Element) node;
                String filterClass = element.getAttribute("class");
                List<Element> filterParamElements = DomUtils.getChildElementsByTagName(element, "param");
                BeanDefinitionBuilder beanFilter;
                try
                {
                    beanFilter = BeanDefinitionBuilder.rootBeanDefinition(ClassUtils.forName(filterClass));
                }
                catch (ClassNotFoundException e)
                {
                    throw new IllegalArgumentException("DWR filter class '" + filterClass + "' was not found. " + "Check the class name specified in <dwr:filter class=\"" + filterClass + "\" /> exists");
                }
                for (Element filterParamElement : filterParamElements)
                {
                    beanFilter.addPropertyValue(filterParamElement.getAttribute("name"), filterParamElement.getAttribute("value"));
                }
                BeanDefinitionHolder holder2 = new BeanDefinitionHolder(beanFilter.getBeanDefinition(), "__filter_" + filterClass + "_" + javascript);
                BeanDefinitionReaderUtils.registerBeanDefinition(holder2, registry);

                ManagedList filterList = new ManagedList();
                filterList.add(new RuntimeBeanReference("__filter_" + filterClass + "_" + javascript));
                creatorConfig.addPropertyValue("filters", filterList);
            }
            else if ("dwr:param".equals(node.getNodeName()))
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
        /* (non-Javadoc)
         * @see org.springframework.beans.factory.xml.BeanDefinitionParser#parse(org.w3c.dom.Element, org.springframework.beans.factory.xml.ParserContext)
         */
        @SuppressWarnings("unchecked")
        public BeanDefinition parse(Element element, ParserContext parserContext)
        {
            BeanDefinitionRegistry registry = parserContext.getRegistry();
            BeanDefinition beanDefinition = registerSpringConfiguratorIfNecessary(registry);

            Element initElement = DomUtils.getChildElementByTagName(element, "init");
            if (initElement != null)
            {
                decorate(initElement, new BeanDefinitionHolder(beanDefinition, DEFAULT_SPRING_CONFIGURATOR_ID), parserContext);
            }

            List<Element> createElements = DomUtils.getChildElementsByTagName(element, "create");
            for (Element createElement : createElements)
            {
                decorate(createElement, new BeanDefinitionHolder(beanDefinition, DEFAULT_SPRING_CONFIGURATOR_ID), parserContext);
            }

            List<Element> convertElements = DomUtils.getChildElementsByTagName(element, "convert");
            for (Element convertElement : convertElements)
            {
                decorate(convertElement, new BeanDefinitionHolder(beanDefinition, DEFAULT_SPRING_CONFIGURATOR_ID), parserContext);
            }

            List<Element> signatureElements = DomUtils.getChildElementsByTagName(element, "signatures");
            for (Element signatureElement : signatureElements)
            {
                decorate(signatureElement, new BeanDefinitionHolder(beanDefinition, DEFAULT_SPRING_CONFIGURATOR_ID), parserContext);
            }

            return beanDefinition;
        }
    }

    protected static class ControllerBeanDefinitionParser implements BeanDefinitionParser
    {
        /* (non-Javadoc)
         * @see org.springframework.beans.factory.xml.BeanDefinitionParser#parse(org.w3c.dom.Element, org.springframework.beans.factory.xml.ParserContext)
         */
        @SuppressWarnings("unchecked")
        public BeanDefinition parse(Element element, ParserContext parserContext)
        {
            BeanDefinitionBuilder dwrController = BeanDefinitionBuilder.rootBeanDefinition(DwrController.class);
            List<Object> configurators = new ManagedList();
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
                if (!StringUtils.hasText(beanName))
                {
                    beanName ="dwrController"; // Offer a sensible default if no id was specified
                }
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

        /**
         * @param dwrControllerDefinition
         * @param parent
         */
        protected void parseControllerParameters(BeanDefinitionBuilder dwrControllerDefinition, Element parent)
        {
            NodeList children = parent.getChildNodes();
            Map<String, String> params = new HashMap<String, String>();
            for (int i = 0; i < children.getLength(); i++)
            {
                Node node = children.item(i);

                if (node.getNodeType() == Node.TEXT_NODE || node.getNodeType() == Node.COMMENT_NODE)
                {
                    continue;
                }

                Element child = (Element) node;
                if ("dwr:config-param".equals(child.getNodeName()))
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

    /**
     * Registers a new bean definition based on <dwr:url-mapping /> schema.
     *
     * @author Jose Noheda [jose.noheda@gmail.com]
     */
    protected class UrlMappingBeanDefinitionParser implements BeanDefinitionParser
    {

        /**
         * Converts <dwr:url-mapping /> tag in the adequate DwrHandlerMapping bean definition.
         * @param element the <dwr:url-mapping /> tag
         * @param parserContext access to the registry
         * @return a DwrHandlerMapping bean definition
         */
        public BeanDefinition parse(Element element, ParserContext parserContext) {
            BeanDefinitionBuilder builder = BeanDefinitionBuilder.rootBeanDefinition(DwrHandlerMapping.class);
            parserContext.getRegistry().registerBeanDefinition("DwrAnnotationURLMapper", builder.getBeanDefinition());
            return parserContext.getRegistry().getBeanDefinition("DwrAnnotationURLMapper");
        }

    }

    /**
     * Registers a bean proxy based in <dwr:proxy-ref />
     *
     * @author Jose Noheda [jose.noheda@gmail.com]
     */
    protected class ProxyBeanDefinitionParser implements BeanDefinitionParser
    {

        public BeanDefinition parse(Element element, ParserContext parserContext)
        {
            String beanRef = element.getAttribute("bean");
            BeanDefinitionRegistry registry = parserContext.getRegistry();
            BeanDefinition beanRefDefinition = findParentDefinition(beanRef, registry);
            //BeanDefinitionHolder beanDefinitionHolder = new BeanDefinitionHolder(beanRefDefinition, beanRef);
            String javascript = element.getAttribute("javascript");
            if (!StringUtils.hasText(javascript))
            {
                if (log.isDebugEnabled())
                {
                    log.debug("No javascript name provided. Remoting using bean id [" + beanRef + "]");
                }
                javascript = StringUtils.capitalize(beanRef);
            }
            BeanDefinitionBuilder beanCreator = BeanDefinitionBuilder.rootBeanDefinition(BeanCreator.class);
            beanCreator.addPropertyValue("beanClass", resolveBeanClassname(beanRefDefinition, registry));
            beanCreator.addPropertyValue("beanId", beanRef);
            beanCreator.addDependsOn(beanRef);
            beanCreator.addPropertyValue("javascript", javascript);
            BeanDefinitionBuilder creatorConfig = BeanDefinitionBuilder.rootBeanDefinition(CreatorConfig.class);
            creatorConfig.addPropertyValue("creator", beanCreator.getBeanDefinition());
            registerCreator(parserContext.getRegistry(), javascript, creatorConfig, new HashMap<String, String>(), element.getChildNodes());
            return creatorConfig.getBeanDefinition();
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
                    throw new FatalBeanException("Unabled to find type for beanName '" + definition.getBeanName() + "'. " + "Check your bean has a correctly configured parent or provide a class for " + " the bean definition");
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
            if (!StringUtils.hasText(javascript))
            {
                if (log.isDebugEnabled())
                {
                    log.debug("No javascript name provided. Remoting using bean id [" + name + "]");
                }
                javascript = StringUtils.capitalize(name);
            }
            beanCreator.addPropertyValue("javascript", javascript);

            BeanDefinitionBuilder creatorConfig = BeanDefinitionBuilder.rootBeanDefinition(CreatorConfig.class);
            creatorConfig.addPropertyValue("creator", beanCreator.getBeanDefinition());
            registerCreator(parserContext.getRegistry(), javascript, creatorConfig, new HashMap<String, String>(), node.getChildNodes());

            return definition;
        }

    }

    /**
     *  Try getting the beanClassName from the definition and if that fails try to get it from
     *  the parent (and even parent BeanFactory if we have to).
     *  @param definition
     *  @param registry
     *  @return class name or null if not found
     */
    protected static String resolveBeanClassname(BeanDefinition definition, BeanDefinitionRegistry registry)
    {
        String beanClassName = definition.getBeanClassName();
        while(!StringUtils.hasText(beanClassName))
        {
            try
            {
                Method m = definition.getClass().getMethod("getParentName", new Class[0]);
                String parentName = (String) m.invoke(definition, new Object[0]);
                BeanDefinition parentDefinition = findParentDefinition(parentName, registry);
                beanClassName = parentDefinition.getBeanClassName();
                definition = parentDefinition;
            } catch (Exception e)
            {
                throw new FatalBeanException("No parent bean could be found for " + definition, e);
            }
        }
        return beanClassName;
    }

    protected static BeanDefinition findParentDefinition(String parentName, BeanDefinitionRegistry registry)
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
                BeanFactory parentBeanFactory = ((HierarchicalBeanFactory) registry).getParentBeanFactory();
                return findParentDefinition(parentName, (BeanDefinitionRegistry) parentBeanFactory);
            }
        }

        // we've exhausted all possibilities
        return null;
    }

    protected class ConverterBeanDefinitionDecorator implements BeanDefinitionDecorator
    {

        public BeanDefinitionHolder decorate(Node node, BeanDefinitionHolder definition, ParserContext parserContext)
        {
            Element element = (Element) node;
            String type = element.getAttribute("type");
            if ("preconfigured".equals(type))
            {
                type += ":" + element.getAttribute("ref");
            }
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

    /**
     * @param converterConfig
     * @param parent
     */
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
            if ("dwr:include".equals(child.getNodeName()))
            {
                converterConfig.addInclude(child.getAttribute("method"));
            }
            else if ("dwr:exclude".equals(child.getNodeName()))
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
        /* (non-Javadoc)
         * @see org.springframework.beans.factory.xml.BeanDefinitionDecorator#decorate(org.w3c.dom.Node, org.springframework.beans.factory.config.BeanDefinitionHolder, org.springframework.beans.factory.xml.ParserContext)
         */
        public BeanDefinitionHolder decorate(Node parent, BeanDefinitionHolder definition, ParserContext parserContext)
        {
            Map<String, String> converters = new HashMap<String, String>();
            Map<String, String> creators = new HashMap<String, String>();
            NodeList inits = parent.getChildNodes();
            for (int j = 0; j < inits.getLength(); j++)
            {
                Node node = inits.item(j);
                if (node.getNodeType() == Node.TEXT_NODE || node.getNodeType() == Node.COMMENT_NODE)
                {
                    continue;
                }

                Element child = (Element) inits.item(j);
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
                    throw new RuntimeException("An unknown sub node '" + child.getNodeName() + "' was found while parsing dwr:init");
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
        /* (non-Javadoc)
         * @see org.springframework.beans.factory.xml.BeanDefinitionDecorator#decorate(org.w3c.dom.Node, org.springframework.beans.factory.config.BeanDefinitionHolder, org.springframework.beans.factory.xml.ParserContext)
         */
        @SuppressWarnings("unchecked")
        public BeanDefinitionHolder decorate(Node node, BeanDefinitionHolder definition, ParserContext parserContext)
        {
            Element element = (Element) node;
            String javascript = element.getAttribute("javascript");
            String creatorType = element.getAttribute("type");

            BeanDefinitionBuilder creatorConfig = BeanDefinitionBuilder.rootBeanDefinition(CreatorConfig.class);

            // Configure "known" creators in the CreatorConfig. If unknown then just create the configuration
            // and leave it up DWR itself to decide if it's a valid creator type
            BeanDefinitionBuilder creator;
            Map<String, String> params = new HashMap<String, String>();
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
                    throw new BeanInitializationException("'class' is a required attribute for the declaration <dwr:creator type=\"null\"" + " javascript=\"" + javascript + "\" ... />");
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
                Map<String, String> registeredCreatorMap = (Map<String, String>) registeredCreators.getValue();
                String creatorClass = registeredCreatorMap.get(creatorType);
                if (creatorClass == null)
                {
                    // the creator type should have been registered
                    throw new UnsupportedOperationException("Type " + creatorType + " is not supported " + " or the custom creator has not been registered dwr:init");
                }
                else
                {
                    try
                    {
                        Class<?> clazz = Class.forName(creatorClass);
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
                        throw new FatalBeanException("ClassNotFoundException trying to register " + " creator '" + creatorClass + "' for javascript type '" + javascript + "'. Check the "
                                + " class in the classpath and that the creator is register in dwr:init", ex);
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

    /**
     * @param registry
     * @return Get a list of the defined Creators
     */
    @SuppressWarnings("unchecked")
    protected static Map<String, RuntimeBeanReference> lookupCreators(BeanDefinitionRegistry registry)
    {
        BeanDefinition config = registerSpringConfiguratorIfNecessary(registry);
        return (Map<String, RuntimeBeanReference>) config.getPropertyValues().getPropertyValue("creators").getValue();
    }

    /**
     * @param registry
     * @return Get a list of the defined Converters
     */
    @SuppressWarnings("unchecked")
    protected static Map<String, ConverterConfig> lookupConverters(BeanDefinitionRegistry registry)
    {
        BeanDefinition config = registerSpringConfiguratorIfNecessary(registry);
        return (Map<String, ConverterConfig>) config.getPropertyValues().getPropertyValue("converters").getValue();
    }

    protected static final String DEFAULT_SPRING_CONFIGURATOR_ID = "__dwrConfiguration";

    /**
     * The log stream
     */
    private static final Log log = LogFactory.getLog(DwrNamespaceHandler.class);

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
