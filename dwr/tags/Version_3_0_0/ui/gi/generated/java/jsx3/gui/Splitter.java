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
 * This class manages layouts by providing a container that will paint its first two child GUI objects separated
by a 'splitter' (either vertical or horizontal). Split panes can contain any number of object types, but most
commonly contain JSXBlock elements to aid in layout feautures such as padding, borders, etc.
 * @author Joe Walker [joe at getahead dot org]
 * @author DRAPGEN - Dwr Reverse Ajax Proxy GENerator
 */
public class Splitter extends jsx3.gui.Block
{
    /**
     * All reverse ajax proxies need context to work from
     * @param context The script that got us to where we are now
     */
    public Splitter(Context context, String extension)
    {
        super(context, extension);
    }

    /**
     * instance initializer
     * @param strName unique name distinguishing this object from all other JSX GUI objects in the JSX application
     * @param ORIENTATION constant; either jsx3.gui.Splitter.ORIENTATIONH or jsx3.gui.Splitter.ORIENTATIONV; if none provided the default (horizontal layout) or vertical (stacked) layout)
     */
    public Splitter(String strName, int ORIENTATION)
    {
        super((Context) null, (String) null);
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall("new Splitter", strName, ORIENTATION);
        setInitScript(script);
    }


    /**
     * 
     */
    public static final int ORIENTATIONH = 0;

    /**
     * 
     */
    public static final int ORIENTATIONV = 1;


    /**
     * Returns a valid percentage (e.g., 100.00%  23.567%) that will be applied to the on-screen element as its CSS width/height percentage
     */

    public void getSubcontainer1Pct(org.directwebremoting.ui.Callback<String> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getSubcontainer1Pct");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, String.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Sets a valid percentage (e.g., 100.00%  23.567%) that will be applied to the on-screen element as its CSS width/height percentage
     * @param strSubcontainerPct valid CSS width property as a percentage (e.g., 34.56%)
     * @param bView false if null; if true the view is updated automatically without a repaint
     * @return this object
     */
    public jsx3.gui.Splitter setSubcontainer1Pct(String strSubcontainerPct, boolean bView)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setSubcontainer1Pct", strSubcontainerPct, bView);
        ScriptSessions.addScript(script);
        return this;
    }

    /**
     * Returns a valid integer representing the minimum size in pixels for the container; the default minimum is 1
     */

    public void getSubcontainer1Min(org.directwebremoting.ui.Callback<Integer> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getSubcontainer1Min");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, Integer.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Sets the minimum size in pixels for the container; the default minimum is 1;
           returns reference to self to facilitate method chaining
     * @param intMin integer value represnting the minimum size in pixels for the container
     * @return this object
     */
    public jsx3.gui.Splitter setSubcontainer1Min(int intMin)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setSubcontainer1Min", intMin);
        ScriptSessions.addScript(script);
        return this;
    }

    /**
     * Returns a valid integer representing the minimum size in pixels for the container; the default minimum is 8
     */

    public void getSubcontainer2Min(org.directwebremoting.ui.Callback<Integer> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getSubcontainer2Min");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, Integer.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Sets the minimum size in pixels for the container; the default minimum is 8;
           returns reference to self to facilitate method chaining
     * @param intMin integer value represnting the minimum size in pixels for the container
     * @return this object
     */
    public jsx3.gui.Splitter setSubcontainer2Min(int intMin)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setSubcontainer2Min", intMin);
        ScriptSessions.addScript(script);
        return this;
    }

    /**
     * *Returns whether the splitter layout is top-over (--) or side-by-side (|).
     */

    public void getOrientation(org.directwebremoting.ui.Callback<Integer> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getOrientation");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, Integer.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Returns whether the splitter layout is top-over (--) or side-by-side (|);
         returns reference to self to facilitate method chaining
     * @param ORIENTATION constant; either jsx3.gui.Splitter.ORIENTATIONH or jsx3.gui.Splitter.ORIENTATIONV
     * @return this object
     */
    public jsx3.gui.Splitter setOrientation(int ORIENTATION)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setOrientation", ORIENTATION);
        ScriptSessions.addScript(script);
        return this;
    }

    /**
     * Returns the URL for the image to use for the splitter handle when the splitter is rendered top over bottom (--).  When not set, Splitter.VSPLITIMAGE will be used when painted on-screen.
     */

    public void getVSplitImage(org.directwebremoting.ui.Callback<String> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getVSplitImage");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, String.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Sets the URL for the image to use for the splitter handle when the splitter is rendered top over bottom (--).
     * @param strURL valid URL. If no background image is wanted, pass an empty string. The resize bar for a top-over-bottom orientation is 8 pixels hight with a variable width; the image will be placed at background position, 'center center', and will not repeat.
     * @return this object
     */
    public jsx3.gui.Splitter setVSplitImage(String strURL)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setVSplitImage", strURL);
        ScriptSessions.addScript(script);
        return this;
    }

    /**
     * Returns the URL for the image to use for the splitter handle when the splitter is rendered side by side ( | ). When not set, Splitter.HSPLITIMAGE will be used when painted on-screen.
     */

    public void getHSplitImage(org.directwebremoting.ui.Callback<String> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getHSplitImage");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, String.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Sets the URL for the image to use for the splitter handle when the splitter is rendered side by side ( | ).
     * @param strURL valid URL. If no background image is wanted, pass an empty string. The resize bar for a side-by-side orientation is 8 pixels wide with a variable height; the image will be placed at background position, 'center center', and will not repeat.
     * @return this object
     */
    public jsx3.gui.Splitter setHSplitImage(String strURL)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setHSplitImage", strURL);
        ScriptSessions.addScript(script);
        return this;
    }

}

