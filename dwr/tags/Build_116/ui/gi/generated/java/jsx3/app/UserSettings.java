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
 * Read-Write per-User settings for a particular GI application (server).

This implementation stores settings in a browser cookie.
 * @author Joe Walker [joe at getahead dot org]
 * @author DRAPGEN - Dwr Reverse Ajax Proxy GENerator
 */
public class UserSettings extends jsx3.lang.Object
{
    /**
     * All reverse ajax proxies need context to work from
     * @param context The script that got us to where we are now
     */
    public UserSettings(Context context, String extension)
    {
        super(context, extension);
    }

    /**
     * The instance initializer.
     * @param objServer the app server.
     * @param intPersistence the persistence code, defaults to <code>PERSIST_INDEFINITE</code>.
     */
    public UserSettings(jsx3.app.Server objServer, int intPersistence)
    {
        super((Context) null, (String) null);
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall("new UserSettings", objServer, intPersistence);
        setInitScript(script);
    }


    /**
     * 
     */
    public static final int PERSIST_SESSION = 1;

    /**
     * 
     */
    public static final int PERSIST_INDEFINITE = 2;


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
     * Sets a stored setting value.
     * @param strKey the setting key.
     * @param value the value to store.
     */
    public void set(String strKey, Object[] value)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "set", strKey, value);
        ScriptSessions.addScript(script);
    }

    /**
     * Sets a stored setting value.
     * @param strKey the setting key.
     * @param value the value to store.
     */
    public void set(String strKey, jsx3.lang.Object value)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "set", strKey, value);
        ScriptSessions.addScript(script);
    }

    /**
     * Sets a stored setting value.
     * @param strKey the setting key.
     * @param value the value to store.
     */
    public void set(String strKey, Integer value)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "set", strKey, value);
        ScriptSessions.addScript(script);
    }

    /**
     * Sets a stored setting value.
     * @param strKey the setting key.
     * @param value the value to store.
     */
    public void set(String strKey, String value)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "set", strKey, value);
        ScriptSessions.addScript(script);
    }

    /**
     * Sets a stored setting value.
     * @param strKey the setting key.
     * @param value the value to store.
     */
    public void set(String strKey, boolean value)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "set", strKey, value);
        ScriptSessions.addScript(script);
    }

    /**
     * Removes a stored setting value.
     * @param strKey the key of the setting to remove.
     */
    public void remove(String strKey)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "remove", strKey);
        ScriptSessions.addScript(script);
    }

    /**
     * Clears all settings of this user settings instance. This implementation deletes the cookie.
     */
    public void clear()
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "clear");
        ScriptSessions.addScript(script);
    }

    /**
     * Persists the user settings. Any modifications to this user settings instance will be lost if this method
is not called.
     */
    public void save()
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "save");
        ScriptSessions.addScript(script);
    }

}

