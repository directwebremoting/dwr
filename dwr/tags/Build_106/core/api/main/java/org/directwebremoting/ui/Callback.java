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
package org.directwebremoting.ui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.directwebremoting.ScriptSession;
import org.directwebremoting.WebContext;
import org.directwebremoting.WebContextFactory;

/**
 * A way to call functions in JavaScript that return data using a reverse ajax
 * proxy.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public abstract class Callback<T>
{
    /**
     * Create a Callback from a DWR thread,
     * i.e. where {@link WebContextFactory#get()} =! null
     */
    public Callback()
    {
        WebContext context = WebContextFactory.get();
        if (context == null)
        {
            throw new IllegalStateException("Attempt to use Callback without any ScriptSessions, from a non DWR thread. There is nowhere for replies to go.");
        }

        sessions.add(context.getScriptSession());
    }

    /**
     * Used when you need to specify the browser that will be providing the
     * response
     * @param session The browser to answer the question
     */
    public Callback(ScriptSession session)
    {
        sessions.add(session);
    }

    /**
     * Used when you need to specify a group of browsers that will be providing
     * the responses. The callback will be executed once per browser that
     * replies.
     * @param sessionList The browsers to answer the question
     */
    public Callback(Collection<ScriptSession> sessionList)
    {
        sessions.addAll(sessionList);
    }

    /**
     * A browser has completed some remote call as has data for you
     * @param data The data returned by the browser
     */
    public abstract void dataReturned(T data);

    /**
     * Accessor for the ScriptSessions that will reply to the question.
     * This method is generally for DWR internal use, but only because it's
     * unlikely to be useful to others.
     * @return An immutable list of browsers that may reply to the question.
     */
    public Collection<ScriptSession> getScriptSessions()
    {
        return Collections.unmodifiableCollection(sessions);
    }

    /**
     * We store the ScriptSessions that we send replies to, here.
     */
    private final List<ScriptSession> sessions = new ArrayList<ScriptSession>();
}
