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

