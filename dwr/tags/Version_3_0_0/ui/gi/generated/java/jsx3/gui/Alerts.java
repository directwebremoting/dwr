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
 * Mixin interface allows implementors to show alerts, confirms, and prompts.
 * @author Joe Walker [joe at getahead dot org]
 * @author DRAPGEN - Dwr Reverse Ajax Proxy GENerator
 */
public class Alerts extends jsx3.lang.Object
{
    /**
     * All reverse ajax proxies need context to work from
     * @param context The script that got us to where we are now
     */
    public Alerts(Context context, String extension)
    {
        super(context, extension);
    }



    /**
     * implementors of this mixin interface must implement this method
     * @return the parent of the alert dialogs
     */

    public jsx3.app.Model getAlertsParent()
    {
        String extension = "getAlertsParent().";
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
     * implementors of this mixin interface must implement this method
     * @param returnType The expected return type
     * @return the parent of the alert dialogs
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

}

