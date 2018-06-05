package org.directwebremoting.servlet;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.directwebremoting.Container;
import org.directwebremoting.WebContextFactory.WebContextBuilder;
import org.directwebremoting.impl.StartupUtil;

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
 * @noinspection RefusedBequest
 */
public class DwrServlet extends HttpServlet
{
    /* (non-Javadoc)
     * @see javax.servlet.GenericServlet#init(javax.servlet.ServletConfig)
     */
    @Override
    public void init(ServletConfig servletConfig) throws ServletException
    {
        super.init(servletConfig);

        try
        {
            StartupUtil.logStartup(getClass().getSimpleName(), servletConfig);

            container = createContainer(servletConfig);

            webContextBuilder = container.getBean(WebContextBuilder.class);
            if (webContextBuilder != null)
            {
                webContextBuilder.engageThread(container, null, null);
            }

            configureContainer(container, servletConfig);
        }
        catch (ServletException ex)
        {
            throw ex;
        }
        catch (Exception ex)
        {
            log.fatal("init failed", ex);
            throw new ServletException(ex);
        }
        finally
        {
            if (webContextBuilder != null)
            {
                webContextBuilder.disengageThread();
            }
        }
    }

    /**
     * Specializations of DwrServlet might have an alternate implementation
     * of Container. This allows subclasses to override the implementation
     * method.
     * Part of {@link #init(ServletConfig)}.
     * @throws ServletException Children might need to throw even if we don't
     */
    protected Container createContainer(ServletConfig servletConfig) throws ServletException
    {
        return StartupUtil.createAndSetupDefaultContainer(servletConfig);
    }

    /**
     * Specializations of DwrServlet might want to configure it differently
     * from the default
     * Part of {@link #init(ServletConfig)}.
     */
    protected void configureContainer(Container defaultContainer, ServletConfig servletConfig) throws ServletException, IOException
    {
        try
        {
            StartupUtil.configureContainerFully(defaultContainer, servletConfig);
        }
        catch (IOException ex)
        {
            throw ex;
        }
        catch (Exception ex)
        {
            throw new ServletException(ex);
        }
    }

    /* (non-Javadoc)
     * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException
    {
        doPost(req, resp);
    }

    /* (non-Javadoc)
     * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
    {
        try
        {
            webContextBuilder.engageThread(container, request, response);
            UrlProcessor processor = container.getBean(UrlProcessor.class);
            processor.handle(request, response);
        }
        finally
        {
            webContextBuilder.disengageThread();
        }
    }

    /* (non-Javadoc)
     * @see javax.servlet.GenericServlet#destroy()
     */
    @Override
    public void destroy()
    {
        webContextBuilder = container.getBean(WebContextBuilder.class);
        if (webContextBuilder != null)
        {
            webContextBuilder.engageThread(container, null, null);
        }
        container.destroy();
        if (webContextBuilder != null)
        {
            webContextBuilder.disengageThread();
        }
        super.destroy();
    }

    /**
     * Accessor for the IoC container.
     */
    public Container getContainer()
    {
        return container;
    }

    /**
     * Our IoC container
     */
    private Container container = null;

    /**
     * The WebContext that keeps http objects local to a thread
     */
    protected WebContextBuilder webContextBuilder = null;

    /**
     * The log stream
     */
    private static final Log log = LogFactory.getLog(DwrServlet.class);
}
