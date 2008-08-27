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
 * Superclass of bar and column chart. Contains all the common functionality, provides template methods
to the subclasses so that they can render horizontal bars or vertical columns.

Basically abstracts a bar/column chart into a chart with a parallel dimension and a normal dimension.
The parallel dimension is the dimension in which the rows/columns extend.
 * @author Joe Walker [joe at getahead dot org]
 * @author DRAPGEN - Dwr Reverse Ajax Proxy GENerator
 */
public class BCChart extends jsx3.chart.CartesianChart
{
    /**
     * All reverse ajax proxies need context to work from
     * @param context The script that got us to where we are now
     */
    public BCChart(Context context, String extension)
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
    public BCChart(String name, int left, int top, int width, int height)
    {
        super((Context) null, (String) null);
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall("new BCChart", name, left, top, width, height);
        setInitScript(script);
    }


    /**
     * 
     */
    public static final String TYPE_CLUSTERED = "clustered";

    /**
     * 
     */
    public static final String TYPE_STACKED = "stacked";

    /**
     * 
     */
    public static final String TYPE_STACKED100 = "stacked100";


    /**
     * Returns the type field, corresponds to the variation of chart, one of {'clustered','stacked','stacked100'}.
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
     * @param type the new value for type, one of {'clustered','stacked','stacked100'}
     */
    public void setType(String type)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setType", type);
        ScriptSessions.addScript(script);
    }

    /**
     * Returns the seriesOverlap field, the ratio of a column width/row height that a column/row overlaps with the adjacent column/row.
     * @param callback seriesOverlap
     */

    public void getSeriesOverlap(org.directwebremoting.ui.Callback<Float> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getSeriesOverlap");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, Float.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Sets the seriesOverlap field.
     * @param seriesOverlap the new value for seriesOverlap, usually between -0.5 and 0.5
     */
    public void setSeriesOverlap(float seriesOverlap)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setSeriesOverlap", seriesOverlap);
        ScriptSessions.addScript(script);
    }

    /**
     * Returns the categoryCoverage field, the ratio of the range of a category that is covered by bars/columns.
     * @param callback categoryCoverage
     */

    public void getCategoryCoverage(org.directwebremoting.ui.Callback<Float> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getCategoryCoverage");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, Float.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Sets the categoryCoverage field.
     * @param categoryCoverage the new value for categoryCoverage, between 0 and 1
     */
    public void setCategoryCoverage(float categoryCoverage)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setCategoryCoverage", categoryCoverage);
        ScriptSessions.addScript(script);
    }

}

