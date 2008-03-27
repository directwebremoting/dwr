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
 * Renders text along an arbitrary line.
 * @author Joe Walker [joe at getahead dot org]
 * @author DRAPGEN - Dwr Reverse Ajax Proxy GENerator
 */
public class TextLine extends jsx3.vector.Shape
{
    /**
     * All reverse ajax proxies need context to work from
     * @param scriptProxy The place we are writing scripts to
     * @param context The script that got us to where we are now
     */
    public TextLine(Context context, String extension, ScriptProxy scriptProxy)
    {
        super(context, extension, scriptProxy);
    }

    /**
     * The instance initializer.
     * @param x1 
     * @param y1 
     * @param x2 
     * @param y2 
     * @param text the text to display on the text path
     */
    public TextLine(int x1, int y1, int x2, int y2, String text)
    {
        super((Context) null, (String) null, (ScriptProxy) null);
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall("new TextLine", x1, y1, x2, y2, text);
        setInitScript(script);
    }



    /**
     * Returns the text field.
     * @param callback text
     */
    @SuppressWarnings("unchecked")
    public void getText(org.directwebremoting.proxy.Callback<String> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getText");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelper.saveCallback(callback, String.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        getScriptProxy().addScript(script);
    }

    /**
     * Sets the text field, the text to display on the text path.
     * @param text the new value for text
     */
    public void setText(String text)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setText", text);
        getScriptProxy().addScript(script);
    }

    /**
     * Returns the color field.
     * @param callback color
     */
    @SuppressWarnings("unchecked")
    public void getColor(org.directwebremoting.proxy.Callback<String> callback)
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
            String key = org.directwebremoting.extend.CallbackHelper.saveCallback(callback, String.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        getScriptProxy().addScript(script);
    }

    /**
     * Returns the fontFamily field.
     * @param callback fontFamily
     */
    @SuppressWarnings("unchecked")
    public void getFontFamily(org.directwebremoting.proxy.Callback<String> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getFontFamily");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelper.saveCallback(callback, String.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        getScriptProxy().addScript(script);
    }

    /**
     * Returns the fontsize field.
     * @param callback fontsize
     */
    @SuppressWarnings("unchecked")
    public void getFontSize(org.directwebremoting.proxy.Callback<String> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getFontSize");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelper.saveCallback(callback, String.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        getScriptProxy().addScript(script);
    }

    /**
     * Returns the fontStyle field.
     * @param callback fontStyle
     */
    @SuppressWarnings("unchecked")
    public void getFontStyle(org.directwebremoting.proxy.Callback<String> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getFontStyle");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelper.saveCallback(callback, String.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        getScriptProxy().addScript(script);
    }

    /**
     * Returns the fontWeight field.
     * @param callback fontWeight
     */
    @SuppressWarnings("unchecked")
    public void getFontWeight(org.directwebremoting.proxy.Callback<String> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getFontWeight");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelper.saveCallback(callback, String.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        getScriptProxy().addScript(script);
    }

    /**
     * Returns the textAlign field.
     * @param callback textAlign
     */
    @SuppressWarnings("unchecked")
    public void getTextAlign(org.directwebremoting.proxy.Callback<String> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getTextAlign");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelper.saveCallback(callback, String.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        getScriptProxy().addScript(script);
    }

    /**
     * Returns the textDecoration field.
     * @param callback textDecoration
     */
    @SuppressWarnings("unchecked")
    public void getTextDecoration(org.directwebremoting.proxy.Callback<String> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getTextDecoration");

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
     * Sets the fontFamily field.
     * @param fontFamily the new value for fontFamily
     */
    public void setFontFamily(String fontFamily)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setFontFamily", fontFamily);
        getScriptProxy().addScript(script);
    }

    /**
     * Sets the fontsize field.
     * @param fontSize the new value for fontsize
     */
    public void setFontSize(String fontSize)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setFontSize", fontSize);
        getScriptProxy().addScript(script);
    }

    /**
     * Sets the fontsize field.
     * @param fontSize the new value for fontsize
     */
    public void setFontSize(int fontSize)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setFontSize", fontSize);
        getScriptProxy().addScript(script);
    }

    /**
     * Sets the fontStyle field.
     * @param fontStyle the new value for fontStyle
     */
    public void setFontStyle(String fontStyle)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setFontStyle", fontStyle);
        getScriptProxy().addScript(script);
    }

    /**
     * Sets the fontWeight field.
     * @param fontWeight the new value for fontWeight
     */
    public void setFontWeight(String fontWeight)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setFontWeight", fontWeight);
        getScriptProxy().addScript(script);
    }

    /**
     * Sets the textAlign field.
     * @param textAlign the new value for textAlign
     */
    public void setTextAlign(String textAlign)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setTextAlign", textAlign);
        getScriptProxy().addScript(script);
    }

    /**
     * Sets the textDecoration field.
     * @param textDecoration the new value for textDecoration
     */
    public void setTextDecoration(String textDecoration)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setTextDecoration", textDecoration);
        getScriptProxy().addScript(script);
    }

}

