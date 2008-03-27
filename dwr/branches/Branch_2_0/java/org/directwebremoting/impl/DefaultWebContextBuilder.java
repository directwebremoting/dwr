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

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.directwebremoting.Container;
import org.directwebremoting.WebContext;
import org.directwebremoting.WebContextFactory.WebContextBuilder;
import org.directwebremoting.util.Logger;

/**
 * A WebContextBuilder that creates DefaultWebContexts.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class DefaultWebContextBuilder implements WebContextBuilder
{
    /* (non-Javadoc)
     * @see org.directwebremoting.WebContextBuilder#set(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, javax.servlet.ServletConfig, javax.servlet.ServletContext, org.directwebremoting.Container)
     */
    public void set(HttpServletRequest request, HttpServletResponse response, ServletConfig config, ServletContext context, Container container)
    {
        try
        {
            WebContext ec = new DefaultWebContext(request, response, config, context, container);
            user.set(ec);
        }
        catch (Exception ex)
        {
            log.fatal("Failed to create an ExecutionContext", ex);
        }
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.WebContextBuilder#get()
     */
    public WebContext get()
    {
        return (WebContext) user.get();
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.WebContextBuilder#unset()
     */
    public void unset()
    {
        user.set(null);
    }

    /**
     * The storage of thread based data
     */
    private static ThreadLocal user = new ThreadLocal();

    /**
     * The log stream
     */
    private static final Logger log = Logger.getLogger(DefaultWebContextBuilder.class);
}
