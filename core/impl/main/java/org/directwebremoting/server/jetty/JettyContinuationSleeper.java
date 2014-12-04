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

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.directwebremoting.extend.RealScriptSession;
import org.directwebremoting.extend.ScriptConduit;
import org.directwebremoting.impl.BaseSleeper;
import org.eclipse.jetty.continuation.Continuation;
import org.eclipse.jetty.continuation.ContinuationSupport;

/**
 * A Sleeper that works with Jetty Continuations
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 * @author Tim Peierls [tim at peierls dot net]
 * @author Mike Wilson
 */
public class JettyContinuationSleeper extends BaseSleeper
{
    /**
     * @param request The request into which we store this as an attribute
     * @param conduit
     * @param scriptSession
     * @param response
     * @throws IOException
     */
    public JettyContinuationSleeper(HttpServletRequest request, HttpServletResponse response, RealScriptSession scriptSession, ScriptConduit conduit) throws IOException
    {
        super(response, scriptSession, conduit);

        continuation = ContinuationSupport.getContinuation(request);
        continuation.setAttribute(ATTRIBUTE_SLEEPER, this);
        continuation.setTimeout(0);
        continuation.suspend();
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.impl.BaseSleeper#enterSleep()
     */
    @Override
    protected void enterSleep()
    {
        // NOP
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.impl.BaseSleeper#wakeUp()
     */
    @Override
    protected void wakeUp()
    {
        synchronized (workLock)
        {
            if (workInProgress) {
                queuedWork = true;
            } else {
                continuation.resume(); // will eventually trigger resumeWork() from a container thread
                workInProgress = true;
            }
        }
    }

    /**
     * The method that should be called by a background thread when we are being awakened
     */
    public void resumeWork()
    {
        while(true) {
            doWork();
            if (continuation.getAttribute(ATTRIBUTE_SLEEPER) == null) {
                // If we have disconnected the Sleeper from the container then it is time to end the async cycle
                return;
            }
            synchronized (workLock)
            {
                if (queuedWork) {
                    // New work has arrived so fall through to take another spin in the loop
                    queuedWork = false;
                } else {
                    // No work in the queue so exit loop and go to sleep again
                    continuation.suspend();
                    workInProgress = false;
                    return;
                }
            }
        }
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.impl.BaseSleeper#close()
     */
    @Override
    protected void close()
    {
        continuation.removeAttribute(ATTRIBUTE_SLEEPER);
    }

    /**
     * Retrieve a stashed sleeper from a request.
     */
    public static JettyContinuationSleeper getSleeper(HttpServletRequest request)
    {
        return (JettyContinuationSleeper) request.getAttribute(ATTRIBUTE_SLEEPER);
    }

    /**
     * The Jetty continuation object
     */
    private final Continuation continuation;

    // Internal state
    private final Object workLock = new Object();
    private boolean workInProgress = false;
    private boolean queuedWork = false;

    /**
     * We remember the sleeper on a request attribute
     */
    protected static final String ATTRIBUTE_SLEEPER = JettyContinuationSleeper.class.getName();
}
