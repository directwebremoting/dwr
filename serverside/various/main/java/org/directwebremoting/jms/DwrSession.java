package org.directwebremoting.jms;

import java.io.Serializable;

import javax.jms.BytesMessage;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageListener;
import javax.jms.Queue;
import javax.jms.QueueBrowser;
import javax.jms.Session;
import javax.jms.StreamMessage;
import javax.jms.TemporaryQueue;
import javax.jms.TemporaryTopic;
import javax.jms.Topic;
import javax.jms.TopicSubscriber;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * An implementation of {@link Session} for DWR
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class DwrSession implements Session
{
    /**
     * @see javax.jms.Connection#createSession(boolean, int)
     * @param transacted See {@link Session#getTransacted()}
     * @param acknowledgeMode See {@link Session#getAcknowledgeMode()}
     * @param connection 
     */
    public DwrSession(DwrConnection connection, boolean transacted, int acknowledgeMode)
    {
        if (transacted)
        {
            throw Unsupported.noTransactions();
        }

        if (acknowledgeMode != AUTO_ACKNOWLEDGE)
        {
            throw Unsupported.noManualAcknowledgment();
        }

        this.transacted = transacted;
        this.acknowledgeMode = acknowledgeMode;
        this.connection = connection;
    }

    /* (non-Javadoc)
     * @see javax.jms.Session#createMessage()
     */
    public DwrMessage createMessage() throws JMSException
    {
        return new DwrMessage();
    }

    /* (non-Javadoc)
     * @see javax.jms.Session#createMapMessage()
     */
    public DwrMessage createMapMessage() throws JMSException
    {
        return new DwrMessage();
    }

    /* (non-Javadoc)
     * @see javax.jms.Session#createTextMessage()
     */
    public DwrMessage createTextMessage() throws JMSException
    {
        return new DwrMessage();
    }

    /* (non-Javadoc)
     * @see javax.jms.Session#createTextMessage(java.lang.String)
     */
    public DwrMessage createTextMessage(String text) throws JMSException
    {
        return new DwrMessage(text);
    }

    /* (non-Javadoc)
     * @see javax.jms.Session#createStreamMessage()
     */
    public StreamMessage createStreamMessage() throws JMSException
    {
        throw Unsupported.noBinaryMessages();
    }

    /* (non-Javadoc)
     * @see javax.jms.Session#createBytesMessage()
     */
    public BytesMessage createBytesMessage() throws JMSException
    {
        throw Unsupported.noBinaryMessages();
    }

    /* (non-Javadoc)
     * @see javax.jms.Session#createObjectMessage()
     */
    public DwrMessage createObjectMessage() throws JMSException
    {
        return new DwrMessage();
    }

    /* (non-Javadoc)
     * @see javax.jms.Session#createObjectMessage(java.io.Serializable)
     */
    public DwrMessage createObjectMessage(Serializable object) throws JMSException
    {
        return new DwrMessage(object);
    }

    /* (non-Javadoc)
     * @see javax.jms.Session#createBrowser(javax.jms.Queue)
     */
    public QueueBrowser createBrowser(Queue queue) throws JMSException
    {
        throw Unsupported.noPointToPoint();
    }

    /* (non-Javadoc)
     * @see javax.jms.Session#createBrowser(javax.jms.Queue, java.lang.String)
     */
    public QueueBrowser createBrowser(Queue queue, String messageSelector) throws JMSException
    {
        throw Unsupported.noPointToPoint();
    }

    /* (non-Javadoc)
     * @see javax.jms.Session#createConsumer(javax.jms.Destination)
     */
    public DwrMessageConsumer createConsumer(Destination destination) throws JMSException
    {
        return new DwrMessageConsumer(connection, destination);
    }

    /* (non-Javadoc)
     * @see javax.jms.Session#createConsumer(javax.jms.Destination, java.lang.String)
     */
    public DwrMessageConsumer createConsumer(Destination destination, String messageSelector) throws JMSException
    {
        return new DwrMessageConsumer(connection, destination, messageSelector);
    }

    /* (non-Javadoc)
     * @see javax.jms.Session#createConsumer(javax.jms.Destination, java.lang.String, boolean)
     */
    public DwrMessageConsumer createConsumer(Destination destination, String messageSelector, boolean noLocal) throws JMSException
    {
        return new DwrMessageConsumer(connection, destination, messageSelector, noLocal);
    }

    /* (non-Javadoc)
     * @see javax.jms.Session#createDurableSubscriber(javax.jms.Topic, java.lang.String)
     */
    public TopicSubscriber createDurableSubscriber(Topic topic, String name) throws JMSException
    {
        throw Unsupported.noDurableSubscriptions();
    }

    /* (non-Javadoc)
     * @see javax.jms.Session#createDurableSubscriber(javax.jms.Topic, java.lang.String, java.lang.String, boolean)
     */
    public TopicSubscriber createDurableSubscriber(Topic topic, String name, String messageSelector, boolean noLocal) throws JMSException
    {
        throw Unsupported.noDurableSubscriptions();
    }

    /* (non-Javadoc)
     * @see javax.jms.Session#createProducer(javax.jms.Destination)
     */
    public DwrMessageProducer createProducer(Destination destination) throws JMSException
    {
        return new DwrMessageProducer(destination, connection);
    }

    /* (non-Javadoc)
     * @see javax.jms.Session#createQueue(java.lang.String)
     */
    public Queue createQueue(String queueName) throws JMSException
    {
        throw Unsupported.noPointToPoint();
    }

    /* (non-Javadoc)
     * @see javax.jms.Session#createTemporaryQueue()
     */
    public TemporaryQueue createTemporaryQueue() throws JMSException
    {
        throw Unsupported.noPointToPoint();
    }

    /* (non-Javadoc)
     * @see javax.jms.Session#createTopic(java.lang.String)
     */
    public DwrTopic createTopic(String topicName) throws JMSException
    {
        return new DwrTopic(topicName);
    }

    /* (non-Javadoc)
     * @see javax.jms.Session#createTemporaryTopic()
     */
    public TemporaryTopic createTemporaryTopic() throws JMSException
    {
        throw Unsupported.noTemporaryTopic();
    }

    /* (non-Javadoc)
     * @see javax.jms.Session#getAcknowledgeMode()
     */
    public int getAcknowledgeMode() throws JMSException
    {
        return acknowledgeMode;
    }

    /* (non-Javadoc)
     * @see javax.jms.Session#setMessageListener(javax.jms.MessageListener)
     */
    public void setMessageListener(MessageListener messageListener) throws JMSException
    {
        this.messageListener = messageListener;
    }

    /* (non-Javadoc)
     * @see javax.jms.Session#getMessageListener()
     */
    public MessageListener getMessageListener() throws JMSException
    {
        return messageListener;
    }

    /* (non-Javadoc)
     * @see javax.jms.Session#getTransacted()
     */
    public boolean getTransacted() throws JMSException
    {
        return transacted;
    }

    /* (non-Javadoc)
     * @see javax.jms.Session#commit()
     */
    public void commit() throws JMSException
    {
        if (!shownTransactionWarning)
        {
            log.warn("DWR's JMS support is not transactional");
            shownTransactionWarning = true;
        }
    }

    /* (non-Javadoc)
     * @see javax.jms.Session#recover()
     */
    public void recover() throws JMSException
    {
        throw Unsupported.noManualAcknowledgment();
    }

    /* (non-Javadoc)
     * @see javax.jms.Session#rollback()
     */
    public void rollback() throws JMSException
    {
        if (!shownTransactionWarning)
        {
            log.warn("DWR's JMS support is not transactional");
            shownTransactionWarning = true;
        }
    }

    /* (non-Javadoc)
     * @see javax.jms.Session#run()
     */
    public void run()
    {
    }

    /* (non-Javadoc)
     * @see javax.jms.Session#close()
     */
    public void close() throws JMSException
    {
    }

    /* (non-Javadoc)
     * @see javax.jms.Session#unsubscribe(java.lang.String)
     */
    public void unsubscribe(String name) throws JMSException
    {
        throw Unsupported.noDurableSubscriptions();
    }

    /**
     * @see Unsupported#noManualAcknowledgment()
     */
    private int acknowledgeMode;

    /**
     * @see Unsupported#noTransactions()
     */
    private boolean transacted;

    /**
     * The current message destination
     */
    private MessageListener messageListener;

    /**
     * The connection through which this session was created
     */
    private DwrConnection connection;

    /**
     * We only want to show the not-transactional warning once.
     */
    private boolean shownTransactionWarning = false;

    /**
     * The log stream
     */
    private static final Log log = LogFactory.getLog(DwrSession.class);
}
