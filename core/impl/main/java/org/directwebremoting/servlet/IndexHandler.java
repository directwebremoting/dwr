package org.directwebremoting.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.directwebremoting.extend.DebugPageGenerator;
import org.directwebremoting.extend.Handler;
import org.directwebremoting.util.MimeConstants;

/**
 * A handler for requests to create a debug index page
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class IndexHandler implements Handler
{
    /* (non-Javadoc)
     * @see org.directwebremoting.Handler#handle(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    public void handle(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        String page = debugPageGenerator.generateIndexPage(request.getContextPath() + request.getServletPath());

        response.setContentType(MimeConstants.MIME_HTML);
        PrintWriter out = response.getWriter();
        out.print(page);
    }

    /**
     * Setter for the debug page generator
     * @param debugPageGenerator The new debug page generator
     */
    public void setDebugPageGenerator(DebugPageGenerator debugPageGenerator)
    {
        this.debugPageGenerator = debugPageGenerator;
    }

    /**
     * The bean to handle debug page requests
     */
    protected DebugPageGenerator debugPageGenerator = null;
}
