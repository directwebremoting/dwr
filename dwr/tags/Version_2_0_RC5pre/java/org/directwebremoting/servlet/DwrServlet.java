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

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.directwebremoting.Container;
import org.directwebremoting.WebContextFactory.WebContextBuilder;
import org.directwebremoting.extend.ServerLoadMonitor;
import org.directwebremoting.impl.ContainerUtil;
import org.directwebremoting.impl.DefaultContainer;
import org.directwebremoting.impl.StartupUtil;
import org.directwebremoting.util.Logger;
import org.directwebremoting.util.ServletLoggingOutput;

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
    public void init(ServletConfig servletConfig) throws ServletException
    {
        super.init(servletConfig);
        ServletContext servletContext = servletConfig.getServletContext();

        try
        {
            // setupLogging() only needed for servlet logging if commons-logging is unavailable
            // logStartup() just outputs some version numbers
            StartupUtil.setupLogging(servletConfig, this);
            StartupUtil.logStartup(servletConfig);

            // create and setup a DefaultContainer
            container = ContainerUtil.createDefaultContainer(servletConfig);
            ContainerUtil.setupDefaultContainer(container, servletConfig);

            webContextBuilder = StartupUtil.initWebContext(servletConfig, servletContext, container);
            StartupUtil.initServerContext(servletConfig, servletContext, container);

            ContainerUtil.prepareForWebContextFilter(servletContext, servletConfig, container, webContextBuilder, this);
            ContainerUtil.configureContainerFully(container, servletConfig);
            ContainerUtil.publishContainer(container, servletConfig);
        }
        catch (ExceptionInInitializerError ex)
        {
            log.fatal("ExceptionInInitializerError. Nested exception:", ex.getException());
            throw new ServletException(ex);
        }
        catch (Exception ex)
        {
            log.fatal("DwrServlet.init() failed", ex);
            throw new ServletException(ex);
        }
        finally
        {
            if (webContextBuilder != null)
            {
                webContextBuilder.unset();
            }

            ServletLoggingOutput.unsetExecutionContext();
        }
    }

    /* (non-Javadoc)
     * @see javax.servlet.GenericServlet#destroy()
     */
    public void destroy()
    {
        shutdown();
        super.destroy();
    }

    /**
     * Kill all comet polls.
     * <p>Technically a servlet engine ought to call this only when all the
     * threads are already removed, however at least Tomcat doesn't do this
     * properly (it waits for a while and then calls destroy anyway).
     * <p>It would be good if we could get {@link #destroy()} to call this
     * method however destroy() is only called once all threads are done so it's
     * too late.
     */
    public void shutdown()
    {
        ServerLoadMonitor monitor = (ServerLoadMonitor) container.getBean(ServerLoadMonitor.class.getName());
        monitor.shutdown();
    }

    /* (non-Javadoc)
     * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException
    {
        doPost(req, resp);
    }

    /* (non-Javadoc)
     * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
    {
        try
        {
            webContextBuilder.set(request, response, getServletConfig(), getServletContext(), container);
            ServletLoggingOutput.setExecutionContext(this);

            UrlProcessor processor = (UrlProcessor) container.getBean(UrlProcessor.class.getName());
            processor.handle(request, response);
        }
        finally
        {
            webContextBuilder.unset();
            ServletLoggingOutput.unsetExecutionContext();
        }
    }

    /**
     * Accessor for the DWR IoC container.
     * @return DWR's IoC container
     */
    public Container getContainer()
    {
        return container;
    }

    /**
     * Our IoC container
     */
    private DefaultContainer container;

    /**
     * The WebContext that keeps http objects local to a thread
     */
    private WebContextBuilder webContextBuilder;

    /**
     * The log stream
     */
    public static final Logger log = Logger.getLogger(DwrServlet.class);
}
