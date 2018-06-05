package org.directwebremoting.spring;

import org.directwebremoting.extend.AbstractCreator;
import org.springframework.beans.BeansException;
import org.springframework.beans.FatalBeanException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

/**
 * A creator that proxies to the specified bean. <br>
 * Note that it can be configured with additional include rules,
 * exclude rules, filters and authentication rules using the
 * specified creator configuration.
 * @see CreatorConfig
 * @author Bram Smeets
 */
public class BeanCreator extends AbstractCreator implements BeanFactoryAware, InitializingBean
{
    /**
     * Is called by the Spring container after all properties have been set. <br>
     * It is implemented in order to initialize the beanClass field correctly and to make sure
     * that either the bean id or the bean itself have been set on this creator.
     * @see org.springframework.beans.factory.InitializingBean
     */
    public void afterPropertiesSet()
    {
        // make sure that either the bean or the beanId have been set correctly
        if (bean != null)
        {
            this.beanClass = bean.getClass();
        }
        else if (beanId != null)
        {
            this.beanClass = beanFactory.getType(beanId);
        }
        else
        {
            throw new FatalBeanException("You should either set the bean property directly or set the beanId property");
        }

        // make sure to handle cglib proxies correctly
        if (ClassUtils.isCglibProxyClass(this.beanClass))
        {
            this.beanClass = this.beanClass.getSuperclass();
        }
    }

    /**
     * Accessor for the class that this creator allows access to. <br>
     * It returns the class specified by the <code>beanClass</code>
     * property. In case no class name has been set, it returns the
     * class of the specified bean.
     * @return the type of this allowed class
     */
    public Class<?> getType()
    {
        return beanClass;
    }

    /**
     * Accessor for the instance of this creator. <br>
     * It returns the specified bean property.
     * @return the bean instance of this creator
     */
    public Object getInstance()
    {
        synchronized (monitor)
        {
            if (bean == null)
            {
                Assert.notNull(beanId, "The bean id needs to be specified");
                bean = beanFactory.getBean(beanId);
            }
        }

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
     * @param beanClass the class of the bean to remote
     */
    public void setBeanClass(Class<?> beanClass)
    {
        this.beanClass = beanClass;
    }


    /**
     * Sets the id of the bean to remote using DWR. <br>
     * Either set this property on the creator, or set the bean to be
     * remoted directly on this creator.
     * @param beanId the id of the bean to remote
     */
    public void setBeanId(String beanId)
    {
        this.beanId = beanId;
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
     * Sets the bean factory that contains this BeanCreator.
     * @param beanFactory the beanFactory that created this BeanCreator
     * @see org.springframework.beans.factory.BeanFactoryAware#setBeanFactory(org.springframework.beans.factory.BeanFactory)
     */
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException
    {
        this.beanFactory = beanFactory;
        
    }
    
    /**
     * The bean for this creator.
     */
    private Object bean;

    /**
     * The optional bean class for this creator.
     */
    private Class<?> beanClass;

    /**
     * The optional bean name.
     */
    private String beanId;

    /**
     * The beanFactory context that creates this creator.
     */
    private BeanFactory beanFactory;
    
    /**
     * The optional creator configuration for this creator.
     */
    private CreatorConfig config;

    /** Monitor object to synchronize on during inititalization. */
    private final Object monitor = new Object();

}
