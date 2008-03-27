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

import org.directwebremoting.convert.BasicBeanConverter;
import org.directwebremoting.extend.MarshallException;
import org.directwebremoting.util.Logger;
import org.directwebremoting.util.Messages;

/**
 * BeanConverter that works with Hibernate3 to get BeanInfo.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class HibernateBeanConverter extends BasicBeanConverter
{
    /**
     * Simple ctor
     * @throws ClassNotFoundException If Hibernate can not be configured
     */
    public HibernateBeanConverter() throws ClassNotFoundException
    {
        if (!HibernateUtil.isHibernateAvailable())
        {
            throw new ClassNotFoundException(Messages.getString("HibernateBeanConverter.MissingClass"));
        }
    }

    /**
     * HibernateBeanConverter (and maybe others) may want to provide alternate
     * versions of bean.getClass()
     * @param type The class to find bean info from
     * @return BeanInfo for the given class
     * @throws MarshallException
     */
    protected BeanInfo getBeanInfo(Class type) throws MarshallException
    {
        try
        {
            // ERROR: HibernateUtil wants an object to do RTTI on
            Class clazz = HibernateUtil.getClass(type);
            return Introspector.getBeanInfo(clazz);
        }
        catch (IntrospectionException ex)
        {
            throw new MarshallException(type, ex);
        }
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.convert.BasicBeanConverter#getValueForProperty(java.lang.Object, java.beans.PropertyDescriptor)
     */
    protected Object getValueForProperty(Object data, PropertyDescriptor descriptor) throws MarshallException
    {
        try
        {
            // We don't marshall un-initialized properties for Hibernate3
            if (HibernateUtil.getHibernateMajorVersion() >= 3)
            {
                String propertyName = descriptor.getName();
                Method method = findGetter(data, propertyName);

                if (method == null)
                {
                    log.warn("Failed to find property: " + propertyName);
                    return null;
                }

                boolean reply = HibernateUtil.isPropertyInitialized(data, propertyName);
                if (!reply)
                {
                    return null;
                }

                Object retval = method.invoke(data, new Object[] {});

                reply = HibernateUtil.isInitialized(retval);
                if (!reply)
                {
                    return null;
                }
            }

            Method getter = descriptor.getReadMethod();
            return getter.invoke(data, new Object[0]);
        }
        catch (Exception ex)
        {
            throw new MarshallException(data.getClass(), ex);
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
    private Method findGetter(Object data, String property) throws IntrospectionException
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
    private static Map methods = new HashMap();

    /**
     * The log stream
     */
    private static final Logger log = Logger.getLogger(HibernateBeanConverter.class);
}
