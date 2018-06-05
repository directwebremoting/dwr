package org.directwebremoting.extend;

import org.directwebremoting.ConversionException;

/**
 * An interface for converting types from a string to some other type.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public interface Converter
{
    /**
     * If we are a compound converter that farms out part of the conversion
     * to other converters then you farm the conversion out via a configuration.
     * @param converterManager The configuration object
     */
    void setConverterManager(ConverterManager converterManager);

    /**
     * Attempt to coerce the data from a string to an Object.
     * If anything goes wrong with inbound conversion then we generally throw
     * an exception because we are converting data from the untrusted Internet
     * so we take the assumption that anything wrong is someone hacking.
     * @param paramType The type to convert to
     * @param data The data to convert
     * @return The converted data, or null if the conversion was not possible
     * @throws ConversionException If the conversion failed for some reason
     */
    Object convertInbound(Class<?> paramType, InboundVariable data) throws ConversionException;

    /**
     * Return a javascript string that defines the variable named varName to
     * have the contents of the converted object data.
     * <p>In contrast to <code>convertInbound()</code> failures in converting
     * data on the way out should not stop processing, and we should carry on
     * if we can. Failures are probably down to some misconfiguration so as much
     * information about the error as can be safely generated to console logs is
     * good. In other words if you need to loop in outbound conversion then it
     * might be a good idea to catch issues inside the loop, log, and carry on.
     * @param data The data to convert
     * @param outctx A collection of objects already converted and the results
     * @return The OutboundVariable that represents the data to convert
     * @throws ConversionException If the conversion failed for some reason
     */
    OutboundVariable convertOutbound(Object data, OutboundContext outctx) throws ConversionException;
}
