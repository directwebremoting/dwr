package org.directwebremoting.event;

import java.util.EventListener;

import org.directwebremoting.proxy.openajax.PubSubHub;

/**
 * Subscriptions in a {@link PubSubHub} happen with a {@link PublishListener}
 * to recieve the publish events via {@link #publishHappened(PublishEvent)}
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public interface PublishListener extends EventListener
{
    /**
     * When a publish event goes off that matches the filters we subscribed
     * @param ev The publish event
     */
    public void publishHappened(PublishEvent ev);
}
