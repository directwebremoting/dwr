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
package org.directwebremoting.proxy.browser;

import java.util.Collection;

import org.directwebremoting.ScriptSession;
import org.directwebremoting.proxy.ScriptProxy;

/**
 * 
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class Window extends ScriptProxy
{
    /**
     * Http thread constructor, that affects no browsers.
     * Calls to {@link Window#addScriptSession(ScriptSession)} or to
     * {@link Window#addScriptSessions(Collection)} will be needed
     */
    public Window()
    {
    }

    /**
     * Http thread constructor that alters a single browser
     * @param scriptSession The browser to alter
     */
    public Window(ScriptSession scriptSession)
    {
        super(scriptSession);
    }

    /**
     * Http thread constructor that alters a number of browsers
     * @param scriptSessions A collection of ScriptSessions that we should act on.
     */
    public Window(Collection<ScriptSession> scriptSessions)
    {
        super(scriptSessions);
    }

    /**
     * Show in an 'alert' dialog
     * @param message The text to go into the alert box
     */
    public void alert(String message)
    {
        addFunctionCall("alert", message);
    }
}
