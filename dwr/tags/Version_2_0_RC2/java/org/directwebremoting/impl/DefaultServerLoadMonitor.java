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
package org.directwebremoting.impl;

import java.util.Random;

import org.directwebremoting.extend.ServerLoadMonitor;
import org.directwebremoting.util.HitMonitor;
import org.directwebremoting.util.Logger;

/**
 * A default implementation of ServerLoadMonitor
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class DefaultServerLoadMonitor implements ServerLoadMonitor 
{
    /* (non-Javadoc)
     * @see org.directwebremoting.ServerLoadMonitor#timeWithinPoll()
     */
    public long getPreStreamWaitTime()
    {
        return preStreamWaitTime;
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.ServerLoadMonitor#timeWithinPoll()
     */
    public long getPostStreamWaitTime()
    {
        return postStreamWaitTime;
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.ServerLoadMonitor#timeToNextPoll()
     */
    public int getTimeToNextPoll()
    {
        hitMonitor.recordHit();
        checkLoading();

        return random.nextInt(timeToNextPoll);
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.ServerLoadMonitor#threadWaitStarting()
     */
    public void threadWaitStarting()
    {
        waitingThreads++;
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.ServerLoadMonitor#threadWaitEnding()
     */
    public void threadWaitEnding()
    {
        waitingThreads--;
    }

    /**
     * Check that we are setting the time to next poll correctly.
     */
    private void checkLoading()
    {
        int hitsPerSecond = hitMonitor.getHitsInLastPeriod() / SECONDS_MONITORED;
        if (hitsPerSecond == 0)
        {
            timeToNextPoll = 0;
        }
        else
        {
            timeToNextPoll = (timeToNextPoll * maxPollHitsPerSecond / hitsPerSecond);
        }

        if (timeToNextPoll < MIN_TIME_TO_NEXT_POLL)
        {
            timeToNextPoll = MIN_TIME_TO_NEXT_POLL;
        }

        int totalPollTime = preStreamWaitTime + postStreamWaitTime;
        if (waitingThreads != 0)
        {
            totalPollTime = totalPollTime * maxWaitingThreads / waitingThreads;
        }

        if (totalPollTime > MAX_PRE_STREAM_WAIT_TIME + MAX_POST_STREAM_WAIT_TIME)
        {
            preStreamWaitTime = totalPollTime - MAX_POST_STREAM_WAIT_TIME;
            postStreamWaitTime = MAX_POST_STREAM_WAIT_TIME;

            if (preStreamWaitTime> MAX_PRE_STREAM_WAIT_TIME)
            {
                preStreamWaitTime = MAX_PRE_STREAM_WAIT_TIME;
            }
        }
        else
        {
            preStreamWaitTime = 0;
            postStreamWaitTime = totalPollTime;
        }

        log.debug("hitsPerSecond=" + hitsPerSecond);
        log.debug("waitingThreads=" + waitingThreads);
        log.debug("timeToNextPoll=" + timeToNextPoll);
        log.debug("preStreamWaitTime=" + preStreamWaitTime);
        log.debug("postStreamWaitTime=" + postStreamWaitTime);
    }

    /**
     * We ask clients to wait a random number of millis before they come
     * back to avoid killing the server
     */
    protected Random random = new Random();

    /**
     * What is the maximium number of threads we keep waiting.
     * We reduce the timeWithinPoll*Stream variables to reduce the load
     */
    protected int maxWaitingThreads = 100;

    /**
     * What is the maximum number of hits we should get per second.
     * We increase the poll time to compensate and reduce the load
     */
    protected int maxPollHitsPerSecond = 40;

    /**
     * The max time we wait before opening a stream to reply.
     */
    protected int preStreamWaitTime = 29000;

    /**
     * The max time we wait after opening a stream before we reply.
     */
    protected int postStreamWaitTime = 1000;

    /**
     * How long are we telling users to wait before they come back next
     */
    protected int timeToNextPoll = 3000;

    /**
     * What is the shortest (random seed) time we wait before next request
     */
    protected static final int MIN_TIME_TO_NEXT_POLL = 3000;

    /**
     * What is the longest we wait for extra input after detecting output
     */
    protected static final int MAX_PRE_STREAM_WAIT_TIME = 29000;

    /**
     * What is the longest we wait for extra input after detecting output
     */
    protected static final int MAX_POST_STREAM_WAIT_TIME = 1000;

    /**
     * We are recording the number of hits in the last 5 seconds.
     * Maybe we should think about making this configurable.
     */
    protected static final int SECONDS_MONITORED = 5;

    /**
     * Our record of the server loading
     */
    protected HitMonitor hitMonitor = new HitMonitor(SECONDS_MONITORED * 1000);

    /**
     * How many sleepers are there?
     */
    protected int waitingThreads = 0;

    /**
     * The log stream
     */
    private static final Logger log = Logger.getLogger(DefaultServerLoadMonitor.class);
}
