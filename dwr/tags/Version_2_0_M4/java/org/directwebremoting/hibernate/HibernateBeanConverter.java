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
     * @param bean The class to find bean info from
     * @return BeanInfo for the given class
     * @throws IntrospectionException
     */
    protected BeanInfo getBeanInfo(Object bean) throws IntrospectionException
    {
        Class clazz = HibernateUtil.getClass(bean);
        return Introspector.getBeanInfo(clazz);
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.convert.BasicBeanConverter#isAvailable(java.lang.Object, java.lang.String)
     */
    public boolean isAvailable(Object data, String property)
    {
        try
        {
            // We don't marshall un-initialized properties for Hibernate3
            if (HibernateUtil.getHibernateMajorVersion() >= 3)
            {
                Method method = findGetter(data, property);

                if (method == null)
                {
                    log.warn("Failed to find property: " + property);
                    return false;
                }

                boolean reply = HibernateUtil.isPropertyInitialized(data, property);
                if (!reply)
                {
                    return false;
                }

                Object retval = method.invoke(data, new Object[] {});

                reply = HibernateUtil.isInitialized(retval);
                if (!reply)
                {
                    return false;
                }
            }

            return true;
        }
        catch (Exception ex)
        {
            log.error("Failed in checking Hibernate the availability of " + property, ex);
            return false;
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
