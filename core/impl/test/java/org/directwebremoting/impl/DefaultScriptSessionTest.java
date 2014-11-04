package org.directwebremoting.impl;

import org.directwebremoting.ScriptSession;
import org.directwebremoting.ScriptSessionFilter;
import org.directwebremoting.event.ScriptSessionBindingEvent;
import org.directwebremoting.event.ScriptSessionBindingListener;
import org.junit.Test;

import static org.junit.Assert.*;

public class DefaultScriptSessionTest
{
    int callCount = 0;

    DefaultScriptSession scriptSession;

    @Test
    public void testGetAttribute_infiniteLoopOnInvalidate() throws Exception
    {
        try
        {
            givenBindingListenerInScriptSession();
            whenGetAttributeIsCalledDuringInvalidation();
            thenScriptSessionEndsUpInEndlessLoop();
        }
        catch (StackOverflowError stackOverflowError)
        {
            stackOverflowError.printStackTrace();
            fail("BUG: Should not end up in infinite loop");
        }
    }

    private void givenBindingListenerInScriptSession()
    {
        MycoScriptSessionManager customManager = new MycoScriptSessionManager();
        ScriptSessionBindingListener listener = new MycoSuite(customManager);
        scriptSession = new DefaultScriptSession("1234", customManager, "/myco", null, false);

        /**
         * Avoid any side effects. Directly add to map, as to simulate a longer
         * running script session.
         */
        customManager.sessionMap.put("not_important", scriptSession);
        scriptSession.attributes.put("attributeThatNeedsToMatchInScriptSessionFilter", listener);
    }

    private void whenGetAttributeIsCalledDuringInvalidation()
    {
        callCount++;
        System.out.println("Called " + callCount + " times");
        scriptSession.invalidate();
    }

    private void thenScriptSessionEndsUpInEndlessLoop()
    {
        System.out.println("Will not get here, since invalidate() will go into stackoverflow");
    }

    /**
     * Assumes that one user can have multiple windows/tabs open and could
     * be browsing the website. Whenever the <i>last</i> script session of a user
     * is invalidated, the application needs to do something special.
     */
    public class MycoScriptSessionManager extends DefaultScriptSessionManager
    {

        /**
         * @return Always a time in the past to simulate an expired script session. This
         * will make sure that {@link org.directwebremoting.impl.DefaultScriptSession#invalidateIfNeeded()}
         * will fire another invalidate (exactly how it happens in the real life example).
         */
        @Override
        public long getScriptSessionTimeout()
        {
            return -1000;
        }

        public boolean hasScriptSessions(ScriptSessionFilter filter)
        {
            for (ScriptSession scriptSess : getAllScriptSessions())
            {
                if (filter.match(scriptSess))
                {
                    return true;
                }
            }
            return false;
        }
    }

    /**
     * A binding listener that on valueUnbound() will ask the scriptsession
     * manager if there are any other scriptsessions that have the exact
     * same listener.
     */
    public class MycoSuite implements ScriptSessionBindingListener {
        /**
         * Fake ID
         */
        private final int id = 1;

        public MycoSuite(MycoScriptSessionManager scriptSessionManager)
        {
            this.scriptSessionManager = scriptSessionManager;
        }

        public void valueBound(ScriptSessionBindingEvent event)
        {
        }

        public void valueUnbound(ScriptSessionBindingEvent event)
        {
            /**
             * Normally does something special if there are no
             * script sessions for this listener. For testing purposes,
             * we just make a simple call
             */
            scriptSessionManager.hasScriptSessions(new ScriptSessionFilter()
            {
                public boolean match(ScriptSession session)
                {
                    MycoSuite mycoSuite = (MycoSuite) session.getAttribute("attributeThatNeedsToMatchInScriptSessionFilter");
                    if (mycoSuite == null) {
                        return false;
                    }

                    return id == mycoSuite.id;

                }
            });
        }

        private final MycoScriptSessionManager scriptSessionManager;
    }

}
