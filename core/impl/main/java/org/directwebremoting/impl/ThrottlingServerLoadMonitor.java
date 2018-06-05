package org.directwebremoting.impl;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.directwebremoting.extend.WaitController;
import org.directwebremoting.util.HitMonitor;

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
 * <p>We impose some limits: a maximum number of simultaneously connected
 * browsers <code>maxWaitingThreads</code>, and the maximum number of
 * connections per second <code>maxHitsPerSecond</code>.</p>
 *
 * <p>We attempt to keep the actual waitingThreads and hitsPerSecond within
 * bounds by varying connectedTime and disconnectedTime.</p>
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
public class ThrottlingServerLoadMonitor extends AbstractServerLoadMonitor
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
    @Override
    public void threadWaitStarting(WaitController controller)
    {
        super.threadWaitStarting(controller);
        hitMonitor.recordHit();

        int currentWaitingThreads = waitingThreads.incrementAndGet();

        // If we are already adjusting skip it.
        if (!adjusting.getAndSet(true))
        {
            loadAdjust(currentWaitingThreads);
            adjusting.set(false);
        }
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.impl.AbstractServerLoadMonitor#threadWaitEnding(org.directwebremoting.extend.WaitController)
     */
    @Override
    public void threadWaitEnding(WaitController controller)
    {
        super.threadWaitEnding(controller);
        waitingThreads.decrementAndGet();
    }

    /**
     * Log a debug message if debug logging is enabled.
     * @param o
     */
    private void debug(Object o) {
        if (log.isDebugEnabled()) {
            log.debug(o);
        }
    }

    /**
     * Check that we are setting the next poll time correctly.
     */
    private void loadAdjust(int currentWaitingThreads)
    {
        // Adjust at most once every SECONDS_MONITORED / 2.
        if ((System.currentTimeMillis() - lastLoadAdjust.get()) < (secondsMonitored/2))
        {
            return;
        }

        // USAGE_LOW check
        if (currentWaitingThreads < maxWaitingThreads)
        {
            // TODO: If the disconnectedTime was previously set different there is
            // no way to return to the default here.
            changeMode(USAGE_LOW, maxConnectedTime, disconnectedTime);
            return;
        }

        // Calculate the number of hits per second based on the number of hits since the
        // last time the monitor ran.
        float hitsPerSecond = (float) hitMonitor.getHitsInLastPeriod() / secondsMonitored;
        int hitsPerSecondAtThreadOut = maxWaitingThreads / roundTripAtThreadOutSeconds;
        int hitsPerSecondAtHitOut = maxHitsPerSecond;

        debug("Hits per second: " + hitsPerSecond);
        debug("Hits per second at ThreadOut: " + hitsPerSecondAtThreadOut);
        debug("Hits per second at HitOut: " + hitsPerSecondAtHitOut);

        // USAGE_HIGH
        if (hitsPerSecond < hitsPerSecondAtThreadOut)
        {
            // We should probably be in USAGE_LOW mode, so we force the low
            // end of the values in USAGE_HIGH mode
            changeMode(USAGE_HIGH, connectedTime, disconnectedTime);
            return;
        }

        float load = hitsPerSecond / maxHitsPerSecond;

        int loadBasedDisconnectedTime = (int) (disconnectedTime * load);
        if (mode.get() == USAGE_DIGG)
        {
            // If we're getting close to the upper bound then slow down
            changeMode(USAGE_DIGG, usageDiggConnectedTime, loadBasedDisconnectedTime);

            // Check that USAGE_DIGG is the correct mode and we shouldn't change
            if (disconnectedTime > usageDiggMinDisconnectedTime)
            {
                return;
            }

            // So we were in USAGE_DIGG, but disconnectedTime was so low that we
            // think USAGE_HIGH is a better mode to try
        }

        if (hitsPerSecond < hitsPerSecondAtHitOut)
        {
            // if hitsPerSecondAtThreadOut=0 and hitsPerSecondAtHitOut=1
            // where would we score?
            float factor = (float) currentWaitingThreads / maxWaitingThreads;

            int tmpConnectedTime = (int) (connectedTime / factor);
            if (tmpConnectedTime > usageHighInitialConnectedTime)
            {
                tmpConnectedTime = usageHighInitialConnectedTime;
            } else if (tmpConnectedTime < usageHighFinalConnectedTime)
            {
                tmpConnectedTime = usageHighFinalConnectedTime;
            }

            changeMode(USAGE_HIGH, tmpConnectedTime, usageHighDisconnectedTime);
            return;
        }

        if (loadBasedDisconnectedTime < usageDiggMinDisconnectedTime)
        {
            changeMode(USAGE_DIGG, usageDiggConnectedTime, usageDiggMinDisconnectedTime);
        } else
        {
            changeMode(USAGE_DIGG, usageDiggConnectedTime, loadBasedDisconnectedTime);
        }
    }

    private void changeMode(int usageMode, int _connectedTime, int _disconnectedTime) {
        this.connectedTime = _connectedTime;
        this.disconnectedTime = _disconnectedTime;
        setMode(usageMode);
    }

    /**
     * For debug purposes we keep a track of what mode we are in.
     * @param newMode The new usage mode
     */
    protected void setMode(int newMode)
    {
        int currentMode = this.mode.getAndSet(newMode);
        if (newMode != currentMode)
        {
            debug("Changing modes, from " + USAGE_NAMES[currentMode] + " to " + USAGE_NAMES[newMode]);
        }
    }

    protected int getMode() {
        return mode.get();
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
     * It might be good to expose this, however there are currently assumptions
     * in the code that the value is set to 60000.
     * See {@link #usageHighInitialConnectedTime}.
     * @param maxConnectedTime the maxConnectedTime to set
     */
    void setMaxConnectedTime(int maxConnectedTime)
    {
        this.maxConnectedTime = maxConnectedTime;
    }

    /**
     * static calculations
     */
    protected static final int usageHighDisconnectedTime = 1000;
    protected static final int usageHighInitialConnectedTime = 49000;
    protected static final int usageHighFinalConnectedTime = 1000;
    protected static final int usageDiggConnectedTime = 0;
    protected static final int usageDiggMinDisconnectedTime = usageHighDisconnectedTime + usageHighFinalConnectedTime;
    protected static final int hitOutRoundTripTime = usageHighDisconnectedTime + usageHighFinalConnectedTime;
    protected static final int threadOutRoundTripTime = usageHighInitialConnectedTime + usageHighDisconnectedTime;
    protected static final int roundTripAtThreadOutSeconds = threadOutRoundTripTime / 1000;

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
    protected AtomicInteger mode = new AtomicInteger(USAGE_LOW);

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
     * We are recording the number of hits in the last 10 seconds.
     */
    protected int secondsMonitored = 10;

    /**
     * Our record of the server loading
     */
    protected HitMonitor hitMonitor = new HitMonitor(secondsMonitored);

    /**
     * How many sleepers are there?
     */
    protected AtomicInteger waitingThreads = new AtomicInteger(0);

    /**
     * Stores the last time the monitor made an adjustment and tells us if
     * the loadAdjustment is already taking place in another thread.
     */
    protected AtomicLong lastLoadAdjust = new AtomicLong(System.currentTimeMillis());
    protected AtomicBoolean adjusting = new AtomicBoolean(false);

    /**
     * The log stream
     */
    private static final Log log = LogFactory.getLog(ThrottlingServerLoadMonitor.class);
}
