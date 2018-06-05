package com.example.dwr.reverseajax;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.directwebremoting.Browser;
import org.directwebremoting.ScriptSession;
import org.directwebremoting.ScriptSessionFilter;
import org.directwebremoting.ServerContextFactory;
import org.directwebremoting.WebContextFactory;
import org.directwebremoting.extend.UninitializingBean;
import org.directwebremoting.ui.dwr.Util;

import com.example.dwr.people.Person;

public class PeopleTable implements Runnable, UninitializingBean
{

    public PeopleTable()
    {
        executor = new ScheduledThreadPoolExecutor(1);
        executor.scheduleAtFixedRate(this, 1, 10, TimeUnit.SECONDS);
    }

    /* (non-Javadoc)
     * @see java.lang.Runnable#run()
     */
    public void run()
    {
        updateTableDisplay();
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.extend.UninitializingBean#destroy()
     */
    public void destroy()
    {
        executor.shutdown();
        try {
            executor.awaitTermination(1, TimeUnit.SECONDS);
        } catch (InterruptedException ex) {}
    }

    public void updateTableDisplay()
    {
        String page = ServerContextFactory.get().getContextPath() + "/reverseajax/peopleTable.html";
        ScriptSessionFilter attributeFilter = new AttributeScriptSessionFilter(SCRIPT_SESSION_ATTR);
        Browser.withPageFiltered(page, attributeFilter, new Runnable()
        {
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
        public boolean match(ScriptSession session)
        {
            Object check = session.getAttribute(attributeName);
            return (check != null && check.equals(Boolean.TRUE));
        }

        private final String attributeName;
    }

    private final static String SCRIPT_SESSION_ATTR = "SCRIPT_SESSION_ATTR";

    private final ScheduledThreadPoolExecutor executor;
}
