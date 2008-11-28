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
 * similar to how a tabbed pane manages a collection of tabs, the stack group is a parent container that manages JSXStack instances
 * @author Joe Walker [joe at getahead dot org]
 * @author DRAPGEN - Dwr Reverse Ajax Proxy GENerator
 */
public class StackGroup extends jsx3.gui.LayoutGrid
{
    /**
     * All reverse ajax proxies need context to work from
     * @param context The script that got us to where we are now
     */
    public StackGroup(Context context, String extension)
    {
        super(context, extension);
    }

    /**
     * The instance initializer.
     * @param strName unique name distinguishing this object from all other JSX GUI objects in the JSX application
     */
    public StackGroup(String strName)
    {
        super((Context) null, (String) null);
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall("new StackGroup", strName);
        setInitScript(script);
    }


    /**
     * 0 : top-over (--) layout (default)
     */
    public static final int ORIENTATIONV = 0;

    /**
     * 1 : side-by-side (|) layout
     */
    public static final int ORIENTATIONH = 1;

    /**
     * 27 (default)
     */
    public static final int DEFAULTBARSIZE = 27;


    /**
     * Returns the size of the handle common to all child stack instances (in pixels). Default: jsx3.gui.StackGroup.DEFAULTBARSIZE
     * @param callback size in pixels
     */

    public void getBarSize(org.directwebremoting.ui.Callback<Integer> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getBarSize");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, Integer.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Sets the size of the handle for the child stack instances;
           returns reference to self to facilitate method chaining
     * @param intBarSize size (in pixels)
     * @return this object
     */
    public jsx3.gui.StackGroup setBarSize(int intBarSize)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setBarSize", intBarSize);
        ScriptSessions.addScript(script);
        return this;
    }

    /**
     * Returns zero-based index for the tab that is active per its placement in the child JScript array
     */

    public void getSelectedIndex(org.directwebremoting.ui.Callback<Integer> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getSelectedIndex");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, Integer.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

}

