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
package jsx3.gui;

import org.directwebremoting.ScriptBuffer;
import org.directwebremoting.ScriptSessions;
import org.directwebremoting.io.Context;

/**
 * Abstract superclass of model objects that are painted to screen.
 * @author Joe Walker [joe at getahead dot org]
 * @author DRAPGEN - Dwr Reverse Ajax Proxy GENerator
 */
public class Painted extends jsx3.app.Model
{
    /**
     * All reverse ajax proxies need context to work from
     * @param context The script that got us to where we are now
     */
    public Painted(Context context, String extension)
    {
        super(context, extension);
    }

    /**
     * instance initializer
     * @param strName unique name distinguishing this object from all other JSX GUI objects in the JSX application
     */
    public Painted(String strName)
    {
        super((Context) null, (String) null);
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall("new Painted", strName);
        setInitScript(script);
    }



    /**
     * Returns the absolute positioning of the object's on-screen view in relation to JSXROOT (whose left/top is 0/0).
           Returns information as a JavaScript object with properties, L, T, W, H
           of @objRoot is null, the on-screen view for JSXROOT is used as the object reference
     * @param objRoot object reference to IE DOM object (i.e., div, span, etc); if null is passed, the first div child of JSXROOT's on-screen representation will be used
     * @param objGUI object reference to item to get absolute position for&#8212;as opposed to this instance (useful for determining placement of html objects contained by JSX objects, but not part of the actual JSX DOM)
     * @return JScript object with properties: L, T, W, H (corresponding to left, top width, height)
     */

    public jsx3.lang.Object getAbsolutePosition(String objRoot, String objGUI)
    {
        String extension = "getAbsolutePosition(\"" + objRoot + "\", \"" + objGUI + "\").";
        try
        {
            java.lang.reflect.Constructor<jsx3.lang.Object> ctor = jsx3.lang.Object.class.getConstructor(Context.class, String.class);
            return ctor.newInstance(this, extension);
        }
        catch (Exception ex)
        {
            throw new IllegalArgumentException("Unsupported type: " + jsx3.lang.Object.class.getName());
        }
    }

    /**
     * Returns the absolute positioning of the object's on-screen view in relation to JSXROOT (whose left/top is 0/0).
           Returns information as a JavaScript object with properties, L, T, W, H
           of @objRoot is null, the on-screen view for JSXROOT is used as the object reference
     * @param objRoot object reference to IE DOM object (i.e., div, span, etc); if null is passed, the first div child of JSXROOT's on-screen representation will be used
     * @param objGUI object reference to item to get absolute position for&#8212;as opposed to this instance (useful for determining placement of html objects contained by JSX objects, but not part of the actual JSX DOM)
     * @param returnType The expected return type
     * @return JScript object with properties: L, T, W, H (corresponding to left, top width, height)
     */

    public <T> T getAbsolutePosition(String objRoot, String objGUI, Class<T> returnType)
    {
        String extension = "getAbsolutePosition(\"" + objRoot + "\", \"" + objGUI + "\").";
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
     * assigns a dynamic property to one of this object's properties
           returns reference to self to facilitate method chaining;
     * @param strName property on this GUI object that will now use a dynamic property (e.g., 'jsxleft','jsxtop','jsxheight',etc.);
     * @param strValue name of a dynamic style, whose value will be used
     * @return this object
     */
    public jsx3.gui.Painted setDynamicProperty(String strName, String strValue)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setDynamicProperty", strName, strValue);
        ScriptSessions.addScript(script);
        return this;
    }

    /**
     * Returns the value of the dynamic property @strPropName; if not found, returns null
     * @param strName property on this GUI object that will now use a dynamic property (e.g., 'jsxleft','jsxtop','jsxheight',etc.);
     * @param callback value of the property
     */

    public void getDynamicProperty(String strName, org.directwebremoting.ui.Callback<String> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getDynamicProperty", strName);

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, String.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Sets a property on the object that when the object is rendered on-screen, the HTML tag will be assigned the given name/value pair as a tag attribute
     * @param strName the name of the property/attribute
     * @param strValue the value for the property; may not contain double-quotes; escape via jsx3.util.strEscapeHTML if necessary or use combinations of single-quotes and escaped single-quotes
     * @return this object (this)
     */
    public jsx3.gui.Painted setAttribute(String strName, String strValue)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setAttribute", strName, strValue);
        ScriptSessions.addScript(script);
        return this;
    }

    /**
     * Returns value for the custom attribute with the given name; returns null if no attribute found
     * @param strName the name of the property/attribute
     */

    public void getAttribute(String strName, org.directwebremoting.ui.Callback<String> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getAttribute", strName);

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, String.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Returns handle to the JavaScript Object Array containing all events for the JSX GUI object;
           NOTE: This object will contain zero or more JavaScript Objects with the following Properties: script, type, system
     */

    public jsx3.lang.Object getAttributes()
    {
        String extension = "getAttributes().";
        try
        {
            java.lang.reflect.Constructor<jsx3.lang.Object> ctor = jsx3.lang.Object.class.getConstructor(Context.class, String.class);
            return ctor.newInstance(this, extension);
        }
        catch (Exception ex)
        {
            throw new IllegalArgumentException("Unsupported type: " + jsx3.lang.Object.class.getName());
        }
    }

    /**
     * Returns handle to the JavaScript Object Array containing all events for the JSX GUI object;
           NOTE: This object will contain zero or more JavaScript Objects with the following Properties: script, type, system
     * @param returnType The expected return type
     */

    public <T> T getAttributes(Class<T> returnType)
    {
        String extension = "getAttributes().";
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
     * removes the specific custom property bound to this object; returns a reference to self (this) to facilitate method chaining
     * @param strName the name of the custom property to remove
     * @return this object
     */

    public jsx3.gui.Painted removeAttribute(String strName)
    {
        String extension = "removeAttribute(\"" + strName + "\").";
        try
        {
            java.lang.reflect.Constructor<jsx3.gui.Painted> ctor = jsx3.gui.Painted.class.getConstructor(Context.class, String.class);
            return ctor.newInstance(this, extension);
        }
        catch (Exception ex)
        {
            throw new IllegalArgumentException("Unsupported type: " + jsx3.gui.Painted.class.getName());
        }
    }

    /**
     * removes the specific custom property bound to this object; returns a reference to self (this) to facilitate method chaining
     * @param strName the name of the custom property to remove
     * @param returnType The expected return type
     * @return this object
     */

    public <T> T removeAttribute(String strName, Class<T> returnType)
    {
        String extension = "removeAttribute(\"" + strName + "\").";
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
     * removes all events bound to this object; NOTE: The object must still be painted/repainted for its corresponding on-screen view to be likewise updated; returns a reference to self (this) to facilitate method chaining
     * @return this object
     */

    public jsx3.gui.Painted removeAttributes()
    {
        String extension = "removeAttributes().";
        try
        {
            java.lang.reflect.Constructor<jsx3.gui.Painted> ctor = jsx3.gui.Painted.class.getConstructor(Context.class, String.class);
            return ctor.newInstance(this, extension);
        }
        catch (Exception ex)
        {
            throw new IllegalArgumentException("Unsupported type: " + jsx3.gui.Painted.class.getName());
        }
    }

    /**
     * removes all events bound to this object; NOTE: The object must still be painted/repainted for its corresponding on-screen view to be likewise updated; returns a reference to self (this) to facilitate method chaining
     * @param returnType The expected return type
     * @return this object
     */

    public <T> T removeAttributes(Class<T> returnType)
    {
        String extension = "removeAttributes().";
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
     * gives focus to the on-screen VIEW for the element; returns a handle to the html/dhtml element as exposed by the native browser
     */

    public void focus(org.directwebremoting.ui.Callback<String> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "focus");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, String.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Returns handle/reference to the JSX GUI Object's on-screen counterpart—basically a handle to a DHTML object such as a DIV, SPAN, etc
     * @param objGUI either the HTML document containing the rendered object or an HTML element in that document.
  This argument is optional but improves the efficiency of this method if provided.
     * @param callback IE DHTML object
     */

    public void getRendered(jsx3.gui.Event objGUI, org.directwebremoting.ui.Callback<String> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getRendered", objGUI);

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, String.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Returns handle/reference to the JSX GUI Object's on-screen counterpart—basically a handle to a DHTML object such as a DIV, SPAN, etc
     * @param objGUI either the HTML document containing the rendered object or an HTML element in that document.
  This argument is optional but improves the efficiency of this method if provided.
     * @param callback IE DHTML object
     */

    public void getRendered(jsx3.lang.Object objGUI, org.directwebremoting.ui.Callback<String> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getRendered", objGUI);

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, String.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Updates the view of this object by calling paint() and replacing the current view with the
returned HTML. This method has no effect if this object is not currently displayed.
     * @param callback the result of calling <code>paint()</code> or <code>null</code> if this object is not displayed.
     */

    public void repaint(org.directwebremoting.ui.Callback<String> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "repaint");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, String.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Returns the DHTML, used for this object's on-screen VIEW
     * @param callback DHTML
     */

    public void paint(org.directwebremoting.ui.Callback<String> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "paint");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, String.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * A hook that subclasses of Painted may override in order to perform additional manipulation of the HTML DOM
created by the paint method. The order of steps follows. All steps occur in a single browser thread so that
the screen does not update between steps 2 and 3.

  The paint() method of this object is called.
  The result of the paint() method is inserted into the HTML DOM.
  The onAfterPaint() method of this object is called, passing in the newly inserted root HTML element.
     * @param objGUI the rendered HTML element representing this object.
     */
    public void onAfterPaint(String objGUI)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "onAfterPaint", objGUI);
        ScriptSessions.addScript(script);
    }

    /**
     * Paints a child of this object without repainting this entire object. The child is inserted into the view of
this object as the last child object, regardless of its actual position relative to other children. This method
has no effect if this object is not currently painted.
     * @param objChild the child object to paint.
     * @param bGroup <code>true</code> if this method is being called iteratively over a collection of
  children. This parameter will only be <code>false</code> on the final call in the iteration.
     * @param objGUI
     * @param bCascadeOnly
     */
    public void paintChild(jsx3.gui.Painted objChild, boolean bGroup, String objGUI, boolean bCascadeOnly)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "paintChild", objChild, bGroup, objGUI, bCascadeOnly);
        ScriptSessions.addScript(script);
    }

    /**
     * Iterates through children and returns concatenation of paint() method for all children.
     * @param c the children to paint. If not provided <code>this.getChildren()</code> is used.
     * @param callback DHTML
     */

    public void paintChildren(Object[] c, org.directwebremoting.ui.Callback<String> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "paintChildren", c);

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, String.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Removes the box model abstraction for a given object and its descendants. This effectively resets the box profiler, so dimensions can be recalculated as if the object was just broought into the visual DOM.
     * @param properties Will designate by name, those properties that should be updated on the object's VIEW (without requiring the MODEL to repaint), including one or more of the following: padding, margin, border
     */
    public void recalcBox(Object[] properties)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "recalcBox", properties);
        ScriptSessions.addScript(script);
    }

}

