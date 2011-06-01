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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.net.URL;

import org.directwebremoting.util.CopyUtils;
import org.directwebremoting.util.LocalUtil;

/**
 * Many {@link JavaScriptHandler}s just read their contents from a file. This
 * class simplifies that.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 * @author Randy Jones (Updates)
 */
public class FileJavaScriptHandler extends JavaScriptHandler
{
    /**
     * @param resource The name of the resource in the classpath that we read
     * our contents from
     */
    public FileJavaScriptHandler(String resource)
    {
        this.resource = resource;
        this.copyright = null;
    }

    /**
     * @param resource The name of the resource in the classpath that we read
     * our contents from
     */
    public FileJavaScriptHandler(String resource, String copyright)
    {
        this.resource = resource;
        this.copyright = copyright;
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.servlet.CachingHandler#generate(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    public String generateCachableContent(String contextPath, String servletPath, String pathInfo) throws IOException
    {
        String output = super.generateCachableContent(contextPath, servletPath, pathInfo);

        if( !debug && copyright != null ) {
            output = getCopyright() + output;
        }

        return output;
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.servlet.TemplateHandler#generateTemplate(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected String generateTemplate(String contextPath, String servletPath, String pathInfo) throws IOException
    {
        StringWriter sw = new StringWriter();
        InputStream raw = null;

        try
        {
            raw = LocalUtil.getInternalResourceAsStream(resource);
            if (raw == null)
            {
                throw new IOException("Failed to find resource: " + resource);
            }

            CopyUtils.copy(raw, sw);
        }
        finally
        {
            LocalUtil.close(raw);
        }

        return sw.toString();
    }

    /**
     * Gets the copyright text to be prepended to the response.
     * @return String copyright text
     * @throws IOException
     */
    protected String getCopyright() throws IOException
    {
        StringWriter sw = new StringWriter();
        InputStream raw = null;

        if(copyright != null )
        {
            try
            {
                raw = LocalUtil.getInternalResourceAsStream(copyright);
                if (raw == null)
                {
                    throw new IOException("Failed to find resource: " + copyright);
                }

                CopyUtils.copy(raw, sw);
            }
            finally
            {
                LocalUtil.close(raw);
            }
        }
        return sw.toString();
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.servlet.CachingHandler#getLastModifiedTime()
     */
    @Override
    protected long getLastModifiedTime()
    {
        URL url = FileJavaScriptHandler.class.getResource(LocalUtil.remappedResourcePath(resource));
        if ("file".equals(url.getProtocol()))
        {
            File file = new File(url.getFile());
            return file.lastModified();
        }

        return LocalUtil.getSystemClassloadTime();
    }

    /**
     * The name of the resource in the classpath that we read our contents from
     */
    private final String resource;

    /**
     * The name of the copyright file in the classpath that we read our contents from
     */
    private final String copyright;
}
