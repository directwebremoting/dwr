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
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.directwebremoting.Container;
import org.directwebremoting.extend.Handler;
import org.directwebremoting.extend.InitializingBean;
import org.directwebremoting.util.Logger;

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
        Collection beanNames = container.getBeanNames();
        for (Iterator it = beanNames.iterator(); it.hasNext();)
        {
            String name = (String) it.next();
            if (name.startsWith(PathConstants.URL_PREFIX))
            {
                Object bean = container.getBean(name);

                if (bean instanceof Handler)
                {
                    urlMapping.put(name.substring(PathConstants.URL_PREFIX.length()), bean);
                }
                else
                {
                    log.error("Discarding non Handler for " + name);
                }
            }
        }
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.Handler#handle(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    public void handle(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        try
        {
            String pathInfo = request.getPathInfo();

            if (pathInfo == null || pathInfo.length() == 0 || pathInfo.equals("/"))
            {
                response.sendRedirect(request.getContextPath() + request.getServletPath() + indexHandlerUrl);
            }
            else
            {
                // Loop through all the known URLs
                for (Iterator it = urlMapping.entrySet().iterator(); it.hasNext();)
                {
                    Map.Entry entry = (Map.Entry) it.next();
                    String url = (String) entry.getKey();

                    // If this URL matches, call the handler
                    if (pathInfo.startsWith(url))
                    {
                        Handler handler = (Handler) entry.getValue();
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
    private String indexHandlerUrl;

    /**
     * The mapping of URLs to {@link Handler}s
     */
    private Map urlMapping = new HashMap();

    /**
     * The default if we have no other action (HTTP-404)
     */
    private Handler notFoundHandler = new NotFoundHandler();

    /**
     * If execution fails, we do this (HTTP-501)
     */
    private ExceptionHandler exceptionHandler = new ExceptionHandler();

    /**
     * The log stream
     */
    private static final Logger log = Logger.getLogger(UrlProcessor.class);
}
