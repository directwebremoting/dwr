package org.directwebremoting.impl;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.directwebremoting.extend.RealScriptSession;
import org.directwebremoting.extend.ScriptConduit;

/**
 * The simplest type of Sleeper that just uses {@link #wait()} and
 * {@link #notify()} to block a thread.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 * @author Mike Wilson
 */
public class ThreadWaitSleeper extends BaseSleeper
{
    public ThreadWaitSleeper(final HttpServletResponse response, final RealScriptSession scriptSession, final ScriptConduit conduit) throws IOException
    {
        super(response, scriptSession, conduit);
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.impl.BaseSleeper#enterSleep()
     */
    @Override
    protected void enterSleep()
    {
        while(true) {
            synchronized (lock) {
                while(!awaken && !closed) {
                    try {
                        lock.wait();
                    } catch (InterruptedException ex) {
                        closed = true;
                    }
                }
                if (closed) {
                    break;
                }
                awaken = false;
            }
            doWork();
        }
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.impl.BaseSleeper#wakeUp()
     */
    @Override
    protected void wakeUp()
    {
        synchronized (lock) {
            awaken = true;
            lock.notify();
        }
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.impl.BaseSleeper#close()
     */
    @Override
    protected void close()
    {
        synchronized (lock) {
            closed = true;
            lock.notify();
        }
    }

    // Internal state
    private final Object lock = new Object();
    private boolean awaken = false;
    private boolean closed = false;
}
