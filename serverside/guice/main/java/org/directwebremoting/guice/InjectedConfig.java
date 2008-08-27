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

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.directwebremoting.extend.Configurator;
import org.directwebremoting.util.FakeServletConfig;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Key;

import static org.directwebremoting.guice.ParamName.*;
import static org.directwebremoting.impl.StartupUtil.*;


class InjectedConfig
{
    InjectedConfig(FakeServletConfig config)
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

    void setClassesInitParameter()
    {
        Set<Class<?>> classes = new HashSet<Class<?>>();
        Injector injector = DwrGuiceUtil.getInjector();
        for (Key<?> key : injector.getBindings().keySet())
        {
            Class<?> atype = key.getAnnotationType();
            if (atype != null && InitParam.class.isAssignableFrom(atype))
            {
                InitParam initParam = InitParam.class.cast(key.getAnnotation());
                ParamName paramName = initParam.value();
                if (paramName == CLASSES)
                {
                    @SuppressWarnings("unchecked")
                    List<Class<?>> classList = (List<Class<?>>) injector.getInstance(key);
                    classes.addAll(classList);
                }
            }
        }
        config.setInitParameter(CLASSES.getName(), classesToString(classes));
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

        setClassesInitParameter();
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
    @Inject(optional=true) @InitParam(SESSION_COOKIE_NAME)             String  sessionCookieName = null;
    @Inject(optional=true) @InitParam(WELCOME_FILES)                   String  welcomeFiles = null;
    @Inject(optional=true) @InitParam(NORMALIZE_INCLUDES_QUERY_STRING) Boolean normalizeIncludesQueryString = null;
    @Inject(optional=true) @InitParam(OVERRIDE_PATH)                   String  overridePath = null;

    @Inject(optional=true) Configurator configurator = null;

    private final FakeServletConfig config;


    private static String classesToString(Collection<Class<?>> classes)
    {
        StringBuilder buf = new StringBuilder();
        int count = 0;
        for (Class<?> cls : classes)
        {
            if (count++ > 0)
            {
                buf.append(", ");
            }
            buf.append(cls.getName());
        }
        return buf.toString();
    }
}
