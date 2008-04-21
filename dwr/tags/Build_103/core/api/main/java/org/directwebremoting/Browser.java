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
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class Browser
{
    public static void withPage(String page, Runnable task)
    {
        Collection<ScriptSession> sessions = ServerContextFactory.get().getScriptSessionsByPage(page);
        target.set(sessions);
        task.run();
        target.remove();
    }

    public static void withSessions(Collection<ScriptSession> sessions, Runnable task)
    {
        target.set(sessions);
        task.run();
        target.remove();
    }

    public static void withSession(ScriptSession session, Runnable task)
    {
        Collection<ScriptSession> sessions = new ArrayList<ScriptSession>();
        sessions.add(session);
        target.set(sessions);
        task.run();
        target.remove();
    }

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

    private static final ThreadLocal<Collection<ScriptSession>> target = new ThreadLocal<Collection<ScriptSession>>();
}
