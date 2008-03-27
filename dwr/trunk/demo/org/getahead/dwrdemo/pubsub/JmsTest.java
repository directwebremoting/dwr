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
package org.getahead.dwrdemo.pubsub;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.ExceptionListener;
import javax.jms.IllegalStateException;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * A demo of the pub/sub side of JMS.
 * Currently DWR does not support point-to-point JMS because there doesn't seem
 * much point - web clients are inherently fickle, so getting the address of a
 * browser out of JNDI seems silly
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class JmsTest
{
    /**
     * We start open
     */
    public JmsTest() throws JMSException
    {
        open();
    }

    /**
     * Setup JMS and create a subscriber listener
     * This method could be exported to the web if needed, but it probably
     * doesn't make much sense unless it's in an admin role
     */
    protected synchronized void open() throws JMSException
    {
        getConnection();
        connection.setExceptionListener(new ExceptionListener()
        {
            public void onException(JMSException ex)
            {
                log.warn("JMS Failure", ex);
            }
        });
        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        // Setup Pub/Sub
        topic = getTopic();
        topicProducer = session.createProducer(topic);
        topicConsumer = session.createConsumer(topic);

        topicConsumer.setMessageListener(new MessageListener()
        {
            public void onMessage(Message message)
            {
                if (message instanceof TextMessage)
                {
                    TextMessage textMessage = (TextMessage) message;
                    try
                    {
                        log.info(textMessage.getText());
                    }
                    catch (JMSException ex)
                    {
                        log.error("Failed to get text from message", ex);
                    }
                }
                else
                {
                    log.info("Not sure how to convert message to string for type " + message.getClass().getName());
                }
            }
        });

        connection.start();
        open = true;
    }

    /**
     * Generally this would be done by JNDI etc
     */
    protected Connection getConnection() throws JMSException
    {
        try
        {
            Class<?> factoryClass = Class.forName(FACTORY_CLASSNAME);
            ConnectionFactory factory = (ConnectionFactory) factoryClass.newInstance();
            connection = factory.createConnection();
            connection.setClientID(CLIENT_ID);
            return connection;
        }
        catch (JMSException ex)
        {
            throw ex;
        }
        catch (Exception ex)
        {
            throw new JMSException(ex.toString());
        }
    }

    /**
     * Generally this would be done by JNDI etc
     */
    protected Topic getTopic() throws JMSException
    {
        return session.createTopic(TOPIC_NAME);
    }

    /**
     * Exported method to publish a message to a topic
     */
    public synchronized void publish(String data) throws JMSException
    {
        if (!open)
        {
            throw new IllegalStateException("JmsTest is closed.");
        }

        String info = "JmsTest says " + data;
        log.info("Publishing message '" + info + "' to '" + topic.getTopicName() + "'");

        TextMessage message = session.createTextMessage(info);
        topicProducer.send(message);
    }

    /**
     * Close down the JMS connection
     * This method could be exported to the web if needed, but it probably
     * doesn't make much sense unless it's in an admin role
     */
    public synchronized void close() throws JMSException
    {
        topicConsumer.close();
        topicProducer.close();

        session.close();
        connection.close();
        open = false;
    }

    /**
     * The Default Connection Factory
     */
    private static final String FACTORY_CLASSNAME = "org.directwebremoting.jms.DwrConnectionFactory";

    /**
     * The default implementation just hard codes a topic name
     */
    private static final String TOPIC_NAME = "org.getahead.dwrdemo.pubsub.testtopic";

    /**
     * The default implementation just hard codes a client id
     */
    private static final String CLIENT_ID = "org.getahead.dwrdemo.pubsub.testservice";

    /**
     * Are we open for business
     */
    private boolean open = false;

    /**
     * The JMS connection
     */
    private Connection connection;

    /**
     * The train of messages that we send/receive
     */
    private Session session;

    /**
     * The topic that we subscribe/publish to
     */
    private Topic topic;

    /**
     * The route to publishing messages to the topic
     */
    private MessageProducer topicProducer;

    /**
     * The route to getting to messages sent to the topic
     */
    protected MessageConsumer topicConsumer;

    /**
     * The log stream
     */
    protected static final Log log = LogFactory.getLog(JmsTest.class);

    static
    {
        try
        {
            org.directwebremoting.jms.DwrConnectionFactory factory = new org.directwebremoting.jms.DwrConnectionFactory();
            org.directwebremoting.jms.DwrConnection connection = factory.createConnection();
            connection.setClientID(CLIENT_ID);
            connection.setExceptionListener(new ExceptionListener()
            {
                public void onException(JMSException ex)
                {
                    log.warn("JMS Failure", ex);
                }
            });
            org.directwebremoting.jms.DwrSession session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            org.directwebremoting.jms.DwrTopic topic = session.createTopic(TOPIC_NAME);
    
            org.directwebremoting.jms.DwrMessageProducer topicProducer = session.createProducer(topic);
            org.directwebremoting.jms.DwrMessageConsumer topicConsumer = session.createConsumer(topic);
            topicConsumer.setMessageListener(new MessageListener()
            {
                public void onMessage(Message message)
                {
                    if (message instanceof TextMessage)
                    {
                        TextMessage textMessage = (TextMessage) message;
                        try
                        {
                            log.info(textMessage.getText());
                        }
                        catch (JMSException ex)
                        {
                            log.error("Failed to get text from message", ex);
                        }
                    }
                    else
                    {
                        log.info("Not sure how to convert message to string for type " + message.getClass().getName());
                    }
                }
            });
            connection.start();
    
            String info = "JmsTest says nothing";
            log.info("Publishing message '" + info + "' to '" + topic.getTopicName() + "'");
    
            org.directwebremoting.jms.DwrMessage message = session.createTextMessage(info);
            topicProducer.send(message);
    
            topicConsumer.close();
            topicProducer.close();
    
            session.close();
            connection.close();
        }
        catch (JMSException ex)
        {
        }
    }
}
