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

import org.directwebremoting.extend.DwrConstants;
import org.directwebremoting.extend.Handler;
import org.directwebremoting.util.IdGenerator;
import org.directwebremoting.util.JavascriptUtil;
import org.directwebremoting.util.LocalUtil;
import org.directwebremoting.util.MimeConstants;

/**
 * Basically a file servlet component that does some <b>very limitted</b>
 * EL type processing on the file. See the source for the cheat.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class FileHandler implements Handler
{
    /**
     * Create a new FileHandler
     * @param filePath The filePath to search for, process and output
     * @param mimeType The mime type to use for this output file
     * @param dynamic Should the script be recalculated each time?
     */
    public FileHandler(String filePath, String mimeType, boolean dynamic)
    {
        this.filePath = filePath;
        this.mimeType = mimeType;
        this.dynamic = dynamic;
    }

    /**
     * Create a new FileHandler
     */
    public FileHandler()
    {
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.Handler#handle(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    public void handle(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        if (dynamic)
        {
            response.setHeader("pragma", "public");
            response.setHeader("Expires", "0");
            response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
        }

        if (!dynamic && isUpToDate(request))
        {
            response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
            return;
        }

        String output;

        synchronized (scriptCache)
        {
            output = (String) scriptCache.get(filePath);
            if (output == null)
            {
                StringBuffer buffer = new StringBuffer();

                String resource = DwrConstants.PACKAGE + filePath;
                InputStream raw = getClass().getResourceAsStream(resource);
                if (raw == null)
                {
                    throw new IOException("Failed to find resource: " + resource);
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
                        if (line.indexOf(PARAM_SCRIPT_COOKIENAME) != -1)
                        {
                            line = LocalUtil.replace(line, PARAM_SCRIPT_COOKIENAME, sessionCookieName);
                        }

                        if (line.indexOf(PARAM_SCRIPT_SESSIONID) != -1)
                        {
                            line = LocalUtil.replace(line, PARAM_SCRIPT_SESSIONID, generator.generateId(pageIdLength));
                        }

                        if (line.indexOf(PARAM_SCRIPT_ALLOWGET) != -1)
                        {
                            line = LocalUtil.replace(line, PARAM_SCRIPT_ALLOWGET, String.valueOf(allowGetForSafariButMakeForgeryEasier));
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
                    scriptCache.put(filePath, output);
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
     * @return true iff the ETags and If-Modified-Since headers say we have not changed
     */
    private boolean isUpToDate(HttpServletRequest req)
    {
        if (ignoreLastModified)
        {
            return false;
        }

        long modifiedSince = -1;
        try
        {
            // HACK: Webfear appears to get confused sometimes
            modifiedSince = req.getDateHeader(HttpConstants.HEADER_IF_MODIFIED);
        }
        catch (RuntimeException ex)
        {
            UrlProcessor.log.warn("Websphere/RAD date failure. If you understand why this might happen please report to dwr-users mailing list");
        }

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
                if (UrlProcessor.log.isDebugEnabled())
                {
                    UrlProcessor.log.debug("Sending 304 for " + filePath + " If-Modified-Since=" + modifiedSince + ", Last-Modified=" + servletContainerStartTime);
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
                if (UrlProcessor.log.isDebugEnabled())
                {
                    UrlProcessor.log.debug("Sending 304 for " + filePath + " Old ETag=" + givenEtag + ", New ETag=" + etag);
                }
                return true;
            }

            // There are no modified setttings, carry on
            return false;
        }

        // Do both values indicate that we are in-date?
        if (etag.equals(givenEtag) && modifiedSince <= servletContainerStartTime)
        {
            if (UrlProcessor.log.isDebugEnabled())
            {
                UrlProcessor.log.debug("Sending 304 for " + filePath);
            }
            return true;
        }

        return false;
    }

    /**
     * @param allowGetForSafariButMakeForgeryEasier Do we reduce security to help Safari
     */
    public void setAllowGetForSafariButMakeForgeryEasier(boolean allowGetForSafariButMakeForgeryEasier)
    {
        this.allowGetForSafariButMakeForgeryEasier = allowGetForSafariButMakeForgeryEasier;
    }

    /**
     * @param ignoreLastModified The ignoreLastModified to set.
     */
    public void setIgnoreLastModified(boolean ignoreLastModified)
    {
        this.ignoreLastModified = ignoreLastModified;
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
     * @param filePath the filePath to set
     */
    public void setFilePath(String filePath)
    {
        this.filePath = filePath;
    }

    /**
     * Are we expected to do the minor EL type processing?
     * @param dynamic the dynamic to set
     */
    public void setDynamic(boolean dynamic)
    {
        this.dynamic = dynamic;
    }

    /**
     * The mime type to send the output under
     * @param mimeType the mimeType to set
     */
    public void setMimeType(String mimeType)
    {
        this.mimeType = mimeType;
    }

    /**
     * By default we disable GET, but this hinders old Safaris
     */
    private boolean allowGetForSafariButMakeForgeryEasier = false;

    /**
     * Do we ignore all the Last-Modified/ETags blathering?
     */
    protected boolean ignoreLastModified = false;

    /**
     * The session cookie name
     */
    protected String sessionCookieName = "JSESSIONID";

    /**
     * How much do we compression javascript by?
     */
    protected int compressionLevel = JavascriptUtil.LEVEL_DEBUGGABLE;

    /**
     * Do we retain comments and unneeded spaces in Javascript code?
     */
    protected boolean scriptCompressed = false;

    /**
     * The method by which we get new page ids
     */
    protected IdGenerator generator = new IdGenerator();

    /**
     * The page id length
     */
    protected int pageIdLength = 16;

    /**
     * We cache the script output for speed
     */
    protected final Map scriptCache = new HashMap();

    /**
     * The file filePath and resource filePath (minus org.directwebremoting) to read from
     */
    private String filePath;

    /**
     * The mime type to send the output under
     */
    private String mimeType;

    /**
     * Are we expected to do the minor EL type processing?
     */
    private boolean dynamic;

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

        etag = "\"" + servletContainerStartTime + '\"';
    }

    /**
     * Does engine.js do GETs for Safari
     */
    protected static final String PARAM_SCRIPT_ALLOWGET = "${allowGetForSafariButMakeForgeryEasier}";

    /**
     * The page id parameter that goes in engine.js
     */
    protected static final String PARAM_SCRIPT_SESSIONID = "${scriptSessionId}";

    /**
     * Under what cookie name is the session id stored?
     */
    protected static final String PARAM_SCRIPT_COOKIENAME = "${sessionCookieName}";
}
