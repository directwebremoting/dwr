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
