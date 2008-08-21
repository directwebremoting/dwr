package com.example.dwr.arac;

import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.directwebremoting.Browser;
import org.directwebremoting.ScriptSession;
import org.directwebremoting.WebContext;
import org.directwebremoting.WebContextFactory;
import org.directwebremoting.ui.ScriptProxy;

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

        ScriptSession session = client.getSession();
        Browser.withSession(session, new Runnable()
        {
            public void run()
            {
                ScriptProxy.addFunctionCall("addMessage", message);
            }
        });

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
                ScriptProxy.addFunctionCall("buildClientTable", clientsByClientId);
            }
        });
    }

    /**
     * The current set of messages
     */
    private Map<String, Client> clientsBySessionId = new TreeMap<String, Client>();

    private Map<Integer, Client> clientsByClientId = new TreeMap<Integer, Client>();

    /**
     * The log stream
     */
    protected static final Log log = LogFactory.getLog(Control.class);
}
