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
 * Allows for rendering a branch of the DOM of an application in a separate browser window.

Access instances of this class with the following methods in the jsx3.app.Server class:

  createAppWindow()
  getAppWindow()
  loadAppWindow()
 * @author Joe Walker [joe at getahead dot org]
 * @author DRAPGEN - Dwr Reverse Ajax Proxy GENerator
 */
public class Window extends jsx3.app.Model
{
    /**
     * All reverse ajax proxies need context to work from
     * @param context The script that got us to where we are now
     */
    public Window(Context context, String extension)
    {
        super(context, extension);
    }

    /**
     * The instance initializer.
     * @param strName a unique name for this window.
     */
    public Window(String strName)
    {
        super((Context) null, (String) null);
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall("new Window", strName);
        setInitScript(script);
    }


    /**
     * Event subject: published after this window has successfully opened.
     */
    public static final String DID_OPEN = "open";

    /**
     * Event subject: published just before this window will close.
     */
    public static final String WILL_CLOSE = "close";

    /**
     * Event subject: published after this window has received focus.
     */
    public static final String DID_FOCUS = "focus";

    /**
     * Event subject: published after this window has been resized via user interaction.
     */
    public static final String DID_RESIZE = "resize";

    /**
     * Event subject: published after this window's parent has closed.
     */
    public static final String PARENT_DID_CLOSE = "pclose";


    /**
     * Opens the browser window of this window instance. Depending on security settings and popup blockers,
this method may or may not actually open a window. The only safe way to determine whether the window
successfully opened is to register for the DID_OPEN event.
     * @param callback <code>true</code> if the window successfully opened (probably).
     */

    public void open(org.directwebremoting.ui.Callback<Boolean> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "open");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, Boolean.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Closes the browser window of this window instance.
     * @param callback <code>true</code> if the window successfully closed or <code>false</code> if it didn't close
   because of JavaScript security constraints or user interaction.
     */

    public void close(org.directwebremoting.ui.Callback<Boolean> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "close");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, Boolean.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Focuses the browser window of this window instance.
     */
    public void focus()
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "focus");
        ScriptSessions.addScript(script);
    }

    /**
     * Returns whether the browser window of this window instance is open.
     * @param callback <code>true</code> if the window is open.
     */

    public void isOpen(org.directwebremoting.ui.Callback<Boolean> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "isOpen");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, Boolean.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Returns whether the parent application window of this window instance is open.
     * @param callback <code>true</code> if the parent window is open.
     */

    public void isParentOpen(org.directwebremoting.ui.Callback<Boolean> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "isParentOpen");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, Boolean.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Moves the browser window of this window instance to a position on the screen. The arguments specify the
offset from the parent application window. If the parent window is no longer open, this window will be moved
relative to the upper-left corner of the screen.
     * @param intOffsetLeft the left offset from the parent window.
     * @param intOffsetTop the top offset from the parent window.
     */
    public void moveTo(int intOffsetLeft, int intOffsetTop)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "moveTo", intOffsetLeft, intOffsetTop);
        ScriptSessions.addScript(script);
    }

    /**
     * Ensures that this window is at least partially visible on the computer screen.
     */
    public void constrainToScreen()
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "constrainToScreen");
        ScriptSessions.addScript(script);
    }

    /**
     * Returns the current x-coordinate screen position of this browser window relative to the parent application window.
If the parent window is no longer open, this method returns the position relative to the upper-left
corner of the screen.
     */

    public void getOffsetLeft(org.directwebremoting.ui.Callback<Integer> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getOffsetLeft");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, Integer.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Returns the current y-coordinate screen position of this browser window relative to the parent application window.
If the parent window is no longer open, this method returns the position relative to the upper-left
corner of the screen.
     */

    public void getOffsetTop(org.directwebremoting.ui.Callback<Integer> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getOffsetTop");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, Integer.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Returns the first DOM child of this window object. If no child exists, this method creates a root block, adds it
to the DOM, and returns it. A window will only render its first DOM child.
     */

    public jsx3.gui.Block getRootBlock()
    {
        String extension = "getRootBlock().";
        try
        {
            java.lang.reflect.Constructor<jsx3.gui.Block> ctor = jsx3.gui.Block.class.getConstructor(Context.class, String.class);
            return ctor.newInstance(this, extension);
        }
        catch (Exception ex)
        {
            throw new IllegalArgumentException("Unsupported type: " + jsx3.gui.Block.class.getName());
        }
    }

    /**
     * Returns the first DOM child of this window object. If no child exists, this method creates a root block, adds it
to the DOM, and returns it. A window will only render its first DOM child.
     * @param returnType The expected return type
     */

    public <T> T getRootBlock(Class<T> returnType)
    {
        String extension = "getRootBlock().";
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
     * Repaints the root block of this window.
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
     * Returns the inner (visible) width of this window. This does not include the border and padding that the
browser may render around the window content.
     */

    public void getWidth(org.directwebremoting.ui.Callback<Integer> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getWidth");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, Integer.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Sets the inner (visible) width of this window. If the window is currently open, the window will be resized
immediately.
     * @param intWidth the inner width of the window in pixels.
     */
    public void setWidth(int intWidth)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setWidth", intWidth);
        ScriptSessions.addScript(script);
    }

    /**
     * Returns the inner (visible) height of this window. This does not include the border and padding that the
browser may render around the window content.
     */

    public void getHeight(org.directwebremoting.ui.Callback<Integer> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getHeight");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, Integer.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Sets the inner (visible) height of this window. If the window is currently open, the window will be resized
immediately.
     * @param intHeight the inner height of the window in pixels.
     */
    public void setHeight(int intHeight)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setHeight", intHeight);
        ScriptSessions.addScript(script);
    }

    /**
     * Returns whether this window is resizable via user interaction. The value returned by this method will reflect
the last value passed to setResizable() and therefore may not truly reflect the current state of the
browser window.
     * @param callback <code>jsx3.Boolean.TRUE</code> or <code>jsx3.Boolean.FALSE</code>.
     */

    public void isResizable(org.directwebremoting.ui.Callback<Integer> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "isResizable");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, Integer.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Sets whether this window is resizable via user interaction. This method will not affect a currently-open window.
     * @param bResizable
     */
    public void setResizable(boolean bResizable)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setResizable", bResizable);
        ScriptSessions.addScript(script);
    }

    /**
     * Returns whether this window will show scroll bars if the content outgrows the window. The value returned by
this method will reflect the last value passed to setScrollable() and therefore may not truly
reflect the current state of the browser window.
     * @param callback <code>jsx3.Boolean.TRUE</code> or <code>jsx3.Boolean.FALSE</code>.
     */

    public void isScrollable(org.directwebremoting.ui.Callback<Integer> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "isScrollable");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, Integer.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Sets whether this window will show scroll bars if the content outgrows the window. This method will not affect a
currently-open window.
     * @param bScrollable
     */
    public void setScrollable(boolean bScrollable)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setScrollable", bScrollable);
        ScriptSessions.addScript(script);
    }

    /**
     * Returns whether this window is "dependent." Dependent windows close automatically when their parents close. If
a window is not dependent, it will stay open after the parent window closes. Note that the parent window contains
all the JavaScript code and so it is very likely that interacting with a window after the parent has closed
will raise errors.
     * @param callback <code>jsx3.Boolean.TRUE</code> or <code>jsx3.Boolean.FALSE</code>.
     */

    public void isDependent(org.directwebremoting.ui.Callback<Integer> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "isDependent");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, Integer.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Sets whether this window is "dependent." This method not affect a currently-open window.
     * @param bDependent
     */
    public void setDependent(boolean bDependent)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setDependent", bDependent);
        ScriptSessions.addScript(script);
    }

    /**
     * Returns the title of this window.
     */

    public void getTitle(org.directwebremoting.ui.Callback<String> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getTitle");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, String.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Sets the title of the window. The title is displayed in the title bar of the browser window. If the window is
currently open, the title will be updated immediately.
     * @param strTitle the title of the window.
     */
    public void setTitle(String strTitle)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setTitle", strTitle);
        ScriptSessions.addScript(script);
    }

}

