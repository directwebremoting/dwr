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
package org.directwebremoting.ui.browser;

import javax.servlet.http.Cookie;

import org.directwebremoting.ScriptSessions;
import org.directwebremoting.util.JavascriptUtil;

/**
 * A copy of some of the functions from the Document DOM object on the server
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class Document
{
    /**
     * Add a cookie to a remote browser/browsers
     */
    public static void setCookie(String name, String value)
    {
        ScriptSessions.addScript(("document.cookie = \"" + JavascriptUtil.escapeJavaScript(name) + "=" + JavascriptUtil.escapeJavaScript(value) + "\""));
    }

    /**
     * Add a cookie to a remote browser/browsers
     */
    public static void setCookie(Cookie cookie)
    {
        String name = JavascriptUtil.escapeJavaScript(cookie.getName());
        String value = JavascriptUtil.escapeJavaScript(cookie.getValue());
        String domain = cookie.getDomain() == null ? "" : ";domain=" + JavascriptUtil.escapeJavaScript(cookie.getDomain());
        String path = cookie.getPath() == null ? "" : ";path=" + JavascriptUtil.escapeJavaScript(cookie.getPath());
        String maxAge = cookie.getMaxAge() == -1 ? "" : ";max-age=" + cookie.getMaxAge();
        String secure = !cookie.getSecure() ? "" : ";secure=true";

        ScriptSessions.addScript(("document.cookie = \"" + name + "=" + value + domain + path + maxAge + secure + "\""));
    }

    /**
     * This would be good, but it doesn't work on some browsers
     * @param title The page to navigate to
    */
    public static void setTitle(String title)
    {
        ScriptSessions.addScript(("document.title = '" + JavascriptUtil.escapeJavaScript(title) + "';"));
    }
}
