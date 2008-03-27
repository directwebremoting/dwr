package uk.ltd.getahead.dwr.impl;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import uk.ltd.getahead.dwr.Creator;
import uk.ltd.getahead.dwr.CreatorManager;
import uk.ltd.getahead.dwr.Messages;
import uk.ltd.getahead.dwr.util.Logger;

/**
 * A class to manage the types of creators and the instansiated creators.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class DefaultCreatorManager implements CreatorManager
{
    /* (non-Javadoc)
     * @see uk.ltd.getahead.dwr.CreatorManager#setDebug(boolean)
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
    public void addCreatorType(String typename, Class clazz)
    {
        if (!Creator.class.isAssignableFrom(clazz))
        {
            throw new IllegalArgumentException(Messages.getString("DefaultCreatorManager.CreatorNotAssignable", clazz.getName(), Creator.class.getName())); //$NON-NLS-1$
        }

        creatorTypes.put(typename, clazz);
    }

    /* (non-Javadoc)
     * @see uk.ltd.getahead.dwr.CreatorManager#addCreator(java.lang.String, java.lang.String, org.w3c.dom.Element)
     */
    public void addCreator(String type, String javascript, Map params) throws InstantiationException, IllegalAccessException, IllegalArgumentException
    {
        Class clazz = (Class) creatorTypes.get(type);

        Creator creator = (Creator) clazz.newInstance();
        Class real = creator.getClass();

        // Initialize the creator with the parameters that we know of.
        for (Iterator it = params.entrySet().iterator(); it.hasNext();)
        {
            Map.Entry entry = (Entry) it.next();
            String key = (String) entry.getKey();
            String value = (String) entry.getValue();

            String setterName = "set" + key.substring(0, 1).toUpperCase() + key.substring(1); //$NON-NLS-1$

            try
            {
                Method method = real.getMethod(setterName, new Class[] { String.class });
                if (method == null)
                {
                    log.error("Failed to find method: " + setterName + "(String s) on class " + real.getName()); //$NON-NLS-1$ //$NON-NLS-2$
                    continue;
                }

                method.invoke(creator, new Object[] { value });
            }
            catch (Exception ex)
            {
                // No-one has a setCreator method, so don't warn about it
                if (!key.equals("creator")) //$NON-NLS-1$
                {
                    log.debug("No method '" + setterName + "(String s)' on class " + real.getName()); //$NON-NLS-1$ //$NON-NLS-2$
                }
            }
        }
        
        creator.setProperties(params);

        // Check that we don't have this one already
        Creator other = (Creator) creators.get(javascript);
        if (other != null)
        {
            throw new IllegalArgumentException(Messages.getString("DefaultCreatorManager.DuplicateName", javascript, other.getType().getName(), type)); //$NON-NLS-1$
        }

        creators.put(javascript, creator);
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
    public Creator getCreator(String name)
    {
        Creator creator = (Creator) creators.get(name);
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
            throw new SecurityException(Messages.getString("DefaultCreatorManager.MissingName", name)); //$NON-NLS-1$
        }

        return creator;
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
    private boolean debug;
}
