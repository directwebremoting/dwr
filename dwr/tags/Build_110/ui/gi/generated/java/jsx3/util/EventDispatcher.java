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
package jsx3.util;

import org.directwebremoting.ScriptBuffer;
import org.directwebremoting.ScriptSessions;
import org.directwebremoting.io.Context;

/**
 * An Event Dispatcher mixin interface, adds the ability to register event listeners and dispatch events
to those listeners.

(Deprecated) Classes that implement this mixin must also implement a method getServer(), which returns the server in
which to look for JSX ids.
 * @author Joe Walker [joe at getahead dot org]
 * @author DRAPGEN - Dwr Reverse Ajax Proxy GENerator
 */
public class EventDispatcher extends jsx3.lang.Object
{
    /**
     * All reverse ajax proxies need context to work from
     * @param context The script that got us to where we are now
     */
    public EventDispatcher(Context context, String extension)
    {
        super(context, extension);
    }


    /**
     * 
     */
    public static final java.lang.Object SUBJECT_ALL = "*";


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
    public void subscribe(Object[] strEventId, org.directwebremoting.ui.CodeBlock objHandler, String objFunction)
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
    public void subscribe(Object[] strEventId, org.directwebremoting.ui.CodeBlock objHandler, org.directwebremoting.ui.CodeBlock objFunction)
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

}

