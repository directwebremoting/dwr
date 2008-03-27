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
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;
import javax.xml.parsers.ParserConfigurationException;

import org.directwebremoting.AccessControl;
import org.directwebremoting.AjaxFilterManager;
import org.directwebremoting.Configurator;
import org.directwebremoting.Container;
import org.directwebremoting.ConverterManager;
import org.directwebremoting.Creator;
import org.directwebremoting.CreatorManager;
import org.directwebremoting.DebugPageGenerator;
import org.directwebremoting.Remoter;
import org.directwebremoting.ScriptSessionManager;
import org.directwebremoting.ServerContextBuilder;
import org.directwebremoting.ServerLoadMonitor;
import org.directwebremoting.WebContextBuilder;
import org.directwebremoting.dwrp.DefaultConverterManager;
import org.directwebremoting.dwrp.HtmlCallMarshaller;
import org.directwebremoting.dwrp.PlainCallMarshaller;
import org.directwebremoting.dwrp.PollHandler;
import org.directwebremoting.servlet.DwrWebContextFilter;
import org.directwebremoting.servlet.UrlProcessor;
import org.directwebremoting.util.LocalUtil;
import org.directwebremoting.util.Logger;
import org.directwebremoting.util.VersionUtil;
import org.xml.sax.SAXException;

/**
 * An abstraction of all the common servlet operations that are required to host
 * a DWR service that depends on the servlet spec.
 * It would be good to have a base class for all servlet operations, however
 * lack of MI prevents us from doing this.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class ContainerUtil
{
    /**
     * Take a DefaultContainer and setup the default beans
     * @param defaultContainer The container to configure
     * @throws InstantiationException If we can't instantiate a bean
     * @throws IllegalAccessException If we have access problems creating a bean
     */
    public static void setupDefaults(DefaultContainer defaultContainer) throws InstantiationException, IllegalAccessException
    {
        defaultContainer.addParameter(AccessControl.class.getName(), DefaultAccessControl.class.getName());
        defaultContainer.addParameter(ConverterManager.class.getName(), DefaultConverterManager.class.getName());
        defaultContainer.addParameter(CreatorManager.class.getName(), DefaultCreatorManager.class.getName());
        defaultContainer.addParameter(UrlProcessor.class.getName(), UrlProcessor.class.getName());
        defaultContainer.addParameter(WebContextBuilder.class.getName(), DefaultWebContextBuilder.class.getName());
        defaultContainer.addParameter(ServerContextBuilder.class.getName(), DefaultServerContextBuilder.class.getName());
        defaultContainer.addParameter(AjaxFilterManager.class.getName(), DefaultAjaxFilterManager.class.getName());
        defaultContainer.addParameter(Remoter.class.getName(), DefaultRemoter.class.getName());
        defaultContainer.addParameter(DebugPageGenerator.class.getName(), DefaultDebugPageGenerator.class.getName());
        defaultContainer.addParameter(HtmlCallMarshaller.class.getName(), HtmlCallMarshaller.class.getName());
        defaultContainer.addParameter(PlainCallMarshaller.class.getName(), PlainCallMarshaller.class.getName());
        defaultContainer.addParameter(PollHandler.class.getName(), PollHandler.class.getName());
        defaultContainer.addParameter(ScriptSessionManager.class.getName(), DefaultScriptSessionManager.class.getName());
        defaultContainer.addParameter(ServerLoadMonitor.class.getName(), DefaultServerLoadMonitor.class.getName());

        defaultContainer.addParameter("debug", "false");
        defaultContainer.addParameter("allowImpossibleTests", "false");
        defaultContainer.addParameter("scriptCompressed", "false");
    }

    /**
     * Take a DefaultContainer and setup the default beans
     * @param defaultContainer The container to configure
     * @param servletConfig The servlet configuration (null to ignore)
     * @throws InstantiationException If we can't instantiate a bean
     * @throws IllegalAccessException If we have access problems creating a bean
     */
    public static void setupFromServletConfig(DefaultContainer defaultContainer, ServletConfig servletConfig) throws InstantiationException, IllegalAccessException
    {
        Enumeration en = servletConfig.getInitParameterNames();
        while (en.hasMoreElements())
        {
            String name = (String) en.nextElement();
            String value = servletConfig.getInitParameter(name);
            defaultContainer.addParameter(name, value);
        }
    }

    /**
     * Make some changes to the ServletContext so {@link DwrWebContextFilter}
     * can find the Container etc.
     * @param config The current configuration
     * @param container The container to save in the ServletContext 
     * @param webContextBuilder The WebContextBuilder to save
     * @param servlet The Servlet to save
     */
    public static void prepareForWebContextFilter(ServletConfig config, Container container, WebContextBuilder webContextBuilder, HttpServlet servlet)
    {
        ServletContext context = config.getServletContext();

        context.setAttribute(Container.class.getName(), container);
        context.setAttribute(WebContextBuilder.class.getName(), webContextBuilder);
        context.setAttribute(ServletConfig.class.getName(), config);
        context.setAttribute(HttpServlet.class.getName(), servlet);
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
    public static boolean configureUsingInitParams(Container container, ServletConfig servletConfig) throws IOException, ParserConfigurationException, SAXException
    {
        Enumeration en = servletConfig.getInitParameterNames();
        boolean foundConfig = false;
        while (en.hasMoreElements())
        {
            String name = (String) en.nextElement();
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
                    Configurator configurator = (Configurator) LocalUtil.classNewInstance(INIT_CUSTOM_CONFIGURATOR, value, Configurator.class);
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
     * Allow all the configurators to have a go at the container in turn
     * @param container The container to configure
     * @param configurators A list of configurators to run against the container
     */
    public static void configure(Container container, List configurators)
    {
        // Allow all the configurators to have a go
        for (Iterator it = configurators.iterator(); it.hasNext();)
        {
            Configurator configurator = (Configurator) it.next();

            log.debug("** Adding config from " + configurator);
            configurator.configure(container);
        }
    }

    /**
     * If helps some situations if people can get at the container by looking
     * in the servlet context, under some name.
     * The name is specified in an initParameter. 
     * @param container The container to publish
     * @param config Source of initParams to dictate publishing and contexts to publish to
     */
    public static void publishContainer(Container container, ServletConfig config)
    {
        String publishName = config.getInitParameter(INIT_PUBLISH_CONTAINER);
        if (publishName != null)
        {
            config.getServletContext().setAttribute(publishName, container);
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
        log.info("- Java Version:   " + System.getProperty("java.version")); //$NON-NLS-2$
        log.info("- Java Vendor:    " + System.getProperty("java.vendor")); //$NON-NLS-2$
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
            Collection beanNames = container.getBeanNames();
            for (Iterator it = beanNames.iterator(); it.hasNext();)
            {
                String name = (String) it.next();
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
            AccessControl accessControl = (AccessControl) container.getBean(AccessControl.class.getName());
            log.debug("AccessControl");
            log.debug("  Type: " + accessControl.getClass().getName());

            // AjaxFilterManager debugging
            AjaxFilterManager ajaxFilterManager = (AjaxFilterManager) container.getBean(AjaxFilterManager.class.getName());
            log.debug("AjaxFilterManager");
            log.debug("  Type: " + ajaxFilterManager.getClass().getName());

            // ConverterManager debugging
            ConverterManager converterManager = (ConverterManager) container.getBean(ConverterManager.class.getName());
            log.debug("ConverterManager");
            log.debug("  Type: " + converterManager.getClass().getName());

            // CreatorManager debugging
            CreatorManager creatorManager = (CreatorManager) container.getBean(CreatorManager.class.getName());
            log.debug("CreatorManager");
            log.debug("  Type: " + creatorManager.getClass().getName());
            Collection creatorNames = creatorManager.getCreatorNames();
            for (Iterator it = creatorNames.iterator(); it.hasNext();)
            {
                String creatorName = (String) it.next();
                Creator creator = creatorManager.getCreator(creatorName);
                log.debug("  Creator: " + creatorName + " = " + creator + " (" + creator.getClass().getName() + ")");
            }
        }
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

    /**
     * Init parameter: Should we publish the container to the servlet context,
     * and if so, under what name?
     */
    public static final String INIT_PUBLISH_CONTAINER = "publishContainerAs";

    /**
     * Init parameter: If you wish to use a custom configurator, place its
     * class name here
     */
    public static final String INIT_CUSTOM_CONFIGURATOR = "customConfigurator";

    /**
     * The log stream
     */
    private static final Logger log = Logger.getLogger(ContainerUtil.class);
}
