package org.directwebremoting.event;

import java.util.EventObject;

import org.directwebremoting.ScriptSession;

/**
 * This is the class representing event notifications for changes to script
 * sessions within a web application.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class ScriptSessionEvent extends EventObject
{
    /**
     * Construct a session event from the given source.
     * @param source The ScriptSession that is being created/destroyed
     */
    public ScriptSessionEvent(ScriptSession source)
    {
        super(source);
    }

    /**
     * Return the session that changed.
     * @return The newly created/destroyed ScriptSession
     */
    public ScriptSession getSession()
    {
        return (ScriptSession) super.getSource();
    }
}
