package org.directwebremoting.dwrp;

import org.directwebremoting.extend.WaitController;
import org.directwebremoting.util.Logger;

/**
 * A {@link WaitController} that works with {@link Object#wait(long)}
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class NotifyWaitController implements WaitController
{
    /**
     * @param lock
     */
    public NotifyWaitController(Object lock)
    {
        this.lock = lock;
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.extend.WaitController#shutdown()
     */
    public void shutdown()
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

        shutdown = true;
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.extend.WaitController#isShutdown()
     */
    public boolean isShutdown()
    {
        return shutdown;
    }

    /**
     * The object that is being {@link Object#wait(long)} on so we can
     * move it on with {@link Object#notifyAll()}.
     */
    private Object lock;

    /**
     * The log stream
     */
    private static final Logger log = Logger.getLogger(NotifyWaitController.class);

    /**
     * Has {@link #shutdown()} been called on this object?
     */
    private boolean shutdown = false;
}