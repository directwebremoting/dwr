package org.directwebremoting;

import java.util.ArrayList;
import java.util.Collection;

import org.directwebremoting.extend.AllScriptSessionFilter;
import org.directwebremoting.extend.AndScriptSessionFilter;
import org.directwebremoting.extend.IdScriptSessionFilter;
import org.directwebremoting.extend.PageScriptSessionFilter;
import org.directwebremoting.extend.TaskDispatcher;
import org.directwebremoting.extend.TaskDispatcherFactory;
import org.directwebremoting.io.JavascriptObject;

/**
 * A collection of APIs that manage reverse ajax APIs.
 * <p>See {@link #withAllSessions} for a menu of the various different with*
 * methods.
 * <p>The {@link Runnable}s that are passed to the with* methods may be executed
 * any number of times between 0 (nothing passes the filter) 1 (where
 * {@link Browser#getTargetSessions()} returns all the matching ScriptSessions)
 * and X (where X is the number of matching ScriptSessions, with
 * {@link Browser#getTargetSessions()} returning one of the ScriptSessions on
 * each invocation).
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class Browser
{
    /**
     * Execute some task (represented by a {@link Runnable}) and aim the output
     * at all browser window open at all pages in this web application.
     * It is likely that a more fine-grained broadcast method will be applicable
     * to most situations. The other with* methods in this class provide extra
     * ways to filter the set of pages to broadcast to. This option could be
     * useful if all pages contain an 'update' area, or to broadcast status
     * information: Window.alert("System reboot in 20mins. Please log off");
     * To send UI code to browser windows looking at a specific page, see the
     * {@link #withPage} method. To send to a custom set of browser windows,
     * see the {@link #withAllSessionsFiltered} method. To send to a specific
     * session, use {@link #withSession}.
     * @param task A code block to execute
     */
    public static void withAllSessions(Runnable task)
    {
        withAllSessions(getServerContext(), task);
    }

    /**
     * As {@link #withAllSessions(Runnable)}, but for use when there is more
     * than one copy of DWR in the ServletContext.
     * <p>
     * For 99% of cases the former method will be much simpler to use.
     * @param serverContext The specific DWR context in which to execute
     */
    public static void withAllSessions(ServerContext serverContext, Runnable task)
    {
        currentServerContext.set(serverContext);
        try {
            TaskDispatcher taskDispatcher = TaskDispatcherFactory.get(serverContext);
            taskDispatcher.dispatchTask(new AllScriptSessionFilter(), task);
        } finally {
            currentServerContext.remove();
        }
    }

    /**
     * Execute a task an send the output to a subset of the total list of users.
     * The {@link ScriptSessionFilter} defines which subset.
     * This method could be used to alert administrators wherever they are on a
     * site about urgent action that need attention.
     * @param filter Used to define the set of browser windows which should
     * receive the update.
     * @param task A code block to execute
     */
    public static void withAllSessionsFiltered(ScriptSessionFilter filter, Runnable task)
    {
        withAllSessionsFiltered(getServerContext(), filter, task);
    }

    /**
     * As {@link #withAllSessionsFiltered(ScriptSessionFilter, Runnable)}, but
     * for use when there is more than one copy of DWR in the ServletContext.
     * <p>
     * For 99% of cases the former method will be much simpler to use.
     * @param serverContext The specific DWR context in which to execute
     */
    public static void withAllSessionsFiltered(ServerContext serverContext, ScriptSessionFilter filter, Runnable task)
    {
        currentServerContext.set(serverContext);
        try {
            TaskDispatcher taskDispatcher = TaskDispatcherFactory.get(serverContext);
            taskDispatcher.dispatchTask(filter, task);
        } finally {
            currentServerContext.remove();
        }
    }

    /**
     * Execute a task and aim the output at all the browser windows open at the
     * same page as the current request. No further filtering is performed.
     * This implies that this method is only of use from a DWR created thread.
     * To send to a subset of the browser windows viewing a page, see the
     * {@link #withCurrentPageFiltered} method.
     * @param task A code block to execute
     */
    public static void withCurrentPage(Runnable task)
    {
        WebContext webContext = WebContextFactory.get();
        currentServerContext.set(webContext);
        try {
            String page = webContext.getCurrentPage();
            TaskDispatcher taskDispatcher = TaskDispatcherFactory.get(webContext);
            taskDispatcher.dispatchTask(new PageScriptSessionFilter(webContext, page), task);
        } finally {
            currentServerContext.remove();
        }
    }

    /**
     * Execute a task and aim the output at all the browser windows open at a
     * given page in this web application. No further filtering is performed.
     * To send to a subset of the browser windows viewing a page, see the
     * {@link #withPageFiltered} method.
     * @param page The page to send to, excluding protocol/host/port specifiers
     * but including context path and servlet path. For example to send to
     * <code>http://example.com:8080/webapp/controller/path/index.html</code>,
     * you should use "/webapp/controller/path/index.html" or since the default
     * PageNormalizer understands default pages, this is the same as sending to
     * browsers viewing "/webapp/controller/path/".
     * To discover the contextPath at runtime you can use
     * javax.servlet.ServletContext#getContextPath with servlet 2.5, or before
     * version 2.5 you can also use {@link ServerContext#getContextPath} or
     * {@link javax.servlet.http.HttpServletRequest#getContextPath}.
     * @param task A code block to execute
     */
    public static void withPage(String page, Runnable task)
    {
        withPage(getServerContext(), page, task);
    }

    /**
     * As {@link #withPage(String, Runnable)}, but for use when there is more
     * than one copy of DWR in the ServletContext.
     * <p>
     * For 99% of cases the former method will be much simpler to use.
     * @param serverContext The specific DWR context in which to execute
     */
    public static void withPage(ServerContext serverContext, String page, Runnable task)
    {
        currentServerContext.set(serverContext);
        try {
            TaskDispatcher taskDispatcher = TaskDispatcherFactory.get(serverContext);
            taskDispatcher.dispatchTask(new PageScriptSessionFilter(serverContext, page), task);
        } finally {
            currentServerContext.remove();
        }
    }

    /**
     * Execute a task and aim the output at a subset of the browser windows open
     * at the same page as the current request. The filter allows you to select
     * the set of users that will be interested in the update.
     * This implies that this method is only of use from a DWR created thread.
     * @param filter Used to define the set of browser windows which should
     * receive the update.
     * @param task A code block to execute
     */
    public static void withCurrentPageFiltered(ScriptSessionFilter filter, Runnable task)
    {
        WebContext webContext = WebContextFactory.get();
        String page = webContext.getCurrentPage();
        ScriptSessionFilter pageFilter = new PageScriptSessionFilter(webContext, page);
        ScriptSessionFilter realFilter = new AndScriptSessionFilter(pageFilter, filter);
        TaskDispatcher taskDispatcher = TaskDispatcherFactory.get(webContext);
        taskDispatcher.dispatchTask(realFilter, task);
    }

    /**
     * Execute a task and aim the output at a subset of the users on a page.
     * This method is useful when you have a small number of pages that each
     * have a significant number of functions on them. The filter allows you to
     * select the set of users that will be interested in the update.
     * @param page The page to send to. See {@link #withPage} for details
     * @param filter Used to define the set of browser windows which should
     * receive the update.
     * @param task A code block to execute
     */
    public static void withPageFiltered(String page, ScriptSessionFilter filter, Runnable task)
    {
        withPageFiltered(getServerContext(), page, filter, task);
    }

    /**
     * As {@link #withPageFiltered}, but for use when there is more than one copy of DWR
     * in a ServletContext.
     * <p>
     * For 99% of cases the former method will be much simpler to use.
     * @param serverContext The specific DWR context in which to execute
     */
    public static void withPageFiltered(ServerContext serverContext, String page, ScriptSessionFilter filter, Runnable task)
    {
        currentServerContext.set(serverContext);
        try {
            ScriptSessionFilter pageFilter = new PageScriptSessionFilter(serverContext, page);
            ScriptSessionFilter realFilter = new AndScriptSessionFilter(pageFilter, filter);
            TaskDispatcher taskDispatcher = TaskDispatcherFactory.get(serverContext);
            taskDispatcher.dispatchTask(realFilter, task);
        } finally {
            currentServerContext.remove();
        }
    }

    /**
     * Execute a task and aim the output at a specific script session. This
     * method is likely to be useful for directed updates that originate away
     * from a thread started by the browser window in question. Examples include
     * an instant message, or the results of a slow method invocation.
     * This method is implicit in anything called from a DWR thread, so should
     * not need to be used to send UI updates back to an originating browser.
     * @param sessionId The {@link ScriptSession}.id of the browser window
     * @param task A code block to execute
     */
    public static void withSession(String sessionId, Runnable task)
    {
        withSession(getServerContext(), sessionId, task);
    }

    /**
     * As {@link #withSession(String, Runnable)}, but for use when there is more
     * than one copy of DWR in a ServletContext.
     * <p>
     * For 99% of cases the former method will be much simpler to use.
     * @param serverContext The specific DWR context in which to execute
     */
    public static void withSession(ServerContext serverContext, String sessionId, Runnable task)
    {
        currentServerContext.set(serverContext);
        try {
            TaskDispatcher taskDispatcher = TaskDispatcherFactory.get(serverContext);
            taskDispatcher.dispatchTask(new IdScriptSessionFilter(sessionId), task);
        } finally {
            currentServerContext.remove();
        }
    }

    /**
     * If a browser passes an object or function to DWR then DWR may need to
     * release the reference that DWR keeps. If the server-side manifestation of
     * this object is a proxy implemented interface, then there is no place for
     * a close method, so this function allows you to close proxy implemented
     * interfaces.
     * @param proxy An proxy created interface implementation from a browser
     * that we no longer need.
     */
    public static void close(Object proxy)
    {
        if (proxy instanceof JavascriptObject)
        {
            JavascriptObject dproxy = (JavascriptObject) proxy;
            dproxy.close();
        }
    }

    /**
     * This method discovers the sessions that are currently being targeted
     * by browser updates.
     * <p>
     * It will generally only be useful to authors of reverse ajax UI proxy
     * APIs. Using it directly may cause scaling problems
     * @return The list of current browser windows.
     */
    public static Collection<ScriptSession> getTargetSessions()
    {
        TaskDispatcher taskDispatcher = TaskDispatcherFactory.get(getServerContext());
        Collection<ScriptSession> sessions = taskDispatcher.getTargetSessions();
        if (sessions != null)
        {
            return sessions;
        }

        WebContext webContext = WebContextFactory.get();
        if (webContext != null)
        {
            sessions = new ArrayList<ScriptSession>();
            sessions.add(webContext.getScriptSession());
            return sessions;
        }

        throw new IllegalStateException("No current UI to manipulate. See org.directwebremoting.Browser to set one.");
    }

    private static ServerContext getServerContext()
    {
        ServerContext servCtx = currentServerContext.get();
        if (servCtx != null)
        {
            return servCtx;
        }
        else
        {
            return ServerContextFactory.get();
        }
    }

    /**
     * ThreadLocal in which the current ServerContext is stored.
     */
    private static final ThreadLocal<ServerContext> currentServerContext = new ThreadLocal<ServerContext>();

}
