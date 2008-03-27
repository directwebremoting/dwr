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
package jsx3.chart;

import org.directwebremoting.ScriptBuffer;
import org.directwebremoting.proxy.ScriptProxy;
import org.directwebremoting.proxy.io.Context;

/**
 * A chart component to render a text box. Used to render the titles of charts, legends, axes, and series.
Encapsulates all the font settings of the label so that the parent container does not experience 
property bloat.
 * @author Joe Walker [joe at getahead dot org]
 * @author DRAPGEN - Dwr Reverse Ajax Proxy GENerator
 */
public class ChartLabel extends jsx3.chart.ChartComponent
{
    /**
     * All reverse ajax proxies need context to work from
     * @param scriptProxy The place we are writing scripts to
     * @param context The script that got us to where we are now
     */
    public ChartLabel(Context context, String extension, ScriptProxy scriptProxy)
    {
        super(context, extension, scriptProxy);
    }

    /**
     * The instance initializer.
     * @param name the GI name of the instance
     * @param text text to display in the label
     */
    public ChartLabel(String name, String text)
    {
        super((Context) null, (String) null, (ScriptProxy) null);
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall("new ChartLabel", name, text);
        setInitScript(script);
    }


    /**
     * the default preferred width
     */
    public static final int DEFAULT_WIDTH = 100;

    /**
     * non-rotated angle
     */
    public static final int ROTATION_NORMAL = 0;

    /**
     * angle for one-quarter clockwise rotation
     */
    public static final int ROTATION_CW = 90;

    /**
     * angle for one-quarter counter-clockwise rotation
     */
    public static final int ROTATION_CCW = 270;


    /**
     * Returns the preferredWidth field.
     * @param callback preferredWidth
     */
    @SuppressWarnings("unchecked")
    public void getPreferredWidth(org.directwebremoting.proxy.Callback<Integer> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getPreferredWidth");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelper.saveCallback(callback, Integer.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        getScriptProxy().addScript(script);
    }

    /**
     * Sets the preferredWidth field.
     * @param preferredWidth the new value for preferredWidth
     */
    public void setPreferredWidth(int preferredWidth)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setPreferredWidth", preferredWidth);
        getScriptProxy().addScript(script);
    }

    /**
     * Returns the preferredHeight field.
     * @param callback preferredHeight
     */
    @SuppressWarnings("unchecked")
    public void getPreferredHeight(org.directwebremoting.proxy.Callback<Integer> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getPreferredHeight");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelper.saveCallback(callback, Integer.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        getScriptProxy().addScript(script);
    }

    /**
     * Sets the preferredHeight field.
     * @param preferredHeight the new value for preferredHeight
     */
    public void setPreferredHeight(int preferredHeight)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setPreferredHeight", preferredHeight);
        getScriptProxy().addScript(script);
    }

    /**
     * Returns the alpha field, the opacity of the background fill.
     * @param callback alpha
     */
    @SuppressWarnings("unchecked")
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
     * Returns the borderStroke field, string representation of the VectorStroke used to outline the background.
     * @param callback borderStroke
     */
    @SuppressWarnings("unchecked")
    public void getBorderStroke(org.directwebremoting.proxy.Callback<String> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getBorderStroke");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelper.saveCallback(callback, String.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        getScriptProxy().addScript(script);
    }

    /**
     * Sets the borderStroke field.
     * @param borderStroke the new value for borderStroke
     */
    public void setBorderStroke(String borderStroke)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setBorderStroke", borderStroke);
        getScriptProxy().addScript(script);
    }

    /**
     * Returns the labelRotation field.
     * @param callback labelRotation
     */
    @SuppressWarnings("unchecked")
    public void getLabelRotation(org.directwebremoting.proxy.Callback<Integer> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getLabelRotation");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelper.saveCallback(callback, Integer.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        getScriptProxy().addScript(script);
    }

    /**
     * Sets the labelRotation field.
     * @param labelRotation the new value for labelRotation, one of {0, 90, 270}
     */
    public void setLabelRotation(int labelRotation)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setLabelRotation", labelRotation);
        getScriptProxy().addScript(script);
    }

    /**
     * whether this label is display at 90 or -90 degrees
     */
    @SuppressWarnings("unchecked")
    public void isRotated(org.directwebremoting.proxy.Callback<Boolean> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "isRotated");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelper.saveCallback(callback, Boolean.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        getScriptProxy().addScript(script);
    }

}

