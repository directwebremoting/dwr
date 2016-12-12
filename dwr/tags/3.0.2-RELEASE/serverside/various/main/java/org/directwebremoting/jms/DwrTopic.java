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
