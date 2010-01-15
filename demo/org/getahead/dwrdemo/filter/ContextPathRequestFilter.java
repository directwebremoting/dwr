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
package org.getahead.dwrdemo.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;


/**
 * A servlet filter that helps us to get the webapp context path (this
 * workaround needed on pre-EE5 containers)
 * @author Mike Wilson
 */
public class ContextPathRequestFilter implements Filter 
{
    /* (non-Javadoc)
     * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
     */
    public void init(FilterConfig fc) throws ServletException
    {
    }

    /* (non-Javadoc)
     * @see javax.servlet.Filter#destroy()
     */
    public void destroy()
    {
    }

    /* (non-Javadoc)
     * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)
     */
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException
    {
        synchronized(ContextPathRequestFilter.class)
        {
            if (ctxpath.equals(""))
                ctxpath = ((HttpServletRequest) request).getContextPath();
        }
        chain.doFilter(request, response);
    }
    
    /**
     * Returns the context path discovered through the first request to the
     * webapp.
     * @return context path
     */
    public static synchronized String getContextPath()
    {
        return ctxpath;
    }

    /**
     * The application's context path
     */
    private static String ctxpath = "";
}
