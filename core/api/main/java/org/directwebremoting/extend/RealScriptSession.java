package org.directwebremoting.extend;

import org.directwebremoting.ScriptSession;

/**
 * RealScriptSession is the real interface that should be implemented in place
 * of ScriptSession. It includes methods required by the guts of DWR, that are
 * not needed by normal users.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 * @author Mike Wilson
 */
public interface RealScriptSession extends ScriptSession
{
    /**
     * While a Marshaller is processing a request it can register a
     * Sleeper with the ScriptSession to say - "tell me when there is new data"
     */
    void setSleeper(Sleeper sleeper);

    /**
     * Remove Sleeper.
     */
    void clearSleeper(Sleeper sleeper);

    /**
     * Get queued script from the supplied index.
     * @param scriptIndex index
     * @return a Script instance or null if nothing in queue
     */
    Script getScript(long scriptIndex);

    /**
     * Confirms that the client has received all scripts up to and including
     * the supplied index so that script data may be purged.
     * @param scriptIndex last index that can be purged
     */
    void confirmScripts(long scriptIndex);

    /**
     * Called whenever a browser accesses this ScriptSession to ensure that the
     * session does not timeout before it should.
     */
    void updateLastAccessedTime();

    /**
     * Set a new page for the ScriptSession (in case of HTML pushState)
     */
    void setPage(String page);

    /**
     * Set a new HttpSession id for the ScriptSession
     */
    void setHttpSessionId(String httpSessionId);

    /**
     * Interface for queued script info class.
     * @author Mike Wilson
     */
    public static interface Script
    {
        long getIndex();
        Object getScript();
    }
}
