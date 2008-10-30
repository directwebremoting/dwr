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
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.directwebremoting.extend.Handler;
import org.directwebremoting.extend.Remoter;
import org.directwebremoting.util.LocalUtil;
import org.directwebremoting.util.MimeConstants;

/**
 * A handler for interface generation requests
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class InterfaceHandler implements Handler
{
    /* (non-Javadoc)
     * @see org.directwebremoting.Handler#handle(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    public void handle(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        String scriptName = request.getPathInfo();
        scriptName = LocalUtil.replace(scriptName, interfaceHandlerUrl, "");
        scriptName = LocalUtil.replace(scriptName, PathConstants.EXTENSION_JS, "");
        if (!LocalUtil.isJavaIdentifier(scriptName))
        {
            throw new SecurityException("Script names may only contain Java Identifiers");
        }

        String path = request.getContextPath() + request.getServletPath();

        String script = remoter.generateInterfaceScript(scriptName, path);

        // Officially we should use MimeConstants.MIME_JS, but if we cheat and
        // use MimeConstants.MIME_PLAIN then it will be easier to read in a
        // browser window, and will still work just fine.
        // However: There have been a number of complaints about this so ...
        response.setContentType(MimeConstants.MIME_JS);
        PrintWriter out = response.getWriter();
        out.print(script);
    }

    /**
     * Setter for the remoter
     * @param remoter
     */
    public void setRemoter(Remoter remoter)
    {
        this.remoter = remoter;
    }

    /**
     * The bean to execute remote requests and generate interfaces
     */
    protected Remoter remoter = null;

    /**
     * Setter for the URL that this handler available on
     * @param interfaceHandlerUrl the interfaceHandlerUrl to set
     */
    public void setInterfaceHandlerUrl(String interfaceHandlerUrl)
    {
        this.interfaceHandlerUrl = interfaceHandlerUrl;
    }

    /**
     * What URL is this handler available on?
     */
    protected String interfaceHandlerUrl;
}
