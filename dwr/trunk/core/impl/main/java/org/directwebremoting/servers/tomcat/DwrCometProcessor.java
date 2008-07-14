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
package org.directwebremoting.servers.tomcat;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.catalina.CometEvent;
import org.apache.catalina.CometProcessor;
import org.directwebremoting.servlet.DwrServlet;

/**
 * A specialization of {@link DwrServlet} that allows Tomcat to give us the
 * ability to drop threads.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class DwrCometProcessor extends DwrServlet implements CometProcessor
{
    /* (non-Javadoc)
     * @see org.directwebremoting.servlet.DwrServlet#init(javax.servlet.ServletConfig)
     */
    @Override
    public void init(ServletConfig servletConfig) throws ServletException
    {
        super.init(servletConfig);

        // Rather than messing with server names, we just declare to
        // TomcatContainerAbstraction that we are running in tomcat
        servletConfig.getServletContext().setAttribute(ATTRIBUTE_ENABLED, true);
    }

    /* (non-Javadoc)
     * @see org.apache.catalina.CometProcessor#event(org.apache.catalina.CometEvent)
     */
    public void event(CometEvent event) throws IOException, ServletException
    {
        HttpServletRequest request = event.getHttpServletRequest();
        HttpServletResponse response = event.getHttpServletResponse();

        if (event.getEventType() == CometEvent.EventType.BEGIN)
        {
            request.setAttribute(ATTRIBUTE_EVENT, event);
            // The docs say that you're only allowed to read when it's a
            // READ event.
        }
        else if (event.getEventType() == CometEvent.EventType.ERROR)
        {
            event.close();
        }
        else if (event.getEventType() == CometEvent.EventType.END)
        {
            event.close();
        }
        else if (event.getEventType() == CometEvent.EventType.READ)
        {
            // Does this handle blocking reads if we just carry on reading?
            // Does anything in our implementation assume that isAvailable or
            // isReady return false if the stream has ended (rather than just
            // blocked)
            service(request, response);

            // If not we could use TomcatContainerAbstraction.isResponseCompleted
            // to bail out if some condition set by the ERROR/END events is not
            // satisfied.
        }
    }

    /**
     * We remember the notify conduit so we can reuse it
     */
    protected static final String ATTRIBUTE_EVENT = "org.directwebremoting.servers.tomcat.event";

    /**
     * Declare to TomcatContainerAbstraction that we are in action
     */
    protected static final String ATTRIBUTE_ENABLED = "org.directwebremoting.servers.tomcat.enabled";
}
