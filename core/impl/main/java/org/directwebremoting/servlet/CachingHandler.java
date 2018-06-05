package org.directwebremoting.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.directwebremoting.extend.Handler;
import org.directwebremoting.util.LocalUtil;

/**
 * A handler that deals with ETags and other nonsense to do with keeping a
 * browsers cache in-sync with a web server.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public abstract class CachingHandler implements Handler
{
    /* (non-Javadoc)
     * @see org.directwebremoting.Handler#handle(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    public void handle(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        long lastModified = getLastModifiedTime();

        // Is the browser in sync with our latest?
        if (isUpToDate(request, lastModified))
        {
            response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
            return;
        }

        // Is our cache up to date WRT the real resource?
        CachedResource resource;
        synchronized (scriptCache)
        {
            String url = getCachingKey(request);
            resource = scriptCache.get(url);

            if (resource == null || lastModified > resource.lastModifiedTime)
            {
                if (log.isDebugEnabled())
                {
                    if (resource == null)
                    {
                        log.debug("Generating contents for " + url + ". It is not currently cached." );
                    }
                    else
                    {
                        log.debug("Generating contents for " + url + ". Resource modtime=" + lastModified + ". Cached modtime");
                    }
                }

                resource = new CachedResource();
                resource.contents = generateCachableContent(request.getContextPath(), request.getServletPath(), request.getPathInfo());
                resource.lastModifiedTime = lastModified;
                scriptCache.put(url, resource);
            }
        }

        response.setContentType(mimeType);
        response.setDateHeader(HttpConstants.HEADER_LAST_MODIFIED, lastModified);
        response.setHeader(HttpConstants.HEADER_ETAG, "\"" + lastModified + '\"');

        if (resource.contents != null)
        {
            PrintWriter out = response.getWriter();
            out.println(resource.contents);
        }
        else
        {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    /**
     * Detect the last time, after which we are sure that the resource has not
     * changed
     * @return The last modification time
     */
    protected abstract long getLastModifiedTime();

    /**
     * Create a String which can be cached and sent as a 302. Returning null
     * signals that the resource doesn't exist and should result in a 404.
     * @param contextPath
     * @param servletPath
     * @param pathInfo
     * @return The string to output for this resource
     * @throws IOException
     */
    public abstract String generateCachableContent(String contextPath, String servletPath, String pathInfo) throws IOException;

    /**
     * Do we need to send the content for this file
     * @param req The HTTP request
     * @return true iff the ETags and If-Modified-Since headers say we have not changed
     */
    protected boolean isUpToDate(HttpServletRequest req, long lastModified)
    {
        String etag = "\"" + lastModified + '\"';

        if (ignoreLastModified)
        {
            return false;
        }

        long modifiedSince = -1;
        try
        {
            // HACK: Websphere appears to get confused sometimes
            modifiedSince = req.getDateHeader(HttpConstants.HEADER_IF_MODIFIED);
        }
        catch (RuntimeException ex)
        {
            // TODO: Check for "length" and re-parse
            // Normally clients send If-Modified-Since in rfc-complaint form
            // ("If-Modified-Since: Tue, 13 Mar 2007 13:11:09 GMT") some proxies
            // or browsers add length to this header so it comes like
            // ("If-Modified-Since: Tue, 13 Mar 2007 13:11:09 GMT; length=35946")
            // Servlet spec says container can throw IllegalArgumentException
            // if header value can not be parsed as http-date.
            // We might want to check for "; length=" and then do our own parsing
            // See: bug DWR-20
            // And: http://www-1.ibm.com/support/docview.wss?uid=swg1PK20062
        }

        if (modifiedSince != -1)
        {
            // Browsers are only accurate to the second
            modifiedSince -= modifiedSince % 1000;
        }
        String givenEtag = req.getHeader(HttpConstants.HEADER_IF_NONE);
        String cachedPath = getCachingKey(req);

        // Deal with missing etags
        if (givenEtag == null)
        {
            // There is no ETag, just go with If-Modified-Since
            if (modifiedSince >= lastModified)
            {
                if (log.isDebugEnabled())
                {
                    log.debug("Sending 304 for " + cachedPath + " If-Modified-Since=" + modifiedSince + ", Last-Modified=" + lastModified);
                }
                return true;
            }

            // There are no modified settings, carry on
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
                    log.debug("Sending 304 for " + cachedPath + ", If-Modified-Since=-1, Old ETag=" + givenEtag + ", New ETag=" + etag);
                }
                return true;
            }

            // There are no modified settings, carry on
            return false;
        }

        // Do both values indicate that we are in-date?
        if (etag.equals(givenEtag) && modifiedSince >= lastModified)
        {
            if (log.isDebugEnabled())
            {
                log.debug("Sending 304 for " + cachedPath + ", If-Modified-Since=" + modifiedSince + ", Last Modified=" + lastModified + ", Old ETag=" + givenEtag + ", New ETag=" + etag);
            }
            return true;
        }

        log.debug("Sending content for " + cachedPath + ", If-Modified-Since=" + modifiedSince + ", Last Modified=" + lastModified + ", Old ETag=" + givenEtag + ", New ETag=" + etag);
        return false;
    }

	/**
	 * Returns the caching key which is based on the servlet path (DWR-470)
	 * as well as the  cachedPath.
     *
     * @param request
     */
    protected String getCachingKey(HttpServletRequest request)
    {
        String absolutePath = LocalUtil.getFullUrlToDwrServlet(request);
        if (null != request.getPathInfo()) {
            absolutePath += request.getPathInfo();
        }
        return absolutePath;
    }

    /**
     * @param ignoreLastModified The ignoreLastModified to set.
     */
    public void setIgnoreLastModified(boolean ignoreLastModified)
    {
        this.ignoreLastModified = ignoreLastModified;
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
     * @return the current mime type
     */
    public String getMimeType()
    {
        return mimeType;
    }

    /**
     * The mime type to send the output under
     */
    private String mimeType;

    /**
     * We cache the script output for speed
     */
    private final Map<String, CachedResource> scriptCache = new HashMap<String, CachedResource>();

    /**
     * I wish Java had tuples
     */
    class CachedResource
    {
        protected String contents;
        protected long lastModifiedTime;
    }

    /**
     * Do we ignore all the Last-Modified/ETags blathering?
     */
    private boolean ignoreLastModified = false;

    /**
     * The log stream
     */
    private static final Log log = LogFactory.getLog(CachingHandler.class);
}
