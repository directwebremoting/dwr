package org.directwebremoting.servlet;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.directwebremoting.Container;
import org.directwebremoting.extend.Handler;
import org.directwebremoting.extend.InitializingBean;
import org.directwebremoting.impl.AccessLogLevel;
import org.directwebremoting.util.LocalUtil;
import org.directwebremoting.util.Loggers;

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
public class UrlProcessor implements Handler, InitializingBean
{
    /* (non-Javadoc)
     * @see org.directwebremoting.InitializingBean#afterPropertiesSet(Container)
     */
    public void afterContainerSetup(Container container)
    {
        // Set up urlMapping
        Collection<String> beanNames = container.getBeanNames();
        for (String name : beanNames)
        {
            if (name.startsWith(PathConstants.PATH_PREFIX))
            {
                Object bean = container.getBean(name);

                if (bean instanceof Handler)
                {
                    Handler handler = (Handler) bean;
                    urlMapping.put(name.substring(PathConstants.PATH_PREFIX.length()), handler);
                }
                else if (bean instanceof String)
                {
                    log.debug("Probably not an issue: the " + name + " (" + bean + ") Handler is not available. This is only a problem if you wanted to use it.");
                }
                else
                {
                    log.error("Discarding non Handler for " + name + " (" + bean.getClass().getName() + " is not an instance of " + Handler.class.getName() + ")");
                }
            }
        }

        // Set up responseHandlerMapping
        for(Handler handler : urlMapping.values())
        {
            ResponseHandler responseHandler = findConfiguredResponseHandler(handler.getClass(), container);
            if (responseHandler == null)
            {
                log.warn("Missing ResponseHandler for " + handler.getClass().getName() + ".");
            }
            responseHandlerMapping.put(handler, responseHandler);
        }

        // Find contextPath
        ServletContext servletContext = container.getBean(ServletContext.class);
        // This will fail (i.e. return null with servlet 2.4, but we patch up
        // in the handle method.
        contextPath = LocalUtil.getProperty(servletContext, "contextPath", String.class);
    }

    /**
     * Walk the Handlers inheritance chain to look for a configured ResponseHandler
     * @param handlerClass
     * @param container
     * @return a ResponseHandler or null
     */
    private ResponseHandler findConfiguredResponseHandler(Class<?> handlerClass, Container container)
    {
        if (handlerClass == null || !Handler.class.isAssignableFrom(handlerClass))
        {
            return null;
        }

        // Try to map a ResponseHandler for the current Handler class
        String key = PathConstants.RESPONSE_PREFIX + handlerClass.getName();
        Object bean = container.getBean(key);
        if (bean != null)
        {
            if (bean instanceof ResponseHandler)
            {
                return (ResponseHandler) bean;
            }
            else
            {
                log.error("Ignoring non ResponseHandler for " + key + " (" + bean.getClass().getName() + " is not an instance of " + ResponseHandler.class.getName() + ")");
            }
        }

        // Continue looking in parent classes and interfaces
        ResponseHandler responseHandler;
        responseHandler = findConfiguredResponseHandler(handlerClass.getSuperclass(), container);
        if (responseHandler != null)
        {
            return responseHandler;
        }
        for(Class<?> intfc : handlerClass.getInterfaces())
        {
            responseHandler = findConfiguredResponseHandler(intfc, container);
            if (responseHandler != null)
            {
                return responseHandler;
            }
        }

        return null;
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.Handler#handle(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    public void handle(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        try
        {
            String pathInfo = request.getPathInfo();
            String requestContextPath = request.getContextPath();

            // Log the call details if the accessLogLevel is call.
            if (AccessLogLevel.getValue(this.accessLogLevel, debug).hierarchy() == 0)
            {
                Loggers.ACCESS.info("Incoming request: " + request.getRequestURI());
            }

            // Patching up for missing servlet 2.5 servletContext.getContextPath
            if (contextPath == null)
            {
                contextPath = requestContextPath;
            }
            if (pathInfo == null || pathInfo.length() == 0 || "/".equals(pathInfo))
            {
                response.sendRedirect(requestContextPath + request.getServletPath() + indexHandlerUrl);
            }
            else
            {
                // Loop through all the known URLs
                for (Entry<String, Handler> entry : urlMapping.entrySet())
                {
                    String url = entry.getKey();
                    // If this URL matches, call the handler
                    if (pathInfo.startsWith(url))
                    {
                        Handler handler = entry.getValue();
                        handle(handler, request, response);
                        return;
                    }
                }
                handle(notFoundHandler, request, response);
            }
        }
        catch (SecurityException se) {
            // We don't want to give the client any information about the security error, handle it with a 404.
            log.error("Security Exception: ", se);
            handle(notFoundHandler, request, response);
        }
        catch (Exception ex)
        {
            exceptionHandler.setException(ex);
            handle(exceptionHandler, request, response);
        }
    }

    private void handle(Handler handler, HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        ResponseHandler responseHandler = responseHandlerMapping.get(handler);
        if (responseHandler != null)
        {
            responseHandler.handle(handler, request, response);
        }
        handler.handle(request, response);
    }

    /**
     * The contextPath cached from the last HTTP servlet request
     * @return the contextPath
     */
    public String getContextPath()
    {
        return contextPath;
    }

    /**
     * The URL for the {@link IndexHandler}
     * @param indexHandlerUrl the indexHandlerUrl to set
     */
    public void setIndexHandlerUrl(String indexHandlerUrl)
    {
        this.indexHandlerUrl = indexHandlerUrl;
    }

    /**
     * When and what should we log? Options are (specified in the DWR servlet's init-params):
     * 1) call (start of call + successful return values).
     * 2) exception (checked) - default for debug.
     * 3) runtimeexception (unchecked).
     * 4) error - default for production.
     * 5) off.
     */
    public void setAccessLogLevel(String accessLogLevel)
    {
        this.accessLogLevel = accessLogLevel;
    }

    /**
     * Set the debug status
     * @param debug The new debug setting
     */
    public void setDebug(boolean debug)
    {
        this.debug = debug;
    }

    /**
     * The URL for the {@link IndexHandler}
     */
    protected String indexHandlerUrl;

    /**
     * The mapping of URLs to {@link Handler}s
     */
    protected Map<String, Handler> urlMapping = new HashMap<String, Handler>();

    /**
     * The mapping of Handlers to ResponseHandlers
     */
    protected Map<Handler, ResponseHandler> responseHandlerMapping = new HashMap<Handler, ResponseHandler>();

    /**
     * The default if we have no other action (HTTP-404)
     */
    protected Handler notFoundHandler = new NotFoundHandler();

    /**
     * If execution fails, we do this (HTTP-501)
     */
    protected ExceptionHandler exceptionHandler = new ExceptionHandler();

    /**
     * The contextPath cached from the last HTTP servlet request
     */
    protected String contextPath = null;

    /**
     * Are we in debug-mode and therefore more helpful at the expense of security?
     */
    private boolean debug = false;

    /**
     * When and what should we log? Options are (specified in the DWR servlet's init-params):
     * 1) call (start of call + successful return values).
     * 2) exception (checked) - default for debug.
     * 3) runtimeexception (unchecked).
     * 4) error - default for production.
     * 5) off.
     */
    protected String accessLogLevel = null;

    /**
     * The log stream
     */
    private static final Log log = LogFactory.getLog(UrlProcessor.class);
}
