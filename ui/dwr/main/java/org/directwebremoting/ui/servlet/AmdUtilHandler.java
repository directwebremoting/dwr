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
