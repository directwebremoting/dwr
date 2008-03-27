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

import java.util.Collection;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.directwebremoting.ScriptSession;
import org.directwebremoting.ServerContext;
import org.directwebremoting.ServerContextFactory;
import org.directwebremoting.proxy.ScriptProxy;
import org.directwebremoting.util.SharedObjects;

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
        ScheduledThreadPoolExecutor executor = SharedObjects.getScheduledThreadPoolExecutor();
        executor.scheduleAtFixedRate(this, 1, 1, TimeUnit.SECONDS);
    }

    /* (non-Javadoc)
     * @see java.lang.Runnable#run()
     */
    public void run()
    {
        ServerContext serverContext = ServerContextFactory.get();
        String contextPath = serverContext.getContextPath();
        if (contextPath == null)
        {
            return;
        }

        Collection<ScriptSession> sessions = serverContext.getScriptSessionsByPage(contextPath + "/gi/dwr-oa-gi.html");
        ScriptProxy proxy = new ScriptProxy(sessions);

        Corporation corp = corporations.getNextChangedCorporation();
        proxy.addFunctionCall("OpenAjax.hub.publish", "gidemo.corp", corp);
    }

    /**
     * The set of corporations that we manage
     */
    protected Corporations corporations = new Corporations();
}
