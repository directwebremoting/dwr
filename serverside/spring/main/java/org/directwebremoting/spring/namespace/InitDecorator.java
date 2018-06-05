package org.directwebremoting.spring.namespace;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.xml.BeanDefinitionDecorator;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Handler for <code>&lt;dwr:init&gt;</code> tags inside <code>&lt;dwr:configuration&gt;</code> tags.
 *
 * @author Jose Noheda [jose.noheda@gmail.com]
 */
public class InitDecorator implements BeanDefinitionDecorator
{

    /**
     * Parses the <code>&lt;dwr:init&gt;</code> tag checking for nested creator and converter definitions.
     */
    public BeanDefinitionHolder decorate(Node initElement, BeanDefinitionHolder configuration, ParserContext parserContext)
    {
        NodeList inits = initElement.getChildNodes();
        Map<String, String> converters = new HashMap<String, String>();
        for (int index = 0; index < inits.getLength(); index++)
        {
            Node node = inits.item(index);
            if (node instanceof Element)
            {
                Element child = (Element) node;
                if (ELEMENT_CONVERTER.equals(node.getNodeName()))
                {
                    converters.put(child.getAttribute(ATTRIBUTE_ID), child.getAttribute(ATTRIBUTE_CLASS));
                }
            }
        }

        configuration.getBeanDefinition().getPropertyValues().addPropertyValue("converterTypes", converters);

        return configuration;
    }

    private static final String ATTRIBUTE_ID = "id";
    private static final String ATTRIBUTE_CLASS = "class";
    private static final String ELEMENT_CONVERTER = "dwr:converter";

}
