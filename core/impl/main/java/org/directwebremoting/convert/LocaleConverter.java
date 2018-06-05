package org.directwebremoting.convert;

import org.directwebremoting.ConversionException;
import org.directwebremoting.extend.AbstractConverter;
import org.directwebremoting.extend.InboundVariable;
import org.directwebremoting.extend.NonNestedOutboundVariable;
import org.directwebremoting.extend.OutboundContext;
import org.directwebremoting.extend.OutboundVariable;
import org.directwebremoting.util.LocalUtil;

/**
 * An implementation of Converter for java.util.Locale.
 * @author Jose Noheda [jose.noheda@gmail.com]
 */
public class LocaleConverter extends AbstractConverter
{
    /**
     * Parses a locale string that matches language_country_variant.
     */
    public Object convertInbound(Class<?> paramType, InboundVariable data) throws ConversionException
    {
        return data.isNull() ? null : LocalUtil.parseLocaleString(data.urlDecode());
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.Converter#convertOutbound(java.lang.Object, org.directwebremoting.OutboundContext)
     */
    public OutboundVariable convertOutbound(Object data, OutboundContext outctx) throws ConversionException
    {
        return new NonNestedOutboundVariable('\"' + data.toString() + '\"');
    }

}
