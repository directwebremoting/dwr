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
package org.directwebremoting.impl;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.xml.parsers.ParserConfigurationException;

import org.directwebremoting.Container;
import org.directwebremoting.WebContextFactory.WebContextBuilder;
import org.directwebremoting.annotations.AnnotationsConfigurator;
import org.directwebremoting.extend.Configurator;
import org.directwebremoting.extend.ConverterManager;
import org.directwebremoting.util.FakeServletConfig;
import org.directwebremoting.util.FakeServletContext;
import org.xml.sax.SAXException;

/**
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class TestEnvironment
{
    /**
     * Configure from a dwr.xml file given as a class resource name
     */
    public static void configureFromClassResource(String classResourceName) throws IOException, ParserConfigurationException, SAXException
    {
        DwrXmlConfigurator local = new DwrXmlConfigurator();
        local.setClassResourceName(classResourceName);
        local.configure(container);
    }

    /**
     *
     */
    public static void engageThread()
    {
        webContextBuilder.engageThread(container, null, null);
    }

    /**
     *
     */
    public static void disengageThread()
    {
        webContextBuilder.disengageThread();
    }

    /**
     *
     */
    public static ConverterManager getConverterManager()
    {
        return container.getBean(ConverterManager.class);
    }

    /**
     * @return the container
     */
    public static Container getContainer()
    {
        return container;
    }

    /**
     * @return the servletConfig
     */
    public static ServletConfig getServletConfig()
    {
        return servletConfig;
    }

    /**
     * @return the servletContext
     */
    public static ServletContext getServletContext()
    {
        return servletContext;
    }

    /**
     * @return the webContextBuilder
     */
    public static WebContextBuilder getWebContextBuilder()
    {
        return webContextBuilder;
    }

    private static Container container;

    private static WebContextBuilder webContextBuilder;

    private final static ServletContext servletContext = new FakeServletContext();

    private final static ServletConfig servletConfig = new FakeServletConfig("dwr-test", servletContext);

    /**
    *
    */
   static
   {
       try
       {
           container = StartupUtil.createAndSetupDefaultContainer(servletConfig);

           webContextBuilder = container.getBean(WebContextBuilder.class);

           StartupUtil.configureFromSystemDwrXml(container);

           Configurator configurator = new AnnotationsConfigurator();
           configurator.configure(container);
       }
       catch (Exception ex)
       {
           throw new RuntimeException(ex);
       }
       finally
       {
           if (webContextBuilder != null)
           {
               webContextBuilder.disengageThread();
           }
       }
   }
}
