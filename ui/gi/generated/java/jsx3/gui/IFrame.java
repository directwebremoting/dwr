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
package jsx3.gui;

import org.directwebremoting.ScriptBuffer;
import org.directwebremoting.ScriptSessions;
import org.directwebremoting.io.Context;

/**
 * Renders an IFrame.
 * @author Joe Walker [joe at getahead dot org]
 * @author DRAPGEN - Dwr Reverse Ajax Proxy GENerator
 */
public class IFrame extends jsx3.gui.Block
{
    /**
     * All reverse ajax proxies need context to work from
     * @param context The script that got us to where we are now
     */
    public IFrame(Context context, String extension)
    {
        super(context, extension);
    }


    /**
     * 
     */
    public static final int SCROLLYES = 1;

    /**
     * 
     */
    public static final int SCROLLNO = 2;

    /**
     * 
     */
    public static final int SCROLLAUTO = 3;


    /**
     * Returns the native iframe object of this iframe. Depending on browser security settings and the URL of this
iframe, the native iframe object may not be available. In this case, this method returns null.
     */

    public void getIFrame(org.directwebremoting.ui.Callback<String> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getIFrame");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, String.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Returns the native document object of this iframe. Depending on browser security settings and the URL of this
iframe, the native document object may not be available. In this case, this method returns null.
     */

    public void getContentDocument(org.directwebremoting.ui.Callback<String> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getContentDocument");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, String.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Returns the URI of this iframe.
     */

    public void getSrc(org.directwebremoting.ui.Callback<String> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getSrc");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, String.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Sets the URI of this iframe. The URI can be absolute or relative from the content base of the server that
owns this object. If this iframe is rendered on screen, its location is updated immediately.
     * @param srcSrc
     * @return this object.
     */
    public jsx3.gui.IFrame setSrc(String srcSrc)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setSrc", srcSrc);
        ScriptSessions.addScript(script);
        return this;
    }

    /**
     * Returns the scroll mode of this iframe.
     */

    public void getScrolling(org.directwebremoting.ui.Callback<Integer> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getScrolling");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, Integer.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Sets the scroll mode of this iframe.
     * @param intScrolling one of <code>SCROLLYES</code>, <code>SCROLLNO</code>, or <code>SCROLLAUTO</code>.
     * @return this object.
     */
    public jsx3.gui.IFrame setScrolling(int intScrolling)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setScrolling", intScrolling);
        ScriptSessions.addScript(script);
        return this;
    }

}

