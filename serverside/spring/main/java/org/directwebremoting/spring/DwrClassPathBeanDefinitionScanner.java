package org.directwebremoting.spring;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.directwebremoting.annotations.DataTransferObject;
import org.directwebremoting.annotations.GlobalFilter;
import org.directwebremoting.annotations.Param;
import org.directwebremoting.annotations.RemoteProperty;
import org.directwebremoting.annotations.RemoteProxy;
import org.directwebremoting.spring.namespace.ConfigurationParser;
import org.directwebremoting.spring.namespace.ConverterParserHelper;
import org.directwebremoting.spring.namespace.CreatorParserHelper;
import org.directwebremoting.util.LocalUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.ManagedList;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

/**
 * @author Jose Noheda [jose.noheda@gmail.com]
 */
public class DwrClassPathBeanDefinitionScanner extends ClassPathBeanDefinitionScanner
{

    private static final Log log = LogFactory.getLog(DwrClassPathBeanDefinitionScanner.class);

    public DwrClassPathBeanDefinitionScanner(BeanDefinitionRegistry registry)
    {
        super(registry, false);
        addExcludeFilter(new AnnotationTypeFilter(Component.class));
        addExcludeFilter(new AnnotationTypeFilter(Service.class));
        addExcludeFilter(new AnnotationTypeFilter(Repository.class));
        addExcludeFilter(new AnnotationTypeFilter(Controller.class));
        setScopedProxyMode(ScopedProxyMode.INTERFACES);
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void registerBeanDefinition(BeanDefinitionHolder definitionHolder, BeanDefinitionRegistry registry)
    {
        try
        {
            Class<?> beanDefinitionClass = LocalClassUtils.forName(definitionHolder.getBeanDefinition().getBeanClassName(), ClassUtils.getDefaultClassLoader());
            RemoteProxy proxy = beanDefinitionClass.getAnnotation(RemoteProxy.class);
            DataTransferObject converter = beanDefinitionClass.getAnnotation(DataTransferObject.class);
            GlobalFilter globalFilter = beanDefinitionClass.getAnnotation(GlobalFilter.class);
            if (proxy != null)
            {
                super.registerBeanDefinition(definitionHolder, registry);
                String javascript = proxy.name();
                if (!StringUtils.hasText(javascript))
                {
                    javascript = beanDefinitionClass.getSimpleName();
                }
                if (log.isInfoEnabled())
                {
                    log.info("Dwr classpath scanning detected candidate bean [" + definitionHolder.getBeanName() + "]. Remoting using " + javascript);
                }
                CreatorParserHelper.registerCreator(definitionHolder, registry, beanDefinitionClass, javascript);
            }
            else if (converter != null)
            {
                if (log.isInfoEnabled())
                {
                    log.info("Dwr classpath scanning detected candidate DTO [" + beanDefinitionClass.getName() + "] processed by converter type [" + converter.type() + "]");
                }
                ConverterConfig converterConfig = new ConverterConfig();
                converterConfig.setType(converter.type());
                setIncludes(beanDefinitionClass, converterConfig);
                String javascript = converter.javascript();
                if (StringUtils.hasText(javascript))
                {
                    converterConfig.setJavascriptClassName(javascript);
                }
                Param[] params = converter.params();
                if ((params != null) && (params.length > 0))
                {
                    Map<String, String> parameters = new HashMap<String, String>();
                    for (Param param : params)
                    {
                        parameters.put(param.name(), param.value());
                    }
                    converterConfig.setParams(parameters);
                }
                ConverterParserHelper.lookupConverters(registry).put(beanDefinitionClass.getName(), converterConfig);
            }
            else if (globalFilter != null)
            {
                if (log.isInfoEnabled())
                {
                    log.info("Dwr classpath scanning detected candidate global filter [" + beanDefinitionClass + "]");
                }
                BeanDefinition springConfigurator = ConfigurationParser.registerConfigurationIfNecessary(registry);
                ManagedList filters = (ManagedList) springConfigurator.getPropertyValues().getPropertyValue("filters").getValue();
                Param[] params = globalFilter.params();
                if (params != null)
                {
                    for (Param param : params)
                    {
                        definitionHolder.getBeanDefinition().getPropertyValues().addPropertyValue(param.name(), param.value());
                    }
                }
                super.registerBeanDefinition(definitionHolder, registry);
                filters.add(new RuntimeBeanReference(definitionHolder.getBeanName()));
            }
        }
        catch (Exception ex)
        {
            if (log.isWarnEnabled())
            {
                log.warn("Dwr classpath scanning detected candidate bean [" + definitionHolder.getBeanName() + "] but could not create needed proxies", ex);
            }
        }
    }

    protected void setIncludes(Class<?> beanDefinitionClass, ConverterConfig converterConfig)
    {
        PropertyDescriptor[] properties = BeanUtils.getPropertyDescriptors(beanDefinitionClass);
        if (properties != null)
        {
            for (PropertyDescriptor p : properties)
            {
                Method getter = p.getReadMethod();
                if (getter != null)
                {
                    if (getter.getAnnotation(RemoteProperty.class) != null)
                    {
                        converterConfig.addInclude(p.getName());
                    }
                }
            }
        }
        Field[] fields = LocalUtil.getAllFields(beanDefinitionClass);
        if (fields != null)
        {
            for (Field field : fields)
            {
                if (field.getAnnotation(RemoteProperty.class) != null)
                {
                    converterConfig.addInclude(field.getName());
                }
            }
        }
    }

}
