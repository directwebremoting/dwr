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
package org.directwebremoting;

import java.util.Collection;

/**
 * A very basic IoC container.
 * See ContainerUtil for information on how to setup a {@link Container}
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public interface Container
{
    /**
     * Get an instance of a bean of a given name (usually name=class name).
     * @param id The type to get an instance of
     * @return The object of the given type, or null if the object does not exist
     */
    Object getBean(String id);

    /**
     * Get an instance of a bean of a given type
     * @param type The type to get an instance of
     * @return The object of the given type, or null if the object does not exist
     */
    <T> T getBean(Class<T> type);

    /**
     * Get a list of all the available beans.
     * Implementation of this method is optional so it is valid for this method
     * to return an empty collection, but to return Objects when queried
     * directly using {@link #getBean(String)}. This method should only be used
     * for debugging purposes.
     * @return A collection containing all the available bean names.
     */
    Collection<String> getBeanNames();

    /**
     * This should be called <em>only</em> by the controlling servlet (e.g.
     * {@link org.directwebremoting.servlet.DwrServlet}) to request that the
     * container wrap things up. It should be called when the context is being
     * destroyed, however probably not on due to a call to
     * {@link javax.servlet.http.HttpServlet#destroy()} because that is only
     * called when all connections are closed. Instead it should be called from
     * {@link javax.servlet.ServletContextListener#contextDestroyed}.
     * In turn this should call contextDestroyed() on any contained objects that
     * themselves implement {@link javax.servlet.ServletContextListener}
     */
    void destroy();
}
