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
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.directwebremoting.WebContextFactory;
import org.directwebremoting.extend.DwrConstants;
import org.directwebremoting.extend.ServerLoadMonitor;
import org.directwebremoting.util.MimeConstants;

/**
 * A Handler that supports requests for engine.js
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class EngineHandler extends JavaScriptHandler
{
    /**
     * Setup the {@link JavaScriptHandler} defaults
     */
    public EngineHandler()
    {
        setMimeType(MimeConstants.MIME_JS);
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.servlet.TemplateHandler#generateTemplate(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected String generateTemplate(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        return readResource(DwrConstants.PACKAGE + "/engine.js");
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.servlet.FileHandler#getSearchReplacePairs()
     */
    @Override
    public Map<String, String> getSearchReplacePairs()
    {
        HttpServletRequest request = WebContextFactory.get().getHttpServletRequest();
        Map<String, String> replace = new HashMap<String, String>();

        // If we are dynamic then we might need to pre-configure some variables.
        boolean streaming = true;

        // If the maxWaitAfterWrite time is less than half a second then we
        // count ourselves to be not streaming, and use the simple XHR
        // connection method.
        if (maxWaitAfterWrite > -1 && maxWaitAfterWrite < 500)
        {
            streaming = false;
        }

        // If the ServerLoadMonitor says no streaming, then obviously ...
        if (!serverLoadMonitor.supportsStreaming())
        {
            streaming = false;
        }

        // Poll using XHR (to avoid IE clicking) if we close
        // the connection than 1sec after output happens.
        String pollWithXhr = streaming ? "false" : "true";
        replace.put("${pollWithXhr}", pollWithXhr);

        // What is the replacement field we use to tell engine.js what we are using
        // for script tag hack protection
        String path = request.getContextPath() + request.getServletPath();
        if (overridePath != null)
        {
            path = overridePath;
        }
        replace.put("${defaultPath}", path);

        // Under what cookie name is the session id stored?
        replace.put("${sessionCookieName}", sessionCookieName);

        // Does engine.js do GETs for Safari
        replace.put("${allowGetForSafariButMakeForgeryEasier}", String.valueOf(allowGetForSafariButMakeForgeryEasier));

        // What is the replacement field we use to tell engine.js what we are
        // using for script tag hack protection
        replace.put("${scriptTagProtection}", scriptTagProtection);

        return replace;
    }

    /**
     * Are we supporting streaming?
     * @param serverLoadMonitor the serverLoadMonitor to set
     */
    public void setServerLoadMonitor(ServerLoadMonitor serverLoadMonitor)
    {
        this.serverLoadMonitor = serverLoadMonitor;
    }

    /**
     * @param allowGetForSafariButMakeForgeryEasier Do we reduce security to help Safari
     */
    public void setAllowGetForSafariButMakeForgeryEasier(boolean allowGetForSafariButMakeForgeryEasier)
    {
        this.allowGetForSafariButMakeForgeryEasier = allowGetForSafariButMakeForgeryEasier;
    }

    /**
     * What is the string we use for script tag hack protection
     * @param scriptTagProtection the scriptTagProtection to set
     */
    public void setScriptTagProtection(String scriptTagProtection)
    {
        this.scriptTagProtection = scriptTagProtection;
    }

    /**
     * If we need to override the default path
     * @param overridePath The new override path
     */
    public void setOverridePath(String overridePath)
    {
        this.overridePath = overridePath;
    }

    /**
     * Alter the session cookie name from the default JSESSIONID.
     * @param sessionCookieName the sessionCookieName to set
     */
    public void setSessionCookieName(String sessionCookieName)
    {
        this.sessionCookieName = sessionCookieName;
    }

    /**
     * Sometimes with proxies, you need to close the stream all the time to
     * make the flush work. A value of -1 indicated that we do not do early
     * closing after writes.
     * @param maxWaitAfterWrite the maxWaitAfterWrite to set
     */
    public void setMaxWaitAfterWrite(int maxWaitAfterWrite)
    {
        this.maxWaitAfterWrite = maxWaitAfterWrite;
    }

    /**
     * The session cookie name
     */
    private String sessionCookieName = "JSESSIONID";

    /**
     * Sometimes with proxies, you need to close the stream all the time to
     * make the flush work. A value of -1 indicated that we do not do early
     * closing after writes.
     * See also: org.directwebremoting.dwrp.PollHandler.maxWaitAfterWrite
     */
    private int maxWaitAfterWrite = -1;

    /**
     * If we need to override the default path
     */
    private String overridePath = null;

    /**
     * By default we disable GET, but this hinders old Safaris
     */
    private boolean allowGetForSafariButMakeForgeryEasier = false;

    /**
     * What is the string we use for script tag hack protection
     */
    private String scriptTagProtection = DwrConstants.SCRIPT_TAG_PROTECTION;

    /**
     * Are we supporting streaming?
     */
    private ServerLoadMonitor serverLoadMonitor;
}
