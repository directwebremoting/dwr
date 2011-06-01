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

import java.io.IOException;

import org.directwebremoting.extend.Converter;
import org.directwebremoting.extend.ConverterManager;
import org.directwebremoting.extend.NamedConverter;
import org.directwebremoting.util.LocalUtil;

/**
 * A handler for DTO class generation requests
 * @author Mike Wilson [mikewse at hotmail dot com]
 */
public class DtoAllHandler extends GeneratedJavaScriptHandler
{
    /* (non-Javadoc)
     * @see org.directwebremoting.servlet.TemplateHandler#generateTemplate(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected String generateTemplate(String contextPath, String servletPath, String pathInfo) throws IOException
    {
        if (!generateDtoClasses.matches(".*\\bdtoall\\b.*"))
        {
            return null;
        }

        return generateDtoAllScript();
    }

    /**
     * Generates the full dtoall script by decorating the DTO classes returned by the Remoter.
     * @return string containing dtoall script
     */
    public String generateDtoAllScript()
    {
        StringBuilder buffer = new StringBuilder();

        buffer
            .append("(function() {\n")
            .append("  var c;\n")
            .append("  var addedNow = [];\n");

        // DTO class definitions
        for (String match : converterManager.getConverterMatchStrings())
        {
            Converter conv = converterManager.getConverterByMatchString(match);
            if (conv instanceof NamedConverter)
            {
                String jsClassName = ((NamedConverter) conv).getJavascript();
                if (LocalUtil.hasLength(jsClassName))
                {
                    buffer.append("\n");
                    String script = remoter.generateDtoJavaScript(jsClassName, "    ", "c");
                    if (script != null)
                    {
                        buffer
                            .append("  if (!dwr.engine._mappedClasses['" + jsClassName + "']) {\n")
                            .append(remoter.generateDtoJavaScript(jsClassName, "    ", "c"))
                            .append("    dwr.engine._setObject('" + jsClassName + "', c);\n")
                            .append("    dwr.engine._mappedClasses['" + jsClassName + "'] = c;\n")
                            .append("    addedNow['" + jsClassName + "'] = true;\n")
                            .append("  }\n");
                    }
                    else
                    {
                        buffer.append("  // Missing mapped class definition for ");
                        buffer.append(jsClassName);
                        buffer.append(". See the server logs for details.\n");
                    }
                }
            }
        }

        // DTO superclass definitions
        for (String match : converterManager.getConverterMatchStrings())
        {
            Converter conv = converterManager.getConverterByMatchString(match);
            if (conv instanceof NamedConverter)
            {
                NamedConverter namedConv = (NamedConverter) conv;
                String jsClassName = namedConv.getJavascript();
                String jsSuperClassName = namedConv.getJavascriptSuperClass();
                if (LocalUtil.hasLength(jsClassName) && LocalUtil.hasLength(jsSuperClassName))
                {
                    String classExpression = "dwr.engine._mappedClasses['" + jsClassName + "']";
                    String superClassExpression = "dwr.engine._mappedClasses['" + jsSuperClassName + "']";
                    buffer
                        .append("\n")
                        .append("  if (addedNow['" + jsClassName + "']) {\n")
                        .append(remoter.generateDtoInheritanceJavaScript("    ", classExpression, superClassExpression, "dwr.engine._delegate"))
                        .append("  }\n");
                }
            }
        }

        buffer.append("})();\n");

        return buffer.toString();
    }

    /**
     * Setter for the generator setting.
     * @param generateDtoClasses list of enabled places to generate DTO classes in
     */
    public void setGenerateDtoClasses(String generateDtoClasses)
    {
        this.generateDtoClasses = generateDtoClasses;
    }

    /**
     * @param converterManager the converterManager to set
     */
    public void setConverterManager(ConverterManager converterManager)
    {
        this.converterManager = converterManager;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        return this.getClass().getSimpleName();
    }

    /**
     * List of enabled places to generate DTO classes in
     */
    protected String generateDtoClasses;

    /**
     * ConverterManager to query for DTO classes
     */
    protected ConverterManager converterManager;
}
