package org.directwebremoting.hibernate;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import net.sf.hibernate.Hibernate;

import org.directwebremoting.ConversionException;
import org.directwebremoting.convert.BeanConverter;
import org.directwebremoting.extend.Property;

/**
 * BeanConverter that works with Hibernate to get BeanInfo.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class H2BeanConverter extends BeanConverter
{
    /* (non-Javadoc)
     * @see org.directwebremoting.extend.NamedConverter#getPropertyMapFromObject(java.lang.Object, boolean, boolean)
     */
    @Override
    public Map<String, Property> getPropertyMapFromObject(Object example, boolean readRequired, boolean writeRequired) throws ConversionException
    {
        Class<?> clazz = Hibernate.getClass(example);
        try
        {
            BeanInfo info = Introspector.getBeanInfo(clazz);

            Map<String, Property> properties = new HashMap<String, Property>();
            for (PropertyDescriptor descriptor : info.getPropertyDescriptors())
            {
                String name = descriptor.getName();

                // We don't marshall getClass()
                if ("class".equals(name))
                {
                    continue;
                }

                // Access rules mean we might not want to do this one
                if (!isAllowedByIncludeExcludeRules(name))
                {
                    continue;
                }

                if (readRequired && descriptor.getReadMethod() == null)
                {
                    continue;
                }

                if (writeRequired && descriptor.getWriteMethod() == null)
                {
                    continue;
                }

                properties.put(name, new H2PropertyDescriptorProperty(descriptor));
            }

            return properties;
        }
        catch (Exception ex)
        {
            throw new ConversionException(clazz, ex);
        }
    }

    /**
     * Cache the method if possible, using the classname and property name to
     * allow for similar named methods.
     * @param data The bean to introspect
     * @param property The property to get the accessor for
     * @return The getter method
     * @throws IntrospectionException
     */
    protected Method findGetter(Object data, String property) throws IntrospectionException
    {
        String key = data.getClass().getName() + ":" + property;

        Method method = methods.get(key);
        if (method == null)
        {
            PropertyDescriptor[] props = Introspector.getBeanInfo(data.getClass()).getPropertyDescriptors();
            for (PropertyDescriptor prop : props)
            {
                if (prop.getName().equalsIgnoreCase(property))
                {
                    method = prop.getReadMethod();
                }
            }

            methods.put(key, method);
        }

        return method;
    }

    /**
     * The cache of method lookups that we've already done
     */
    protected final Map<String, Method> methods = new HashMap<String, Method>();
}
