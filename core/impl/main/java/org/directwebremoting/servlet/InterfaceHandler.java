package org.directwebremoting.servlet;

import org.directwebremoting.Container;
import org.directwebremoting.extend.ContainerUtil;
import org.directwebremoting.extend.EnginePrivate;

/**
 * A handler for interface generation requests
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class InterfaceHandler extends BaseInterfaceHandler
{
    /* (non-Javadoc)
     * @see org.directwebremoting.servlet.BaseInterfaceHandler#getBaseInterfacePath()
     */
    @Override
    protected String getBaseInterfacePath()
    {
        return interfaceHandlerUrl;
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.servlet.BaseInterfaceHandler#generateInterface(java.lang.String, java.lang.String)
     */
    @Override
    public String generateInterfaceScript(String contextPath, String servletPath, String scriptName)
    {
        StringBuilder buffer = new StringBuilder();

        buffer
            .append(EnginePrivate.getRequireEngineScript())
            .append("\n");

        // An optimization here might be to only generate class
        // definitions for classes used as parameters in the class that we are
        // currently generating a proxy for.
        if (generateDtoClasses.matches(".*\\binterface\\b.*") && converterManager.getNamedConverterJavaScriptNames().size() > 0)
        {
            buffer
                .append(dtoAllHandler.generateDtoAllScript(contextPath, servletPath))
                .append("\n");
        }

        buffer
            .append("(function() {\n")
            .append("  if (dwr.engine._getObject(\"" + scriptName + "\") == undefined) {\n")
            .append("    var p;\n")
            .append("    \n")
            .append(remoter.generateInterfaceJavaScript(scriptName, "    ", "p", contextPath + servletPath))
            .append("    \n")
            .append("    dwr.engine._setObject(\"" + scriptName + "\", p);\n")
            .append("  }\n")
            .append("})();\n");

        return buffer.toString();
    }

    /**
     * We use the container to find the DtoAllHandler
     * @param container the container to set
     */
    public void setContainer(Container container)
    {
        dtoAllHandler = (DtoAllHandler) ContainerUtil.getHandlerForUrlProperty(container, "dtoAllHandlerUrl");
    }

    /**
     * Setter for the URL that this handler available on
     * @param interfaceHandlerUrl the interfaceHandlerUrl to set
     */
    public void setInterfaceHandlerUrl(String interfaceHandlerUrl)
    {
        this.interfaceHandlerUrl = interfaceHandlerUrl;
    }

    /**
     * What URL is this handler available on?
     */
    protected String interfaceHandlerUrl;

    /**
     * We use the container to find the DtoAllHandler
     */
    protected Container container;

    /**
     * We use the DtoAllHandler to generate DTO classes when applicable.
     */
    protected DtoAllHandler dtoAllHandler;
}
