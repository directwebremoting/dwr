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
package org.directwebremoting.proxy;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.directwebremoting.ScriptBuffer;
import org.directwebremoting.ScriptSession;

/**
 * Class to help people send scripts to collections of browsers.
 * ScriptProxy also is the base class for the Java implementations of Util
 * and Scriptaculous.Effect.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
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
    public ScriptProxy(Collection scriptSessions)
    {
        this.scriptSessions.addAll(scriptSessions);
    }

    /**
     * @param scriptSession
     */
    public void addScriptSession(ScriptSession scriptSession)
    {
        scriptSessions.add(scriptSession);
    }

    /**
     * @param addScriptSessions
     */
    public void addScriptSessions(Collection addScriptSessions)
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
     * @param param4 The fouth parameter to the above function
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
     * Utility to add the given script to all known browsers.
     * @param script The Javascript to send to the browsers
     */
    public void addScript(ScriptBuffer script)
    {
        for (Iterator it = scriptSessions.iterator(); it.hasNext();)
        {
            ScriptSession scriptSession = (ScriptSession) it.next();
            scriptSession.addScript(script);
        }
    }

    /**
     * The browsers that we affect.
     */
    private final List scriptSessions = new ArrayList();
}
