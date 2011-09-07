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
package org.directwebremoting.impl;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.directwebremoting.extend.MarshallException;
import org.directwebremoting.extend.Property;

/**
 * An implementation of {@link Property} that proxies to a {@link PropertyDescriptor}
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class PropertyDescriptorProperty implements Property
{
    /**
     * @param descriptor The PropertyDescriptor that we are proxying to
     */
    public PropertyDescriptorProperty(PropertyDescriptor descriptor)
    {
        this.descriptor = descriptor;
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.extend.Property#getName()
     */
    public String getName()
    {
        return descriptor.getName();
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.extend.Property#getPropertyType()
     */
    public Class getPropertyType()
    {
        return descriptor.getPropertyType();
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.extend.Property#getValue(java.lang.Object)
     */
    public Object getValue(Object bean) throws MarshallException
    {
        try
        {
            return descriptor.getReadMethod().invoke(bean, new Object[0]);
        }
        catch (InvocationTargetException ex)
        {
            throw new MarshallException(bean.getClass(), ex.getTargetException());
        }
        catch (Exception ex)
        {
            throw new MarshallException(bean.getClass(), ex);
        }
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.extend.Property#setValue(java.lang.Object, java.lang.Object)
     */
    public void setValue(Object bean, Object value) throws MarshallException
    {
        try
        {
            descriptor.getWriteMethod().invoke(bean, new Object[] { value });
        }
        catch (InvocationTargetException ex)
        {
            throw new MarshallException(bean.getClass(), ex.getTargetException());
        }
        catch (Exception ex)
        {
            throw new MarshallException(bean.getClass(), ex);
        }
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.extend.Property#getSetter()
     */
    public Method getSetter()
    {
        return descriptor.getWriteMethod();
    }

    /**
     * The PropertyDescriptor that we are proxying to
     */
    protected PropertyDescriptor descriptor;
}
