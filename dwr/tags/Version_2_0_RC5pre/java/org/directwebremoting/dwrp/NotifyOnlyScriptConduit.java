package org.directwebremoting.dwrp;

import org.directwebremoting.ScriptBuffer;
import org.directwebremoting.extend.ScriptConduit;
import org.directwebremoting.util.Logger;

/**
 * Implementation of ScriptConduit that simply calls <code>notifyAll()</code>
 * if a script is added.
 * No actual script adding is done here.
 * Useful in conjunction with a streamWait()
 */
public class NotifyOnlyScriptConduit extends ScriptConduit
{
    /**
     * @param lock Object to wait and notifyAll with
     */
    public NotifyOnlyScriptConduit(Object lock)
    {
        super(RANK_PROCEDURAL);
        this.lock = lock;
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.ScriptConduit#addScript(org.directwebremoting.ScriptBuffer)
     */
    public boolean addScript(ScriptBuffer script)
    {
        try
        {
            synchronized (lock)
            {
                lock.notifyAll();
            }
        }
        catch (Exception ex)
        {
            log.warn("Failed to notify all ScriptSession users", ex);
        }

        // We have not done anything with the script, so
        return false;
    }

    /**
     * The object to notify lock holders on
     */
    private final Object lock;

    /**
     * The log stream
     */
    private static final Logger log = Logger.getLogger(NotifyOnlyScriptConduit.class);
}