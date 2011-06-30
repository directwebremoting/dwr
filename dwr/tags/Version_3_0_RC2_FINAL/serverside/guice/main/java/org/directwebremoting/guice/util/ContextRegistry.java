/*
 * Copyright 2008 Tim Peierls
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
