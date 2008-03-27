package uk.ltd.getahead.dwr;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.w3c.dom.Element;

import uk.ltd.getahead.dwr.util.Log;

/**
 * A class to manage the types of creators and the instansiated creators.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public final class CreatorManager
{
    /**
     * Simple ctor
     * @param debug Are we in debug mode
     */
    public CreatorManager(boolean debug)
    {
        this.debug = debug;
    }

    /**
     * In init mode, add a new type of creator
     * @param typename The name of the new creator type
     * @param clazz The class that we create
     */
    protected void addCreatorType(String typename, Class clazz)
    {
        if (!Creator.class.isAssignableFrom(clazz))
        {
            throw new IllegalArgumentException(Messages.getString("CreatorManager.CreatorNotAssignabe", clazz.getName(), Creator.class.getName())); //$NON-NLS-1$
        }

        creatorTypes.put(typename, clazz);
    }

    /**
     * Add a new creator
     * @param type The class to use as a creator
     * @param javascript The name for it in Javascript
     * @param allower The DOM element in case we need more info
     * @throws InstantiationException If reflection based creation fails
     * @throws IllegalAccessException If reflection based creation fails
     * @throws IllegalArgumentException If we have a duplicate name
     */
    protected void addCreator(String type, String javascript, Element allower) throws InstantiationException, IllegalAccessException, IllegalArgumentException
    {
        Class clazz = (Class) creatorTypes.get(type);

        Creator creator = (Creator) clazz.newInstance();
        creator.init(allower);

        // Check that we don't have this one already
        Creator other = (Creator) creators.get(javascript);
        if (other != null)
        {
            throw new IllegalArgumentException(Messages.getString("CreatorManager.DuplicateName", javascript, other.getType().getName(), type)); //$NON-NLS-1$
        }

        creators.put(javascript, creator);
    }

    /**
     * Get a list of the javascript names of the allowed creators.
     * This method could be seen as a security risk because it could allow an
     * attacker to find out extra information about your system so it is only
     * available if debug is turned on.
     * @return Loop over all the known allowed classes
     */
    protected Collection getCreatorNames()
    {
        if (!debug)
        {
            throw new SecurityException();
        }

        return Collections.unmodifiableSet(creators.keySet());
    }

    /**
     * Find an <code>Creator</code> by name
     * @param name The name to lookup against
     * @return The found Creator instance, or null if none was found.
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

            Log.warn(buffer.toString());
            throw new SecurityException(Messages.getString("CreatorManager.MissingName", name)); //$NON-NLS-1$
        }

        return creator;
    }

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
