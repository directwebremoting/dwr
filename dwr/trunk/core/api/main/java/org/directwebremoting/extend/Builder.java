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

import javax.servlet.ServletContext;

/**
 * Class to enable us to build 'singleton' interface implementations.
 * It is assumed that there is one 'singleton' per {@link ServletContext}.
 * Both this class and implementations of it are generally for use by DWR
 * developers. If you are a DWR user then unless you're doing something very
 * deep, you're probably digging too far.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public interface Builder<T>
{
    /**
     * Get the object that is associated with this thread, assuming that there
     * is no confusion over the current {@link ServletContext}.
     * If DWR is configured more than once in this {@link ServletContext} then
     * it is probably best to use {@link #get(ServletContext)} to ensure that
     * the correct object is found.
     * @return The object associated with this thread, or null if there is
     * confusion over which {@link ServletContext} to access.
     */
    T get();

    /**
     * Get the object that is associated with this thread, whilst specifying
     * the current {@link ServletContext}.
     * @param context The web application environment
     * @return The object that is associated with this web application
     */
    T get(ServletContext context);
}
