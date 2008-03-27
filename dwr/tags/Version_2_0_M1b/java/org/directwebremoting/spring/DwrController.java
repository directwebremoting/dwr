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

import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.directwebremoting.DwrConstants;
import org.directwebremoting.WebContextBuilder;
import org.directwebremoting.WebContextFactory;
import org.directwebremoting.impl.ContainerUtil;
import org.directwebremoting.impl.DwrXmlConfigurator;
import org.directwebremoting.servlet.UrlProcessor;
import org.directwebremoting.util.FakeServletConfig;
import org.directwebremoting.util.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;


/**
 * A Spring Controller that handles DWR requests using a ContainerUtil
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class DwrController extends AbstractController implements BeanNameAware, InitializingBean, BeanFactoryAware, ServletContextAware
{
    /* (non-Javadoc)
     * @see org.springframework.beans.factory.BeanFactoryAware#setBeanFactory(org.springframework.beans.factory.BeanFactory)
     */
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException
    {
        try
        {
            container = new SpringContainer();
            container.setBeanFactory(beanFactory);

            Assert.notNull(servletConfig, "The servlet config has not been set on the controller"); //$NON-NLS-1$
            ContainerUtil.setupDefaults(container);
            ContainerUtil.setupFromServletConfig(container, servletConfig);
            container.configurationFinished();

            // Cached to save looking them up
            webContextBuilder = (WebContextBuilder) container.getBean(WebContextBuilder.class.getName());
            processor = (UrlProcessor) container.getBean(UrlProcessor.class.getName());

            // Now we have set the implementations we can set the WebContext up
            WebContextFactory.setWebContextBuilder(webContextBuilder);
        }
        catch (InstantiationException ex)
        {
            throw new BeanCreationException("Failed to instansiate", ex); //$NON-NLS-1$
        }
        catch (IllegalAccessException ex)
        {
            throw new BeanCreationException("Access error", ex); //$NON-NLS-1$
        }
    }

    /**
     * Setter for use by the Spring IoC container to tell us what Configurators
     * exist for us to configure ourselves.
     * @param configurators
     */
    public void setConfigurators(List configurators)
    {
        this.configurators = configurators;
    }

    /**
     * Do we prefix the list of Configurators with a default to read the system
     * dwr.xml file?
     * @param includeDefaultConfig the includeDefaultConfig to set
     */
    public void setIncludeDefaultConfig(boolean includeDefaultConfig)
    {
        this.includeDefaultConfig = includeDefaultConfig;
    }

    /* (non-Javadoc)
     * @see org.springframework.web.context.ServletContextAware#setServletContext(javax.servlet.ServletContext)
     */
    public void setServletContext(ServletContext servletContext)
    {
        this.servletContext = servletContext;
    }

    /* (non-Javadoc)
     * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
     */
    public void afterPropertiesSet() throws Exception
    {
        Assert.notNull(servletContext, "The servlet context has not been set on the controller"); //$NON-NLS-1$
        try
        {
            servletConfig = new FakeServletConfig(name, servletContext);
            webContextBuilder.set(null, null, servletConfig, servletContext, container);

            // The dwr.xml from within the JAR file.
            if (includeDefaultConfig)
            {
                DwrXmlConfigurator system = new DwrXmlConfigurator();
                system.setClassResourceName(DwrConstants.FILE_DWR_XML);
                system.configure(container);
            }

            ContainerUtil.configure(container, configurators);
        }
        catch (Exception ex)
        {
            log.fatal("init failed", ex); //$NON-NLS-1$
            throw ex;
        }
        finally
        {
            webContextBuilder.unset();
        }
    }

    /* (non-Javadoc)
     * @see org.springframework.web.servlet.mvc.AbstractController#handleRequestInternal(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        try
        {
            webContextBuilder.set(request, response, servletConfig, servletContext, container);
            processor.handle(request, response);
        }
        finally
        {
            webContextBuilder.unset();
        }

        return null;
    }

    /* (non-Javadoc)
     * @see org.springframework.beans.factory.BeanNameAware#setBeanName(java.lang.String)
     */
    public void setBeanName(final String name)
    {
        this.name = name;
    }

    /**
     * How is this deployed in Spring
     */
    private String name;

    /**
     * The processor will actually handle the http requests
     */
    protected UrlProcessor processor;

    /**
     * The WebContext that keeps http objects local to a thread
     */
    protected WebContextBuilder webContextBuilder;

    /**
     * DWRs IoC container (that passes stuff to Spring in this case)
     */
    private SpringContainer container;

    /**
     * The servlet context passed to us by Spring
     */
    private ServletContext servletContext;

    /**
     * The fake ServletConfig
     */
    private ServletConfig servletConfig;

    /**
     * Do we prefix the list of Configurators with a default to read the system
     * dwr.xml file?
     */
    private boolean includeDefaultConfig = true;

    /**
     * What Configurators exist for us to configure ourselves.
     */
    private List configurators;

    /**
     * The log stream
     */
    private static final Logger log = Logger.getLogger(DwrController.class);
}
