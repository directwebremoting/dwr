package org.directwebremoting.convert;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.directwebremoting.ConversionException;
import org.directwebremoting.extend.AbstractConverter;
import org.directwebremoting.extend.InboundVariable;
import org.directwebremoting.extend.NonNestedOutboundVariable;
import org.directwebremoting.extend.OutboundContext;
import org.directwebremoting.extend.OutboundVariable;
import org.directwebremoting.util.JavascriptUtil;
import org.directwebremoting.util.LocalUtil;

/**
 * Converter for all primitive types
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class PrimitiveConverter extends AbstractConverter
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

        String value = data.getValue().trim();

        try
        {
            return LocalUtil.simpleConvert(value, paramType);
        }
        catch (NumberFormatException ex)
        {
            log.debug("Error converting " + value + " to a number.");
            throw new ConversionException(paramType, "Format error converting number.");
        }
        catch (IllegalArgumentException ex)
        {
            throw new ConversionException(paramType);
        }
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.Converter#convertOutbound(java.lang.Object, org.directwebremoting.OutboundContext)
     */
    public OutboundVariable convertOutbound(Object data, OutboundContext outctx)
    {
        Class<?> paramType = data.getClass();

        if (data.equals(Boolean.TRUE))
        {
            return new NonNestedOutboundVariable("true");
        }
        else if (data.equals(Boolean.FALSE))
        {
            return new NonNestedOutboundVariable("false");
        }
        else if (paramType == Character.class)
        {
            // Treat characters as strings
            return new NonNestedOutboundVariable('\"' + JavascriptUtil.escapeJavaScript(data.toString()) + '\"');
        }
        else
        {
            // We just use the default toString for all numbers
            return new NonNestedOutboundVariable(data.toString());
        }
    }

    /**
     * The log stream
     */
    private static final Log log = LogFactory.getLog(PrimitiveConverter.class);
}
