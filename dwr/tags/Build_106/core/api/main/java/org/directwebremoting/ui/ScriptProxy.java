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
