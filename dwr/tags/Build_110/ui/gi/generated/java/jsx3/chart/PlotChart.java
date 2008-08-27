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
 * An plot (scatter/point/bubble) chart.

Series: PointSeries, BubbleSeries
Axes: both X and Y axis must be value axes
 * @author Joe Walker [joe at getahead dot org]
 * @author DRAPGEN - Dwr Reverse Ajax Proxy GENerator
 */
public class PlotChart extends jsx3.chart.CartesianChart
{
    /**
     * All reverse ajax proxies need context to work from
     * @param context The script that got us to where we are now
     */
    public PlotChart(Context context, String extension)
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
    public PlotChart(String name, int left, int top, int width, int height)
    {
        super((Context) null, (String) null);
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall("new PlotChart", name, left, top, width, height);
        setInitScript(script);
    }


    /**
     * 
     */
    public static final String MAG_RADIUS = "radius";

    /**
     * 
     */
    public static final String MAG_DIAMETER = "diameter";

    /**
     * 
     */
    public static final String MAG_AREA = "area";

    /**
     * 
     */
    public static final int DEFAULT_MAX_POINT_RADIUS = 30;


    /**
     * Returns the maxPointRadius field, limit the radius of points on this chart to this value.
     * @param callback maxPointRadius
     */

    public void getMaxPointRadius(org.directwebremoting.ui.Callback<Integer> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getMaxPointRadius");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, Integer.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Sets the maxPointRadius field.
     * @param maxPointRadius the new value for maxPointRadius
     */
    public void setMaxPointRadius(int maxPointRadius)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setMaxPointRadius", maxPointRadius);
        ScriptSessions.addScript(script);
    }

    /**
     * Returns the magnitudeMethod field; the magnitude method defines how the magnitude value of a record in a bubble series is converted to a radius for the rendered point; a value of "radius" means that the magnitude will equal the radius of the point, "diameter" means that the magnitude will equal the diameter (2 * radius), and "area" means that the area of the point will be proportional to the magnitude.
     * @param callback magnitudeMethod, one of {"radius","diameter","area"}
     */

    public void getMagnitudeMethod(org.directwebremoting.ui.Callback<String> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getMagnitudeMethod");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, String.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Sets the magnitudeMethod field, one of {"radius","diameter","area"}.
     * @param magnitudeMethod the new value for magnitudeMethod
     */
    public void setMagnitudeMethod(String magnitudeMethod)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setMagnitudeMethod", magnitudeMethod);
        ScriptSessions.addScript(script);
    }

}

