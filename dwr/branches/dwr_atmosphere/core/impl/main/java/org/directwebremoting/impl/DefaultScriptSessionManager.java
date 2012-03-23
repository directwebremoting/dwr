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
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.directwebremoting.Container;
import org.directwebremoting.ScriptSession;
import org.directwebremoting.event.ScriptSessionEvent;
import org.directwebremoting.event.ScriptSessionListener;
import org.directwebremoting.extend.InitializingBean;
import org.directwebremoting.extend.PageNormalizer;
import org.directwebremoting.extend.RealScriptSession;
import org.directwebremoting.extend.ScriptSessionManager;
import org.directwebremoting.extend.UninitializingBean;
import org.directwebremoting.util.Loggers;

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
     * @see org.directwebremoting.extend.UninitializingBean#contextDestroyed()
     */
    public void contextDestroyed()
    {
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.extend.UninitializingBean#servletDestroyed()
     */
    public void servletDestroyed()
    {
        future.cancel(true);
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.ScriptSessionManager#getScriptSession(java.lang.String)
     */
    public RealScriptSession getScriptSession(String sentScriptId, String page, String httpSessionId)
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
                    Loggers.SESSION.error("Invalid Page: Passed page=" + page + ", but page in script session=" + storedPage);
                    throw new SecurityException("Invalid Page");
                }
            }

            associateScriptSessionAndPage(scriptSession, page);
            associateScriptSessionAndHttpSession(scriptSession, httpSessionId);
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
     * <p/>
     * In case you may decide to provide your own implementation, please be
     * aware of synchronization requirements for e.g.
     * {@link org.directwebremoting.impl.DefaultScriptSession#scripts}. Synchronization
     * examples can be found in
     * {@link DefaultScriptSession#hasWaitingScripts()} or
     * {@link org.directwebremoting.impl.DefaultScriptSession#addScriptConduit(org.directwebremoting.extend.ScriptConduit)}.
     *
     * @param sentScriptId The script ID that was sent by the browser, and should
     * likely be used to create the DefaultScripSession.
     * @param page The page for which the scriptsession needs to be created.
     * @return the DefaultScriptSession object for this <code>sentScriptId</code>
     * and <code>page</code>
     */
    protected DefaultScriptSession createScriptSession(String sentScriptId, String page)
    {
        return new DefaultScriptSession(sentScriptId, this, page);
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
     * @see #associateScriptSessionAndHttpSession(DefaultScriptSession, String)
     * @param scriptSession The script session to be unlinked
     */
    protected void disassociateScriptSessionAndHttpSession(DefaultScriptSession scriptSession)
    {
        Object httpSessionId = scriptSession.getAttribute(ATTRIBUTE_HTTPSESSIONID);
        if (httpSessionId == null)
        {
            return;
        }

        Set<String> scriptSessionIds = sessionXRef.get(httpSessionId);
        if (scriptSessionIds == null)
        {
            Loggers.SESSION.debug("Warning: No script session ids for http session");
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
     * <p>This allows people to call {@link org.directwebremoting.Browser#withPage}
     * <p>This method is an ideal point to override and do something better.
     * @param scriptSession The script session to be linked to a page
     * @param page The page (un-normalized) to be linked to
     */
    protected void associateScriptSessionAndPage(DefaultScriptSession scriptSession, String page)
    {
        if (page == null)
        {
            return;
        }

        String normalizedPage = pageNormalizer.normalizePage(page);

        Set<DefaultScriptSession> pageSessions = pageSessionMap.get(normalizedPage);
        if (pageSessions == null)
        {
            pageSessions = new HashSet<DefaultScriptSession>();
            Set<DefaultScriptSession> prev = pageSessionMap.putIfAbsent(normalizedPage, pageSessions);
            if (prev != null)
            {
                pageSessions = prev;
            }
        }

        pageSessions.add(scriptSession);
        scriptSession.setAttribute(ATTRIBUTE_PAGE, normalizedPage);
    }

    /**
     * Unlink any pages from this script session
     * @see #associateScriptSessionAndPage(DefaultScriptSession, String)
     * @param scriptSession The script session to be unlinked
     */
    protected void disassociateScriptSessionAndPage(DefaultScriptSession scriptSession)
    {
        for (Set<DefaultScriptSession> pageSessions : pageSessionMap.values())
        {
            pageSessions.remove(scriptSession);
        }
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.extend.ScriptSessionManager#getScriptSessionsByHttpSessionId(java.lang.String)
     */
    public Collection<RealScriptSession> getScriptSessionsByHttpSessionId(String httpSessionId)
    {
        Collection<RealScriptSession> reply = new ArrayList<RealScriptSession>();

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

        disassociateScriptSessionAndPage(scriptSession);
        disassociateScriptSessionAndHttpSession(scriptSession);

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

    /**
     * @see #setScheduledThreadPoolExecutor(ScheduledThreadPoolExecutor)
     */
    protected ScheduledThreadPoolExecutor executor;

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
     * Allows us to associate script sessions with http sessions.
     * The key is an http session id, the
     */
    protected final Map<String, Set<String>> sessionXRef = new ConcurrentHashMap<String, Set<String>>();

    /**
     * The map of all the known sessions.
     * The key is the script session id, the value is the session data
     */
    protected final ConcurrentMap<String, DefaultScriptSession> sessionMap = new ConcurrentHashMap<String, DefaultScriptSession>();

    /**
     * The map of pages that have sessions.
     * The key is a normalized page, the value the script sessions that are
     * known to be currently visiting the page
     */
    protected final ConcurrentMap<String, Set<DefaultScriptSession>> pageSessionMap = new ConcurrentHashMap<String, Set<DefaultScriptSession>>();
}
