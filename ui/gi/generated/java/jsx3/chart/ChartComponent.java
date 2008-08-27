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
import org.directwebremoting.io.Context;

/**
 * A base class for every logical component of a chart. A chart component exists in the DOM tree and
is selectable with ctrl-click in a component editor in General Interfaceª Builder.
 * @author Joe Walker [joe at getahead dot org]
 * @author DRAPGEN - Dwr Reverse Ajax Proxy GENerator
 */
public class ChartComponent extends jsx3.gui.Block
{
    /**
     * All reverse ajax proxies need context to work from
     * @param context The script that got us to where we are now
     */
    public ChartComponent(Context context, String extension)
    {
        super(context, extension);
    }

    /**
     * The instance initializer.
     * @param name the GI name of the instance
     */
    public ChartComponent(String name)
    {
        super((Context) null, (String) null);
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall("new ChartComponent", name);
        setInitScript(script);
    }



    /**
     * Returns the chart of which this component is a part.
     * @return this if this is a chart, or the first ancestor that is a chart
     */

    public jsx3.chart.Chart getChart()
    {
        String extension = "getChart().";
        try
        {
            java.lang.reflect.Constructor<jsx3.chart.Chart> ctor = jsx3.chart.Chart.class.getConstructor(Context.class, String.class);
            return ctor.newInstance(this, extension);
        }
        catch (Exception ex)
        {
            throw new IllegalArgumentException("Unsupported type: " + jsx3.chart.Chart.class.getName());
        }
    }

    /**
     * Returns the chart of which this component is a part.
     * @param returnType The expected return type
     * @return this if this is a chart, or the first ancestor that is a chart
     */

    public <T> T getChart(Class<T> returnType)
    {
        String extension = "getChart().";
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

