package org.directwebremoting.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.directwebremoting.extend.DebugPageGenerator;
import org.directwebremoting.extend.Handler;
import org.directwebremoting.util.LocalUtil;
import org.directwebremoting.util.MimeConstants;

/**
 * A handler for requests to create a class debug page
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class TestHandler implements Handler
{
    /* (non-Javadoc)
     * @see org.directwebremoting.Handler#handle(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    public void handle(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        String scriptName = request.getPathInfo();
        scriptName = scriptName.replace(testHandlerUrl, "");
        while(scriptName.endsWith("/"))
        {
            scriptName = scriptName.substring(0, scriptName.length() - 1);
        }
        if (!LocalUtil.isValidScriptName(scriptName))
        {
            throw new SecurityException("Illegal script name.");
        }

        String page = debugPageGenerator.generateTestPage(request.getContextPath() + request.getServletPath(), scriptName);

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

    /**
     * Setter for the URL that this handler available on
     * @param testHandlerUrl the testHandlerUrl to set
     */
    public void setTestHandlerUrl(String testHandlerUrl)
    {
        this.testHandlerUrl = testHandlerUrl;
    }

    /**
     * What URL is this handler available on?
     */
    protected String testHandlerUrl;
}
