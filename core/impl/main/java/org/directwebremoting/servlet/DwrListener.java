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

import java.util.Collection;
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
     * Find all the containers that have been registered, and check all the
     * contained beans for ones that implement {@link ServletContextListener}
     * and pass the {@link ServletContextListener#contextDestroyed} event on.
     * @param ev The event object to pass on
     * @see javax.servlet.ServletContextListener#contextInitialized(javax.servlet.ServletContextEvent)
     */
    public void contextInitialized(ServletContextEvent ev)
    {
        List<Container> containers = StartupUtil.getAllPublishedContainers(ev.getServletContext());

        if (containers.isEmpty())
        {
            log.debug("No containers to shutdown");
            return;
        }

        for (Container container : containers)
        {
            log.debug("ServletContext initializing for container: " + container.getClass().getSimpleName());

            Collection<String> beanNames = container.getBeanNames();
            for (String beanName : beanNames)
            {
                Object bean = container.getBean(beanName);
                if (bean instanceof ServletContextListener)
                {
                    ServletContextListener scl = (ServletContextListener) bean;
                    log.debug("- For contained bean: " + beanName);
                    scl.contextInitialized(ev);
                }
            }
        }
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
            log.debug("ServletContext destroying for container: " + container.getClass().getSimpleName());

            Collection<String> beanNames = container.getBeanNames();
            for (String beanName : beanNames)
            {
                Object bean = container.getBean(beanName);
                if (bean instanceof ServletContextListener)
                {
                    ServletContextListener scl = (ServletContextListener) bean;
                    log.debug("- For contained bean: " + beanName);
                    scl.contextDestroyed(ev);
                }
            }
        }
    }

    /**
     * The log stream
     */
    private static final Log log = LogFactory.getLog(DwrListener.class);
}
