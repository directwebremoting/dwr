package org.directwebremoting.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.directwebremoting.ScriptSession;
import org.directwebremoting.ScriptSessionFilter;
import org.directwebremoting.extend.ScriptSessionManager;
import org.directwebremoting.extend.TaskDispatcher;

/**
 * Default single node implementation of TaskDispatcher
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class DefaultTaskDispatcher implements TaskDispatcher
{
    /* (non-Javadoc)
     * @see org.directwebremoting.extend.TaskDispatcher#dispatchTask(org.directwebremoting.ScriptSessionFilter, java.lang.Runnable)
     */
    public void dispatchTask(ScriptSessionFilter filter, Runnable task)
    {
        Collection<ScriptSession> all = scriptSessionManager.getAllScriptSessions();
        Collection<ScriptSession> use = new ArrayList<ScriptSession>();

        for (ScriptSession session : all)
        {
            if (filter.match(session))
            {
                use.add(session);
            }
        }

        if (use.size() > 0)
        {
            if (log.isDebugEnabled()) {
        	    log.debug("Execution time: " + new Date().toString() + " - Executing task (" + task.getClass().getSimpleName() + ") against " + use.size() + " sessions.");
            }
            target.set(use);
            task.run();
            target.remove();
        }
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.extend.TaskDispatcher#getTargetSessions()
     */
    public Collection<ScriptSession> getTargetSessions()
    {
        return target.get();
    }

    /**
     * Connection to the DwrServlet that we are attached to
     */
    public void setScriptSessionManager(ScriptSessionManager scriptSessionManager)
    {
        this.scriptSessionManager = scriptSessionManager;
    }

    /**
     * @see #setScriptSessionManager(ScriptSessionManager)
     */
    private ScriptSessionManager scriptSessionManager;

    /**
     * ThreadLocal in which the list of sessions are stored.
     */
    private final ThreadLocal<Collection<ScriptSession>> target = new ThreadLocal<Collection<ScriptSession>>();

    /**
     * The log stream
     */
    private static final Log log = LogFactory.getLog(DefaultTaskDispatcher.class);
}
