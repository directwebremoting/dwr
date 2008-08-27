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
 * Defines a base class for GUI controls that implement both the cross-platform box profile painting introduced in
3.2 and the cross-platform (VML/SVG) vector painting, also introduced in 3.2.

This class should be extended by custom GUI classes that will display vector elements.
 * @author Joe Walker [joe at getahead dot org]
 * @author DRAPGEN - Dwr Reverse Ajax Proxy GENerator
 */
public class Block extends jsx3.gui.Block
{
    /**
     * All reverse ajax proxies need context to work from
     * @param context The script that got us to where we are now
     */
    public Block(Context context, String extension)
    {
        super(context, extension);
    }



    /**
     * Returns the vector canvas on which this control paints itself. If no canvas has already been created, then
createVector() is called to create it.
     */

    public jsx3.vector.Tag getCanvas()
    {
        String extension = "getCanvas().";
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
     * Returns the vector canvas on which this control paints itself. If no canvas has already been created, then
createVector() is called to create it.
     * @param returnType The expected return type
     */

    public <T> T getCanvas(Class<T> returnType)
    {
        String extension = "getCanvas().";
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
     * Creates the vector tag tree that will render this GUI control. Subclasses of this class should override this
method to specify the manner in which they render.

The basic template for a method overriding this method is:

CustomVector.prototype.createVector = function() {
  var objCanvas = this.jsxsuper();
  // modify objCanvas, add children, etc.
  return objCanvas;
};

This method should do the work of creating and updating the vector tree to the state when it is ready to be
rendered on screen, but without calling updateVector() directly.
     */

    public jsx3.vector.Tag createVector()
    {
        String extension = "createVector().";
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
     * Creates the vector tag tree that will render this GUI control. Subclasses of this class should override this
method to specify the manner in which they render.

The basic template for a method overriding this method is:

CustomVector.prototype.createVector = function() {
  var objCanvas = this.jsxsuper();
  // modify objCanvas, add children, etc.
  return objCanvas;
};

This method should do the work of creating and updating the vector tree to the state when it is ready to be
rendered on screen, but without calling updateVector() directly.
     * @param returnType The expected return type
     */

    public <T> T createVector(Class<T> returnType)
    {
        String extension = "createVector().";
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
     * Updates the pre-existing vector tree of this control on, for example, a resize or repaint event. Methods
overriding this method should return true if the update is successful or false to
force the vector tree to be completely recreated with createVector().

The basic template for a method overriding this method is:

CustomVector.prototype.updateVector = function(objVector) {
  this.jsxsuper(objVector);
  // modify objCanvas, modify children, etc.
  return true;
};
     * @param objVector the root of the vector render tree.
     * @param callback <code>true</code> if the tree could be updated inline or <code>false</code> if it must be
   recreated by calling <code>createVector()</code>.
     */

    public void updateVector(jsx3.vector.Tag objVector, org.directwebremoting.ui.Callback<Boolean> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "updateVector", objVector);

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, Boolean.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Instantiates and returns a new instance of jsx3.vector.Canvas. The implementation of
createVector() in this class calls this method to create the base vector tag. This method may be
overridden to provide a base tag of another type that Canvas.
     */

    public jsx3.vector.Tag createCanvas()
    {
        String extension = "createCanvas().";
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
     * Instantiates and returns a new instance of jsx3.vector.Canvas. The implementation of
createVector() in this class calls this method to create the base vector tag. This method may be
overridden to provide a base tag of another type that Canvas.
     * @param returnType The expected return type
     */

    public <T> T createCanvas(Class<T> returnType)
    {
        String extension = "createCanvas().";
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
     * Renders a cross-platform vector event handler. When an event of type strEvtType bubbles up to the
HTML element rendered by objElm, the instance method of this object whose name is
strMethod will be called with two parameters: the browser event wrapped in an instance of
jsx3.gui.Event, and the native HTMLElement that defined the event handler.
     * @param strEvtType the event type, one of <code>jsx3.gui.Event.CLICK</code>, etc.
     * @param strMethod the instance method to call on this object when the event is received.
     * @param objElm the HTML element to which to add the event handler.
     */
    public void paintEventHandler(String strEvtType, String strMethod, jsx3.vector.Tag objElm)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "paintEventHandler", strEvtType, strMethod, objElm);
        ScriptSessions.addScript(script);
    }

}

