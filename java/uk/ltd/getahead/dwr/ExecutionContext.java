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

import uk.ltd.getahead.dwr.util.SwallowingHttpServletResponse;

/**
 * Class to enable us to access servlet parameters.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public final class ExecutionContext
{
    /**
     * Prevent instansiation outside of setExecutionContext()
     * @param request The incoming http request
     * @param response The outgoing http reply
     * @param config The servlet configuration
     * @param context The servlet context
     * @see ExecutionContext#setExecutionContext(HttpServletRequest, HttpServletResponse, ServletConfig, ServletContext)
     */
    private ExecutionContext(HttpServletRequest request, HttpServletResponse response, ServletConfig config, ServletContext context)
    {
        this.request = request;
        this.response = response;
        this.config = config;
        this.context = context;
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
    public String forwardToString(final String url) throws ServletException, IOException
    {
        final StringWriter sout = new StringWriter();
        final StringBuffer buffer = sout.getBuffer();

        HttpServletResponse fakeResponse = new SwallowingHttpServletResponse(getHttpServletResponse(), sout, url);

        getServletContext().getRequestDispatcher(url).forward(getHttpServletRequest(), fakeResponse);

        return buffer.toString();
    }

    /**
     * Fish the version number out of the dwr.properties file.
     * @return The current version number.
     */
    public String getVersion()
    {
        if (props == null)
        {
            loadProperties();
        }

        return props.getProperty(KEY_VERSION);
    }

    /**
     * Fish the version number out of the dwr.properties file.
     * @return The current version number.
     */
    protected String getSourceControlInfo()
    {
        if (props == null)
        {
            loadProperties();
        }

        return props.getProperty(KEY_SCCINFO);
    }

    /**
     * It's only a matter of time before someone starts to point out the race
     * condition here, so I might as well point out that we don't care if the
     * properties file gets loaded twice.
     */
    private synchronized void loadProperties()
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

    private static final String FILENAME_VERSION = "/dwr-version.properties"; //$NON-NLS-1$
    private static final String KEY_VERSION = "version"; //$NON-NLS-1$
    private static final String KEY_SCCINFO = "scc-info"; //$NON-NLS-1$
    private static final String KEY_ERROR = "error"; //$NON-NLS-1$
    private static final String VALUE_UNKNOWN = "unknown"; //$NON-NLS-1$

    private final HttpServletRequest request;
    private final HttpServletResponse response;
    private final ServletConfig config;
    private final ServletContext context;
    private Properties props = null;

    private static ThreadLocal user = new ThreadLocal();

    /**
     * This method will be removed in the next release
     * @return The current ExecutionContext
     * @deprecated Use ExecutionContext.get();
     * @see ExecutionContext#get()
     */
    public static ExecutionContext getExecutionContext()
    {
        return get();
    }

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
    protected static void setExecutionContext(HttpServletRequest request, HttpServletResponse response, ServletConfig config, ServletContext context)
    {
        user.set(new ExecutionContext(request, response, config, context));
    }

    /**
     * Unset the current ExecutionContext
     * This method is only for use internally to DWR.
     * @see ExecutionContext#setExecutionContext(HttpServletRequest, HttpServletResponse, ServletConfig, ServletContext)
     */
    protected static void unset()
    {
        user.set(null);
    }
}
