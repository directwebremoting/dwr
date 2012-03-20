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
package org.directwebremoting.extend;

import javax.servlet.ServletConfig;
import javax.servlet.http.HttpServletRequest;

/**
 * We need to keep container specific logic out of BasePollHandler, and other
 * parts of DWR. Each container will need an implementation of this interface,
 * which will generally just return configurations of generic classes.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public interface ContainerAbstraction
{
    /**
     * We ask ContainerAbstractions to identify if the environment given is
     * 'theirs'. Are they fit for this environment?
     * @param servletConfig Access to {@link javax.servlet.ServletContext} etc.
     * @return true if this implementation should be used
     */
    boolean isNativeEnvironment(ServletConfig servletConfig);

    /**
     * The setup process allows the ContainerAbstraction to select a special
     * ServerLoadMonitor implementation
     * @return The ServerLoadMonitor implementation to go with this container
     */
    Class<? extends ServerLoadMonitor> getServerLoadMonitorImplementation();

    /**
     * Some async-servlet implementations (Jetty) restart requests, when we
     * might have already completed dealing with them. If this method returns
     * true then PollHandler will assume that we're done and will bail out
     * before we even get started.
     * @param request The request that we might be finished with
     * @return true if the request is completed
     */
    boolean isResponseCompleted(HttpServletRequest request);

    /**
     * Create a sleeper that is appropriate for the given servlet container
     * @param request The Sleeper will probably need to know about the request
     * @return A method of sending threads to sleep.
     */
    Sleeper createSleeper(HttpServletRequest request);
}
