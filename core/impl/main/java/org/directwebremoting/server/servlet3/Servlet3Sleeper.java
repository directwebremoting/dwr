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
package org.directwebremoting.server.servlet3;

import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.directwebremoting.extend.Sleeper;

/**
 * A Sleeper that works with Jetty Continuations
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class Servlet3Sleeper implements Sleeper
{
    /**
     * @param request The request into which we store this as an attribute
     */
    public Servlet3Sleeper(HttpServletRequest request)
    {
        this.request = request;
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.dwrp.Sleeper#goToSleep(java.lang.Runnable)
     */
    public void goToSleep(Runnable onAwakening)
    {
        synchronized (wakeUpCalledLock)
        {
            if (wakeUpCalled)
            {
                onAwakening.run();
            }
            else
            {
                // request.suspend();

                try
                {
                    suspendMethod.invoke(request);
                }
                catch (Exception ex)
                {
                    throw new RuntimeException(ex);
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

            try
            {
                // request.complete();

                completeMethod.invoke(request);
            }
            catch (Exception ex)
            {
                log.warn("Error completing comet request", ex);
            }
        }
    }

    /**
     * We want this to compile on Servlet 2.4
     */
    static
    {
        try
        {
            suspendMethod = HttpServletRequest.class.getMethod("suspend");
            completeMethod = HttpServletRequest.class.getMethod("complete");
        }
        catch (Exception ex)
        {
            throw new RuntimeException(ex);
        }
    }

    /**
     * When the server supports servlet 3 ...
     */
    private static final Method suspendMethod;
    private static final Method completeMethod;

    /**
     * The request object that we call suspend/resume on
     */
    private HttpServletRequest request;

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
     * Has the continuation been restarted already?
     */
    protected boolean resumed = false;

    /**
     * The log stream
     */
    private static final Log log = LogFactory.getLog(Servlet3Sleeper.class);
}
