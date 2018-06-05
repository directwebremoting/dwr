package org.directwebremoting.servlet;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.directwebremoting.Container;
import org.directwebremoting.WebContextFactory.WebContextBuilder;

/**
 * A Servlet Filter that can be used with other web frameworks to allow use of
 * WebContextFactory. Any servlet threads that have their request URL mapped
 * through a DwrWebContextFilter will have a WebContext available to them.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class DwrWebContextFilter implements Filter
{
    /* (non-Javadoc)
     * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
     */
    public void init(FilterConfig newFilterConfig) throws ServletException
    {
        filterConfig = newFilterConfig;
    }

    /* (non-Javadoc)
     * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)
     */
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException
    {
        ServletContext servletContext = filterConfig.getServletContext();

        Container container = (Container) servletContext.getAttribute(Container.class.getName());
        if (container == null)
        {
            log.error("DwrWebContextFilter can not find ServletContext attribute for the DWR Container. Is DwrServlet configured in this web-application?");
        }

        ServletConfig servletConfig = (ServletConfig) servletContext.getAttribute(ServletConfig.class.getName());
        if (servletConfig == null)
        {
            log.error("DwrWebContextFilter can not find ServletContext attribute for the ServletConfig.");
        }

        WebContextBuilder webContextBuilder = (WebContextBuilder) servletContext.getAttribute(WebContextBuilder.class.getName());
        if (webContextBuilder == null)
        {
            log.error("DwrWebContextFilter can not find ServletContext attribute for the WebContextBuilder. WebContext will not be available.");
        }
        else
        {
            try
            {
                webContextBuilder.engageThread(container, (HttpServletRequest) request, (HttpServletResponse) response);
                chain.doFilter(request, response);
            }
            finally
            {
                webContextBuilder.disengageThread();
            }
        }
    }

    /* (non-Javadoc)
     * @see javax.servlet.Filter#destroy()
     */
    public void destroy()
    {
    }

    /**
     * The log stream
     */
    private static final Log log = LogFactory.getLog(DwrWebContextFilter.class);

    /**
     * The filter config, that we use to get at the servlet context
     */
    private FilterConfig filterConfig;
}
