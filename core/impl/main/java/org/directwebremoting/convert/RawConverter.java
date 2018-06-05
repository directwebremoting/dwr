package org.directwebremoting.convert;

import org.directwebremoting.ConversionException;
import org.directwebremoting.extend.AbstractConverter;
import org.directwebremoting.extend.Converter;
import org.directwebremoting.extend.InboundVariable;
import org.directwebremoting.extend.NonNestedOutboundVariable;
import org.directwebremoting.extend.OutboundContext;
import org.directwebremoting.extend.OutboundVariable;
import org.directwebremoting.extend.RealRawData;
import org.directwebremoting.io.RawData;

/**
 * A Converter that does nothing.
 * This converter is useful when the type is not known at call time, but can be
 * derived with internal knowledge, so the output can be passed manually to the
 * converter manager.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class RawConverter extends AbstractConverter implements Converter
{
    /* (non-Javadoc)
     * @see org.directwebremoting.Converter#convertInbound(java.lang.Class, org.directwebremoting.InboundVariable, org.directwebremoting.InboundContext)
     */
    public Object convertInbound(Class<?> paramType, InboundVariable data) throws ConversionException
    {
        return new RealRawData(data, data.getContext());
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.Converter#convertOutbound(java.lang.Object, org.directwebremoting.OutboundContext)
     */
    public OutboundVariable convertOutbound(Object data, OutboundContext outctx) throws ConversionException
    {
        // We don't actually need to cast to RawData, but we want to make sure
        // that that's was we've really been given
        RawData raw = (RawData) data;
        return new NonNestedOutboundVariable(raw.toString());
    }
}
