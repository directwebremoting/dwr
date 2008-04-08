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

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;

/**
 * A wrapper around Jetty Ajax Continuations
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class Continuation
{
    /**
     * Fish the Jetty continuation out of the request if it exists
     * @param request The http request
     */
    public Continuation(HttpServletRequest request)
    {
        proxy = request.getAttribute(ATTRIBUTE_JETTY_CONTINUATION);
        if (proxy == null && isGrizzly())
        {
            try
            {
                Class<?> gContinuation = LocalUtil.classForName("com.sun.grizzly.Continuation");
                Method gMethod = gContinuation.getMethod("getContinuation");
                proxy = gMethod.invoke((Object[])null,(Object[])null);
            }
            catch (Throwable ex)
            {
            }
        }
    }

    /**
     * Are continuations working?
     * If this method returns false then all the other methods will fail.
     * @return true if Jetty continuations are working
     */
    public boolean isAvailable()
    {
        return proxy != null;
    }

    /**
     * Suspend the thread for a maximum of sleepTime milliseconds
     * @param sleepTime The maximum time to wait
     * @throws Exception If reflection breaks
     */
    public void suspend(long sleepTime) throws Exception
    {
        try
        {
            suspendMethod.invoke(proxy, sleepTime);
        }
        catch (InvocationTargetException ex)
        {
            rethrowWithoutWrapper(ex);
        }
    }

    /**
     * Resume an continuation.
     * For Jetty: does not work like a real continuation because it restarts
     * the http request.
     * @throws Exception If reflection breaks
     */
    public void resume() throws Exception
    {
        try
        {
            resumeMethod.invoke(proxy);
        }
        catch (InvocationTargetException ex)
        {
            rethrowWithoutWrapper(ex);
        }
    }

    /**
     * Accessor for the object associated with this continuation
     * @return the object associated with this continuation
     * @throws Exception If reflection breaks
     */
    public Object getObject() throws Exception
    {
        try
        {
            return getObject.invoke(proxy);
        }
        catch (InvocationTargetException ex)
        {
            return rethrowWithoutWrapper(ex);
        }
    }

    /**
     * Accessor for the object associated with this continuation
     * @param object the object associated with this continuation
     * @throws Exception If reflection breaks
     */
    public void setObject(Object object) throws Exception
    {
        try
        {
            setObject.invoke(proxy, object);
        }
        catch (InvocationTargetException ex)
        {
            rethrowWithoutWrapper(ex);
        }
    }

    /**
     * We shouldn't be catching Jetty RetryRequests so we rethrow them.
     * @param th The exception to test for continuation-ness
     */
    public static void rethrowIfContinuation(Throwable th)
    {
        Throwable ex = th;

        if (ex instanceof InvocationTargetException)
        {
            ex = ((InvocationTargetException) ex).getTargetException();
        }

        // Allow Jetty RequestRetry exception to propogate to container!
        if ("org.mortbay.jetty.RetryRequest".equals(ex.getClass().getName()))
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
    private Object proxy;

    /**
     * The log stream
     */
    private static final Log log = LogFactory.getLog(Continuation.class);

    /**
     * The attribute under which Jetty stores it's Contuniations.
     * TODO: This feels like a mighty hack. I hope Greg doesn't change it without telling us!
     */
    private static final String ATTRIBUTE_JETTY_CONTINUATION = "org.mortbay.jetty.ajax.Continuation";

    /**
     * Jetty code used by reflection to allow it to run outside of Jetty
     */
    protected static Class<?> continuationClass = null;

    /**
     * How we suspend the continuation
     */
    protected static Method suspendMethod = null;

    /**
     * How we resume the continuation
     */
    protected static Method resumeMethod = null;

    /**
     * How we get the associated continuation object
     */
    protected static Method getObject = null;

    /**
     * How we set the associated continuation object
     */
    protected static Method setObject = null;

    /**
     * Are we using Jetty at all?
     */
    protected static boolean isJetty = false;
    
    /**
     * Are we using Grizzly at all?
     */
    protected static boolean isGrizzly = false;

    /**
     * Can we use Jetty?
     */
    static
    {
        try
        {
            try
            {
                continuationClass = LocalUtil.classForName("org.mortbay.util.ajax.Continuation");
                isJetty = true;
            } 
            catch (Exception ex) 
            {
                Class<?> gContinuation = LocalUtil.classForName("com.sun.grizzly.Continuation");
                Method gMethod = gContinuation.getMethod("getContinuation");
                continuationClass = gMethod.invoke(gMethod).getClass();
                isGrizzly = true;
            }

            suspendMethod = continuationClass.getMethod("suspend", Long.TYPE);
            resumeMethod = continuationClass.getMethod("resume");
            getObject = continuationClass.getMethod("getObject");
            setObject = continuationClass.getMethod("setObject", Object.class);
        }
        catch (Exception ex)
        {
            isJetty = false;
            log.debug("No Jetty or Grizzly Continuation class, using standard Servlet API");
        }
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
