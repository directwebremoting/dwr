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
 * Read-Only system settings interface.
 * @author Joe Walker [joe at getahead dot org]
 * @author DRAPGEN - Dwr Reverse Ajax Proxy GENerator
 */
public class Settings extends jsx3.lang.Object
{
    /**
     * All reverse ajax proxies need context to work from
     * @param context The script that got us to where we are now
     */
    public Settings(Context context, String extension)
    {
        super(context, extension);
    }

    /**
     * The instance initializer. Creates a view onto the settings persisted on disk. All identical instances of this
class are backed by the same XML source document.
     * @param intDomain the domain of the settings to load, one of <code>jsx3.app.Settings.DOMAIN</code>...
     * @param objInstance if in the project or addin domain, the key of the specific project or addin to load settings for
     */
    public Settings(int intDomain, String objInstance)
    {
        super((Context) null, (String) null);
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall("new Settings", intDomain, objInstance);
        setInitScript(script);
    }

    /**
     * The instance initializer. Creates a view onto the settings persisted on disk. All identical instances of this
class are backed by the same XML source document.
     * @param intDomain the domain of the settings to load, one of <code>jsx3.app.Settings.DOMAIN</code>...
     * @param objInstance if in the project or addin domain, the key of the specific project or addin to load settings for
     */
    public Settings(jsx3.xml.CdfDocument intDomain, jsx3.lang.Object objInstance)
    {
        super((Context) null, (String) null);
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall("new Settings", intDomain, objInstance);
        setInitScript(script);
    }

    /**
     * The instance initializer. Creates a view onto the settings persisted on disk. All identical instances of this
class are backed by the same XML source document.
     * @param intDomain the domain of the settings to load, one of <code>jsx3.app.Settings.DOMAIN</code>...
     * @param objInstance if in the project or addin domain, the key of the specific project or addin to load settings for
     */
    public Settings(jsx3.xml.CdfDocument intDomain, String objInstance)
    {
        super((Context) null, (String) null);
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall("new Settings", intDomain, objInstance);
        setInitScript(script);
    }

    /**
     * The instance initializer. Creates a view onto the settings persisted on disk. All identical instances of this
class are backed by the same XML source document.
     * @param intDomain the domain of the settings to load, one of <code>jsx3.app.Settings.DOMAIN</code>...
     * @param objInstance if in the project or addin domain, the key of the specific project or addin to load settings for
     */
    public Settings(int intDomain, jsx3.lang.Object objInstance)
    {
        super((Context) null, (String) null);
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall("new Settings", intDomain, objInstance);
        setInitScript(script);
    }


    /**
     * 
     */
    public static final int DOMAIN_PROJECT = 2;

    /**
     * 
     */
    public static final int DOMAIN_ADDIN = 3;


    /**
     * Returns a stored setting value.
     * @param strKey the setting key.
     * @param callback the stored value.
     */

    public void get(String strKey, org.directwebremoting.ui.Callback<String> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "get", strKey);

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, String.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Returns a stored setting value as the raw XML node.
     * @param strKey the setting key.
     */

    public jsx3.xml.Node getNode(String strKey)
    {
        String extension = "getNode(\"" + strKey + "\").";
        try
        {
            java.lang.reflect.Constructor<jsx3.xml.Node> ctor = jsx3.xml.Node.class.getConstructor(Context.class, String.class);
            return ctor.newInstance(this, extension);
        }
        catch (Exception ex)
        {
            throw new IllegalArgumentException("Unsupported type: " + jsx3.xml.Node.class.getName());
        }
    }


}

