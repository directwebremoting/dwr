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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.directwebremoting.Calls;
import org.directwebremoting.Container;
import org.directwebremoting.DebugPageGenerator;
import org.directwebremoting.DwrConstants;
import org.directwebremoting.Remoter;
import org.directwebremoting.Replies;
import org.directwebremoting.ScriptSessionManager;
import org.directwebremoting.WebContextFactory;
import org.directwebremoting.dwrp.DwrpHtmlJsMarshaller;
import org.directwebremoting.dwrp.DwrpPlainJsMarshaller;
import org.directwebremoting.impl.DefaultScriptSessionManager;
import org.directwebremoting.util.IdGenerator;
import org.directwebremoting.util.JavascriptUtil;
import org.directwebremoting.util.LocalUtil;
import org.directwebremoting.util.Logger;
import org.directwebremoting.util.MimeConstants;

/**
 * This is the main servlet that handles all the requests to DWR.
 * <p>It is on the large side because it can't use technologies like JSPs etc
 * since it all needs to be deployed in a single jar file, and while it might be
 * possible to integrate Velocity or similar I think simplicity is more
 * important, and there are only 2 real pages both script heavy in this servlet
 * anyway.</p>
 * <p>There are 5 things to do, in the order that you come across them:</p>
 * <ul>
 * <li>The index test page that points at the classes</li>
 * <li>The class test page that lets you execute methods</li>
 * <li>The interface javascript that uses the engine to send requests</li>
 * <li>The engine javascript to form the iframe request and process replies</li>
 * <li>The exec 'page' that executes the method and returns data to the iframe</li>
 * </ul>
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class UrlProcessor
{
    /**
     * Handle servlet requests aimed at DWR
     * @param request The servlet request
     * @param response The servlet response
     * @throws IOException If there are IO issues
     */
    public void handle(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        try
        {
            String pathInfo = request.getPathInfo();
            String servletPath = request.getServletPath();
            String contextPath = request.getContextPath();

            if (nullPathInfoWorkaround && pathInfo == null)
            {
                pathInfo = request.getServletPath();
                servletPath = PathConstants.PATH_ROOT;
                log.debug("Default servlet suspected. pathInfo=" + pathInfo + "; contextPath=" + contextPath + "; servletPath=" + servletPath); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            }

            if (pathInfo == null ||
                pathInfo.length() == 0 ||
                pathInfo.equals(PathConstants.PATH_ROOT))
            {
                response.sendRedirect(contextPath + servletPath + PathConstants.FILE_INDEX);
            }
            else if (pathInfo.startsWith(PathConstants.FILE_INDEX))
            {
                String page = debugPageGenerator.generateIndexPage(contextPath + servletPath);

                response.setContentType(MimeConstants.MIME_HTML);
                PrintWriter out = response.getWriter();
                out.print(page);
            }
            else if (pathInfo.startsWith(PathConstants.PATH_TEST))
            {
                String scriptName = pathInfo;
                scriptName = LocalUtil.replace(scriptName, PathConstants.PATH_TEST, ""); //$NON-NLS-1$
                scriptName = LocalUtil.replace(scriptName, PathConstants.PATH_ROOT, ""); //$NON-NLS-1$

                String page = debugPageGenerator.generateTestPage(contextPath + servletPath, scriptName);

                response.setContentType(MimeConstants.MIME_HTML);
                PrintWriter out = response.getWriter();
                out.print(page);
            }
            else if (pathInfo.startsWith(PathConstants.PATH_INTERFACE))
            {
                String scriptName = pathInfo;
                scriptName = LocalUtil.replace(scriptName, PathConstants.PATH_INTERFACE, ""); //$NON-NLS-1$
                scriptName = LocalUtil.replace(scriptName, PathConstants.EXTENSION_JS, ""); //$NON-NLS-1$
                String path = contextPath + servletPath;

                String script = remoter.generateInterfaceScript(scriptName, path);

                // Officially we should use MimeConstants.MIME_JS, but if we cheat and
                // use MimeConstants.MIME_PLAIN then it will be easier to read in a
                // browser window, and will still work just fine.
                response.setContentType(MimeConstants.MIME_PLAIN);
                PrintWriter out = response.getWriter();
                out.print(script);
            }
            else if (pathInfo.startsWith(PathConstants.PATH_PLAINJS))
            {
                Calls calls = plainJsMarshaller.marshallInbound(request, response);
                Replies replies = remoter.execute(calls);
                plainJsMarshaller.marshallOutbound(replies, request, response);
            }
            else if (pathInfo.startsWith(PathConstants.PATH_HTMLJS))
            {
                Calls calls = htmlJsMarshaller.marshallInbound(request, response);
                Replies replies = remoter.execute(calls);
                htmlJsMarshaller.marshallOutbound(replies, request, response);
            }
            else if (pathInfo.equalsIgnoreCase(PathConstants.FILE_ENGINE))
            {
                doFile(request, response, PathConstants.FILE_ENGINE, MimeConstants.MIME_JS, true);
            }
            else if (pathInfo.equalsIgnoreCase(PathConstants.FILE_UTIL))
            {
                doFile(request, response, PathConstants.FILE_UTIL, MimeConstants.MIME_JS, false);
            }
            else if (pathInfo.startsWith(PathConstants.PATH_STATUS))
            {
                Container container = WebContextFactory.get().getContainer();
                ScriptSessionManager manager = (ScriptSessionManager) container.getBean(ScriptSessionManager.class.getName());
                if (manager instanceof DefaultScriptSessionManager)
                {
                    DefaultScriptSessionManager dssm = (DefaultScriptSessionManager) manager;
                    dssm.debug();
                }
            }
            else
            {
                log.warn("Page not found (" + pathInfo + "). In debug/test mode try viewing /[WEB-APP]/dwr/"); //$NON-NLS-1$ //$NON-NLS-2$
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        }
        catch (Exception ex)
        {
            log.warn("Error: " + ex); //$NON-NLS-1$
            if (ex instanceof SecurityException && log.isDebugEnabled())
            {
                log.debug("- User Agent: " + request.getHeader(HttpConstants.HEADER_USER_AGENT)); //$NON-NLS-1$
                log.debug("- Remote IP:  " + request.getRemoteAddr()); //$NON-NLS-1$
                log.debug("- Request URL:" + request.getRequestURL()); //$NON-NLS-1$
                log.debug("- Query:      " + request.getQueryString()); //$NON-NLS-1$
                log.debug("- Method:     " + request.getMethod()); //$NON-NLS-1$
   
                ex.printStackTrace();
            }

            response.setContentType(MimeConstants.MIME_HTML);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            PrintWriter out = response.getWriter();
            out.println("<html><head><title>Error</title</head><body>"); //$NON-NLS-1$
            out.println("<p><b>Error</b>: " + ex.getMessage() + "</p>"); //$NON-NLS-1$ //$NON-NLS-2$
            out.println("<p>For further information about DWR see:</p><ul>"); //$NON-NLS-1$
            out.println("<li><a href='http://getahead.ltd.uk/dwr/documentation'>DWR Documentation</a></li>"); //$NON-NLS-1$
            out.println("<li><a href='http://getahead.ltd.uk/dwr/support'>DWR Mailing List</a></li>"); //$NON-NLS-1$
            out.println("</ul>"); //$NON-NLS-1$
            out.println("<script type='text/javascript'>"); //$NON-NLS-1$
            out.println("alert('" + ex.getMessage() + "');"); //$NON-NLS-1$ //$NON-NLS-2$
            out.println("</script>"); //$NON-NLS-1$
            out.println("</body></html>"); //$NON-NLS-1$
        }
    }

    /**
     * Basically a file servlet component that does some <b>very limitted</b>
     * EL type processing on the file. See the source for the cheat.
     * @param request The request from the browser
     * @param response The response channel
     * @param path The path to search for, process and output
     * @param mimeType The mime type to use for this output file
     * @param dynamic Should the script be recalculated each time?
     * @throws IOException If writing to the output fails
     */
    protected void doFile(HttpServletRequest request, HttpServletResponse response, String path, String mimeType, boolean dynamic) throws IOException
    {
        if (!dynamic && isUpToDate(request, path))
        {
            response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
            return;
        }

        String output;

        synchronized (scriptCache)
        {
            output = (String) scriptCache.get(path);
            if (output == null)
            {
                StringBuffer buffer = new StringBuffer();

                String resource = DwrConstants.PACKAGE + path;
                InputStream raw = getClass().getResourceAsStream(resource);
                if (raw == null)
                {
                    throw new IOException("Failed to find resource: " + resource); //$NON-NLS-1$
                }

                BufferedReader in = new BufferedReader(new InputStreamReader(raw));
                while (true)
                {
                    String line = in.readLine();
                    if (line == null)
                    {
                        break;
                    }

                    if (dynamic)
                    {
                        if (line.indexOf(PARAM_HTTP_SESSIONID) != -1)
                        {
                            line = LocalUtil.replace(line, PARAM_HTTP_SESSIONID, request.getSession(true).getId());
                        }

                        if (line.indexOf(PARAM_SCRIPT_SESSIONID) != -1)
                        {
                            line = LocalUtil.replace(line, PARAM_SCRIPT_SESSIONID, generator.generateId(pageIdLength));
                        }
                    }

                    buffer.append(line);
                    buffer.append('\n');
                }

                output = buffer.toString();

                if (mimeType.equals(MimeConstants.MIME_JS) && scriptCompressed)
                {
                    output = JavascriptUtil.compress(output, compressionLevel);
                }

                if (!dynamic)
                {
                    scriptCache.put(path, output);
                }
            }
        }

        response.setContentType(mimeType);
        response.setDateHeader(HttpConstants.HEADER_LAST_MODIFIED, servletContainerStartTime);
        response.setHeader(HttpConstants.HEADER_ETAG, etag);

        PrintWriter out = response.getWriter();
        out.println(output);
    }

    /**
     * Do we need to send the conent for this file
     * @param req The HTTP request
     * @param path The file path (for debug purposes)
     * @return true iff the ETags and If-Modified-Since headers say we have not changed
     */
    private boolean isUpToDate(HttpServletRequest req, String path)
    {
        if (ignoreLastModified)
        {
            return false;
        }

        long modifiedSince = req.getDateHeader(HttpConstants.HEADER_IF_MODIFIED);
        if (modifiedSince != -1)
        {
            // Browsers are only accurate to the second
            modifiedSince -= modifiedSince % 1000;
        }
        String givenEtag = req.getHeader(HttpConstants.HEADER_IF_NONE);

        // Deal with missing etags
        if (givenEtag == null)
        {
            // There is no ETag, just go with If-Modified-Since
            if (modifiedSince > servletContainerStartTime)
            {
                if (log.isDebugEnabled())
                {
                    log.debug("Sending 304 for " + path + " If-Modified-Since=" + modifiedSince + ", Last-Modified=" + servletContainerStartTime); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                }
                return true;
            }

            // There are no modified setttings, carry on
            return false;
        }

        // Deal with missing If-Modified-Since
        if (modifiedSince == -1)
        {
            if (!etag.equals(givenEtag))
            {
                // There is an ETag, but no If-Modified-Since
                if (log.isDebugEnabled())
                {
                    log.debug("Sending 304 for " + path + " Old ETag=" + givenEtag + ", New ETag=" + etag); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                }
                return true;
            }

            // There are no modified setttings, carry on
            return false;
        }

        // Do both values indicate that we are in-date?
        if (etag.equals(givenEtag) && modifiedSince <= servletContainerStartTime)
        {
            if (log.isDebugEnabled())
            {
                log.debug("Sending 304 for " + path); //$NON-NLS-1$
            }
            return true;
        }

        return false;
    }

    /**
     * @param ignoreLastModified The ignoreLastModified to set.
     */
    public void setIgnoreLastModified(boolean ignoreLastModified)
    {
        this.ignoreLastModified = ignoreLastModified;
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
     * Setter for the remoter
     * @param remoter
     */
    public void setRemoter(Remoter remoter)
    {
        this.remoter = remoter;
    }

    /**
     *
     * @param debugPageGenerator
     */
    public void setDebugPageGenerator(DebugPageGenerator debugPageGenerator)
    {
        this.debugPageGenerator = debugPageGenerator;
    }

    /**
     * Setter for the Plain Javascript Marshaller
     * @param marshaller
     */
    public void setPlainJsMarshaller(DwrpPlainJsMarshaller marshaller)
    {
        this.plainJsMarshaller = marshaller;
    }

    /**
     * Setter for the HTML Javascript Marshaller
     * @param marshaller
     */
    public void setHtmlJsMarshaller(DwrpHtmlJsMarshaller marshaller)
    {
        this.htmlJsMarshaller = marshaller;
    }

    /**
     * Do we use our hack for when pathInfo is null?
     * @param nullPathInfoWorkaround The nullPathInfoWorkaround to set.
     */
    public void setNullPathInfoWorkaround(boolean nullPathInfoWorkaround)
    {
        this.nullPathInfoWorkaround = nullPathInfoWorkaround;
    }

    /**
     * The time on the script files
     */
    private static final long servletContainerStartTime;

    /**
     * The etag (=time for us) on the script files
     */
    private static final String etag;

    /**
     * Initialize the container start time
     */
    static
    {
        // Browsers are only accurate to the second
        long now = System.currentTimeMillis();
        servletContainerStartTime = now - (now % 1000);

        etag = "\"" + servletContainerStartTime + '\"'; //$NON-NLS-1$
    }

    /**
     * Do we use our hack for when pathInfo is null?
     * Enabling this will require you to have a / on the end of the DWR root URL
     */
    protected boolean nullPathInfoWorkaround = false;

    /**
     * The page id length
     */
    protected int pageIdLength = 16;

    /**
     * The method by which we get new page ids
     */
    protected IdGenerator generator = new IdGenerator();

    /**
     * Do we ignore all the Last-Modified/ETags blathering?
     */
    protected boolean ignoreLastModified = false;

    /**
     * How much do we compression javascript by?
     */
    protected int compressionLevel = JavascriptUtil.LEVEL_DEBUGGABLE;

    /**
     * Do we retain comments and unneeded spaces in Javascript code?
     */
    protected boolean scriptCompressed = false;

    /**
     * We cache the script output for speed
     */
    protected final Map scriptCache = new HashMap();

    /**
     * The bean to handle debug page requests
     */
    protected DebugPageGenerator debugPageGenerator = null;

    /**
     * The 'HTML Javascript' method by which objects are marshalled
     */
    protected DwrpPlainJsMarshaller plainJsMarshaller = null;

    /**
     * The 'Plain Javascript' method by which objects are marshalled
     */
    protected DwrpHtmlJsMarshaller htmlJsMarshaller = null;

    /**
     * The bean to execute remote requests and generate interfaces
     */
    protected Remoter remoter = null;

    /**
     * The session id parameter that goes in engine.js
     */
    protected static final String PARAM_HTTP_SESSIONID = "${httpSessionId}"; //$NON-NLS-1$

    /**
     * The page id parameter that goes in engine.js
     */
    protected static final String PARAM_SCRIPT_SESSIONID = "${scriptSessionId}"; //$NON-NLS-1$

    /**
     * The log stream
     */
    protected static final Logger log = Logger.getLogger(UrlProcessor.class);
}
