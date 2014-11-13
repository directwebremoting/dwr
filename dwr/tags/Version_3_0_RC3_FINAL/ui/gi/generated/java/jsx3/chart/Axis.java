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
 * A base class for all types of axis. Provides all the common properties as well as all rendering
logic. Rendering relies on template methods implemented in concrete subclasses.

An axis renders in the following location based on its horizontal and primary properties:

  horizontal x primary   -> bottom
  vertical x primary     -> left
  horizontal x secondary -> top
  vertical x secondary   -> right
 * @author Joe Walker [joe at getahead dot org]
 * @author DRAPGEN - Dwr Reverse Ajax Proxy GENerator
 */
public class Axis extends jsx3.chart.ChartComponent
{
    /**
     * All reverse ajax proxies need context to work from
     * @param context The script that got us to where we are now
     */
    public Axis(Context context, String extension)
    {
        super(context, extension);
    }

    /**
     * The instance initializer.
     * @param name the GI name of the instance
     * @param horizontal whether this axis is horizontal (x), otherwise it's vertical (y)
     * @param primary whether this axis is primary, otherwise it's secondary
     */
    public Axis(String name, boolean horizontal, boolean primary)
    {
        super((Context) null, (String) null);
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall("new Axis", name, horizontal, primary);
        setInitScript(script);
    }


    /**
     * 
     */
    public static final String TICK_INSIDE = "inside";

    /**
     * 
     */
    public static final String TICK_OUTSIDE = "outside";

    /**
     * 
     */
    public static final String TICK_CROSS = "cross";

    /**
     * 
     */
    public static final String TICK_NONE = "none";

    /**
     * 
     */
    public static final String LABEL_HIGH = "high";

    /**
     * 
     */
    public static final String LABEL_LOW = "low";

    /**
     * 
     */
    public static final String LABEL_AXIS = "axis";


    /**
     * formats labels as a percent, ie "50%"
     * @param v
     */

    public void percent(Integer v, org.directwebremoting.ui.Callback<String> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "percent", v);

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, String.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * formats labels in scientific notation, ie "5e2"
     * @param v
     * @param signif
     */

    public void scientific(Integer v, int signif, org.directwebremoting.ui.Callback<String> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "scientific", v, signif);

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, String.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Returns the horizontal field, whether this is an x axis, otherwise it is a y axis.
     * @param callback horizontal
     */

    public void getHorizontal(org.directwebremoting.ui.Callback<Boolean> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getHorizontal");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, Boolean.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Sets the horizontal field.
     * @param horizontal the new value for horizontal
     */
    public void setHorizontal(boolean horizontal)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setHorizontal", horizontal);
        ScriptSessions.addScript(script);
    }

    /**
     * Returns the showAxis field, whether to show the line along the axis.
     * @param callback showAxis
     */

    public void getShowAxis(org.directwebremoting.ui.Callback<Boolean> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getShowAxis");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, Boolean.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Sets the showAxis field.
     * @param showAxis the new value for showAxis
     */
    public void setShowAxis(boolean showAxis)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setShowAxis", showAxis);
        ScriptSessions.addScript(script);
    }

    /**
     * Returns the labelFunction field.
     * @param callback labelFunction
     */

    public void getLabelFunction(org.directwebremoting.ui.Callback<org.directwebremoting.ui.CodeBlock> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getLabelFunction");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, org.directwebremoting.ui.CodeBlock.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Sets the labelFunction field, allows for formatting and transformation of a major tick label; should eval to a function with the signature function(object) : string.
     * @param labelFunction the new value for labelFunction
     */
    public void setLabelFunction(String labelFunction)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setLabelFunction", labelFunction);
        ScriptSessions.addScript(script);
    }

    /**
     * Returns the axisStroke field, string representation of the VectorStroke used to draw the line of the axis.
     * @param callback axisStroke
     */

    public void getAxisStroke(org.directwebremoting.ui.Callback<String> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getAxisStroke");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, String.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Sets the axisStroke field.
     * @param axisStroke the new value for axisStroke
     */
    public void setAxisStroke(String axisStroke)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setAxisStroke", axisStroke);
        ScriptSessions.addScript(script);
    }

    /**
     * Returns the showLabels field, whether to show major tick labels.
     * @param callback showLabels
     */

    public void getShowLabels(org.directwebremoting.ui.Callback<Boolean> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getShowLabels");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, Boolean.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Sets the showLabels field.
     * @param showLabels the new value for showLabels
     */
    public void setShowLabels(boolean showLabels)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setShowLabels", showLabels);
        ScriptSessions.addScript(script);
    }

    /**
     * Returns the labelGap field, the pixel gap between the tick lines and the labels.
     * @param callback labelGap
     */

    public void getLabelGap(org.directwebremoting.ui.Callback<Integer> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getLabelGap");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, Integer.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Sets the labelGap field.
     * @param labelGap the new value for labelGap
     */
    public void setLabelGap(int labelGap)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setLabelGap", labelGap);
        ScriptSessions.addScript(script);
    }

    /**
     * Sets the labelPlacement field, checks for invalid values.
     * @param labelPlacement the new value for labelPlacement, one of {'axis','high','low'}
     */
    public void setLabelPlacement(String labelPlacement)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setLabelPlacement", labelPlacement);
        ScriptSessions.addScript(script);
    }

    /**
     * Returns the tickLength field, the length in pixels of the major tick (if tickPlacement is "cross" the length will actually be twice this.
     * @param callback tickLength
     */

    public void getTickLength(org.directwebremoting.ui.Callback<Integer> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getTickLength");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, Integer.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Sets the tickLength field.
     * @param tickLength the new value for tickLength
     */
    public void setTickLength(int tickLength)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setTickLength", tickLength);
        ScriptSessions.addScript(script);
    }

    /**
     * Returns the tickPlacement field, where to place the major ticks.
     * @param callback tickPlacement, one of {'none','inside','outside','cross'}
     */

    public void getTickPlacement(org.directwebremoting.ui.Callback<String> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getTickPlacement");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, String.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Sets the tickPlacement field.
     * @param tickPlacement the new value for tickPlacement, one of {'none','inside','outside','cross'}
     */
    public void setTickPlacement(String tickPlacement)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setTickPlacement", tickPlacement);
        ScriptSessions.addScript(script);
    }

    /**
     * Returns the tickStroke field, string representation of VectorStroke used to draw major ticks.
     * @param callback tickStroke
     */

    public void getTickStroke(org.directwebremoting.ui.Callback<String> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getTickStroke");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, String.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Sets the tickStroke field.
     * @param tickStroke the new value for tickStroke
     */
    public void setTickStroke(String tickStroke)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setTickStroke", tickStroke);
        ScriptSessions.addScript(script);
    }

    /**
     * Returns the minorTickDivisions field, number of minor tick divisions between major ticks; the number of minor ticks drawn will be this number minus 1.
     * @param callback minorTickDivisions
     */

    public void getMinorTickDivisions(org.directwebremoting.ui.Callback<Integer> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getMinorTickDivisions");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, Integer.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Sets the minorTickDivisions field.
     * @param minorTickDivisions the new value for minorTickDivisions
     */
    public void setMinorTickDivisions(int minorTickDivisions)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setMinorTickDivisions", minorTickDivisions);
        ScriptSessions.addScript(script);
    }

    /**
     * Returns the minorTickLength field, the length in pixels of the minor tick (if tickPlacement is "cross" the length will actually be twice this.
     * @param callback minorTickLength
     */

    public void getMinorTickLength(org.directwebremoting.ui.Callback<Integer> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getMinorTickLength");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, Integer.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Sets the minorTickLength field.
     * @param minorTickLength the new value for minorTickLength
     */
    public void setMinorTickLength(int minorTickLength)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setMinorTickLength", minorTickLength);
        ScriptSessions.addScript(script);
    }

    /**
     * Returns the minorTickPlacement field, where to place the minor ticks.
     * @param callback minorTickPlacement, one of {'none','inside','outside','cross'}
     */

    public void getMinorTickPlacement(org.directwebremoting.ui.Callback<String> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getMinorTickPlacement");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, String.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Sets the minorTickPlacement field.
     * @param minorTickPlacement the new value for minorTickPlacement, one of {'none','inside','outside','cross'}
     */
    public void setMinorTickPlacement(String minorTickPlacement)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setMinorTickPlacement", minorTickPlacement);
        ScriptSessions.addScript(script);
    }

    /**
     * Returns the minorTickStroke field, string representation of VectorStroke used to draw minor ticks.
     * @param callback minorTickStroke
     */

    public void getMinorTickStroke(org.directwebremoting.ui.Callback<String> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getMinorTickStroke");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, String.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Sets the minorTickStroke field.
     * @param minorTickStroke the new value for minorTickStroke
     */
    public void setMinorTickStroke(String minorTickStroke)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setMinorTickStroke", minorTickStroke);
        ScriptSessions.addScript(script);
    }

    /**
     * Returns the labelClass field, the CSS class used to render major tick labels.
     * @param callback labelClass
     */

    public void getLabelClass(org.directwebremoting.ui.Callback<String> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getLabelClass");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, String.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Sets the labelClass field.
     * @param labelClass the new value for labelClass
     */
    public void setLabelClass(String labelClass)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setLabelClass", labelClass);
        ScriptSessions.addScript(script);
    }

    /**
     * Returns the labelStyle field, the CSS style attribute used to render major tick labels.
     * @param callback labelStyle
     */

    public void getLabelStyle(org.directwebremoting.ui.Callback<String> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getLabelStyle");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, String.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Sets the labelStyle field.
     * @param labelStyle the new value for labelStyle
     */
    public void setLabelStyle(String labelStyle)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setLabelStyle", labelStyle);
        ScriptSessions.addScript(script);
    }

    /**
     * Returns the labelColor field, the RGB color value of the label font; note that this is the only way to set the color of the text, using a CSS style attribute will have no effect.
     * @param callback labelColor
     */

    public void getLabelColor(org.directwebremoting.ui.Callback<String> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getLabelColor");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, String.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Sets the labelColor field.
     * @param labelColor the new value for labelColor
     */
    public void setLabelColor(Integer labelColor)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setLabelColor", labelColor);
        ScriptSessions.addScript(script);
    }

    /**
     * Sets the labelColor field.
     * @param labelColor the new value for labelColor
     */
    public void setLabelColor(String labelColor)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setLabelColor", labelColor);
        ScriptSessions.addScript(script);
    }

    /**
     * Returns the display width, the maximum amount of space perpendicular to the axis and outside of the data area that the ticks and labels may occupy (doesn't include area given to axis title).
     * @param callback displayWidth
     */

    public void getDisplayWidth(org.directwebremoting.ui.Callback<Integer> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getDisplayWidth");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, Integer.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Sets the displayWidth field.
     * @param displayWidth the new value for displayWidth
     */
    public void setDisplayWidth(int displayWidth)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setDisplayWidth", displayWidth);
        ScriptSessions.addScript(script);
    }

    /**
     * Returns the optional jsx3.chart.ChartLabel child.
     */

    public jsx3.chart.ChartLabel getAxisTitle()
    {
        String extension = "getAxisTitle().";
        try
        {
            java.lang.reflect.Constructor<jsx3.chart.ChartLabel> ctor = jsx3.chart.ChartLabel.class.getConstructor(Context.class, String.class);
            return ctor.newInstance(this, extension);
        }
        catch (Exception ex)
        {
            throw new IllegalArgumentException("Unsupported type: " + jsx3.chart.ChartLabel.class.getName());
        }
    }


    /**
     * Returns the opposing axis.
     */

    public jsx3.chart.Axis getOpposingAxis()
    {
        String extension = "getOpposingAxis().";
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
     * Returns the opposing axis.
     * @param returnType The expected return type
     */

    public <T> T getOpposingAxis(Class<T> returnType)
    {
        String extension = "getOpposingAxis().";
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

