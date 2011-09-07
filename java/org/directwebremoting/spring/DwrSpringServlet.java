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
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.directwebremoting.WebContextFactory.WebContextBuilder;
import org.directwebremoting.impl.ContainerUtil;
import org.directwebremoting.impl.StartupUtil;
import org.directwebremoting.servlet.UrlProcessor;
import org.directwebremoting.util.Logger;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * The servlet that handles all calls to DWR. <br>
 * It retrieves its configuration from the Spring IoC container. This is done in two ways:
 * <ol>
 *   <li>Use the Spring namespace. When using the Spring namespace for DWR, the confgiuration for DWR is
 *       automatically picked up by this servlet.</li>
 *   <li>Explicitly specify which configurations to pick up. When explicitly defining the DWR configuration in
 *       Spring yourself, you can explicitely specify them in the init parameters.</li>
 * </ol>
 * Same as with the <code>DwrServlet</code>, you can specify a <code>debug</code> init parameter on this servlet
 * to put DWR in debug mode (allowing access to the very handy debug pages).
 *
 * @see org.directwebremoting.servlet.DwrServlet
 *
 * @author Bram Smeets
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class DwrSpringServlet extends HttpServlet
{
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
    public void init(ServletConfig servletConfig) throws ServletException
    {
        super.init(servletConfig);
        ServletContext servletContext = servletConfig.getServletContext();

        try
        {
            WebApplicationContext webappContext = WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext);

            container = new SpringContainer();
            container.setBeanFactory(webappContext);
            ContainerUtil.setupDefaults(container, servletConfig);
            ContainerUtil.setupFromServletConfig(container, servletConfig);

            container.setupFinished();

            webContextBuilder = StartupUtil.initWebContext(servletConfig, servletContext, container);
            StartupUtil.initServerContext(servletConfig, servletContext, container);

            ContainerUtil.prepareForWebContextFilter(servletContext, servletConfig, container, webContextBuilder, this);
            // retrieve the configurators from Spring (loaded by the ContextLoaderListener)
            try
            {
                configurators.add(webappContext.getBean(DwrNamespaceHandler.DEFAULT_SPRING_CONFIGURATOR_ID));
            }
            catch (NoSuchBeanDefinitionException ex)
            {
                throw new ServletException("No DWR configuration was found in your application context, make sure to define one", ex);
            }

            if (includeDefaultConfig)
            {
                ContainerUtil.configureFromSystemDwrXml(container);
            }

            ContainerUtil.configureFromInitParams(container, servletConfig);
            ContainerUtil.configure(container, configurators);

            ContainerUtil.publishContainer(container, servletConfig);
        }
        catch (InstantiationException ex)
        {
            throw new BeanCreationException("Failed to instansiate", ex);
        }
        catch (IllegalAccessException ex)
        {
            throw new BeanCreationException("Access error", ex);
        }
        catch (Exception ex)
        {
            log.fatal("init failed", ex);
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

            UrlProcessor processor = (UrlProcessor) container.getBean(UrlProcessor.class.getName());
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
    private List configurators = new ArrayList();

    /**
     * The log stream
     */
    private static final Logger log = Logger.getLogger(DwrSpringServlet.class);
}
