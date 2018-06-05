package org.directwebremoting.spring.namespace;

import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.xml.BeanDefinitionDecorator;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * Handles <code>&lt;dwr:filter/&gt;</code> nested tags.
 *
 * @author Jose Noheda [jose.noheda@gmail.com]
 */
public class FilterDecorator extends FilterParserHelper implements BeanDefinitionDecorator
{

    public BeanDefinitionHolder decorate(Node filterElement, BeanDefinitionHolder parent, ParserContext parserContext)
    {
        String name = parent.getBeanName();
        Element element = (Element) filterElement;
        BeanDefinitionRegistry registry = parserContext.getRegistry();
        processFilter(registry, element, name);
        addGlobalFilter(registry, element, name);
        return parent;
    }

}
