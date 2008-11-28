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
package org.directwebremoting.impl;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.directwebremoting.ConversionException;
import org.directwebremoting.extend.Converter;
import org.directwebremoting.extend.ConverterManager;
import org.directwebremoting.extend.ErrorOutboundVariable;
import org.directwebremoting.extend.InboundContext;
import org.directwebremoting.extend.InboundVariable;
import org.directwebremoting.extend.NamedConverter;
import org.directwebremoting.extend.NonNestedOutboundVariable;
import org.directwebremoting.extend.OutboundContext;
import org.directwebremoting.extend.OutboundVariable;
import org.directwebremoting.extend.Property;
import org.directwebremoting.extend.RealRawData;
import org.directwebremoting.io.RawData;
import org.directwebremoting.util.ClasspathScanner;
import org.directwebremoting.util.LocalUtil;
import org.directwebremoting.util.Loggers;

/**
 * A class to manage the converter types and the instantiated class name matches.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class DefaultConverterManager implements ConverterManager
{
    /* (non-Javadoc)
     * @see org.directwebremoting.ConverterManager#addConverterType(java.lang.String, java.lang.String)
     */
    public void addConverterType(String id, String className)
    {
        if (!LocalUtil.isJavaIdentifier(id))
        {
            Loggers.STARTUP.error("Illegal identifier: '" + id + "'");
            return;
        }

        Class<? extends Converter> clazz = LocalUtil.classForName(id, className, Converter.class);
        if (clazz != null)
        {
            Loggers.STARTUP.debug("- adding converter type: " + id + " = " + clazz.getName());
            converterTypes.put(id, clazz);
        }
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.ConverterManager#addConverter(java.lang.String, java.lang.String, java.util.Map)
     */
    public void addConverter(String match, String type, Map<String, String> params) throws IllegalArgumentException, InstantiationException, IllegalAccessException
    {
        Class<?> clazz = converterTypes.get(type);
        if (clazz == null)
        {
            log.info("Probably not an issue: " + match + " is not available so the " + type + " converter will not load. This is only an problem if you wanted to use it.");
            return;
        }

        Converter converter = (Converter) clazz.newInstance();
        LocalUtil.setParams(converter, params, ignore);

        // add the converter for the specified match
        addConverter(match, converter);
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.ConverterManager#addConverter(java.lang.String, org.directwebremoting.Converter)
     */
    public void addConverter(String match, Converter converter) throws IllegalArgumentException
    {
        if (LocalUtil.hasText(match) && match.contains("*") && !match.startsWith("["))
        {
            boolean recursive = match.endsWith("**");
            String scan = recursive ? match.substring(0, match.length() - 1) : match;

            ClasspathScanner scanner = new ClasspathScanner(scan, recursive);
            for (String clazz : scanner.getClasses())
            {
                // Call ourselves without the wildcard, processed by the else below
                addConverter(clazz, converter);
            }
        }
        else
        {
            // Check that we don't have this one already
            Converter other = converters.get(match);
            if (other != null)
            {
                Loggers.STARTUP.warn("Clash of converters for " + match + ". Using " + converter.getClass().getName() + " in place of " + other.getClass().getName());
            }

            Loggers.STARTUP.debug("- adding converter: " + converter.getClass().getSimpleName() + " for " + match);

            converter.setConverterManager(this);

            if (converter instanceof NamedConverter)
            {
                try
                {
                    NamedConverter namedConverter = (NamedConverter) converter;

                    // Set up stuff for mapped JavaScript class (if any)
                    if (LocalUtil.hasLength(namedConverter.getJavascript()))
                    {
                        Class<?> javaClass = Class.forName(match);
                        namedConverter.setJavascript(inferClassName(match, namedConverter.getJavascript()));
                        namedConverter.setInstanceType(javaClass);

                        // Set up stuff for mapped JavaScript superclass (if not already assigned)
                        if (!LocalUtil.hasLength(namedConverter.getJavascriptSuperClass()))
                        {
                            // (Here we depend on that the match string is a non-wildcarded
                            // Java class name and the relevant match strings in the
                            // converters collection are also non-wildcarded Java
                            // class/interface names)

                            // First check if any of our super-classes are mapped
                            Class<?> javaSuperClass = javaClass.getSuperclass();
                            while(javaSuperClass != null)
                            {
                                String jsSuperClassName = getMappedJavaScriptClassName(javaSuperClass);
                                if (jsSuperClassName != null)
                                {
                                    namedConverter.setJavascriptSuperClass(jsSuperClassName);
                                    break;
                                }

                                // Continue with next class
                                javaSuperClass = javaSuperClass.getSuperclass();
                            }

                            // If we still have no superclass then continue trying with interfaces
                            if (!LocalUtil.hasLength(namedConverter.getJavascriptSuperClass()))
                            {
                                Class<?> checkInterfacesOnClass = javaClass;
                                while(checkInterfacesOnClass != null)
                                {
                                    String jsSuperClassName = findMappedJavaScriptClassNameForAnyInterface(checkInterfacesOnClass);
                                    if (jsSuperClassName != null)
                                    {
                                        namedConverter.setJavascriptSuperClass(jsSuperClassName);
                                        break;
                                    }

                                    // Continue with next class
                                    checkInterfacesOnClass = checkInterfacesOnClass.getSuperclass();
                                }
                            }
                        }
                    }
                }
                catch (Exception cne)
                {
                    Loggers.STARTUP.warn("Could not load class [" + match + "]. Conversion will try to work upon other runtime information");
                }
            }

            converters.put(match, converter);
        }
    }

    /**
     * Expands the Javascript wildcard (if any).
     * @param match the java class
     * @param jsClassName the javascript attribute of a converter
     * @return a string that does not contain * or null
     */
    protected String inferClassName(String match, String jsClassName)
    {
        String className = jsClassName;
        if (jsClassName != null)
        {
            if ("*".equals(jsClassName))
            {
                className = match.substring(match.lastIndexOf('.') + 1);
            }
            else if ("**".equals(jsClassName))
            {
                className = match;
            }
            else if (jsClassName.indexOf("*") > 0)
            {
                className = jsClassName.replace("*", match.substring(match.lastIndexOf('.') + 1));
            }

            if (!className.equals(jsClassName) && log.isDebugEnabled())
            {
                Loggers.STARTUP.debug("- expanded javascript [" + jsClassName + "] to [" + className + "] for " + match);
            }
        }

        return className;
    }

    /**
     * Convenience method to find a class's mapped JavaScript class name IF
     * the Java class is being converted and IF there is a mapped class name.
     * @param javaClass the java class
     * @return a non-empty classname, or null if not found
     */
    protected String getMappedJavaScriptClassName(Class<?> javaClass)
    {
        Converter conv = converters.get(javaClass.getName());
        if (conv != null)
        {
            // Check if mapped
            if (conv instanceof NamedConverter)
            {
                NamedConverter namedConv = (NamedConverter) conv;
                if (LocalUtil.hasLength(namedConv.getJavascript()))
                {
                    return namedConv.getJavascript();
                }
            }
        }
        return null;
    }

    /**
     * Recurse a class's interface graph and return the first found mapped
     * JavaScript class name.
     * @param checkInterfacesOnClass the java class
     * @return a non-empty classname, or null if not found
     */
    protected String findMappedJavaScriptClassNameForAnyInterface(Class<?> checkInterfacesOnClass)
    {
        Class<?>[] interfaces = checkInterfacesOnClass.getInterfaces();
        for (Class<?> intfc : interfaces)
        {
            // Check if this interface is mapped
            String jsClassName = getMappedJavaScriptClassName(intfc);
            if (jsClassName != null)
            {
                return jsClassName;
            }

            // Otherwise recursively try with the interfaces we are extending
            jsClassName = findMappedJavaScriptClassNameForAnyInterface(intfc);
            if (jsClassName != null)
            {
                return jsClassName;
            }
        }

        return null;
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.ConverterManager#getConverterMatchStrings()
     */
    public Collection<String> getConverterMatchStrings()
    {
        return Collections.unmodifiableSet(converters.keySet());
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.ConverterManager#getConverterByMatchString(java.lang.String)
     */
    public Converter getConverterByMatchString(String match)
    {
        return converters.get(match);
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.ConverterManager#isConvertable(java.lang.Class)
     */
    public boolean isConvertable(Class<?> paramType)
    {
        return getConverter(paramType) != null;
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.extend.ConverterManager#getClientDeclaredType(org.directwebremoting.extend.InboundVariable)
     */
    public Class<?> getClientDeclaredType(InboundVariable data)
    {
        if (data == null)
        {
            return null;
        }

        String objectName = data.getNamedObjectType();
        if (objectName != null)
        {
            NamedConverter converter = getNamedConverter(Object.class, objectName);
            if (converter != null)
            {
                return converter.getInstanceType();
            }
            else
            {
                return null;
            }
        }

        // TODO: we do know the type that the client sent - we might be able
        // to do something better here.
        return null;
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.ConverterManager#convertInbound(java.lang.Class, org.directwebremoting.InboundVariable, org.directwebremoting.InboundContext, org.directwebremoting.TypeHintContext)
     */
    @SuppressWarnings("unchecked")
    public <T> T convertInbound(Class<T> paramType, InboundVariable data, Property thc) throws ConversionException
    {
        InboundContext context = data.getContext();

        Object converted = context.getConverted(data, paramType);
        if (converted == null)
        {
            Converter converter = null;

            // Was the inbound variable marshalled as an Object in the client
            // (could mean that this is an instance of one of our generated
            // JavaScript classes)
            String type = data.getNamedObjectType();
            if (type != null)
            {
                converter = getNamedConverter(paramType, type);
            }

            // Fall back to the standard way of locating a converter if we
            // didn't find anything above
            if (converter == null)
            {
                converter = getConverter(paramType);
            }

            if (converter == null)
            {
                log.error("Missing converter. Context of conversion: " + thc);
                throw new ConversionException(paramType, "No converter found inbound for '" + paramType.getName() + "'");
            }

            context.pushContext(thc);
            converted = converter.convertInbound(paramType, data);
            context.popContext();
        }

        return (T) converted;
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.extend.ConverterManager#convertInbound(java.lang.Class, org.directwebremoting.io.RawData)
     */
    public <T> T convertInbound(Class<T> paramType, RawData rawData) throws ConversionException
    {
        RealRawData realRawData = (RealRawData) rawData;
        InboundVariable inboundVariable = realRawData.getInboundVariable();
        return convertInbound(paramType, inboundVariable, null);
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.ConverterManager#convertOutbound(java.lang.Object, org.directwebremoting.OutboundContext)
     */
    public OutboundVariable convertOutbound(Object data, OutboundContext converted) throws ConversionException
    {
        if (data == null)
        {
            return new NonNestedOutboundVariable("null");
        }

        // Check to see if we have done this one already
        OutboundVariable ov = converted.get(data);
        if (ov != null)
        {
            // So the object as been converted already, we just need to refer to it.
            return ov.getReferenceVariable();
        }

        // So we will have to do the conversion
        Converter converter = getConverter(data);
        if (converter == null)
        {
            String message = "No converter found for '" + data.getClass().getName() + "'";
            log.error(message);
            return new ErrorOutboundVariable(message);
        }

        return converter.convertOutbound(data, converted);
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.extend.ConverterManager#setOverrideProperty(org.directwebremoting.extend.Property, org.directwebremoting.extend.Property)
     */
    public void setOverrideProperty(Property original, Property replacement)
    {
        propertyOverrideMap.put(original, replacement);
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.extend.ConverterManager#checkOverride(org.directwebremoting.extend.Property)
     */
    public Property checkOverride(Property property)
    {
        Property override = propertyOverrideMap.get(property);
        if (override != null)
        {
            return override;
        }
        else
        {
            return property;
        }
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.ConverterManager#setConverters(java.util.Map)
     */
    public void setConverters(Map<String, Converter> converters)
    {
        this.converters = converters;
    }

    /**
     * Like <code>getConverter(object.getClass());</code> except that since the
     * object can be null we check for that fist and then do a lookup against
     * the <code>Void.TYPE</code> converter
     * @param object The object to find a converter for
     * @return The converter for the given type
     */
    private Converter getConverter(Object object)
    {
        if (object == null)
        {
            return getConverter(Void.TYPE);
        }

        return getConverter(object.getClass());
    }

    /**
     * When we are using typed Javascript names we sometimes want to get a
     * specially named converter
     * @param paramType The class that we are converting to
     * @param javascriptClassName The type name as passed in from the client
     * @return The Converter that matches this request (if any)
     * @throws ConversionException IF marshalling fails
     */
    protected NamedConverter getNamedConverter(Class<?> paramType, String javascriptClassName) throws ConversionException
    {
        // Locate a converter for this JavaScript classname
        for (Map.Entry<String, Converter> entry : converters.entrySet())
        {
            String match = entry.getKey();
            Converter conv = entry.getValue();

            // JavaScript mapping is only applicable for compound converters
            if (conv instanceof NamedConverter)
            {
                NamedConverter boConv = (NamedConverter) conv;
                if (boConv.getJavascript() != null && boConv.getJavascript().equals(javascriptClassName))
                {
                    // We found a potential converter! But is the converter's
                    // Java class compatible with the parameter type?
                    try
                    {
                        Class<?> inboundClass = LocalUtil.classForName(match);
                        if (paramType.isAssignableFrom(inboundClass))
                        {
                            // Hack: We also want to make sure that the
                            // converter creates its object based on the inbound
                            // class instead of the parameter type, and we have
                            // to use the other reference for this:
                            boConv.setInstanceType(inboundClass);
                            return boConv;
                        }
                    }
                    catch (ClassNotFoundException ex)
                    {
                        throw new ConversionException(paramType, ex);
                    }
                }
            }
        }

        return null;
    }

    /**
     * @param paramType The type to find a converter for
     * @return The converter for the given type, or null if one can't be found
     */
    private Converter getConverter(Class<?> paramType)
    {
        // Can we find a converter assignable to paramType in the HashMap?
        Converter converter = getConverterAssignableFrom(paramType);
        if (converter != null)
        {
            return converter;
        }

        String lookup = paramType.getName();

        // Before we start trying for a match on package parts we check for
        // dynamic proxies
        if (lookup.startsWith("$Proxy"))
        {
            converter = converters.get("$Proxy*");
            if (converter != null)
            {
                return converter;
            }
        }

        while (true)
        {
            // Can we find a converter using wildcards?
            converter = converters.get(lookup + ".*");
            if (converter != null)
            {
                return converter;
            }

            // Arrays can have wildcards like [L* so we don't require a '.'
            converter = converters.get(lookup + '*');
            if (converter != null)
            {
                return converter;
            }

            // Give up if the name is now empty
            if (lookup.length() == 0)
            {
                break;
            }

            // Strip of the component after the last .
            int lastdot = lookup.lastIndexOf('.');
            if (lastdot != -1)
            {
                lookup = lookup.substring(0, lastdot);
            }
            else
            {
                int arrayMarkers = 0;
                while (lookup.charAt(arrayMarkers) == '[')
                {
                    arrayMarkers++;
                }

                if (arrayMarkers == 0)
                {
                    // so we are out of dots and out of array markers
                    // bail out.
                    break;
                }

                // We want to keep the type marker too
                lookup = lookup.substring(arrayMarkers - 1, arrayMarkers + 1);

                // Now can we find it?
                converter = converters.get(lookup);
                if (converter != null)
                {
                    return converter;
                }
            }
        }

        return null;
    }

    /**
     * @param paramType The type to find a converter for
     * @return The converter assignable for the given type, or null if one can't be found
     */
    private Converter getConverterAssignableFrom(Class<?> paramType)
    {
        if (paramType == null)
        {
            return null;
        }

        String lookup = paramType.getName();

        // Can we find the converter for paramType in the converters HashMap?
        Converter converter = converters.get(lookup);
        if (converter != null)
        {
            return converter;
        }

        // Lookup all of the interfaces of this class for a match
        for (Class<?> anInterface : paramType.getInterfaces())
        {
            converter = getConverterAssignableFrom(anInterface);
            if (converter != null)
            {
                converters.put(lookup, converter);
                return converter;
            }
        }

        // Let's search it in paramType superClass
        converter = getConverterAssignableFrom(paramType.getSuperclass());
        if (converter != null)
        {
            converters.put(lookup, converter);
        }

        return converter;
    }

    /**
     * Where we store real type information behind generic types
     */
    protected Map<Property, Property> propertyOverrideMap = new HashMap<Property, Property>();

    /**
     * The list of the available converters
     */
    protected Map<String, Class<? extends Converter>> converterTypes = new HashMap<String, Class<? extends Converter>>();

    /**
     * The list of the configured converters
     */
    protected Map<String, Converter> converters = new HashMap<String, Converter>();

    /**
     * The properties that we don't warn about if they don't exist.
     * @see DefaultConverterManager#addConverter(String, String, Map)
     */
    private static final List<String> ignore = Arrays.asList("converter", "match");

    /**
     * The log stream
     */
    private static final Log log = LogFactory.getLog(DefaultConverterManager.class);
}
