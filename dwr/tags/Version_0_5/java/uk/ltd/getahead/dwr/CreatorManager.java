package uk.ltd.getahead.dwr;

import java.util.Collection;

import org.w3c.dom.Element;

/**
 * A class to manage the types of creators and the instansiated creators.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public interface CreatorManager
{
    /**
     * Debug mode allows access to the list of creator names
     * @param debug Are we in debug mode
     * @see CreatorManager#getCreatorNames()
     */
    public void setDebug(boolean debug);

    /**
     * Debug mode allows access to the list of creator names
     * @return Are we in debug mode
     * @see CreatorManager#getCreatorNames()
     */
    public boolean isDebug();

    /**
     * In init mode, add a new type of creator
     * @param typename The name of the new creator type
     * @param clazz The class that we create
     */
    public void addCreatorType(String typename, Class clazz);

    /**
     * Add a new creator
     * @param type The class to use as a creator
     * @param javascript The name for it in Javascript
     * @param allower The DOM element in case we need more info
     * @throws InstantiationException If reflection based creation fails
     * @throws IllegalAccessException If reflection based creation fails
     * @throws IllegalArgumentException If we have a duplicate name
     */
    public void addCreator(String type, String javascript, Element allower) throws InstantiationException, IllegalAccessException, IllegalArgumentException;

    /**
     * Get a list of the javascript names of the allowed creators.
     * This method could be seen as a security risk because it could allow an
     * attacker to find out extra information about your system so it is only
     * available if debug is turned on.
     * @return Loop over all the known allowed classes
     * @see CreatorManager#setDebug(boolean)
     */
    public Collection getCreatorNames();

    /**
     * Find an <code>Creator</code> by name
     * @param name The name to lookup against
     * @return The found Creator instance, or null if none was found.
     */
    public Creator getCreator(String name);
}