package org.directwebremoting.event;

import java.util.EventListener;

import org.directwebremoting.proxy.openajax.PubSubHub;

/**
 * In order to propogate publish messages to another hub, some sort of filter
 * is required to prevent a publish storm. A {@link SubscriptionListener} enables
 * hubs to get notifications of what they need to propogate.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public interface SubscriptionListener extends EventListener
{
    /**
     * Someone has subscribed a {@link PublishListener} to a {@link PubSubHub}
     * @param ev The subscription event
     */
    public void subscribeHappened(SubscriptionEvent ev);

    /**
     * Someone has unsubscribed a {@link PublishListener} from a {@link PubSubHub}
     * @param ev The subscription event
     */
    public void unsubscribeHappened(SubscriptionEvent ev);
}
