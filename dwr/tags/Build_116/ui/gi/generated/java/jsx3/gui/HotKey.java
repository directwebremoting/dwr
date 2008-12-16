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
 * Encapsulates a keydown event listener that is invoked by a certain combination of keys pressed simultaneously.
 * @author Joe Walker [joe at getahead dot org]
 * @author DRAPGEN - Dwr Reverse Ajax Proxy GENerator
 */
public class HotKey extends jsx3.lang.Object
{
    /**
     * All reverse ajax proxies need context to work from
     * @param context The script that got us to where we are now
     */
    public HotKey(Context context, String extension)
    {
        super(context, extension);
    }


    /**
     * Event type published just after a hot key is invoked.
     */
    public static final String WAS_INVOKED = "invoked";


    /**
     * 
     * @param strKey
     * @param fctCallback
     */

    public jsx3.gui.HotKey valueOf(String strKey, org.directwebremoting.ui.CodeBlock fctCallback)
    {
        String extension = "valueOf(\"" + strKey + "\", \"" + fctCallback + "\").";
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
     * Returns the keycode that this hot key responds to.
     */

    public void getKeyCode(org.directwebremoting.ui.Callback<Integer> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getKeyCode");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, Integer.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Returns whether this hot key should be invoked for the keydown event objEvent.
     * @param objEvent
     */

    public void isMatch(jsx3.gui.Event objEvent, org.directwebremoting.ui.Callback<Boolean> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "isMatch", objEvent);

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, Boolean.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Invokes this hot key by executing its callback function. This hot key also publishes a WAS_INVOKED
event through the event dispatcher interface.
     * @param objThis
     * @param arrArgs
     * @return this method returns whatever value was returned by the hot key callback function.
     */

    public jsx3.lang.Object invoke(jsx3.lang.Object objThis, Object[] arrArgs)
    {
        String extension = "invoke(\"" + objThis + "\", \"" + arrArgs + "\").";
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
     * Invokes this hot key by executing its callback function. This hot key also publishes a WAS_INVOKED
event through the event dispatcher interface.
     * @param objThis
     * @param arrArgs
     * @param returnType The expected return type
     * @return this method returns whatever value was returned by the hot key callback function.
     */

    public <T> T invoke(jsx3.lang.Object objThis, Object[] arrArgs, Class<T> returnType)
    {
        String extension = "invoke(\"" + objThis + "\", \"" + arrArgs + "\").";
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
     * Returns whether this hot key is enabled.
     */

    public void isEnabled(org.directwebremoting.ui.Callback<Boolean> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "isEnabled");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, Boolean.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Sets whether this hot key is enabled. Hot keys may be turned off temporarily by sending false to
this method.
     * @param bEnabled
     */
    public void setEnabled(boolean bEnabled)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setEnabled", bEnabled);
        ScriptSessions.addScript(script);
    }

    /**
     * Returns whether this hot key had been destoyed.
     */

    public void isDestroyed(org.directwebremoting.ui.Callback<Boolean> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "isDestroyed");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, Boolean.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Destroys this hot key. Once a hot key is destroyed it cannot be invoked again.
     */
    public void destroy()
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "destroy");
        ScriptSessions.addScript(script);
    }

    /**
     * Converts the string representation of a keyboard key to an integer keycode. This keycode will match the keycode
value of a jsx3.gui.Event of type keydown.

The following string representations are supported:

  alpha numeric characters: A-Z, a-z, 0-9

  the punctuation keys in the string: ";,./'[]\-=`"

  functions keys: F1-F15

  special keys: enter, esc, tab, del, space,
    backspace, up, down, left, right,
    insert, home, end, pgup, pgdn.
     * @param strChar the string representation of a key.
     * @param callback the keycode.
     */

    public void keyDownCharToCode(String strChar, org.directwebremoting.ui.Callback<Integer> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "keyDownCharToCode", strChar);

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, Integer.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Publishes an event to all subscribed objects.
     * @param objEvent the event, should have at least a field 'subject' that is the event id, another common field is 'target' (target will default to this instance)
     * @param callback the number of listeners to which the event was broadcast
     */

    public void publish(jsx3.lang.Object objEvent, org.directwebremoting.ui.Callback<Integer> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "publish", objEvent);

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, Integer.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Subscribes an object or function to a type of event published by this object.

As of version 3.4 a string value for objHandler is deprecated.
     * @param strEventId the event type(s).
     * @param objHandler if an object, the instance to notify of events (objFunction is required); if a string, the JSX id of the instance to notify of events (objFunction is required), must exist in the same Server; if a function, the function to call to notify of events (objFunction ignored)
     * @param objFunction if objHandler is a string or object then the function to call on that instance. either a function or a string that is the name of a method of the instance
     */
    public void subscribe(Object[] strEventId, org.directwebremoting.ui.CodeBlock objHandler, org.directwebremoting.ui.CodeBlock objFunction)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "subscribe", strEventId, objHandler, objFunction);
        ScriptSessions.addScript(script);
    }

    /**
     * Subscribes an object or function to a type of event published by this object.

As of version 3.4 a string value for objHandler is deprecated.
     * @param strEventId the event type(s).
     * @param objHandler if an object, the instance to notify of events (objFunction is required); if a string, the JSX id of the instance to notify of events (objFunction is required), must exist in the same Server; if a function, the function to call to notify of events (objFunction ignored)
     * @param objFunction if objHandler is a string or object then the function to call on that instance. either a function or a string that is the name of a method of the instance
     */
    public void subscribe(Object[] strEventId, jsx3.lang.Object objHandler, String objFunction)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "subscribe", strEventId, objHandler, objFunction);
        ScriptSessions.addScript(script);
    }

    /**
     * Subscribes an object or function to a type of event published by this object.

As of version 3.4 a string value for objHandler is deprecated.
     * @param strEventId the event type(s).
     * @param objHandler if an object, the instance to notify of events (objFunction is required); if a string, the JSX id of the instance to notify of events (objFunction is required), must exist in the same Server; if a function, the function to call to notify of events (objFunction ignored)
     * @param objFunction if objHandler is a string or object then the function to call on that instance. either a function or a string that is the name of a method of the instance
     */
    public void subscribe(String strEventId, String objHandler, org.directwebremoting.ui.CodeBlock objFunction)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "subscribe", strEventId, objHandler, objFunction);
        ScriptSessions.addScript(script);
    }

    /**
     * Subscribes an object or function to a type of event published by this object.

As of version 3.4 a string value for objHandler is deprecated.
     * @param strEventId the event type(s).
     * @param objHandler if an object, the instance to notify of events (objFunction is required); if a string, the JSX id of the instance to notify of events (objFunction is required), must exist in the same Server; if a function, the function to call to notify of events (objFunction ignored)
     * @param objFunction if objHandler is a string or object then the function to call on that instance. either a function or a string that is the name of a method of the instance
     */
    public void subscribe(Object[] strEventId, String objHandler, String objFunction)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "subscribe", strEventId, objHandler, objFunction);
        ScriptSessions.addScript(script);
    }

    /**
     * Subscribes an object or function to a type of event published by this object.

As of version 3.4 a string value for objHandler is deprecated.
     * @param strEventId the event type(s).
     * @param objHandler if an object, the instance to notify of events (objFunction is required); if a string, the JSX id of the instance to notify of events (objFunction is required), must exist in the same Server; if a function, the function to call to notify of events (objFunction ignored)
     * @param objFunction if objHandler is a string or object then the function to call on that instance. either a function or a string that is the name of a method of the instance
     */
    public void subscribe(String strEventId, org.directwebremoting.ui.CodeBlock objHandler, org.directwebremoting.ui.CodeBlock objFunction)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "subscribe", strEventId, objHandler, objFunction);
        ScriptSessions.addScript(script);
    }

    /**
     * Subscribes an object or function to a type of event published by this object.

As of version 3.4 a string value for objHandler is deprecated.
     * @param strEventId the event type(s).
     * @param objHandler if an object, the instance to notify of events (objFunction is required); if a string, the JSX id of the instance to notify of events (objFunction is required), must exist in the same Server; if a function, the function to call to notify of events (objFunction ignored)
     * @param objFunction if objHandler is a string or object then the function to call on that instance. either a function or a string that is the name of a method of the instance
     */
    public void subscribe(Object[] strEventId, String objHandler, org.directwebremoting.ui.CodeBlock objFunction)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "subscribe", strEventId, objHandler, objFunction);
        ScriptSessions.addScript(script);
    }

    /**
     * Subscribes an object or function to a type of event published by this object.

As of version 3.4 a string value for objHandler is deprecated.
     * @param strEventId the event type(s).
     * @param objHandler if an object, the instance to notify of events (objFunction is required); if a string, the JSX id of the instance to notify of events (objFunction is required), must exist in the same Server; if a function, the function to call to notify of events (objFunction ignored)
     * @param objFunction if objHandler is a string or object then the function to call on that instance. either a function or a string that is the name of a method of the instance
     */
    public void subscribe(String strEventId, jsx3.lang.Object objHandler, String objFunction)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "subscribe", strEventId, objHandler, objFunction);
        ScriptSessions.addScript(script);
    }

    /**
     * Subscribes an object or function to a type of event published by this object.

As of version 3.4 a string value for objHandler is deprecated.
     * @param strEventId the event type(s).
     * @param objHandler if an object, the instance to notify of events (objFunction is required); if a string, the JSX id of the instance to notify of events (objFunction is required), must exist in the same Server; if a function, the function to call to notify of events (objFunction ignored)
     * @param objFunction if objHandler is a string or object then the function to call on that instance. either a function or a string that is the name of a method of the instance
     */
    public void subscribe(String strEventId, jsx3.lang.Object objHandler, org.directwebremoting.ui.CodeBlock objFunction)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "subscribe", strEventId, objHandler, objFunction);
        ScriptSessions.addScript(script);
    }

    /**
     * Subscribes an object or function to a type of event published by this object.

As of version 3.4 a string value for objHandler is deprecated.
     * @param strEventId the event type(s).
     * @param objHandler if an object, the instance to notify of events (objFunction is required); if a string, the JSX id of the instance to notify of events (objFunction is required), must exist in the same Server; if a function, the function to call to notify of events (objFunction ignored)
     * @param objFunction if objHandler is a string or object then the function to call on that instance. either a function or a string that is the name of a method of the instance
     */
    public void subscribe(Object[] strEventId, jsx3.lang.Object objHandler, org.directwebremoting.ui.CodeBlock objFunction)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "subscribe", strEventId, objHandler, objFunction);
        ScriptSessions.addScript(script);
    }

    /**
     * Subscribes an object or function to a type of event published by this object.

As of version 3.4 a string value for objHandler is deprecated.
     * @param strEventId the event type(s).
     * @param objHandler if an object, the instance to notify of events (objFunction is required); if a string, the JSX id of the instance to notify of events (objFunction is required), must exist in the same Server; if a function, the function to call to notify of events (objFunction ignored)
     * @param objFunction if objHandler is a string or object then the function to call on that instance. either a function or a string that is the name of a method of the instance
     */
    public void subscribe(String strEventId, org.directwebremoting.ui.CodeBlock objHandler, String objFunction)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "subscribe", strEventId, objHandler, objFunction);
        ScriptSessions.addScript(script);
    }

    /**
     * Subscribes an object or function to a type of event published by this object.

As of version 3.4 a string value for objHandler is deprecated.
     * @param strEventId the event type(s).
     * @param objHandler if an object, the instance to notify of events (objFunction is required); if a string, the JSX id of the instance to notify of events (objFunction is required), must exist in the same Server; if a function, the function to call to notify of events (objFunction ignored)
     * @param objFunction if objHandler is a string or object then the function to call on that instance. either a function or a string that is the name of a method of the instance
     */
    public void subscribe(String strEventId, String objHandler, String objFunction)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "subscribe", strEventId, objHandler, objFunction);
        ScriptSessions.addScript(script);
    }

    /**
     * Subscribes an object or function to a type of event published by this object.

As of version 3.4 a string value for objHandler is deprecated.
     * @param strEventId the event type(s).
     * @param objHandler if an object, the instance to notify of events (objFunction is required); if a string, the JSX id of the instance to notify of events (objFunction is required), must exist in the same Server; if a function, the function to call to notify of events (objFunction ignored)
     * @param objFunction if objHandler is a string or object then the function to call on that instance. either a function or a string that is the name of a method of the instance
     */
    public void subscribe(Object[] strEventId, org.directwebremoting.ui.CodeBlock objHandler, String objFunction)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "subscribe", strEventId, objHandler, objFunction);
        ScriptSessions.addScript(script);
    }

    /**
     * Unsubscribe an object or function from an event published by this object.

As of version 3.4 a string value for objHandler is deprecated.
     * @param strEventId the event type(s).
     * @param objHandler the value of objHandler passed to subscribe
     */
    public void unsubscribe(String strEventId, org.directwebremoting.ui.CodeBlock objHandler)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "unsubscribe", strEventId, objHandler);
        ScriptSessions.addScript(script);
    }

    /**
     * Unsubscribe an object or function from an event published by this object.

As of version 3.4 a string value for objHandler is deprecated.
     * @param strEventId the event type(s).
     * @param objHandler the value of objHandler passed to subscribe
     */
    public void unsubscribe(Object[] strEventId, org.directwebremoting.ui.CodeBlock objHandler)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "unsubscribe", strEventId, objHandler);
        ScriptSessions.addScript(script);
    }

    /**
     * Unsubscribe an object or function from an event published by this object.

As of version 3.4 a string value for objHandler is deprecated.
     * @param strEventId the event type(s).
     * @param objHandler the value of objHandler passed to subscribe
     */
    public void unsubscribe(String strEventId, jsx3.lang.Object objHandler)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "unsubscribe", strEventId, objHandler);
        ScriptSessions.addScript(script);
    }

    /**
     * Unsubscribe an object or function from an event published by this object.

As of version 3.4 a string value for objHandler is deprecated.
     * @param strEventId the event type(s).
     * @param objHandler the value of objHandler passed to subscribe
     */
    public void unsubscribe(Object[] strEventId, jsx3.lang.Object objHandler)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "unsubscribe", strEventId, objHandler);
        ScriptSessions.addScript(script);
    }

    /**
     * Unsubscribe an object or function from an event published by this object.

As of version 3.4 a string value for objHandler is deprecated.
     * @param strEventId the event type(s).
     * @param objHandler the value of objHandler passed to subscribe
     */
    public void unsubscribe(Object[] strEventId, String objHandler)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "unsubscribe", strEventId, objHandler);
        ScriptSessions.addScript(script);
    }

    /**
     * Unsubscribe an object or function from an event published by this object.

As of version 3.4 a string value for objHandler is deprecated.
     * @param strEventId the event type(s).
     * @param objHandler the value of objHandler passed to subscribe
     */
    public void unsubscribe(String strEventId, String objHandler)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "unsubscribe", strEventId, objHandler);
        ScriptSessions.addScript(script);
    }

    /**
     * Unsubscribes all subscribed objects to a type of event published by this object.
     * @param strEventId the event type
     */
    public void unsubscribeAll(String strEventId)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "unsubscribeAll", strEventId);
        ScriptSessions.addScript(script);
    }

}

