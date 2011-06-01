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
 * A Handler that supports requests for engine.js compatible with the CommonJS AMD format.
 * @author Mike Wilson
 */
public class CommonJsAmdEngineHandler extends BaseEngineHandler
{
    /* (non-Javadoc)
     * @see org.directwebremoting.servlet.FileJavaScriptHandler#generateTemplate(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected String generateTemplate(String contextPath, String servletPath, String pathInfo) throws IOException
    {
        CommonJsAmdModule mod = new CommonJsAmdModule(contextPath, servletPath);
        final StringBuilder buf = new StringBuilder();

        // Local variable to capture the DWR namespace
        buf.append("var dwr;\n");

        // Add standard engine.js contents
        buf.append("\n");
        buf.append("// standard engine.js\n");
        buf.append("\n");
        buf.append(super.generateTemplate(contextPath, servletPath, pathInfo));
        buf.append("\n");
        buf.append("// end standard engine.js\n");
        buf.append("\n");

        // Alias allowing dwr.* or dwr.engine.* to be used
        buf.append("dwr.engine.engine = dwr.engine;\n");

        buf.append("return dwr.engine;\n");

        mod.addContent(buf.toString());

        return mod.toString();
    }
}
