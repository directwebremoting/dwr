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
import java.util.SortedSet;
import java.util.TreeSet;

import org.directwebremoting.ScriptBuffer;
import org.directwebremoting.extend.MarshallException;
import org.directwebremoting.extend.RealScriptSession;
import org.directwebremoting.extend.ScriptConduit;
import org.directwebremoting.util.Logger;

/**
 * An implementation of ScriptSession and RealScriptSession.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class DefaultScriptSession implements RealScriptSession
{
    /**
     * Simple constructor
     * @param id The new unique identifier for this session
     * @param manager The manager that created us
     */
    protected DefaultScriptSession(String id, DefaultScriptSessionManager manager)
    {
        this.id = id;
        if (id == null)
        {
            throw new IllegalArgumentException("id can not be null");
        }

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
        return attributes.get(name);
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.ScriptSession#setAttribute(java.lang.String, java.lang.Object)
     */
    public void setAttribute(String name, Object value)
    {
        checkNotInvalidated();
        attributes.put(name, value);
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.ScriptSession#removeAttribute(java.lang.String)
     */
    public void removeAttribute(String name)
    {
        checkNotInvalidated();
        attributes.remove(name);
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.ScriptSession#getAttributeNames()
     */
    public Iterator getAttributeNames()
    {
        checkNotInvalidated();
        return attributes.keySet().iterator();
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.ScriptSession#invalidate()
     */
    public void invalidate()
    {
        invalidated = true;
        manager.invalidate(this);
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.ScriptSession#isInvalidated()
     */
    public boolean isInvalidated()
    {
        return invalidated;
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
        checkNotInvalidated();
        return lastAccessedTime;
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
            if (conduits.size() == 0)
            {
                // There are no conduits, just store it until there are
                scripts.add(script);
            }
            else
            {
                // Try all the conduits, starting with the first
                boolean written = false;
                for (Iterator it = conduits.iterator(); !written && it.hasNext();)
                {
                    ScriptConduit conduit = (ScriptConduit) it.next();
                    try
                    {
                        written = conduit.addScript(script);

                        if (written)
                        {
                            conduit.flush();
                        }
                    }
                    catch (Exception ex)
                    {
                        log.warn("Failed to write to ScriptConduit, removing from list: " + conduit);
                        it.remove();
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

        // And add the conduit into the list
        synchronized (scriptLock)
        {
            conduits.add(conduit);

            // If there are any outstanding scripts, dump them to the new conduit
            boolean output = false;

            for (Iterator it = scripts.iterator(); it.hasNext();)
            {
                ScriptBuffer script = (ScriptBuffer) it.next();

                try
                {
                    if (conduit.addScript(script))
                    {
                        output = true;
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
                }
            }

            if (output)
            {
                conduit.flush();
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
                log.warn("Removing unattached ScriptConduit: " + conduit);
                debug();
            }
        }
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.extend.RealScriptSession#flushConduits()
     */
    public void flushConduits()
    {
        checkNotInvalidated();

        // First we try to add the script to an existing conduit
        synchronized (scriptLock)
        {
            if (conduits.size() > 0)
            {
                for (Iterator it = conduits.iterator(); it.hasNext();)
                {
                    ScriptConduit conduit = (ScriptConduit) it.next();
                    
                    try
                    {
                        conduit.flush();
                    }
                    catch (Exception ex)
                    {
                        log.warn("Failed to flush to ScriptConduit, removing from list: " + conduit);
                        it.remove();
                    }
                }
            }
        }
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.extend.RealScriptSession#getScriptLock()
     */
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

    /**
     * Called whenever a browser accesses this data using DWR
     */
    protected void updateLastAccessedTime()
    {
        lastAccessedTime = System.currentTimeMillis();
    }

    /**
     * Check that we are still valid and throw an IllegalStateException if not.
     * At the same time set the lastAccessedTime flag.
     * @throws IllegalStateException If this object has become invalid
     */
    protected void checkNotInvalidated()
    {
        long now = System.currentTimeMillis();
        long age = now - lastAccessedTime;
        if (age > manager.getScriptSessionTimeout())
        {
            invalidate();
        }

        if (invalidated)
        {
            log.warn("ScriptSession has been invalidated.");
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
            for (Iterator it = conduits.iterator(); it.hasNext();)
            {
                ScriptConduit c = (ScriptConduit) it.next();
                log.debug("- " + c);
            }
        }
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    public int hashCode()
    {
        return 572 + id.hashCode();
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
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

        if (!this.id.equals(that.id))
        {
            return false;
        }

        return true;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    public String toString()
    {
        return "DefaultScriptSession[id=" + id + "]";
    }

    /**
     * The script conduits that we can use to transfer data to the browser
     */
    protected final SortedSet conduits = new TreeSet();

    /**
     * The list of waiting scripts
     */
    protected final List scripts = new ArrayList();

    /**
     * What is our page session id?
     */
    protected String id = null;

    /**
     * When we we created?
     */
    protected long creationTime = 0L;

    /**
     * When the the web page that we represent last contact us using DWR?
     */
    protected long lastAccessedTime = 0L;

    /**
     * Have we been made invalid?
     */
    protected boolean invalidated = false;

    /**
     * The server side attributes for this page
     */
    protected Map attributes = Collections.synchronizedMap(new HashMap());

    /**
     * The session manager that collects sessions together
     */
    protected DefaultScriptSessionManager manager;

    /**
     * The object that we use to synchronize against when we want to alter
     * the path of scripts (to conduits or the scripts list)
     */
    private final Object scriptLock = new Object();

    /**
     * The log stream
     */
    private static final Logger log = Logger.getLogger(DefaultScriptSession.class);
}
