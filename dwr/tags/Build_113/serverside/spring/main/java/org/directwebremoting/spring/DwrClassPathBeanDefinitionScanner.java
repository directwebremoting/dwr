/*
 * Copyright 2008 the original author or authors.
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

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.directwebremoting.annotations.DataTransferObject;
import org.directwebremoting.annotations.Param;
import org.directwebremoting.annotations.RemoteProperty;
import org.directwebremoting.annotations.RemoteProxy;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
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
    protected void registerBeanDefinition(BeanDefinitionHolder definitionHolder, BeanDefinitionRegistry registry)
    {
        try
        {
            Class<?> beanDefinitionClass = ClassUtils.forName(definitionHolder.getBeanDefinition().getBeanClassName());
            RemoteProxy proxy = beanDefinitionClass.getAnnotation(RemoteProxy.class);
            DataTransferObject converter = beanDefinitionClass.getAnnotation(DataTransferObject.class);
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
                DwrAnnotationPostProcessor.registerCreator(definitionHolder, registry, beanDefinitionClass, javascript);
            }
            else if (converter != null)
            {
                if (log.isInfoEnabled())
                {
                    log.info("Dwr classpath scanning detected candidate DTO [" + beanDefinitionClass.getName() + "] processed by converter type [" + converter.type() + "]");
                }
                ConverterConfig converterConfig = new ConverterConfig();
                converterConfig.setType(converter.type());
                PropertyDescriptor[] properties = BeanUtils.getPropertyDescriptors(beanDefinitionClass);
                if ((properties != null) && (properties.length > 0))
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
                DwrNamespaceHandler.lookupConverters(registry).put(beanDefinitionClass.getName(), converterConfig);
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

    /**
     * The log stream
     */
    private static final Log log = LogFactory.getLog(DwrClassPathBeanDefinitionScanner.class);
}
