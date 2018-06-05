package org.directwebremoting.impl;

import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.http.HttpSession;

import org.directwebremoting.ScriptSession;
import org.directwebremoting.event.ScriptSessionListener;
import org.directwebremoting.extend.ConverterManager;
import org.directwebremoting.extend.PageNormalizer;
import org.directwebremoting.extend.RealScriptSession;
import org.directwebremoting.extend.ScriptSessionManager;

/**
 * A default implementation of ScriptSessionManager.
 * <p>There are synchronization constraints on this class that could be broken
 * by subclasses. Specifically anyone accessing either <code>sessionMap</code>
 * or <code>pageSessionMap</code> must be holding the <code>sessionLock</code>.
 * <p>In addition you should note that {@link DefaultScriptSession} and
 * {@link TransientScriptSessionManager} make calls to each other and you should
 * take care not to break any constraints in inheriting from these classes.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class TransientScriptSessionManager implements ScriptSessionManager
{
    /* (non-Javadoc)
     * @see org.directwebremoting.ScriptSessionManager#getOrCreateScriptSession(java.lang.String, java.lang.String, javax.servlet.http.HttpSession)
     */
    public RealScriptSession getOrCreateScriptSession(String id, String page, HttpSession httpSession)
    {
        return new DefaultScriptSession(id, this, page, converterManager, jsonOutput);
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.extend.ScriptSessionManager#getScriptSessionById(java.lang.String)
     */
    public ScriptSession getScriptSessionById(String scriptSessionId)
    {
        return null;
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.extend.ScriptSessionManager#getScriptSessionsByHttpSessionId(java.lang.String)
     */
    public Collection<ScriptSession> getScriptSessionsByHttpSessionId(String httpSessionId)
    {
        return new ArrayList<ScriptSession>();
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.ScriptSessionManager#getAllScriptSessions()
     */
    public Collection<ScriptSession> getAllScriptSessions()
    {
        return new ArrayList<ScriptSession>();
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.extend.ScriptSessionManager#addScriptSessionListener(org.directwebremoting.event.ScriptSessionListener)
     */
    public void addScriptSessionListener(ScriptSessionListener li)
    {
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.extend.ScriptSessionManager#removeScriptSessionListener(org.directwebremoting.event.ScriptSessionListener)
     */
    public void removeScriptSessionListener(ScriptSessionListener li)
    {
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.extend.ScriptSessionManager#getInitCode()
     */
    public String getInitCode()
    {
        return "dwr.engine._ordered = false;";
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
}
