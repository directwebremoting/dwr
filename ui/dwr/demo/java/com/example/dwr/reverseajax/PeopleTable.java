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
package com.example.dwr.reverseajax;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.directwebremoting.Browser;
import org.directwebremoting.ScriptSession;
import org.directwebremoting.ScriptSessionFilter;
import org.directwebremoting.ServerContextFactory;
import org.directwebremoting.WebContextFactory;
import org.directwebremoting.impl.DaemonThreadFactory;
import org.directwebremoting.ui.dwr.Util;

import com.example.dwr.people.Person;

public class PeopleTable implements Runnable
{

    public PeopleTable()
    {
        ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(1, new DaemonThreadFactory());
        executor.scheduleAtFixedRate(this, 1, 30, TimeUnit.SECONDS);
    }

    /* (non-Javadoc)
     * @see java.lang.Runnable#run()
     */
    @Override
    public void run()
    {
        updateTableDisplay();
    }

    public void updateTableDisplay()
    {
        String page = ServerContextFactory.get().getContextPath() + "/reverseajax/peopleTable.html";
        ScriptSessionFilter attributeFilter = new AttributeScriptSessionFilter(SCRIPT_SESSION_ATTR);
        Browser.withPageFiltered(page, attributeFilter, new Runnable()
        {
            @Override
            public void run()
            {
                Person person = new Person(true);
                String[][] data = {
                    {person.getId(), person.getName(), person.getAddress(), person.getAge()+"", person.isSuperhero()+""}
                };
                Util.addRows("peopleTable", data);
            }
        });
    }

    public void addAttributeToScriptSession() {
        ScriptSession scriptSession = WebContextFactory.get().getScriptSession();
        scriptSession.setAttribute(SCRIPT_SESSION_ATTR, true);
    }

    public void removeAttributeToScriptSession() {
        ScriptSession scriptSession = WebContextFactory.get().getScriptSession();
        scriptSession.removeAttribute(SCRIPT_SESSION_ATTR);
    }

    protected class AttributeScriptSessionFilter implements ScriptSessionFilter
    {
        public AttributeScriptSessionFilter(String attributeName)
        {
            this.attributeName = attributeName;
        }

        /* (non-Javadoc)
         * @see org.directwebremoting.ScriptSessionFilter#match(org.directwebremoting.ScriptSession)
         */
        @Override
        public boolean match(ScriptSession session)
        {
            Object check = session.getAttribute(attributeName);
            return (check != null && check.equals(Boolean.TRUE));
        }

        private final String attributeName;
    }

    private final static String SCRIPT_SESSION_ATTR = "SCRIPT_SESSION_ATTR";
}
