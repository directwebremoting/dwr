package org.directwebremoting.servlet;

import org.directwebremoting.Container;
import org.directwebremoting.extend.ContainerUtil;

/**
 * A handler for interface generation requests compatible with Dojo module system.
 * @author Mike Wilson
 */
public class DojoInterfaceHandler extends BaseInterfaceHandler
{
    /* (non-Javadoc)
     * @see org.directwebremoting.servlet.BaseInterfaceHandler#getBaseInterfacePath()
     */
    @Override
    protected String getBaseInterfacePath()
    {
        return dojoInterfaceHandlerUrl;
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.servlet.BaseInterfaceHandler#generateInterface(java.lang.String, java.lang.String)
     */
    @Override
    public String generateInterfaceScript(String contextPath, String servletPath, String scriptName)
    {
        DojoModule mod = new DojoModule(contextPath, servletPath, dojoInterfaceBaseModulePath, scriptName);
        mod.addRequire(dojoDwrBaseModulePath, "engine");
        String moduleName = mod.expandModulePath(dojoInterfaceBaseModulePath, scriptName);

        // An optimization here might be to only generate class
        // definitions for classes used as parameters in the class that we are
        // currently generating a proxy for.
        if (generateDtoClasses.matches(".*\\binterface\\b.*") && converterManager.getNamedConverterJavaScriptNames().size() > 0)
        {
            mod.addContent(dojoDtoAllHandler.generateDtoAllScript(contextPath, servletPath));
            mod.addContent("\n");
        }

        // Capturing closure
        mod.addContent("(function(dwr) {\n");
        mod.addContent("  var p;\n");
        mod.addContent("\n");

        // Add standard interface contents
        mod.addContent(remoter.generateInterfaceJavaScript(scriptName, "  ", "p", contextPath + servletPath));

        // Set interface module and close the capturing closure
        mod.addContent("  \n");
        mod.addContent("  dojo.setObject(\"" + moduleName + "\", p);\n");
        mod.addContent("})(" + mod.expandModulePath(dojoDwrBaseModulePath) + ");\n");


        return mod.toString();
    }

    /**
     * We use the container to find the DojoDtoAllHandler
     * @param container the container to set
     */
    public void setContainer(Container container)
    {
        dojoDtoAllHandler = (DojoDtoAllHandler) ContainerUtil.getHandlerForUrlProperty(container, "dojoDtoAllHandlerUrl");
    }

    /**
     * Setter for the URL that this handler is available on
     * @param url the url to set
     */
    public void setDojoInterfaceHandlerUrl(final String url)
    {
        dojoInterfaceHandlerUrl = url;
    }

    /**
     * Setter for the module path that our interfaces on
     * @param modulePath the modulePath to set
     */
    public void setDojoInterfaceBaseModulePath(final String modulePath)
    {
        dojoInterfaceBaseModulePath = modulePath;
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
     * We use the DtoAllHandler to generate DTO classes when applicable.
     */
    protected DojoDtoAllHandler dojoDtoAllHandler;

    /**
     * What URL is this handler available on?
     */
    protected String dojoInterfaceHandlerUrl;

    /**
     * What module path are our interfaces on?
     */
    protected String dojoInterfaceBaseModulePath;

    /**
     * What module path is dwr.engine on?
     */
    protected String dojoDwrBaseModulePath;
}
