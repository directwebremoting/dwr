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

import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;
import org.directwebremoting.convert.BeanConverter;
import org.directwebremoting.convert.PlainProperty;
import org.directwebremoting.extend.Converter;
import org.directwebremoting.extend.MarshallException;
import org.directwebremoting.extend.Property;
import org.hibernate.Hibernate;
import org.hibernate.engine.SessionImplementor;
import org.hibernate.proxy.HibernateProxy;
import org.hibernate.proxy.LazyInitializer;

/**
 * BeanConverter that works with Hibernate to get BeanInfo.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class H3BeanConverter extends BeanConverter implements Converter
{
    /* (non-Javadoc)
     * @see org.directwebremoting.convert.BeanConverter#getPropertyMapFromObject(java.lang.Object, boolean, boolean)
     */
    @Override
    public Map<String, Property> getPropertyMapFromObject(Object example, boolean readRequired, boolean writeRequired) throws MarshallException
    {
        Class<?> clazz = getClass(example);

        try
        {
            BeanInfo info = Introspector.getBeanInfo(clazz);
            PropertyDescriptor[] descriptors = info.getPropertyDescriptors();

            Map<String, Property> properties = new HashMap<String, Property>();
            for (PropertyDescriptor descriptor : descriptors)
            {
                String name = descriptor.getName();

                // We don't marshall getClass()
                if ("class".equals(name))
                {
                    continue;
                }

                // And this is something added by hibernate
                if ("hibernateLazyInitializer".equals(name))
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

                if (!assumeSession)
                {
                    // We don't marshall un-initialized properties for
                    // Hibernate3
                    Method method = findGetter(example, name);

                    if (method == null)
                    {
                        log.warn("Failed to find property: " + name);

                        properties.put(name, new PlainProperty(name, null));
                        continue;
                    }

                    if (!Hibernate.isPropertyInitialized(example, name))
                    {
                        properties.put(name, new PlainProperty(name, null));
                        continue;
                    }

                    // This might be a lazy-collection so we need to double check
                    Object retval = method.invoke(example);
                    if (!Hibernate.isInitialized(retval))
                    {
                        properties.put(name, new PlainProperty(name, null));
                        continue;
                    }
                }

                properties.put(name, new H3PropertyDescriptorProperty(descriptor));
            }

            return properties;
        }
        catch (Exception ex)
        {
            throw new MarshallException(clazz, ex);
        }
    }

    /**
     * Hibernate makes {@link Class#getClass()} diffficult ...
     * @param example The class that we want to call {@link Class#getClass()} on
     * @return The type of the given object
     */
    public Class<?> getClass(Object example)
    {
        if (example instanceof HibernateProxy)
        {
            HibernateProxy proxy = (HibernateProxy) example;
            LazyInitializer initializer = proxy.getHibernateLazyInitializer();
            SessionImplementor implementor = initializer.getSession();

            if (initializer.isUninitialized())
            {
                try
                {
                    // getImplementation is going to want to talk to a session
                    if (implementor.isClosed())
                    {
                        // Give up and return example.getClass();
                        return example.getClass();
                    }
                }
                catch (NoSuchMethodError ex)
                {
                    // We must be using Hibernate 3.0/3.1 which doesn't have
                    // this method
                }
            }

            return initializer.getImplementation().getClass();
        }
        else
        {
            return example.getClass();
        }
    }

    /**
     * Cache the method if possible, using the classname and property name to
     * allow for similar named methods.
     * @param data The bean to introspect
     * @param property The property to get the accessor for
     * @return The getter method
     * @throws IntrospectionException If Introspector.getBeanInfo() fails
     */
    protected Method findGetter(Object data, String property) throws IntrospectionException
    {
        Class<?> clazz = getClass(data);
        // String key = clazz.getName() + ":" + property;

        Method method = null; // methods.get(key);
        // if (method == null)
        // {
            PropertyDescriptor[] props = Introspector.getBeanInfo(clazz).getPropertyDescriptors();
            for (PropertyDescriptor prop : props)
            {
                if (prop.getName().equalsIgnoreCase(property))
                {
                    method = prop.getReadMethod();
                }
            }

            // methods.put(key, method);
        // }

        return method;
    }

    /**
     * @param assumeSession the assumeSession to set
     */
    public void setAssumeSession(boolean assumeSession)
    {
        this.assumeSession = assumeSession;
    }

    /**
     * Do we assume there is an open session and read properties?
     */
    protected boolean assumeSession = false;

    /**
     * The cache of method lookups that we've already done
     */
    //protected final Map<String, Method> methods = new HashMap<String, Method>();

    /**
     * The log stream
     */
    private static final Log log = LogFactory.getLog(H3BeanConverter.class);
}
