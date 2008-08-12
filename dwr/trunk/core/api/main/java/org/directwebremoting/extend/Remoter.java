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
package org.directwebremoting.extend;

/**
 * The heart of DWR is a system to generate content from some requests.
 * This interface generates scripts and executes remote calls.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public interface Remoter
{
    /**
     * Generate some Javascript that forms an interface definition
     * @param scriptName The script to generate for
     * @param includeDto Specifies whether mapped DTO classes should be added
     * @param contextServletPath request.contextPath + request.servletPath.
     * @return An interface javascript
     * @throws SecurityException
     */
    String generateInterfaceScript(String scriptName, boolean includeDto, String contextServletPath) throws SecurityException;

    /**
     * Generate JavaScript that forms a mapped DTO class
     * @param jsClassName The mapped JavaScript class name
     * @return JavaScript class definition
     * @throws SecurityException
     */
    String generateDtoScript(String jsClassName) throws SecurityException;

    /**
     * Generate JavaScript with all mapped DTO classes
     * @return JavaScript class definitions
     * @throws SecurityException
     */
    String generateAllDtoScripts() throws SecurityException;

    /**
     * Execute a set of remote calls and generate set of reply data for later
     * conversion to whatever wire protocol we are using today.
     * @param calls The set of calls to execute
     * @return A set of reply data objects
     */
    Replies execute(Calls calls);

    /**
     * The path to the DWR servlet is probably just equal to request.contextPath
     * plus request.servletPath. However there are 2 ways to override this.
     * One is to provide an overridePath setting, and the other is to specify
     * useAbsolutePath=true, when the full URL up to the DWR servlet is used.
     * This method simply echos back the contextServletPath unless one of those
     * 2 settings are used in which case the modified value is returned.
     * @param contextServletPath request.contextPath + request.servletPath.
     * @return The path to the DWR servlet
     */
    String getPathToDwrServlet(String contextServletPath);
}
