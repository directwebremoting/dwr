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
 * Native browser event wrapper.
 * @author Joe Walker [joe at getahead dot org]
 * @author DRAPGEN - Dwr Reverse Ajax Proxy GENerator
 */
public class Event extends jsx3.lang.Object
{
    /**
     * All reverse ajax proxies need context to work from
     * @param context The script that got us to where we are now
     */
    public Event(Context context, String extension)
    {
        super(context, extension);
    }


    /**
     * The browser native event type beforeunload.
     */
    public static final String BEFOREUNLOAD = "beforeunload";

    /**
     * The browser native event type blur.
     */
    public static final String BLUR = "blur";

    /**
     * The browser native event type change.
     */
    public static final String CHANGE = "change";

    /**
     * The browser native event type click.
     */
    public static final String CLICK = "click";

    /**
     * The browser native event type dblclick.
     */
    public static final String DOUBLECLICK = "dblclick";

    /**
     * The browser native event type error.
     */
    public static final String ERROR = "error";

    /**
     * The browser native event type focus.
     */
    public static final String FOCUS = "focus";

    /**
     * The browser native event type keydown.
     */
    public static final String KEYDOWN = "keydown";

    /**
     * The browser native event type keypress.
     */
    public static final String KEYPRESS = "keypress";

    /**
     * The browser native event type keyup.
     */
    public static final String KEYUP = "keyup";

    /**
     * The browser native event type load.
     */
    public static final String LOAD = "load";

    /**
     * The browser native event type mousedown.
     */
    public static final String MOUSEDOWN = "mousedown";

    /**
     * The browser native event type mousemove.
     */
    public static final String MOUSEMOVE = "mousemove";

    /**
     * The browser native event type mouseout.
     */
    public static final String MOUSEOUT = "mouseout";

    /**
     * The browser native event type mouseover.
     */
    public static final String MOUSEOVER = "mouseover";

    /**
     * The browser native event type mouseup.
     */
    public static final String MOUSEUP = "mouseup";

    /**
     * The browser native event type mousewheel.
     */
    public static final String MOUSEWHEEL = "mousewheel";

    /**
     * The browser native event type unload.
     */
    public static final String UNLOAD = "unload";

    /**
     * The browser native event type resize.
     */
    public static final String RESIZE = "resize";

    /**
     * The browser native key code for the Alt key.
     */
    public static final int KEY_ALT = 18;

    /**
     * The browser native key code for the down arrow key.
     */
    public static final int KEY_ARROW_DOWN = 40;

    /**
     * The browser native key code for the left arrow key.
     */
    public static final int KEY_ARROW_LEFT = 37;

    /**
     * The browser native key code for the right arrow key.
     */
    public static final int KEY_ARROW_RIGHT = 39;

    /**
     * The browser native key code for the up arrow key.
     */
    public static final int KEY_ARROW_UP = 38;

    /**
     * The browser native key code for the Backspace key.
     */
    public static final int KEY_BACKSPACE = 8;

    /**
     * The browser native key code for the Ctrl key.
     */
    public static final int KEY_CONTROL = 17;

    /**
     * The browser native key code for the Delete key.
     */
    public static final int KEY_DELETE = 46;

    /**
     * The browser native key code for the End key.
     */
    public static final int KEY_END = 35;

    /**
     * The browser native key code for the Enter key.
     */
    public static final int KEY_ENTER = 13;

    /**
     * The browser native key code for the Esc key.
     */
    public static final int KEY_ESCAPE = 27;

    /**
     * The browser native key code for the Home key.
     */
    public static final int KEY_HOME = 36;

    /**
     * The browser native key code for the Insert key.
     */
    public static final int KEY_INSERT = 45;

    /**
     * The browser native key code for the Meta key.
     */
    public static final int KEY_META = 224;

    /**
     * The browser native key code for the Page Down key.
     */
    public static final int KEY_PAGE_DOWN = 34;

    /**
     * The browser native key code for the Page Up key.
     */
    public static final int KEY_PAGE_UP = 33;

    /**
     * The browser native key code for the Shift key.
     */
    public static final int KEY_SHIFT = 16;

    /**
     * The browser native key code for the space bar key.
     */
    public static final int KEY_SPACE = 32;

    /**
     * The browser native key code for the Tab key.
     */
    public static final int KEY_TAB = 9;

    /**
     * The browser native key code for the 0 key.
     */
    public static final int KEY_0 = 48;

    /**
     * The browser native key code for the 9 key.
     */
    public static final int KEY_9 = 57;

    /**
     * The browser native key code for the A key.
     */
    public static final int KEY_A = 65;

    /**
     * The browser native key code for the Z key.
     */
    public static final int KEY_Z = 90;

    /**
     * The browser native key code for the number pad 0 key.
     */
    public static final int KEY_NP0 = 96;

    /**
     * The browser native key code for the number pad 9 key.
     */
    public static final int KEY_NP9 = 105;

    /**
     * The browser native key code for the number pad division (/) key.
     */
    public static final int KEY_NPDIV = 111;

    /**
     * The browser native key code for the number pad multiply (*) key.
     */
    public static final int KEY_NPMUL = 106;

    /**
     * The browser native key code for the number pad subtract (-) key.
     */
    public static final int KEY_NPSUB = 109;

    /**
     * The browser native key code for the number pad addition (+) key.
     */
    public static final int KEY_NPADD = 107;

    /**
     * The browser native key code for the number pad decimal (.) key.
     */
    public static final int KEY_NPDEC = 110;

    /**
     * The browser native key code for the F1 key.
     */
    public static final int KEY_F1 = 112;

    /**
     * The browser native key code for the F15 key.
     */
    public static final int KEY_F15 = 126;


    /**
     * Subscribes an event handler to events of type strEventId that bubble all the way up to the browser window.
     * @param strEventId the event type, e.g. <code>jsx3.gui.Event.CLICK</code>.
     * @param objHandler
     * @param objFunction
     */
    public void subscribe(String strEventId, java.lang.Object objHandler, java.lang.Object objFunction)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "subscribe", strEventId, objHandler, objFunction);
        ScriptSessions.addScript(script);
    }

    /**
     * Unsubscribes an event handler from events of type strEventId that bubble all the way up to the browser window.
     * @param strEventId the event type, e.g. <code>jsx3.gui.Event.CLICK</code>.
     * @param objHandler
     */
    public void unsubscribe(String strEventId, java.lang.Object objHandler)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "unsubscribe", strEventId, objHandler);
        ScriptSessions.addScript(script);
    }

    /**
     * Unsubscribes all event handlers from a events of type strEventId that bubble all the way up to the browser window.
     * @param strEventId the event type, e.g. <code>jsx3.gui.Event.CLICK</code>.
     */
    public void unsubscribeAll(String strEventId)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "unsubscribeAll", strEventId);
        ScriptSessions.addScript(script);
    }

    /**
     * Returns the type of event, e.g. mousedown, click, etc.
     * @param callback event type
     */

    public void getType(org.directwebremoting.ui.Callback<String> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getType");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, String.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Returns handle to the HTML element acted upon (click, mousedown, etc).
     * @param callback HTML object
     */

    public void srcElement(org.directwebremoting.ui.Callback<String> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "srcElement");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, String.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Returns handle to the HTML element that was moused over (onmouseover).
     * @param callback HTML object
     */

    public void toElement(org.directwebremoting.ui.Callback<String> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "toElement");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, String.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Returns handle to the HTML element that was moused away from (onmouseout).
     * @param callback HTML object
     */

    public void fromElement(org.directwebremoting.ui.Callback<String> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "fromElement");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, String.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Returns integer representing the key code of the key just pressed/keyed-down.
     * @param callback keycode
     */

    public void keyCode(org.directwebremoting.ui.Callback<Integer> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "keyCode");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, Integer.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Returns the clientX property for the event (where it occurred on-screen).
     * @param callback pixel position
     */

    public void clientX(org.directwebremoting.ui.Callback<Integer> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "clientX");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, Integer.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Returns the clientY property for the event (where it occurred on-screen).
     * @param callback pixel position
     */

    public void clientY(org.directwebremoting.ui.Callback<Integer> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "clientY");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, Integer.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Returns the actual position in the browser from the left edge for where the event occurred.
     * @param callback pixel position
     */

    public void getTrueX(org.directwebremoting.ui.Callback<Integer> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getTrueX");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, Integer.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Returns the actual position in the browser from the top edge for where the event occurred.
     * @param callback pixel position
     */

    public void getTrueY(org.directwebremoting.ui.Callback<Integer> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getTrueY");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, Integer.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Returns true if the shift key was pressed.
     */

    public void shiftKey(org.directwebremoting.ui.Callback<Boolean> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "shiftKey");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, Boolean.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Returns true the ctrl key was pressed.
     */

    public void ctrlKey(org.directwebremoting.ui.Callback<Boolean> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "ctrlKey");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, Boolean.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Returns true if the alt key was pressed.
     */

    public void altKey(org.directwebremoting.ui.Callback<Boolean> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "altKey");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, Boolean.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Returns true if the enter key was pressed.
     */

    public void enterKey(org.directwebremoting.ui.Callback<Boolean> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "enterKey");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, Boolean.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Returns true if the space bar was pressed.
     */

    public void spaceKey(org.directwebremoting.ui.Callback<Boolean> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "spaceKey");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, Boolean.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Returns true if the tab key was pressed.
     */

    public void tabKey(org.directwebremoting.ui.Callback<Boolean> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "tabKey");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, Boolean.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Returns true if the right-arrow key was pressed
     */

    public void rightArrow(org.directwebremoting.ui.Callback<Boolean> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "rightArrow");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, Boolean.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Returns true if the left-arrow key was pressed.
     */

    public void leftArrow(org.directwebremoting.ui.Callback<Boolean> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "leftArrow");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, Boolean.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Returns true if the up-arrow key was pressed.
     */

    public void upArrow(org.directwebremoting.ui.Callback<Boolean> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "upArrow");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, Boolean.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Returns true if the down-arrow key was pressed.
     */

    public void downArrow(org.directwebremoting.ui.Callback<Boolean> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "downArrow");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, Boolean.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Returns true if the delete key was pressed.
     */

    public void deleteKey(org.directwebremoting.ui.Callback<Boolean> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "deleteKey");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, Boolean.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Returns true if the backspace key was pressed.
     */

    public void backspaceKey(org.directwebremoting.ui.Callback<Boolean> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "backspaceKey");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, Boolean.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Returns true if the insert key was pressed.
     */

    public void insertKey(org.directwebremoting.ui.Callback<Boolean> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "insertKey");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, Boolean.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Returns true if the home key was pressed.
     */

    public void homeKey(org.directwebremoting.ui.Callback<Boolean> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "homeKey");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, Boolean.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Returns true if the end key was pressed.
     */

    public void endKey(org.directwebremoting.ui.Callback<Boolean> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "endKey");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, Boolean.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Returns true if the page-up key was pressed.
     */

    public void pageUpKey(org.directwebremoting.ui.Callback<Boolean> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "pageUpKey");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, Boolean.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Returns true if the page-down key was pressed.
     */

    public void pageDownKey(org.directwebremoting.ui.Callback<Boolean> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "pageDownKey");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, Boolean.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Returns true if the escape key was pressed.
     */

    public void escapeKey(org.directwebremoting.ui.Callback<Boolean> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "escapeKey");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, Boolean.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Cancels event bubbling for the event.
     */
    public void cancelBubble()
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "cancelBubble");
        ScriptSessions.addScript(script);
    }

    /**
     * Cancels the return value for the event.
     */
    public void cancelReturn()
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "cancelReturn");
        ScriptSessions.addScript(script);
    }

    /**
     * Cancels the key from firing by setting the keyCode to 0 (zero) for the event.
     */
    public void cancelKey()
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "cancelKey");
        ScriptSessions.addScript(script);
    }

    /**
     * Returns true if the left-mouse-button was clicked.
     */

    public void leftButton(org.directwebremoting.ui.Callback<Boolean> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "leftButton");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, Boolean.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Returns true if the right-mouse-button was clicked.
     */

    public void rightButton(org.directwebremoting.ui.Callback<Boolean> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "rightButton");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, Boolean.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Returns integer designating the mouse button clicked/moused-down/moused-up; 1 (left), 2 (right), and as supported.
     */

    public void button(org.directwebremoting.ui.Callback<Integer> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "button");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, Integer.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Sets the the return value for the event.
     * @param strReturn string message to set on the returnValue for the event
     */
    public void setReturn(String strReturn)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setReturn", strReturn);
        ScriptSessions.addScript(script);
    }

    /**
     * Whether one of the four arrow keys was pressed.
     */

    public void isArrowKey(org.directwebremoting.ui.Callback<Boolean> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "isArrowKey");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, Boolean.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Whether one of the 15 function keys was pressed.
     */

    public void isFunctionKey(org.directwebremoting.ui.Callback<Boolean> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "isFunctionKey");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, Boolean.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

}

