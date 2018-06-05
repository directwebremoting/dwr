package org.directwebremoting;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Accessor for the current {@link WebContext}.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class WebContextFactory
{
    /**
     * Accessor for the current {@link WebContext}.
     * @return The current WebContext or null if the current thread was not
     * started by DWR.
     */
    public static WebContext get()
    {
        if (builder == null)
        {
            return null;
        }

        return builder.get();
    }

    /**
     * The WebContextBuilder from which we will get WebContext objects
     */
    private static WebContextBuilder builder = null;

    /**
     * Internal method to allow us to get the WebContextBuilder from which we
     * will get WebContext objects.
     * Do NOT call this method from outside of DWR.
     */
    public static void attach(Container container)
    {
        WebContextFactory.builder = container.getBean(WebContextBuilder.class);
    }

    /**
     * Class to enable us to access servlet parameters.
     * This class is for internal use only.
     */
    public interface WebContextBuilder
    {
        /**
         * Accessor for the WebContext that is associated with this thread.
         * This method is only for use internally to DWR.
         * @see WebContextFactory#get()
         */
        WebContext get();

        /**
         * Make the current thread know what the current request is.
         * This method is only for use internally to DWR.
         * @param container The IoC container
         * @param request The incoming http request
         * @param response The outgoing http reply
         * @see #disengageThread()
         */
        void engageThread(Container container, HttpServletRequest request, HttpServletResponse response);

        /**
         * Make the current thread know what the current request is.
         * Uses an existing WebContext for example from another thread.
         * This method is only for use internally to DWR.
         * @see #disengageThread()
         */
        void engageThread(WebContext webContext);

        /**
         * Unset the current ExecutionContext
         * This method is only for use internally to DWR.
         * @see #engageThread(Container, HttpServletRequest, HttpServletResponse)
         */
        void disengageThread();
    }

    /**
     * The log stream
     */
    private static final Log log = LogFactory.getLog(WebContextFactory.class);
}
