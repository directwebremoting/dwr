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
package org.directwebremoting.extend;

import java.util.Collection;

import org.directwebremoting.ScriptSession;
import org.directwebremoting.event.ScriptSessionListener;

/**
 * A ScriptSessionManager looks after a number of sessions (keyed using a
 * Javascript variable)
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public interface ScriptSessionManager
{
    /**
     * Get a list of all the currently known ScriptSessions by id.
     * Note that the list of known sessions is continually changing so it is
     * possible that the list will be out of date by the time it is used. For
     * this reason you should check that getScriptSession(String id) returns
     * something non null.
     * @return An iterator over the currently known sessions, by id
     */
    Collection<ScriptSession> getAllScriptSessions();

    /**
     * Lookup all the windows associated with a given browser
     * @param httpSessionId The browser id to lookup
     * @return A list of script sessions for each open window
     */
    Collection<RealScriptSession> getScriptSessionsByHttpSessionId(String httpSessionId);

    /**
     * For a given script session id, return the related ScriptSession object
     * or null if the id is not known.
     * @param id The id to get a ScriptSession object for
     * @param url The URL including 'http://', up to (but not including) '?' or '#' (or null if not known)
     * @param httpSessionId The session ID (or null if not known)
     * @return A ScriptSession to match the ID, or null if a match is not found.
     */
    RealScriptSession getScriptSession(String id, String url, String httpSessionId);

    /**
     * Accessor for the time (in milliseconds) when unused ScriptSessions will expire
     * @return the scriptSessionTimeout
     */
    long getScriptSessionTimeout();

    /**
     * Maintain the list of {@link ScriptSessionListener}s
     * @param li the ScriptSessionListener to add
     */
    void addScriptSessionListener(ScriptSessionListener li);

    /**
     * Maintain the list of {@link ScriptSessionListener}s
     * @param li the ScriptSessionListener to remove
     */
    void removeScriptSessionListener(ScriptSessionListener li);

    /**
     * Some implementations of ScriptSessionManager need to add custom code into
     * engine.js to register ScriptSessions with the server.
     */
    String getInitCode();

    /**
     * The default length of time a session can go unused before it
     * automatically becomes invalid and is recycled.
     * The default is 5mins
     */
    static final long DEFAULT_TIMEOUT_MILLIS = 5L * 60L * 1000L;
}
