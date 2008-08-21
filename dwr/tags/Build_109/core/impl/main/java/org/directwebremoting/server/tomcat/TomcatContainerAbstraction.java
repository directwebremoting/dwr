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
package org.directwebremoting.server.tomcat;

import javax.servlet.ServletConfig;
import javax.servlet.http.HttpServletRequest;

import org.apache.catalina.CometEvent;
import org.directwebremoting.extend.ContainerAbstraction;
import org.directwebremoting.extend.ServerLoadMonitor;
import org.directwebremoting.extend.Sleeper;
import org.directwebremoting.impl.ThreadDroppingServerLoadMonitor;

/**
 * An abstraction of the servlet container that is specific to Jetty
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class TomcatContainerAbstraction implements ContainerAbstraction
{
    /* (non-Javadoc)
     * @see org.directwebremoting.extend.ContainerAbstraction#isResponseCompleted(javax.servlet.http.HttpServletRequest)
     */
    public boolean isResponseCompleted(HttpServletRequest request)
    {
        return false;
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.dwrp.ContainerAbstraction#isNativeEnvironment(javax.servlet.ServletConfig)
     */
    public boolean isNativeEnvironment(ServletConfig servletConfig)
    {
        Object enabled = servletConfig.getServletContext().getAttribute(DwrCometProcessor.ATTRIBUTE_ENABLED);
        return enabled != null && ((Boolean) enabled) == true;
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.dwrp.ContainerAbstraction#createSleeper(javax.servlet.http.HttpServletRequest)
     */
    public Sleeper createSleeper(HttpServletRequest request)
    {
        CometEvent event = (CometEvent) request.getAttribute(DwrCometProcessor.ATTRIBUTE_EVENT);
        return new TomcatSleeper(event);
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.dwrp.ContainerAbstraction#getServerLoadMonitorImplementation()
     */
    public Class<? extends ServerLoadMonitor> getServerLoadMonitorImplementation()
    {
        return ThreadDroppingServerLoadMonitor.class;
    }
}
