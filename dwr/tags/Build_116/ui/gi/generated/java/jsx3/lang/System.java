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
package jsx3.lang;

import org.directwebremoting.ScriptBuffer;
import org.directwebremoting.ScriptSessions;
import org.directwebremoting.io.Context;

/**
 * A collection of GI system related functions.
 * @author Joe Walker [joe at getahead dot org]
 * @author DRAPGEN - Dwr Reverse Ajax Proxy GENerator
 */
public class System extends jsx3.lang.Object
{
    /**
     * All reverse ajax proxies need context to work from
     * @param context The script that got us to where we are now
     */
    public System(Context context, String extension)
    {
        super(context, extension);
    }



    /**
     * Returns a system property as specified by a JSS file loaded by the JSX runtime or an addin.
     * @param strKey
     */

    public void getProperty(String strKey, org.directwebremoting.ui.Callback<String> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getProperty", strKey);

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, String.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * If the locale has been explicitly set with setLocale(), that locale is returned. Otherwise the
locale is determined by introspecting the browser. If all else fails the default locale, en_US, is returned.
     */
    public void getLocale()
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "getLocale");
        ScriptSessions.addScript(script);
    }

    /**
     * Sets the system-wide locale. This in turn affects all applications running under the JSX system.
     * @param objLocale
     */
    public void setLocale(java.lang.Object objLocale)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setLocale", objLocale);
        ScriptSessions.addScript(script);
    }

    /**
     * 
     */
    public void reloadLocalizedResources()
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "reloadLocalizedResources");
        ScriptSessions.addScript(script);
    }

    /**
     * 
     * @param strKey
     * @param strTokens
     */

    public void getMessage(String strKey, jsx3.lang.Object strTokens, org.directwebremoting.ui.Callback<String> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getMessage", strKey, strTokens);

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, String.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Returns the version number of General Interface.
     * @param callback <code>"3.1.0"</code>, etc.
     */

    public void getVersion(org.directwebremoting.ui.Callback<String> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getVersion");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, String.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

}

