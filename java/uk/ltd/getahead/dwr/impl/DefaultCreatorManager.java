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
package uk.ltd.getahead.dwr.impl;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import uk.ltd.getahead.dwr.Creator;
import uk.ltd.getahead.dwr.CreatorManager;
import uk.ltd.getahead.dwr.Messages;
import uk.ltd.getahead.dwr.util.LocalUtil;
import uk.ltd.getahead.dwr.util.Logger;

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
     * @see uk.ltd.getahead.dwr.CreatorManager#isDebug()
     */
    public boolean isDebug()
    {
        return debug;
    }

    /* (non-Javadoc)
     * @see uk.ltd.getahead.dwr.CreatorManager#addCreatorType(java.lang.String, java.lang.Class)
     */
    public void addCreatorType(String typeName, Class clazz)
    {
        if (!Creator.class.isAssignableFrom(clazz))
        {
            throw new IllegalArgumentException(Messages.getString("DefaultCreatorManager.CreatorNotAssignable", clazz.getName(), Creator.class.getName())); //$NON-NLS-1$
        }

        try
        {
            clazz.newInstance();
        }
        catch (InstantiationException ex)
        {
            throw new IllegalArgumentException(Messages.getString("DefaultCreatorManager.CreatorNotInstantiatable", clazz.getName(), ex.toString())); //$NON-NLS-1$
        }
        catch (IllegalAccessException ex)
        {
            throw new IllegalArgumentException(Messages.getString("DefaultCreatorManager.CreatorNotAccessable", clazz.getName(), ex.toString())); //$NON-NLS-1$
        }

        creatorTypes.put(typeName, clazz);
    }

    /* (non-Javadoc)
     * @see uk.ltd.getahead.dwr.CreatorManager#addCreator(java.lang.String, java.lang.String, java.util.Map)
     */
    public void addCreator(String typeName, String scriptName, Map params) throws InstantiationException, IllegalAccessException, IllegalArgumentException
    {
        Class clazz = (Class) creatorTypes.get(typeName);

        if (clazz == null)
        {
            log.error("Missing creator: " + typeName + " (while initializing creator for: " + scriptName + ".js)"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            return;
        }

        Creator creator = (Creator) clazz.newInstance();

        // Initialize the creator with the parameters that we know of.
        for (Iterator it = params.entrySet().iterator(); it.hasNext();)
        {
            Map.Entry entry = (Entry) it.next();
            String key = (String) entry.getKey();
            Object value = entry.getValue();

            try
            {
                LocalUtil.setProperty(creator, key, value);
            }
            catch (NoSuchMethodException ex)
            {
                // No-one has setCreator or setClass, so don't warn about it
                if (!key.equals("creator") && !key.equals("class")) //$NON-NLS-1$ //$NON-NLS-2$
                {
                    log.debug("No property '" + key + "' on " + creator.getClass().getName()); //$NON-NLS-1$ //$NON-NLS-2$
                }
            }
            catch (InvocationTargetException ex)
            {
                log.warn("Error setting " + key + "=" + value + " on " + creator.getClass().getName(), ex.getTargetException()); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            }
            catch (Exception ex)
            {
                log.warn("Error setting " + key + "=" + value + " on " + creator.getClass().getName(), ex); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            }
        }

        creator.setProperties(params);

        // add the creator for the script name
        addCreator(scriptName, creator);
    }

    /* (non-Javadoc)
     * @see uk.ltd.getahead.dwr.CreatorManager#addCreator(java.lang.String, uk.ltd.getahead.dwr.Creator)
     */
    public void addCreator(String scriptName, Creator creator) throws IllegalArgumentException
    {
        // Check that we don't have this one already
        Creator other = (Creator) creators.get(scriptName);
        if (other != null)
        {
            throw new IllegalArgumentException(Messages.getString("DefaultCreatorManager.DuplicateName", scriptName, other.getType().getName(), creator)); //$NON-NLS-1$
        }

        // Check that it can at least tell us what type of thing we will be getting
        try
        {
            Class test = creator.getType();
            if (test == null)
            {
                log.error("Creator: '" + creator + "' for " + scriptName + ".js is returning null for type queries."); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            }
            else
            {
                creators.put(scriptName, creator);
            }
        }
        catch (NoClassDefFoundError ex)
        {
            log.error("Missing class for creator '" + creator + "'. Cause: " + ex.getMessage()); //$NON-NLS-1$ //$NON-NLS-2$
        }
        catch (Exception ex)
        {
            log.error("Error loading class for creator '" + creator + "'.", ex); //$NON-NLS-1$ //$NON-NLS-2$
        }
    }

    /* (non-Javadoc)
     * @see uk.ltd.getahead.dwr.CreatorManager#getCreatorNames()
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
     * @see uk.ltd.getahead.dwr.CreatorManager#getCreator(java.lang.String)
     */
    public Creator getCreator(String scriptName) throws SecurityException
    {
        Creator creator = (Creator) creators.get(scriptName);
        if (creator == null)
        {
            StringBuffer buffer = new StringBuffer("Names of known classes are: "); //$NON-NLS-1$
            for (Iterator it = creators.keySet().iterator(); it.hasNext();)
            {
                String key = (String) it.next();
                buffer.append(key);
                buffer.append(' ');
            }

            log.warn(buffer.toString());
            throw new SecurityException(Messages.getString("DefaultCreatorManager.MissingName", scriptName)); //$NON-NLS-1$
        }

        return creator;
    }

    /* (non-Javadoc)
     * @see uk.ltd.getahead.dwr.CreatorManager#setCreators(java.util.Map)
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
}
