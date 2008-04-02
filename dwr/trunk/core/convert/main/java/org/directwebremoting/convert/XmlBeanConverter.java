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
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.xmlbeans.XmlObject;
import org.directwebremoting.dwrp.ParseUtil;
import org.directwebremoting.dwrp.ProtocolConstants;
import org.directwebremoting.extend.InboundContext;
import org.directwebremoting.extend.InboundVariable;
import org.directwebremoting.extend.MarshallException;
import org.directwebremoting.extend.Property;
import org.directwebremoting.extend.TypeHintContext;
import org.directwebremoting.impl.PropertyDescriptorProperty;
import org.directwebremoting.util.LocalUtil;
import org.directwebremoting.util.Messages;

/**
 * A Converter for Apache XMLBeans.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 * @author Matthew Young [matthew dot young at forsakringskassan dot se]
 */
public class XmlBeanConverter extends BeanConverter
{
    /* (non-Javadoc)
     * @see org.directwebremoting.extend.Converter#convertInbound(java.lang.Class, org.directwebremoting.extend.InboundVariable, org.directwebremoting.extend.InboundContext)
     */
    @Override
    public Object convertInbound(Class<?> paramType, InboundVariable iv, InboundContext inctx) throws MarshallException
    {
        String value = iv.getValue();

        logger.debug("handling variable (" + value + ") for class (" + paramType.getName() + ")");

        // If the text is null then the whole bean is null
        if (value.trim().equals(ProtocolConstants.INBOUND_NULL))
        {
            return null;
        }

        if (!value.startsWith(ProtocolConstants.INBOUND_MAP_START))
        {
            throw new MarshallException(paramType, Messages.getString("BeanConverter.FormatError", ProtocolConstants.INBOUND_MAP_START));
        }

        if (!value.endsWith(ProtocolConstants.INBOUND_MAP_END))
        {
            throw new MarshallException(paramType, Messages.getString("BeanConverter.FormatError", ProtocolConstants.INBOUND_MAP_START));
        }

        value = value.substring(1, value.length() - 1);

        try
        {
            if (instanceType != null)
            {
                Class.forName(instanceType.getName());
            }
            else
            {
                Class.forName(paramType.getName());
            }

            Class<?>[] innerClasses = paramType.getClasses();
            Class<?> factory = null;
            for (Class<?> aClass : innerClasses)
            {
                if (aClass.getName().endsWith("Factory"))
                {
                    factory = aClass;
                }
            }

            if (factory == null)
            {
                logger.error("XmlObject.Factory method not found for Class [" + paramType.toString() + "]");
                throw new MarshallException(paramType, "XmlObject.Factory method not found");
            }

            Class<?>[] emptyArglist = new Class[0];
            Method newInstance = factory.getMethod("newInstance", emptyArglist);
            Object bean = newInstance.invoke(null, (Object[]) emptyArglist);

            if (instanceType != null)
            {
                inctx.addConverted(iv, instanceType, bean);
            }
            else
            {
                inctx.addConverted(iv, paramType, bean);
            }

            Map<String, Property> properties = getPropertyMapFromClass(paramType, false, true);

            // Loop through the properties passed in
            Map<String, String> tokens = extractInboundTokens(paramType, value);
            for (Entry<String, String> entry : tokens.entrySet())
            {
                String key = entry.getKey();
                String val = entry.getValue();

                logger.debug("token entry (" + key + ") with value (" + val + ")");

                Property property = properties.get(key);
                if (property == null)
                {
                    logger.warn("Missing java bean property to match javascript property: " + key + ". For causes see debug level logs:");

                    logger.debug("- The javascript may be refer to a property that does not exist");
                    logger.debug("- You may be missing the correct setter: set" + Character.toTitleCase(key.charAt(0)) + key.substring(1) + "()");
                    logger.debug("- The property may be excluded using include or exclude rules.");

                    StringBuffer all = new StringBuffer();
                    for (Iterator<String> pit = properties.keySet().iterator(); pit.hasNext();)
                    {
                        all.append(pit.next());
                        if (pit.hasNext())
                        {
                            all.append(',');
                        }
                    }
                    logger.debug("Fields exist for (" + all + ").");
                    continue;
                }

                Class<?> propType = property.getPropertyType();

                String[] split = ParseUtil.splitInbound(val);
                String splitValue = split[LocalUtil.INBOUND_INDEX_VALUE];
                String splitType = split[LocalUtil.INBOUND_INDEX_TYPE];

                InboundVariable nested = new InboundVariable(iv.getLookup(), null, splitType, splitValue);
                nested.dereference();

                TypeHintContext incc = createTypeHintContext(inctx, property);

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
     * @see org.directwebremoting.convert.BeanConverter#getPropertyMapFromClass(java.lang.Class, boolean, boolean)
     */
    @Override
    public Map<String, Property> getPropertyMapFromClass(Class<?> paramType, boolean readRequired, boolean writeRequired) throws MarshallException
    {
        try
        {
            if (!XmlObject.class.isAssignableFrom(paramType))
            {
                throw new MarshallException(paramType, "class (" + paramType.getName() + ") not assignable from XmlObject");
            }

            Class<?> beanInterface;
            if (paramType.isInterface())
            {
                beanInterface = paramType;
            }
            else
            {
                beanInterface = paramType.getInterfaces()[0];
            }

            Class<?> superInterface = (Class<?>) beanInterface.getGenericInterfaces()[0];

            Map<String, Property> properties = new HashMap<String, Property>();

            while (XmlObject.class.isAssignableFrom(superInterface))
            {
                BeanInfo info = Introspector.getBeanInfo(beanInterface);
                PropertyDescriptor[] descriptors = info.getPropertyDescriptors();

                for (int i = 0; i < descriptors.length; i++)
                {
                    PropertyDescriptor descriptor = descriptors[i];
                    String name = descriptor.getName();
                    String type = descriptor.getPropertyType().getName();

                    // register Enum types
                    if (type.matches(".*\\$Enum"))
                    {
                        getConverterManager().addConverter(type, enumConverter);
                    }

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

                beanInterface = (Class<?>) beanInterface.getGenericInterfaces()[0];
                superInterface = (Class<?>) beanInterface.getGenericInterfaces()[0];
            }

            return properties;
        }
        catch (IntrospectionException ex)
        {
            throw new MarshallException(paramType, ex);
        }

    }

    private static StringEnumAbstractBaseConverter enumConverter = new StringEnumAbstractBaseConverter();

    private static Log logger = LogFactory.getLog(XmlBeanConverter.class);
}
