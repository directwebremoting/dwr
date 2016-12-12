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
 * A handler for dtoall generation requests
 * @author Mike Wilson [mikewse at hotmail dot com]
 */
public class DtoAllHandler extends BaseDtoAllHandler
{
    /**
     * Generates the full dtoall script by decorating the DTO classes returned by the Remoter.
     * @return string containing dtoall script
     */
    @Override
    public String generateDtoAllScript(String contextPath, String servletPath)
    {
        StringBuilder buffer = new StringBuilder();

        buffer
            .append("(function() {\n")
            .append("  var c;\n")
            .append("  var addedNow = [];\n");

        // DTO class definitions
        for (String jsClassName : converterManager.getNamedConverterJavaScriptNames())
        {
            buffer
                .append("\n")
                .append("  if (!dwr.engine._mappedClasses[\"" + jsClassName + "\"]) {\n")
                .append(remoter.generateDtoJavaScript(jsClassName, "    ", "c"))
                .append("    dwr.engine._setObject(\"" + jsClassName + "\", c);\n")
                .append("    dwr.engine._mappedClasses[\"" + jsClassName + "\"] = c;\n")
                .append("    addedNow[\"" + jsClassName + "\"] = true;\n")
                .append("  }\n");
        }

        // DTO superclass definitions
        for (String jsClassName : converterManager.getNamedConverterJavaScriptNames())
        {
            NamedConverter namedConv = converterManager.getNamedConverter(jsClassName);
            String jsSuperClassName = namedConv.getJavascriptSuperClass();
            if (LocalUtil.hasLength(jsSuperClassName))
            {
                String classExpression = "dwr.engine._mappedClasses[\"" + jsClassName + "\"]";
                String superClassExpression = "dwr.engine._mappedClasses[\"" + jsSuperClassName + "\"]";
                buffer
                    .append("\n")
                    .append("  if (addedNow[\"" + jsClassName + "\"]) {\n")
                    .append(remoter.generateDtoInheritanceJavaScript("    ", classExpression, superClassExpression, "dwr.engine._delegate"))
                    .append("  }\n");
            }
        }

        buffer.append("})();\n");

        return buffer.toString();
    }
}
