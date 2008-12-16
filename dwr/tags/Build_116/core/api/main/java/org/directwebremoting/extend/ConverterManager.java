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

import org.directwebremoting.ConversionException;
import org.directwebremoting.io.RawData;

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
    void addConverter(String match, String type, Map<String, String> params) throws IllegalArgumentException, InstantiationException, IllegalAccessException;

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
    Collection<String> getConverterMatchStrings();

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
     * @return true iff <code>paramType</code> is convertible
     */
    boolean isConvertable(Class<?> paramType);

    /**
     * Sometimes data from clients contains type information which should take
     * higher priority than the type guessing that we do as part of the method
     * matching. This method extracts the client passed type information.
     * @param data The string version of the object from the client
     * @return Any type information specified by the client
     */
    Class<?> getClientDeclaredType(InboundVariable data);

    /**
     * Convert an object from being a string into an object of some type.
     * Designed for use with converters that have a working map passed to them
     * @param paramType The type that you want the object to be
     * @param data The string version of the object
     * @return The convertible object
     * @throws ConversionException If the conversion failed for some reason
     */
    <T> T convertInbound(Class<T> paramType, InboundVariable data, Property thc) throws ConversionException;

    /**
     * RawData is something of a special case for conversion - it's designed to
     * be converted outside of the normal automatic conversion process when the
     * type can't be known until later. This method helps us with those cases
     * without exposing too much of what {@link RawData} holds.
     * @param <T>
     * @param paramType The type we wish to convert to
     * @param rawData The RawData object holding data to be converted
     * @return The convertible object
     * @throws ConversionException If the conversion failed for some reason
     */
    <T> T convertInbound(Class<T> paramType, RawData rawData) throws ConversionException;

    /**
     * Convert an object into a Javascript representation of the same.
     * This method is for use by converters wishing to recurse into some object.
     * @param data The object to convert
     * @param converted The list of converted objects so far
     * @return A Javascript string version of the object
     * @throws ConversionException If the conversion failed for some reason
     */
    OutboundVariable convertOutbound(Object data, OutboundContext converted) throws ConversionException;

    /**
     * We don't know enough from a method signature like setUsers(Set s) to be
     * able to cast the inbound data to a set of Users. This method enables us
     * to specify this extra information.
     * @param original The original property that might need replacing
     * @param replacement A replacement property that returns the correct type
     */
    void setOverrideProperty(Property original, Property replacement);

    /**
     * All {@link Property}s can create children to represent sub-types, but the
     * ConverterManager might want to override the options that they provide
     * using types configured by a signatures element.
     * @param property The property to check for overriding
     * @return The original property unless an override was provided
     */
    Property checkOverride(Property property);

    /**
     * Sets the converters for this converter manager.
     * @param converters the map of match pattern and their converter instances
     */
    void setConverters(Map<String, Converter> converters);
}
