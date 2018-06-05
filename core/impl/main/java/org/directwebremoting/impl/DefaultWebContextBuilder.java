package org.directwebremoting.impl;

import java.util.ArrayList;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.directwebremoting.Container;
import org.directwebremoting.WebContext;
import org.directwebremoting.WebContextFactory.WebContextBuilder;

/**
 * A WebContextBuilder that creates DefaultWebContexts.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class DefaultWebContextBuilder implements WebContextBuilder
{
    /* (non-Javadoc)
     * @see org.directwebremoting.WebContextBuilder#get()
     */
    public WebContext get()
    {
        ArrayList<WebContext> stack = contextStack.get();
        if (stack == null || stack.size() == 0)
        {
            return null;
        }
        return stack.get(stack.size() - 1);
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.WebContextBuilder#set(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, javax.servlet.ServletConfig, javax.servlet.ServletContext, org.directwebremoting.Container)
     */
    public void engageThread(Container container, HttpServletRequest request, HttpServletResponse response)
    {
        try
        {
            ServletConfig servletConfig = container.getBean(ServletConfig.class);
            ServletContext servletContext = container.getBean(ServletContext.class);

            WebContext ec = new DefaultWebContext(container, request, response, servletConfig, servletContext);
            engageThread(ec);
        }
        catch (Exception ex)
        {
            log.fatal("Failed to create an ExecutionContext", ex);
        }
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.WebContextFactory.WebContextBuilder#engageThread(org.directwebremoting.WebContext)
     */
    public void engageThread(WebContext webContext)
    {
        ArrayList<WebContext> stack = contextStack.get();
        if (stack == null)
        {
            stack = new ArrayList<WebContext>();
            contextStack.set(stack);
        }
        stack.add(webContext);
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.WebContextBuilder#unset()
     */
    public void disengageThread()
    {
        // null check for DWR-426 - DefaultWebContextBuilder disengageThread throws null pointer exception.
        if (contextStack != null)
        {
            ArrayList<WebContext> stack = contextStack.get();
            if (stack != null)
            {
                if (stack.size() > 0)
                {
                    stack.remove(stack.size() - 1);
                }
                if (stack.size() == 0)
                {
                    contextStack.set(null);
                }
            }
        }
    }

    /**
     * The storage of thread based data as a stack
     */
    private static ThreadLocal<ArrayList<WebContext>> contextStack = new ThreadLocal<ArrayList<WebContext>>();

    /**
     * The log stream
     */
    private static final Log log = LogFactory.getLog(DefaultWebContextBuilder.class);
}
