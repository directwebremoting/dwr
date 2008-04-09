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

import java.util.Iterator;

import org.directwebremoting.event.ScriptSessionBindingListener;

/**
 * Script scope is like session scope except that it is managed using a
 * Javascript variable.
 * The operations on a Page are similar to (and derived from) the options on a
 * Session, with some added simplification.
 * @see javax.servlet.http.HttpSession
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public interface ScriptSession
{
    /**
     * Returns the object bound with the specified name in this session, or
     * <code>null</code> if no object is bound under the name.
     * @param name a string specifying the name of the object
     * @return the object with the specified name
     * @throws IllegalStateException if the page has been invalidated
     */
    Object getAttribute(String name);

    /**
     * Binds an object to this session, using the name specified.
     * If an object of the same name is already bound to the session, the
     * object is replaced.
     * <p>After this method executes, and if the new object implements
     * {@link ScriptSessionBindingListener}, the container calls
     * {@link ScriptSessionBindingListener#valueBound}.
     * <p>If an object was already bound to this session of this name that
     * implements {@link ScriptSessionBindingListener}, its
     * {@link ScriptSessionBindingListener#valueUnbound} method is called.
     * <p>If the value passed in is null, this has the same effect as calling
     * {@link #removeAttribute}.
     * @param name the name to which the object is bound; cannot be null
     * @param value the object to be bound
     * @throws IllegalStateException if the page has been invalidated
     */
    void setAttribute(String name, Object value);

    /**
     * Removes the object bound with the specified name from this session.
     * If the session does not have an object bound with the specified name,
     * this method does nothing.
     * <p>After this method executes, and if the object implements
     * {@link ScriptSessionBindingListener}, the container calls
     * {@link ScriptSessionBindingListener#valueUnbound}.
     * @param name the name of the object to remove from this session
     * @throws IllegalStateException if the page has been invalidated
     */
    void removeAttribute(String name);

    /**
     * Returns an <code>Enumeration</code> of <code>String</code> objects
     * containing the names of all the objects bound to this session.
     * @return an <code>Iterator</code> of <code>String</code>s, specifying the
     *     names of all the objects bound to this session
     * @throws IllegalStateException if the page has been invalidated
     */
    Iterator<String> getAttributeNames();

    /**
     * Invalidates this session then unbinds any objects bound to it.
     * @throws IllegalStateException if the page has been invalidated
     */
    void invalidate();

    /**
     * Checks to see if this ScriptSession has been invalidated.
     * <p>There is no similar method on {@link javax.servlet.http.HttpSession}
     * because it is assumed that you do not store HttpSessions from one request
     * to another, so all sessions that you have access to will always be either
     * valid, or you have just invalidated it yourself so you wont need to ask.
     * This method makes up for the change that now ScriptSessions are
     * accessible from outside the normal scope.
     * @return true if the ScriptSession has been invalidated
     */
    boolean isInvalidated();

    /**
     * Add a script to the list waiting for remote execution.
     * The version automatically wraps the string in a ClientScript object.
     * @param script The script to execute
     */
    void addScript(ScriptBuffer script);

    /**
     * Returns a string containing the unique identifier assigned to this
     * session. The identifier is assigned by the servlet container and is
     * implementation dependent.
     * @return a string specifying the identifier assigned to this session
     * @throws IllegalStateException if the page has been invalidated
     */
    String getId();

    /**
     * Returns the time when this session was created, measured in milliseconds
     * since midnight January 1, 1970 GMT.
     * @return when was this page created, in milliseconds since 1/1/1970 GMT
     * @throws IllegalStateException if the page has been invalidated
     */
    long getCreationTime();

    /**
     * Returns the last time the client sent a request associated with this
     * session, as the number of milliseconds since 1/1/1970 GMT, and marked by
     * the time the container received the request.
     * <p>Actions that your application takes, such as getting or setting a
     * value associated with the session, do not affect the access time.
     * @return when was this page last accessed, in milliseconds since 1/1/1970 GMT
     * @throws IllegalStateException if the page has been invalidated
     */
    long getLastAccessedTime();

    /**
     * What page is this script session attached to?
     * The page does not include server information, but does include everything
     * from the host/port onwards, including the query parameters depending on
     * the configured PageNormalizer, which by default removes them.
     * @return The page that this script session is viewing
     */
    String getPage();
}
