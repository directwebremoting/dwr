package org.directwebremoting.filter;

import java.lang.reflect.Method;

import org.directwebremoting.AjaxFilter;
import org.directwebremoting.AjaxFilterChain;
import org.directwebremoting.ScriptBuffer;
import org.directwebremoting.ScriptSession;
import org.directwebremoting.WebContextFactory;
import org.directwebremoting.io.DwrConvertedException;

/**
 * {@link ScriptSession}s are timed out based on total user activity, which
 * includes reverse ajax calls. You may wish to timeout a ScriptSession while
 * the user is still on the page if they have no taken any actions in the last
 * 'n' seconds.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class ActionTimeoutAjaxFilter implements AjaxFilter
{
    /* (non-Javadoc)
     * @see org.directwebremoting.AjaxFilter#doFilter(java.lang.Object, java.lang.reflect.Method, java.lang.Object[], org.directwebremoting.AjaxFilterChain)
     */
    public Object doFilter(Object obj, Method method, Object[] params, AjaxFilterChain chain) throws Exception
    {
        ScriptSession session = WebContextFactory.get().getScriptSession();
        Long lastAccess = (Long) session.getAttribute(ATTRIBUTE_LAST_ACTION);

        // Free pass in if you've not done anything so far.
        if (lastAccess != null)
        {
            long now = System.currentTimeMillis();
            if (now > lastAccess + actionTimeoutMillis)
            {
                session.addScript(new ScriptBuffer(onTimeout));
                session.invalidate();
                throw new DwrConvertedException("Your session has timed out");
            }
        }

        Object reply = chain.doFilter(obj, method, params);

        session.setAttribute(ATTRIBUTE_LAST_ACTION, System.currentTimeMillis());

        return reply;
    }

    /**
     * The time after which we detect out of date ScriptSessions, and time
     * them out.
     */
    public void setActionTimeoutMillis(long actionTimeoutMillis)
    {
        this.actionTimeoutMillis = actionTimeoutMillis;
    }

    /**
     * @see #setActionTimeoutMillis(long)
     */
    private long actionTimeoutMillis;

    /**
     * What do we ask the browser to do when the timeout happens?
     */
    public void setOnTimeout(String onTimeout)
    {
        this.onTimeout = onTimeout;
    }

    /**
     * @see #setOnTimeout(String)
     */
    private String onTimeout = "window.setLocation('about:blank');";

    /**
     * The attribute under which we store the last action time.
     */
    private static final String ATTRIBUTE_LAST_ACTION = "org.directwebremoting.filter.ActionTimeoutAjaxFilter.lastActionTime";
}
