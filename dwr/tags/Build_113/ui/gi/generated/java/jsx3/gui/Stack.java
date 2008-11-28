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
 * This class is equivalent to a tab, but uses the stack metaphor; like a tab, it has one child—a block for its content; a jsx3.gui.Stack instance should only be contained by a jsx3.gui.StackGroup instance for proper rendering.
 * @author Joe Walker [joe at getahead dot org]
 * @author DRAPGEN - Dwr Reverse Ajax Proxy GENerator
 */
public class Stack extends jsx3.gui.Block
{
    /**
     * All reverse ajax proxies need context to work from
     * @param context The script that got us to where we are now
     */
    public Stack(Context context, String extension)
    {
        super(context, extension);
    }

    /**
     * instance initializer
     * @param strName unique name distinguishing this object from all other JSX GUI objects in the JSX application
     * @param strCaption if != null,  will be set as the text property (label) for the stack
     */
    public Stack(String strName, String strCaption)
    {
        super((Context) null, (String) null);
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall("new Stack", strName, strCaption);
        setInitScript(script);
    }


    /**
     * 0 (default)
     */
    public static final int ORIENTATIONV = 0;

    /**
     * 1
     */
    public static final int ORIENTATIONH = 1;

    /**
     * #aaccff
     */
    public static final String ACTIVECOLOR = "#aaaafe";

    /**
     * #e8e8f5
     */
    public static final String INACTIVECOLOR = "#aaaacb";

    /**
     * #ffffff
     */
    public static final String CHILDBGCOLOR = "#ffffff";

    /**
     * 
     */
    public static final String BACKGROUND = null;


    /**
     * Makes this stack the visible (expanded) stack in the parent stack group.
     */
    public void doShow()
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "doShow");
        ScriptSessions.addScript(script);
    }

    /**
     * Returns the child of this stack that will be painted as the content of this stack. This implementation
returns the first child that is not a menu or a toolbar button.
     */

    public jsx3.app.Model getContentChild()
    {
        String extension = "getContentChild().";
        try
        {
            java.lang.reflect.Constructor<jsx3.app.Model> ctor = jsx3.app.Model.class.getConstructor(Context.class, String.class);
            return ctor.newInstance(this, extension);
        }
        catch (Exception ex)
        {
            throw new IllegalArgumentException("Unsupported type: " + jsx3.app.Model.class.getName());
        }
    }

    /**
     * Returns the child of this stack that will be painted as the content of this stack. This implementation
returns the first child that is not a menu or a toolbar button.
     * @param returnType The expected return type
     */

    public <T> T getContentChild(Class<T> returnType)
    {
        String extension = "getContentChild().";
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
     * Returns valid CSS property value, (i.e., red, #ffffff) when caption bar for stack is moused over. Default: jsx3.gui.Stack.ACTIVECOLOR
     * @param callback valid CSS property value, (i.e., red, #ffffff)
     */

    public void getActiveColor(org.directwebremoting.ui.Callback<String> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getActiveColor");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, String.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Sets valid CSS property value, (i.e., red, #ffffff) when caption bar for stack is moused over;
           returns reference to self to facilitate method chaining
     * @param strActiveColor valid CSS property value, (i.e., red, #ffffff)
     * @return this object
     */
    public jsx3.gui.Stack setActiveColor(String strActiveColor)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setActiveColor", strActiveColor);
        ScriptSessions.addScript(script);
        return this;
    }

    /**
     * Returns valid CSS property value, (i.e., red, #ffffff) when caption bar for stack is moused out. Default: jsx3.gui.Stack.INACTIVECOLOR
     * @param callback valid CSS property value, (i.e., red, #ffffff)
     */

    public void getInactiveColor(org.directwebremoting.ui.Callback<String> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getInactiveColor");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, String.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Sets valid CSS property value, (i.e., red, #ffffff) when caption bar for stack is moused out;
           returns reference to self to facilitate method chaining
     * @param strInactiveColor valid CSS property value, (i.e., red, #ffffff)
     * @return this object
     */
    public jsx3.gui.Stack setInactiveColor(String strInactiveColor)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setInactiveColor", strInactiveColor);
        ScriptSessions.addScript(script);
        return this;
    }

    /**
     * Returns whether or not this stack is the active (expanded) stack
     */

    public void isFront(org.directwebremoting.ui.Callback<Boolean> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "isFront");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, Boolean.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

}

