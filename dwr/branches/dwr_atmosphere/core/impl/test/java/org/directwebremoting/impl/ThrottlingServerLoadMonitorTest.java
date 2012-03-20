/*
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

import org.directwebremoting.extend.WaitController;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author matt conroy
 */
public class ThrottlingServerLoadMonitorTest implements WaitController
{
    private static ThrottlingServerLoadMonitor t;

    @BeforeClass
    public static void start() {
        t = new ThrottlingServerLoadMonitor();
    }

    @Test
    public void loadTest() throws InterruptedException {
        checkMode(t.USAGE_LOW);

        // Simulate load.
        for (int i = 0; i < 1000; i++)
        {
            t.threadWaitStarting(this);
        }
        checkMode(t.USAGE_DIGG);

        // TODO: This is about as much testing as we can do since the
        // state changes are completely based on the number of hits per second.
        // The only way that I can think of at the moment to test this better
        // would be to open up additional methods of the Monitor which is
        // probably not a good idea.
    }

    // TODO: I don't like having to check multiple times for the
    // thread to run, but it is simple and does the job.
    private void checkMode(int mode) throws InterruptedException {
        // Give the thread some time to run.
        // Gotta love multi processor boxes.
        for (int i = 0; i < 3; i++)
        {
            synchronized(t.SERVER_SLEEP_LOCK) {
                t.SERVER_SLEEP_LOCK.notify();
            }
            synchronized(t.SERVER_MONITOR_LOCK) {
                t.SERVER_MONITOR_LOCK.notify();
            }

            if (t.getMode() == mode)
            {
                break;
            }

            Thread.sleep(100);
        }
        Assert.assertEquals(mode, t.getMode());
    }

    @AfterClass
    public static void stop() {
        t.contextDestroyed();
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.extend.WaitController#shutdown()
     */
    public void shutdown()
    {
        // TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see org.directwebremoting.extend.WaitController#isShutdown()
     */
    public boolean isShutdown()
    {
        // TODO Auto-generated method stub
        return false;
    }
}

