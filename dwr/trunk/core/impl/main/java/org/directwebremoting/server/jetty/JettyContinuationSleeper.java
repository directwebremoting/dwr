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
package org.directwebremoting.server.jetty;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.directwebremoting.extend.Sleeper;
import org.directwebremoting.impl.ThreadWaitSleeper;
import org.directwebremoting.util.Continuation;

/**
 * A Sleeper that works with Jetty Continuations
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 * @author Tim Peierls [tim at peierls dot net]
 */
public class JettyContinuationSleeper implements Sleeper
{
    /**
     * @param request The request into which we store this as an attribute
     */
    public JettyContinuationSleeper(HttpServletRequest request)
    {
        this.continuation = new Continuation(request);
        this.request = request;
    }

    /**
     * Is this a restarted continuation?
     * @param request The request on which a Sleeper might be stored
     * @return true if this request is from a restarted Continuation
     */
    public static boolean isRestart(HttpServletRequest request)
    {
        return getSleeper(request) != null;
    }

    /**
     * Act on a restarted continuation by executing the onAwakening action
     * @param request The request on which the Sleeper is stored
     */
    public static void restart(HttpServletRequest request)
    {
        JettyContinuationSleeper sleeper = getSleeper(request);
        if (sleeper == null)
        {
            throw new IllegalStateException("No JettyContinuationSleeper in HttpServletRequest");
        }

        sleeper.resume(); // calls onAwakening
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
            try
            {
                // This throws a RuntimeException that must propagate to the
                // container. The docs say that a value of 0 should suspend
                // forever, but that did not to happen in 6.1.1 so we
                // suspend for BigNum
                continuation.suspend(Integer.MAX_VALUE);
            }
            catch (Exception ex)
            {
                Continuation.rethrowIfContinuation(ex);

                // Log unsuccessful attempt to use Jetty continuation.
                log.warn("Exception", ex);

                // Revert to thread waiting.
                proxy = new ThreadWaitSleeper();

                state.set(State.BLOCKED); // write volatile

                // Block until wakeUp call.
                proxy.goToSleep(awakening);
            } finally {
                if (state.compareAndSet(State.BLOCKED, State.FINAL))
                {
                    // We have just been awakened from BLOCKED and
                    // awakening task has run.
                }
                else // state.get() == ABOUT_TO_SLEEP
                {
                    // This is a rethrow for a Jetty continuation,
                    // so we store the onAwakening task and tell the
                    // request how to restart on continuation resume.
                    onAwakening = awakening;
                    saveSleeperOnRequest();

                    state.set(State.SLEEPING); // write volatile
                }
            }
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
        switch (state.get()) {  // read volatile
            case INITIAL:
                // We might have been awakened before goToSleep.
                state.compareAndSet(State.INITIAL, State.PRE_AWAKENED);
                wakeUp(); // retry
                break;

            case PRE_AWAKENED:
                // Do nothing now; goToSleep will eventually run
                // its onAwakening argument.
                break;

            case ABOUT_TO_SLEEP:
                // Spin until we're either SLEEPING or BLOCKED.
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
                wakeUp(); // retry
                break;

            case SLEEPING:
                if (state.compareAndSet(State.SLEEPING, State.RESUMING))
                {
                    try
                    {
                        continuation.resume();
                    }
                    catch (Exception ex)
                    {
                        log.error("Broken reflection", ex);
                    }
                }
                else
                {
                    // Someone else got in first. The only states
                    // we could be in now are going to ignore the
                    // wakeUp call, but for completeness we retry.
                    wakeUp(); // retry
                }
                break;

            case BLOCKED:
                // Cause onAwakening to be called in blocked thread.
                proxy.wakeUp();
                break;

            case RESUMING:
            case FINAL:
                // wakeUp called already, nothing to do.
                break;
        }
    }

    private void resume()
    {
        if (!state.compareAndSet(State.RESUMING, State.FINAL)) // read-volatile
        {
            throw new IllegalStateException("Attempt to resume from state " + state.get());
        }

        request.removeAttribute(ATTRIBUTE_CONDUIT);

        onAwakening.run();
    }

    private void saveSleeperOnRequest()
    {
        request.setAttribute(ATTRIBUTE_CONDUIT, this);
    }

    private static JettyContinuationSleeper getSleeper(HttpServletRequest request)
    {
        return (JettyContinuationSleeper) request.getAttribute(ATTRIBUTE_CONDUIT);
    }


    enum State
    {
        INITIAL,        // the state at construction time
        PRE_AWAKENED,   // wakeUp called before goToSleep
        ABOUT_TO_SLEEP, // trying to sleep
        SLEEPING,       // sleeping using continuation
        BLOCKED,        // sleeping by blocking with ThreadWaitSleeper
        RESUMING,       // resuming from continuation sleep
        FINAL,          // the state after resumption or pre-awakened sleep attempt
    }


    /**
     * Atomic enum to manage state.
     */
    private final AtomicReference<State> state = new AtomicReference<State>(State.INITIAL);

    /**
     * If continuations fail, we proxy to a Thread Wait version
     */
    /* @GuardedBy("state") */ private ThreadWaitSleeper proxy = null;

    /**
     * The continuation object
     */
    private final Continuation continuation;

    /**
     * What we do when we are woken up
     */
    /* @GuardedBy("state") */ private Runnable onAwakening;

    /**
     * The request on which we save this Sleeper for later retrieval.
     */
    private final HttpServletRequest request;

    /**
     * We remember the notify conduit so we can reuse it
     */
    protected static final String ATTRIBUTE_CONDUIT = "org.directwebremoting.server.jetty.notifyConduit";

    /**
     * The log stream
     */
    private static final Log log = LogFactory.getLog(JettyContinuationSleeper.class);
}