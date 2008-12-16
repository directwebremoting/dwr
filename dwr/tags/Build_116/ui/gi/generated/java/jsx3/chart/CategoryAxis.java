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
 * Axis type that displays a set of discrete values (categories). Usually a category corresponds to a
record in the chart's data provider.
 * @author Joe Walker [joe at getahead dot org]
 * @author DRAPGEN - Dwr Reverse Ajax Proxy GENerator
 */
public class CategoryAxis extends jsx3.chart.Axis
{
    /**
     * All reverse ajax proxies need context to work from
     * @param context The script that got us to where we are now
     */
    public CategoryAxis(Context context, String extension)
    {
        super(context, extension);
    }

    /**
     * The instance initializer.
     * @param name the GI name of the instance
     * @param horizontal whether this axis is horizontal (x), otherwise it's vertical (y)
     * @param primary whether this axis is primary, otherwise it's secondary
     */
    public CategoryAxis(String name, boolean horizontal, boolean primary)
    {
        super((Context) null, (String) null);
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall("new CategoryAxis", name, horizontal, primary);
        setInitScript(script);
    }


    /**
     * 
     */
    public static final String TICKS_ALIGNED = "aligned";

    /**
     * 
     */
    public static final String TICKS_BETWEEN = "between";


    /**
     * Returns the tickAlignment field, if 'between' then the midpoint of the category is between two major ticks, otherwise if 'aligned' then the midpoint of the category is aligned with a major tick.
     * @param callback tickAlignment, one of {'aligned','between'}
     */

    public void getTickAlignment(org.directwebremoting.ui.Callback<String> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getTickAlignment");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, String.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Sets the tickAlignment field.
     * @param tickAlignment the new value for tickAlignment, one of {'aligned','between'}
     */
    public void setTickAlignment(String tickAlignment)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setTickAlignment", tickAlignment);
        ScriptSessions.addScript(script);
    }

    /**
     * Returns the categoryField field, the attribute of records from the data provider that contains the category name (this value can still be transformed by Axis's 'labelFunction' field).
     * @param callback categoryField
     */

    public void getCategoryField(org.directwebremoting.ui.Callback<String> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getCategoryField");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, String.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Sets the categoryField field.
     * @param categoryField the new value for categoryField
     */
    public void setCategoryField(String categoryField)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setCategoryField", categoryField);
        ScriptSessions.addScript(script);
    }

    /**
     * Returns the paddingLow field, the number of category widths to pad the beginning of the axis with.
     * @param callback paddingLow
     */

    public void getPaddingLow(org.directwebremoting.ui.Callback<Float> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getPaddingLow");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, Float.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Sets the paddingLow field.
     * @param paddingLow the new value for paddingLow
     */
    public void setPaddingLow(float paddingLow)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setPaddingLow", paddingLow);
        ScriptSessions.addScript(script);
    }

    /**
     * Returns the paddingHigh field, the number of category widths to pad the end of the axis with.
     * @param callback paddingHigh
     */

    public void getPaddingHigh(org.directwebremoting.ui.Callback<Float> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getPaddingHigh");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, Float.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Sets the paddingHigh field.
     * @param paddingHigh the new value for paddingHigh
     */
    public void setPaddingHigh(float paddingHigh)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setPaddingHigh", paddingHigh);
        ScriptSessions.addScript(script);
    }

}

