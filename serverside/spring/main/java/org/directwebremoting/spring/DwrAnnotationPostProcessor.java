package org.directwebremoting.spring;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.directwebremoting.annotations.GlobalFilter;
import org.directwebremoting.annotations.Param;
import org.directwebremoting.annotations.RemoteProxy;
import org.directwebremoting.spring.namespace.ConfigurationParser;
import org.directwebremoting.spring.namespace.CreatorParserHelper;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.ManagedList;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

/**
 * @author Jose Noheda [jose.noheda@gmail.com]
 */
public class DwrAnnotationPostProcessor extends CreatorParserHelper implements BeanFactoryPostProcessor
{

    private static final Log log = LogFactory.getLog(DwrAnnotationPostProcessor.class);

    @SuppressWarnings("unchecked")
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException
    {
        BeanDefinitionRegistry beanDefinitionRegistry = (BeanDefinitionRegistry) beanFactory;
        for (String beanName : beanDefinitionRegistry.getBeanDefinitionNames())
        {
            BeanDefinition springConfigurator = ConfigurationParser.registerConfigurationIfNecessary(beanDefinitionRegistry);
            BeanDefinitionHolder beanDefinitionHolder = new BeanDefinitionHolder(beanDefinitionRegistry.getBeanDefinition(beanName), beanName);
            Class<?> beanDefinitionClass = getBeanDefinitionClass(beanDefinitionHolder, beanDefinitionRegistry);
            if (beanDefinitionClass != null)
            {
                RemoteProxy remoteProxy = beanDefinitionClass.getAnnotation(RemoteProxy.class);
                if (remoteProxy != null)
                {
                    String javascript = remoteProxy.name();
                    if (!StringUtils.hasText(javascript))
                    {
                        javascript = beanDefinitionClass.getSimpleName();
                    }
                    if (log.isInfoEnabled())
                    {
                        log.info("Detected candidate bean [" + beanName + "]. Remoting using " + javascript);
                    }
                    registerCreator(beanDefinitionHolder, beanDefinitionRegistry, beanDefinitionClass, javascript);
                }
                GlobalFilter globalFilter = beanDefinitionClass.getAnnotation(GlobalFilter.class);
                if (globalFilter != null)
                {
                    if (log.isInfoEnabled())
                    {
                        log.info("Detected global filter [" + beanDefinitionClass + "].");
                    }
                    ManagedList filters = (ManagedList) springConfigurator.getPropertyValues().getPropertyValue("filters").getValue();
                    Param[] params = globalFilter.params();
                    if (params != null)
                    {
                        for (Param param : params)
                        {
                            beanDefinitionHolder.getBeanDefinition().getPropertyValues().addPropertyValue(param.name(), param.value());
                        }
                    }
                    filters.add(new RuntimeBeanReference(beanName));
                }
            }
        }
    }

    protected Class<?> getBeanDefinitionClass(BeanDefinitionHolder beanDefinitionHolder, BeanDefinitionRegistry beanDefinitionRegistry)
    {
        try
        {
            String beanClassName = resolveBeanClassname(beanDefinitionHolder.getBeanDefinition(), beanDefinitionRegistry);
            return LocalClassUtils.forName(beanClassName, ClassUtils.getDefaultClassLoader());
        }
        catch (Exception cne)
        {
            if (log.isInfoEnabled())
            {
                log.info("Could not infer class for [" + beanDefinitionHolder.getBeanName() + "]. Is it a factory bean? Omitting bean from annotation processing");
            }
            return null;
        }
    }

}
