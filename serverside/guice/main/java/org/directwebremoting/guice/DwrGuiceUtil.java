package org.directwebremoting.guice;

import java.util.LinkedList;
import java.util.concurrent.Callable;

import javax.servlet.ServletContext;

import org.directwebremoting.ServerContext;
import org.directwebremoting.WebContext;
import org.directwebremoting.WebContextFactory;
import org.directwebremoting.impl.StartupUtil;

import com.google.inject.Injector;

/**
 * Utilities for making Injector and ServletContext instances available.
 * @author Tim Peierls [tim at peierls dot net]
 */
public class DwrGuiceUtil
{
    /**
     * The key under which a provided Injector is stashed in a ServletContext.
     * The name is prefixed by the package to avoid conflicting with other
     * listeners using the same technique.
     */
    public static final String INJECTOR = "org.directwebremoting.guice.Injector";


    /**
     * Returns the Injector instance published in the current servlet context.
     */
    public static Injector getInjector()
    {
        return AbstractDwrGuiceServletContextListener.getPublishedInjector(getServletContext());
    }

    /**
     * Gets the servlet context from the thread-local stash, if any,
     * otherwise from the current web context, if one exists,
     * otherwise from the singleton server context, if it exists,
     * otherwise from the first of all server contexts, if there are any,
     * otherwise null.
     */
    public static ServletContext getServletContext()
    {
        LinkedList<ServletContext> sclist = servletContexts.get();
        if (!sclist.isEmpty())
        {
            return sclist.getFirst();
        }

        WebContext webcx = WebContextFactory.get();
        if (webcx != null)
        {
            return webcx.getServletContext();
        }

        ServerContext serverContext = StartupUtil.getSingletonServerContext();
        if (serverContext != null)
        {
            return serverContext.getServletContext();
        }

        for (ServerContext sc : StartupUtil.getAllServerContexts())
        {
            // Use the ServletContext of the first ServerContext we see.
            return sc.getServletContext();
        }

        return null;
    }

    /**
     * Executes the given Runnable with the thread-locally stashed servlet context
     * set to the given value.
     */
    public static void withServletContext(ServletContext servletContext, Runnable runnable)
    {
        pushServletContext(servletContext);
        try
        {
            runnable.run();
        }
        finally
        {
            popServletContext();
        }
    }

    /**
     * Executes the given Callable with the thread-locally stashed servlet context
     * set to the given value, and returns the result.
     */
    public static <T> T withServletContext(ServletContext servletContext, Callable<T> callable) throws Exception
    {
        pushServletContext(servletContext);
        try
        {
            return callable.call();
        }
        finally
        {
            popServletContext();
        }
    }

    /**
     * Thread-locally pushes a servlet context. Call {@link #popServletContext}
     * in a finally block when calling this method.
     */
    private static void pushServletContext(ServletContext context)
    {
        servletContexts.get().addFirst(context);
    }

    /**
     * Pops a thread-locally stashed servlet context. Call this in
     * a finally block when {@link #pushServletContext} is called.
     */
    private static void popServletContext()
    {
        servletContexts.get().removeFirst();
    }

    private static final ThreadLocal<LinkedList<ServletContext>> servletContexts =
        new ThreadLocal<LinkedList<ServletContext>>()
        {
            @Override
            protected LinkedList<ServletContext> initialValue()
            {
                return new LinkedList<ServletContext>();
            }
        };
}
