package uk.ltd.getahead.dwr;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import uk.ltd.getahead.dwr.util.LocalUtil;

/**
 * A type to manage the converter types and the instansiated class name matches.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class ConverterManager
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
            throw new IllegalArgumentException("Class " + clazz + " does not implement " + Converter.class.getName());
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
            throw new IllegalArgumentException("Unknown converter called: "+type);
        }

        Converter converter = (Converter) clazz.newInstance();
        converter.init(this);

        // Check that we don't have this one already
        Converter other = (Converter) converters.get(match);
        if (other != null)
        {
            throw new IllegalArgumentException("Match '" + match + "' is used by 2 converters.");
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
     * Convert an object into a Javavscript representation of the same
     * This method is for use by top level objects i.e. DWRServlet
     * @param object The object to convert
     * @return A Javascript string version of the object
     * @throws ConversionException If the conversion failed for some reason
     */
    public ScriptSetup convertFrom(Object object) throws ConversionException
    {
        Map converted = createConversionContext();
        return convertFrom(object, converted);
    }

    /**
     * Convert an object into a Javavscript representation of the same.
     * This method is for use by converters wishing to recurse into some object.
     * @param object The object to convert
     * @param converted The list of converted objects so far
     * @return A Javascript string version of the object
     * @throws ConversionException If the conversion failed for some reason
     */
    public ScriptSetup convertFrom(Object object, Map converted) throws ConversionException
    {
        // Check to see if we have done this one already
        ScriptSetup ss = (ScriptSetup) converted.get(object);
        if (ss != null)
        {
            // So the object as been converted already, we just need to refer to it.
            return new ScriptSetup("", ss.assignCode);
        }

        // So we will have to create one for ourselves
        ss = new ScriptSetup();
        String varName = getNextVariableName(converted);
        ss.assignCode = varName;

        // Save this for another time so we don't recurse into it
        converted.put(object, ss);

        Converter converter = getConverter(object);
        if (converter == null)
        {
            throw new ConversionException("No converter found for " + object.getClass().getName());
        }

        ss.initCode = converter.convertFrom(object, ss.assignCode, converted);

        return ss;
    }

    /**
     * Convert an object from being a string into an object of some type.
     * Designed for use 'from the outside' i.e. not by a Converter that is part
     * of the conversion process.
     * @param paramType The type that you want the object to be
     * @param data The string version of the object
     * @return The coerced object or null if the object could not be coerced
     * @throws ConversionException If the conversion failed for some reason
     */
    public Object convertTo(Class paramType, ConversionData data) throws ConversionException
    {
        return convertTo(paramType, data, new HashMap());
    }

    /**
     * Convert an object from being a string into an object of some type.
     * Designed for use with converters that have a working map passed to them
     * @param paramType The type that you want the object to be
     * @param data The string version of the object
     * @param working The map of data that we are working on
     * @return The coerced object or null if the object could not be coerced
     * @throws ConversionException If the conversion failed for some reason
     */
    public Object convertTo(Class paramType, ConversionData data, Map working) throws ConversionException
    {
        Object done = working.get(data);
        if (done != null)
        {
            return done;
        }

        Converter converter = getConverter(paramType);
        return converter.convertTo(paramType, data, working);
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
        for (Iterator it = converters.keySet().iterator(); it.hasNext(); )
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
            Converter converter = (Converter) converters.get(lookup+".*");
            if (converter != null)
            {
                return converter;
            }

            // Arrays can have wildcards like [L* so we don't require a .
            converter = (Converter) converters.get(lookup+"*");
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
                if (lookup.startsWith("["))
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

        throw new ConversionException("No converter found for " + paramType.getName());
    }

    /**
     * Create a ConversionContext which stores context information about a set
     * of conversions from Java to Javascript.
     * @return A new conversion context
     */
    private Map createConversionContext()
    {
        Map converted = new HashMap();
        converted.put(VAR_COUNT_KEY, new Integer(0));

        return converted;
    }

    /**
     * Create a new variable name to keep everything we declare separate
     * @param converted The map of converted variables so far.
     * @return A new unique variable name
     */
    private String getNextVariableName(Map converted)
    {
        Integer varCount = (Integer) converted.get(VAR_COUNT_KEY);
        String varName = "s" + varCount;
        converted.put(VAR_COUNT_KEY, new Integer(varCount.intValue() + 1));

        return varName;
    }

    /**
     * The list of the available converters
     */
    private Map converterTypes = new HashMap();

    /**
     * The list of the configured converters
     */
    private Map converters = new HashMap();

    /**
     * This is the key we use in the map of converted objects so we can
     * generate unique variable names.
     */
    private static final Object VAR_COUNT_KEY = new Object();
}
