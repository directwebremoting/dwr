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
package org.directwebremoting.proxy;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.directwebremoting.ScriptSession;
import org.directwebremoting.WebContext;
import org.directwebremoting.WebContextFactory;

/**
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class AbstractProxy
{
    /**
     * 
     */
    protected AbstractProxy()
    {
        webContext = WebContextFactory.get();
    }

    /**
     * @param scriptSession
     */
    protected void addScriptSession(ScriptSession scriptSession)
    {
        scriptSessions.add(scriptSession);
    }

    /**
     * @param addScriptSessions
     */
    protected void addScriptSessions(Collection addScriptSessions)
    {
        scriptSessions.addAll(addScriptSessions);
    }

    /**
     * Utility to add the given script to all known browsers.
     * @param script The Javascript to send to the browsers
     */
    protected void addScript(String script)
    {
        for (Iterator it = scriptSessions.iterator(); it.hasNext();)
        {
            ScriptSession scriptSession = (ScriptSession) it.next();
            scriptSession.addScript(script);
        }
    }

    /**
     * @return the webContext
     */
    protected WebContext getWebContext()
    {
        return webContext;
    }

    /**
     * We're going to need this for converting data
     */
    private final WebContext webContext;

    /**
     * The browsers that we affect.
     */
    private final List scriptSessions = new ArrayList();
}
