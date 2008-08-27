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
 * Renders a dialog window. A dialog can contain other DOM objects; usually a dialog contains one child of type
jsx3.gui.WindowBar and one child of type jsx3.gui.Block.
 * @author Joe Walker [joe at getahead dot org]
 * @author DRAPGEN - Dwr Reverse Ajax Proxy GENerator
 */
public class Dialog extends jsx3.gui.Block
{
    /**
     * All reverse ajax proxies need context to work from
     * @param context The script that got us to where we are now
     */
    public Dialog(Context context, String extension)
    {
        super(context, extension);
    }

    /**
     * instance initializer
     * @param strName unique name distinguishing this object from all other JSX GUI objects in the JSX application
     * @param vntWidth width in pixels
     * @param vntHeight height in pixels
     * @param strTitle if != null,  will be set as the text property on the child captionbar
     */
    public Dialog(String strName, int vntWidth, int vntHeight, String strTitle)
    {
        super((Context) null, (String) null);
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall("new Dialog", strName, vntWidth, vntHeight, strTitle);
        setInitScript(script);
    }

    /**
     * instance initializer
     * @param strName unique name distinguishing this object from all other JSX GUI objects in the JSX application
     * @param vntWidth width in pixels
     * @param vntHeight height in pixels
     * @param strTitle if != null,  will be set as the text property on the child captionbar
     */
    public Dialog(String strName, String vntWidth, String vntHeight, String strTitle)
    {
        super((Context) null, (String) null);
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall("new Dialog", strName, vntWidth, vntHeight, strTitle);
        setInitScript(script);
    }

    /**
     * instance initializer
     * @param strName unique name distinguishing this object from all other JSX GUI objects in the JSX application
     * @param vntWidth width in pixels
     * @param vntHeight height in pixels
     * @param strTitle if != null,  will be set as the text property on the child captionbar
     */
    public Dialog(String strName, int vntWidth, String vntHeight, String strTitle)
    {
        super((Context) null, (String) null);
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall("new Dialog", strName, vntWidth, vntHeight, strTitle);
        setInitScript(script);
    }

    /**
     * instance initializer
     * @param strName unique name distinguishing this object from all other JSX GUI objects in the JSX application
     * @param vntWidth width in pixels
     * @param vntHeight height in pixels
     * @param strTitle if != null,  will be set as the text property on the child captionbar
     */
    public Dialog(String strName, String vntWidth, int vntHeight, String strTitle)
    {
        super((Context) null, (String) null);
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall("new Dialog", strName, vntWidth, vntHeight, strTitle);
        setInitScript(script);
    }


    /**
     * Enum value for the windowState property of instances of this class indicating a minimized dialog.
     */
    public static final int MINIMIZED = 0;

    /**
     * Enum value for the windowState property of instances of this class indicating a maximized dialog.
     */
    public static final int MAXIMIZED = 1;

    /**
     * #e8e8f5 (default)
     */
    public static final String DEFAULTBACKGROUNDCOLOR = "#e8e8f5";

    /**
     * Enum value for the resizable property of instances of this class indicating a non-resizable dialog.
     */
    public static final int FIXED = 0;

    /**
     * Enum value for the resizable property of instances of this class indicating a resizable dialog.
     */
    public static final int RESIZABLE = 1;

    /**
     * Enum value for the modal property of instances of this class indicating a modal dialog.
     */
    public static final int MODAL = 1;

    /**
     * Enum value for the modal property of instances of this class indicating a non-modal dialog.
     */
    public static final int NONMODAL = 0;


    /**
     * typically called by minimize/windowshade jsx3.gui.ToolbarButton in the dialog box's caption bar; toggles the window's state between full-size and window-shaded (where only the dialog's caption bar is visible); or fully minimized to the application task bar if one exists
     * @param STATE if != null, window state is set to fullsize (jsx3.gui.Dialog.MAXIMIZED) and window-shade (jsx3.gui.Dialog.MINIMIZED); if no value is passed, the state is toggled
     */
    public void doToggleState(int STATE)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "doToggleState", STATE);
        ScriptSessions.addScript(script);
    }

    /**
     * Returns whether this dialog instance is the front-most dialog among all open dialogs
     */

    public void isFront(org.directwebremoting.ui.Callback<Boolean> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "isFront");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, Boolean.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Returns object handle to the jsx3.gui.ToolbarButton instance that resides in the application (this.getServer()) task bar and is associated with this dialog instance; returns null if none found
     * @param objTaskBar specify the task bar to search in
     * @return or null
     */

    public jsx3.gui.ToolbarButton getTaskButton(jsx3.gui.WindowBar objTaskBar)
    {
        String extension = "getTaskButton(\"" + objTaskBar + "\").";
        try
        {
            java.lang.reflect.Constructor<jsx3.gui.ToolbarButton> ctor = jsx3.gui.ToolbarButton.class.getConstructor(Context.class, String.class);
            return ctor.newInstance(this, extension);
        }
        catch (Exception ex)
        {
            throw new IllegalArgumentException("Unsupported type: " + jsx3.gui.ToolbarButton.class.getName());
        }
    }


    /**
     * removes the dialog box from the JSX DOM and removes its on-screen VIEW from the browser DOM
     */
    public void doClose()
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "doClose");
        ScriptSessions.addScript(script);
    }

    /**
     * Returns state of the window (full-size / window-shaded). Default: jsx3.gui.Dialog.MAXIMIZED
     * @param callback one of: jsx3.gui.Dialog.MAXIMIZED or jsx3.gui.Dialog.MINIMIZED
     */

    public void getWindowState(org.directwebremoting.ui.Callback<Integer> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getWindowState");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, Integer.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Sets state of the window (full-size / window-shaded); returns ref to self for method chaining
     * @param STATE one of: jsx3.gui.Dialog.MAXIMIZED or jsx3.gui.Dialog.MINIMIZED
     * @return this object
     */
    public jsx3.gui.Dialog setWindowState(int STATE)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setWindowState", STATE);
        ScriptSessions.addScript(script);
        return this;
    }

    /**
     * Returns numeric multiplier for the dialog's z-index. If a dialog box needs to always be on top of other dialog box instances, this multiplier can be increased to assure the appropriate zIndex.  For example, a value of 5 would mean that this dialog box would be stacked on top of all dialog boxes with a alwaysOnTop multiplier less than 5. Default: 1
     * @param callback integer
     */

    public void getZMultiplier(org.directwebremoting.ui.Callback<Integer> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getZMultiplier");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, Integer.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Sets numeric multiplier for the dialog's z-index. If a dialog box needs to always be on top of other dialog box instances, this multiplier can be increased to assure the appropriate zIndex.  For example, a value of 5 would mean that this dialog box would be stacked on top of all dialog boxes with a alwaysOnTop multiplier less than 5
     * @param intMultiplier integer; zero is allowed, but is not recommended; passing null is equivalent to passing 1
     * @return this object
     */
    public jsx3.gui.Dialog setZMultiplier(int intMultiplier)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setZMultiplier", intMultiplier);
        ScriptSessions.addScript(script);
        return this;
    }

    /**
     * Returns whether a dialog displays as modal or not. Modal dialogs mask the rest of the container with an semi-transparent mask that blocks mouse interaction. Modal dialogs do not show up in the task bar. Default: jsx3.gui.Dialog.NONMODAL
     * @param callback one of: jsx3.gui.Dialog.NONMODAL or jsx3.gui.Dialog.MODAL
     */

    public void getModal(org.directwebremoting.ui.Callback<Integer> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getModal");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, Integer.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Sets whether a dialog displays as modal or not. Modal dialogs mask the rest of the container with an semi-transparent mask that blocks mouse interaction. Modal dialogs do not show up in the task bar.
     * @param intModal one of: jsx3.gui.Dialog.NONMODAL or jsx3.gui.Dialog.MODAL
     * @return this object
     */
    public jsx3.gui.Dialog setModal(int intModal)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setModal", intModal);
        ScriptSessions.addScript(script);
        return this;
    }

    /**
     * Toggles the state of the dialog between 'maximized' and its 'initial state'
     * @param objTBB toolbarbutton instance on the dialog to toggle the image/tip text for
     */
    public void doMaximize(jsx3.gui.ToolbarButton objTBB)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "doMaximize", objTBB);
        ScriptSessions.addScript(script);
    }

    /**
     * Returns whether the dialog can be resized or not. Default: jsx3.gui.Dialog.RESIZABLE
     * @param callback one of: jsx3.gui.Dialog.RESIZABLE jsx3.gui.Dialog.FIXED
     */

    public void getResize(org.directwebremoting.ui.Callback<Integer> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getResize");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, Integer.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Sets whether the dialog box's on-screen view can be resized; returns reference to self to facilitate method chaining
     * @param RESIZE one of: jsx3.gui.Dialog.RESIZABLE jsx3.gui.Dialog.FIXED
     * @return this object
     */
    public jsx3.gui.Dialog setResize(int RESIZE)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setResize", RESIZE);
        ScriptSessions.addScript(script);
        return this;
    }

    /**
     * Sets resize parameters such as min width, max width, etc for the dialog; returns reference to self to facilitate method chaining
     * @param RESIZE one of: jsx3.gui.Dialog.RESIZABLE jsx3.gui.Dialog.FIXED
     * @param intMinX min width for the dialog when being resized
     * @param intMinY min height for the dialog when being resized
     * @param intMaxX max width for the dialog when being resized
     * @param intMaxY max heightfor the dialog when being resized
     * @param strAfterResizeFunction
     * @return this object
     */
    public jsx3.gui.Dialog setResizeParameters(int RESIZE, int intMinX, int intMinY, int intMaxX, int intMaxY, String strAfterResizeFunction)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setResizeParameters", RESIZE, intMinX, intMinY, intMaxX, intMaxY, strAfterResizeFunction);
        ScriptSessions.addScript(script);
        return this;
    }

    /**
     * Returns an object handle to the jsx3.gui.WindowBar instance associated with the jsx3.gui.Dialog instance
     * @return jsx3.gui.WindowBar instance or null
     */

    public jsx3.gui.WindowBar getCaptionBar()
    {
        String extension = "getCaptionBar().";
        try
        {
            java.lang.reflect.Constructor<jsx3.gui.WindowBar> ctor = jsx3.gui.WindowBar.class.getConstructor(Context.class, String.class);
            return ctor.newInstance(this, extension);
        }
        catch (Exception ex)
        {
            throw new IllegalArgumentException("Unsupported type: " + jsx3.gui.WindowBar.class.getName());
        }
    }


    /**
     * Implements necessary method for the Alerts interface.
     * @return this object.
     */

    public jsx3.gui.Block getAlertsParent()
    {
        String extension = "getAlertsParent().";
        try
        {
            java.lang.reflect.Constructor<jsx3.gui.Block> ctor = jsx3.gui.Block.class.getConstructor(Context.class, String.class);
            return ctor.newInstance(this, extension);
        }
        catch (Exception ex)
        {
            throw new IllegalArgumentException("Unsupported type: " + jsx3.gui.Block.class.getName());
        }
    }

    /**
     * Implements necessary method for the Alerts interface.
     * @param returnType The expected return type
     * @return this object.
     */

    public <T> T getAlertsParent(Class<T> returnType)
    {
        String extension = "getAlertsParent().";
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
     * Modifies the top and left properties of this dialog in order to fit it within its parent container.
This method ensures that at least a certain amount of the dialog shows on the East, South, and West edges
of the server and that the entire dialog shows on the North edge.

This method is called by default after the user moves a dialog with the mouse. If an AFTER_MOVE
model event is specified, then this method is not called automatically and must be called explicitly by the
model event.
     * @param arg if <code>true</code>, this dialog will be placed entirely within its container,
   with a certain amount of padding, and this dialog will be resized if necessary. If this argument is an array,
   it is taken as the N-E-S-W minimum pixels to show after constraining the position of the dialog. A
   <code>null</code> value for any dimension means that the entire dimension should be shown. A negative value
   means the number of pixels less than the size of the dialog in that dimension.
     */
    public void constrainPosition(boolean arg)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "constrainPosition", arg);
        ScriptSessions.addScript(script);
    }

    /**
     * Modifies the top and left properties of this dialog in order to fit it within its parent container.
This method ensures that at least a certain amount of the dialog shows on the East, South, and West edges
of the server and that the entire dialog shows on the North edge.

This method is called by default after the user moves a dialog with the mouse. If an AFTER_MOVE
model event is specified, then this method is not called automatically and must be called explicitly by the
model event.
     * @param arg if <code>true</code>, this dialog will be placed entirely within its container,
   with a certain amount of padding, and this dialog will be resized if necessary. If this argument is an array,
   it is taken as the N-E-S-W minimum pixels to show after constraining the position of the dialog. A
   <code>null</code> value for any dimension means that the entire dimension should be shown. A negative value
   means the number of pixels less than the size of the dialog in that dimension.
     */
    public void constrainPosition(Object[] arg)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "constrainPosition", arg);
        ScriptSessions.addScript(script);
    }

    /**
     * alerts the user's attention to the dialog box by making its caption bar 'flash' on-screen (as it typical with a windows modal dialog)
     */
    public void beep()
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "beep");
        ScriptSessions.addScript(script);
    }

    /**
     * show an alert dialog
     * @param strTitle the title of the dialog
     * @param strMessage the message to display
     * @param fctOnOk callback function on pressing ok button, receives the dialog as an argument; if null the dialog will close itself; if defined must explicitly close the dialog
     * @param strOk the text of the ok button, can be false to remove button from display
     * @param objParams argument to configureAlert()
     */
    public void alert(String strTitle, String strMessage, org.directwebremoting.ui.CodeBlock fctOnOk, String strOk, String objParams)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "alert", strTitle, strMessage, fctOnOk, strOk, objParams);
        ScriptSessions.addScript(script);
    }

    /**
     * configure the dialog
     * @param objDialog the dialog
     * @param objParams may include fields 'width', 'height', 'noTitle', and 'nonModal'.
     */
    public void configureAlert(java.lang.Object objDialog, jsx3.lang.Object objParams)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "configureAlert", objDialog, objParams);
        ScriptSessions.addScript(script);
    }

    /**
     * show a confirm alert
     * @param strTitle the title of the dialog
     * @param strMessage the message to display
     * @param fctOnOk callback function on pressing ok button, receives the dialog as an argument; if null the dialog will close itself; if defined must explicitly close the dialog
     * @param fctOnCancel callback function on pressing cancel button, receives the dialog as an argument; if null the dialog will close itself; if defined must explicitly close the dialog
     * @param strOk the text of the ok button
     * @param strCancel the text of the cancel button
     * @param intBtnDefault the bold button that receives return key, 1:ok, 2:cancel, 3:no
     * @param fctOnNo callback function on pressing no button, receives the dialog as an argument; if null the dialog will close itself; if defined must explicitly close the dialog
     * @param strNo the text of the no button
     * @param objParams argument to configureAlert()
     */
    public void confirm(String strTitle, String strMessage, org.directwebremoting.ui.CodeBlock fctOnOk, org.directwebremoting.ui.CodeBlock fctOnCancel, String strOk, String strCancel, int intBtnDefault, org.directwebremoting.ui.CodeBlock fctOnNo, String strNo, String objParams)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "confirm", strTitle, strMessage, fctOnOk, fctOnCancel, strOk, strCancel, intBtnDefault, fctOnNo, strNo, objParams);
        ScriptSessions.addScript(script);
    }

    /**
     * show a text box input prompt
     * @param strTitle the title of the dialog
     * @param strMessage the message to display
     * @param fctOnOk callback function on pressing ok button, receives the dialog as an argument, and the value of the text input as a second argument; if null the dialog will close itself; if defined must explicitly close the dialog
     * @param fctOnCancel callback function on pressing cancel button, receives the dialog as an argument; if null the dialog will close itself; if defined must explicitly close the dialog
     * @param strOk the text of the ok button
     * @param strCancel the text of the cancel button
     * @param objParams argument to configureAlert()
     */
    public void prompt(String strTitle, String strMessage, org.directwebremoting.ui.CodeBlock fctOnOk, org.directwebremoting.ui.CodeBlock fctOnCancel, String strOk, String strCancel, String objParams)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "prompt", strTitle, strMessage, fctOnOk, fctOnCancel, strOk, strCancel, objParams);
        ScriptSessions.addScript(script);
    }

}

