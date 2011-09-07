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
import org.directwebremoting.util.Logger;

/**
 * A smart implementation of ServerLoadMonitor.
 * 
 * <p>What a browser does:</p>
 * <pre>
 *       connected disconnected connected ...
 *      ____________          ____________
 *      |          |          |          |
 *      |          |          |          |
 * _____|          |__________|          |______
 *       [---cT---] [---dT---] [---cT---] ...
 * </pre>
 * <p>Where cT is the connectedTime and dT is the disconnectedTime.</p>
 * 
 * <p>We impose some limits: a maximum number of symultaneously connected
 * browsers <code>maxWaitingThreads</code>, and the maximum number of
 * connections per second <code>maxHitsPerSecond</code>.</p>
 * 
 * <p>We attempt to keep the actual waitingThreads and hitsPerSecond within
 * bounds by vairying connectedTime and disconnectedTime.</p>
 * 
 * <p>The system is in one of 3 modes: USAGE_LOW, USAGE_HIGH and USAGE_DIGG. The
 * boundary between USAGE_LOW and USAGE_HIGH is called threadOut. The boundary
 * between USAGE_HIGH and USAGE_DIGG is called hitOut.</p>
 * 
 * <p>The system starts in USAGE_LOW mode. This mode uses constant values of
 * connectedTime=60 secs and disconnectedTime=0 secs. We could use much bigger
 * values for connectedTime (like infinite) however the servlet spec does not
 * enable servlet engines to inform us if the browser goes away so we check by
 * asking the browser to reconnect periodically.</p>
 * 
 * <p>In USAGE_LOW mode we measure the number of clients using the number of
 * concurrently connected browsers (waitingThreads), when this goes above
 * maxWaitingThreads we move into USAGE_HIGH mode.</p>
 * 
 * <p>On entering USAGE_HIGH mode, the settings (initially) change to
 * connectedTime=49 secs and disconnectedTime=1 sec. As the load increases the
 * connectedTime decreases linearly from 49 secs down to prevent the hits per
 * second from going above maxHitsPerSecond. If the connectedTime goes below
 * 1sec then the mode switches to USAGE_DIGG. If the connectedTime goes above
 * 49 secs then mode switches to USAGE_LOW.</p>
 * 
 * <p>Note: there is some danger of an overlap where the system toggles between
 * USAGE_HIGH and USAGE_LOW. We need some way to prevent this from happening.
 * </p>
 * 
 * <p>On entering USAGE_DIGG mode, the connectedTime changes to 0 secs, and the
 * disconnectedTime changes to 2 secs (to keep the round trip time at 2 secs).
 * The disconnectedTime alters to prevent the hitsPerSecond from going above
 * maxHitsPerSecond (In USAGE_HIGH mode the connectedTime was altered).
 * When the disconnectedTime would go under 2 secs, we switch back to USAGE_HIGH
 * mode.</p> 
 * @author Joe Walker [joe at getahead dot org]
 */
public class DefaultServerLoadMonitor extends AbstractServerLoadMonitor implements ServerLoadMonitor
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
        waitingThreads++;
        super.threadWaitStarting(controller);

        checkLoading();
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.impl.AbstractServerLoadMonitor#threadWaitEnding(org.directwebremoting.extend.WaitController)
     */
    public void threadWaitEnding(WaitController controller)
    {
        waitingThreads--;
        super.threadWaitEnding(controller);
    }

    /**
     * Check that we are setting the time to next poll correctly.
     */
    private void checkLoading()
    {
        float hitsPerSecond = (float) hitMonitor.getHitsInLastPeriod() / SECONDS_MONITORED;

        if (waitingThreads < maxWaitingThreads)
        {
            connectedTime = maxConnectedTime;
            disconnectedTime = 0;

            setMode(USAGE_LOW);
            return;
        }

        int roundTripAtThreadOutSeconds = threadOutRoundTripTime / 1000;

        int hitsPerSecondAtThreadOut = maxWaitingThreads / roundTripAtThreadOutSeconds;
        int hitsPerSecondAtHitOut = maxHitsPerSecond;
        
        if (hitsPerSecond < hitsPerSecondAtThreadOut)
        {
            // We should probably be in USAGE_LOW mode, so we force the low
            // end of the values in USAGE_HIGH mode
            connectedTime = usageHighInitialConnectedTime;
            disconnectedTime = usageHighDisconnectedTime;

            setMode(USAGE_HIGH);
            return;
        }

        if (mode == USAGE_DIGG)
        {
            // If we're getting close to the upper bound then slow down
            float load = hitsPerSecond / maxHitsPerSecond;
            connectedTime = usageDiggConnectedTime;
            disconnectedTime = (int) (disconnectedTime * load);

            // Check that USAGE_DIGG is the correct mode and we shouldn't change
            if (disconnectedTime > usageDiggMinDisconnectedTime)
            {
                setMode(USAGE_DIGG);
                return;
            }

            // So we were in USAGE_DIGG, but disconnectedTime was so low that we
            // think USAGE_HIGH is a better mode to try
        }

        if (hitsPerSecond < hitsPerSecondAtHitOut)
        {
            // if hitsPerSecondAtThreadOut=0 and hitsPerSecondAtHitOut=1
            // where would we score?
            float factor = (float) waitingThreads / maxWaitingThreads;
            connectedTime = (int) (connectedTime / factor);

            if (connectedTime > usageHighInitialConnectedTime)
            {
                connectedTime = usageHighInitialConnectedTime;
            }

            if (connectedTime < usageHighFinalConnectedTime)
            {
                connectedTime = usageHighFinalConnectedTime;
            }

            disconnectedTime = usageHighDisconnectedTime;

            setMode(USAGE_HIGH);
            return;
        }

        float load = hitsPerSecond / maxHitsPerSecond;
        connectedTime = usageDiggConnectedTime;
        disconnectedTime = (int) (disconnectedTime * load);

        if (disconnectedTime < usageDiggMinDisconnectedTime)
        {
            disconnectedTime = usageDiggMinDisconnectedTime;
        }

        setMode(USAGE_DIGG);
        return;
    }

    /**
     * For debug purposes we keep a track of what mode we are in.
     * @param mode The new usage mode
     */
    protected void setMode(int mode)
    {
        if (log.isDebugEnabled() && mode != this.mode)
        {
            log.debug("Changing modes, from " + USAGE_NAMES[this.mode] + " to " + USAGE_NAMES[mode]);
        }

        this.mode = mode;
    }

    /**
     * @param maxWaitingThreads the maxWaitingThreads to set
     */
    public void setMaxWaitingThreads(int maxWaitingThreads)
    {
        this.maxWaitingThreads = maxWaitingThreads;
    }

    /**
     * @param maxHitsPerSecond the maxHitsPerSecond to set
     */
    public void setMaxHitsPerSecond(int maxHitsPerSecond)
    {
        this.maxHitsPerSecond = maxHitsPerSecond;
    }

    /**
     * It might be good top expose this, however there are currently assumptions
     * in the code that the value is set to 60000.
     * See {@link #usageHighInitialConnectedTime}.
     * @param maxConnectedTime the maxConnectedTime to set
     */
    void setMaxConnectedTime(int maxConnectedTime)
    {
        this.maxConnectedTime = maxConnectedTime;
    }

    /**
     * 
     */
    protected static final int usageHighDisconnectedTime = 1000;
    protected static final int usageHighInitialConnectedTime = 49000;
    protected static final int usageHighFinalConnectedTime = 1000;
    protected static final int usageDiggConnectedTime = 0;
    protected static final int usageDiggMinDisconnectedTime = usageHighDisconnectedTime + usageHighFinalConnectedTime;
    protected static final int hitOutRoundTripTime = usageHighDisconnectedTime + usageHighFinalConnectedTime;
    protected static final int threadOutRoundTripTime = usageHighInitialConnectedTime + usageHighDisconnectedTime;

    /**
     * Static configuration data: The max number of threads we keep waiting.
     * We reduce the timeWithinPoll*Stream variables to reduce the load
     */
    protected int maxWaitingThreads = 100;

    /**
     * Static configuration data: The max number of hits per second.
     * We increase the poll time to compensate and reduce the load. If this
     * number is not at least half maxWaitingThreads then the USAGE_HIGH mode
     * will not exist and the system will sublime from USAGE_LOW to USAGE_DIGG
     */
    protected int maxHitsPerSecond = 100;

    /**
     * Static configuration data: What is the longest we wait for extra input
     * after detecting output.
     */
    protected int maxConnectedTime = 60000;

    /**
     * The system is under-utilized. Everyone does comet.
     */
    protected static final int USAGE_LOW = 0;

    /**
     * This system can't cope with everyone on comet, we are in mixed mode.
     */
    protected static final int USAGE_HIGH = 1;

    /**
     * The system is very heavily used, polling only.
     */
    protected static final int USAGE_DIGG = 2;

    /**
     * Some Strings to help us give some debug output
     */
    protected static final String[] USAGE_NAMES = { "Low", "High", "Digg" };

    /**
     * What is the current usage mode.
     */
    protected int mode = USAGE_LOW;

    /**
     * The time we are currently waiting before sending a browser away and
     * asking it to reconnect.
     */
    protected int connectedTime = 60000;

    /**
     * How long are we telling users to wait before they come back next
     */
    protected int disconnectedTime = 1000;

    /**
     * We are recording the number of hits in the last 5 seconds.
     * Maybe we should think about making this configurable.
     */
    protected static final int SECONDS_MONITORED = 10;

    /**
     * Our record of the server loading
     */
    protected HitMonitor hitMonitor = new HitMonitor(SECONDS_MONITORED);

    /**
     * How many sleepers are there?
     */
    protected int waitingThreads = 0;

    /**
     * The log stream
     */
    private static final Logger log = Logger.getLogger(DefaultServerLoadMonitor.class);
}
