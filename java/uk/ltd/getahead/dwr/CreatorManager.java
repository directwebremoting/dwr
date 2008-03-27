package uk.ltd.getahead.dwr;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import uk.ltd.getahead.dwr.util.LocalUtil;
import uk.ltd.getahead.dwr.util.Logger;

/**
 * A class to manage the types of creators and the instansiated creators.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class CreatorManager
{
    /**
     * Debug mode allows access to the list of creator names
     * @param debug Are we in debug mode
     * @see CreatorManager#getCreatorNames()
     */
    public void setDebug(boolean debug)
    {
        this.debug = debug;
    }

    /**
     * Debug mode allows access to the list of creator names
     * @return Are we in debug mode
     * @see CreatorManager#getCreatorNames()
     */
    public boolean isDebug()
    {
        return debug;
    }

    /**
     * In init mode, add a new type of creator
     * @param typename The name of the new creator type
     * @param clazz The class that we create
     */
    public void addCreatorType(String typename, Class clazz)
    {
        if (!Creator.class.isAssignableFrom(clazz))
        {
            throw new IllegalArgumentException(Messages.getString("DefaultCreatorManager.CreatorNotAssignable", clazz.getName(), Creator.class.getName())); //$NON-NLS-1$
        }

        creatorTypes.put(typename, clazz);
    }

    /**
     * Add a new creator
     * @param typename The class to use as a creator
     * @param scriptName The name of the creator to Javascript
     * @param params The extra parameters to allow the creator to configure itself
     * @throws InstantiationException If reflection based creation fails
     * @throws IllegalAccessException If reflection based creation fails
     * @throws IllegalArgumentException If we have a duplicate name
     */
    public void addCreator(String typename, String scriptName, Map params) throws InstantiationException, IllegalAccessException, IllegalArgumentException
    {
        Class clazz = (Class) creatorTypes.get(typename);

        Creator creator = (Creator) clazz.newInstance();

        // Initialize the creator with the parameters that we know of.
        for (Iterator it = params.entrySet().iterator(); it.hasNext();)
        {
            Map.Entry entry = (Entry) it.next();
            String key = (String) entry.getKey();

            try
            {
                LocalUtil.setProperty(creator, key, entry.getValue());
            }
            catch (Exception ex)
            {
                // No-one has a setCreator method, so don't warn about it
                if (!key.equals("creator")) //$NON-NLS-1$
                {
                    log.debug("No property '" + key + "' on class " + creator.getClass().getName()); //$NON-NLS-1$ //$NON-NLS-2$
                }
            }
        }

        creator.setProperties(params);

        // Check that we don't have this one already
        Creator other = (Creator) creators.get(scriptName);
        if (other != null)
        {
            throw new IllegalArgumentException(Messages.getString("DefaultCreatorManager.DuplicateName", scriptName, other.getType().getName(), typename)); //$NON-NLS-1$
        }

        creators.put(scriptName, creator);
    }

    /**
     * Get a list of the javascript names of the allowed creators.
     * This method could be seen as a security risk because it could allow an
     * attacker to find out extra information about your system so it is only
     * available if debug is turned on.
     * @return Loop over all the known allowed classes
     * @throws SecurityException If we are not in debug mode
     * @see CreatorManager#setDebug(boolean)
     */
    public Collection getCreatorNames() throws SecurityException
    {
        if (!debug)
        {
            throw new SecurityException();
        }

        return Collections.unmodifiableSet(creators.keySet());
    }

    /**
     * Find an <code>Creator</code> by name
     * @param scriptName The name of the creator to Javascript
     * @return The found Creator instance, or null if none was found.
     * @throws SecurityException If the Creator is not known
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

    /**
     * Sets the creators for this creator manager.
     * @param creators the map of managed beans and their creator instances
     */
    public void setCreators(Map creators)
    {
        this.creators = creators;
    }

    /**
     * The log stream
     */
    private static final Logger log = Logger.getLogger(CreatorManager.class);

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
