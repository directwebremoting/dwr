package uk.ltd.getahead.dwr;

/**
 * An interface for converting types from a string to some other type.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public interface Converter
{
    /**
     * If we are a compound converter that farms out part of the conversion
     * to other converters then you farm the conversion out via a configuration.
     * @param config The confiuration object
     */
    public void init(ConverterManager config);

    /**
     * Attempt to coerce the data from a string to an Object
     * @param paramType The type to convert to
     * @param data The data to convert
     * @param inctx The map of data that we are working on
     * @return The convered data, or null if the conversion was not possible
     * @throws ConversionException If the conversion failed for some reason
     */
    public Object convertInbound(Class paramType, InboundVariable data, InboundContext inctx) throws ConversionException;

    /**
     * Return a javascript string that defines the variable named varName to
     * have the contents of the converted object data.
     * @param data The data to convert
     * @param varName The variable to define
     * @param outctx Objects already converted and the results
     * @return The string that defines the given variable
     * @throws ConversionException If the conversion failed for some reason
     */
    public String convertOutbound(Object data, String varName, OutboundContext outctx) throws ConversionException;
}
