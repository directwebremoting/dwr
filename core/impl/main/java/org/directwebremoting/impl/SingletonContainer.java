package org.directwebremoting.impl;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;

import org.directwebremoting.Container;
import org.directwebremoting.WebContextFactory.WebContextBuilder;
import org.directwebremoting.annotations.AnnotationsConfigurator;
import org.directwebremoting.extend.Configurator;
import org.directwebremoting.extend.ConverterManager;

/**
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class SingletonContainer
{
    /**
     *
     */
    public SingletonContainer(String classResourceName) throws Exception
    {
        try
        {
            container = StartupUtil.createAndSetupDefaultContainer(servletConfig);

            StartupUtil.initContainerBeans(servletConfig, servletContext, container);
            webContextBuilder = container.getBean(WebContextBuilder.class);
            StartupUtil.prepareForWebContextFilter(servletContext, servletConfig, container, webContextBuilder, null);
            StartupUtil.publishContainer(container, servletConfig);

            StartupUtil.configureFromSystemDwrXml(container);

            DwrXmlConfigurator local = new DwrXmlConfigurator();
            local.setClassResourceName(classResourceName);
            local.configure(container);

            Configurator configurator = new AnnotationsConfigurator();
            configurator.configure(container);
        }
        finally
        {
            if (webContextBuilder != null)
            {
                webContextBuilder.unset();
            }
        }
    }

    /**
     * 
     */
    public void engageThread()
    {
        webContextBuilder.set(null, null, servletConfig, servletContext, container);
    }

    /**
     * 
     */
    public void disengageThread()
    {
        webContextBuilder.unset();
    }

    /**
     *
     */
    public ConverterManager getConverterManager()
    {
        return container.getBean(ConverterManager.class);
    }

    /**
     * @return the container
     */
    public Container getContainer()
    {
        return container;
    }

    /**
     * @return the servletConfig
     */
    public ServletConfig getServletConfig()
    {
        return servletConfig;
    }

    /**
     * @return the servletContext
     */
    public ServletContext getServletContext()
    {
        return servletContext;
    }

    /**
     * @return the webContextBuilder
     */
    public WebContextBuilder getWebContextBuilder()
    {
        return webContextBuilder;
    }

    private Container container;

    private WebContextBuilder webContextBuilder;

    private ServletContext servletContext = new FakeServletContext();

    private ServletConfig servletConfig = new FakeServletConfig("dwr-test", servletContext);
}
