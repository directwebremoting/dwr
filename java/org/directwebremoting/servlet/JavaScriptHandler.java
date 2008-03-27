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

import org.directwebremoting.util.JavascriptUtil;
import org.directwebremoting.util.MimeConstants;

/**
 * Basically a file servlet component that does some <b>very limited</b>
 * EL type processing on the file. See the source for the cheat.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public abstract class JavaScriptHandler extends TemplateHandler
{
    /* (non-Javadoc)
     * @see org.directwebremoting.servlet.CachingFileHandler#generate(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected String generateCachableContent(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        String output = super.generateCachableContent(request, response);

        if (getMimeType().equals(MimeConstants.MIME_JS) && scriptCompressed)
        {
            output = JavascriptUtil.compress(output, compressionLevel);
        }

        return output;
    }

    /**
     * To what level do we compress scripts?
     * @param scriptCompressed The scriptCompressed to set.
     */
    public void setScriptCompressed(boolean scriptCompressed)
    {
        this.scriptCompressed = scriptCompressed;
    }

    /**
     * @param compressionLevel The compressionLevel to set.
     */
    public void setCompressionLevel(int compressionLevel)
    {
        this.compressionLevel = compressionLevel;
    }

    /**
     * How much do we compression javascript by?
     */
    protected int compressionLevel = JavascriptUtil.LEVEL_DEBUGGABLE;

    /**
     * Do we retain comments and unneeded spaces in Javascript code?
     */
    private boolean scriptCompressed = false;
}
