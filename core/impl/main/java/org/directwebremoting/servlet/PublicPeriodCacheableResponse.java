package org.directwebremoting.servlet;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.directwebremoting.extend.Handler;
import org.directwebremoting.util.LocalUtil;

/**
 * @author Mike Wilson
 */
public class PublicPeriodCacheableResponse implements ResponseHandler
{
    /* (non-Javadoc)
     * @see org.directwebremoting.servlet.ResponseHandler#handle(org.directwebremoting.extend.Handler, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    public void handle(Handler handler, HttpServletRequest request, HttpServletResponse response)
    {
        if (periodCacheableSecs == 0)
        {
            LocalUtil.addNoCacheHeaders(response);
            return;
        }

        int cacheSecs;

        if (periodCacheableSecs < 0)
        {
            // Defaults
            if (debug)
            {
                cacheSecs = 1*60; // 1 minute
            }
            else
            {
                cacheSecs = 5*60; // 5 minutes
            }
        }
        else
        {
            // Explicit setting by developer
            cacheSecs = periodCacheableSecs;
        }

        long expiry = new Date().getTime() + cacheSecs*1000;

        // Set standard HTTP/1.1 cache headers.
        response.setHeader("Cache-Control", "public, max-age=" + cacheSecs);

        // Set to expire far in the past. Prevents caching at the proxy server
        response.setDateHeader("Expires", expiry);
    }

    public void setPeriodCacheableTime(int secs)
    {
        this.periodCacheableSecs = secs;
    }

    public void setDebug(boolean debug)
    {
        this.debug = debug;
    }

    private int periodCacheableSecs = -1;

    private boolean debug = false;
}

