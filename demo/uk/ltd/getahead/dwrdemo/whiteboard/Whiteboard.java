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
package uk.ltd.getahead.dwrdemo.whiteboard;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.directwebremoting.ScriptSession;
import org.directwebremoting.WebContext;
import org.directwebremoting.WebContextFactory;
import org.directwebremoting.proxy.dwr.DwrUtil;

/**
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class Whiteboard
{
    /**
     * Update the text field for everyone else
     * @param text The text to publish
     */
    public void update(String text)
    {
        WebContext wctx = WebContextFactory.get();
        String currentPage = wctx.getCurrentPage();

        Collection sessions = wctx.getScriptSessionsByPage(currentPage);

        sessions.remove(wctx.getScriptSession());
        DwrUtil utilAll = new DwrUtil(sessions);
        utilAll.setValue("whiteboardSource", text, false);
    }

    /**
     * Claim to be the person editing
     * @param value Am I an editor?
     */
    public void claim(boolean value)
    {
        WebContext wctx = WebContextFactory.get();
        String ipaddr = wctx.getHttpServletRequest().getRemoteAddr();

        ScriptSession scriptSession = wctx.getScriptSession();
        scriptSession.setAttribute("ipaddr", ipaddr);
        scriptSession.setAttribute("claim", new Boolean(value));

        String currentPage = wctx.getCurrentPage();

        Collection sessions = wctx.getScriptSessionsByPage(currentPage);

        List output = new ArrayList();
        for (Iterator it = sessions.iterator(); it.hasNext();)
        {
            ScriptSession session = (ScriptSession) it.next();
            Map map = new HashMap();
            map.put("ipaddr", session.getAttribute("ipaddr"));
            map.put("claim", session.getAttribute("claim"));
            output.add(map);
        }

        DwrUtil utilAll = new DwrUtil(sessions);
        utilAll.removeAllOptions("whiteboardUsers");
        utilAll.addOptions("whiteboardUsers", output, "ipaddr");
    }
}
