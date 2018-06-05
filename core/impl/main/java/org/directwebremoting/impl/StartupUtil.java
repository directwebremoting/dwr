package org.directwebremoting.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.xml.parsers.ParserConfigurationException;

import org.directwebremoting.Container;
import org.directwebremoting.HubFactory;
import org.directwebremoting.ServerContext;
import org.directwebremoting.ServerContextFactory;
import org.directwebremoting.WebContextFactory;
import org.directwebremoting.WebContextFactory.WebContextBuilder;
import org.directwebremoting.annotations.AnnotationsConfigurator;
import org.directwebremoting.event.ScriptSessionListener;
import org.directwebremoting.extend.AccessControl;
import org.directwebremoting.extend.AjaxFilterManager;
import org.directwebremoting.extend.CallbackHelperFactory;
import org.directwebremoting.extend.Configurator;
import org.directwebremoting.extend.ContainerAbstraction;
import org.directwebremoting.extend.ContainerConfigurationException;
import org.directwebremoting.extend.ConverterManager;
import org.directwebremoting.extend.Creator;
import org.directwebremoting.extend.CreatorManager;
import org.directwebremoting.extend.DwrConstants;
import org.directwebremoting.extend.Handler;
import org.directwebremoting.extend.ModuleManager;
import org.directwebremoting.extend.ScriptSessionManager;
import org.directwebremoting.extend.ServerLoadMonitor;
import org.directwebremoting.extend.TaskDispatcherFactory;
import org.directwebremoting.json.parse.JsonParserFactory;
import org.directwebremoting.json.serialize.JsonSerializerFactory;
import org.directwebremoting.servlet.PathConstants;
import org.directwebremoting.servlet.UrlProcessor;
import org.directwebremoting.util.FakeServletConfig;
import org.directwebremoting.util.FakeServletContextFactory;
import org.directwebremoting.util.LocalUtil;
import org.directwebremoting.util.Loggers;
import org.directwebremoting.util.VersionUtil;
import org.xml.sax.SAXException;

/**
 * Some utilities to help get DWR up and running
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class StartupUtil
{
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
     * Init parameter: If you wish to use a custom configurator, place its
     * class name here
     */
    public static final String INIT_CUSTOM_CONFIGURATOR = "customConfigurator";

    /**
     * The name under which we publish all {@link Container}s.
     */
    public static final String ATTRIBUTE_CONTAINER_LIST = "org.directwebremoting.ContainerList";

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
            ServletConfig servletConfig = new FakeServletConfig("test", FakeServletContextFactory.create());
            logStartup("DWR:OutOfContainer", servletConfig);
            Container container = createAndSetupDefaultContainer(servletConfig);
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
            webContextBuilder.disengageThread();
        }
    }

    /**
     * Some logging so we have a good clue what we are working with.
     * @param name The servlet name (so we can distinguish implementations)
     * @param config The servlet config
     */
    public static void logStartup(String name, ServletConfig config)
    {
        ServletContext servletContext = config.getServletContext();

        // SERVLET24: Use getContextPath directly in 2.5
        String contextPath = LocalUtil.getProperty(servletContext, "ContextPath", String.class);
        contextPath = contextPath == null ? "" :  " at " + contextPath;

        Loggers.STARTUP.info("Starting: " + name + " v" + VersionUtil.getLabel() + " on " + servletContext.getServerInfo() + " / JDK " + System.getProperty("java.version") + " from " + System.getProperty("java.vendor") + contextPath);
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
            String typeName = servletConfig.getInitParameter(LocalUtil.originalDwrClassName(Container.class.getName()));
            if (typeName == null)
            {
                container = new DefaultContainer();
            }
            else
            {
                Loggers.STARTUP.debug("Using alternate Container implementation: " + typeName);
                Class<?> type = LocalUtil.classForName(typeName);
                container = (Container) type.newInstance();
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
     */
    public static DefaultContainer createDefaultContainer(ServletConfig servletConfig) throws ContainerConfigurationException
    {
        try
        {
            String typeName = servletConfig.getInitParameter(LocalUtil.originalDwrClassName(Container.class.getName()));
            if (typeName == null)
            {
                return new DefaultContainer();
            }

            Loggers.STARTUP.debug("Using alternate Container implementation: " + typeName);
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
        Loggers.STARTUP.debug("Setup: Getting parameters from defaults.properties:");
        setupDefaults(container);

        // Add the ServletConfig and ServletContext to the container so they can
        // be injected into contained beans
        Loggers.STARTUP.debug("Setup: Getting parameters from environment:");
        container.addBean(Container.class, container);
        container.addBean(ServletConfig.class, servletConfig);
        container.addBean(ServletContext.class, servletConfig.getServletContext());

        Loggers.STARTUP.debug("Setup: Getting parameters from ServletConfig:");
        setupFromServletConfig(container, servletConfig);

        Loggers.STARTUP.debug("Setup: Applying long versions of shortcut parameters:");
        applyParameterShortcuts(container);

        Loggers.STARTUP.debug("Setup: Resolving multiple implementations:");
        resolveMultipleImplementations(container, servletConfig);

        Loggers.STARTUP.debug("Setup: Autowire beans");
        container.setupFinished();

        Loggers.STARTUP.debug("Setup: Resolving listener implementations:");
        resolveListenerImplementations(container, servletConfig);

        Loggers.STARTUP.debug("Setup: Initializing Factories:");
        ServerContext serverContext = ServerContextFactory.attach(container);
        WebContextFactory.attach(container);
        HubFactory.attach(container);
        JsonParserFactory.attach(container);
        JsonSerializerFactory.attach(container);
        CallbackHelperFactory.attach(container);
        TaskDispatcherFactory.attach(container);

        // Make some changes to the ServletContext so {@link DwrWebContextFilter}
        // can find the Container etc.
        WebContextBuilder webContextBuilder = container.getBean(WebContextBuilder.class);
        ServletContext servletContext = servletConfig.getServletContext();
        servletContext.setAttribute(Container.class.getName(), container);
        servletContext.setAttribute(WebContextBuilder.class.getName(), webContextBuilder);
        servletContext.setAttribute(ServletConfig.class.getName(), servletConfig);

        publishContainer(container, serverContext, servletConfig);
    }

    /**
     * Some parameters might be shortcuts for one or more other parameters.
     * For example 'interactivity. This method resolves those shortcuts by
     * adding new values into the container.
     */
    private static void applyParameterShortcuts(DefaultContainer container)
    {
        Object bean = container.getBean("interactivity");
        if (bean != null)
        {
            if (bean instanceof String)
            {
                String level = (String) bean;
                if ("stateless".equals(level))
                {
                    container.addImplementation(ScriptSessionManager.class, TransientScriptSessionManager.class);
                }
                else if ("passiveReverseAjax".equals(level))
                {
                    // The default - do nothing
                }
                else if ("activeReverseAjax".equals(level))
                {
                    container.addParameter("activeReverseAjaxEnabled", "true");
                }
                else
                {
                    Loggers.STARTUP.error("Illegal value for 'interactivity' parameter of '" + level + "'. Valid values are [stateless|passiveReverseAjax|activeReverseAjax]. Ignoring.");
                }
            }
            else
            {
                Loggers.STARTUP.error("Found non-string value for 'interactivity' parameter. Ignoring.");
            }
        }

        String allowGetForSafariButMakeForgeryEasier = container.getParameter("allowGetForSafariButMakeForgeryEasier");
        String allowGetButMakeForgeryEasier = container.getParameter("allowGetButMakeForgeryEasier");
        if (allowGetForSafariButMakeForgeryEasier != null && allowGetButMakeForgeryEasier == null)
        {
            container.addParameter("allowGetButMakeForgeryEasier", allowGetForSafariButMakeForgeryEasier);
        }
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
    	try
    	{
            resolveMultipleImplementation(container, LocalUtil.originalDwrClassName("org.directwebremoting.dwrp.FileUpload"));
    	}
    	catch(Exception fue)
    	{
    		Loggers.STARTUP.debug("A FileUpload implementation is not available. Details: " + fue, fue);
    	}
    	try
    	{
            resolveMultipleImplementation(container, LocalUtil.originalDwrClassName("org.directwebremoting.extend.Compressor"));
        } catch(Exception ce)
        {
        	Loggers.STARTUP.debug("A Compressor implemenation is not available. Details: " + ce, ce);
        }

        Object value = container.getBean(LocalUtil.originalDwrClassName(ContainerAbstraction.class.getName()));
        List<Object> abstractionImpls = new ArrayList<Object>();
        if (value instanceof String)
        {
            Collections.addAll(abstractionImpls, ((String) value).replace(",", " ").split(" "));
        }
        else
        {
            abstractionImpls.add(value);
        }
        Loggers.STARTUP.debug("- Selecting a " + ContainerAbstraction.class.getSimpleName() + " from " + abstractionImpls);

        for (Object abstractionImpl : abstractionImpls)
        {
            try
            {
                if (abstractionImpl == null)
                {
                    continue;
                }

                ContainerAbstraction abstraction;
                if (abstractionImpl instanceof String)
                {
                    String abstractionImplName = (String) abstractionImpl;
                    if (abstractionImplName.trim().length() == 0)
                    {
                        continue;
                    }
                    Class<ContainerAbstraction> abstractionClass = (Class<ContainerAbstraction>) LocalUtil.classForName(abstractionImplName);
                    abstraction = abstractionClass.newInstance();                }
                else
                {
                    abstraction = (ContainerAbstraction) abstractionImpl;
                }

                if (abstraction.isNativeEnvironment(servletConfig))
                {
                    Loggers.STARTUP.info("Starting: Using container abstraction " + abstraction.getClass().getName());
                    container.addImplementation(ContainerAbstraction.class, abstraction.getClass());

                    String loadMonitorImplName = container.getParameter(LocalUtil.originalDwrClassName(ServerLoadMonitor.class.getName()));
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
                Loggers.STARTUP.debug("  - Can't use : " + abstractionImpl + " to implement " + ContainerAbstraction.class.getName() + ". This is probably not an error unless you were expecting to use it. Reason: " + ex);
            }
            catch (NoClassDefFoundError ex)
            {
                Loggers.STARTUP.debug("  - Can't use : " + abstractionImpl + " to implement " + ContainerAbstraction.class.getName() + ". This is probably not an error unless you were expecting to use it. Reason: " + ex);
            }
        }

        throw new ContainerConfigurationException("None of the configured ContainerAbstractions claims isNativeEnvironment=true. Implementations tested: " + abstractionImpls);
    }

    /**
     * Some interfaces have multiple options, and we pick the first that we
     * can construct. The assumption is that multiple implementations are
     * held as strings concatenated, separated with spaces.
     * @param container The container which has a multiple implementation
     * @param toResolveString The class name which needs disambiguating
     */
    protected static void resolveMultipleImplementation(DefaultContainer container, String toResolveString)
    {
        Class<?> toResolve = null;
        try
        {
            toResolve = LocalUtil.classForName(toResolveString);
        }
        catch (Exception ex)
        {
            Loggers.STARTUP.debug(toResolveString + " is not available. Details: " + ex);
        }

        Object implObjs = container.getBean(toResolveString);
        if (implObjs == null || ! (implObjs instanceof String))
        {
            // Found no value or instance that was already instantiated, do nothing
            return;
        }

        String implNames = (String) implObjs;
        Loggers.STARTUP.debug("- Selecting a " + toResolveString + " from " + implNames);

        implNames = implNames.replace(',', ' ');
        for (String implName : implNames.split(" "))
        {
            if (implName.equals(""))
            {
                continue;
            }

            try
            {
                Class<?> impl = LocalUtil.classForName(implName);
                if (!toResolve.isAssignableFrom(impl))
                {
                    Loggers.STARTUP.error("  - Can't cast: " + impl.getName() + " to " + toResolve.getName());
                }

                impl.newInstance();
                container.addParameter(LocalUtil.originalDwrClassName(toResolve.getName()), impl.getName());

                return;
            }
            catch (Exception ex)
            {
                Loggers.STARTUP.debug("  - Can't use : " + implName + " to implement " + toResolve.getName() + ". This is probably not an error unless you were expecting to use it. Reason: " + ex);
            }
            catch (Throwable t)
            {
                Loggers.STARTUP.debug("  - Can't use : " + implName + " to implement " + toResolve.getName() + ". This is probably not an error unless you were expecting to use it. Reason: " + t);
            }
        }
    }

    /**
     * We need to add all the {@link ScriptSessionListener}s to the
     * {@link ScriptSessionManager}.
     * @param container The container to configure
     * @param servletConfig Information about the environment
     * @throws ContainerConfigurationException If we can't use a bean
     */
    public static void resolveListenerImplementations(DefaultContainer container, ServletConfig servletConfig) throws ContainerConfigurationException
    {
        ScriptSessionManager manager = container.getBean(ScriptSessionManager.class);

        Object scriptSessionListenerClasses = container.getBean(LocalUtil.originalDwrClassName(ScriptSessionListener.class.getName()));
        if (scriptSessionListenerClasses == null)
        {
            Loggers.STARTUP.debug("- No implementations of " + ScriptSessionListener.class.getSimpleName() + " to register");
            return;
        }

        if (! (scriptSessionListenerClasses instanceof String))
        {
            // Found instance that was already instantiated, do nothing
            return;
        }

        String implNames = (String) scriptSessionListenerClasses;
        Loggers.STARTUP.debug("- Creating list of " + ScriptSessionListener.class.getSimpleName() + " from " + implNames);

        implNames = implNames.replace(',', ' ');
        for (String implName : implNames.split(" "))
        {
            if (implName.equals(""))
            {
                continue;
            }

            try
            {
                Class<?> impl = LocalUtil.classForName(implName);
                if (!ScriptSessionListener.class.isAssignableFrom(impl))
                {
                    Loggers.STARTUP.error("  - Can't cast: " + impl.getName() + " to " + ScriptSessionListener.class.getName());
                }
                else
                {
                    @SuppressWarnings("unchecked")
                    Class<? extends ScriptSessionListener> i = (Class<? extends ScriptSessionListener>) impl;
                    ScriptSessionListener instance = i.newInstance();

                    manager.addScriptSessionListener(instance);
                }
            }
            catch (Exception ex)
            {
                Loggers.STARTUP.error("  - Can't use : " + implName + " to implement " + ScriptSessionListener.class.getName() + ". Reason: " + ex);
            }
            catch (NoClassDefFoundError ex)
            {
                Loggers.STARTUP.error("  - Can't use : " + implName + " to implement " + ScriptSessionListener.class.getName() + ". Reason: " + ex);
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
            InputStream in = LocalUtil.getInternalResourceAsStream(DwrConstants.SYSTEM_DEFAULT_PROPERTIES_PATH);
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
     * Configure using the system dwr.xml from within the JAR file.
     * @param container The container to configure
     * @throws ParserConfigurationException If the config file parse fails
     * @throws IOException If the config file read fails
     * @throws SAXException If the config file parse fails
     */
    public static void configureFromSystemDwrXml(Container container) throws IOException, ParserConfigurationException, SAXException
    {
        DwrXmlConfigurator system = new DwrXmlConfigurator();
        system.setClassResourceName(DwrConstants.SYSTEM_DWR_XML_PATH);
        system.configure(container);
    }

    /**
     * Configure using the users dwr.xml that sits next in WEB-INF
     * @param container The container to configure
     * @throws ParserConfigurationException If the config file parse fails
     * @throws IOException If the config file read fails
     * @throws SAXException If the config file parse fails
     */
    public static void configureFromDefaultDwrXml(Container container, ServletConfig servletConfig) throws IOException, ParserConfigurationException, SAXException
    {
        DwrXmlConfigurator local = new DwrXmlConfigurator();
        local.setServletResourceName(servletConfig.getServletContext(), DwrConstants.USER_DWR_XML_PATH);
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
                    local.setServletResourceName(servletConfig.getServletContext(), fileName);
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
                    Loggers.STARTUP.debug("Loaded config from: " + value);
                }
                catch (Exception ex)
                {
                    Loggers.STARTUP.error("Failed to start custom configurator", ex);
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
        Object data = container.getBean("classes");
        if (null != data)
        {
            Configurator configurator = new AnnotationsConfigurator();
            configurator.configure(container);

            Loggers.STARTUP.debug("Java5 AnnotationsConfigurator enabled");
            return true;
        }
        return false;
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
            Loggers.STARTUP.debug("Adding config from " + configurator);
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
                configureFromDefaultDwrXml(container, servletConfig);
            }
            catch (IOException ex)
            {
                // This is fatal unless we are on JDK5+ AND using annotations
                delayedIOException = ex;
            }
        }

        if (!configureFromAnnotations(container))
        {
            Loggers.STARTUP.debug("Java5 AnnotationsConfigurator disabled");

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
    private static void publishContainer(Container container, ServerContext serverContext, ServletConfig servletConfig)
    {
        ServletContext servletContext = servletConfig.getServletContext();

        // Push the container into a list that holds all the known containers
        SerializableContainerListWrapper containers = (SerializableContainerListWrapper) servletContext.getAttribute(ATTRIBUTE_CONTAINER_LIST);
        if (containers == null)
        {
            containers = new SerializableContainerListWrapper();
        }
        containers.add(container);
        servletContext.setAttribute(ATTRIBUTE_CONTAINER_LIST, containers);

        // Attempt to set the singleton ServerContext, unsetting for all if
        // there is already one
        synchronized (contextMap)
        {
            switch (foundContexts)
            {
            case 0:
                // No-one has been here before - set us as the default
                singletonServerContext = serverContext;
                break;
            case 1:
                // We're second - remove the previous guy from being default
                singletonServerContext = null;
                Loggers.STARTUP.debug("Multiple instances of DWR detected.");
                break;
            default:
                // We're third or more - leave it that there is no default
                break;
            }

            // Whatever, record the ServerContext against our servlet name
            String name = servletConfig.getServletName();
            contextMap.put(name, serverContext);
            Loggers.STARTUP.debug("Adding to contextMap, a serverContext called " + name);

            foundContexts++;
        }
    }

    /**
     * If there is only once instance of DWR defined in a ServletContext then
     * we can get at it using this method.
     * @return The one-and-only ServerContext or null if there are more than 1.
     */
    public static ServerContext getSingletonServerContext()
    {
        synchronized (contextMap)
        {
            return singletonServerContext;
        }
    }

    /**
     * Returns a Collection of all ServerContexts in which DWR has been defined.
     * @return Collection of ServerContexts.
     */
    public static Collection<ServerContext> getAllServerContexts()
    {
        Collection<ServerContext> reply = new ArrayList<ServerContext>();
        reply.addAll(contextMap.values());
        return Collections.unmodifiableCollection(reply);
    }

    /**
     * Get a list of all known Containers for the given {@link ServletContext}
     * @param servletContext The context in which {@link Container}s are stored.
     * @return a list of published {@link Container}s.
     */
    public static List<Container> getAllPublishedContainers(ServletContext servletContext)
    {
        List<Container> reply = new ArrayList<Container>();

        SerializableContainerListWrapper containers = (SerializableContainerListWrapper) servletContext.getAttribute(ATTRIBUTE_CONTAINER_LIST);
        if (containers != null)
        {
            reply.addAll(containers.getAll());
        }

        return reply;
    }

    /**
     * Create a bunch of debug information about a container
     * @param container The container to print debug information about
     */
    public static void debugConfig(Container container)
    {
        if (Loggers.STARTUP.isDebugEnabled())
        {
            // Container level debug
            Loggers.STARTUP.debug("Container");
            Loggers.STARTUP.debug("  Type: " + container.getClass().getName());
            for (String name : container.getBeanNames())
            {
                Object object = container.getBean(name);

                if (object instanceof String)
                {
                    Loggers.STARTUP.debug("  Param: " + name + " = " + object + " (" + object.getClass().getName() + ")");
                }
                else
                {
                    Loggers.STARTUP.debug("  Bean: " + name + " = " + object + " (" + object.getClass().getName() + ")");
                }
            }

            // AccessControl debugging
            AccessControl accessControl = container.getBean(AccessControl.class);
            Loggers.STARTUP.debug("AccessControl");
            Loggers.STARTUP.debug("  Type: " + accessControl.getClass().getName());

            // AjaxFilterManager debugging
            AjaxFilterManager ajaxFilterManager = container.getBean(AjaxFilterManager.class);
            Loggers.STARTUP.debug("AjaxFilterManager");
            Loggers.STARTUP.debug("  Type: " + ajaxFilterManager.getClass().getName());

            // ConverterManager debugging
            ConverterManager converterManager = container.getBean(ConverterManager.class);
            Loggers.STARTUP.debug("ConverterManager");
            Loggers.STARTUP.debug("  Type: " + converterManager.getClass().getName());

            // CreatorManager debugging
            CreatorManager creatorManager = container.getBean(CreatorManager.class);
            Loggers.STARTUP.debug("CreatorManager");
            Loggers.STARTUP.debug("  Type: " + creatorManager.getClass().getName());
            for (String creatorName : creatorManager.getCreatorNames(false))
            {
                Creator creator = creatorManager.getCreator(creatorName, false);
                Loggers.STARTUP.debug("  Creator: " + creatorName + " = " + creator + " (" + creator.getClass().getName() + ")");
            }

            // ModuleManager debugging
            ModuleManager moduleManager = container.getBean(ModuleManager.class);
            Loggers.STARTUP.debug("ModuleManager");
            Loggers.STARTUP.debug("  Type: " + moduleManager.getClass().getName());
        }
    }

    /**
     * We store a single ServerContext in the contextMap under this name.
     */
    private static ServerContext singletonServerContext;

    /**
     * To enable us to get at a complete list of all {@link ServerContext}s
     */
    private static final Map<String, ServerContext> contextMap = new HashMap<String, ServerContext>();

    /**
     * How many Contexts are there in this classloader that we need to
     * distinguish? Things will be a lot harder if there is more than 1.
     */
    private static int foundContexts = 0;

    /**
     * A small wrapper class to allow our stuff stored on ServletContext to
     * participate in serialization. Appservers like f ex WebLogic may issue
     * warnings if things are not serializable even though this is not required.
     * @author Mike Wilson
     */
    public static class SerializableContainerListWrapper implements Serializable
    {
        transient List<Container> list = null;

        public void add(Container container)
        {
            ensureCreated();
            list.add(container);
        }

        public Collection<Container> getAll()
        {
            ensureCreated();
            return list;
        }

        private void ensureCreated()
        {
            if (list == null)
            {
                list = new ArrayList<Container>();
            }
        }
    }
}
