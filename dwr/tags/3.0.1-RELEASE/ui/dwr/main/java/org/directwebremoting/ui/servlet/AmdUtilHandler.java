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

import org.directwebremoting.servlet.AmdModule;

/**
 * A Handler that supports requests for util.js compatible with the AMD format.
 * @author Mike Wilson
 */
public class AmdUtilHandler extends BaseUtilHandler
{
    /* (non-Javadoc)
     * @see org.directwebremoting.servlet.FileJavaScriptHandler#generateTemplate(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected String generateTemplate(String contextPath, String servletPath, String pathInfo) throws IOException
    {
        AmdModule mod = new AmdModule(contextPath, servletPath);
        final StringBuilder buf = new StringBuilder();

        // Local variable to capture the DWR namespace
        buf.append("var dwr;\n");

        // Add standard util.js contents
        buf.append("\n");
        buf.append("// standard util.js\n");
        buf.append("\n");
        buf.append(super.generateTemplate(contextPath, servletPath, pathInfo));
        buf.append("\n");
        buf.append("// end standard util.js\n");
        buf.append("\n");

        // Alias allowing dwr.* or dwr.util.* to be used
        buf.append("dwr.util.util = dwr.util;\n");

        buf.append("return dwr.util;\n");

        mod.addContent(buf.toString());

        return mod.toString();
    }
}
