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

import java.beans.PropertyDescriptor;

import net.sf.hibernate.LazyInitializationException;

import org.directwebremoting.ConversionException;
import org.directwebremoting.extend.Property;
import org.directwebremoting.extend.PropertyDescriptorProperty;

/**
 * A {@link Property} that catches Hibernate exceptions.
 * This is useful for Hibernate 2 where lazy loading results in an exception
 * and you are unable to detect and prevent this.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class H2PropertyDescriptorProperty extends PropertyDescriptorProperty
{
    /**
     * Simple constructor
     * @param descriptor The PropertyDescriptor that we are proxying to
     */
    public H2PropertyDescriptorProperty(PropertyDescriptor descriptor)
    {
        super(descriptor);
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.impl.PropertyDescriptorProperty#getValue(java.lang.Object)
     */
    @Override
    public Object getValue(Object bean) throws ConversionException
    {
        try
        {
            return super.getValue(bean);
        }
        catch (LazyInitializationException ex)
        {
            return null;
        }
        catch (Exception ex)
        {
            throw new ConversionException(bean.getClass(), ex);
        }
    }
}
