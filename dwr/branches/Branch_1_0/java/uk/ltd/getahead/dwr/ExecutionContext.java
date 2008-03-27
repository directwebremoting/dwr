package uk.ltd.getahead.dwr;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.Properties;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import uk.ltd.getahead.dwr.util.Logger;
import uk.ltd.getahead.dwr.util.SwallowingHttpServletResponse;

/**
 * Class to enable us to access servlet parameters.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class ExecutionContext
{
    /**
     * Prevent instansiation outside of setExecutionContext()
     * @param newRequest The incoming http request
     * @param newResponse The outgoing http reply
     * @param newConfig The servlet configuration
     * @param newContext The servlet context
     * @see ExecutionContext#setExecutionContext(HttpServletRequest, HttpServletResponse, ServletConfig, ServletContext)
     */
    protected void init(HttpServletRequest newRequest, HttpServletResponse newResponse, ServletConfig newConfig, ServletContext newContext)
    {
        this.request = newRequest;
        this.response = newResponse;
        this.config = newConfig;
        this.context = newContext;
    }

    /**
     * Returns the current session associated with this request,
     * or if the request does not have a session, creates one.
     * @return Returns the http session.
     * @see HttpServletRequest#getSession()
     */
    public HttpSession getSession()
    {
        return request.getSession();
    }

    /**
     * Returns the current HttpSession associated with this request or, if
     * there is no current session and create is true, returns a new session.
     * If create is false and the request has no valid HttpSession, this method
     * returns null.
     * @param create false to return null if there's no current session
     * @return the session associated with this request
     * @see HttpServletRequest#getSession(boolean)
     */
    public HttpSession getSession(boolean create)
    {
        return request.getSession(create);
    }

    /**
     * Accessor for the servlet config.
     * @return Returns the config.
     */
    public ServletConfig getServletConfig()
    {
        return config;
    }

    /**
     * Returns the ServletContext to which this session belongs.
     * @return The servlet context information.
     */
    public ServletContext getServletContext()
    {
        return context;
    }

    /**
     * Accessor for the http request information.
     * @return Returns the request.
     */
    public HttpServletRequest getHttpServletRequest()
    {
        return request;
    }

    /**
     * Accessor for the http response bean.
     * @return Returns the response.
     */
    public HttpServletResponse getHttpServletResponse()
    {
        return response;
    }

    /**
     * Forward a request to a given URL and catch the data written to it
     * @param url The URL to forward to
     * @return The text that results from forwarding to the given URL
     * @throws IOException if the target resource throws this exception
     * @throws ServletException if the target resource throws this exception
     * @throws IllegalStateException if the response was already committed
     */
    public String forwardToString(String url) throws ServletException, IOException
    {
        StringWriter sout = new StringWriter();
        StringBuffer buffer = sout.getBuffer();

        HttpServletResponse fakeResponse = new SwallowingHttpServletResponse(getHttpServletResponse(), sout);

        getServletContext().getRequestDispatcher(url).forward(getHttpServletRequest(), fakeResponse);

        return buffer.toString();
    }

    /**
     * Fish the version number out of the dwr.properties file.
     * @return The current version number.
     */
    public String getVersion()
    {
        synchronized (propLock)
        {
            if (props == null)
            {
                loadProperties();
            }

            return props.getProperty(KEY_VERSION);
        }
    }

    /**
     * Fish the version number out of the dwr.properties file.
     * @return The current version number.
     */
    protected String getSourceControlInfo()
    {
        synchronized (propLock)
        {
            if (props == null)
            {
                loadProperties();
            }

            return props.getProperty(KEY_SCCINFO);
        }
    }

    /**
     * Load the properties from the internal properties file.
     */
    private void loadProperties()
    {
        synchronized (propLock)
        {
            props = new Properties();

            try
            {
                InputStream in = getClass().getResourceAsStream(FILENAME_VERSION);
                props.load(in);
            }
            catch (Exception ex)
            {
                props.put(KEY_VERSION, VALUE_UNKNOWN);
                props.put(KEY_SCCINFO, VALUE_UNKNOWN);
                props.put(KEY_ERROR, ex.toString());
            }
        }
    }

    private HttpServletRequest request = null;
    private HttpServletResponse response = null;
    private ServletConfig config = null;
    private ServletContext context = null;

    private static final String FILENAME_VERSION = "/dwr-version.properties"; //$NON-NLS-1$
    private static final String KEY_VERSION = "version"; //$NON-NLS-1$
    private static final String KEY_SCCINFO = "scc-info"; //$NON-NLS-1$
    private static final String KEY_ERROR = "error"; //$NON-NLS-1$
    private static final String VALUE_UNKNOWN = "unknown"; //$NON-NLS-1$

    private static Properties props = null;
    private static final Object propLock = new Object();

    /**
     * Accessor for the current ExecutionContext.
     * @return The current ExecutionContext
     */
    public static ExecutionContext get()
    {
        return (ExecutionContext) user.get();
    }

    /**
     * Make the current thread know what the current request is.
     * This method is only for use internally to DWR.
     * @param request The incoming http request
     * @param response The outgoing http reply
     * @param config The servlet configuration
     * @param context The servlet context
     * @see ExecutionContext#unset()
     */
    public static void setExecutionContext(HttpServletRequest request, HttpServletResponse response, ServletConfig config, ServletContext context)
    {
        try
        {
            ExecutionContext ec = (ExecutionContext) implementation.newInstance();
            ec.init(request, response, config, context);
            user.set(ec);
        }
        catch (Exception ex)
        {
            log.fatal("Failed to create an ExecutionContext", ex); //$NON-NLS-1$
        }
    }

    /**
     * Unset the current ExecutionContext
     * This method is only for use internally to DWR.
     * @see ExecutionContext#setExecutionContext(HttpServletRequest, HttpServletResponse, ServletConfig, ServletContext)
     */
    public static void unset()
    {
        user.set(null);
    }

    /**
     * Alter the implementation of ExecutionContext to some child class
     * @param singletonType The new implementation class
     */
    protected static void setImplementation(Class singletonType)
    {
        implementation = singletonType;
    }

    private static ThreadLocal user = new ThreadLocal();

    private static Class implementation = ExecutionContext.class;

    /**
     * The log stream
     */
    private static final Logger log = Logger.getLogger(ExecutionContext.class);
}
