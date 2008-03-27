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

import javax.servlet.ServletContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * An accessor for the current Hub
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class HubFactory
{
    /**
     * Accessor for the current Hub.
     * @return The current Hub.
     */
    public static Hub get()
    {
        if (builder == null)
        {
            log.warn("HubBuilder is null. This probably means that DWR has not initialized properly");
            return null;
        }

        return builder.get();
    }

    /**
     * Accessor for the current Hub in more complex setups.
     * For some setups DWR may not be able to discover the correct environment
     * (i.e. ServletContext), so we need to tell it. This generally happens if
     * you have DWR configured twice in a single context. Unless you are writing
     * code that someone else will configure, it is probably safe to use the
     * simpler {@link #get()} method.
     * @param ctx The servlet context to allow us to bootstrap
     * @return The current Hub.
     */
    public static Hub get(ServletContext ctx)
    {
        if (builder == null)
        {
            log.warn("HubBuilder is null. This probably means that DWR has not initialized properly");
            return null;
        }

        return builder.get(ctx);
    }

    /**
     * Internal method to allow us to get the HubBuilder from which we
     * will get Hub objects.
     * Do not call this method from outside of DWR.
     * @param builder The factory object (from DwrServlet)
     */
    public static void setHubBuilder(HubBuilder builder)
    {
        HubFactory.builder = builder;
    }

    /**
     * The HubBuilder from which we will get Hub objects
     */
    private static HubBuilder builder = null;

    /**
     * Class to enable us to access servlet parameters.
     */
    public interface HubBuilder
    {
        /**
         * Make the current webapp know what the current config/context is.
         * This method is only for use internally to DWR.
         * @param context The servlet context
         */
        void set(ServletContext context);

        /**
         * Accessor for the current Hub.
         * For some setups DWR may not be able to discover the correct
         * environment (i.e. ServletContext), so we need to tell it.
         * @param context The web application environment
         * @return The Hub that is associated with this web application
         */
        Hub get(ServletContext context);

        /**
         * Accessor for the current Hub
         * @return The Hub that is associated with this web application
         */
        Hub get();
    }

    /**
     * The log stream
     */
    private static final Log log = LogFactory.getLog(WebContextFactory.class);
}
