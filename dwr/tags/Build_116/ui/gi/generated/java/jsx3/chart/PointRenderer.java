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
 * Objects that implement this interface may be used in Line/Area/Point/Bubble charts to render the points that appear
at each datapoint.

Additionally, this interface contains several static fields that are implementors of this interface.
 * @author Joe Walker [joe at getahead dot org]
 * @author DRAPGEN - Dwr Reverse Ajax Proxy GENerator
 */
public class PointRenderer extends jsx3.lang.Object
{
    /**
     * All reverse ajax proxies need context to work from
     * @param context The script that got us to where we are now
     */
    public PointRenderer(Context context, String extension)
    {
        super(context, extension);
    }



    /**
     * Renders the point in the bounds specified by {x1,y1} {x2,y2}.
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @param fill
     * @param stroke
     */

    public jsx3.vector.Tag render(int x1, int y1, int x2, int y2, jsx3.vector.Fill fill, jsx3.vector.Stroke stroke)
    {
        String extension = "render(\"" + x1 + "\", \"" + y1 + "\", \"" + x2 + "\", \"" + y2 + "\", \"" + fill + "\", \"" + stroke + "\").";
        try
        {
            java.lang.reflect.Constructor<jsx3.vector.Tag> ctor = jsx3.vector.Tag.class.getConstructor(Context.class, String.class);
            return ctor.newInstance(this, extension);
        }
        catch (Exception ex)
        {
            throw new IllegalArgumentException("Unsupported type: " + jsx3.vector.Tag.class.getName());
        }
    }

    /**
     * Renders the point in the bounds specified by {x1,y1} {x2,y2}.
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @param fill
     * @param stroke
     * @param returnType The expected return type
     */

    public <T> T render(int x1, int y1, int x2, int y2, jsx3.vector.Fill fill, jsx3.vector.Stroke stroke, Class<T> returnType)
    {
        String extension = "render(\"" + x1 + "\", \"" + y1 + "\", \"" + x2 + "\", \"" + y2 + "\", \"" + fill + "\", \"" + stroke + "\").";
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
     * Converts the area of the point to display to the radius of the box that it should fill. Required
   if the point renderer will be used in a plot chart.
     * @param area
     */

    public void areaToRadius(Integer area, org.directwebremoting.ui.Callback<Integer> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "areaToRadius", area);

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, Integer.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

}

