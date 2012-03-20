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
package jsx3.vector;

import org.directwebremoting.ScriptBuffer;
import org.directwebremoting.ScriptSessions;
import org.directwebremoting.io.Context;

/**
 * Paints a vector line defined by two end points.
 * @author Joe Walker [joe at getahead dot org]
 * @author DRAPGEN - Dwr Reverse Ajax Proxy GENerator
 */
public class Line extends jsx3.vector.Shape
{
    /**
     * All reverse ajax proxies need context to work from
     * @param context The script that got us to where we are now
     */
    public Line(Context context, String extension)
    {
        super(context, extension);
    }

    /**
     * The instance initializer.
     * @param left left position (in pixels) of the object relative to its parent container
     * @param top top position (in pixels) of the object relative to its parent container
     * @param x1 the x coordinate of the starting point
     * @param y1 the y coordinate of the starting point
     * @param x2 the x coordinate of the ending point
     * @param y2 the y coordinate of the ending point
     */
    public Line(int left, int top, int x1, int y1, int x2, int y2)
    {
        super((Context) null, (String) null);
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall("new Line", left, top, x1, y1, x2, y2);
        setInitScript(script);
    }



    /**
     * Sets all the coordinates at once.
     * @param x1 the x coordinate of the starting point
     * @param y1 the y coordinate of the starting point
     * @param x2 the x coordinate of the ending point
     * @param y2 the y coordinate of the ending point
     */
    public void setPoints(int x1, int y1, int x2, int y2)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setPoints", x1, y1, x2, y2);
        ScriptSessions.addScript(script);
    }

    /**
     * Returns the x1 field.
     * @param callback x1
     */

    public void getX1(org.directwebremoting.ui.Callback<Integer> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getX1");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, Integer.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Sets the x1 field.
     * @param x1 the new value for x1
     */
    public void setX1(int x1)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setX1", x1);
        ScriptSessions.addScript(script);
    }

    /**
     * Returns the y1 field.
     * @param callback y1
     */

    public void getY1(org.directwebremoting.ui.Callback<Integer> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getY1");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, Integer.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Sets the y1 field.
     * @param y1 the new value for y1
     */
    public void setY1(int y1)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setY1", y1);
        ScriptSessions.addScript(script);
    }

    /**
     * Returns the x2 field.
     * @param callback x2
     */

    public void getX2(org.directwebremoting.ui.Callback<Integer> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getX2");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, Integer.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Sets the x2 field.
     * @param x2 the new value for x2
     */
    public void setX2(int x2)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setX2", x2);
        ScriptSessions.addScript(script);
    }

    /**
     * Returns the y2 field.
     * @param callback y2
     */

    public void getY2(org.directwebremoting.ui.Callback<Integer> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getY2");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, Integer.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Sets the y2 field.
     * @param y2 the new value for y2
     */
    public void setY2(int y2)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setY2", y2);
        ScriptSessions.addScript(script);
    }

}

