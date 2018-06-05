package org.directwebremoting.ui;

import org.directwebremoting.ScriptBuffer;
import org.directwebremoting.ScriptSessions;

/**
 * Class to help people send scripts to collections of browsers.
 * ScriptProxy also is the base class for the Java implementations of GI, Util
 * and Script.aculo.us.Effect.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 * @deprecated
 */
@Deprecated
public final class ScriptProxy
{
    /**
     * Call a named function with one parameter.
     * @param funcName The name of the function to call
     * @param params The parameters to the above function
     */
    public static void addFunctionCall(String funcName, Object... params)
    {
        ScriptSessions.addFunctionCall(funcName, params);
    }

    /**
     * Utility to add the given script to all known browsers.
     * @param script The Javascript to send to the browsers
     */
    public static void addScript(ScriptBuffer script)
    {
        ScriptSessions.addScript(script);
    }

    /**
     * Utility to add the given script to all known browsers.
     * @param script The Javascript to send to the browsers
     */
    public static void addScript(String script)
    {
        ScriptSessions.addScript(script);
    }
}
