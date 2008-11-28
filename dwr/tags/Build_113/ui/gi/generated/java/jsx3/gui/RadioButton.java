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
 * A GUI control that implements a single radio button.

Several radio buttons may be organized into a group ("radio group") with the groupName property.
No more than one radio button of the set of radio buttons sharing a single groupName value may be
selected at one time.
 * @author Joe Walker [joe at getahead dot org]
 * @author DRAPGEN - Dwr Reverse Ajax Proxy GENerator
 */
public class RadioButton extends jsx3.gui.Block
{
    /**
     * All reverse ajax proxies need context to work from
     * @param context The script that got us to where we are now
     */
    public RadioButton(Context context, String extension)
    {
        super(context, extension);
    }

    /**
     * The instance initializer.
     * @param strName a unique name distinguishing this object from all other JSX GUI objects in the JSX application.
     * @param vntLeft the left offset of this object from the parent container as a number (in pixels) or a string css value.
     * @param vntTop the top offset of this object from the parent container as a number (in pixels) or a string css value.
     * @param vntWidth the width of this object as a number (in pixels) or a string css value.
     * @param vntHeight the height of this object as a number (in pixels) or a string css value.
     * @param strText the text/HTML markup to display with the radio button.
     * @param strValue the value of the radio button (equivalent to the <code>value</code> property on a standard HTML radio button).
     * @param strGroupName the group name of the radio button (equivalent to the <code>name</code> property on a standard HTML radio button).
     * @param intSelected the default selection state of the radio button. <code>SELECTED</code> or
   <code>UNSELECTED</code>. <code>null</code> is equivalent to <code>SELECTED</code>.
     */
    public RadioButton(String strName, int vntLeft, int vntTop, int vntWidth, String vntHeight, String strText, String strValue, String strGroupName, int intSelected)
    {
        super((Context) null, (String) null);
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall("new RadioButton", strName, vntLeft, vntTop, vntWidth, vntHeight, strText, strValue, strGroupName, intSelected);
        setInitScript(script);
    }

    /**
     * The instance initializer.
     * @param strName a unique name distinguishing this object from all other JSX GUI objects in the JSX application.
     * @param vntLeft the left offset of this object from the parent container as a number (in pixels) or a string css value.
     * @param vntTop the top offset of this object from the parent container as a number (in pixels) or a string css value.
     * @param vntWidth the width of this object as a number (in pixels) or a string css value.
     * @param vntHeight the height of this object as a number (in pixels) or a string css value.
     * @param strText the text/HTML markup to display with the radio button.
     * @param strValue the value of the radio button (equivalent to the <code>value</code> property on a standard HTML radio button).
     * @param strGroupName the group name of the radio button (equivalent to the <code>name</code> property on a standard HTML radio button).
     * @param intSelected the default selection state of the radio button. <code>SELECTED</code> or
   <code>UNSELECTED</code>. <code>null</code> is equivalent to <code>SELECTED</code>.
     */
    public RadioButton(String strName, String vntLeft, String vntTop, int vntWidth, String vntHeight, String strText, String strValue, String strGroupName, int intSelected)
    {
        super((Context) null, (String) null);
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall("new RadioButton", strName, vntLeft, vntTop, vntWidth, vntHeight, strText, strValue, strGroupName, intSelected);
        setInitScript(script);
    }

    /**
     * The instance initializer.
     * @param strName a unique name distinguishing this object from all other JSX GUI objects in the JSX application.
     * @param vntLeft the left offset of this object from the parent container as a number (in pixels) or a string css value.
     * @param vntTop the top offset of this object from the parent container as a number (in pixels) or a string css value.
     * @param vntWidth the width of this object as a number (in pixels) or a string css value.
     * @param vntHeight the height of this object as a number (in pixels) or a string css value.
     * @param strText the text/HTML markup to display with the radio button.
     * @param strValue the value of the radio button (equivalent to the <code>value</code> property on a standard HTML radio button).
     * @param strGroupName the group name of the radio button (equivalent to the <code>name</code> property on a standard HTML radio button).
     * @param intSelected the default selection state of the radio button. <code>SELECTED</code> or
   <code>UNSELECTED</code>. <code>null</code> is equivalent to <code>SELECTED</code>.
     */
    public RadioButton(String strName, int vntLeft, String vntTop, int vntWidth, int vntHeight, String strText, String strValue, String strGroupName, int intSelected)
    {
        super((Context) null, (String) null);
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall("new RadioButton", strName, vntLeft, vntTop, vntWidth, vntHeight, strText, strValue, strGroupName, intSelected);
        setInitScript(script);
    }

    /**
     * The instance initializer.
     * @param strName a unique name distinguishing this object from all other JSX GUI objects in the JSX application.
     * @param vntLeft the left offset of this object from the parent container as a number (in pixels) or a string css value.
     * @param vntTop the top offset of this object from the parent container as a number (in pixels) or a string css value.
     * @param vntWidth the width of this object as a number (in pixels) or a string css value.
     * @param vntHeight the height of this object as a number (in pixels) or a string css value.
     * @param strText the text/HTML markup to display with the radio button.
     * @param strValue the value of the radio button (equivalent to the <code>value</code> property on a standard HTML radio button).
     * @param strGroupName the group name of the radio button (equivalent to the <code>name</code> property on a standard HTML radio button).
     * @param intSelected the default selection state of the radio button. <code>SELECTED</code> or
   <code>UNSELECTED</code>. <code>null</code> is equivalent to <code>SELECTED</code>.
     */
    public RadioButton(String strName, String vntLeft, String vntTop, String vntWidth, int vntHeight, String strText, String strValue, String strGroupName, int intSelected)
    {
        super((Context) null, (String) null);
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall("new RadioButton", strName, vntLeft, vntTop, vntWidth, vntHeight, strText, strValue, strGroupName, intSelected);
        setInitScript(script);
    }

    /**
     * The instance initializer.
     * @param strName a unique name distinguishing this object from all other JSX GUI objects in the JSX application.
     * @param vntLeft the left offset of this object from the parent container as a number (in pixels) or a string css value.
     * @param vntTop the top offset of this object from the parent container as a number (in pixels) or a string css value.
     * @param vntWidth the width of this object as a number (in pixels) or a string css value.
     * @param vntHeight the height of this object as a number (in pixels) or a string css value.
     * @param strText the text/HTML markup to display with the radio button.
     * @param strValue the value of the radio button (equivalent to the <code>value</code> property on a standard HTML radio button).
     * @param strGroupName the group name of the radio button (equivalent to the <code>name</code> property on a standard HTML radio button).
     * @param intSelected the default selection state of the radio button. <code>SELECTED</code> or
   <code>UNSELECTED</code>. <code>null</code> is equivalent to <code>SELECTED</code>.
     */
    public RadioButton(String strName, String vntLeft, int vntTop, String vntWidth, String vntHeight, String strText, String strValue, String strGroupName, int intSelected)
    {
        super((Context) null, (String) null);
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall("new RadioButton", strName, vntLeft, vntTop, vntWidth, vntHeight, strText, strValue, strGroupName, intSelected);
        setInitScript(script);
    }

    /**
     * The instance initializer.
     * @param strName a unique name distinguishing this object from all other JSX GUI objects in the JSX application.
     * @param vntLeft the left offset of this object from the parent container as a number (in pixels) or a string css value.
     * @param vntTop the top offset of this object from the parent container as a number (in pixels) or a string css value.
     * @param vntWidth the width of this object as a number (in pixels) or a string css value.
     * @param vntHeight the height of this object as a number (in pixels) or a string css value.
     * @param strText the text/HTML markup to display with the radio button.
     * @param strValue the value of the radio button (equivalent to the <code>value</code> property on a standard HTML radio button).
     * @param strGroupName the group name of the radio button (equivalent to the <code>name</code> property on a standard HTML radio button).
     * @param intSelected the default selection state of the radio button. <code>SELECTED</code> or
   <code>UNSELECTED</code>. <code>null</code> is equivalent to <code>SELECTED</code>.
     */
    public RadioButton(String strName, int vntLeft, String vntTop, String vntWidth, String vntHeight, String strText, String strValue, String strGroupName, int intSelected)
    {
        super((Context) null, (String) null);
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall("new RadioButton", strName, vntLeft, vntTop, vntWidth, vntHeight, strText, strValue, strGroupName, intSelected);
        setInitScript(script);
    }

    /**
     * The instance initializer.
     * @param strName a unique name distinguishing this object from all other JSX GUI objects in the JSX application.
     * @param vntLeft the left offset of this object from the parent container as a number (in pixels) or a string css value.
     * @param vntTop the top offset of this object from the parent container as a number (in pixels) or a string css value.
     * @param vntWidth the width of this object as a number (in pixels) or a string css value.
     * @param vntHeight the height of this object as a number (in pixels) or a string css value.
     * @param strText the text/HTML markup to display with the radio button.
     * @param strValue the value of the radio button (equivalent to the <code>value</code> property on a standard HTML radio button).
     * @param strGroupName the group name of the radio button (equivalent to the <code>name</code> property on a standard HTML radio button).
     * @param intSelected the default selection state of the radio button. <code>SELECTED</code> or
   <code>UNSELECTED</code>. <code>null</code> is equivalent to <code>SELECTED</code>.
     */
    public RadioButton(String strName, int vntLeft, int vntTop, String vntWidth, String vntHeight, String strText, String strValue, String strGroupName, int intSelected)
    {
        super((Context) null, (String) null);
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall("new RadioButton", strName, vntLeft, vntTop, vntWidth, vntHeight, strText, strValue, strGroupName, intSelected);
        setInitScript(script);
    }

    /**
     * The instance initializer.
     * @param strName a unique name distinguishing this object from all other JSX GUI objects in the JSX application.
     * @param vntLeft the left offset of this object from the parent container as a number (in pixels) or a string css value.
     * @param vntTop the top offset of this object from the parent container as a number (in pixels) or a string css value.
     * @param vntWidth the width of this object as a number (in pixels) or a string css value.
     * @param vntHeight the height of this object as a number (in pixels) or a string css value.
     * @param strText the text/HTML markup to display with the radio button.
     * @param strValue the value of the radio button (equivalent to the <code>value</code> property on a standard HTML radio button).
     * @param strGroupName the group name of the radio button (equivalent to the <code>name</code> property on a standard HTML radio button).
     * @param intSelected the default selection state of the radio button. <code>SELECTED</code> or
   <code>UNSELECTED</code>. <code>null</code> is equivalent to <code>SELECTED</code>.
     */
    public RadioButton(String strName, String vntLeft, String vntTop, int vntWidth, int vntHeight, String strText, String strValue, String strGroupName, int intSelected)
    {
        super((Context) null, (String) null);
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall("new RadioButton", strName, vntLeft, vntTop, vntWidth, vntHeight, strText, strValue, strGroupName, intSelected);
        setInitScript(script);
    }

    /**
     * The instance initializer.
     * @param strName a unique name distinguishing this object from all other JSX GUI objects in the JSX application.
     * @param vntLeft the left offset of this object from the parent container as a number (in pixels) or a string css value.
     * @param vntTop the top offset of this object from the parent container as a number (in pixels) or a string css value.
     * @param vntWidth the width of this object as a number (in pixels) or a string css value.
     * @param vntHeight the height of this object as a number (in pixels) or a string css value.
     * @param strText the text/HTML markup to display with the radio button.
     * @param strValue the value of the radio button (equivalent to the <code>value</code> property on a standard HTML radio button).
     * @param strGroupName the group name of the radio button (equivalent to the <code>name</code> property on a standard HTML radio button).
     * @param intSelected the default selection state of the radio button. <code>SELECTED</code> or
   <code>UNSELECTED</code>. <code>null</code> is equivalent to <code>SELECTED</code>.
     */
    public RadioButton(String strName, int vntLeft, int vntTop, String vntWidth, int vntHeight, String strText, String strValue, String strGroupName, int intSelected)
    {
        super((Context) null, (String) null);
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall("new RadioButton", strName, vntLeft, vntTop, vntWidth, vntHeight, strText, strValue, strGroupName, intSelected);
        setInitScript(script);
    }

    /**
     * The instance initializer.
     * @param strName a unique name distinguishing this object from all other JSX GUI objects in the JSX application.
     * @param vntLeft the left offset of this object from the parent container as a number (in pixels) or a string css value.
     * @param vntTop the top offset of this object from the parent container as a number (in pixels) or a string css value.
     * @param vntWidth the width of this object as a number (in pixels) or a string css value.
     * @param vntHeight the height of this object as a number (in pixels) or a string css value.
     * @param strText the text/HTML markup to display with the radio button.
     * @param strValue the value of the radio button (equivalent to the <code>value</code> property on a standard HTML radio button).
     * @param strGroupName the group name of the radio button (equivalent to the <code>name</code> property on a standard HTML radio button).
     * @param intSelected the default selection state of the radio button. <code>SELECTED</code> or
   <code>UNSELECTED</code>. <code>null</code> is equivalent to <code>SELECTED</code>.
     */
    public RadioButton(String strName, String vntLeft, int vntTop, String vntWidth, int vntHeight, String strText, String strValue, String strGroupName, int intSelected)
    {
        super((Context) null, (String) null);
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall("new RadioButton", strName, vntLeft, vntTop, vntWidth, vntHeight, strText, strValue, strGroupName, intSelected);
        setInitScript(script);
    }

    /**
     * The instance initializer.
     * @param strName a unique name distinguishing this object from all other JSX GUI objects in the JSX application.
     * @param vntLeft the left offset of this object from the parent container as a number (in pixels) or a string css value.
     * @param vntTop the top offset of this object from the parent container as a number (in pixels) or a string css value.
     * @param vntWidth the width of this object as a number (in pixels) or a string css value.
     * @param vntHeight the height of this object as a number (in pixels) or a string css value.
     * @param strText the text/HTML markup to display with the radio button.
     * @param strValue the value of the radio button (equivalent to the <code>value</code> property on a standard HTML radio button).
     * @param strGroupName the group name of the radio button (equivalent to the <code>name</code> property on a standard HTML radio button).
     * @param intSelected the default selection state of the radio button. <code>SELECTED</code> or
   <code>UNSELECTED</code>. <code>null</code> is equivalent to <code>SELECTED</code>.
     */
    public RadioButton(String strName, int vntLeft, String vntTop, String vntWidth, int vntHeight, String strText, String strValue, String strGroupName, int intSelected)
    {
        super((Context) null, (String) null);
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall("new RadioButton", strName, vntLeft, vntTop, vntWidth, vntHeight, strText, strValue, strGroupName, intSelected);
        setInitScript(script);
    }

    /**
     * The instance initializer.
     * @param strName a unique name distinguishing this object from all other JSX GUI objects in the JSX application.
     * @param vntLeft the left offset of this object from the parent container as a number (in pixels) or a string css value.
     * @param vntTop the top offset of this object from the parent container as a number (in pixels) or a string css value.
     * @param vntWidth the width of this object as a number (in pixels) or a string css value.
     * @param vntHeight the height of this object as a number (in pixels) or a string css value.
     * @param strText the text/HTML markup to display with the radio button.
     * @param strValue the value of the radio button (equivalent to the <code>value</code> property on a standard HTML radio button).
     * @param strGroupName the group name of the radio button (equivalent to the <code>name</code> property on a standard HTML radio button).
     * @param intSelected the default selection state of the radio button. <code>SELECTED</code> or
   <code>UNSELECTED</code>. <code>null</code> is equivalent to <code>SELECTED</code>.
     */
    public RadioButton(String strName, int vntLeft, String vntTop, int vntWidth, String vntHeight, String strText, String strValue, String strGroupName, int intSelected)
    {
        super((Context) null, (String) null);
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall("new RadioButton", strName, vntLeft, vntTop, vntWidth, vntHeight, strText, strValue, strGroupName, intSelected);
        setInitScript(script);
    }

    /**
     * The instance initializer.
     * @param strName a unique name distinguishing this object from all other JSX GUI objects in the JSX application.
     * @param vntLeft the left offset of this object from the parent container as a number (in pixels) or a string css value.
     * @param vntTop the top offset of this object from the parent container as a number (in pixels) or a string css value.
     * @param vntWidth the width of this object as a number (in pixels) or a string css value.
     * @param vntHeight the height of this object as a number (in pixels) or a string css value.
     * @param strText the text/HTML markup to display with the radio button.
     * @param strValue the value of the radio button (equivalent to the <code>value</code> property on a standard HTML radio button).
     * @param strGroupName the group name of the radio button (equivalent to the <code>name</code> property on a standard HTML radio button).
     * @param intSelected the default selection state of the radio button. <code>SELECTED</code> or
   <code>UNSELECTED</code>. <code>null</code> is equivalent to <code>SELECTED</code>.
     */
    public RadioButton(String strName, String vntLeft, String vntTop, String vntWidth, String vntHeight, String strText, String strValue, String strGroupName, int intSelected)
    {
        super((Context) null, (String) null);
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall("new RadioButton", strName, vntLeft, vntTop, vntWidth, vntHeight, strText, strValue, strGroupName, intSelected);
        setInitScript(script);
    }

    /**
     * The instance initializer.
     * @param strName a unique name distinguishing this object from all other JSX GUI objects in the JSX application.
     * @param vntLeft the left offset of this object from the parent container as a number (in pixels) or a string css value.
     * @param vntTop the top offset of this object from the parent container as a number (in pixels) or a string css value.
     * @param vntWidth the width of this object as a number (in pixels) or a string css value.
     * @param vntHeight the height of this object as a number (in pixels) or a string css value.
     * @param strText the text/HTML markup to display with the radio button.
     * @param strValue the value of the radio button (equivalent to the <code>value</code> property on a standard HTML radio button).
     * @param strGroupName the group name of the radio button (equivalent to the <code>name</code> property on a standard HTML radio button).
     * @param intSelected the default selection state of the radio button. <code>SELECTED</code> or
   <code>UNSELECTED</code>. <code>null</code> is equivalent to <code>SELECTED</code>.
     */
    public RadioButton(String strName, int vntLeft, int vntTop, int vntWidth, int vntHeight, String strText, String strValue, String strGroupName, int intSelected)
    {
        super((Context) null, (String) null);
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall("new RadioButton", strName, vntLeft, vntTop, vntWidth, vntHeight, strText, strValue, strGroupName, intSelected);
        setInitScript(script);
    }

    /**
     * The instance initializer.
     * @param strName a unique name distinguishing this object from all other JSX GUI objects in the JSX application.
     * @param vntLeft the left offset of this object from the parent container as a number (in pixels) or a string css value.
     * @param vntTop the top offset of this object from the parent container as a number (in pixels) or a string css value.
     * @param vntWidth the width of this object as a number (in pixels) or a string css value.
     * @param vntHeight the height of this object as a number (in pixels) or a string css value.
     * @param strText the text/HTML markup to display with the radio button.
     * @param strValue the value of the radio button (equivalent to the <code>value</code> property on a standard HTML radio button).
     * @param strGroupName the group name of the radio button (equivalent to the <code>name</code> property on a standard HTML radio button).
     * @param intSelected the default selection state of the radio button. <code>SELECTED</code> or
   <code>UNSELECTED</code>. <code>null</code> is equivalent to <code>SELECTED</code>.
     */
    public RadioButton(String strName, String vntLeft, int vntTop, int vntWidth, String vntHeight, String strText, String strValue, String strGroupName, int intSelected)
    {
        super((Context) null, (String) null);
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall("new RadioButton", strName, vntLeft, vntTop, vntWidth, vntHeight, strText, strValue, strGroupName, intSelected);
        setInitScript(script);
    }

    /**
     * The instance initializer.
     * @param strName a unique name distinguishing this object from all other JSX GUI objects in the JSX application.
     * @param vntLeft the left offset of this object from the parent container as a number (in pixels) or a string css value.
     * @param vntTop the top offset of this object from the parent container as a number (in pixels) or a string css value.
     * @param vntWidth the width of this object as a number (in pixels) or a string css value.
     * @param vntHeight the height of this object as a number (in pixels) or a string css value.
     * @param strText the text/HTML markup to display with the radio button.
     * @param strValue the value of the radio button (equivalent to the <code>value</code> property on a standard HTML radio button).
     * @param strGroupName the group name of the radio button (equivalent to the <code>name</code> property on a standard HTML radio button).
     * @param intSelected the default selection state of the radio button. <code>SELECTED</code> or
   <code>UNSELECTED</code>. <code>null</code> is equivalent to <code>SELECTED</code>.
     */
    public RadioButton(String strName, String vntLeft, int vntTop, int vntWidth, int vntHeight, String strText, String strValue, String strGroupName, int intSelected)
    {
        super((Context) null, (String) null);
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall("new RadioButton", strName, vntLeft, vntTop, vntWidth, vntHeight, strText, strValue, strGroupName, intSelected);
        setInitScript(script);
    }


    /**
     * Value for the selected field indicating an unselected radio button.
     */
    public static final int UNSELECTED = 0;

    /**
     * Value for the selected field indicating a selected radio button.
     */
    public static final int SELECTED = 1;

    /**
     * jsx30radio
     */
    public static final String DEFAULTCLASSNAME = "jsx30radio";


    /**
     * Returns the group name of this radio button, which is equivalent to the name property on a
standard HTML radio button.
     */

    public void getGroupName(org.directwebremoting.ui.Callback<String> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getGroupName");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, String.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Sets the group name of this radio button. If this property is set, this radio button will be a member of the set
of radio buttons sharing the same value for this property. No more than one member of this set may be selected
at one time.
     * @param strGroupName the new group name.
     */
    public void setGroupName(String strGroupName)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setGroupName", strGroupName);
        ScriptSessions.addScript(script);
    }

    /**
     * Returns the default selection state of this radio button. To get the current state use getSelected().
     * @param callback <code>SELECTED</code> or <code>UNSELECTED</code>.
     */

    public void getDefaultSelected(org.directwebremoting.ui.Callback<Integer> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getDefaultSelected");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, Integer.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Sets the default selection state of this radio button.
     * @param intSelected <code>SELECTED</code> or <code>UNSELECTED</code>.
     * @return this object.
     */
    public jsx3.gui.RadioButton setDefaultSelected(int intSelected)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setDefaultSelected", intSelected);
        ScriptSessions.addScript(script);
        return this;
    }

    /**
     * Returns the current selection state of this radio button.
     * @param callback <code>SELECTED</code> or <code>UNSELECTED</code>.
     */

    public void getSelected(org.directwebremoting.ui.Callback<Integer> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getSelected");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, Integer.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Sets the current selection state of this radio button. This method immediately updates the view of this
object if it is currently rendered on the screen. If intSelected is equal to SELECTED
any other radio buttons in the radio group of this object will be unselected.
     * @param intSelected if <code>SELECTED</code> or <code>null</code>, this object is selected, otherwise it
   is unselected.
     * @param objGUI
     * @return this object.
     */
    public jsx3.gui.RadioButton setSelected(int intSelected, jsx3.lang.Object objGUI)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setSelected", intSelected, objGUI);
        ScriptSessions.addScript(script);
        return this;
    }

    /**
     * Returns the list of sibling radio buttons. This list is comprised of the radio buttons whose groupName property
is equal to the groupName property of this radio button. The return value does not include this radio button.
This method will only return siblings if this radio button is rendered and will only return sibling radio
buttons that are also rendered on screen.
     * @param bRendered
     */

    public void getSiblings(boolean bRendered, org.directwebremoting.ui.Callback<Object[]> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getSiblings", bRendered);

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, Object[].class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Returns the value of this radio button. When this radio button is selected, the value of its radio group is
equal to the value of this radio button.
     * @param callback the value of this radio button.
     */

    public void getValue(org.directwebremoting.ui.Callback<String> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getValue");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, String.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Sets the value of this radio button.
     * @param strValue the value of this radiobutton. In general, each radio button is a radio group has
   a unique value.
     * @return this object.
     */
    public jsx3.gui.RadioButton setValue(String strValue)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setValue", strValue);
        ScriptSessions.addScript(script);
        return this;
    }

    /**
     * Returns the value of the selected radio button in the radio group of this radio button.
     * @param callback the value of the selected radio button or <code>null</code> if no button is selected.
     */

    public void getGroupValue(org.directwebremoting.ui.Callback<String> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getGroupValue");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, String.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Sets the selected radio button of the radio group of this radio button by value.
     * @param strValue the value of the radio button of the radio group of this radio button to select.
     */
    public void setGroupValue(String strValue)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setGroupValue", strValue);
        ScriptSessions.addScript(script);
    }

    /**
     * Validates that this radio button is selected if it is required. A radiobutton will pass validation if it is
optional or if it is required and it or one of its sibling radio buttons is selected.
     * @param callback <code>jsx3.gui.Form.STATEVALID</code> or <code>jsx3.gui.Form.INSTATEVALID</code>.
     */

    public void doValidate(org.directwebremoting.ui.Callback<Integer> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "doValidate");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, Integer.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Binds the given key sequence to a callback function. Any object that has a key binding (specified with
setKeyBinding()) will call this method when painted to register the key sequence with an appropriate
ancestor of this form control. Any key down event that bubbles up to the ancestor without being intercepted
and matches the given key sequence will invoke the given callback function.

As of 3.2: The hot key will be registered with the first ancestor found that is either a
jsx3.gui.Window, a jsx3.gui.Dialog, or the root block of a jsx3.app.Server.
     * @param fctCallback JavaScript function to execute when the given sequence is keyed by the user.
     * @param strKeys a plus-delimited ('+') key sequence such as <code>ctrl+s</code> or
  <code>ctrl+shift+alt+h</code> or <code>shift+a</code>, etc. Any combination of shift, ctrl, and alt are
  supported, including none. Also supported as the final token are <code>enter</code>, <code>esc</code>,
  <code>tab</code>, <code>del</code>, and <code>space</code>. To specify the final token as a key code, the
  last token can be the key code contained in brackets, <code>[13]</code>.
     * @return the registered hot key.
     */

    public jsx3.gui.HotKey doKeyBinding(org.directwebremoting.ui.CodeBlock fctCallback, String strKeys)
    {
        String extension = "doKeyBinding(\"" + fctCallback + "\", \"" + strKeys + "\").";
        try
        {
            java.lang.reflect.Constructor<jsx3.gui.HotKey> ctor = jsx3.gui.HotKey.class.getConstructor(Context.class, String.class);
            return ctor.newInstance(this, extension);
        }
        catch (Exception ex)
        {
            throw new IllegalArgumentException("Unsupported type: " + jsx3.gui.HotKey.class.getName());
        }
    }


    /**
     * Resets the validation state of this control.
     * @return this object.
     */

    public jsx3.gui.Form doReset()
    {
        String extension = "doReset().";
        try
        {
            java.lang.reflect.Constructor<jsx3.gui.Form> ctor = jsx3.gui.Form.class.getConstructor(Context.class, String.class);
            return ctor.newInstance(this, extension);
        }
        catch (Exception ex)
        {
            throw new IllegalArgumentException("Unsupported type: " + jsx3.gui.Form.class.getName());
        }
    }

    /**
     * Resets the validation state of this control.
     * @param returnType The expected return type
     * @return this object.
     */

    public <T> T doReset(Class<T> returnType)
    {
        String extension = "doReset().";
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

    /**
     * Returns the background color of this control when it is disabled.
     * @param callback valid CSS property value, (i.e., red, #ff0000)
     */

    public void getDisabledBackgroundColor(org.directwebremoting.ui.Callback<String> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getDisabledBackgroundColor");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, String.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Returns the font color to use when this control is disabled.
     * @param callback valid CSS property value, (i.e., red, #ff0000)
     */

    public void getDisabledColor(org.directwebremoting.ui.Callback<String> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getDisabledColor");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, String.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Returns the state for the form field control. If no enabled state is set, this method returns
STATEENABLED.
     * @param callback <code>STATEDISABLED</code> or <code>STATEENABLED</code>.
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
     * Returns the key binding that when keyed will fire the execute event for this control.
     * @param callback plus-delimited (e.g.,'+') key sequence such as ctrl+s or ctrl+shift+alt+h or shift+a, etc
     */

    public void getKeyBinding(org.directwebremoting.ui.Callback<String> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getKeyBinding");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, String.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Returns whether or not this control is required. If the required property has never been set, this method returns
OPTIONAL.
     * @param callback <code>REQUIRED</code> or <code>OPTIONAL</code>.
     */

    public void getRequired(org.directwebremoting.ui.Callback<Integer> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getRequired");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, Integer.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Returns the validation state of this control. If the validationState property has never been set, this method returns
STATEVALID.
     * @param callback <code>STATEINVALID</code> or <code>STATEVALID</code>.
     */

    public void getValidationState(org.directwebremoting.ui.Callback<Integer> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getValidationState");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, Integer.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Sets the background color of this form control when it is disabled.
     * @param strColor valid CSS property value, (i.e., red, #ff0000)
     * @return this object.
     */

    public jsx3.gui.Form setDisabledBackgroundColor(String strColor)
    {
        String extension = "setDisabledBackgroundColor(\"" + strColor + "\").";
        try
        {
            java.lang.reflect.Constructor<jsx3.gui.Form> ctor = jsx3.gui.Form.class.getConstructor(Context.class, String.class);
            return ctor.newInstance(this, extension);
        }
        catch (Exception ex)
        {
            throw new IllegalArgumentException("Unsupported type: " + jsx3.gui.Form.class.getName());
        }
    }

    /**
     * Sets the background color of this form control when it is disabled.
     * @param strColor valid CSS property value, (i.e., red, #ff0000)
     * @param returnType The expected return type
     * @return this object.
     */

    public <T> T setDisabledBackgroundColor(String strColor, Class<T> returnType)
    {
        String extension = "setDisabledBackgroundColor(\"" + strColor + "\").";
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

    /**
     * Sets the font color to use when this control is disabled.
     * @param strColor valid CSS property value, (i.e., red, #ff0000)
     * @return this object.
     */

    public jsx3.gui.Form setDisabledColor(String strColor)
    {
        String extension = "setDisabledColor(\"" + strColor + "\").";
        try
        {
            java.lang.reflect.Constructor<jsx3.gui.Form> ctor = jsx3.gui.Form.class.getConstructor(Context.class, String.class);
            return ctor.newInstance(this, extension);
        }
        catch (Exception ex)
        {
            throw new IllegalArgumentException("Unsupported type: " + jsx3.gui.Form.class.getName());
        }
    }

    /**
     * Sets the font color to use when this control is disabled.
     * @param strColor valid CSS property value, (i.e., red, #ff0000)
     * @param returnType The expected return type
     * @return this object.
     */

    public <T> T setDisabledColor(String strColor, Class<T> returnType)
    {
        String extension = "setDisabledColor(\"" + strColor + "\").";
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

    /**
     * Sets whether this control is enabled. Disabled controls do not respond to user interaction.
     * @param intEnabled <code>STATEDISABLED</code> or <code>STATEENABLED</code>. <code>null</code> is
   equivalent to <code>STATEENABLED</code>.
     * @param bRepaint if <code>true</code> this control is immediately repainted to reflect the new setting.
     */
    public void setEnabled(int intEnabled, boolean bRepaint)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setEnabled", intEnabled, bRepaint);
        ScriptSessions.addScript(script);
    }

    /**
     * Sets the key binding that when keyed will fire the bound execute (jsx3.gui.Interactive.EXECUTE)
event for this control.
     * @param strSequence plus-delimited (e.g.,'+') key sequence such as ctrl+s or ctrl+shift+alt+h or shift+a, etc
     * @return this object.
     */

    public jsx3.gui.Form setKeyBinding(String strSequence)
    {
        String extension = "setKeyBinding(\"" + strSequence + "\").";
        try
        {
            java.lang.reflect.Constructor<jsx3.gui.Form> ctor = jsx3.gui.Form.class.getConstructor(Context.class, String.class);
            return ctor.newInstance(this, extension);
        }
        catch (Exception ex)
        {
            throw new IllegalArgumentException("Unsupported type: " + jsx3.gui.Form.class.getName());
        }
    }

    /**
     * Sets the key binding that when keyed will fire the bound execute (jsx3.gui.Interactive.EXECUTE)
event for this control.
     * @param strSequence plus-delimited (e.g.,'+') key sequence such as ctrl+s or ctrl+shift+alt+h or shift+a, etc
     * @param returnType The expected return type
     * @return this object.
     */

    public <T> T setKeyBinding(String strSequence, Class<T> returnType)
    {
        String extension = "setKeyBinding(\"" + strSequence + "\").";
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

    /**
     * Sets whether or not this control is required.
     * @param required {int} <code>REQUIRED</code> or <code>OPTIONAL</code>.
     * @return this object.
     */

    public jsx3.gui.Form setRequired(int required)
    {
        String extension = "setRequired(\"" + required + "\").";
        try
        {
            java.lang.reflect.Constructor<jsx3.gui.Form> ctor = jsx3.gui.Form.class.getConstructor(Context.class, String.class);
            return ctor.newInstance(this, extension);
        }
        catch (Exception ex)
        {
            throw new IllegalArgumentException("Unsupported type: " + jsx3.gui.Form.class.getName());
        }
    }

    /**
     * Sets whether or not this control is required.
     * @param required {int} <code>REQUIRED</code> or <code>OPTIONAL</code>.
     * @param returnType The expected return type
     * @return this object.
     */

    public <T> T setRequired(int required, Class<T> returnType)
    {
        String extension = "setRequired(\"" + required + "\").";
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

    /**
     * Sets the validation state of this control. The validation state of a control is not serialized.
     * @param intState <code>STATEINVALID</code> or <code>STATEVALID</code>.
     * @return this object.
     */

    public jsx3.gui.Form setValidationState(int intState)
    {
        String extension = "setValidationState(\"" + intState + "\").";
        try
        {
            java.lang.reflect.Constructor<jsx3.gui.Form> ctor = jsx3.gui.Form.class.getConstructor(Context.class, String.class);
            return ctor.newInstance(this, extension);
        }
        catch (Exception ex)
        {
            throw new IllegalArgumentException("Unsupported type: " + jsx3.gui.Form.class.getName());
        }
    }

    /**
     * Sets the validation state of this control. The validation state of a control is not serialized.
     * @param intState <code>STATEINVALID</code> or <code>STATEVALID</code>.
     * @param returnType The expected return type
     * @return this object.
     */

    public <T> T setValidationState(int intState, Class<T> returnType)
    {
        String extension = "setValidationState(\"" + intState + "\").";
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

