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
 * A handler for DTO class generation requests
 * @author Mike Wilson [mikewse at hotmail dot com]
 */
public class DtoHandler extends BaseDtoHandler
{
    /* (non-Javadoc)
     * @see org.directwebremoting.servlet.BaseDtoHandler#getBaseDtoPath()
     */
    @Override
    protected String getBaseDtoPath()
    {
        return dtoHandlerUrl;
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
            StringBuilder buffer = new StringBuilder();

            buffer
                .append("(function() {\n")
                .append("  var c;\n")
                .append("  if (!dwr.engine._mappedClasses[\"" + jsClassName + "\"]) {\n");

            // Generate DTO
            buffer
                .append(dtojs)
                .append("    dwr.engine._setObject(\"" + jsClassName + "\", c);\n")
                .append("    dwr.engine._mappedClasses[\"" + jsClassName + "\"] = c;\n");

            // Generate inheritance
            NamedConverter namedConv = converterManager.getNamedConverter(jsClassName);
            String jsSuperClassName = namedConv.getJavascriptSuperClass();
            if (LocalUtil.hasLength(jsSuperClassName))
            {
                String classExpression = "dwr.engine._mappedClasses[\"" + jsClassName + "\"]";
                String superClassExpression = "dwr.engine._mappedClasses[\"" + jsSuperClassName + "\"]";
                buffer
                    .append(remoter.generateDtoInheritanceJavaScript("    ", classExpression, superClassExpression, "dwr.engine._delegate"));
            }

            buffer
                .append("  }\n")
                .append("})();\n");

            script = buffer.toString();
        }

        return script;
    }

    /**
     * Setter for the URL that this handler available on
     * @param dtoHandlerUrl the dtoHandlerUrl to set
     */
    public void setDtoHandlerUrl(String dtoHandlerUrl)
    {
        this.dtoHandlerUrl = dtoHandlerUrl;
    }

    /**
     * What URL is this handler available on?
     */
    protected String dtoHandlerUrl;
}
