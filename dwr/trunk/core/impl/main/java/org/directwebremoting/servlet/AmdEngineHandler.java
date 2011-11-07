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
package org.directwebremoting.servlet;

import java.io.IOException;

/**
 * A Handler that supports requests for engine.js compatible with the AMD format.
 * @author Mike Wilson
 */
public class AmdEngineHandler extends BaseEngineHandler
{
    /* (non-Javadoc)
     * @see org.directwebremoting.servlet.FileJavaScriptHandler#generateTemplate(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected String generateTemplate(String contextPath, String servletPath, String pathInfo) throws IOException
    {
        AmdModule mod = new AmdModule(contextPath, servletPath);

        // Local variable to capture the DWR namespace
        mod.addContent("var dwr;\n");

        // Add standard engine.js contents
        mod.addContent("\n");
        mod.addContent("// standard engine.js\n");
        mod.addContent("\n");
        mod.addContent(super.generateTemplate(contextPath, servletPath, pathInfo));
        mod.addContent("\n");
        mod.addContent("// end standard engine.js\n");
        mod.addContent("\n");

        // Alias allowing dwr.* or dwr.engine.* to be used
        mod.addContent("dwr.engine.engine = dwr.engine;\n");

        mod.addContent("return dwr.engine;\n");

        return mod.toString();
    }
}
