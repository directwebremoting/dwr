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
            log.warn("Missing WebContextBuilder. Is DWR setup properly?");
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
