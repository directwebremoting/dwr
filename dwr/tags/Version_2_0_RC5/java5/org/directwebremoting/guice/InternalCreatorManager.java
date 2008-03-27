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

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.directwebremoting.extend.Creator;
import org.directwebremoting.extend.CreatorManager;
import org.directwebremoting.impl.DefaultCreatorManager;
import org.directwebremoting.util.Logger;

import static org.directwebremoting.guice.DwrGuiceUtil.getInjector;
import static org.directwebremoting.guice.DwrGuiceUtil.getServletContext;

/**
 * Extends an existing creator manager with an injected list of creators
 * specified at Guice bind-time. Only to be used in conjection with {@link DwrGuiceServlet}.
 * @author Tim Peierls [tim at peierls dot net]
 */
public class InternalCreatorManager implements CreatorManager 
{
    /**
     * Retrieves an underlying creator manager from thread-local state
     * to which this class delegates {@link CreatorManager} calls.
     * Adds any creators found from the Guice bindings.
     */
    public InternalCreatorManager()
    {
        this.creatorManager = getCreatorManager();
        addCreators();
    }

    public void setDebug(boolean debug)
    {
        if (creatorManager instanceof DefaultCreatorManager)
        {
            DefaultCreatorManager dcm = (DefaultCreatorManager) creatorManager;
            dcm.setDebug(debug);
        }
    }

    public boolean isDebug()
    {
        return creatorManager.isDebug();
    }

    public void addCreatorType(String typeName, String className)
    {
        creatorManager.addCreatorType(typeName, className);
    }

    public void addCreator(String scriptName, String typeName, Map params) throws InstantiationException, IllegalAccessException, IllegalArgumentException
    {
        creatorManager.addCreator(scriptName, typeName, params);
    }

    public void addCreator(String scriptName, Creator creator) throws IllegalArgumentException
    {
        creatorManager.addCreator(scriptName, creator);
    }

    public Collection getCreatorNames() throws SecurityException
    {
        return creatorManager.getCreatorNames();
    }

    public Creator getCreator(String scriptName) throws SecurityException
    {
        return creatorManager.getCreator(scriptName);
    }

    public void setCreators(Map creators)
    {
        creatorManager.setCreators(creators);
    }
    
    public boolean isInitApplicationScopeCreatorsAtStartup()
    {
        if (creatorManager instanceof DefaultCreatorManager)
        {
            DefaultCreatorManager dcm = (DefaultCreatorManager) creatorManager;
            return dcm.isInitApplicationScopeCreatorsAtStartup();
        }
        return false;
    }

    public void setInitApplicationScopeCreatorsAtStartup(boolean initApplicationScopeCreatorsAtStartup)
    {
        if (creatorManager instanceof DefaultCreatorManager)
        {
            DefaultCreatorManager dcm = (DefaultCreatorManager) creatorManager;
            dcm.setInitApplicationScopeCreatorsAtStartup(initApplicationScopeCreatorsAtStartup);
        }
    }

    
    private final CreatorManager creatorManager;

    
    private void addCreators()
    {
        Injector injector = getInjector();
        for (Key<?> key : injector.getBindings().keySet())
        {
            Class<?> atype = key.getAnnotationType();
            if (atype != null && Remoted.class.isAssignableFrom(atype))
            {
                String scriptName = Remoted.class.cast(key.getAnnotation()).value();
                if (scriptName.equals(""))
                {
                    Class cls = (Class) key.getTypeLiteral().getType();
                    scriptName = cls.getSimpleName();
                }
                addCreator(scriptName, new InternalCreator(injector, key, scriptName));
            }
        }
    }

    
    /**
     * Stores a type name in a thread-local variable for later retrieval by
     * {@code getCreatorManager}.
     */
    static void setTypeName(String name)
    {
        typeName.set(name);
    }
    
    private static CreatorManager getCreatorManager()
    {
        String name = typeName.get();
        try
        {
            Class<? extends CreatorManager> cls = 
                (Class<? extends CreatorManager>) Class.forName(name);
            return cls.newInstance();
        }
        catch (Exception e)
        {
            if (name != null && !"".equals(name)) {
                log.warn("Couldn't make CreatorManager from type: " + name);
            }
            return new DefaultCreatorManager();
        }
    }

    /**
     * Place to stash a type name for retrieval in same thread.
     */
    private static final ThreadLocal<String> typeName = new ThreadLocal<String>();


    /**
     * The log stream
     */
    private static final Logger log = Logger.getLogger(InternalCreatorManager.class);
}
