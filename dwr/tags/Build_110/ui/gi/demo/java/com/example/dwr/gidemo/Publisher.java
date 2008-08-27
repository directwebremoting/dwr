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
package com.example.dwr.gidemo;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.directwebremoting.Browser;
import org.directwebremoting.ScriptSessions;
import org.directwebremoting.ServerContextFactory;

/**
 * A generator of random objects to push to GI
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class Publisher implements Runnable
{
    /**
     * Create a new publish thread and start it
     */
    public Publisher()
    {
        ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(1);
        executor.scheduleAtFixedRate(this, 1, 1, TimeUnit.SECONDS);
    }

    /* (non-Javadoc)
     * @see java.lang.Runnable#run()
     */
    public void run()
    {
        String contextPath = ServerContextFactory.get().getContextPath();
        if (contextPath == null)
        {
            return;
        }

        Browser.withPage(contextPath + "/gi/dwr-oa-gi.html", new Runnable()
        {
            public void run()
            {
                Corporation corp = corporations.getNextChangedCorporation();
                ScriptSessions.addFunctionCall("OpenAjax.hub.publish", "gidemo.corp", corp);
            }
        });
    }

    /**
     * The set of corporations that we manage
     */
    protected Corporations corporations = new Corporations();
}
