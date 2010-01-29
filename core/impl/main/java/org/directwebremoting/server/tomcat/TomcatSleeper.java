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

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

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
        event.getHttpServletRequest().setAttribute(DwrCometProcessor.ATTRIBUTE_EVENT, this);
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.dwrp.Sleeper#goToSleep(java.lang.Runnable)
     */
    public void goToSleep(Runnable awakening)
    {
        if (awakening == null)
        {
            throw new NullPointerException("Null value for awakening");
        }

        if (state.compareAndSet(State.INITIAL, State.ABOUT_TO_SLEEP))
        {
            // Signal to DwrCometProcessor.event that we're finishing, not because
            // we're done, but because we want to sleep.
            event.getHttpServletRequest().setAttribute(DwrCometProcessor.ATTRIBUTE_SLEEP, true);
            onAwakening = awakening;
            state.set(State.SLEEPING); // write volatile
        }
        else if (state.compareAndSet(State.PRE_AWAKENED, State.FINAL))
        {
            awakening.run();
        }
        else
        {
            throw new IllegalStateException("Attempt to goToSleep in state " + state.get());
        }
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.extend.Sleeper#wakeUp()
     */
    public void wakeUp()
    {
        boolean retry;
        do
        {
            retry = false;
            switch (state.get()) // read volatile
            {
                case INITIAL:
                    // We might have been awakened before goToSleep.
                    state.compareAndSet(State.INITIAL, State.PRE_AWAKENED);
                    retry=true; // retry
                    break;

                case PRE_AWAKENED:
                    // Do nothing now; goToSleep will eventually run
                    // its onAwakening argument.
                    break;

                case ABOUT_TO_SLEEP:
                    // Spin until we're SLEEPING.
                    // This case is unlikely, but if it does occur,
                    // the spin won't be for very long.
                    try
                    {
                        do
                        {
                            TimeUnit.MILLISECONDS.sleep(1);
                        }
                        while (state.get() == State.ABOUT_TO_SLEEP);
                    }
                    catch (InterruptedException ex)
                    {
                        Thread.currentThread().interrupt();
                        return; // give up on wakeUp
                    }
                    retry=true; // retry
                    break;

                case SLEEPING:
                    if (state.compareAndSet(State.SLEEPING, State.RESUMING))
                    {
                        onAwakening.run();
                        try
                        {
                            event.close();
                        }
                        catch (Exception ex)
                        {
                            log.error("Error while closing the event", ex);
                        }
                    }
                    else
                    {
                        // Someone else got in first. The only states
                        // we could be in now are going to ignore the
                        // wakeUp call, but for completeness we retry.
                        retry=true; // retry
                    }
                    break;

                case RESUMING:
                case FINAL:
                    // wakeUp called already, nothing to do.
                    break;
            }
        }
        while (retry);
    }

    enum State
    {
        INITIAL, // the state at construction time
        PRE_AWAKENED, // wakeUp called before goToSleep
        ABOUT_TO_SLEEP, // trying to sleep
        SLEEPING, // sleeping
        RESUMING, // sleeping by blocking with ThreadWaitSleeper
        FINAL, // the state after resumption or pre-awakened sleep attempt
    }

    /**
     * Atomic enum to manage state.
     */
    private final AtomicReference<State> state = new AtomicReference<State>(State.INITIAL);

    /**
     * Tomcat's container for the request/response for this transaction
     */
    private final CometEvent event;

    /**
     * What we do when we are woken up
     */
    /* @GuardedBy("state") */private Runnable onAwakening;

    /**
     * The log stream
     */
    private static final Log log = LogFactory.getLog(TomcatSleeper.class);
}
