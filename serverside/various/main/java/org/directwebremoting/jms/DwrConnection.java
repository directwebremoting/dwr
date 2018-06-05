package org.directwebremoting.jms;

import javax.jms.Connection;
import javax.jms.ConnectionConsumer;
import javax.jms.Destination;
import javax.jms.ExceptionListener;
import javax.jms.JMSException;
import javax.jms.ServerSessionPool;
import javax.jms.Session;
import javax.jms.Topic;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.directwebremoting.ServerContext;

/**
 * An implementation of {@link Connection} for DWR
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class DwrConnection implements Connection
{
    /* (non-Javadoc)
     * @see javax.jms.Connection#createSession(boolean, int)
     */
    public DwrSession createSession(boolean transacted, int acknowledgeMode) throws JMSException
    {
        if (transacted)
        {
            log.error("transacted == true is not currently supported");
        }

        if (acknowledgeMode != Session.AUTO_ACKNOWLEDGE)
        {
            log.error("acknowledgeMode != Session.AUTO_ACKNOWLEDGE is not currently supported");
        }

        return new DwrSession(this, transacted, acknowledgeMode);
    }

    /* (non-Javadoc)
     * @see javax.jms.Connection#createConnectionConsumer(javax.jms.Destination, java.lang.String, javax.jms.ServerSessionPool, int)
     */
    public ConnectionConsumer createConnectionConsumer(Destination destination, String messageSelector, ServerSessionPool sessionPool, int maxMessages) throws JMSException
    {
        throw Unsupported.noConnectionConsumers();
    }

    /* (non-Javadoc)
     * @see javax.jms.Connection#createDurableConnectionConsumer(javax.jms.Topic, java.lang.String, java.lang.String, javax.jms.ServerSessionPool, int)
     */
    public ConnectionConsumer createDurableConnectionConsumer(Topic topic, String subscriptionName, String messageSelector, ServerSessionPool sessionPool, int maxMessages) throws JMSException
    {
        throw Unsupported.noConnectionConsumers();
    }

    /* (non-Javadoc)
     * @see javax.jms.Connection#getMetaData()
     */
    public DwrConnectionMetaData getMetaData() throws JMSException
    {
        return new DwrConnectionMetaData();
    }

    /* (non-Javadoc)
     * @see javax.jms.Connection#start()
     */
    public void start() throws JMSException
    {
        if (state == State.CLOSED)
        {
            throw new JMSException("Connection has been closed");
        }

        state = State.STARTED;
    }

    /* (non-Javadoc)
     * @see javax.jms.Connection#stop()
     */
    public void stop() throws JMSException
    {
        if (state == State.CLOSED)
        {
            throw new JMSException("Connection has been closed");
        }

        state = State.STOPPED;
    }

    /* (non-Javadoc)
     * @see javax.jms.Connection#close()
     */
    public void close() throws JMSException
    {
        state = State.CLOSED;
    }

    /* (non-Javadoc)
     * @see javax.jms.Connection#setClientID(java.lang.String)
     */
    public void setClientID(String clientId) throws JMSException
    {
        this.clientId = clientId;
    }

    /* (non-Javadoc)
     * @see javax.jms.Connection#getClientID()
     */
    public String getClientID() throws JMSException
    {
        return clientId;
    }

    /* (non-Javadoc)
     * @see javax.jms.Connection#setExceptionListener(javax.jms.ExceptionListener)
     */
    public void setExceptionListener(ExceptionListener listener) throws JMSException
    {
        this.listener = listener;
    }

    /* (non-Javadoc)
     * @see javax.jms.Connection#getExceptionListener()
     */
    public ExceptionListener getExceptionListener() throws JMSException
    {
        return listener;
    }

    /**
     * Children need to know if they can send messages.
     * @return Has {@link Connection#start()} been called
     */
    public State getState()
    {
        return state;
    }

    /**
     * @return the servletContext
     */
    public ServerContext getServerContext()
    {
        return serverContext;
    }

    /**
     * @param serverContext the servletContext to set
     */
    public void setServerContext(ServerContext serverContext)
    {
        this.serverContext = serverContext;
    }

    /**
     * Our connection to the DWR infrastructure
     */
    private ServerContext serverContext;

    /**
     * What's the current state of the current connection?
     */
    private State state = State.STOPPED;

    /**
     * The ID of this client
     */
    private String clientId;

    /**
     * If something goes wrong we call this
     */
    private ExceptionListener listener;

    /**
     * The log stream
     */
    private static final Log log = LogFactory.getLog(DwrConnection.class);
}
