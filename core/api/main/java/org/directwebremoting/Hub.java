package org.directwebremoting;

import org.directwebremoting.event.MessageListener;

/**
 * A server-side hub that can publish data across reverse ajax and sync with
 * a number of other hubs, including: JMS, the OpenAjax Alliance hub, and
 * potentially others.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public interface Hub
{
    /**
     * Called to create a subscription so that future publishes to a similarly
     * named topic alert the MessageListener that a publish has happened.
     * <p><b>Warning</b>
     * Currently the topic is a simple string pattern match. This more closely
     * resembles JMS rather than the OpenAjax hub because we're taking the
     * simplistic approach for now. I'm sure there will be some nasty problems
     * that fall out of this.
     * <p>The OpenAjax hub allows subscriber data that is passed back to the
     * subscriber when the event happens. Since listeners are an ideal place for
     * this data it isn't supported here. The OAA hub also allows for filters,
     * but I'm thinking that this can be easily supported by the listener. What
     * am I missing?
     * @param topicName The topic to subscribe to.
     * @param listener The object to notify of matching calls to publish()
     */
    void subscribe(String topicName, MessageListener listener);

    /**
     * Reverse the action of {@link #subscribe(String, MessageListener)}
     * @param topicName The topic to subscribe to.
     * @param listener The object to notify of matching calls to publish()
     */
    boolean unsubscribe(String topicName, MessageListener listener);

    /**
     * Publish some data to a certain topic.
     * @param topicName The topic to subscribe to.
     * @param message The data to publish
     */
    void publish(String topicName, Object message);
}
