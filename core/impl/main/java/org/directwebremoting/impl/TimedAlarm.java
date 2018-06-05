package org.directwebremoting.impl;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.directwebremoting.extend.Alarm;
import org.directwebremoting.extend.Sleeper;

/**
 * An Alarm that goes off after a certain length of time.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class TimedAlarm implements Alarm
{
    /**
     * @param waitTime How long we wait before the Alarm goes off
     */
    public TimedAlarm(final Sleeper sleeper, long waitTime, ScheduledThreadPoolExecutor executor)
    {
        if (waitTime == 0)
        {
            sleeper.wakeUpToClose();
        }
        else
        {
            Runnable runnable = new Runnable()
            {
                public void run()
                {
                    sleeper.wakeUpToClose();
                }
            };

            future = executor.schedule(runnable, waitTime, TimeUnit.MILLISECONDS);
        }
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.extend.Alarm#cancel()
     */
    public void cancel()
    {
        if (future != null)
        {
            future.cancel(true);
        }
    }

    /**
     * The future result that allows us to cancel the timer
     */
    private ScheduledFuture<?> future;
}
