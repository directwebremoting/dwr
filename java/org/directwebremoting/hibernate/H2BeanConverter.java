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
package org.directwebremoting.hibernate;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import net.sf.hibernate.Hibernate;

import org.directwebremoting.convert.BeanConverter;
import org.directwebremoting.extend.Converter;
import org.directwebremoting.extend.MarshallException;

/**
 * BeanConverter that works with Hibernate to get BeanInfo.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class H2BeanConverter extends BeanConverter implements Converter
{
    /* (non-Javadoc)
     * @see org.directwebremoting.extend.NamedConverter#getPropertyMapFromObject(java.lang.Object, boolean, boolean)
     */
    public Map getPropertyMapFromObject(Object example, boolean readRequired, boolean writeRequired) throws MarshallException
    {
        Class clazz = Hibernate.getClass(example);
        try
        {
            BeanInfo info = Introspector.getBeanInfo(clazz);
            PropertyDescriptor[] descriptors = info.getPropertyDescriptors();

            Map properties = new HashMap();
            for (int i = 0; i < descriptors.length; i++)
            {
                PropertyDescriptor descriptor = descriptors[i];
                String name = descriptor.getName();

                // We don't marshall getClass()
                if (name.equals("class"))
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
            throw new MarshallException(clazz, ex);
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

        Method method = (Method) methods.get(key);
        if (method == null)
        {
            PropertyDescriptor[] props = Introspector.getBeanInfo(data.getClass()).getPropertyDescriptors();
            for (int i = 0; i < props.length; i++)
            {
                if (props[i].getName().equalsIgnoreCase(property))
                {
                    method = props[i].getReadMethod();
                }
            }

            methods.put(key, method);
        }

        return method;
    }

    /**
     * The cache of method lookups that we've already done
     */
    protected final Map methods = new HashMap();
}
