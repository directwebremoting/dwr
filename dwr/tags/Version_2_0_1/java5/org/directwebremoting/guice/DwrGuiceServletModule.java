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

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import com.google.inject.Provider;

import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.directwebremoting.ScriptSession;
import org.directwebremoting.ServerContext;
import org.directwebremoting.ServerContextFactory;
import org.directwebremoting.WebContext;
import org.directwebremoting.WebContextFactory;
import static org.directwebremoting.guice.DwrScopes.*;
import static org.directwebremoting.guice.DwrGuiceUtil.getServletContext;

/**
 * Configures DWR scopes and creates bindings for commonly
 * used objects related to those scopes: request, response,
 * script session, session, servlet context, and web context. 
 * <p>
 * Since Guice's ServletModule makes its own bindings for requests,
 * responses, and sessions, this class has a constructor that lets
 * the user specify whether to avoid conflicts by not binding these types.
 * </p>
 * @author Tim Peierls [tim at peierls dot net]
 */
class DwrGuiceServletModule extends AbstractModule 
{
    /**
     * Creates a module to configure DWR scopes and bindings;
     * conflicts with the bindings provided by Guice's {@link ServletModule}.
     * <p>
     * Normally you would not use this constructor directly, but instead call
     * {@link AbstractDwrModule#bindDwrScopes() bindDwrScopes} in your binding
     * code.
     * </p>
     */
    public DwrGuiceServletModule() 
    {
        this.bindPotentiallyConflictingTypes = true;
    }
    
    /**
     * Creates a module to configure DWR scopes and bindings;
     * conflicts with the bindings provided by Guice's {@link ServletModule}
     * unless {@code bindPotentiallyConflictingTypes} is false, in which 
     * case this module will not create its own bindings for requests, 
     * responses, and sessions.
     * <p>
     * Normally you would not use this constructor directly, but instead call
     * {@link AbstractDwrModule#bindDwrScopes(boolean) bindDwrScopes} in your binding
     * code.
     * </p>
     */
    public DwrGuiceServletModule(boolean bindPotentiallyConflictingTypes) 
    {
        this.bindPotentiallyConflictingTypes = bindPotentiallyConflictingTypes;
    }


    protected void configure() 
    {
        bindScope(RequestScoped.class, REQUEST);
        bindScope(SessionScoped.class, SESSION);
        bindScope(ScriptSessionScoped.class, SCRIPT);
        bindScope(ApplicationScoped.class, APPLICATION);
        bindScope(GlobalApplicationScoped.class, GLOBAL);
        
        if (bindPotentiallyConflictingTypes) 
        {
            // Use DWR request and session scopes for request, response, and session.
            
            Provider<HttpServletRequest> requestProvider =
                new Provider<HttpServletRequest>() 
                {
                    public HttpServletRequest get() 
                    {
                        WebContext webcx = WebContextFactory.get();
                        return webcx.getHttpServletRequest();
                    }

                    public String toString() 
                    {
                        return "RequestProvider";
                    }
                };
            bind(HttpServletRequest.class).toProvider(requestProvider);
            bind(ServletRequest.class).toProvider(requestProvider);

            Provider<HttpServletResponse> responseProvider =
                new Provider<HttpServletResponse>() 
                {
                    public HttpServletResponse get() 
                    {
                        WebContext webcx = WebContextFactory.get();
                        return webcx.getHttpServletResponse();
                    }

                    public String toString() 
                    {
                        return "ResponseProvider";
                    }
                };
            bind(HttpServletResponse.class).toProvider(responseProvider);
            bind(ServletResponse.class).toProvider(responseProvider);
                
            Provider<HttpSession> sessionProvider = 
                new Provider<HttpSession>() 
                {
                    public HttpSession get() 
                    {
                        WebContext webcx = WebContextFactory.get();
                        return webcx.getSession();
                    }

                    public String toString() 
                    {
                        return "SessionProvider";
                    }
                };
            bind(HttpSession.class).toProvider(sessionProvider);
        } 

        Provider<Map<String, String[]>> requestParametersProvider =
            new Provider<Map<String, String[]>>() 
            {
                @SuppressWarnings({"unchecked"})
                public Map<String, String[]> get() 
                {
                    WebContext webcx = WebContextFactory.get();
                    return webcx.getHttpServletRequest().getParameterMap();
                }

                public String toString() 
                {
                    return "RequestParametersProvider";
                }
            };           
        bind(new TypeLiteral<Map<String, String[]>>() {})
            .annotatedWith(RequestParameters.class)
            .toProvider(requestParametersProvider);

       
        Provider<ScriptSession> scriptSessionProvider =
            new Provider<ScriptSession>() 
            {
                public ScriptSession get() 
                {
                    WebContext webcx = WebContextFactory.get();
                    return webcx.getScriptSession();
                }

                public String toString() 
                {
                    return "ScriptSessionProvider";
                }
            };
        bind(ScriptSession.class).toProvider(scriptSessionProvider);
        
        Provider<ServletContext> servletContextProvider =
            new Provider<ServletContext>() 
            {
                public ServletContext get() 
                {
                    // Can work even if WebContext isn't found.
                    return getServletContext();
                }

                public String toString() 
                {
                    return "ServletContextProvider";
                }
            };            
        bind(ServletContext.class).toProvider(servletContextProvider);
        
        Provider<WebContext> webContextProvider =
            new Provider<WebContext>() 
            {
                public WebContext get() 
                {
                    return WebContextFactory.get();
                }

                public String toString() 
                {
                    return "WebContextProvider";
                }
            };            
        bind(WebContext.class).toProvider(webContextProvider);
        
        Provider<ServerContext> serverContextProvider =
            new Provider<ServerContext>() 
            {
                public ServerContext get() 
                {
                    return ServerContextFactory.get(getServletContext());
                }

                public String toString() 
                {
                    return "ServerContextProvider";
                }
            };            
        bind(ServerContext.class).toProvider(serverContextProvider);
    }

    
    private final boolean bindPotentiallyConflictingTypes;
}
