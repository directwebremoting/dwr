package org.directwebremoting.servlet;

/**
 * A handler for interface generation requests compatible with AMD format.
 * @author Mike Wilson
 */
public class AmdInterfaceHandler extends BaseInterfaceHandler
{
    /* (non-Javadoc)
     * @see org.directwebremoting.servlet.BaseInterfaceHandler#getBaseInterfacePath()
     */
    @Override
    protected String getBaseInterfacePath()
    {
        return amdInterfaceHandlerUrl;
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.servlet.BaseInterfaceHandler#generateInterface(java.lang.String, java.lang.String)
     */
    @Override
    public String generateInterfaceScript(String contextPath, String servletPath, String scriptName)
    {
        AmdModule mod = new AmdModule(contextPath, servletPath);
        mod.addDependency(amdDwrBaseModulePath, "engine", "dwr");

        mod.addContent("  var p;\n");
        mod.addContent("\n");

        // Add standard interface contents
        mod.addContent(remoter.generateInterfaceJavaScript(scriptName, "  ", "p", contextPath + servletPath));

        mod.addContent("\n");
        mod.addContent("  return p;\n");

        return mod.toString();
    }

    /**
     * Setter for the URL that this handler is available on
     * @param url the url to set
     */
    public void setAmdInterfaceHandlerUrl(final String url)
    {
        amdInterfaceHandlerUrl = url;
    }

    /**
     * Setter for the module path that dwr.engine is on
     * @param modulePath the modulePath to set
     */
    public void setAmdDwrBaseModulePath(final String modulePath)
    {
        amdDwrBaseModulePath = modulePath;
    }

    /**
     * What URL is this handler available on?
     */
    protected String amdInterfaceHandlerUrl;

    /**
     * What module path is dwr.engine on?
     */
    protected String amdDwrBaseModulePath;
}
