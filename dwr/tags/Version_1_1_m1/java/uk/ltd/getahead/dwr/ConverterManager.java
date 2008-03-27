package uk.ltd.getahead.dwr;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * A class to manage the converter types and the instansiated class name matches.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public interface ConverterManager
{
    /**
     * Add a new converter type
     * @param id The name of the converter type
     * @param clazz The class to do the conversion
     */
    void addConverterType(String id, Class clazz);

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
     * @return The coerced object or null if the object could not be coerced
     * @throws ConversionException If the conversion failed for some reason
     */
    Object convertInbound(Class paramType, InboundVariable iv, InboundContext inctx) throws ConversionException;

    /**
     * Convert an object into a Javavscript representation of the same.
     * This method is for use by converters wishing to recurse into some object.
     * @param object The object to convert
     * @param converted The list of converted objects so far
     * @return A Javascript string version of the object
     * @throws ConversionException If the conversion failed for some reason
     */
    OutboundVariable convertOutbound(Object object, OutboundContext converted) throws ConversionException;

    /**
     * We don't know enough from a method signature like setUsers(Set s) to be
     * able to cast the inbound data to a set of Users. This method enables us
     * to specify this extra information.
     * @param method The method to annotate
     * @param paramNo The number of the parameter to edit (counts from 0)
     * @param index The index of the item between &lt; and &gt;.
     * @param type The type of the specified parameter. 
     */
    void setExtraTypeInfo(Method method, int paramNo, int index, Class type);

    /**
     * The extra type information that we have learnt about a method parameter.
     * This method will return null if there is nothing extra to know
     * @param method The method to annotate
     * @param paramNo The number of the parameter to edit (counts from 0)
     * @param index The index of the item between &lt; and &gt;.
     * @return A list of types to fill out a generic type
     */
    Class getExtraTypeInfo(Method method, int paramNo, int index);

    /**
     * Sets the converters for this converter manager.
     * @param converters the map of match pattern and their converter instances
     */
    void setConverters(Map converters);
}
