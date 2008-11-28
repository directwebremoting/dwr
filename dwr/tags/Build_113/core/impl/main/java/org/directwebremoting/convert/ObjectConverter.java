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

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

import org.directwebremoting.ConversionException;
import org.directwebremoting.extend.FieldProperty;
import org.directwebremoting.extend.InboundContext;
import org.directwebremoting.extend.Property;

/**
 * Convert a Javascript associative array into a JavaBean
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class ObjectConverter extends BasicObjectConverter
{
    /**
     * Do we force accessibility for private fields
     * @param force "true|false" to set the force accessibility flag
     */
    public void setForce(String force)
    {
        this.force = Boolean.valueOf(force);
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.extend.NamedConverter#getPropertyMapFromObject(java.lang.Object, boolean, boolean)
     */
    public Map<String, Property> getPropertyMapFromObject(Object example, boolean readRequired, boolean writeRequired) throws ConversionException
    {
        Class<?> clazz = example.getClass();
        return getPropertyMapFromClass(clazz, readRequired, writeRequired);
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.extend.NamedConverter#getPropertyMapFromClass(java.lang.Class, boolean, boolean)
     */
    public Map<String, Property> getPropertyMapFromClass(Class<?> type, boolean readRequired, boolean writeRequired)
    {
        Map<String, Property> allFields = new HashMap<String, Property>();

        while (type != Object.class)
        {
            Field[] fields = type.getDeclaredFields();

            fieldLoop:
            for (Field field : fields)
            {
                String name = field.getName();

                // We don't marshall getClass()
                if ("class".equals(name))
                {
                    continue fieldLoop;
                }

                // Access rules mean we might not want to do this one
                if (!isAllowedByIncludeExcludeRules(name))
                {
                    continue fieldLoop;
                }

                if (!Modifier.isPublic(field.getModifiers()))
                {
                    if (force)
                    {
                        field.setAccessible(true);
                    }
                    else
                    {
                        continue fieldLoop;
                    }
                }

                allFields.put(name, new FieldProperty(field));
            }

            type = type.getSuperclass();
        }

        return allFields;
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.convert.BasicObjectConverter#createTypeHintContext(org.directwebremoting.extend.InboundContext, org.directwebremoting.extend.Property)
     */
    @Override
    protected Property createTypeHintContext(InboundContext inctx, Property property)
    {
        return inctx.getCurrentProperty();
    }

    /**
     * Do we force accessibility for hidden fields
     */
    private boolean force = false;
}
