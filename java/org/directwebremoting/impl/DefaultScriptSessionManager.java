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
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class DefaultScriptSessionManager implements ScriptSessionManager
{
    /* (non-Javadoc)
     * @see org.directwebremoting.ScriptSessionManager#getScriptSession(java.lang.String)
     */
    public RealScriptSession getScriptSession(String id)
    {
        DefaultScriptSession session;

        synchronized (sessionLock)
        {
            session = (DefaultScriptSession) sessionMap.get(id);
            if (session == null)
            {
                session = new DefaultScriptSession(id, this);
                sessionMap.put(id, session);
            }
            else
            {
                session.updateLastAccessedTime();
            }
        }

        return session;
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
     * @param session The session to get rid of
     */
    protected void invalidate(RealScriptSession session)
    {
        // Can we think of a reason why we need to sync both together?
        // It feels like a deadlock risk to do so
        synchronized (sessionLock)
        {
            RealScriptSession removed = (RealScriptSession) sessionMap.remove(session.getId());
            if (!session.equals(removed))
            {
                log.error("ScriptSession already removed from manager. session=" + session + " removed=" + removed);
            }

            int removeCount = 0;
            for (Iterator it = pageSessionMap.values().iterator(); it.hasNext();)
            {
                Set pageSessions = (Set) it.next();
                boolean isRemoved = pageSessions.remove(session);
                
                if (isRemoved)
                {
                    removeCount++;
                }
            }

            if (removeCount != 1)
            {
                log.error("DefaultScriptSessionManager.invalidate(): removeCount=" + removeCount + " when invalidating: " + session);
            }
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

                long age = now - session.getLastAccessedTime();
                if (age > scriptSessionTimeout)
                {
                    timeouts.add(session);
                }
            }
        }

        for (Iterator it = timeouts.iterator(); it.hasNext();)
        {
            DefaultScriptSession session = (DefaultScriptSession) it.next();
            session.invalidate();
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
     * How we turn pages into the canonical form.
     */
    protected PageNormalizer pageNormalizer;

    /**
     * How long do we wait before we timeout script sessions?
     */
    protected long scriptSessionTimeout = DEFAULT_TIMEOUT_MILLIS;

    /**
     * What we synchronize against when we want to access either sessionMap or
     * pageSessionMap
     */
    protected final Object sessionLock = new Object();

    /**
     * The map of all the known sessions
     */
    protected Map sessionMap = new HashMap();

    /**
     * The map of pages that have sessions
     */
    protected Map pageSessionMap = new HashMap();

    /**
     * The log stream
     */
    private static final Logger log = Logger.getLogger(DefaultScriptSessionManager.class);
}
