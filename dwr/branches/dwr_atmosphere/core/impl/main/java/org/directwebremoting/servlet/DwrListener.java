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
package org.directwebremoting.servlet;

import java.util.List;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.directwebremoting.Container;
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
            container.contextDestroyed();
        }
    }

    /**
     * The log stream
     */
    private static final Log log = LogFactory.getLog(DwrListener.class);
}
