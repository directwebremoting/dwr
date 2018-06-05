package org.directwebremoting.impl;

import java.util.concurrent.ScheduledThreadPoolExecutor;

import javax.servlet.ServletContextListener;

import org.directwebremoting.extend.UninitializingBean;

/**
 * Just a standard ScheduledThreadPoolExecutor with a single default thread in
 * the pool (we're not doing heavy scheduling) that is also a
 * {@link ServletContextListener} so the {@link org.directwebremoting.Container}
 * can shut us down.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class AutoShutdownScheduledThreadPoolExecutor extends ScheduledThreadPoolExecutor implements UninitializingBean
{
    /**
     * This is generally used as an event timer, so we don't need more than one
     * thread running at a time.
     */
    public AutoShutdownScheduledThreadPoolExecutor()
    {
        super(1, new DaemonThreadFactory());
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.extend.UninitializingBean#destroy()
     */
    public void destroy()
    {
        this.shutdownNow();
    }
}
