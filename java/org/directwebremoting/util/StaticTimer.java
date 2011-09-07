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
package org.directwebremoting.util;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * A {@link Timer} can manage many {@link TimerTask}s, but each {@link Timer}
 * requires a new thread to operate, so it makes more sense to share a Timer
 * amongst man {@link TimerTask}s.
 * 
 * <p>There are a number of things that this class does not do that it perhaps
 * should, for example, to count the number of outstanding tasks, and to drop
 * the thread when there are none, however in a server using reverse ajax it is
 * likely that there will be another one soon, and a lightly loaded server is
 * not in need of careful thread management.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class StaticTimer
{
    /**
     * Schedules the specified task for execution after the specified delay.
     * @see Timer#schedule(TimerTask, long)
     * @param task task to be scheduled.
     * @param delay delay in milliseconds before task is to be executed.
     */
    public static void schedule(TimerTask task, long delay)
    {
        getTimer().schedule(task, delay);
    }

    /**
     * Schedules the specified task for execution at the specified time.  If
     * the time is in the past, the task is scheduled for immediate execution.
     * @see Timer#schedule(TimerTask, Date)
     * @param task task to be scheduled.
     * @param time time at which task is to be executed.
     */
    public static void schedule(TimerTask task, Date time)
    {
        getTimer().schedule(task, time);
    }

    /**
     * Schedules the specified task for repeated <i>fixed-delay execution</i>,
     * beginning after the specified delay.  Subsequent executions take place
     * at approximately regular intervals separated by the specified period.
     * @see Timer#schedule(TimerTask, long, long)
     * @param task task to be scheduled.
     * @param delay delay in milliseconds before task is to be executed.
     * @param period time in milliseconds between successive task executions.
     */
    public static void schedule(TimerTask task, long delay, long period)
    {
        getTimer().schedule(task, delay, period);
    }

    /**
     * Schedules the specified task for repeated <i>fixed-delay execution</i>,
     * beginning at the specified time. Subsequent executions take place at
     * approximately regular intervals, separated by the specified period.
     * @see Timer#schedule(TimerTask, Date, long)
     * @param task task to be scheduled.
     * @param firstTime First time at which task is to be executed.
     * @param period time in milliseconds between successive task executions.
     */
    public static void schedule(TimerTask task, Date firstTime, long period)
    {
        getTimer().schedule(task, firstTime, period);
    }

    /**
     * Schedules the specified task for repeated <i>fixed-rate execution</i>,
     * beginning after the specified delay.  Subsequent executions take place
     * at approximately regular intervals, separated by the specified period.
     * @see Timer#scheduleAtFixedRate(TimerTask, Date, long)
     * @param task task to be scheduled.
     * @param delay delay in milliseconds before task is to be executed.
     * @param period time in milliseconds between successive task executions.
     */
    public static void scheduleAtFixedRate(TimerTask task, long delay, long period)
    {
        getTimer().scheduleAtFixedRate(task, delay, period);
    }

    /**
     * Schedules the specified task for repeated <i>fixed-rate execution</i>,
     * beginning at the specified time. Subsequent executions take place at
     * approximately regular intervals, separated by the specified period.
     * @see Timer#scheduleAtFixedRate(TimerTask, Date, long)
     * @param task task to be scheduled.
     * @param firstTime First time at which task is to be executed.
     * @param period time in milliseconds between successive task executions.
     */
    public static void scheduleAtFixedRate(TimerTask task, Date firstTime, long period)
    {
        getTimer().scheduleAtFixedRate(task, firstTime, period);
    }

    /**
     * Create a new {@link Timer} if one doesn't already exist.
     * @return the static Timer
     */
    private static Timer getTimer()
    {
        synchronized (timerLock)
        {
            if (timer == null)
            {
                timer = new Timer();
            }
        }

        return timer;
    }

    /**
     * To synchronize access to the creation of timer
     */
    private static Object timerLock = new Object();

    /**
     * The shared Timer
     */
    protected static Timer timer;
}
