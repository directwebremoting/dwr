package org.directwebremoting.server.servlet2;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.directwebremoting.extend.ContainerAbstraction;
import org.directwebremoting.extend.RealScriptSession;
import org.directwebremoting.extend.ScriptConduit;
import org.directwebremoting.extend.ServerLoadMonitor;
import org.directwebremoting.extend.Sleeper;
import org.directwebremoting.impl.DefaultServerLoadMonitor;
import org.directwebremoting.impl.ThreadWaitSleeper;

/**
 * An abstraction of the servlet container that just follows the standards
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class Servlet24ContainerAbstraction implements ContainerAbstraction
{
    /* (non-Javadoc)
     * @see org.directwebremoting.dwrp.ContainerAbstraction#isNativeEnvironment(javax.servlet.ServletConfig)
     */
    public boolean isNativeEnvironment(ServletConfig servletConfig)
    {
        return true;
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.dwrp.ContainerAbstraction#getServerLoadMonitorImplementation()
     */
    public Class<? extends ServerLoadMonitor> getServerLoadMonitorImplementation()
    {
        return DefaultServerLoadMonitor.class;
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.extend.ContainerAbstraction#isResponseCompleted(javax.servlet.http.HttpServletRequest)
     */
    public boolean handleResumedRequest(HttpServletRequest request)
    {
        return false;
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.extend.ContainerAbstraction#createSleeper()
     */
    public Sleeper createSleeper(HttpServletRequest request, HttpServletResponse response, RealScriptSession scriptSession, ScriptConduit conduit) throws IOException
    {
        return new ThreadWaitSleeper(response, scriptSession, conduit);
    }
}
