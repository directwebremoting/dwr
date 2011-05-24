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
package org.directwebremoting.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * A wrapper around Jetty and Grizzly Ajax Continuations
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class Continuation
{
    /**
     * Fish the continuation out of the request if it exists.
     * @param request The http request
     */
    public Continuation(HttpServletRequest request)
    {
        Object tempContinuation = null;
        try
        {
            if (isJetty())
            {
                // Get Continuation through Jetty API
                Class<?> jContinuation = LocalUtil.classForName("org.eclipse.jetty.continuation.ContinuationSupport");
                Method jMethod = jContinuation.getMethod("getContinuation", ServletRequest.class);
                tempContinuation = jMethod.invoke(null, request);
            }
            else if (isGrizzly())
            {
                // Get Continuation through Grizzly API
                Class<?> gContinuation = LocalUtil.classForName("com.sun.grizzly.Continuation");
                Method gMethod = gContinuation.getMethod("getContinuation");
                tempContinuation = gMethod.invoke(null, (Object[]) null);
            }
        }
        catch (Throwable ignored)
        {
            log.debug("Throwable caught when trying to get server Continuation", ignored);
        }
        proxy = tempContinuation;
    }

    /**
     * Are continuations working?
     * If this method returns false then all the other methods will fail.
     * @return true if Continuations are working
     */
    public boolean isAvailable()
    {
        return proxy != null;
    }

    /**
     * Suspend the thread for a maximum of sleepTime milliseconds
     * @throws Exception If reflection breaks
     */
    public void suspend() throws Exception
    {
        try
        {
            if (isJetty())
            {
                suspendMethod.invoke(proxy);
            }
            else if (isGrizzly())
            {
                suspendMethod.invoke(proxy, 60000); // Suspend for 1 minute?
            }
        }
        catch (InvocationTargetException ex)
        {
            rethrowWithoutWrapper(ex);
        }
    }

    /**
     * Resume a continuation.
     * For Jetty: does not work like a real continuation because it restarts
     * the http request.
     * @throws Exception If reflection breaks
     */
    public void resume() throws Exception
    {
        try
        {
            if (!((Boolean)isSuspendedMethod.invoke(proxy)).booleanValue()) {
                resumeMethod.invoke(proxy);
            }
        }
        catch (InvocationTargetException ex)
        {
            rethrowWithoutWrapper(ex);
        }
    }

    /**
     * We shouldn't be catching Jetty RetryRequests so we re-throw them.
     * @param th The exception to test for continuation-ness
     */
    public static void rethrowIfContinuation(Throwable th)
    {
        Throwable ex = th;

        if (ex instanceof InvocationTargetException)
        {
            ex = ((InvocationTargetException) ex).getTargetException();
        }

        // Allow Jetty RequestRetry exception to propagate to container!
        if ("org.eclipse.jetty.RetryRequest".equals(ex.getClass().getName()))
        {
            throw (RuntimeException) ex;
        }
    }

    /**
     * Unwrap an InvocationTargetException
     * @param ex The exception to unwrap
     * @return Nothing. This method will not complete normally
     * @throws Exception If reflection breaks
     */
    private static Object rethrowWithoutWrapper(InvocationTargetException ex) throws Exception
    {
        Throwable target = ex.getTargetException();
        if (target instanceof Exception)
        {
            throw (Exception) target;
        }

        if (target instanceof Error)
        {
            throw (Error) target;
        }

        throw ex;
    }

    /**
     * The real continuation object
     */
    private final Object proxy;

    /**
     * The log stream
     */
    private static final Log log = LogFactory.getLog(Continuation.class);

    /**
     * Jetty code used by reflection to allow it to run outside of Jetty
     */
    protected static final Class<?> continuationClass;

    /**
     * How we suspend the continuation
     */
    protected static final Method suspendMethod;

    /**
     * How we resume the continuation
     */
    protected static final Method resumeMethod;

    /**
     * Is this Continuation suspended?
     */
    protected static final Method isSuspendedMethod;

    /**
     * Are we using Jetty at all?
     */
    protected static boolean isJetty = false;

    /**
     * Are we using Grizzly at all?
     */
    protected static boolean isGrizzly = false;

    /**
     * Can we use Jetty/Grizzly?
     */
    static
    {
        Class<?> tempContinuationClass = null;
        try
        {
            try
            {
                tempContinuationClass = LocalUtil.classForName("org.eclipse.jetty.continuation.Continuation");
                isJetty = true;
            }
            catch (Exception ex)
            {
                Class<?> gContinuation = LocalUtil.classForName("com.sun.grizzly.Continuation");
                Method gMethod = gContinuation.getMethod("getContinuation");
                tempContinuationClass = gMethod.invoke(gMethod).getClass();
                isGrizzly = true;
            }
        }
        catch (Exception ex)
        {
            isJetty = false;
            log.debug("No Jetty or Grizzly Continuation class, using standard Servlet API");
        }
        continuationClass = tempContinuationClass;
        if (isJetty())
        {
            suspendMethod = LocalUtil.getMethod(continuationClass, "suspend");
            isSuspendedMethod = LocalUtil.getMethod(continuationClass, "isSuspended");
        }
        else if (isGrizzly)
        {
            suspendMethod = LocalUtil.getMethod(continuationClass, "suspend", long.class);
            isSuspendedMethod = null;
        }
        else
        {
            suspendMethod = null;
            isSuspendedMethod = null;
        }
        resumeMethod = LocalUtil.getMethod(continuationClass, "resume");
    }

    /**
     * @return True if we have detected Jetty classes
     */
    public static boolean isJetty()
    {
        return isJetty;
    }

    /**
     * @return True if we have detected Grizzly classes
     */
    public static boolean isGrizzly()
    {
        return isGrizzly;
    }
}
