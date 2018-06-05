package org.directwebremoting.servlet;

import java.io.IOException;

/**
 * A Handler that supports requests for engine.js compatible with the Dojo format.
 * @author Mike Wilson
 */
public class DojoEngineHandler extends BaseEngineHandler
{
    /* (non-Javadoc)
     * @see org.directwebremoting.servlet.FileJavaScriptHandler#generateTemplate(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected String generateTemplate(String contextPath, String servletPath, String pathInfo) throws IOException
    {
        DojoModule mod = new DojoModule(contextPath, servletPath, dojoDwrBaseModulePath, "engine");

        // Capture the DWR namespace if not mapped to standard "dwr" path
        boolean remap = !dojoDwrBaseModulePath.equals("dwr");
        if (remap)
        {
            mod.addContent("(function(dwr) {\n");
            mod.addContent("\n");
        }

        // Add standard engine.js contents
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
