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

/**
 * We need a way to record how heavily used the server is, and adjust our
 * behavior to reduce the load on the server.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class HitMonitor
{
    /**
     * Create a HitMonitor that records the number of hits in the last n
     * milliseconds.
     * @param seconds The number of seconds to record hits for
     */
    public HitMonitor(int seconds)
    {
        hitLog = new long[seconds];
    }

    /**
     * A hit has happened, record some load on the server
     */
    public void recordHit()
    {
        synchronized (hitLog)
        {
            trimHitLog();
            hitLog[0]++;
        }
    }

    /**
     * How to detect the number of hits in the time period specified in the
     * constructor.
     * @return The hit count
     */
    public int getHitsInLastPeriod()
    {
        synchronized (hitLog)
        {
            trimHitLog();

            int count = 0;
            for (int i = 0; i < hitLog.length; i++)
            {
                count += hitLog[i];
            }

            return count;
        }
    }

    /**
     * Remove all the hits that are no longer relevant.
     * PERFORMANCE: There is probably a faster way to do this
     */
    private void trimHitLog()
    {
        long now = getCurrentTimestamp();
        int secondsPassedSinceLastHit = (int) (now - zeroTimestamp);
        zeroTimestamp = now;

        if (secondsPassedSinceLastHit > 0)
        {
            // Move the counts down
            for (int i = hitLog.length - 1; i >= 0; i--)
            {
                if (i >= secondsPassedSinceLastHit)
                {
                    hitLog[i] = hitLog[i - secondsPassedSinceLastHit];
                }
                else
                {
                    hitLog[i] = 0;
                }
            }
        }
    }

    /**
     * A timestamp is {@link System#currentTimeMillis()} divided by 1000
     * @return The current timestamp
     */
    private long getCurrentTimestamp()
    {
        return System.currentTimeMillis() / 1000;
    }

    /**
     * What is the timestamp of the first element of the hitLog?
     */
    private long zeroTimestamp = getCurrentTimestamp();

    /**
     * Our log of hits
     */
    private final long[] hitLog;
}
