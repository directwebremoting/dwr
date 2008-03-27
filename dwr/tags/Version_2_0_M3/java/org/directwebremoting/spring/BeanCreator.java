/*
 * Copyright 2005 Joe Walker
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.directwebremoting.spring;

import org.directwebremoting.create.AbstractCreator;
import org.springframework.aop.framework.ProxyFactoryBean;

/**
 * A creator that proxies to the specified bean. <br>
 * Note that it can be configured with additional include rules,
 * exclude rules, filters and authentication rules using the
 * specified creator configuration.
 * @see CreatorConfig
 * @author Bram Smeets
 */
public class BeanCreator extends AbstractCreator
{
    /**
     * Accessor for the class that this creator allows access to. <br>
     * It returns the class specified by the <code>beanClass</code>
     * property. In case no class name has been set, it returns the
     * class of the specified bean.
     * @return the type of this allowed class
     */
    public Class getType()
    {
        if (beanClass != null)
        {
            return beanClass;
        }

        if (bean.getClass().equals(ProxyFactoryBean.class)) {
            ProxyFactoryBean proxy = (ProxyFactoryBean) getInstance();
            return proxy.getObjectType();
        }

        return bean.getClass();
    }

    /**
     * Accessor for the instance of this creator. <br>
     * It returns the specified bean property.
     * @return the bean instance of this creator
     */
    public Object getInstance()
    {
        return bean;
    }

    /**
     * Sets the bean for this bean creator.
     * @param bean the bean for this creator
     */
    public void setBean(Object bean)
    {
        this.bean = bean;
    }

    /**
     * Sets the bean class for this creator. <br>
     * Use this property to specify a different class or interface for
     * instance in case the specified bean is a proxy or implementation
     * and we want to expose the interface.
     * @param beanClass
     */
    public void setBeanClass(Class beanClass)
    {
        this.beanClass = beanClass;
    }

    /**
     * Sets the configuration for this creator. <br>
     * Use the configuration to specify include and exclude rules, filters
     * and/or authentication rules.
     * @see org.directwebremoting.spring.CreatorConfig
     * @param config the configuration for this creator
     */
    public void setConfig(CreatorConfig config)
    {
        this.config = config;
    }

    /**
     * Gets the configuration for this creator.
     * @return the configuration for this creator
     */
    public CreatorConfig getConfig()
    {
        return config;
    }

    /**
     * The bean for this creator.
     */
    private Object bean;

    /**
     * The optional bean class for this creator.
     */
    private Class beanClass;

    /**
     * The optional creator configuration for this creator.
     */
    private CreatorConfig config;
}
