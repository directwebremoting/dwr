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
 * A chart component that renders a grid of lines and fills aligned with an x and y axis.
 * @author Joe Walker [joe at getahead dot org]
 * @author DRAPGEN - Dwr Reverse Ajax Proxy GENerator
 */
public class GridLines extends jsx3.chart.ChartComponent
{
    /**
     * All reverse ajax proxies need context to work from
     * @param context The script that got us to where we are now
     */
    public GridLines(Context context, String extension)
    {
        super(context, extension);
    }

    /**
     * The instance initializer.
     * @param name the GI name of the instance
     * @param left left position (in pixels) of the object relative to its parent container
     * @param top top position (in pixels) of the object relative to its parent container
     * @param width width (in pixels) of the chart
     * @param height height (in pixels) of the chart
     */
    public GridLines(String name, int left, int top, int width, int height)
    {
        super((Context) null, (String) null);
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall("new GridLines", name, left, top, width, height);
        setInitScript(script);
    }



    /**
     * Returns the horizontalAbove field, whether to draw the horizontal lines and fills above the vertical ones.
     * @param callback horizontalAbove
     */

    public void getHorizontalAbove(org.directwebremoting.ui.Callback<Boolean> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getHorizontalAbove");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, Boolean.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Sets the horizontalAbove field.
     * @param horizontalAbove the new value for horizontalAbove
     */
    public void setHorizontalAbove(boolean horizontalAbove)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setHorizontalAbove", horizontalAbove);
        ScriptSessions.addScript(script);
    }

    /**
     * Returns the inForeground field, whether to draw this legend on top of the data series (or below).
     * @param callback inForeground
     */

    public void getInForeground(org.directwebremoting.ui.Callback<Boolean> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getInForeground");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, Boolean.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Sets the inForeground field.
     * @param inForeground the new value for inForeground
     */
    public void setInForeground(boolean inForeground)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setInForeground", inForeground);
        ScriptSessions.addScript(script);
    }

    /**
     * Returns the borderStroke field, string representation of the stroke used to outline the grid lines.
     * @param callback borderStroke
     */

    public void getBorderStroke(org.directwebremoting.ui.Callback<String> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getBorderStroke");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, String.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Sets the borderStroke field.
     * @param borderStroke the new value for borderStroke
     */
    public void setBorderStroke(String borderStroke)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setBorderStroke", borderStroke);
        ScriptSessions.addScript(script);
    }

    /**
     * Returns the fillV field, array of string representations of vector fills used to fill in areas between vertical major ticks; if the length of the array is greater than one, the areas alternate through the list of fills.
     * @param callback fillV
     */

    public void getFillV(org.directwebremoting.ui.Callback<Object[]> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getFillV");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, Object[].class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Sets the fillV field.
     * @param fillV the new value for fillV
     */
    public void setFillV(Object[] fillV)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setFillV", fillV);
        ScriptSessions.addScript(script);
    }

    /**
     * Returns the strokeMajorV field, array of string representations of VectorStroke's used to draw the vertical major ticks; if the length of the array is greater than one, the ticks alternate through the list of strokes.
     * @param callback strokeMajorV
     */

    public void getStrokeMajorV(org.directwebremoting.ui.Callback<Object[]> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getStrokeMajorV");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, Object[].class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Sets the strokeMajorV field.
     * @param strokeMajorV the new value for strokeMajorV
     */
    public void setStrokeMajorV(Object[] strokeMajorV)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setStrokeMajorV", strokeMajorV);
        ScriptSessions.addScript(script);
    }

    /**
     * Returns the strokeMinorV field, array of string representations of VectorStroke's used to draw the vertical minor ticks; if the length of the array is greater than one, the ticks alternate through the list of strokes.
     * @param callback strokeMinorV
     */

    public void getStrokeMinorV(org.directwebremoting.ui.Callback<Object[]> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getStrokeMinorV");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, Object[].class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Sets the strokeMinorV field.
     * @param strokeMinorV the new value for strokeMinorV
     */
    public void setStrokeMinorV(Object[] strokeMinorV)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setStrokeMinorV", strokeMinorV);
        ScriptSessions.addScript(script);
    }

    /**
     * Returns the fillH field, array of string representations of vector fills used to fill in areas between horizontal major ticks; if the length of the array is greater than one, the areas alternate through the list of fills.
     * @param callback fillH
     */

    public void getFillH(org.directwebremoting.ui.Callback<Object[]> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getFillH");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, Object[].class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Sets the fillH field.
     * @param fillH the new value for fillH
     */
    public void setFillH(Object[] fillH)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setFillH", fillH);
        ScriptSessions.addScript(script);
    }

    /**
     * Returns the strokeMajorH field, array of string representations of VectorStroke's used to draw the horizontal major ticks; if the length of the array is greater than one, the ticks alternate through the list of strokes.
     * @param callback strokeMajorH
     */

    public void getStrokeMajorH(org.directwebremoting.ui.Callback<Object[]> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getStrokeMajorH");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, Object[].class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Sets the strokeMajorH field.
     * @param strokeMajorH the new value for strokeMajorH
     */
    public void setStrokeMajorH(Object[] strokeMajorH)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setStrokeMajorH", strokeMajorH);
        ScriptSessions.addScript(script);
    }

    /**
     * Returns the strokeMinorH field, array of string representations of VectorStroke's used to draw the horizontal minor ticks; if the length of the array is greater than one, the ticks alternate through the list of strokes.
     * @param callback strokeMinorH
     */

    public void getStrokeMinorH(org.directwebremoting.ui.Callback<Object[]> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getStrokeMinorH");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, Object[].class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Sets the strokeMinorH field.
     * @param strokeMinorH the new value for strokeMinorH
     */
    public void setStrokeMinorH(Object[] strokeMinorH)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setStrokeMinorH", strokeMinorH);
        ScriptSessions.addScript(script);
    }

    /**
     * Returns the x (horizontal) axis used to determine where to draw tick lines.
     */

    public jsx3.chart.Axis getXAxis()
    {
        String extension = "getXAxis().";
        try
        {
            java.lang.reflect.Constructor<jsx3.chart.Axis> ctor = jsx3.chart.Axis.class.getConstructor(Context.class, String.class);
            return ctor.newInstance(this, extension);
        }
        catch (Exception ex)
        {
            throw new IllegalArgumentException("Unsupported type: " + jsx3.chart.Axis.class.getName());
        }
    }

    /**
     * Returns the x (horizontal) axis used to determine where to draw tick lines.
     * @param returnType The expected return type
     */

    public <T> T getXAxis(Class<T> returnType)
    {
        String extension = "getXAxis().";
        try
        {
            java.lang.reflect.Constructor<T> ctor = returnType.getConstructor(Context.class, String.class);
            return ctor.newInstance(this, extension);
        }
        catch (Exception ex)
        {
            throw new IllegalArgumentException("Unsupported return type: " + returnType.getName());
        }
    }

    /**
     * Returns the y (vertical) axis used to determine where to draw tick lines.
     */

    public jsx3.chart.Axis getYAxis()
    {
        String extension = "getYAxis().";
        try
        {
            java.lang.reflect.Constructor<jsx3.chart.Axis> ctor = jsx3.chart.Axis.class.getConstructor(Context.class, String.class);
            return ctor.newInstance(this, extension);
        }
        catch (Exception ex)
        {
            throw new IllegalArgumentException("Unsupported type: " + jsx3.chart.Axis.class.getName());
        }
    }

    /**
     * Returns the y (vertical) axis used to determine where to draw tick lines.
     * @param returnType The expected return type
     */

    public <T> T getYAxis(Class<T> returnType)
    {
        String extension = "getYAxis().";
        try
        {
            java.lang.reflect.Constructor<T> ctor = returnType.getConstructor(Context.class, String.class);
            return ctor.newInstance(this, extension);
        }
        catch (Exception ex)
        {
            throw new IllegalArgumentException("Unsupported return type: " + returnType.getName());
        }
    }

}

