package org.directwebremoting.proxy;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.directwebremoting.ScriptBuffer;
import org.directwebremoting.ScriptSession;

/**
 * Class to help people send scripts to collections of browsers.
 * ScriptProxy also is the base class for the Java implementations of GI, Util
 * and Script.aculo.us.Effect.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 * @deprecated Use org.directwebremoting.ui.ScriptProxy
 * @see org.directwebremoting.ui.ScriptProxy
 */
@Deprecated
public class ScriptProxy
{
    /**
     * Http thread constructor
     */
    public ScriptProxy()
    {
    }

    /**
     * Http thread constructor
     * @param scriptSession The browser to alter
     */
    public ScriptProxy(ScriptSession scriptSession)
    {
        scriptSessions.add(scriptSession);
    }

    /**
     * Non-http thread constructor
     * @param scriptSessions The browsers to alter
     */
    public ScriptProxy(Collection<ScriptSession> scriptSessions)
    {
        this.scriptSessions.addAll(scriptSessions);
    }

    /**
     * @param scriptSession The script session to add to the list
     */
    public void addScriptSession(ScriptSession scriptSession)
    {
        scriptSessions.add(scriptSession);
    }

    /**
     * @param addScriptSessions The script sessions to add to the list
     */
    public void addScriptSessions(Collection<ScriptSession> addScriptSessions)
    {
        scriptSessions.addAll(addScriptSessions);
    }

    /**
     * Call a named function with no parameters.
     * @param funcName The name of the function to call
     */
    public void addFunctionCall(String funcName)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendScript(funcName)
              .appendScript("();");
        addScript(script);
    }

    /**
     * Call a named function with one parameter.
     * @param funcName The name of the function to call
     * @param param1 The first parameter to the above function
     */
    public void addFunctionCall(String funcName, Object param1)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendScript(funcName)
              .appendScript("(")
              .appendData(param1)
              .appendScript(");");
        addScript(script);
    }

    /**
     * Call a named function with one parameter.
     * @param funcName The name of the function to call
     * @param param1 The first parameter to the above function
     * @param param2 The second parameter to the above function
     */
    public void addFunctionCall(String funcName, Object param1, Object param2)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendScript(funcName)
              .appendScript("(")
              .appendData(param1)
              .appendScript(",")
              .appendData(param2)
              .appendScript(");");
        addScript(script);
    }

    /**
     * Call a named function with one parameter.
     * @param funcName The name of the function to call
     * @param param1 The first parameter to the above function
     * @param param2 The second parameter to the above function
     * @param param3 The third parameter to the above function
     */
    public void addFunctionCall(String funcName, Object param1, Object param2, Object param3)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendScript(funcName)
              .appendScript("(")
              .appendData(param1)
              .appendScript(",")
              .appendData(param2)
              .appendScript(",")
              .appendData(param3)
              .appendScript(");");
        addScript(script);
    }

    /**
     * Call a named function with one parameter.
     * @param funcName The name of the function to call
     * @param param1 The first parameter to the above function
     * @param param2 The second parameter to the above function
     * @param param3 The third parameter to the above function
     * @param param4 The fourth parameter to the above function
     */
    public void addFunctionCall(String funcName, Object param1, Object param2, Object param3, Object param4)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendScript(funcName)
              .appendScript("(")
              .appendData(param1)
              .appendScript(",")
              .appendData(param2)
              .appendScript(",")
              .appendData(param3)
              .appendScript(",")
              .appendData(param4)
              .appendScript(");");
        addScript(script);
    }

    /**
     * Call a named function with one parameter.
     * @param funcName The name of the function to call
     * @param param1 The first parameter to the above function
     * @param param2 The second parameter to the above function
     * @param param3 The third parameter to the above function
     * @param param4 The fourth parameter to the above function
     * @param param5 The fifth parameter to the above function
     */
    public void addFunctionCall(String funcName, Object param1, Object param2, Object param3, Object param4, Object param5)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendScript(funcName)
              .appendScript("(")
              .appendData(param1)
              .appendScript(",")
              .appendData(param2)
              .appendScript(",")
              .appendData(param3)
              .appendScript(",")
              .appendData(param4)
              .appendScript(",")
              .appendData(param5)
              .appendScript(");");
        addScript(script);
    }

    /**
     * Call a named function with one parameter.
     * @param funcName The name of the function to call
     * @param param1 The first parameter to the above function
     * @param param2 The second parameter to the above function
     * @param param3 The third parameter to the above function
     * @param param4 The fourth parameter to the above function
     * @param param5 The fifth parameter to the above function
     * @param param6 The sixth parameter to the above function
     */
    public void addFunctionCall(String funcName, Object param1, Object param2, Object param3, Object param4, Object param5, Object param6)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendScript(funcName)
              .appendScript("(")
              .appendData(param1)
              .appendScript(",")
              .appendData(param2)
              .appendScript(",")
              .appendData(param3)
              .appendScript(",")
              .appendData(param4)
              .appendScript(",")
              .appendData(param5)
              .appendScript(",")
              .appendData(param6)
              .appendScript(");");
        addScript(script);
    }

    /**
     * Call a named function with one parameter.
     * @param funcName The name of the function to call
     * @param param1 The first parameter to the above function
     * @param param2 The second parameter to the above function
     * @param param3 The third parameter to the above function
     * @param param4 The fourth parameter to the above function
     * @param param5 The fifth parameter to the above function
     * @param param6 The sixth parameter to the above function
     * @param param7 The seventh parameter to the above function
     */
    public void addFunctionCall(String funcName, Object param1, Object param2, Object param3, Object param4, Object param5, Object param6, Object param7)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendScript(funcName)
              .appendScript("(")
              .appendData(param1)
              .appendScript(",")
              .appendData(param2)
              .appendScript(",")
              .appendData(param3)
              .appendScript(",")
              .appendData(param4)
              .appendScript(",")
              .appendData(param5)
              .appendScript(",")
              .appendData(param6)
              .appendScript(",")
              .appendData(param7)
              .appendScript(");");
        addScript(script);
    }

    /**
     * Utility to add the given script to all known browsers.
     * @param script The Javascript to send to the browsers
     */
    public void addScript(ScriptBuffer script)
    {
        for (ScriptSession scriptSession : scriptSessions)
        {
            scriptSession.addScript(script);
        }
    }

    /**
     * The browsers that we affect.
     */
    private final List<ScriptSession> scriptSessions = new ArrayList<ScriptSession>();
}
