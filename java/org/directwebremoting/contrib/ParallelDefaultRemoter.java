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
package org.directwebremoting.contrib;

import org.directwebremoting.extend.Call;
import org.directwebremoting.extend.Calls;
import org.directwebremoting.extend.Replies;
import org.directwebremoting.extend.Reply;
import org.directwebremoting.impl.DefaultRemoter;
import org.directwebremoting.util.Logger;

import edu.emory.mathcs.backport.java.util.concurrent.Callable;
import edu.emory.mathcs.backport.java.util.concurrent.ExecutionException;
import edu.emory.mathcs.backport.java.util.concurrent.Executors;
import edu.emory.mathcs.backport.java.util.concurrent.Future;
import edu.emory.mathcs.backport.java.util.concurrent.ThreadPoolExecutor;
import edu.emory.mathcs.backport.java.util.concurrent.TimeUnit;
import edu.emory.mathcs.backport.java.util.concurrent.TimeoutException;

/**
 * This implementation is not officially supported, and may be removed
 * at some point in the future.
 * Remoter implementation executing in parallel a group of remote calls.
 * @author <a href="mailto:chussenet@yahoo.com">Claude Hussenet</a>
 */
public class ParallelDefaultRemoter extends DefaultRemoter
{
    class OneCall implements Callable
    {
        private Call call;

        /**
         * @param call The call to execute
         */
        public OneCall(Call call)
        {
            this.call = call;
        }

        public Object call()
        {
            return execute(call);
        }
    }

    /**
     * Initialize thread pool with :
     * Core pool size : 10;
     * Maximum pool size = 100;
     * Keep alive time = 5000(ms);
     * Timeout = 10000(ms);
     */
    public ParallelDefaultRemoter()
    {
        executorService = (ThreadPoolExecutor) Executors.newCachedThreadPool();
        executorService.setCorePoolSize(corePoolsize);
        executorService.setMaximumPoolSize(maximumPoolsize);
        executorService.setKeepAliveTime(keepAliveTime, TimeUnit.MILLISECONDS);

        log.info(executorService.getClass().getName().indexOf("edu.emory.mathcs.backport") > -1 ? "Backport of java.util.concurrent package used !" : "java.util.concurrent package used !");
    }

    /**
     * Sets the maximum time to wait in (ms)
     * @param timeout Time in (ms)
     */
    public void setParallelDefaultRemoterTimeout(long timeout)
    {
        this.timeout = timeout;
    }

    /**
     * Sets the core number of threads.
     * @param corePoolsize How many threads do we use
     */
    public void setParallelDefaultRemoterCorePoolsize(int corePoolsize)
    {
        this.corePoolsize = corePoolsize;
        executorService.setCorePoolSize(corePoolsize);
    }

    /**
     * Sets the maximum allowed number of threads.
     * @param maximumPoolsize Maximum of threads
     */
    public void setParallelDefaultRemoterMaximumPoolsize(int maximumPoolsize)
    {
        this.maximumPoolsize = maximumPoolsize;
        executorService.setMaximumPoolSize(maximumPoolsize);
    }

    /**
     * Sets the time limit in (ms) for which threads may remain idle before being
     * terminated.
     * @param keepAliveTime Time in (ms)
     */
    public void setParallelDefaultRemoterKeepAliveTime(long keepAliveTime)
    {
        this.keepAliveTime = keepAliveTime;
        executorService.setKeepAliveTime(keepAliveTime, TimeUnit.MILLISECONDS);
    }

    /**
     * Execute a set of remote calls in parallel and generate set of reply data
     * for later conversion to whatever wire protocol we are using today.
     * @param calls The set of calls to execute in parallel
     * @return A set of reply data objects
     */
    public Replies execute(Calls calls)
    {
        Replies replies = new Replies(calls.getBatchId());
        Future future[] = new Future[calls.getCallCount()];

        if (calls.getCallCount() == 1)
        {
            return super.execute(calls);
        }
        else
        {
            for (int callNum = 0; callNum < calls.getCallCount(); callNum++)
            {
                Call call = calls.getCall(callNum);
                future[callNum] = executorService.submit(new OneCall(call));
            }
            for (int callNum = 0; callNum < calls.getCallCount(); callNum++)
            {
                try
                {
                    Reply reply = (Reply) future[callNum].get(this.timeout, TimeUnit.MILLISECONDS);
                    replies.addReply(reply);
                }
                catch (InterruptedException ex)
                {
                    log.warn("Method execution failed: ", ex);
                    replies.addReply(new Reply(calls.getCall(callNum).getCallId(), null, ex));
                }
                catch (ExecutionException ex)
                {
                    log.warn("Method execution failed: ", ex);
                    replies.addReply(new Reply(calls.getCall(callNum).getCallId(), null, ex));
                }
                catch (TimeoutException ex)
                {
                    log.warn("Method execution failed: ", ex);
                    replies.addReply(new Reply(calls.getCall(callNum).getCallId(), null, ex));
                }
            }
            return replies;
        }
    }

    private static final Logger log = Logger.getLogger(ParallelDefaultRemoter.class);

    private int corePoolsize = 10;

    private int maximumPoolsize = 100;

    private long keepAliveTime = 5000;

    private long timeout = 10000;

    private ThreadPoolExecutor executorService;
}
