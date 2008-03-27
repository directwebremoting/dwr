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

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import uk.ltd.getahead.dwr.impl.DefaultContainer;
import uk.ltd.getahead.dwr.util.Logger;
import uk.ltd.getahead.dwr.util.ServletLoggingOutput;

/**
 * This is the main servlet that handles all the requests to DWR.
 * <p>It is on the large side because it can't use technologies like JSPs etc
 * since it all needs to be deployed in a single jar file, and while it might be
 * possible to integrate Velocity or similar I think simplicity is more
 * important, and there are only 2 real pages both script heavy in this servlet
 * anyway.</p>
 * <p>There are 5 things to do, in the order that you come across them:</p>
 * <ul>
 * <li>The index test page that points at the classes</li>
 * <li>The class test page that lets you execute methods</li>
 * <li>The interface javascript that uses the engine to send requests</li>
 * <li>The engine javascript to form the iframe request and process replies</li>
 * <li>The exec 'page' that executes the method and returns data to the iframe</li>
 * </ul>
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class DWRServlet extends HttpServlet
{
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

            // Load the factory with implementation information
            DefaultContainer factory = new DefaultContainer();
            factory.addParameter(AccessControl.class.getName(), "uk.ltd.getahead.dwr.impl.DefaultAccessControl"); //$NON-NLS-1$
            factory.addParameter(Configuration.class.getName(), "uk.ltd.getahead.dwr.impl.DefaultConfiguration"); //$NON-NLS-1$
            factory.addParameter(ConverterManager.class.getName(), "uk.ltd.getahead.dwr.impl.DefaultConverterManager"); //$NON-NLS-1$
            factory.addParameter(CreatorManager.class.getName(), "uk.ltd.getahead.dwr.impl.DefaultCreatorManager"); //$NON-NLS-1$
            factory.addParameter(Processor.class.getName(), "uk.ltd.getahead.dwr.impl.DefaultProcessor"); //$NON-NLS-1$
            factory.addParameter(WebContextBuilder.class.getName(), "uk.ltd.getahead.dwr.impl.DefaultWebContextBuilder"); //$NON-NLS-1$

            factory.addParameter("index", "uk.ltd.getahead.dwr.impl.DefaultIndexProcessor"); //$NON-NLS-1$ //$NON-NLS-2$
            factory.addParameter("test", "uk.ltd.getahead.dwr.impl.DefaultTestProcessor"); //$NON-NLS-1$ //$NON-NLS-2$
            factory.addParameter("interface", "uk.ltd.getahead.dwr.impl.DefaultInterfaceProcessor"); //$NON-NLS-1$ //$NON-NLS-2$
            factory.addParameter("exec", "uk.ltd.getahead.dwr.impl.DefaultExecProcessor"); //$NON-NLS-1$ //$NON-NLS-2$
            factory.addParameter("file", "uk.ltd.getahead.dwr.impl.FileProcessor"); //$NON-NLS-1$ //$NON-NLS-2$

            factory.addParameter("debug", "false"); //$NON-NLS-1$ //$NON-NLS-2$
            factory.addParameter("allowImpossibleTests", "false"); //$NON-NLS-1$ //$NON-NLS-2$

            factory.addParameter("scriptCompressed", "true"); //$NON-NLS-1$ //$NON-NLS-2$

            Enumeration en = config.getInitParameterNames();
            while (en.hasMoreElements())
            {
                String name = (String) en.nextElement();
                String value = config.getInitParameter(name);
                factory.addParameter(name, value);
            }
            factory.configurationFinished();
            container = factory;

            // Now we have set the implementations we can set the WebContext up
            builder = (WebContextBuilder) container.getBean(WebContextBuilder.class.getName());
            WebContextFactory.setWebContextBuilder(builder);

            // And we lace it with the context so far to help init go smoothly
            builder.set(null, null, getServletConfig(), getServletContext(), container);

            // Load the system config file
            Configuration configuration = (Configuration) container.getBean(Configuration.class.getName());
            InputStream in = getClass().getResourceAsStream(FILE_DWR_XML);
            try
            {
                configuration.addConfig(in);
            }
            catch (Exception ex)
            {
                log.fatal("Failed to load system config file from dwr.jar", ex); //$NON-NLS-1$
                throw new ServletException(Messages.getString("DWRServlet.SystemConfigError"), ex); //$NON-NLS-1$
            }

            // Find all the init params
            en = config.getInitParameterNames();
            boolean foundConfig = false;

            // Loop through the ones that do exist
            while (en.hasMoreElements())
            {
                String name = (String) en.nextElement();
                if (name.startsWith(INIT_CONFIG))
                {
                    foundConfig = true;

                    // if the init param starts with "config" then try to load it
                    String configFile = config.getInitParameter(name);
                    readFile(configFile, configuration);
                }
            }

            // If there are none then use the default name
            if (!foundConfig)
            {
                String skip = config.getInitParameter(INIT_SKIP_DEFAULT);
                if (!Boolean.valueOf(skip).booleanValue())
                {
                    readFile(DEFAULT_DWR_XML, configuration);
                }
            }

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
    protected static final String FILE_DWR_XML = "/uk/ltd/getahead/dwr/dwr.xml"; //$NON-NLS-1$

    /**
     * The default dwr.xml file path
     */
    protected static final String DEFAULT_DWR_XML = "/WEB-INF/dwr.xml"; //$NON-NLS-1$

    /**
     * The log stream
     */
    private static final Logger log = Logger.getLogger(DWRServlet.class);
}
