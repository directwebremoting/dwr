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
        this.proxy = request.getAttribute(ATTRIBUTE_JETTY_CONTINUATION);
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
            suspendMethod.invoke(proxy, new Object[] { new Long(sleepTime) });
        }
        catch (InvocationTargetException ex)
        {
            rethrowWithoutWrapper(ex);
        }
    }

    /**
     * Resume an continuation.
     * For Jetty this does not work like a real continuation because it restarts
     * the http request.
     * @throws Exception If reflection breaks
     */
    public void resume() throws Exception
    {
        try
        {
            resumeMethod.invoke(proxy, new Object[0]);
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
            return getObject.invoke(proxy, new Object[0]);
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
            setObject.invoke(proxy, new Object[] { object });
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

    private Object proxy;

    /**
     * The log stream
     */
    private static final Logger log = Logger.getLogger(Continuation.class);

    /**
     * The attribute under which Jetty stores it's Contuniations.
     * TODO: This feels like a mighty hack. I hope Greg doesn't change it without telling us!
     */
    private static final String ATTRIBUTE_JETTY_CONTINUATION = "org.mortbay.jetty.ajax.Continuation";

    /**
     * Jetty code used by reflection to allow it to run outside of Jetty
     */
    protected static Class continuationClass;

    /**
     * How we suspend the continuation
     */
    protected static Method suspendMethod;

    /**
     * How we resume the continuation
     */
    protected static Method resumeMethod;

    /**
     * How we get the associated continuation object
     */
    protected static Method getObject;

    /**
     * How we set the associated continuation object
     */
    protected static Method setObject;

    /**
     * Can we use Jetty?
     */
    static
    {
        try
        {
            continuationClass = LocalUtil.classForName("org.mortbay.util.ajax.Continuation");
            suspendMethod = continuationClass.getMethod("suspend", new Class[] { Long.TYPE });
            resumeMethod = continuationClass.getMethod("resume", new Class[] {});
            getObject = continuationClass.getMethod("getObject", new Class[] {});
            setObject = continuationClass.getMethod("setObject", new Class[] { Object.class });
        }
        catch (Exception ex)
        {
            log.debug("No Jetty ContuniationSupport class, using standard Servlet API");
        }
    }
}
