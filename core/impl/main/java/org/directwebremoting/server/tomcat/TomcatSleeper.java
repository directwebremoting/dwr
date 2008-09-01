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
package org.directwebremoting.server.tomcat;

import java.io.IOException;

import org.apache.catalina.CometEvent;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.directwebremoting.extend.Sleeper;

/**
 * A Sleeper that works with Tomcat {@link CometEvent}s
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class TomcatSleeper implements Sleeper
{
    /**
     * @param event The request into which we store this as an attribute
     */
    public TomcatSleeper(CometEvent event)
    {
        this.event = event;

        // At this point JettyContinuationSleeper is fully initialized so it is
        // safe to allow other classes to see and use us.
        //noinspection ThisEscapedInObjectConstruction
        event.getHttpServletRequest().setAttribute(DwrCometProcessor.ATTRIBUTE_EVENT, this);
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.dwrp.Sleeper#goToSleep(java.lang.Runnable)
     */
    public void goToSleep(Runnable awakening)
    {
        this.onAwakening = awakening;

        // Signal to DwrCometProcessor.event that we're finishing, not because
        // we're done, but because we want to sleep.
        event.getHttpServletRequest().setAttribute(DwrCometProcessor.ATTRIBUTE_SLEEP, true);

        synchronized (wakeUpCalledLock)
        {
            if (wakeUpCalled)
            {
                onAwakening.run();
            }
        }
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.extend.Sleeper#wakeUp()
     */
    public void wakeUp()
    {
        synchronized (wakeUpCalledLock)
        {
            if (wakeUpCalled)
            {
                return;
            }

            wakeUpCalled = true;

            if (onAwakening != null)
            {
                onAwakening.run();
            }

            try
            {
                event.close();
            }
            catch (IOException ex)
            {
                log.warn("Error while closing the event", ex);
            }
        }
    }

    /**
     * Tomcat's container for the request/response for this transaction
     */
    private final CometEvent event;

    /**
     * All operations that involve going to sleep of waking up must hold this
     * lock before they take action.
     */
    private final Object wakeUpCalledLock = new Object();

    /**
     * Has wakeUp been called?
     */
    private boolean wakeUpCalled = false;

    /**
     * What we do when we are woken up
     */
    private Runnable onAwakening;

    /**
     * The log stream
     */
    private static final Log log = LogFactory.getLog(TomcatSleeper.class);
}
