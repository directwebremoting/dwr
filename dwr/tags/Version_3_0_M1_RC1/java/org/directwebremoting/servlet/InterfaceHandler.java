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
import org.directwebremoting.extend.Remoter;
import org.directwebremoting.util.LocalUtil;
import org.directwebremoting.util.MimeConstants;

/**
 * A handler for interface generation requests
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class InterfaceHandler extends JavaScriptHandler
{
    /**
     * Setup the {@link JavaScriptHandler} defaults
     */
    public InterfaceHandler()
    {
        setMimeType(MimeConstants.MIME_JS);
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.servlet.TemplateHandler#generateTemplate(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected String generateTemplate(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        String scriptName = request.getPathInfo();
        scriptName = scriptName.replace(interfaceHandlerUrl, "");
        scriptName = scriptName.replace(PathConstants.EXTENSION_JS, "");
        if (!LocalUtil.isJavaIdentifier(scriptName))
        {
            log.debug("Throwing at request for script with name: '" + scriptName + "'");
            throw new SecurityException("Script names may only contain Java Identifiers");
        }

        String path = request.getContextPath() + request.getServletPath();

        return remoter.generateInterfaceScript(scriptName, path);
    }

    /**
     * Setter for the remoter
     * @param remoter The new remoter
     */
    public void setRemoter(Remoter remoter)
    {
        this.remoter = remoter;
    }

    /**
     * Setter for the URL that this handler available on
     * @param interfaceHandlerUrl the interfaceHandlerUrl to set
     */
    public void setInterfaceHandlerUrl(String interfaceHandlerUrl)
    {
        this.interfaceHandlerUrl = interfaceHandlerUrl;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        return "InterfaceHandler(" + interfaceHandlerUrl + ")";
    }

    /**
     * The bean to execute remote requests and generate interfaces
     */
    protected Remoter remoter = null;

    /**
     * What URL is this handler available on?
     */
    protected String interfaceHandlerUrl;

    /**
     * The log stream
     */
    private static final Log log = LogFactory.getLog(InterfaceHandler.class);
}
