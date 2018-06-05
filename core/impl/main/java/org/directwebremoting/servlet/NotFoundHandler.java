package org.directwebremoting.servlet;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;
import org.directwebremoting.extend.Handler;

/**
 * Display a 404 "not found" message
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class NotFoundHandler implements Handler
{
    /* (non-Javadoc)
     * @see org.directwebremoting.Handler#handle(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    public void handle(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        log.warn("Page not found. pathInfo='" + request.getPathInfo() + "' requestUrl='" + request.getRequestURI() + "'");
        log.warn("In debug/test mode try viewing /[WEB-APP]/dwr/");

        response.sendError(HttpServletResponse.SC_NOT_FOUND);
    }

    /**
     * The log stream
     */
    private static final Log log = LogFactory.getLog(NotFoundHandler.class);
}
