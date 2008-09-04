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

import java.lang.reflect.Constructor;

import org.directwebremoting.ConversionException;

/**
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class ConstructorProperty implements Property
{
    /**
     * @param constructor
     * @param parameterName
     * @param parameterNum
     */
    public ConstructorProperty(Constructor<?> constructor, String parameterName, int parameterNum)
    {
        this.constructor = constructor;
        this.parameterName = parameterName;
        this.parameterNum = parameterNum;
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.extend.Property#createTypeHintContext(org.directwebremoting.extend.InboundContext)
     */
    public TypeHintContext createTypeHintContext(ConverterManager converterManager)
    {
        return new TypeHintContext(converterManager, this, parameterNum);
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.extend.Property#getName()
     */
    public String getName()
    {
        return parameterName;
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.extend.Property#getPropertyType()
     */
    public Class<?> getPropertyType()
    {
        return constructor.getParameterTypes()[parameterNum];
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.extend.Property#getValue(java.lang.Object)
     */
    public Object getValue(Object bean) throws ConversionException
    {
        throw new UnsupportedOperationException("Can't get value from constructor parameter");
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.extend.Property#setValue(java.lang.Object, java.lang.Object)
     */
    public void setValue(Object bean, Object value) throws ConversionException
    {
        throw new UnsupportedOperationException("Can't set value to constructor parameter");
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode()
    {
        return constructor.hashCode() + parameterNum;
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

        ConstructorProperty that = (ConstructorProperty) obj;

        if (!this.constructor.equals(that.constructor))
        {
            return false;
        }

        return this.parameterNum == that.parameterNum;
    }

    private final Constructor<?> constructor;

    private final String parameterName;

    private final int parameterNum;
}
