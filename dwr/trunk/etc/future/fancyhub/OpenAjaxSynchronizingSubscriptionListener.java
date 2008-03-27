package org.directwebremoting.proxy.openajax;

import java.util.List;
import java.util.Set;

import org.directwebremoting.ScriptBuffer;
import org.directwebremoting.ScriptSession;
import org.directwebremoting.event.SubscriptionEvent;
import org.directwebremoting.event.SubscriptionListener;

/**
 * A SubscriptionListener to ensure that remote hubs stay in sync with the
 * server-side {@link PubSubHub}.
 * @author Joe Walker [joe at getahead dot ltd dot uk] 
 */
public class OpenAjaxSynchronizingSubscriptionListener implements SubscriptionListener
{
    /**
     * All {@link OpenAjaxSynchronizingSubscriptionListener}s need to know about
     * the remoted {@link OpenAjaxSynchronizer} so they can get the list of
     * enrolled {@link ScriptSession}s
     * @param openAjaxSynchronizer From which we get the enrolled {@link ScriptSession}s 
     */
    protected OpenAjaxSynchronizingSubscriptionListener(OpenAjaxSynchronizer openAjaxSynchronizer)
    {
        this.openAjaxSynchronizer = openAjaxSynchronizer;
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.proxy.openajax.SubscriptionListener#subscribeHappened(org.directwebremoting.proxy.openajax.SubscriptionEvent)
     */
    public void subscribeHappened(SubscriptionEvent ev)
    {
        PubSubHub pubSubHub = ev.getPubSubHub();

        Set<String> names = pubSubHub.getSubscribedNames();
        Set<String> prefixes = pubSubHub.getSubscribedPrefixes();

        ScriptBuffer script = new ScriptBuffer();
        script.appendScript("PubSubHubSynchronizer.synchronizeOn(")
              .appendData(prefixes)
              .appendScript(",")
              .appendData(names)
              .appendScript(");");

        List<ScriptSession> scriptSessions = openAjaxSynchronizer.getEnrolledScriptSessions();
        for (ScriptSession scriptSession : scriptSessions)
        {
            scriptSession.addScript(script);
        }
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.proxy.openajax.SubscriptionListener#unsubscribeHappened(org.directwebremoting.proxy.openajax.SubscriptionEvent)
     */
    public void unsubscribeHappened(SubscriptionEvent ev)
    {
        // We do the same thing on subscribe and unsubscribe
        subscribeHappened(ev);
    }

    /**
     * The remoted bean that tracks ScriptSessions
     */
    private OpenAjaxSynchronizer openAjaxSynchronizer;
}
