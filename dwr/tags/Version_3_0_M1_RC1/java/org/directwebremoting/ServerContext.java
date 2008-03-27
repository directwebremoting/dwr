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

import java.util.Collection;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;

/**
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public interface ServerContext
{
    /**
     * Get a list of all ScriptSessions on a given page.
     * Note that the list of known sessions is continually changing so it is
     * possible that the list will be out of date by the time it is used. For
     * this reason you should check that getScriptSession(String id) returns
     * something non null.
     * @param url The URL including 'http://', up to (but not including) '?' or '#'
     * @return A collection of all the ScriptSessions.
     */
    Collection<ScriptSession> getScriptSessionsByPage(String url);

    /**
     * You can request access to a specific {@link ScriptSession} if you know
     * it's ID.
     * <p>Take care with this method because it allows actions from one browser
     * to affect another which could be a bad thing. It is certainly a VERY BAD
     * idea to let session id's from one browser escape into another.
     * <p>Consider that it is entirely possible that the ScriptSession may
     * timeout while you are holding a reference to it.
     * @param sessionId The script session ID to lookup
     * @return The ScriptSession for the given ID, or null if it does not exist
     */
    ScriptSession getScriptSessionById(String sessionId);

    /**
     * Get a list of all the ScriptSessions known to this server at the given
     * time.
     * Note that the list of known sessions is continually changing so it is
     * possible that the list will be out of date by the time it is used. For
     * this reason you should check that getScriptSession(String id) returns
     * something non null.
     * @return A collection of all the ScriptSessions.
     */
    Collection<ScriptSession> getAllScriptSessions();

    /**
     * Accessor for the servlet config.
     * @return Returns the config.
     */
    ServletConfig getServletConfig();

    /**
     * Returns the ServletContext to which this session belongs.
     * @return The servlet context information.
     */
    ServletContext getServletContext();

    /**
     * Returns the portion of the request URI that indicates the context
     * of the request.
     * <p>Annoyingly you can't get to this from the {@link ServletContext} so
     * you need to cache the value from a recent HttpServletRequest.
     * <p>The context path always comes first in a request URI.  The path starts
     * with a "/" character but does not end with a "/" character.
     * For servlets in the default (root) context, this method returns "".
     * The container does not decode this string.
     * @return The portion of the request URI that indicates the context
     */
    String getContextPath();

    /**
     * Accessor for the IoC container.
     * @return The IoC container that created the interface implementations.
     */
    Container getContainer();

    /**
     * Fish the version number out of the dwr.properties file.
     * @return The current version number.
     */
    String getVersion();
}
