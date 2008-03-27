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
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.TreeMap;

import org.directwebremoting.InboundContext;
import org.directwebremoting.InboundVariable;
import org.directwebremoting.MarshallException;
import org.directwebremoting.OutboundContext;
import org.directwebremoting.OutboundVariable;
import org.directwebremoting.TypeHintContext;
import org.directwebremoting.dwrp.ConversionConstants;
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
    /**
     * Some child converters (like Hibernate at least) need to check that a
     * property should be marshalled. This allows them to veto a marshal
     * @param data The object to check on
     * @param property The property of the <code>data</code> object
     * @return true if we should continue and marshall it.
     */
    public abstract boolean isAvailable(Object data, String property);

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
            throw new IllegalArgumentException(Messages.getString("BeanConverter.MissingOpener", ConversionConstants.INBOUND_MAP_START));
        }

        if (!value.endsWith(ConversionConstants.INBOUND_MAP_END))
        {
            throw new IllegalArgumentException(Messages.getString("BeanConverter.MissingCloser", ConversionConstants.INBOUND_MAP_START));
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
            BeanInfo info = getBeanInfo(bean);
            PropertyDescriptor[] descriptors = info.getPropertyDescriptors();
            Map props = new HashMap();
            for (int i = 0; i < descriptors.length; i++)
            {
                String key = descriptors[i].getName();
                props.put(key, descriptors[i]);
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
                    throw new MarshallException(Messages.getString("BeanConverter.MissingSeparator", ConversionConstants.INBOUND_MAP_ENTRY, token));
                }

                String key = token.substring(0, colonpos).trim();
                String val = token.substring(colonpos + 1).trim();

                Method setter = null;
                PropertyDescriptor descriptor = (PropertyDescriptor) props.get(key);
                if (descriptor == null)
                {
                    log.warn("Null descriptor for key=" + key);
                    continue;
                }

                setter = descriptor.getWriteMethod();
                if (setter == null)
                {
                    log.warn("setter method for property " + key + " is not visible to DWR.");
                    log.info("You can add a public set" + Character.toTitleCase(key.charAt(0)) + key.substring(1) + "() method, or switch to using the ObjectConverter to read from members directly.");
                }
                else
                {
                    Class propType = descriptor.getPropertyType();

                    String[] split = LocalUtil.splitInbound(val);
                    String splitType = split[LocalUtil.INBOUND_INDEX_TYPE];
                    String splitValue = split[LocalUtil.INBOUND_INDEX_VALUE];

                    InboundVariable nested = new InboundVariable(iv.getLookup(), null, splitType, splitValue);

                    TypeHintContext incc = new TypeHintContext(converterManager, setter, 0);
                    Object output = converterManager.convertInbound(propType, nested, inctx, incc);

                    setter.invoke(bean, new Object[] { output });
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
            BeanInfo info = getBeanInfo(data);
            PropertyDescriptor[] descriptors = info.getPropertyDescriptors();
            for (int i = 0; i < descriptors.length; i++)
            {
                PropertyDescriptor descriptor = descriptors[i];
                String name = descriptor.getName();

                try
                {
                    // We don't marshall things we can't read
                    Method getter = descriptor.getReadMethod();
                    if (getter == null)
                    {
                        continue;
                    }

                    // We don't marshall getClass()
                    if (name.equals("class"))
                    {
                        continue;
                    }

                    // Access rules mean we might not want to do this one
                    if (!isAllowed(name))
                    {
                        log.debug("Skipping marshalling " + name + " due to include/exclude rules");
                        continue;
                    }

                    if (!isAvailable(data, name))
                    {
                        log.debug("Skipping marshalling " + name + " due to availability rules");
                        continue;
                    }

                    Object value = getter.invoke(data, new Object[0]);
                    OutboundVariable nested = getConverterManager().convertOutbound(value, outctx);

                    ovs.put(name, nested);
                }
                catch (Exception ex)
                {
                    log.warn("Failed to convert " + name, ex);
                }
            }
        }
        catch (IntrospectionException ex)
        {
            throw new MarshallException(ex);
        }

        ConverterUtil.addMapInit(ov, ovs);
        return ov;
    }

    /**
     * HibernateBeanConverter (and maybe others) may want to provide alternate
     * versions of bean.getClass()
     * @param bean The class to find bean info from
     * @return BeanInfo for the given class
     * @throws IntrospectionException
     */
    protected BeanInfo getBeanInfo(Object bean) throws IntrospectionException
    {
        return Introspector.getBeanInfo(bean.getClass());
    }

    /**
     * The log stream
     */
    private static final Logger log = Logger.getLogger(BasicBeanConverter.class);
}
