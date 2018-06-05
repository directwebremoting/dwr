package org.directwebremoting.convert;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.directwebremoting.ConversionException;
import org.directwebremoting.extend.AbstractConverter;
import org.directwebremoting.extend.InboundVariable;
import org.directwebremoting.extend.NonNestedOutboundVariable;
import org.directwebremoting.extend.OutboundContext;
import org.directwebremoting.extend.OutboundVariable;

/**
 * Converter for all primitive types
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class BigNumberConverter extends AbstractConverter
{
    /* (non-Javadoc)
     * @see org.directwebremoting.Converter#convertInbound(java.lang.Class, org.directwebremoting.InboundVariable, org.directwebremoting.InboundContext)
     */
    public Object convertInbound(Class<?> paramType, InboundVariable data) throws ConversionException
    {
        if (data.isNull())
        {
            return null;
        }

        String value = data.getValue();

        if (value == null || value.length() == 0)
        {
            return null;
        }

        try
        {
            if (paramType == BigDecimal.class)
            {
                return new BigDecimal(value.trim());
            }

            if (paramType == BigInteger.class)
            {
                return new BigInteger(value.trim());
            }

            throw new ConversionException(paramType);
        }
        catch (NumberFormatException ex)
        {
            log.debug("Error converting " + value + " to a number.");
            throw new ConversionException(paramType, "Format error converting number.");
        }
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.Converter#convertOutbound(java.lang.Object, org.directwebremoting.OutboundContext)
     */
    public OutboundVariable convertOutbound(Object data, OutboundContext outctx)
    {
        if (data == null)
        {
            return new NonNestedOutboundVariable("null");
        }

        return new NonNestedOutboundVariable(data.toString());
    }

    /**
     * The log stream
     */
    private static final Log log = LogFactory.getLog(BigNumberConverter.class);
}
