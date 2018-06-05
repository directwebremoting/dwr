package org.directwebremoting.filter;

import java.lang.reflect.Method;

import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;
import org.directwebremoting.AjaxFilter;
import org.directwebremoting.AjaxFilterChain;

/**
 * An example filter that delays responding to a query by a customizable time
 * to simulate Internet latency. Half of the delay is inserted before and half
 * after the invocation.
 * <p>The default delay is 100 milliseconds.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class ExtraLatencyAjaxFilter implements AjaxFilter
{
    /* (non-Javadoc)
     * @see org.directwebremoting.AjaxFilter#doFilter(java.lang.Object, java.lang.reflect.Method, java.lang.Object[], org.directwebremoting.AjaxFilterChain)
     */
    public Object doFilter(Object obj, Method method, Object[] params, AjaxFilterChain chain) throws Exception
    {
        try
        {
            Thread.sleep(delay/2);
        }
        catch (InterruptedException ex)
        {
            log.warn("Pre-exec interuption", ex);
        }

        Object reply = chain.doFilter(obj, method, params);

        try
        {
            Thread.sleep(delay/2);
        }
        catch (InterruptedException ex)
        {
            log.warn("Post-exec interuption", ex);
        }

        return reply;
    }

    /**
     * @return Returns the delay in milliseconds.
     */
    public long getDelay()
    {
        return delay;
    }

    /**
     * @param delay The delay to set.
     */
    public void setDelay(long delay)
    {
        this.delay = delay;
    }

    /**
     * The delay time in milliseconds.
     * We wait for half this value before and half after the call
     */
    private long delay = 100;

    /**
     * The log stream
     */
    private static final Log log = LogFactory.getLog(ExtraLatencyAjaxFilter.class);
}
