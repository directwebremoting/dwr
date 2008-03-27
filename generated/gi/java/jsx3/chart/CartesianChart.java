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
import org.directwebremoting.proxy.ScriptProxy;
import org.directwebremoting.proxy.io.Context;

/**
 * Base chart class for charts that render on a cartesian plane with x and y axes. Currently only supports
primary x and y axes, even though there are methods pertaining to secondary axes.

Cartesian charts can have the following additional children:

  {0,n} GridLines, will render lines aligned with the axes below or above the data series
  {2,n} Axis, required to define the coordinate space of the cartesian plane
 * @author Joe Walker [joe at getahead dot org]
 * @author DRAPGEN - Dwr Reverse Ajax Proxy GENerator
 */
public class CartesianChart extends jsx3.chart.Chart
{
    /**
     * All reverse ajax proxies need context to work from
     * @param scriptProxy The place we are writing scripts to
     * @param context The script that got us to where we are now
     */
    public CartesianChart(Context context, String extension, ScriptProxy scriptProxy)
    {
        super(context, extension, scriptProxy);
    }

    /**
     * The instance initializer.
     * @param name the GI name of the instance
     * @param left left position (in pixels) of the chart relative to its parent container
     * @param top top position (in pixels) of the chart relative to its parent container
     * @param width width (in pixels) of the chart
     * @param height height (in pixels) of the chart
     */
    public CartesianChart(String name, int left, int top, int width, int height)
    {
        super((Context) null, (String) null, (ScriptProxy) null);
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall("new CartesianChart", name, left, top, width, height);
        setInitScript(script);
    }



    /**
     * Returns the array of children GridLines instances.
     * @param callback gridLines
     */
    @SuppressWarnings("unchecked")
    public void getGridLines(org.directwebremoting.proxy.Callback<Object[]> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getGridLines");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelper.saveCallback(callback, Object[].class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        getScriptProxy().addScript(script);
    }

    /**
     * Returns the primary x axis, if any.
     * @return primaryXAxis
     */
    @SuppressWarnings("unchecked")
    public jsx3.chart.Axis getPrimaryXAxis()
    {
        String extension = "getPrimaryXAxis().";
        try
        {
            java.lang.reflect.Constructor<jsx3.chart.Axis> ctor = jsx3.chart.Axis.class.getConstructor(Context.class, String.class, ScriptProxy.class);
            return ctor.newInstance(this, extension, getScriptProxy());
        }
        catch (Exception ex)
        {
            throw new IllegalArgumentException("Unsupported type: " + jsx3.chart.Axis.class.getName());
        }
    }

    /**
     * Returns the primary x axis, if any.
     * @param returnType The expected return type
     * @return primaryXAxis
     */
    @SuppressWarnings("unchecked")
    public <T> T getPrimaryXAxis(Class<T> returnType)
    {
        String extension = "getPrimaryXAxis().";
        try
        {
            java.lang.reflect.Constructor<T> ctor = returnType.getConstructor(Context.class, String.class, ScriptProxy.class);
            return ctor.newInstance(this, extension, getScriptProxy());
        }
        catch (Exception ex)
        {
            throw new IllegalArgumentException("Unsupported return type: " + returnType.getName());
        }
    }

    /**
     * Returns the primary y axis, if any.
     * @return primaryYAxis
     */
    @SuppressWarnings("unchecked")
    public jsx3.chart.Axis getPrimaryYAxis()
    {
        String extension = "getPrimaryYAxis().";
        try
        {
            java.lang.reflect.Constructor<jsx3.chart.Axis> ctor = jsx3.chart.Axis.class.getConstructor(Context.class, String.class, ScriptProxy.class);
            return ctor.newInstance(this, extension, getScriptProxy());
        }
        catch (Exception ex)
        {
            throw new IllegalArgumentException("Unsupported type: " + jsx3.chart.Axis.class.getName());
        }
    }

    /**
     * Returns the primary y axis, if any.
     * @param returnType The expected return type
     * @return primaryYAxis
     */
    @SuppressWarnings("unchecked")
    public <T> T getPrimaryYAxis(Class<T> returnType)
    {
        String extension = "getPrimaryYAxis().";
        try
        {
            java.lang.reflect.Constructor<T> ctor = returnType.getConstructor(Context.class, String.class, ScriptProxy.class);
            return ctor.newInstance(this, extension, getScriptProxy());
        }
        catch (Exception ex)
        {
            throw new IllegalArgumentException("Unsupported return type: " + returnType.getName());
        }
    }

    /**
     * Returns the range for axis, delegates to getXRange() or getYRange().
     * @param axis 
     * @param callback [min,max] or null if no range can be found
     */
    @SuppressWarnings("unchecked")
    public void getRangeForAxis(jsx3.chart.Axis axis, org.directwebremoting.proxy.Callback<Object[]> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getRangeForAxis", axis);

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelper.saveCallback(callback, Object[].class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        getScriptProxy().addScript(script);
    }

    /**
     * Returns the range of x values in the data provider, subclasses must implement.
     * @param series the series to consider
     * @param callback [min,max] or null if no range can be found
     */
    @SuppressWarnings("unchecked")
    public void getXRange(Object[] series, org.directwebremoting.proxy.Callback<Object[]> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getXRange", series);

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelper.saveCallback(callback, Object[].class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        getScriptProxy().addScript(script);
    }

    /**
     * Returns the range of y values in the data provider, subclasses must implement.
     * @param series the series to consider
     * @param callback [min,max] or null if no range can be found
     */
    @SuppressWarnings("unchecked")
    public void getYRange(Object[] series, org.directwebremoting.proxy.Callback<Object[]> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getYRange", series);

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelper.saveCallback(callback, Object[].class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        getScriptProxy().addScript(script);
    }

}

