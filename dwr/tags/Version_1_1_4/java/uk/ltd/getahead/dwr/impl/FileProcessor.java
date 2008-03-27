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
package uk.ltd.getahead.dwr.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import uk.ltd.getahead.dwr.AbstractDWRServlet;
import uk.ltd.getahead.dwr.util.JavascriptUtil;
import uk.ltd.getahead.dwr.util.Logger;

/**
 * A file servlet component that does some very limitted.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class FileProcessor
{
    /**
     * Basically a file servlet component that does some <b>very limitted</b>
     * EL type processing on the file. See the source for the cheat.
     * @param req The request from the browser
     * @param resp The response channel
     * @param path The path to search for, process and output
     * @param mimeType The mime type to use for this output file
     * @throws IOException If writing to the output fails
     */
    protected void doFile(HttpServletRequest req, HttpServletResponse resp, String path, String mimeType) throws IOException
    {
        if (isUpToDate(req, path))
        {
            resp.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
            return;
        }

        String output = null;

        synchronized (scriptCache)
        {
            output = (String) scriptCache.get(path);
            if (output == null)
            {
                StringBuffer buffer = new StringBuffer();

                String resource = AbstractDWRServlet.PACKAGE + path;
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

                    buffer.append(line);
                    buffer.append('\n');
                }

                output = buffer.toString();

                if (mimeType.equals(HtmlConstants.MIME_JS) && scriptCompressed)
                {
                    output = jsutil.compress(output, compressionLevel);
                }

                scriptCache.put(path, output);
            }
        }

        resp.setContentType(mimeType);
        resp.setDateHeader(HtmlConstants.HEADER_LAST_MODIFIED, servletContainerStartTime);
        resp.setHeader(HtmlConstants.HEADER_ETAG, etag);

        PrintWriter out = resp.getWriter();
        out.println(output);
        out.flush();
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

        long modifiedSince = -1;
        try
        {
            // HACK: Webfear appears to get confused sometimes
            modifiedSince = req.getDateHeader(HtmlConstants.HEADER_IF_MODIFIED);
        }
        catch (RuntimeException ex)
        {
            log.warn("Websphere/RAD date failure. If you understand why this might happen please report to dwr-users mailing list"); //$NON-NLS-1$
        }

        if (modifiedSince != -1)
        {
            // Browsers are only accurate to the second
            modifiedSince -= modifiedSince % 1000;
        }
        String givenEtag = req.getHeader(HtmlConstants.HEADER_IF_NONE);

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
     * @return Returns the ignoreLastModified.
     */
    public boolean isIgnoreLastModified()
    {
        return ignoreLastModified;
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
     * Do we ignore all the Last-Modified/ETags blathering?
     */
    private boolean ignoreLastModified = false;

    /**
     * How much do we compression javascript by?
     */
    private int compressionLevel = JavascriptUtil.LEVEL_DEBUGGABLE;

    /**
     * Do we retain comments and unneeded spaces in Javascript code?
     */
    private boolean scriptCompressed = true;

    /**
     * We cache the script output for speed
     */
    private final Map scriptCache = new HashMap();

    /**
     * The means by which we strip comments
     */
    private JavascriptUtil jsutil = new JavascriptUtil();

    /**
     * The log stream
     */
    private static final Logger log = Logger.getLogger(FileProcessor.class);
}
