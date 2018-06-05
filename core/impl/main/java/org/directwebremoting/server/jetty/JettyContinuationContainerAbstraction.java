package org.directwebremoting.server.jetty;

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
import org.directwebremoting.util.LocalUtil;

/**
 * An abstraction of the servlet container that is specific to Jetty
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class JettyContinuationContainerAbstraction implements ContainerAbstraction
{
    /* (non-Javadoc)
     * @see org.directwebremoting.dwrp.ContainerAbstraction#isNativeEnvironment(javax.servlet.ServletConfig)
     */
    public boolean isNativeEnvironment(ServletConfig servletConfig)
    {
        try {
            LocalUtil.classForName("org.eclipse.jetty.continuation.ContinuationSupport");
            return true;
        } catch(ClassNotFoundException ex) {
            return false;
        }
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.dwrp.ContainerAbstraction#getServerLoadMonitorImplementation()
     */
    public Class<? extends ServerLoadMonitor> getServerLoadMonitorImplementation()
    {
        return DefaultServerLoadMonitor.class;
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.extend.ContainerAbstraction#handleResumedRequest(javax.servlet.http.HttpServletRequest)
     */
    public boolean handleResumedRequest(HttpServletRequest request)
    {
        JettyContinuationSleeper sleeper = JettyContinuationSleeper.getSleeper(request);
        if (sleeper != null)
        {
            sleeper.resumeWork();
            return true;
        }
        return false;
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.extend.ContainerAbstraction#createSleeper(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, org.directwebremoting.extend.RealScriptSession, org.directwebremoting.extend.ScriptConduit)
     */
    public Sleeper createSleeper(HttpServletRequest request, HttpServletResponse response, RealScriptSession scriptSession, ScriptConduit conduit) throws IOException
    {
        return new JettyContinuationSleeper(request, response, scriptSession, conduit);
    }
}
