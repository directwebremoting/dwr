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

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.directwebremoting.extend.MarshallException;
import org.directwebremoting.extend.Property;

/**
 * An implementation of {@link Property} that proxies to a {@link Field}
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class FieldProperty implements Property
{
    /**
     * @param field The Field that we are proxying to
     */
    public FieldProperty(Field field)
    {
        this.field = field;
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.extend.Property#getName()
     */
    public String getName()
    {
        return field.getName();
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.extend.Property#getPropertyType()
     */
    public Class getPropertyType()
    {
        return field.getType();
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.extend.Property#getValue(java.lang.Object)
     */
    public Object getValue(Object bean) throws MarshallException
    {
        try
        {
            return field.get(bean);
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
            field.set(bean, value);
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
        return null;
    }

    /**
     * The Field that we are proxying to
     */
    private Field field;
}
