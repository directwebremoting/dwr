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

import java.io.IOException;
import java.util.TimerTask;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.directwebremoting.ScriptBuffer;
import org.directwebremoting.ScriptSession;
import org.directwebremoting.extend.RealScriptSession;
import org.directwebremoting.extend.ScriptConduit;
import org.directwebremoting.util.StaticTimer;

/**
 * An Alarm that goes off whenever output happens on a {@link ScriptSession}.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class OutputAlarm extends BasicAlarm implements Alarm
{
    /**
     * @param scriptSession The script session to monitor
     * @param maxWaitAfterWrite How long do we wait after output
     */
    public OutputAlarm(RealScriptSession scriptSession, int maxWaitAfterWrite)
    {
        this.maxWaitAfterWrite = maxWaitAfterWrite;
        this.scriptSession = scriptSession;
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.dwrp.Alarm#setAlarmAction(org.directwebremoting.dwrp.Sleeper)
     */
    public void setAlarmAction(Sleeper sleeper)
    {
        try
        {
            scriptSession.addScriptConduit(conduit);
        }
        catch (IOException ex)
        {
            log.warn("Error adding monitor to script session", ex);
        }

        super.setAlarmAction(sleeper);
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.dwrp.Alarm#cancel()
     */
    public void cancel()
    {
        scriptSession.removeScriptConduit(conduit);
        if (task != null)
        {
            task.cancel();
        }
        super.cancel();
    }

    /**
     * @author Joe Walker [joe at getahead dot ltd dot uk]
     */
    protected class AlarmScriptConduit extends ScriptConduit
    {
        /**
         * Create an AlarmScriptConduit
         */
        protected AlarmScriptConduit()
        {
            super(RANK_PROCEDURAL);
        }

        /* (non-Javadoc)
         * @see org.directwebremoting.extend.ScriptConduit#addScript(org.directwebremoting.ScriptBuffer)
         */
        public boolean addScript(ScriptBuffer script)
        {
            if (maxWaitAfterWrite == 0)
            {
                raiseAlarm();
            }
            else
            {
                task = new TimerTask()
                {
                    public void run()
                    {
                        try
                        {
                            raiseAlarm();
                        }
                        catch (Exception ex)
                        {
                            log.warn("Unexpected error raising alarm", ex);
                        }
                    }
                };

                StaticTimer.schedule(task, maxWaitAfterWrite);
            }

            return false;
        }
    }

    /**
     * A conduit to alert us if there is output
     */
    protected ScriptConduit conduit = new AlarmScriptConduit();

    /**
     * How long do we wait after output happens in case there is more output
     */
    protected int maxWaitAfterWrite;

    /**
     * The script session to monitor for output
     */
    protected RealScriptSession scriptSession;

    /**
     * The task that causes the alarm to go off
     */
    protected TimerTask task;

    /**
     * The log stream
     */
    protected static final Log log = LogFactory.getLog(OutputAlarm.class);
}
