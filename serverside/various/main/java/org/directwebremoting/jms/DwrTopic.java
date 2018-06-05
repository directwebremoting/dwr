package org.directwebremoting.jms;

import javax.jms.JMSException;
import javax.jms.Topic;

/**
 * A {@link Topic} for DWR
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class DwrTopic implements Topic
{
    /**
     * @see javax.jms.Session#createTopic(String)
     */
    public DwrTopic(String topicName)
    {
        this.topicName = topicName;
    }

    /* (non-Javadoc)
     * @see javax.jms.Topic#getTopicName()
     */
    public String getTopicName() throws JMSException
    {
        return topicName;
    }

    /**
     * The name of this topic
     */
    private String topicName;
}
