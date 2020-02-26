package org.directwebremoting.spring.namespace;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.directwebremoting.spring.BeanCreator;
import org.directwebremoting.spring.CreatorConfig;
import org.directwebremoting.spring.LocalClassUtils;
import org.springframework.beans.FatalBeanException;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.xml.BeanDefinitionDecorator;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * @author Jose Noheda [jose.noheda@gmail.com]
 */
public class RemoteDecorator extends CreatorParserHelper implements BeanDefinitionDecorator
{

    private static final Log log = LogFactory.getLog(RemoteDecorator.class);

    public BeanDefinitionHolder decorate(Node remoteElement, BeanDefinitionHolder bean, ParserContext parserContext)
    {
        String name = bean.getBeanName();
        Element element = (Element) remoteElement;
        String javascript = element.getAttribute("javascript");
        BeanDefinitionBuilder beanCreator = BeanDefinitionBuilder.rootBeanDefinition(BeanCreator.class);

        try
        {
            String beanClassName = resolveBeanClassname(bean.getBeanDefinition(), parserContext.getRegistry());
            if (beanClassName == null)
            {
                throw new FatalBeanException("Unabled to find type for beanName '" + name + "'. " + "Check your bean has a correctly configured parent or provide a class for " + " the bean definition");
            }
            beanCreator.addPropertyValue("beanClass", LocalClassUtils.forName(beanClassName, ClassUtils.getDefaultClassLoader()));
        }
        catch (ClassNotFoundException e)
        {
            throw new FatalBeanException("Unable to create DWR bean creator for '" + name + "'.", e);
        }


        if (name.startsWith("scopedTarget."))
        {
            name = name.substring(name.indexOf(".") + 1);
        }
        if (!StringUtils.hasText(javascript))
        {
            javascript = StringUtils.capitalize(name);
            if (log.isDebugEnabled())
            {
                log.debug("No javascript name provided. Remoting using bean id [" + javascript + "]");
            }
        }
        beanCreator.addPropertyValue("beanId", name);
        beanCreator.addPropertyValue("javascript", javascript);

        BeanDefinitionRegistry registry = parserContext.getRegistry();
        BeanDefinitionBuilder creatorConfig = BeanDefinitionBuilder.rootBeanDefinition(CreatorConfig.class);
        creatorConfig.addPropertyValue("creator", beanCreator.getBeanDefinition());
        configureCreator(registry, javascript, creatorConfig, remoteElement.getChildNodes());
        registerCreator(registry, creatorConfig, javascript);

        return bean;
    }

}
