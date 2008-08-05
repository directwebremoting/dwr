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
package org.directwebremoting.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.swing.event.EventListenerList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.directwebremoting.ScriptSession;
import org.directwebremoting.ServerContext;
import org.directwebremoting.event.ScriptSessionEvent;
import org.directwebremoting.event.ScriptSessionListener;
import org.directwebremoting.extend.PageNormalizer;
import org.directwebremoting.extend.RealScriptSession;
import org.directwebremoting.extend.ScriptSessionManager;
import org.directwebremoting.util.IdGenerator;
import org.directwebremoting.util.SharedObjects;

/**
 * A default implementation of ScriptSessionManager.
 * <p>There are synchronization constraints on this class that could be broken
 * by subclasses. Specifically anyone accessing either <code>sessionMap</code>
 * or <code>pageSessionMap</code> must be holding the <code>sessionLock</code>.
 * <p>In addition you should note that {@link DefaultScriptSession} and
 * {@link DefaultScriptSessionManager} make calls to each other and you should
 * take care not to break any constraints in inheriting from these classes.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class DefaultScriptSessionManager implements ScriptSessionManager
{
    /**
     * Setup a timer that will invalidate sessions
     */
    public DefaultScriptSessionManager()
    {
        Runnable runnable = new Runnable()
        {
            public void run()
            {
                maybeCheckTimeouts();
            }
        };

        ScheduledThreadPoolExecutor executor = SharedObjects.getScheduledThreadPoolExecutor();
        executor.scheduleWithFixedDelay(runnable, 60, 60, TimeUnit.SECONDS);

        // Maybe we need be able to cancel the executor?
        // ScheduledFuture<?> future = executor.schedule...
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.ScriptSessionManager#getScriptSession(java.lang.String)
     */
    public RealScriptSession getScriptSession(String id, String page, String httpSessionId)
    {
        maybeCheckTimeouts();

        DefaultScriptSession scriptSession;

        synchronized (sessionLock)
        {
            scriptSession = sessionMap.get(id);
            if (scriptSession == null)
            {
                return null;
            }

            associateScriptSessionAndPage(scriptSession, page);
            associateScriptSessionAndHttpSession(scriptSession, httpSessionId);

            // Maybe we should update the access time of the ScriptSession?
            //  scriptSession.updateLastAccessedTime();
            // Since this call could come from outside of a call from the
            // browser, it's not really an indication that this session is still
            // alive, so we don't.
        }

        return scriptSession;
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.extend.ScriptSessionManager#createScriptSession(org.directwebremoting.extend.RealWebContext)
     */
    public RealScriptSession createScriptSession(String page, String httpSessionId)
    {
        String id = generator.generateId(16);

        DefaultScriptSession scriptSession = new DefaultScriptSession(id, this, page);

        synchronized (sessionLock)
        {
            sessionMap.put(id, scriptSession);

            associateScriptSessionAndPage(scriptSession, page);
            associateScriptSessionAndHttpSession(scriptSession, httpSessionId);
        }

        // See notes on synchronization in invalidate()
        fireScriptSessionCreatedEvent(scriptSession);

        return scriptSession;
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
    protected void associateScriptSessionAndHttpSession(RealScriptSession scriptSession, String httpSessionId)
    {
        scriptSession.setAttribute(ATTRIBUTE_HTTPSESSIONID, httpSessionId);

        Set<String> scriptSessionIds = sessionXRef.get(httpSessionId);
        if (scriptSessionIds == null)
        {
            scriptSessionIds = new HashSet<String>();
            sessionXRef.put(httpSessionId, scriptSessionIds);
        }
        scriptSessionIds.add(scriptSession.getId());
    }

    /**
     * Unlink any http sessions from this script session
     * @see #associateScriptSessionAndHttpSession(RealScriptSession, String)
     * @param scriptSession The script session to be unlinked
     */
    protected void disassociateScriptSessionAndHttpSession(RealScriptSession scriptSession)
    {
        Object httpSessionId = scriptSession.getAttribute(ATTRIBUTE_HTTPSESSIONID);
        if (httpSessionId == null)
        {
            return;
        }

        Set<String> scriptSessionIds = sessionXRef.get(httpSessionId);
        if (scriptSessionIds == null)
        {
            log.debug("Warning: No script session ids for http session");
            return;
        }
        scriptSessionIds.remove(scriptSession.getId());
        if (scriptSessionIds.size() == 0)
        {
            sessionXRef.remove(httpSessionId);
        }

        scriptSession.setAttribute(ATTRIBUTE_HTTPSESSIONID, null);
    }

    /**
     * Link a script session to a web page.
     * <p>This allows people to call {@link ServerContext#getScriptSessionsByPage(String)}
     * <p>This method is an ideal point to override and do something better.
     * @param scriptSession The script session to be linked to a page
     * @param page The page (un-normalized) to be linked to
     */
    protected void associateScriptSessionAndPage(RealScriptSession scriptSession, String page)
    {
        if (page == null)
        {
            return;
        }

        String normalizedPage = pageNormalizer.normalizePage(page);

        Set<RealScriptSession> pageSessions = pageSessionMap.get(normalizedPage);
        if (pageSessions == null)
        {
            pageSessions = new HashSet<RealScriptSession>();
            pageSessionMap.put(normalizedPage, pageSessions);
        }

        pageSessions.add(scriptSession);

        scriptSession.setAttribute(ATTRIBUTE_PAGE, normalizedPage);
    }

    /**
     * Unlink any pages from this script session
     * @see #associateScriptSessionAndPage(RealScriptSession, String)
     * @param scriptSession The script session to be unlinked
     */
    protected void disassociateScriptSessionAndPage(RealScriptSession scriptSession)
    {
        for (Set<RealScriptSession> pageSessions : pageSessionMap.values())
        {
            pageSessions.remove(scriptSession);
        }
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.ScriptSessionManager#getScriptSessionsByPage(java.lang.String)
     */
    public Collection<ScriptSession> getScriptSessionsByPage(String url)
    {
        String normalizedPage = pageNormalizer.normalizePage(url);
        synchronized (sessionLock)
        {
            Set<RealScriptSession> pageSessions = pageSessionMap.get(normalizedPage);
            if (pageSessions == null)
            {
                pageSessions = new HashSet<RealScriptSession>();
            }

            Set<ScriptSession> reply = new HashSet<ScriptSession>();
            reply.addAll(pageSessions);
            return reply;
        }
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.extend.ScriptSessionManager#getScriptSessionsByHttpSessionId(java.lang.String)
     */
    public Collection<RealScriptSession> getScriptSessionsByHttpSessionId(String httpSessionId)
    {
        Collection<RealScriptSession> reply = new ArrayList<RealScriptSession>();

        synchronized (sessionLock)
        {
            Set<String> scriptSessionIds = sessionXRef.get(httpSessionId);
            if (scriptSessionIds != null)
            {
                for (String scriptSessionId : scriptSessionIds)
                {
                    DefaultScriptSession scriptSession = sessionMap.get(scriptSessionId);
                    if (scriptSession != null)
                    {
                        reply.add(scriptSession);
                    }
                }
            }
        }

        return reply;
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.ScriptSessionManager#getAllScriptSessions()
     */
    public Collection<ScriptSession> getAllScriptSessions()
    {
        synchronized (sessionLock)
        {
            Set<ScriptSession> reply = new HashSet<ScriptSession>();
            reply.addAll(sessionMap.values());
            return reply;
        }
    }

    /**
     * Remove the given session from the list of sessions that we manage, and
     * leave it for the GC vultures to pluck.
     * @param scriptSession The session to get rid of
     */
    protected void invalidate(RealScriptSession scriptSession)
    {
        // Can we think of a reason why we need to sync both together?
        // It feels like a deadlock risk to do so
        synchronized (sessionLock)
        {
            // Due to the way systems get a number of script sessions for a page
            // and the perform a number of actions on them, we may get a number
            // of invalidation checks, and therefore calls to invalidate().
            // We could protect ourselves from this by having a
            // 'hasBeenInvalidated' flag, but we're taking the simple option
            // here of just allowing multiple invalidations.
            sessionMap.remove(scriptSession.getId());

            disassociateScriptSessionAndPage(scriptSession);
            disassociateScriptSessionAndHttpSession(scriptSession);
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

        synchronized (sessionLock)
        {
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

    /* (non-Javadoc)
     * @see org.directwebremoting.extend.ScriptSessionManager#addScriptSessionListener(org.directwebremoting.event.ScriptSessionListener)
     */
    public void addScriptSessionListener(ScriptSessionListener li)
    {
        scriptSessionListeners.add(ScriptSessionListener.class, li);
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.extend.ScriptSessionManager#removeScriptSessionListener(org.directwebremoting.event.ScriptSessionListener)
     */
    public void removeScriptSessionListener(ScriptSessionListener li)
    {
        scriptSessionListeners.remove(ScriptSessionListener.class, li);
    }

    /**
     * Accessor for the PageNormalizer.
     * @param pageNormalizer The new PageNormalizer
     */
    public void setPageNormalizer(PageNormalizer pageNormalizer)
    {
        this.pageNormalizer = pageNormalizer;
    }

    /**
     * @param scriptSessionCheckTime the scriptSessionCheckTime to set
     */
    public void setScriptSessionCheckTime(long scriptSessionCheckTime)
    {
        this.scriptSessionCheckTime = scriptSessionCheckTime;
    }

    /**
     * This should be called whenever a {@link ScriptSession} is created
     * @param scriptSession The newly created ScriptSession
     */
    protected void fireScriptSessionCreatedEvent(ScriptSession scriptSession)
    {
        ScriptSessionEvent ev = null;

        Object[] listeners = scriptSessionListeners.getListenerList();
        for (int i = 0; i < listeners.length - 2; i += 2)
        {
            if (listeners[i] == ScriptSessionListener.class)
            {
                if (ev == null)
                {
                    ev = new ScriptSessionEvent(scriptSession);
                }

                ((ScriptSessionListener) listeners[i + 1]).sessionCreated(ev);
            }
        }
    }

    /**
     * This should be called whenever a {@link ScriptSession} is destroyed
     * @param scriptSession The newly destroyed ScriptSession
     */
    protected void fireScriptSessionDestroyedEvent(ScriptSession scriptSession)
    {
        ScriptSessionEvent ev = null;

        Object[] listeners = scriptSessionListeners.getListenerList();
        for (int i = 0; i < listeners.length - 2; i += 2)
        {
            if (listeners[i] == ScriptSessionListener.class)
            {
                if (ev == null)
                {
                    ev = new ScriptSessionEvent(scriptSession);
                }

                ((ScriptSessionListener) listeners[i + 1]).sessionDestroyed(ev);
            }
        }
    }

    /**
     * Use of this attribute is currently discouraged, we may make this public
     * in a later release. Until then, it may change or be removed without warning.
     */
    public static final String ATTRIBUTE_HTTPSESSIONID = "org.directwebremoting.ScriptSession.HttpSessionId";

    /**
     * Use of this attribute is currently discouraged, we may make this public
     * in a later release. Until then, it may change or be removed without warning.
     */
    public static final String ATTRIBUTE_PAGE = "org.directwebremoting.ScriptSession.Page";

    /**
     * The list of current {@link ScriptSessionListener}s
     */
    protected EventListenerList scriptSessionListeners = new EventListenerList();

    /**
     * How we create script session ids.
     */
    private static IdGenerator generator = new IdGenerator();

    /**
     * By default we check for sessions that need expiring every 30 seconds
     */
    protected static final long DEFAULT_SESSION_CHECK_TIME = 30000;

    /**
     * How we turn pages into the canonical form.
     */
    protected PageNormalizer pageNormalizer;

    /**
     * How long do we wait before we timeout script sessions?
     */
    private long scriptSessionTimeout = DEFAULT_TIMEOUT_MILLIS;

    /**
     * How often do we check for script sessions that need timing out
     */
    protected long scriptSessionCheckTime = DEFAULT_SESSION_CHECK_TIME;

    /**
     * We check for sessions that need timing out every
     * {@link #scriptSessionCheckTime}; this is when we last checked.
     */
    protected long lastSessionCheckAt = System.currentTimeMillis();

    /**
     * What we synchronize against when we want to access either sessionMap or
     * pageSessionMap
     */
    protected final Object sessionLock = new Object();

    /**
     * Allows us to associate script sessions with http sessions.
     * The key is an http session id, the 
     * <p>GuardedBy("sessionLock")
     */
    private Map<String, Set<String>> sessionXRef = new HashMap<String, Set<String>>();

    /**
     * The map of all the known sessions.
     * The key is the script session id, the value is the session data
     * <p>GuardedBy("sessionLock")
     */
    private Map<String, DefaultScriptSession> sessionMap = new HashMap<String, DefaultScriptSession>();

    /**
     * The map of pages that have sessions.
     * The key is a normalized page, the value the script sessions that are
     * known to be currently visiting the page
     * <p>GuardedBy("sessionLock")
     */
    private Map<String, Set<RealScriptSession>> pageSessionMap = new HashMap<String, Set<RealScriptSession>>();

    /**
     * The log stream
     */
    private static final Log log = LogFactory.getLog(DefaultScriptSessionManager.class);
}
