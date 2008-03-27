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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeMap;

import org.directwebremoting.Converter;
import org.directwebremoting.InboundContext;
import org.directwebremoting.InboundVariable;
import org.directwebremoting.MarshallException;
import org.directwebremoting.OutboundContext;
import org.directwebremoting.OutboundVariable;
import org.directwebremoting.dwrp.ConversionConstants;
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
            throw new IllegalArgumentException(Messages.getString("BeanConverter.MissingOpener", ConversionConstants.INBOUND_MAP_START)); //$NON-NLS-1$
        }

        if (!value.endsWith(ConversionConstants.INBOUND_MAP_END))
        {
            throw new IllegalArgumentException(Messages.getString("BeanConverter.MissingCloser", ConversionConstants.INBOUND_MAP_START)); //$NON-NLS-1$
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

            // We know what we are converting to, so we create a map of property
            // names against PropertyDescriptors to speed lookup later
            Field[] fields = getAllFields(bean);
            Map props = new HashMap();
            for (int i = 0; i < fields.length; i++)
            {
                String key = fields[i].getName();
                props.put(key, fields[i]);
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

            // Loop through the property declarations
            StringTokenizer st = new StringTokenizer(value, ConversionConstants.INBOUND_MAP_SEPARATOR);
            int size = st.countTokens();
            for (int i = 0; i < size; i++)
            {
                String token = st.nextToken();
                if (token.trim().length() == 0)
                {
                    continue;
                }

                int colonpos = token.indexOf(ConversionConstants.INBOUND_MAP_ENTRY);
                if (colonpos == -1)
                {
                    throw new MarshallException(Messages.getString("BeanConverter.MissingSeparator", ConversionConstants.INBOUND_MAP_ENTRY, token)); //$NON-NLS-1$
                }

                String key = token.substring(0, colonpos).trim();
                String val = token.substring(colonpos + 1).trim();

                Field field = (Field) props.get(key);
                if (field == null)
                {
                    log.warn("No field for " + key); //$NON-NLS-1$
                    StringBuffer all = new StringBuffer();
                    for (Iterator it = props.keySet().iterator(); it.hasNext();)
                    {
                        all.append(it.next());
                        if (it.hasNext())
                        {
                            all.append(',');
                        }
                    }
                    log.warn("Fields exist for (" + all + ")."); //$NON-NLS-1$ //$NON-NLS-2$
                }
                else
                {
                    Class propType = field.getType();

                    String[] split = LocalUtil.splitInbound(val);
                    String splitValue = split[LocalUtil.INBOUND_INDEX_VALUE];
                    String splitType = split[LocalUtil.INBOUND_INDEX_TYPE];

                    InboundVariable nested = new InboundVariable(iv.getLookup(), null, splitType, splitValue);

                    if (!field.isAccessible())
                    {
                        if (force)
                        {
                            field.setAccessible(true);
                        }
                        else
                        {
                            log.debug("Field: " + field.getName() + " is not accessible. use <param name='force' value='true'/>"); //$NON-NLS-1$ //$NON-NLS-2$
                            continue;
                        }
                    }

                    Object output = converterManager.convertInbound(propType, nested, inctx, inctx.getCurrentTypeHintContext());
                    field.set(bean, output);
                }
            }

            return bean;
        }
        catch (MarshallException ex)
        {
            throw ex;
        }
        catch (Exception ex)
        {
            throw new MarshallException(ex);
        }
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.Converter#convertOutbound(java.lang.Object, org.directwebremoting.OutboundContext)
     */
    public OutboundVariable convertOutbound(Object data, OutboundContext outctx) throws MarshallException
    {
        // Where we collect out converted children
        Map ovs = new TreeMap();

        // We need to do this before collecing the children to save recurrsion
        OutboundVariable ov = outctx.createOutboundVariable(data);

        try
        {
            Field[] fields = getAllFields(data);
            for (int i = 0; i < fields.length; i++)
            {
                Field field = fields[i];
                String name = field.getName();

                try
                {
                    // We don't marshall getClass()
                    if (name.equals("class")) //$NON-NLS-1$
                    {
                        continue;
                    }

                    // Access rules mean we might not want to do this one
                    if (!isAllowed(name))
                    {
                        log.debug("Skipping marshalling " + name + " due to include/exclude rules"); //$NON-NLS-1$ //$NON-NLS-2$
                        continue;
                    }

                    if (!field.isAccessible())
                    {
                        if (force)
                        {
                            field.setAccessible(true);
                        }
                        else
                        {
                            log.debug("Field: " + field.getName() + " is not accessible. use <param name='force' value='true'/>"); //$NON-NLS-1$ //$NON-NLS-2$
                            continue;
                        }
                    }

                    Object value = field.get(data);
                    OutboundVariable nested = getConverterManager().convertOutbound(value, outctx);

                    ovs.put(name, nested);
                }
                catch (Exception ex)
                {
                    log.warn("Failed to convert " + name, ex); //$NON-NLS-1$
                }
            }
        }
        catch (Exception ex)
        {
            throw new MarshallException(ex);
        }

        ConverterUtil.addMapInit(ov, ovs);
        return ov;
    }

    /**
     * Get the fields from a bean. You can't use <code>class.getFields()</code>
     * in place of this because it only gives you accessible fields, and
     * although <code>class.getDeclaredFields()</code> does give in-accessible
     * fields is doesn't walk up the tree.
     * @param bean The class to find bean info from
     * @return An array of all the fields for the given object
     */
    protected Field[] getAllFields(Object bean)
    {
        Set allFields = new HashSet();

        Class clazz = bean.getClass();

        while (clazz != Object.class)
        {
            Field[] fields = clazz.getDeclaredFields();
            for (int i = 0; i < fields.length; i++)
            {
                allFields.add(fields[i]);
            }

            clazz = clazz.getSuperclass();
        }

        return (Field[]) allFields.toArray(new Field[allFields.size()]);
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
