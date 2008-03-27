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
package uk.ltd.getahead.dwrdemo.clock;

import java.util.Collection;
import java.util.Date;

import javax.servlet.ServletContext;

import org.directwebremoting.ServerContext;
import org.directwebremoting.ServerContextFactory;
import org.directwebremoting.WebContextFactory;
import org.directwebremoting.proxy.dwrutil.DwrUtil;

/**
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class Clock implements Runnable
{
    /**
     * 
     */
    public Clock()
    {
        servletContext = WebContextFactory.get().getServletContext();
    }

    /**
     * 
     */
    public void toggle()
    {
        active = !active;

        if (active)
        {
            new Thread(this).start();
        }
    }

    /* (non-Javadoc)
     * @see java.lang.Runnable#run()
     */
    public void run()
    {
        try
        {
            while (active)
            {
                ServerContext sctx = ServerContextFactory.get(servletContext);
                Collection sessions = sctx.getScriptSessionsByPage("/dwr/clock/index.html"); //$NON-NLS-1$

                DwrUtil pages = new DwrUtil(sessions, servletContext);
                pages.setValue("clockDisplay", new Date().toString()); //$NON-NLS-1$

                Thread.sleep(1000);
            }
        }
        catch (InterruptedException ex)
        {
            ex.printStackTrace();
        }
    }        

    /**
     * Our key to get hold of ServerContexts
     */
    private ServletContext servletContext;

    /**
     * Are we updating the clocks on all the pages?
     */
    private boolean active = false;
}
