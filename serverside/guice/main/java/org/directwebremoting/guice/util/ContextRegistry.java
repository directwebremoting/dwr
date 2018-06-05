package org.directwebremoting.guice.util;

import com.google.inject.Key;

/**
 * Manages instances for a context. This class is only useful for
 * defining new implementations of {@link AbstractContextScope}.
 * @author Tim Peierls [tim at peierls dot net]
 */
public interface ContextRegistry<C, R>
{
    /**
     * Returns the registry object associated with the given context.
     */
    R registryFor(C context);


    /**
     * Looks up an InstanceProvider for a key (either directly or using
     * the precalculated key.toString() value) in a registry object,
     * returning null if not found.
     */
    <T> InstanceProvider<T> get(R registry, Key<T> key, String keyString);


    /**
     * Looks up an InstanceProvider for a key (either directly or using
     * the precalculated key.toString() value) in a registry object,
     * returning null if not found, otherwise returning the existing value.
     */
    <T> InstanceProvider<T> putIfAbsent(R registry, Key<T> key, String keyString,
                                        InstanceProvider<T> creator);


    /**
     * Removes the registry entry for the given key (either directly or using
     * the precalculated key.toString() value) from a registry object if
     * the registered value is identical to {@code creator}.
     * @return whether the value was removed
     */
    <T> boolean remove(R registry, Key<T> key, String keyString,
                       InstanceProvider<T> creator);
}
