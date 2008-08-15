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

/**
 * An Alarm that goes off whenever output happens on a {@link ScriptSession}.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class OutputAlarm implements Alarm
{
    /**
     * @param scriptSession The script session to monitor
     * @param maxWaitAfterWrite How long do we wait after output
     */
    public OutputAlarm(Sleeper sleeper, RealScriptSession scriptSession, int maxWaitAfterWrite, ScheduledThreadPoolExecutor executor)
    {
        this.sleeper = sleeper;
        this.maxWaitAfterWrite = maxWaitAfterWrite;
        this.scriptSession = scriptSession;
        this.executor = executor;

        conduit = new AlarmScriptConduit();
        try
        {
            scriptSession.addScriptConduit(conduit);
        }
        catch (IOException ex)
        {
            log.warn("Error adding monitor to script session", ex);
        }
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.dwrp.Alarm#cancel()
     */
    public void cancel()
    {
        scriptSession.removeScriptConduit(conduit);
        if (future != null)
        {
            future.cancel(false);
        }
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
            super(RANK_PROCEDURAL, false);
        }

        /* (non-Javadoc)
         * @see org.directwebremoting.extend.ScriptConduit#addScript(org.directwebremoting.ScriptBuffer)
         */
        @Override
        public boolean addScript(ScriptBuffer script)
        {
            // log.debug("Output alarm went off. Additional wait of " + maxWaitAfterWrite);

            if (maxWaitAfterWrite <= 0)
            {
                sleeper.wakeUp();
            }
            else
            {
                Runnable runnable = new Runnable()
                {
                    public void run()
                    {
                        sleeper.wakeUp();
                    }
                };

                future = executor.schedule(runnable, maxWaitAfterWrite, TimeUnit.MILLISECONDS);
            }

            return false;
        }
    }

    /**
     * The thread that needs to know about shutdown
     */
    private final Sleeper sleeper;

    /**
     * A conduit to alert us if there is output
     */
    protected final ScriptConduit conduit;

    /**
     * How long do we wait after output happens in case there is more output
     */
    protected final int maxWaitAfterWrite;

    /**
     * The script session to monitor for output
     */
    protected final RealScriptSession scriptSession;

    /**
     * The future result that allows us to cancel the timer
     */
    protected ScheduledFuture<?> future;

    /**
     * How we schedule the ScriptConduit to call {@link Sleeper#wakeUp()}
     * after {@link #maxWaitAfterWrite} millis has passed.
     */
    protected final ScheduledThreadPoolExecutor executor;

    /**
     * The log stream
     */
    private static final Log log = LogFactory.getLog(OutputAlarm.class);
}
