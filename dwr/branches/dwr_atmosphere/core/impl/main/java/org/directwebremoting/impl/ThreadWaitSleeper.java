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
package org.directwebremoting.impl;

import java.util.concurrent.CountDownLatch;

import org.directwebremoting.extend.Sleeper;

/**
 * The simplest type of Sleeper that just uses {@link #wait()} and
 * {@link #notify()} to halt a Thread and restart it.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class ThreadWaitSleeper implements Sleeper
{
    /* (non-Javadoc)
     * @see org.directwebremoting.extend.Sleeper#goToSleep(java.lang.Runnable)
     */
    public void goToSleep(Runnable onAwakening)
    {
        try
        {
            latch.await();
        }
        catch (InterruptedException ex)
        {
            // We could pass the exception up the tree, but different sleepers
            // do very different things, when going to sleep (including
            // returning immediately and throwing a continuation exception)
            // So propagating the exception just confuses and already confusing
            // situation, without achieving anything.
            Thread.currentThread().interrupt();
        }
        finally
        {
            onAwakening.run();
        }
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.extend.Sleeper#wakeUp()
     */
    public void wakeUp()
    {
        latch.countDown();
    }

    /**
     * Ensure that once woken up we don't sleep
     */
    private final CountDownLatch latch = new CountDownLatch(1);
}
