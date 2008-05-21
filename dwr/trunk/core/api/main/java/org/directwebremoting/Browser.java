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
package org.directwebremoting;

import java.util.ArrayList;
import java.util.Collection;

/**
 * A collection of APIs that manage reverse ajax APIs.
 * <p>See {@link #withAllSessions} for a menu of the various different with*
 * methods.
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
        Collection<ScriptSession> sessions = ServerContextFactory.get().getAllScriptSessions();
        withSessions(sessions, task);
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
        Collection<ScriptSession> all = ServerContextFactory.get().getAllScriptSessions();
        withSessions(filter(all, filter), task);
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
        Collection<ScriptSession> sessions = ServerContextFactory.get().getScriptSessionsByPage(page);
        withSessions(sessions, task);
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
        Collection<ScriptSession> sessions = ServerContextFactory.get().getScriptSessionsByPage(page);
        withSessions(filter(sessions, filter), task);
    }

    /**
     * Execute a task and aim the output at a specific script session. This
     * method is likely to be useful for directed updates that originate away
     * from a thread started by the browser window in question. Examples include
     * an instant message, or the results of a slow method invocation.
     * This method is implicit in anything called from a DWR thread, so should
     * not need to be used to send UI updates back to an originating browser.
     * @param session The browser window to send messages to
     * @param task A code block to execute
     */
    public static void withSession(ScriptSession session, Runnable task)
    {
        Collection<ScriptSession> sessions = new ArrayList<ScriptSession>();
        sessions.add(session);
        withSessions(sessions, task);
    }

    /**
     * This method discovers the sessions that are currently being targeted
     * by browser updates. It will generally only be useful to authors of
     * reverse ajax UI proxy APIs.
     * @return The list of current browser windows.
     */
    public static Collection<ScriptSession> getTargetSessions()
    {
        Collection<ScriptSession> sessions = target.get();
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

    /**
     * Execute a task and aim the output at a specific set of users. This method
     * is private because the filter version is likely to be just as easy to use
     * and potentially better suited to a clustered environment.
     * @param sessions The browser window to send the generated scripts to
     * @param task A code block to execute
     */
    private static void withSessions(Collection<ScriptSession> sessions, Runnable task)
    {
        target.set(sessions);
        task.run();
        target.remove();
    }

    /**
     * Utility method to run a filter over a set of script sessions.
     * @param start The un-filtered set of script sessions
     * @param filter The filter to use to trim this list down
     * @return The subset of script sessions left by the filter
     */
    private static Collection<ScriptSession> filter(Collection<ScriptSession> start, ScriptSessionFilter filter)
    {
        Collection<ScriptSession> use = new ArrayList<ScriptSession>();
        for (ScriptSession session : start)
        {
            if (filter.match(session))
            {
                use.add(session);
            }
        }

        return use;
    }

    /**
     * ThreadLocal in which the list of sessions are stored.
     */
    private static final ThreadLocal<Collection<ScriptSession>> target = new ThreadLocal<Collection<ScriptSession>>();
}
