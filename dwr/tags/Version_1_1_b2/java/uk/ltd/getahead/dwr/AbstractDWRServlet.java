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
package uk.ltd.getahead.dwr;

import uk.ltd.getahead.dwr.util.Logger;
import uk.ltd.getahead.dwr.util.ServletLoggingOutput;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

import java.io.InputStream;
import java.io.IOException;

/**
 * This is abstract servlet that all DWR servlets should extend.
 * <p>There are 5 things to do, in the order that you come across them:</p>
 * <ul>
 * <li>The index test page that points at the classes</li>
 * <li>The class test page that lets you execute methods</li>
 * <li>The interface javascript that uses the engine to send requests</li>
 * <li>The engine javascript to form the iframe request and process replies</li>
 * <li>The exec 'page' that executes the method and returns data to the iframe</li>
 * </ul>
 * </p>
 * <p>Each subclass should override the two abstract methods:
 * <ul>
 * <li>getContainer(ServletConfig) - the implementation should provide a specific
 * implementation of the {@link Container} interface</li>
 * <li>configure(ServletConfig, Configuration) - the subclass can perform additional
 * configuration at this point.</li>
 * </ul>
 * </p>
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 * @author Bram Smeets
 */
public abstract class AbstractDWRServlet extends HttpServlet
{
    /**
     * Concrete implementations of a DwrServlet will need to provide the system
     * with a pre configured Container from which we can get the components that
     * make up DWR.
     * @param config the servlet configuration to obtain the init parameters from
     * @return the default container implementation
     * @throws ServletException in case adding of a parameter fails
     */
    protected abstract Container getContainer(ServletConfig config) throws ServletException;

    /**
     * Performs additional configuration based on the specified init parameters.
     * It adds additional configuration based on the configuration files
     * specified by init parameters. In case no confuration file has been
     * specified, the default DWR configuration is loaded.
     * @param config the servlet configuration to obtain the init parameters from
     * @param configuration the configuration to add configuration to
     * @throws ServletException in case the additional configuration fails
     */
    protected abstract void configure(ServletConfig config, Configuration configuration) throws ServletException;

    /* (non-Javadoc)
    * @see javax.servlet.GenericServlet#init(javax.servlet.ServletConfig)
    */
    public void init(ServletConfig config) throws ServletException
    {
        super.init(config);

        try
        {
            ServletLoggingOutput.setExecutionContext(this);

            // How much logging do we do?
            String logLevel = config.getInitParameter(INIT_LOGLEVEL);
            if (logLevel != null)
            {
                ServletLoggingOutput.setLevel(logLevel);
            }

            // call the abstract method to obtain the container
            container = getContainer(config);

            // Now we have set the implementations we can set the WebContext up
            builder = (WebContextBuilder) container.getBean(WebContextBuilder.class.getName());
            WebContextFactory.setWebContextBuilder(builder);

            // And we lace it with the context so far to help init go smoothly
            builder.set(null, null, getServletConfig(), getServletContext(), container);

            // Load the system config file
            Configuration configuration = (Configuration) container.getBean(Configuration.class.getName());
            InputStream in = getClass().getResourceAsStream(FILE_DWR_XML);
            log.info("retrieved system configuration file: " + in); //$NON-NLS-1$

            try
            {
                configuration.addConfig(in);
            }
            catch (Exception ex)
            {
                log.fatal("Failed to load system config file from dwr.jar", ex); //$NON-NLS-1$
                throw new ServletException(Messages.getString("DWRServlet.SystemConfigError"), ex); //$NON-NLS-1$
            }

            // call the abstract method to perform additional configuration
            configure(config, configuration);

            // Finally the processor that handles doGet and doPost
            processor = (Processor) container.getBean(Processor.class.getName());
        }
        catch (ServletException ex)
        {
            throw ex;
        }
        catch (Exception ex)
        {
            log.fatal("init failed", ex); //$NON-NLS-1$
            throw new ServletException(ex);
        }
        finally
        {
            if (builder != null)
            {
                builder.unset();
            }

            ServletLoggingOutput.unsetExecutionContext();
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
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException
    {
        try
        {
            builder.set(req, resp, getServletConfig(), getServletContext(), container);
            ServletLoggingOutput.setExecutionContext(this);

            processor.handle(req, resp);
        }
        finally
        {
            builder.unset();
            ServletLoggingOutput.unsetExecutionContext();
        }
    }

    /**
     * Load a DWR config file.
     * @param configFile the config file to read
     * @param configuration The current configuration loader
     * @throws ServletException If the extra checking of the config file fails
     */
    protected void readFile(String configFile, Configuration configuration) throws ServletException
    {
        try
        {
            InputStream in = getServletContext().getResourceAsStream(configFile);
            if (in == null)
            {
                log.error("Missing config file: " + configFile); //$NON-NLS-1$
            }
            else
            {
                configuration.addConfig(in);
            }
        }
        catch (Exception ex)
        {
            throw new ServletException(Messages.getString("DWRServlet.ConfigError", configFile), ex); //$NON-NLS-1$
        }
    }

    /**
     * The processor will actually handle the http requests
     */
    protected Processor processor;

    /**
     * The WebContext that keeps http objects local to a thread
     */
    protected WebContextBuilder builder;

    /**
     * The IoC container
     */
    protected Container container;

    /**
     * The package name because people need to load resources in this package.
     */
    public static final String PACKAGE = "/uk/ltd/getahead/dwr"; //$NON-NLS-1$

    /**
     * Init parameter: Skip reading the default config file if none are specified.
     */
    protected static final String INIT_SKIP_DEFAULT = "skipDefaultConfig"; //$NON-NLS-1$

    /**
     * Init parameter: Set a dwr.xml config file.
     * This is only a prefix since we might have more than 1 config file.
     */
    protected static final String INIT_CONFIG = "config"; //$NON-NLS-1$

    /**
     * Init parameter: If we are doing Servlet.log logging, to what level?
     */
    protected static final String INIT_LOGLEVEL = "logLevel"; //$NON-NLS-1$

    /**
     * The system dwr.xml resource name
     */
    protected static final String FILE_DWR_XML = PACKAGE + "/dwr.xml"; //$NON-NLS-1$

    /**
     * The default dwr.xml file path
     */
    protected static final String DEFAULT_DWR_XML = "/WEB-INF/dwr.xml"; //$NON-NLS-1$

    /**
     * The log stream
     */
    private static final Logger log = Logger.getLogger(DWRServlet.class);
}
