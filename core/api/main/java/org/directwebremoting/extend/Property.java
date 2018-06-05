package org.directwebremoting.extend;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Member;

import org.directwebremoting.ConversionException;

/**
 * Describes some way to get or set a bit of data on an object.
 * <p>There are obvious Properties for a getter/setter pair
 * {@link PropertyDescriptorProperty}, for a field {@link FieldProperty} and
 * also for a constructor parameter {@link ConstructorProperty}. There is a
 * {@link PlainProperty} for when we have a collection of Properties of a bean,
 * but need to augment it in some way and {@link NestedProperty} which is used
 * when we're digging into nested generic type info.
 * It would be nice if {@link PropertyDescriptor}, and the various reflection
 * types like {@link Member} had a common supertype, but they don't. In a way
 * this is it.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public interface Property
{
    /**
     * Gets the name of this property
     * @return The property name
     */
    String getName();

    /**
     * What type does this property
     * @return The type of object that will be returned by {@link #getValue(Object)}
     */
    Class<?> getPropertyType();

    /**
     * Get the value of this property of the passed in java bean
     * @param bean The bean to introspect
     * @return The value assigned to this property of the passed in bean
     * @throws ConversionException If the reflection access fails
     */
    Object getValue(Object bean) throws ConversionException;

    /**
     * Set the value of this property of the passed in java bean
     * @param bean The bean to introspect
     * @param value The value assigned to this property of the passed in bean
     * @throws ConversionException If the reflection access fails
     */
    void setValue(Object bean, Object value) throws ConversionException;

    /**
     * Properties may have children where we have nested generic type info.
     * For example with {@code setFoo(List<Map<String, Class<?>> x)} we can
     * find type information about the nested bits of generic data.
     * <p>
     * If calling this method then you almost certainly want to check for an
     * overridden property as defined by the {@link ConverterManager} by calling
     * {@link ConverterManager#checkOverride(Property)}
     * @param index Generally the parameter offset.
     * @return A Property to describe the nested bit of data
     */
    Property createChild(int index) throws ConversionException;
}
