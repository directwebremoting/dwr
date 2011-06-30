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
package com.example.dwr.reverseajax;

import java.util.Calendar;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.directwebremoting.Browser;
import org.directwebremoting.ServerContextFactory;
import org.directwebremoting.impl.DaemonThreadFactory;
import org.directwebremoting.ui.dwr.Util;

/**
 * A server-side clock that broadcasts the server time to any browsers that will
 * listen.
 * This is an example of how to control clients using server side threads
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class ClockMS implements Runnable
{

    /**
     * Create a schedule to update the clock every second.
     */
    public ClockMS()
    {
        ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(1, new DaemonThreadFactory());
        executor.scheduleAtFixedRate(this, 1, 50, TimeUnit.MILLISECONDS);
    }

    /* (non-Javadoc)
     * @see java.lang.Runnable#run()
     */
    public void run()
    {
        if (active)
        {
            Calendar cal = Calendar.getInstance();
            int ms = cal.get(Calendar.MILLISECOND);
            String newTimeString = cal.getTime().toString() + " MS:" + (ms - (ms % 250));
            // We check this has not already been sent to avoid duplicate transmissions
            if (!newTimeString.equals(timeString))
            {
                setClockDisplay(newTimeString);
                timeString = newTimeString;
            }
        }
    }

    /**
     * Called from the client to turn the clock on/off
     */
    public synchronized void toggle()
    {
        active = !active;

        if (active)
        {
            setClockDisplay("Started");
        }
        else
        {
            setClockDisplay("Stopped");
        }
    }

    /**
     * Actually alter the clients.
     * In DWR 2.x you had to know the ServletContext in order to be able to get
     * a ServerContext. With DWR 3.0 this restriction has been removed.
     * This method is public so you can call this from the dwr auto-generated
     * pages to demo altering one page from another
     * @param output The string to display.
     */
    public void setClockDisplay(final String output)
    {
        String page = ServerContextFactory.get().getContextPath() + "/reverseajax/clock_logging_ms.html";
        Browser.withPage(page, new Runnable()
        {
            public void run()
            {
                Util.setValue("clockDisplay", output);
            }
        });
    }

    /**
     * Are we updating the clocks on all the pages?
     */
    protected transient boolean active = false;

    /**
     * The last time string
     */
    protected String timeString = "";
}
