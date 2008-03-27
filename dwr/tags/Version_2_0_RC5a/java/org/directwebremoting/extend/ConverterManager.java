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
 * A class to manage the converter types and the instantiated class name matches.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public interface ConverterManager
{
    /**
     * Add a new converter type
     * @param id The name of the converter type
     * @param className The class to do the conversion
     */
    void addConverterType(String id, String className);

    /**
     * Add a new converter
     * @param match The class name(s) to match
     * @param type The name of the converter type
     * @param params The extra parameters to allow the creator to configure itself
     * @throws InstantiationException If reflection based creation fails
     * @throws IllegalAccessException If reflection based creation fails
     * @throws IllegalArgumentException If we have a duplicate name
     */
    void addConverter(String match, String type, Map params) throws IllegalArgumentException, InstantiationException, IllegalAccessException;

    /**
     * Add a new converter
     * @param match The class name(s) to match
     * @param converter The converter to add
     * @throws IllegalArgumentException If we have a duplicate name
     */
    void addConverter(String match, Converter converter) throws IllegalArgumentException;

    /**
     * In order to be able to create stub remote objects we need to know what
     * they are so you can get a collection of all match strings.
     * @return A Collection of all the converter match strings
     * @see #getConverterByMatchString(String)
     */
    Collection getConverterMatchStrings();

    /**
     * In order to be able to create stub remote objects we need to know what
     * they are so you can lookup match strings and retrieve the converter.
     * @param match The match string to lookup
     * @return The matching converter
     * @see #getConverterMatchStrings()
     */
    Converter getConverterByMatchString(String match);

    /**
     * Check if we can coerce the given type
     * @param paramType The type to check
     * @return true iff <code>paramType</code> is coercable
     */
    boolean isConvertable(Class paramType);

    /**
     * Convert an object from being a string into an object of some type.
     * Designed for use with converters that have a working map passed to them
     * @param paramType The type that you want the object to be
     * @param iv The string version of the object
     * @param inctx The map of data that we are working on
     * @param incc The context of this type conversion
     * @return The coerced object or null if the object could not be coerced
     * @throws MarshallException If the conversion failed for some reason
     */
    Object convertInbound(Class paramType, InboundVariable iv, InboundContext inctx, TypeHintContext incc) throws MarshallException;

    /**
     * Convert an object into a Javavscript representation of the same.
     * This method is for use by converters wishing to recurse into some object.
     * @param object The object to convert
     * @param converted The list of converted objects so far
     * @return A Javascript string version of the object
     * @throws MarshallException If the conversion failed for some reason
     */
    OutboundVariable convertOutbound(Object object, OutboundContext converted) throws MarshallException;

    /**
     * We don't know enough from a method signature like setUsers(Set s) to be
     * able to cast the inbound data to a set of Users. This method enables us
     * to specify this extra information.
     * @param thc The context to find any extra type information from
     * @param type The type of the specified parameter.
     */
    void setExtraTypeInfo(TypeHintContext thc, Class type);

    /**
     * The extra type information that we have learnt about a method parameter.
     * This method will return null if there is nothing extra to know
     * @param thc The context to find any extra type information from
     * @return A type to use to fill out the generic type
     */
    Class getExtraTypeInfo(TypeHintContext thc);

    /**
     * Sets the converters for this converter manager.
     * @param converters the map of match pattern and their converter instances
     */
    void setConverters(Map converters);
}
