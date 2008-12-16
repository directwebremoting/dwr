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
 * GUI utility class that provides a way to display HTML content on-screen in an HTML equivalent of a heavyweight container.
Instances of this class are often used to display menu lists, select lists, spyglass, and focus-rectangles.
An instance of this class cannot be serialized, it is merely a run-time construct similar to an alert or input box.
 * @author Joe Walker [joe at getahead dot org]
 * @author DRAPGEN - Dwr Reverse Ajax Proxy GENerator
 */
public class Heavyweight extends jsx3.lang.Object
{
    /**
     * All reverse ajax proxies need context to work from
     * @param context The script that got us to where we are now
     */
    public Heavyweight(Context context, String extension)
    {
        super(context, extension);
    }

    /**
     * instance initializer
     * @param strId id to identify this HW instance among all others; this id will be used by both jsx3.gui.Heavyweight (to index it in the hash) and by the browser as the HTML tag's "id" attribute. If no ID is passed, a unique ID will be assigned by the system and is available by calling, [object].getId();
     * @param objOwner
     */
    public Heavyweight(String strId, jsx3.gui.Painted objOwner)
    {
        super((Context) null, (String) null);
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall("new Heavyweight", strId, objOwner);
        setInitScript(script);
    }


    /**
     * 32000
     */
    public static final int DEFAULTZINDEX = 32000;


    /**
     * Returns the instance of the heavyweight object with the given ID; to get the on-screen HTML instance call: jsx3.gui.Heavyweight.GO([id]).getRendered()
     * @param strId unique ID for the heavyweight instance
     */

    public jsx3.gui.Heavyweight GO(String strId)
    {
        String extension = "GO(\"" + strId + "\").";
        try
        {
            java.lang.reflect.Constructor<jsx3.gui.Heavyweight> ctor = jsx3.gui.Heavyweight.class.getConstructor(Context.class, String.class);
            return ctor.newInstance(this, extension);
        }
        catch (Exception ex)
        {
            throw new IllegalArgumentException("Unsupported type: " + jsx3.gui.Heavyweight.class.getName());
        }
    }


    /**
     * Sets the text/HTML for the control to be displayed on-screen; returns reference to self to facilitate method chaining;
     * @param bDisplay true if null; if true, the heavyweight container is positioned and displayed immediately; if false, the container is painted on-screen, but its CSS 'visibility' property is set to 'hidden', allowing the developer to adjust as needed (via 2-pass, etc) before actually displaying;
     */
    public void show(boolean bDisplay)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "show", bDisplay);
        ScriptSessions.addScript(script);
    }

    /**
     * can be called if show() has been called; resets the ratio (width/total) of the VIEW to be that of [object].getRatio()
     */
    public void applyRatio()
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "applyRatio");
        ScriptSessions.addScript(script);
    }

    /**
     * can be called if show() has been called; allows an existing HW window to re-apply its rules (used for complex layouts requiring a multi-pass)
     * @param strAxis character (string) representing whether the rule is for the X or Y axis. Rememeber to capitalize!
     * @param intSize
     */
    public void applyRules(String strAxis, int intSize)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "applyRules", strAxis, intSize);
        ScriptSessions.addScript(script);
    }

    /**
     * destorys the on-screen VIEW for the HW container; Hide() only affects the VIEW; this is not the same as setting visibility to "hidden", which doesn't actually destroy the VIEW
     */
    public void hide()
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "hide");
        ScriptSessions.addScript(script);
    }

    /**
     * destroy's the on-screen VIEW for the HW container AND removes any reference to the instance from the hash; Destroy() affects the MODEL and the VIEW
     */
    public void destroy()
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "destroy");
        ScriptSessions.addScript(script);
    }

    /**
     * Returns handle/reference to the Heavyweight Object's on-screen counterpart—basically a handle to a DHTML SPAN;
     * @param objGUI optional argument improves efficiency if provided.
     * @param callback Browser-Native DHTML object
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
     * Returns handle/reference to the Heavyweight Object's on-screen counterpart—basically a handle to a DHTML SPAN;
     * @param objGUI optional argument improves efficiency if provided.
     * @param callback Browser-Native DHTML object
     */

    public void getRendered(String objGUI, org.directwebremoting.ui.Callback<String> callback)
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
     * Returns the unique id for this heavyweight instance
     */

    public void getId(org.directwebremoting.ui.Callback<String> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getId");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, String.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Returns the HTML content to display inside the HW instance on-screen
     */

    public void getHTML(org.directwebremoting.ui.Callback<String> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getHTML");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, String.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Sets the HTML content to display inside the HW instance on-screen; returns ref to self
     * @param strHTML HTML
     * @param bRepaint
     * @return this
     */
    public jsx3.gui.Heavyweight setHTML(String strHTML, boolean bRepaint)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setHTML", strHTML, bRepaint);
        ScriptSessions.addScript(script);
        return this;
    }

    /**
     * Returns an object reference to the Browser Element parent to be used; if none specified, the browser BODY will be used
     */

    public void getDomParent(org.directwebremoting.ui.Callback<String> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getDomParent");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, String.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Sets an object reference to the Browser Element parent to be used; if none specified, the browser BODY will be used.
Note that this method must be called before setting any point rules for the hW instance, so those functions know the true origin from which to calculate left/top positions; returns ref to self
     * @param objGUI HTML element in the browser
     * @return this
     */
    public jsx3.gui.Heavyweight setDomParent(String objGUI)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setDomParent", objGUI);
        ScriptSessions.addScript(script);
        return this;
    }

    /**
     * Returns the ratio (a decimal between .01 and .99) to multiply the "Rise + Run" by. When applied by the 'show'
command during a double-pass, a width to height ratio can be established to provide a consistent L&F for
the text content.  For example, a value of .8 would mean that the width of the heavyweight container would
represent 80% and the height would represent 20% of the total perimiter
     */

    public void getRatio(org.directwebremoting.ui.Callback<Integer> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getRatio");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, Integer.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Sets the ratio (a decimal between .01 and .99) to multiply the "Rise + Run" by. When applied by the 'show' command during a double-pass, a width to height ratio can be established to provide a consistent L&F for the text content.  For example, a value of .8 would mean that the width of the heavyweight container would represent 80% and the height would represent 20% of the total perimiter;
           returns a ref to self
     * @param vntRatio any value between .01 and .99
     * @return this
     */
    public jsx3.gui.Heavyweight setRatio(Integer vntRatio)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setRatio", vntRatio);
        ScriptSessions.addScript(script);
        return this;
    }

    /**
     * Returns the overflow property for CONTENTS of the HW container; it is assumed that anytime a perfect fit cannot occur that the content will have its overflow property set to 'auto' unless specified otherwise
     * @param callback [jsx3.gui.Block.OVERFLOWSCROLL, jsx3.gui.Block.OVERFLOWHIDDEN, jsx3.gui.Block.OVERFLOWEXPAND]
     */

    public void getOverflow(org.directwebremoting.ui.Callback<String> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getOverflow");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, String.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Sets the overflow property for CONTENTS of the HW container; it is assumed that anytime a perfect fit cannot occur that the content will have its overflow property set to 'auto' unless specified otherwise
           returns reference to self to facilitate method chaining;
     * @param strOverflow [jsx3.gui.Block.OVERFLOWSCROLL, jsx3.gui.Block.OVERFLOWHIDDEN, jsx3.gui.Block.OVERFLOWEXPAND]
     * @return this object
     */
    public jsx3.gui.Heavyweight setOverflow(String strOverflow)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setOverflow", strOverflow);
        ScriptSessions.addScript(script);
        return this;
    }

    /**
     * if the HW instance has an on-screen VIEW, this method can be used to toggle its visibility; it has no effect on the MODEL; it is most commonly used when "[object].show(false);" is called, allowing the developer to manually adjust layout before actually showing the HW instance.
           returns a ref to self for method chaining
     * @param strVisibility [jsx3.gui.Block.VISIBILITYVISIBLE, jsx3.gui.Block.VISIBILITYHIDDEN]
     * @return this object
     */
    public jsx3.gui.Heavyweight setVisibility(String strVisibility)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setVisibility", strVisibility);
        ScriptSessions.addScript(script);
        return this;
    }

    /**
     * Returns the z-index property; assumes jsx3.gui.Heavyweight.DEFAULTZINDEX if none supplied
     */

    public void getZIndex(org.directwebremoting.ui.Callback<Integer> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getZIndex");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, Integer.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Sets the CSS z-index for the object; if null, is passed, jsx3.gui.Heavyweight.DEFAULTZINDEX will be used as the default value
     * @param intZIndex z-index value
     */
    public void setZIndex(int intZIndex)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setZIndex", intZIndex);
        ScriptSessions.addScript(script);
    }

    /**
     * Returns the CSS width property (in pixels); if this value is set, it is assumed that the Heavyweight container will not have its width lessened to fit on-screen.
     * @param callback width (in pixels)
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
     * Sets the CSS width property (in pixels); if this value is set, it is assumed that the Heavyweight container will not have its width lessened to fit on-screen.
     * @param intWidth width (in pixels)
     */
    public void setWidth(int intWidth)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setWidth", intWidth);
        ScriptSessions.addScript(script);
    }

    /**
     * Returns the CSS height property (in pixels); if this value is set, it is assumed that the Heavyweight container will not have its height lessened to fit on-screen.
     * @param callback height (in pixels)
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
     * Sets the CSS height property (in pixels); if this value is set, it is assumed that the Heavyweight container will not have its height lessened to fit on-screen.
           returns reference to self to facilitate method chaining;
     * @param intHeight height (in pixels)
     * @return this object
     */
    public jsx3.gui.Heavyweight setHeight(int intHeight)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setHeight", intHeight);
        ScriptSessions.addScript(script);
        return this;
    }

    /**
     * adds a POSITION RULE ruleset (X value) (a simple structure/hash) to the array of position rules; Note that POSITION RULE objects are used by the show() method to determine the best possible location for a heavyweight item
     * @param objAnchor Either an event, or an on-screen HTML element
     * @param strAnchorPoint REQUIRED if @objAnchor is an HTML element; when used, defines one of  the valid 9 compass points: 4 primary: (N, S, E, W); 4 secondary: (NE, SE, SW, NW); and origin: (O). Note that it
           is from this point (on @objAnchor) that the heavyweight item will try to position itself
     * @param strPoint Defines one of  the valid 9 compass points: 4 primary: (N, S, E, W); 4 secondary: (NE, SE, SW, NW); and origin: (O). Note that it
           is from this point (on the Heavyweight instance) that the heavyweight item will try to position itself
     * @param intOff offset (in pixels) by which to nudge the horizontal placement of the HW instance before displaying (useful for submenus, for example, where their left has a -10px offset to overlay the parent menu item)
     * @return this object (this)
     */

    public jsx3.gui.Heavyweight addXRule(jsx3.gui.Event objAnchor, String strAnchorPoint, String strPoint, int intOff)
    {
        String extension = "addXRule(\"" + objAnchor + "\", \"" + strAnchorPoint + "\", \"" + strPoint + "\", \"" + intOff + "\").";
        try
        {
            java.lang.reflect.Constructor<jsx3.gui.Heavyweight> ctor = jsx3.gui.Heavyweight.class.getConstructor(Context.class, String.class);
            return ctor.newInstance(this, extension);
        }
        catch (Exception ex)
        {
            throw new IllegalArgumentException("Unsupported type: " + jsx3.gui.Heavyweight.class.getName());
        }
    }


    /**
     * adds a POSITION RULE ruleset (X value) (a simple structure/hash) to the array of position rules; Note that POSITION RULE objects are used by the show() method to determine the best possible location for a heavyweight item
     * @param objAnchor Either an event, or an on-screen HTML element
     * @param strAnchorPoint REQUIRED if @objAnchor is an HTML element; when used, defines one of  the valid 9 compass points: 4 primary: (N, S, E, W); 4 secondary: (NE, SE, SW, NW); and origin: (O). Note that it
           is from this point (on @objAnchor) that the heavyweight item will try to position itself
     * @param strPoint Defines one of  the valid 9 compass points: 4 primary: (N, S, E, W); 4 secondary: (NE, SE, SW, NW); and origin: (O). Note that it
           is from this point (on the Heavyweight instance) that the heavyweight item will try to position itself
     * @param intOff offset (in pixels) by which to nudge the horizontal placement of the HW instance before displaying (useful for submenus, for example, where their left has a -10px offset to overlay the parent menu item)
     * @return this object (this)
     */

    public jsx3.gui.Heavyweight addXRule(jsx3.lang.Object objAnchor, String strAnchorPoint, String strPoint, int intOff)
    {
        String extension = "addXRule(\"" + objAnchor + "\", \"" + strAnchorPoint + "\", \"" + strPoint + "\", \"" + intOff + "\").";
        try
        {
            java.lang.reflect.Constructor<jsx3.gui.Heavyweight> ctor = jsx3.gui.Heavyweight.class.getConstructor(Context.class, String.class);
            return ctor.newInstance(this, extension);
        }
        catch (Exception ex)
        {
            throw new IllegalArgumentException("Unsupported type: " + jsx3.gui.Heavyweight.class.getName());
        }
    }


    /**
     * adds a POSITION RULE ruleset (Y value) (a simple structure/hash) to the array of position rules; Note that POSITION RULE objects are used by the show() method to determine the best possible location for a heavyweight item
     * @param objAnchor Either an event or an on-screen HTML element
     * @param strAnchorPoint REQUIRED if @objAnchor is an HTML element; when used, defines one of  the valid 9 compass points: 4 primary: (N, S, E, W); 4 secondary: (NE, SE, SW, NW); and origin: (O). Note that it
           is from this point (on @objAnchor) that the heavyweight item will try to position itself
     * @param strPoint Defines one of  the valid 9 compass points: 4 primary: (N, S, E, W); 4 secondary: (NE, SE, SW, NW); and origin: (O). Note that it
           is from this point (on the Heavyweight instance) that the heavyweight item will try to position itself
     * @param intOff offset (in pixels) by which to nudge the vertical placement of the HW instance before displaying (useful for submenus, for example, where their left has a -10px offset to overlay the parent menu item)
     * @return this object (this)
     */

    public jsx3.gui.Heavyweight addYRule(jsx3.lang.Object objAnchor, String strAnchorPoint, String strPoint, int intOff)
    {
        String extension = "addYRule(\"" + objAnchor + "\", \"" + strAnchorPoint + "\", \"" + strPoint + "\", \"" + intOff + "\").";
        try
        {
            java.lang.reflect.Constructor<jsx3.gui.Heavyweight> ctor = jsx3.gui.Heavyweight.class.getConstructor(Context.class, String.class);
            return ctor.newInstance(this, extension);
        }
        catch (Exception ex)
        {
            throw new IllegalArgumentException("Unsupported type: " + jsx3.gui.Heavyweight.class.getName());
        }
    }


    /**
     * adds a POSITION RULE ruleset (Y value) (a simple structure/hash) to the array of position rules; Note that POSITION RULE objects are used by the show() method to determine the best possible location for a heavyweight item
     * @param objAnchor Either an event or an on-screen HTML element
     * @param strAnchorPoint REQUIRED if @objAnchor is an HTML element; when used, defines one of  the valid 9 compass points: 4 primary: (N, S, E, W); 4 secondary: (NE, SE, SW, NW); and origin: (O). Note that it
           is from this point (on @objAnchor) that the heavyweight item will try to position itself
     * @param strPoint Defines one of  the valid 9 compass points: 4 primary: (N, S, E, W); 4 secondary: (NE, SE, SW, NW); and origin: (O). Note that it
           is from this point (on the Heavyweight instance) that the heavyweight item will try to position itself
     * @param intOff offset (in pixels) by which to nudge the vertical placement of the HW instance before displaying (useful for submenus, for example, where their left has a -10px offset to overlay the parent menu item)
     * @return this object (this)
     */

    public jsx3.gui.Heavyweight addYRule(jsx3.gui.Event objAnchor, String strAnchorPoint, String strPoint, int intOff)
    {
        String extension = "addYRule(\"" + objAnchor + "\", \"" + strAnchorPoint + "\", \"" + strPoint + "\", \"" + intOff + "\").";
        try
        {
            java.lang.reflect.Constructor<jsx3.gui.Heavyweight> ctor = jsx3.gui.Heavyweight.class.getConstructor(Context.class, String.class);
            return ctor.newInstance(this, extension);
        }
        catch (Exception ex)
        {
            throw new IllegalArgumentException("Unsupported type: " + jsx3.gui.Heavyweight.class.getName());
        }
    }


    /**
     * adds a POSITION RULE ruleset (a simple structure/hash) to the array of position rules; Note that POSITION RULE objects are used by the show() method to determine the best possible location for a heavyweight item
     * @param intPixel left position (in pixels) for the anchorpoint the heavyweight instance will try to layout in context of
     * @param strPoint Defines one of  the valid 9 compass points: 4 primary: (N, S, E, W); 4 secondary: (NE, SE, SW, NW); and origin: (O). Note that it
           is from this point (on the Heavyweight instance) that the heavyweight item will try to position itself
     * @param intOff offset (in pixels) by which to nudge the vertical placement of the HW instance before displaying (useful for submenus, for example, where their left has a -10px offset to overlay the parent menu item)
     * @param strAxis character (string) representing whether the rule is for the X or Y axis. Rememeber to capitalize!
     * @return this object (this)
     */

    public jsx3.gui.Heavyweight addRule(int intPixel, String strPoint, int intOff, String strAxis)
    {
        String extension = "addRule(\"" + intPixel + "\", \"" + strPoint + "\", \"" + intOff + "\", \"" + strAxis + "\").";
        try
        {
            java.lang.reflect.Constructor<jsx3.gui.Heavyweight> ctor = jsx3.gui.Heavyweight.class.getConstructor(Context.class, String.class);
            return ctor.newInstance(this, extension);
        }
        catch (Exception ex)
        {
            throw new IllegalArgumentException("Unsupported type: " + jsx3.gui.Heavyweight.class.getName());
        }
    }


    /**
     * Returns a POSITION RULE object at the given index; Note that POSITION RULE objects are JavaScript objects that implement the following 3 properties: _pixel (the on-screen point around which to pivot/place), _offset (amount to nudge the placement), _point (compass direction)
     * @param intIndex the index (in rank order of execution) of the POSITION RULEing rule set to apply (it is assumed that at least one POSITION RULE ruleset exists)
     * @param strAxis character (string) representing whether the rule is for the X or Y axis. Rememeber to capitalize!
     */

    public void getPositionRule(int intIndex, String strAxis, org.directwebremoting.ui.Callback<String> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getPositionRule", intIndex, strAxis);

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, String.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Returns a JavaScript object array (hash).  This hash contains the Y rules and the X rules for positioning the object
     */

    public jsx3.lang.Object getPositionRules()
    {
        String extension = "getPositionRules().";
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
     * Returns a JavaScript object array (hash).  This hash contains the Y rules and the X rules for positioning the object
     * @param returnType The expected return type
     */

    public <T> T getPositionRules(Class<T> returnType)
    {
        String extension = "getPositionRules().";
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
     * Returns a JavaScript object with properties:  X,Y (Left and Top); relating to the 4 primary (N, S, E, W), 4 secondary (NE, SE, SW, NW), and origin (O) compass positions for O
     * @param objGUI GUI object in the browser DOM (typically an HTML element such as a DIV or SPAN) for which to provide the X,Y for
     * @param strPoint a character denoting one of the valid 9 compass points: 4 primary: (N, S, E, W); 4 secondary: (NE, SE, SW, NW); and origin: (O)
     */
    public void getPoint(int objGUI, String strPoint)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "getPoint", objGUI, strPoint);
        ScriptSessions.addScript(script);
    }

    /**
     * Returns a JavaScript object with properties:  X,Y (Left and Top); relating to the 4 primary (N, S, E, W), 4 secondary (NE, SE, SW, NW), and origin (O) compass positions for O
     * @param objGUI GUI object in the browser DOM (typically an HTML element such as a DIV or SPAN) for which to provide the X,Y for
     * @param strPoint a character denoting one of the valid 9 compass points: 4 primary: (N, S, E, W); 4 secondary: (NE, SE, SW, NW); and origin: (O)
     */
    public void getPoint(jsx3.gui.Block objGUI, String strPoint)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "getPoint", objGUI, strPoint);
        ScriptSessions.addScript(script);
    }

    /**
     * Returns a JavaScript object with properties:  X,Y (Left and Top); relating to the 4 primary (N, S, E, W), 4 secondary (NE, SE, SW, NW), and origin (O) compass positions for O
     * @param objGUI GUI object in the browser DOM (typically an HTML element such as a DIV or SPAN) for which to provide the X,Y for
     * @param strPoint a character denoting one of the valid 9 compass points: 4 primary: (N, S, E, W); 4 secondary: (NE, SE, SW, NW); and origin: (O)
     */
    public void getPoint(String objGUI, String strPoint)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "getPoint", objGUI, strPoint);
        ScriptSessions.addScript(script);
    }

}

