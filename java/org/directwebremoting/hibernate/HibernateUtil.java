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

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.directwebremoting.util.LocalUtil;
import org.directwebremoting.util.Logger;

/**
 * We work with Hibernate 2 and 3, and the package names have changed, so this
 * class provides a wrapper that dynamically does the right thing.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class HibernateUtil
{
    /**
     * Were the Hibernate classes found on the classpath?
     * @return true if Hibernate is available
     */
    public static boolean isHibernateAvailable()
    {
        return hibernateMajorVersion != -1;
    }

    /**
     * The hibernate major version number, or -1 for not supported.
     * Currently only 2 and 3 are supported.
     * @return The hibernate mojor version number
     */
    public static int getHibernateMajorVersion()
    {
        return hibernateMajorVersion;
    }

    /**
     * Get the true, underlying class of a proxied persistent class.
     * This operation will initialize a proxy by side-effect.
     * @param proxy a persistable object or proxy
     * @return the true class of the instance
     * @throws IntrospectionException
     */
    public static Class getClass(Object proxy) throws IntrospectionException
    {
        try
        {
            Class clazz = (Class) getClass.invoke(null, new Object[] { proxy });
            return clazz;
        }
        catch (Exception ex)
        {
            log.error("Logic Error", ex);
            throw new IntrospectionException(ex.getMessage());
        }
    }

    /**
     * Is the given bean initialized?
     * @param proxy The bean to check the initialized status of
     * @return true if the bean is initialized
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public static boolean isInitialized(Object proxy) throws IllegalAccessException, InvocationTargetException
    {
        Boolean reply = (Boolean) isInitialized.invoke(null, new Object[] { proxy });
        return reply.booleanValue();
    }

    /**
     * Is the given property of a bean initialized?
     * @param proxy The bean to check the initialized status of
     * @param property The property of the given bean
     * @return true if the bean property is initialized
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public static boolean isPropertyInitialized(Object proxy, String property) throws IllegalAccessException, InvocationTargetException
    {
        Boolean reply = (Boolean) isPropertyInitialized.invoke(null, new Object[] { proxy, property });
        return reply.booleanValue();
    }

    /**
     * 
     */
    public static void beginTransaction()
    {
    }

    /**
     * 
     */
    public static void commitTransaction()
    {
    }

    /**
     * The Hibernate utility class under Hibernate2
     */
    private static final String CLASS_HIBERNATE2 = "net.sf.hibernate.Hibernate";

    /**
     * The Hibernate utility class under Hibernate3
     */
    private static final String CLASS_HIBERNATE3 = "org.hibernate.Hibernate";

    /**
     * The cached getClass method from Hibernate
     */
    private static Method getClass;

    /**
     * The cached isPropertyInitialized from Hibernate
     */
    private static Method isPropertyInitialized;

    /**
     * The cached isInitialized from Hibernate
     */
    private static Method isInitialized;

    /**
     * The hibernate major version number, or -1 for not supported.
     * Currently only 2 and 3 are supported.
     */
    private static int hibernateMajorVersion = -1;

    /**
     * The log stream
     */
    private static final Logger log = Logger.getLogger(HibernateUtil.class);

    static
    {
        Class hibernate = null;

        try
        {
            hibernate = LocalUtil.classForName(CLASS_HIBERNATE3);
            log.info("Found Hibernate3 class: " + hibernate.getName());
            hibernateMajorVersion = 3;
        }
        catch (Exception ex)
        {
            try
            {
                hibernate = LocalUtil.classForName(CLASS_HIBERNATE2);
                log.info("Found Hibernate2 class: " + hibernate.getName());
                hibernateMajorVersion = 2;
            }
            catch (Exception ex2)
            {
            }
        }

        if (hibernate != null)
        {
            try
            {
                getClass = hibernate.getMethod("getClass", new Class[] { Object.class });
            }
            catch (Exception ex)
            {
                log.error("Hibernate classes are available but no getClass method was found. Disabling Hibernate.", ex);
                hibernateMajorVersion = -1;
            }
    
            try
            {
                isPropertyInitialized = hibernate.getMethod("isPropertyInitialized", new Class[] { Object.class, String.class });
            }
            catch (Exception ex)
            {
                log.info("Hibernate.isPropertyInitialized() is not available in Hibernate2 so initialization checks will not take place");
                hibernateMajorVersion = -1;
            }
    
            try
            {
                isInitialized = hibernate.getMethod("isInitialized", new Class[] { Object.class });
            }
            catch (Exception ex)
            {
                log.info("Hibernate.isInitialized() is not available - verify you have the Hibernate jar on your classpath");
                hibernateMajorVersion = -1;
            }
        }
    }
}
