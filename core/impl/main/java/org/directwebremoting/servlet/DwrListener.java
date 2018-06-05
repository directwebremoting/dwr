package org.directwebremoting.servlet;

import java.util.List;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.directwebremoting.Container;
import org.directwebremoting.WebContextFactory.WebContextBuilder;
import org.directwebremoting.impl.StartupUtil;

/**
 * A {@link ServletContextListener} that can be used to pass the events on to
 * everything in DWRs {@link Container}.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class DwrListener implements ServletContextListener
{
    /**
     * This happens before the DwrServlet has started so there is nothing
     * we can do.
     */
    public void contextInitialized(ServletContextEvent ev)
    {
    }

    /**
     * Find all the containers that have been registered, and check all the
     * contained beans for ones that implement {@link ServletContextListener}
     * and pass the {@link ServletContextListener#contextDestroyed} event on.
     * @param ev The event object to pass on
     * @see javax.servlet.ServletContextListener#contextDestroyed(javax.servlet.ServletContextEvent)
     */
    public void contextDestroyed(ServletContextEvent ev)
    {
        List<Container> containers = StartupUtil.getAllPublishedContainers(ev.getServletContext());

        if (containers.isEmpty())
        {
            log.debug("No containers to shutdown");
            return;
        }

        for (Container container : containers)
        {
            WebContextBuilder webContextBuilder = container.getBean(WebContextBuilder.class);
            if (webContextBuilder != null)
            {
                webContextBuilder.engageThread(container, null, null);
            }
            container.destroy();
            if (webContextBuilder != null)
            {
                webContextBuilder.disengageThread();
            }
        }
    }

    /**
     * The log stream
     */
    private static final Log log = LogFactory.getLog(DwrListener.class);
}
