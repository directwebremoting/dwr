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
package org.directwebremoting.jaxer.servlet;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.directwebremoting.servlet.InterfaceHandler;
import org.directwebremoting.servlet.PathConstants;

/**
 * A handler for interface generation requests
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class JaxerInterfaceHandler extends InterfaceHandler
{
    /* (non-Javadoc)
     * @see org.directwebremoting.servlet.TemplateHandler#generateTemplate(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected String generateTemplate(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        String scriptName = request.getPathInfo();

        if (!scriptName.endsWith(PathConstants.EXTENSION_JS))
        {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return "";
        }

        scriptName = scriptName.replaceFirst("/", "");
        scriptName = scriptName.replace(PathConstants.EXTENSION_JS, "");

        String contextServletPath = request.getContextPath() + request.getServletPath();
        return remoter.generateInterfaceScript(scriptName, generateDtoClasses.matches(".*\\binterface\\b.*"), contextServletPath);
    }
}
