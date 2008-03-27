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

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Class to enable us to access servlet parameters.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public interface WebContextBuilder
{
    /**
     * Make the current thread know what the current request is.
     * This method is only for use internally to DWR.
     * @param request The incoming http request
     * @param response The outgoing http reply
     * @param config The servlet configuration
     * @param context The servlet context
     * @param container The IoC container
     * @see WebContextBuilder#unset()
     */
    void set(HttpServletRequest request, HttpServletResponse response, ServletConfig config, ServletContext context, Container container);

    /**
     * @return The WebContext that is associated with this thread
     */
    WebContext get();

    /**
     * Unset the current ExecutionContext
     * This method is only for use internally to DWR.
     * @see WebContextBuilder#set(HttpServletRequest, HttpServletResponse, ServletConfig, ServletContext, Container)
     */
    void unset();
}
