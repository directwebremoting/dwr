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
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.directwebremoting.Container;
import org.directwebremoting.ServerContextFactory;
import org.directwebremoting.WebContextFactory;
import org.directwebremoting.ServerContextFactory.ServerContextBuilder;
import org.directwebremoting.WebContextFactory.WebContextBuilder;
import org.directwebremoting.util.FakeServletConfig;
import org.directwebremoting.util.FakeServletContext;
import org.directwebremoting.util.Logger;
import org.directwebremoting.util.ServletLoggingOutput;
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
     * @throws ServletException If the setup fails.
     */
    public Container outOfContainerInit() throws ServletException
    {
        try
        {
            ServletConfig servletConfig = new FakeServletConfig("test", new FakeServletContext());
            ServletContext servletContext = servletConfig.getServletContext();

            StartupUtil.setupLogging(servletConfig, null);
            StartupUtil.logStartup(servletConfig);

            DefaultContainer container = ContainerUtil.createDefaultContainer(servletConfig);
            ContainerUtil.setupDefaultContainer(container, servletConfig);

            WebContextBuilder webContextBuilder = StartupUtil.initWebContext(servletConfig, servletContext, container);
            StartupUtil.initServerContext(servletConfig, servletContext, container);

            ContainerUtil.prepareForWebContextFilter(servletContext, servletConfig, container, webContextBuilder, null);
            ContainerUtil.configureContainerFully(container, servletConfig);
            ContainerUtil.publishContainer(container, servletConfig);

            return container;
        }
        catch (ServletException ex)
        {
            throw ex;
        }
        catch (Exception ex)
        {
            throw new ServletException(ex);
        }
    }

    /**
     * Clean up the current thread when {@link #outOfContainerInit()} has been
     * called.
     * @param container The container created by {@link #outOfContainerInit()}.
     */
    public void outOfContainerDestroy(Container container)
    {
        ServletLoggingOutput.unsetExecutionContext();

        WebContextBuilder webContextBuilder = (WebContextBuilder) container.getBean(WebContextBuilder.class.getName());
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
     * Get the {@link WebContextFactory.WebContextBuilder} out of the
     * {@link Container}, configure it (call WebContextBuilder#set()) and use it
     * to configure the {@link WebContextFactory}.
     * @param servletConfig The servlet configuration
     * @param servletContext The servlet context
     * @param servlet The servlet that we are running under
     * @param container The container to save in the ServletContext
     * @return a new WebContextBuilder
     */
    public static WebContextBuilder initWebContext(ServletConfig servletConfig, ServletContext servletContext, Container container)
    {
        WebContextBuilder webContextBuilder = (WebContextBuilder) container.getBean(WebContextBuilder.class.getName());
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
     */
    public static ServerContextBuilder initServerContext(ServletConfig servletConfig, ServletContext servletContext, Container container)
    {
        ServerContextBuilder serverContextBuilder = (ServerContextBuilder) container.getBean(ServerContextBuilder.class.getName());
        ServerContextFactory.setServerContextBuilder(serverContextBuilder);
        serverContextBuilder.set(servletConfig, servletContext, container);

        return serverContextBuilder;
    }

    /**
     * We have some special logging classes to maintain an optional dependence
     * on commons-logging. This sets the servlet for when this is not available.
     * @param servletConfig The servlet configuration
     * @param servlet The servlet that we are running under
     */
    public static void setupLogging(ServletConfig servletConfig, HttpServlet servlet)
    {
        ServletLoggingOutput.setExecutionContext(servlet);
        String logLevel = servletConfig.getInitParameter(ContainerUtil.INIT_LOGLEVEL);
        if (logLevel != null)
        {
            ServletLoggingOutput.setLevel(logLevel);
        }
    }

    /**
     * The log stream
     */
    private static final Logger log = Logger.getLogger(StartupUtil.class);
}
