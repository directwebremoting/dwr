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
import org.directwebremoting.proxy.ScriptProxy;
import org.directwebremoting.proxy.io.Context;

/**
 * This class provides a way to organize a set of GUI objects in a grid. The dimensions of each cell in the grid
are determined by the row heights and column widths. The height of each row and width of each column may be defined
either as a percent, an integer pixel value, or as "*". Each dimension may specify one or more divisions as "*",
in which case these rows/columns share the remaining space in that dimension equally once the other rows/columns
are fitted.

GUI objects that are children of this DOM node are rendered in the cells. The first child is rendered in the
top-left corner. Subsequent children are rendered in rows from left to right and top to bottom.
 * @author Joe Walker [joe at getahead dot org]
 * @author DRAPGEN - Dwr Reverse Ajax Proxy GENerator
 */
public class LayoutGrid extends jsx3.gui.Block
{
    /**
     * All reverse ajax proxies need context to work from
     * @param scriptProxy The place we are writing scripts to
     * @param context The script that got us to where we are now
     */
    public LayoutGrid(Context context, String extension, ScriptProxy scriptProxy)
    {
        super(context, extension, scriptProxy);
    }

    /**
     * The instance initializer.
     * @param strName unique name distinguishing this object from all other JSX GUI objects in the JSX application
     */
    public LayoutGrid(String strName)
    {
        super((Context) null, (String) null, (ScriptProxy) null);
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall("new LayoutGrid", strName);
        setInitScript(script);
    }



    /**
     * 
     */
    @SuppressWarnings("unchecked")
    public void getCols(org.directwebremoting.proxy.Callback<String> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getCols");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelper.saveCallback(callback, String.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        getScriptProxy().addScript(script);
    }

    /**
     * 
     * @param strCols 
     * @param bRepaint 
     * @return this object.
     */
    public jsx3.gui.LayoutGrid setCols(String strCols, boolean bRepaint)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setCols", strCols, bRepaint);
        getScriptProxy().addScript(script);
        return this;
    }

    /**
     * 
     */
    @SuppressWarnings("unchecked")
    public void getRows(org.directwebremoting.proxy.Callback<String> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getRows");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelper.saveCallback(callback, String.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        getScriptProxy().addScript(script);
    }

    /**
     * 
     * @param strRows 
     * @param bRepaint 
     * @return this object.
     */
    public jsx3.gui.LayoutGrid setRows(String strRows, boolean bRepaint)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setRows", strRows, bRepaint);
        getScriptProxy().addScript(script);
        return this;
    }

}

