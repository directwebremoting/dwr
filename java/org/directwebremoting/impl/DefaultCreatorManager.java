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
package org.directwebremoting.impl;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;
import org.directwebremoting.WebContext;
import org.directwebremoting.WebContextFactory;
import org.directwebremoting.extend.Creator;
import org.directwebremoting.extend.CreatorManager;
import org.directwebremoting.util.LocalUtil;
import org.directwebremoting.util.Messages;

/**
 * A class to manage the types of creators and the instantiated creators.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class DefaultCreatorManager implements CreatorManager
{
    /**
     * Set the debug status
     * @param debug The new debug setting
     */
    public void setDebug(boolean debug)
    {
        this.debug = debug;
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.CreatorManager#isDebug()
     */
    public boolean isDebug()
    {
        return debug;
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.CreatorManager#addCreatorType(java.lang.String, java.lang.String)
     */
    public void addCreatorType(String typeName, String className)
    {
        if (!LocalUtil.isJavaIdentifier(typeName))
        {
            log.error("Illegal identifier: '" + typeName + "'");
            return;
        }

        Class<? extends Creator> clazz = LocalUtil.classForName(typeName, className, Creator.class);
        if (clazz != null)
        {
            log.debug("- adding creator type: " + typeName + " = " + clazz);
            creatorTypes.put(typeName, clazz);
        }
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.CreatorManager#addCreator(java.lang.String, java.lang.String, java.util.Map)
     */
    public void addCreator(String scriptName, String creatorName, Map<String, String> params) throws InstantiationException, IllegalAccessException, IllegalArgumentException
    {
        Class<? extends Creator> clazz = creatorTypes.get(creatorName);
        if (clazz == null)
        {
            log.error("Missing creator: " + creatorName + " (while initializing creator for: " + scriptName + ".js)");
            return;
        }

        Creator creator = clazz.newInstance();

        LocalUtil.setParams(creator, params, ignore);
        creator.setProperties(params);

        // add the creator for the script name
        addCreator(creator.getJavascript(), creator);
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.CreatorManager#addCreator(java.lang.String, org.directwebremoting.Creator)
     */
    public void addCreator(String scriptName, Creator creator) throws IllegalArgumentException
    {
        // Check that we don't have this one already
        Creator other = creators.get(scriptName);
        if (other != null)
        {
            throw new IllegalArgumentException(Messages.getString("DefaultCreatorManager.DuplicateName", scriptName, other.getType().getName(), creator));
        }

        // Check that it can at least tell us what type of thing we will be getting
        try
        {
            Class<?> test = creator.getType();
            if (test == null)
            {
                log.error("Creator: '" + creator + "' for " + scriptName + ".js is returning null for type queries.");
            }
            else
            {
                log.debug("- adding creator: " + creator.getClass().getSimpleName() + " for " + scriptName);
                creators.put(scriptName, creator);
            }
        }
        catch (NoClassDefFoundError ex)
        {
            log.error("Missing class for creator '" + creator + "'. Cause: " + ex.getMessage());
        }
        catch (Exception ex)
        {
            log.error("Error loading class for creator '" + creator + "'.", ex);
        }

        // If this is application scope then it might make sense to create one
        // now rather than wait for first use. Otherwise this job is done by
        // DefaultRemoter.execute(Call call)
        if (initApplicationScopeCreatorsAtStartup && creator.getScope().equals(Creator.APPLICATION))
        {
            try
            {
                WebContext webcx = WebContextFactory.get();
                Object object = creator.getInstance();            
                webcx.getServletContext().setAttribute(creator.getJavascript(), object);

                log.debug("Created new " + creator.getJavascript() + ", stored in application.");
            }
            catch (InstantiationException ex)
            {
                log.warn("Failed to create " + creator.getJavascript(), ex);
                log.debug("Maybe it will succeed when the application is in flight. If so you should probably set initApplicationScopeCreatorsAtStartup=false in web.xml");
            }
        }
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.CreatorManager#getCreatorNames()
     */
    public Collection<String> getCreatorNames() throws SecurityException
    {
        if (!debug)
        {
            throw new SecurityException();
        }

        return Collections.unmodifiableSet(creators.keySet());
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.CreatorManager#getCreator(java.lang.String)
     */
    public Creator getCreator(String scriptName) throws SecurityException
    {
        Creator creator = creators.get(scriptName);
        if (creator == null)
        {
            StringBuffer buffer = new StringBuffer("Names of known classes are: ");
            for (String key : creators.keySet())
            {
                buffer.append(key);
                buffer.append(' ');
            }

            log.warn(buffer.toString());
            throw new SecurityException(Messages.getString("DefaultCreatorManager.MissingName", scriptName));
        }

        return creator;
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.CreatorManager#setCreators(java.util.Map)
     */
    public void setCreators(Map<String, Creator> creators)
    {
        this.creators = creators;
    }

    /**
     * Do we do full-create on startup?
     * @return true if we are doing full-create
     */
    public boolean isInitApplicationScopeCreatorsAtStartup()
    {
        return initApplicationScopeCreatorsAtStartup;
    }

    /**
     * Do we do full-create on startup?
     * @param initApplicationScopeCreatorsAtStartup true for full create
     */
    public void setInitApplicationScopeCreatorsAtStartup(boolean initApplicationScopeCreatorsAtStartup)
    {
        this.initApplicationScopeCreatorsAtStartup = initApplicationScopeCreatorsAtStartup;
    }

    /**
     * The log stream
     */
    private static final Log log = LogFactory.getLog(DefaultCreatorManager.class);

    /**
     * The list of the available creators
     */
    protected Map<String, Class<? extends Creator>> creatorTypes = new HashMap<String, Class<? extends Creator>>();

    /**
     * The list of the configured creators
     */
    protected Map<String, Creator> creators = new HashMap<String, Creator>();

    /**
     * Are we in debug mode?
     */
    protected boolean debug = false;

    /**
     * Do we do full-create on startup?
     */
    protected boolean initApplicationScopeCreatorsAtStartup = false;

    /**
     * The properties that we don't warn about if they don't exist.
     * @see DefaultCreatorManager#addCreator(String, String, Map)
     */
    protected static List<String> ignore = Arrays.asList("creator", "class");
}
