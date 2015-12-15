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
import org.directwebremoting.util.HitMonitor;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author matt conroy
 */
public class ThrottlingServerLoadMonitorTest extends ThrottlingServerLoadMonitor implements WaitController
{
    private static ThrottlingServerLoadMonitor t;

    @BeforeClass
    public static void start() {
        t = new ThrottlingServerLoadMonitorTest();
    }

    @Test
    public void loadTest() {
        checkMode(ThrottlingServerLoadMonitor.USAGE_LOW);

        // Simulate load.
        for (int i = 0; i < 10000; i++)
        {
            t.threadWaitStarting(this);

            // For the sake of testing we need to run the load balancer every
            // time through the loop.
            t.lastLoadAdjust.set(System.currentTimeMillis() - t.secondsMonitored * 1000);
        }

        checkMode(ThrottlingServerLoadMonitor.USAGE_DIGG);

        for (int i = 0; i < 10000; i++)
        {
            t.threadWaitEnding(this);
        }

        // Simulate no hits
        hitMonitor = new HitMonitor(t.secondsMonitored);
        t.lastLoadAdjust.set(System.currentTimeMillis() - t.secondsMonitored);
        t.threadWaitStarting(this);
        checkMode(ThrottlingServerLoadMonitor.USAGE_LOW);

        // TODO More testing can be done with additional timing calculations.
    }

    private void checkMode(int m) {
        Assert.assertEquals(m, t.getMode());
    }

    @AfterClass
    public static void stop() {
        t.destroy();
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.extend.WaitController#shutdown()
     */
    public void shutdown()
    {
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.extend.WaitController#isShutdown()
     */
    public boolean isShutdown()
    {
        return false;
    }
}

