package org.directwebremoting.convert;

import java.util.Currency;

import org.directwebremoting.ConversionException;
import org.directwebremoting.extend.AbstractConverter;
import org.directwebremoting.extend.InboundVariable;
import org.directwebremoting.extend.NonNestedOutboundVariable;
import org.directwebremoting.extend.OutboundContext;
import org.directwebremoting.extend.OutboundVariable;

/**
 * An implementation of Converter for java.util.Currency.
 * @author Jose Noheda [jose.noheda@gmail.com]
 */
public class CurrencyConverter extends AbstractConverter
{
    /**
	* Parses a currency ISO code.
	*/
    public Object convertInbound(Class<?> paramType, InboundVariable data) throws ConversionException
    {
		if (data.isNull())
		{
			return null;
		}
		try
		{
			Currency currency = Currency.getInstance(data.getValue());
			if (currency == null)
			{
				throw new IllegalArgumentException(data.getValue() + " is not a valid java.util.Currency value");
			}
			return currency;
		}
		catch (Exception ex)
		{
			throw new ConversionException(Currency.class, ex);
		}
    }

    /* (non-Javadoc)
	 * @see org.directwebremoting.Converter#convertOutbound(java.lang.Object, org.directwebremoting.OutboundContext)
	 */
    public OutboundVariable convertOutbound(Object data, OutboundContext outctx) throws ConversionException
    {
        return new NonNestedOutboundVariable('\"' + ((Currency) data).getCurrencyCode() + '\"');
    }

}
