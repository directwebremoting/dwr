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
 * Renders a tabbed pane, which consists of a set of tabs, only one of which is visible at a time.
 * @author Joe Walker [joe at getahead dot org]
 * @author DRAPGEN - Dwr Reverse Ajax Proxy GENerator
 */
public class TabbedPane extends jsx3.gui.Block
{
    /**
     * All reverse ajax proxies need context to work from
     * @param context The script that got us to where we are now
     */
    public TabbedPane(Context context, String extension)
    {
        super(context, extension);
    }

    /**
     * instance initializer
     * @param strName unique name distinguishing this object from all other JSX GUI objects in the JSX application
     */
    public TabbedPane(String strName)
    {
        super((Context) null, (String) null);
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall("new TabbedPane", strName);
        setInitScript(script);
    }


    /**
     * 50
     */
    public static final int AUTO_SCROLL_INTERVAL = 50;

    /**
     * jsx:///images/tab/l.png
     */
    public static final String LSCROLLER = null;

    /**
     * jsx:///images/tab/r.png
     */
    public static final String RSCROLLER = null;

    /**
     * 20 (default)
     */
    public static final int DEFAULTTABHEIGHT = 20;


    /**
     * Returns the zero-based child index of the active child tab.
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

    /**
     * Sets the active tab of this tabbed pane. Pass either the zero-based child index of the tab to activate or
the tab itself.
     * @param intIndex
     * @param bRepaint if <code>true</code>, immediately updates the view to reflect the new active tab.
     * @return this object.
     */
    public jsx3.gui.TabbedPane setSelectedIndex(jsx3.gui.Tab intIndex, boolean bRepaint)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setSelectedIndex", intIndex, bRepaint);
        ScriptSessions.addScript(script);
        return this;
    }

    /**
     * Sets the active tab of this tabbed pane. Pass either the zero-based child index of the tab to activate or
the tab itself.
     * @param intIndex
     * @param bRepaint if <code>true</code>, immediately updates the view to reflect the new active tab.
     * @return this object.
     */
    public jsx3.gui.TabbedPane setSelectedIndex(int intIndex, boolean bRepaint)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setSelectedIndex", intIndex, bRepaint);
        ScriptSessions.addScript(script);
        return this;
    }

    /**
     * Returns the CSS height property (in pixels) for child tabs
     * @param callback height (in pixels)
     */

    public void getTabHeight(org.directwebremoting.ui.Callback<Integer> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getTabHeight");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, Integer.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Sets the CSS height property for the object (in pixels) for child tabs;
           returns reference to self to facilitate method chaining
     * @param intTabHeight height (in pixels)
     * @return this object
     */
    public jsx3.gui.TabbedPane setTabHeight(int intTabHeight)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setTabHeight", intTabHeight);
        ScriptSessions.addScript(script);
        return this;
    }

    /**
     * whether or not to show the tabs of the tabbed pane. if false then only the content of each tab is drawn.
     */
    public void getShowTabs()
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "getShowTabs");
        ScriptSessions.addScript(script);
    }

    /**
     * whether or not to show the tabs of the tabbed pane. if false then only the content of each tab is drawn.
     * @param intShowTabs
     */
    public void setShowTabs(int intShowTabs)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setShowTabs", intShowTabs);
        ScriptSessions.addScript(script);
    }

}

