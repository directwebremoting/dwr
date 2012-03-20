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
     * Get the contained instance of a bean/setting of a given name.
     * @param id The type to get an instance of
     * @return The object of the given type, or null if the object does not exist
     */
    Object getBean(String id);

    /**
     * Get the contained instance of a bean of a given type
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
     * This should be called <em>only</em> by
     * {@link org.directwebremoting.servlet.DwrListener}. It requests all
     * reverse ajax threads to stop.
     * <p>
     * {@link javax.servlet.http.HttpServlet#destroy()} is called only when all
     * connections are closed. If a DwrListener is configured then we can close
     * down the connections in a timely way. All other tidy-up is done by
     * {@link #servletDestroyed()} which will work even when a DwrListener has
     * not been configured.
     * @see #servletDestroyed()
     */
    void contextDestroyed();

    /**
     * Should be called only by {@link org.directwebremoting.servlet.DwrServlet}
     * (or other servlet implementations). It requests all other threads to
     * stop, and any tidy-up that can be done after the context has been fully
     * destroyed.
     * @see #contextDestroyed()
     */
    void servletDestroyed();

    /**
     * Sometimes we need to create a bean as a one-off object and have it
     * injected with settings by the container.
     * This does not make the object part of the container.
     * @param type The type to get an instance of
     */
    <T> T newInstance(Class<T> type) throws InstantiationException, IllegalAccessException;

    /**
     * Sometimes we need to take a bean not created by the container, and inject
     * it with the data that it would contain if it was created by the
     * container.
     * This does not make the object part of the container.
     * @param object The object to inject.
     * @deprecated
     */
    @Deprecated
    void initializeBean(Object object);
}
