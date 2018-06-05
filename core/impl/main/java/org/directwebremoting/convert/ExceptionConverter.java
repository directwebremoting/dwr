package org.directwebremoting.convert;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.util.Map;

import org.directwebremoting.ConversionException;
import org.directwebremoting.extend.PlainProperty;
import org.directwebremoting.extend.Property;
import org.directwebremoting.extend.PropertyDescriptorProperty;

/**
 * A special case of BeanConverter that doesn't convert StackTraces
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class ExceptionConverter extends BeanConverter
{
    /* (non-Javadoc)
     * @see org.directwebremoting.convert.BasicBeanConverter#getPropertyDescriptors(java.lang.Class, boolean, boolean)
     */
    @Override
    public Map<String, Property> getPropertyMapFromClass(Class<?> type, boolean readRequired, boolean writeRequired) throws ConversionException
    {
        Map<String, Property> descriptors = super.getPropertyMapFromClass(type, readRequired, writeRequired);
        descriptors.put("javaClassName", new PlainProperty("javaClassName", type.getName()));

        // Make sure Throwable's standard properties are added
        // (fix for Bean Introspector peculiarities)
        try
        {
            fixMissingThrowableProperty(descriptors, "message", "getMessage");
            fixMissingThrowableProperty(descriptors, "cause", "getCause");
        }
        catch (IntrospectionException ex)
        {
            throw new ConversionException(type, ex);
        }

        return descriptors;
    }

    /**
     * Make sure Throwable's standard properties are added
     * (fix for Bean Introspector peculiarities)
     * @param descriptors The Map of the known descriptors
     * @param name The name of the property to add
     * @param readMethodName A method name to use when getting said property
     * @throws IntrospectionException If we fail to build a PropertyDescriptor
     */
    protected void fixMissingThrowableProperty(Map<String, Property> descriptors, String name, String readMethodName) throws IntrospectionException
    {
        if (!descriptors.containsKey(name) && isAllowedByIncludeExcludeRules(name))
        {
            PropertyDescriptor descriptor = new PropertyDescriptor(name, Throwable.class, readMethodName, null);
            descriptors.put(name, new PropertyDescriptorProperty(descriptor));
        }
    }
}
