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
package jsx3.vector;

import org.directwebremoting.ScriptBuffer;
import org.directwebremoting.proxy.ScriptProxy;
import org.directwebremoting.proxy.io.Context;

/**
 * The base class for jsx3.vector.Group and jsx3.vector.Shape. Defines getters and setters for the shared vector
tag attributes and CSS style extensions.
 * @author Joe Walker [joe at getahead dot org]
 * @author DRAPGEN - Dwr Reverse Ajax Proxy GENerator
 */
public class Tag extends jsx3.html.BlockTag
{
    /**
     * All reverse ajax proxies need context to work from
     * @param scriptProxy The place we are writing scripts to
     * @param context The script that got us to where we are now
     */
    public Tag(Context context, String extension, ScriptProxy scriptProxy)
    {
        super(context, extension, scriptProxy);
    }

    /**
     * The instance initializer.
     * @param strTagName 
     * @param left left position (in pixels) of the object relative to its parent container
     * @param top top position (in pixels) of the object relative to its parent container
     * @param width width (in pixels) of the object
     * @param height height (in pixels) of the object
     */
    public Tag(String strTagName, int left, int top, int width, int height)
    {
        super((Context) null, (String) null, (ScriptProxy) null);
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall("new Tag", strTagName, left, top, width, height);
        setInitScript(script);
    }



    /**
     * Returns the tooltip, the text that is displayed on mouse over.
     */
    public void getToolTip()
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "getToolTip");
        getScriptProxy().addScript(script);
    }

    /**
     * Sets the tooltip, the text that is displayed on mouse over.
     * @param title 
     */
    public void setToolTip(String title)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setToolTip", title);
        getScriptProxy().addScript(script);
    }

    /**
     * Returns the rotation field.
     * @param callback rotation
     */
    @SuppressWarnings("unchecked")
    public void getRotation(org.directwebremoting.proxy.Callback<Integer> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getRotation");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelper.saveCallback(callback, Integer.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        getScriptProxy().addScript(script);
    }

    /**
     * Sets the rotation field, an angle between 0 and 360.
     * @param rotation the new value for rotation
     */
    public void setRotation(int rotation)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setRotation", rotation);
        getScriptProxy().addScript(script);
    }

}

