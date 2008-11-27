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

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.directwebremoting.annotations.RemoteMethod;
import org.directwebremoting.annotations.RemoteProxy;
import org.springframework.beans.BeansException;
import org.springframework.beans.FatalBeanException;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

/**
 * @author Jose Noheda [jose.noheda@gmail.com]
 */
public class DwrAnnotationPostProcessor implements BeanFactoryPostProcessor
{

    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException
    {
        BeanDefinitionRegistry beanDefinitionRegistry = (BeanDefinitionRegistry) beanFactory;
        for (String beanName : beanDefinitionRegistry.getBeanDefinitionNames())
        {
            BeanDefinitionHolder beanDefinitionHolder = new BeanDefinitionHolder(beanDefinitionRegistry.getBeanDefinition(beanName), beanName);
            Class<?> beanDefinitionClass = getBeanDefinitionClass(beanDefinitionHolder, beanDefinitionRegistry);
            if (beanDefinitionClass != null)
            {
                RemoteProxy annotation = beanDefinitionClass.getAnnotation(RemoteProxy.class);
                if (annotation != null)
                {
                    String javascript = annotation.name();
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
            }
        }
    }

    protected Class<?> getBeanDefinitionClass(BeanDefinitionHolder beanDefinitionHolder, BeanDefinitionRegistry beanDefinitionRegistry)
    {
        try
        {
            String beanClassName = DwrNamespaceHandler.resolveBeanClassname(beanDefinitionHolder.getBeanDefinition(), beanDefinitionRegistry);
            return ClassUtils.forName(beanClassName);
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

    protected static void registerCreator(BeanDefinitionHolder beanDefinitionHolder, BeanDefinitionRegistry beanDefinitionRegistry, Class<?> beanDefinitionClass, String javascript)
    {
        String creatorConfigName = "__" + javascript;
        if (beanDefinitionRegistry.containsBeanDefinition(creatorConfigName))
        {
            log.info("[" + javascript + "] remote bean definition already detected. Mixed use of <dwr:annotation-config /> and <dwr:annotation-scan />? Re-scanned package?");
        }
        else
        {
            BeanDefinitionBuilder beanCreator = BeanDefinitionBuilder.rootBeanDefinition(BeanCreator.class);
            try {
                beanCreator.addPropertyValue("beanClass", beanDefinitionClass);
                String name = beanDefinitionHolder.getBeanName();
                if (name.startsWith("scopedTarget."))
                {
                    name = name.substring(name.indexOf(".") + 1);
                }
                beanCreator.addPropertyValue("beanId", name);
                beanCreator.addDependsOn(name);
                beanCreator.addPropertyValue("javascript", javascript);
                BeanDefinitionBuilder creatorConfig = BeanDefinitionBuilder.rootBeanDefinition(CreatorConfig.class);
                creatorConfig.addPropertyValue("creator", beanCreator.getBeanDefinition());
                List<String> includes = new ArrayList<String>();
                for (Method method : beanDefinitionClass.getMethods()) {
                    if (method.getAnnotation(RemoteMethod.class) != null)
                    {
                        includes.add(method.getName());
                    }
                }
                creatorConfig.addPropertyValue("includes", includes);
                BeanDefinitionHolder aux = new BeanDefinitionHolder(creatorConfig.getBeanDefinition(), creatorConfigName);
                BeanDefinitionReaderUtils.registerBeanDefinition(aux, beanDefinitionRegistry);
                DwrNamespaceHandler.lookupCreators(beanDefinitionRegistry).put(javascript, new RuntimeBeanReference(creatorConfigName));
            } catch (Exception ex) {
                throw new FatalBeanException("Unable to create DWR bean creator for '" + beanDefinitionHolder.getBeanName() + "'. ", ex);
            }
        }
    }

    /**
     * The log stream
     */
    private static final Log log = LogFactory.getLog(DwrAnnotationPostProcessor.class);

}
