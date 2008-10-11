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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.directwebremoting.annotations.RemoteProxy;
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
        addIncludeFilter(new AnnotationTypeFilter(RemoteProxy.class));
        addExcludeFilter(new AnnotationTypeFilter(Component.class));
        addExcludeFilter(new AnnotationTypeFilter(Service.class));
        addExcludeFilter(new AnnotationTypeFilter(Repository.class));
        addExcludeFilter(new AnnotationTypeFilter(Controller.class));
        setScopedProxyMode(ScopedProxyMode.INTERFACES);
    }

    /* (non-Javadoc)
     * @see org.springframework.context.annotation.ClassPathBeanDefinitionScanner#registerBeanDefinition(org.springframework.beans.factory.config.BeanDefinitionHolder, org.springframework.beans.factory.support.BeanDefinitionRegistry)
     */
    @Override
    protected void registerBeanDefinition(BeanDefinitionHolder definitionHolder, BeanDefinitionRegistry registry)
    {
        try
        {
            Class<?> beanDefinitionClass = ClassUtils.forName(definitionHolder.getBeanDefinition().getBeanClassName());
            RemoteProxy annotation = beanDefinitionClass.getAnnotation(RemoteProxy.class);
            if (annotation != null)
            {
                super.registerBeanDefinition(definitionHolder, registry);
                String javascript = annotation.name();
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
    protected static final Log log = LogFactory.getLog(DwrClassPathBeanDefinitionScanner.class);
}
