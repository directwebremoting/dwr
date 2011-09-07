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
package org.directwebremoting.extend;

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
     * @return Are we in debug mode
     * @see CreatorManager#getCreatorNames()
     */
    boolean isDebug();

    /**
     * In init mode, add a new type of creator
     * @param id The name of the new creator type
     * @param className The class that we create
     */
    void addCreatorType(String id, String className);

    /**
     * Add a new creator
     * @param scriptName The name of the creator to Javascript
     * @param typename The class to use as a creator
     * @param params The extra parameters to allow the creator to configure itself
     * @throws InstantiationException If reflection based creation fails
     * @throws IllegalAccessException If reflection based creation fails
     * @throws IllegalArgumentException If we have a duplicate name
     */
    void addCreator(String scriptName, String typename, Map params) throws InstantiationException, IllegalAccessException, IllegalArgumentException;

    /**
     * Add a new creator
     * @param scriptName The name of the creator to Javascript
     * @param creator The creator to add
     * @throws IllegalArgumentException If we have a duplicate name
     */
    void addCreator(String scriptName, Creator creator) throws IllegalArgumentException;

    /**
     * Get a list of the javascript names of the allowed creators.
     * This method could be seen as a security risk because it could allow an
     * attacker to find out extra information about your system so it is only
     * available if debug is turned on.
     * @return Loop over all the known allowed classes
     * @throws SecurityException If we are not in debug mode
     */
    Collection getCreatorNames() throws SecurityException;

    /**
     * Find an <code>Creator</code> by name
     * @param scriptName The name of the creator to Javascript
     * @return The found Creator instance, or null if none was found.
     * @throws SecurityException If the Creator is not known
     */
    Creator getCreator(String scriptName) throws SecurityException;

    /**
     * Sets the creators for this creator manager.
     * @param creators the map of managed beans and their creator instances
     */
    void setCreators(Map creators);
}
