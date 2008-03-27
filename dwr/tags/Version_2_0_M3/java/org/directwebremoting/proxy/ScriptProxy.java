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

import javax.servlet.ServletContext;

import org.directwebremoting.ScriptBuffer;
import org.directwebremoting.ScriptSession;
import org.directwebremoting.ServerContext;
import org.directwebremoting.ServerContextFactory;
import org.directwebremoting.WebContextFactory;

/**
 * Class to help people send scripts to collections of browsers.
 * ScriptProxy also is the base class for the Java implementations of DwrUtil
 * and Scriptaculous.Effect.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class ScriptProxy
{
    /**
     * Http thread constructor
     */
    public ScriptProxy()
    {
        serverContext = WebContextFactory.get();
    }

    /**
     * Non-http thread constructor
     * @param sctx The servlet context to allow us to locate a webapp
     */
    public ScriptProxy(ServletContext sctx)
    {
        serverContext = ServerContextFactory.get(sctx);
    }

    /**
     * Http thread constructor
     * @param scriptSession The browser to alter
     */
    public ScriptProxy(ScriptSession scriptSession)
    {
        serverContext = WebContextFactory.get();
        scriptSessions.add(scriptSession);
    }

    /**
     * Non-http thread constructor
     * @param scriptSession The browser to alter
     * @param sctx The servlet context to allow us to locate a webapp
     */
    public ScriptProxy(ScriptSession scriptSession, ServletContext sctx)
    {
        serverContext = ServerContextFactory.get(sctx);
        scriptSessions.add(scriptSession);
    }

    /**
     * Http thread constructor
     * @param scriptSessions The browsers to alter
     */
    public ScriptProxy(Collection scriptSessions)
    {
        serverContext = WebContextFactory.get();
        this.scriptSessions.addAll(scriptSessions);
    }

    /**
     * Non-http thread constructor
     * @param scriptSessions The browsers to alter
     * @param sctx The servlet context to allow us to locate a webapp
     */
    public ScriptProxy(Collection scriptSessions, ServletContext sctx)
    {
        serverContext = ServerContextFactory.get(sctx);
        this.scriptSessions.addAll(scriptSessions);
    }

    /**
     * Create a new ScriptBuffer from whatever resources we have.
     * @return a new ScriptBuffer
     */
    protected ScriptBuffer createScriptBuffer()
    {
        return new ScriptBuffer(serverContext);
    }

    /**
     * @param scriptSession
     */
    public void addScriptSession(ScriptSession scriptSession)
    {
        scriptSessions.add(scriptSession);
    }

    /**
     * @param addScriptSessions
     */
    public void addScriptSessions(Collection addScriptSessions)
    {
        scriptSessions.addAll(addScriptSessions);
    }

    /**
     * Utility to add the given script to all known browsers.
     * @param script The Javascript to send to the browsers
     */
    public void addScript(ScriptBuffer script)
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
    protected ServerContext getServerContext()
    {
        return serverContext;
    }

    /**
     * We're going to need this for converting data
     */
    private final ServerContext serverContext;

    /**
     * The browsers that we affect.
     */
    private final List scriptSessions = new ArrayList();
}
