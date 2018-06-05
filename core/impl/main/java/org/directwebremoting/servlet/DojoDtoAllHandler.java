package org.directwebremoting.servlet;

import org.directwebremoting.extend.NamedConverter;
import org.directwebremoting.util.LocalUtil;

/**
 * A handler for dtoall generation requests in the Dojo module format
 * @author Mike Wilson [mikewse at hotmail dot com]
 */
public class DojoDtoAllHandler extends BaseDtoAllHandler
{
    /**
     * Generates the full dtoall script by decorating the DTO classes returned by the Remoter.
     * @return string containing dtoall script
     */
    @Override
    public String generateDtoAllScript(String contextPath, String servletPath)
    {
        DojoModule mod = new DojoModule(contextPath, servletPath, dojoDwrBaseModulePath, "dtoall");
        mod.addRequire(dojoDwrBaseModulePath, "engine");
        String engineModule = mod.expandModulePath(dojoDwrBaseModulePath, "engine");

        mod.addContent("(function() {\n");
        mod.addContent("  var c;\n");
        mod.addContent("  var addedNow = [];\n");

        // DTO class definitions
        for (String jsClassName : converterManager.getNamedConverterJavaScriptNames())
        {
            String dtoModule = mod.expandModulePath(dojoDtoBaseModulePath, jsClassName);
            mod.addContent("\n");
            mod.addContent("  dojo.provide(\"" + dtoModule + "\");\n");
            mod.addContent("  if (!" + engineModule + "._mappedClasses[\"" + jsClassName + "\"]) {\n");
            mod.addContent(remoter.generateDtoJavaScript(jsClassName, "    ", "c"));
            mod.addContent("    " + engineModule + "._mappedClasses[\"" + jsClassName + "\"] = c;\n");
            mod.addContent("    addedNow[\"" + jsClassName + "\"] = true;\n");
            mod.addContent("  }\n");
            mod.addContent("  dojo.setObject(\"" + dtoModule + "\", " + engineModule + "._mappedClasses[\"" + jsClassName + "\"]);\n");
        }

        // DTO inheritance definitions
        for (String jsClassName : converterManager.getNamedConverterJavaScriptNames())
        {
            NamedConverter namedConv = converterManager.getNamedConverter(jsClassName);
            String jsSuperClassName = namedConv.getJavascriptSuperClass();
            if (LocalUtil.hasLength(jsSuperClassName))
            {
                String classExpression = engineModule + "._mappedClasses[\"" + jsClassName + "\"]";
                String superClassExpression = engineModule + "._mappedClasses[\"" + jsSuperClassName + "\"]";
                mod.addContent("\n");
                mod.addContent("  if (addedNow[\"" + jsClassName + "\"]) {\n");
                mod.addContent(remoter.generateDtoInheritanceJavaScript("    ", classExpression, superClassExpression, engineModule + "._delegate"));
                mod.addContent("  }\n");
            }
        }

        mod.addContent("})();\n");

        return mod.toString();
    }

    /**
     * Setter for the module path that dtoall is on
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
     * What module path is dtoall on?
     */
    protected String dojoDwrBaseModulePath;

    /**
     * What module path are DTOs on?
     */
    protected String dojoDtoBaseModulePath;
}
