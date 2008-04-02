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
 * Represents a vector line style.
 * @author Joe Walker [joe at getahead dot org]
 * @author DRAPGEN - Dwr Reverse Ajax Proxy GENerator
 */
public class Stroke extends jsx3.html.Tag
{
    /**
     * All reverse ajax proxies need context to work from
     * @param scriptProxy The place we are writing scripts to
     * @param context The script that got us to where we are now
     */
    public Stroke(Context context, String extension, ScriptProxy scriptProxy)
    {
        super(context, extension, scriptProxy);
    }

    /**
     * The instance initializer.
     * @param color the color value, as a hex String or 24-bit integer value, defaults to 0x000000
     * @param width the width of the stroke, in pixels, defaults to 1
     * @param alpha the opacity value, valid values are between 0 and 1, defaults to 1
     */
    public Stroke(String color, int width, float alpha)
    {
        super((Context) null, (String) null, (ScriptProxy) null);
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall("new Stroke", color, width, alpha);
        setInitScript(script);
    }

    /**
     * The instance initializer.
     * @param color the color value, as a hex String or 24-bit integer value, defaults to 0x000000
     * @param width the width of the stroke, in pixels, defaults to 1
     * @param alpha the opacity value, valid values are between 0 and 1, defaults to 1
     */
    public Stroke(int color, int width, float alpha)
    {
        super((Context) null, (String) null, (ScriptProxy) null);
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall("new Stroke", color, width, alpha);
        setInitScript(script);
    }



    /**
     * Returns the color field.
     * @param callback color
     */

    public void getColor(org.directwebremoting.proxy.Callback<Integer> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getColor");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelper.saveCallback(callback, Integer.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        getScriptProxy().addScript(script);
    }

    /**
     * Returns the color field, as a CSS hex string.
     */

    public void getColorHtml(org.directwebremoting.proxy.Callback<String> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getColorHtml");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelper.saveCallback(callback, String.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        getScriptProxy().addScript(script);
    }

    /**
     * Sets the color field.
     * @param color the new value for color
     */
    public void setColor(String color)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setColor", color);
        getScriptProxy().addScript(script);
    }

    /**
     * Sets the color field.
     * @param color the new value for color
     */
    public void setColor(int color)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setColor", color);
        getScriptProxy().addScript(script);
    }

    /**
     * Returns the width field.
     * @param callback width
     */

    public void getWidth(org.directwebremoting.proxy.Callback<Integer> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getWidth");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelper.saveCallback(callback, Integer.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        getScriptProxy().addScript(script);
    }

    /**
     * Sets the width field.
     * @param width the new value for width
     */
    public void setWidth(int width)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setWidth", width);
        getScriptProxy().addScript(script);
    }

    /**
     * Returns the alpha field.
     * @param callback alpha
     */

    public void getAlpha(org.directwebremoting.proxy.Callback<Float> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getAlpha");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelper.saveCallback(callback, Float.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        getScriptProxy().addScript(script);
    }

    /**
     * Sets the alpha field.
     * @param alpha the new value for alpha
     */
    public void setAlpha(float alpha)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setAlpha", alpha);
        getScriptProxy().addScript(script);
    }

    /**
     * parses a VectorStroke from a string representation, that format is "color width alpha"
     * @param v the string representation
     * @param callback null if v is empty, v if v is already a VectorStroke, or otherwise a new VectorStroke created by parsing the string according to the format specified above
     */

    public void valueOf(String v, org.directwebremoting.proxy.Callback<String> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "valueOf", v);

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelper.saveCallback(callback, String.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        getScriptProxy().addScript(script);
    }

}

