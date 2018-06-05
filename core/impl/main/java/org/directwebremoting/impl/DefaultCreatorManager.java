package org.directwebremoting.impl;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.directwebremoting.WebContext;
import org.directwebremoting.WebContextFactory;
import org.directwebremoting.extend.Creator;
import org.directwebremoting.extend.CreatorManager;
import org.directwebremoting.extend.UninitializingBean;
import org.directwebremoting.util.LocalUtil;
import org.directwebremoting.util.Loggers;

/**
 * A class to manage the types of creators and the instantiated creators.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class DefaultCreatorManager implements CreatorManager, UninitializingBean
{
    /* (non-Javadoc)
     * @see org.directwebremoting.CreatorManager#addCreatorType(java.lang.String, java.lang.String)
     */
    public void addCreatorType(String typeName, String className)
    {
        if (!LocalUtil.isJavaIdentifier(typeName))
        {
            Loggers.STARTUP.error("Illegal identifier: '" + typeName + "'");
            return;
        }

        Class<? extends Creator> clazz = LocalUtil.classForName(typeName, className, Creator.class);
        if (clazz != null)
        {
            Loggers.STARTUP.debug("- adding creator type: " + typeName + " = " + clazz);
            creatorTypes.put(typeName, clazz);
        }
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.CreatorManager#addCreator(java.lang.String, java.lang.String, java.util.Map)
     */
    public void addCreator(String typeName, Map<String, String> params) throws InstantiationException, IllegalAccessException, IllegalArgumentException
    {
        Class<? extends Creator> clazz = creatorTypes.get(typeName);
        if (clazz == null)
        {
            Loggers.STARTUP.error("Missing creator: " + typeName + " (while initializing creator for: " + params.get("javascript") + ".js)");
            return;
        }

        Creator creator = clazz.newInstance();

        LocalUtil.setParams(creator, params, ignore);
        creator.setProperties(params);

        // add the creator for the script name
        addCreator(creator);
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.CreatorManager#addCreator(java.lang.String, org.directwebremoting.Creator)
     */
    public void addCreator(Creator creator) throws IllegalArgumentException
    {
        // Check that we don't have this one already
        Creator other = creators.get(creator.getJavascript());
        if (other != null)
        {
            Loggers.STARTUP.error("Javascript name " + creator.getJavascript() + " is used by 2 classes (" + other.getType().getName() + " and " + creator + ")");
            throw new IllegalArgumentException("Duplicate name found. See logs for details.");
        }

        // Check that it can at least tell us what type of thing we will be getting
        try
        {
            Class<?> test = creator.getType();
            if (test == null)
            {
                Loggers.STARTUP.error("Creator: '" + creator + "' for " + creator.getJavascript() + ".js is returning null for type queries.");
            }
            else
            {
                Loggers.STARTUP.debug("- adding creator: " + creator.getClass().getSimpleName() + " for " + creator.getJavascript());
                creators.put(creator.getJavascript(), creator);
            }
        }
        catch (NoClassDefFoundError ex)
        {
            Loggers.STARTUP.error("Missing class for creator '" + creator + "'. Cause: " + ex.getMessage());
        }
        catch (Exception ex)
        {
            Loggers.STARTUP.error("Error loading class for creator '" + creator + "'.", ex);
        }

        // If this is application scope then it might make sense to create one
        // now rather than wait for first use. Otherwise this job is done by
        // DefaultRemoter.execute(Call call)
        if (initApplicationScopeCreatorsAtStartup && creator.getScope().equals(Creator.APPLICATION))
        {
            try
            {
                Object object = creator.getInstance();
                servletContext.setAttribute(creator.getJavascript(), object);

                Loggers.STARTUP.debug("Created new " + creator.getJavascript() + ", stored in application.");
            }
            catch (InstantiationException ex)
            {
                Loggers.STARTUP.warn("Failed to create " + creator.getJavascript(), ex);
                Loggers.STARTUP.debug("Maybe it will succeed when the application is in flight. If so you should probably set initApplicationScopeCreatorsAtStartup=false in web.xml");
            }
        }
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.CreatorManager#getCreatorNames(boolean)
     */
    public Collection<String> getCreatorNames(boolean includeHidden) throws SecurityException
    {
        if (includeHidden)
        {
            return Collections.unmodifiableSet(creators.keySet());
        }
        else
        {
            Collection<String> noHidden = new HashSet<String>();
            for (Map.Entry<String, Creator> entry : creators.entrySet())
            {
                Creator creator = entry.getValue();
                if (!creator.isHidden())
                {
                    noHidden.add(entry.getKey());
                }
            }

            return noHidden;
        }
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.CreatorManager#getCreator(java.lang.String, boolean)
     */
    public Creator getCreator(String scriptName, boolean includeHidden) throws SecurityException
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

            log.error("Class not found: '" + scriptName + "'");
            log.warn(buffer.toString());
            throw new SecurityException("Class not found");
        }

        if (creator.isHidden() && !includeHidden)
        {
            log.warn("Attempt made to get hidden class with name: " + scriptName + " while includeHidden=false");
            throw new SecurityException("Class not found");
        }

        return creator;
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.extend.UninitializingBean#destroy()
     */
    public void destroy()
    {
        WebContext webcx = WebContextFactory.get();
        for(Creator c : creators.values()) {
            if (c.getScope().equals(Creator.APPLICATION)) {
                Object creatorInstance = webcx.getServletContext().getAttribute(c.getJavascript());
                if (creatorInstance instanceof UninitializingBean) {
                    ((UninitializingBean) creatorInstance).destroy();
                }
            }
        }
    }

    /**
     * The list of the available creators
     */
    protected Map<String, Class<? extends Creator>> creatorTypes = new HashMap<String, Class<? extends Creator>>();

    /* (non-Javadoc)
     * @see org.directwebremoting.CreatorManager#setCreators(java.util.Map)
     */
    public void setCreators(Map<String, Creator> creators)
    {
        this.creators = creators;
    }

    /**
     * The list of the configured creators
     */
    protected Map<String, Creator> creators = new HashMap<String, Creator>();

    /* (non-Javadoc)
     * @see org.directwebremoting.CreatorManager#isDebug()
     */
    public boolean isDebug()
    {
        return debug;
    }

    /**
     * Set the debug status
     * @param debug The new debug setting
     */
    public void setDebug(boolean debug)
    {
        this.debug = debug;
    }

    /**
     * Are we in debug mode?
     */
    protected boolean debug = false;

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
     * When initApplicationScopeCreatorsAtStartup = true we need somewhere to
     * store the objects
     */
    public void setServletContext(ServletContext servletContext)
    {
        this.servletContext = servletContext;
    }

    /**
     * Do we do full-create on startup?
     */
    protected boolean initApplicationScopeCreatorsAtStartup = false;

    /**
     * @see #setServletContext(ServletContext)
     */
    protected ServletContext servletContext;

    /**
     * The properties that we don't warn about if they don't exist.
     * @see DefaultCreatorManager#addCreator(String, Map)
     */
    protected static final List<String> ignore = Arrays.asList("creator", "class");

    /**
     * The log stream
     */
    private static final Log log = LogFactory.getLog(DefaultCreatorManager.class);
}
