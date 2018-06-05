package org.directwebremoting.jms;

import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;

/**
 * A {@link MessageListener} especially for {@link DwrMessageConsumer} that
 * enables us to block waiting for a message.
 * <p>What happens if we try to do a blocking read while there is a non-blocking
 * MessageListener waiting? The JavaDoc says the read can be one or the other so
 * we assume that we don't need to deliver to both syncs.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class BlockingMessageListener implements MessageListener
{
    /* (non-Javadoc)
     * @see javax.jms.MessageListener#onMessage(javax.jms.Message)
     */
    public void onMessage(Message newMessage)
    {
        this.message = newMessage;
        synchronized (lock)
        {
            lock.notifyAll();
        }
    }

    /**
     * @see MessageConsumer#receive(long)
     */
    public Message receive(long timeout)
    {
        try
        {
            synchronized (lock)
            {
                lock.wait(timeout);
            }
        }
        catch (InterruptedException ex)
        {
            // restore interrupt flag
            Thread.currentThread().interrupt();
        }

        return message;
    }

    /**
     * The received message
     */
    private Message message;

    /**
     * The object to wait on
     */
    private Object lock = new Object();
}
