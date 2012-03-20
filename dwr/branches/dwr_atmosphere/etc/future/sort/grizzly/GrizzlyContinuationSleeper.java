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

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.directwebremoting.util.Continuation;

/**
 * A Sleeper that works with Grizzly Continuations
 * @author Jeanfrancois Arcand [jeanfrancois dot arcand at sun dot com]
 */
public class GrizzlyContinuationSleeper implements Sleeper
{
    /**
     * @param request The request into which we store this as an attribute
     */
    public GrizzlyContinuationSleeper(HttpServletRequest request)
    {
        continuation = new Continuation(request);
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.dwrp.Sleeper#goToSleep(java.lang.Runnable)
     */
    public void goToSleep(Runnable awakening)
    {
        this.onAwakening = awakening;

        try
        {
            continuation.suspend(-1);
        }
        catch (Exception ex)
        {
            Continuation.rethrowIfContinuation(ex);

            log.warn("Exception", ex);
            proxy = new ThreadWaitSleeper();
            proxy.goToSleep(onAwakening);
        }
    }

    
    /* (non-Javadoc)
     * @see org.directwebremoting.dwrp.PollHandler.Sleeper#wakeUp()
     */
    public void wakeUp()
    {
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
                        /*
                         * Flush bytes if any first as before resuming the 
                         * as Grizzly Comet isn't allowing writes once the
                         * continuation is resumed. 
                         *
                         * This can be achieved
                         * by using Grizzly CometHandler, which isn't exposed
                         * with DWR.
                         */
                        onAwakening.run();
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
    
    /**
     * If continuations fail, we proxy to a Thread Wait version
     */
    protected ThreadWaitSleeper proxy = null;

    /**
     * What we do when we are woken up
     */
    protected Runnable onAwakening;

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
    public static final String ATTRIBUTE_JETTY_CONDUIT = "org.directwebremoting.dwrp.notifyConduit";
   
    private static final Log log = LogFactory.getLog(GrizzlyContinuationSleeper.class);
}

