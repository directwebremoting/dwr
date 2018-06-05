package org.directwebremoting.spring.namespace;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.directwebremoting.spring.DwrController;
import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.ManagedList;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.util.StringUtils;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Handles <code>&lt;dwr:controller&gt;</code> tag.
 *
 * @author Jose Noheda [jose.noheda@gmail.com]
 */
public class ControllerParser extends AbstractSingleBeanDefinitionParser
{

    @Override
    protected Class<?> getBeanClass(Element element)
    {
        return DwrController.class;
    }

    @Override
    protected String resolveId(Element element, AbstractBeanDefinition definition, ParserContext parserContext) throws BeanDefinitionStoreException
    {
        String id = super.resolveId(element, definition, parserContext);
        return StringUtils.hasText(id) ? id : "dwrController";
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void doParse(Element element, BeanDefinitionBuilder dwrController)
    {
        List<Object> configurators = new ManagedList();
        configurators.add(new RuntimeBeanReference(ConfigurationParser.DEFAULT_SPRING_CONFIGURATOR_ID));
        dwrController.addPropertyValue("configurators", configurators);

        String debug = element.getAttribute("debug");
        if (StringUtils.hasText(debug))
        {
            dwrController.addPropertyValue("debug", debug);
        }

        parseControllerParameters(dwrController, element);

    }

    private void parseControllerParameters(BeanDefinitionBuilder dwrController, Element element)
    {
        NodeList children = element.getChildNodes();
        Map<String, String> params = new HashMap<String, String>();
        for (int i = 0; i < children.getLength(); i++)
        {
            Node node = children.item(i);
            if (node instanceof Element)
            {
                Element child = (Element) node;
                if ("dwr:config-param".equals(child.getNodeName()))
                {
                    params.put(child.getAttribute("name"), child.getAttribute("value"));
                }
            }
        }
        dwrController.addPropertyValue("configParams", params);
    }

}
