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
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.directwebremoting.Container;
import org.directwebremoting.extend.Handler;
import org.directwebremoting.extend.InitializingBean;
import org.directwebremoting.util.LocalUtil;

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
public class UrlProcessor implements Handler, InitializingBean
{
    /* (non-Javadoc)
     * @see org.directwebremoting.InitializingBean#afterPropertiesSet(Container)
     */
    public void afterContainerSetup(Container container)
    {
        Collection<String> beanNames = container.getBeanNames();
        for (String name : beanNames)
        {
            if (name.startsWith(PathConstants.PATH_PREFIX))
            {
                Object bean = container.getBean(name);

                if (bean instanceof Handler)
                {
                    Handler handler = (Handler) bean;
                    urlMapping.put(name.substring(PathConstants.PATH_PREFIX.length()), handler);
                }
                else if (bean instanceof String)
                {
                    log.error("Discarding non Handler for " + name + " (" + bean + " can not be loaded)");
                }
                else
                {
                    log.error("Discarding non Handler for " + name + " (" + bean.getClass().getName() + " is not an instance of " + Handler.class.getName() + ")");
                }
            }
        }

        ServletContext servletContext = container.getBean(ServletContext.class);

        // This will fail (i.e. return null with servlet 2.4, but we patch up
        // in the handle method.
        contextPath = LocalUtil.getProperty(servletContext, "contextPath", String.class);
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.Handler#handle(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    public void handle(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        try
        {
            String pathInfo = request.getPathInfo();
            String requestContextPath = request.getContextPath();

            // Patching up for missing servlet 2.5 servletContext.getContextPath
            if (contextPath == null)
            {
                contextPath = requestContextPath;
            }

            if (pathInfo == null || pathInfo.length() == 0 || "/".equals(pathInfo))
            {
                response.sendRedirect(requestContextPath + request.getServletPath() + indexHandlerUrl);
            }
            else
            {
                // Loop through all the known URLs
                for (Entry<String, Handler> entry : urlMapping.entrySet())
                {
                    String url = entry.getKey();

                    // If this URL matches, call the handler
                    if (pathInfo.startsWith(url))
                    {
                        Handler handler = entry.getValue();
                        handler.handle(request, response);
                        return;
                    }
                }

                notFoundHandler.handle(request, response);
            }
        }
        catch (Exception ex)
        {
            exceptionHandler.setException(ex);
            exceptionHandler.handle(request, response);
        }
    }

    /**
     * The contextPath cached from the last HTTP servlet request
     * @return the contextPath
     */
    public String getContextPath()
    {
        return contextPath;
    }

    /**
     * The URL for the {@link IndexHandler}
     * @param indexHandlerUrl the indexHandlerUrl to set
     */
    public void setIndexHandlerUrl(String indexHandlerUrl)
    {
        this.indexHandlerUrl = indexHandlerUrl;
    }

    /**
     * The URL for the {@link IndexHandler}
     */
    protected String indexHandlerUrl;

    /**
     * The mapping of URLs to {@link Handler}s
     */
    protected Map<String, Handler> urlMapping = new HashMap<String, Handler>();

    /**
     * The default if we have no other action (HTTP-404)
     */
    protected Handler notFoundHandler = new NotFoundHandler();

    /**
     * If execution fails, we do this (HTTP-501)
     */
    protected ExceptionHandler exceptionHandler = new ExceptionHandler();

    /**
     * The contextPath cached from the last HTTP servlet request
     */
    protected String contextPath = null;

    /**
     * The log stream
     */
    private static final Log log = LogFactory.getLog(UrlProcessor.class);
}
