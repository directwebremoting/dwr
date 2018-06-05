package org.directwebremoting.guice;

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

import com.google.inject.Provider;
import com.google.inject.TypeLiteral;

import static org.directwebremoting.guice.DwrGuiceUtil.*;
import static org.directwebremoting.guice.DwrScopes.*;

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
class DwrGuiceServletModule extends AbstractDwrModule
{
    /**
     * Creates a module to configure DWR scopes and bindings;
     * might produce conflicts with the bindings provided by
     * Guice's ServletModule if {@code bindPotentiallyConflictingTypes}
     * is true. In addition, this module always binds the potentially
     * conflicting types (ServletRequest, ServletResponse, HttpSession,
     * HttpServletRequest, and HttpServetResponse) with a distinguishing
     * {@link Dwr @Dwr} annotation.
     */
    DwrGuiceServletModule(boolean bindPotentiallyConflictingTypes)
    {
        this.bindPotentiallyConflictingTypes = bindPotentiallyConflictingTypes;
    }


    @Override
    protected void configure()
    {
        bindScope(RequestScoped.class, REQUEST);
        bindScope(SessionScoped.class, SESSION);
        bindScope(ScriptSessionScoped.class, SCRIPT);
        bindScope(ApplicationScoped.class, APPLICATION);
        bindScope(GlobalApplicationScoped.class, GLOBAL);

        if (bindPotentiallyConflictingTypes)
        {
            bind(ServletRequest.class)
                .toProvider(requestProvider);
            bind(HttpServletRequest.class)
                .toProvider(requestProvider);
            bind(ServletResponse.class)
                .toProvider(responseProvider);
            bind(HttpServletResponse.class)
                .toProvider(responseProvider);
            bind(HttpSession.class)
                .toProvider(sessionProvider);
            bind(ServletContext.class)
                .toProvider(servletContextProvider);
        }

        bind(ServletRequest.class)
            .annotatedWith(Dwr.class)
            .toProvider(requestProvider);
        bind(HttpServletRequest.class)
            .annotatedWith(Dwr.class)
            .toProvider(requestProvider);
        bind(ServletResponse.class)
            .annotatedWith(Dwr.class)
            .toProvider(responseProvider);
        bind(HttpServletResponse.class)
            .annotatedWith(Dwr.class)
            .toProvider(responseProvider);
        bind(HttpSession.class)
            .annotatedWith(Dwr.class)
            .toProvider(sessionProvider);
        bind(ServletContext.class)
            .annotatedWith(Dwr.class)
            .toProvider(servletContextProvider);

        bind(new TypeLiteral<Map<String, String[]>>() {})
            .annotatedWith(RequestParameters.class)
            .toProvider(requestParametersProvider);

        bind(ScriptSession.class)
            .toProvider(scriptSessionProvider);
        bind(WebContext.class)
            .toProvider(webContextProvider);
        bind(ServerContext.class)
            .toProvider(serverContextProvider);
    }


    private final Provider<HttpServletRequest> requestProvider = new Provider<HttpServletRequest>()
    {
        public HttpServletRequest get()
        {
            WebContext webcx = WebContextFactory.get();
            return webcx.getHttpServletRequest();
        }

        @Override
        public String toString()
        {
            return "RequestProvider";
        }
    };

    private final Provider<HttpServletResponse> responseProvider = new Provider<HttpServletResponse>()
    {
        public HttpServletResponse get()
        {
            WebContext webcx = WebContextFactory.get();
            return webcx.getHttpServletResponse();
        }

        @Override
        public String toString()
        {
            return "ResponseProvider";
        }
    };

    private final Provider<HttpSession> sessionProvider = new Provider<HttpSession>()
    {
        public HttpSession get()
        {
            WebContext webcx = WebContextFactory.get();
            return webcx.getSession();
        }

        @Override
        public String toString()
        {
            return "SessionProvider";
        }
    };

    private final Provider<Map<String, String[]>> requestParametersProvider = new Provider<Map<String, String[]>>()
    {
        @SuppressWarnings({"unchecked"})
        public Map<String, String[]> get()
        {
            WebContext webcx = WebContextFactory.get();
            return webcx.getHttpServletRequest().getParameterMap();
        }

        @Override
        public String toString()
        {
            return "RequestParametersProvider";
        }
    };

    private final Provider<ScriptSession> scriptSessionProvider = new Provider<ScriptSession>()
    {
        public ScriptSession get()
        {
            WebContext webcx = WebContextFactory.get();
            return webcx.getScriptSession();
        }

        @Override
        public String toString()
        {
            return "ScriptSessionProvider";
        }
    };

    private final Provider<ServletContext> servletContextProvider = new Provider<ServletContext>()
    {
        public ServletContext get()
        {
            // Can work even if WebContext isn't found.
            return getServletContext();
        }

        @Override
        public String toString()
        {
            return "ServletContextProvider";
        }
    };

    private final Provider<WebContext> webContextProvider = new Provider<WebContext>()
    {
        public WebContext get()
        {
            return WebContextFactory.get();
        }

        @Override
        public String toString()
        {
            return "WebContextProvider";
        }
    };

    private final Provider<ServerContext> serverContextProvider = new Provider<ServerContext>()
    {
        public ServerContext get()
        {
            return ServerContextFactory.get();
        }

        @Override
        public String toString()
        {
            return "ServerContextProvider";
        }
    };
}
