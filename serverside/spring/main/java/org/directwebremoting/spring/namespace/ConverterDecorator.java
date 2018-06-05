package org.directwebremoting.spring.namespace;

import org.directwebremoting.spring.ConverterConfig;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.xml.BeanDefinitionDecorator;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * Handles <code>&lt;convert&gt;</code> tag.
 *
 * @author Jose Noheda [jose.noheda@gmail.com]
 */
public class ConverterDecorator extends ConverterParserHelper implements BeanDefinitionDecorator
{

    public BeanDefinitionHolder decorate(Node node, BeanDefinitionHolder parent, ParserContext parserContext)
    {
        Element element = (Element) node;
        BeanDefinitionRegistry registry = parserContext.getRegistry();

        String type = element.getAttribute("type");
        if ("preconfigured".equals(type))
        {
            type += ":" + element.getAttribute("ref");
        }

        ConverterConfig converterConfig = new ConverterConfig();
        converterConfig.setType(type);
        converterConfig.setJavascriptClassName(element.getAttribute("javascript"));

        String forceAsString = element.getAttribute("force");
        if (forceAsString != null)
        {
            boolean force = Boolean.parseBoolean(forceAsString);
            converterConfig.setForce(force);
        }

        parseConverterSettings(converterConfig, element);
        lookupConverters(registry).put(element.getAttribute("class"), converterConfig);

        return parent;
    }

}
