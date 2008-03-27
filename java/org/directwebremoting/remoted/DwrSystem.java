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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;

import org.directwebremoting.Container;
import org.directwebremoting.ScriptConduit;
import org.directwebremoting.ScriptSession;
import org.directwebremoting.ServerLoadMonitor;
import org.directwebremoting.WebContext;
import org.directwebremoting.WebContextFactory;
import org.directwebremoting.util.ContinuationUtil;
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

        ScriptSession scriptSession = context.getScriptSession();

        // The comet part of a poll request
        try
        {
            long sleepTime = monitor.timeWithinPollPostStream();
            if (sleepTime > 0)
            {
                // flush any scripts already written.
                scriptSession.flushConduits();
                Thread.sleep(sleepTime);
            }
        }
        catch (InterruptedException ex)
        {
            log.warn("Interupted", ex); //$NON-NLS-1$
        }

        return monitor.timeToNextPoll();
    }

    /**
     * The polling system needs to be able to wait for something to happen
     */
    public void pollPreStreamWait()
    {
        WebContext context = WebContextFactory.get();
        Container container = context.getContainer();

        ScriptSession scriptSession = context.getScriptSession();
        ServerLoadMonitor monitor = (ServerLoadMonitor) container.getBean(ServerLoadMonitor.class.getName());
        long sleepTime = monitor.timeWithinPollPreStream();

        synchronized (scriptSession.getScriptLock())
        {
            // Don't wait if there are queued scripts
            if (scriptSession.getQueuedScripts() > 0)
            {
                return;
            }

            // If this is Jetty then we can use Continuations
            HttpServletRequest request = context.getHttpServletRequest();

            boolean useSleep = true;
            Object continuation = request.getAttribute(ATTRIBUTE_JETTY_CONTINUATION);
            if (continuation != null)
            {
                useSleep = false;
                ScriptConduit listener = null;

                try
                {
                    // create a listener 
                    listener = (ScriptConduit) getObject.invoke(continuation, new Object[0]);
                    if (listener == null)
                    {
                        listener = new ResumeContinuationScriptConduit(continuation);
                        setObject.invoke(continuation, new Object[] { listener });
                    }
                    scriptSession.addScriptConduit(listener);

                    // continuation.suspend(sleepTime);
                    // NB. May throw a Runtime exception that must propogate to the container!
                    suspendMethod.invoke(continuation, new Object[] { new Long(sleepTime) });
                }
                catch (InvocationTargetException ex)
                {
                    ContinuationUtil.rethrowIfContinuation(ex.getTargetException());

                    log.warn("Error in Reflection", ex.getTargetException()); //$NON-NLS-1$
                    useSleep = true;
                }
                catch (Exception ex)
                {
                    log.warn("Exception", ex); //$NON-NLS-1$
                    useSleep = true;
                }
                finally
                {
                    if (listener != null)
                    {
                        scriptSession.removeScriptConduit(listener);
                    }
                }
            }

            if (useSleep)
            {
                Object lock = scriptSession.getScriptLock();
                // TODO avoid the expense of creation and registration
                ScriptConduit listener = new NotifyOnlyScriptConduit(lock);

                // The comet part of a poll request
                try
                {
                    scriptSession.addScriptConduit(listener);

                    scriptSession.getScriptLock().wait(sleepTime);
                }
                catch (InterruptedException ex)
                {
                    log.warn("Interupted", ex); //$NON-NLS-1$
                }
                finally
                {
                    scriptSession.removeScriptConduit(listener);
                }
            }
        }
    }

    /**
     * Implementation of ScriptConduit that simply calls <code>notifyAll()</code>
     * if a script is added.
     * No actual script adding is done here.
     * Useful in conjunction with a preStreamWait to
     */
    private static final class NotifyOnlyScriptConduit implements ScriptConduit
    {
        /**
         * @param lock Object to wait and notifyAll with
         */
        protected NotifyOnlyScriptConduit(Object lock)
        {
            this.lock = lock;
        }

        /* (non-Javadoc)
         * @see org.directwebremoting.ScriptConduit#addScript(java.lang.String)
         */
        public boolean addScript(String script)
        {
            try
            {
                synchronized (lock)
                {
                    lock.notifyAll();
                }
            }
            catch (Exception ex)
            {
                log.warn("Failed to notify all ScriptSession users", ex); //$NON-NLS-1$
            }

            // We havn't done anything with the script, so
            return false;
        }

        /* (non-Javadoc)
         * @see org.directwebremoting.ScriptConduit#flush()
         */
        public void flush()
        {
        }

        private final Object lock;
    }

    /**
     * Implementaion of ScriptConduit that just resumes a continuation.
     */
    private static final class ResumeContinuationScriptConduit implements ScriptConduit
    {
        /**
         * @param continuation
         */
        protected ResumeContinuationScriptConduit(Object continuation)
        {
            this.continuation = continuation;
        }

        /* (non-Javadoc)
         * @see org.directwebremoting.ScriptConduit#addScript(java.lang.String)
         */
        public boolean addScript(String script)
        {
            try
            {
                // continuation.resume();
                resumeMethod.invoke(continuation, new Object[0]);
            }
            catch (Exception ex)
            {
                log.warn("Exception in continuation.resume()", ex); //$NON-NLS-1$
            }

            // never actually handle the script!
            return false;
        }

        /* (non-Javadoc)
         * @see org.directwebremoting.ScriptConduit#flush()
         */
        public void flush()
        {
        }

        private final Object continuation;
    }

    /**
     * The attribute under which Jetty stores it's Contuniations.
     * TODO: This feels like a mighty hack. I hope Greg doesn't change it without telling us!
     */
    private static final String ATTRIBUTE_JETTY_CONTINUATION = "org.mortbay.jetty.ajax.Continuation"; //$NON-NLS-1$

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
     * The log stream
     */
    protected static final Logger log = Logger.getLogger(DwrSystem.class);

    /**
     * Can we use Jetty?
     */
    static
    {
        try
        {
            continuationClass = LocalUtil.classForName("org.mortbay.util.ajax.Continuation"); //$NON-NLS-1$
            suspendMethod = continuationClass.getMethod("suspend", new Class[] { Long.TYPE }); //$NON-NLS-1$
            resumeMethod = continuationClass.getMethod("resume", new Class[] {}); //$NON-NLS-1$
            getObject = continuationClass.getMethod("getObject", new Class[] {}); //$NON-NLS-1$
            setObject = continuationClass.getMethod("setObject", new Class[] { Object.class }); //$NON-NLS-1$
        }
        catch (Exception ex)
        {
            log.debug("No Jetty ContuniationSupport class, using standard Servlet API"); //$NON-NLS-1$
        }
    }
}
