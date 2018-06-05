package org.directwebremoting.extend;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
    boolean handleResumedRequest(HttpServletRequest request);

    /**
     * Create a sleeper that is appropriate for the given servlet container
     * @param request The Sleeper will probably need to know about the request
     * @param response The Sleeper will probably need to know about the response
     * @param scriptSession The Sleeper will probably need to know about the scriptSession
     * @param conduit The Sleeper will probably need to know about the conduit
     * @return A method of sending threads to sleep.
     */
    Sleeper createSleeper(HttpServletRequest request, HttpServletResponse response, RealScriptSession scriptSession, ScriptConduit conduit) throws IOException;
}
