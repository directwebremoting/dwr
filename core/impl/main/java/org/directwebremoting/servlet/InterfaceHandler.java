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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.directwebremoting.extend.Converter;
import org.directwebremoting.extend.ConverterManager;
import org.directwebremoting.extend.EnginePrivate;
import org.directwebremoting.extend.NamedConverter;
import org.directwebremoting.util.LocalUtil;

/**
 * A handler for interface generation requests
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class InterfaceHandler extends GeneratedJavaScriptHandler
{
    /* (non-Javadoc)
     * @see org.directwebremoting.servlet.TemplateHandler#generateTemplate(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected String generateTemplate(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        String scriptName = request.getPathInfo();

        if (!scriptName.endsWith(PathConstants.EXTENSION_JS))
        {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return "";
        }

        scriptName = scriptName.replace(interfaceHandlerUrl, "");
        scriptName = scriptName.replace(PathConstants.EXTENSION_JS, "");

        if (!LocalUtil.isValidScriptName(scriptName))
        {
            log.debug("Throwing at request for script with name: '" + scriptName + "'");
            throw new SecurityException("Illegal script name.");
        }

        // Generate script
        String contextServletPath = request.getContextPath() + request.getServletPath();
        return generateInterface(scriptName, contextServletPath);
    }

    /**
     * Does the actual work of generating the interface script
     * @param scriptName
     * @param contextServletPath
     * @return interface script
     */
    protected String generateInterface(String scriptName, String contextServletPath)
    {
        StringBuilder buffer = new StringBuilder();

        buffer
            .append(EnginePrivate.getRequireEngineScript())
            .append("(function() {\n");

        // An optimization here might be to only generate class
        // definitions for classes used as parameters in the class that we are
        // currently generating a proxy for.
        if (generateDtoClasses.matches(".*\\binterface\\b.*"))
        {
            buffer
                .append("  var c;\n")
                .append("  var added = {};\n")
                .append("  var getClass = dwr.engine._getObject;\n")
                .append("  function setClass(name, c) {\n")
                .append("    dwr.engine._mappedClasses[name] = c;\n")
                .append("    dwr.engine._setObject(name, c);\n")
                .append("    added[name] = true;\n")
                .append("  }\n");
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
                        String script = remoter.generateDtoScript(jsClassName, "    ", "c");
                        if (script != null)
                        {
                            buffer
                                .append("  if (typeof getClass('" + jsClassName + "') != 'function') {\n")
                                .append(remoter.generateDtoScript(jsClassName, "    ", "c"))
                                .append("    setClass('" + jsClassName + "', c);\n")
                                .append("  }\n");
                        }
                        else
                        {
                            buffer.append("// Missing mapped class definition for ");
                            buffer.append(jsClassName);
                            buffer.append(". See the server logs for details.\n");
                        }
                    }
                }
            }
            // DTO superclass definitions
            buffer.append("\n");
            for (String match : converterManager.getConverterMatchStrings())
            {
                Converter conv = converterManager.getConverterByMatchString(match);
                if (conv instanceof NamedConverter)
                {
                    String jsClassName = ((NamedConverter) conv).getJavascript();
                    if (LocalUtil.hasLength(jsClassName))
                    {
                        String inheritance = remoter.generateDtoInheritanceScript(jsClassName, "    ", "c", "getClass");
                        if (inheritance != null)
                        {
                            buffer
                                .append("  if (added['" + jsClassName + "']) {\n")
                                .append("    c = getClass('" + jsClassName + "');\n")
                                .append(inheritance)
                                .append("  }\n");
                        }
                    }
                }
            }
        }

        buffer
            .append("\n")
            .append("  var p;\n")
            .append("  if (typeof dwr.engine._getObject('" + scriptName + "') == 'undefined') {\n")
            .append(remoter.generateInterfaceScript(scriptName, "    ", "p", contextServletPath))
            .append("    dwr.engine._setObject('" + scriptName + "', p);\n")
            .append("  }\n");

        buffer
            .append("})();\n");

        return buffer.toString();
    }

    /**
     * @param converterManager the converterManager to set
     */
    public void setConverterManager(ConverterManager converterManager)
    {
        this.converterManager = converterManager;
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
     * Setter for the generator setting.
     * @param generateDtoClasses list of enabled places to generate DTO classes in
     */
    public void setGenerateDtoClasses(String generateDtoClasses)
    {
        this.generateDtoClasses = generateDtoClasses;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        return this.getClass().getSimpleName() + "(" + interfaceHandlerUrl + ")";
    }

    /**
     * ConverterManager to query for DTO classes
     */
    private ConverterManager converterManager;

    /**
     * What URL is this handler available on?
     */
    protected String interfaceHandlerUrl;

    /**
     * List of enabled places to generate DTO classes in
     */
    protected String generateDtoClasses;

    /**
     * The log stream
     */
    private static final Log log = LogFactory.getLog(InterfaceHandler.class);
}
