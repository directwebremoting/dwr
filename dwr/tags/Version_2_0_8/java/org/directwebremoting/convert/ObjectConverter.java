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

import org.directwebremoting.extend.Converter;
import org.directwebremoting.extend.InboundContext;
import org.directwebremoting.extend.MarshallException;
import org.directwebremoting.extend.Property;
import org.directwebremoting.extend.TypeHintContext;
import org.directwebremoting.impl.FieldProperty;

/**
 * Convert a Javascript associative array into a JavaBean
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class ObjectConverter extends BasicObjectConverter implements Converter
{
    /**
     * Do we force accessibility for private fields
     * @param force "true|false" to set the force accessibility flag
     */
    public void setForce(String force)
    {
        this.force = Boolean.valueOf(force).booleanValue();
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.extend.NamedConverter#getPropertyMapFromObject(java.lang.Object, boolean, boolean)
     */
    public Map getPropertyMapFromObject(Object example, boolean readRequired, boolean writeRequired) throws MarshallException
    {
        Class clazz = example.getClass();
        return getPropertyMapFromClass(clazz, readRequired, writeRequired);
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.extend.NamedConverter#getPropertyMap(java.lang.Class, boolean, boolean)
     */
    public Map getPropertyMapFromClass(Class type, boolean readRequired, boolean writeRequired)
    {
        Map allFields = new HashMap();

        while (type != Object.class)
        {
            Field[] fields = type.getDeclaredFields();

            fieldLoop:
            for (int i = 0; i < fields.length; i++)
            {
                Field field = fields[i];
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
    protected TypeHintContext createTypeHintContext(InboundContext inctx, Property property)
    {
        return inctx.getCurrentTypeHintContext();
    }

    /**
     * Do we force accessibillity for hidden fields
     */
    private boolean force = false;
}
