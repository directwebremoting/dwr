package uk.ltd.getahead.dwr;

import java.util.Map;

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
     * @param working The map of data that we are working on
     * @return The convered data, or null if the conversion was not possible
     * @throws ConversionException If the conversion failed for some reason
     */
    public Object convertTo(Class paramType, ConversionData data, Map working) throws ConversionException;

    /**
     * Attempt to coerce the data from an Object to a string
     * @param data The data to convert
     * @param varName The variable to declare
     * @param converted Objects already converted and the results
     * @return The string that declares the given variable
     * @throws ConversionException If the conversion failed for some reason
     */
    public String convertFrom(Object data, String varName, Map converted) throws ConversionException;
}
