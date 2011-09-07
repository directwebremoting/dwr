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

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;

/**
 * Accessor for the current ServerContext.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class ServerContextFactory
{
    /**
     * Accessor for the current ServerContext.
     * @param ctx The servlet context to allow us to bootstrap
     * @return The current ServerContext.
     */
    public static ServerContext get(ServletContext ctx)
    {
        if (builder == null)
        {
            return null;
        }

        return builder.get(ctx);
    }

    /**
     * Internal method to allow us to get the ServerContextBuilder from which we
     * will get ServerContext objects.
     * Do not call this method from outside of DWR.
     * @param builder The factory object (from DwrServlet)
     */
    public static void setServerContextBuilder(ServerContextBuilder builder)
    {
        ServerContextFactory.builder = builder;
    }

    /**
     * The ServerContextBuilder from which we will get ServerContext objects
     */
    private static ServerContextBuilder builder;

    /**
     * Class to enable us to access servlet parameters.
     */
    public interface ServerContextBuilder
    {
        /**
         * Make the current webapp know what the current config/context is.
         * This method is only for use internally to DWR.
         * @param config The servlet configuration
         * @param context The servlet context
         * @param container The IoC container
         */
        void set(ServletConfig config, ServletContext context, Container container);

        /**
         * Accessor for the current ServerContext
         * @param context The web application environment
         * @return The ServerContext that is associated with this web application
         */
        ServerContext get(ServletContext context);
    }
}
