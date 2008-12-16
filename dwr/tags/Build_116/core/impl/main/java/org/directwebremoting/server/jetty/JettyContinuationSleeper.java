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

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.directwebremoting.extend.Sleeper;
import org.directwebremoting.impl.ThreadWaitSleeper;
import org.directwebremoting.util.Continuation;

/**
 * A Sleeper that works with Jetty Continuations
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class JettyContinuationSleeper implements Sleeper
{
    /**
     * @param request The request into which we store this as an attribute
     */
    public JettyContinuationSleeper(HttpServletRequest request)
    {
        continuation = new Continuation(request);

        // At this point JettyContinuationSleeper is fully initialized so it is
        // safe to allow other classes to see and use us.
        //noinspection ThisEscapedInObjectConstruction
        request.setAttribute(ATTRIBUTE_CONDUIT, this);
    }

    /**
     * Is this a restarted continuation?
     * @param request The request on which a Sleeper might be stored
     * @return true if this request is from a restarted Continuation
     */
    public static boolean isRestart(HttpServletRequest request)
    {
        return request.getAttribute(ATTRIBUTE_CONDUIT) != null;
    }

    /**
     * Act on a restarted continuation by executing the onAwakening action
     * @param request The request on which the Sleeper is stored
     */
    public static void restart(HttpServletRequest request)
    {
        JettyContinuationSleeper sleeper = (JettyContinuationSleeper) request.getAttribute(ATTRIBUTE_CONDUIT);
        if (sleeper == null)
        {
            throw new IllegalStateException("No JettyContinuationSleeper in HttpServletRequest");
        }

        request.removeAttribute(ATTRIBUTE_CONDUIT);
        sleeper.onAwakening.run();
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.dwrp.Sleeper#goToSleep(java.lang.Runnable)
     */
    public void goToSleep(Runnable awakening)
    {
        this.onAwakening = awakening;

        synchronized (wakeUpCalledLock)
        {
            if (wakeUpCalled)
            {
                onAwakening.run();
            }
            else
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

                    log.warn("Exception", ex);
                    proxy = new ThreadWaitSleeper();
                    proxy.goToSleep(onAwakening);
                }
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

            if (proxy != null)
            {
                proxy.wakeUp();
            }
            else
            {
                synchronized (continuation)
                {
                    if (!resumed)
                    {
                        try
                        {
                            continuation.resume();
                        }
                        catch (Exception ex)
                        {
                            log.error("Broken reflection", ex);
                        }

                        resumed = true;
                    }
                }
            }
        }
    }

    /**
     * All operations that involve going to sleep of waking up must hold this
     * lock before they take action.
     */
    private Object wakeUpCalledLock = new Object();

    /**
     * Has wakeUp been called?
     */
    private boolean wakeUpCalled = false;

    /**
     * If continuations fail, we proxy to a Thread Wait version
     */
    private ThreadWaitSleeper proxy = null;

    /**
     * What we do when we are woken up
     */
    private Runnable onAwakening;

    /**
     * The continuation object
     */
    protected final Continuation continuation;

    /**
     * Has the continuation been restarted already?
     */
    protected boolean resumed = false;

    /**
     * We remember the notify conduit so we can reuse it
     */
    protected static final String ATTRIBUTE_CONDUIT = "org.directwebremoting.server.jetty.notifyConduit";

    /**
     * The log stream
     */
    private static final Log log = LogFactory.getLog(JettyContinuationSleeper.class);
}
