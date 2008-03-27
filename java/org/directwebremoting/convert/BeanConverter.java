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
package org.directwebremoting.convert;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.HashMap;
import java.util.Map;

import org.directwebremoting.extend.Converter;
import org.directwebremoting.extend.InboundContext;
import org.directwebremoting.extend.MarshallException;
import org.directwebremoting.extend.Property;
import org.directwebremoting.extend.TypeHintContext;
import org.directwebremoting.impl.PropertyDescriptorProperty;

/**
 * Convert a Javascript associative array into a JavaBean
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class BeanConverter extends BasicObjectConverter implements Converter
{
    /* (non-Javadoc)
     * @see org.directwebremoting.extend.NamedConverter#getPropertyMapFromObject(java.lang.Object, boolean, boolean)
     */
    public Map getPropertyMapFromObject(Object example, boolean readRequired, boolean writeRequired) throws MarshallException
    {
        return getPropertyMapFromClass(example.getClass(), readRequired, writeRequired);
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.extend.NamedConverter#getPropertyMap(java.lang.Class, boolean, boolean)
     */
    public Map getPropertyMapFromClass(Class type, boolean readRequired, boolean writeRequired) throws MarshallException
    {
        try
        {
            BeanInfo info = Introspector.getBeanInfo(type);
            PropertyDescriptor[] descriptors = info.getPropertyDescriptors();

            Map properties = new HashMap();
            for (int i = 0; i < descriptors.length; i++)
            {
                PropertyDescriptor descriptor = descriptors[i];
                String name = descriptor.getName();

                // We don't marshall getClass()
                if (name.equals("class"))
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

                properties.put(name, new PropertyDescriptorProperty(descriptor));
            }

            return properties;
        }
        catch (IntrospectionException ex)
        {
            throw new MarshallException(type, ex);
        }
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.convert.BasicObjectConverter#createTypeHintContext(org.directwebremoting.extend.InboundContext, org.directwebremoting.extend.Property)
     */
    protected TypeHintContext createTypeHintContext(InboundContext inctx, Property property)
    {
        return new TypeHintContext(converterManager, property.getSetter(), 0);
    }
}
