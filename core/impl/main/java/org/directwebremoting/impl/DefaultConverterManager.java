package org.directwebremoting.impl;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

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
     *
     * Called only at start-up! It is important that this method only be called at start-up.  Otherwise we
     * will need to modify it so it is thread safe.
     *
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
     *
     * Called only at start-up! It is important that this method only be called at start-up.  Otherwise we
     * will need to modify it so it is thread safe.
     *
     * @see org.directwebremoting.ConverterManager#addConverter(java.lang.String, java.lang.String, java.util.Map)
     */
    public void addConverter(String match, String type, Map<String, String> params) throws IllegalArgumentException, InstantiationException, IllegalAccessException
    {
        Class<?> clazz = converterTypes.get(type);
        if (clazz == null)
        {
            log.debug("Probably not an issue: " + match + " is not available so the " + type + " converter will not load. This is only an problem if you wanted to use it.");
            return;
        }

        Converter converter = (Converter) clazz.newInstance();
        LocalUtil.setParams(converter, params, ignore);

        // add the converter for the specified match
        addConverter(match, converter);
    }

    /* (non-Javadoc)
     *
     * Called only at start-up! It is important that this method only be called at start-up.  Otherwise we
     * will need to modify it so it is thread safe.
     *
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
            Converter other = convertersByMatch.get(match);
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
                    Class<?> javaClass = LocalUtil.classForName(match);
                    setUpClassMapping(namedConverter, javaClass);
                }
                catch (Exception cne)
                {
                    Loggers.STARTUP.warn("Could not load class [" + match + "]. Conversion will try to work upon other runtime information");
                }
            }

            convertersByMatch.put(match, converter);
            converterCache.clear();
        }
    }

    /* (non-Javadoc)
     *
     * Called only at start-up! It is important that this method only be called at start-up.  Otherwise we
     * will need to modify it so it is thread safe.
     *
     * @see org.directwebremoting.extend.ConverterManager#addConverter(java.lang.Class, org.directwebremoting.extend.Converter)
     */
    public void addConverter(Class<?> clazz, Converter converter)
    {
        // Check that we don't have this one already
        Converter other = convertersByClass.get(clazz);
        if (other != null)
        {
            Loggers.STARTUP.warn("Clash of converters for " + clazz.getName() + ". Using " + converter.getClass().getName() + " in place of " + other.getClass().getName());
        }

        Loggers.STARTUP.debug("- adding converter: " + converter.getClass().getSimpleName() + " for " + clazz.getName());

        converter.setConverterManager(this);

        if (converter instanceof NamedConverter)
        {
            NamedConverter namedConverter = (NamedConverter) converter;
            setUpClassMapping(namedConverter, clazz);
        }

        convertersByClass.put(clazz, converter);
        converterCache.clear();
    }

    /**
     * Does all the initializations for converters using class-mapping (set up
     * superclass, expand wildcards, etc)
     * @param namedConverter converter that may use class-mapping
     * @param clazz the java class to convert using class-mapping
     */
    protected void setUpClassMapping(NamedConverter namedConverter, Class<?> clazz)
    {
        // Set up stuff for mapped JavaScript class (if any)
        if (LocalUtil.hasLength(namedConverter.getJavascript()))
        {
            namedConverter.setJavascript(LocalUtil.inferWildcardReplacements(clazz.getName(), namedConverter.getJavascript()));
            namedConverter.setInstanceType(clazz);

            // Set up stuff for mapped JavaScript superclass (if not already assigned)
            if (!LocalUtil.hasLength(namedConverter.getJavascriptSuperClass()))
            {
                // (Here we depend on that the match string is a non-wildcarded
                // Java class name and the relevant match strings in the
                // converters collection are also non-wildcarded Java
                // class/interface names)

                // First check if any of our super-classes are mapped
                Class<?> javaSuperClass = clazz.getSuperclass();
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
                    Class<?> checkInterfacesOnClass = clazz;
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

            convertersByJavascript.put(namedConverter.getJavascript(), namedConverter);
            converterCache.clear();
        }
    }

    /**
     * Convenience method to find a class's mapped JavaScript class name IF
     * the Java class is being converted and IF there is a mapped class name.
     * @param clazz the java class
     * @return a non-empty classname, or null if not found
     */
    protected String getMappedJavaScriptClassName(Class<?> clazz)
    {
        Converter conv = getConverter(clazz);
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
        return Collections.unmodifiableSet(convertersByMatch.keySet());
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.ConverterManager#getConverterByMatchString(java.lang.String)
     */
    public Converter getConverterByMatchString(String match)
    {
        return convertersByMatch.get(match);
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.extend.ConverterManager#getNamedConverterJavaScriptNames()
     */
    public Collection<String> getNamedConverterJavaScriptNames()
    {
        return convertersByJavascript.keySet();
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.ConverterManager#getNamedConverter(java.lang.String)
     */
    public NamedConverter getNamedConverter(String javascriptClassName)
    {
        return convertersByJavascript.get(javascriptClassName);
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.ConverterManager#isConvertable(java.lang.Class)
     */
    public boolean isConvertable(Class<?> clazz)
    {
        return getConverter(clazz) != null;
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
            NamedConverter converter = getNamedConverter(objectName, Object.class);
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
                converter = getNamedConverter(type, paramType);
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
                throw new ConversionException(paramType, "No inbound converter found for property '" + thc.getName() + "' of type '" + paramType.getName() + "'");
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
        if (!converted.isJsonMode())
        {
            OutboundVariable ov = converted.get(data);
            if (ov != null)
            {
                // So the object as been converted already, we just need to refer to it.
                return ov.getReferenceVariable();
            }
        }

        // So we will have to do the conversion
        Converter converter = getConverter(data);
        if (converter == null)
        {
            String message = "No outbound converter found for '" + data.getClass().getName() + "'";
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
     *
     * Called only at start-up! It is important that this method only be called at start-up.  Otherwise we
     * will need to modify it so it is thread safe.
     *
     * @see org.directwebremoting.ConverterManager#setConverters(java.util.Map)
     */
    public void setConverters(Map<String, Converter> converters)
    {
        synchronized (this.convertersByMatch)
        {
            this.convertersByMatch.clear();
            this.convertersByMatch.putAll(converters);
            converterCache.clear();
        }
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
     * @throws ConversionException If marshalling fails
     */
    protected NamedConverter getNamedConverter(String javascriptClassName, Class<?> paramType) throws ConversionException
    {
        NamedConverter namedConv = getNamedConverter(javascriptClassName);
        if (namedConv != null)
        {
            if (paramType.isAssignableFrom(namedConv.getInstanceType()))
            {
                return namedConv;
            }
        }
        return null;
    }

    /**
     * @param clazz The type to find a converter for
     * @return The converter for the given type, or null if one can't be found
     */
    private Converter getConverter(Class<?> clazz)
    {
        // Can we find a converter assignable to clazz in the HashMap?
        Converter converter = getConverterAssignableFrom(clazz);
        if (converter != null)
        {
            return converter;
        }

        String lookup = LocalUtil.originalDwrClassName(clazz.getName());

        // Before we start trying for a match on package parts we check for
        // dynamic proxies
        if (lookup.startsWith("$Proxy"))
        {
            converter = convertersByMatch.get("$Proxy*");
            if (converter != null)
            {
                converterCache.putIfAbsent(clazz, converter);
                return converter;
            }
        }

        while (true)
        {
            // Can we find a converter using wildcards?
            converter = convertersByMatch.get(lookup + ".*");
            if (converter != null)
            {
                converterCache.putIfAbsent(clazz, converter);
                return converter;
            }

            // Arrays can have wildcards like [L* so we don't require a '.'
            converter = convertersByMatch.get(lookup + '*');
            if (converter != null)
            {
                converterCache.putIfAbsent(clazz, converter);
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
                converter = convertersByMatch.get(lookup);
                if (converter != null)
                {
                    converterCache.putIfAbsent(clazz, converter);
                    return converter;
                }
            }
        }

        return null;
    }

    /**
     * @param clazz The type to find a converter for
     * @return The converter assignable for the given type, or null if one can't be found
     */
    private Converter getConverterAssignableFrom(Class<?> clazz)
    {
        if (clazz == null)
        {
            return null;
        }

        // Can we find a suitable converter already in the converter cache?
        Converter converter = converterCache.get(clazz);
        if (converter != null)
        {
            return converter;
        }

        try {
            // Can we find a matching converter supplied by class?
            converter = convertersByClass.get(clazz);
            if (converter != null)
            {
                return converter;
            }

            // Can we find a matching converter supplied by class name?
            converter = convertersByMatch.get(LocalUtil.originalDwrClassName(clazz.getName()));
            if (converter != null)
            {
                return converter;
            }

            // Lookup all of the interfaces of this class for a match
            for (Class<?> anInterface : clazz.getInterfaces())
            {
                converter = getConverterAssignableFrom(anInterface);
                if (converter != null)
                {
                    return converter;
                }
            }

            // Let's search it in superClass
            converter = getConverterAssignableFrom(clazz.getSuperclass());
            if (converter != null)
            {
                return converter;
            }

            return null;
        }
        finally {
            if (converter != null)
            {
                converterCache.putIfAbsent(clazz, converter);
            }
        }
    }

    /**
     * Where we store real type information behind generic types
     */
    protected Map<Property, Property> propertyOverrideMap = new HashMap<Property, Property>();

    /**
     * The list of the registered converter types
     */
    protected Map<String, Class<? extends Converter>> converterTypes = new HashMap<String, Class<? extends Converter>>();

    /**
     * The list of converters registered by class name match string
     */
    protected Map<String, Converter> convertersByMatch = new HashMap<String, Converter>();

    /**
     * The list of converters registered by class
     */
    protected Map<Class<?>, Converter> convertersByClass = new HashMap<Class<?>, Converter>();

    /**
     * The converter cache used during conversion (gets populated based on registered converters)
     */
    protected final Map<String, NamedConverter> convertersByJavascript = new HashMap<String, NamedConverter>();

    /**
     * The converter cache used during conversion (gets populated based on registered converters)
     */
    protected final ConcurrentMap<Class<?>, Converter> converterCache = new ConcurrentHashMap<Class<?>, Converter>();

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
