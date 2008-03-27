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

import org.directwebremoting.Container;
import org.directwebremoting.ScriptConduit;
import org.directwebremoting.ScriptSession;
import org.directwebremoting.ServerLoadMonitor;
import org.directwebremoting.WebContext;
import org.directwebremoting.WebContextFactory;
import org.directwebremoting.util.Continuation;
import org.directwebremoting.util.Logger;

/**
 * Utilities to help PollHandler
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class PollUtil
{
    /**
     * The polling system needs to be able to wait for something to happen
     * @param partialResponse Does the XHR.responseText object do partial responses
     */
    public static void preStreamWait(boolean partialResponse)
    {
        WebContext context = WebContextFactory.get();
        Container container = context.getContainer();

        ScriptSession scriptSession = context.getScriptSession();
        ServerLoadMonitor monitor = (ServerLoadMonitor) container.getBean(ServerLoadMonitor.class.getName());
        long preStreamWaitTime = monitor.getPreStreamWaitTime();

        // If the browser can't handle partial responses then we'll need to do
        // all our waiting in the pre-stream phase where we plan to be interupted
        if (!partialResponse)
        {
            long postStreamWaitTime = monitor.getPostStreamWaitTime();
            preStreamWaitTime += postStreamWaitTime;
        }

        Object lock = scriptSession.getScriptLock();
        synchronized (lock)
        {
            // Don't wait if there are queued scripts
            if (scriptSession.hasWaitingScripts())
            {
                return;
            }

            // If this is Jetty then we can use Continuations
            HttpServletRequest request = context.getHttpServletRequest();

            boolean useSleep = true;
            Continuation continuation = new Continuation(request);
            if (continuation.isAvailable())
            {
                useSleep = false;
                ScriptConduit listener = null;

                try
                {
                    // create a listener
                    listener = (ScriptConduit) continuation.getObject();
                    if (listener == null)
                    {
                        listener = new ResumeContinuationScriptConduit(continuation);
                        continuation.setObject(listener);
                    }
                    scriptSession.addScriptConduit(listener);

                    // JETTY: throws a RuntimeException that must propogate to the container!
                    continuation.suspend(preStreamWaitTime);
                }
                catch (Exception ex)
                {
                    Continuation.rethrowIfContinuation(ex);
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
                // TODO avoid the expense of creation and registration
                ScriptConduit listener = new NotifyOnlyScriptConduit(lock);

                // The comet part of a poll request
                try
                {
                    scriptSession.addScriptConduit(listener);

                    try
                    {
                        lock.wait(preStreamWaitTime);
                    }
                    catch (InterruptedException ex)
                    {
                        log.warn("Interupted", ex); //$NON-NLS-1$
                    }
                }
                finally
                {
                    scriptSession.removeScriptConduit(listener);
                }
            }
        }
    }

    /**
     * The polling system needs to be able to wait for something to happen.
     * @param partialResponse Does the XHR.responseText object do partial responses
     */
    public static void postStreamWait(boolean partialResponse)
    {
        WebContext context = WebContextFactory.get();
        Container container = context.getContainer();

        ServerLoadMonitor monitor = (ServerLoadMonitor) container.getBean(ServerLoadMonitor.class.getName());

        // The comet part of a poll request
        long postStreamWaitTime = monitor.getPostStreamWaitTime();
        if (postStreamWaitTime > 0)
        {
            // Flush any scripts already written.
            // This is a bit of a broad brush: We only really need to flush the
            // conduit that is part of this response, but is there any harm
            // in flushing too many?
            //ScriptSession scriptSession = context.getScriptSession();
            //scriptSession.flushConduits();

            // This is the same as Thread.sleep() except that this allows us to
            // keep track of how many people are waiting to control server load
            try
            {
                // If the browser can't handle partialResponses then we need to
                // reply quickly. 100ms should be enough for any work being done
                // on other threads to complete. If not it can wait.
                if (!partialResponse)
                {
                    postStreamWaitTime = 100;
                }

                Thread.sleep(postStreamWaitTime);
            }
            catch (InterruptedException ex)
            {
                log.warn("Interupted", ex); //$NON-NLS-1$
            }
        }
    }

    /**
     * Implementation of ScriptConduit that simply calls <code>notifyAll()</code>
     * if a script is added.
     * No actual script adding is done here.
     * Useful in conjunction with a preStreamWait to
     */
    private static final class NotifyOnlyScriptConduit extends ScriptConduit
    {
        /**
         * @param lock Object to wait and notifyAll with
         */
        protected NotifyOnlyScriptConduit(Object lock)
        {
            super(RANK_PROCEDURAL);
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

            // We have not done anything with the script, so
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
    private static final class ResumeContinuationScriptConduit extends ScriptConduit
    {
        /**
         * @param continuation
         */
        protected ResumeContinuationScriptConduit(Continuation continuation)
        {
            super(RANK_PROCEDURAL);
            this.continuation = continuation;
        }

        /* (non-Javadoc)
         * @see org.directwebremoting.ScriptConduit#addScript(java.lang.String)
         */
        public boolean addScript(String script)
        {
            try
            {
                continuation.resume();
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

        /**
         * The Jetty continuation
         */
        private final Continuation continuation;
    }

    /**
     * The log stream
     */
    protected static final Logger log = Logger.getLogger(PollUtil.class);
}
