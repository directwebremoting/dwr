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

import java.util.LinkedList;
import java.util.ListIterator;

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
     * @param millis The number of millis to record hits for
     */
    public HitMonitor(int millis)
    {
        this.millis = millis;
    }

    /**
     * A hit has happened, record some load on the server
     */
    public void recordHit()
    {
        synchronized (hitLog)
        {
            Long time = new Long(System.currentTimeMillis());
            hitLog.add(time);
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
            return hitLog.size();
        }
    }

    /**
     * Remove all the hits that are no longer relevant.
     * PERFORMANCE: There is probably a faster way to do this
     */
    private void trimHitLog()
    {
        long now = System.currentTimeMillis();
        long purgeBefore = now - millis;

        ListIterator it = hitLog.listIterator(hitLog.size());
        while (it.hasPrevious())
        {
            Long hitTime = (Long) it.previous();
            if (hitTime.longValue() >= purgeBefore)
            {
                it.remove();
            }
        }
    }

    /**
     * Our log of hits
     */
    private LinkedList hitLog = new LinkedList();

    /**
     * How many milliseconds are we recording the hits for?
     */
    private int millis;
}
