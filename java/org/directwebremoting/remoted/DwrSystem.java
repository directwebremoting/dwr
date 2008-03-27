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
package org.directwebremoting.remoted;

import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.directwebremoting.Container;
import org.directwebremoting.ScriptSession;
import org.directwebremoting.ServerLoadMonitor;
import org.directwebremoting.WebContext;
import org.directwebremoting.WebContextFactory;
import org.directwebremoting.util.LocalUtil;
import org.directwebremoting.util.Logger;

/**
 * A collection of system level actions that can be called remotely.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class DwrSystem
{
    /**
     * The polling system needs to be able to wait for something to happen
     * @return How long should the client wait until it next polls
     */
    public int poll()
    {
        WebContext context = WebContextFactory.get();

        Container container = context.getContainer();
        ServerLoadMonitor monitor = (ServerLoadMonitor) container.getBean(ServerLoadMonitor.class.getName());
        long sleepTime = monitor.timeWithinPoll();

        // If this is Jetty then we can use Continuations
        HttpServletResponse response = context.getHttpServletResponse();
        if (response.getClass().getName().equals("org.mortbay.util.ajax.AjaxFilter.AjaxResponse")) //$NON-NLS-1$
        {
            try
            {
                HttpServletRequest request = context.getHttpServletRequest();
                ScriptSession scriptSession = context.getScriptSession();

                // Continuation continuation = ContinuationSupport.getContinuation(request, true);
                Object continuation = getContinuationMethod.invoke(null, new Object[] { request, Boolean.TRUE });
                if (continuation != null)
                {
                    scriptSession.setAttribute("org.mortbay.util.ajax.Continuation", continuation); //$NON-NLS-1$

                    // continuation.suspend(sleepTime);
                    suspendMethod.invoke(continuation, new Object[] { new Long(sleepTime) });

                    // The example does the following ... Why?
                    // Object event= continuation.getEvent(timeoutMS);
                }

                // The example does the following ... Why?
                // Signal for a new poll
                // response.objectResponse("poll", "");
            }
            catch (Exception ex)
            {
                // If continuations fails, we must just fall back to polling
            }
        }
        else
        {
            // The comet part of a poll request
            try
            {
                Thread.sleep(sleepTime);
            }
            catch (InterruptedException ex)
            {
                log.warn("Interupted", ex); //$NON-NLS-1$
            }
        }

        return monitor.timeToNextPoll();
    }

    /**
     * Jetty code used by reflection to allow it to run outside of Jetty
     */
    private static Class continuationClass;

    /**
     * Jetty code used by reflection to allow it to run outside of Jetty
     */
    private static Class continuationSupportClass;

    /**
     * How we get get a Continuation object
     */
    private static Method getContinuationMethod;

    /**
     * How we suspend the continuation
     */
    private static Method suspendMethod;

    /**
     * The log stream
     */
    private static final Logger log = Logger.getLogger(DwrSystem.class);

    /**
     * Can we use Jetty?
     */
    static
    {
        try
        {
            continuationClass = LocalUtil.classForName("org.mortbay.util.ajax.Continuation"); //$NON-NLS-1$
            continuationSupportClass = LocalUtil.classForName("org.mortbay.util.ajax.ContinuationSupport"); //$NON-NLS-1$
            getContinuationMethod = continuationSupportClass.getMethod("getContinuation", new Class[] { HttpServletRequest.class, Boolean.class }); //$NON-NLS-1$
            suspendMethod = continuationClass.getMethod("suspend", new Class[] { Long.TYPE }); //$NON-NLS-1$
        }
        catch (Exception ex)
        {
            log.debug("No Jetty ContuniationSupport class, using standard Servlet API"); //$NON-NLS-1$
        }
    }
}
