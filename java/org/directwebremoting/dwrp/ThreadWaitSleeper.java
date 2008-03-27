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
package org.directwebremoting.dwrp;

/**
 * The simplest type of Sleeper that just uses {@link #wait()} and
 * {@link #notify()} to halt a Thread and restart it.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class ThreadWaitSleeper implements Sleeper
{
    /* (non-Javadoc)
     * @see org.directwebremoting.dwrp.PollHandler.Sleeper#goToSleep()
     */
    public void goToSleep(Runnable onAwakening)
    {
        try
        {
            synchronized (lock)
            {
                lock.wait();
            }
        }
        catch (InterruptedException ex)
        {
            Thread.interrupted();
        }
        finally
        {
            onAwakening.run();
        }
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.dwrp.PollHandler.Sleeper#wakeUp()
     */
    public void wakeUp()
    {
        synchronized (lock)
        {
            lock.notifyAll();
        }
    }

    /**
     * The object to lock on
     */
    private Object lock = new Object();
}
