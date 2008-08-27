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
 * Renders a tab in a tabbed pane. An instance of this class must be child of an instance of
jsx3.gui.TabbedPane. A tab should have exactly one child, usually a jsx3.gui.Block,
which holds all of its content.
 * @author Joe Walker [joe at getahead dot org]
 * @author DRAPGEN - Dwr Reverse Ajax Proxy GENerator
 */
public class Tab extends jsx3.gui.Block
{
    /**
     * All reverse ajax proxies need context to work from
     * @param context The script that got us to where we are now
     */
    public Tab(Context context, String extension)
    {
        super(context, extension);
    }

    /**
     * instance initializer
     * @param strName unique name distinguishing this object from all other JSX GUI objects in the JSX application
     * @param strText text to display within the given tab; if null, jsx3.gui.Tab.DEFAULTTEXT is used
     * @param vntWidth one of: 1) the width as an integer representing a fixed pixel width for the tab (e.g., 80) ; 2) the width as a percentage representing this tab's width as a percentage of how wide the entire tabbed pane should be (e.g., "25%"); 3) no value (null) to designate that this tab should be just large engough to contain the value of the parameter, @strText;
     * @param strHexActiveColor valid css property for defining the color to use when the tab is active (i.e., red, #ff0000, etc)
     * @param strHexInactiveColor valid css property for defining the color to use when the tab is inactive (i.e., red, #ff0000, etc)
     */
    public Tab(String strName, String strText, String vntWidth, String strHexActiveColor, String strHexInactiveColor)
    {
        super((Context) null, (String) null);
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall("new Tab", strName, strText, vntWidth, strHexActiveColor, strHexInactiveColor);
        setInitScript(script);
    }

    /**
     * instance initializer
     * @param strName unique name distinguishing this object from all other JSX GUI objects in the JSX application
     * @param strText text to display within the given tab; if null, jsx3.gui.Tab.DEFAULTTEXT is used
     * @param vntWidth one of: 1) the width as an integer representing a fixed pixel width for the tab (e.g., 80) ; 2) the width as a percentage representing this tab's width as a percentage of how wide the entire tabbed pane should be (e.g., "25%"); 3) no value (null) to designate that this tab should be just large engough to contain the value of the parameter, @strText;
     * @param strHexActiveColor valid css property for defining the color to use when the tab is active (i.e., red, #ff0000, etc)
     * @param strHexInactiveColor valid css property for defining the color to use when the tab is inactive (i.e., red, #ff0000, etc)
     */
    public Tab(String strName, String strText, int vntWidth, String strHexActiveColor, String strHexInactiveColor)
    {
        super((Context) null, (String) null);
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall("new Tab", strName, strText, vntWidth, strHexActiveColor, strHexInactiveColor);
        setInitScript(script);
    }


    /**
     * 
     */
    public static final String DEFAULTBEVELIMAGE = null;

    /**
     * #e8e8f5
     */
    public static final String DEFAULTACTIVECOLOR = "#e8e8f5";

    /**
     * #d8d8e5
     */
    public static final String DEFAULTINACTIVECOLOR = "#d8d8e5";

    /**
     * 
     */
    public static final String ACTIVEBEVEL = null;

    /**
     * 
     */
    public static final String INACTIVEBEVEL = null;

    /**
     * #e8e8f5
     */
    public static final String CHILDBGCOLOR = "#e8e8f5";

    /**
     * 0 : disabled
     */
    public static final int STATEDISABLED = 0;

    /**
     * 1 : enabled (default)
     */
    public static final int STATEENABLED = 1;


    /**
     * Returns background image that will underlay each tab to provide an outset-type border. Default: jsx3.gui.Tab.DEFAULTBEVELIMAGE
     * @param callback valid url (typically relative) to point to an image that can be used as a bacground image for the tab
     */

    public void getBevel(org.directwebremoting.ui.Callback<String> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getBevel");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, String.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Sets background image that will underlay each tab to provide an outset-type border; if this value is not set or null is passed, the default background image for the jsx3.gui.Tab class will be use the contant value, jsx3.gui.Tab.DEFAULTBEVELIMAGE;
     * @param strURL valid url (typically relative) to point to an image that can be used as a bacground image for the tab
     * @return this object
     */
    public jsx3.gui.Tab setBevel(String strURL)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setBevel", strURL);
        ScriptSessions.addScript(script);
        return this;
    }

    /**
     * Brings this tab and its associated pane forward in the stack among all sibling tabs.
     */
    public void doShow()
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "doShow");
        ScriptSessions.addScript(script);
    }

    /**
     * Returns valid CSS property value, (e.g., red, #ffffff) when tab is active. Default: jsx3.gui.Tab.DEFAULTACTIVECOLOR
     * @param callback valid CSS property value, (e.g., red, #ffffff)
     */

    public void getActiveColor(org.directwebremoting.ui.Callback<String> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getActiveColor");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, String.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Sets valid CSS property value, (e.g., red, #ffffff) when tab is active;
     * @param strActiveColor valid CSS property value, (e.g., red, #ffffff)
     * @return this object
     */
    public jsx3.gui.Tab setActiveColor(String strActiveColor)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setActiveColor", strActiveColor);
        ScriptSessions.addScript(script);
        return this;
    }

    /**
     * Returns valid CSS property value, (e.g., red, #ffffff) when tab is inactive (not selected tab in the group). Default: jsx3.gui.Tab.DEFAULTINACTIVECOLOR
     * @param callback valid CSS property value, (e.g., red, #ffffff)
     */

    public void getInactiveColor(org.directwebremoting.ui.Callback<String> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getInactiveColor");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, String.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Sets valid CSS property value, (e.g., red, #ffffff) when tab is inactive (not selected tab in the group);
     * @param strInactiveColor valid CSS property value, (e.g., red, #ffffff)
     * @return this object
     */
    public jsx3.gui.Tab setInactiveColor(String strInactiveColor)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setInactiveColor", strInactiveColor);
        ScriptSessions.addScript(script);
        return this;
    }

    /**
     * Returns the state for the tab control. Default: jsx3.gui.Tab.STATEENABLED
     * @param callback one of: jsx3.gui.Tab.STATEDISABLED, jsx3.gui.Tab.STATEENABLED
     */

    public void getEnabled(org.directwebremoting.ui.Callback<Integer> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getEnabled");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, Integer.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Sets the enabled state for the tab control; returns reference to self to facilitate method chaining
     * @param STATE one of: jsx3.gui.Tab.STATEDISABLED, jsx3.gui.Tab.STATEENABLED
     * @return this object
     */
    public jsx3.gui.Tab setEnabled(int STATE)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setEnabled", STATE);
        ScriptSessions.addScript(script);
        return this;
    }

    /**
     * Returns the child of this tab that will be painted as the content of this tab. This implementation returns the
first child of this stack.
     */

    public jsx3.app.Model getContentChild()
    {
        String extension = "getContentChild().";
        try
        {
            java.lang.reflect.Constructor<jsx3.app.Model> ctor = jsx3.app.Model.class.getConstructor(Context.class, String.class);
            return ctor.newInstance(this, extension);
        }
        catch (Exception ex)
        {
            throw new IllegalArgumentException("Unsupported type: " + jsx3.app.Model.class.getName());
        }
    }

    /**
     * Returns the child of this tab that will be painted as the content of this tab. This implementation returns the
first child of this stack.
     * @param returnType The expected return type
     */

    public <T> T getContentChild(Class<T> returnType)
    {
        String extension = "getContentChild().";
        try
        {
            java.lang.reflect.Constructor<T> ctor = returnType.getConstructor(Context.class, String.class);
            return ctor.newInstance(this, extension);
        }
        catch (Exception ex)
        {
            throw new IllegalArgumentException("Unsupported return type: " + returnType.getName());
        }
    }

}

