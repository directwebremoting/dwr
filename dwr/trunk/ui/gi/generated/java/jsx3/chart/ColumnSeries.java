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
 * The data series type for a jsx3.chart.ColumnChart. Draws vertical columns between the x axis and a y value determined
by the data provider, or between two y values determined by the data provider. A column series has the
following fields:

yField the attribute of a record to use as the vertical extent of the column, required
xField the attribute of a record to use as the horizontal midpoint of the column, required if the x axis
    of the chart is a value axis
minField the attribute of a record to use as the minimum vertical extent of the column, optional
 * @author Joe Walker [joe at getahead dot org]
 * @author DRAPGEN - Dwr Reverse Ajax Proxy GENerator
 */
public class ColumnSeries extends jsx3.chart.BCSeries
{
    /**
     * All reverse ajax proxies need context to work from
     * @param context The script that got us to where we are now
     */
    public ColumnSeries(Context context, String extension)
    {
        super(context, extension);
    }

    /**
     * The instance initializer.
     * @param name the GI name of the instance
     * @param seriesName the name of the Series, will be displayed in the Legend for most chart types
     */
    public ColumnSeries(String name, String seriesName)
    {
        super((Context) null, (String) null);
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall("new ColumnSeries", name, seriesName);
        setInitScript(script);
    }


    /**
     * 
     */
    public static final int DEFAULT_COLUMNWIDTH = 10;


    /**
     * Returns the columnWidth field.
     * @param callback columnWidth
     */

    public void getColumnWidth(org.directwebremoting.ui.Callback<Integer> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getColumnWidth");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, Integer.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Sets the columnWidth field.
     * @param columnWidth the new value for columnWidth
     */
    public void setColumnWidth(int columnWidth)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setColumnWidth", columnWidth);
        ScriptSessions.addScript(script);
    }

    /**
     * The default tooltip function for this type of series.
     * @param series
     * @param record
     */

    public void tooltip(jsx3.chart.Series series, jsx3.xml.Node record, org.directwebremoting.ui.Callback<String> callback)
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
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, String.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

}

