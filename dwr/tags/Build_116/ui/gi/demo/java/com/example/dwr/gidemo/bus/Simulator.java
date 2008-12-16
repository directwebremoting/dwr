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
package com.example.dwr.gidemo.bus;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.directwebremoting.Browser;
import org.directwebremoting.HubFactory;

/**
 *
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class Simulator implements Runnable
{
    /**
     *
     */
    public Simulator()
    {
        setActive(true);
    }

    /* (non-Javadoc)
     * @see java.lang.Runnable#run()
     */
    public void run()
    {
        try
        {
            log.info("Simulator: Starting server-side thread");

            while (active)
            {
                Browser.withPage("/dwr-gi/dwr_oa_gi.html", new Runnable()
                {
                    public void run()
                    {
                        objectHolder.put("obj", getRandomObject());
                        HubFactory.get().publish("cdf.object", objectHolder);
                    }
                });

                Thread.sleep(random.nextInt(500));
            }

            log.info("Simulator: Stopping server-side thread");
        }
        catch (InterruptedException ex)
        {
            ex.printStackTrace();
        }
    }

    /**
     *
     */
    private Map<String, String> getRandomObject()
    {
        Map<String, String> reply = new HashMap<String, String>();

        reply.put("jsxid", "" + random.nextInt(10));

        for (int i = 1; i <= attributesPerRecord; i++)
        {
            reply.put("C" + i, getRandomNumberString());
        }

        return reply;
    }

    /**
     *
     */
    private String getRandomNumberString()
    {
        float base = random.nextInt(100000);
        base = base / 100;
        return String.valueOf(base);
    }

    public int getAttributesPerRecord()
    {
        return attributesPerRecord;
    }

    public void setAttributesPerRecord(int attributesPreRecord)
    {
        this.attributesPerRecord = attributesPreRecord;
    }

    public int getPublishesPerSecond()
    {
        return publishesPerSecond;
    }

    public void setPublishesPerSecond(int publishesPerSecond)
    {
        this.publishesPerSecond = publishesPerSecond;
    }

    /**
     *
     */
    public synchronized void setActive(boolean active)
    {
        if (active == this.active)
        {
            return;
        }

        this.active = active;
        log.warn("Simulator: Setting active state to: " + active);

        if (this.active)
        {
            new Thread(this).start();
        }
    }

    public boolean isActive()
    {
        return active;
    }

    private final Random random = new Random();

    private int attributesPerRecord = 5;

    private int publishesPerSecond = 1;

    private boolean active = false;

    private final Map<String, Map<String, String>> objectHolder = new HashMap<String, Map<String,String>>();

    /**
     * The log stream
     */
    private static final Log log = LogFactory.getLog(Simulator.class);
}
