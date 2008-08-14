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
import org.directwebremoting.util.LocalUtil;

/**
 * A handler for DTO class generation requests
 * @author Mike Wilson [mikewse at hotmail dot com]
 */
public class DtoHandler extends GeneratedJavaScriptHandler
{
    /* (non-Javadoc)
     * @see org.directwebremoting.servlet.TemplateHandler#generateTemplate(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected String generateTemplate(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        if (!generateDtoClasses.matches(".*\\bdto\\b.*"))
        {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return "";
        }

        String jsClassName = request.getPathInfo();

        if (!jsClassName.startsWith(dtoHandlerUrl) || !jsClassName.endsWith(PathConstants.EXTENSION_JS))
        {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return "";
        }

        jsClassName = jsClassName.substring(dtoHandlerUrl.length());
        jsClassName = jsClassName.substring(0, jsClassName.length() - PathConstants.EXTENSION_JS.length());

        if (!LocalUtil.isJavaIdentifierWithPackage(jsClassName))
        {
            log.debug("Throwing at request for class with name: '" + jsClassName + "'");
            throw new SecurityException("Class names must comply with Java package and class identifiers");
        }

        return remoter.generateDtoScript(jsClassName);
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
        return this.getClass().getSimpleName() + "(" + dtoHandlerUrl + ")";
    }

    /**
     * What URL is this handler available on?
     */
    protected String dtoHandlerUrl;

    /**
     * List of enabled places to generate DTO classes in
     */
    protected String generateDtoClasses;

    /**
     * The log stream
     */
    private static final Log log = LogFactory.getLog(DtoHandler.class);
}
