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
 * A data series used for a jsx3.chart.PointChart. A point series has the following fields:
xField - the attribute of a record to use as the x-coordinate of points in the series, required
yField - the attribute of a record to use as the y-coordinate of points in the series, required
magnitude - the magnitude of all the points in the series
pointRenderer - string that evals to an object that implements the renderer interface, optional
 * @author Joe Walker [joe at getahead dot org]
 * @author DRAPGEN - Dwr Reverse Ajax Proxy GENerator
 */
public class PointSeries extends jsx3.chart.PlotSeries
{
    /**
     * All reverse ajax proxies need context to work from
     * @param context The script that got us to where we are now
     */
    public PointSeries(Context context, String extension)
    {
        super(context, extension);
    }

    /**
     * The instance initializer.
     * @param name the GI name of the instance
     * @param seriesName the name of the Series, will be displayed in the Legend for most chart types
     */
    public PointSeries(String name, String seriesName)
    {
        super((Context) null, (String) null);
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall("new PointSeries", name, seriesName);
        setInitScript(script);
    }


    /**
     * 
     */
    public static final int DEFAULT_MAGNITUDE = 4;


    /**
     * Returns the magnitude field, the magnitude value to use for each data point in this series.
     * @param callback magnitude
     */

    public void getMagnitude(org.directwebremoting.ui.Callback<Integer> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getMagnitude");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, Integer.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Sets the magnitude field.
     * @param magnitude the new value for magnitude
     */
    public void setMagnitude(int magnitude)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setMagnitude", magnitude);
        ScriptSessions.addScript(script);
    }

    /**
     * The default tooltip function for this type of series.
     * @param series
     * @param record
     */

    public void tooltip(jsx3.chart.Series series, jsx3.xml.Node record, org.directwebremoting.ui.Callback<String> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "tooltip", series, record);

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, String.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

}

