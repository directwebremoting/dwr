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
 * Provides a object-oriented interface for a standard command button.

This class publishes the following model events:


        EXECUTE Ð when the user clicks on the enabled button, when the button has focus and the user presses the
   space or enter key, or when doExecute() is called on the button.

        MENU Ð on a mouseup event with the right button when the button has a bound context menu.
 * @author Joe Walker [joe at getahead dot org]
 * @author DRAPGEN - Dwr Reverse Ajax Proxy GENerator
 */
public class Button extends jsx3.gui.Block
{
    /**
     * All reverse ajax proxies need context to work from
     * @param context The script that got us to where we are now
     */
    public Button(Context context, String extension)
    {
        super(context, extension);
    }

    /**
     * instance initializer
     * @param strName unique name distinguishing this object from all other JSX GUI objects in the JSX application
     * @param intLeft left position (in pixels) of the object relative to its parent container; not required if button is one of: jsx3.gui.Button.SYSTEMOPEN, jsx3.gui.Button.DIALOGCLOSE, jsx3.gui.Button.DIALOGALPHA, jsx3.gui.Button.DIALOGSHADE
     * @param intTop top position (in pixels) of the object relative to its parent container; not required if button is one of: jsx3.gui.Button.SYSTEMOPEN, jsx3.gui.Button.DIALOGCLOSE, jsx3.gui.Button.DIALOGALPHA, jsx3.gui.Button.DIALOGSHADE
     * @param intWidth width (in pixels) of the object; not required if button is one of: jsx3.gui.Button.SYSTEMOPEN, jsx3.gui.Button.DIALOGCLOSE, jsx3.gui.Button.DIALOGALPHA, jsx3.gui.Button.DIALOGSHADE
     * @param strText text to display in the given button; if null JSXTABLEHEADERCELL.DEFAULTTEXT is used
     */
    public Button(String strName, int intLeft, int intTop, int intWidth, String strText)
    {
        super((Context) null, (String) null);
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall("new Button", strName, intLeft, intTop, intWidth, strText);
        setInitScript(script);
    }


    /**
     * #e8e8f5
     */
    public static final String DEFAULTBACKGROUNDCOLOR = "#e8e8f5";

    /**
     * #f6f6ff
     */
    public static final String DEFAULTHIGHLIGHT = "#f6f6ff";

    /**
     * #a6a6af
     */
    public static final String DEFAULTSHADOW = "#a6a6af";

    /**
     * 18
     */
    public static final int DEFAULTHEIGHT = 17;

    /**
     * jsx30button
     */
    public static final String DEFAULTCLASSNAME = "jsx30button";


    /**
     * Sets the validation state for the button and returns the validation state; always returns jsx3.gui.Form.STATEVALID (as buttons can only be valid)
     * @param callback jsx3.gui.Form.STATEVALID
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
     * Invokes the EXECUTE model event of this button. Note that because the model event is invoked
programmatically rather than by user interaction the objEVENT event parameter will be
null if the objEvent parameter is undefined.
     * @param objEvent the browser event that caused the execution of this button. This argument is
   optional and should only be provided if the execute of this button is the result of a browser event. This
   parameter will be passed along to the model event as <code>objEVENT</code>.
     */
    public void doExecute(jsx3.gui.Event objEvent)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "doExecute", objEvent);
        ScriptSessions.addScript(script);
    }

    /**
     * Because it implements the jsx3.gui.Form class, the jsx3.gui.Button class implements various form-related methods (validate, getValue, disable, etc); in the case of 'getValue', this method will return the button's text (caption)
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

    /**
     * Sets the value of this control.
     * @param vntValue string/int value for the component
     * @return this object.
     */

    public jsx3.gui.Form setValue(Integer vntValue)
    {
        String extension = "setValue(\"" + vntValue + "\").";
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
     * Sets the value of this control.
     * @param vntValue string/int value for the component
     * @param returnType The expected return type
     * @return this object.
     */

    public <T> T setValue(Integer vntValue, Class<T> returnType)
    {
        String extension = "setValue(\"" + vntValue + "\").";
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
     * Sets the value of this control.
     * @param vntValue string/int value for the component
     * @return this object.
     */

    public jsx3.gui.Form setValue(String vntValue)
    {
        String extension = "setValue(\"" + vntValue + "\").";
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
     * Sets the value of this control.
     * @param vntValue string/int value for the component
     * @param returnType The expected return type
     * @return this object.
     */

    public <T> T setValue(String vntValue, Class<T> returnType)
    {
        String extension = "setValue(\"" + vntValue + "\").";
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

