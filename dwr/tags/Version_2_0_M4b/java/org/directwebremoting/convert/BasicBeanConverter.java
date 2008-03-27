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
import java.util.Iterator;
import java.util.Map;

import org.directwebremoting.dwrp.ConversionConstants;
import org.directwebremoting.extend.InboundContext;
import org.directwebremoting.extend.InboundVariable;
import org.directwebremoting.extend.MarshallException;
import org.directwebremoting.extend.OutboundContext;
import org.directwebremoting.extend.Property;
import org.directwebremoting.extend.TypeHintContext;
import org.directwebremoting.impl.PropertyDescriptorProperty;
import org.directwebremoting.util.LocalUtil;
import org.directwebremoting.util.Logger;
import org.directwebremoting.util.Messages;

/**
 * BasicBeanConverter is a base to [Bean|Exception|Hibernate]Converter.
 * BasicBeanConverter provides implementaions of
 * {@link #convertInbound(Class, InboundVariable, InboundContext)} and
 * {@link #convertOutbound(Object, OutboundContext)} to {@link BasicObjectConverter}
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public abstract class BasicBeanConverter extends BasicObjectConverter
{
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

                TypeHintContext incc = new TypeHintContext(converterManager, property.getSetter(), 0);

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
    public Map getPropertyMap(Class type, boolean readRequired, boolean writeRequired) throws MarshallException
    {
        BeanInfo info = getBeanInfo(type);
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

    /**
     * HibernateBeanConverter (and maybe others) may want to provide alternate
     * versions of bean.getClass()
     * @param type The class to find bean info from
     * @return BeanInfo for the given class
     * @throws MarshallException
     */
    protected BeanInfo getBeanInfo(Class type) throws MarshallException
    {
        try
        {
            return Introspector.getBeanInfo(type);
        }
        catch (IntrospectionException ex)
        {
            throw new MarshallException(type, ex);
        }
    }

    /**
     * The log stream
     */
    private static final Logger log = Logger.getLogger(BasicBeanConverter.class);
}
