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
 * The interface that defines the methods that affect the behavior of a composite object used as an edit mask of a
matrix column. Any object used as an edit mask that extends jsx3.gui.Block but does not implement
jsx3.gui.Form will have these methods inserted into it. Such an object only has to define the methods
whose default behavior it wishes to override.
 * @author Joe Walker [joe at getahead dot org]
 * @author DRAPGEN - Dwr Reverse Ajax Proxy GENerator
 */
public class BlockMask extends jsx3.gui.matrix.EditMask
{
    /**
     * All reverse ajax proxies need context to work from
     * @param context The script that got us to where we are now
     */
    public BlockMask(Context context, String extension)
    {
        super(context, extension);
    }



    /**
     * 
     * @param objColumn
     */
    public void emInit(java.lang.Object objColumn)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "emInit", objColumn);
        ScriptSessions.addScript(script);
    }

    /**
     * 
     * @param strValue
     * @param objTdDim
     * @param objPaneDim
     * @param objMatrix
     * @param objColumn
     * @param strRecordId
     * @param objTD
     */
    public void emBeginEdit(String strValue, java.lang.Object objTdDim, java.lang.Object objPaneDim, java.lang.Object objMatrix, java.lang.Object objColumn, String strRecordId, java.lang.Object objTD)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "emBeginEdit", strValue, objTdDim, objPaneDim, objMatrix, objColumn, strRecordId, objTD);
        ScriptSessions.addScript(script);
    }

    /**
     * 
     */
    public void emGetValue()
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "emGetValue");
        ScriptSessions.addScript(script);
    }

    /**
     * Returns the DOM node that should be focused when the edit session begins. The default behavior is to return
the first descendant (breadth-first) that implements jsx3.gui.Form.
     */

    public jsx3.gui.Painted getMaskFirstResponder()
    {
        String extension = "getMaskFirstResponder().";
        try
        {
            java.lang.reflect.Constructor<jsx3.gui.Painted> ctor = jsx3.gui.Painted.class.getConstructor(Context.class, String.class);
            return ctor.newInstance(this, extension);
        }
        catch (Exception ex)
        {
            throw new IllegalArgumentException("Unsupported type: " + jsx3.gui.Painted.class.getName());
        }
    }

    /**
     * Returns the DOM node that should be focused when the edit session begins. The default behavior is to return
the first descendant (breadth-first) that implements jsx3.gui.Form.
     * @param returnType The expected return type
     */

    public <T> T getMaskFirstResponder(Class<T> returnType)
    {
        String extension = "getMaskFirstResponder().";
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
     * Returns the value currently stored in this edit mask. The default behavior is to return the value of
the first descendant (breadth-first) that implements jsx3.gui.Form.
     */

    public void getMaskValue(org.directwebremoting.ui.Callback<String> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getMaskValue");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, String.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Sets the value currently stored in this edit mask. The default behavior is to set the value of
the first descendant (breadth-first) that implements jsx3.gui.Form.
     * @param strValue
     */
    public void setMaskValue(String strValue)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setMaskValue", strValue);
        ScriptSessions.addScript(script);
    }

    /**
     * 
     */
    public void emEndEdit()
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "emEndEdit");
        ScriptSessions.addScript(script);
    }

}

