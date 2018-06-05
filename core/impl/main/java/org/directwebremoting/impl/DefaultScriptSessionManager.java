package org.directwebremoting.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;

import org.directwebremoting.Container;
import org.directwebremoting.ScriptSession;
import org.directwebremoting.event.ScriptSessionEvent;
import org.directwebremoting.event.ScriptSessionListener;
import org.directwebremoting.extend.ConverterManager;
import org.directwebremoting.extend.InitializingBean;
import org.directwebremoting.extend.PageNormalizer;
import org.directwebremoting.extend.RealScriptSession;
import org.directwebremoting.extend.ScriptSessionManager;
import org.directwebremoting.extend.UninitializingBean;
import org.directwebremoting.util.Loggers;

/**
 * A default implementation of ScriptSessionManager.
 * <p>You should note that {@link DefaultScriptSession} and
 * {@link DefaultScriptSessionManager} make calls to each other and you should
 * take care not to break any constraints in inheriting from these classes.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class DefaultScriptSessionManager implements ScriptSessionManager, InitializingBean, UninitializingBean
{
    /* (non-Javadoc)
     * @see org.directwebremoting.extend.InitializingBean#afterContainerSetup(org.directwebremoting.Container)
     */
    public void afterContainerSetup(Container container)
    {
        Runnable runnable = new Runnable()
        {
            public void run()
            {
                maybeCheckTimeouts();
            }
        };

        future = executor.scheduleWithFixedDelay(runnable, 60, 60, TimeUnit.SECONDS);
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.extend.UninitializingBean#destroy()
     */
    public void destroy()
    {
        future.cancel(true);
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.ScriptSessionManager#getScriptSession(java.lang.String)
     */
    public RealScriptSession getOrCreateScriptSession(String sentScriptId, String page, HttpSession httpSession)
    {
        maybeCheckTimeouts();
        DefaultScriptSession scriptSession;
        // We only use a "full" script session when we got an id from the
        // client layer. When there is no script session id we just create
        // a temporary script session for the duration of the call
        if ("".equals(sentScriptId))
        {
            scriptSession = createScriptSession("", page);
            Loggers.SESSION.debug("Creating temporary script session on " + scriptSession.getPage());
        }
        else
        {
            scriptSession = sessionMap.get(sentScriptId);
            if (scriptSession == null)
            {
                scriptSession = createScriptSession(sentScriptId, page);
                Loggers.SESSION.debug("Creating " + scriptSession + " on " + scriptSession.getPage());
                sessionMap.putIfAbsent(sentScriptId, scriptSession);
                // See notes on synchronization in invalidate()
                fireScriptSessionCreatedEvent(scriptSession);
            }
            else
            {
                // This could be called from a poll or an rpc call, so this is a
                // good place to update the session access time
                scriptSession.updateLastAccessedTime();

                String storedPage = scriptSession.getPage();
                if (!storedPage.equals(page))
                {
                    scriptSession.setPage(page);
                }
            }

            final String httpSessionId = (httpSession != null ? httpSession.getId() : null);

            // Keep ScriptSession association to its HttpSession (if any)
            synchronized (scriptSession)
            {
                if (scriptSession.getHttpSessionId() != null && !scriptSession.getHttpSessionId().equals(httpSessionId))
                {
                    disassociateScriptSessionAndHttpSession(scriptSession, httpSessionId);
                }
                if (httpSessionId != null && !httpSessionId.equals(scriptSession.getHttpSessionId()))
                {
                    associateScriptSessionAndHttpSession(scriptSession, httpSessionId);
                }
            }

            // Listen for HttpSession invalidation to update association to ScriptSession
            if (httpSession != null)
            {
                HttpSessionBindingListener invalidationListener = (HttpSessionBindingListener) httpSession.getAttribute(ATTRIBUTE_INVALIDATIONLISTENER);
                if (invalidationListener == null)
                {
                    synchronized (this)
                    {
                        // This is a valid use of DCL as the getAttribute() calls are synchronized
                        invalidationListener = (HttpSessionBindingListener) httpSession.getAttribute(ATTRIBUTE_INVALIDATIONLISTENER);
                        if (invalidationListener == null)
                        {
                            httpSession.setAttribute(ATTRIBUTE_INVALIDATIONLISTENER, new InvalidationListener(this, httpSessionId));
                        }
                    }
                }
            }
        }

        // Maybe we should update the access time of the ScriptSession?
        // scriptSession.updateLastAccessedTime();
        // Since this call could come from outside of a call from the
        // browser, it's not really an indication that this session is still
        // alive, so we don't.

        return scriptSession;
    }

    /**
     * Extension point allowing for custom implementations of the DefaultScriptSession.
     * @param sentScriptId The script ID that was sent by the browser, and should
     * likely be used to create the DefaultScripSession.
     * @param page The page for which the scriptsession needs to be created.
     * @return the DefaultScriptSession object for this <code>sentScriptId</code>
     * and <code>page</code>
     */
    protected DefaultScriptSession createScriptSession(String sentScriptId, String page)
    {
        return new DefaultScriptSession(sentScriptId, this, page, converterManager, jsonOutput);
    }

    /**
     * Link a script session and an http session in some way
     * Exactly what we should do here is still something of a mystery. We don't
     * really have much experience on the best way to handle this, so currently
     * we're just setting a script session attribute that points at the
     * http session id, and not exposing it.
     * <p>This method is an ideal point to override and do something better.
     * @param scriptSession The script session to be linked to an http session
     * @param httpSessionId The http session from the browser with the given scriptSession
     */
    protected void associateScriptSessionAndHttpSession(DefaultScriptSession scriptSession, String httpSessionId)
    {
        if (httpSessionId == null)
        {
            return;
        }

        // We may have to retry as we are (deliberately) not locking the parent map
        boolean done = false;
        do {
            Set<DefaultScriptSession> scriptSessions = httpSessionXref.get(httpSessionId);
            if (scriptSessions == null)
            {
                scriptSessions = new HashSet<DefaultScriptSession>();
                httpSessionXref.putIfAbsent(httpSessionId, scriptSessions);
            }

            // We use the lock on the script session set to coordinate both access
            // to the set AND to lock the map key pointing to it
            synchronized (scriptSessions)
            {
                if (httpSessionXref.get(httpSessionId) != scriptSessions) {
                    // The key mapping was changed by another thread before we
                    // managed to lock it.
                    // Retry by taking another spin in the loop.
                }
                else
                {
                    // We have a lock on the script session set and we know it is
                    // mapped by the key, so safe to add the new script session and
                    // consider us done
                    scriptSessions.add(scriptSession);
                    done = true;
                }
            }
        } while(!done);

        // Update field on ScriptSession outside synchronized block to avoid deadlocks
        scriptSession.setHttpSessionId(httpSessionId);
    }

    /**
     * Unlink any http sessions from this script session
     */
    protected void disassociateScriptSessionAndHttpSession(DefaultScriptSession scriptSession, String httpSessionId)
    {
        if (httpSessionId == null)
        {
            return;
        }

        Set<DefaultScriptSession> scriptSessions = httpSessionXref.get(httpSessionId);
        if (scriptSessions == null)
        {
            return;
        }

        // We use the lock on the script session set to coordinate both access
        // to the set AND to lock the map key pointing to it
        synchronized (scriptSessions)
        {
            if (!scriptSessions.contains(scriptSession))
            {
                return;
            }

            scriptSessions.remove(scriptSession);
            if (scriptSessions.size() == 0)
            {
                httpSessionXref.remove(httpSessionId);
            }
        }

        // Update field on ScriptSession outside synchronized block to avoid deadlocks
        scriptSession.setHttpSessionId(null);
    }

    private void disassociateAllScriptSessionsFromHttpSession(String httpSessionId)
    {
        if (httpSessionId == null)
        {
            return;
        }

        Set<DefaultScriptSession> scriptSessions = httpSessionXref.get(httpSessionId);
        if (scriptSessions == null)
        {
            return;
        }

        // We use the lock on the script session set to coordinate both access
        // to the set AND to lock the map key pointing to it
        Set<DefaultScriptSession> copy = new HashSet<DefaultScriptSession>();
        synchronized (scriptSessions)
        {
            copy.addAll(scriptSessions);
            scriptSessions.clear();
            httpSessionXref.remove(httpSessionId);
        }

        // Update field on all the ScriptSessions outside synchronized block to avoid deadlocks
        for(DefaultScriptSession scriptSession : copy)
        {
            synchronized (scriptSession)
            {
                if (httpSessionId.equals(scriptSession.getHttpSessionId()))
                {
                    scriptSession.setHttpSessionId(null);
                }
            }
        }
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.extend.ScriptSessionManager#getScriptSessionById(java.lang.String)
     */
    public ScriptSession getScriptSessionById(String scriptSessionId)
    {
        return sessionMap.get(scriptSessionId);
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.extend.ScriptSessionManager#getScriptSessionsByHttpSessionId(java.lang.String)
     */
    public Collection<ScriptSession> getScriptSessionsByHttpSessionId(String httpSessionId)
    {
        Collection<ScriptSession> reply = new HashSet<ScriptSession>();

        Set<? extends ScriptSession> scriptSessions = httpSessionXref.get(httpSessionId);
        if (scriptSessions != null)
        {
            // We need to lock the set to keep it stable from updates in associate()
            // and disassociate() while we loop
            synchronized (scriptSessions)
            {
                reply.addAll(scriptSessions);
            }
        }

        return reply;
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.ScriptSessionManager#getAllScriptSessions()
     */
    public Collection<ScriptSession> getAllScriptSessions()
    {
        Set<ScriptSession> reply = new HashSet<ScriptSession>();
        reply.addAll(sessionMap.values());
        return reply;
    }

    /**
     * Remove the given session from the list of sessions that we manage, and
     * leave it for the GC vultures to pluck.
     * @param scriptSession The session to get rid of
     */
    protected void invalidate(DefaultScriptSession scriptSession)
    {
        Loggers.SESSION.debug("Invalidating " + scriptSession + " from " + scriptSession.getPage());

        // Due to the way systems get a number of script sessions for a page
        // and the perform a number of actions on them, we may get a number
        // of invalidation checks, and therefore calls to invalidate().
        // We could protect ourselves from this by having a
        // 'hasBeenInvalidated' flag, but we're taking the simple option
        // here of just allowing multiple invalidations.
        sessionMap.remove(scriptSession.getId());

        synchronized (scriptSession)
        {
            disassociateScriptSessionAndHttpSession(scriptSession, scriptSession.getHttpSessionId());
        }

        // Are there any risks from doing this outside the locks?
        // The initial analysis is that 'Destroyed' is past tense so you would
        // have expected it to have happened already.
        fireScriptSessionDestroyedEvent(scriptSession);
    }

    /**
     * If we call {@link #checkTimeouts()} too often is could bog things down so
     * we only check every one in a while (default 30 secs); this checks to see
     * of we need to check, and checks if we do.
     */
    protected void maybeCheckTimeouts()
    {
        long now = System.currentTimeMillis();
        if (now - scriptSessionCheckTime > lastSessionCheckAt)
        {
            checkTimeouts();
            lastSessionCheckAt = now;
        }
    }

    /**
     * Do a check on all the known sessions to see if and have timeout and need
     * removing.
     */
    protected void checkTimeouts()
    {
        long now = System.currentTimeMillis();
        List<ScriptSession> timeouts = new ArrayList<ScriptSession>();

        for (DefaultScriptSession session : sessionMap.values())
        {
            if (session.isInvalidated())
            {
                continue;
            }

            long age = now - session.getLastAccessedTime();
            if (age > scriptSessionTimeout)
            {
                timeouts.add(session);
            }
        }

        for (ScriptSession scriptSession : timeouts)
        {
            DefaultScriptSession session = (DefaultScriptSession) scriptSession;
            session.invalidate();
        }
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.extend.ScriptSessionManager#addScriptSessionListener(org.directwebremoting.event.ScriptSessionListener)
     */
    public void addScriptSessionListener(ScriptSessionListener li)
    {
        scriptSessionListeners.add(li);
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.extend.ScriptSessionManager#removeScriptSessionListener(org.directwebremoting.event.ScriptSessionListener)
     */
    public void removeScriptSessionListener(ScriptSessionListener li)
    {
        scriptSessionListeners.remove(li);
    }

    /**
     * This should be called whenever a {@link ScriptSession} is created
     * @param scriptSession The newly created ScriptSession
     */
    protected void fireScriptSessionCreatedEvent(ScriptSession scriptSession)
    {
        ScriptSessionEvent ev = null;
        for (int i = scriptSessionListeners.size() - 1; i >= 0; i--)
        {
            if (ev == null)
            {
                ev = new ScriptSessionEvent(scriptSession);
            }
            scriptSessionListeners.get(i).sessionCreated(ev);
        }
    }

    /**
     * This should be called whenever a {@link ScriptSession} is destroyed
     * @param scriptSession The newly destroyed ScriptSession
     */
    protected void fireScriptSessionDestroyedEvent(ScriptSession scriptSession)
    {
        ScriptSessionEvent ev = null;
        for (int i = scriptSessionListeners.size() - 1; i >= 0; i--)
        {
            if (ev == null)
            {
                ev = new ScriptSessionEvent(scriptSession);
            }
            scriptSessionListeners.get(i).sessionDestroyed(ev);
        }
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.extend.ScriptSessionManager#getInitCode()
     */
    public String getInitCode()
    {
        return "dwr.engine._execute(dwr.engine._pathToDwrServlet, '__System', 'pageLoaded', [ function() { dwr.engine._ordered = false; }]);";
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.ScriptSessionManager#getScriptSessionTimeout()
     */
    public long getScriptSessionTimeout()
    {
        return scriptSessionTimeout;
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.ScriptSessionManager#setScriptSessionTimeout(long)
     */
    public void setScriptSessionTimeout(long scriptSessionTimeout)
    {
        this.scriptSessionTimeout = scriptSessionTimeout;
    }

    /**
     * How long do we wait before we timeout script sessions?
     */
    private long scriptSessionTimeout = DEFAULT_TIMEOUT_MILLIS;

    /**
     * How we turn pages into the canonical form.
     * @param pageNormalizer The new PageNormalizer
     */
    public void setPageNormalizer(PageNormalizer pageNormalizer)
    {
        this.pageNormalizer = pageNormalizer;
    }

    /**
     * @see #setPageNormalizer(PageNormalizer)
     */
    protected PageNormalizer pageNormalizer;

    /**
     * How often do we check for script sessions that need timing out
     */
    public void setScriptSessionCheckTime(long scriptSessionCheckTime)
    {
        this.scriptSessionCheckTime = scriptSessionCheckTime;
    }

    /**
     * @see #setScriptSessionCheckTime(long)
     */
    protected long scriptSessionCheckTime = DEFAULT_SESSION_CHECK_TIME;

    /**
     * How often do we check for script sessions that need timing out
     */
    public void setScheduledThreadPoolExecutor(ScheduledThreadPoolExecutor executor)
    {
        this.executor = executor;
    }

   /*
    * Accessor for the ConverterManager that we configure
    * @param converterManager
    */
   public void setConverterManager(ConverterManager converterManager)
   {
       this.converterManager = converterManager;
   }

   /**
    * How we convert parameters
    */
   protected ConverterManager converterManager = null;

   /**
    * @param jsonOutput Are we outputting in JSON mode?
    */
   public void setJsonOutput(boolean jsonOutput)
   {
       this.jsonOutput = jsonOutput;
   }

   /**
    * Are we outputting in JSON mode?
    */
   protected boolean jsonOutput = false;

    /**
     * @see #setScheduledThreadPoolExecutor(ScheduledThreadPoolExecutor)
     */
    protected ScheduledThreadPoolExecutor executor;

    /**
     * HttpSession attribute that stores the invalidation listener
     */
    protected static final String ATTRIBUTE_INVALIDATIONLISTENER = DefaultScriptSessionManager.class.getName() + ".InvalidationListener";

    /**
     * By default we check for sessions that need expiring every 30 seconds
     */
    protected static final long DEFAULT_SESSION_CHECK_TIME = 30000;

    /**
     * The list of current {@link ScriptSessionListener}s
     */
    protected List<ScriptSessionListener> scriptSessionListeners = new CopyOnWriteArrayList<ScriptSessionListener>();

    /**
     * The session timeout checker function so we can shutdown cleanly
     */
    private volatile ScheduledFuture<?> future;

    /**
     * We check for sessions that need timing out every
     * {@link #scriptSessionCheckTime}; this is when we last checked.
     */
    protected volatile long lastSessionCheckAt = System.currentTimeMillis();

    /**
     * The map of all the known sessions.
     * The key is the script session id, the value is the session data
     */
    protected final ConcurrentMap<String, DefaultScriptSession> sessionMap = new ConcurrentHashMap<String, DefaultScriptSession>();

    /**
     * Allows us to associate script sessions with http sessions.
     * The key is an http session id, the value is a set of script sessions
     */
    protected final ConcurrentMap<String, Set<DefaultScriptSession>> httpSessionXref = new ConcurrentHashMap<String, Set<DefaultScriptSession>>();

    /**
     * BindingListener used to detect session destroy so we can clean up our ScriptSession cross-reference.
     * @author Mike Wilson
     */
    private static class InvalidationListener implements HttpSessionBindingListener, Serializable
    {
        private final transient DefaultScriptSessionManager scriptSessionManager;
        private final transient String httpSessionId;
        public InvalidationListener(DefaultScriptSessionManager scriptSessionManager, String httpSessionId)
        {
            this.scriptSessionManager = scriptSessionManager;
            this.httpSessionId = httpSessionId;
        }
        public void valueBound(HttpSessionBindingEvent arg0)
        {
            // NOP
        }
        public void valueUnbound(HttpSessionBindingEvent arg0)
        {
            if (scriptSessionManager != null && httpSessionId != null)
            {
                scriptSessionManager.disassociateAllScriptSessionsFromHttpSession(httpSessionId);
            }
        }
    }
}
