package org.directwebremoting.guice;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.directwebremoting.ScriptSession;
import org.directwebremoting.WebContextFactory;
import org.directwebremoting.guice.util.AbstractSimpleContextScope;
import org.directwebremoting.guice.util.ContextScope;

import static org.directwebremoting.guice.DwrGuiceUtil.*;

/**
 * Scopes available to DWR applications.
 * @author Tim Peierls [tim at peierls dot net]
 */
public class DwrScopes
{
    /**
     * HTTP request scope.
     */
    public static final ContextScope<HttpServletRequest> REQUEST =
        new AbstractSimpleContextScope<HttpServletRequest>(
            HttpServletRequest.class, "DwrScopes.REQUEST")
        {
            @Override
            public HttpServletRequest get()
            {
                return WebContextFactory.get().getHttpServletRequest();
            }

            @Override
            public Object get(HttpServletRequest request, String name)
            {
                return request.getAttribute(name);
            }

            @Override
            public void put(HttpServletRequest request, String name, Object value)
            {
                request.setAttribute(name, value);
            }
        };

    /**
     * DWR script session scope.
     */
    public static final ContextScope<ScriptSession> SCRIPT =
        new AbstractSimpleContextScope<ScriptSession>(ScriptSession.class, "DwrScopes.SCRIPT")
        {
            @Override
            public ScriptSession get()
            {
                return WebContextFactory.get().getScriptSession();
            }

            @Override
            public Object get(ScriptSession scriptSession, String name)
            {
                return scriptSession.getAttribute(name);
            }

            @Override
            public void put(ScriptSession scriptSession, String name, Object value)
            {
                scriptSession.setAttribute(name, value);
            }
        };

    /**
     * HTTP session scope. The implementation uses session identity to
     * to track which sessions are open. Since the servlet spec doesn't
     * guarantee identity of sessions between requests, don't rely on
     * {@code getOpenContexts()} or {@code close(session, handlers)} to
     * work correctly for this scope.
     */
    public static final ContextScope<HttpSession> SESSION =
        new AbstractSimpleContextScope<HttpSession>(HttpSession.class, "DwrScopes.SESSION")
        {
            @Override
            public HttpSession get()
            {
                return WebContextFactory.get().getSession();
            }

            @Override
            public Object get(HttpSession session, String name)
            {
                return session.getAttribute(name);
            }

            @Override
            public void put(HttpSession session, String name, Object value)
            {
                session.setAttribute(name, value);
            }
        };

    /**
     * Application scope: objects in this scope <em>are</em> eagerly initialized
     * during DWR servlet initialization, and Closeable objects in this scope are
     * closed during DWR servlet destruction.
     */
    public static final ContextScope<ServletContext> APPLICATION =
        new ApplicationScope("DwrScopes.APPLICATION");

    /**
     * Global application scope: like {@link #APPLICATION}, but objects in
     * this scope are <em>not</em> eagerly initialized and Closeable objects
     * in this scope are closed during servlet context destruction (not
     * during DWR servlet destruction).
     */
    public static final ContextScope<ServletContext> GLOBAL =
        new ApplicationScope("DwrScopes.GLOBAL");


    static class ApplicationScope extends AbstractSimpleContextScope<ServletContext>
    {
        ApplicationScope(String scopeName)
        {
            super(ServletContext.class, scopeName);
        }

        @Override
        public ServletContext get()
        {
            return getServletContext();
        }

        @Override
        public Object get(ServletContext servletContext, String name)
        {
            /*
            if (log.isDebugEnabled())
            {
                log.debug(String.format("servletContext.getAttribute(%s)", name));
            }
            //*/
            return servletContext.getAttribute(name);
        }

        @Override
        public void put(ServletContext servletContext, String name, Object value)
        {
            /*
            if (log.isDebugEnabled())
            {
                log.debug(String.format("servletContext.setAttribute(%s, %s)", name, value));
            }
            //*/
            servletContext.setAttribute(name, value);
        }
    }

    private DwrScopes() { /* uninstantiable */ }
}
