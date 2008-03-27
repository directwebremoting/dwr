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

import org.directwebremoting.extend.ServerLoadMonitor;
import org.directwebremoting.extend.WaitController;
import org.directwebremoting.util.HitMonitor;

/**
 * A smart implementation of ServerLoadMonitor, customized for Jetty.
 * The ThreadDroppingServerLoadMonitor attempts to keep the hit rate down by increasing
 * the disconnected time as usage increases.
 * @author Joe Walker [joe at getahead dot org]
 */
public class ThreadDroppingServerLoadMonitor extends AbstractServerLoadMonitor implements ServerLoadMonitor
{
    /* (non-Javadoc)
     * @see org.directwebremoting.extend.ServerLoadMonitor#supportsStreaming()
     */
    public boolean supportsStreaming()
    {
        return true;
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.extend.ServerLoadMonitor#getConnectedTime()
     */
    public long getConnectedTime()
    {
        return connectedTime;
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.ServerLoadMonitor#timeToNextPoll()
     */
    public int getDisconnectedTime()
    {
        return disconnectedTime;
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.impl.AbstractServerLoadMonitor#threadWaitStarting(org.directwebremoting.extend.WaitController)
     */
    public void threadWaitStarting(WaitController controller)
    {
        hitMonitor.recordHit();
        super.threadWaitStarting(controller);

        checkLoading();
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.impl.AbstractServerLoadMonitor#threadWaitEnding(org.directwebremoting.extend.WaitController)
     */
    public void threadWaitEnding(WaitController controller)
    {
        super.threadWaitEnding(controller);
    }

    /**
     * Check that we are setting the time to next poll correctly.
     */
    private void checkLoading()
    {
        float hitsPerSecond = (float) hitMonitor.getHitsInLastPeriod() / SECONDS_MONITORED;

        // If we're getting close to the upper bound then slow down
        float load = hitsPerSecond / maxHitsPerSecond;

        disconnectedTime = (int) (disconnectedTime * load);
        if (disconnectedTime == 0)
        {
            disconnectedTime = 1;
        }
    }

    /**
     * @param maxHitsPerSecond the maxHitsPerSecond to set
     */
    public void setMaxHitsPerSecond(int maxHitsPerSecond)
    {
        this.maxHitsPerSecond = maxHitsPerSecond;
    }

    /**
     * Static configuration data: The max number of hits per second.
     * We increase the poll time to compensate and reduce the load. If this
     * number is not at least half maxWaitingThreads then the USAGE_HIGH mode
     * will not exist and the system will sublime from USAGE_LOW to USAGE_DIGG
     */
    protected int maxHitsPerSecond = 100;

    /**
     * The time we are currently waiting before sending a browser away and
     * asking it to reconnect.
     */
    protected int connectedTime = 60000;

    /**
     * How long are we telling users to wait before they come back next
     */
    protected int disconnectedTime = 1;

    /**
     * We are recording the number of hits in the last 5 seconds.
     * Maybe we should think about making this configurable.
     */
    protected static final int SECONDS_MONITORED = 10;

    /**
     * Our record of the server loading
     */
    protected HitMonitor hitMonitor = new HitMonitor(SECONDS_MONITORED);
}
