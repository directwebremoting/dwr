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

import org.directwebremoting.extend.DebugPageGenerator;
import org.directwebremoting.extend.Handler;
import org.directwebremoting.util.LocalUtil;
import org.directwebremoting.util.MimeConstants;

/**
 * A handler for requests to create a class debug page
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class TestHandler implements Handler
{
    /* (non-Javadoc)
     * @see org.directwebremoting.Handler#handle(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    public void handle(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        String scriptName = request.getPathInfo();
        scriptName = scriptName.replace(testHandlerUrl, "");
        scriptName = scriptName.replace("/", "");
        if (!LocalUtil.isJavaIdentifier(scriptName))
        {
            throw new SecurityException("Script names may only contain Java Identifiers");
        }

        String page = debugPageGenerator.generateTestPage(request.getContextPath() + request.getServletPath(), scriptName);

        response.setContentType(MimeConstants.MIME_HTML);
        PrintWriter out = response.getWriter();
        out.print(page);
    }

    /**
     * Setter for the debug page generator
     * @param debugPageGenerator The new debug page generator
     */
    public void setDebugPageGenerator(DebugPageGenerator debugPageGenerator)
    {
        this.debugPageGenerator = debugPageGenerator;
    }

    /**
     * The bean to handle debug page requests
     */
    protected DebugPageGenerator debugPageGenerator = null;

    /**
     * Setter for the URL that this handler available on
     * @param testHandlerUrl the testHandlerUrl to set
     */
    public void setTestHandlerUrl(String testHandlerUrl)
    {
        this.testHandlerUrl = testHandlerUrl;
    }

    /**
     * What URL is this handler available on?
     */
    protected String testHandlerUrl;
}
