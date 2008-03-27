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
package uk.ltd.getahead.dwrdemo.cobrowse;

import java.util.Collection;

import org.directwebremoting.ScriptBuffer;
import org.directwebremoting.WebContext;
import org.directwebremoting.WebContextFactory;
import org.directwebremoting.proxy.ScriptProxy;
import org.directwebremoting.proxy.dwr.DwrUtil;

/**
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class CoBrowse
{
    /**
     * @param url The url to make everyone jump to
     */
    public void jump(String url)
    {
        WebContext wctx = WebContextFactory.get();
        String currentPage = wctx.getCurrentPage();

        // For all the browsers on the current page:
        Collection sessions = wctx.getScriptSessionsByPage(currentPage);

        ScriptProxy all = new ScriptProxy(sessions);
        all.addScript(new ScriptBuffer("$('cobrowseIframe').src = '" + url + "';"));

        sessions.remove(wctx.getScriptSession());
        DwrUtil utilAll = new DwrUtil(sessions);
        utilAll.setValue("cobrowseUrl", url);
    }
}
