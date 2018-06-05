package org.directwebremoting.impl;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContextListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.directwebremoting.extend.ServerLoadMonitor;
import org.directwebremoting.extend.UninitializingBean;
import org.directwebremoting.extend.WaitController;

/**
 * A base implementation of {@link ServerLoadMonitor} that implements waiting
 * functionality, mostly to provide {@link ServletContextListener#contextDestroyed}
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public abstract class AbstractServerLoadMonitor implements ServerLoadMonitor, UninitializingBean
{
    /* (non-Javadoc)
     * @see org.directwebremoting.extend.UninitializingBean#destroy()
     */
    public void destroy()
    {
        if (shutdownCalled)
        {
            return;
        }

        synchronized (waitControllers)
        {
            List<WaitController> copy = new ArrayList<WaitController>();
            copy.addAll(waitControllers);

            for (WaitController controller : copy)
            {
                controller.shutdown();
            }

            log.debug(" - shutdown on: " + this);
            shutdownCalled = true;
        }
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.extend.ServerLoadMonitor#threadWaitStarting(org.directwebremoting.extend.WaitController)
     */
    public void threadWaitStarting(WaitController controller)
    {
        synchronized (waitControllers)
        {
            waitControllers.add(controller);
        }
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.extend.ServerLoadMonitor#threadWaitEnding(org.directwebremoting.extend.WaitController)
     */
    public void threadWaitEnding(WaitController controller)
    {
        synchronized (waitControllers)
        {
            waitControllers.remove(controller);
        }
    }

    /**
     * If there are too many WaitControllers waiting then we can kill one off at
     * random.
     * @param count How many {@link WaitController}s do we shutdown?
     */
    public void shutdownRandomWaitControllers(int count)
    {
        synchronized (waitControllers)
        {
            for (int i = 0; i < count && !waitControllers.isEmpty(); i++)
            {
                waitControllers.get(0).shutdown();
            }
        }
    }

    /**
     * Have we been shutdown already?
     */
    private boolean shutdownCalled = false;

    /**
     * The known wait controllers
     */
    protected final List<WaitController> waitControllers = new ArrayList<WaitController>();

    /**
     * The log stream
     */
    private static final Log log = LogFactory.getLog(AbstractServerLoadMonitor.class);
}
