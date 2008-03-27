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
import java.util.Iterator;
import java.util.Map;

import org.directwebremoting.dwrp.ConversionConstants;
import org.directwebremoting.extend.Converter;
import org.directwebremoting.extend.InboundContext;
import org.directwebremoting.extend.InboundVariable;
import org.directwebremoting.extend.MarshallException;
import org.directwebremoting.extend.Property;
import org.directwebremoting.extend.TypeHintContext;
import org.directwebremoting.impl.FieldProperty;
import org.directwebremoting.util.LocalUtil;
import org.directwebremoting.util.Logger;
import org.directwebremoting.util.Messages;

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
     * @see org.directwebremoting.Converter#convertInbound(java.lang.Class, org.directwebremoting.InboundVariable, org.directwebremoting.InboundContext)
     */
    public Object convertInbound(Class paramType, InboundVariable iv, InboundContext inctx) throws MarshallException
    {
        String value = iv.getValue();

        // If the text is null then the whole bean is null
        if (value.trim().equals(ConversionConstants.INBOUND_NULL))
        {
            return null;
        }

        if (!value.startsWith(ConversionConstants.INBOUND_MAP_START))
        {
            throw new MarshallException(paramType, Messages.getString("BeanConverter.FormatError", ConversionConstants.INBOUND_MAP_START));
        }

        if (!value.endsWith(ConversionConstants.INBOUND_MAP_END))
        {
            throw new MarshallException(paramType, Messages.getString("BeanConverter.FormatError", ConversionConstants.INBOUND_MAP_START));
        }

        value = value.substring(1, value.length() - 1);

        try
        {
            Object bean;
            if (instanceType != null)
            {
                bean = instanceType.newInstance();
            }
            else
            {
                bean = paramType.newInstance();
            }

            // We should put the new object into the working map in case it
            // is referenced later nested down in the conversion process.
            if (instanceType != null)
            {
                inctx.addConverted(iv, instanceType, bean);
            }
            else
            {
                inctx.addConverted(iv, paramType, bean);
            }

            Map properties = getPropertyMap(bean.getClass(), false, true);

            // Loop through the properties passed in
            Map tokens = extractInboundTokens(paramType, value);
            for (Iterator it = tokens.entrySet().iterator(); it.hasNext();)
            {
                Map.Entry entry = (Map.Entry) it.next();
                String key = (String) entry.getKey();
                String val = (String) entry.getValue();

                Property property = (Property) properties.get(key);
                if (property == null)
                {
                    log.warn("Missing java bean property to match javascript property: " + key + ". For causes see debug level logs:");

                    log.debug("- The javascript may be refer to a property that does not exist");
                    log.debug("- You may be missing the correct setter: set" + Character.toTitleCase(key.charAt(0)) + key.substring(1) + "()");
                    log.debug("- The property may be excluded using include or exclude rules.");

                    StringBuffer all = new StringBuffer();
                    for (Iterator pit = properties.keySet().iterator(); pit.hasNext();)
                    {
                        all.append(pit.next());
                        if (pit.hasNext())
                        {
                            all.append(',');
                        }
                    }
                    log.debug("Fields exist for (" + all + ").");
                    continue;
                }

                Class propType = property.getPropertyType();

                String[] split = LocalUtil.splitInbound(val);
                String splitValue = split[LocalUtil.INBOUND_INDEX_VALUE];
                String splitType = split[LocalUtil.INBOUND_INDEX_TYPE];

                InboundVariable nested = new InboundVariable(iv.getLookup(), null, splitType, splitValue);

                TypeHintContext incc = inctx.getCurrentTypeHintContext();

                Object output = converterManager.convertInbound(propType, nested, inctx, incc);
                property.setValue(bean, output);
            }

            return bean;
        }
        catch (MarshallException ex)
        {
            throw ex;
        }
        catch (Exception ex)
        {
            throw new MarshallException(paramType, ex);
        }
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.convert.BasicObjectConverter#getPropertyMap(java.lang.Class, boolean, boolean)
     */
    public Map getPropertyMap(Class type, boolean readRequired, boolean writeRequired)
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
                if (name.equals("class"))
                {
                    continue fieldLoop;
                }

                // Access rules mean we might not want to do this one
                if (!isAllowedByIncludeExcludeRules(name))
                {
                    continue;
                }

                if (!Modifier.isPublic(field.getModifiers()))
                {
                    if (force)
                    {
                        field.setAccessible(true);
                    }
                    else
                    {
                        continue;
                    }
                }

                allFields.put(name, new FieldProperty(field));
            }

            type = type.getSuperclass();
        }

        return allFields;
    }

    /**
     * Do we force accessibillity for hidden fields
     */
    private boolean force;

    /**
     * The log stream
     */
    private static final Logger log = Logger.getLogger(ObjectConverter.class);
}
