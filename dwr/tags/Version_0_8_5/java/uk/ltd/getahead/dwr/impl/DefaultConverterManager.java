package uk.ltd.getahead.dwr.impl;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import uk.ltd.getahead.dwr.ConversionException;
import uk.ltd.getahead.dwr.Converter;
import uk.ltd.getahead.dwr.ConverterManager;
import uk.ltd.getahead.dwr.InboundContext;
import uk.ltd.getahead.dwr.InboundVariable;
import uk.ltd.getahead.dwr.Messages;
import uk.ltd.getahead.dwr.OutboundContext;
import uk.ltd.getahead.dwr.OutboundVariable;
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

        converterTypes.put(id, clazz);
    }

    /* (non-Javadoc)
     * @see uk.ltd.getahead.dwr.ConverterManager#addConverter(java.lang.String, java.lang.String)
     */
    public void addConverter(String match, String type) throws IllegalArgumentException, InstantiationException, IllegalAccessException
    {
        Class clazz = (Class) converterTypes.get(type);
        if (clazz == null)
        {
            log.info("Can't marshall " + match + " because converter '" + type + "' is not available. The converter definition may be missing, or required element may be missing from the CLASSPATH"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            return;
        }

        Converter converter = (Converter) clazz.newInstance();
        converter.setConverterManager(this);

        // Check that we don't have this one already
        Converter other = (Converter) converters.get(match);
        if (other != null)
        {
            throw new IllegalArgumentException(Messages.getString("DefaultConverterManager.DuplicateMatches", match)); //$NON-NLS-1$
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
    public Object convertInbound(Class paramType, InboundVariable iv, InboundContext inctx) throws ConversionException
    {
        Object converted = inctx.getConverted(iv);
        if (converted != null)
        {
            return converted;
        }

        Converter converter = getConverter(paramType);
        if (converter == null)
        {
            throw new ConversionException(Messages.getString("DefaultConverterManager.MissingConverter", paramType.getName())); //$NON-NLS-1$
        }

        if (iv.isNull())
        {
            return null;
        }

        return converter.convertInbound(paramType, iv, inctx);
    }

    /* (non-Javadoc)
     * @see uk.ltd.getahead.dwr.ConverterManager#convertOutbound(java.lang.Object, uk.ltd.getahead.dwr.OutboundContext)
     */
    public OutboundVariable convertOutbound(Object object, OutboundContext converted) throws ConversionException
    {
        if (object == null)
        {
            String varName = converted.getNextVariableName();
            return new OutboundVariable("var " + varName + " = null;", varName); //$NON-NLS-1$ //$NON-NLS-2$
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
            throw new ConversionException(Messages.getString("DefaultConverterManager.MissingConverter", object.getClass().getName())); //$NON-NLS-1$
        }

        ov.setInitCode(converter.convertOutbound(object, ov.getAssignCode(), converted));

        return ov;
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

            // Arrays can have wildcards like [L* so we don't require a .
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
            if (lastdot == -1)
            {
                // Cope with arrays
                if (lookup.charAt(0) == '[')
                {
                    lastdot = 2;
                }
                else
                {
                    // bail if no more dots and not an array
                    break;
                }
            }

            lookup = lookup.substring(0, lastdot);
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

    /* (non-Javadoc)
     * @see uk.ltd.getahead.dwr.ConverterManager#addParameterInfo(java.lang.reflect.Method, int, int, java.lang.Class)
     */
    public void setExtraTypeInfo(Method method, int paramNo, int index, Class type)
    {
        ParamInfoKey key = new ParamInfoKey(method, paramNo, index);
        extraTypeInfoMap.put(key, type);
    }

    /* (non-Javadoc)
     * @see uk.ltd.getahead.dwr.ConverterManager#getExtraTypeInfo(java.lang.reflect.Method, int, int)
     */
    public Class getExtraTypeInfo(Method method, int paramNo, int index)
    {
        ParamInfoKey key = new ParamInfoKey(method, paramNo, index);
        return (Class) extraTypeInfoMap.get(key);
    }

    /**
     * Something to hold the method, paramNo and index together as an object
     * that can be a key in a Map.
     */
    private class ParamInfoKey
    {
        /**
         * Setup this object
         * @param method The method to annotate
         * @param paramNo The number of the parameter to edit (counts from 0)
         * @param index The index of the item between &lt; and &gt;.
         */
        ParamInfoKey(Method method, int paramNo, int index)
        {
            this.method = method;
            this.paramNo = paramNo;
            this.index = index;
        }

        /* (non-Javadoc)
         * @see java.lang.Object#hashCode()
         */
        public int hashCode()
        {
            return method.hashCode() + paramNo + index;
        }

        /* (non-Javadoc)
         * @see java.lang.Object#equals(java.lang.Object)
         */
        public boolean equals(Object obj)
        {
            if (obj == null)
            {
                return false;
            }

            if (!this.getClass().equals(obj.getClass()))
            {
                return false;
            }

            if (obj == this)
            {
                return true;
            }

            ParamInfoKey that = (ParamInfoKey) obj;

            if (!this.method.equals(that.method))
            {
                return false;
            }

            if (this.paramNo != that.paramNo)
            {
                return false;
            }

            if (this.index != that.index)
            {
                return false;
            }

            return true;
        }

        /* (non-Javadoc)
         * @see java.lang.Object#toString()
         */
        public String toString()
        {
            return method.getName() + '[' + paramNo + "]<" + index + '>'; //$NON-NLS-1$
        }

        Method method;
        int paramNo;
        int index;
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
