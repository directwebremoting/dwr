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

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.directwebremoting.Container;
import org.directwebremoting.HubFactory;
import org.directwebremoting.ServerContext;
import org.directwebremoting.ServerContextFactory;
import org.directwebremoting.WebContextFactory;
import org.directwebremoting.HubFactory.HubBuilder;
import org.directwebremoting.ServerContextFactory.ServerContextBuilder;
import org.directwebremoting.WebContextFactory.WebContextBuilder;
import org.directwebremoting.annotations.AnnotationsConfigurator;
import org.directwebremoting.extend.AccessControl;
import org.directwebremoting.extend.AjaxFilterManager;
import org.directwebremoting.extend.Compressor;
import org.directwebremoting.extend.Configurator;
import org.directwebremoting.extend.ContainerAbstraction;
import org.directwebremoting.extend.ContainerConfigurationException;
import org.directwebremoting.extend.ConverterManager;
import org.directwebremoting.extend.Creator;
import org.directwebremoting.extend.CreatorManager;
import org.directwebremoting.extend.DwrConstants;
import org.directwebremoting.extend.Handler;
import org.directwebremoting.extend.ServerLoadMonitor;
import org.directwebremoting.servlet.DwrWebContextFilter;
import org.directwebremoting.servlet.PathConstants;
import org.directwebremoting.servlet.UrlProcessor;
import org.directwebremoting.util.FakeServletConfig;
import org.directwebremoting.util.FakeServletContext;
import org.directwebremoting.util.LocalUtil;
import org.directwebremoting.util.VersionUtil;
import org.xml.sax.SAXException;

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

            logStartup(servletConfig);

            Container container = createAndSetupDefaultContainer(servletConfig);

            initContainerBeans(servletConfig, servletContext, container);
            WebContextBuilder webContextBuilder = container.getBean(WebContextBuilder.class);

            prepareForWebContextFilter(servletContext, servletConfig, container, webContextBuilder, null);
            publishContainer(container, servletConfig);
            configureContainerFully(container, servletConfig);

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
        log.info("Starting: DWR v" + VersionUtil.getLabel() + " running on " + config.getServletContext().getServerInfo() + " with java v" + System.getProperty("java.version") + " from " + System.getProperty("java.vendor"));
    }

    /**
     * A combination of {@link #createDefaultContainer(ServletConfig)} and
     * {@link #setupDefaultContainer(DefaultContainer, ServletConfig)}.
     * @param servletConfig The source of init parameters
     * @return A setup implementation of DefaultContainer
     * @throws ContainerConfigurationException If the specified class could not be found or instantiated
     */
    public static Container createAndSetupDefaultContainer(ServletConfig servletConfig) throws ContainerConfigurationException
    {
        Container container;
        
        try
        {
            String typeName = servletConfig.getInitParameter(Container.class.getName());
            if (typeName == null)
            {
                container = new DefaultContainer();
            }
            else
            {
                log.debug("Using alternate Container implementation: " + typeName);
                Class<?> type = LocalUtil.classForName(typeName);
                container = (DefaultContainer) type.newInstance();
            }

            if (container instanceof DefaultContainer)
            {
                DefaultContainer defaultContainer = (DefaultContainer) container;
                setupDefaultContainer(defaultContainer, servletConfig);
            }
        }
        catch (Exception ex)
        {
            throw new ContainerConfigurationException(ex);
        }

        return container;
    }

    /**
     * Create a {@link DefaultContainer}, allowing users to upgrade to a child
     * of DefaultContainer using an {@link ServletConfig} init parameter of
     * <code>org.directwebremoting.Container</code>. Note that while the
     * parameter name is the classname of {@link Container}, currently the only
     * this can only be used to create children that inherit from
     * {@link DefaultContainer}. This restriction may be relaxed in the future.
     * Unlike {@link #setupDefaultContainer(DefaultContainer, ServletConfig)},
     * this method does not call any setup methods.
     * @param servletConfig The source of init parameters
     * @return An un'setup' implementation of DefaultContainer
     * @throws ContainerConfigurationException If the specified class could not be found
     * @see ServletConfig#getInitParameter(String)
     * @deprecated Use {@link #createAndSetupDefaultContainer(ServletConfig)}
     */
    @Deprecated
    public static DefaultContainer createDefaultContainer(ServletConfig servletConfig) throws ContainerConfigurationException
    {
        try
        {
            String typeName = servletConfig.getInitParameter(Container.class.getName());
            if (typeName == null)
            {
                return new DefaultContainer();
            }

            log.debug("Using alternate Container implementation: " + typeName);
            Class<?> type = LocalUtil.classForName(typeName);
            return (DefaultContainer) type.newInstance();
        }
        catch (Exception ex)
        {
            throw new ContainerConfigurationException(ex);
        }
    }

    /**
     * Setup a {@link DefaultContainer}.
     * Using {@link #createDefaultContainer(ServletConfig)} followed by
     * {@link #setupFromServletConfig(DefaultContainer, ServletConfig)} before
     * calling {@link DefaultContainer#setupFinished()}.
     * @param container The container to configure
     * @param servletConfig The source of init parameters
     * @throws ContainerConfigurationException If we can't use a bean
     */
    public static void setupDefaultContainer(DefaultContainer container, ServletConfig servletConfig) throws ContainerConfigurationException
    {
        log.debug("Setup: Getting parameters from defaults.properties:");
        setupDefaults(container);

        log.debug("Setup: Getting parameters from ServletConfig:");
        setupFromServletConfig(container, servletConfig);

        log.debug("Setup: Resolving multiple implementations:");
        resolveMultipleImplementations(container, servletConfig);

        log.debug("Setup: Autowire beans");
        container.setupFinished();
    }

    /**
     * We need to decide which {@link ContainerAbstraction} should be the
     * default for this {@link Container}, also we should prepare the default
     * {@link ServerLoadMonitor}.
     * @param container The container to configure
     * @param servletConfig Information about the environment
     * @throws ContainerConfigurationException If we can't use a bean
     */
    @SuppressWarnings("unchecked")
    public static void resolveMultipleImplementations(DefaultContainer container, ServletConfig servletConfig) throws ContainerConfigurationException
    {
        resolveMultipleImplementation(container, Compressor.class);

        String abstractionImplNames = container.getParameter(ContainerAbstraction.class.getName());
        log.debug("- Selecting a " + ContainerAbstraction.class.getSimpleName() + " from " + abstractionImplNames);

        for (String abstractionImplName : abstractionImplNames.split(" "))
        {
            try
            {
                Class<ContainerAbstraction> abstractionImpl = (Class<ContainerAbstraction>) Class.forName(abstractionImplName);
                ContainerAbstraction abstraction = abstractionImpl.newInstance();
                if (abstraction.isNativeEnvironment(servletConfig))
                {
                    container.addImplementation(ContainerAbstraction.class, abstractionImpl);

                    String loadMonitorImplName = container.getParameter(ServerLoadMonitor.class.getName());
                    if (loadMonitorImplName == null)
                    {
                        Class<? extends ServerLoadMonitor> loadMonitorImpl = abstraction.getServerLoadMonitorImplementation();
                        container.addImplementation(ServerLoadMonitor.class, loadMonitorImpl);
                    }

                    return;
                }
            }
            catch (Exception ex)
            {
                log.debug("  - Can't use : " + abstractionImplName + " to implement " + ContainerAbstraction.class.getName() + ". This is probably not an error unless you were expecting to use it. Reason: " + ex.toString());
            }
            catch (NoClassDefFoundError ex)
            {
                log.debug("  - Can't use : " + abstractionImplName + " to implement " + ContainerAbstraction.class.getName() + ". This is probably not an error unless you were expecting to use it. Reason: " + ex.toString());
            }
        }

        throw new ContainerConfigurationException("None of the configured ContainerAbstractions claims isNativeEnvironment=true. Implementations tested: " + abstractionImplNames);
    }

    /**
     * Some interfaces have multiple options, and we pick the first that we
     * can construct. The assumption is that multiple implementations are
     * held as strings concatenated, separated with spaces.
     * @param container The container which has a multiple implementation
     * @param toResolve The class which needs disambiguating
     */
    protected static void resolveMultipleImplementation(DefaultContainer container, Class<?> toResolve)
    {
        String abstractionImplNames = container.getParameter(toResolve.getName());
        log.debug("- Selecting a " + toResolve.getSimpleName() + " from " + abstractionImplNames);

        for (String abstractionImplName : abstractionImplNames.split(" "))
        {
            try
            {
                Class<?> abstractionImpl = Class.forName(abstractionImplName);
                if (!toResolve.isAssignableFrom(abstractionImpl))
                {
                    log.error("  - Can't cast: " + abstractionImpl.getName() + " to " + toResolve.getName());
                }

                abstractionImpl.newInstance();
                container.addParameter(toResolve.getName(), abstractionImpl.getName());

                return;
            }
            catch (Exception ex)
            {
                log.debug("  - Can't use : " + abstractionImplName + " to implement " + toResolve.getName() + ". This is probably not an error unless you were expecting to use it. Reason: " + ex.toString());
            }
            catch (NoClassDefFoundError ex)
            {
                log.debug("  - Can't use : " + abstractionImplName + " to implement " + toResolve.getName() + ". This is probably not an error unless you were expecting to use it. Reason: " + ex.toString());
            }
        }
    }

    /**
     * Take a DefaultContainer and setup the default beans
     * @param container The container to configure
     * @throws ContainerConfigurationException If we can't use a bean
     */
    public static void setupDefaults(DefaultContainer container) throws ContainerConfigurationException
    {
        try
        {
            InputStream in = StartupUtil.class.getResourceAsStream(DwrConstants.FILE_DEFAULT_PROPERTIES);
            Properties defaults = new Properties();
            defaults.load(in);

            for (Map.Entry<?, ?> entry : defaults.entrySet())
            {
                String key = (String) entry.getKey();
                String value = (String) entry.getValue();
                container.addParameter(key, value);
            }
        }
        catch (IOException ex)
        {
            throw new ContainerConfigurationException("Failed to load system defaults", ex);
        }
    }

    /**
     * Creates entries in the {@link Container} so 2 lookups are possible.
     * <ul>
     * <li>You can find a {@link Handler} for a URL. Used by {@link UrlProcessor}
     * <li>You can inject (or lookup) the URL assigned to a {@link Handler}
     * </ul>
     * @param container The container to create the entries in
     * @param url The URL of the new {@link Handler}
     * @param handler The class of Handler
     * @param propertyName The property name (for injection and lookup)
     * @throws ContainerConfigurationException From {@link DefaultContainer#addParameter(String, Object)}
     */
    public static void createPathMapping(DefaultContainer container, String url, Class<? extends Handler> handler, String propertyName) throws ContainerConfigurationException
    {
        container.addParameter(PathConstants.PATH_PREFIX + url, handler.getName());
        if (propertyName != null)
        {
            container.addParameter(propertyName, url);
        }
    }

    /**
     * Creates entries in the {@link Container} so 1 lookup is possible.
     * <ul>
     * <li>You can find a {@link Handler} for a URL. Used by {@link UrlProcessor}
     * </ul>
     * @param container The container to create the entries in
     * @param url The URL of the new {@link Handler}
     * @param handler The class of Handler
     * @throws ContainerConfigurationException From {@link DefaultContainer#addParameter(String, Object)}
     */
    public static void createPathMapping(DefaultContainer container, String url, Class<? extends Handler> handler) throws ContainerConfigurationException
    {
        createPathMapping(container, url, handler, null);
    }

    /**
     * Take a DefaultContainer and setup the default beans
     * @param container The container to configure
     * @param servletConfig The servlet configuration (null to ignore)
     * @throws ContainerConfigurationException If we can't use a bean
     */
    @SuppressWarnings("unchecked")
    public static void setupFromServletConfig(DefaultContainer container, ServletConfig servletConfig) throws ContainerConfigurationException
    {
        Enumeration<String> en = servletConfig.getInitParameterNames();
        while (en.hasMoreElements())
        {
            String name = en.nextElement();
            String value = servletConfig.getInitParameter(name);
            container.addParameter(name, value);
        }
    }

    /**
     * Make some changes to the ServletContext so {@link DwrWebContextFilter}
     * can find the Container etc.
     * @param context The servlet context
     * @param config The servlet configuration
     * @param container The container to save in the ServletContext
     * @param webContextBuilder The WebContextBuilder to save
     * @param servlet The Servlet to save
     */
    public static void prepareForWebContextFilter(ServletContext context, ServletConfig config, Container container, WebContextBuilder webContextBuilder, HttpServlet servlet)
    {
        context.setAttribute(Container.class.getName(), container);
        context.setAttribute(WebContextBuilder.class.getName(), webContextBuilder);
        context.setAttribute(ServletConfig.class.getName(), config);
        context.setAttribute(HttpServlet.class.getName(), servlet);
    }

    /**
     * Configure using the system dwr.xml from within the JAR file.
     * @param container The container to configure
     * @throws ParserConfigurationException If the config file parse fails
     * @throws IOException If the config file read fails
     * @throws SAXException If the config file parse fails
     */
    public static void configureFromSystemDwrXml(Container container) throws IOException, ParserConfigurationException, SAXException
    {
        DwrXmlConfigurator system = new DwrXmlConfigurator();
        system.setClassResourceName(DwrConstants.FILE_DWR_XML);
        system.configure(container);
    }

    /**
     * Configure using the users dwr.xml that sits next in WEB-INF
     * @param container The container to configure
     * @throws ParserConfigurationException If the config file parse fails
     * @throws IOException If the config file read fails
     * @throws SAXException If the config file parse fails
     */
    public static void configureFromDefaultDwrXml(Container container) throws IOException, ParserConfigurationException, SAXException
    {
        DwrXmlConfigurator local = new DwrXmlConfigurator();
        local.setServletResourceName(DwrConstants.DEFAULT_DWR_XML);
        local.configure(container);
    }

    /**
     * Add configurators from init params to the end of the list of
     * configurators.
     * @param container The container to configure
     * @param servletConfig The source of init parameters
     * @return true if any Configurators were read
     * @throws SAXException If the config file parse fails
     * @throws ParserConfigurationException If the config file parse fails
     * @throws IOException If the config file read fails
     */
    @SuppressWarnings("unchecked")
    public static boolean configureFromInitParams(Container container, ServletConfig servletConfig) throws IOException, ParserConfigurationException, SAXException
    {
        Enumeration<String> en = servletConfig.getInitParameterNames();
        boolean foundConfig = false;
        while (en.hasMoreElements())
        {
            String name = en.nextElement();
            String value = servletConfig.getInitParameter(name);

            // if the init param starts with "config" then try to load it
            if (name.startsWith(INIT_CONFIG))
            {
                foundConfig = true;

                StringTokenizer st = new StringTokenizer(value, "\n,");
                while (st.hasMoreTokens())
                {
                    String fileName = st.nextToken().trim();
                    DwrXmlConfigurator local = new DwrXmlConfigurator();
                    local.setServletResourceName(fileName);
                    local.configure(container);
                }
            }
            else if (name.equals(INIT_CUSTOM_CONFIGURATOR))
            {
                foundConfig = true;

                try
                {
                    Configurator configurator = LocalUtil.classNewInstance(INIT_CUSTOM_CONFIGURATOR, value, Configurator.class);
                    configurator.configure(container);
                    log.debug("Loaded config from: " + value);
                }
                catch (Exception ex)
                {
                    log.warn("Failed to start custom configurator", ex);
                }
            }
        }

        return foundConfig;
    }

    /**
     * Annotations must not break 1.3, so we use reflection to create an
     * <code>org.directwebremoting.annotations.AnnotationsConfigurator</code>
     * and the catch all sorts of random exceptions for the benefit of
     * Websphere.
     * @param container The container to configure
     * @return true if the configuration worked.
     */
    public static boolean configureFromAnnotations(Container container)
    {
        Configurator configurator = new AnnotationsConfigurator();
        configurator.configure(container);

        log.debug("Java5 AnnotationsConfigurator enabled");
        return true;
    }

    /**
     * Allow all the configurators to have a go at the container in turn
     * @param container The container to configure
     * @param configurators A list of configurators to run against the container
     */
    public static void configure(Container container, List<Configurator> configurators)
    {
        // Allow all the configurators to have a go
        for (Configurator configurator : configurators)
        {
            log.debug("** Adding config from " + configurator);
            configurator.configure(container);
        }
    }

    /**
     * Run all the default configuration options on a Container
     * @param container The container to configure
     * @param servletConfig The source of init parameters
     * @throws SAXException If the config file parse fails
     * @throws ParserConfigurationException If the config file parse fails
     * @throws IOException If the config file read fails
     */
    public static void configureContainerFully(Container container, ServletConfig servletConfig) throws IOException, ParserConfigurationException, SAXException
    {
        configureFromSystemDwrXml(container);
        boolean foundConfig = configureFromInitParams(container, servletConfig);

        // The default dwr.xml file that sits by web.xml
        boolean skip = Boolean.valueOf(servletConfig.getInitParameter(INIT_SKIP_DEFAULT));
        IOException delayedIOException = null;
        if (!foundConfig && !skip)
        {
            try
            {
                configureFromDefaultDwrXml(container);
            }
            catch (IOException ex)
            {
                // This is fatal unless we are on JDK5+ AND using annotations
                delayedIOException = ex;
            }
        }

        if (!configureFromAnnotations(container))
        {
            log.debug("Java5 AnnotationsConfigurator disabled");

            if (delayedIOException != null)
            {
                throw delayedIOException;
            }
        }
    }

    /**
     * If helps some situations if people can get at the container by looking
     * in the servlet context, under some name.
     * The name is specified in an initParameter.
     * @param container The container to publish
     * @param servletConfig Source of initParams to dictate publishing and contexts to publish to
     */
    @SuppressWarnings("unchecked")
    public static void publishContainer(Container container, ServletConfig servletConfig)
    {
        ServletContext servletContext = servletConfig.getServletContext();

        // TODO: Make sense of this
        // Store this container in the servletContext so long as no-one else did
        // INIT_PUBLISH_CONTAINER="publishContainerAs" which does not sound like
        // a logical name. Plus what happens when there is more than one DWR?
        /*
        String publishName = servletConfig.getInitParameter(INIT_PUBLISH_CONTAINER);
        if (publishName != null)
        {
            servletContext.setAttribute(publishName, container);
        }
        */

        // Push the container into a list that holds all the known containers
        List<Container> containers = (List<Container>) servletContext.getAttribute(ATTRIBUTE_CONTAINER_LIST);
        if (containers == null)
        {
            containers = new ArrayList<Container>();
        }
        containers.add(container);
        servletContext.setAttribute(ATTRIBUTE_CONTAINER_LIST, containers);

        // Update the list of ServerContexts
        ServerContext serverContext = ServerContextFactory.get(servletContext);
        String name = servletConfig.getServletName();
        contextMap.put(name, serverContext);
        log.debug("Adding to contextMap, a serverContext called " + name);

        if (contextMap.containsKey(DEFAULT_SERVERCONTEXT_NAME))
        {
            contextMap.remove(DEFAULT_SERVERCONTEXT_NAME);
            log.debug("Multiple instances of DWR detected.");
        }
        else
        {
            contextMap.put(DEFAULT_SERVERCONTEXT_NAME, serverContext);
        }
    }

    /**
     * If there is only once instance of DWR defined in a ServletContext then
     * we can get at it using this method.
     * @return The one-and-only ServerContext or null if there are more than 1.
     */
    public static ServerContext getSingletonServerContext()
    {
        return contextMap.get(DEFAULT_SERVERCONTEXT_NAME);
    }

    /**
     * Get a list of all known Containers for the given {@link ServletContext}
     * @param servletContext The context in which {@link Container}s are stored.
     * @return a list of published {@link Container}s.
     */
    @SuppressWarnings("unchecked")
    public static List<Container> getAllPublishedContainers(ServletContext servletContext)
    {
        List<Container> containers = (List<Container>) servletContext.getAttribute(ATTRIBUTE_CONTAINER_LIST);
        if (containers == null)
        {
            containers = new ArrayList<Container>();
        }

        return containers;
    }

    /**
     * Internal use only.
     * <p>If we detect that the server is being shutdown then we want to kill
     * any reverse ajax threads.
     * @param containers The list of containers to look for ServerLoadMonitors in
     * @param title What causes this (for debug purposes)
     */
    public static void shutdownServerLoadMonitorsInContainerList(List<Container> containers, String title)
    {
        if (containers == null || containers.isEmpty())
        {
            log.debug("No containers to shutdown for: " + title);
            return;
        }

        log.debug("Shutting down containers for: " + title);
        for (Container container : containers)
        {
            ServerLoadMonitor monitor = container.getBean(ServerLoadMonitor.class);
            monitor.shutdown();
        }
    }

    /**
     * Create a bunch of debug information about a container
     * @param container The container to print debug information about
     */
    public static void debugConfig(Container container)
    {
        if (log.isDebugEnabled())
        {
            // Container level debug
            log.debug("Container");
            log.debug("  Type: " + container.getClass().getName());
            for (String name : container.getBeanNames())
            {
                Object object = container.getBean(name);

                if (object instanceof String)
                {
                    log.debug("  Param: " + name + " = " + object + " (" + object.getClass().getName() + ")");
                }
                else
                {
                    log.debug("  Bean: " + name + " = " + object + " (" + object.getClass().getName() + ")");
                }
            }

            // AccessControl debugging
            AccessControl accessControl = container.getBean(AccessControl.class);
            log.debug("AccessControl");
            log.debug("  Type: " + accessControl.getClass().getName());

            // AjaxFilterManager debugging
            AjaxFilterManager ajaxFilterManager = container.getBean(AjaxFilterManager.class);
            log.debug("AjaxFilterManager");
            log.debug("  Type: " + ajaxFilterManager.getClass().getName());

            // ConverterManager debugging
            ConverterManager converterManager = container.getBean(ConverterManager.class);
            log.debug("ConverterManager");
            log.debug("  Type: " + converterManager.getClass().getName());

            // CreatorManager debugging
            CreatorManager creatorManager = container.getBean(CreatorManager.class);
            log.debug("CreatorManager");
            log.debug("  Type: " + creatorManager.getClass().getName());
            for (String creatorName : creatorManager.getCreatorNames(false))
            {
                Creator creator = creatorManager.getCreator(creatorName, false);
                log.debug("  Creator: " + creatorName + " = " + creator + " (" + creator.getClass().getName() + ")");
            }
        }
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
     * Get the {@link WebContextBuilder} out of the
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
     * Get the {@link ServerContextBuilder} out of the
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
     * Get the {@link HubBuilder} out of the {@link Container},
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
    public static void setupLogging(ServletConfig servletConfig, HttpServlet servlet)
    {
    }

    /**
     * Init parameter: Set a dwr.xml config file.
     * This is only a prefix since we might have more than 1 config file.
     */
    public static final String INIT_CONFIG = "config";

    /**
     * Init parameter: Skip reading the default config file if none are specified.
     */
    public static final String INIT_SKIP_DEFAULT = "skipDefaultConfig";

    /**
     * Init parameter: If we are doing Servlet.log logging, to what level?
     */
    public static final String INIT_LOGLEVEL = "logLevel";

    /*
     * Init parameter: Should we publish the {@link Container} to the servlet
     * context, and if so, under what name?
     */
    //public static final String INIT_PUBLISH_CONTAINER = "publishContainerAs";

    /**
     * Init parameter: If you wish to use a custom configurator, place its
     * class name here
     */
    public static final String INIT_CUSTOM_CONFIGURATOR = "customConfigurator";

    /**
     * The name under which we publish all {@link Container}s.
     */
    public static final String ATTRIBUTE_CONTAINER_LIST = "org.directwebremoting.ContainerList";

    /**
     * We store a single ServerContext in the contextMap under this name.
     */
    public static final String DEFAULT_SERVERCONTEXT_NAME = "__default";

    /**
     * To enable us to get at a ServerContext if one has been defined or at
     * all of them if several have been defined
     */
    private static final Map<String, ServerContext> contextMap = new HashMap<String, ServerContext>();

    /**
     * The log stream
     */
    private static final Log log = LogFactory.getLog(StartupUtil.class);
}
