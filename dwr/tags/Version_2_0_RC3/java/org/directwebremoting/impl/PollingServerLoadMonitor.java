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

/**
 * A default implementation of ServerLoadMonitor
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class PollingServerLoadMonitor extends AbstractServerLoadMonitor implements ServerLoadMonitor
{
    /* (non-Javadoc)
     * @see org.directwebremoting.extend.ServerLoadMonitor#getMaxConnectedTime()
     */
    public long getMaxConnectedTime()
    {
        return 0;
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.ServerLoadMonitor#timeToNextPoll()
     */
    public int getTimeToNextPoll()
    {
        return timeToNextPoll;
    }

    /**
     * Accessor for the disconnected time.
     * @param timeToNextPoll How long should clients spend disconnected
     */
    public void setTimeToNextPoll(int timeToNextPoll)
    {
        this.timeToNextPoll = timeToNextPoll;
    }

    /**
     * How long are we telling users to wait before they come back next
     */
    protected int timeToNextPoll = 5000;
}
