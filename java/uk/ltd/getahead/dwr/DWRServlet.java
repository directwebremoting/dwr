package uk.ltd.getahead.dwr;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

        // How much logging do we do?
        String logLevel = config.getInitParameter(INIT_LOGLEVEL);
        if (logLevel != null)
        {
            ServletLoggingOutput.setLevel(logLevel);
        }

        // Load the factory with implementation information
        Factory factory = new Factory();
        factory.setImplementation(AccessControl.class.getName(), "uk.ltd.getahead.dwr.impl.DefaultAccessControl"); //$NON-NLS-1$
        factory.setImplementation(Configuration.class.getName(), "uk.ltd.getahead.dwr.impl.DefaultConfiguration"); //$NON-NLS-1$
        factory.setImplementation(ConverterManager.class.getName(), "uk.ltd.getahead.dwr.impl.DefaultConverterManager"); //$NON-NLS-1$
        factory.setImplementation(CreatorManager.class.getName(), "uk.ltd.getahead.dwr.impl.DefaultCreatorManager"); //$NON-NLS-1$
        factory.setImplementation(Processor.class.getName(), "uk.ltd.getahead.dwr.impl.DefaultProcessor"); //$NON-NLS-1$

        Enumeration en = config.getInitParameterNames();
        while (en.hasMoreElements())
        {
            String name = (String) en.nextElement();
            if (name.startsWith(INIT_IMPL_PREFIX))
            {
                // if the init param starts with "config" then try to load it
                String value = config.getInitParameter(name);

                try
                {
                    factory.setImplementation(name, value);
                }
                catch (Exception ex)
                {
                    log.fatal("Invalid implmentation parameter: " + name + "=" + value, ex); //$NON-NLS-1$ //$NON-NLS-2$
                    throw new ServletException(Messages.getString("DWRServlet.ExecutionContextInit", name, value), ex); //$NON-NLS-1$
                }
            }
        }
        factory.configurationFinished();

        // Configure the ExecutionContext
        // Maybe we want to override the ExecutionContext
        // This code is un-documented, and experimental. We'll keep it if it
        // turns out to be useful.
        String execCxName = config.getInitParameter(ExecutionContext.class.getName());
        if (execCxName != null)
        {
            try
            {
                Class type = Class.forName(execCxName);
                ExecutionContext.setImplementation(type);
            }
            catch (Exception ex)
            {
                log.fatal("Invalid executionContext parameter", ex); //$NON-NLS-1$
                throw new ServletException(Messages.getString("DWRServlet.ExecutionContextInit", execCxName, ex), ex); //$NON-NLS-1$
            }
        }

        // Are we in debug mode?
        String debugStr = config.getInitParameter(INIT_DEBUG);
        boolean debug = Boolean.valueOf(debugStr).booleanValue();
        CreatorManager creatorManager = (CreatorManager) factory.getBean(CreatorManager.class);
        creatorManager.setDebug(debug);

        // Load the system config file
        Configuration configuration = (Configuration) factory.getBean(Configuration.class);
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
            readFile(DEFAULT_DWR_XML, configuration);
        }

        // Finally the processor that handles doGet and doPost
        processor = (Processor) factory.getBean(Processor.class);
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
            ExecutionContext.setExecutionContext(req, resp, getServletConfig(), getServletContext());
            ServletLoggingOutput.setExecutionContext(this);

            processor.handle(req, resp);
        }
        finally
        {
            ExecutionContext.unset();
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
                throw new ServletException(Messages.getString("DWRServlet.MissingFile", configFile)); //$NON-NLS-1$
            }

            configuration.addConfig(in);
        }
        catch (Exception ex)
        {
            throw new ServletException(Messages.getString("DWRServlet.ConfigError", configFile), ex); //$NON-NLS-1$
        }
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    public int hashCode()
    {
        log.error("Warning: DWRServlet has not been designed for use in collections."); //$NON-NLS-1$
        return super.hashCode();
    }

    protected Processor processor;

    /**
     * The package name because people need to load resources in this package.  
     */
    public static final String PACKAGE = "/uk/ltd/getahead/dwr/"; //$NON-NLS-1$

    /**
     * Init parameter: Set a dwr.xml config file.
     * This is only a prefix since we might have more than 1 config file.
     */
    protected static final String INIT_CONFIG = "config"; //$NON-NLS-1$

    /**
     * Init parameter: Are we in debug mode?
     */
    protected static final String INIT_DEBUG = "debug"; //$NON-NLS-1$

    /**
     * Init parameter: If we are doing Servlet.log logging, to what level?
     */
    protected static final String INIT_LOGLEVEL = "logLevel"; //$NON-NLS-1$

    /**
     * Init parameter: Allows us to override a default implementation.
     */
    protected static final String INIT_IMPL_PREFIX = "uk.ltd.getahead.dwr"; //$NON-NLS-1$

    /**
     * The system dwr.xml resource name
     */
    protected static final String FILE_DWR_XML = "dwr.xml"; //$NON-NLS-1$

    /**
     * The default dwr.xml file path
     */
    protected static final String DEFAULT_DWR_XML = "/WEB-INF/dwr.xml"; //$NON-NLS-1$

    /**
     * The log stream
     */
    private static final Logger log = Logger.getLogger(DWRServlet.class);
}
