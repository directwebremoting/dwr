/*
 * Copyright 2007 Tim Peierls
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
package org.directwebremoting.guice;

import com.google.inject.Binding;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.name.Named;

import java.util.ArrayList;
import java.util.List;
import static java.util.Map.Entry;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.directwebremoting.extend.AjaxFilterManager;
import org.directwebremoting.extend.Configurator;
import org.directwebremoting.extend.ConverterManager;
import org.directwebremoting.extend.CreatorManager;
import org.directwebremoting.servlet.DwrServlet;
import org.directwebremoting.util.Logger;

import static org.directwebremoting.guice.DwrGuiceUtil.getInjector;
import static org.directwebremoting.guice.DwrGuiceUtil.popServletContext;
import static org.directwebremoting.guice.DwrGuiceUtil.pushServletContext;
import static org.directwebremoting.guice.ParamName.*;
import static org.directwebremoting.impl.ContainerUtil.INIT_CUSTOM_CONFIGURATOR;

/**
 * An extension of the basic 
 * {@link org.directwebremoting.servlet.DwrServlet DwrServlet} 
 * that configures itself for dependency injection with Guice. 
 * Must be used in conjunction with
 * {@link org.directwebremoting.guice.DwrGuiceServletContextFilter DwrGuiceServletContextFilter}.
 * @author Tim Peierls [tim at peierls dot net]
 */
public class DwrGuiceServlet extends DwrServlet 
{
    /**
     * Copies DWR configuration values from the Guice bindings into 
     * {@code servletConfig} to make these values accessible to the 
     * standard DWR servlet configuration machinery.
     */
    @Override public void init(ServletConfig servletConfig) throws ServletException
    {
        // Save this for later use by destroy.
        this.servletContext = servletConfig.getServletContext();
        
        // Set the current context thread-locally so our internal classes can
        // look up the Injector and use it in turn to look up further objects.
        pushServletContext(this.servletContext);
        try
        {      
            // Since ServletConfig is immutable, we use a modifiable 
            // decoration of the real servlet configuration and pass 
            // that to the init method of the superclass.
            ModifiableServletConfig config = new ModifiableServletConfig(servletConfig);
        
            // Apply settings configured at bind-time.
            setInitParameters(config);

            // Use our internal manager classes to replace and delegate to
            // any user-specified or default implementations, after adding
            // additional creators and converters registered at bind-time.
            configureDelegatedTypes(config);
            
            // Normal DwrServlet initialization happens here using the
            // modified ServletConfig instead of the one we were passed.
            super.init(config);

            // Objects with (non-global) application scope are initialized
            // eagerly.
            initApplicationScoped();            
        }
        finally
        {
            // Clean up the ThreadLocal we just used.
            popServletContext();
        }
    }
    
    /**
     * Closes any {@code Closeable} application-scoped objects.
     * IO exceptions are collected but ignored.
     */
    @Override public void destroy()
    {
        pushServletContext(this.servletContext);
        try
        {            
            // Closeable objects with (non-global) application scope are closed.
            List<Exception> exceptions = destroyApplicationScoped();
            
            super.destroy();
            
            for (Exception ex : exceptions)
            {
                log.warn("During servlet shutdown", ex);
            }
        }
        finally
        {
            popServletContext();
            this.servletContext = null;
        }
    }
    
    /**
     * Inject some values that might have been configured at bind-time.
     * Override web.xml <init-param> settings in each case that injection
     * is successful.
     */
    private void setInitParameters(ModifiableServletConfig config)
    {
        Injector injector = getInjector();
        InjectedConfig cfg = new InjectedConfig(config);
        injector.injectMembers(cfg);
        cfg.setParameters();
    }
    
    private static class InjectedConfig
    {
        InjectedConfig(ModifiableServletConfig config)
        {
            this.config = config;
        }
        
        void setParameter(ParamName paramName, Object value)
        {
            if (value != null)
            {
                config.setInitParameter(paramName.getName(), value.toString());
            }
        }
        
        void setParameters()
        {
            setParameter(ALLOW_GET_FOR_SAFARI,            allowGetForSafariButMakeForgeryEasier);
            setParameter(CROSS_DOMAIN_SESSION_SECURITY,   crossDomainSessionSecurity);
            setParameter(ALLOW_SCRIPT_TAG_REMOTING,       allowScriptTagRemoting);
            setParameter(DEBUG,                           debug);
            setParameter(SCRIPT_SESSION_TIMEOUT,          scriptSessionTimeout);
            setParameter(MAX_CALL_COUNT,                  maxCallCount);
            setParameter(ACTIVE_REVERSE_AJAX_ENABLED,     activeReverseAjaxEnabled);
            setParameter(MAX_WAIT_AFTER_WRITE,            maxWaitAfterWrite);
            setParameter(DISCONNECTED_TIME,               disconnectedTime);
            setParameter(POLL_AND_COMET_ENABLED,          pollAndCometEnabled);
            setParameter(MAX_WAITING_THREADS,             maxWaitingThreads);
            setParameter(MAX_POLL_HITS_PER_SECOND,        maxPollHitsPerSecond);
            setParameter(PRE_STREAM_WAIT_TIME,            preStreamWaitTime);
            setParameter(POST_STREAM_WAIT_TIME,           postStreamWaitTime);
            setParameter(IGNORE_LAST_MODIFIED,            ignoreLastModified);
            setParameter(SCRIPT_COMPRESSED,               scriptCompressed);
            setParameter(SESSION_COOKIE_NAME,             sessionCookieName);
            setParameter(WELCOME_FILES,                   welcomeFiles);
            setParameter(NORMALIZE_INCLUDES_QUERY_STRING, normalizeIncludesQueryString);
            setParameter(OVERRIDE_PATH,                   overridePath);

            if (configurator != null) 
            {
                // InternalConfigurator knows how to look up the configurator
                // instance again and delegate to it.
                config.setInitParameter(INIT_CUSTOM_CONFIGURATOR, InternalConfigurator.class.getName());
            }

            if (classes != null) 
            {
                config.setInitParameter(CLASSES.getName(), classListToString(classes));
            }
        }
        
        @Inject(optional=true) @InitParam(ALLOW_GET_FOR_SAFARI)            Boolean allowGetForSafariButMakeForgeryEasier = null;
        @Inject(optional=true) @InitParam(CROSS_DOMAIN_SESSION_SECURITY)   Boolean crossDomainSessionSecurity = null;
        @Inject(optional=true) @InitParam(ALLOW_SCRIPT_TAG_REMOTING)       Boolean allowScriptTagRemoting = null;
        @Inject(optional=true) @InitParam(DEBUG)                           Boolean debug = null;
        @Inject(optional=true) @InitParam(SCRIPT_SESSION_TIMEOUT)          Long    scriptSessionTimeout = null;
        @Inject(optional=true) @InitParam(MAX_CALL_COUNT)                  Integer maxCallCount = null;
        @Inject(optional=true) @InitParam(ACTIVE_REVERSE_AJAX_ENABLED)     Boolean activeReverseAjaxEnabled = null;
        @Inject(optional=true) @InitParam(MAX_WAIT_AFTER_WRITE)            Long    maxWaitAfterWrite = null;
        @Inject(optional=true) @InitParam(DISCONNECTED_TIME)               Long    disconnectedTime = null;
        @Inject(optional=true) @InitParam(POLL_AND_COMET_ENABLED)          Boolean pollAndCometEnabled = null;
        @Inject(optional=true) @InitParam(MAX_WAITING_THREADS)             Integer maxWaitingThreads = null;
        @Inject(optional=true) @InitParam(MAX_POLL_HITS_PER_SECOND)        Integer maxPollHitsPerSecond = null;
        @Inject(optional=true) @InitParam(PRE_STREAM_WAIT_TIME)            Long    preStreamWaitTime = null;
        @Inject(optional=true) @InitParam(POST_STREAM_WAIT_TIME)           Long    postStreamWaitTime = null;
        @Inject(optional=true) @InitParam(IGNORE_LAST_MODIFIED)            Boolean ignoreLastModified = null;
        @Inject(optional=true) @InitParam(SCRIPT_COMPRESSED)               Boolean scriptCompressed = null;
        @Inject(optional=true) @InitParam(SESSION_COOKIE_NAME)             String  sessionCookieName = null;
        @Inject(optional=true) @InitParam(WELCOME_FILES)                   String  welcomeFiles = null;
        @Inject(optional=true) @InitParam(NORMALIZE_INCLUDES_QUERY_STRING) Boolean normalizeIncludesQueryString = null;
        @Inject(optional=true) @InitParam(OVERRIDE_PATH)                   String  overridePath = null;
        
        @Inject(optional=true) Configurator configurator = null;
        
        @Inject(optional=true) @InitParam(CLASSES) List<Class> classes = null;
        
        private final ModifiableServletConfig config;
    }
    

    private void configureDelegatedTypes(ModifiableServletConfig config)
    {
        // Get the user-specified type names, if any, for CreatorManager 
        // and ConverterManager and stash them (thread-locally) so that 
        // InternalCreatorManager and InternalConverterManager can retrieve 
        // them in their parameterless constructors.
        
        InternalCreatorManager.setTypeName(config.getInitParameter(INIT_CREATOR_MANAGER));
        InternalConverterManager.setTypeName(config.getInitParameter(INIT_CONVERTER_MANAGER));
        InternalAjaxFilterManager.setTypeName(config.getInitParameter(INIT_AJAX_FILTER_MANAGER));
        
        // Tell DWR to use our special delegating classes that know how to 
        // create delegates of the appropriate type by looking at the type 
        // names that we just stashed.
        
        config.setInitParameter(INIT_CREATOR_MANAGER, InternalCreatorManager.class.getName());
        config.setInitParameter(INIT_CONVERTER_MANAGER, InternalConverterManager.class.getName());
        config.setInitParameter(INIT_AJAX_FILTER_MANAGER, InternalAjaxFilterManager.class.getName());
    }
    
    
    private static void initApplicationScoped()
    {
        Injector injector = getInjector();
        for (Key<?> key : DwrScopes.APPLICATION.getKeysInScope())
        {
            // Eagerly create application-scoped object.
            injector.getInstance(key);
        }
    }
    
    private static List<Exception> destroyApplicationScoped()
    {
        final List<Exception> exceptions = new ArrayList<Exception>();
        DwrScopes.APPLICATION.closeAll(new ExceptionLoggingCloseableHandler(exceptions));
        return exceptions;
    }


    static String classListToString(List<Class> classList)
    {
        StringBuilder buf = new StringBuilder();
        int count = 0;
        for (Class cls : classList)
        {
            if (count++ > 0) buf.append(", ");
            buf.append(cls.getName());
        }
        return buf.toString();
    }

    
    /**
     * Used to stash context for later use by destroy().
     */
    private volatile ServletContext servletContext;
    
    
    /** 
     * The name DWR uses to look up a CreatorManager implementation class name 
     */
    private static final String INIT_CREATOR_MANAGER = CreatorManager.class.getName();   
    
    /** 
     * The name DWR uses to look up a ConverterManager implementation class name 
     */
    private static final String INIT_CONVERTER_MANAGER = ConverterManager.class.getName();
    
    /** 
     * The name DWR uses to look up an AjaxFilterManager implementation class name 
     */
    private static final String INIT_AJAX_FILTER_MANAGER = AjaxFilterManager.class.getName();

    /**
     * The log stream
     */
    private static final Logger log = Logger.getLogger(DwrGuiceServlet.class);
}
