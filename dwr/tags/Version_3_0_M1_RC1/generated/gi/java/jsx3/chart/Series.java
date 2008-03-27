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
 * The base class for all data series classes. In general, a chart is made up of a fixed number of 
configured series and a variable number of categories. A series is essentially an addressing scheme
that defines how to get information out of each category.
 * @author Joe Walker [joe at getahead dot org]
 * @author DRAPGEN - Dwr Reverse Ajax Proxy GENerator
 */
public class Series extends jsx3.chart.ChartComponent
{
    /**
     * All reverse ajax proxies need context to work from
     * @param scriptProxy The place we are writing scripts to
     * @param context The script that got us to where we are now
     */
    public Series(Context context, String extension, ScriptProxy scriptProxy)
    {
        super(context, extension, scriptProxy);
    }

    /**
     * The instance initializer.
     * @param name the GI name of the instance
     * @param seriesName the name of the Series, will be displayed in the Legend for most chart types
     */
    public Series(String name, String seriesName)
    {
        super((Context) null, (String) null, (ScriptProxy) null);
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall("new Series", name, seriesName);
        setInitScript(script);
    }



    /**
     * Returns the seriesName field.
     * @param callback seriesName
     */
    @SuppressWarnings("unchecked")
    public void getSeriesName(org.directwebremoting.proxy.Callback<String> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getSeriesName");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelper.saveCallback(callback, String.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        getScriptProxy().addScript(script);
    }

    /**
     * Sets the seriesName field, this name is usually displayed in a legend or as a label.
     * @param seriesName the new value for seriesName
     */
    public void setSeriesName(String seriesName)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setSeriesName", seriesName);
        getScriptProxy().addScript(script);
    }

    /**
     * Sets the function used to render tooltips for each area drawn by this series.
     * @param tooltipFunction evals to a function with the signature function(series,record) : string
     */
    public void setTooltipFunction(String tooltipFunction)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setTooltipFunction", tooltipFunction);
        getScriptProxy().addScript(script);
    }

    /**
     * Returns the function used to render tooltips for each area drawn by this series.
     * @param callback function(series,record) : string
     */
    @SuppressWarnings("unchecked")
    public void getTooltipFunction(org.directwebremoting.proxy.Callback<org.directwebremoting.proxy.CodeBlock> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getTooltipFunction");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelper.saveCallback(callback, org.directwebremoting.proxy.CodeBlock.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        getScriptProxy().addScript(script);
    }

    /**
     * Returns the stroke field.
     * @param callback stroke
     */
    @SuppressWarnings("unchecked")
    public void getStroke(org.directwebremoting.proxy.Callback<String> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getStroke");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelper.saveCallback(callback, String.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        getScriptProxy().addScript(script);
    }

    /**
     * Sets the stroke field, string representation of a VectorStroke.
     * @param stroke the new value for stroke
     */
    public void setStroke(String stroke)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setStroke", stroke);
        getScriptProxy().addScript(script);
    }

    /**
     * Returns the fill field.
     * @param callback fill
     */
    @SuppressWarnings("unchecked")
    public void getFill(org.directwebremoting.proxy.Callback<String> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getFill");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelper.saveCallback(callback, String.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        getScriptProxy().addScript(script);
    }

    /**
     * Sets the fill field, string representation of a vector fill.
     * @param fill the new value for fill
     */
    public void setFill(String fill)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setFill", fill);
        getScriptProxy().addScript(script);
    }

    /**
     * Returns the fillGradient field.
     * @param callback fillGradient
     */
    @SuppressWarnings("unchecked")
    public void getFillGradient(org.directwebremoting.proxy.Callback<String> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getFillGradient");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelper.saveCallback(callback, String.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        getScriptProxy().addScript(script);
    }

    /**
     * Sets the fillGradient field, string representation of a vector fill gradient.
     * @param fillGradient the new value for fillGradient
     */
    public void setFillGradient(String fillGradient)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setFillGradient", fillGradient);
        getScriptProxy().addScript(script);
    }

    /**
     * Returns the x axis that this series is plotted against.
     * @return the x axis
     */
    @SuppressWarnings("unchecked")
    public jsx3.chart.Axis getXAxis()
    {
        String extension = "getXAxis().";
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
     * Returns the x axis that this series is plotted against.
     * @param returnType The expected return type
     * @return the x axis
     */
    @SuppressWarnings("unchecked")
    public <T> T getXAxis(Class<T> returnType)
    {
        String extension = "getXAxis().";
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
     * Returns the y axis that this series is plotted against.
     * @return the y axis
     */
    @SuppressWarnings("unchecked")
    public jsx3.chart.Axis getYAxis()
    {
        String extension = "getYAxis().";
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
     * Returns the y axis that this series is plotted against.
     * @param returnType The expected return type
     * @return the y axis
     */
    @SuppressWarnings("unchecked")
    public <T> T getYAxis(Class<T> returnType)
    {
        String extension = "getYAxis().";
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
     * Returns the colorFunction field.
     * @param callback colorFunction
     */
    @SuppressWarnings("unchecked")
    public void getColorFunction(org.directwebremoting.proxy.Callback<org.directwebremoting.proxy.CodeBlock> callback)
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
            String key = org.directwebremoting.extend.CallbackHelper.saveCallback(callback, org.directwebremoting.proxy.CodeBlock.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        getScriptProxy().addScript(script);
    }

    /**
     * Sets the colorFunction field.
     * @param colorFunction the new value for colorFunction
     */
    public void setColorFunction(String colorFunction)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setColorFunction", colorFunction);
        getScriptProxy().addScript(script);
    }

    /**
     * Returns the optional jsx3.chart.ChartLabel child of this series.
     */
    @SuppressWarnings("unchecked")
    public jsx3.chart.ChartLabel getLabel()
    {
        String extension = "getLabel().";
        try
        {
            java.lang.reflect.Constructor<jsx3.chart.ChartLabel> ctor = jsx3.chart.ChartLabel.class.getConstructor(Context.class, String.class, ScriptProxy.class);
            return ctor.newInstance(this, extension, getScriptProxy());
        }
        catch (Exception ex)
        {
            throw new IllegalArgumentException("Unsupported type: " + jsx3.chart.ChartLabel.class.getName());
        }
    }


}

