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
 * A data series for a line chart. An line series draws a set of points connected by a line.
A line series has the following properties:

xField the attribute of a record to use as the x-coordinate of points in the series, required if
    the x-axis is a value axis
yField the attribute of a record to use as the y-coordinate of points in the series, required
form defines how the area is drawn, one of {'segment','step','reverseStep','horizontal','vertical'},
    defaults to 'segment'
interpolateValues if true the the line will be continuous even over missing data points, if false the
    the line will break over missing data points
pointRadius the radius of the points to render at each data point on the line, optional
pointRenderer string that evals to an object that implements the renderer interface, optional
pointFill string representation of a vector fill for the points
pointStroke string representation of a VectorStroke for the points
pointGradient string representation of a vector fill gradient for the points
 * @author Joe Walker [joe at getahead dot org]
 * @author DRAPGEN - Dwr Reverse Ajax Proxy GENerator
 */
public class LineSeries extends jsx3.chart.Series
{
    /**
     * All reverse ajax proxies need context to work from
     * @param scriptProxy The place we are writing scripts to
     * @param context The script that got us to where we are now
     */
    public LineSeries(Context context, String extension, ScriptProxy scriptProxy)
    {
        super(context, extension, scriptProxy);
    }

    /**
     * The instance initializer.
     * @param name the GI name of the instance
     * @param seriesName the name of the Series, will be displayed in the Legend for most chart types
     */
    public LineSeries(String name, String seriesName)
    {
        super((Context) null, (String) null, (ScriptProxy) null);
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall("new LineSeries", name, seriesName);
        setInitScript(script);
    }


    /**
     * 
     */
    public static final String FORM_SEGMENT = "segment";

    /**
     * 
     */
    public static final String FORM_STEP = "step";

    /**
     * 
     */
    public static final String FORM_REVSTEP = "reverseStep";

    /**
     * 
     */
    public static final String FORM_HORIZONTAL = "horizontal";

    /**
     * 
     */
    public static final String FORM_VERTICAL = "vertical";


    /**
     * Returns the xField field.
     * @param callback xField
     */
    @SuppressWarnings("unchecked")
    public void getXField(org.directwebremoting.proxy.Callback<String> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getXField");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelper.saveCallback(callback, String.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        getScriptProxy().addScript(script);
    }

    /**
     * Sets the xField field.
     * @param xField the new value for xField
     */
    public void setXField(String xField)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setXField", xField);
        getScriptProxy().addScript(script);
    }

    /**
     * Returns the yField field.
     * @param callback yField
     */
    @SuppressWarnings("unchecked")
    public void getYField(org.directwebremoting.proxy.Callback<String> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getYField");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelper.saveCallback(callback, String.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        getScriptProxy().addScript(script);
    }

    /**
     * Sets the yField field.
     * @param yField the new value for yField
     */
    public void setYField(String yField)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setYField", yField);
        getScriptProxy().addScript(script);
    }

    /**
     * Returns the form field.
     * @param callback form
     */
    @SuppressWarnings("unchecked")
    public void getForm(org.directwebremoting.proxy.Callback<String> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getForm");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelper.saveCallback(callback, String.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        getScriptProxy().addScript(script);
    }

    /**
     * Sets the form field, checks for valid value.
     * @param form the new value for form, one of {'segment','step','reverseStep','horizontal','vertical'}
     */
    public void setForm(String form)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setForm", form);
        getScriptProxy().addScript(script);
    }

    /**
     * Returns the interpolateValues field.
     * @param callback interpolateValues
     */
    @SuppressWarnings("unchecked")
    public void getInterpolateValues(org.directwebremoting.proxy.Callback<Boolean> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getInterpolateValues");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelper.saveCallback(callback, Boolean.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        getScriptProxy().addScript(script);
    }

    /**
     * Sets the interpolateValues field.
     * @param interpolateValues the new value for interpolateValues
     */
    public void setInterpolateValues(boolean interpolateValues)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setInterpolateValues", interpolateValues);
        getScriptProxy().addScript(script);
    }

    /**
     * Returns the pointRadius field.
     * @param callback pointRadius
     */
    @SuppressWarnings("unchecked")
    public void getPointRadius(org.directwebremoting.proxy.Callback<Integer> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getPointRadius");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelper.saveCallback(callback, Integer.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        getScriptProxy().addScript(script);
    }

    /**
     * Sets the pointRadius field.
     * @param pointRadius the new value for pointRadius
     */
    public void setPointRadius(int pointRadius)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setPointRadius", pointRadius);
        getScriptProxy().addScript(script);
    }

    /**
     * Returns the pointRenderer field.
     * @return pointRenderer
     */
    @SuppressWarnings("unchecked")
    public jsx3.chart.PointRenderer getPointRenderer()
    {
        String extension = "getPointRenderer().";
        try
        {
            java.lang.reflect.Constructor<jsx3.chart.PointRenderer> ctor = jsx3.chart.PointRenderer.class.getConstructor(Context.class, String.class, ScriptProxy.class);
            return ctor.newInstance(this, extension, getScriptProxy());
        }
        catch (Exception ex)
        {
            throw new IllegalArgumentException("Unsupported type: " + jsx3.chart.PointRenderer.class.getName());
        }
    }


    /**
     * Sets the pointRenderer field, should eval to an object that implements the renderer interface.
     * @param pointRenderer the new value for pointRenderer, as a string
     */
    public void setPointRenderer(String pointRenderer)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setPointRenderer", pointRenderer);
        getScriptProxy().addScript(script);
    }

    /**
     * Returns the pointFill field.
     * @param callback pointFill
     */
    @SuppressWarnings("unchecked")
    public void getPointFill(org.directwebremoting.proxy.Callback<String> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getPointFill");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelper.saveCallback(callback, String.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        getScriptProxy().addScript(script);
    }

    /**
     * Sets the pointFill field.
     * @param pointFill the new value for pointFill
     */
    public void setPointFill(String pointFill)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setPointFill", pointFill);
        getScriptProxy().addScript(script);
    }

    /**
     * Returns the pointStroke field.
     * @param callback pointStroke
     */
    @SuppressWarnings("unchecked")
    public void getPointStroke(org.directwebremoting.proxy.Callback<String> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getPointStroke");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelper.saveCallback(callback, String.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        getScriptProxy().addScript(script);
    }

    /**
     * Sets the pointStroke field.
     * @param pointStroke the new value for pointStroke
     */
    public void setPointStroke(String pointStroke)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setPointStroke", pointStroke);
        getScriptProxy().addScript(script);
    }

    /**
     * Returns the pointGradient field.
     * @param callback pointGradient
     */
    @SuppressWarnings("unchecked")
    public void getPointGradient(org.directwebremoting.proxy.Callback<String> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getPointGradient");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelper.saveCallback(callback, String.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        getScriptProxy().addScript(script);
    }

    /**
     * Sets the pointGradient field.
     * @param pointGradient the new value for pointGradient
     */
    public void setPointGradient(String pointGradient)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setPointGradient", pointGradient);
        getScriptProxy().addScript(script);
    }

    /**
     * The default tooltip function for this type of series.
     * @param series 
     * @param record 
     */
    @SuppressWarnings("unchecked")
    public void tooltip(jsx3.chart.Series series, jsx3.xml.Node record, org.directwebremoting.proxy.Callback<String> callback)
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
            String key = org.directwebremoting.extend.CallbackHelper.saveCallback(callback, String.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        getScriptProxy().addScript(script);
    }

}

