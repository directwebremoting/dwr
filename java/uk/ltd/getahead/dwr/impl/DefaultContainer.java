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
package uk.ltd.getahead.dwr.impl;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import uk.ltd.getahead.dwr.Container;
import uk.ltd.getahead.dwr.util.Logger;

/**
 * DefaultContainer is like a mini-IoC container for DWR.
 * At least it is an IoC container by interface (check: no params that have
 * anything to do with DWR), but it is hard coded specifically for DWR. If we
 * want to make more of it we can later, but this is certainly not going to
 * become a full blown IoC container.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class DefaultContainer implements Container
{
    /**
     * Set the class that should be used to implement the given interface
     * @param askFor The interface to implement
     * @param value The new implementation
     * @throws IllegalAccessException If the specified beans could not be accessed
     * @throws InstantiationException If the specified beans could not be created 
     */
    public void addParameter(Object askFor, Object value) throws InstantiationException, IllegalAccessException
    {
        // Maybe the value is a classname that needs instansiating
        if (value instanceof String)
        {
            try
            {
                Class impl = Class.forName((String) value);
                value = impl.newInstance();
            }
            catch (ClassNotFoundException ex)
            {
                // it's not a classname, leave it
            }
        }

        // If we have an instansiated value object and askFor is an interface
        // then we can check that one implements the other
        if (!(value instanceof String) && askFor instanceof String)
        {
            try
            {
                Class iface = Class.forName((String) askFor);
                if (!iface.isAssignableFrom(value.getClass()))
                {
                    log.error("Can't cast: " + value + " to " + askFor); //$NON-NLS-1$ //$NON-NLS-2$
                }
            }
            catch (ClassNotFoundException ex)
            {
                // it's not a classname, leave it
            }
        }

        if (value instanceof String && log.isDebugEnabled())
        {
            log.debug("Adding IoC setting: " + askFor + "=" + value); //$NON-NLS-1$ //$NON-NLS-2$
        }
        else
        {
            log.debug("Adding IoC implementation: " + askFor + "=" + value.getClass().getName()); //$NON-NLS-1$ //$NON-NLS-2$
        }

        beans.put(askFor, value);
    }

    /**
     * Called to indicate that we finished called setImplementation.
     * @see DefaultContainer#addParameter(Object, Object)
     */
    public void configurationFinished()
    {
        // We try to autowire each bean in turn
        for (Iterator it = beans.entrySet().iterator(); it.hasNext();)
        {
            Map.Entry entry = (Map.Entry) it.next();
            // Class type = (Class) entry.getKey();
            Object ovalue = entry.getValue();

            if (!(ovalue instanceof String))
            {
                log.debug("Trying to autowire: " + ovalue.getClass().getName()); //$NON-NLS-1$
    
                Method[] methods = ovalue.getClass().getMethods();
                methods:
                for (int i = 0; i < methods.length; i++)
                {
                    Method setter = methods[i];
    
                    if (setter.getName().startsWith("set") && //$NON-NLS-1$
                        setter.getName().length() > 3 &&
                        setter.getParameterTypes().length == 1)
                    {
                        String name = Character.toLowerCase(setter.getName().charAt(3)) + setter.getName().substring(4);
                        Class propertyType = setter.getParameterTypes()[0];

                        // First we try auto-wire by name
                        Object setting = beans.get(name);
                        if (setting != null)
                        {
                            if (propertyType.isAssignableFrom(setting.getClass()))
                            {
                                log.debug("- autowire-by-name: " + name + "=" + setting); //$NON-NLS-1$ //$NON-NLS-2$
                                invoke(setter, ovalue, setting);

                                continue methods;
                            }
                            else if (propertyType == Boolean.TYPE && setting.getClass() == String.class)
                            {
                                Boolean bool = Boolean.valueOf((String) setting);
                                log.debug("- autowire-by-name: " + name + "=" + bool); //$NON-NLS-1$ //$NON-NLS-2$
                                invoke(setter, ovalue, bool);

                                continue methods;
                            }
                        }

                        // Next we try autowire-by-type
                        Object value = beans.get(propertyType.getName());
                        if (value != null)
                        {
                            log.debug("- autowire-by-type: " + name + "=" + value.getClass().getName()); //$NON-NLS-1$ //$NON-NLS-2$
                            invoke(setter, ovalue, value);

                            continue methods;
                        }

                        log.debug("- skipped autowire: " + name); //$NON-NLS-1$ //$NON-NLS-2$
                    }
                }
            }
        }
    }

    /**
     * A helper to do the reflection
     * @param setter The method to invoke
     * @param bean The object to invoke the method on
     * @param value The value to assign to the property using the setter method
     */
    private void invoke(Method setter, Object bean, Object value)
    {
        try
        {
            setter.invoke(bean, new Object[] { value });
        }
        catch (IllegalArgumentException ex)
        {
            log.error("- Internal error: " + ex.getMessage()); //$NON-NLS-1$
        }
        catch (IllegalAccessException ex)
        {
            log.error("- Permission error: " + ex.getMessage()); //$NON-NLS-1$
        }
        catch (InvocationTargetException ex)
        {
            log.error("- Exception during auto-wire", ex.getTargetException()); //$NON-NLS-1$
        }
    }

    /* (non-Javadoc)
     * @see uk.ltd.getahead.dwr.Container#getBean(java.lang.String)
     */
    public Object getBean(String id)
    {
        Object reply = beans.get(id);
        if (reply == null)
        {
            if (log.isDebugEnabled())
            {
                for (Iterator it = beans.keySet().iterator(); it.hasNext();)
                {
                    log.debug("- known bean: " + it.next()); //$NON-NLS-1$
                }
            }

            throw new IllegalArgumentException("DefaultContainer can't find a " + id); //$NON-NLS-1$
        }

        return reply;
    }

    /**
     * The beans that we know of indexed by type
     */
    protected Map beans = new TreeMap();

    /**
     * The log stream
     */
    private static final Logger log = Logger.getLogger(DefaultContainer.class);
}
