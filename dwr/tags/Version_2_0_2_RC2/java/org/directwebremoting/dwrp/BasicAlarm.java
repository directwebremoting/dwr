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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.directwebremoting.util.LocalUtil;

/**
 * An Alarm that goes off if something is badly broken.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class BasicAlarm implements Alarm
{
    /**
     * It's all gone wrong - inform the Sleeper
     */
    public void raiseAlarm()
    {
        log.debug("Alarm raised: " + LocalUtil.getShortClassName(getClass()));
        synchronized (sleeperLock)
        {
            if (sleeper != null)
            {
                sleeper.wakeUp();
            }
        }
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.dwrp.Alarm#cancel()
     */
    public void cancel()
    {
        synchronized (sleeperLock)
        {
            sleeper = null;
        }
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.dwrp.Alarm#setAlarmAction(org.directwebremoting.dwrp.Sleeper)
     */
    public void setAlarmAction(Sleeper sleeper)
    {
        // I think the life-cycle dictates that we don't need to synchronize here
        // however it's probably simpler to synchronize and be sure
        synchronized (sleeperLock)
        {
            this.sleeper = sleeper;
        }
    }

    /**
     * The protection for the sleeper
     */
    private Object sleeperLock = new Object();

    /**
     * The thread that needs to know about shutdown
     * @protectedBy(sleeperLock)
     */
    private Sleeper sleeper;

    /**
     * The log stream
     */
    private static final Log log = LogFactory.getLog(BasicAlarm.class);
}
