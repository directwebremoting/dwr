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

import org.directwebremoting.ScriptSession;
import org.directwebremoting.ScriptSessionManager;
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
    public ScriptSession getScriptSession(String id)
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
     * @see org.directwebremoting.ScriptSessionManager#setPageForScriptSession(org.directwebremoting.ScriptSession, java.lang.String)
     */
    public void setPageForScriptSession(ScriptSession scriptSession, String page)
    {
        synchronized (sessionLock)
        {
            Set pageSessions = (Set) pageSessionMap.get(page);
            if (pageSessions == null)
            {
                pageSessions = new HashSet();
                pageSessionMap.put(page, pageSessions);
            }

            pageSessions.add(scriptSession);
        }
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.ScriptSessionManager#getScriptSessionsByPage(java.lang.String)
     */
    public Collection getScriptSessionsByPage(String page)
    {
        synchronized (sessionLock)
        {
            Set pageSessions = (Set) pageSessionMap.get(page);
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
    protected void invalidate(ScriptSession session)
    {
        // Can we think of a reason why we need to sync both together?
        // It feels like a deadlock risk to do so
        synchronized (sessionLock)
        {
            ScriptSession removed = (ScriptSession) sessionMap.remove(session.getId());

            if (!session.equals(removed))
            {
                throw new IllegalStateException("Can't remove session from all sessons, session=" + session + " removed=" + removed); //$NON-NLS-1$ //$NON-NLS-2$
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
                throw new IllegalStateException("Remove count=" + removeCount); //$NON-NLS-1$
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
     * Debug the current status
     */
    public void debug()
    {
        if (log.isDebugEnabled())
        {
            synchronized (sessionLock)
            {
                log.debug("DefaultScriptSessionManager containing the following sessions:"); //$NON-NLS-1$
                for (Iterator sit = sessionMap.entrySet().iterator(); sit.hasNext();)
                {
                    Map.Entry idEntry = (Map.Entry) sit.next();
                    String id = (String) idEntry.getKey();
                    ScriptSession session = (ScriptSession) idEntry.getValue();
    
                    log.debug("- " + id + "=" + session); //$NON-NLS-1$ //$NON-NLS-2$
    
                    for (Iterator pit = pageSessionMap.entrySet().iterator(); pit.hasNext();)
                    {
                        Map.Entry pageEntry = (Map.Entry) pit.next();
                        String page = (String) pageEntry.getKey();
                        Set pageSessions = (Set) pageEntry.getValue();
    
                        if (pageSessions.contains(session))
                        {
                            log.debug("  - on page=" + page); //$NON-NLS-1$
                        }
                    }
    
                    if (session instanceof DefaultScriptSession)
                    {
                        DefaultScriptSession dss = (DefaultScriptSession) session;
    
                        log.debug("  - creationTime=" + dss.creationTime); //$NON-NLS-1$
                        log.debug("  - lastAccessedTime=" + dss.lastAccessedTime); //$NON-NLS-1$
    
                        // Debug the attributes
                        if (dss.attributes.size() == 0)
                        {
                            log.debug("  - no attributes"); //$NON-NLS-1$
                        }
                        else
                        {
                            log.debug("  - with attributes:"); //$NON-NLS-1$
                            for (Iterator it = dss.attributes.entrySet().iterator(); it.hasNext();)
                            {
                                Map.Entry entry = (Map.Entry) it.next();
                                log.debug("    - " + entry.getKey() + "=" + entry.getValue()); //$NON-NLS-1$ //$NON-NLS-2$
                            }
                        }
    
                        // Debug the Conduits
                        if (dss.conduits.size() == 0)
                        {
                            log.debug("  - no conduits"); //$NON-NLS-1$
                        }
                        else
                        {
                            log.debug("  - with conduits:"); //$NON-NLS-1$
                            for (Iterator it = dss.conduits.iterator(); it.hasNext();)
                            {
                                log.debug("    - " + it.next()); //$NON-NLS-1$
                            }
                        }
    
                        // Debug the Scripts
                        if (dss.scripts.size() == 0)
                        {
                            log.debug("  - no waiting scripts"); //$NON-NLS-1$
                        }
                        else
                        {
                            log.debug("  - with waiting scripts:"); //$NON-NLS-1$
                            for (Iterator it = dss.scripts.iterator(); it.hasNext();)
                            {
                                log.debug("    - " + it.next()); //$NON-NLS-1$
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * How long do we wait before we timeout script sessions?
     */
    private long scriptSessionTimeout = DEFAULT_TIMEOUT_MILLIS;

    /**
     * What we synchronize against when we want to access either sessionMap or
     * pageSessionMap
     */
    private final Object sessionLock = new Object();

    /**
     * The map of all the known sessions
     */
    private Map sessionMap = new HashMap();

    /**
     * The map of pages that have sessions
     */
    private Map pageSessionMap = new HashMap();

    /**
     * The log stream
     */
    private static final Logger log = Logger.getLogger(DefaultScriptSessionManager.class);
}
