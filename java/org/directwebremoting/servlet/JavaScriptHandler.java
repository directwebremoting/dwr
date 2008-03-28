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
import org.directwebremoting.extend.Compressor;
import org.directwebremoting.util.MimeConstants;

/**
 * Once we know a resource is JavaScript, we can go about compressing it.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public abstract class JavaScriptHandler extends TemplateHandler
{
    /**
     * Setup the {@link JavaScriptHandler} defaults
     */
    public JavaScriptHandler()
    {
        setMimeType(MimeConstants.MIME_JS);
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.servlet.CachingHandler#generate(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected String generateCachableContent(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        String output = super.generateCachableContent(request, response);

        if (debug || compressor == null)
        {
            return output;
        }

        try
        {
            return compressor.compressJavaScript(output);
        }
        catch (Exception ex)
        {
            log.warn("Compression system (" + compressor.getClass().getSimpleName() +") failed to compress script", ex);
            return output;
        }
    }

    /**
     * Setter for the current JavaScript compression library
     * @param compressor The new compression library
     */
    public void setCompressor(Compressor compressor)
    {
        this.compressor = compressor;
    }

    /**
     * In debug mode we don't do compression at all
     */
    public void setDebug(boolean debug)
    {
        this.debug = debug;
    }

    /**
     * Are we compressing the script?
     */
    private Compressor compressor;

    /**
     * In debug mode, we skip script compression
     */
    private boolean debug = false;

    /**
     * The log stream
     */
    private static final Log log = LogFactory.getLog(JavaScriptHandler.class);
}
