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

import java.util.Map;

/**
 * Additions to Converter that allow objects to have names that
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public interface NamedConverter extends Converter
{
    /**
     * Get a map of property names to implementations of {@link Property}.
     * <p>HibernateBeanConverter (and maybe others) may want to provide
     * alternate versions of bean.getClass(), and we may wish to fake or hide
     * properties in some cases.
     * <p>This implementation is preferred above the alternate:
     * {@link #getPropertyMapFromClass(Class, boolean, boolean)} because it
     * potentially retains important extra type information.
     * @param example The object to find bean info from
     * @param readRequired The properties returned must be readable
     * @param writeRequired The properties returned must be writeable
     * @return An array of PropertyDescriptors describing the beans properties
     * @see #getPropertyMapFromClass(Class, boolean, boolean)
     * @throws MarshallException If the introspection fails
     */
    Map getPropertyMapFromObject(Object example, boolean readRequired, boolean writeRequired) throws MarshallException;

    /**
     * Get a map of property names to implementations of {@link Property}.
     * <p>HibernateBeanConverter (and maybe others) may want to provide
     * alternate versions of bean.getClass(), and we may wish to fake or hide
     * properties in some cases.
     * <p>If you have a real object to investigate then it is probably better
     * to call {@link #getPropertyMapFromObject(Object, boolean, boolean)}
     * because that version can take into accound extra runtime type info.
     * @param type The class to find bean info from
     * @param readRequired The properties returned must be readable
     * @param writeRequired The properties returned must be writeable
     * @return An array of PropertyDescriptors describing the beans properties
     * @see #getPropertyMapFromObject(Object, boolean, boolean)
     * @throws MarshallException If the introspection fails
     */
    Map getPropertyMapFromClass(Class type, boolean readRequired, boolean writeRequired) throws MarshallException;

    /**
     * @return Returns the instanceType.
     */
    Class getInstanceType();

    /**
     * @param instanceType The instanceType to set.
     */
    void setInstanceType(Class instanceType);

    /**
     * Accessor for the javascript class name for the converted objects.
     * @return The Javascript name
     */
    String getJavascript();

    /**
     * Accessor for the javascript class name for the converted objects.
     * @param javascript The Javascript name
     */
    void setJavascript(String javascript);
}
