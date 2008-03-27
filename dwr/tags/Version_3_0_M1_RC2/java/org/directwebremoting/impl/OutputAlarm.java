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

import java.io.IOException;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.directwebremoting.ScriptBuffer;
import org.directwebremoting.ScriptSession;
import org.directwebremoting.extend.Alarm;
import org.directwebremoting.extend.RealScriptSession;
import org.directwebremoting.extend.ScriptConduit;
import org.directwebremoting.extend.Sleeper;
import org.directwebremoting.util.SharedObjects;

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

        conduit = new AlarmScriptConduit();
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.dwrp.Alarm#setAlarmAction(org.directwebremoting.dwrp.Sleeper)
     */
    @Override
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
    @Override
    public void cancel()
    {
        scriptSession.removeScriptConduit(conduit);
        if (future != null)
        {
            future.cancel(false);
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
        @Override
        public boolean addScript(ScriptBuffer script)
        {
            // log.debug("Output alarm went off. Additional wait of " + maxWaitAfterWrite);

            if (maxWaitAfterWrite == 0)
            {
                raiseAlarm();
            }
            else
            {
                Runnable runnable = new Runnable()
                {
                    public void run()
                    {
                        raiseAlarm();
                    }
                };

                ScheduledThreadPoolExecutor executor = SharedObjects.getScheduledThreadPoolExecutor();
                future = executor.schedule(runnable, maxWaitAfterWrite, TimeUnit.MILLISECONDS);
            }

            return false;
        }
    }

    /**
     * A conduit to alert us if there is output
     */
    protected ScriptConduit conduit = null;

    /**
     * How long do we wait after output happens in case there is more output
     */
    protected int maxWaitAfterWrite;

    /**
     * The script session to monitor for output
     */
    protected RealScriptSession scriptSession;

    /**
     * The log stream
     */
    protected static final Log log = LogFactory.getLog(OutputAlarm.class);

    /**
     * The future result that allows us to cancel the timer
     */
    protected ScheduledFuture<?> future;
}
