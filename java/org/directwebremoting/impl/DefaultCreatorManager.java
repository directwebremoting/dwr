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
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.directwebremoting.WebContext;
import org.directwebremoting.WebContextFactory;
import org.directwebremoting.extend.Creator;
import org.directwebremoting.extend.CreatorManager;
import org.directwebremoting.util.LocalUtil;
import org.directwebremoting.util.Logger;
import org.directwebremoting.util.Messages;

/**
 * A class to manage the types of creators and the instansiated creators.
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

        Class clazz = LocalUtil.classForName(typeName, className, Creator.class);
        if (clazz != null)
        {
            log.debug("- adding creator type: " + typeName + " = " + clazz);
            creatorTypes.put(typeName, clazz);
        }
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.CreatorManager#addCreator(java.lang.String, java.lang.String, java.util.Map)
     */
    public void addCreator(String scriptName, String typeName, Map params) throws InstantiationException, IllegalAccessException, IllegalArgumentException
    {
        if (!LocalUtil.isJavaIdentifier(scriptName))
        {
            log.error("Illegal identifier: '" + scriptName + "'");
            return;
        }

        Class clazz = (Class) creatorTypes.get(typeName);
        if (clazz == null)
        {
            log.error("Missing creator: " + typeName + " (while initializing creator for: " + scriptName + ".js)");
            return;
        }

        Creator creator = (Creator) clazz.newInstance();

        LocalUtil.setParams(creator, params, ignore);
        creator.setProperties(params);

        // add the creator for the script name
        addCreator(scriptName, creator);
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.CreatorManager#addCreator(java.lang.String, org.directwebremoting.Creator)
     */
    public void addCreator(String scriptName, Creator creator) throws IllegalArgumentException
    {
        // Check that we don't have this one already
        Creator other = (Creator) creators.get(scriptName);
        if (other != null)
        {
            throw new IllegalArgumentException(Messages.getString("DefaultCreatorManager.DuplicateName", scriptName, other.getType().getName(), creator));
        }

        // Check that it can at least tell us what type of thing we will be getting
        try
        {
            Class test = creator.getType();
            if (test == null)
            {
                log.error("Creator: '" + creator + "' for " + scriptName + ".js is returning null for type queries.");
            }
            else
            {
                log.debug("- adding creator: " + LocalUtil.getShortClassName(creator.getClass()) + " for " + scriptName);
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
    public Collection getCreatorNames() throws SecurityException
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
        Creator creator = (Creator) creators.get(scriptName);
        if (creator == null)
        {
            StringBuffer buffer = new StringBuffer("Names of known classes are: ");
            for (Iterator it = creators.keySet().iterator(); it.hasNext();)
            {
                String key = (String) it.next();
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
    public void setCreators(Map creators)
    {
        this.creators = creators;
    }

    /**
     * The log stream
     */
    private static final Logger log = Logger.getLogger(DefaultCreatorManager.class);

    /**
     * The list of the available creators
     */
    private Map creatorTypes = new HashMap();

    /**
     * The list of the configured creators
     */
    private Map creators = new HashMap();

    /**
     * Are we in debug mode?
     */
    private boolean debug = false;

    /**
     * Do we do full-create on startup?
     */
    private boolean initApplicationScopeCreatorsAtStartup = false;

    /**
     * The properties that we don't warn about if they don't exist.
     * @see DefaultCreatorManager#addCreator(String, String, Map)
     */
    private static List ignore = Arrays.asList(new String[] { "creator", "class" });

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
}
