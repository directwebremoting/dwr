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
 * This class provides a container-based, object-oriented approach to creating static html objects (basically this class creates "DIV" objects).  This class is useful for creating objects as simple as 'labels' that can be placed anywhere on the screen.  The advantage to using this class instead of trying to insert static html in the html window is that it allows the given HTML string to be managed by the 'container-management' strategy employed by the JSX Architecture
 * @author Joe Walker [joe at getahead dot org]
 * @author DRAPGEN - Dwr Reverse Ajax Proxy GENerator
 */
public class Block extends jsx3.gui.Painted
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
     * instance initializer
     * @param strName unique name distinguishing this object from all other JSX GUI objects in the JSX application
     * @param vntLeft either a number (i.e, 12, 30, etc) or a number and a unit value (i.e., "25%", "36pt", etc); if a number is passed, pixels will be the assumed unit when painted to screen
     * @param vntTop either a number (i.e, 12, 30, etc) or a number and a unit value (i.e., "25%", "36pt", etc); if a number is passed, pixels will be the assumed unit when painted to screen
     * @param vntWidth either a number (i.e, 12, 30, etc) or a number and a unit value (i.e., "25%", "36pt", etc); if a number is passed, pixels will be the assumed unit when painted to screen
     * @param vntHeight either a number (i.e, 12, 30, etc) or a number and a unit value (i.e., "25%", "36pt", etc); if a number is passed, pixels will be the assumed unit when painted to screen
     * @param strHTML Text/HTML markup to place in the jsx3.gui.Block instance
     */
    public Block(String strName, int vntLeft, int vntTop, String vntWidth, int vntHeight, String strHTML)
    {
        super((Context) null, (String) null);
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall("new Block", strName, vntLeft, vntTop, vntWidth, vntHeight, strHTML);
        setInitScript(script);
    }

    /**
     * instance initializer
     * @param strName unique name distinguishing this object from all other JSX GUI objects in the JSX application
     * @param vntLeft either a number (i.e, 12, 30, etc) or a number and a unit value (i.e., "25%", "36pt", etc); if a number is passed, pixels will be the assumed unit when painted to screen
     * @param vntTop either a number (i.e, 12, 30, etc) or a number and a unit value (i.e., "25%", "36pt", etc); if a number is passed, pixels will be the assumed unit when painted to screen
     * @param vntWidth either a number (i.e, 12, 30, etc) or a number and a unit value (i.e., "25%", "36pt", etc); if a number is passed, pixels will be the assumed unit when painted to screen
     * @param vntHeight either a number (i.e, 12, 30, etc) or a number and a unit value (i.e., "25%", "36pt", etc); if a number is passed, pixels will be the assumed unit when painted to screen
     * @param strHTML Text/HTML markup to place in the jsx3.gui.Block instance
     */
    public Block(String strName, String vntLeft, String vntTop, int vntWidth, String vntHeight, String strHTML)
    {
        super((Context) null, (String) null);
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall("new Block", strName, vntLeft, vntTop, vntWidth, vntHeight, strHTML);
        setInitScript(script);
    }

    /**
     * instance initializer
     * @param strName unique name distinguishing this object from all other JSX GUI objects in the JSX application
     * @param vntLeft either a number (i.e, 12, 30, etc) or a number and a unit value (i.e., "25%", "36pt", etc); if a number is passed, pixels will be the assumed unit when painted to screen
     * @param vntTop either a number (i.e, 12, 30, etc) or a number and a unit value (i.e., "25%", "36pt", etc); if a number is passed, pixels will be the assumed unit when painted to screen
     * @param vntWidth either a number (i.e, 12, 30, etc) or a number and a unit value (i.e., "25%", "36pt", etc); if a number is passed, pixels will be the assumed unit when painted to screen
     * @param vntHeight either a number (i.e, 12, 30, etc) or a number and a unit value (i.e., "25%", "36pt", etc); if a number is passed, pixels will be the assumed unit when painted to screen
     * @param strHTML Text/HTML markup to place in the jsx3.gui.Block instance
     */
    public Block(String strName, int vntLeft, String vntTop, int vntWidth, String vntHeight, String strHTML)
    {
        super((Context) null, (String) null);
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall("new Block", strName, vntLeft, vntTop, vntWidth, vntHeight, strHTML);
        setInitScript(script);
    }

    /**
     * instance initializer
     * @param strName unique name distinguishing this object from all other JSX GUI objects in the JSX application
     * @param vntLeft either a number (i.e, 12, 30, etc) or a number and a unit value (i.e., "25%", "36pt", etc); if a number is passed, pixels will be the assumed unit when painted to screen
     * @param vntTop either a number (i.e, 12, 30, etc) or a number and a unit value (i.e., "25%", "36pt", etc); if a number is passed, pixels will be the assumed unit when painted to screen
     * @param vntWidth either a number (i.e, 12, 30, etc) or a number and a unit value (i.e., "25%", "36pt", etc); if a number is passed, pixels will be the assumed unit when painted to screen
     * @param vntHeight either a number (i.e, 12, 30, etc) or a number and a unit value (i.e., "25%", "36pt", etc); if a number is passed, pixels will be the assumed unit when painted to screen
     * @param strHTML Text/HTML markup to place in the jsx3.gui.Block instance
     */
    public Block(String strName, int vntLeft, String vntTop, int vntWidth, int vntHeight, String strHTML)
    {
        super((Context) null, (String) null);
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall("new Block", strName, vntLeft, vntTop, vntWidth, vntHeight, strHTML);
        setInitScript(script);
    }

    /**
     * instance initializer
     * @param strName unique name distinguishing this object from all other JSX GUI objects in the JSX application
     * @param vntLeft either a number (i.e, 12, 30, etc) or a number and a unit value (i.e., "25%", "36pt", etc); if a number is passed, pixels will be the assumed unit when painted to screen
     * @param vntTop either a number (i.e, 12, 30, etc) or a number and a unit value (i.e., "25%", "36pt", etc); if a number is passed, pixels will be the assumed unit when painted to screen
     * @param vntWidth either a number (i.e, 12, 30, etc) or a number and a unit value (i.e., "25%", "36pt", etc); if a number is passed, pixels will be the assumed unit when painted to screen
     * @param vntHeight either a number (i.e, 12, 30, etc) or a number and a unit value (i.e., "25%", "36pt", etc); if a number is passed, pixels will be the assumed unit when painted to screen
     * @param strHTML Text/HTML markup to place in the jsx3.gui.Block instance
     */
    public Block(String strName, String vntLeft, int vntTop, String vntWidth, int vntHeight, String strHTML)
    {
        super((Context) null, (String) null);
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall("new Block", strName, vntLeft, vntTop, vntWidth, vntHeight, strHTML);
        setInitScript(script);
    }

    /**
     * instance initializer
     * @param strName unique name distinguishing this object from all other JSX GUI objects in the JSX application
     * @param vntLeft either a number (i.e, 12, 30, etc) or a number and a unit value (i.e., "25%", "36pt", etc); if a number is passed, pixels will be the assumed unit when painted to screen
     * @param vntTop either a number (i.e, 12, 30, etc) or a number and a unit value (i.e., "25%", "36pt", etc); if a number is passed, pixels will be the assumed unit when painted to screen
     * @param vntWidth either a number (i.e, 12, 30, etc) or a number and a unit value (i.e., "25%", "36pt", etc); if a number is passed, pixels will be the assumed unit when painted to screen
     * @param vntHeight either a number (i.e, 12, 30, etc) or a number and a unit value (i.e., "25%", "36pt", etc); if a number is passed, pixels will be the assumed unit when painted to screen
     * @param strHTML Text/HTML markup to place in the jsx3.gui.Block instance
     */
    public Block(String strName, String vntLeft, int vntTop, String vntWidth, String vntHeight, String strHTML)
    {
        super((Context) null, (String) null);
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall("new Block", strName, vntLeft, vntTop, vntWidth, vntHeight, strHTML);
        setInitScript(script);
    }

    /**
     * instance initializer
     * @param strName unique name distinguishing this object from all other JSX GUI objects in the JSX application
     * @param vntLeft either a number (i.e, 12, 30, etc) or a number and a unit value (i.e., "25%", "36pt", etc); if a number is passed, pixels will be the assumed unit when painted to screen
     * @param vntTop either a number (i.e, 12, 30, etc) or a number and a unit value (i.e., "25%", "36pt", etc); if a number is passed, pixels will be the assumed unit when painted to screen
     * @param vntWidth either a number (i.e, 12, 30, etc) or a number and a unit value (i.e., "25%", "36pt", etc); if a number is passed, pixels will be the assumed unit when painted to screen
     * @param vntHeight either a number (i.e, 12, 30, etc) or a number and a unit value (i.e., "25%", "36pt", etc); if a number is passed, pixels will be the assumed unit when painted to screen
     * @param strHTML Text/HTML markup to place in the jsx3.gui.Block instance
     */
    public Block(String strName, String vntLeft, int vntTop, int vntWidth, int vntHeight, String strHTML)
    {
        super((Context) null, (String) null);
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall("new Block", strName, vntLeft, vntTop, vntWidth, vntHeight, strHTML);
        setInitScript(script);
    }

    /**
     * instance initializer
     * @param strName unique name distinguishing this object from all other JSX GUI objects in the JSX application
     * @param vntLeft either a number (i.e, 12, 30, etc) or a number and a unit value (i.e., "25%", "36pt", etc); if a number is passed, pixels will be the assumed unit when painted to screen
     * @param vntTop either a number (i.e, 12, 30, etc) or a number and a unit value (i.e., "25%", "36pt", etc); if a number is passed, pixels will be the assumed unit when painted to screen
     * @param vntWidth either a number (i.e, 12, 30, etc) or a number and a unit value (i.e., "25%", "36pt", etc); if a number is passed, pixels will be the assumed unit when painted to screen
     * @param vntHeight either a number (i.e, 12, 30, etc) or a number and a unit value (i.e., "25%", "36pt", etc); if a number is passed, pixels will be the assumed unit when painted to screen
     * @param strHTML Text/HTML markup to place in the jsx3.gui.Block instance
     */
    public Block(String strName, int vntLeft, String vntTop, String vntWidth, int vntHeight, String strHTML)
    {
        super((Context) null, (String) null);
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall("new Block", strName, vntLeft, vntTop, vntWidth, vntHeight, strHTML);
        setInitScript(script);
    }

    /**
     * instance initializer
     * @param strName unique name distinguishing this object from all other JSX GUI objects in the JSX application
     * @param vntLeft either a number (i.e, 12, 30, etc) or a number and a unit value (i.e., "25%", "36pt", etc); if a number is passed, pixels will be the assumed unit when painted to screen
     * @param vntTop either a number (i.e, 12, 30, etc) or a number and a unit value (i.e., "25%", "36pt", etc); if a number is passed, pixels will be the assumed unit when painted to screen
     * @param vntWidth either a number (i.e, 12, 30, etc) or a number and a unit value (i.e., "25%", "36pt", etc); if a number is passed, pixels will be the assumed unit when painted to screen
     * @param vntHeight either a number (i.e, 12, 30, etc) or a number and a unit value (i.e., "25%", "36pt", etc); if a number is passed, pixels will be the assumed unit when painted to screen
     * @param strHTML Text/HTML markup to place in the jsx3.gui.Block instance
     */
    public Block(String strName, int vntLeft, String vntTop, String vntWidth, String vntHeight, String strHTML)
    {
        super((Context) null, (String) null);
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall("new Block", strName, vntLeft, vntTop, vntWidth, vntHeight, strHTML);
        setInitScript(script);
    }

    /**
     * instance initializer
     * @param strName unique name distinguishing this object from all other JSX GUI objects in the JSX application
     * @param vntLeft either a number (i.e, 12, 30, etc) or a number and a unit value (i.e., "25%", "36pt", etc); if a number is passed, pixels will be the assumed unit when painted to screen
     * @param vntTop either a number (i.e, 12, 30, etc) or a number and a unit value (i.e., "25%", "36pt", etc); if a number is passed, pixels will be the assumed unit when painted to screen
     * @param vntWidth either a number (i.e, 12, 30, etc) or a number and a unit value (i.e., "25%", "36pt", etc); if a number is passed, pixels will be the assumed unit when painted to screen
     * @param vntHeight either a number (i.e, 12, 30, etc) or a number and a unit value (i.e., "25%", "36pt", etc); if a number is passed, pixels will be the assumed unit when painted to screen
     * @param strHTML Text/HTML markup to place in the jsx3.gui.Block instance
     */
    public Block(String strName, int vntLeft, int vntTop, int vntWidth, int vntHeight, String strHTML)
    {
        super((Context) null, (String) null);
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall("new Block", strName, vntLeft, vntTop, vntWidth, vntHeight, strHTML);
        setInitScript(script);
    }

    /**
     * instance initializer
     * @param strName unique name distinguishing this object from all other JSX GUI objects in the JSX application
     * @param vntLeft either a number (i.e, 12, 30, etc) or a number and a unit value (i.e., "25%", "36pt", etc); if a number is passed, pixels will be the assumed unit when painted to screen
     * @param vntTop either a number (i.e, 12, 30, etc) or a number and a unit value (i.e., "25%", "36pt", etc); if a number is passed, pixels will be the assumed unit when painted to screen
     * @param vntWidth either a number (i.e, 12, 30, etc) or a number and a unit value (i.e., "25%", "36pt", etc); if a number is passed, pixels will be the assumed unit when painted to screen
     * @param vntHeight either a number (i.e, 12, 30, etc) or a number and a unit value (i.e., "25%", "36pt", etc); if a number is passed, pixels will be the assumed unit when painted to screen
     * @param strHTML Text/HTML markup to place in the jsx3.gui.Block instance
     */
    public Block(String strName, String vntLeft, String vntTop, int vntWidth, int vntHeight, String strHTML)
    {
        super((Context) null, (String) null);
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall("new Block", strName, vntLeft, vntTop, vntWidth, vntHeight, strHTML);
        setInitScript(script);
    }

    /**
     * instance initializer
     * @param strName unique name distinguishing this object from all other JSX GUI objects in the JSX application
     * @param vntLeft either a number (i.e, 12, 30, etc) or a number and a unit value (i.e., "25%", "36pt", etc); if a number is passed, pixels will be the assumed unit when painted to screen
     * @param vntTop either a number (i.e, 12, 30, etc) or a number and a unit value (i.e., "25%", "36pt", etc); if a number is passed, pixels will be the assumed unit when painted to screen
     * @param vntWidth either a number (i.e, 12, 30, etc) or a number and a unit value (i.e., "25%", "36pt", etc); if a number is passed, pixels will be the assumed unit when painted to screen
     * @param vntHeight either a number (i.e, 12, 30, etc) or a number and a unit value (i.e., "25%", "36pt", etc); if a number is passed, pixels will be the assumed unit when painted to screen
     * @param strHTML Text/HTML markup to place in the jsx3.gui.Block instance
     */
    public Block(String strName, int vntLeft, int vntTop, int vntWidth, String vntHeight, String strHTML)
    {
        super((Context) null, (String) null);
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall("new Block", strName, vntLeft, vntTop, vntWidth, vntHeight, strHTML);
        setInitScript(script);
    }

    /**
     * instance initializer
     * @param strName unique name distinguishing this object from all other JSX GUI objects in the JSX application
     * @param vntLeft either a number (i.e, 12, 30, etc) or a number and a unit value (i.e., "25%", "36pt", etc); if a number is passed, pixels will be the assumed unit when painted to screen
     * @param vntTop either a number (i.e, 12, 30, etc) or a number and a unit value (i.e., "25%", "36pt", etc); if a number is passed, pixels will be the assumed unit when painted to screen
     * @param vntWidth either a number (i.e, 12, 30, etc) or a number and a unit value (i.e., "25%", "36pt", etc); if a number is passed, pixels will be the assumed unit when painted to screen
     * @param vntHeight either a number (i.e, 12, 30, etc) or a number and a unit value (i.e., "25%", "36pt", etc); if a number is passed, pixels will be the assumed unit when painted to screen
     * @param strHTML Text/HTML markup to place in the jsx3.gui.Block instance
     */
    public Block(String strName, String vntLeft, String vntTop, String vntWidth, int vntHeight, String strHTML)
    {
        super((Context) null, (String) null);
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall("new Block", strName, vntLeft, vntTop, vntWidth, vntHeight, strHTML);
        setInitScript(script);
    }

    /**
     * instance initializer
     * @param strName unique name distinguishing this object from all other JSX GUI objects in the JSX application
     * @param vntLeft either a number (i.e, 12, 30, etc) or a number and a unit value (i.e., "25%", "36pt", etc); if a number is passed, pixels will be the assumed unit when painted to screen
     * @param vntTop either a number (i.e, 12, 30, etc) or a number and a unit value (i.e., "25%", "36pt", etc); if a number is passed, pixels will be the assumed unit when painted to screen
     * @param vntWidth either a number (i.e, 12, 30, etc) or a number and a unit value (i.e., "25%", "36pt", etc); if a number is passed, pixels will be the assumed unit when painted to screen
     * @param vntHeight either a number (i.e, 12, 30, etc) or a number and a unit value (i.e., "25%", "36pt", etc); if a number is passed, pixels will be the assumed unit when painted to screen
     * @param strHTML Text/HTML markup to place in the jsx3.gui.Block instance
     */
    public Block(String strName, String vntLeft, int vntTop, int vntWidth, String vntHeight, String strHTML)
    {
        super((Context) null, (String) null);
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall("new Block", strName, vntLeft, vntTop, vntWidth, vntHeight, strHTML);
        setInitScript(script);
    }

    /**
     * instance initializer
     * @param strName unique name distinguishing this object from all other JSX GUI objects in the JSX application
     * @param vntLeft either a number (i.e, 12, 30, etc) or a number and a unit value (i.e., "25%", "36pt", etc); if a number is passed, pixels will be the assumed unit when painted to screen
     * @param vntTop either a number (i.e, 12, 30, etc) or a number and a unit value (i.e., "25%", "36pt", etc); if a number is passed, pixels will be the assumed unit when painted to screen
     * @param vntWidth either a number (i.e, 12, 30, etc) or a number and a unit value (i.e., "25%", "36pt", etc); if a number is passed, pixels will be the assumed unit when painted to screen
     * @param vntHeight either a number (i.e, 12, 30, etc) or a number and a unit value (i.e., "25%", "36pt", etc); if a number is passed, pixels will be the assumed unit when painted to screen
     * @param strHTML Text/HTML markup to place in the jsx3.gui.Block instance
     */
    public Block(String strName, String vntLeft, String vntTop, String vntWidth, String vntHeight, String strHTML)
    {
        super((Context) null, (String) null);
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall("new Block", strName, vntLeft, vntTop, vntWidth, vntHeight, strHTML);
        setInitScript(script);
    }

    /**
     * instance initializer
     * @param strName unique name distinguishing this object from all other JSX GUI objects in the JSX application
     * @param vntLeft either a number (i.e, 12, 30, etc) or a number and a unit value (i.e., "25%", "36pt", etc); if a number is passed, pixels will be the assumed unit when painted to screen
     * @param vntTop either a number (i.e, 12, 30, etc) or a number and a unit value (i.e., "25%", "36pt", etc); if a number is passed, pixels will be the assumed unit when painted to screen
     * @param vntWidth either a number (i.e, 12, 30, etc) or a number and a unit value (i.e., "25%", "36pt", etc); if a number is passed, pixels will be the assumed unit when painted to screen
     * @param vntHeight either a number (i.e, 12, 30, etc) or a number and a unit value (i.e., "25%", "36pt", etc); if a number is passed, pixels will be the assumed unit when painted to screen
     * @param strHTML Text/HTML markup to place in the jsx3.gui.Block instance
     */
    public Block(String strName, int vntLeft, int vntTop, String vntWidth, String vntHeight, String strHTML)
    {
        super((Context) null, (String) null);
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall("new Block", strName, vntLeft, vntTop, vntWidth, vntHeight, strHTML);
        setInitScript(script);
    }


    /**
     * 2
     */
    public static final int OVERFLOWHIDDEN = 2;

    /**
     * 3 (default)
     */
    public static final int OVERFLOWEXPAND = 3;

    /**
     * Verdana
     */
    public static final String DEFAULTFONTNAME = "Verdana";

    /**
     * #000000
     */
    public static final String DEFAULTCOLOR = "#000000";

    /**
     * &#160;
     */
    public static final String DEFAULTTEXT = "&#160;";

    /**
     * span
     */
    public static final String DEFAULTTAGNAME = "span";

    /**
     * bold
     */
    public static final String FONTBOLD = "bold";

    /**
     * normal (default)
     */
    public static final String FONTNORMAL = "normal";

    /**
     * [empty string] (default)
     */
    public static final String DISPLAYBLOCK = "";

    /**
     * none
     */
    public static final String DISPLAYNONE = "none";

    /**
     * [empty string] (default)
     */
    public static final String VISIBILITYVISIBLE = "";

    /**
     * hidden
     */
    public static final String VISIBILITYHIDDEN = "hidden";

    /**
     * left (default)
     */
    public static final String ALIGNLEFT = "left";

    /**
     * center
     */
    public static final String ALIGNCENTER = "center";

    /**
     * right
     */
    public static final String ALIGNRIGHT = "right";

    /**
     * 0
     */
    public static final int ABSOLUTE = 0;

    /**
     * 1 (default)
     */
    public static final int RELATIVE = 1;

    /**
     * JSX/images/spc.gif
     */
    public static final String SPACE = null;


    /**
     * Returns valid CSS property value, (e.g., red, #ffffff, rgb(255,0,0))
     * @param callback valid CSS property value, (e.g., red, #ffffff, rgb(255,0,0))
     */

    public void getBackgroundColor(org.directwebremoting.ui.Callback<String> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getBackgroundColor");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, String.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Sets valid CSS property value, (e.g., red, #ffffff, rgb(255,0,0));
           returns reference to self to facilitate method chaining;
     * @param strColor valid CSS property value, (e.g., red, #ffffff, rgb(255,0,0))
     * @param bRepaint if <code>true</code>, the view of this object is immediately updated, obviating the need to call <code>repaint()</code>.
     * @return this object
     */
    public jsx3.gui.Block setBackgroundColor(String strColor, boolean bRepaint)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setBackgroundColor", strColor, bRepaint);
        ScriptSessions.addScript(script);
        return this;
    }

    /**
     * Returns valid CSS property value for the background such as:  background-image:url(x.gif);  or background-image:url(x.gif);background-repeat:no-repeat;
     * @param callback valid CSS property for the background such as:  background-image:url(x.gif);  or background-image:url(x.gif);background-repeat:no-repeat;
     */

    public void getBackground(org.directwebremoting.ui.Callback<String> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getBackground");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, String.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Sets valid CSS property value for the background such as:  background-image:url(x.gif);  or background-image:url(x.gif);background-repeat:no-repeat;
           returns reference to self to facilitate method chaining;
     * @param strBG valid CSS property value for the background such as:  background-image:url(x.gif);  or background-image:url(x.gif);background-repeat:no-repeat;
     * @return this object
     */
    public jsx3.gui.Block setBackground(String strBG)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setBackground", strBG);
        ScriptSessions.addScript(script);
        return this;
    }

    /**
     * Returns CSS property value(s) for a border (border: solid 1px #000000)
     */

    public void getBorder(org.directwebremoting.ui.Callback<String> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getBorder");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, String.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Sets CSS property value(s) for a border (border: solid 1px #000000). Properties can be strung
together as in: border:solid 1px #989885;border-left-width:0px;
     * @param strCSS valid CSS property value for a border (border: solid 1px #000000)
     * @param bRecalc if true, the VIEW will be updated with requiring a repaint
     * @return this object
     */
    public jsx3.gui.Block setBorder(String strCSS, boolean bRecalc)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setBorder", strCSS, bRecalc);
        ScriptSessions.addScript(script);
        return this;
    }

    /**
     * Returns valid CSS property value, (e.g., red, #ffffff, rgb(255,0,0))
     * @param callback valid CSS property value, (e.g., red, #ffffff, rgb(255,0,0))
     */

    public void getColor(org.directwebremoting.ui.Callback<String> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getColor");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, String.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Sets valid CSS property value, (e.g., red, #ffffff, rgb(255,0,0));
           returns reference to self to facilitate method chaining;
     * @param strColor valid CSS property value, (e.g., red, #ffffff, rgb(255,0,0))
     * @param bRepaint if <code>true</code>, the view of this object is immediately updated, obviating the need to call <code>repaint()</code>.
     * @return this object
     */
    public jsx3.gui.Block setColor(String strColor, boolean bRepaint)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setColor", strColor, bRepaint);
        ScriptSessions.addScript(script);
        return this;
    }

    /**
     * Returns valid CSS property value, (e.g., default,wait,col-resize); if no value or an empty string, null is returned
     * @param callback valid CSS property value, (e.g., default,wait,col-resize)
     */

    public void getCursor(org.directwebremoting.ui.Callback<String> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getCursor");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, String.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Sets valid CSS property value, (e.g., default,wait,col-resize)
     * @param strCursor valid CSS property value, (e.g., default,wait,col-resize)
     * @param bRepaint if <code>true</code>, the view of this object is immediately updated, obviating the need to call <code>repaint()</code>.
     */
    public void setCursor(String strCursor, boolean bRepaint)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setCursor", strCursor, bRepaint);
        ScriptSessions.addScript(script);
    }

    /**
     * Returns CSS text to override the standard instance properties on the painted object.
     * @param callback CSS text
     */

    public void getCSSOverride(org.directwebremoting.ui.Callback<String> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getCSSOverride");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, String.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Sets CSS text to override the standard instance properties on the painted object. Convenience method for extending this object. CSS properties affecting layout, including border-width, padding, margin, width, and height
are strongly discouraged, as they may interfere with the framework's internal box models.
Since some controls are composited from multiple HTML elements, some styles may not cascade to nested elements.
Instance Properties are the preferred method for applying styles.
     * @param strCSS CSS text.  Fore example, <code>color:red;background-color:orange;</code>
     * @return this object
     */
    public jsx3.gui.Block setCSSOverride(String strCSS)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setCSSOverride", strCSS);
        ScriptSessions.addScript(script);
        return this;
    }

    /**
     * Returns the named CSS rule(s) to apply to the painted object.
     */

    public void getClassName(org.directwebremoting.ui.Callback<String> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getClassName");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, String.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Sets the named CSS rule(s) to apply to the painted object. Rules that specify border-width, padding, margin, width, and height are strongly discouraged.
Multiple rules may be specified, delimited with a space.  For example, label emphasis.
Since some controls are composited from multiple HTML elements, some rule styles may not cascade to nested elements.
Dynamic Properties are the preferred method for applying global styles.
     * @param strClassName CSS property name without the leading "."
     * @return this object
     */
    public jsx3.gui.Block setClassName(String strClassName)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setClassName", strClassName);
        ScriptSessions.addScript(script);
        return this;
    }

    /**
     * Returns the CSS display for the object; one of jsx3.gui.Block.DISPLAYNONE (display:none;) and jsx3.gui.Block.DISPLAYBLOCK (display:;)
     */

    public void getDisplay(org.directwebremoting.ui.Callback<String> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getDisplay");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, String.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Sets the display for the object. Note that although the framework uses CSS to apply this setting, the actual values that get set are determined by the system.
Only those parameters listed for @DISPLAY are supported as inputs to this function.
     * @param intDisplay one of jsx3.gui.Block.DISPLAYNONE (display:none;) and jsx3.gui.Block.DISPLAYBLOCK (display:;)
     * @param bRepaint if <code>true</code>, the view of this object is immediately updated, obviating the need to call <code>repaint()</code>.
     * @return this object
     */
    public jsx3.gui.Block setDisplay(String intDisplay, boolean bRepaint)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setDisplay", intDisplay, bRepaint);
        ScriptSessions.addScript(script);
        return this;
    }

    /**
     * Returns the CSS font-family for the object
     * @param callback valid CSS font-family property value
     */

    public void getFontName(org.directwebremoting.ui.Callback<String> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getFontName");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, String.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Sets the CSS font-family for the object;
           returns reference to self to facilitate method chaining;
     * @param strFontName valid CSS font-family property value
     * @return this object
     */
    public jsx3.gui.Block setFontName(String strFontName)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setFontName", strFontName);
        ScriptSessions.addScript(script);
        return this;
    }

    /**
     * Returns the CSS font-size for the object
     * @param callback font-size (in pixels)
     */

    public void getFontSize(org.directwebremoting.ui.Callback<Integer> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getFontSize");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, Integer.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Sets the CSS font-size for the object;
           returns reference to self to facilitate method chaining;
     * @param intPixelSize font-size (in pixels)
     * @return this object
     */
    public jsx3.gui.Block setFontSize(int intPixelSize)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setFontSize", intPixelSize);
        ScriptSessions.addScript(script);
        return this;
    }

    /**
     * Returns the CSS font-weight for the object ("bold" or "normal")
     * @param callback [jsx3.gui.Block.FONTBOLD. jsx3.gui.Block.FONTNORMAL]
     */

    public void getFontWeight(org.directwebremoting.ui.Callback<String> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getFontWeight");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, String.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Sets the CSS font-weight for the object ("bold" or "normal");
           returns reference to self to facilitate method chaining;
     * @param FONTWEIGHT [jsx3.gui.Block.FONTBOLD. jsx3.gui.Block.FONTNORMAL]
     * @return this object
     */
    public jsx3.gui.Block setFontWeight(String FONTWEIGHT)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setFontWeight", FONTWEIGHT);
        ScriptSessions.addScript(script);
        return this;
    }

    /**
     * Returns the height property of this object.
     * @param callback height.
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
     * Sets the height property of this object.
     * @param vntHeight the height as a non-negative integer or non-negative percentage. For example: 45%, 12.
     * @param bRepaint if <code>true</code>, the view of this object is immediately updated, obviating the need to call <code>repaint()</code>.
     * @return this object.
     */
    public jsx3.gui.Block setHeight(String vntHeight, boolean bRepaint)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setHeight", vntHeight, bRepaint);
        ScriptSessions.addScript(script);
        return this;
    }

    /**
     * Sets the height property of this object.
     * @param vntHeight the height as a non-negative integer or non-negative percentage. For example: 45%, 12.
     * @param bRepaint if <code>true</code>, the view of this object is immediately updated, obviating the need to call <code>repaint()</code>.
     * @return this object.
     */
    public jsx3.gui.Block setHeight(int vntHeight, boolean bRepaint)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setHeight", vntHeight, bRepaint);
        ScriptSessions.addScript(script);
        return this;
    }

    /**
     * Returns IE tab index for setting the tabIndex property for the on-screen DHTML for the object
     */

    public void getIndex(org.directwebremoting.ui.Callback<Integer> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getIndex");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, Integer.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Sets IE tab index for setting the tabIndex property for the on-screen DHTML for the object;
           returns reference to self to facilitate method chaining;
     * @param intIndex any value in the valid range of -32767 to 32767
     * @param bRepaint if <code>true</code>, the view of this object is immediately updated, obviating the need to call <code>repaint()</code>.
     * @return this object
     */
    public jsx3.gui.Block setIndex(int intIndex, boolean bRepaint)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setIndex", intIndex, bRepaint);
        ScriptSessions.addScript(script);
        return this;
    }

    /**
     * Returns the left property of this object.
     * @param callback left.
     */

    public void getLeft(org.directwebremoting.ui.Callback<Integer> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getLeft");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, Integer.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Sets the left property of this object. The left property specifies the horizontal offset of this object
from its parent and only applies if this object is absolutely positioned.
     * @param vntLeft the left value. Only numeric values and percentages are supported. For example: 25, -10, 20%.
     * @param bRepaint if @vntLeft is in integer (a number with no modifier) and @bRepaint is true, the object's on-screen VIEW is immediately updated to match its MODEL, obviating the need to call '[object].repaint()'
     * @return this object.
     */
    public jsx3.gui.Block setLeft(int vntLeft, boolean bRepaint)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setLeft", vntLeft, bRepaint);
        ScriptSessions.addScript(script);
        return this;
    }

    /**
     * Sets the left property of this object. The left property specifies the horizontal offset of this object
from its parent and only applies if this object is absolutely positioned.
     * @param vntLeft the left value. Only numeric values and percentages are supported. For example: 25, -10, 20%.
     * @param bRepaint if @vntLeft is in integer (a number with no modifier) and @bRepaint is true, the object's on-screen VIEW is immediately updated to match its MODEL, obviating the need to call '[object].repaint()'
     * @return this object.
     */
    public jsx3.gui.Block setLeft(String vntLeft, boolean bRepaint)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setLeft", vntLeft, bRepaint);
        ScriptSessions.addScript(script);
        return this;
    }

    /**
     * Set one to four dimensions at once. This operation is more efficient than calling more than one of
setLeft, setTop, etc. Any argument can be null to indicate not to update it.
     * @param left the new left value or an array containing all four new values
     * @param top the new top value
     * @param width the new width value
     * @param height the new height value
     * @param bRepaint whether to update the display of this object immediately. If <code>left</code> is
   an <code>Array</code> then this parameter is the second parameter passed to this method.
     */
    public void setDimensions(String left, int top, int width, int height, boolean bRepaint)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setDimensions", left, top, width, height, bRepaint);
        ScriptSessions.addScript(script);
    }

    /**
     * Set one to four dimensions at once. This operation is more efficient than calling more than one of
setLeft, setTop, etc. Any argument can be null to indicate not to update it.
     * @param left the new left value or an array containing all four new values
     * @param top the new top value
     * @param width the new width value
     * @param height the new height value
     * @param bRepaint whether to update the display of this object immediately. If <code>left</code> is
   an <code>Array</code> then this parameter is the second parameter passed to this method.
     */
    public void setDimensions(Object[] left, int top, int width, int height, boolean bRepaint)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setDimensions", left, top, width, height, bRepaint);
        ScriptSessions.addScript(script);
    }

    /**
     * Set one to four dimensions at once. This operation is more efficient than calling more than one of
setLeft, setTop, etc. Any argument can be null to indicate not to update it.
     * @param left the new left value or an array containing all four new values
     * @param top the new top value
     * @param width the new width value
     * @param height the new height value
     * @param bRepaint whether to update the display of this object immediately. If <code>left</code> is
   an <code>Array</code> then this parameter is the second parameter passed to this method.
     */
    public void setDimensions(Object[] left, int top, String width, int height, boolean bRepaint)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setDimensions", left, top, width, height, bRepaint);
        ScriptSessions.addScript(script);
    }

    /**
     * Set one to four dimensions at once. This operation is more efficient than calling more than one of
setLeft, setTop, etc. Any argument can be null to indicate not to update it.
     * @param left the new left value or an array containing all four new values
     * @param top the new top value
     * @param width the new width value
     * @param height the new height value
     * @param bRepaint whether to update the display of this object immediately. If <code>left</code> is
   an <code>Array</code> then this parameter is the second parameter passed to this method.
     */
    public void setDimensions(String left, int top, String width, int height, boolean bRepaint)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setDimensions", left, top, width, height, bRepaint);
        ScriptSessions.addScript(script);
    }

    /**
     * Set one to four dimensions at once. This operation is more efficient than calling more than one of
setLeft, setTop, etc. Any argument can be null to indicate not to update it.
     * @param left the new left value or an array containing all four new values
     * @param top the new top value
     * @param width the new width value
     * @param height the new height value
     * @param bRepaint whether to update the display of this object immediately. If <code>left</code> is
   an <code>Array</code> then this parameter is the second parameter passed to this method.
     */
    public void setDimensions(Object[] left, int top, String width, String height, boolean bRepaint)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setDimensions", left, top, width, height, bRepaint);
        ScriptSessions.addScript(script);
    }

    /**
     * Set one to four dimensions at once. This operation is more efficient than calling more than one of
setLeft, setTop, etc. Any argument can be null to indicate not to update it.
     * @param left the new left value or an array containing all four new values
     * @param top the new top value
     * @param width the new width value
     * @param height the new height value
     * @param bRepaint whether to update the display of this object immediately. If <code>left</code> is
   an <code>Array</code> then this parameter is the second parameter passed to this method.
     */
    public void setDimensions(String left, String top, int width, int height, boolean bRepaint)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setDimensions", left, top, width, height, bRepaint);
        ScriptSessions.addScript(script);
    }

    /**
     * Set one to four dimensions at once. This operation is more efficient than calling more than one of
setLeft, setTop, etc. Any argument can be null to indicate not to update it.
     * @param left the new left value or an array containing all four new values
     * @param top the new top value
     * @param width the new width value
     * @param height the new height value
     * @param bRepaint whether to update the display of this object immediately. If <code>left</code> is
   an <code>Array</code> then this parameter is the second parameter passed to this method.
     */
    public void setDimensions(Object[] left, String top, String width, String height, boolean bRepaint)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setDimensions", left, top, width, height, bRepaint);
        ScriptSessions.addScript(script);
    }

    /**
     * Set one to four dimensions at once. This operation is more efficient than calling more than one of
setLeft, setTop, etc. Any argument can be null to indicate not to update it.
     * @param left the new left value or an array containing all four new values
     * @param top the new top value
     * @param width the new width value
     * @param height the new height value
     * @param bRepaint whether to update the display of this object immediately. If <code>left</code> is
   an <code>Array</code> then this parameter is the second parameter passed to this method.
     */
    public void setDimensions(Object[] left, String top, int width, int height, boolean bRepaint)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setDimensions", left, top, width, height, bRepaint);
        ScriptSessions.addScript(script);
    }

    /**
     * Set one to four dimensions at once. This operation is more efficient than calling more than one of
setLeft, setTop, etc. Any argument can be null to indicate not to update it.
     * @param left the new left value or an array containing all four new values
     * @param top the new top value
     * @param width the new width value
     * @param height the new height value
     * @param bRepaint whether to update the display of this object immediately. If <code>left</code> is
   an <code>Array</code> then this parameter is the second parameter passed to this method.
     */
    public void setDimensions(int left, int top, String width, int height, boolean bRepaint)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setDimensions", left, top, width, height, bRepaint);
        ScriptSessions.addScript(script);
    }

    /**
     * Set one to four dimensions at once. This operation is more efficient than calling more than one of
setLeft, setTop, etc. Any argument can be null to indicate not to update it.
     * @param left the new left value or an array containing all four new values
     * @param top the new top value
     * @param width the new width value
     * @param height the new height value
     * @param bRepaint whether to update the display of this object immediately. If <code>left</code> is
   an <code>Array</code> then this parameter is the second parameter passed to this method.
     */
    public void setDimensions(String left, int top, int width, String height, boolean bRepaint)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setDimensions", left, top, width, height, bRepaint);
        ScriptSessions.addScript(script);
    }

    /**
     * Set one to four dimensions at once. This operation is more efficient than calling more than one of
setLeft, setTop, etc. Any argument can be null to indicate not to update it.
     * @param left the new left value or an array containing all four new values
     * @param top the new top value
     * @param width the new width value
     * @param height the new height value
     * @param bRepaint whether to update the display of this object immediately. If <code>left</code> is
   an <code>Array</code> then this parameter is the second parameter passed to this method.
     */
    public void setDimensions(int left, String top, String width, int height, boolean bRepaint)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setDimensions", left, top, width, height, bRepaint);
        ScriptSessions.addScript(script);
    }

    /**
     * Set one to four dimensions at once. This operation is more efficient than calling more than one of
setLeft, setTop, etc. Any argument can be null to indicate not to update it.
     * @param left the new left value or an array containing all four new values
     * @param top the new top value
     * @param width the new width value
     * @param height the new height value
     * @param bRepaint whether to update the display of this object immediately. If <code>left</code> is
   an <code>Array</code> then this parameter is the second parameter passed to this method.
     */
    public void setDimensions(String left, String top, String width, String height, boolean bRepaint)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setDimensions", left, top, width, height, bRepaint);
        ScriptSessions.addScript(script);
    }

    /**
     * Set one to four dimensions at once. This operation is more efficient than calling more than one of
setLeft, setTop, etc. Any argument can be null to indicate not to update it.
     * @param left the new left value or an array containing all four new values
     * @param top the new top value
     * @param width the new width value
     * @param height the new height value
     * @param bRepaint whether to update the display of this object immediately. If <code>left</code> is
   an <code>Array</code> then this parameter is the second parameter passed to this method.
     */
    public void setDimensions(int left, int top, String width, String height, boolean bRepaint)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setDimensions", left, top, width, height, bRepaint);
        ScriptSessions.addScript(script);
    }

    /**
     * Set one to four dimensions at once. This operation is more efficient than calling more than one of
setLeft, setTop, etc. Any argument can be null to indicate not to update it.
     * @param left the new left value or an array containing all four new values
     * @param top the new top value
     * @param width the new width value
     * @param height the new height value
     * @param bRepaint whether to update the display of this object immediately. If <code>left</code> is
   an <code>Array</code> then this parameter is the second parameter passed to this method.
     */
    public void setDimensions(int left, int top, int width, int height, boolean bRepaint)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setDimensions", left, top, width, height, bRepaint);
        ScriptSessions.addScript(script);
    }

    /**
     * Set one to four dimensions at once. This operation is more efficient than calling more than one of
setLeft, setTop, etc. Any argument can be null to indicate not to update it.
     * @param left the new left value or an array containing all four new values
     * @param top the new top value
     * @param width the new width value
     * @param height the new height value
     * @param bRepaint whether to update the display of this object immediately. If <code>left</code> is
   an <code>Array</code> then this parameter is the second parameter passed to this method.
     */
    public void setDimensions(String left, String top, int width, String height, boolean bRepaint)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setDimensions", left, top, width, height, bRepaint);
        ScriptSessions.addScript(script);
    }

    /**
     * Set one to four dimensions at once. This operation is more efficient than calling more than one of
setLeft, setTop, etc. Any argument can be null to indicate not to update it.
     * @param left the new left value or an array containing all four new values
     * @param top the new top value
     * @param width the new width value
     * @param height the new height value
     * @param bRepaint whether to update the display of this object immediately. If <code>left</code> is
   an <code>Array</code> then this parameter is the second parameter passed to this method.
     */
    public void setDimensions(Object[] left, String top, String width, int height, boolean bRepaint)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setDimensions", left, top, width, height, bRepaint);
        ScriptSessions.addScript(script);
    }

    /**
     * Set one to four dimensions at once. This operation is more efficient than calling more than one of
setLeft, setTop, etc. Any argument can be null to indicate not to update it.
     * @param left the new left value or an array containing all four new values
     * @param top the new top value
     * @param width the new width value
     * @param height the new height value
     * @param bRepaint whether to update the display of this object immediately. If <code>left</code> is
   an <code>Array</code> then this parameter is the second parameter passed to this method.
     */
    public void setDimensions(Object[] left, String top, int width, String height, boolean bRepaint)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setDimensions", left, top, width, height, bRepaint);
        ScriptSessions.addScript(script);
    }

    /**
     * Set one to four dimensions at once. This operation is more efficient than calling more than one of
setLeft, setTop, etc. Any argument can be null to indicate not to update it.
     * @param left the new left value or an array containing all four new values
     * @param top the new top value
     * @param width the new width value
     * @param height the new height value
     * @param bRepaint whether to update the display of this object immediately. If <code>left</code> is
   an <code>Array</code> then this parameter is the second parameter passed to this method.
     */
    public void setDimensions(Object[] left, int top, int width, String height, boolean bRepaint)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setDimensions", left, top, width, height, bRepaint);
        ScriptSessions.addScript(script);
    }

    /**
     * Set one to four dimensions at once. This operation is more efficient than calling more than one of
setLeft, setTop, etc. Any argument can be null to indicate not to update it.
     * @param left the new left value or an array containing all four new values
     * @param top the new top value
     * @param width the new width value
     * @param height the new height value
     * @param bRepaint whether to update the display of this object immediately. If <code>left</code> is
   an <code>Array</code> then this parameter is the second parameter passed to this method.
     */
    public void setDimensions(int left, String top, int width, String height, boolean bRepaint)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setDimensions", left, top, width, height, bRepaint);
        ScriptSessions.addScript(script);
    }

    /**
     * Set one to four dimensions at once. This operation is more efficient than calling more than one of
setLeft, setTop, etc. Any argument can be null to indicate not to update it.
     * @param left the new left value or an array containing all four new values
     * @param top the new top value
     * @param width the new width value
     * @param height the new height value
     * @param bRepaint whether to update the display of this object immediately. If <code>left</code> is
   an <code>Array</code> then this parameter is the second parameter passed to this method.
     */
    public void setDimensions(int left, String top, int width, int height, boolean bRepaint)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setDimensions", left, top, width, height, bRepaint);
        ScriptSessions.addScript(script);
    }

    /**
     * Set one to four dimensions at once. This operation is more efficient than calling more than one of
setLeft, setTop, etc. Any argument can be null to indicate not to update it.
     * @param left the new left value or an array containing all four new values
     * @param top the new top value
     * @param width the new width value
     * @param height the new height value
     * @param bRepaint whether to update the display of this object immediately. If <code>left</code> is
   an <code>Array</code> then this parameter is the second parameter passed to this method.
     */
    public void setDimensions(String left, String top, String width, int height, boolean bRepaint)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setDimensions", left, top, width, height, bRepaint);
        ScriptSessions.addScript(script);
    }

    /**
     * Set one to four dimensions at once. This operation is more efficient than calling more than one of
setLeft, setTop, etc. Any argument can be null to indicate not to update it.
     * @param left the new left value or an array containing all four new values
     * @param top the new top value
     * @param width the new width value
     * @param height the new height value
     * @param bRepaint whether to update the display of this object immediately. If <code>left</code> is
   an <code>Array</code> then this parameter is the second parameter passed to this method.
     */
    public void setDimensions(int left, int top, int width, String height, boolean bRepaint)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setDimensions", left, top, width, height, bRepaint);
        ScriptSessions.addScript(script);
    }

    /**
     * Set one to four dimensions at once. This operation is more efficient than calling more than one of
setLeft, setTop, etc. Any argument can be null to indicate not to update it.
     * @param left the new left value or an array containing all four new values
     * @param top the new top value
     * @param width the new width value
     * @param height the new height value
     * @param bRepaint whether to update the display of this object immediately. If <code>left</code> is
   an <code>Array</code> then this parameter is the second parameter passed to this method.
     */
    public void setDimensions(String left, int top, String width, String height, boolean bRepaint)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setDimensions", left, top, width, height, bRepaint);
        ScriptSessions.addScript(script);
    }

    /**
     * Set one to four dimensions at once. This operation is more efficient than calling more than one of
setLeft, setTop, etc. Any argument can be null to indicate not to update it.
     * @param left the new left value or an array containing all four new values
     * @param top the new top value
     * @param width the new width value
     * @param height the new height value
     * @param bRepaint whether to update the display of this object immediately. If <code>left</code> is
   an <code>Array</code> then this parameter is the second parameter passed to this method.
     */
    public void setDimensions(int left, String top, String width, String height, boolean bRepaint)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setDimensions", left, top, width, height, bRepaint);
        ScriptSessions.addScript(script);
    }

    /**
     * Returns the dimensions in an array of four int values
     * @param callback [left,top,width,height]
     */

    public void getDimensions(org.directwebremoting.ui.Callback<Object[]> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getDimensions");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, Object[].class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Returns CSS property value(s) for a margin (margin:4px;)
     */

    public void getMargin(org.directwebremoting.ui.Callback<String> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getMargin");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, String.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Sets CSS property value for margin.
     * @param strCSS The preferred method to set margin is by moving clockwise, beginning with the <b>north</b>
compass position, <b>without</b> the pixel designation.  For example, to specify a top margin of 8 pixels, use <code>8 0 0 0</code>. CSS syntax is
supported, but requires that pixels be designated.  For example, using <code>margin:5px;margin-left:10px;</code>, is equivalent to
<code>5 5 5 10</code>.
     * @param bRecalc if true, the VIEW will be updated with requiring a repaint
     * @return this object
     */
    public jsx3.gui.Block setMargin(String strCSS, boolean bRecalc)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setMargin", strCSS, bRecalc);
        ScriptSessions.addScript(script);
        return this;
    }

    /**
     * Returns the overflow property for the object, that defines how its on-screen view will behave when its contents are larger than its specified width and/or height
     * @param callback [jsx3.gui.Block.OVERFLOWSCROLL, jsx3.gui.Block.OVERFLOWHIDDEN, jsx3.gui.Block.OVERFLOWEXPAND]
     */

    public void getOverflow(org.directwebremoting.ui.Callback<Integer> callback)
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
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, Integer.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Sets the overflow property for the object, that defines how its on-screen view will behave when its contents are larger than its specified width and/or height;
           returns reference to self to facilitate method chaining;
     * @param OVERFLOW [jsx3.gui.Block.OVERFLOWSCROLL, jsx3.gui.Block.OVERFLOWHIDDEN, jsx3.gui.Block.OVERFLOWEXPAND]
     * @return this object
     */
    public jsx3.gui.Block setOverflow(int OVERFLOW)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setOverflow", OVERFLOW);
        ScriptSessions.addScript(script);
        return this;
    }

    /**
     * Returns CSS property value(s) for a padding (padding:4px;)
     */

    public void getPadding(org.directwebremoting.ui.Callback<String> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getPadding");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, String.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Sets the CSS property value for padding an object.
     * @param strCSS The preferred method to set padding is by moving clockwise, beginning with the <b>north</b>
compass position, <b>without</b> the pixel designation.  For example, to specify a top padding of 8 pixels, use <code>8 0 0 0</code>. CSS syntax is
supported, but requires that pixels be designated.  For example, using <code>padding:5px;padding-left:10px;</code>, is equivalent to
<code>5 5 5 10</code>.
     * @param bRecalc if true, the VIEW will be updated with requiring a repaint
     * @return this object
     */
    public jsx3.gui.Block setPadding(String strCSS, boolean bRecalc)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setPadding", strCSS, bRecalc);
        ScriptSessions.addScript(script);
        return this;
    }

    /**
     * Returns if the instance is relatively positioned on-screen; returns one of: jsx3.gui.Block.ABSOLUTE jsx3.gui.Block.RELATIVE
     */

    public void getRelativePosition(org.directwebremoting.ui.Callback<Integer> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getRelativePosition");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, Integer.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Sets if the jsx3.gui.Block instance is relatively positioned on-screen;
           returns reference to self to facilitate method chaining;
     * @param intRelative jsx3.gui.Block.RELATIVE will be applied to the view if null. One of: jsx3.gui.Block.RELATIVE jsx3.gui.Block.ABSOLUTE
     * @param bRepaint if <code>true</code>, the view of this object is immediately updated, obviating the need to call <code>repaint()</code>.
     * @return this object
     */
    public jsx3.gui.Block setRelativePosition(int intRelative, boolean bRepaint)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setRelativePosition", intRelative, bRepaint);
        ScriptSessions.addScript(script);
        return this;
    }

    /**
     * Returns HTML tag name to use when rendering the object on-screen (span is the default); if the property is null,
         jsx3.gui.Block.DEFAULTTAGNAME is used;
     * @param callback valid HTML tag name
     */

    public void getTagName(org.directwebremoting.ui.Callback<String> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getTagName");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, String.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Sets HTML tag name to use when rendering the object on-screen (jsx3.gui.Block.DEFAULTTAGNAME is the default);
           returns reference to self to facilitate method chaining;
     * @param strTagName valid HTML tag name (span, div, form, ol, ul, li, etc); if null is passed, the getter will use jsx3.gui.Block.DEFAULTTAGNAME
     * @return this object
     */
    public jsx3.gui.Block setTagName(String strTagName)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setTagName", strTagName);
        ScriptSessions.addScript(script);
        return this;
    }

    /**
     * Returns the text/HTML for the control to be displayed on-screen; returns an empty string if null; since the text
is rendered on-screen as browser-native HTML, the equivalent of an empty tag (e.g., <span\>) would be an
enclosing tag with an empty string (no content): <span></span>.  To return null would be equivalent to
<span>null</span>, which is not the same as <span/>
     */

    public void getText(org.directwebremoting.ui.Callback<String> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getText");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, String.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Sets the text/HTML for the control to be displayed on-screen;
           returns reference to self to facilitate method chaining;
     * @param strText text/HTML
     * @param bRepaint if <code>true</code>, the view of this object is immediately updated, obviating the need to call <code>repaint()</code>.
     */
    public jsx3.gui.Block setText(String strText, boolean bRepaint)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setText", strText, bRepaint);
        ScriptSessions.addScript(script);
        return this;
    }

    /**
     * Returns the CSS text-align property for the object; if no property value exists, jsx3.gui.Block.ALIGNLEFT is returned by default
     * @param callback one of: jsx3.gui.Block.ALIGNLEFT, jsx3.gui.Block.ALIGNRIGHT, jsx3.gui.Block.ALIGNCENTER
     */

    public void getTextAlign(org.directwebremoting.ui.Callback<String> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getTextAlign");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, String.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Sets the CSS text-align property for the object;
           returns reference to self to facilitate method chaining;
     * @param ALIGN one of: jsx3.gui.Block.ALIGNLEFT, jsx3.gui.Block.ALIGNRIGHT, jsx3.gui.Block.ALIGNCENTER
     */
    public jsx3.gui.Block setTextAlign(String ALIGN)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setTextAlign", ALIGN);
        ScriptSessions.addScript(script);
        return this;
    }

    /**
     * Returns the tooltip text to display when the object is hovered over.  Returns an empty string if null.
     */

    public void getTip(org.directwebremoting.ui.Callback<String> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getTip");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, String.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Sets the tooltip text to display when the object is hovered over. Updates Model and View.
Returns reference to self to facilitate method chaining;
     * @param strTip text/HTML
     * @return this object
     */
    public jsx3.gui.Block setTip(String strTip)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setTip", strTip);
        ScriptSessions.addScript(script);
        return this;
    }

    /**
     * Returns the top property of this object.
     * @param callback top.
     */

    public void getTop(org.directwebremoting.ui.Callback<Integer> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getTop");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, Integer.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Sets the top property of this object. The top property specifies the vertical offset of this object
from its parent and only applies if this object is absolutely positioned.
     * @param vntTop the top value. Only numeric values and percentages are supported. For example: 25, -10, 20%.
     * @param bRepaint if <code>true</code>, the view of this object is immediately updated, obviating the need to call <code>repaint()</code>.
     * @return this object.
     */
    public jsx3.gui.Block setTop(String vntTop, boolean bRepaint)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setTop", vntTop, bRepaint);
        ScriptSessions.addScript(script);
        return this;
    }

    /**
     * Sets the top property of this object. The top property specifies the vertical offset of this object
from its parent and only applies if this object is absolutely positioned.
     * @param vntTop the top value. Only numeric values and percentages are supported. For example: 25, -10, 20%.
     * @param bRepaint if <code>true</code>, the view of this object is immediately updated, obviating the need to call <code>repaint()</code>.
     * @return this object.
     */
    public jsx3.gui.Block setTop(int vntTop, boolean bRepaint)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setTop", vntTop, bRepaint);
        ScriptSessions.addScript(script);
        return this;
    }

    /**
     * Returns the visibility property for the object
     * @param callback [jsx3.gui.Block.VISIBILITYVISIBLE, jsx3.gui.Block.VISIBILITYHIDDEN]
     */

    public void getVisibility(org.directwebremoting.ui.Callback<String> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getVisibility");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, String.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Sets the CSS visibility property the object
     * @param VISIBILITY [jsx3.gui.Block.VISIBILITYVISIBLE, jsx3.gui.Block.VISIBILITYHIDDEN]
     * @param bRepaint if <code>true</code>, the view of this object is immediately updated, obviating the need to call <code>repaint()</code>.
     */
    public void setVisibility(String VISIBILITY, boolean bRepaint)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setVisibility", VISIBILITY, bRepaint);
        ScriptSessions.addScript(script);
    }

    /**
     * Returns the width property of this object.
     * @param callback width.
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
     * Sets the width property of this object.
     * @param vntWidth the width as non-negative integer or non-negative percentage. For example: 45%, 12.
     * @param bRepaint if <code>true</code>, the view of this object is immediately updated, obviating the need to call <code>repaint()</code>.
     * @return this object.
     */
    public jsx3.gui.Block setWidth(String vntWidth, boolean bRepaint)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setWidth", vntWidth, bRepaint);
        ScriptSessions.addScript(script);
        return this;
    }

    /**
     * Sets the width property of this object.
     * @param vntWidth the width as non-negative integer or non-negative percentage. For example: 45%, 12.
     * @param bRepaint if <code>true</code>, the view of this object is immediately updated, obviating the need to call <code>repaint()</code>.
     * @return this object.
     */
    public jsx3.gui.Block setWidth(int vntWidth, boolean bRepaint)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setWidth", vntWidth, bRepaint);
        ScriptSessions.addScript(script);
        return this;
    }

    /**
     * Returns the CSS z-index property
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
     * Sets the CSS z-index for the object
     * @param intZIndex z-index value
     * @param bRepaint if <code>true</code>, the view of this object is immediately updated, obviating the need to call <code>repaint()</code>.
     */
    public void setZIndex(int intZIndex, boolean bRepaint)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setZIndex", intZIndex, bRepaint);
        ScriptSessions.addScript(script);
    }

    /**
     * displays a "blocking mask" inside the block to stop user interactions with content within the block. Applies only to Blocks. Use only on blocks with no padding (padding:0px)
     * @param strMessage text/message to display in the blocking mask to tell the user it is disabled
     */
    public void showMask(String strMessage)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "showMask", strMessage);
        ScriptSessions.addScript(script);
    }

    /**
     * removes the "blocking" mask inside the block to stop user interactions with existing content
     */
    public void hideMask()
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "hideMask");
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
     * Sets whether is object supports programmatic drag, meanining it will allow any contained item to be dragged/dropped.
Implementing classes can decide whether to consult this value or ignore it.
     * @param bDrag <code>jsx3.Boolean.TRUE</code> or <code>jsx3.Boolean.FALSE</code>
     * @return this object
     */

    public jsx3.gui.Interactive setCanDrag(int bDrag)
    {
        String extension = "setCanDrag(\"" + bDrag + "\").";
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
     * Sets whether is object supports programmatic drag, meanining it will allow any contained item to be dragged/dropped.
Implementing classes can decide whether to consult this value or ignore it.
     * @param bDrag <code>jsx3.Boolean.TRUE</code> or <code>jsx3.Boolean.FALSE</code>
     * @param returnType The expected return type
     * @return this object
     */

    public <T> T setCanDrag(int bDrag, Class<T> returnType)
    {
        String extension = "setCanDrag(\"" + bDrag + "\").";
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
     * Sets whether this object can be the target of a drop event. Implementing classes can decide whether to consult
this value or ignore it.
     * @param bDrop <code>jsx3.Boolean.TRUE</code> or <code>jsx3.Boolean.FALSE</code>
     * @return this object
     */

    public jsx3.gui.Interactive setCanDrop(int bDrop)
    {
        String extension = "setCanDrop(\"" + bDrop + "\").";
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
     * Sets whether this object can be the target of a drop event. Implementing classes can decide whether to consult
this value or ignore it.
     * @param bDrop <code>jsx3.Boolean.TRUE</code> or <code>jsx3.Boolean.FALSE</code>
     * @param returnType The expected return type
     * @return this object
     */

    public <T> T setCanDrop(int bDrop, Class<T> returnType)
    {
        String extension = "setCanDrop(\"" + bDrop + "\").";
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
        String extension = "setCanMove(\"" + bMovable + "\").";
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
     * Sets whether is object can be moved around the screen (this is not the same as drag/drop). Implementing classes
can decide whether to consult this value or ignore it.
     * @param bMovable <code>jsx3.Boolean.TRUE</code> or <code>jsx3.Boolean.FALSE</code>
     * @param returnType The expected return type
     * @return this object
     */

    public <T> T setCanMove(int bMovable, Class<T> returnType)
    {
        String extension = "setCanMove(\"" + bMovable + "\").";
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
     * Sets whether is object can be spyglassed. Implementing classes can decide whether to consult
this value or ignore it.
     * @param bSpy <code>jsx3.Boolean.TRUE</code> or <code>jsx3.Boolean.FALSE</code>
     * @return this object
     */

    public jsx3.gui.Interactive setCanSpy(int bSpy)
    {
        String extension = "setCanSpy(\"" + bSpy + "\").";
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
     * Sets whether is object can be spyglassed. Implementing classes can decide whether to consult
this value or ignore it.
     * @param bSpy <code>jsx3.Boolean.TRUE</code> or <code>jsx3.Boolean.FALSE</code>
     * @param returnType The expected return type
     * @return this object
     */

    public <T> T setCanSpy(int bSpy, Class<T> returnType)
    {
        String extension = "setCanSpy(\"" + bSpy + "\").";
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
        String extension = "setEvent(\"" + strScript + "\", \"" + strType + "\").";
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
     * @param returnType The expected return type
     * @return reference to this
     */

    public <T> T setEvent(String strScript, String strType, Class<T> returnType)
    {
        String extension = "setEvent(\"" + strScript + "\", \"" + strType + "\").";
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
     * Sets the name of the jsx3.gui.Menu instance to display when a user
clicks on this object with the right button. The name is a pointer by-name to a JSX object in the same server.
     * @param strMenu name or id (jsxname or jsxid) of the context menu
     * @return this object
     */

    public jsx3.gui.Interactive setMenu(String strMenu)
    {
        String extension = "setMenu(\"" + strMenu + "\").";
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
     * Sets the name of the jsx3.gui.Menu instance to display when a user
clicks on this object with the right button. The name is a pointer by-name to a JSX object in the same server.
     * @param strMenu name or id (jsxname or jsxid) of the context menu
     * @param returnType The expected return type
     * @return this object
     */

    public <T> T setMenu(String strMenu, Class<T> returnType)
    {
        String extension = "setMenu(\"" + strMenu + "\").";
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

}

