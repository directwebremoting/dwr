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
package uk.ltd.getahead.dwr;

import java.util.Enumeration;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

import uk.ltd.getahead.dwr.impl.DefaultContainer;

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
public class DWRServlet extends AbstractDWRServlet
{
    /* (non-Javadoc)
     * @see uk.ltd.getahead.dwr.AbstractDWRServlet#getContainer(javax.servlet.ServletConfig)
     */
    public Container getContainer(ServletConfig config) throws ServletException
    {
        try
        {
            // Load the factory with implementation information
            DefaultContainer factory = new DefaultContainer();
            factory.addParameter(AccessControl.class.getName(), "uk.ltd.getahead.dwr.impl.DefaultAccessControl"); //$NON-NLS-1$
            factory.addParameter(Configuration.class.getName(), "uk.ltd.getahead.dwr.impl.DefaultConfiguration"); //$NON-NLS-1$
            factory.addParameter(ConverterManager.class.getName(), "uk.ltd.getahead.dwr.impl.DefaultConverterManager"); //$NON-NLS-1$
            factory.addParameter(CreatorManager.class.getName(), "uk.ltd.getahead.dwr.impl.DefaultCreatorManager"); //$NON-NLS-1$
            factory.addParameter(Processor.class.getName(), "uk.ltd.getahead.dwr.impl.DefaultProcessor"); //$NON-NLS-1$
            factory.addParameter(WebContextBuilder.class.getName(), "uk.ltd.getahead.dwr.impl.DefaultWebContextBuilder"); //$NON-NLS-1$

            factory.addParameter("index", "uk.ltd.getahead.dwr.impl.DefaultIndexProcessor"); //$NON-NLS-1$ //$NON-NLS-2$
            factory.addParameter("test", "uk.ltd.getahead.dwr.impl.DefaultTestProcessor"); //$NON-NLS-1$ //$NON-NLS-2$
            factory.addParameter("interface", "uk.ltd.getahead.dwr.impl.DefaultInterfaceProcessor"); //$NON-NLS-1$ //$NON-NLS-2$
            factory.addParameter("exec", "uk.ltd.getahead.dwr.impl.DefaultExecProcessor"); //$NON-NLS-1$ //$NON-NLS-2$
            factory.addParameter("file", "uk.ltd.getahead.dwr.impl.FileProcessor"); //$NON-NLS-1$ //$NON-NLS-2$

            factory.addParameter("debug", "false"); //$NON-NLS-1$ //$NON-NLS-2$
            factory.addParameter("allowImpossibleTests", "false"); //$NON-NLS-1$ //$NON-NLS-2$

            factory.addParameter("scriptCompressed", "true"); //$NON-NLS-1$ //$NON-NLS-2$

            Enumeration en = config.getInitParameterNames();
            while (en.hasMoreElements())
            {
                String name = (String) en.nextElement();
                String value = config.getInitParameter(name);
                factory.addParameter(name, value);
            }
            factory.configurationFinished();

            return factory;
        }
        catch (Exception ex)
        {
            throw new ServletException(ex);
        }
    }

    /* (non-Javadoc)
     * @see uk.ltd.getahead.dwr.AbstractDWRServlet#configure(javax.servlet.ServletConfig, uk.ltd.getahead.dwr.Configuration)
     */
    public void configure(ServletConfig config, Configuration configuration) throws ServletException
    {
        // Find all the init params
        Enumeration en = config.getInitParameterNames();
        boolean foundConfig = false;

        // Loop through the ones that do exist
        while (en.hasMoreElements())
        {
            String name = (String) en.nextElement();
            if (name.startsWith(INIT_CONFIG))
            {
                foundConfig = true;

                // if the init param starts with "config" then try to load it
                String configFile = config.getInitParameter(name);
                readFile(configFile, configuration);
            }
        }

        // If there are none then use the default name
        if (!foundConfig)
        {
            String skip = config.getInitParameter(INIT_SKIP_DEFAULT);
            if (!Boolean.valueOf(skip).booleanValue())
            {
                readFile(DEFAULT_DWR_XML, configuration);
            }
        }
    }
}
