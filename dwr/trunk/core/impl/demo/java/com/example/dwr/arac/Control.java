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
package com.example.dwr.arac;

import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.directwebremoting.Browser;
import org.directwebremoting.ScriptSession;
import org.directwebremoting.ScriptSessions;
import org.directwebremoting.WebContext;
import org.directwebremoting.WebContextFactory;

/**
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class Control
{
    /**
     * @param name The new message text to add
     */
    public void pingFromClient(String name)
    {
        WebContext wctx = WebContextFactory.get();
        ScriptSession session = wctx.getScriptSession();
        String id = session.getId();

        Client client = clientsBySessionId.get(id);
        if (client == null)
        {
            client = new Client(name, session);
            clientsBySessionId.put(id, client);
            clientsByClientId.put(new Integer(client.getId()), client);
        }

        client.setName(name);
        client.setLastPinged(new Date());

        serverRefresh();
    }

    /**
     * @param id
     * @param message
     */
    public void sendToClient(int id, final String message)
    {
        Client client = clientsByClientId.get(new Integer(id));
        client.setLastMessaged(new Date());

        ScriptSessions.addFunctionCall("addMessage", message);
        serverRefresh();
    }

    /**
     *
     */
    public void serverRefresh()
    {
        String page = WebContextFactory.get().getContextPath() + "/arac/server.html";

        // For all the browsers on the current page:
        Browser.withPage(page, new Runnable()
        {
            public void run()
            {
                ScriptSessions.addFunctionCall("buildClientTable", clientsByClientId);
            }
        });
    }

    /**
     * The current set of messages
     */
    private final Map<String, Client> clientsBySessionId = new TreeMap<String, Client>();

    private final Map<Integer, Client> clientsByClientId = new TreeMap<Integer, Client>();

    /**
     * The log stream
     */
    protected static final Log log = LogFactory.getLog(Control.class);
}
