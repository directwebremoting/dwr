package org.directwebremoting.server.servlet3;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
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
public class Servlet30ContainerAbstraction implements ContainerAbstraction
{
    /* (non-Javadoc)
     * @see org.directwebremoting.dwrp.ContainerAbstraction#isNativeEnvironment(javax.servlet.ServletConfig)
     */
    public boolean isNativeEnvironment(ServletConfig servletConfig)
    {
        ServletContext ctx = servletConfig.getServletContext();
        return ctx.getMajorVersion() >= 3 && ctx.getEffectiveMajorVersion() >= 3;
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
     * @see org.directwebremoting.extend.ContainerAbstraction#createSleeper(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, org.directwebremoting.extend.RealScriptSession, org.directwebremoting.extend.ScriptConduit)
     */
    public Sleeper createSleeper(HttpServletRequest request, HttpServletResponse response, RealScriptSession scriptSession, ScriptConduit conduit) throws IOException
    {
        if (request.isAsyncSupported()) {
            return new Servlet30Sleeper(request, response, scriptSession, conduit);
        } else {
            return new ThreadWaitSleeper(response, scriptSession, conduit);
        }
    }
}
