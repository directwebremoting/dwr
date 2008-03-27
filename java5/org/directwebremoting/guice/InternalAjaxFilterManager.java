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

import com.google.inject.Key;
import com.google.inject.Injector;
import com.google.inject.Provider;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import org.directwebremoting.AjaxFilter;
import org.directwebremoting.impl.DefaultAjaxFilterManager;
import org.directwebremoting.extend.*;
import org.directwebremoting.util.Logger;

import static org.directwebremoting.guice.DwrGuiceUtil.getInjector;
import static org.directwebremoting.guice.DwrGuiceUtil.getServletContext;

/**
 * Extends an existing ajax filter manager with an injected list of ajax filters
 * specified at Guice bind-time. Only to be used in conjection with
 * {@link DwrGuiceServlet}.
 * @author Tim Peierls [tim at peierls dot net]
 */
public class InternalAjaxFilterManager implements AjaxFilterManager 
{
    /**
     * Retrieves an underlying ajaxFilter manager from thread-local state
     * to which this class delegates {@link AjaxFilterManager} calls.
     */
    public InternalAjaxFilterManager()
    {
        this.ajaxFilterManager = getAjaxFilterManager();
        addAjaxFilters();
    }

    public Iterator<AjaxFilter> getAjaxFilters(String scriptname)
    {
        return (Iterator<AjaxFilter>) ajaxFilterManager.getAjaxFilters(scriptname);
    }

    public void addAjaxFilter(AjaxFilter filter)
    {
        ajaxFilterManager.addAjaxFilter(filter);
    }

    public void addAjaxFilter(AjaxFilter filter, String scriptname)
    {
        ajaxFilterManager.addAjaxFilter(filter, scriptname);
    }

    
    private final AjaxFilterManager ajaxFilterManager;

    private void addAjaxFilters()
    {
        Injector injector = getInjector();
        for (Key<?> key : injector.getBindings().keySet())
        {
            Class<?> atype = key.getAnnotationType();
            if (atype != null && Filtering.class.isAssignableFrom(atype))
            {
                String scriptName = Filtering.class.cast(key.getAnnotation()).value();
                Provider<AjaxFilter> provider = injector.getProvider((Key<AjaxFilter>) key);
                if ("".equals(scriptName))
                {
                    addAjaxFilter(new InternalAjaxFilter(provider));
                }
                else
                {
                    addAjaxFilter(new InternalAjaxFilter(provider), scriptName);
                }
            }
        }
    }

    
    /**
     * Stores a type name in a thread-local variable for later retrieval by
     * {@code getAjaxFilterManager}.
     */
    static void setTypeName(String name)
    {
        typeName.set(name);
    }
    
    private static AjaxFilterManager getAjaxFilterManager()
    {
        String name = typeName.get();
        try
        {
            Class<? extends AjaxFilterManager> cls = 
                (Class<? extends AjaxFilterManager>) Class.forName(name);
            return cls.newInstance();
        }
        catch (Exception e)
        {
            if (name != null && !"".equals(name)) {
                log.warn("Couldn't make AjaxFilterManager from type: " + name);
            }
            return new DefaultAjaxFilterManager();
        }
    }


    /**
     * Place to stash a type name for retrieval in same thread.
     */
    private static final ThreadLocal<String> typeName = new ThreadLocal<String>();


    /**
     * The log stream
     */
    private static final Logger log = Logger.getLogger(InternalAjaxFilterManager.class);
}
