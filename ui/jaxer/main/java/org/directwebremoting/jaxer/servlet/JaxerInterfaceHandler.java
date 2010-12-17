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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.directwebremoting.extend.Module;
import org.directwebremoting.extend.ModuleManager;
import org.directwebremoting.servlet.InterfaceHandler;
import org.directwebremoting.servlet.PathConstants;

/**
 * This is a customization of {@link InterfaceHandler} which is needed because
 * DWR+Jaxer has a different definition of scriptName than plain vanilla DWR
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
        String fullCreatorName = request.getPathInfo();

        if (!fullCreatorName.endsWith(PathConstants.EXTENSION_JS))
        {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return "";
        }

        fullCreatorName = fullCreatorName.replaceFirst("/", "");
        fullCreatorName = fullCreatorName.replace(PathConstants.EXTENSION_JS, "");

        // Lookup the module using long creatorName
        Module module = moduleManager.getModule(fullCreatorName, false);
        if (module == null)
        {
            log.warn("Failed to find creator using: " + fullCreatorName);
            throw new SecurityException("Failed to find creator");
        }

        // Internally use short scriptName
        String scriptName = module.getName();

        String contextServletPath = request.getContextPath() + request.getServletPath();
        return generateInterface(scriptName, contextServletPath);
    }

    /**
     * Setter for the ModuleManager
     * @param moduleManager The new ModuleManager
     */
    public void setModuleManager(ModuleManager moduleManager)
    {
        this.moduleManager = moduleManager;
    }

    /**
     * Used for looking up modules for the requested interfaces.
     */
    protected ModuleManager moduleManager = null;

    /**
     * The log stream
     */
    private static final Log log = LogFactory.getLog(JaxerInterfaceHandler.class);
}
