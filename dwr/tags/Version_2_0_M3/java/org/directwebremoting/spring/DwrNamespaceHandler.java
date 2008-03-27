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

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.*;
import org.springframework.beans.factory.xml.*;
import org.springframework.util.xml.DomUtils;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.directwebremoting.filter.ExtraLatencyAjaxFilter;
import org.directwebremoting.create.NewCreator;

import java.util.*;

/**
 * The Spring namespace handler which handles all elements that are defined as
 * part of the DWR namespace. <br/>
 * The DWR namespace is defined in the <code>spring-dwr.xsd</code> file. All
 * elements that are encountered in Spring configuration files are automatically
 * converted to their actual bean representation in the Spring bean registry.
 * @author Erik Wiersma
 * @author Bram Smeets
 */
public class DwrNamespaceHandler extends NamespaceHandlerSupport
{
    /* (non-Javadoc)
     * @see org.springframework.beans.factory.xml.NamespaceHandler#init()
     */
    public void init()
    {
        registerBeanDefinitionParser("configuration", new ConfigurationBeanDefinitionParser());
        registerBeanDefinitionParser("controller", new ControllerBeanDefinitionParser());

        registerBeanDefinitionDecorator("create", new CreatorBeanDefinitionDecorator());
        registerBeanDefinitionDecorator("convert", new ConverterBeanDefinitionDecorator());
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
            builder.addPropertyValue("creators", creators);
            builder.addPropertyValue("converters", converters);
            registry.registerBeanDefinition(DEFAULT_SPRING_CONFIGURATOR_ID, builder.getBeanDefinition());
            return builder.getBeanDefinition();
        }
        return registry.getBeanDefinition(DEFAULT_SPRING_CONFIGURATOR_ID);
    }

    /**
     * Registers a new {@link org.directwebremoting.Creator} in the registry using name <code>javascript</code>.
     * TODO: Specifically tailored to SpringCreator; ignores <code>type</code>
     * @param registryBuilder
     * @param javascript The name of the bean in the registry.
     * @param beanCreator The {@link org.directwebremoting.Creator} to register.
     * @param children The node list to check for nested elements
     */
    protected void registerCreator(BeanDefinitionRegistryBuilder registryBuilder, String javascript, BeanDefinitionBuilder beanCreator, NodeList children)
    {
        registerSpringConfiguratorIfNecessary(registryBuilder.getRegistry());

        registryBuilder.register(beanCreator);

        BeanDefinitionBuilder creatorConfig = BeanDefinitionBuilder.rootBeanDefinition(CreatorConfig.class);
        creatorConfig.addPropertyReference("creator", beanCreator);

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
                registryBuilder.register("__latencyFilter_" + javascript, beanFilter);

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
            else
            {
                throw new RuntimeException("an unknown dwr:remote sub node was fouund: " + node.getNodeName());
            }
        }

        String creatorConfigName = "__" + javascript;
        registryBuilder.register(creatorConfigName, creatorConfig);

        creators.put(javascript, new RuntimeBeanReference(creatorConfigName));
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
                findDecoratorForNode(createElement).decorate(createElement, new BeanDefinitionHolder(beanDefinition, DEFAULT_SPRING_CONFIGURATOR_ID), parserContext);
            }

            List convertElements = DomUtils.getChildElementsByTagName(element, "convert");
            iter = convertElements.iterator();
            while (iter.hasNext())
            {
                Element convertElement = (Element) iter.next();
                findDecoratorForNode(convertElement).decorate(convertElement, new BeanDefinitionHolder(beanDefinition, DEFAULT_SPRING_CONFIGURATOR_ID), parserContext);
            }

            return beanDefinition;
        }
    }

    protected class ControllerBeanDefinitionParser implements BeanDefinitionParser
    {
        public BeanDefinition parse(Element element, ParserContext parserContext)
        {
            BeanDefinitionRegistryBuilder registryBuilder = new BeanDefinitionRegistryBuilder(parserContext.getRegistry());
            BeanDefinitionBuilder dwrController = BeanDefinitionBuilder.rootBeanDefinition(DwrController.class);
            List configurators = new ManagedList();
            configurators.add(new RuntimeBeanReference(DEFAULT_SPRING_CONFIGURATOR_ID));
            dwrController.addPropertyValue("configurators", configurators);
            dwrController.addPropertyValue("debug", element.getAttribute("debug"));
            registryBuilder.register(element.getAttribute("id"), dwrController);
            return dwrController.getBeanDefinition();
        }
    }

    protected class RemoteBeanDefinitionDecorator implements BeanDefinitionDecorator
    {
        public BeanDefinitionHolder decorate(Node node, BeanDefinitionHolder definition, ParserContext parserContext)
        {
            BeanDefinitionRegistryBuilder registryBuilder = new BeanDefinitionRegistryBuilder(parserContext.getRegistry());

            Element element = (Element) node;

            String parentBeanName = definition.getBeanName();
            String javascript = element.getAttribute("javascript");

            BeanDefinitionBuilder beanCreator = BeanDefinitionBuilder.rootBeanDefinition(BeanCreator.class);
            beanCreator.addPropertyValue("bean", new RuntimeBeanReference(parentBeanName));
            beanCreator.addPropertyValue("javascript", javascript);

            registerCreator(registryBuilder, javascript, beanCreator, node.getChildNodes());

            return definition;
        }
    }

    protected class ConverterBeanDefinitionDecorator implements BeanDefinitionDecorator
    {

        public BeanDefinitionHolder decorate(Node node, BeanDefinitionHolder definition, ParserContext parserContext)
        {
            Element element = (Element) node;
            String type = element.getAttribute("type");

            if ("array".equals(type))
            {
                throw new UnsupportedOperationException("Type " + type + " is not yet supported");
            }
            else if ("bean".equals(type))
            {
                converters.put(element.getAttribute("class"), "bean");
            }
            else if ("collection".equals(type))
            {
                throw new UnsupportedOperationException("Type " + type + " is not yet supported");
            }
            else if ("map".equals(type))
            {
                throw new UnsupportedOperationException("Type " + type + " is not yet supported");
            }
            else if ("enum".equals(type))
            {
                throw new UnsupportedOperationException("Type " + type + " is not yet supported");
            }
            else
            {
                throw new UnsupportedOperationException("Type " + type + " is not yet supported");
            }
            return definition;
        }
    }

    /**
     * Uses the BeanDefinitionDecorator since we need access to the name of the parent definition??
     */
    protected class CreatorBeanDefinitionDecorator implements BeanDefinitionDecorator
    {
        public BeanDefinitionHolder decorate(Node node, BeanDefinitionHolder definition, ParserContext parserContext)
        {
            BeanDefinitionRegistryBuilder registryBuilder = new BeanDefinitionRegistryBuilder(parserContext.getRegistry());

            String parentBeanName = definition.getBeanName();
            Element element = (Element) node;
            String javascript = element.getAttribute("javascript");
            String type = element.getAttribute("type");

            // Make sure the Creator is registered in the registry.
            BeanDefinitionBuilder creator;
            if ("spring".equals(type))
            {
                // TODO: duplicate of RemoteBeanDefinitionDecorator
                creator = BeanDefinitionBuilder.rootBeanDefinition(SpringCreator.class);
                creator.addPropertyValue("bean", new RuntimeBeanReference(parentBeanName));
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

            registerCreator(registryBuilder, javascript, creator, node.getChildNodes());

            return definition;
        }
    }

    protected final static String DEFAULT_SPRING_CONFIGURATOR_ID = "__dwrConfiguration";

    private Map creators = new ManagedMap();

    protected Map converters = new ManagedMap();
}
