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
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.directwebremoting.create.NewCreator;
import org.directwebremoting.filter.ExtraLatencyAjaxFilter;
import org.directwebremoting.util.Logger;
import org.springframework.beans.FatalBeanException;
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
     * TODO: Specifically tailored to SpringCreator; ignores <code>type</code>
     * @param registry The definition of all the Beans
     * @param javascript The name of the bean in the registry.
     * @param beanCreator The {@link org.directwebremoting.extend.Creator} to register.
     * @param children The node list to check for nested elements
     */
    protected void registerCreator(BeanDefinitionRegistry registry, String javascript, BeanDefinitionBuilder beanCreator, NodeList children)
    {
        registerSpringConfiguratorIfNecessary(registry);

        BeanDefinitionHolder holder = new BeanDefinitionHolder(beanCreator.getBeanDefinition(), "__" + javascript + "_creator");
        BeanDefinitionReaderUtils.registerBeanDefinition(holder, registry);

        BeanDefinitionBuilder creatorConfig = BeanDefinitionBuilder.rootBeanDefinition(CreatorConfig.class);
        creatorConfig.addPropertyReference("creator", "__" + javascript + "_creator");

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
                    // TODO: proper error handling
                    throw new IllegalArgumentException("bla");
                }
                BeanDefinitionHolder holder2 = new BeanDefinitionHolder(beanFilter.getBeanDefinition(), "__filter_" + filterClass + "_" + javascript);
                BeanDefinitionReaderUtils.registerBeanDefinition(holder2, registry);

                ManagedList filterList = new ManagedList();
                filterList.add(new RuntimeBeanReference("__filter_" + filterClass + "_" + javascript));
                creatorConfig.addPropertyValue("filters", filterList);
            }
            else
            {
                throw new RuntimeException("an unknown dwr:remote sub node was fouund: " + node.getNodeName());
            }
        }

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
            BeanDefinitionHolder holder = new BeanDefinitionHolder(dwrController.getBeanDefinition(), beanName, aliases);
            BeanDefinitionReaderUtils.registerBeanDefinition(holder, parserContext.getRegistry());

            return dwrController.getBeanDefinition();
        }
    }

    protected class RemoteBeanDefinitionDecorator implements BeanDefinitionDecorator
    {
        public BeanDefinitionHolder decorate(Node node, BeanDefinitionHolder definition, ParserContext parserContext)
        {
            Element element = (Element) node;

            String javascript = element.getAttribute("javascript");

            BeanDefinitionBuilder beanCreator = BeanDefinitionBuilder.rootBeanDefinition(BeanCreator.class);

            try
            {
                beanCreator.addPropertyValue("beanClass", ClassUtils.forName(definition.getBeanDefinition().getBeanClassName()));
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

            registerCreator(parserContext.getRegistry(), javascript, beanCreator, node.getChildNodes());

            return definition;
        }
    }

    protected class ConverterBeanDefinitionDecorator implements BeanDefinitionDecorator
    {

        public BeanDefinitionHolder decorate(Node node, BeanDefinitionHolder definition, ParserContext parserContext)
        {
            Element element = (Element) node;
            String type = element.getAttribute("type");
            String className = element.getAttribute("class");

            BeanDefinitionRegistry registry = parserContext.getRegistry();

            ConverterConfig converterConfig = new ConverterConfig();
            converterConfig.setType(type);
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
                throw new RuntimeException("an unknown dwr:remote sub node was fouund: " + node.getNodeName());
            }
        }

    }

    /**
     * Uses the BeanDefinitionDecorator since we need access to the name of the parent definition??
     */
    protected class CreatorBeanDefinitionDecorator implements BeanDefinitionDecorator
    {
        public BeanDefinitionHolder decorate(Node node, BeanDefinitionHolder definition, ParserContext parserContext)
        {
            Element element = (Element) node;
            String javascript = element.getAttribute("javascript");
            String type = element.getAttribute("type");

            // Make sure the Creator is registered in the registry.
            BeanDefinitionBuilder creator;
            if ("spring".equals(type))
            {
                // TODO: duplicate of RemoteBeanDefinitionDecorator
                creator = BeanDefinitionBuilder.rootBeanDefinition(SpringCreator.class);
                // creator.addPropertyValue("bean", new RuntimeBeanReference(parentBeanName));
                try
                {
                    creator.addPropertyValue("beanClass", Class.forName(definition.getBeanDefinition().getBeanClassName()));
                }
                catch (ClassNotFoundException e)
                {
                    throw new FatalBeanException("Unable to create DWR bean creator for '" + definition.getBeanName() + "'.", e);
                }
                creator.addPropertyValue("javascript", javascript);
            }
            else if ("new".equals(type))
            {
                creator = BeanDefinitionBuilder.rootBeanDefinition(NewCreator.class);
                creator.addPropertyValue("className", node.getAttributes().getNamedItem("class").getNodeValue());
                creator.addPropertyValue("javascript", javascript);
            }
            else
            {
                throw new UnsupportedOperationException("Type " + type + " is not yet supported");
            }

            registerCreator(parserContext.getRegistry(), javascript, creator, node.getChildNodes());

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
}
