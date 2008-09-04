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

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import org.directwebremoting.ConversionException;

/**
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class NestedProperty implements MethodProperty
{
    /**
     * @param method
     * @param parameterNumber
     * @param parentParameterType
     */
    public NestedProperty(Method method, Type parentParameterType, int parameterNumber, int newParameterNumber)
    {
        if (parentParameterType instanceof ParameterizedType)
        {
            ParameterizedType ptype = (ParameterizedType) parentParameterType;
            Type[] actualTypeArguments = ptype.getActualTypeArguments();

            if (newParameterNumber >= actualTypeArguments.length)
            {
                throw new IllegalArgumentException("newParameterNumber=" + newParameterNumber + " is too big when parameterType=" + parentParameterType + " give actualTypeArguments.length=" + actualTypeArguments.length);
            }

            this.parameterType = actualTypeArguments[newParameterNumber];
        }
        else
        {
            this.parameterType = null;
        }

        this.method = method;
        this.parameterNumber = parameterNumber;
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.extend.Property#createTypeHintContext(org.directwebremoting.extend.ConverterManager, org.directwebremoting.extend.InboundContext)
     */
    public TypeHintContext createTypeHintContext(ConverterManager converterManager)
    {
        return new TypeHintContext(converterManager, this, parameterNumber);
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.extend.Property#getName()
     */
    public String getName()
    {
        return "NestedProperty";
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.extend.Property#getPropertyType()
     */
    public Class<?> getPropertyType()
    {
        return null;
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.extend.Property#getValue(java.lang.Object)
     */
    public Object getValue(Object bean) throws ConversionException
    {
        throw new UnsupportedOperationException("Can't get value from nested property");
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.extend.Property#setValue(java.lang.Object, java.lang.Object)
     */
    public void setValue(Object bean, Object value) throws ConversionException
    {
        throw new UnsupportedOperationException("Can't set value to nested property");
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.extend.MethodProperty#getMethod()
     */
    public Method getMethod()
    {
        return method;
    }

    /**
     * @return The type parameter
     */
    public Type getParameterType()
    {
        return parameterType;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode()
    {
        return method.hashCode() + parameterNumber;
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

        NestedProperty that = (NestedProperty) obj;

        if (!this.method.equals(that.method))
        {
            return false;
        }

        return this.parameterNumber == that.parameterNumber;
    }

    private final Method method;

    private final int parameterNumber;

    private final Type parameterType;
}
