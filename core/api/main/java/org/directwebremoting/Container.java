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
     * Closes down all parts of DWR in a timely way, stops threads,
     * and performs tidy-up.
     */
    void destroy();

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
     */
    void initializeBean(Object object);
}
