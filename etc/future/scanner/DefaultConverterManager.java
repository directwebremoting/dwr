package org.directwebremoting.impl;

import java.lang.reflect.Method;
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
import org.directwebremoting.extend.RealRawData;
import org.directwebremoting.extend.TypeHintContext;
import org.directwebremoting.io.RawData;
import org.directwebremoting.util.LocalUtil;

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
            log.error("Illegal identifier: '" + id + "'");
            return;
        }

        Class<? extends Converter> clazz = LocalUtil.classForName(id, className, Converter.class);
        if (clazz != null)
        {
            log.debug("- adding converter type: " + id + " = " + clazz.getName());
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
        // Check that we don't have this one already
        Converter other = converters.get(match);
        if (other != null)
        {
            log.warn("Clash of converters for " + match + ". Using " + converter.getClass().getName() + " in place of " + other.getClass().getName());
        }

        log.debug("- adding converter: " + converter.getClass().getSimpleName() + " for " + match);

        converter.setConverterManager(this);
        converters.put(match, converter);
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
     * @see org.directwebremoting.ConverterManager#convertInbound(InboundVariable)
     */
    public Object convertInbound(InboundVariable data)
    {
        Converter converter = null;
        String type = data.getNamedObjectType();

        if (LocalUtil.hasText(type))
        {
            for (Map.Entry<String, Converter> entry : converters.entrySet())
            {
                String match = entry.getKey();
                Converter conv = entry.getValue();
                if (conv instanceof NamedConverter)
                {
                    NamedConverter boConv = (NamedConverter) conv;
                    String javascript = boConv.getJavascript();
                    boolean valid = false;
                    if (javascript != null)
                    {
                        valid = javascript.equals(type);
                        if (!valid)
                        {
                            if (javascript.indexOf("*") >= 0)
                            {
                                valid = true;
                                match = match.replace("*", type);
                            }
                        }
                    }

                    if (valid)
                    {
                        try
                        {
                            Class<?> inboundClass = LocalUtil.classForName(match);
                            boConv.setInstanceType(inboundClass);
                            converter = boConv;
                        }
                        catch (Exception ex)
                        {
                            // Skip this converter
                        }
                    }
                }
            }
        }
        else if ("Array".equals(data.getType()))
        {
            if (!"[]".equals(data.getValue()))
            {
                // TODO: eh?
            }
        }

        if (converter == null)
        {
            return null;
        }

        return converter.convertInbound(null, data);
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.ConverterManager#convertInbound(java.lang.Class, org.directwebremoting.InboundVariable, org.directwebremoting.InboundContext, org.directwebremoting.TypeHintContext)
     */
    @SuppressWarnings("unchecked")
    public <T> T convertInbound(Class<T> paramType, InboundVariable data, TypeHintContext thc) throws ConversionException
    {
        if (data == null)
        {
            return null;
        }

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

            // We only think about doing a null conversion ourselves once we are
            // sure that there is a converter available. This prevents hackers
            // from passing null to things they are not allowed to convert
            if (data.isNull())
            {
                return null;
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
        TypeHintContext typeHintContext = new TypeHintContext(this, null, 0);

        return convertInbound(paramType, inboundVariable, typeHintContext);
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
     * @see org.directwebremoting.ConverterManager#setExtraTypeInfo(org.directwebremoting.TypeHintContext, java.lang.Class)
     */
    public void setExtraTypeInfo(TypeHintContext thc, Class<?> type)
    {
        extraTypeInfoMap.put(thc, type);
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.ConverterManager#getExtraTypeInfo(org.directwebremoting.TypeHintContext)
     */
    public Class<?> getExtraTypeInfo(TypeHintContext thc)
    {
        return extraTypeInfoMap.get(thc);
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.extend.ConverterManager#setTypeInfo(java.lang.reflect.Method, int, int, java.lang.Class)
     */
    public void setTypeInfo(Method method, int i, int j, Class<?> clazz)
    {
        TypeHintContext thc = new TypeHintContext(this, method, i).createChildContext(j);
        this.setExtraTypeInfo(thc, clazz);
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
    protected Converter getNamedConverter(Class<?> paramType, String javascriptClassName) throws ConversionException
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
    protected Map<TypeHintContext, Class<?>> extraTypeInfoMap = new HashMap<TypeHintContext, Class<?>>();

    /**
     * The list of the available converters
     */
    protected Map<String, Class<?>> converterTypes = new HashMap<String, Class<?>>();

    /**
     * The list of the configured converters
     */
    protected Map<String, Converter> converters = new HashMap<String, Converter>();

    /**
     * The properties that we don't warn about if they don't exist.
     * @see DefaultConverterManager#addConverter(String, String, Map)
     */
    private static List<String> ignore = Arrays.asList("converter", "match");

    /**
     * The log stream
     */
    private static final Log log = LogFactory.getLog(DefaultConverterManager.class);
}
