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
package jsx3.gui.matrix;

import org.directwebremoting.ScriptBuffer;
import org.directwebremoting.ScriptSessions;
import org.directwebremoting.io.Context;

/**
 * The interface defining the methods that affect the behavior of an object used as an edit mask in a matrix column.

If an object is placed in the DOM as a child of a matrix column, it will be used as an edit mask. Any methods
in this interface that the object does not implement will be inserted into the object. This interface is a "loose"
interface because the class of an edit mask does not need to implement it in its class declaration. The class
simply needs to define any methods whose default behavior it wishes to override.

Any edit mask that implements the jsx3.gui.Form interface will have the methods in this interface
inserted into it. If the edit mask does not implement jsx3.gui.Form but extends
jsx3.gui.Block, the methods in the jsx3.gui.Matrix.BlockMask interface are inserted
instead.
 * @author Joe Walker [joe at getahead dot org]
 * @author DRAPGEN - Dwr Reverse Ajax Proxy GENerator
 */
public class EditMask extends jsx3.lang.Object
{
    /**
     * All reverse ajax proxies need context to work from
     * @param context The script that got us to where we are now
     */
    public EditMask(Context context, String extension)
    {
        super(context, extension);
    }



    /**
     * This method is called once when the edit mask is discovered by the matrix column to give it an opportunity
to initialize itself.
     * @param objColumn the matrix column parent.
     */
    public void emInit(jsx3.gui.matrix.Column objColumn)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "emInit", objColumn);
        ScriptSessions.addScript(script);
    }

    /**
     * Called whenever an edit session begins.
     * @param strValue
     * @param objTdDim
     * @param objPaneDim
     * @param objMatrix
     * @param objColumn
     * @param strRecordId
     * @param objTD
     * @param callback <code>false</code> to cancel the edit session.
     */

    public void emBeginEdit(String strValue, jsx3.lang.Object objTdDim, jsx3.lang.Object objPaneDim, jsx3.gui.Matrix objMatrix, jsx3.gui.matrix.Column objColumn, String strRecordId, String objTD, org.directwebremoting.ui.Callback<Boolean> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "emBeginEdit", strValue, objTdDim, objPaneDim, objMatrix, objColumn, strRecordId, objTD);

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, Boolean.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Called when the current edit session ends. This method should return the edited value.
     * @param callback the edited value.
     */

    public void emEndEdit(org.directwebremoting.ui.Callback<String> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "emEndEdit");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, String.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Returns the current value stored in the edit mask.
     * @param callback the current value of the edit mask.
     */

    public void emGetValue(org.directwebremoting.ui.Callback<String> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "emGetValue");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, String.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Suspends an edit session so that if this mask loses focus, the edit session does not close.
     */
    public void suspendEditSession()
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "suspendEditSession");
        ScriptSessions.addScript(script);
    }

    /**
     * Resumes an edit session so that the edit session will close the next time this mask loses focus.
     */
    public void resumeEditSession()
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "resumeEditSession");
        ScriptSessions.addScript(script);
    }

    /**
     * Returns the state of the current edit session if this object is involved in a jsx3.gui.Matrix
edit mask session. The state has the following keys:

matrix {jsx3.gui.Matrix}
column {jsx3.gui.Matrix.Column}
recordId {String}
td {HTMLElement}
value {String} may be null
     * @return the edit session.
     */

    public jsx3.lang.Object emGetSession()
    {
        String extension = "emGetSession().";
        try
        {
            java.lang.reflect.Constructor<jsx3.lang.Object> ctor = jsx3.lang.Object.class.getConstructor(Context.class, String.class);
            return ctor.newInstance(this, extension);
        }
        catch (Exception ex)
        {
            throw new IllegalArgumentException("Unsupported type: " + jsx3.lang.Object.class.getName());
        }
    }

    /**
     * Returns the state of the current edit session if this object is involved in a jsx3.gui.Matrix
edit mask session. The state has the following keys:

matrix {jsx3.gui.Matrix}
column {jsx3.gui.Matrix.Column}
recordId {String}
td {HTMLElement}
value {String} may be null
     * @param returnType The expected return type
     * @return the edit session.
     */

    public <T> T emGetSession(Class<T> returnType)
    {
        String extension = "emGetSession().";
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
     * Commits the current edit session of this edit mask.
     * @param objEvent the wrapped browser event that logically caused this commit to occur. If this
   parameter is provided then all the model events related to committing an edit session are triggered.
     * @param bKeepOpen if <code>true</code> then the current value of this edit mask is committed without
   closing the current edit session.
     */
    public void commitEditMask(jsx3.gui.Event objEvent, boolean bKeepOpen)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "commitEditMask", objEvent, bKeepOpen);
        ScriptSessions.addScript(script);
    }

}

