package org.directwebremoting.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.directwebremoting.extend.Handler;
import org.directwebremoting.util.MimeConstants;

/**
 * A handler for requests to create a debug index page
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class AboutHandler implements Handler
{
    /* (non-Javadoc)
     * @see org.directwebremoting.Handler#handle(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    public void handle(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        response.setContentType(MimeConstants.MIME_HTML);
        PrintWriter out = response.getWriter();
        out.print("<html><head><title>DWR - Easy Ajax for Java</title></head><body>");
        out.print("<p><a href='http://directwebremoting.org/dwr/'>DWR - Easy Ajax for Java</a></p>");
        out.print("</body></html>");
    }
}
