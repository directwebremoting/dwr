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

/**
 * Page scope is like session scope except that it is managed using a Javascript
 * variable.
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
    public Object getAttribute(String name);

    /**
     * Binds an object to this session, using the name specified.
     * If an object of the same name is already bound to the session, the
     * object is replaced.
     * <p>After this method executes, and if the new object implements
     * <code>HttpSessionBindingListener</code>, the container calls 
     * <code>HttpSessionBindingListener.valueBound</code>. The container then   
     * notifies any <code>HttpSessionAttributeListener</code>s in the web 
     * application.
     * <p>If an object was already bound to this session of this name that
     * implements <code>HttpSessionBindingListener</code>, its 
     * <code>HttpSessionBindingListener.valueUnbound</code> method is called.
     * <p>If the value passed in is null, this has the same effect as calling 
     * <code>removeAttribute()<code>.
     * @param name the name to which the object is bound; cannot be null
     * @param value the object to be bound
     * @throws IllegalStateException if the page has been invalidated
     */
    public void setAttribute(String name, Object value);

    /**
     * Removes the object bound with the specified name from this session.
     * If the session does not have an object bound with the specified name,
     * this method does nothing.
     * <p>After this method executes, and if the object implements
     * <code>HttpSessionBindingListener</code>, the container calls 
     * <code>HttpSessionBindingListener.valueUnbound</code>. The container
     * then notifies any <code>HttpSessionAttributeListener</code>s in the web 
     * application.
     * @param name the name of the object to remove from this session
     * @throws IllegalStateException if the page has been invalidated
     */
    public void removeAttribute(String name);

    /**
     * Returns an <code>Enumeration</code> of <code>String</code> objects
     * containing the names of all the objects bound to this session.
     * @return an <code>Iterator</code> of <code>String</code>s, specifying the
     *     names of all the objects bound to this session
     * @throws IllegalStateException if the page has been invalidated
     */
    public Iterator getAttributeNames();

    /**
     * Invalidates this session then unbinds any objects bound to it. 
     * @throws IllegalStateException if the page has been invalidated
     */
    public void invalidate();

    /**
     * Add a script to the list waiting for remote execution.
     * The version automatically wraps the string in a ClientScript object.
     * @param script The script to execute
     */
    public void addScript(String script);

    /**
     * Returns a string containing the unique identifier assigned to this
     * session. The identifier is assigned by the servlet container and is
     * implementation dependent.
     * @return a string specifying the identifier assigned to this session
     * @throws IllegalStateException if the page has been invalidated
     */
    public String getId();

    /**
     * Returns the time when this session was created, measured in milliseconds
     * since midnight January 1, 1970 GMT.
     * @return when was this page created, in milliseconds since 1/1/1970 GMT
     * @throws IllegalStateException if the page has been invalidated
     */
    public long getCreationTime();

    /**
     * Returns the last time the client sent a request associated with this
     * session, as the number of milliseconds since 1/1/1970 GMT, and marked by
     * the time the container recieved the request. 
     * <p>Actions that your application takes, such as getting or setting a
     * value associated with the session, do not affect the access time.
     * @return when was this page last accessed, in milliseconds since 1/1/1970 GMT
     * @throws IllegalStateException if the page has been invalidated
     */
    public long getLastAccessedTime();

    /**
     * While a Marshaller is processing a request it can register a
     * ScriptConduit with the ScriptSession to say - "pass scripts to me"
     * <p>
     * Several Marshallers may be active on the same page as a time and it
     * doesn't really matter which gets the script. So ScriptSession should
     * record all of the active ScriptConduits, but just pick one
     * @param conduit The new ScriptConduit
     * @see ScriptSession#removeScriptConduit(ScriptConduit)
     */
    public void addScriptConduit(ScriptConduit conduit);

    /**
     * Remove a ScriptConduit.
     * @param conduit The ScriptConduit to remove
     * @see ScriptSession#addScriptConduit(ScriptConduit)
     */
    public void removeScriptConduit(ScriptConduit conduit);

    /*
     * Specifies the time, in seconds, between client requests before the 
     * servlet container will invalidate this session. A negative time indicates
     * the session should never timeout.
     * @param interval An integer specifying the number of seconds 
     */
    //public void setMaxInactiveInterval(int interval);

    /*
     * Returns the maximum time interval, in seconds, that the servlet container
     * will keep this session open between client accesses.
     * After this interval, the servlet container will invalidate the session.
     * The maximum time interval can be set with the
     * <code>setMaxInactiveInterval</code> method.
     * A negative time indicates the session should never timeout.
     * @return the number of seconds this session remains open between client requests
     * @see #setMaxInactiveInterval
     */
    //public int getMaxInactiveInterval();
}
