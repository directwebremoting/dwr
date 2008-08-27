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
 * Paints a vector rectangle.
 * @author Joe Walker [joe at getahead dot org]
 * @author DRAPGEN - Dwr Reverse Ajax Proxy GENerator
 */
public class Rectangle extends jsx3.vector.Shape
{
    /**
     * All reverse ajax proxies need context to work from
     * @param context The script that got us to where we are now
     */
    public Rectangle(Context context, String extension)
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
    public Rectangle(int left, int top, int width, int height)
    {
        super((Context) null, (String) null);
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall("new Rectangle", left, top, width, height);
        setInitScript(script);
    }



    /**
     * Clips this rectangle to the bounds of obj.
     * @param obj any object that has <code>getLeft()</code>, etc methods.
     */
    public void clipToBox(jsx3.gui.Block obj)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "clipToBox", obj);
        ScriptSessions.addScript(script);
    }

    /**
     * Clips this rectangle to the bounds of obj.
     * @param obj any object that has <code>getLeft()</code>, etc methods.
     */
    public void clipToBox(jsx3.html.BlockTag obj)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "clipToBox", obj);
        ScriptSessions.addScript(script);
    }

    /**
     * Clips this rectangle to the bounds of {l1, t1, w1, h1}.
     * @param l1
     * @param t1
     * @param w1
     * @param h1
     */
    public void clipTo(int l1, int t1, int w1, int h1)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "clipTo", l1, t1, w1, h1);
        ScriptSessions.addScript(script);
    }

}

