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
package uk.ltd.getahead.dwr.impl;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import uk.ltd.getahead.dwr.ConversionException;
import uk.ltd.getahead.dwr.Converter;
import uk.ltd.getahead.dwr.ConverterManager;
import uk.ltd.getahead.dwr.InboundContext;
import uk.ltd.getahead.dwr.InboundVariable;
import uk.ltd.getahead.dwr.Messages;
import uk.ltd.getahead.dwr.OutboundContext;
import uk.ltd.getahead.dwr.OutboundVariable;
import uk.ltd.getahead.dwr.TypeHintContext;
import uk.ltd.getahead.dwr.util.LocalUtil;
import uk.ltd.getahead.dwr.util.Logger;

/**
 * A class to manage the converter types and the instansiated class name matches.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class DefaultConverterManager implements ConverterManager
{
    /* (non-Javadoc)
     * @see uk.ltd.getahead.dwr.ConverterManager#addConverterType(java.lang.String, java.lang.Class)
     */
    public void addConverterType(String id, Class clazz)
    {
        if (!Converter.class.isAssignableFrom(clazz))
        {
            throw new IllegalArgumentException(Messages.getString("DefaultConverterManager.ConverterNotAssignable", clazz, Converter.class.getName())); //$NON-NLS-1$
        }

        try
        {
            clazz.newInstance();
        }
        catch (InstantiationException ex)
        {
            throw new IllegalArgumentException(Messages.getString("DefaultConverterManager.ConverterNotInstantiatable", clazz.getName(), ex.toString())); //$NON-NLS-1$
        }
        catch (IllegalAccessException ex)
        {
            throw new IllegalArgumentException(Messages.getString("DefaultConverterManager.ConverterNotAccessable", clazz.getName(), ex.toString())); //$NON-NLS-1$
        }

        converterTypes.put(id, clazz);
    }

    /* (non-Javadoc)
     * @see uk.ltd.getahead.dwr.ConverterManager#addConverter(java.lang.String, java.lang.String, java.util.Map)
     */
    public void addConverter(String match, String type, Map params) throws IllegalArgumentException, InstantiationException, IllegalAccessException
    {
        Class clazz = (Class) converterTypes.get(type);
        if (clazz == null)
        {
            log.info("Can't marshall " + match + " because converter '" + type + "' is not available. The converter definition may be missing, or required element may be missing from the CLASSPATH"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            return;
        }

        Converter converter = (Converter) clazz.newInstance();
        converter.setConverterManager(this);

        // Initialize the creator with the parameters that we know of.
        for (Iterator it = params.entrySet().iterator(); it.hasNext();)
        {
            Map.Entry entry = (Entry) it.next();
            String key = (String) entry.getKey();
            Object value = entry.getValue();

            try
            {
                LocalUtil.setProperty(converter, key, value);
            }
            catch (NoSuchMethodException ex)
            {
                // No-one has a setCreator method, so don't warn about it
                if (!key.equals("converter") && !key.equals("match")) //$NON-NLS-1$ //$NON-NLS-2$
                {
                    log.debug("No property '" + key + "' on " + converter.getClass().getName()); //$NON-NLS-1$ //$NON-NLS-2$
                }
            }
            catch (InvocationTargetException ex)
            {
                log.warn("Error setting " + key + "=" + value + " on " + converter.getClass().getName(), ex.getTargetException()); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            }
            catch (Exception ex)
            {
                log.warn("Error setting " + key + "=" + value + " on " + converter.getClass().getName(), ex); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            }
        }

        // add the converter for the specified match
        addConverter(match, converter);
    }

    /* (non-Javadoc)
     * @see uk.ltd.getahead.dwr.ConverterManager#addConverter(java.lang.String, uk.ltd.getahead.dwr.Converter)
     */
    public void addConverter(String match, Converter converter) throws IllegalArgumentException
    {
        // Check that we don't have this one already
        Converter other = (Converter) converters.get(match);
        if (other != null)
        {
            log.warn("Clash of converters for " + match + ". Using " + converter.getClass().getName() + " in place of " + other.getClass().getName()); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        }

        converters.put(match, converter);
    }

    /* (non-Javadoc)
     * @see uk.ltd.getahead.dwr.ConverterManager#isConvertable(java.lang.Class)
     */
    public boolean isConvertable(Class paramType)
    {
        return getConverter(paramType) != null;
    }

    /* (non-Javadoc)
     * @see uk.ltd.getahead.dwr.ConverterManager#convertInbound(java.lang.Class, uk.ltd.getahead.dwr.InboundVariable, uk.ltd.getahead.dwr.InboundContext)
     */
    public Object convertInbound(Class paramType, InboundVariable iv, InboundContext inctx, TypeHintContext incc) throws ConversionException
    {
        Object converted = inctx.getConverted(iv);
        if (converted == null)
        {
            Converter converter = getConverter(paramType);
            if (converter == null)
            {
                throw new ConversionException(Messages.getString("DefaultConverterManager.MissingConverter", paramType.getName())); //$NON-NLS-1$
            }

            // We only think about doing a null conversion ourselves once we are
            // sure that there is a converter available. This prevents hackers
            // from passing null to things they are not allowed to convert
            if (iv.isNull())
            {
                return null;
            }

            inctx.pushContext(incc);
            converted = converter.convertInbound(paramType, iv, inctx);
            inctx.popContext();
        }

        return converted;
    }

    /* (non-Javadoc)
     * @see uk.ltd.getahead.dwr.ConverterManager#convertOutbound(java.lang.Object, uk.ltd.getahead.dwr.OutboundContext)
     */
    public OutboundVariable convertOutbound(Object object, OutboundContext converted) throws ConversionException
    {
        if (object == null)
        {
            String varName = converted.getNextVariableName();
            return new OutboundVariable("var " + varName + "=null;", varName); //$NON-NLS-1$ //$NON-NLS-2$
        }

        // Check to see if we have done this one already
        OutboundVariable ov = converted.get(object);
        if (ov != null)
        {
            // So the object as been converted already, we just need to refer to it.
            return new OutboundVariable("", ov.getAssignCode()); //$NON-NLS-1$
        }

        // So we will have to create one for ourselves
        ov = new OutboundVariable();
        String varName = converted.getNextVariableName();
        ov.setAssignCode(varName);

        // Save this for another time so we don't recurse into it
        converted.put(object, ov);

        Converter converter = getConverter(object);
        if (converter == null)
        {
            log.error(Messages.getString("DefaultConverterManager.MissingConverter", object.getClass().getName())); //$NON-NLS-1$
            return new OutboundVariable("var " + varName + "=null;", varName); //$NON-NLS-1$ //$NON-NLS-2$
        }

        ov.setInitCode(converter.convertOutbound(object, ov.getAssignCode(), converted));

        return ov;
    }

    /* (non-Javadoc)
     * @see uk.ltd.getahead.dwr.ConverterManager#setExtraTypeInfo(java.lang.reflect.Method, int, int, java.lang.Class)
     */
    public void setExtraTypeInfo(TypeHintContext thc, Class type)
    {
        extraTypeInfoMap.put(thc, type);
    }

    /* (non-Javadoc)
     * @see uk.ltd.getahead.dwr.ConverterManager#getExtraTypeInfo(TypeHintContext)
     */
    public Class getExtraTypeInfo(TypeHintContext thc)
    {
        Class type = (Class) extraTypeInfoMap.get(thc);
        if (type == null)
        {
            log.warn("Missing type info for " + thc + ". Assuming this is a map with String keys. Please add to <signatures> in dwr.xml"); //$NON-NLS-1$ //$NON-NLS-2$
            type = String.class;

            if (log.isDebugEnabled())
            {
                log.debug("Known extra type info:"); //$NON-NLS-1$
                for (Iterator it = extraTypeInfoMap.entrySet().iterator(); it.hasNext();)
                {
                    Map.Entry entry = (Map.Entry) it.next();
                    Class temp = (Class) entry.getValue();
                    log.debug("  " + entry.getKey() + " = " + temp.getName()); //$NON-NLS-1$ //$NON-NLS-2$
                }
            }
        }
        else
        {
            log.debug("Using extra type info for " + thc + " of " + type); //$NON-NLS-1$ //$NON-NLS-2$
        }

        return type;
    }

    /* (non-Javadoc)
     * @see uk.ltd.getahead.dwr.ConverterManager#setConverters(java.util.Map)
     */
    public void setConverters(Map converters)
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
     * @param paramType The type to find a converter for
     * @return The converter for the given type, or null if one can't be found
     */
    private Converter getConverter(Class paramType)
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
        if (lookup.startsWith("$Proxy")) //$NON-NLS-1$
        {
            converter = (Converter) converters.get("$Proxy*"); //$NON-NLS-1$
            if (converter != null)
            {
                return converter;
            }
        }

        while (true)
        {
            // Can we find a converter using wildcards?
            converter = (Converter) converters.get(lookup + ".*"); //$NON-NLS-1$
            if (converter != null)
            {
                return converter;
            }

            // Arrays can have wildcards like [L* so we don't require a '.'
            converter = (Converter) converters.get(lookup + '*');
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
                converter = (Converter) converters.get(lookup);
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
    private Converter getConverterAssignableFrom(Class paramType)
    {
        if (paramType == null)
        {
            return null;
        }

        String lookup = paramType.getName();

        // Can we find the converter for paramType in the converters HashMap?
        Converter converter = (Converter) converters.get(lookup);
        if (converter != null)
        {
            return converter;
        }

        // Lookup all of the interfaces of this class for a match
        Class[] interfaces = paramType.getInterfaces();
        for (int i = 0; i < interfaces.length; i++)
        {
            converter = getConverterAssignableFrom(interfaces[i]);
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
    private Map extraTypeInfoMap = new HashMap();

    /**
     * The log stream
     */
    private static final Logger log = Logger.getLogger(DefaultConverterManager.class);

    /**
     * The list of the available converters
     */
    private Map converterTypes = new HashMap();

    /**
     * The list of the configured converters
     */
    private Map converters = new HashMap();
}
