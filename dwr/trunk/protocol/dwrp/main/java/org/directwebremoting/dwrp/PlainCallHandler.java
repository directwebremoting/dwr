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
package org.directwebremoting.dwrp;

import java.io.IOException;
import java.io.PrintWriter;

import org.directwebremoting.util.MimeConstants;

/**
 * A Handler standard DWR calls whose replies are NOT HTML wrapped.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class PlainCallHandler extends BaseCallHandler
{
    /* (non-Javadoc)
     * @see org.directwebremoting.dwrp.BaseCallHandler#getOutboundMimeType()
     */
    @Override
    protected String getOutboundMimeType()
    {
        return MimeConstants.MIME_JS;
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.dwrp.BaseCallHandler#sendOutboundScriptPrefix(java.io.PrintWriter, java.lang.String)
     */
    @Override
    protected void sendOutboundScriptPrefix(PrintWriter out, String batchId) throws IOException
    {
        if (!allowScriptTagRemoting)
        {
            synchronized (out)
            {
                out.println(scriptTagProtection);
            }
        }
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.dwrp.BaseCallHandler#sendOutboundScriptSuffix(java.io.PrintWriter, java.lang.String)
     */
    @Override
    protected void sendOutboundScriptSuffix(PrintWriter out, String batchId) throws IOException
    {
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.dwrp.BaseCallHandler#sendScript(java.io.PrintWriter, java.lang.String)
     */
    @Override
    protected void sendScript(PrintWriter out, String script) throws IOException
    {
        synchronized (out)
        {
            out.println(script);
        }
    }

    /**
     * Do we allow ScriptTag remoting?
     * @param allowScriptTagRemoting The new value to set
     */
    public void setAllowScriptTagRemoting(boolean allowScriptTagRemoting)
    {
        this.allowScriptTagRemoting = allowScriptTagRemoting;
    }

    /**
     * Do we allow ScriptTag remoting.
     */
    private boolean allowScriptTagRemoting = false;

    /**
     * What is the string we use for script tag hack protection
     * @param scriptTagProtection the scriptTagProtection to set
     */
    public void setScriptTagProtection(String scriptTagProtection)
    {
        this.scriptTagProtection = scriptTagProtection;
    }

    /**
     * What is the string we use for script tag hack protection
     */
    private String scriptTagProtection;
}
