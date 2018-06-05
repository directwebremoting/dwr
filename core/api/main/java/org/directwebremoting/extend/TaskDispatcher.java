package org.directwebremoting.extend;

import java.util.Collection;

import org.directwebremoting.ScriptSession;
import org.directwebremoting.ScriptSessionFilter;

/**
 * We would like to do some reverse ajax work, but need something to find the
 * ScriptSessions add act on them all.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public interface TaskDispatcher
{
    /**
     * Take a runnable action and run it against every {@link ScriptSession}
     * that matches a specified filter.
     */
    public void dispatchTask(ScriptSessionFilter filter, Runnable task);

    /**
     * This method discovers the sessions that are currently being targeted
     * by browser updates.
     * <p>
     * It will generally only be useful to authors of reverse ajax UI proxy
     * APIs. Using it directly may cause scaling problems
     * @return The list of current browser windows.
     */
    public Collection<ScriptSession> getTargetSessions();
}
