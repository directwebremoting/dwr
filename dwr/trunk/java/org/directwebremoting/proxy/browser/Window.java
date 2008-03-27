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

import java.net.URI;
import java.net.URL;
import java.util.Collection;

import org.directwebremoting.ScriptBuffer;
import org.directwebremoting.ScriptSession;
import org.directwebremoting.extend.CallbackHelper;
import org.directwebremoting.proxy.Callback;
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

    /**
     * Show a 'confirm' dialog
     * @param message The text to go into the alert box
     * @param callback The function to be called when a browser replies
     */
    public void confirm(String message, Callback<Boolean> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + "confirm", message);

        if (callback != null)
        {
            String key = CallbackHelper.saveCallback(callback, Boolean.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        addScript(script);
    }

   /**
     * Show a 'prompt' dialog
     * @param message The text to go into the alert box
     * @param callback The function to be called when a browser replies
     */
    public void prompt(String message, Callback<String> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + "prompt", message);

        if (callback != null)
        {
            String key = CallbackHelper.saveCallback(callback, String.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        addScript(script);
    }

    /**
     * Attempt to close this window
     */
    public void close()
    {
        addFunctionCall("close");
    }

    /**
     * Navigate to a new page
     * @param newPage The page to navigate to
     */
    public void setLocation(URI newPage)
    {
        setLocation(newPage.toASCIIString());
    }

    /**
     * Navigate to a new page
     * @param newPage The page to navigate to
     */
    public void setLocation(URL newPage)
    {
        setLocation(newPage.toExternalForm());
    }

    /**
     * Navigate to a new page
     * @param newPage The page to navigate to
     */
    public void setLocation(String newPage)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendData("window.location = '" + newPage + "';");
        addScript(script);
    }

    /*
     * This would be good, but it doesn't work on some browsers
     * @param title The page to navigate to
     *
    public void setTitle(String title)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendData("window.document.title = '" + title + "';");
        addScript(script);
    }
    */
}
