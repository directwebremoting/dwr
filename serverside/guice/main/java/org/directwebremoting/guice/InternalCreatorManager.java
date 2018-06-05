package org.directwebremoting.guice;

import java.util.Collection;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.directwebremoting.extend.Creator;
import org.directwebremoting.extend.CreatorManager;
import org.directwebremoting.impl.DefaultCreatorManager;
import org.directwebremoting.util.LocalUtil;

import com.google.inject.Injector;
import com.google.inject.Key;

import static org.directwebremoting.guice.DwrGuiceUtil.*;

/**
 * Extends an existing creator manager with an injected list of creators
 * specified at Guice bind-time. Only to be used in conjunction with
 * {@link DwrGuiceServlet}.
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

    /**
     * @param debug Are we in debug mode?
     */
    public void setDebug(boolean debug)
    {
        if (creatorManager instanceof DefaultCreatorManager)
        {
            DefaultCreatorManager dcm = (DefaultCreatorManager) creatorManager;
            dcm.setDebug(debug);
        }
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.extend.CreatorManager#isDebug()
     */
    public boolean isDebug()
    {
        return creatorManager.isDebug();
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.extend.CreatorManager#addCreatorType(java.lang.String, java.lang.String)
     */
    public void addCreatorType(String type, String className)
    {
        creatorManager.addCreatorType(type, className);
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.extend.CreatorManager#addCreator(java.lang.String, java.lang.String, java.util.Map)
     */
    public void addCreator(String type, Map<String, String> params) throws InstantiationException, IllegalAccessException, IllegalArgumentException
    {
        creatorManager.addCreator(type, params);
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.extend.CreatorManager#addCreator(java.lang.String, org.directwebremoting.extend.Creator)
     */
    public void addCreator(Creator creator) throws IllegalArgumentException
    {
        creatorManager.addCreator(creator);
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.extend.CreatorManager#getCreatorNames()
     */
    //@Deprecated
    //
    //public Collection<String> getCreatorNames() throws SecurityException
    //{
    //    return creatorManager.getCreatorNames();
    //}

    /* (non-Javadoc)
     * @see org.directwebremoting.extend.CreatorManager#getCreatorNames()
     */
    public Collection<String> getCreatorNames(boolean includesHidden) throws SecurityException
    {
        return creatorManager.getCreatorNames(includesHidden);
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.extend.CreatorManager#getCreator(java.lang.String)
     */
    //@Deprecated
    //public Creator getCreator(String scriptName) throws SecurityException
    //{
    //    return creatorManager.getCreator(scriptName);
    //}

    /* (non-Javadoc)
     * @see org.directwebremoting.extend.CreatorManager#getCreator(java.lang.String, boolean)
     */
    public Creator getCreator(String scriptName, boolean includesHidden) throws SecurityException
    {
        return creatorManager.getCreator(scriptName, includesHidden);
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.extend.CreatorManager#setCreators(java.util.Map)
     */
    public void setCreators(Map<String, Creator> creators)
    {
        creatorManager.setCreators(creators);
    }

    /**
     * @return accessor for if we create the created objects at startup
     */
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
                if ("".equals(scriptName))
                {
                    Class<?> cls = (Class<?>) key.getTypeLiteral().getType();
                    scriptName = cls.getSimpleName();
                }
                addCreator(new InternalCreator(injector, key, scriptName));
            }
        }
    }

    /**
     * Stores a type name in a thread-local variable for later retrieval by
     * {@code getCreatorManager}.
     * @param name The new type name
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
            @SuppressWarnings("unchecked")
            Class<? extends CreatorManager> cls = (Class<? extends CreatorManager>) LocalUtil.classForName(name);
            return cls.newInstance();
        }
        catch (Exception e)
        {
            if (name != null && !"".equals(name))
            {
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
    private static final Log log = LogFactory.getLog(InternalCreatorManager.class);
}
