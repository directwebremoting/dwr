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

/**
 * Additions to Converter that allow objects to have names that
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public interface NamedConverter extends Converter
{
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

    /**
     * Fetch an array of all the allowed property names.
     * @param mappedType The class in the inheritance hierachy to use as a base
     * @return A list of allowed propery names
     */
    String[] getAllowedPropertyNames(Class mappedType);

    /**
     * Find the type for a given property name
     * @param mappedType The class in the inheritance hierachy to use as a base
     * @param propertyName The property name to lookup
     * @return The class for the property type
     */
    Class getPropertyType(Class mappedType, String propertyName);
}
