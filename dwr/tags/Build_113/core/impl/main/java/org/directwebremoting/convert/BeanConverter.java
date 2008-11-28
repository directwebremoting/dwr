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

import org.directwebremoting.ConversionException;
import org.directwebremoting.extend.InboundContext;
import org.directwebremoting.extend.Property;
import org.directwebremoting.extend.PropertyDescriptorProperty;

/**
 * Convert a Javascript associative array into a JavaBean
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class BeanConverter extends BasicObjectConverter
{
    /* (non-Javadoc)
     * @see org.directwebremoting.extend.NamedConverter#getPropertyMapFromObject(java.lang.Object, boolean, boolean)
     */
    public Map<String, Property> getPropertyMapFromObject(Object example, boolean readRequired, boolean writeRequired) throws ConversionException
    {
        return getPropertyMapFromClass(example.getClass(), readRequired, writeRequired);
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.extend.NamedConverter#getPropertyMap(java.lang.Class, boolean, boolean)
     */
    public Map<String, Property> getPropertyMapFromClass(Class<?> type, boolean readRequired, boolean writeRequired) throws ConversionException
    {
        try
        {
            BeanInfo info = Introspector.getBeanInfo(type);
            PropertyDescriptor[] descriptors = info.getPropertyDescriptors();

            Map<String, Property> properties = new HashMap<String, Property>();
            for (PropertyDescriptor descriptor : descriptors)
            {
                String name = descriptor.getName();

                // We don't marshall getClass()
                if ("class".equals(name))
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
            throw new ConversionException(type, ex);
        }
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.convert.BasicObjectConverter#createTypeHintContext(org.directwebremoting.extend.InboundContext, org.directwebremoting.extend.Property)
     */
    @Override
    protected Property createTypeHintContext(InboundContext inctx, Property property)
    {
        return property;
    }
}
