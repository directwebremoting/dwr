package org.directwebremoting.proxy.openajax;

import org.directwebremoting.Hub;
import org.directwebremoting.HubFactory;

import junit.framework.TestCase;

/**
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class PubSubHubTest extends TestCase
{
    /**
     * 
     */
    public void testSubscribeStringStringSubscriptionListener()
    {
        Hub hub = HubFactory.get();

        Object subscriptionData = new Integer(42);
        Object publisherData = new Float(3.1416);
        CustomSubscriptionListener pn = new CustomSubscriptionListener("httpId", "scriptId", "prefix", "name", subscriptionData, publisherData);

        hub.subscribe("prefix", "name", pn, subscriptionData);
        assertEquals(pn.getNotifications(), 0);

        hub.publish("prefix", "name", publisherData);
        assertEquals(pn.getNotifications(), 1);

        hub.publish("prefix1", "name1", publisherData);
        assertEquals(pn.getNotifications(), 1);

        hub.publish("prefix", "name1", publisherData);
        assertEquals(pn.getNotifications(), 1);

        hub.publish("prefix1", "name", publisherData);
        assertEquals(pn.getNotifications(), 1);

        hub.publish("prefix", "name", publisherData);
        assertEquals(pn.getNotifications(), 2);

        hub.unsubscribe("prefix", "name", pn);
        assertEquals(pn.getNotifications(), 2);

        hub.publish("prefix", "name", publisherData);
        assertEquals(pn.getNotifications(), 2);

        hub.subscribe("*", "scriptId", "prefix", "name", pn, subscriptionData);
        assertEquals(pn.getNotifications(), 2);

        hub.publish("prefix", "name", publisherData);
        assertEquals(pn.getNotifications(), 3);

        hub.publish("prefix", "*", publisherData);
        assertEquals(pn.getNotifications(), 4);

        hub.publish("*", "name", publisherData);
        assertEquals(pn.getNotifications(), 5);

        hub.publish("prefix", PubSubHub.ANY_NAME, publisherData);
        assertEquals(pn.getNotifications(), 6);

        hub.publish(PubSubHub.ANY_PREFIX, "name", publisherData);
        assertEquals(pn.getNotifications(), 7);
    }

    /**
     * A test PublishListener that simply counts publishes
     */
    private static final class CustomSubscriptionListener implements PublishListener
    {
        public CustomSubscriptionListener(String httpSessionId, String scriptSessionId, String prefix, String name, Object subscriptionData, Object publisherData)
        {
            this.httpSessionId = httpSessionId;
            this.scriptSessionId = scriptSessionId;
            this.prefix = prefix;
            this.name = name;
            this.subscriptionData = subscriptionData;
            this.publisherData = publisherData;
        }

        /* (non-Javadoc)
         * @see org.directwebremoting.proxy.openajax.PublishListener#eventHappened(org.directwebremoting.proxy.openajax.PublishEvent)
         */
        public void publishHappened(PublishEvent ev)
        {
            if (!ev.getHttpSessionId().equals(PubSubHub.ANY_HTTP_SESSION))
            {
                assertEquals(httpSessionId, ev.getHttpSessionId());
            }

            if (!ev.getScriptSessionId().equals(PubSubHub.ANY_SCRIPT_SESSION))
            {
                assertEquals(scriptSessionId, ev.getScriptSessionId());
            }

            if (!ev.getPrefix().equals(PubSubHub.ANY_PREFIX))
            {
                assertEquals(prefix, ev.getPrefix());
            }

            if (!ev.getName().equals(PubSubHub.ANY_NAME))
            {
                assertEquals(name, ev.getName());
            }

            assertEquals(subscriptionData, ev.getSubscriberData());
            assertEquals(publisherData, ev.getPublisherData());

            notifications++;
        }

        public int getNotifications()
        {
            return notifications;
        }

        /* (non-Javadoc)
         * @see java.lang.Object#toString()
         */
        @Override
        public String toString()
        {
            return "PublishListener[" + prefix + "/" + name + "]";
        }

        private int notifications = 0;

        private String httpSessionId;
        private String scriptSessionId;
        private String prefix;
        private String name;
        private Object subscriptionData;
        private Object publisherData;
    }
}
