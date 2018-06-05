package org.directwebremoting.servlet;

import java.util.HashMap;
import java.util.Map;

import org.directwebremoting.extend.DwrConstants;
import org.directwebremoting.extend.Remoter;
import org.directwebremoting.extend.ScriptSessionManager;
import org.directwebremoting.extend.ServerLoadMonitor;
import org.directwebremoting.util.VersionUtil;

/**
 * A Handler that supports requests for engine.js
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class BaseEngineHandler extends FileJavaScriptHandler
{
    /**
     * Setup the default values
     */
    public BaseEngineHandler()
    {
        super(DwrConstants.PACKAGE_PATH + "/engine.js", DwrConstants.PACKAGE_PATH + "/copyright.txt");
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.servlet.FileHandler#getSearchReplacePairs()
     */
    @Override
    protected Map<String, String> getSearchReplacePairs(String contextPath, String servletPath, String pathInfo)
    {
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
        String useStreamingPoll = streaming ? "true" : "false";
        replace.put("${useStreamingPoll}", useStreamingPoll);

        // Setup paths
        replace.put("${contextPath}", contextPath);
        String contextServletPath = contextPath + servletPath;
        String pathToDwrServlet = remoter.getPathToDwrServlet(contextServletPath);
        replace.put("${pathToDwrServlet}", pathToDwrServlet);
        replace.put("${overridePath}", overridePath);
        replace.put("${overrideContextPath}", overrideContextPath);

        // Cookie config
        replace.put("${cookieAttributes}", cookieAttributes);

        // Does engine.js do GETs
        replace.put("${allowGetButMakeForgeryEasier}", String.valueOf(allowGetButMakeForgeryEasier));

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

        replace.put("${debug}", String.valueOf(debug));

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
     * @param allowGetButMakeForgeryEasier Do we reduce security
     */
    public void setAllowGetButMakeForgeryEasier(boolean allowGetButMakeForgeryEasier)
    {
        this.allowGetButMakeForgeryEasier = allowGetButMakeForgeryEasier;
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
     * If we need to override the default path
     * @param overridePath The new override path
     */
    public void setOverridePath(String overridePath)
    {
        this.overridePath = overridePath;
    }

    /**
     * If we need to override the default contextPath
     * @param overridePath The new contextPath
     */
    public void setOverrideContextPath(String overridePath)
    {
        this.overrideContextPath = overridePath;
    }

    /**
     * Extra attributes to append to the DWRSESSIONID cookie (domain, secure, etc)
     * @param attributeString attribute string according to cookie syntax
     */
    public void setCookieAttributes(String attributeString)
    {
        this.cookieAttributes = attributeString;
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
     * Sometimes with proxies, you need to close the stream all the time to
     * make the flush work. A value of -1 indicated that we do not do early
     * closing after writes.
     * See also: org.directwebremoting.dwrp.BasePollHandler.maxWaitAfterWrite
     */
    private int maxWaitAfterWrite = -1;

    /**
     * By default we disable GET
     */
    private boolean allowGetButMakeForgeryEasier = false;

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

    /**
     * If we need to override the default path
     */
    protected String overridePath = "";

    /**
     * If we need to override the default contextPath
     */
    protected String overrideContextPath = "";

    /**
     * Extra attributes to append to the DWRSESSIONID cookie (domain, secure, etc)
     */
    protected String cookieAttributes = "";
}
