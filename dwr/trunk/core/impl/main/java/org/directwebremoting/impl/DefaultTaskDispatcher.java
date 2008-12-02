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

import java.util.ArrayList;
import java.util.Collection;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.directwebremoting.ScriptSession;
import org.directwebremoting.ScriptSessionFilter;
import org.directwebremoting.extend.ScriptSessionManager;
import org.directwebremoting.extend.TaskDispatcher;

/**
 * Default single node implementation of TaskDispatcher
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class DefaultTaskDispatcher implements TaskDispatcher
{
    /* (non-Javadoc)
     * @see org.directwebremoting.extend.TaskDispatcher#dispatchTask(org.directwebremoting.ScriptSessionFilter, java.lang.Runnable)
     */
    public void dispatchTask(ScriptSessionFilter filter, Runnable task)
    {
        Collection<ScriptSession> all = scriptSessionManager.getAllScriptSessions();
        Collection<ScriptSession> use = new ArrayList<ScriptSession>();

        for (ScriptSession session : all)
        {
            if (filter.match(session))
            {
                use.add(session);
            }
        }

        if (use.size() > 0)
        {
            log.debug("Executing task (" + task.getClass().getSimpleName() + ") against " + use.size() + " sessions.");

            target.set(use);
            task.run();
            target.remove();
        }
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.extend.TaskDispatcher#getTargetSessions()
     */
    public Collection<ScriptSession> getTargetSessions()
    {
        Collection<ScriptSession> sessions = target.get();
        if (sessions != null)
        {
            return sessions;
        }

        throw new IllegalStateException("No current UI to manipulate. See org.directwebremoting.Browser to set one.");
    }

    /**
     * Connection to the DwrServlet that we are attached to
     */
    public void setScriptSessionManager(ScriptSessionManager scriptSessionManager)
    {
        this.scriptSessionManager = scriptSessionManager;
    }

    /**
     * @see #setScriptSessionManager(ScriptSessionManager)
     */
    private ScriptSessionManager scriptSessionManager;

    /**
     * ThreadLocal in which the list of sessions are stored.
     */
    private final ThreadLocal<Collection<ScriptSession>> target = new ThreadLocal<Collection<ScriptSession>>();

    /**
     * The log stream
     */
    private static final Log log = LogFactory.getLog(DefaultTaskDispatcher.class);
}
