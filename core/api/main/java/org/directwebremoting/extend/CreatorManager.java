package org.directwebremoting.extend;

import java.util.Collection;
import java.util.Map;

/**
 * A class to manage the types of creators and the instantiated creators.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public interface CreatorManager
{
    /**
     * Debug mode allows access to the list of creator names
     * @return Are we in debug mode
     */
    boolean isDebug();

    /**
     * In init mode, add a new type of creator
     * @param typeName The name of the new creator type
     * @param className The class that we create
     */
    void addCreatorType(String typeName, String className);

    /**
     * Add a new creator
     * @param typeName The class to use as a creator
     * @param params The extra parameters to allow the creator to configure itself
     * @throws InstantiationException If reflection based creation fails
     * @throws IllegalAccessException If reflection based creation fails
     * @throws IllegalArgumentException If we have a duplicate name
     */
    void addCreator(String typeName, Map<String, String> params) throws InstantiationException, IllegalAccessException, IllegalArgumentException;

    /**
     * Add a new creator.
     * read the value from the creator
     * @param creator The creator to add
     * @throws IllegalArgumentException If we have a duplicate name
     */
    void addCreator(Creator creator) throws IllegalArgumentException;

    /**
     * Get a list of the javascript names of the allowed creators.
     * This method could be seen as a security risk because it could allow an
     * attacker to find out extra information about your system so it is only
     * available if debug is turned on.
     * @param includeHidden Should we include hidden classes in the list?
     * @return Loop over all the known allowed classes
     * @throws SecurityException If we are not in debug mode
     */
    Collection<String> getCreatorNames(boolean includeHidden) throws SecurityException;

    /**
     * Find an <code>Creator</code> by name
     * @param scriptName The name of the creator to Javascript
     * @param includeHidden Should we include hidden classes?
     * @return The found Creator instance, or null if none was found.
     * @throws SecurityException If the Creator is not known
     */
    Creator getCreator(String scriptName, boolean includeHidden) throws SecurityException;

    // We might put these 2 back in if we get backwards compatibility complaints
    // but their existence can caused bugs, so we're going to leave them out
    // for as long as possible. Please complain to the DWR users mailing list
    // if you want to see them back in.
    /*
     * Equivalent to (and deprecated by) {@link #getCreatorNames(boolean)},
     * with includeHidden set to <code>false</code>
     * @return Loop over all the known allowed classes
     * @throws SecurityException If we are not in debug mode
     * @deprecated Use {@link #getCreatorNames(boolean)}
     */
    //@Deprecated
    //Collection<String> getCreatorNames() throws SecurityException;

    /*
     * Equivalent to (and deprecated by) {@link #getCreator(String, boolean)},
     * with includeHidden set to <code>false</code>
     * @param scriptName The name of the creator to Javascript
     * @return The found Creator instance, or null if none was found.
     * @throws SecurityException If the Creator is not known
     * @deprecated Use {@link #getCreator(String, boolean)}
     */
    //@Deprecated
    //Creator getCreator(String scriptName) throws SecurityException;

    /**
     * Sets the creators for this creator manager.
     * @param creators the map of managed beans and their creator instances
     */
    void setCreators(Map<String, Creator> creators);
}
