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
 * A class that defines the methods required for an object to be used by Matrix.Column instances to
format cells of data.
 * @author Joe Walker [joe at getahead dot org]
 * @author DRAPGEN - Dwr Reverse Ajax Proxy GENerator
 */
public class ColumnFormat extends jsx3.lang.Object
{
    /**
     * All reverse ajax proxies need context to work from
     * @param context The script that got us to where we are now
     */
    public ColumnFormat(Context context, String extension)
    {
        super(context, extension);
    }



    /**
     * Returns a column formatter for a string key. The key may be one of the following:


          @unescape Ð

          @unescape_all Ð

          @lookup Ð

          @datetime[,(short|medium|long|full,format)] Ð

          @date[,(short|medium|long|full,format)] Ð

          @time[,(short|medium|long|full,format)] Ð

          @number[,(integer|percent|currency,format)] Ð

          @message,format
           Ð
     * @param strKey
     * @param objColumn
     */

    public jsx3.gui.matrix.ColumnFormat getInstance(String strKey, jsx3.gui.matrix.Column objColumn)
    {
        String extension = "getInstance(\"" + strKey + "\", \"" + objColumn + "\").";
        try
        {
            java.lang.reflect.Constructor<jsx3.gui.matrix.ColumnFormat> ctor = jsx3.gui.matrix.ColumnFormat.class.getConstructor(Context.class, String.class);
            return ctor.newInstance(this, extension);
        }
        catch (Exception ex)
        {
            throw new IllegalArgumentException("Unsupported type: " + jsx3.gui.matrix.ColumnFormat.class.getName());
        }
    }


    /**
     * Classes that implement this interface must provide this method to allow for browser-specific or similar type 'switch'. If
false is returned, the formatter will not even attempt to iterate
     * @param callback true if the formatter should be called to iterate and format
     */

    public void validate(org.directwebremoting.ui.Callback<Boolean> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "validate");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, Boolean.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Formats the Matrix cell, a native DIV element.
     * @param objDiv on-screen DIV element to be formatted. Note that this DIV is contained within a TD
     * @param strCDFKey CDF record id for the record in the data model bound to the affected on-screen row
     * @param objMatrix matrix instance
     * @param objMatrixColumn matrix column instance
     * @param intRowNumber row number for row containing this cell (1-based)
     * @param objServer server instance. Useful for querying locale (for localized output)
     */
    public void format(String objDiv, String strCDFKey, jsx3.gui.Matrix objMatrix, jsx3.gui.matrix.Column objMatrixColumn, int intRowNumber, jsx3.app.Server objServer)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "format", objDiv, strCDFKey, objMatrix, objMatrixColumn, intRowNumber, objServer);
        ScriptSessions.addScript(script);
    }

}

