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
package org.directwebremoting.impl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.directwebremoting.Hub;
import org.directwebremoting.event.MessageEvent;
import org.directwebremoting.event.MessageListener;

/**
 * DWR's default implementation of {@link Hub}
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class DefaultHub implements Hub
{
    /* (non-Javadoc)
     * @see org.directwebremoting.Hub#subscribe(java.lang.String, org.directwebremoting.event.MessageListener)
     */
    public void subscribe(String topicName, MessageListener listener)
    {
        synchronized (subscriptions)
        {
            Set<MessageListener> listeners = subscriptions.get(topicName);
            if (listeners == null)
            {
                listeners = new HashSet<MessageListener>();
                subscriptions.put(topicName, listeners);
            }

            listeners.add(listener);
        }
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.Hub#unsubscribe(java.lang.String, org.directwebremoting.event.MessageListener)
     */
    public boolean unsubscribe(String topicName, MessageListener listener)
    {
        synchronized (subscriptions)
        {
            boolean unsubscribed = false;

            Set<MessageListener> listeners = subscriptions.get(topicName);
            if (listeners != null)
            {
                unsubscribed = listeners.remove(listener);
                if (listeners.isEmpty())
                {
                    subscriptions.remove(topicName);
                }
            }

            return unsubscribed;
        }
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.Hub#publish(java.lang.String, java.lang.Object)
     */
    public void publish(String topicName, Object data)
    {
        // First clone the list of subscriptions
        Set<MessageListener> listeners;
        synchronized (subscriptions)
        {
            Set<MessageListener> current = subscriptions.get(topicName);
            if (current == null)
            {
                return;
            }

            listeners = new HashSet<MessageListener>();
            listeners.addAll(current);
        }

        // Then tell everyone about the message
        for (MessageListener listener : listeners)
        {
            MessageEvent event;
            if (data instanceof MessageEvent)
            {
                event = (MessageEvent) data;
            }
            else
            {
                event = new MessageEvent(this, data);
            }
            listener.onMessage(event);
        }
    }

    /**
     * The cache of current subscriptions
     */
    private final Map<String, Set<MessageListener>> subscriptions = new HashMap<String, Set<MessageListener>>();
}
