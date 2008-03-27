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
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.xml.parsers.ParserConfigurationException;

import org.directwebremoting.Container;
import org.directwebremoting.ServerContextFactory.ServerContextBuilder;
import org.directwebremoting.WebContextFactory.WebContextBuilder;
import org.directwebremoting.dwrp.DefaultConverterManager;
import org.directwebremoting.dwrp.HtmlCallMarshaller;
import org.directwebremoting.dwrp.PlainCallMarshaller;
import org.directwebremoting.extend.AccessControl;
import org.directwebremoting.extend.AjaxFilterManager;
import org.directwebremoting.extend.Configurator;
import org.directwebremoting.extend.ConverterManager;
import org.directwebremoting.extend.Creator;
import org.directwebremoting.extend.CreatorManager;
import org.directwebremoting.extend.DebugPageGenerator;
import org.directwebremoting.extend.DwrConstants;
import org.directwebremoting.extend.PageNormalizer;
import org.directwebremoting.extend.Remoter;
import org.directwebremoting.extend.ScriptSessionManager;
import org.directwebremoting.extend.ServerLoadMonitor;
import org.directwebremoting.servlet.AboutHandler;
import org.directwebremoting.servlet.AuthHandler;
import org.directwebremoting.servlet.DwrWebContextFilter;
import org.directwebremoting.servlet.EngineHandler;
import org.directwebremoting.servlet.HtmlCallHandler;
import org.directwebremoting.servlet.HtmlPollHandler;
import org.directwebremoting.servlet.IndexHandler;
import org.directwebremoting.servlet.InterfaceHandler;
import org.directwebremoting.servlet.PathConstants;
import org.directwebremoting.servlet.PlainCallHandler;
import org.directwebremoting.servlet.PlainPollHandler;
import org.directwebremoting.servlet.TestHandler;
import org.directwebremoting.servlet.UrlProcessor;
import org.directwebremoting.servlet.UtilHandler;
import org.directwebremoting.servlet.WebworkUtilHandler;
import org.directwebremoting.util.LocalUtil;
import org.directwebremoting.util.Logger;
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
     * Create a {@link DefaultContainer}, allowing users to upgrade to a child
     * of DefaultContainer using an {@link ServletConfig} init parameter of
     * <code>org.directwebremoting.Container</code>. Note that while the
     * parameter name is the classname of {@link Container}, currently the only
     * this can only be used to create children that inherit from
     * {@link DefaultContainer}. This restriction may be relaxed in the future.
     * Unlike {@link #setupDefaultContainer(DefaultContainer, ServletConfig)},
     * this method does not call any setup methods.
     * @param servletConfig The source of init parameters
     * @return An unsetup implementaion of DefaultContainer
     * @throws ServletException If the specified class could not be found
     * @see ServletConfig#getInitParameter(String) 
     */
    public static DefaultContainer createDefaultContainer(ServletConfig servletConfig) throws ServletException
    {
        try
        {
            String typeName = servletConfig.getInitParameter(Container.class.getName());
            if (typeName == null)
            {
                return new DefaultContainer();
            }

            log.debug("Using alternate Container implementation: " + typeName);
            Class type = LocalUtil.classForName(typeName);
            return (DefaultContainer) type.newInstance();
        }
        catch (Exception ex)
        {
            throw new ServletException(ex);
        }
    }

    /**
     * Setup a {@link DefaultContainer}.
     * Using {@link #createDefaultContainer(ServletConfig)} followed by
     * {@link #setupFromServletConfig(DefaultContainer, ServletConfig)} before
     * calling {@link DefaultContainer#setupFinished()}.
     * @param container The container to configure
     * @param servletConfig The source of init parameters
     * @throws InstantiationException If we can't instantiate a bean
     * @throws IllegalAccessException If we have access problems creating a bean
     */
    public static void setupDefaultContainer(DefaultContainer container, ServletConfig servletConfig) throws InstantiationException, IllegalAccessException
    {
        setupDefaults(container);
        setupFromServletConfig(container, servletConfig);
        container.setupFinished();
    }

    /**
     * Take a DefaultContainer and setup the default beans
     * @param container The container to configure
     * @throws InstantiationException If we can't instantiate a bean
     * @throws IllegalAccessException If we have access problems creating a bean
     */
    public static void setupDefaults(DefaultContainer container) throws InstantiationException, IllegalAccessException
    {
        container.addParameter(AccessControl.class.getName(), DefaultAccessControl.class.getName());
        container.addParameter(ConverterManager.class.getName(), DefaultConverterManager.class.getName());
        container.addParameter(CreatorManager.class.getName(), DefaultCreatorManager.class.getName());
        container.addParameter(UrlProcessor.class.getName(), UrlProcessor.class.getName());
        container.addParameter(WebContextBuilder.class.getName(), DefaultWebContextBuilder.class.getName());
        container.addParameter(ServerContextBuilder.class.getName(), DefaultServerContextBuilder.class.getName());
        container.addParameter(AjaxFilterManager.class.getName(), DefaultAjaxFilterManager.class.getName());
        container.addParameter(Remoter.class.getName(), DefaultRemoter.class.getName());
        container.addParameter(DebugPageGenerator.class.getName(), DefaultDebugPageGenerator.class.getName());
        container.addParameter(PlainCallMarshaller.class.getName(), PlainCallMarshaller.class.getName());
        container.addParameter(HtmlCallMarshaller.class.getName(), HtmlCallMarshaller.class.getName());
        container.addParameter(PlainPollHandler.class.getName(), PlainPollHandler.class.getName());
        container.addParameter(HtmlPollHandler.class.getName(), HtmlPollHandler.class.getName());
        container.addParameter(ScriptSessionManager.class.getName(), DefaultScriptSessionManager.class.getName());
        container.addParameter(ServerLoadMonitor.class.getName(), DefaultServerLoadMonitor.class.getName());
        container.addParameter(PageNormalizer.class.getName(), DefaultPageNormalizer.class.getName());

        // Mapping handlers to URLs
        container.addParameter(PathConstants.URL_PREFIX + "/index.html", IndexHandler.class.getName());
        container.addParameter(PathConstants.URL_PREFIX + "/engine.js", EngineHandler.class.getName());
        container.addParameter(PathConstants.URL_PREFIX + "/util.js", UtilHandler.class.getName());
        container.addParameter(PathConstants.URL_PREFIX + "/auth.js", AuthHandler.class.getName());
        container.addParameter(PathConstants.URL_PREFIX + "/webwork/DWRActionUtil.js", WebworkUtilHandler.class.getName());
        container.addParameter(PathConstants.URL_PREFIX + "/about", AboutHandler.class.getName());
        container.addParameter(PathConstants.URL_PREFIX + "/test/", TestHandler.class.getName());
        container.addParameter(PathConstants.URL_PREFIX + "/interface/", InterfaceHandler.class.getName());
        container.addParameter(PathConstants.URL_PREFIX + "/call/plaincall/", PlainCallHandler.class.getName());
        container.addParameter(PathConstants.URL_PREFIX + "/call/htmlcall/", HtmlCallHandler.class.getName());
        container.addParameter(PathConstants.URL_PREFIX + "/call/plainpoll/", PlainPollHandler.class.getName());
        container.addParameter(PathConstants.URL_PREFIX + "/call/htmlpoll/", HtmlPollHandler.class.getName());

        container.addParameter(PathConstants.URL_INDEX, "/index.html");
        container.addParameter(PathConstants.URL_ENGINE, "/engine.js");
        container.addParameter(PathConstants.URL_UTIL, "/util.js");
        container.addParameter(PathConstants.URL_AUTH, "/auth.js");
        container.addParameter(PathConstants.URL_WEBWORKUTIL, "/webwork/DWRActionUtil.js");
        container.addParameter(PathConstants.URL_TEST, "/test/");
        container.addParameter(PathConstants.URL_INTERFACE, "/interface/");
        // The Poll and Call URLs can not be changed easily because they are
        // referenced from engine.js. Maybe one day this would be a good
        // extension
    }

    /**
     * Take a DefaultContainer and setup the default beans
     * @param container The container to configure
     * @param servletConfig The servlet configuration (null to ignore)
     * @throws InstantiationException If we can't instantiate a bean
     * @throws IllegalAccessException If we have access problems creating a bean
     */
    public static void setupFromServletConfig(DefaultContainer container, ServletConfig servletConfig) throws InstantiationException, IllegalAccessException
    {
        Enumeration en = servletConfig.getInitParameterNames();
        while (en.hasMoreElements())
        {
            String name = (String) en.nextElement();
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
    public static boolean configureFromInitParams(Container container, ServletConfig servletConfig) throws IOException, ParserConfigurationException, SAXException
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
     * Annotations must not break 1.3, so we use reflection to create an
     * <code>org.directwebremoting.annotations.AnnotationsConfigurator</code>
     * and the catch all sorts of random exceptions for the benefit of
     * Websphere.
     * @param container The container to configure
     * @return true if the configuration worked.
     */
    public static boolean configureFromAnnotations(Container container)
    {
        try
        {
            Class annotationCfgClass = LocalUtil.classForName("org.directwebremoting.annotations.AnnotationsConfigurator");
    
            Configurator configurator = (Configurator) annotationCfgClass.newInstance();
            configurator.configure(container);

            log.debug("Java5 AnnotationsConfigurator enabled");
            return true;
        }
        catch (UnsupportedClassVersionError ex)
        {
            // This will happen in JDK 1.4 and below
            return false;
        }
        catch (ClassNotFoundException ex)
        {
            // This will happen when run in an IDE without the java5 tree
            log.warn("AnnotationsConfigurator is missing. Are you running from within an IDE?");
            return false;
        }
        catch (Exception ex)
        {
            // This happens if there is a bug in AnnotationsConfigurator
            log.warn("Failed to start annotations", ex);
            return false;
        }
        catch (Throwable ex)
        {
            if (ex.getClass().getName().equals(UnsupportedClassVersionError.class.getName()))
            {
                log.error("Caught impossible Throwable", ex);
                return false;
            }
            else if (ex.getClass().getName().equals(LinkageError.class.getName()))
            {
                log.error("Caught impossible Throwable", ex);
                return false;
            }
            else if (ex instanceof Error)
            {
                log.fatal("Rethrowing Error:" + ex);
                throw (Error) ex;
            }
            else
            {
                // This can't happen: We've handled all Exceptions, and
                // Errors, so we can't get to execute this code.
                log.error("Ilogical catch state", ex);
                return false;
            }
        }
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
        boolean skip = Boolean.valueOf(servletConfig.getInitParameter(INIT_SKIP_DEFAULT)).booleanValue();
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
