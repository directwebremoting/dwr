package uk.ltd.getahead.dwr.impl;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import uk.ltd.getahead.dwr.Processor;
import uk.ltd.getahead.dwr.util.Logger;

/**
 * This is the main servlet that handles all the requests to DWR.
 * <p>It is on the large side because it can't use technologies like JSPs etc
 * since it all needs to be deployed in a single jar file, and while it might be
 * possible to integrate Velocity or similar I think simplicity is more
 * important, and there are only 2 real pages both script heavy in this servlet
 * anyway.</p>
 * <p>There are 5 things to do, in the order that you come across them:</p>
 * <ul>
 * <li>The index test page that points at the classes</li>
 * <li>The class test page that lets you execute methods</li>
 * <li>The interface javascript that uses the engine to send requests</li>
 * <li>The engine javascript to form the iframe request and process replies</li>
 * <li>The exec 'page' that executes the method and returns data to the iframe</li>
 * </ul>
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class DefaultProcessor implements Processor
{
    /* (non-Javadoc)
     * @see uk.ltd.getahead.dwr.Processor#handle(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    public void handle(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException
    {
        String pathInfo = req.getPathInfo();
        String servletPath = req.getServletPath();
        if (pathInfo == null)
        {
            pathInfo = req.getServletPath();
            servletPath = HtmlConstants.PATH_ROOT;
            log.debug("Default servlet suspected. pathInfo=" + pathInfo + "; contextPath=" + req.getContextPath() + "; servletPath=" + servletPath); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        }

        if (pathInfo == null || pathInfo.length() == 0 || pathInfo.equals(HtmlConstants.PATH_ROOT) || pathInfo.equals(req.getContextPath()))
        {
            resp.sendRedirect(req.getContextPath() + servletPath + HtmlConstants.FILE_INDEX);
        }
        else if (pathInfo.equals(req.getContextPath()))
        {
            resp.sendRedirect(req.getContextPath() + HtmlConstants.FILE_INDEX);
        }
        else if (pathInfo != null && pathInfo.startsWith(HtmlConstants.FILE_INDEX))
        {
            index.handle(req, resp);
        }
        else if (pathInfo != null && pathInfo.startsWith(HtmlConstants.PATH_TEST))
        {
            test.handle(req, resp);
        }
        else if (pathInfo != null && pathInfo.startsWith(HtmlConstants.PATH_INTERFACE))
        {
            iface.handle(req, resp);
        }
        else if (pathInfo != null && pathInfo.startsWith(HtmlConstants.PATH_EXEC))
        {
            exec.handle(req, resp);
        }
        else if (pathInfo != null && pathInfo.equalsIgnoreCase(HtmlConstants.FILE_ENGINE))
        {
            file.doFile(resp, HtmlConstants.FILE_ENGINE, HtmlConstants.MIME_JS);
        }
        else if (pathInfo != null && pathInfo.equalsIgnoreCase(HtmlConstants.FILE_UTIL))
        {
            file.doFile(resp, HtmlConstants.FILE_UTIL, HtmlConstants.MIME_JS);
        }
        else if (pathInfo != null && pathInfo.equalsIgnoreCase(HtmlConstants.FILE_DEPRECATED))
        {
            file.doFile(resp, HtmlConstants.FILE_DEPRECATED, HtmlConstants.MIME_JS);
        }
        else
        {
            log.warn("Page not found (" + pathInfo + "). In debug/test mode try viewing /[WEB-APP]/dwr/"); //$NON-NLS-1$ //$NON-NLS-2$
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    /**
     * @param exec The exec to set.
     */
    public void setExec(Processor exec)
    {
        this.exec = exec;
    }

    /**
     * @param file The file to set.
     */
    public void setFile(FileProcessor file)
    {
        this.file = file;
    }

    /**
     * @param iface The iface to set.
     */
    public void setInterface(Processor iface)
    {
        this.iface = iface;
    }

    /**
     * @param index The index to set.
     */
    public void setIndex(Processor index)
    {
        this.index = index;
    }

    /**
     * @param test The test to set.
     */
    public void setTest(Processor test)
    {
        this.test = test;
    }

    private Processor index;
    private Processor test;
    private Processor iface;
    private Processor exec;
    private FileProcessor file;

    /**
     * The log stream
     */
    private static final Logger log = Logger.getLogger(DefaultProcessor.class);
}
