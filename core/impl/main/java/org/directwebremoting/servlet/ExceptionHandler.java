package org.directwebremoting.servlet;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.directwebremoting.extend.Handler;

/**
 * Handles an exception occurring during the request dispatching.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class ExceptionHandler implements Handler
{
    /* (non-Javadoc)
     * @see org.directwebremoting.Handler#handle(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    public void handle(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        log.warn("Error: " + cause);
        log.debug("Debug for stack trace:", cause);

        try
        {
            // We are going to act on this in engine.js so we are hoping that
            // that SC_NOT_IMPLEMENTED (501) is not something that the servers
            // use that much. I would have used something unassigned like 506+
            // But that could cause future problems and might not get through
            // proxies and the like
            response.sendError(HttpServletResponse.SC_NOT_IMPLEMENTED, "Error. Details logged to the console");
        }
        catch (Exception ex)
        {
            // If the browser has gone away we expect to fail, and may not be
            // able to recover
            // Technically an IOException should work here, but Jetty appears to
            // throw an ArrayIndexOutOfBoundsException sometimes, if the browser
            // has gone away.
            log.debug("Error in error handler, the browser probably went away: " + ex);
        }
    }

    /**
     * @param cause The cause of the failure
     */
    public void setException(Exception cause)
    {
        this.cause = cause;
    }

    /**
     * The cause of the failure
     */
    private Exception cause;

    /**
     * The log stream
     */
    private static final Log log = LogFactory.getLog(ExceptionHandler.class);
}
