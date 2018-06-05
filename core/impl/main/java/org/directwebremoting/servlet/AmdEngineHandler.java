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

        // Local variable to capture dwrConfig
        mod.addDependency("", "module", "module"); // Use pseudo module in AMD to access configuration
        mod.addContent("var dwrConfig = module && module.config && module.config();\n");

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
