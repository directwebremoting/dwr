package org.directwebremoting.servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.directwebremoting.extend.Handler;
import org.directwebremoting.util.LocalUtil;

/**
 * @author Mike Wilson
 */
public class UncacheableResponse implements ResponseHandler
{
    /* (non-Javadoc)
     * @see org.directwebremoting.servlet.ResponseHandler#handle(org.directwebremoting.extend.Handler, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    public void handle(Handler handler, HttpServletRequest request, HttpServletResponse response)
    {
        LocalUtil.addNoCacheHeaders(response);
    }
}

