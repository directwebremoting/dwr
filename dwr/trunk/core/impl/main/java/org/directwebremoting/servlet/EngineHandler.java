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

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.directwebremoting.WebContextFactory;
import org.directwebremoting.extend.DwrConstants;
import org.directwebremoting.extend.Remoter;
import org.directwebremoting.extend.ScriptSessionManager;
import org.directwebremoting.extend.ServerLoadMonitor;
import org.directwebremoting.util.VersionUtil;

/**
 * A Handler that supports requests for engine.js
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class EngineHandler extends FileJavaScriptHandler
{
    /**
     * Setup the default values
     */
    public EngineHandler()
    {
        super(DwrConstants.PACKAGE + "/engine.js");
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
        String contextServletPath = request.getContextPath() + request.getServletPath();
        String pathToDwrServlet = remoter.getPathToDwrServlet(contextServletPath);
        replace.put("${pathToDwrServlet}", pathToDwrServlet);

        // Under what cookie name is the session id stored?
        replace.put("${sessionCookieName}", sessionCookieName);

        // Does engine.js do GETs for Safari
        replace.put("${allowGetForSafariButMakeForgeryEasier}", String.valueOf(allowGetForSafariButMakeForgeryEasier));

        // What is the replacement field we use to tell engine.js what we are
        // using for script tag hack protection
        replace.put("${scriptTagProtection}", scriptTagProtection);

        // engine.js needs to know the URLs to send requests to:
        replace.put("${plainCallHandlerUrl}", plainCallHandlerUrl);
        replace.put("${plainPollHandlerUrl}", plainPollHandlerUrl);
        replace.put("${htmlCallHandlerUrl}", htmlCallHandlerUrl);
        replace.put("${htmlPollHandlerUrl}", htmlPollHandlerUrl);

        // Do we start off with everything in Sjax mode?
        replace.put("${defaultToAsync}", String.valueOf(defaultToAsync));

        // Version numbers so clients can sort out what they're up against
        replace.put("${versionMajor}", String.valueOf(VersionUtil.getMajor()));
        replace.put("${versionMinor}", String.valueOf(VersionUtil.getMinor()));
        replace.put("${versionRevision}", String.valueOf(VersionUtil.getRevision()));
        replace.put("${versionBuild}", String.valueOf(VersionUtil.getBuild()));
        replace.put("${versionTitle}", String.valueOf(VersionUtil.getTitle()));
        replace.put("${versionLabel}", String.valueOf(VersionUtil.getLabel()));

        replace.put("${initCode}", scriptSessionManager.getInitCode());

        return replace;
    }

    /**
     * This API is primarily for JAWR to discover the tags that we are using
     * to customize our output.
     */
    public static List<String> getKeywords()
    {
        return Arrays.asList(new String[] {
            "pollWithXhr",
            "pathToDwrServlet",
            "sessionCookieName",
            "allowGetForSafariButMakeForgeryEasier",
            "scriptTagProtection",
            "plainCallHandlerUrl",
            "plainPollHandlerUrl",
            "htmlCallHandlerUrl",
            "htmlPollHandlerUrl",
            "defaultToAsync",
            "versionMajor",
            "versionMinor",
            "versionRevision",
            "versionBuild",
            "versionTitle",
            "versionLabel",
            "initCode",
        });
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
     * @param plainCallHandlerUrl the plainCallHandlerUrl to set
     */
    public void setPlainCallHandlerUrl(String plainCallHandlerUrl)
    {
        this.plainCallHandlerUrl = plainCallHandlerUrl;
    }

    /**
     * @param plainPollHandlerUrl the plainPollHandlerUrl to set
     */
    public void setPlainPollHandlerUrl(String plainPollHandlerUrl)
    {
        this.plainPollHandlerUrl = plainPollHandlerUrl;
    }

    /**
     * @param htmlCallHandlerUrl the htmlCallHandlerUrl to set
     */
    public void setHtmlCallHandlerUrl(String htmlCallHandlerUrl)
    {
        this.htmlCallHandlerUrl = htmlCallHandlerUrl;
    }

    /**
     * @param htmlPollHandlerUrl the htmlPollHandlerUrl to set
     */
    public void setHtmlPollHandlerUrl(String htmlPollHandlerUrl)
    {
        this.htmlPollHandlerUrl = htmlPollHandlerUrl;
    }

    /**
     * @param remoter the remoter to set
     */
    public void setRemoter(Remoter remoter)
    {
        this.remoter = remoter;
    }

    /**
     * @param scriptSessionManager the scriptSessionManager to set
     */
    public void setScriptSessionManager(ScriptSessionManager scriptSessionManager)
    {
        this.scriptSessionManager = scriptSessionManager;
    }

    /**
     * URL that engine.js makes calls into
     */
    private String plainCallHandlerUrl;

    /**
     * URL that engine.js makes calls into
     */
    private String plainPollHandlerUrl;

    /**
     * URL that engine.js makes calls into
     */
    private String htmlCallHandlerUrl;

    /**
     * URL that engine.js makes calls into
     */
    private String htmlPollHandlerUrl;

    /**
     * The source of the commands to we give to the client on startup?
     */
    private ScriptSessionManager scriptSessionManager;

    /**
     * The session cookie name
     */
    private String sessionCookieName = "JSESSIONID";

    /**
     * Sometimes with proxies, you need to close the stream all the time to
     * make the flush work. A value of -1 indicated that we do not do early
     * closing after writes.
     * See also: org.directwebremoting.dwrp.BasePollHandler.maxWaitAfterWrite
     */
    private int maxWaitAfterWrite = -1;

    /**
     * By default we disable GET, but this hinders old Safaris
     */
    private boolean allowGetForSafariButMakeForgeryEasier = false;

    /**
     * What is the string we use for script tag hack protection
     */
    private String scriptTagProtection;

    /**
     * Does DWR by default use synchronous XHR - i.e. Sjax
     */
    private final boolean defaultToAsync = true;

    /**
     * So we can correctly calculate the path to the DWR servlet
     */
    private Remoter remoter;

    /**
     * Are we supporting streaming?
     */
    private ServerLoadMonitor serverLoadMonitor;
}
