package uk.ltd.getahead.dwr;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import uk.ltd.getahead.dwr.util.LocalUtil;
import uk.ltd.getahead.dwr.util.Logger;

/**
 * A class to manage the converter types and the instansiated class name matches.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class ConverterManager
{
    /**
     * Add a new converter type
     * @param id The name of the converter type
     * @param clazz The class to do the conversion
     */
    public void addConverterType(String id, Class clazz)
    {
        if (!Converter.class.isAssignableFrom(clazz))
        {
            throw new IllegalArgumentException(Messages.getString("DefaultConverterManager.ConverterNotAssignable", clazz, Converter.class.getName())); //$NON-NLS-1$
        }

        converterTypes.put(id, clazz);
    }

    /**
     * Add a new converter
     * @param match The class name(s) to match
     * @param type The name of the converter type
     * @param params The extra parameters to allow the creator to configure itself
     * @throws InstantiationException If reflection based creation fails
     * @throws IllegalAccessException If reflection based creation fails
     * @throws IllegalArgumentException If we have a duplicate name
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

            try
            {
                LocalUtil.setProperty(converter, key, entry.getValue());
            }
            catch (Exception ex)
            {
                // No-one has a setCreator method, so don't warn about it
                if (!key.equals("converter") && !key.equals("match")) //$NON-NLS-1$ //$NON-NLS-2$
                {
                    log.debug("No property '" + key + "' on class " + converter.getClass().getName()); //$NON-NLS-1$ //$NON-NLS-2$
                }
            }
        }

        // Check that we don't have this one already
        Converter other = (Converter) converters.get(match);
        if (other != null)
        {
            throw new IllegalArgumentException(Messages.getString("DefaultConverterManager.DuplicateMatches", match)); //$NON-NLS-1$
        }

        converters.put(match, converter);
    }

    /**
     * Check if we can coerce the given type
     * @param paramType The type to check
     * @return true iff <code>paramType</code> is coercable
     */
    public boolean isConvertable(Class paramType)
    {
        return getConverter(paramType) != null;
    }

    /**
     * Convert an object from being a string into an object of some type.
     * Designed for use with converters that have a working map passed to them
     * @param paramType The type that you want the object to be
     * @param iv The string version of the object
     * @param inctx The map of data that we are working on
     * @return The coerced object or null if the object could not be coerced
     * @throws ConversionException If the conversion failed for some reason
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

    /**
     * Convert an object into a Javavscript representation of the same.
     * This method is for use by converters wishing to recurse into some object.
     * @param object The object to convert
     * @param converted The list of converted objects so far
     * @return A Javascript string version of the object
     * @throws ConversionException If the conversion failed for some reason
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
     * We don't know enough from a method signature like setUsers(Set s) to be
     * able to cast the inbound data to a set of Users. This method enables us
     * to specify this extra information.
     * @param method The method to annotate
     * @param paramNo The number of the parameter to edit (counts from 0)
     * @param index The index of the item between &lt; and &gt;.
     * @param type The type of the specified parameter. 
     */
    public void setExtraTypeInfo(Method method, int paramNo, int index, Class type)
    {
        ParamInfoKey key = new ParamInfoKey(method, paramNo, index);
        extraTypeInfoMap.put(key, type);
    }

    /**
     * The extra type information that we have learnt about a method parameter.
     * This method will return null if there is nothing extra to know
     * @param method The method to annotate
     * @param paramNo The number of the parameter to edit (counts from 0)
     * @param index The index of the item between &lt; and &gt;.
     * @return A list of types to fill out a generic type
     */
    public Class getExtraTypeInfo(Method method, int paramNo, int index)
    {
        ParamInfoKey key = new ParamInfoKey(method, paramNo, index);
        return (Class) extraTypeInfoMap.get(key);
    }

    /**
     * Sets the converters for this converter manager.
     * @param converters the map of match pattern and their converter instances
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

    /**
     * Something to hold the method, paramNo and index together as an object
     * that can be a key in a Map.
     */
    private static class ParamInfoKey
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

        final Method method;
        final int paramNo;
        final int index;
    }

    /**
     * Where we store real type information behind generic types
     */
    private Map extraTypeInfoMap = new HashMap();

    /**
     * The log stream
     */
    private static final Logger log = Logger.getLogger(ConverterManager.class);

    /**
     * The list of the available converters
     */
    private Map converterTypes = new HashMap();

    /**
     * The list of the configured converters
     */
    private Map converters = new HashMap();
}