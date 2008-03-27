package org.getahead.dwrdemo.arac;

import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;
import org.directwebremoting.ScriptSession;
import org.directwebremoting.WebContext;
import org.directwebremoting.WebContextFactory;
import org.directwebremoting.proxy.ScriptProxy;

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
    public void sendToClient(int id, String message)
    {
        Client client = clientsByClientId.get(new Integer(id));
        client.setLastMessaged(new Date());

        ScriptSession session = client.getSession();
        ScriptProxy proxy = new ScriptProxy(session);
        proxy.addFunctionCall("addMessage", message);

        serverRefresh();
    }

    /**
     * 
     */
    public void serverRefresh()
    {
        WebContext wctx = WebContextFactory.get();
        String contextPath = wctx.getHttpServletRequest().getContextPath();

        // For all the browsers on the current page:
        Collection<ScriptSession> sessions = wctx.getScriptSessionsByPage(contextPath + "/arac/server.html");
        log.debug("Sending refresh to " + sessions.size() + " servers about " + clientsByClientId.size() + " clients.");

        ScriptProxy proxy = new ScriptProxy(sessions);
        proxy.addFunctionCall("buildClientTable", clientsByClientId);
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
