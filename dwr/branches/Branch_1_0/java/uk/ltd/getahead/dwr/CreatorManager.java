package uk.ltd.getahead.dwr;

import java.util.Collection;
import java.util.Map;

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
     * @param typename The class to use as a creator
     * @param scriptName The name of the creator to Javascript
     * @param params The extra parameters to allow the creator to configure itself
     * @throws InstantiationException If reflection based creation fails
     * @throws IllegalAccessException If reflection based creation fails
     * @throws IllegalArgumentException If we have a duplicate name
     */
    public void addCreator(String typename, String scriptName, Map params) throws InstantiationException, IllegalAccessException, IllegalArgumentException;

    /**
     * Get a list of the javascript names of the allowed creators.
     * This method could be seen as a security risk because it could allow an
     * attacker to find out extra information about your system so it is only
     * available if debug is turned on.
     * @return Loop over all the known allowed classes
     * @throws SecurityException If we are not in debug mode
     * @see CreatorManager#setDebug(boolean)
     */
    public Collection getCreatorNames() throws SecurityException;

    /**
     * Find an <code>Creator</code> by name
     * @param scriptName The name of the creator to Javascript
     * @return The found Creator instance, or null if none was found.
     * @throws SecurityException If the Creator is not known
     */
    public Creator getCreator(String scriptName) throws SecurityException;

    /**
     * Sets the creators for this creator manager.
     * @param creators the map of managed beans and their creator instances
     */
    public void setCreators(Map creators);
}
