package uk.ltd.getahead.dwr.convert;

import java.math.BigDecimal;
import java.math.BigInteger;

import uk.ltd.getahead.dwr.ConversionException;
import uk.ltd.getahead.dwr.OutboundContext;
import uk.ltd.getahead.dwr.InboundContext;
import uk.ltd.getahead.dwr.InboundVariable;
import uk.ltd.getahead.dwr.Converter;
import uk.ltd.getahead.dwr.ConverterManager;

/**
 * Converter for all primitive types
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class BigNumberConverter implements Converter
{
    /* (non-Javadoc)
     * @see uk.ltd.getahead.dwr.Converter#init(uk.ltd.getahead.dwr.Configuration)
     */
    public void init(ConverterManager config)
    {
    }
    
    /* (non-Javadoc)
     * @see uk.ltd.getahead.dwr.Converter#convertTo(java.lang.Class, uk.ltd.getahead.dwr.InboundVariable, java.util.Map)
     */
    public Object convertInbound(Class paramType, InboundVariable data, InboundContext inctx) throws ConversionException
    {
        String value = data.getValue();
        try
        {
            if (paramType == BigDecimal.class)
            {
                return new BigDecimal(value.trim());
            }
            else if (paramType == BigInteger.class)
            {
                return new BigInteger(value.trim());
            }

            throw new ConversionException("Non-primitive paramType: "+paramType.getName());
        }
        catch (NumberFormatException ex)
        {
            throw new ConversionException("Format error converting " + value + " to " + paramType.getName(), ex);
        }
    }

    /* (non-Javadoc)
     * @see uk.ltd.getahead.dwr.Converter#convertFrom(java.lang.Object, java.lang.String, java.util.Map)
     */
    public String convertOutbound(Object object, String varname, OutboundContext outctx)
    {
        return "var " + varname + " = " + object.toString() + ";";
    }
}