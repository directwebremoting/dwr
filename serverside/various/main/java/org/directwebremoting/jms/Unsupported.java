package org.directwebremoting.jms;

import javax.jms.ConnectionConsumer;

/**
 * This class tracks the parts of the JMS spec that DWR to not support.
 * In some respects this is hugely over-engineered, we could simply inline all
 * these methods and delete the class, however the advantage of this approach is
 * that we can track what needs to be done to add support for some feature.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class Unsupported
{
    /**
     * DWR does not support binary messages.
     * It could by making use of the file download feature, however we need to
     * bed things in a bit first
     */
    static UnsupportedOperationException noBinaryMessages()
    {
        return new UnsupportedOperationException(PREFIX + "Binary messages");
    }

    /**
     * DWR does not support manual acknowledgment.
     * When DWR supports Gears properly then we might be able to add this
     */
    static UnsupportedOperationException noManualAcknowledgment()
    {
        return new UnsupportedOperationException(PREFIX + "Manual Acknowledgment");
    }

    /**
     * Publish of point to point messages aimed at browsers seems like a very
     * broken concept. Maybe this will never be implemented.
     */
    static UnsupportedOperationException noPointToPoint()
    {
        return new UnsupportedOperationException(PREFIX + "Point to point messages");
    }

    /**
     * When DWR supports Gears properly then we might be able to add this
     */
    static UnsupportedOperationException noTransactions()
    {
        return new UnsupportedOperationException(PREFIX + "Point to point messages");
    }

    /**
     * We don't expire messages other than beyond the script session timeout
     * which is not set at a message level
     */
    static UnsupportedOperationException noMessageExpiry()
    {
        return new UnsupportedOperationException(PREFIX + "Message expiry");
    }

    /**
     * DWR treats all messages fairly. Queue jumping is expected to be hard to
     * implement
     */
    static UnsupportedOperationException noMessagePriority()
    {
        return new UnsupportedOperationException(PREFIX + "Message priority");
    }

    /**
     * {@link ConnectionConsumer}s are for high message load arenas
     */
    static UnsupportedOperationException noConnectionConsumers()
    {
        return new UnsupportedOperationException(PREFIX + "Message priority");
    }

    /**
     * Since it's not clear how we will map message properties to JSON messages,
     * we are likely to ignore this in the short term
     */
    static UnsupportedOperationException noMessageSelectors()
    {
        return new UnsupportedOperationException(PREFIX + "Message Selectors");
    }

    /**
     * What do temporary topics (without names) mean in a disconnected system
     * like with the web?
     */
    static UnsupportedOperationException noTemporaryTopic()
    {
        return new UnsupportedOperationException(PREFIX + "Temporary Topics");
    }

    /**
     * What do durable subscriptions mean in a disconnected system like with the
     * web?
     */
    static UnsupportedOperationException noDurableSubscriptions()
    {
        return new UnsupportedOperationException(PREFIX + "Durable Subscriptions");
    }

    /**
     * Some prose to flesh out the reason
     */
    private static final String PREFIX = "Unsupported in this version of DWR: ";
}
