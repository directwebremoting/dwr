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
 * A handler for DTO class generation requests compatible with the CommonJS AMD module format.
 * @author Mike Wilson [mikewse at hotmail dot com]
 */
public class CommonJsAmdDtoHandler extends BaseDtoHandler
{
    /* (non-Javadoc)
     * @see org.directwebremoting.servlet.BaseDtoHandler#getBaseDtoPath()
     */
    @Override
    protected String getBaseDtoPath()
    {
        return commonJsAmdDtoHandlerUrl;
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.servlet.BaseDtoHandler#generateDtoScript(java.lang.String)
     */
    @Override
    public String generateDtoScript(String contextPath, String servletPath, String jsClassName)
    {
        String script = null;

        String dtojs = remoter.generateDtoJavaScript(jsClassName, "  ", "c");
        if (dtojs != null)
        {
            NamedConverter namedConv = converterManager.getNamedConverter(jsClassName);
            String jsSuperClassName = namedConv.getJavascriptSuperClass();

            CommonJsAmdModule mod = new CommonJsAmdModule(contextPath, servletPath);
            mod.addDependency(commonJsAmdDwrBaseModulePath, "engine", "dwr");
            if (LocalUtil.hasLength(jsSuperClassName))
            {
                mod.addDependency(commonJsAmdDtoBaseModulePath, jsSuperClassName, "parent");
            }

            mod.addContent("  var c;\n");
            mod.addContent("\n");

            // Generate DTO
            mod.addContent(dtojs);
            mod.addContent("    dwr.engine._mappedClasses[\"" + jsClassName + "\"] = c;\n");

            // Generate inheritance
            if (LocalUtil.hasLength(jsSuperClassName))
            {
                String classExpression = "dwr.engine._mappedClasses[\"" + jsClassName + "\"]";
                mod.addContent(remoter.generateDtoInheritanceJavaScript("    ", classExpression, "parent", "dwr.engine._delegate"));
            }

            mod.addContent("\n");
            mod.addContent("  return c;\n");

            script = mod.toString();
        }

        return script;
    }

    /**
     * Setter for the URL that this handler is available on
     * @param commonJsAmdDtoHandlerUrl the dtoHandlerUrl to set
     */
    public void setCommonJsAmdDtoHandlerUrl(String commonJsAmdDtoHandlerUrl)
    {
        this.commonJsAmdDtoHandlerUrl = commonJsAmdDtoHandlerUrl;
    }

    /**
     * Setter for the module path that dwr.engine is on
     * @param modulePath the modulePath to set
     */
    public void setCommonJsAmdDwrBaseModulePath(final String modulePath)
    {
        commonJsAmdDwrBaseModulePath = modulePath;
    }

    /**
     * Setter for the module path that DTO is on
     * @param modulePath the modulePath to set
     */
    public void setCommonJsAmdDtoBaseModulePath(final String modulePath)
    {
        commonJsAmdDtoBaseModulePath = modulePath;
    }

    /**
     * What URL is this handler available on?
     */
    protected String commonJsAmdDtoHandlerUrl;

    /**
     * What module path is dwr.engine on?
     */
    protected String commonJsAmdDwrBaseModulePath;

    /**
     * What module path are DTOs on?
     */
    protected String commonJsAmdDtoBaseModulePath;
}