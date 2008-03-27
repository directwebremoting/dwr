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
package org.getahead.dwrdemo.gidemo;

import java.util.Date;
import java.util.Random;

/**
 * An object representing the stock price of a corporation.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class Corporation
{
    /**
     * @param jsxid
     * @param name
     * @param last
     * @param change
     */
    public Corporation(String jsxid, String name)
    {
        this.jsxid = jsxid;
        this.name = name;

        last = random.nextFloat() * 100;
        last = Math.round(last * 100) / 100;
        time = new Date();
        change = 0.0F;
        max = last;
        min = last;
    }

    private String jsxid;

    private String name;

    private float last;

    private Date time;

    private float change;

    private float max;

    private float min;

    private Random random = new Random();

    /**
     * @return the jsxid
     */
    public String getJsxid()
    {
        return jsxid;
    }

    /**
     * @return the name
     */
    public String getName()
    {
        return name;
    }

    /**
     * @return the last
     */
    public float getLast()
    {
        return last;
    }

    /**
     * @return the time
     */
    public Date getTime()
    {
        return time;
    }

    /**
     * @return the change
     */
    public float getChange()
    {
        return change;
    }

    /**
     * @return the max
     */
    public float getMax()
    {
        return max;
    }

    /**
     * @return the min
     */
    public float getMin()
    {
        return min;
    }

    /**
     * Alter the stock price
     */
    public void change()
    {
        float newChange = (random.nextFloat() * 4) - 2;
        newChange = Math.round(newChange * 100) / 100;

        if (last + newChange < 9)
        {
            newChange = -newChange;
        }

        change = newChange;
        last += newChange;

        if (last > max)
        {
            max = last;
        }

        if (last < min)
        {
            min = last;
        }

        time = new Date();
    }
}
