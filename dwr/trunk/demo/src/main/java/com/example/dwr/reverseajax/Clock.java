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

import java.util.Date;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.directwebremoting.Browser;
import org.directwebremoting.ScriptSession;
import org.directwebremoting.ScriptSessionFilter;
import org.directwebremoting.WebContextFactory;
import org.directwebremoting.impl.DaemonThreadFactory;
import org.directwebremoting.ui.dwr.Util;

/**
 * A server-side clock that broadcasts the server time to any browsers that will
 * listen.
 * This is an example of how to control clients using server side threads
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class Clock implements Runnable
{
    /**
     * Create a schedule to update the clock every second.
     */
    public Clock()
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
            String newTimeString = new Date().toString();
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
            setClockDisplayForAll("Clock started");
        }
        else
        {
            setClockDisplayForAll("Clock stopped");
        }
    }

    private class UpdatesEnabledFilter implements ScriptSessionFilter {
    	private String attrName;
    	
    	public UpdatesEnabledFilter(String attrName) {
    		this.attrName = attrName;
    	}
    	
		@Override
		public boolean match(ScriptSession ss) {
			Object check = ss.getAttribute(attrName);
	        return (check != null && check.equals(Boolean.TRUE));
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
    public void setClockDisplayForAll(final String output)
    {
        Browser.withAllSessions(new Runnable()
        {
            public void run()
            {
                Util.setValue("clockDisplay", output);
            }
        });
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
        Browser.withAllSessionsFiltered(new UpdatesEnabledFilter(UPDATES_ENABLED_ATTR), new Runnable()
        {
            public void run()
            {
                Util.setValue("clockDisplay", output);
            }
        });
    }

    /**
     * 
     * @param enabled
     */
    public void setEnabledAttribute(Boolean enabled) {
    	ScriptSession scriptSession = WebContextFactory.get().getScriptSession();
    	scriptSession.setAttribute(UPDATES_ENABLED_ATTR, enabled);
    }
    
    private static String UPDATES_ENABLED_ATTR = "UPDATES_ENABLED";
    
    /**
     * Are we updating the clocks on all the pages?
     */
    protected transient boolean active = false;

    /**
     * The last time string
     */
    protected String timeString = "";
}
