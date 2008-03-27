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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.directwebremoting.extend.ServerLoadMonitor;

/**
 * A default implementation of ServerLoadMonitor
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class PollingServerLoadMonitor extends AbstractServerLoadMonitor implements ServerLoadMonitor
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
    public void setTimeToNextPoll(int disconnectedTime)
    {
        log.warn("timeToNextPoll is deprecated. Please use disconnectedTime");
        this.disconnectedTime = disconnectedTime;
    }

    /**
     * Accessor for the disconnected time.
     * @param disconnectedTime How long should clients spend disconnected
     */
    public void setDisconnectedTime(int disconnectedTime)
    {
        this.disconnectedTime = disconnectedTime;
    }

    /**
     * How long are we telling users to wait before they come back next
     */
    protected int disconnectedTime = 5000;

    /**
     * The log stream
     */
    private static final Log log = LogFactory.getLog(PollingServerLoadMonitor.class);
}
