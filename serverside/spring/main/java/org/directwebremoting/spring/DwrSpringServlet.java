package org.directwebremoting.spring;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.directwebremoting.Container;
import org.directwebremoting.WebContextFactory.WebContextBuilder;
import org.directwebremoting.extend.Configurator;
import org.directwebremoting.impl.StartupUtil;
import org.directwebremoting.servlet.DwrServlet;
import org.directwebremoting.spring.namespace.ConfigurationParser;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * The servlet that handles all calls to DWR. <br>
 * It retrieves its configuration from the Spring IoC container. This is done in two ways:
 * <ol>
 *   <li>Use the Spring namespace. When using the Spring namespace for DWR, the configuration for DWR is
 *       automatically picked up by this servlet.</li>
 *   <li>Explicitly specify which configurations to pick up. When explicitly defining the DWR configuration in
 *       Spring yourself, you can explicitly specify them in the init parameters.</li>
 * </ol>
 * Same as with the <code>DwrServlet</code>, you can specify a <code>debug</code> init parameter on this servlet
 * to put DWR in debug mode (allowing access to the very handy debug pages).
 *
 * @see org.directwebremoting.servlet.DwrServlet
 *
 * @author Bram Smeets
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class DwrSpringServlet extends DwrServlet
{
    /* (non-Javadoc)
     * @see org.directwebremoting.servlet.DwrServlet#createContainer(javax.servlet.ServletConfig)
     */
    @Override
    protected SpringContainer createContainer(ServletConfig servletConfig)
    {
        ApplicationContext appContext = getApplicationContext(servletConfig.getServletContext());

        SpringContainer springContainer = new SpringContainer();
        springContainer.setBeanFactory(appContext);
        StartupUtil.setupDefaultContainer(springContainer, servletConfig);
        return springContainer;
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.servlet.DwrServlet#configureContainer(org.directwebremoting.Container, javax.servlet.ServletConfig)
     */
    @Override
    protected void configureContainer(Container container, ServletConfig servletConfig) throws ServletException, IOException
    {
        // retrieve the configurators from Spring (loaded by the ContextLoaderListener)
        try
        {
            ApplicationContext appContext = getApplicationContext(servletConfig.getServletContext());
            configurators.add((Configurator) appContext.getBean(ConfigurationParser.DEFAULT_SPRING_CONFIGURATOR_ID));
        }
        catch (NoSuchBeanDefinitionException ex)
        {
            throw new ServletException("No DWR configuration was found in your application context, make sure to define one", ex);
        }

        try
        {
            if (includeDefaultConfig)
            {
                StartupUtil.configureFromSystemDwrXml(container);
            }

            StartupUtil.configureFromInitParams(container, servletConfig);
            StartupUtil.configure(container, configurators);
        }
        catch (IOException ex)
        {
            throw ex;
        }
        catch (Exception ex)
        {
            throw new ServletException(ex);
        }
    }

    /**
     * Setter for use by the Spring IoC container to tell us what Configurators
     * exist for us to configure ourselves.
     * @param configurators
     */
    public void setConfigurators(List<Configurator> configurators)
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

    /**
     * Use provided application context rather than the default.
     * @param applicationContext
     */
    public void setApplicationContext(ApplicationContext applicationContext)
    {
        this.applicationContext = applicationContext;
    }

    /**
     * Allow easy override when retrieving the application context.
     * @param servletContext
     * @return the default application context.
     */
    protected ApplicationContext getApplicationContext(ServletContext servletContext)
    {
        return this.applicationContext == null
                ? WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext)
                : this.applicationContext;
    }

    /**
     * The application context that has the configuration. If null,
     * the default application context will be used.
     */
    private ApplicationContext applicationContext = null;

    /**
     * Do we prefix the list of Configurators with a default to read the system
     * dwr.xml file?
     */
    private boolean includeDefaultConfig = true;

    /**
     * What Configurators exist for us to configure ourselves.
     */
    private List<Configurator> configurators = new ArrayList<Configurator>();

    /**
     * The WebContext that keeps http objects local to a thread
     */
    protected WebContextBuilder webContextBuilder = null;
}
