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
 * Paints the result of an XSL transformation in an Block. A transformation is performed on the XML source document
with the XSL source document. The result is serialized HTML, which is painted to screen.
 * @author Joe Walker [joe at getahead dot org]
 * @author DRAPGEN - Dwr Reverse Ajax Proxy GENerator
 */
public class BlockX extends jsx3.gui.Block
{
    /**
     * All reverse ajax proxies need context to work from
     * @param context The script that got us to where we are now
     */
    public BlockX(Context context, String extension)
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
     */
    public BlockX(String strName, int vntLeft, int vntTop, String vntWidth, int vntHeight)
    {
        super((Context) null, (String) null);
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall("new BlockX", strName, vntLeft, vntTop, vntWidth, vntHeight);
        setInitScript(script);
    }

    /**
     * instance initializer
     * @param strName unique name distinguishing this object from all other JSX GUI objects in the JSX application
     * @param vntLeft either a number (i.e, 12, 30, etc) or a number and a unit value (i.e., "25%", "36pt", etc); if a number is passed, pixels will be the assumed unit when painted to screen
     * @param vntTop either a number (i.e, 12, 30, etc) or a number and a unit value (i.e., "25%", "36pt", etc); if a number is passed, pixels will be the assumed unit when painted to screen
     * @param vntWidth either a number (i.e, 12, 30, etc) or a number and a unit value (i.e., "25%", "36pt", etc); if a number is passed, pixels will be the assumed unit when painted to screen
     * @param vntHeight either a number (i.e, 12, 30, etc) or a number and a unit value (i.e., "25%", "36pt", etc); if a number is passed, pixels will be the assumed unit when painted to screen
     */
    public BlockX(String strName, int vntLeft, String vntTop, String vntWidth, int vntHeight)
    {
        super((Context) null, (String) null);
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall("new BlockX", strName, vntLeft, vntTop, vntWidth, vntHeight);
        setInitScript(script);
    }

    /**
     * instance initializer
     * @param strName unique name distinguishing this object from all other JSX GUI objects in the JSX application
     * @param vntLeft either a number (i.e, 12, 30, etc) or a number and a unit value (i.e., "25%", "36pt", etc); if a number is passed, pixels will be the assumed unit when painted to screen
     * @param vntTop either a number (i.e, 12, 30, etc) or a number and a unit value (i.e., "25%", "36pt", etc); if a number is passed, pixels will be the assumed unit when painted to screen
     * @param vntWidth either a number (i.e, 12, 30, etc) or a number and a unit value (i.e., "25%", "36pt", etc); if a number is passed, pixels will be the assumed unit when painted to screen
     * @param vntHeight either a number (i.e, 12, 30, etc) or a number and a unit value (i.e., "25%", "36pt", etc); if a number is passed, pixels will be the assumed unit when painted to screen
     */
    public BlockX(String strName, String vntLeft, String vntTop, int vntWidth, int vntHeight)
    {
        super((Context) null, (String) null);
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall("new BlockX", strName, vntLeft, vntTop, vntWidth, vntHeight);
        setInitScript(script);
    }

    /**
     * instance initializer
     * @param strName unique name distinguishing this object from all other JSX GUI objects in the JSX application
     * @param vntLeft either a number (i.e, 12, 30, etc) or a number and a unit value (i.e., "25%", "36pt", etc); if a number is passed, pixels will be the assumed unit when painted to screen
     * @param vntTop either a number (i.e, 12, 30, etc) or a number and a unit value (i.e., "25%", "36pt", etc); if a number is passed, pixels will be the assumed unit when painted to screen
     * @param vntWidth either a number (i.e, 12, 30, etc) or a number and a unit value (i.e., "25%", "36pt", etc); if a number is passed, pixels will be the assumed unit when painted to screen
     * @param vntHeight either a number (i.e, 12, 30, etc) or a number and a unit value (i.e., "25%", "36pt", etc); if a number is passed, pixels will be the assumed unit when painted to screen
     */
    public BlockX(String strName, String vntLeft, int vntTop, String vntWidth, String vntHeight)
    {
        super((Context) null, (String) null);
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall("new BlockX", strName, vntLeft, vntTop, vntWidth, vntHeight);
        setInitScript(script);
    }

    /**
     * instance initializer
     * @param strName unique name distinguishing this object from all other JSX GUI objects in the JSX application
     * @param vntLeft either a number (i.e, 12, 30, etc) or a number and a unit value (i.e., "25%", "36pt", etc); if a number is passed, pixels will be the assumed unit when painted to screen
     * @param vntTop either a number (i.e, 12, 30, etc) or a number and a unit value (i.e., "25%", "36pt", etc); if a number is passed, pixels will be the assumed unit when painted to screen
     * @param vntWidth either a number (i.e, 12, 30, etc) or a number and a unit value (i.e., "25%", "36pt", etc); if a number is passed, pixels will be the assumed unit when painted to screen
     * @param vntHeight either a number (i.e, 12, 30, etc) or a number and a unit value (i.e., "25%", "36pt", etc); if a number is passed, pixels will be the assumed unit when painted to screen
     */
    public BlockX(String strName, String vntLeft, int vntTop, int vntWidth, String vntHeight)
    {
        super((Context) null, (String) null);
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall("new BlockX", strName, vntLeft, vntTop, vntWidth, vntHeight);
        setInitScript(script);
    }

    /**
     * instance initializer
     * @param strName unique name distinguishing this object from all other JSX GUI objects in the JSX application
     * @param vntLeft either a number (i.e, 12, 30, etc) or a number and a unit value (i.e., "25%", "36pt", etc); if a number is passed, pixels will be the assumed unit when painted to screen
     * @param vntTop either a number (i.e, 12, 30, etc) or a number and a unit value (i.e., "25%", "36pt", etc); if a number is passed, pixels will be the assumed unit when painted to screen
     * @param vntWidth either a number (i.e, 12, 30, etc) or a number and a unit value (i.e., "25%", "36pt", etc); if a number is passed, pixels will be the assumed unit when painted to screen
     * @param vntHeight either a number (i.e, 12, 30, etc) or a number and a unit value (i.e., "25%", "36pt", etc); if a number is passed, pixels will be the assumed unit when painted to screen
     */
    public BlockX(String strName, int vntLeft, int vntTop, int vntWidth, String vntHeight)
    {
        super((Context) null, (String) null);
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall("new BlockX", strName, vntLeft, vntTop, vntWidth, vntHeight);
        setInitScript(script);
    }

    /**
     * instance initializer
     * @param strName unique name distinguishing this object from all other JSX GUI objects in the JSX application
     * @param vntLeft either a number (i.e, 12, 30, etc) or a number and a unit value (i.e., "25%", "36pt", etc); if a number is passed, pixels will be the assumed unit when painted to screen
     * @param vntTop either a number (i.e, 12, 30, etc) or a number and a unit value (i.e., "25%", "36pt", etc); if a number is passed, pixels will be the assumed unit when painted to screen
     * @param vntWidth either a number (i.e, 12, 30, etc) or a number and a unit value (i.e., "25%", "36pt", etc); if a number is passed, pixels will be the assumed unit when painted to screen
     * @param vntHeight either a number (i.e, 12, 30, etc) or a number and a unit value (i.e., "25%", "36pt", etc); if a number is passed, pixels will be the assumed unit when painted to screen
     */
    public BlockX(String strName, String vntLeft, String vntTop, int vntWidth, String vntHeight)
    {
        super((Context) null, (String) null);
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall("new BlockX", strName, vntLeft, vntTop, vntWidth, vntHeight);
        setInitScript(script);
    }

    /**
     * instance initializer
     * @param strName unique name distinguishing this object from all other JSX GUI objects in the JSX application
     * @param vntLeft either a number (i.e, 12, 30, etc) or a number and a unit value (i.e., "25%", "36pt", etc); if a number is passed, pixels will be the assumed unit when painted to screen
     * @param vntTop either a number (i.e, 12, 30, etc) or a number and a unit value (i.e., "25%", "36pt", etc); if a number is passed, pixels will be the assumed unit when painted to screen
     * @param vntWidth either a number (i.e, 12, 30, etc) or a number and a unit value (i.e., "25%", "36pt", etc); if a number is passed, pixels will be the assumed unit when painted to screen
     * @param vntHeight either a number (i.e, 12, 30, etc) or a number and a unit value (i.e., "25%", "36pt", etc); if a number is passed, pixels will be the assumed unit when painted to screen
     */
    public BlockX(String strName, String vntLeft, String vntTop, String vntWidth, int vntHeight)
    {
        super((Context) null, (String) null);
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall("new BlockX", strName, vntLeft, vntTop, vntWidth, vntHeight);
        setInitScript(script);
    }

    /**
     * instance initializer
     * @param strName unique name distinguishing this object from all other JSX GUI objects in the JSX application
     * @param vntLeft either a number (i.e, 12, 30, etc) or a number and a unit value (i.e., "25%", "36pt", etc); if a number is passed, pixels will be the assumed unit when painted to screen
     * @param vntTop either a number (i.e, 12, 30, etc) or a number and a unit value (i.e., "25%", "36pt", etc); if a number is passed, pixels will be the assumed unit when painted to screen
     * @param vntWidth either a number (i.e, 12, 30, etc) or a number and a unit value (i.e., "25%", "36pt", etc); if a number is passed, pixels will be the assumed unit when painted to screen
     * @param vntHeight either a number (i.e, 12, 30, etc) or a number and a unit value (i.e., "25%", "36pt", etc); if a number is passed, pixels will be the assumed unit when painted to screen
     */
    public BlockX(String strName, int vntLeft, int vntTop, String vntWidth, String vntHeight)
    {
        super((Context) null, (String) null);
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall("new BlockX", strName, vntLeft, vntTop, vntWidth, vntHeight);
        setInitScript(script);
    }

    /**
     * instance initializer
     * @param strName unique name distinguishing this object from all other JSX GUI objects in the JSX application
     * @param vntLeft either a number (i.e, 12, 30, etc) or a number and a unit value (i.e., "25%", "36pt", etc); if a number is passed, pixels will be the assumed unit when painted to screen
     * @param vntTop either a number (i.e, 12, 30, etc) or a number and a unit value (i.e., "25%", "36pt", etc); if a number is passed, pixels will be the assumed unit when painted to screen
     * @param vntWidth either a number (i.e, 12, 30, etc) or a number and a unit value (i.e., "25%", "36pt", etc); if a number is passed, pixels will be the assumed unit when painted to screen
     * @param vntHeight either a number (i.e, 12, 30, etc) or a number and a unit value (i.e., "25%", "36pt", etc); if a number is passed, pixels will be the assumed unit when painted to screen
     */
    public BlockX(String strName, int vntLeft, String vntTop, int vntWidth, int vntHeight)
    {
        super((Context) null, (String) null);
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall("new BlockX", strName, vntLeft, vntTop, vntWidth, vntHeight);
        setInitScript(script);
    }

    /**
     * instance initializer
     * @param strName unique name distinguishing this object from all other JSX GUI objects in the JSX application
     * @param vntLeft either a number (i.e, 12, 30, etc) or a number and a unit value (i.e., "25%", "36pt", etc); if a number is passed, pixels will be the assumed unit when painted to screen
     * @param vntTop either a number (i.e, 12, 30, etc) or a number and a unit value (i.e., "25%", "36pt", etc); if a number is passed, pixels will be the assumed unit when painted to screen
     * @param vntWidth either a number (i.e, 12, 30, etc) or a number and a unit value (i.e., "25%", "36pt", etc); if a number is passed, pixels will be the assumed unit when painted to screen
     * @param vntHeight either a number (i.e, 12, 30, etc) or a number and a unit value (i.e., "25%", "36pt", etc); if a number is passed, pixels will be the assumed unit when painted to screen
     */
    public BlockX(String strName, String vntLeft, int vntTop, String vntWidth, int vntHeight)
    {
        super((Context) null, (String) null);
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall("new BlockX", strName, vntLeft, vntTop, vntWidth, vntHeight);
        setInitScript(script);
    }

    /**
     * instance initializer
     * @param strName unique name distinguishing this object from all other JSX GUI objects in the JSX application
     * @param vntLeft either a number (i.e, 12, 30, etc) or a number and a unit value (i.e., "25%", "36pt", etc); if a number is passed, pixels will be the assumed unit when painted to screen
     * @param vntTop either a number (i.e, 12, 30, etc) or a number and a unit value (i.e., "25%", "36pt", etc); if a number is passed, pixels will be the assumed unit when painted to screen
     * @param vntWidth either a number (i.e, 12, 30, etc) or a number and a unit value (i.e., "25%", "36pt", etc); if a number is passed, pixels will be the assumed unit when painted to screen
     * @param vntHeight either a number (i.e, 12, 30, etc) or a number and a unit value (i.e., "25%", "36pt", etc); if a number is passed, pixels will be the assumed unit when painted to screen
     */
    public BlockX(String strName, String vntLeft, String vntTop, String vntWidth, String vntHeight)
    {
        super((Context) null, (String) null);
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall("new BlockX", strName, vntLeft, vntTop, vntWidth, vntHeight);
        setInitScript(script);
    }

    /**
     * instance initializer
     * @param strName unique name distinguishing this object from all other JSX GUI objects in the JSX application
     * @param vntLeft either a number (i.e, 12, 30, etc) or a number and a unit value (i.e., "25%", "36pt", etc); if a number is passed, pixels will be the assumed unit when painted to screen
     * @param vntTop either a number (i.e, 12, 30, etc) or a number and a unit value (i.e., "25%", "36pt", etc); if a number is passed, pixels will be the assumed unit when painted to screen
     * @param vntWidth either a number (i.e, 12, 30, etc) or a number and a unit value (i.e., "25%", "36pt", etc); if a number is passed, pixels will be the assumed unit when painted to screen
     * @param vntHeight either a number (i.e, 12, 30, etc) or a number and a unit value (i.e., "25%", "36pt", etc); if a number is passed, pixels will be the assumed unit when painted to screen
     */
    public BlockX(String strName, int vntLeft, int vntTop, int vntWidth, int vntHeight)
    {
        super((Context) null, (String) null);
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall("new BlockX", strName, vntLeft, vntTop, vntWidth, vntHeight);
        setInitScript(script);
    }

    /**
     * instance initializer
     * @param strName unique name distinguishing this object from all other JSX GUI objects in the JSX application
     * @param vntLeft either a number (i.e, 12, 30, etc) or a number and a unit value (i.e., "25%", "36pt", etc); if a number is passed, pixels will be the assumed unit when painted to screen
     * @param vntTop either a number (i.e, 12, 30, etc) or a number and a unit value (i.e., "25%", "36pt", etc); if a number is passed, pixels will be the assumed unit when painted to screen
     * @param vntWidth either a number (i.e, 12, 30, etc) or a number and a unit value (i.e., "25%", "36pt", etc); if a number is passed, pixels will be the assumed unit when painted to screen
     * @param vntHeight either a number (i.e, 12, 30, etc) or a number and a unit value (i.e., "25%", "36pt", etc); if a number is passed, pixels will be the assumed unit when painted to screen
     */
    public BlockX(String strName, int vntLeft, String vntTop, String vntWidth, String vntHeight)
    {
        super((Context) null, (String) null);
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall("new BlockX", strName, vntLeft, vntTop, vntWidth, vntHeight);
        setInitScript(script);
    }

    /**
     * instance initializer
     * @param strName unique name distinguishing this object from all other JSX GUI objects in the JSX application
     * @param vntLeft either a number (i.e, 12, 30, etc) or a number and a unit value (i.e., "25%", "36pt", etc); if a number is passed, pixels will be the assumed unit when painted to screen
     * @param vntTop either a number (i.e, 12, 30, etc) or a number and a unit value (i.e., "25%", "36pt", etc); if a number is passed, pixels will be the assumed unit when painted to screen
     * @param vntWidth either a number (i.e, 12, 30, etc) or a number and a unit value (i.e., "25%", "36pt", etc); if a number is passed, pixels will be the assumed unit when painted to screen
     * @param vntHeight either a number (i.e, 12, 30, etc) or a number and a unit value (i.e., "25%", "36pt", etc); if a number is passed, pixels will be the assumed unit when painted to screen
     */
    public BlockX(String strName, int vntLeft, String vntTop, int vntWidth, String vntHeight)
    {
        super((Context) null, (String) null);
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall("new BlockX", strName, vntLeft, vntTop, vntWidth, vntHeight);
        setInitScript(script);
    }

    /**
     * instance initializer
     * @param strName unique name distinguishing this object from all other JSX GUI objects in the JSX application
     * @param vntLeft either a number (i.e, 12, 30, etc) or a number and a unit value (i.e., "25%", "36pt", etc); if a number is passed, pixels will be the assumed unit when painted to screen
     * @param vntTop either a number (i.e, 12, 30, etc) or a number and a unit value (i.e., "25%", "36pt", etc); if a number is passed, pixels will be the assumed unit when painted to screen
     * @param vntWidth either a number (i.e, 12, 30, etc) or a number and a unit value (i.e., "25%", "36pt", etc); if a number is passed, pixels will be the assumed unit when painted to screen
     * @param vntHeight either a number (i.e, 12, 30, etc) or a number and a unit value (i.e., "25%", "36pt", etc); if a number is passed, pixels will be the assumed unit when painted to screen
     */
    public BlockX(String strName, String vntLeft, int vntTop, int vntWidth, int vntHeight)
    {
        super((Context) null, (String) null);
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall("new BlockX", strName, vntLeft, vntTop, vntWidth, vntHeight);
        setInitScript(script);
    }



    /**
     * Returns the XSL string of this object.
     */

    public void getXSLString(org.directwebremoting.ui.Callback<String> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getXSLString");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, String.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Sets the XSL string of this object.
     * @param strXSL
     * @return this object.
     */

    public jsx3.xml.Cacheable setXSLString(String strXSL)
    {
        String extension = "setXSLString(\"" + strXSL + "\").";
        try
        {
            java.lang.reflect.Constructor<jsx3.xml.Cacheable> ctor = jsx3.xml.Cacheable.class.getConstructor(Context.class, String.class);
            return ctor.newInstance(this, extension);
        }
        catch (Exception ex)
        {
            throw new IllegalArgumentException("Unsupported type: " + jsx3.xml.Cacheable.class.getName());
        }
    }

    /**
     * Sets the XSL string of this object.
     * @param strXSL
     * @param returnType The expected return type
     * @return this object.
     */

    public <T> T setXSLString(String strXSL, Class<T> returnType)
    {
        String extension = "setXSLString(\"" + strXSL + "\").";
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
     * Returns the XSL URL of this object.
     */

    public void getXSLURL(org.directwebremoting.ui.Callback<String> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getXSLURL");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, String.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Sets the XSL URL of this object.
     * @param strXSLURL
     * @return this object.
     */

    public jsx3.xml.Cacheable setXSLURL(String strXSLURL)
    {
        String extension = "setXSLURL(\"" + strXSLURL + "\").";
        try
        {
            java.lang.reflect.Constructor<jsx3.xml.Cacheable> ctor = jsx3.xml.Cacheable.class.getConstructor(Context.class, String.class);
            return ctor.newInstance(this, extension);
        }
        catch (Exception ex)
        {
            throw new IllegalArgumentException("Unsupported type: " + jsx3.xml.Cacheable.class.getName());
        }
    }

    /**
     * Sets the XSL URL of this object.
     * @param strXSLURL
     * @param returnType The expected return type
     * @return this object.
     */

    public <T> T setXSLURL(String strXSLURL, Class<T> returnType)
    {
        String extension = "setXSLURL(\"" + strXSLURL + "\").";
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
     * Resets the XML source document stored in the server cache under the XML ID of this object to an empty CDF
document.
     */
    public void clearXmlData()
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "clearXmlData");
        ScriptSessions.addScript(script);
    }

    /**
     * Returns whether this object removes its XML and XSL source documents from the cache of its server when it
is destroyed.
     * @param callback <code>CLEANUPRESOURCES</code> or <code>SHARERESOURCES</code>.
     */

    public void getShareResources(org.directwebremoting.ui.Callback<Integer> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getShareResources");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, Integer.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Returns the XML source document of this object. The XML document is determined by the following steps:

  If an XML document exists in the server cache under an ID equal to the XML ID of this object, that
    document is returned.
  If the XML string of this object is not empty, a new document is created by parsing this string.
  If the XML URL of this object is not empty, a new document is created by parsing the file at the location
    specified by the URL resolved against the server owning this object.
  Otherwise, an empty CDF document is returned.

If a new document is created for this object (any of the steps listed above except for the first one), the
following actions are also taken:

  If creating the document resulted in an error (XML parsing error, file not found error, etc) the offending
    document is returned immediately.
  Otherwise, setSourceXML is called on this object, passing in the created document.
     */

    public jsx3.xml.CdfDocument getXML()
    {
        String extension = "getXML().";
        try
        {
            java.lang.reflect.Constructor<jsx3.xml.CdfDocument> ctor = jsx3.xml.CdfDocument.class.getConstructor(Context.class, String.class);
            return ctor.newInstance(this, extension);
        }
        catch (Exception ex)
        {
            throw new IllegalArgumentException("Unsupported type: " + jsx3.xml.CdfDocument.class.getName());
        }
    }

    /**
     * Returns the XML source document of this object. The XML document is determined by the following steps:

  If an XML document exists in the server cache under an ID equal to the XML ID of this object, that
    document is returned.
  If the XML string of this object is not empty, a new document is created by parsing this string.
  If the XML URL of this object is not empty, a new document is created by parsing the file at the location
    specified by the URL resolved against the server owning this object.
  Otherwise, an empty CDF document is returned.

If a new document is created for this object (any of the steps listed above except for the first one), the
following actions are also taken:

  If creating the document resulted in an error (XML parsing error, file not found error, etc) the offending
    document is returned immediately.
  Otherwise, setSourceXML is called on this object, passing in the created document.
     * @param returnType The expected return type
     */

    public <T> T getXML(Class<T> returnType)
    {
        String extension = "getXML().";
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
     * Returns the XML ID of this object.
     * @param callback the XML ID.
     */

    public void getXMLId(org.directwebremoting.ui.Callback<String> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getXMLId");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, String.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Returns the XML string of this object.
     */

    public void getXMLString(org.directwebremoting.ui.Callback<String> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getXMLString");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, String.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Returns the list of XML transformers of this object.
     */

    public void getXMLTransformers(org.directwebremoting.ui.Callback<Object[]> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getXMLTransformers");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, Object[].class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Returns the XML URL of this object.
     */

    public void getXMLURL(org.directwebremoting.ui.Callback<String> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getXMLURL");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, String.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Returns the XSL source document of this object. The XSL document is determined by the following steps:

  If an XSL document exists in the server cache under an ID equal to the XSL ID of this object, that
    document is returned.
  (Deprecated) If the XSL string of this object is not null, a new document is created by parsing this string.
  (Deprecated) If the XSL URL of this object is not null, a new document is created by parsing the file at the location
    specified by the URL resolved against the server owning this object.
  Otherwise, the default stylesheet (Cacheable.DEFAULTSTYLESHEET) is returned.
     * @return the XSL source document.
     */

    public jsx3.xml.CdfDocument getXSL()
    {
        String extension = "getXSL().";
        try
        {
            java.lang.reflect.Constructor<jsx3.xml.CdfDocument> ctor = jsx3.xml.CdfDocument.class.getConstructor(Context.class, String.class);
            return ctor.newInstance(this, extension);
        }
        catch (Exception ex)
        {
            throw new IllegalArgumentException("Unsupported type: " + jsx3.xml.CdfDocument.class.getName());
        }
    }

    /**
     * Returns the XSL source document of this object. The XSL document is determined by the following steps:

  If an XSL document exists in the server cache under an ID equal to the XSL ID of this object, that
    document is returned.
  (Deprecated) If the XSL string of this object is not null, a new document is created by parsing this string.
  (Deprecated) If the XSL URL of this object is not null, a new document is created by parsing the file at the location
    specified by the URL resolved against the server owning this object.
  Otherwise, the default stylesheet (Cacheable.DEFAULTSTYLESHEET) is returned.
     * @param returnType The expected return type
     * @return the XSL source document.
     */

    public <T> T getXSL(Class<T> returnType)
    {
        String extension = "getXSL().";
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
     * Returns the XSL ID of this object.
     */

    public void getXSLId(org.directwebremoting.ui.Callback<String> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getXSLId");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, String.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Returns a map containing all the parameters to pass to the XSL stylesheet during transformation.
     */

    public jsx3.lang.Object getXSLParams()
    {
        String extension = "getXSLParams().";
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
     * Returns a map containing all the parameters to pass to the XSL stylesheet during transformation.
     * @param returnType The expected return type
     */

    public <T> T getXSLParams(Class<T> returnType)
    {
        String extension = "getXSLParams().";
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
     * Returns whether the XML data source of this object is loaded asynchronously.
     * @param callback <code>0</code> or <code>1</code>.
     */

    public void getXmlAsync(org.directwebremoting.ui.Callback<Integer> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getXmlAsync");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, Integer.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Returns whether this object is bound to the XML document stored in the data cache.
     * @param callback <code>0</code> or <code>1</code>.
     */

    public void getXmlBind(org.directwebremoting.ui.Callback<Integer> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getXmlBind");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, Integer.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * This method is called in two situations:

  When the datasource of this object finishes loading (success, error, or timeout), if the
      xmlAsync property of this object is true, its datasource is specified as an
       XML URL, and the first time doTransform() was called the datasource was still loading.
  Any time the value stored in the server XML cache under the key equal to the XML Id of this object
      changes, if the xmlBind property of this object is true.

Any methods overriding this method should begin with a call to jsxsupermix().
     * @param objEvent the event published by the cache.
     */
    public void onXmlBinding(jsx3.lang.Object objEvent)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "onXmlBinding", objEvent);
        ScriptSessions.addScript(script);
    }

    /**
     * Removes a parameter from the list of parameters to pass to the XSL stylesheet during transformation.
     * @param strName the name of the XSL parameter to remove.
     * @return this object.
     */

    public jsx3.xml.Cacheable removeXSLParam(String strName)
    {
        String extension = "removeXSLParam(\"" + strName + "\").";
        try
        {
            java.lang.reflect.Constructor<jsx3.xml.Cacheable> ctor = jsx3.xml.Cacheable.class.getConstructor(Context.class, String.class);
            return ctor.newInstance(this, extension);
        }
        catch (Exception ex)
        {
            throw new IllegalArgumentException("Unsupported type: " + jsx3.xml.Cacheable.class.getName());
        }
    }

    /**
     * Removes a parameter from the list of parameters to pass to the XSL stylesheet during transformation.
     * @param strName the name of the XSL parameter to remove.
     * @param returnType The expected return type
     * @return this object.
     */

    public <T> T removeXSLParam(String strName, Class<T> returnType)
    {
        String extension = "removeXSLParam(\"" + strName + "\").";
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
     * Removes all parameters from the list of parameters to pass to the XSL stylesheet during transformation.
     * @return this object.
     */

    public jsx3.xml.Cacheable removeXSLParams()
    {
        String extension = "removeXSLParams().";
        try
        {
            java.lang.reflect.Constructor<jsx3.xml.Cacheable> ctor = jsx3.xml.Cacheable.class.getConstructor(Context.class, String.class);
            return ctor.newInstance(this, extension);
        }
        catch (Exception ex)
        {
            throw new IllegalArgumentException("Unsupported type: " + jsx3.xml.Cacheable.class.getName());
        }
    }

    /**
     * Removes all parameters from the list of parameters to pass to the XSL stylesheet during transformation.
     * @param returnType The expected return type
     * @return this object.
     */

    public <T> T removeXSLParams(Class<T> returnType)
    {
        String extension = "removeXSLParams().";
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
     * Removes the XML and XSL source documents from the server cache.
     * @param objServer the server owning the cache to modify. This is a required argument only if
   <code>this.getServer()</code> does not returns a server instance.
     */
    public void resetCacheData(jsx3.app.Server objServer)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "resetCacheData", objServer);
        ScriptSessions.addScript(script);
    }

    /**
     * Removes the XML source document stored under the XML ID of this object from the server cache.
     * @param objServer the server owning the cache to modify. This is a required argument only if
   <code>this.getServer()</code> does not returns a server instance.
     */
    public void resetXmlCacheData(jsx3.app.Server objServer)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "resetXmlCacheData", objServer);
        ScriptSessions.addScript(script);
    }

    /**
     * Sets whether this object removes its XML and XSL source documents from the cache of its server when it
is destroyed.
     * @param intShare <code>CLEANUPRESOURCES</code> or <code>SHARERESOURCES</code>. <code>CLEANUPRESOURCES</code>
  is the default value if the property is <code>null</code>.
     * @return this object.
     */

    public jsx3.xml.Cacheable setShareResources(int intShare)
    {
        String extension = "setShareResources(\"" + intShare + "\").";
        try
        {
            java.lang.reflect.Constructor<jsx3.xml.Cacheable> ctor = jsx3.xml.Cacheable.class.getConstructor(Context.class, String.class);
            return ctor.newInstance(this, extension);
        }
        catch (Exception ex)
        {
            throw new IllegalArgumentException("Unsupported type: " + jsx3.xml.Cacheable.class.getName());
        }
    }

    /**
     * Sets whether this object removes its XML and XSL source documents from the cache of its server when it
is destroyed.
     * @param intShare <code>CLEANUPRESOURCES</code> or <code>SHARERESOURCES</code>. <code>CLEANUPRESOURCES</code>
  is the default value if the property is <code>null</code>.
     * @param returnType The expected return type
     * @return this object.
     */

    public <T> T setShareResources(int intShare, Class<T> returnType)
    {
        String extension = "setShareResources(\"" + intShare + "\").";
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
     * Sets the source document of this object as though objDoc were retrieved from the XML URL or XML
string of this object. This method executes the following steps:

  The document is transformed serially by each XML transformers of this object.
  The XML document is saved in the server cache under the XML ID of this object.
  If this object is an instance of jsx3.xml.CDF and the root node is a <data> element
    and its jsxassignids attribute is equal to 1, all <record> elements without a
    jsxid attribute are assigned a unique jsxid.
  If this object is an instance of jsx3.xml.CDF, convertProperties() is called
    on this object.
     * @param objDoc
     * @param objCache
     * @return the document stored in the server cache as the data source of this object. If
  transformers were run, this value will not be equal to the <code>objDoc</code> parameter.
     */

    public jsx3.xml.CdfDocument setSourceXML(jsx3.xml.CdfDocument objDoc, jsx3.app.Cache objCache)
    {
        String extension = "setSourceXML(\"" + objDoc + "\", \"" + objCache + "\").";
        try
        {
            java.lang.reflect.Constructor<jsx3.xml.CdfDocument> ctor = jsx3.xml.CdfDocument.class.getConstructor(Context.class, String.class);
            return ctor.newInstance(this, extension);
        }
        catch (Exception ex)
        {
            throw new IllegalArgumentException("Unsupported type: " + jsx3.xml.CdfDocument.class.getName());
        }
    }

    /**
     * Sets the source document of this object as though objDoc were retrieved from the XML URL or XML
string of this object. This method executes the following steps:

  The document is transformed serially by each XML transformers of this object.
  The XML document is saved in the server cache under the XML ID of this object.
  If this object is an instance of jsx3.xml.CDF and the root node is a <data> element
    and its jsxassignids attribute is equal to 1, all <record> elements without a
    jsxid attribute are assigned a unique jsxid.
  If this object is an instance of jsx3.xml.CDF, convertProperties() is called
    on this object.
     * @param objDoc
     * @param objCache
     * @param returnType The expected return type
     * @return the document stored in the server cache as the data source of this object. If
  transformers were run, this value will not be equal to the <code>objDoc</code> parameter.
     */

    public <T> T setSourceXML(jsx3.xml.CdfDocument objDoc, jsx3.app.Cache objCache, Class<T> returnType)
    {
        String extension = "setSourceXML(\"" + objDoc + "\", \"" + objCache + "\").";
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
     * Sets the XML ID of this object. This value is the key under which the XML source document of this object is
saved in the cache of the server owning this object. The developer may specify either a unique or shared value.
If no value is specified, a unique id is generated.
     * @param strXMLId
     * @return this object.
     */

    public jsx3.xml.Cacheable setXMLId(String strXMLId)
    {
        String extension = "setXMLId(\"" + strXMLId + "\").";
        try
        {
            java.lang.reflect.Constructor<jsx3.xml.Cacheable> ctor = jsx3.xml.Cacheable.class.getConstructor(Context.class, String.class);
            return ctor.newInstance(this, extension);
        }
        catch (Exception ex)
        {
            throw new IllegalArgumentException("Unsupported type: " + jsx3.xml.Cacheable.class.getName());
        }
    }

    /**
     * Sets the XML ID of this object. This value is the key under which the XML source document of this object is
saved in the cache of the server owning this object. The developer may specify either a unique or shared value.
If no value is specified, a unique id is generated.
     * @param strXMLId
     * @param returnType The expected return type
     * @return this object.
     */

    public <T> T setXMLId(String strXMLId, Class<T> returnType)
    {
        String extension = "setXMLId(\"" + strXMLId + "\").";
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
     * Sets the XML string of this object. Setting this value to the string serialization of an XML document is one
way of specifying the source XML document of this object.
     * @param strXML <code>null</code> or a well-formed serialized XML element.
     * @return this object.
     */

    public jsx3.xml.Cacheable setXMLString(String strXML)
    {
        String extension = "setXMLString(\"" + strXML + "\").";
        try
        {
            java.lang.reflect.Constructor<jsx3.xml.Cacheable> ctor = jsx3.xml.Cacheable.class.getConstructor(Context.class, String.class);
            return ctor.newInstance(this, extension);
        }
        catch (Exception ex)
        {
            throw new IllegalArgumentException("Unsupported type: " + jsx3.xml.Cacheable.class.getName());
        }
    }

    /**
     * Sets the XML string of this object. Setting this value to the string serialization of an XML document is one
way of specifying the source XML document of this object.
     * @param strXML <code>null</code> or a well-formed serialized XML element.
     * @param returnType The expected return type
     * @return this object.
     */

    public <T> T setXMLString(String strXML, Class<T> returnType)
    {
        String extension = "setXMLString(\"" + strXML + "\").";
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
     * Sets the list of XML transformers of this object. The XML source document of this object is transformed
serially by each of these transformers before it is placed in the XML cache.

Each transformer is either the URI of an XSLT document (which will be resolved against the
the server of this object) or the cache id of a XSLT document in the XML cache of the server
of this object. When any transformer is loaded from a URI it is placed in the server cache under the id
equal to its resolved URI. Any transformer that does not correspond to a valid XSLT document will be skipped
without throwing an error.
     * @param arrTrans
     */
    public void setXMLTransformers(Object[] arrTrans)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setXMLTransformers", arrTrans);
        ScriptSessions.addScript(script);
    }

    /**
     * Sets the XML URL of this object. Settings this value to the URI of an XML document is one way of specifying the
source XML document of this object.
     * @param strXMLURL <code>null</code> or a URI that when resolved against the server owning this object
  specifies a valid XML document.
     * @return this object.
     */

    public jsx3.xml.Cacheable setXMLURL(String strXMLURL)
    {
        String extension = "setXMLURL(\"" + strXMLURL + "\").";
        try
        {
            java.lang.reflect.Constructor<jsx3.xml.Cacheable> ctor = jsx3.xml.Cacheable.class.getConstructor(Context.class, String.class);
            return ctor.newInstance(this, extension);
        }
        catch (Exception ex)
        {
            throw new IllegalArgumentException("Unsupported type: " + jsx3.xml.Cacheable.class.getName());
        }
    }

    /**
     * Sets the XML URL of this object. Settings this value to the URI of an XML document is one way of specifying the
source XML document of this object.
     * @param strXMLURL <code>null</code> or a URI that when resolved against the server owning this object
  specifies a valid XML document.
     * @param returnType The expected return type
     * @return this object.
     */

    public <T> T setXMLURL(String strXMLURL, Class<T> returnType)
    {
        String extension = "setXMLURL(\"" + strXMLURL + "\").";
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
     * Adds a name/value pair to the list of parameters to pass to the XSL stylesheet during transformation. If
strValue is null the parameter is removed.
     * @param strName the name of the XSL parameter to add.
     * @param strValue the value of the XSL parameter to add.
     * @return this object.
     */

    public jsx3.xml.Cacheable setXSLParam(String strName, String strValue)
    {
        String extension = "setXSLParam(\"" + strName + "\", \"" + strValue + "\").";
        try
        {
            java.lang.reflect.Constructor<jsx3.xml.Cacheable> ctor = jsx3.xml.Cacheable.class.getConstructor(Context.class, String.class);
            return ctor.newInstance(this, extension);
        }
        catch (Exception ex)
        {
            throw new IllegalArgumentException("Unsupported type: " + jsx3.xml.Cacheable.class.getName());
        }
    }

    /**
     * Adds a name/value pair to the list of parameters to pass to the XSL stylesheet during transformation. If
strValue is null the parameter is removed.
     * @param strName the name of the XSL parameter to add.
     * @param strValue the value of the XSL parameter to add.
     * @param returnType The expected return type
     * @return this object.
     */

    public <T> T setXSLParam(String strName, String strValue, Class<T> returnType)
    {
        String extension = "setXSLParam(\"" + strName + "\", \"" + strValue + "\").";
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
     * Sets whether the XML data source of this object is loaded asynchronously. This setting only applies to
data sources loaded from an XML URL.
     * @param bAsync
     * @return this object.
     */

    public jsx3.xml.Cacheable setXmlAsync(boolean bAsync)
    {
        String extension = "setXmlAsync(\"" + bAsync + "\").";
        try
        {
            java.lang.reflect.Constructor<jsx3.xml.Cacheable> ctor = jsx3.xml.Cacheable.class.getConstructor(Context.class, String.class);
            return ctor.newInstance(this, extension);
        }
        catch (Exception ex)
        {
            throw new IllegalArgumentException("Unsupported type: " + jsx3.xml.Cacheable.class.getName());
        }
    }

    /**
     * Sets whether the XML data source of this object is loaded asynchronously. This setting only applies to
data sources loaded from an XML URL.
     * @param bAsync
     * @param returnType The expected return type
     * @return this object.
     */

    public <T> T setXmlAsync(boolean bAsync, Class<T> returnType)
    {
        String extension = "setXmlAsync(\"" + bAsync + "\").";
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
     * Sets whether this object is bound to the XML document stored in the data cache. If this object is bound to the
cache, then the onXmlBinding() method of this object is called any time the document stored in
the cache under the XML Id of this object changes.
     * @param bBind
     * @param callback <code>0</code> or <code>1</code>.
     */

    public void setXmlBind(boolean bBind, org.directwebremoting.ui.Callback<Integer> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "setXmlBind", bBind);

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, Integer.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

}

