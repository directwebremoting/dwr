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
package org.directwebremoting.ui;

import java.util.Collection;

import org.directwebremoting.Browser;
import org.directwebremoting.ScriptBuffer;
import org.directwebremoting.ScriptSession;

/**
 * Class to help people send scripts to collections of browsers.
 * ScriptProxy also is the base class for the Java implementations of GI, Util
 * and Script.aculo.us.Effect.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public final class ScriptProxy
{
    /**
     * Call a named function with no parameters.
     * @param funcName The name of the function to call
     */
    public static void addFunctionCall(String funcName)
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
    public static void addFunctionCall(String funcName, Object param1)
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
    public static void addFunctionCall(String funcName, Object param1, Object param2)
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
    public static void addFunctionCall(String funcName, Object param1, Object param2, Object param3)
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
    public static void addFunctionCall(String funcName, Object param1, Object param2, Object param3, Object param4)
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
    public static void addFunctionCall(String funcName, Object param1, Object param2, Object param3, Object param4, Object param5)
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
    public static void addFunctionCall(String funcName, Object param1, Object param2, Object param3, Object param4, Object param5, Object param6)
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
    public static void addFunctionCall(String funcName, Object param1, Object param2, Object param3, Object param4, Object param5, Object param6, Object param7)
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
    public static void addScript(ScriptBuffer script)
    {
        Collection<ScriptSession> sessions = Browser.getTargetSessions();
        for (ScriptSession scriptSession : sessions)
        {
            scriptSession.addScript(script);
        }
    }
}
