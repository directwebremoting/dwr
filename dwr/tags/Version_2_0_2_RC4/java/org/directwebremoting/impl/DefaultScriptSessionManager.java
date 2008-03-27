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
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.directwebremoting.extend.PageNormalizer;
import org.directwebremoting.extend.RealScriptSession;
import org.directwebremoting.extend.ScriptSessionManager;
import org.directwebremoting.util.Logger;

/**
 * A default implmentation of ScriptSessionManager.
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
    /* (non-Javadoc)
     * @see org.directwebremoting.ScriptSessionManager#getScriptSession(java.lang.String)
     */
    public RealScriptSession getScriptSession(String id)
    {
        maybeCheckTimeouts();

        synchronized (sessionLock)
        {
            DefaultScriptSession scriptSession = (DefaultScriptSession) sessionMap.get(id);
            if (scriptSession == null)
            {
                scriptSession = new DefaultScriptSession(id, this);
                sessionMap.put(id, scriptSession);
            }
            else
            {
                scriptSession.updateLastAccessedTime();
            }

            return scriptSession;
        }
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.ScriptSessionManager#setPageForScriptSession(org.directwebremoting.extend.RealScriptSession, java.lang.String)
     */
    public void setPageForScriptSession(RealScriptSession scriptSession, String page)
    {
        String normalizedPage = pageNormalizer.normalizePage(page);
        synchronized (sessionLock)
        {
            Set pageSessions = (Set) pageSessionMap.get(normalizedPage);
            if (pageSessions == null)
            {
                pageSessions = new HashSet();
                pageSessionMap.put(normalizedPage, pageSessions);
            }

            pageSessions.add(scriptSession);
        }
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.ScriptSessionManager#getScriptSessionsByPage(java.lang.String)
     */
    public Collection getScriptSessionsByPage(String page)
    {
        String normalizedPage = pageNormalizer.normalizePage(page);
        synchronized (sessionLock)
        {
            Set pageSessions = (Set) pageSessionMap.get(normalizedPage);
            if (pageSessions == null)
            {
                pageSessions = new HashSet();
            }

            Set reply = new HashSet();
            reply.addAll(pageSessions);
            return reply;
        }
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.ScriptSessionManager#getAllScriptSessions()
     */
    public Collection getAllScriptSessions()
    {
        synchronized (sessionLock)
        {
            Set reply = new HashSet();
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
            RealScriptSession removed = (RealScriptSession) sessionMap.remove(scriptSession.getId());
            if (!scriptSession.equals(removed))
            {
                log.error("ScriptSession already removed from manager. scriptSession=" + scriptSession + " removed=" + removed);
            }

            int removeCount = 0;
            for (Iterator it = pageSessionMap.values().iterator(); it.hasNext();)
            {
                Set pageSessions = (Set) it.next();
                boolean isRemoved = pageSessions.remove(scriptSession);

                if (isRemoved)
                {
                    removeCount++;
                }
            }

            if (removeCount != 1)
            {
                log.error("DefaultScriptSessionManager.invalidate(): removeCount=" + removeCount + " when invalidating: " + scriptSession);
            }
        }
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
        List timeouts = new ArrayList();

        synchronized (sessionLock)
        {
            for (Iterator it = sessionMap.values().iterator(); it.hasNext();)
            {
                DefaultScriptSession session = (DefaultScriptSession) it.next();

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

            for (Iterator it = timeouts.iterator(); it.hasNext();)
            {
                DefaultScriptSession session = (DefaultScriptSession) it.next();
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
    public void setScriptSessionTimeout(long timeout)
    {
        this.scriptSessionTimeout = timeout;
    }

    /**
     * Accessfor for the PageNormalizer.
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
    protected long scriptSessionTimeout = DEFAULT_TIMEOUT_MILLIS;

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
     * The map of all the known sessions
     * <p>GuardedBy("sessionLock")
     */
    protected Map sessionMap = new HashMap();

    /**
     * The map of pages that have sessions
     * <p>GuardedBy("sessionLock")
     */
    protected Map pageSessionMap = new HashMap();

    /**
     * The log stream
     */
    private static final Logger log = Logger.getLogger(DefaultScriptSessionManager.class);
}
