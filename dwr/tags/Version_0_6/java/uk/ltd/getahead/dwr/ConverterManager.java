package uk.ltd.getahead.dwr;

/**
 * A class to manage the converter types and the instansiated class name matches.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public interface ConverterManager
{
    /**
     * Add a new converter type
     * @param id The name of the converter type
     * @param clazz The class to do the conversion
     */
    public void addConverterType(String id, Class clazz);

    /**
     * Add a new converter
     * @param match The class name(s) to match
     * @param type The name of the converter type
     * @throws InstantiationException If reflection based creation fails
     * @throws IllegalAccessException If reflection based creation fails
     * @throws IllegalArgumentException If we have a duplicate name
     */
    public void addConverter(String match, String type) throws IllegalArgumentException, InstantiationException, IllegalAccessException;

    /**
     * Check if we can coerce the given type
     * @param paramType The type to check
     * @return true iff <code>paramType</code> is coercable
     */
    public boolean isConvertable(Class paramType);

    /**
     * Convert an object from being a string into an object of some type.
     * Designed for use with converters that have a working map passed to them
     * @param paramType The type that you want the object to be
     * @param iv The string version of the object
     * @param inctx The map of data that we are working on
     * @return The coerced object or null if the object could not be coerced
     * @throws ConversionException If the conversion failed for some reason
     */
    public Object convertInbound(Class paramType, InboundVariable iv, InboundContext inctx) throws ConversionException;

    /**
     * Convert an object into a Javavscript representation of the same
     * This method is for use by top level objects i.e. DWRServlet
     * @param object The object to convert
     * @return A Javascript string version of the object
     * @throws ConversionException If the conversion failed for some reason
     */
    public OutboundVariable convertOutbound(Object object) throws ConversionException;

    /**
     * Convert an object into a Javavscript representation of the same.
     * This method is for use by converters wishing to recurse into some object.
     * @param object The object to convert
     * @param converted The list of converted objects so far
     * @return A Javascript string version of the object
     * @throws ConversionException If the conversion failed for some reason
     */
    public OutboundVariable convertOutbound(Object object, OutboundContext converted) throws ConversionException;
}
