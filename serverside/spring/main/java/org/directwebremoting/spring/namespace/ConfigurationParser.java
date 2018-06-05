package org.directwebremoting.spring.namespace;

import java.util.List;

import org.directwebremoting.spring.SpringConfigurator;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.ManagedList;
import org.springframework.beans.factory.support.ManagedMap;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.NamespaceHandlerSupport;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.util.Assert;
import org.springframework.util.xml.DomUtils;
import org.w3c.dom.Element;

/**
 * Parses a <code>&lt;dwr:configuration&gt;</code> tag and all its children.
 *
 * @author Jose Noheda [jose.noheda@gmail.com]
 */
public final class ConfigurationParser implements BeanDefinitionParser
{

    /**
     * Provided bean name for the configuration tag.
     */
    public static final String DEFAULT_SPRING_CONFIGURATOR_ID = "__dwrConfiguration";

    private final NamespaceHandlerSupport handler;

    /**
     * Configures the namespace handler for this context.
     *
     * @param handler a non null instance
     */
    public ConfigurationParser(NamespaceHandlerSupport handler) {
        Assert.notNull(handler);
        this.handler = handler;
    }

    /**
     * Registers the configuration object and recursively processes children.
     */
    public BeanDefinition parse(Element configurationElement, ParserContext parserContext)
    {
        BeanDefinitionRegistry registry = parserContext.getRegistry();
        BeanDefinition configuration = registerConfigurationIfNecessary(registry);
        parseConfigurationChildren(configurationElement, configuration, parserContext);
        return configuration;
    }

    /**
     * Checks if a configuration object (usually from a <code>&lt;dwr:configuration&gt;</code> tag) has already
     * been processed. If not it registers one automatically.
     *
     * @param registry a non null instance
     * @return the configuration bean definition currently in the context (cannot be null)
     */
    public static BeanDefinition registerConfigurationIfNecessary(BeanDefinitionRegistry registry)
    {
        if (!registry.containsBeanDefinition(DEFAULT_SPRING_CONFIGURATOR_ID))
        {
            BeanDefinitionBuilder builder = BeanDefinitionBuilder.rootBeanDefinition(SpringConfigurator.class);
            builder.addPropertyValue("creators", new ManagedMap());
            builder.addPropertyValue("converters", new ManagedMap());
            builder.addPropertyValue("filters", new ManagedList());
            registry.registerBeanDefinition(DEFAULT_SPRING_CONFIGURATOR_ID, builder.getBeanDefinition());
        }
        return registry.getBeanDefinition(DEFAULT_SPRING_CONFIGURATOR_ID);
    }

    private void parseConfigurationChildren(Element configurationElement, BeanDefinition configuration, ParserContext parserContext)
    {
        parseInit(configurationElement, configuration, parserContext);
        parseFilters(configurationElement, configuration, parserContext);
        parseSignatures(configurationElement, configuration, parserContext);
        parseConverters(configurationElement, configuration, parserContext);
    }

    private void parseInit(Element configurationElement, BeanDefinition configuration, ParserContext parserContext)
    {
        Element initElement = DomUtils.getChildElementByTagName(configurationElement, "init");
        if (initElement != null)
        {
            handler.decorate(initElement, new BeanDefinitionHolder(configuration, DEFAULT_SPRING_CONFIGURATOR_ID), parserContext);
        }
    }

    @SuppressWarnings("unchecked")
    private void parseFilters(Element configurationElement, BeanDefinition configuration, ParserContext parserContext)
    {
        List<Element> filterElements = DomUtils.getChildElementsByTagName(configurationElement, "filter");
        for (Element filterElement : filterElements)
        {
            handler.decorate(filterElement, new BeanDefinitionHolder(configuration, DEFAULT_SPRING_CONFIGURATOR_ID), parserContext);
        }
    }

    @SuppressWarnings("unchecked")
    private void parseSignatures(Element configurationElement, BeanDefinition configuration, ParserContext parserContext)
    {
        List<Element> signatureElements = DomUtils.getChildElementsByTagName(configurationElement, "signatures");
        for (Element signatureElement : signatureElements)
        {
            handler.decorate(signatureElement, new BeanDefinitionHolder(configuration, DEFAULT_SPRING_CONFIGURATOR_ID), parserContext);
        }
    }

    @SuppressWarnings("unchecked")
    private void parseConverters(Element configurationElement, BeanDefinition configuration, ParserContext parserContext)
    {
        List<Element> convertElements = DomUtils.getChildElementsByTagName(configurationElement, "convert");
        for (Element convertElement : convertElements)
        {
            handler.decorate(convertElement, new BeanDefinitionHolder(configuration, DEFAULT_SPRING_CONFIGURATOR_ID), parserContext);
        }
    }

}
