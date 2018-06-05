package org.directwebremoting.spring;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.directwebremoting.WebContextFactory.WebContextBuilder;
import org.directwebremoting.extend.Configurator;
import org.directwebremoting.impl.StartupUtil;
import org.directwebremoting.servlet.UrlProcessor;
import org.directwebremoting.spring.namespace.ConfigurationParser;
import org.directwebremoting.util.FakeServletConfig;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.util.Assert;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

/**
 * A Spring Controller that handles DWR requests. <br/>
 * Using this controller allows you to configure DWR entirely in Spring. You do not have to create
 * a separate <code>dwr.xml</code> configuration file when using this controller.
 *
 * <p>The following configuration provides a basic example of how too define this controller as a bean
 * in your application context.
 *
 * <code>
 * <pre>
   &lt;bean id="dwrController" class="org.directwebremoting.spring.DwrController">
      &lt;property name="configurators">
         &lt;list>
            &lt;ref bean="dwrConfiguration"/>
         &lt;/list>
      &lt;/property>
      &lt;property name="debug" value="true"/>
   &lt;/bean>

   &lt;bean id="dwrConfiguration" class="org.directwebremoting.spring.SpringConfigurator">
      &lt;property name="creators">
         &lt;map>
            &lt;entry key="<b>beanName</b>">
               &lt;bean class="org.directwebremoting.spring.CreatorConfig">
                  &lt;property name="creator">
                     &lt;bean class="org.directwebremoting.spring.BeanCreator">
                        &lt;property name="bean" ref="<b>BeanName</b>"/>
                     &lt;/bean>
                  &lt;/property>
               &lt;/bean>
            &lt;/entry>
         &lt;/map>
      &lt;/property>
   &lt;/bean>

   &lt;-- the bean you want to remote using DWR -->
   &lt;bean id="<b>beanName</b>" class="BeanName"/>
   </pre></code>
 *
 * In the near future we want to provide a DWR namespace for Spring, which should allow you to
 * something like the following:
 * <code>
 * <pre>
   &lt;dwr:configuration>
      &lt;debug/>
   &lt;/dwr:configuration>

   &lt;-- the bean you want to remote using DWR -->
   &lt;bean id="<b>beanName</b>" class="BeanName">
      &lt;dwr:remote javascript="<b>beanName</b>"/>
   &lt;/bean>
   </pre></code>
 * Which should be equivalent to the previous example. Please note that this is still work in progress
 * and is therefore subject to change.</p>
 *
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 * @author Bram Smeets
 */
public class DwrController extends AbstractController implements BeanNameAware, InitializingBean, DisposableBean, BeanFactoryAware
{
    /**
     * Is called by the Spring container to set the bean factory. <br/>
     * This bean factory is then used to obtain the global DWR configuration from. This global configuration is
     * optional as DWR will provide defaults where possible.
     * @see org.springframework.beans.factory.BeanFactoryAware#setBeanFactory(org.springframework.beans.factory.BeanFactory)
     */
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException
    {
        container = new SpringContainer();
        container.setBeanFactory(beanFactory);
    }

    /**
     * Sets whether DWR should be in debug mode (default is <code>false</code>). <br/>
     * This allows access to the debug pages provided by DWR under <code>/[WEBAPP]/dwr/</code>.
     * <b>NOTE</b>: make sure to not set this property to <code>true</code> in a production environment.
     * @param debug the indication of whether to start DWR in debug mode
     */
    public void setDebug(boolean debug)
    {
        this.debug = debug;
    }

    /**
     * Sets the configurators to apply to this controller. <br/>
     * The configurators are used to set up DWR correctly.
     * @param configurators the configurators to apply to this controller
     */
    public void setConfigurators(List<Configurator> configurators)
    {
        this.configurators = configurators;
    }

    /**
     * Sets whether the default DWR configuration should be included (default is <code>true</code>). <br/>
     * This default configuration contains all build-in creators and converters. You normally want this
     * default configuration to be included.
     * @param includeDefaultConfig the indication of whether to include the default configuration
     */
    public void setIncludeDefaultConfig(boolean includeDefaultConfig)
    {
        this.includeDefaultConfig = includeDefaultConfig;
    }

    /**
     * Is called by the Spring container after all properties have been set. <br/>
     * This method actually makes sure the container is correctly initialized and all configurators
     * are processed.
     * @throws Exception in case setting up fails
     * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
     */
    public void afterPropertiesSet() throws Exception
    {
        ApplicationContext parent = getApplicationContext().getParent();
        if (parent != null)
        {
            try
            {
                Object parentConfigurator = parent.getBean(ConfigurationParser.DEFAULT_SPRING_CONFIGURATOR_ID);
                if ((parentConfigurator != null) && (!configurators.contains(parentConfigurator)))
                {
                    configurators.add((Configurator) parentConfigurator);
                }
            } catch (RuntimeException rex)
            {
                if (log.isDebugEnabled())
                {
                    log.debug("Could not detect dwr configuration in parent context");
                }
            }
        }
        ServletContext servletContext = getServletContext();

        Assert.notNull(servletContext, "The servlet context has not been set on the controller");
        Assert.notNull(configurators, "The required 'configurators' property should be set");

        // Use a fake servlet config as Spring 1.x does not provide ServletConfigAware functionality
        // Now only allow Controller to be configured using parameters
        configParams.put("debug", "" + debug);

        servletConfig = new FakeServletConfig(name, servletContext, configParams);

        try
        {
            StartupUtil.logStartup(getClass().getSimpleName(), servletConfig);
            StartupUtil.setupDefaultContainer(container, servletConfig);
            webContextBuilder = container.getBean(WebContextBuilder.class);

            // The dwr.xml from within the JAR file.
            if (includeDefaultConfig)
            {
                StartupUtil.configureFromSystemDwrXml(container);
            }

            StartupUtil.configure(container, configurators);
        }
        catch (Exception ex)
        {
            log.fatal("init failed", ex);
            throw ex;
        }
        finally
        {
            if (webContextBuilder != null)
            {
                webContextBuilder.disengageThread();
            }
        }
    }

    /**
     * Handles all request to this controller. <br/>
     * It delegates to the <code>UrlProcessor</code> and also takes case of setting and unsetting of the
     * current <code>WebContext</code>.
     * @param request the request to handle
     * @param response the response to handle
     * @throws Exception in case handling of the request fails unexpectedly
     * @see org.directwebremoting.WebContext
     */
    @Override
    protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        try
        {
            // set up the web context and delegate to the processor
            webContextBuilder.engageThread(container, request, response);

            UrlProcessor processor = container.getBean(UrlProcessor.class);
            processor.handle(request, response);
        }
        finally
        {
            webContextBuilder.disengageThread();
        }

        // return null to inform the dispatcher servlet the request has already been handled
        return null;
    }

    /**
     * Is called by the Spring container to set the name of this bean.
     * @param name the name of this bean in the Spring container
     * @see BeanNameAware#setBeanName(String)
     */
    public void setBeanName(String name)
    {
        this.name = name;
    }

    /**
     * Additional parameters such as pollAndCometEnabled. For a full list see:
     * @param configParams the configParams to set
     */
    public void setConfigParams(Map<String, String> configParams)
    {
        Assert.notNull(configParams, "configParams cannot be null");
        this.configParams = configParams;
    }

    /* (non-Javadoc)
     * @see org.springframework.beans.factory.DisposableBean#destroy()
     */
    public void destroy() throws Exception
    {
        if (webContextBuilder != null)
        {
            webContextBuilder.engageThread(container, null, null);
        }
        container.destroy();
        if (webContextBuilder != null)
        {
            webContextBuilder.disengageThread();
        }
    }

    /**
     * How is this deployed in Spring
     */
    private String name;

    /**
     * Whether to allow access to the debug pages
     */
    private boolean debug = false;

    /**
     * The builder for the <code>WebContext</code> that keeps http objects local to a thread
     * @see org.directwebremoting.WebContext
     */
    protected WebContextBuilder webContextBuilder;

    /**
     * DWRs IoC container (that passes stuff to Spring in this case)
     */
    private SpringContainer container;

    /**
     * The fake ServletConfig
     */
    private ServletConfig servletConfig;

    /**
     * Do we prefix the list of Configurators with a default to read the system
     * dwr.xml file?
     */
    private boolean includeDefaultConfig = true;

    /**
     * What Configurators exist for us to configure ourselves.
     */
    private List<Configurator> configurators;

    /**
     * Additional parameters such as pollAndCometEnabled. For a full list see:
     */
    private Map<String, String> configParams = new HashMap<String, String>();

    /**
     * The log stream
     */
    private static final Log log = LogFactory.getLog(DwrController.class);
}
