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
import org.directwebremoting.ConversionException;
import org.directwebremoting.extend.InboundVariable;
import org.directwebremoting.extend.Property;
import org.directwebremoting.extend.PropertyDescriptorProperty;
import org.directwebremoting.extend.ProtocolConstants;
import org.directwebremoting.util.LocalUtil;

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
    public Object convertInbound(Class<?> paramType, InboundVariable data) throws ConversionException
    {
        if (data == null)
        {
            return null;
        }

        String value = data.getValue();

        log.debug("handling variable (" + value + ") for class (" + paramType.getName() + ")");

        // If the text is null then the whole bean is null
        if (value.trim().equals(ProtocolConstants.INBOUND_NULL))
        {
            return null;
        }

        if (!value.startsWith(ProtocolConstants.INBOUND_MAP_START) || !value.endsWith(ProtocolConstants.INBOUND_MAP_END))
        {
            log.warn("Expected object while converting data for " + paramType.getName() + " in " + data.getContext().getCurrentProperty() + ". Passed: " + value);
            throw new ConversionException(paramType, "Data conversion error. See logs for more details.");
        }

        value = value.substring(1, value.length() - 1);

        try
        {
            if (instanceType != null)
            {
               LocalUtil.classForName(instanceType.getName());
            }
            else
            {
                LocalUtil.classForName(paramType.getName());
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
                log.error("XmlObject.Factory method not found for Class [" + paramType.toString() + "]");
                throw new ConversionException(paramType, "XmlObject.Factory method not found");
            }

            Class<?>[] emptyArglist = new Class[0];
            Method newInstance = factory.getMethod("newInstance", emptyArglist);
            Object bean = newInstance.invoke(null, (Object[]) emptyArglist);

            if (instanceType != null)
            {
                data.getContext().addConverted(data, instanceType, bean);
            }
            else
            {
                data.getContext().addConverted(data, paramType, bean);
            }

            Map<String, Property> properties = getPropertyMapFromClass(paramType, false, true);

            // Loop through the properties passed in
            Map<String, String> tokens = extractInboundTokens(paramType, value);
            for (Entry<String, String> entry : tokens.entrySet())
            {
                String key = entry.getKey();
                String val = entry.getValue();

                log.debug("token entry (" + key + ") with value (" + val + ")");

                Property property = properties.get(key);
                if (property == null)
                {
                    log.warn("Missing java bean property to match javascript property: " + key + ". For causes see debug level logs:");

                    log.debug("- The javascript may be refer to a property that does not exist");
                    log.debug("- You may be missing the correct setter: set" + Character.toTitleCase(key.charAt(0)) + key.substring(1) + "()");
                    log.debug("- The property may be excluded using include or exclude rules.");

                    StringBuffer all = new StringBuffer();
                    for (Iterator<String> pit = properties.keySet().iterator(); pit.hasNext();)
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

                Class<?> propType = property.getPropertyType();

                Object output = convert(val, propType, data.getContext(), property);
                property.setValue(bean, output);
            }

            return bean;
        }
        catch (ConversionException ex)
        {
            throw ex;
        }
        catch (Exception ex)
        {
            throw new ConversionException(paramType, ex);
        }
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.convert.BeanConverter#getPropertyMapFromClass(java.lang.Class, boolean, boolean)
     */
    @Override
    public Map<String, Property> getPropertyMapFromClass(Class<?> paramType, boolean readRequired, boolean writeRequired) throws ConversionException
    {
        try
        {
            if (!XmlObject.class.isAssignableFrom(paramType))
            {
                throw new ConversionException(paramType, "class (" + paramType.getName() + ") not assignable from XmlObject");
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

                    if (writeRequired && LocalUtil.getWriteMethod(paramType, descriptor) == null)
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
            throw new ConversionException(paramType, ex);
        }

    }

    /**
     * This used to be static, but there's a chance that could fail when there
     * is more than 1 DWR servlet in a given context.
     */
    private final StringEnumAbstractBaseConverter enumConverter = new StringEnumAbstractBaseConverter();

    /**
     * The log stream
     */
    private static Log log = LogFactory.getLog(XmlBeanConverter.class);
}
