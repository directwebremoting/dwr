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
package org.directwebremoting.ui.browser;

import java.net.URI;
import java.net.URL;

import org.directwebremoting.ScriptBuffer;
import org.directwebremoting.extend.CallbackHelperFactory;
import org.directwebremoting.ui.Callback;
import org.directwebremoting.ui.ScriptProxy;

/**
 * 
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class Window
{
    /**
     * Show in an 'alert' dialog
     * @param message The text to go into the alert box
     */
    public static void alert(String message)
    {
        ScriptProxy.addFunctionCall("alert", message);
    }

    /**
     * Show a 'confirm' dialog
     * @param message The text to go into the alert box
     * @param callback The function to be called when a browser replies
     */
    public static void confirm(String message, Callback<Boolean> callback)
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
            String key = CallbackHelperFactory.saveCallback(callback, Boolean.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptProxy.addScript(script);
    }

   /**
     * Show a 'prompt' dialog
     * @param message The text to go into the alert box
     * @param callback The function to be called when a browser replies
     */
    public static void prompt(String message, Callback<String> callback)
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
            String key = CallbackHelperFactory.saveCallback(callback, String.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptProxy.addScript(script);
    }

    /**
     * Attempt to close this window
     */
    public static void close()
    {
        ScriptProxy.addFunctionCall("close");
    }

    /**
     * Navigate to a new page
     * @param newPage The page to navigate to
     */
    public static void setLocation(URI newPage)
    {
        setLocation(newPage.toASCIIString());
    }

    /**
     * Navigate to a new page
     * @param newPage The page to navigate to
     */
    public static void setLocation(URL newPage)
    {
        setLocation(newPage.toExternalForm());
    }

    /**
     * Navigate to a new page
     * @param newPage The page to navigate to
     */
    public static void setLocation(String newPage)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendData("window.location = '" + newPage + "';");

        ScriptProxy.addScript(script);
    }

    /*
     * This would be good, but it doesn't work on some browsers
     * @param title The page to navigate to
     *
    public static void setTitle(String title)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendData("window.document.title = '" + title + "';");

        Collection<ScriptSession> sessions = Browser.getTargetSessions();
        ScriptProxy proxy = new ScriptProxy(sessions);
        proxy.addScript(script);
    }
    */
}
