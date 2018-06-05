package org.directwebremoting;

import java.util.Collection;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;

/**
 * ServerContext is something of a misnomer - it refers to a running DwrServlet
 * (or a similar concept when DWR is run inside Guice/Spring/etc).
 * ServerContext is useful for threads that are NOT created by DWR to allow
 * them to interact with the environment that the DWR instance knows about.
 * <p>If you are running in a DWR thread then you are probably better off using
 * {@link WebContext} in place of {@link ServerContext}.
 * <p>From DWR 3, you are probably better off looking towards {@link Browser},
 * {@link ScriptSessions} or the various reverse ajax proxy APIs in order to
 * interact with web-clients.
 * <p>
 * {@link ServerContext} is accessible from {@link ServerContextFactory#get()}.
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
     * @deprecated Use {@link Browser#withPage(String, Runnable)} instead
     */
    @Deprecated
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
     * @deprecated Use {@link Browser#withSession(String, Runnable)} instead
     */
    @Deprecated
    ScriptSession getScriptSessionById(String sessionId);

    /**
     * Get a list of all the ScriptSessions known to this server at the given
     * time.
     * Note that the list of known sessions is continually changing so it is
     * possible that the list will be out of date by the time it is used. For
     * this reason you should check that getScriptSession(String id) returns
     * something non null.
     * @return A collection of all the ScriptSessions.
     * @deprecated Use {@link Browser#withAllSessions(Runnable)} instead
     */
    @Deprecated
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
     * <p>Annoyingly you can't get to this from the {@link ServletContext} until
     * servlet 2.5 you need to cache the value from a recent HttpServletRequest.
     * <p>The context path always comes first in a request URI.  The path starts
     * with a "/" character but does not end with a "/" character.
     * For servlets in the default (root) context, this method returns "".
     * The container does not decode this string.
     * <p>WARNING: This method may return null if DWR has not received any
     * requests. If this method is called from outside of DWR, as the servlet
     * environment is starting up you should check for a null reply and try
     * again later.
     * @return The portion of the request URI that indicates the context or null
     * if DWR has not received and requests so far
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
