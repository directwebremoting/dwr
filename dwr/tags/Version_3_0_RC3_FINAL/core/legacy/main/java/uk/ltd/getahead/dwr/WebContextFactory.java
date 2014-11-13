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
package uk.ltd.getahead.dwr;

import java.io.IOException;
import java.util.Collection;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.directwebremoting.Container;
import org.directwebremoting.ScriptSession;

/**
 * Accessor for the current WebContext.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 * @deprecated Use org.directwebremoting.WebContextFactory
 */
@Deprecated
public class WebContextFactory
{
    /**
     * Accessor for the current WebContext.
     * @return The current WebContext or null if the current thread was not
     * started by DWR.
     * @deprecated Use org.directwebremoting.WebContextFactory.get()
     */
    @Deprecated
    public static WebContext get()
    {
        org.directwebremoting.WebContext wctx = org.directwebremoting.WebContextFactory.get();
        return new ProxyWebContext(wctx);
    }

    /**
     * How we support <code>uk.ltd.getahead.dwr.WebContext</code> when we only
     * have an <code>org.directwebremoting.WebContext</code>.
     * @author Joe Walker [joe at getahead dot ltd dot uk]
     */
    private static final class ProxyWebContext implements WebContext
    {
        /**
         * @param proxy The real WebContext that we proxy to
         */
        public ProxyWebContext(org.directwebremoting.WebContext proxy)
        {
            this.proxy = proxy;
        }

        /* (non-Javadoc)
         * @see org.directwebremoting.WebContext#forwardToString(java.lang.String)
         */
        public String forwardToString(String url) throws ServletException, IOException
        {
            return proxy.forwardToString(url);
        }

        /* (non-Javadoc)
         * @see org.directwebremoting.WebContext#getAllScriptSessions()
         */
        public Collection<ScriptSession> getAllScriptSessions()
        {
            return proxy.getAllScriptSessions();
        }

        /* (non-Javadoc)
         * @see org.directwebremoting.WebContext#getContainer()
         */
        public Container getContainer()
        {
            return proxy.getContainer();
        }

        /* (non-Javadoc)
         * @see org.directwebremoting.WebContext#getCurrentPage()
         */
        public String getCurrentPage()
        {
            return proxy.getCurrentPage();
        }

        /* (non-Javadoc)
         * @see org.directwebremoting.WebContext#getHttpServletRequest()
         */
        public HttpServletRequest getHttpServletRequest()
        {
            return proxy.getHttpServletRequest();
        }

        /* (non-Javadoc)
         * @see org.directwebremoting.WebContext#getHttpServletResponse()
         */
        public HttpServletResponse getHttpServletResponse()
        {
            return proxy.getHttpServletResponse();
        }

        /* (non-Javadoc)
         * @see org.directwebremoting.WebContext#getScriptSession()
         */
        public ScriptSession getScriptSession()
        {
            return proxy.getScriptSession();
        }

        /* (non-Javadoc)
         * @see org.directwebremoting.WebContext#getScriptSessionsByPage(java.lang.String)
         */
        public Collection<ScriptSession> getScriptSessionsByPage(String url)
        {
            return proxy.getScriptSessionsByPage(url);
        }

        /* (non-Javadoc)
         * @see org.directwebremoting.ServerContext#getScriptSessionById(java.lang.String)
         */
        public ScriptSession getScriptSessionById(String sessionId)
        {
            return proxy.getScriptSessionById(sessionId);
        }

        /* (non-Javadoc)
         * @see org.directwebremoting.WebContext#getServletConfig()
         */
        public ServletConfig getServletConfig()
        {
            return proxy.getServletConfig();
        }

        /* (non-Javadoc)
         * @see org.directwebremoting.WebContext#getServletContext()
         */
        public ServletContext getServletContext()
        {
            return proxy.getServletContext();
        }

        /* (non-Javadoc)
         * @see org.directwebremoting.WebContext#getSession()
         */
        public HttpSession getSession()
        {
            return proxy.getSession();
        }

        /* (non-Javadoc)
         * @see org.directwebremoting.WebContext#getSession(boolean)
         */
        public HttpSession getSession(boolean create)
        {
            return proxy.getSession(create);
        }

        /* (non-Javadoc)
         * @see org.directwebremoting.WebContext#getVersion()
         */
        public String getVersion()
        {
            return proxy.getVersion();
        }

        /* (non-Javadoc)
         * @see org.directwebremoting.ServerContext#getContextPath()
         */
        public String getContextPath()
        {
            return proxy.getContextPath();
        }

        /**
         * The WebContext that we proxy to
         */
        private org.directwebremoting.WebContext proxy;
    }
}
