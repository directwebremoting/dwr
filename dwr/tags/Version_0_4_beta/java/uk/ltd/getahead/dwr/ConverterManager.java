package uk.ltd.getahead.dwr;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import uk.ltd.getahead.dwr.util.LocalUtil;

/**
 * A type to manage the converter types and the instansiated class name matches.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public final class ConverterManager
{
    /**
     * Default ctor
     */
    public ConverterManager()
    {
    }

    /**
     * Add a new converter type
     * @param id The name of the converter type
     * @param clazz The class to do the conversion
     */
    public void addConverterType(String id, Class clazz)
    {
        if (!Converter.class.isAssignableFrom(clazz))
        {
            throw new IllegalArgumentException(Messages.getString("ConverterManager.ConverterNotAssignable", clazz, Converter.class.getName())); //$NON-NLS-1$
        }

        converterTypes.put(id, clazz);
    }

    /**
     * Add a new converter
     * @param match The class name(s) to match
     * @param type The name of the converter type
     * @throws InstantiationException If reflection based creation fails
     * @throws IllegalAccessException If reflection based creation fails
     * @throws IllegalArgumentException If we have a duplicate name
     */
    public void addConverter(String match, String type) throws IllegalArgumentException, InstantiationException, IllegalAccessException
    {
        Class clazz = (Class) converterTypes.get(type);
        if (clazz == null)
        {
            throw new IllegalArgumentException(Messages.getString("ConverterManager.Converterunknown", type)); //$NON-NLS-1$
        }

        Converter converter = (Converter) clazz.newInstance();
        converter.init(this);

        // Check that we don't have this one already
        Converter other = (Converter) converters.get(match);
        if (other != null)
        {
            throw new IllegalArgumentException(Messages.getString("ConverterManager.DuplicateMatches", match)); //$NON-NLS-1$
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
        try
        {
            Converter converter = getConverter(paramType);
            return converter != null;
        }
        catch (ConversionException ex)
        {
            return false;
        }
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
        return converter.convertInbound(paramType, iv, inctx);
    }

    /**
     * Convert an object into a Javavscript representation of the same
     * This method is for use by top level objects i.e. DWRServlet
     * @param object The object to convert
     * @return A Javascript string version of the object
     * @throws ConversionException If the conversion failed for some reason
     */
    public OutboundVariable convertOutbound(Object object) throws ConversionException
    {
        return convertOutbound(object, new OutboundContext());
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
            throw new ConversionException(Messages.getString("ConverterManager.MissingConverter", object.getClass().getName())); //$NON-NLS-1$
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
     * @throws ConversionException If the converter can not be found
     */
    private Converter getConverter(Object object) throws ConversionException
    {
        if (object == null)
        {
            return getConverter(Void.TYPE);
        }

        return getConverter(object.getClass());
    }

    /**
     * @param paramType The type to find a converter for
     * @return The converter for the given type
     * @throws ConversionException If the converter can not be found
     */
    private Converter getConverter(Class paramType) throws ConversionException
    {
        String lookup = paramType.getName();

        // We first check for exact matches using instanceof
        for (Iterator it = converters.keySet().iterator(); it.hasNext();)
        {
            String name = (String) it.next();
            try
            {
                Class clazz = Class.forName(name);

                if (LocalUtil.isEquivalent(clazz, paramType) || clazz.isAssignableFrom(paramType))
                {
                    return (Converter) converters.get(name);
                }
            }
            catch (Exception ex)
            {
                // Do nothing, having * in the classname is legitimate
            }
        }

        while (true)
        {
            // Can we find a converter using wildcards?
            Converter converter = (Converter) converters.get(lookup + ".*"); //$NON-NLS-1$
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

        throw new ConversionException(Messages.getString("ConverterManager.MissingConverter", paramType.getName())); //$NON-NLS-1$
    }

    /**
     * The list of the available converters
     */
    private Map converterTypes = new HashMap();

    /**
     * The list of the configured converters
     */
    private Map converters = new HashMap();
}
