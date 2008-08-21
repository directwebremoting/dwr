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
package org.directwebremoting.jms;

import java.util.Date;
import java.util.UUID;

import javax.jms.Destination;
import javax.jms.IllegalStateException;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.servlet.ServletContext;

import org.directwebremoting.Hub;
import org.directwebremoting.HubFactory;

/**
 * An implementation of {@link MessageProducer} that sends messages out over
 * Reverse Ajax to a client side hub
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class DwrMessageProducer implements MessageProducer
{
    /**
     * @param destination
     */
    public DwrMessageProducer(Destination destination, DwrConnection connection)
    {
        this.destination = destination;
        ServletContext servletContext = connection.getServletContext();

        if (servletContext != null)
        {
            hub = HubFactory.get(servletContext);
        }
        else
        {
            hub = HubFactory.get();
        }
    }

    /* (non-Javadoc)
     * @see javax.jms.MessageProducer#close()
     */
    public void close() throws JMSException
    {
        state = State.CLOSED;
    }

    /* (non-Javadoc)
     * @see javax.jms.MessageProducer#getDeliveryMode()
     */
    public int getDeliveryMode() throws JMSException
    {
        return deliveryMode;
    }

    /* (non-Javadoc)
     * @see javax.jms.MessageProducer#getDestination()
     */
    public Destination getDestination() throws JMSException
    {
        return destination;
    }

    /* (non-Javadoc)
     * @see javax.jms.MessageProducer#getDisableMessageID()
     */
    public boolean getDisableMessageID() throws JMSException
    {
        return disableMessageID;
    }

    /* (non-Javadoc)
     * @see javax.jms.MessageProducer#getDisableMessageTimestamp()
     */
    public boolean getDisableMessageTimestamp() throws JMSException
    {
        return disableMessageTimestamp;
    }

    /* (non-Javadoc)
     * @see javax.jms.MessageProducer#getPriority()
     */
    public int getPriority() throws JMSException
    {
        return priority;
    }

    /* (non-Javadoc)
     * @see javax.jms.MessageProducer#getTimeToLive()
     */
    public long getTimeToLive() throws JMSException
    {
        return timeToLive;
    }

    /* (non-Javadoc)
     * @see javax.jms.MessageProducer#send(javax.jms.Message)
     */
    public void send(Message message) throws JMSException
    {
        send(destination, message, deliveryMode, priority, timeToLive);
    }

    /* (non-Javadoc)
     * @see javax.jms.MessageProducer#send(javax.jms.Destination, javax.jms.Message)
     */
    public void send(Destination realDestination, Message message) throws JMSException
    {
        send(realDestination, message, deliveryMode, priority, timeToLive);
    }

    /* (non-Javadoc)
     * @see javax.jms.MessageProducer#send(javax.jms.Message, int, int, long)
     */
    public void send(Message message, int realDeliveryMode, int realPriority, long realTimeToLive) throws JMSException
    {
        send(destination, message, realDeliveryMode, realPriority, realTimeToLive);
    }

    /* (non-Javadoc)
     * @see javax.jms.MessageProducer#send(javax.jms.Destination, javax.jms.Message, int, int, long)
     */
    public void send(Destination realDestination, Message message, int realDeliveryMode, int realPriority, long realTimeToLive) throws JMSException
    {
        if (state == State.CLOSED)
        {
            throw new IllegalStateException("DwrMessageProducer has been closed");
        }

        if (!disableMessageID)
        {
            message.setJMSMessageID(UUID.randomUUID().toString());
        }

        if (!disableMessageTimestamp)
        {
            message.setJMSTimestamp(new Date().getTime());
        }

        if (realDestination instanceof DwrTopic)
        {
            DwrTopic dwrtopic = (DwrTopic) realDestination;
            String topicName = dwrtopic.getTopicName();

            hub.publish(topicName, message);
        }
        else
        {
            throw new IllegalStateException("Unsuported Destination type (" + realDestination.getClass().getCanonicalName() + "). Only Topics are currently supported.");
        }
    }

    /* (non-Javadoc)
     * @see javax.jms.MessageProducer#setDeliveryMode(int)
     */
    public void setDeliveryMode(int deliveryMode) throws JMSException
    {
        this.deliveryMode = deliveryMode;
        throw Unsupported.noManualAcknowledgment();
    }

    /* (non-Javadoc)
     * @see javax.jms.MessageProducer#setDisableMessageID(boolean)
     */
    public void setDisableMessageID(boolean disableMessageID) throws JMSException
    {
        this.disableMessageID = disableMessageID;
    }

    /* (non-Javadoc)
     * @see javax.jms.MessageProducer#setDisableMessageTimestamp(boolean)
     */
    public void setDisableMessageTimestamp(boolean disableMessageTimestamp) throws JMSException
    {
        this.disableMessageTimestamp = disableMessageTimestamp;
    }

    /* (non-Javadoc)
     * @see javax.jms.MessageProducer#setPriority(int)
     */
    public void setPriority(int priority) throws JMSException
    {
        this.priority = priority;
        throw Unsupported.noMessagePriority();
    }

    /* (non-Javadoc)
     * @see javax.jms.MessageProducer#setTimeToLive(long)
     */
    public void setTimeToLive(long timeToLive) throws JMSException
    {
        this.timeToLive = timeToLive;
        throw Unsupported.noMessageExpiry();
    }

    /**
     * The DWR hub that does the real work
     */
    private Hub hub;

    /**
     * The default queue or topic
     */
    private Destination destination;

    /**
     * For queues: are messages persistent
     */
    private int deliveryMode;

    /**
     * Have we been closed?
     */
    private State state;

    /**
     * Do we skip assigning message IDs?
     */
    private boolean disableMessageID;

    /**
     * Do we skip assigning timestamps
     */
    private boolean disableMessageTimestamp;

    /**
     * What is the default priority for messages we produce
     */
    private int priority;

    /**
     * How long before the system can delete messages that we produce
     */
    private long timeToLive;
}
