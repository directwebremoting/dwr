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
import org.directwebremoting.ScriptSessions;
import org.directwebremoting.io.Context;

/**
 * Type of axis that displays a linear range of values.
 * @author Joe Walker [joe at getahead dot org]
 * @author DRAPGEN - Dwr Reverse Ajax Proxy GENerator
 */
public class LinearAxis extends jsx3.chart.Axis
{
    /**
     * All reverse ajax proxies need context to work from
     * @param context The script that got us to where we are now
     */
    public LinearAxis(Context context, String extension)
    {
        super(context, extension);
    }

    /**
     * The instance initializer.
     * @param name the GI name of the instance
     * @param horizontal whether this axis is horizontal (x), otherwise it's vertical (y)
     * @param primary whether this axis is primary, otherwise it's secondary
     */
    public LinearAxis(String name, boolean horizontal, boolean primary)
    {
        super((Context) null, (String) null);
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall("new LinearAxis", name, horizontal, primary);
        setInitScript(script);
    }


    /**
     * The minimum number of intervals to show when calculating by auto adjust.
     */
    public static final int MIN_INTERVALS = 5;

    /**
     * The maximum number of intervals to show when calculating by auto adjust.
     */
    public static final int MAX_INTERVALS = 11;


    /**
     * Returns the autoAdjust field, whether to adjust the max/min/interval to the range of the data.
     * @param callback autoAdjust
     */

    public void getAutoAdjust(org.directwebremoting.ui.Callback<Boolean> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getAutoAdjust");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, Boolean.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Sets the autoAdjust field.
     * @param autoAdjust the new value for autoAdjust
     */
    public void setAutoAdjust(boolean autoAdjust)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setAutoAdjust", autoAdjust);
        ScriptSessions.addScript(script);
    }

    /**
     * Returns the baseAtZero field, whether to set either the min or max value of this axis to 0 if applicable and if autoAdjust is true.
     * @param callback baseAtZero
     */

    public void getBaseAtZero(org.directwebremoting.ui.Callback<Boolean> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getBaseAtZero");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, Boolean.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Sets the baseAtZero field.
     * @param baseAtZero the new value for baseAtZero
     */
    public void setBaseAtZero(boolean baseAtZero)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setBaseAtZero", baseAtZero);
        ScriptSessions.addScript(script);
    }

    /**
     * Returns the min field, the minimum value displayed by this axis, overrides autoAdjust.
     * @param callback min
     */

    public void getMin(org.directwebremoting.ui.Callback<Integer> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getMin");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, Integer.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Sets the min field.
     * @param min the new value for min
     */
    public void setMin(int min)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setMin", min);
        ScriptSessions.addScript(script);
    }

    /**
     * Returns the max field, the maximum value displayed by this axis, overrides autoAdjust.
     * @param callback max
     */

    public void getMax(org.directwebremoting.ui.Callback<Integer> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getMax");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, Integer.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Sets the max field.
     * @param max the new value for max
     */
    public void setMax(int max)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setMax", max);
        ScriptSessions.addScript(script);
    }

    /**
     * Returns the interval field, the interval between major ticks, overrides autoAdjust.
     * @param callback interval
     */

    public void getInterval(org.directwebremoting.ui.Callback<Integer> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getInterval");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, Integer.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Sets the interval field.
     * @param interval the new value for interval
     */
    public void setInterval(int interval)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setInterval", interval);
        ScriptSessions.addScript(script);
    }

    /**
     * convert a number value to a coordinate between 0 and this.length, if the value is outside of the range of the axis, return the closest value in the range
     * @param value a value displayed along the axis
     * @param callback coordinate along the axis
     */

    public void getCoordinateFor(Integer value, org.directwebremoting.ui.Callback<Integer> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getCoordinateFor", value);

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, Integer.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * same as getCoordinateFor(), but does not clip to bounds of the axis
     * @param value
     */
    public void getCoordinateForNoClip(java.lang.Object value)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "getCoordinateForNoClip", value);
        ScriptSessions.addScript(script);
    }

}

