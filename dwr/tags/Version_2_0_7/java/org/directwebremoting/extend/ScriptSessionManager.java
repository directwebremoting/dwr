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
    Collection getAllScriptSessions();

    /**
     * For a given script session id, either create a new ScriptSession object
     * or retrieve an existing one if one exists.
     * @param url The URL including 'http://', up to (but not including) '?' or '#'
     * @return A ScriptSession.
     */
    Collection getScriptSessionsByPage(String url);

    /**
     * For a given script session id, either create a new ScriptSession object
     * or retrieve an existing one if one exists.
     * @param id The id to get a ScriptSession object for
     * @return A ScriptSession.
     */
    RealScriptSession getScriptSession(String id);

    /**
     * Locate the given script session on a page
     * @param scriptSession The session to locate on a page
     * @param url The URL including 'http://', up to (but not including) '?' or '#'
     */
    void setPageForScriptSession(RealScriptSession scriptSession, String url);

    /**
     * Accessor for the time (in milliseconds) when unused ScriptSessions will expire
     * @return the scriptSessionTimeout
     */
    public long getScriptSessionTimeout();

    /**
     * Accessor for the time (in milliseconds) when unused ScriptSessions will expire
     * @param scriptSessionTimeout the timeout to set
     */
    public void setScriptSessionTimeout(long scriptSessionTimeout);

    /**
     * The default length of time a session can go unused before it
     * automatically becomes invalid and is recycled.
     * The default is 5mins
     */
    public static final long DEFAULT_TIMEOUT_MILLIS = 300000;
}
