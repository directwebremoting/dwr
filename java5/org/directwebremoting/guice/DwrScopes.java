/*
 * Copyright 2007 Tim Peierls
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.directwebremoting.guice;

import com.google.inject.Key;
import com.google.inject.Provider;
import com.google.inject.Scope;
import com.google.inject.util.ToStringBuilder;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.ServletContext;

import org.directwebremoting.ScriptSession;
import org.directwebremoting.WebContext;
import org.directwebremoting.WebContextFactory;
import org.directwebremoting.util.Logger;

import static org.directwebremoting.guice.DwrGuiceUtil.getServletContext;

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
            public HttpServletRequest get()
            {
                return WebContextFactory.get().getHttpServletRequest();
            }
            
            public Object get(HttpServletRequest request, String name)
            {
                return request.getAttribute(name);
            }
            
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
            public ScriptSession get()
            {
                return WebContextFactory.get().getScriptSession();
            }
            
            public Object get(ScriptSession scriptSession, String name)
            {
                return scriptSession.getAttribute(name);
            }
            
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
            public HttpSession get()
            {
                return WebContextFactory.get().getSession();
            }
            
            public Object get(HttpSession session, String name)
            {
                return session.getAttribute(name);
            }
            
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

        public ServletContext get()
        {
            return getServletContext();
        }
        
        public Object get(ServletContext servletContext, String name)
        {
            if (log.isDebugEnabled())
            {
                log.debug(String.format("servletContext.getAttribute(%s)", name));
            }
            return servletContext.getAttribute(name);
        }
        
        public void put(ServletContext servletContext, String name, Object value)
        {
            if (log.isDebugEnabled())
            {
                log.debug(String.format("servletContext.setAttribute(%s, %s)", name, value));
            }
            servletContext.setAttribute(name, value);
        }
    };

    private DwrScopes() { /* uninstantiable */ }

    /**
     * The log stream
     */
    private static final Logger log = Logger.getLogger(DwrScopes.class);
}
