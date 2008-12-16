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
 * A more efficient way of painting many vector rectangles of the same fill and stroke.
 * @author Joe Walker [joe at getahead dot org]
 * @author DRAPGEN - Dwr Reverse Ajax Proxy GENerator
 */
public class RectangleGroup extends jsx3.vector.Shape
{
    /**
     * All reverse ajax proxies need context to work from
     * @param context The script that got us to where we are now
     */
    public RectangleGroup(Context context, String extension)
    {
        super(context, extension);
    }

    /**
     * The instance initializer.
     * @param left left position (in pixels) of the object relative to its parent container
     * @param top top position (in pixels) of the object relative to its parent container
     * @param width width (in pixels) of the object
     * @param height height (in pixels) of the object
     */
    public RectangleGroup(int left, int top, int width, int height)
    {
        super((Context) null, (String) null);
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall("new RectangleGroup", left, top, width, height);
        setInitScript(script);
    }



    /**
     * add a rectangle to this group
     * @param x1 the x-coordinate of the left edge of the rectangle
     * @param y1 the y-coordinate of the top edge of the rectangle
     * @param x2 the x-coordinate of the right edge of the rectangle
     * @param y2 the y-coordinate of the bottom edge of the rectangle
     */
    public void addRectangle(int x1, int y1, int x2, int y2)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "addRectangle", x1, y1, x2, y2);
        ScriptSessions.addScript(script);
    }

    /**
     * add a rectangle to this group
     * @param x1 the x-coordinate of the left edge of the rectangle
     * @param y1 the y-coordinate of the top edge of the rectangle
     * @param w the width of the rectangle
     * @param h the height of the rectangle
     */
    public void addRelativeRectangle(int x1, int y1, int w, int h)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "addRelativeRectangle", x1, y1, w, h);
        ScriptSessions.addScript(script);
    }

    /**
     * clear all rectangles out of the group
     */
    public void clearRectangles()
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "clearRectangles");
        ScriptSessions.addScript(script);
    }

}

