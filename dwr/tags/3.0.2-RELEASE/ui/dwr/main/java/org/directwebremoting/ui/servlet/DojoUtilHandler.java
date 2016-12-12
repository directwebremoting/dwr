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
package org.directwebremoting.ui.servlet;

import java.io.IOException;

import org.directwebremoting.servlet.DojoModule;

/**
 * A Handler that supports requests for util.js compatible with the Dojo format.
 * @author Mike Wilson
 */
public class DojoUtilHandler extends BaseUtilHandler
{
    /* (non-Javadoc)
     * @see org.directwebremoting.servlet.FileJavaScriptHandler#generateTemplate(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected String generateTemplate(String contextPath, String servletPath, String pathInfo) throws IOException
    {
        DojoModule mod = new DojoModule(contextPath, servletPath, dojoDwrBaseModulePath, "util");

        // Capture the DWR namespace if not mapped to standard "dwr" path
        boolean remap = !dojoDwrBaseModulePath.equals("dwr");
        if (remap)
        {
            mod.addContent("(function(dwr) {\n");
            mod.addContent("\n");
        }

        // Add standard util.js contents
        mod.addContent(super.generateTemplate(contextPath, servletPath, pathInfo));

        // Close the capturing closure
        if (remap)
        {
            mod.addContent("\n");
            mod.addContent("})(" + mod.expandModulePath(dojoDwrBaseModulePath) + ");\n");
        }

        return mod.toString();
    }

    /**
     * Setter for the module path that dwr.engine is on
     * @param modulePath the modulePath to set
     */
    public void setDojoDwrBaseModulePath(final String modulePath)
    {
        dojoDwrBaseModulePath = modulePath;
    }

    /**
     * What module path is dwr.engine on?
     */
    protected String dojoDwrBaseModulePath;
}
