package org.directwebremoting;

import java.util.Collection;

import org.directwebremoting.event.ScriptSessionBindingListener;

/**
 * A class to act on the current {@link ScriptSession}(s).
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class ScriptSessions
{
    /**
     * Binds an object to this session, using the name specified.
     * If an object of the same name is already bound to the session, the
     * object is replaced.
     * <p>After this method executes, and if the new object implements
     * {@link ScriptSessionBindingListener}, the container calls
     * {@link ScriptSessionBindingListener#valueBound}.
     * <p>If an object was already bound to this session of this name that
     * implements {@link ScriptSessionBindingListener}, its
     * {@link ScriptSessionBindingListener#valueUnbound} method is called.
     * <p>If the value passed in is null, this has the same effect as calling
     * {@link #removeAttribute}.
     * @param name the name to which the object is bound; cannot be null
     * @param value the object to be bound
     * @throws IllegalStateException if the page has been invalidated
     */
    public static void setAttribute(String name, Object value)
    {
        Collection<ScriptSession> sessions = Browser.getTargetSessions();
        for (ScriptSession scriptSession : sessions)
        {
            scriptSession.setAttribute(name, value);
        }
    }

    /**
     * Removes the object bound with the specified name from this session.
     * If the session does not have an object bound with the specified name,
     * this method does nothing.
     * <p>After this method executes, and if the object implements
     * {@link ScriptSessionBindingListener}, the container calls
     * {@link ScriptSessionBindingListener#valueUnbound}.
     * @param name the name of the object to remove from this session
     * @throws IllegalStateException if the page has been invalidated
     */
    public static void removeAttribute(String name)
    {
        Collection<ScriptSession> sessions = Browser.getTargetSessions();
        for (ScriptSession scriptSession : sessions)
        {
            scriptSession.removeAttribute(name);
        }
    }

    /**
     * Add a script to the list waiting for remote execution.
     * @param script The script to execute
     */
    public static void addScript(ScriptBuffer script)
    {
        Collection<ScriptSession> sessions = Browser.getTargetSessions();
        for (ScriptSession scriptSession : sessions)
        {
            scriptSession.addScript(script);
        }
    }

    /**
     * Utility to add the given script to all known browsers.
     * This version automatically wraps the string in a ClientScript object.
     * @param scriptString The Javascript to send to the browsers
     */
    public static void addScript(String scriptString)
    {
        addScript(new ScriptBuffer(scriptString));
    }

    /**
     * Call a named function with one parameter.
     * @param funcName The name of the function to call
     * @param params The parameters to the above function
     */
    public static void addFunctionCall(String funcName, Object... params)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendScript(funcName).appendScript("(");

        for (int i = 0; i < params.length; i++)
        {
            if (i != 0)
            {
                script.appendScript(",");
            }
            script.appendData(params[i]);
        }

        script.appendScript(");");
        addScript(script);
    }
}
