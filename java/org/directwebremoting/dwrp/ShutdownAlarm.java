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
package org.directwebremoting.dwrp;

import org.directwebremoting.extend.ServerLoadMonitor;
import org.directwebremoting.extend.WaitController;

/**
 * An Alarm that allows the system to close all connections when it is shutting
 * down.
 * <p><b>WARNING</b>: This code has a non-obvious side effect - The server load
 * monitor (which hands out shutdown messages) also monitors usage by looking
 * at the number of connected alarms.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class ShutdownAlarm extends BasicAlarm implements Alarm
{
    /**
     * Register ourselves with the ServerLoadMonitor so we can raise an
     * Alarm if we get shutdown
     * @param serverLoadMonitor
     */
    public ShutdownAlarm(ServerLoadMonitor serverLoadMonitor)
    {
        this.serverLoadMonitor = serverLoadMonitor;
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.dwrp.PollHandler.Alarm#setAlarmAction(org.directwebremoting.dwrp.PollHandler.Sleeper)
     */
    public void setAlarmAction(Sleeper sleeper)
    {
        serverLoadMonitor.threadWaitStarting(waitController);
        super.setAlarmAction(sleeper);
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.dwrp.PollHandler.Alarm#cancel()
     */
    public void cancel()
    {
        serverLoadMonitor.threadWaitEnding(waitController);
        super.cancel();
    }

    /**
     * The listener to allow the ServerLoadMonitor to shut us down
     */
    private WaitController waitController = new WaitController()
    {
        /* (non-Javadoc)
         * @see org.directwebremoting.extend.WaitController#shutdown()
         */
        public void shutdown()
        {
            raiseAlarm();
            shutdown = true;
        }

        /* (non-Javadoc)
         * @see org.directwebremoting.extend.WaitController#isShutdown()
         */
        public boolean isShutdown()
        {
            return shutdown;
        }

        /**
         * We neeed to be able to say if we have been shutdown
         */
        boolean shutdown = false;
    };

    /**
     * The source of shutdown messages
     */
    protected ServerLoadMonitor serverLoadMonitor;
}
