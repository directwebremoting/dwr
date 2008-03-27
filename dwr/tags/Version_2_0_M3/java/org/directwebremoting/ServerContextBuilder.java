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
 * Class to enable us to access servlet parameters.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
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
