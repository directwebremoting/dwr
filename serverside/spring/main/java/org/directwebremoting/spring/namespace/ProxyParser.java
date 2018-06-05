package org.directwebremoting.spring.namespace;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.directwebremoting.spring.BeanCreator;
import org.directwebremoting.spring.CreatorConfig;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.util.StringUtils;
import org.w3c.dom.Element;

/**
 * Creates a proxy for a bean usually defined in another Spring context and referenced
 * using <code>&lt;dwr:proxy-ref /&gt;</code>
 *
 * @author Jose Noheda [jose.noheda@gmail.com]
 */
public class ProxyParser extends CreatorParserHelper implements BeanDefinitionParser
{

    private static final Log log = LogFactory.getLog(ProxyParser.class);

    public BeanDefinition parse(Element proxyElement, ParserContext parserContext)
    {
        String beanRef = proxyElement.getAttribute("bean");
        BeanDefinitionRegistry registry = parserContext.getRegistry();
        BeanDefinition beanRefDefinition = findParentDefinition(beanRef, registry);
        String javascript = proxyElement.getAttribute("javascript");
        if (!StringUtils.hasText(javascript))
        {
            javascript = StringUtils.capitalize(beanRef);
            if (log.isDebugEnabled())
            {
                log.debug("No javascript name provided. Remoting using bean id [" + javascript + "]");
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
        configureCreator(registry, javascript, creatorConfig, proxyElement.getChildNodes());
        registerCreator(registry, creatorConfig, javascript);
        return creatorConfig.getBeanDefinition();
    }

}
