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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.directwebremoting.Creator;
import org.directwebremoting.WebContextFactory;
import org.directwebremoting.create.AbstractCreator;
import org.directwebremoting.util.LocalUtil;
import org.directwebremoting.util.Logger;
import org.directwebremoting.util.Messages;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.servlet.support.RequestContextUtils;

/**
 * A creator that relies on a spring bean factory.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class SpringCreator extends AbstractCreator implements Creator
{
    /**
     * @return Returns the beanName.
     */
    public String getBeanName()
    {
        return beanName;
    }

    /**
     * @param beanName The beanName to set.
     */
    public void setBeanName(String beanName)
    {
        this.beanName = beanName;
    }

    /**
     * What sort of class do we create?
     * @param classname The name of the class
     */
    public void setClass(String classname)
    {
        try
        {
            this.clazz = LocalUtil.classForName(classname);
        }
        catch (ClassNotFoundException ex)
        {
            throw new IllegalArgumentException(Messages.getString("Creator.ClassNotFound", classname)); //$NON-NLS-1$
        }
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.create.Creator#init(org.w3c.dom.Element)
     */
    public void setProperties(Map params) throws IllegalArgumentException
    {
        List locValues = new ArrayList();

        for (Iterator it = params.keySet().iterator(); it.hasNext();)
        {
            String key = (String) it.next();
            String value = (String) params.get(key);
            if (key.startsWith("location")) //$NON-NLS-1$
            {
                log.debug("Adding configLocation: " + value + " from parameter: " + key); //$NON-NLS-1$ //$NON-NLS-2$
                locValues.add(value);
            }
        }

        configLocation = (String[]) locValues.toArray(new String[locValues.size()]);
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.Creator#getType()
     */
    public Class getType()
    {
        if (clazz == null)
        {
            try
            {
                clazz = getInstance().getClass();
            }
            catch (InstantiationException ex)
            {
                log.error("Failed to instansiate object to detect type.", ex); //$NON-NLS-1$
                return Object.class;
            }
        }

        return clazz;
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.Creator#getInstance()
     */
    public Object getInstance() throws InstantiationException
    {
        try
        {
            if (overrideFactory != null)
            {
                return overrideFactory.getBean(beanName);
            }

            if (factory == null)
            {
                factory = getBeanFactory();
            }

            if (factory == null)
            {
                log.error("DWR can't find a spring config. See following info logs for solutions"); //$NON-NLS-1$
                log.info("- Option 1. In dwr.xml, <create creator='spring' ...> add <param name='location1' value='beans.xml'/> for each spring config file."); //$NON-NLS-1$
                log.info("- Option 2. Use a spring org.springframework.web.context.ContextLoaderListener."); //$NON-NLS-1$
                log.info("- Option 3. Call SpringCreator.setOverrideBeanFactory() from your web-app"); //$NON-NLS-1$
                throw new InstantiationException(Messages.getString("SpringCreator.MissingConfig")); //$NON-NLS-1$
            }

            return factory.getBean(beanName);
        }
        catch (RuntimeException ex)
        {
            throw ex;
        }
        catch (Exception ex)
        {
            log.error("Error", ex); //$NON-NLS-1$
            throw new InstantiationException(ex.toString());
        }
    }

    /**
     * @return A found BeanFactory configuration
     */
    private BeanFactory getBeanFactory()
    {
        // If someone has set a resource name then we need to load that.
        if (configLocation != null && configLocation.length > 0)
        {
            log.info("Spring BeanFactory via ClassPathXmlApplicationContext using " + configLocation.length + "configLocations."); //$NON-NLS-1$ //$NON-NLS-2$
            return new ClassPathXmlApplicationContext(configLocation);
        }

        ServletContext srvCtx = WebContextFactory.get().getServletContext();
        HttpServletRequest request = WebContextFactory.get().getHttpServletRequest();

        if (request != null)
        {
            return RequestContextUtils.getWebApplicationContext(request, srvCtx);
        }
        else
        {
            return WebApplicationContextUtils.getWebApplicationContext(srvCtx);
        }
    }

    /**
     * Set a web-app wide BeanFactory.
     * This method is misnamed (The parameter is a BeanFactory and not a
     * XmlBeanFactory)
     * @param factory The factory to set.
     * @deprecated This method is misnamed use setOverrideBeanFactory
     */
    public static void setXmlBeanFactory(BeanFactory factory)
    {
        SpringCreator.overrideFactory = factory;
    }

    /**
     * Set a web-app wide BeanFactory.
     * @param factory The factory to set.
     */
    public static void setOverrideBeanFactory(BeanFactory factory)
    {
        SpringCreator.overrideFactory = factory;
    }

    /**
     * The name of the spring bean we want to create
     */
    private String beanName = null;

    /**
     * The log stream
     */
    private static final Logger log = Logger.getLogger(SpringCreator.class);

    /**
     * The Spring beans factory from which we get our beans.
     */
    private BeanFactory factory = null;

    /**
     * An web-app wide BeanFactory that we can use to override any calculated
     * ones.
     */
    private static BeanFactory overrideFactory = null;

    /**
     * The cached type of bean that we are creating
     */
    private Class clazz = null;

    /**
     * An array of locations to search through for a beans.xml file
     */
    private String[] configLocation = null;
}
