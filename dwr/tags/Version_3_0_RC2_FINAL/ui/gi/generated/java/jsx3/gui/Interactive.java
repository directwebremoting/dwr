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
 * Mixin interface. Provides functionality to subclasses of jsx3.gui.Painted that allows them to publish model
events.

Note that this class requires that implementors of this class extends  the jsx3.gui.Painted class and
implement the jsx3.util.EventDispatcher interface
 * @author Joe Walker [joe at getahead dot org]
 * @author DRAPGEN - Dwr Reverse Ajax Proxy GENerator
 */
public class Interactive extends jsx3.lang.Object
{
    /**
     * All reverse ajax proxies need context to work from
     * @param context The script that got us to where we are now
     */
    public Interactive(Context context, String extension)
    {
        super(context, extension);
    }


    /**
     * 
     */
    public static final String JSXBLUR = "jsxblur";

    /**
     * 
     */
    public static final String JSXCHANGE = "jsxchange";

    /**
     * 
     */
    public static final String JSXCLICK = "jsxclick";

    /**
     * 
     */
    public static final String JSXDOUBLECLICK = "jsxdblclick";

    /**
     * 
     */
    public static final String JSXFOCUS = "jsxfocus";

    /**
     * 
     */
    public static final String JSXKEYDOWN = "jsxkeydown";

    /**
     * 
     */
    public static final String JSXKEYPRESS = "jsxkeypress";

    /**
     * 
     */
    public static final String JSXKEYUP = "jsxkeyup";

    /**
     * 
     */
    public static final String JSXLOAD = "jsxload";

    /**
     * 
     */
    public static final String JSXMOUSEDOWN = "jsxmousedown";

    /**
     * 
     */
    public static final String JSXMOUSEOUT = "jsxmouseout";

    /**
     * 
     */
    public static final String JSXMOUSEOVER = "jsxmouseover";

    /**
     * 
     */
    public static final String JSXMOUSEUP = "jsxmouseup";

    /**
     * 
     */
    public static final String JSXMOUSEWHEEL = "jsxmousewheel";

    /**
     * 
     */
    public static final String FOCUS_STYLE = "text-decoration:underline";

    /**
     * 
     */
    public static final String ADOPT = "jsxadopt";

    /**
     * 
     */
    public static final String AFTER_APPEND = "jsxafterappend";

    /**
     * 
     */
    public static final String AFTER_COMMIT = "jsxaftercommit";

    /**
     * 
     */
    public static final String AFTER_EDIT = "jsxafteredit";

    /**
     * 
     */
    public static final String AFTER_MOVE = "jsxaftermove";

    /**
     * 
     */
    public static final String AFTER_REORDER = "jsxafterreorder";

    /**
     * 
     */
    public static final String AFTER_RESIZE = "jsxafterresize";

    /**
     * 
     */
    public static final String AFTER_RESIZE_VIEW = "jsxafterresizeview";

    /**
     * 
     */
    public static final String AFTER_SORT = "jsxaftersort";

    /**
     * 
     */
    public static final String BEFORE_APPEND = "jsxbeforeappend";

    /**
     * 
     */
    public static final String BEFORE_DROP = "jsxbeforedrop";

    /**
     * 
     */
    public static final String BEFORE_EDIT = "jsxbeforeedit";

    /**
     * 
     */
    public static final String BEFORE_MOVE = "jsxbeforemove";

    /**
     * 
     */
    public static final String BEFORE_RESIZE = "jsxbeforeresize";

    /**
     * 
     */
    public static final String BEFORE_SELECT = "jsxbeforeselect";

    /**
     * 
     */
    public static final String BEFORE_SORT = "jsxbeforesort";

    /**
     * 
     */
    public static final String CANCEL_DROP = "jsxcanceldrop";

    /**
     * 
     */
    public static final String CHANGE = "jsxchange";

    /**
     * 
     */
    public static final String CTRL_DROP = "jsxctrldrop";

    /**
     * 
     */
    public static final String DESTROY = "jsxdestroy";

    /**
     * 
     */
    public static final String DATA = "jsxdata";

    /**
     * 
     */
    public static final String DRAG = "jsxdrag";

    /**
     * 
     */
    public static final String DROP = "jsxdrop";

    /**
     * 
     */
    public static final String EXECUTE = "jsxexecute";

    /**
     * 
     */
    public static final String HIDE = "jsxhide";

    /**
     * 
     */
    public static final String INCR_CHANGE = "jsxincchange";

    /**
     * 
     */
    public static final String INPUT = "jsxinput";

    /**
     * 
     */
    public static final String MENU = "jsxmenu";

    /**
     * 
     */
    public static final String SCROLL = "jsxscroll";

    /**
     * 
     */
    public static final String SELECT = "jsxselect";

    /**
     * 
     */
    public static final String SHOW = "jsxshow";

    /**
     * 
     */
    public static final String SPYGLASS = "jsxspy";

    /**
     * 
     */
    public static final String TOGGLE = "jsxtoggle";


    /**
     * Programmatically sets an event of this instance. Sets the script that will execute when this object publishes
a model event. The script value will be saved in the serialization file of a component. Not all classes that
implement this interface will publish events of every type. Consult the documentation of a class for a
description of the events it publishes.

For programmatic registering of event handlers when persistence in a serialization file is not required,
consider using jsx3.util.EventDispatcher.subscribe() instead of this method. Whenever a model
event is published, it is published using the EventDispatcher interface as well as by executing
any registered event script.
     * @param strScript the actual JavaScript code that will execute when the given event is published.
   For example: <code>obj.setEvent("alert('hello.');", jsx3.gui.Interactive.EXECUTE);</code>
     * @param strType the event type. Must be one of the model event types defined as static fields in this class
     * @return reference to this
     */
    public jsx3.gui.Interactive setEvent(String strScript, String strType)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setEvent", strScript, strType);
        ScriptSessions.addScript(script);
        return this;
    }

    /**
     * Returns the associative array containing all the registered event script of this object. This method returns
the instance field itself and not a copy.
     * @return an associative array mapping event type to event script
     */

    public jsx3.lang.Object getEvents()
    {
        String extension = "getEvents().";
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
     * Returns the associative array containing all the registered event script of this object. This method returns
the instance field itself and not a copy.
     * @param returnType The expected return type
     * @return an associative array mapping event type to event script
     */

    public <T> T getEvents(Class<T> returnType)
    {
        String extension = "getEvents().";
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
     * Returns the event script registered for the given event type. This script could have been set by the
setEvent() method or during component deserialization.
     * @param strType the event type, one of the model event types defined as static fields in this class
     * @param callback the JavaScript event script
     */

    public void getEvent(String strType, org.directwebremoting.ui.Callback<String> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getEvent", strType);

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, String.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Returns true if there is a event script registered for the given event type.
     * @param strType the event type, one of the model event types defined as static fields in this class
     * @param callback the JavaScript event script
     */

    public void hasEvent(String strType, org.directwebremoting.ui.Callback<String> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "hasEvent", strType);

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, String.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Publishes a model event. This method both evaluates any registered event script for the given event type
and publishes the event through the EventDispatcher interface. This method ensures that any
registered event script is executed in isolation to prevent most side effects.
     * @param strType the event type, one of the model event types defined as static fields in this class
     * @param objContext JavaScript object array with name/value pairs that provide a local
   variable stack for the execution of the event script. This argument is also passed as the <code>context</code>
   property of the event object that is published through the <code>EventDispatcher</code> interface.
     * @return the result of evaluating the event script or <code>null</code> if not event script is registered
     */

    public jsx3.lang.Object doEvent(String strType, jsx3.lang.Object objContext)
    {
        String extension = "doEvent(\"" + strType + "\", \"" + objContext + "\").";
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
     * Publishes a model event. This method both evaluates any registered event script for the given event type
and publishes the event through the EventDispatcher interface. This method ensures that any
registered event script is executed in isolation to prevent most side effects.
     * @param strType the event type, one of the model event types defined as static fields in this class
     * @param objContext JavaScript object array with name/value pairs that provide a local
   variable stack for the execution of the event script. This argument is also passed as the <code>context</code>
   property of the event object that is published through the <code>EventDispatcher</code> interface.
     * @param returnType The expected return type
     * @return the result of evaluating the event script or <code>null</code> if not event script is registered
     */

    public <T> T doEvent(String strType, jsx3.lang.Object objContext, Class<T> returnType)
    {
        String extension = "doEvent(\"" + strType + "\", \"" + objContext + "\").";
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
     * Removes an event script registered for the given model event type.
     * @param strType the event type, one of the model event types defined as static fields in this class
     * @return this object
     */

    public jsx3.gui.Interactive removeEvent(String strType)
    {
        String extension = "removeEvent(\"" + strType + "\").";
        try
        {
            java.lang.reflect.Constructor<jsx3.gui.Interactive> ctor = jsx3.gui.Interactive.class.getConstructor(Context.class, String.class);
            return ctor.newInstance(this, extension);
        }
        catch (Exception ex)
        {
            throw new IllegalArgumentException("Unsupported type: " + jsx3.gui.Interactive.class.getName());
        }
    }

    /**
     * Removes an event script registered for the given model event type.
     * @param strType the event type, one of the model event types defined as static fields in this class
     * @param returnType The expected return type
     * @return this object
     */

    public <T> T removeEvent(String strType, Class<T> returnType)
    {
        String extension = "removeEvent(\"" + strType + "\").";
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
     * Removes all events scripts registered with this object.
     * @return this object
     */

    public jsx3.gui.Interactive removeEvents()
    {
        String extension = "removeEvents().";
        try
        {
            java.lang.reflect.Constructor<jsx3.gui.Interactive> ctor = jsx3.gui.Interactive.class.getConstructor(Context.class, String.class);
            return ctor.newInstance(this, extension);
        }
        catch (Exception ex)
        {
            throw new IllegalArgumentException("Unsupported type: " + jsx3.gui.Interactive.class.getName());
        }
    }

    /**
     * Removes all events scripts registered with this object.
     * @param returnType The expected return type
     * @return this object
     */

    public <T> T removeEvents(Class<T> returnType)
    {
        String extension = "removeEvents().";
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
     * Sets whether is object can be moved around the screen (this is not the same as drag/drop). Implementing classes
can decide whether to consult this value or ignore it.
     * @param bMovable <code>jsx3.Boolean.TRUE</code> or <code>jsx3.Boolean.FALSE</code>
     * @return this object
     */
    public jsx3.gui.Interactive setCanMove(int bMovable)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setCanMove", bMovable);
        ScriptSessions.addScript(script);
        return this;
    }

    /**
     * Returns whether is object can be moved around the screen (this is not the same as drag/drop).
     * @param callback <code>jsx3.Boolean.TRUE</code> or <code>jsx3.Boolean.FALSE</code>
     */

    public void getCanMove(org.directwebremoting.ui.Callback<Integer> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getCanMove");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, Integer.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Sets whether is object supports programmatic drag, meanining it will allow any contained item to be dragged/dropped.
Implementing classes can decide whether to consult this value or ignore it.
     * @param bDrag <code>jsx3.Boolean.TRUE</code> or <code>jsx3.Boolean.FALSE</code>
     * @return this object
     */
    public jsx3.gui.Interactive setCanDrag(int bDrag)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setCanDrag", bDrag);
        ScriptSessions.addScript(script);
        return this;
    }

    /**
     * Returns whether is object supports programmatic drag, meanining it will allow any contained item to be
dragged and dropped on another container supporting drop.
     * @param callback <code>jsx3.Boolean.TRUE</code> or <code>jsx3.Boolean.FALSE</code>
     */

    public void getCanDrag(org.directwebremoting.ui.Callback<Integer> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getCanDrag");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, Integer.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Sets whether this object can be the target of a drop event. Implementing classes can decide whether to consult
this value or ignore it.
     * @param bDrop <code>jsx3.Boolean.TRUE</code> or <code>jsx3.Boolean.FALSE</code>
     * @return this object
     */
    public jsx3.gui.Interactive setCanDrop(int bDrop)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setCanDrop", bDrop);
        ScriptSessions.addScript(script);
        return this;
    }

    /**
     * Returns whether this object can be the target of a drop event.
     * @param callback <code>jsx3.Boolean.TRUE</code> or <code>jsx3.Boolean.FALSE</code>
     */

    public void getCanDrop(org.directwebremoting.ui.Callback<Integer> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getCanDrop");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, Integer.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Sets whether is object can be spyglassed. Implementing classes can decide whether to consult
this value or ignore it.
     * @param bSpy <code>jsx3.Boolean.TRUE</code> or <code>jsx3.Boolean.FALSE</code>
     * @return this object
     */
    public jsx3.gui.Interactive setCanSpy(int bSpy)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setCanSpy", bSpy);
        ScriptSessions.addScript(script);
        return this;
    }

    /**
     * Returns whether is object can be spyglassed.
     * @param callback <code>jsx3.Boolean.TRUE</code> or <code>jsx3.Boolean.FALSE</code>
     */

    public void getCanSpy(org.directwebremoting.ui.Callback<Integer> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getCanSpy");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, Integer.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Returns the name of the jsx3.gui.Menu instance to display (as a context menu) when a user
clicks on this object with the right button.
     */

    public void getMenu(org.directwebremoting.ui.Callback<String> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getMenu");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, String.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Sets the name of the jsx3.gui.Menu instance to display when a user
clicks on this object with the right button. The name is a pointer by-name to a JSX object in the same server.
     * @param strMenu name or id (jsxname or jsxid) of the context menu
     * @return this object
     */
    public jsx3.gui.Interactive setMenu(String strMenu)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setMenu", strMenu);
        ScriptSessions.addScript(script);
        return this;
    }

    /**
     * called by 'window.setTimeout()' to display the spyglass hover for a given object;
     * @param strHTML HTML/text to display in the spyglass; as the spyglass does not define a height/width, this content will
         have improved layout if it specifies a preferred width in its in-line-style or referenced-css rule.
     * @param intLeft use an integer to specify an on-screen location; otherwise, use a <code>jsx3.gui.Event</code> instance to have the system automatically calculate the x/y position.
     * @param intTop use an integer if <code>intLeft</code> also uses an integer. Otherwise, use null.
     */
    public void showSpy(String strHTML, jsx3.gui.Event intLeft, int intTop)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "showSpy", strHTML, intLeft, intTop);
        ScriptSessions.addScript(script);
    }

    /**
     * called by 'window.setTimeout()' to display the spyglass hover for a given object;
     * @param strHTML HTML/text to display in the spyglass; as the spyglass does not define a height/width, this content will
         have improved layout if it specifies a preferred width in its in-line-style or referenced-css rule.
     * @param intLeft use an integer to specify an on-screen location; otherwise, use a <code>jsx3.gui.Event</code> instance to have the system automatically calculate the x/y position.
     * @param intTop use an integer if <code>intLeft</code> also uses an integer. Otherwise, use null.
     */
    public void showSpy(String strHTML, int intLeft, int intTop)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "showSpy", strHTML, intLeft, intTop);
        ScriptSessions.addScript(script);
    }

    /**
     * Sets the CSS definition to apply to an HTML element when a spyglass is shown for that element
     * @param strCSS valid CSS. For example, text-decoration:underline;color:red;
     */
    public void setSpyStyles(String strCSS)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setSpyStyles", strCSS);
        ScriptSessions.addScript(script);
    }

    /**
     * Registers a hot key with this JSX model node. All keydown events that bubble up to this object
will be checked against the hot key. If an event matches, the callback function will execute and the event
bubble will be canceled.

If the four parameters vntKey, bShift, bControl, and bAlt
match a previously registered hot key, the previous hot key is clobbered by the new one. Only one hot key callback
function (the most recently registered) will be executed by a single keydown event.
     * @param vntCallback either a function, or the name of a method bound to this object.
   When a keydown event bubbles up to this object that matches the hot key created by this method, this function
   is called on this object. If this function returns <code>false</code> then this hot key will not cancel the
   key event. This parameter can also be an instance of <code>HotKey</code>, in which case all
   other parameters are ignored.
     * @param vntKey if this parameter is a String, the hot key matches that key (the keycode to match is
   determined by <code>HotKey.keyDownCharToCode()</code>). If it is an integer, the hot key will match that
   keycode value.
     * @param bShift if not <code>null</code> the shift key state of the keydown event must match this value
   to invoke the hot key.
     * @param bControl if not <code>null</code> the control key state of the keydown event must match this value
   to invoke the hot key.
     * @param bAlt if not <code>null</code> the alt key state of the keydown event must match this value
   to invoke the hot key.
     * @return the registered hot key.
     */

    public jsx3.gui.HotKey registerHotKey(org.directwebremoting.ui.CodeBlock vntCallback, String vntKey, boolean bShift, boolean bControl, boolean bAlt)
    {
        String extension = "registerHotKey(\"" + vntCallback + "\", \"" + vntKey + "\", \"" + bShift + "\", \"" + bControl + "\", \"" + bAlt + "\").";
        try
        {
            java.lang.reflect.Constructor<jsx3.gui.HotKey> ctor = jsx3.gui.HotKey.class.getConstructor(Context.class, String.class);
            return ctor.newInstance(this, extension);
        }
        catch (Exception ex)
        {
            throw new IllegalArgumentException("Unsupported type: " + jsx3.gui.HotKey.class.getName());
        }
    }


    /**
     * Registers a hot key with this JSX model node. All keydown events that bubble up to this object
will be checked against the hot key. If an event matches, the callback function will execute and the event
bubble will be canceled.

If the four parameters vntKey, bShift, bControl, and bAlt
match a previously registered hot key, the previous hot key is clobbered by the new one. Only one hot key callback
function (the most recently registered) will be executed by a single keydown event.
     * @param vntCallback either a function, or the name of a method bound to this object.
   When a keydown event bubbles up to this object that matches the hot key created by this method, this function
   is called on this object. If this function returns <code>false</code> then this hot key will not cancel the
   key event. This parameter can also be an instance of <code>HotKey</code>, in which case all
   other parameters are ignored.
     * @param vntKey if this parameter is a String, the hot key matches that key (the keycode to match is
   determined by <code>HotKey.keyDownCharToCode()</code>). If it is an integer, the hot key will match that
   keycode value.
     * @param bShift if not <code>null</code> the shift key state of the keydown event must match this value
   to invoke the hot key.
     * @param bControl if not <code>null</code> the control key state of the keydown event must match this value
   to invoke the hot key.
     * @param bAlt if not <code>null</code> the alt key state of the keydown event must match this value
   to invoke the hot key.
     * @return the registered hot key.
     */

    public jsx3.gui.HotKey registerHotKey(jsx3.gui.HotKey vntCallback, String vntKey, boolean bShift, boolean bControl, boolean bAlt)
    {
        String extension = "registerHotKey(\"" + vntCallback + "\", \"" + vntKey + "\", \"" + bShift + "\", \"" + bControl + "\", \"" + bAlt + "\").";
        try
        {
            java.lang.reflect.Constructor<jsx3.gui.HotKey> ctor = jsx3.gui.HotKey.class.getConstructor(Context.class, String.class);
            return ctor.newInstance(this, extension);
        }
        catch (Exception ex)
        {
            throw new IllegalArgumentException("Unsupported type: " + jsx3.gui.HotKey.class.getName());
        }
    }


    /**
     * Registers a hot key with this JSX model node. All keydown events that bubble up to this object
will be checked against the hot key. If an event matches, the callback function will execute and the event
bubble will be canceled.

If the four parameters vntKey, bShift, bControl, and bAlt
match a previously registered hot key, the previous hot key is clobbered by the new one. Only one hot key callback
function (the most recently registered) will be executed by a single keydown event.
     * @param vntCallback either a function, or the name of a method bound to this object.
   When a keydown event bubbles up to this object that matches the hot key created by this method, this function
   is called on this object. If this function returns <code>false</code> then this hot key will not cancel the
   key event. This parameter can also be an instance of <code>HotKey</code>, in which case all
   other parameters are ignored.
     * @param vntKey if this parameter is a String, the hot key matches that key (the keycode to match is
   determined by <code>HotKey.keyDownCharToCode()</code>). If it is an integer, the hot key will match that
   keycode value.
     * @param bShift if not <code>null</code> the shift key state of the keydown event must match this value
   to invoke the hot key.
     * @param bControl if not <code>null</code> the control key state of the keydown event must match this value
   to invoke the hot key.
     * @param bAlt if not <code>null</code> the alt key state of the keydown event must match this value
   to invoke the hot key.
     * @return the registered hot key.
     */

    public jsx3.gui.HotKey registerHotKey(jsx3.gui.HotKey vntCallback, int vntKey, boolean bShift, boolean bControl, boolean bAlt)
    {
        String extension = "registerHotKey(\"" + vntCallback + "\", \"" + vntKey + "\", \"" + bShift + "\", \"" + bControl + "\", \"" + bAlt + "\").";
        try
        {
            java.lang.reflect.Constructor<jsx3.gui.HotKey> ctor = jsx3.gui.HotKey.class.getConstructor(Context.class, String.class);
            return ctor.newInstance(this, extension);
        }
        catch (Exception ex)
        {
            throw new IllegalArgumentException("Unsupported type: " + jsx3.gui.HotKey.class.getName());
        }
    }


    /**
     * Registers a hot key with this JSX model node. All keydown events that bubble up to this object
will be checked against the hot key. If an event matches, the callback function will execute and the event
bubble will be canceled.

If the four parameters vntKey, bShift, bControl, and bAlt
match a previously registered hot key, the previous hot key is clobbered by the new one. Only one hot key callback
function (the most recently registered) will be executed by a single keydown event.
     * @param vntCallback either a function, or the name of a method bound to this object.
   When a keydown event bubbles up to this object that matches the hot key created by this method, this function
   is called on this object. If this function returns <code>false</code> then this hot key will not cancel the
   key event. This parameter can also be an instance of <code>HotKey</code>, in which case all
   other parameters are ignored.
     * @param vntKey if this parameter is a String, the hot key matches that key (the keycode to match is
   determined by <code>HotKey.keyDownCharToCode()</code>). If it is an integer, the hot key will match that
   keycode value.
     * @param bShift if not <code>null</code> the shift key state of the keydown event must match this value
   to invoke the hot key.
     * @param bControl if not <code>null</code> the control key state of the keydown event must match this value
   to invoke the hot key.
     * @param bAlt if not <code>null</code> the alt key state of the keydown event must match this value
   to invoke the hot key.
     * @return the registered hot key.
     */

    public jsx3.gui.HotKey registerHotKey(org.directwebremoting.ui.CodeBlock vntCallback, int vntKey, boolean bShift, boolean bControl, boolean bAlt)
    {
        String extension = "registerHotKey(\"" + vntCallback + "\", \"" + vntKey + "\", \"" + bShift + "\", \"" + bControl + "\", \"" + bAlt + "\").";
        try
        {
            java.lang.reflect.Constructor<jsx3.gui.HotKey> ctor = jsx3.gui.HotKey.class.getConstructor(Context.class, String.class);
            return ctor.newInstance(this, extension);
        }
        catch (Exception ex)
        {
            throw new IllegalArgumentException("Unsupported type: " + jsx3.gui.HotKey.class.getName());
        }
    }


    /**
     * Registers a hot key with this JSX model node. All keydown events that bubble up to this object
will be checked against the hot key. If an event matches, the callback function will execute and the event
bubble will be canceled.

If the four parameters vntKey, bShift, bControl, and bAlt
match a previously registered hot key, the previous hot key is clobbered by the new one. Only one hot key callback
function (the most recently registered) will be executed by a single keydown event.
     * @param vntCallback either a function, or the name of a method bound to this object.
   When a keydown event bubbles up to this object that matches the hot key created by this method, this function
   is called on this object. If this function returns <code>false</code> then this hot key will not cancel the
   key event. This parameter can also be an instance of <code>HotKey</code>, in which case all
   other parameters are ignored.
     * @param vntKey if this parameter is a String, the hot key matches that key (the keycode to match is
   determined by <code>HotKey.keyDownCharToCode()</code>). If it is an integer, the hot key will match that
   keycode value.
     * @param bShift if not <code>null</code> the shift key state of the keydown event must match this value
   to invoke the hot key.
     * @param bControl if not <code>null</code> the control key state of the keydown event must match this value
   to invoke the hot key.
     * @param bAlt if not <code>null</code> the alt key state of the keydown event must match this value
   to invoke the hot key.
     * @return the registered hot key.
     */

    public jsx3.gui.HotKey registerHotKey(String vntCallback, int vntKey, boolean bShift, boolean bControl, boolean bAlt)
    {
        String extension = "registerHotKey(\"" + vntCallback + "\", \"" + vntKey + "\", \"" + bShift + "\", \"" + bControl + "\", \"" + bAlt + "\").";
        try
        {
            java.lang.reflect.Constructor<jsx3.gui.HotKey> ctor = jsx3.gui.HotKey.class.getConstructor(Context.class, String.class);
            return ctor.newInstance(this, extension);
        }
        catch (Exception ex)
        {
            throw new IllegalArgumentException("Unsupported type: " + jsx3.gui.HotKey.class.getName());
        }
    }


    /**
     * Registers a hot key with this JSX model node. All keydown events that bubble up to this object
will be checked against the hot key. If an event matches, the callback function will execute and the event
bubble will be canceled.

If the four parameters vntKey, bShift, bControl, and bAlt
match a previously registered hot key, the previous hot key is clobbered by the new one. Only one hot key callback
function (the most recently registered) will be executed by a single keydown event.
     * @param vntCallback either a function, or the name of a method bound to this object.
   When a keydown event bubbles up to this object that matches the hot key created by this method, this function
   is called on this object. If this function returns <code>false</code> then this hot key will not cancel the
   key event. This parameter can also be an instance of <code>HotKey</code>, in which case all
   other parameters are ignored.
     * @param vntKey if this parameter is a String, the hot key matches that key (the keycode to match is
   determined by <code>HotKey.keyDownCharToCode()</code>). If it is an integer, the hot key will match that
   keycode value.
     * @param bShift if not <code>null</code> the shift key state of the keydown event must match this value
   to invoke the hot key.
     * @param bControl if not <code>null</code> the control key state of the keydown event must match this value
   to invoke the hot key.
     * @param bAlt if not <code>null</code> the alt key state of the keydown event must match this value
   to invoke the hot key.
     * @return the registered hot key.
     */

    public jsx3.gui.HotKey registerHotKey(String vntCallback, String vntKey, boolean bShift, boolean bControl, boolean bAlt)
    {
        String extension = "registerHotKey(\"" + vntCallback + "\", \"" + vntKey + "\", \"" + bShift + "\", \"" + bControl + "\", \"" + bAlt + "\").";
        try
        {
            java.lang.reflect.Constructor<jsx3.gui.HotKey> ctor = jsx3.gui.HotKey.class.getConstructor(Context.class, String.class);
            return ctor.newInstance(this, extension);
        }
        catch (Exception ex)
        {
            throw new IllegalArgumentException("Unsupported type: " + jsx3.gui.HotKey.class.getName());
        }
    }


}

