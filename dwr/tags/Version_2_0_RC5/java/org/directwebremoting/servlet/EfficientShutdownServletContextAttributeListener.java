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

import javax.servlet.ServletContextAttributeEvent;
import javax.servlet.ServletContextAttributeListener;

import org.directwebremoting.extend.ServerLoadMonitor;
import org.directwebremoting.impl.ContainerUtil;

/**
 * A {@link ServletContextAttributeListener} that can be used to call
 * {@link ServerLoadMonitor#shutdown()} when the servlet container is stopped
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class EfficientShutdownServletContextAttributeListener implements ServletContextAttributeListener
{
    /* (non-Javadoc)
     * @see javax.servlet.ServletContextAttributeListener#attributeAdded(javax.servlet.ServletContextAttributeEvent)
     */
    public void attributeAdded(ServletContextAttributeEvent ev)
    {
    }

    /* (non-Javadoc)
     * @see javax.servlet.ServletContextAttributeListener#attributeRemoved(javax.servlet.ServletContextAttributeEvent)
     */
    public void attributeRemoved(ServletContextAttributeEvent ev)
    {
        if (ev.getName().equals(ContainerUtil.ATTRIBUTE_CONTAINER_LIST))
        {
            List containers = (List) ev.getValue();
            ContainerUtil.shutdownServerLoadMonitorsInContainerList(containers, "EfficientShutdownServletContextAttributeListener");
        }
    }

    /* (non-Javadoc)
     * @see javax.servlet.ServletContextAttributeListener#attributeReplaced(javax.servlet.ServletContextAttributeEvent)
     */
    public void attributeReplaced(ServletContextAttributeEvent ev)
    {
    }
}

