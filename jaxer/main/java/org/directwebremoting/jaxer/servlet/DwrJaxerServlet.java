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
package org.directwebremoting.jaxer.servlet;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.directwebremoting.WebContextFactory.WebContextBuilder;
import org.directwebremoting.extend.DwrConstants;
import org.directwebremoting.extend.ServerLoadMonitor;
import org.directwebremoting.impl.ContainerUtil;
import org.directwebremoting.impl.DwrXmlConfigurator;
import org.directwebremoting.impl.StartupUtil;
import org.directwebremoting.jaxer.impl.JaxerContainer;
import org.directwebremoting.servlet.UrlProcessor;

/**
 * This is the main servlet for the DWR/Jaxer integration.
 * It handles all the requests to DWR from the Jaxer server. Currently this
 * communication looks just like normal DWR, however as this project evolves
 * it is likely that some of the processing done by {@link UrlProcessor} will
 * be superseded by a protocol that takes advantage of a single connection.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 * @noinspection RefusedBequest
 */
public class DwrJaxerServlet extends HttpServlet
{
    /* (non-Javadoc)
     * @see javax.servlet.GenericServlet#init(javax.servlet.ServletConfig)
     */
    @Override
    public void init(ServletConfig servletConfig) throws ServletException
    {
        super.init(servletConfig);
        ServletContext servletContext = servletConfig.getServletContext();

        try
        {
            ContainerUtil.resolveMultipleImplementations(container, servletConfig);
            container.setupFinished();

            StartupUtil.initContainerBeans(servletConfig, servletContext, container);
            webContextBuilder = container.getBean(WebContextBuilder.class);

            ContainerUtil.prepareForWebContextFilter(servletContext, servletConfig, container, webContextBuilder, this);

            DwrXmlConfigurator system = new DwrXmlConfigurator();
            system.setClassResourceName(DwrConstants.FILE_DWR_XML);
            system.configure(container);

            DwrXmlConfigurator custom = new DwrXmlConfigurator();
            custom.setClassResourceName("/org/directwebremoting/jaxer/dwr.xml");
            custom.configure(container);

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
        }
    }

    /* (non-Javadoc)
     * @see javax.servlet.GenericServlet#destroy()
     */
    @Override
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
        ServerLoadMonitor monitor = container.getBean(ServerLoadMonitor.class);
        monitor.shutdown();
    }

    /* (non-Javadoc)
     * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException
    {
        doPost(req, resp);
    }

    /* (non-Javadoc)
     * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
    {
        try
        {
            webContextBuilder.set(request, response, getServletConfig(), getServletContext(), container);

            UrlProcessor processor = container.getBean(UrlProcessor.class);
            processor.handle(request, response);
        }
        finally
        {
            webContextBuilder.unset();
        }
    }

    /**
     * Our IoC container
     */
    private JaxerContainer container = new JaxerContainer();

    /**
     * The WebContext that keeps http objects local to a thread
     */
    private WebContextBuilder webContextBuilder = null;

    /**
     * The log stream
     */
    private static final Log log = LogFactory.getLog(DwrJaxerServlet.class);
}
