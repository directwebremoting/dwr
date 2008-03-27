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
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeMap;

import org.directwebremoting.dwrp.ConversionConstants;
import org.directwebremoting.dwrp.ObjectOutboundVariable;
import org.directwebremoting.extend.InboundContext;
import org.directwebremoting.extend.InboundVariable;
import org.directwebremoting.extend.MarshallException;
import org.directwebremoting.extend.OutboundContext;
import org.directwebremoting.extend.OutboundVariable;
import org.directwebremoting.extend.TypeHintContext;
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
            throw new MarshallException(paramType, Messages.getString("BeanConverter.FormatError", ConversionConstants.INBOUND_MAP_START));
        }

        if (!value.endsWith(ConversionConstants.INBOUND_MAP_END))
        {
            throw new MarshallException(paramType, Messages.getString("BeanConverter.FormatError", ConversionConstants.INBOUND_MAP_START));
        }

        value = value.substring(1, value.length() - 1);

        try
        {
            Map tokens = extractInboundTokens(paramType, value);

            Class type = paramType;
            if (instanceType != null)
            {
                type = instanceType;
            }

            /*
            String overrideTypeName = (String) tokens.get("_class");
            if (overrideTypeName != null)
            {
                Class overrideType = Class.forName(overrideTypeName);
                if (converterManager.allowOverride(type, overrideType))
                {
                    type = overrideType;
                }
            }
            */

            Object bean = type.newInstance();

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
            for (Iterator it = tokens.entrySet().iterator(); it.hasNext();)
            {
                Map.Entry entry = (Map.Entry) it.next();
                String key = (String) entry.getKey();
                String val = (String) entry.getValue();

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
        catch (InvocationTargetException ex)
        {
            throw new MarshallException(paramType, ex.getTargetException());
        }
        catch (Exception ex)
        {
            throw new MarshallException(paramType, ex);
        }
    }

    /**
     * Loop over all the inputs and extract a Map of key:value pairs
     * @param paramType The type we are converting to
     * @param value The input string
     * @return A Map of the tokens in the string
     * @throws MarshallException If the marshalling fails
     */
    protected Map extractInboundTokens(Class paramType, String value) throws MarshallException
    {
        Map tokens = new HashMap();
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
                throw new MarshallException(paramType, Messages.getString("BeanConverter.MissingSeparator", ConversionConstants.INBOUND_MAP_ENTRY, token));
            }

            String key = token.substring(0, colonpos).trim();
            String val = token.substring(colonpos + 1).trim();
            tokens.put(key, val);
        }

        return tokens;
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.Converter#convertOutbound(java.lang.Object, org.directwebremoting.OutboundContext)
     */
    public OutboundVariable convertOutbound(Object data, OutboundContext outctx) throws MarshallException
    {
        // Where we collect out converted children
        Map ovs = new TreeMap();

        // We need to do this before collecing the children to save recurrsion
        ObjectOutboundVariable ov = new ObjectOutboundVariable(outctx);
        outctx.put(data, ov);

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
                    Method getter = descriptor.getReadMethod();

                    if (shouldMarshall(getter, data, name))
                    {
                        Object value = getter.invoke(data, new Object[0]);
                        OutboundVariable nested = getConverterManager().convertOutbound(value, outctx);

                        ovs.put(name, nested);
                    }
                }
                catch (Exception ex)
                {
                    log.warn("Failed to convert " + name, ex);
                }
            }
        }
        catch (IntrospectionException ex)
        {
            throw new MarshallException(data.getClass(), ex);
        }

        ov.init(ovs, getJavascript());

        return ov;
    }

    /**
     * Is this a member that we should consider marshalling?
     * @param getter The read method
     * @param data The object on which the read method is to be called
     * @param name The name of the property that we will be reading
     * @return true if we should marshall the property
     */
    private boolean shouldMarshall(Method getter, Object data, String name)
    {
        // We don't marshall things we can't read
        if (getter == null)
        {
            return false;
        }

        // We don't marshall getClass()
        if (name.equals("class"))
        {
            return false;
        }

        // Access rules mean we might not want to do this one
        if (!isAllowed(name))
        {
            log.debug("Skipping marshalling " + name + " due to include/exclude rules");
            return false;
        }

        if (!isAvailable(data, name))
        {
            log.debug("Skipping marshalling " + name + " due to availability rules");
            return false;
        }

        return true;
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.convert.BasicObjectConverter#getAllPropertyNames(java.lang.Class)
     */
    public String[] getAllPropertyNames(Class mappedType)
    {
        try
        {
            BeanInfo info = Introspector.getBeanInfo(mappedType);

            Set allFieldNames = new HashSet();
            PropertyDescriptor[] descriptors = info.getPropertyDescriptors();
            for (int i = 0; i < descriptors.length; i++)
            {
                PropertyDescriptor descriptor = descriptors[i];
                allFieldNames.add(descriptor.getName());
            }

            return (String[]) allFieldNames.toArray(new String[allFieldNames.size()]);
        }
        catch (IntrospectionException ex)
        {
            throw new IllegalArgumentException(ex.getMessage());
        }
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.convert.BasicObjectConverter#getPropertyType(java.lang.Class, java.lang.String)
     */
    public Class getPropertyType(Class mappedType, String propertyName)
    {
        try
        {
            BeanInfo info = Introspector.getBeanInfo(mappedType);
            Class propType = null;

            PropertyDescriptor[] descriptors = info.getPropertyDescriptors();
            for (int i = 0; i < descriptors.length; i++)
            {
                PropertyDescriptor descriptor = descriptors[i];
                String name = descriptor.getName();
                if (name.equals(propertyName))
                {
                    propType = descriptor.getClass();
                }
            }

            if (propType == null)
            {
                throw new IllegalArgumentException("No property '" + propertyName + "' in class " + mappedType.getName() + ".");
            }

            return propType;
        }
        catch (IntrospectionException ex)
        {
            throw new IllegalArgumentException(ex.getMessage());
        }
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
