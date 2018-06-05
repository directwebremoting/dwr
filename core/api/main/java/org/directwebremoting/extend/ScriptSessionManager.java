package org.directwebremoting.extend;

import java.util.Collection;

import javax.servlet.http.HttpSession;

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
    Collection<ScriptSession> getScriptSessionsByHttpSessionId(String httpSessionId);

    /**
     * Lookup the ScriptSession corresponding to a ScriptSession id.
     * @param scriptSessionId The id to lookup
     * @return The corresponding ScriptSession object, or null.
     */
    ScriptSession getScriptSessionById(String scriptSessionId);

    /**
     * For a given script session id, return the existing ScriptSession object
     * or create a new if needed.
     * @param id The id to get a ScriptSession object for
     * @param url The URL including 'http://', up to (but not including) '?' or '#' (or null if not known)
     * @param httpSession The session to associate the ScriptSession with (if any)
     * @return A ScriptSession to match the ID.
     */
    RealScriptSession getOrCreateScriptSession(String id, String url, HttpSession httpSession);

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
