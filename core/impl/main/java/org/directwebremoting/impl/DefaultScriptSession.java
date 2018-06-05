package org.directwebremoting.impl;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.directwebremoting.ScriptBuffer;
import org.directwebremoting.event.ScriptSessionBindingEvent;
import org.directwebremoting.event.ScriptSessionBindingListener;
import org.directwebremoting.extend.ConverterManager;
import org.directwebremoting.extend.RealScriptSession;
import org.directwebremoting.extend.ScriptBufferUtil;
import org.directwebremoting.extend.ScriptSessionManager;
import org.directwebremoting.extend.Sleeper;

/**
 * An implementation of ScriptSession and RealScriptSession.
 * <p>You should note that {@link DefaultScriptSession} and
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
    protected DefaultScriptSession(String id, ScriptSessionManager manager, String page, ConverterManager converterManager, boolean jsonOutput)
    {
        this.id = id;
        if (id == null)
        {
            throw new IllegalArgumentException("id can not be null");
        }
        this.manager = manager;
        this.page = page;
        this.converterManager = converterManager;
        this.jsonOutput = jsonOutput;

        this.creationTime = System.currentTimeMillis();
        this.lastAccessedTime = creationTime;
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.ScriptSession#getAttribute(java.lang.String)
     */
    public Object getAttribute(String name)
    {
        invalidateIfNeeded();
        return attributes.get(name);
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.ScriptSession#setAttribute(java.lang.String, java.lang.Object)
     */
    public void setAttribute(String name, Object value)
    {
        invalidateIfNeeded();
        if (value != null)
        {
            if (value instanceof ScriptSessionBindingListener)
            {
                ScriptSessionBindingListener listener = (ScriptSessionBindingListener) value;
                listener.valueBound(new ScriptSessionBindingEvent(this, name));
            }
            attributes.put(name, value);
        }
        else
        {
            removeAttribute(name);
        }
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.ScriptSession#removeAttribute(java.lang.String)
     */
    public void removeAttribute(String name)
    {
        invalidateIfNeeded();
        Object value = attributes.remove(name);
        if (value != null && value instanceof ScriptSessionBindingListener)
        {
            ScriptSessionBindingListener listener = (ScriptSessionBindingListener) value;
            listener.valueUnbound(new ScriptSessionBindingEvent(this, name));
        }
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.ScriptSession#getAttributeNames()
     */
    public Iterator<String> getAttributeNames()
    {
        invalidateIfNeeded();
        Set<String> keys = Collections.unmodifiableSet(new HashSet<String>(attributes.keySet()));
        return keys.iterator();
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.ScriptSession#invalidate()
     */
    public void invalidate()
    {
        invalidated = true;

        // attributes is a concurrent map and can be safely iterated.
        for (Map.Entry<String, Object> entry : attributes.entrySet())
        {
            Object value = entry.getValue();
            if (value instanceof ScriptSessionBindingListener)
            {
                ScriptSessionBindingListener listener = (ScriptSessionBindingListener) value;
                listener.valueUnbound(new ScriptSessionBindingEvent(this, entry.getKey()));
            }
        }
        if (manager instanceof DefaultScriptSessionManager)
        {
            ((DefaultScriptSessionManager) manager).invalidate(this);
        }
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
        invalidateIfNeeded();
        return creationTime;
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.ScriptSession#getLastAccessedTime()
     */
    public long getLastAccessedTime()
    {
        // For many accesses here we check to see if we should invalidate
        // ourselves, but getLastAccessedTime() is used as part of the process
        // that DefaultScriptSessionManager goes through in order to check
        // everything for validity. So if we do this check here then DSSM will
        // give a ConcurrentModificationException if anything does timeout
        // checkNotInvalidated();
        return lastAccessedTime;
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.ScriptSession#addScript(org.directwebremoting.ScriptBuffer)
     */
    public void addScript(ScriptBuffer script)
    {
        invalidateIfNeeded();

        if (script == null)
        {
            throw new NullPointerException("null script");
        }
        synchronized (this.scripts)
        {
            scripts.add(ScriptBufferUtil.createOutput(script, converterManager, jsonOutput));
        }
        synchronized (sleeperLock)
        {
            if (sleeper != null) {
                sleeper.wakeUpForData();
            }
        }
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.ScriptSession#addRunnable(org.directwebremoting.ScriptRunnable, org.directwebremoting.ScriptPhase)
     */
    public void addRunnable(final Runnable runnable)
    {
        invalidateIfNeeded();

        if (runnable == null)
        {
            throw new NullPointerException("null runnable");
        }
        synchronized (this.scripts)
        {
            scripts.add(runnable);
        }
        synchronized (sleeperLock)
        {
            if (sleeper != null) {
                sleeper.wakeUpForData();
            }
        }
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.extend.RealScriptSession#setSleeper(org.directwebremoting.extend.Sleeper)
     */
    public void setSleeper(Sleeper sleeper)
    {
        invalidateIfNeeded();
        synchronized (sleeperLock)
        {
            this.sleeper = sleeper;
        }
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.extend.RealScriptSession#clearSleeper()
     */
    @SuppressWarnings("hiding")
    public void clearSleeper(Sleeper sleeper)
    {
        invalidateIfNeeded();
        synchronized (sleeperLock)
        {
            if (this.sleeper == sleeper)
            {
                this.sleeper = null;
            }
        }
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.extend.RealScriptSession#getScript(long)
     */
    public Script getScript(long scriptIndex)
    {
        synchronized (scripts)
        {
            if (scriptsOffset < 0)
            {
                throw new IllegalStateException("Confirmed script index must be set before accessing scripts.");
            }

            int listIndex = (int) (scriptIndex - scriptsOffset);
            if (listIndex < 0)
            {
                listIndex = 0;
            }
            if (listIndex < scripts.size()) {
                final Object script = scripts.get(listIndex);
                final long resultingScriptIndex = scriptsOffset + listIndex;
                return new Script()
                {
                    public long getIndex()
                    {
                        return resultingScriptIndex;
                    }
                    public Object getScript()
                    {
                        return script;
                    }
                };
            } else {
                return null;
            }
        }
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.extend.RealScriptSession#confirmTransfer(long)
     */
    public void confirmScripts(long confirmedScriptIndex)
    {
        long nextScriptIndex = confirmedScriptIndex + 1;
        synchronized (scripts)
        {
            // If this is a re-born script session with uninitialized script offset, or
            // client's offset is outside our range for other reasons, then just
            // fast-forward ourselves to the client's offset so it will continue
            // receiving an unbroken series of scripts
            if (scriptsOffset < 0 || nextScriptIndex > scriptsOffset + scripts.size())
            {
                scriptsOffset = nextScriptIndex;
            }

            // Purge confirmed
            long remove = nextScriptIndex - scriptsOffset; // if we are on offset 10 and 10 is confirmed, we should remove 1
            if (remove > 0) {
                for(long i=0; i<remove; i++)
                {
                    scripts.removeFirst();
                }
                scriptsOffset = nextScriptIndex;
            }
        }
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.ScriptSession#setPage(String)
     */
    public void setPage(String page)
    {
        this.page = page;
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.ScriptSession#getPage()
     */
    public String getPage()
    {
        return page;
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.extend.RealScriptSession#setHttpSessionId(java.lang.String)
     */
    public synchronized void setHttpSessionId(String httpSessionId)
    {
        this.httpSessionId = httpSessionId;
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.ScriptSession#getHttpSessionId()
     */
    public synchronized String getHttpSessionId()
    {
        return httpSessionId;
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.extend.RealScriptSession#updateLastAccessedTime()
     */
    public void updateLastAccessedTime()
    {
        lastAccessedTime = System.currentTimeMillis();
        connectionValidationTime = 0; // reset connection validation
    }

    /**
     * Check that we are still valid and throw an IllegalStateException if not.
     * At the same time set the lastAccessedTime flag.
     */
    protected void invalidateIfNeeded()
    {
        if (invalidated)
        {
            return;
        }
        long now = System.currentTimeMillis();
        long age = now - lastAccessedTime;
        if (age > manager.getScriptSessionTimeout())
        {
            synchronized (sleeperLock) {
                if (connectionValidationTime > 0 && now < connectionValidationTime) {
                    // We are waiting for an already started connection validation so do nothing
                    return;
                } else if (connectionValidationTime == 0 && sleeper != null) {
                    // Start a connection validation by closing the current poll
                    int disconnectedTime = sleeper.wakeUpToClose();
                    connectionValidationTime = now + disconnectedTime + connectionValidationTimeout;
                    return;
                }
            }

            // No ongoing connection validation and no connection (sleeper) so this is
            // the end of the road
            invalidate();
        }
    }

    /**
     * Set the time to wait for client to poll when asking for connection validation.
     * @param connectionValidationTimeout
     */
    public void setConnectionValidationTimeout(int connectionValidationTimeout)
    {
        this.connectionValidationTimeout = connectionValidationTimeout;
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
        return "DefaultScriptSession[id=" + getDebugName() + "]";
    }

    /**
     * A very short name for debugging purposes
     */
    protected String getDebugName()
    {
        int slashPos = id.indexOf('/');
        return id.substring(0, 4) + (slashPos >= 0 ? id.substring(slashPos, slashPos+5) : "");
    }

    /**
     * How we convert parameters
     */
    protected ConverterManager converterManager = null;

    /**
     * Are we outputting in JSON mode?
     */
    protected boolean jsonOutput = false;

    /**
     * The server side attributes for this page.
     * <p>GuardedBy("attributes")
     */
    protected final Map<String, Object> attributes = new ConcurrentHashMap<String, Object>();

    /**
     * When the the web page that we represent last contact us using DWR?
     */
    private volatile long lastAccessedTime = 0L;

    /**
     * When > 0 we are waiting for the client until the set time to validate the connection by polling again
     */
    private volatile long connectionValidationTime = 0;

    /**
     * How long to wait for a client to poll when asking for connection validation
     */
    private int connectionValidationTimeout = 10000;

    /**
     * Have we been made invalid?
     */
    private volatile boolean invalidated = false;

    /**
     * The sleeper that waits for new data.
     */
    protected Sleeper sleeper = null;

    /**
     * The lock for synchronizing sleepers access.
     */
    protected Object sleeperLock = new Object();

    /**
     * The script index of the first item in the scripts collection.
     */
    protected long scriptsOffset = 0;

    /**
     * The list of waiting scripts (String or Runnable).
     * <p>GuardedBy("self") for iteration and compound actions.
     */
    protected final LinkedList<Object> scripts = new LinkedList<Object>();

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
    protected String page;

    /**
     * The corresponding HttpSession id (if any)
     */
    protected String httpSessionId;

    /**
     * The session manager that collects sessions together
     * <p>This should not need careful synchronization since it is unchanging
     */
    protected final ScriptSessionManager manager;
}
