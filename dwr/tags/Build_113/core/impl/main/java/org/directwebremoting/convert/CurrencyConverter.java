/*
 * Copyright 2008 the original author or authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
