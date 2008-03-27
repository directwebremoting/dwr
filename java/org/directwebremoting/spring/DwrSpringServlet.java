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

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.directwebremoting.DwrConstants;
import org.directwebremoting.WebContextBuilder;
import org.directwebremoting.WebContextFactory;
import org.directwebremoting.impl.ContainerUtil;
import org.directwebremoting.impl.DwrXmlConfigurator;
import org.directwebremoting.servlet.UrlProcessor;
import org.directwebremoting.util.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;


/**
 * The servlet that handles all calls to DWR.<br>
 * It retrieves its configuration from the Spring IoC container.
 * @author Bram Smeets
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class DwrSpringServlet extends HttpServlet implements BeanFactoryAware
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

            ContainerUtil.setupDefaults(container);
            ContainerUtil.setupFromServletConfig(container, getServletConfig());
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
     * @see javax.servlet.GenericServlet#init(javax.servlet.ServletConfig)
     */
    public void init(ServletConfig config) throws ServletException
    {
        super.init(config);

        try
        {
            webContextBuilder.set(null, null, getServletConfig(), getServletContext(), container);

            // Load the dwr.xml from within the JAR file.
            if (includeDefaultConfig)
            {
                DwrXmlConfigurator system = new DwrXmlConfigurator();
                system.setClassResourceName(DwrConstants.FILE_DWR_XML);
                system.configure(container);
            }

            ContainerUtil.configureUsingInitParams(container, config);

            ContainerUtil.configure(container, configurators);
        }
        catch (Exception ex)
        {
            log.fatal("init failed", ex); //$NON-NLS-1$
            throw new ServletException(ex);
        }
        finally
        {
            webContextBuilder.unset();
        }
    }

    /* (non-Javadoc)
     * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException
    {
        doPost(req, resp);
    }

    /* (non-Javadoc)
     * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
    {
        try
        {
            webContextBuilder.set(request, response, getServletConfig(), getServletContext(), container);
            processor.handle(request, response);
        }
        finally
        {
            webContextBuilder.unset();
        }
    }

    /**
     * DWRs IoC container (that passes stuff to Spring in this case)
     */
    private SpringContainer container;

    /**
     * The processor will actually handle the http requests
     */
    protected UrlProcessor processor;

    /**
     * The WebContext that keeps http objects local to a thread
     */
    protected WebContextBuilder webContextBuilder;

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
    private static final Logger log = Logger.getLogger(DwrSpringServlet.class);
}
