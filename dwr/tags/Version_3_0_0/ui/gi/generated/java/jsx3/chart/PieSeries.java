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
 * A data series for a pie chart. Draws a complete pie or ring of a doughnut. A pie series is slightly
different from all the other series because it gets colored in by category instead of all one color.
A pie series has the following fields:

xField the attribute of a record to use as the relative size of each slice of the pie, required
totalAngle the total angle for the series, if not set use the value from the chart
startAngle the angle of the start of the first slice, 0 is top and increasing values go clockwise,
    if not set use the value from the chart
colors an array of string representations of vector fills to color in the slices, if not set use
    the value from the chart
colorFunction a function that determines the color of each slice, with the signature
    function(record, index) : jsx3.vector.Fill, if not set use the value from the chart
stroke string representation of a VectorStroke to outline the slices with, if not set use the value
    from the chart
labelPlacement where to place a label with the name of the series, relative to the series, one of
    {'top','right','bottom','left'}
labelOffset the padding (may be negative) between the outer edge of the series and the close edge of
    the label
 * @author Joe Walker [joe at getahead dot org]
 * @author DRAPGEN - Dwr Reverse Ajax Proxy GENerator
 */
public class PieSeries extends jsx3.chart.Series
{
    /**
     * All reverse ajax proxies need context to work from
     * @param context The script that got us to where we are now
     */
    public PieSeries(Context context, String extension)
    {
        super(context, extension);
    }

    /**
     * The instance initializer.
     * @param name the GI name of the instance
     * @param seriesName the name of the Series, will be displayed in the Legend for most chart types
     */
    public PieSeries(String name, String seriesName)
    {
        super((Context) null, (String) null);
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall("new PieSeries", name, seriesName);
        setInitScript(script);
    }



    /**
     * Returns the totalAngle field, overrides per-chart setting in PieChart.
     * @param callback totalAngle
     */

    public void getTotalAngle(org.directwebremoting.ui.Callback<Integer> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getTotalAngle");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, Integer.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Sets the totalAngle field.
     * @param totalAngle the new value for totalAngle
     */
    public void setTotalAngle(int totalAngle)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setTotalAngle", totalAngle);
        ScriptSessions.addScript(script);
    }

    /**
     * Returns the startAngle field, overrides per-chart setting in PieChart.
     * @param callback startAngle
     */

    public void getStartAngle(org.directwebremoting.ui.Callback<Integer> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getStartAngle");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, Integer.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Sets the startAngle field.
     * @param startAngle the new value for startAngle
     */
    public void setStartAngle(int startAngle)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setStartAngle", startAngle);
        ScriptSessions.addScript(script);
    }

    /**
     * Returns the field field, the attribute of the data provider to use as values for this series.
     * @param callback field
     */

    public void getField(org.directwebremoting.ui.Callback<String> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getField");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, String.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Sets the field field.
     * @param field the new value for field
     */
    public void setField(String field)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setField", field);
        ScriptSessions.addScript(script);
    }

    /**
     * Returns the value of a data point in this series for the given record.
     * @param record the <record/> node
     */

    public void getValue(jsx3.xml.Node record, org.directwebremoting.ui.Callback<Integer> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getValue", record);

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, Integer.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Returns the colors field, overrides per-chart setting in PieChart.
     * @param callback colors
     */

    public void getColors(org.directwebremoting.ui.Callback<Object[]> callback)
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
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, Object[].class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Sets the colors field.
     * @param colors the new value for colors
     */
    public void setColors(Object[] colors)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setColors", colors);
        ScriptSessions.addScript(script);
    }

    /**
     * Returns the labelPlacement field, where to place the optional ChartLabel child.
     * @param callback labelPlacement
     */

    public void getLabelPlacement(org.directwebremoting.ui.Callback<String> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getLabelPlacement");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, String.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Sets the labelPlacement field.
     * @param labelPlacement the new value for labelPlacement
     */
    public void setLabelPlacement(String labelPlacement)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setLabelPlacement", labelPlacement);
        ScriptSessions.addScript(script);
    }

    /**
     * Returns the labelOffset field, the pixel offset of the optional ChartLabel child.
     * @param callback labelOffset
     */

    public void getLabelOffset(org.directwebremoting.ui.Callback<Integer> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getLabelOffset");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, Integer.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Sets the labelOffset field.
     * @param labelOffset the new value for labelOffset
     */
    public void setLabelOffset(int labelOffset)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setLabelOffset", labelOffset);
        ScriptSessions.addScript(script);
    }

    /**
     * The default tooltip function for this type of series.
     * @param series
     * @param record
     * @param percent
     */

    public void tooltip(jsx3.chart.Series series, jsx3.xml.Node record, java.lang.Object percent, org.directwebremoting.ui.Callback<String> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "tooltip", series, record, percent);

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, String.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

}

