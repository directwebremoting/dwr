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
package uk.ltd.getahead.dwr.convert;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import uk.ltd.getahead.dwr.Messages;
import uk.ltd.getahead.dwr.util.Logger;

/**
 * BeanConverter that works with Hibernate3 to get BeanInfo.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class HibernateBeanConverter extends BeanConverter
{
    /**
     * Simple ctor
     * @throws ClassNotFoundException If Hibernate can not be configured
     */
    public HibernateBeanConverter() throws ClassNotFoundException
    {
        try
        {
            hibernate = Class.forName(CLASS_HIBERNATE3);
            log.info("Found Hibernate3 class: " + hibernate.getName()); //$NON-NLS-1$
        }
        catch (Exception ex)
        {
            try
            {
                hibernate = Class.forName(CLASS_HIBERNATE2);
                log.info("Found Hibernate2 class: " + hibernate.getName()); //$NON-NLS-1$
            }
            catch (Exception ex2)
            {
                throw new ClassNotFoundException(Messages.getString("HibernateBeanConverter.MissingClass")); //$NON-NLS-1$
            }
        }

        try
        {
            getClass = hibernate.getMethod("getClass", new Class[] { Object.class }); //$NON-NLS-1$
        }
        catch (Exception ex)
        {
            throw new ClassNotFoundException(Messages.getString("HibernateBeanConverter.MissingGetClass")); //$NON-NLS-1$
        }

        try
        {
            isPropertyInitialized = hibernate.getMethod("isPropertyInitialized", new Class[] { Object.class, String.class }); //$NON-NLS-1$
        }
        catch (Exception ex)
        {
            log.info("Hibernate.isPropertyInitialized() is not available in Hibernate2 so initialization checks will not take place"); //$NON-NLS-1$
        }

        try
        {
            isInitialized = hibernate.getMethod("isInitialized", new Class[] { Object.class }); //$NON-NLS-1$
        }
        catch (Exception ex)
        {
            log.info("Hibernate.isInitialized() is not available - verify you have the Hibernate jar on your classpath"); //$NON-NLS-1$
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
        try
        {
            Class clazz = (Class) getClass.invoke(null, new Object[] { bean });
            BeanInfo info = Introspector.getBeanInfo(clazz);
            return info;
        }
        catch (IntrospectionException ex)
        {
            throw ex;
        }
        catch (Exception ex)
        {
            log.error("Logic Error", ex); //$NON-NLS-1$
            throw new IntrospectionException(ex.getMessage());
        }
    }

    /* (non-Javadoc)
     * @see uk.ltd.getahead.dwr.convert.BeanConverter#isAvailable(java.lang.Object, java.lang.String)
     */
    public boolean isAvailable(Object data, String property)
    {
        try
        {
            // We don't marshall un-initialized properties for Hibernate3
            if (isPropertyInitialized != null)
            {
                PropertyDescriptor[] props = Introspector.getBeanInfo(data.getClass()).getPropertyDescriptors();

                // Cache the method if possible, using the classname and
                // property name to allow for similar named methods (getName())
                String key = data.getClass().getName() + property;

                Method method = (Method) methods.get(key);
                if (method == null)
                {
                    for (int i = 0; i < props.length; i++)
                    {
                        if (props[i].getName().equalsIgnoreCase(property))
                        {
                            method = props[i].getReadMethod();
                        }
                    }
                    methods.put(key, method);
                }

                if (method == null)
                {
                    log.warn("Failed to find property: " + property); //$NON-NLS-1$
                    return false;
                }

                Boolean reply = (Boolean) isPropertyInitialized.invoke(null, new Object[] { data, property });
                Boolean reply2 = (Boolean) isInitialized.invoke(null, new Object[] { method.invoke(data, new Object[]{ }) });

                boolean inited = reply.booleanValue() && reply2.booleanValue();
                if (!inited)
                {
                    return false;
                }
            }

            return true;
        }
        catch (Exception ex)
        {
            log.error("Failed in checking Hibernate the availability of " + property, ex); //$NON-NLS-1$
            return false;
        }
    }

    /**
     * The cache of method lookups that we've already done
     */
    private static Map methods = new HashMap();

    /**
     * The Hibernate utility class under Hibernate2
     */
    private static final String CLASS_HIBERNATE2 = "net.sf.hibernate.Hibernate"; //$NON-NLS-1$

    /**
     * The Hibernate utility class under Hibernate3
     */
    private static final String CLASS_HIBERNATE3 = "org.hibernate.Hibernate"; //$NON-NLS-1$

    /**
     * The Hibernate utility class (either H2 or H3)
     */
    private Class hibernate;

    /**
     * The cached getClass method from Hibernate
     */
    private Method getClass;

    /**
     * The cached isPropertyInitialized from Hibernate
     */
    private Method isPropertyInitialized;

    /**
     * The cached isInitialized from Hibernate
     */
    private Method isInitialized;

    /**
     * The log stream
     */
    private static final Logger log = Logger.getLogger(HibernateBeanConverter.class);
}
