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
package org.directwebremoting.extend;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

import org.directwebremoting.ConversionException;
import org.directwebremoting.util.LocalUtil;

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
    public Class<?> getPropertyType()
    {
        Method method = descriptor.getReadMethod();

        Type[] types = method.getGenericParameterTypes();
        if (types.length == 0)
        {
            return descriptor.getPropertyType();
        }

        Type parameterType = types[0];
        return LocalUtil.toClass(parameterType, toString());
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.extend.Property#getValue(java.lang.Object)
     */
    public Object getValue(Object bean) throws ConversionException
    {
        try
        {
            return descriptor.getReadMethod().invoke(bean);
        }
        catch (InvocationTargetException ex)
        {
            throw new ConversionException(bean.getClass(), ex.getTargetException());
        }
        catch (Exception ex)
        {
            throw new ConversionException(bean.getClass(), ex);
        }
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.extend.Property#setValue(java.lang.Object, java.lang.Object)
     */
    public void setValue(Object bean, Object value) throws ConversionException
    {
        try
        {
            descriptor.getWriteMethod().invoke(bean, value);
        }
        catch (InvocationTargetException ex)
        {
            throw new ConversionException(bean.getClass(), ex.getTargetException());
        }
        catch (Exception ex)
        {
            throw new ConversionException(bean.getClass(), ex);
        }
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.extend.Property#createChild(int)
     */
    public Property createChild(int newParameterNumber)
    {
        Method method = descriptor.getReadMethod();

        // Type[] types = method.getGenericParameterTypes();
        // if (types.length == 0)
        // {
        //     return new NestedProperty(this, method, null, 0, newParameterNumber);
        // }
        // return new NestedProperty(this, method, types[0], 0, newParameterNumber);

        Type type = method.getGenericReturnType();
        if (type == null)
        {
            return new NestedProperty(this, method, null, 0, newParameterNumber);
        }

        return new NestedProperty(this, method, type, 0, newParameterNumber);
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode()
    {
        return descriptor.getReadMethod().hashCode();
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj)
    {
        if (obj == this)
        {
            return true;
        }

        if (obj == null || this.getClass() != obj.getClass())
        {
            return false;
        }

        PropertyDescriptorProperty that = (PropertyDescriptorProperty) obj;

        if (!this.descriptor.getReadMethod().equals(that.descriptor.getReadMethod()))
        {
            return false;
        }

        return true;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        return "PropertyDescriptorProperty[" + descriptor.getName() + "=" + descriptor.getPropertyType() + "]";
    }

    /**
     * The PropertyDescriptor that we are proxying to
     */
    protected PropertyDescriptor descriptor;
}
