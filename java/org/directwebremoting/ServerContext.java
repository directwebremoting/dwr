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
    Collection getScriptSessionsByPage(String url);

    /**
     * Get a list of all the ScriptSessions known to this server at the given
     * time.
     * Note that the list of known sessions is continually changing so it is
     * possible that the list will be out of date by the time it is used. For
     * this reason you should check that getScriptSession(String id) returns
     * something non null.
     * @return A collection of all the ScriptSessions.
     */
    Collection getAllScriptSessions();

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
