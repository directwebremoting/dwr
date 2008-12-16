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
 * An axis that displays a range of values logarithmically (base^n and base^(n+1) occupy same visual space).
 * @author Joe Walker [joe at getahead dot org]
 * @author DRAPGEN - Dwr Reverse Ajax Proxy GENerator
 */
public class LogarithmicAxis extends jsx3.chart.Axis
{
    /**
     * All reverse ajax proxies need context to work from
     * @param context The script that got us to where we are now
     */
    public LogarithmicAxis(Context context, String extension)
    {
        super(context, extension);
    }

    /**
     * The instance initializer.
     * @param name the GI name of the instance
     * @param horizontal whether this axis is horizontal (x), otherwise it's vertical (y)
     * @param primary whether this axis is primary, otherwise it's secondary
     */
    public LogarithmicAxis(String name, boolean horizontal, boolean primary)
    {
        super((Context) null, (String) null);
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall("new LogarithmicAxis", name, horizontal, primary);
        setInitScript(script);
    }



    /**
     * Returns the autoAdjust field.
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
     * Returns the baseAtZero field, whether or not to set the minimum value to base^0, otherwise will choose an appropriate value for the data range.
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
     * Returns the showNegativeValues field.
     * @param callback showNegativeValues
     */

    public void getShowNegativeValues(org.directwebremoting.ui.Callback<Boolean> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getShowNegativeValues");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, Boolean.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Sets the showNegativeValues field.
     * @param showNegativeValues the new value for showNegativeValues
     */
    public void setShowNegativeValues(boolean showNegativeValues)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setShowNegativeValues", showNegativeValues);
        ScriptSessions.addScript(script);
    }

    /**
     * Returns the minExponent field, the range of values displayed will begin at base^minExpronent.
     * @param callback minExponent
     */

    public void getMinExponent(org.directwebremoting.ui.Callback<Integer> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getMinExponent");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, Integer.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Sets the minExponent field.
     * @param minExponent the new value for minExponent
     */
    public void setMinExponent(int minExponent)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setMinExponent", minExponent);
        ScriptSessions.addScript(script);
    }

    /**
     * Returns the maxExponent field, the range of values displayed will end at base^maxExponent.
     * @param callback maxExponent
     */

    public void getMaxExponent(org.directwebremoting.ui.Callback<Integer> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getMaxExponent");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, Integer.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Sets the maxExponent field.
     * @param maxExponent the new value for maxExponent
     */
    public void setMaxExponent(int maxExponent)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setMaxExponent", maxExponent);
        ScriptSessions.addScript(script);
    }

    /**
     * Returns the base field.
     * @param callback base
     */

    public void getBase(org.directwebremoting.ui.Callback<Integer> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getBase");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, Integer.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Sets the base field.
     * @param base the new value for base
     */
    public void setBase(int base)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setBase", base);
        ScriptSessions.addScript(script);
    }

    /**
     * Returns the majorDivisions field, the number of major tick line divisions to place between the values base^n and base^(n+1). A good value can be base-1, though the default value is 1..
     * @param callback majorDivisions
     */

    public void getMajorDivisions(org.directwebremoting.ui.Callback<Integer> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getMajorDivisions");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, Integer.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Sets the majorDivisions field.
     * @param majorDivisions the new value for majorDivisions
     */
    public void setMajorDivisions(int majorDivisions)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setMajorDivisions", majorDivisions);
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

