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
import org.directwebremoting.ScriptSessions;
import org.directwebremoting.io.Context;

/**
 * Represents a vector fill, the color and gradient that fills a solid vector shape.
 * @author Joe Walker [joe at getahead dot org]
 * @author DRAPGEN - Dwr Reverse Ajax Proxy GENerator
 */
public class Fill extends jsx3.html.Tag
{
    /**
     * All reverse ajax proxies need context to work from
     * @param context The script that got us to where we are now
     */
    public Fill(Context context, String extension)
    {
        super(context, extension);
    }

    /**
     * The instance initializer.
     * @param color the color value, as a hex String or 24-bit integer value, defaults to 0x000000
     * @param alpha the opacity value, valid values are between 0 and 1, defaults to 1
     */
    public Fill(int color, float alpha)
    {
        super((Context) null, (String) null);
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall("new Fill", color, alpha);
        setInitScript(script);
    }

    /**
     * The instance initializer.
     * @param color the color value, as a hex String or 24-bit integer value, defaults to 0x000000
     * @param alpha the opacity value, valid values are between 0 and 1, defaults to 1
     */
    public Fill(String color, float alpha)
    {
        super((Context) null, (String) null);
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall("new Fill", color, alpha);
        setInitScript(script);
    }



    /**
     * Returns the color field, as previously set in the constructor or with setColor().
     */
    public void getColor()
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "getColor");
        ScriptSessions.addScript(script);
    }

    /**
     * Returns the color field, as a CSS hex string.
     */

    public void getColorHtml(org.directwebremoting.ui.Callback<String> callback)
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
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, String.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Sets the color field.
     * @param color the new value for color
     */
    public void setColor(Integer color)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setColor", color);
        ScriptSessions.addScript(script);
    }

    /**
     * Sets the color field.
     * @param color the new value for color
     */
    public void setColor(String color)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setColor", color);
        ScriptSessions.addScript(script);
    }

    /**
     * Returns the alpha field, as previously set in the constructor or with setAlpha().
     * @param callback alpha
     */

    public void getAlpha(org.directwebremoting.ui.Callback<Float> callback)
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
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, Float.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Sets the alpha field, valid values are between 0 (transparent) and 1 (opaque)..
     * @param alpha the new value for alpha
     */
    public void setAlpha(float alpha)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setAlpha", alpha);
        ScriptSessions.addScript(script);
    }

    /**
     * Returns the type field, as set with setType().
     * @param callback type
     */

    public void getType(org.directwebremoting.ui.Callback<String> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getType");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, String.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Sets the type field, valid values are enumerated in the VML specification, though only 'solid', 'gradient', and 'gradientradial' are truly supported by this class.
     * @param type the new value for type
     */
    public void setType(String type)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setType", type);
        ScriptSessions.addScript(script);
    }

    /**
     * Returns the color2 field, as set with setColor2().
     */
    public void getColor2()
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "getColor2");
        ScriptSessions.addScript(script);
    }

    /**
     * ? getColor2Html() {String} gets the color2 field, as a CSS hex string
     */
    public void getColor2Html()
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "getColor2Html");
        ScriptSessions.addScript(script);
    }

    /**
     * Sets the color2 field.
     * @param color2 the new value for color2
     */
    public void setColor2(String color2)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setColor2", color2);
        ScriptSessions.addScript(script);
    }

    /**
     * Sets the color2 field.
     * @param color2 the new value for color2
     */
    public void setColor2(Integer color2)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setColor2", color2);
        ScriptSessions.addScript(script);
    }

    /**
     * Returns the alpha2 field, as set with setAlpha2().
     * @param callback alpha2
     */

    public void getAlpha2(org.directwebremoting.ui.Callback<Float> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getAlpha2");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, Float.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Sets the alpha2 field, valid values are between 0 (transparent) and 1 (opaque)..
     * @param alpha2 the new value for alpha2
     */
    public void setAlpha2(float alpha2)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setAlpha2", alpha2);
        ScriptSessions.addScript(script);
    }

    /**
     * Returns the angle field (the angle along which the gradient goes), as set with setAngle().
     * @param callback angle
     */

    public void getAngle(org.directwebremoting.ui.Callback<Integer> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getAngle");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, Integer.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Sets the angle field, valid values are between 0 and 360. 0 is the vector pointing rightward.
     * @param angle the new value for angle
     */
    public void setAngle(int angle)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setAngle", angle);
        ScriptSessions.addScript(script);
    }

    /**
     * Returns the colors field, as set with setColors().
     * @param callback colors
     */

    public void getColors(org.directwebremoting.ui.Callback<String> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getColors");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, String.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Sets the colors field, see the documentation for <fill> in the VML documentation.
     * @param colors the new value for colors
     */
    public void setColors(String colors)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setColors", colors);
        ScriptSessions.addScript(script);
    }

    /**
     * Parses a vector fill from its string representation. The format is "color alpha".
     * @param v the string representation of a fill.
     * @return <code>null</code> if <code>v</code> is empty, <code>v</code> if <code>v</code>
    is already a vector fill, or otherwise a new vector fill created by parsing the string according to the
    format specified above.
     */

    public jsx3.vector.Fill valueOf(String v)
    {
        String extension = "valueOf(\"" + v + "\").";
        try
        {
            java.lang.reflect.Constructor<jsx3.vector.Fill> ctor = jsx3.vector.Fill.class.getConstructor(Context.class, String.class);
            return ctor.newInstance(this, extension);
        }
        catch (Exception ex)
        {
            throw new IllegalArgumentException("Unsupported type: " + jsx3.vector.Fill.class.getName());
        }
    }


    /**
     * Parses a vector fill from its string representation. The format is "color alpha".
     * @param v the string representation of a fill.
     * @return <code>null</code> if <code>v</code> is empty, <code>v</code> if <code>v</code>
    is already a vector fill, or otherwise a new vector fill created by parsing the string according to the
    format specified above.
     */

    public jsx3.vector.Fill valueOf(jsx3.vector.Fill v)
    {
        String extension = "valueOf(\"" + v + "\").";
        try
        {
            java.lang.reflect.Constructor<jsx3.vector.Fill> ctor = jsx3.vector.Fill.class.getConstructor(Context.class, String.class);
            return ctor.newInstance(this, extension);
        }
        catch (Exception ex)
        {
            throw new IllegalArgumentException("Unsupported type: " + jsx3.vector.Fill.class.getName());
        }
    }


}

