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
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.directwebremoting.DwrConstants;
import org.directwebremoting.WebContextBuilder;
import org.directwebremoting.WebContextFactory;
import org.directwebremoting.impl.ContainerUtil;
import org.directwebremoting.impl.DefaultContainer;
import org.directwebremoting.impl.DwrXmlConfigurator;
import org.directwebremoting.util.Logger;
import org.directwebremoting.util.ServletLoggingOutput;
import org.directwebremoting.util.VersionUtil;

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
    public void init(ServletConfig config) throws ServletException
    {
        super.init(config);

        container = new DefaultContainer();
        try
        {
            // Setup logging
            ServletLoggingOutput.setExecutionContext(this);
            String logLevel = config.getInitParameter(ContainerUtil.INIT_LOGLEVEL);
            if (logLevel != null)
            {
                ServletLoggingOutput.setLevel(logLevel);
            }
            log.info("DWR Version " + VersionUtil.getVersion() + " starting."); //$NON-NLS-1$ //$NON-NLS-2$

            ContainerUtil.setupDefaults(container);
            ContainerUtil.setupFromServletConfig(container, config);
            container.configurationFinished();

            // Cached to save looking them up
            webContextBuilder = (WebContextBuilder) container.getBean(WebContextBuilder.class.getName());
            processor = (UrlProcessor) container.getBean(UrlProcessor.class.getName());

            // Now we have set the implementations we can set the WebContext up
            WebContextFactory.setWebContextBuilder(webContextBuilder);

            webContextBuilder.set(null, null, getServletConfig(), getServletContext(), container);

            // The dwr.xml from within the JAR file.
            DwrXmlConfigurator system = new DwrXmlConfigurator();
            system.setClassResourceName(DwrConstants.FILE_DWR_XML);
            system.configure(container);

            // dwr.xml files specified in the web.xml
            boolean foundConfig = ContainerUtil.configureUsingInitParams(container, config);

            // The default dwr.xml file that sits by web.xml
            boolean skip = Boolean.valueOf(config.getInitParameter(ContainerUtil.INIT_SKIP_DEFAULT)).booleanValue();
            if (!foundConfig && !skip)
            {
                DwrXmlConfigurator local = new DwrXmlConfigurator();
                local.setServletResourceName(DwrConstants.DEFAULT_DWR_XML);
                local.configure(container);
            }
        }
        catch (Exception ex)
        {
            log.fatal("init failed", ex); //$NON-NLS-1$
            throw new ServletException(ex);
        }
        finally
        {
            webContextBuilder.unset();
            ServletLoggingOutput.unsetExecutionContext();
        }
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

            processor.handle(request, response);
        }
        finally
        {
            webContextBuilder.unset();
            ServletLoggingOutput.unsetExecutionContext();
        }
    }

    /**
     * The processor will actually handle the http requests
     */
    protected UrlProcessor processor;

    /**
     * The WebContext that keeps http objects local to a thread
     */
    protected WebContextBuilder webContextBuilder;

    /**
     * Our IoC container
     */
    protected DefaultContainer container;

    /**
     * The log stream
     */
    private static final Logger log = Logger.getLogger(DwrServlet.class);
}