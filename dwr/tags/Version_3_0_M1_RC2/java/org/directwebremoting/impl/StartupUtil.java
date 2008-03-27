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
package org.directwebremoting.impl;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.directwebremoting.Container;
import org.directwebremoting.HubFactory;
import org.directwebremoting.ServerContextFactory;
import org.directwebremoting.WebContextFactory;
import org.directwebremoting.HubFactory.HubBuilder;
import org.directwebremoting.ServerContextFactory.ServerContextBuilder;
import org.directwebremoting.WebContextFactory.WebContextBuilder;
import org.directwebremoting.extend.ContainerConfigurationException;
import org.directwebremoting.util.FakeServletConfig;
import org.directwebremoting.util.FakeServletContext;
import org.directwebremoting.util.VersionUtil;

/**
 * Some utilities to help get DWR up and running
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class StartupUtil
{
    /**
     * A way to setup DWR outside of any Containers.
     * This method can also serve as a template for in container code wanting
     * to get DWR setup. Callers of this method should clean up after themselves
     * by calling {@link #outOfContainerDestroy(Container)}
     * @return A new initialized container.
     * @throws ContainerConfigurationException If we can't use a bean
     */
    public static Container outOfContainerInit() throws ContainerConfigurationException
    {
        try
        {
            ServletConfig servletConfig = new FakeServletConfig("test", new FakeServletContext());
            ServletContext servletContext = servletConfig.getServletContext();

            StartupUtil.logStartup(servletConfig);

            Container container = ContainerUtil.createAndSetupDefaultContainer(servletConfig);

            WebContextBuilder webContextBuilder = StartupUtil.initWebContext(servletConfig, servletContext, container);
            StartupUtil.initServerContext(servletConfig, servletContext, container);

            ContainerUtil.prepareForWebContextFilter(servletContext, servletConfig, container, webContextBuilder, null);
            ContainerUtil.configureContainerFully(container, servletConfig);
            ContainerUtil.publishContainer(container, servletConfig);

            return container;
        }
        catch (ContainerConfigurationException ex)
        {
            throw ex;
        }
        catch (Exception ex)
        {
            throw new ContainerConfigurationException(ex);
        }
    }

    /**
     * Clean up the current thread when {@link #outOfContainerInit()} has been
     * called.
     * @param container The container created by {@link #outOfContainerInit()}.
     */
    public static void outOfContainerDestroy(Container container)
    {
        WebContextBuilder webContextBuilder = container.getBean(WebContextBuilder.class);
        if (webContextBuilder != null)
        {
            webContextBuilder.unset();
        }
    }

    /**
     * Some logging so we have a good clue what we are working with.
     * @param config The servlet config
     */
    public static void logStartup(ServletConfig config)
    {
        log.info("DWR Version " + VersionUtil.getVersion() + " starting.");
        log.info("- Servlet Engine: " + config.getServletContext().getServerInfo());
        log.info("- Java Version:   " + System.getProperty("java.version"));
        log.info("- Java Vendor:    " + System.getProperty("java.vendor"));
    }

    /**
     * Get the various objects out of the {@link Container}, and configure them.
     * @param servletConfig The servlet configuration
     * @param servletContext The servlet context
     * @param container The container to save in the ServletContext
     */
    public static void initContainerBeans(ServletConfig servletConfig, ServletContext servletContext, Container container)
    {
        initWebContext(servletConfig, servletContext, container);
        initServerContext(servletConfig, servletContext, container);
        initHub(servletContext, container);
    }

    /**
     * Get the {@link WebContextFactory.WebContextBuilder} out of the
     * {@link Container}, configure it (call WebContextBuilder#set()) and use it
     * to configure the {@link WebContextFactory}.
     * @param servletConfig The servlet configuration
     * @param servletContext The servlet context
     * @param container The container to save in the ServletContext
     * @return a new WebContextBuilder
     * @deprecated Use {@link #initContainerBeans(ServletConfig, ServletContext, Container)}
     */
    @Deprecated
    public static WebContextBuilder initWebContext(ServletConfig servletConfig, ServletContext servletContext, Container container)
    {
        WebContextBuilder webContextBuilder = container.getBean(WebContextBuilder.class);
        WebContextFactory.setWebContextBuilder(webContextBuilder);
        webContextBuilder.set(null, null, servletConfig, servletContext, container);

        return webContextBuilder;
    }

    /**
     * Get the {@link ServerContextFactory.ServerContextBuilder} out of the
     * {@link Container}, configure it and use it to configure the
     * {@link ServerContextFactory}
     * @param servletConfig The servlet configuration
     * @param servletContext The servlet context
     * @param container The container to save in the ServletContext
     * @return The newly created ServerContextBuilder
     * @deprecated Use {@link #initContainerBeans(ServletConfig, ServletContext, Container)}
     */
    @Deprecated
    public static ServerContextBuilder initServerContext(ServletConfig servletConfig, ServletContext servletContext, Container container)
    {
        ServerContextBuilder serverContextBuilder = container.getBean(ServerContextBuilder.class);
        ServerContextFactory.setServerContextBuilder(serverContextBuilder);
        serverContextBuilder.set(servletConfig, servletContext, container);

        return serverContextBuilder;
    }

    /**
     * Get the {@link HubFactory.HubBuilder} out of the {@link Container},
     * configure it and use it to configure the {@link HubFactory}
     * @param servletContext The servlet context
     * @param container The container to save in the ServletContext
     * @return The newly created HubBuilder
     * @deprecated Use {@link #initContainerBeans(ServletConfig, ServletContext, Container)}
     */
    @Deprecated
    public static HubBuilder initHub(ServletContext servletContext, Container container)
    {
        HubBuilder hubBuilder = container.getBean(HubBuilder.class);
        HubFactory.setHubBuilder(hubBuilder);
        hubBuilder.set(servletContext);

        return hubBuilder;
    }

    /**
     * We have some special logging classes to maintain an optional dependence
     * on commons-logging. This sets the servlet for when this is not available.
     * @param servletConfig The servlet configuration
     * @param servlet The servlet that we are running under
     * @deprecated Since version 2.1 DWR does not use Servlet Logging
     */
    @Deprecated
    @SuppressWarnings("unused")
    public static void setupLogging(ServletConfig servletConfig, HttpServlet servlet)
    {
    }

    /**
     * The log stream
     */
    private static final Log log = LogFactory.getLog(StartupUtil.class);
}
