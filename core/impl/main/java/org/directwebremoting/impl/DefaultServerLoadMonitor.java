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

public class DefaultServerLoadMonitor extends AbstractServerLoadMonitor
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
     * @see org.directwebremoting.extend.ServerLoadMonitor#getDisconnectedTime()
     */
    public int getDisconnectedTime()
    {
        return disconnectedTime;
    }

    /**
     * The time we are currently waiting before sending a browser away and
     * asking it to reconnect.
     */
    protected int connectedTime = 60000;

    /**
     * How long are we telling users to wait before they come back next
     */
    protected int disconnectedTime = 1000;
}
