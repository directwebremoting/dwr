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
 * A pie chart.

Series: PieSeries only.
Axes: No axes, it's a radial chart.
 * @author Joe Walker [joe at getahead dot org]
 * @author DRAPGEN - Dwr Reverse Ajax Proxy GENerator
 */
public class PieChart extends jsx3.chart.RadialChart
{
    /**
     * All reverse ajax proxies need context to work from
     * @param context The script that got us to where we are now
     */
    public PieChart(Context context, String extension)
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
    public PieChart(String name, int left, int top, int width, int height)
    {
        super((Context) null, (String) null);
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall("new PieChart", name, left, top, width, height);
        setInitScript(script);
    }



    /**
     * Returns the innerRadius field, the radius as the hole in the middle of the pie as a ratio of the entire radius.
     * @param callback innerRadius
     */

    public void getInnerRadius(org.directwebremoting.ui.Callback<Float> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getInnerRadius");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, Float.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Sets the innerRadius field.
     * @param innerRadius the new value for innerRadius, between 0.0 and 1.0
     */
    public void setInnerRadius(float innerRadius)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setInnerRadius", innerRadius);
        ScriptSessions.addScript(script);
    }

    /**
     * Returns the seriesPadding field, the amount of padding between rings in a doughnut chart as a ratio of the width of a ring.
     * @param callback seriesPadding
     */

    public void getSeriesPadding(org.directwebremoting.ui.Callback<Float> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getSeriesPadding");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, Float.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Sets the seriesPadding field.
     * @param seriesPadding the new value for seriesPadding, positive value, not too big
     */
    public void setSeriesPadding(float seriesPadding)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setSeriesPadding", seriesPadding);
        ScriptSessions.addScript(script);
    }

    /**
     * Returns the totalAngle field, the total angle used for each series; may be overridden on a series-by-series basis by jsx3.chart.PieSeries.totalAngle.
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
     * @param totalAngle the new value for totalAngle, between 0 and 360
     */
    public void setTotalAngle(int totalAngle)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setTotalAngle", totalAngle);
        ScriptSessions.addScript(script);
    }

    /**
     * Returns the startAngle field, the start angle of the first slice in each series; 0 points upwards and increasing values go clockwise; may be overridden on a series-by-series basis by jsx3.chart.PieSeries.startAngle.
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
     * @param startAngle the new value for startAngle, between 0 and 360
     */
    public void setStartAngle(int startAngle)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setStartAngle", startAngle);
        ScriptSessions.addScript(script);
    }

    /**
     * Returns the categoryField field, the attribute of a record that contains the category name value; necessary because there is no CategoryAxis to define this in a radial chart.
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
     * Returns the colors field, an array of string representations of a vector fill, to color in the slices of all the data series; may be overridden by jsx3.chart.PieSeries.colors for an individual series..
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
     * Returns the colorFunction field, a function that defines how to color in the slices of each data series in this chart, with the signature function(record, index) : jsx3.vector.Fill; may be overridden by jsx3.chart.PieSeries.colorFunction for an individual series..
     * @param callback colorFunction
     */

    public void getColorFunction(org.directwebremoting.ui.Callback<org.directwebremoting.ui.CodeBlock> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getColorFunction");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, org.directwebremoting.ui.CodeBlock.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Sets the colorFunction field.
     * @param colorFunction the new value for colorFunction, should eval to a function with the signature function(record, index) : jsx3.vector.Fill
     */
    public void setColorFunction(String colorFunction)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setColorFunction", colorFunction);
        ScriptSessions.addScript(script);
    }

    /**
     * Returns the seriesStroke field, string representation of a VectorStroke to outline the slices of all the series with; may be overridden by jsx3.chart.PieSeries.stroke for an individual series..
     * @param callback seriesStroke
     */

    public void getSeriesStroke(org.directwebremoting.ui.Callback<String> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getSeriesStroke");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, String.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Sets the seriesStroke field.
     * @param seriesStroke the new value for seriesStroke
     */
    public void setSeriesStroke(String seriesStroke)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setSeriesStroke", seriesStroke);
        ScriptSessions.addScript(script);
    }

    /**
     * default coloring scheme for pie series, simply converts the default coloring scheme for series into a coloring scheme for categories
     * @param record
     * @param index the index of the record in the data provider
     */

    public jsx3.vector.Fill defaultColoring(jsx3.xml.Node record, int index)
    {
        String extension = "defaultColoring(\"" + record + "\", \"" + index + "\").";
        try
        {
            java.lang.reflect.Constructor<jsx3.vector.Fill> ctor = jsx3.vector.Fill.class.getConstructor(Context.class, String.class);
            return ctor.newInstance(this, extension);
        }
        catch (Exception ex)
        {
            throw new IllegalArgumentException("Unsupported type: " + jsx3.vector.Fill.class.getName());
        }
    }


}

