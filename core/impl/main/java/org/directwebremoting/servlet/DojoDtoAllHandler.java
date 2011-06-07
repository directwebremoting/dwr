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
                String superClassExpression = engineModule + "._mappedClasses[\"" + jsSuperClassName + "\"]";
                mod.addContent("\n");
                mod.addContent("  if (addedNow[\"" + jsClassName + "\"]) {\n");
                mod.addContent(remoter.generateDtoInheritanceJavaScript("    ", "c", superClassExpression, engineModule + "._delegate"));
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
