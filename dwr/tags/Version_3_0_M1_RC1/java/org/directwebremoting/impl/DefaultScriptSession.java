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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;
import org.directwebremoting.ScriptBuffer;
import org.directwebremoting.extend.MarshallException;
import org.directwebremoting.extend.RealScriptSession;
import org.directwebremoting.extend.ScriptConduit;

/**
 * An implementation of ScriptSession and RealScriptSession.
 * <p>There are synchronization constraints on this class. See the field
 * comments of the type: <code>GuardedBy("lock")</code>.
 * <p>In addition you should note that {@link DefaultScriptSession} and
 * {@link DefaultScriptSessionManager} make calls to each other and you should
 * take care not to break any constraints in inheriting from these classes.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class DefaultScriptSession implements RealScriptSession
{
    /**
     * Simple constructor
     * @param id The new unique identifier for this session
     * @param manager The manager that created us
     * @param page The URL of the page on which we sit
     */
    protected DefaultScriptSession(String id, DefaultScriptSessionManager manager, String page)
    {
        this.id = id;
        if (id == null)
        {
            throw new IllegalArgumentException("id can not be null");
        }

        this.page = page;
        this.manager = manager;
        this.creationTime = System.currentTimeMillis();
        this.lastAccessedTime = creationTime;
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.ScriptSession#getAttribute(java.lang.String)
     */
    public Object getAttribute(String name)
    {
        checkNotInvalidated();
        synchronized (attributes)
        {
            return attributes.get(name);
        }
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.ScriptSession#setAttribute(java.lang.String, java.lang.Object)
     */
    public void setAttribute(String name, Object value)
    {
        checkNotInvalidated();
        synchronized (attributes)
        {
            attributes.put(name, value);
        }
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.ScriptSession#removeAttribute(java.lang.String)
     */
    public void removeAttribute(String name)
    {
        checkNotInvalidated();
        synchronized (attributes)
        {
            attributes.remove(name);
        }
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.ScriptSession#getAttributeNames()
     */
    public Iterator<String> getAttributeNames()
    {
        checkNotInvalidated();
        synchronized (attributes)
        {
            Set<String> keys = Collections.unmodifiableSet(attributes.keySet());
            return keys.iterator();
        }
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.ScriptSession#invalidate()
     */
    public void invalidate()
    {
        synchronized (invalidLock)
        {
            invalidated = true;
            manager.invalidate(this);
        }
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.ScriptSession#isInvalidated()
     */
    public boolean isInvalidated()
    {
        synchronized (invalidLock)
        {
            return invalidated;
        }
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.ScriptSession#getId()
     */
    public String getId()
    {
        return id;
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.ScriptSession#getCreationTime()
     */
    public long getCreationTime()
    {
        checkNotInvalidated();
        return creationTime;
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.ScriptSession#getLastAccessedTime()
     */
    public long getLastAccessedTime()
    {
        synchronized (invalidLock)
        {
            // For many accesses here we check to see if we should invalidate
            // ourselves, but getLastAccessedTime() is used as part of the process
            // that DefaultScriptSessionManager goes through in order to check
            // everything for validity. So if we do this check here then DSSM will
            // give a ConcurrentModificationException if anything does timeout
            // checkNotInvalidated();
            return lastAccessedTime;
        }
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.ScriptSession#addScript(java.lang.String)
     */
    public void addScript(ScriptBuffer script)
    {
        checkNotInvalidated();

        if (script == null)
        {
            throw new NullPointerException("null script");
        }

        // First we try to add the script to an existing conduit
        synchronized (scriptLock)
        {
            if (conduits.isEmpty())
            {
                // There are no conduits, just store it until there are
                scripts.add(script);
            }
            else
            {
                // Try all the conduits, starting with the first
                boolean written = false;
                for (Iterator<ScriptConduit> it = conduits.iterator(); !written && it.hasNext();)
                {
                    ScriptConduit conduit = it.next();
                    try
                    {
                        written = conduit.addScript(script);
                    }
                    catch (Exception ex)
                    {
                        it.remove();
                        log.debug("Failed to write to ScriptConduit, removing from list: " + conduit);
                    }
                }

                if (!written)
                {
                    scripts.add(script);
                }
            }
        }
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.extend.RealScriptSession#addScriptConduit(org.directwebremoting.extend.ScriptConduit)
     */
    public void addScriptConduit(ScriptConduit conduit) throws IOException
    {
        checkNotInvalidated();

        synchronized (scriptLock)
        {
            writeScripts(conduit);
            conduits.add(conduit);
        }
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.extend.RealScriptSession#writeScripts(org.directwebremoting.extend.ScriptConduit)
     */
    public void writeScripts(ScriptConduit conduit) throws IOException
    {
        checkNotInvalidated();

        synchronized (scriptLock)
        {
            for (Iterator<ScriptBuffer> it = scripts.iterator(); it.hasNext();)
            {
                ScriptBuffer script = it.next();

                try
                {
                    if (conduit.addScript(script))
                    {
                        it.remove();
                    }
                    else
                    {
                        // If we didn't write this one, don't bother with any more
                        break;
                    }
                }
                catch (MarshallException ex)
                {
                    log.warn("Failed to convert data. Dropping Javascript: " + script, ex);
                    it.remove();
                }
            }
        }
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.extend.RealScriptSession#removeScriptConduit(org.directwebremoting.extend.ScriptConduit)
     */
    public void removeScriptConduit(ScriptConduit conduit)
    {
        checkNotInvalidated();

        synchronized (scriptLock)
        {
            boolean removed = conduits.remove(conduit);
            if (!removed)
            {
                log.debug("Removing unattached ScriptConduit: " + conduit);
                debug();
            }
        }
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.extend.RealScriptSession#getScriptLock()
     */
    @Deprecated
    public Object getScriptLock()
    {
        return scriptLock;
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.extend.RealScriptSession#hasWaitingScripts()
     */
    public boolean hasWaitingScripts()
    {
        synchronized (scriptLock)
        {
            return !scripts.isEmpty();
        }
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.ScriptSession#getPage()
     */
    public String getPage()
    {
        return page;
    }

    /**
     * Called whenever a browser accesses this data using DWR
     */
    protected void updateLastAccessedTime()
    {
        // It's a bad idea to call native methods with locks held
        long temp = System.currentTimeMillis();

        synchronized (invalidLock)
        {
            lastAccessedTime = temp;
        }
    }

    /**
     * Check that we are still valid and throw an IllegalStateException if not.
     * At the same time set the lastAccessedTime flag.
     * @throws IllegalStateException If this object has become invalid
     */
    protected void checkNotInvalidated()
    {
        // It's a bad idea to call native methods with locks held
        long now = System.currentTimeMillis();

        synchronized (invalidLock)
        {
            long age = now - lastAccessedTime;
            if (age > manager.getScriptSessionTimeout())
            {
                invalidate();
            }
        }
    }

    /**
     * Some debug output
     */
    private void debug()
    {
        if (log.isDebugEnabled())
        {
            log.debug("Known ScriptConduits:");
            for (ScriptConduit scriptConduit : conduits)
            {
                log.debug("- " + scriptConduit);
            }
        }
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode()
    {
        return 572 + id.hashCode();
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj)
    {
        if (obj == null)
        {
            return false;
        }

        if (obj == this)
        {
            return true;
        }

        if (!this.getClass().equals(obj.getClass()))
        {
            return false;
        }

        DefaultScriptSession that = (DefaultScriptSession) obj;

        return this.id.equals(that.id);

    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        return "DefaultScriptSession[id=" + id + "]";
    }

    /**
     * The server side attributes for this page.
     * <p>GuardedBy("attributes")
     */
    protected final Map<String, Object> attributes = Collections.synchronizedMap(new HashMap<String, Object>());

    /**
     * When the the web page that we represent last contact us using DWR?
     * <p>GuardedBy("invalidLock")
     */
    private long lastAccessedTime = 0L;

    /**
     * Have we been made invalid?
     * <p>GuardedBy("invalidLock")
     */
    private boolean invalidated = false;

    /**
     * The object that we use to synchronize against when we want to work with
     * the invalidation state of this object
     */
    private final Object invalidLock = new Object();

    /**
     * The script conduits that we can use to transfer data to the browser.
     * <p>GuardedBy("scriptLock")
     */
    protected final SortedSet<ScriptConduit> conduits = new TreeSet<ScriptConduit>();

    /**
     * The list of waiting scripts.
     * <p>GuardedBy("scriptLock")
     */
    protected final List<ScriptBuffer> scripts = new ArrayList<ScriptBuffer>();

    /**
     * The object that we use to synchronize against when we want to alter
     * the path of scripts (to conduits or the scripts list)
     */
    private final Object scriptLock = new Object();

    /**
     * What is our page session id?
     * <p>This should not need careful synchronization since it is unchanging
     */
    protected final String id;

    /**
     * When we we created?
     * <p>This should not need careful synchronization since it is unchanging
     */
    protected final long creationTime;

    /**
     * The page being viewed.
     */
    protected final String page;

    /**
     * The session manager that collects sessions together
     * <p>This should not need careful synchronization since it is unchanging
     */
    protected final DefaultScriptSessionManager manager;

    /**
     * The log stream
     */
    private static final Log log = LogFactory.getLog(DefaultScriptSession.class);
}
