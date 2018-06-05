package org.directwebremoting.impl;

import org.directwebremoting.extend.Alarm;
import org.directwebremoting.extend.ServerLoadMonitor;
import org.directwebremoting.extend.Sleeper;
import org.directwebremoting.extend.WaitController;

/**
 * An Alarm that allows the system to close all connections when it is shutting
 * down.
 * <p><b>WARNING</b>: This code has a non-obvious side effect - The server load
 * monitor (which hands out shutdown messages) also monitors usage by looking
 * at the number of connected alarms.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class ShutdownAlarm implements Alarm
{
    /**
     * Register ourselves with the ServerLoadMonitor so we can raise an
     * Alarm if we get shutdown
     * @param serverLoadMonitor
     */
    public ShutdownAlarm(Sleeper sleeper, ServerLoadMonitor serverLoadMonitor)
    {
        this.sleeper = sleeper;
        this.serverLoadMonitor = serverLoadMonitor;
        this.serverLoadMonitor.threadWaitStarting(waitController);
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.extend.Alarm#cancel()
     */
    public void cancel()
    {
        serverLoadMonitor.threadWaitEnding(waitController);
    }

    /**
     * The listener to allow the ServerLoadMonitor to shut us down
     */
    private final WaitController waitController = new WaitController()
    {
        /* (non-Javadoc)
         * @see org.directwebremoting.extend.WaitController#shutdown()
         */
        public void shutdown()
        {
            sleeper.wakeUpToClose();
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
         * We need to be able to say if we have been shutdown
         */
        boolean shutdown = false;
    };

    /**
     * The thread that needs to know about shutdown
     */
    private final Sleeper sleeper;

    /**
     * The source of shutdown messages
     */
    protected final ServerLoadMonitor serverLoadMonitor;
}
