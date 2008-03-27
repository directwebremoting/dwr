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
package uk.ltd.getahead.dwr.servlet;

import java.io.IOException;

import javax.faces.FactoryFinder;
import javax.faces.context.FacesContext;
import javax.faces.context.FacesContextFactory;
import javax.faces.lifecycle.Lifecycle;
import javax.faces.lifecycle.LifecycleFactory;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * Filter integration for DWR framework. This filter was inspired by this
 * article: http://www.thoughtsabout.net/blog/archives/000033.html
 * @author Pierpaolo Follia (Latest revision: $Author: esa50833 $)
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class FacesExtensionFilter implements Filter
{
    private ServletContext servletContext = null;

    /* (non-Javadoc)
     * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
     */
    public void init(FilterConfig config) throws ServletException
    {
        servletContext = config.getServletContext();
    }

    /* (non-Javadoc)
     * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)
     */
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException
    {
        // Create the faces context
        FacesContext facesContext = FacesContext.getCurrentInstance();
        if (facesContext == null)
        {
            FacesContextFactory contextFactory = (FacesContextFactory) FactoryFinder.getFactory(FactoryFinder.FACES_CONTEXT_FACTORY);
            LifecycleFactory lifecycleFactory = (LifecycleFactory) FactoryFinder.getFactory(FactoryFinder.LIFECYCLE_FACTORY);
            Lifecycle lifecycle = lifecycleFactory.getLifecycle(LifecycleFactory.DEFAULT_LIFECYCLE);

            // Either set a private member
            //   servletContext = filterConfig.getServletContext();
            // in your filter init() method or set it here like this:
            //   ServletContext servletContext =
            //     ((HttpServletRequest) request).getSession().getServletContext();
            // Note that the above line would fail if you are using any other
            // protocol than http

            // Doesn't set this instance as the current instance of
            // FacesContext.getCurrentInstance
            facesContext = contextFactory.getFacesContext(servletContext, request, response, lifecycle);

            // Set using our inner class
            InnerFacesContext.setFacesContextAsCurrentInstance(facesContext);
        }

        // call the filter chain
        chain.doFilter(request, response);
    }

    /* (non-Javadoc)
     * @see javax.servlet.Filter#destroy()
     */
    public void destroy()
    {
    }

    // You need an inner class to be able to call
    // FacesContext.setCurrentInstance
    // since it's a protected method
    private abstract static class InnerFacesContext extends FacesContext
    {
        protected static void setFacesContextAsCurrentInstance(FacesContext facesContext)
        {
            FacesContext.setCurrentInstance(facesContext);
        }
    }
}
