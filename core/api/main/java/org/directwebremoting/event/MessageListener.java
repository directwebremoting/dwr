package org.directwebremoting.event;

import java.util.EventObject;

/**
 * Most listeners have a parameter that inherits from {@link EventObject}, but
 * not JMS which just uses a 
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public interface MessageListener
{
    /**
     * Passes a message to the listener.
     * @param message the message passed to the listener
     */
    void onMessage(MessageEvent message);
}
