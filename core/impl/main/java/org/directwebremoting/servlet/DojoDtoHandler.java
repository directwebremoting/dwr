package org.directwebremoting.servlet;

import org.directwebremoting.extend.NamedConverter;
import org.directwebremoting.util.LocalUtil;

/**
 * A handler for DTO class generation requests compatible with the Dojo module format.
 * @author Mike Wilson [mikewse at hotmail dot com]
 */
public class DojoDtoHandler extends BaseDtoHandler
{
    /* (non-Javadoc)
     * @see org.directwebremoting.servlet.BaseDtoHandler#getBaseDtoPath()
     */
    @Override
    protected String getBaseDtoPath()
    {
        return dojoDtoHandlerUrl;
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.servlet.BaseDtoHandler#generateDtoScript(java.lang.String)
     */
    @Override
    public String generateDtoScript(String contextPath, String servletPath, String jsClassName)
    {
        String script = null;

        String dtojs = remoter.generateDtoJavaScript(jsClassName, "    ", "c");
        if (dtojs != null)
        {
            DojoModule mod = new DojoModule(contextPath, servletPath, dojoDtoBaseModulePath, jsClassName);
            mod.addRequire(dojoDwrBaseModulePath, "engine");

            String engineModule = mod.expandModulePath(dojoDwrBaseModulePath, "engine");
            String dtoModule = mod.expandModulePath(dojoDtoBaseModulePath, jsClassName);

            mod.addContent("(function() {\n");
            mod.addContent("  var c;\n");
            mod.addContent("  if (!" + engineModule + "._mappedClasses[\"" + jsClassName + "\"]) {\n");

            // Generate DTO
            mod.addContent(dtojs);
            mod.addContent("    " + engineModule + "._mappedClasses[\"" + jsClassName + "\"] = c;\n");

            // Generate inheritance
            NamedConverter namedConv = converterManager.getNamedConverter(jsClassName);
            String jsSuperClassName = namedConv.getJavascriptSuperClass();
            if (LocalUtil.hasLength(jsSuperClassName))
            {
                mod.addContent("\n");
                mod.addRequire(dojoDtoBaseModulePath, jsSuperClassName);
                String superClassExpression = engineModule + "._mappedClasses[\"" + jsSuperClassName + "\"]";
                mod.addContent(remoter.generateDtoInheritanceJavaScript("    ", "c", superClassExpression, engineModule + "._delegate"));
            }

            mod.addContent("  }\n");
            mod.addContent("  dojo.setObject(\"" + dtoModule + "\", " + engineModule + "._mappedClasses[\"" + jsClassName + "\"]);\n");
            mod.addContent("})();\n");

            script = mod.toString();
        }

        return script;
    }

    /**
     * Setter for the URL that this handler is available on
     * @param dojoDtoHandlerUrl the dtoHandlerUrl to set
     */
    public void setDojoDtoHandlerUrl(String dojoDtoHandlerUrl)
    {
        this.dojoDtoHandlerUrl = dojoDtoHandlerUrl;
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
     * Setter for the module path that DTO is on
     * @param modulePath the modulePath to set
     */
    public void setDojoDtoBaseModulePath(final String modulePath)
    {
        dojoDtoBaseModulePath = modulePath;
    }

    /**
     * What URL is this handler available on?
     */
    protected String dojoDtoHandlerUrl;

    /**
     * What module path is dwr.engine on?
     */
    protected String dojoDwrBaseModulePath;

    /**
     * What module path are DTOs on?
     */
    protected String dojoDtoBaseModulePath;
}
