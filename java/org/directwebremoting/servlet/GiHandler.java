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

import org.directwebremoting.extend.DwrConstants;
import org.directwebremoting.util.MimeConstants;

/**
 * A Handler that supports requests for util.js
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class GiHandler extends JavaScriptHandler
{
    /**
     * Setup the {@link JavaScriptHandler} defaults
     */
    public GiHandler()
    {
        setMimeType(MimeConstants.MIME_JS);
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.servlet.TemplateHandler#generateTemplate(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected String generateTemplate(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        return readResource(DwrConstants.PACKAGE + "/gi.js");
    }
}
