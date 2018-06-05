package org.directwebremoting.event;

import java.util.EventListener;

/**
 * Implementations of this interface are notified of changes to the list of
 * active sessions in a web application.
 * @see ScriptSessionEvent
 * @see javax.servlet.http.HttpSessionListener
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public interface ScriptSessionListener extends EventListener
{
    /**
     * Notification that a session was created.
     * @param ev the notification event
     */
    public void sessionCreated(ScriptSessionEvent ev);

    /**
     * Notification that a session is about to be invalidated.
     * @param ev the notification event
     */
    public void sessionDestroyed(ScriptSessionEvent ev);
}
