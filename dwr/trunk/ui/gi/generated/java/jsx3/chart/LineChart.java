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
 * A line chart.

Series: LineSeries only.
Axes: Y axis must be value axis, X axis can be value or category axis

The 'type' my be 'overlay', 'stacked', or 'stacked100', which correspond to the basic Excel-like
variations of a line chart.
 * @author Joe Walker [joe at getahead dot org]
 * @author DRAPGEN - Dwr Reverse Ajax Proxy GENerator
 */
public class LineChart extends jsx3.chart.CartesianChart
{
    /**
     * All reverse ajax proxies need context to work from
     * @param context The script that got us to where we are now
     */
    public LineChart(Context context, String extension)
    {
        super(context, extension);
    }

    /**
     * The instance initializer.
     * @param name the GI name of the instance
     * @param left left position (in pixels) of the chart relative to its parent container
     * @param top top position (in pixels) of the chart relative to its parent container
     * @param width width (in pixels) of the chart
     * @param height height (in pixels) of the chart
     */
    public LineChart(String name, int left, int top, int width, int height)
    {
        super((Context) null, (String) null);
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall("new LineChart", name, left, top, width, height);
        setInitScript(script);
    }


    /**
     * 
     */
    public static final String TYPE_OVERLAY = "overlay";

    /**
     * 
     */
    public static final String TYPE_STACKED = "stacked";

    /**
     * 
     */
    public static final String TYPE_STACKED100 = "stacked100";


    /**
     * Returns the type field, one of {'overlay','stacked','stacked100'}.
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
     * Sets the type field.
     * @param type the new value for type, one of {'overlay','stacked','stacked100'}
     */
    public void setType(String type)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setType", type);
        ScriptSessions.addScript(script);
    }

}

