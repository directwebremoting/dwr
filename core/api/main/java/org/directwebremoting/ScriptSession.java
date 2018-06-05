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
     * Returns the object bound with the specified name in this ScriptSession, or
     * <code>null</code> if no object is bound under the name.
     * @param name a string specifying the name of the object
     * @return the object with the specified name
     */
    Object getAttribute(String name);

    /**
     * Binds an object to this ScriptSession, using the name specified.
     * If an object of the same name is already bound to the ScriptSession, the
     * object is replaced.
     * <p>After this method executes, and if the new object implements
     * {@link ScriptSessionBindingListener}, the container calls
     * {@link ScriptSessionBindingListener#valueBound}.
     * <p>If an object was already bound to this ScriptSession of this name that
     * implements {@link ScriptSessionBindingListener}, its
     * {@link ScriptSessionBindingListener#valueUnbound} method is called.
     * <p>If the value passed in is null, this has the same effect as calling
     * {@link #removeAttribute}.
     * @param name the name to which the object is bound; cannot be null
     * @param value the object to be bound
     */
    void setAttribute(String name, Object value);

    /**
     * Removes the object bound with the specified name from this ScriptSession.
     * If the ScriptSession does not have an object bound with the specified name,
     * this method does nothing.
     * <p>After this method executes, and if the object implements
     * {@link ScriptSessionBindingListener}, the container calls
     * {@link ScriptSessionBindingListener#valueUnbound}.
     * @param name the name of the object to remove from this ScriptSession
     */
    void removeAttribute(String name);

    /**
     * Returns an <code>Enumeration</code> of <code>String</code> objects
     * containing the names of all the objects bound to this ScriptSession.
     * @return an <code>Iterator</code> of <code>String</code>s, specifying the
     *     names of all the objects bound to this ScriptSession
     */
    Iterator<String> getAttributeNames();

    /**
     * Invalidates this ScriptSession then unbinds any objects bound to it.
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
     * @param script The script to execute
     */
    void addScript(ScriptBuffer script);

    /**
     * Add a Runnable to the waiting list for execution. A new poll request will
     * be triggered and the runnable will be executed at the beginning of it.
     * The WebContext may be used to access request details from within the Runnable.
     * @param runnable
     */
    void addRunnable(Runnable runnable);

    /**
     * Returns a string containing the unique identifier assigned to this
     * ScriptSession. The identifier is assigned by DWR.
     * @return a string specifying the identifier assigned to this ScriptSession
     */
    String getId();

    /**
     * Returns the time when this ScriptSession was created, measured in milliseconds
     * since midnight January 1, 1970 GMT.
     * @return when was this ScriptSession created, in milliseconds since 1/1/1970 GMT
     */
    long getCreationTime();

    /**
     * Returns the last time the client sent a request associated with this
     * ScriptSession, as the number of milliseconds since 1/1/1970 GMT, and marked by
     * the time the container received the request.
     * <p>Actions that your application takes, such as getting or setting a
     * value associated with the session, do not affect the access time.
     * @return when was this ScriptSession last accessed, in milliseconds since 1/1/1970 GMT
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

    /**
     * If this ScriptSession's browser session also has a HttpSession then this method
     * gives access to its id (typically corresponding to the JSESSIONID cookie).
     * @return the id from an active associated HttpSession if any, otherwise null
     */
    String getHttpSessionId();
}
