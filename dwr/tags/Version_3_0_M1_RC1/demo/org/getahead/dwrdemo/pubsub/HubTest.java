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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.directwebremoting.HubFactory;
import org.directwebremoting.event.MessageEvent;
import org.directwebremoting.event.MessageListener;
import org.directwebremoting.extend.MarshallException;
import org.getahead.dwrdemo.people.Person;

/**
 * A demo of the pub/sub side of JMS.
 * Currently DWR does not support point-to-point JMS because there doesn't seem
 * much point - web clients are inherently fickle, so getting the address of a
 * browser out of JNDI seems silly
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class HubTest
{
    /**
     * Setup the hub and create a subscriber listener
     */
    public HubTest()
    {
        HubFactory.get().subscribe(TOPIC_TEXT, new MessageListener()
        {
            public void onMessage(MessageEvent message)
            {
                try
                {
                    log.info(message.getData(String.class));
                }
                catch (MarshallException ex)
                {
                    log.info("Failed to read data published to " + TOPIC_TEXT);
                }
            }
        });

        HubFactory.get().subscribe(TOPIC_PEOPLE, new MessageListener()
        {
            public void onMessage(MessageEvent message)
            {
                try
                {
                    log.info(message.getData(Person.class));
                }
                catch (MarshallException ex)
                {
                    log.info("Failed to read data published to " + TOPIC_TEXT);
                }
            }
        });
    }

    /**
     * Exported method to publish a string to a topic
     */
    public void publishText(String data)
    {
        String info = "JmsTest says " + data;
        log.info("Publishing message '" + info + "' to '" + TOPIC_TEXT + "'");
        HubFactory.get().publish(TOPIC_TEXT, info);
    }

    /**
     * Exported method to publish a bean to a topic
     */
    public void publishPerson(Person person)
    {
        log.info("Publishing person '" + person.getName() + "' to '" + TOPIC_PEOPLE + "'");
        HubFactory.get().publish(TOPIC_PEOPLE, person);
    }

    /**
     * The default implementation just hard codes a topic name
     */
    private static final String TOPIC_TEXT = "pubsub.topicText";

    /**
     * The default implementation just hard codes a topic name
     */
    private static final String TOPIC_PEOPLE = "pubsub.topicPeople";

    /**
     * The log stream
     */
    protected static final Log log = LogFactory.getLog(HubTest.class);
}
