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
import javax.servlet.ServletException;

import org.directwebremoting.Container;
import org.directwebremoting.extend.DwrConstants;
import org.directwebremoting.impl.DwrXmlConfigurator;
import org.directwebremoting.impl.StartupUtil;
import org.directwebremoting.jaxer.impl.JaxerContainer;
import org.directwebremoting.servlet.DwrServlet;
import org.directwebremoting.servlet.UrlProcessor;

/**
 * This is the main servlet for the DWR/Jaxer integration.
 * It handles all the requests to DWR from the Jaxer server. Currently this
 * communication looks just like normal DWR, however as this project evolves
 * it is likely that some of the processing done by {@link UrlProcessor} will
 * be superseded by a protocol that takes advantage of a single connection.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class DwrJaxerServlet extends DwrServlet
{
    /**
     * Specializations of DwrServlet might have an alternate implementation
     * of Container. This allows subclasses to override the implementation
     * method.
     * Part of {@link #init(ServletConfig)}.
     * @throws ServletException Children might need to throw even if we don't
     */
    @Override
    protected Container createContainer(ServletConfig servletConfig) throws ServletException
    {
        JaxerContainer container = new JaxerContainer();
        StartupUtil.resolveMultipleImplementations(container, servletConfig);
        container.setupFinished();
        return container;
    }

    /**
     * Specializations of DwrServlet might want to configure it differently
     * from the default
     * Part of {@link #init(ServletConfig)}.
     */
    @Override
    protected void configureContainer(Container container, ServletConfig servletConfig) throws ServletException, IOException
    {
        try
        {
            DwrXmlConfigurator system = new DwrXmlConfigurator();
            system.setClassResourceName(DwrConstants.FILE_DWR_XML);
            system.configure(container);

            DwrXmlConfigurator custom = new DwrXmlConfigurator();
            custom.setClassResourceName("/org/directwebremoting/jaxer/dwr.xml");
            custom.configure(container);
        }
        catch (Exception ex)
        {
            throw new ServletException(ex);
        }
    }
}
