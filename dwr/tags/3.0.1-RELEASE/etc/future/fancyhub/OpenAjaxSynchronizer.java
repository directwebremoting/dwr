package org.directwebremoting.proxy.openajax;

import java.util.ArrayList;
import java.util.List;

import org.directwebremoting.Container;
import org.directwebremoting.ScriptSession;
import org.directwebremoting.WebContext;
import org.directwebremoting.WebContextFactory;
import org.directwebremoting.event.ScriptSessionEvent;
import org.directwebremoting.event.ScriptSessionListener;
import org.directwebremoting.extend.ScriptSessionManager;

/**
 * This class is designed for export by DWR to enable the OpenAjax hub to
 * exchange publish/subscribe data with the server.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class OpenAjaxSynchronizer
{
    /**
     * 
     */
    public OpenAjaxSynchronizer()
    {
        Container container = WebContextFactory.get().getContainer();

        ScriptSessionManager scriptSessionManager = (ScriptSessionManager) container.getBean(ScriptSessionManager.class.getName());
        scriptSessionManager.addScriptSessionListener(new CustomScriptSessionListener());

        pubSubHub = (PubSubHub) container.getBean(PubSubHub.class.getName());
        pubSubHub.subscribe(PubSubHub.ANY_PREFIX, PubSubHub.ANY_NAME, new OpenAjaxSynchronizingPublishListener(this));
        pubSubHub.addSubscriptionListener(new OpenAjaxSynchronizingSubscriptionListener(this));
    }

    /**
     * Called by clients that wish to be included in the whole hub sync thing.
     */
    public void enroll()
    {
        WebContext webContext = WebContextFactory.get();
        ScriptSession scriptSession = webContext.getScriptSession();
        synchronized (enrolledScriptSessions)
        {
            enrolledScriptSessions.add(scriptSession);
        }
    }

    /**
     * This method allows the client side hub synchronizer to forward publish
     * messages to the server hub
     * @param prefix The prefix that was published to
     * @param name The name that was published to
     * @param data The published data
     * @param hubsVisited The list of hubs that this message has been through
     */
    public void publish(String prefix, String name, String data, List<String> hubsVisited)
    {
        WebContext webContext = WebContextFactory.get();

        String httpSessionId = webContext.getSession(true).getId();
        String scriptSessionId = webContext.getScriptSession().getId();

        pubSubHub.publish(httpSessionId, scriptSessionId, prefix, name, data, hubsVisited);
    }

    /**
     * Get a list of the {@link ScriptSession}s that are part of this
     * distributed hub. 
     * @return The current list of known enrolled {@link ScriptSession}s
     */
    protected List<ScriptSession> getEnrolledScriptSessions()
    {
        List<ScriptSession> reply = new ArrayList<ScriptSession>();
        synchronized (enrolledScriptSessions)
        {
            reply.addAll(enrolledScriptSessions);
        }
        return reply;
    }

    /**
     * The current server side hub
     */
    private PubSubHub pubSubHub;

    /**
     * The current list of known enrolled {@link ScriptSession}s
     */
    protected List<ScriptSession> enrolledScriptSessions =  new ArrayList<ScriptSession>();

    /**
     * We need to know when pages go away so we don't keep publishing changes
     * to them.
     */
    protected final class CustomScriptSessionListener implements ScriptSessionListener
    {
        /* (non-Javadoc)
         * @see org.directwebremoting.event.ScriptSessionListener#sessionCreated(org.directwebremoting.event.ScriptSessionEvent)
         */
        public void sessionCreated(ScriptSessionEvent ev)
        {
            // The creation of a ScriptSession is not important, because they
            // might not enroll. We care about the enrolled ones that have gone
            // away.
        }

        /* (non-Javadoc)
         * @see org.directwebremoting.event.ScriptSessionListener#sessionDestroyed(org.directwebremoting.event.ScriptSessionEvent)
         */
        public void sessionDestroyed(ScriptSessionEvent ev)
        {
            ScriptSession scriptSession = ev.getSession();
            synchronized (enrolledScriptSessions)
            {
                enrolledScriptSessions.add(scriptSession);
            }
        }
    }
}
