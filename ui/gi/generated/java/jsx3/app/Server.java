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
package jsx3.app;

import org.directwebremoting.ScriptBuffer;
import org.directwebremoting.ScriptSessions;
import org.directwebremoting.io.Context;

/**
 * The controller of the JSX architecture.
 * @author Joe Walker [joe at getahead dot org]
 * @author DRAPGEN - Dwr Reverse Ajax Proxy GENerator
 */
public class Server extends jsx3.lang.Object
{
    /**
     * All reverse ajax proxies need context to work from
     * @param context The script that got us to where we are now
     */
    public Server(Context context, String extension)
    {
        super(context, extension);
    }

    /**
     * Sets environment variables used by this class (the controller for the JSX architecture)
     * @param strAppPath URL (either relative or absolute) for the application to load
     * @param objGUI the browser element (body, div, span, td, etc) into which the GI application should load
     * @param bPaint false if null; if true, the application VIEW will immediately be generated.
     * @param objEnv
     */
    public Server(String strAppPath, String objGUI, boolean bPaint, jsx3.lang.Object objEnv)
    {
        super((Context) null, (String) null);
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall("new Server", strAppPath, objGUI, bPaint, objEnv);
        setInitScript(script);
    }


    /**
     * The subject of an event that jsx3.app.Server publishes when an instance of this class
   is created. The target of the event object is the initialized server.
     */
    public static final String INITED = "inited";

    /**
     * The subject of an event that instances of this class publish when a context help hot key is pressed
   in the context of a DOM node that has a help ID. The event has the following fields:


          target - the server publishing the event.

          model - the DOM node that received the key event.

          helpid - the help ID of the nearest ancestor of model that defines a help ID.
     */
    public static final String HELP = "help";


    /**
     * Returns the value of an environment setting of this server. Valid keys correspond to deployment options and are
(case-insensitive):

VERSION
APPPATH
ABSPATH
CAPTION
MODE
SYSTEM
NAMESPACE
CANCELERROR
CANCELRIGHTCLICK
BODYHOTKEYS
WIDTH
HEIGHT
LEFT
TOP
POSITION
OVERFLOW
UNICODE
EVENTSVERS
     * @param strEnvKey the key of the environment value to return
     */

    public void getEnv(String strEnvKey, org.directwebremoting.ui.Callback<String> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getEnv", strEnvKey);

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, String.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Returns the settings of this server/project per config.xml
     */

    public jsx3.app.Settings getSettings()
    {
        String extension = "getSettings().";
        try
        {
            java.lang.reflect.Constructor<jsx3.app.Settings> ctor = jsx3.app.Settings.class.getConstructor(Context.class, String.class);
            return ctor.newInstance(this, extension);
        }
        catch (Exception ex)
        {
            throw new IllegalArgumentException("Unsupported type: " + jsx3.app.Settings.class.getName());
        }
    }


    /**
     * Returns handle to a descendant taskbar belonging to this server instance (this is where JSXDialog instances will try to minimize to if it exists); returns null if none found;
           if no taskbar is found, dialogs are not minimized, but are 'window shaded'Ñlike a Mac used to do
     * @param objJSX if null, this.JSXROOT is assumed; otherwise the object in the DOM from which to start looking for a descendant taskbar (a jsx3.gui.WindowBar instance)
     */

    public jsx3.gui.WindowBar getTaskBar(jsx3.app.Model objJSX)
    {
        String extension = "getTaskBar(\"" + objJSX + "\").";
        try
        {
            java.lang.reflect.Constructor<jsx3.gui.WindowBar> ctor = jsx3.gui.WindowBar.class.getConstructor(Context.class, String.class);
            return ctor.newInstance(this, extension);
        }
        catch (Exception ex)
        {
            throw new IllegalArgumentException("Unsupported type: " + jsx3.gui.WindowBar.class.getName());
        }
    }


    /**
     * call to shut down a server instance and free up resources used by the server (cache,dom,etc)
     */
    public void destroy()
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "destroy");
        ScriptSessions.addScript(script);
    }

    /**
     * Paints this application and its default component into the application view port on the host HTML page. The
system class loader calls this method once all the required resources of the application have loaded. The
order of actions taken by this method is:

  Load the default component file
  Execute the onload script for the application
  Paint the default component in the view port
     * @param objXML the pre-loaded default component document.
     */
    public void paint(jsx3.xml.CdfDocument objXML)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "paint", objXML);
        ScriptSessions.addScript(script);
    }

    /**
     * set all four dimensions for a jsx3.Server instance, allowing the developer to adjust the width/height/left/width for the server. Must be called during/after the onload event for the server instance as it affects the VIEW for the server.  Updates the absolutely positioned element that contains JSXROOT.
     * @param left the new left value or a JavaScript array containing all four new values (in pixels)
     * @param top the new top value (in pixels)
     * @param width the new width value (in pixels)
     * @param height the new height value (in pixels)
     */
    public void setDimensions(int left, int top, int width, int height)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setDimensions", left, top, width, height);
        ScriptSessions.addScript(script);
    }

    /**
     * set all four dimensions for a jsx3.Server instance, allowing the developer to adjust the width/height/left/width for the server. Must be called during/after the onload event for the server instance as it affects the VIEW for the server.  Updates the absolutely positioned element that contains JSXROOT.
     * @param left the new left value or a JavaScript array containing all four new values (in pixels)
     * @param top the new top value (in pixels)
     * @param width the new width value (in pixels)
     * @param height the new height value (in pixels)
     */
    public void setDimensions(Object[] left, int top, int width, int height)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setDimensions", left, top, width, height);
        ScriptSessions.addScript(script);
    }

    /**
     * Loads an external resource into this server instance. What this method does depends on the strType
parameter.


          script - Loads a JavaScript file asynchronously into the memory space of the page hosting this
      application; returns null.

          css - Loads a CSS file asynchronously into the memory space of the page hosting this
      application; returns null.

          xml or xsl - Loads an XML file synchronously into the XML cache of this
      application; returns the loaded jsx3.xml.Document instance.

          jss or ljss - Loads a dynamic properties file or localized properties bundle
      synchronously into this application; returns null.

          services - Loads and parses a mapping rules file synchronously; returns a new instance of
      jsx3.net.Service.
     * @param strSrc the path to the resource.
     * @param strId the unique identifier of the resource. A resource loaded by this method may clobber
   a previously loaded resource of the same type and id.
     * @param strType the type of include, one of: <code>css</code>, <code>jss</code>, <code>xml</code>,
   <code>xsl</code>, <code>script</code> (for JavaScript), <code>services</code> (for mapping rules),
   or <code>ljss</code>.
     * @param bReload if <code>true</code>, a JavaScript or CSS file is reloaded from the remote server
   without checking the local browser cache. Other types of resources are not affected by this parameter.
     * @return the return type depends on the <code>strType</code>
   parameter. See the method description.
     */

    public jsx3.xml.CdfDocument loadInclude(java.net.URI strSrc, String strId, String strType, String bReload)
    {
        String extension = "loadInclude(\"" + strSrc + "\", \"" + strId + "\", \"" + strType + "\", \"" + bReload + "\").";
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
     * Loads an external resource into this server instance. What this method does depends on the strType
parameter.


          script - Loads a JavaScript file asynchronously into the memory space of the page hosting this
      application; returns null.

          css - Loads a CSS file asynchronously into the memory space of the page hosting this
      application; returns null.

          xml or xsl - Loads an XML file synchronously into the XML cache of this
      application; returns the loaded jsx3.xml.Document instance.

          jss or ljss - Loads a dynamic properties file or localized properties bundle
      synchronously into this application; returns null.

          services - Loads and parses a mapping rules file synchronously; returns a new instance of
      jsx3.net.Service.
     * @param strSrc the path to the resource.
     * @param strId the unique identifier of the resource. A resource loaded by this method may clobber
   a previously loaded resource of the same type and id.
     * @param strType the type of include, one of: <code>css</code>, <code>jss</code>, <code>xml</code>,
   <code>xsl</code>, <code>script</code> (for JavaScript), <code>services</code> (for mapping rules),
   or <code>ljss</code>.
     * @param bReload if <code>true</code>, a JavaScript or CSS file is reloaded from the remote server
   without checking the local browser cache. Other types of resources are not affected by this parameter.
     * @param returnType The expected return type
     * @return the return type depends on the <code>strType</code>
   parameter. See the method description.
     */

    public <T> T loadInclude(java.net.URI strSrc, String strId, String strType, String bReload, Class<T> returnType)
    {
        String extension = "loadInclude(\"" + strSrc + "\", \"" + strId + "\", \"" + strType + "\", \"" + bReload + "\").";
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
     * Loads an external resource into this server instance. What this method does depends on the strType
parameter.


          script - Loads a JavaScript file asynchronously into the memory space of the page hosting this
      application; returns null.

          css - Loads a CSS file asynchronously into the memory space of the page hosting this
      application; returns null.

          xml or xsl - Loads an XML file synchronously into the XML cache of this
      application; returns the loaded jsx3.xml.Document instance.

          jss or ljss - Loads a dynamic properties file or localized properties bundle
      synchronously into this application; returns null.

          services - Loads and parses a mapping rules file synchronously; returns a new instance of
      jsx3.net.Service.
     * @param strSrc the path to the resource.
     * @param strId the unique identifier of the resource. A resource loaded by this method may clobber
   a previously loaded resource of the same type and id.
     * @param strType the type of include, one of: <code>css</code>, <code>jss</code>, <code>xml</code>,
   <code>xsl</code>, <code>script</code> (for JavaScript), <code>services</code> (for mapping rules),
   or <code>ljss</code>.
     * @param bReload if <code>true</code>, a JavaScript or CSS file is reloaded from the remote server
   without checking the local browser cache. Other types of resources are not affected by this parameter.
     * @return the return type depends on the <code>strType</code>
   parameter. See the method description.
     */

    public jsx3.xml.CdfDocument loadInclude(String strSrc, String strId, String strType, String bReload)
    {
        String extension = "loadInclude(\"" + strSrc + "\", \"" + strId + "\", \"" + strType + "\", \"" + bReload + "\").";
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
     * Loads an external resource into this server instance. What this method does depends on the strType
parameter.


          script - Loads a JavaScript file asynchronously into the memory space of the page hosting this
      application; returns null.

          css - Loads a CSS file asynchronously into the memory space of the page hosting this
      application; returns null.

          xml or xsl - Loads an XML file synchronously into the XML cache of this
      application; returns the loaded jsx3.xml.Document instance.

          jss or ljss - Loads a dynamic properties file or localized properties bundle
      synchronously into this application; returns null.

          services - Loads and parses a mapping rules file synchronously; returns a new instance of
      jsx3.net.Service.
     * @param strSrc the path to the resource.
     * @param strId the unique identifier of the resource. A resource loaded by this method may clobber
   a previously loaded resource of the same type and id.
     * @param strType the type of include, one of: <code>css</code>, <code>jss</code>, <code>xml</code>,
   <code>xsl</code>, <code>script</code> (for JavaScript), <code>services</code> (for mapping rules),
   or <code>ljss</code>.
     * @param bReload if <code>true</code>, a JavaScript or CSS file is reloaded from the remote server
   without checking the local browser cache. Other types of resources are not affected by this parameter.
     * @param returnType The expected return type
     * @return the return type depends on the <code>strType</code>
   parameter. See the method description.
     */

    public <T> T loadInclude(String strSrc, String strId, String strType, String bReload, Class<T> returnType)
    {
        String extension = "loadInclude(\"" + strSrc + "\", \"" + strId + "\", \"" + strType + "\", \"" + bReload + "\").";
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
     * Removes a loaded JavaScript or CSS resource from the browser DOM.
     * @param strId the id used when loading the resource.
     */
    public void unloadInclude(String strId)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "unloadInclude", strId);
        ScriptSessions.addScript(script);
    }

    /**
     * Loads an application resource. This method looks up a resource registered with this application by its id.
The resource must be registered in the config.xml file of this application.
     * @param strId unique identifier for the resource (its unique id as an application resource file).
     * @return the return type depends on the type of resource.
   See the documentation for <code>loadInclude()</code> for more information.
     */

    public jsx3.xml.CdfDocument loadResource(String strId)
    {
        String extension = "loadResource(\"" + strId + "\").";
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
     * Loads an application resource. This method looks up a resource registered with this application by its id.
The resource must be registered in the config.xml file of this application.
     * @param strId unique identifier for the resource (its unique id as an application resource file).
     * @param returnType The expected return type
     * @return the return type depends on the type of resource.
   See the documentation for <code>loadInclude()</code> for more information.
     */

    public <T> T loadResource(String strId, Class<T> returnType)
    {
        String extension = "loadResource(\"" + strId + "\").";
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
     * updates a single dynamic style property; dynamic properties are used by jsx3.gui.Block objects that extend the astract class, jsx3.gui.Block;
     * @param strPropName id for this dynamic property among all properties
     * @param vntValue value of the property; if null, the property with the name, @strPropName will be removed
     */
    public void setDynamicProperty(String strPropName, String vntValue)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setDynamicProperty", strPropName, vntValue);
        ScriptSessions.addScript(script);
    }

    /**
     * Returns the value of the dynamic property @strPropName
     * @param strPropName id for this dynamic property among all properties
     * @param strToken if present tokens such as {0}, {1}, {n} will be replaced with the nth element of this vararg array
     * @param callback value of the property
     */

    public void getDynamicProperty(String strPropName, String strToken, org.directwebremoting.ui.Callback<String> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getDynamicProperty", strPropName, strToken);

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, String.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Sets a Cookie with the given name and value
     * @param name name of the cookie
     * @param value value of the cookie
     * @param expires valid jscript date object. for example: new Date("11:59:59 12-31-2004")
     * @param path path where the cookie is valid (default: path of calling document)
     * @param domain domain where the cookie is valid (default: domain of calling document)
     * @param secure valid jscript date object. for example: new Date("11:59:59 12-31-2004")
     * @param bRaw
     */
    public void setCookie(String name, String value, java.util.Date expires, String path, String domain, boolean secure, boolean bRaw)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setCookie", name, value, expires, path, domain, secure, bRaw);
        ScriptSessions.addScript(script);
    }

    /**
     * Returns the value for the Cookie with the given @name
     * @param name name of the cookie
     * @param bRaw
     */

    public void getCookie(String name, boolean bRaw, org.directwebremoting.ui.Callback<String> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getCookie", name, bRaw);

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, String.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * delete a cookie
     * @param name name of the cookie
     * @param path path where the cookie is valid (default: path of calling document)
     * @param domain domain where the cookie is valid (default: domain of calling document)
     */
    public void deleteCookie(String name, String path, String domain)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "deleteCookie", name, path, domain);
        ScriptSessions.addScript(script);
    }

    /**
     * Returns the root block for this server (JSXROOT)
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
     * Returns the root block for this server (JSXROOT)
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
     * To implement jsx3.gui.Alerts interface.
     * @return the root block.
     */

    public jsx3.app.Model getAlertsParent()
    {
        String extension = "getAlertsParent().";
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
     * To implement jsx3.gui.Alerts interface.
     * @param returnType The expected return type
     * @return the root block.
     */

    public <T> T getAlertsParent(Class<T> returnType)
    {
        String extension = "getAlertsParent().";
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
     * Returns the body block for this server (JSXBODY)
     */

    public jsx3.gui.Block getBodyBlock()
    {
        String extension = "getBodyBlock().";
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
     * Returns the body block for this server (JSXBODY)
     * @param returnType The expected return type
     */

    public <T> T getBodyBlock(Class<T> returnType)
    {
        String extension = "getBodyBlock().";
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
     * Returns the list of objects that are children of the body object. These are the root objects
    in a serialization file and the root nodes in the Component Hierarchy palette.
     */

    public void getRootObjects(org.directwebremoting.ui.Callback<Object[]> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getRootObjects");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, Object[].class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Returns the XML/XSL cache for this server
     */

    public jsx3.app.Cache getCache()
    {
        String extension = "getCache().";
        try
        {
            java.lang.reflect.Constructor<jsx3.app.Cache> ctor = jsx3.app.Cache.class.getConstructor(Context.class, String.class);
            return ctor.newInstance(this, extension);
        }
        catch (Exception ex)
        {
            throw new IllegalArgumentException("Unsupported type: " + jsx3.app.Cache.class.getName());
        }
    }


    /**
     * Returns the DOM for this server
     */

    public jsx3.app.DOM getDOM()
    {
        String extension = "getDOM().";
        try
        {
            java.lang.reflect.Constructor<jsx3.app.DOM> ctor = jsx3.app.DOM.class.getConstructor(Context.class, String.class);
            return ctor.newInstance(this, extension);
        }
        catch (Exception ex)
        {
            throw new IllegalArgumentException("Unsupported type: " + jsx3.app.DOM.class.getName());
        }
    }


    /**
     * Looks up a DOM node owned by this server by id or by name.
     * @param strId either the id (_jsxid) of the object or its name (jsxname)
     * @return the JSX object or null if none found
     */

    public jsx3.app.Model getJSX(String strId)
    {
        String extension = "getJSX(\"" + strId + "\").";
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
     * Looks up a DOM node owned by this server by id or by name.
     * @param strId either the id (_jsxid) of the object or its name (jsxname)
     * @param returnType The expected return type
     * @return the JSX object or null if none found
     */

    public <T> T getJSX(String strId, Class<T> returnType)
    {
        String extension = "getJSX(\"" + strId + "\").";
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
     * Looks up a DOM node owned by this server by name. If more than one such objects exist, only one is returned.
     * @param strId the name (jsxname) of the object
     * @return the JSX object or null if none found
     */

    public jsx3.app.Model getJSXByName(String strId)
    {
        String extension = "getJSXByName(\"" + strId + "\").";
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
     * Looks up a DOM node owned by this server by name. If more than one such objects exist, only one is returned.
     * @param strId the name (jsxname) of the object
     * @param returnType The expected return type
     * @return the JSX object or null if none found
     */

    public <T> T getJSXByName(String strId, Class<T> returnType)
    {
        String extension = "getJSXByName(\"" + strId + "\").";
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
     * Looks up a DOM node owned by this server by id.
     * @param strId the id (_jsxid) of the object
     * @return the JSX object or null if none found
     */

    public jsx3.app.Model getJSXById(String strId)
    {
        String extension = "getJSXById(\"" + strId + "\").";
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
     * Looks up a DOM node owned by this server by id.
     * @param strId the id (_jsxid) of the object
     * @param returnType The expected return type
     * @return the JSX object or null if none found
     */

    public <T> T getJSXById(String strId, Class<T> returnType)
    {
        String extension = "getJSXById(\"" + strId + "\").";
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
     * Creates a new jsx3.gui.Window instance for this server. A branch of the DOM of this application can be placed
in this window in order to distribute the application across multiple browser windows.
     * @param strName the unique name of the window to create
     */

    public jsx3.gui.Window createAppWindow(String strName)
    {
        String extension = "createAppWindow(\"" + strName + "\").";
        try
        {
            java.lang.reflect.Constructor<jsx3.gui.Window> ctor = jsx3.gui.Window.class.getConstructor(Context.class, String.class);
            return ctor.newInstance(this, extension);
        }
        catch (Exception ex)
        {
            throw new IllegalArgumentException("Unsupported type: " + jsx3.gui.Window.class.getName());
        }
    }


    /**
     * Loads a new jsx3.gui.Window instance from a component file.
     * @param strSource either an XML document containing the window to load or the URL of the
   component to load.
     */

    public jsx3.gui.Window loadAppWindow(String strSource)
    {
        String extension = "loadAppWindow(\"" + strSource + "\").";
        try
        {
            java.lang.reflect.Constructor<jsx3.gui.Window> ctor = jsx3.gui.Window.class.getConstructor(Context.class, String.class);
            return ctor.newInstance(this, extension);
        }
        catch (Exception ex)
        {
            throw new IllegalArgumentException("Unsupported type: " + jsx3.gui.Window.class.getName());
        }
    }


    /**
     * Loads a new jsx3.gui.Window instance from a component file.
     * @param strSource either an XML document containing the window to load or the URL of the
   component to load.
     */

    public jsx3.gui.Window loadAppWindow(jsx3.xml.Node strSource)
    {
        String extension = "loadAppWindow(\"" + strSource + "\").";
        try
        {
            java.lang.reflect.Constructor<jsx3.gui.Window> ctor = jsx3.gui.Window.class.getConstructor(Context.class, String.class);
            return ctor.newInstance(this, extension);
        }
        catch (Exception ex)
        {
            throw new IllegalArgumentException("Unsupported type: " + jsx3.gui.Window.class.getName());
        }
    }


    /**
     * Retrieves a previously created jsx3.gui.Window instance.
     * @param strName the unique name of the window to retrieve
     * @return the window instance or <code>null</code> if no such window exists.
     */

    public jsx3.gui.Window getAppWindow(String strName)
    {
        String extension = "getAppWindow(\"" + strName + "\").";
        try
        {
            java.lang.reflect.Constructor<jsx3.gui.Window> ctor = jsx3.gui.Window.class.getConstructor(Context.class, String.class);
            return ctor.newInstance(this, extension);
        }
        catch (Exception ex)
        {
            throw new IllegalArgumentException("Unsupported type: " + jsx3.gui.Window.class.getName());
        }
    }


    /**
     * Returns the browser document object containing a particular JSX object. This method inspects whether the
JSX object is a descendent of the root block of this server or one of its jsx3.gui.Window roots.
     * @param objJSX
     * @param callback document object
     */

    public void getDocumentOf(jsx3.app.Model objJSX, org.directwebremoting.ui.Callback<String> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getDocumentOf", objJSX);

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, String.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Returns the browser DOM object where a particulat JSX object renders. This method inspects the main root of
this server as well as all of its jsx3.gui.Window roots.
     * @param objJSX
     * @param callback DOM object
     */

    public void getRenderedOf(jsx3.app.Model objJSX, org.directwebremoting.ui.Callback<String> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getRenderedOf", objJSX);

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, String.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Resolves a URI that is referenced from a file in this server. This method takes into account the changes in
resource addressing between 3.1 and 3.2. For version 3.1, the URI is resolved as any URI in the system, using
jsx3.resolveURI(). In version 3.2, the URI is taken as relative to the application folder. In
particular, a relative URI will be resolved to a base of the application folder, an absolute URI will be
unaffected.
     * @param strURI the URI to resolve.
     * @param callback the resolved URI.
     */

    public void resolveURI(String strURI, org.directwebremoting.ui.Callback<java.net.URI> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "resolveURI", strURI);

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, java.net.URI.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Resolves a URI that is referenced from a file in this server. This method takes into account the changes in
resource addressing between 3.1 and 3.2. For version 3.1, the URI is resolved as any URI in the system, using
jsx3.resolveURI(). In version 3.2, the URI is taken as relative to the application folder. In
particular, a relative URI will be resolved to a base of the application folder, an absolute URI will be
unaffected.
     * @param strURI the URI to resolve.
     * @param callback the resolved URI.
     */

    public void resolveURI(java.net.URI strURI, org.directwebremoting.ui.Callback<java.net.URI> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "resolveURI", strURI);

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, java.net.URI.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * 
     */

    public void getUriPrefix(org.directwebremoting.ui.Callback<String> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getUriPrefix");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, String.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * 
     * @param strURI the URI to relativize.
     * @param bRel
     * @param callback the relativized URI.
     */

    public void relativizeURI(java.net.URI strURI, boolean bRel, org.directwebremoting.ui.Callback<java.net.URI> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "relativizeURI", strURI, bRel);

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, java.net.URI.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * 
     * @param strURI the URI to relativize.
     * @param bRel
     * @param callback the relativized URI.
     */

    public void relativizeURI(String strURI, boolean bRel, org.directwebremoting.ui.Callback<java.net.URI> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "relativizeURI", strURI, bRel);

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, java.net.URI.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Returns the current locale of this server. If the locale has been set explicitly with setLocale(),
that locale is returned. Otherwise, getDefaultLocale() is consulted, and finally the system-wide
locale.
     */

    public void getLocale(org.directwebremoting.ui.Callback<java.util.Locale> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getLocale");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, java.util.Locale.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Sets the locale of this server.
     * @param objLocale
     */
    public void setLocale(java.util.Locale objLocale)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setLocale", objLocale);
        ScriptSessions.addScript(script);
    }

    /**
     * Returns the default locale of this server. This is configured with the default_locale configuration
setting.
     */

    public void getDefaultLocale(org.directwebremoting.ui.Callback<java.util.Locale> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getDefaultLocale");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, java.util.Locale.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Reloads all resource files that are localized. This method should be called after calling
setLocale() for the server to render properly in the new locale.
     */
    public void reloadLocalizedResources()
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "reloadLocalizedResources");
        ScriptSessions.addScript(script);
    }

    /**
     * Invokes context-sensitive help as though the user had pressed the help hot key in the context of the DOM node
objJSX.
     * @param objJSX
     */
    public void invokeHelp(jsx3.app.Model objJSX)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "invokeHelp", objJSX);
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
    public void subscribe(String strEventId, jsx3.lang.Object objHandler, org.directwebremoting.ui.CodeBlock objFunction)
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
    public void unsubscribe(String strEventId, String objHandler)
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
    public void unsubscribe(String strEventId, jsx3.lang.Object objHandler)
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
     * show an alert dialog
     * @param strTitle the title of the dialog
     * @param strMessage the message to display
     * @param fctOnOk callback function on pressing ok button, receives the dialog as an argument; if null the dialog will close itself; if defined must explicitly close the dialog
     * @param strOk the text of the ok button, can be false to remove button from display
     * @param objParams argument to configureAlert()
     */
    public void alert(String strTitle, String strMessage, org.directwebremoting.ui.CodeBlock fctOnOk, String strOk, String objParams)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "alert", strTitle, strMessage, fctOnOk, strOk, objParams);
        ScriptSessions.addScript(script);
    }

    /**
     * configure the dialog
     * @param objDialog the dialog
     * @param objParams may include fields 'width', 'height', 'noTitle', and 'nonModal'.
     */
    public void configureAlert(java.lang.Object objDialog, jsx3.lang.Object objParams)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "configureAlert", objDialog, objParams);
        ScriptSessions.addScript(script);
    }

    /**
     * show a confirm alert
     * @param strTitle the title of the dialog
     * @param strMessage the message to display
     * @param fctOnOk callback function on pressing ok button, receives the dialog as an argument; if null the dialog will close itself; if defined must explicitly close the dialog
     * @param fctOnCancel callback function on pressing cancel button, receives the dialog as an argument; if null the dialog will close itself; if defined must explicitly close the dialog
     * @param strOk the text of the ok button
     * @param strCancel the text of the cancel button
     * @param intBtnDefault the bold button that receives return key, 1:ok, 2:cancel, 3:no
     * @param fctOnNo callback function on pressing no button, receives the dialog as an argument; if null the dialog will close itself; if defined must explicitly close the dialog
     * @param strNo the text of the no button
     * @param objParams argument to configureAlert()
     */
    public void confirm(String strTitle, String strMessage, org.directwebremoting.ui.CodeBlock fctOnOk, org.directwebremoting.ui.CodeBlock fctOnCancel, String strOk, String strCancel, int intBtnDefault, org.directwebremoting.ui.CodeBlock fctOnNo, String strNo, String objParams)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "confirm", strTitle, strMessage, fctOnOk, fctOnCancel, strOk, strCancel, intBtnDefault, fctOnNo, strNo, objParams);
        ScriptSessions.addScript(script);
    }

    /**
     * show a text box input prompt
     * @param strTitle the title of the dialog
     * @param strMessage the message to display
     * @param fctOnOk callback function on pressing ok button, receives the dialog as an argument, and the value of the text input as a second argument; if null the dialog will close itself; if defined must explicitly close the dialog
     * @param fctOnCancel callback function on pressing cancel button, receives the dialog as an argument; if null the dialog will close itself; if defined must explicitly close the dialog
     * @param strOk the text of the ok button
     * @param strCancel the text of the cancel button
     * @param objParams argument to configureAlert()
     */
    public void prompt(String strTitle, String strMessage, org.directwebremoting.ui.CodeBlock fctOnOk, org.directwebremoting.ui.CodeBlock fctOnCancel, String strOk, String strCancel, String objParams)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "prompt", strTitle, strMessage, fctOnOk, fctOnCancel, strOk, strCancel, objParams);
        ScriptSessions.addScript(script);
    }

}

