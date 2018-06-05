package org.directwebremoting.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * A default implementation of ServerLoadMonitor
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class PollingServerLoadMonitor extends AbstractServerLoadMonitor
{
    /* (non-Javadoc)
     * @see org.directwebremoting.extend.ServerLoadMonitor#supportsStreaming()
     */
    public boolean supportsStreaming()
    {
        return false;
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.extend.ServerLoadMonitor#getMaxConnectedTime()
     */
    public long getConnectedTime()
    {
        return 0;
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.ServerLoadMonitor#timeToNextPoll()
     */
    public int getDisconnectedTime()
    {
        return disconnectedTime;
    }

    /**
     * Accessor for the disconnected time.
     * @param disconnectedTime How long should clients spend disconnected
     * @deprecated Use {@link #setDisconnectedTime(int)} instead
     */
    @Deprecated
    public void setTimeToNextPoll(int disconnectedTime)
    {
        log.warn("timeToNextPoll is deprecated. Please use disconnectedTime");
        setDisconnectedTime(disconnectedTime);
    }

    /**
     * Accessor for the disconnected time.
     * @param disconnectedTime How long should clients spend disconnected
     */
    public void setDisconnectedTime(int disconnectedTime)
    {
        if (disconnectedTime < 500)
        {
            log.warn("Small values of disconnectedTime could heavy server load. Using disconnectedTime=500");
            disconnectedTime = 500;
        }

        this.disconnectedTime = disconnectedTime;
    }

    /**
     * How long are we telling users to wait before they come back next
     */
    protected int disconnectedTime = 500;

    /**
     * The log stream
     */
    private static final Log log = LogFactory.getLog(PollingServerLoadMonitor.class);
}
