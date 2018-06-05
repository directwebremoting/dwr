package org.directwebremoting.convert;

import org.directwebremoting.ConversionException;
import org.directwebremoting.extend.AbstractConverter;
import org.directwebremoting.extend.InboundVariable;
import org.directwebremoting.extend.NonNestedOutboundVariable;
import org.directwebremoting.extend.OutboundContext;
import org.directwebremoting.extend.OutboundVariable;

/**
 * An implementation of Converter for Context helper objects for ScriptProxies.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class ContextConverter extends AbstractConverter
{
    /* (non-Javadoc)
     * @see org.directwebremoting.Converter#convertInbound(java.lang.Class, org.directwebremoting.InboundVariable, org.directwebremoting.InboundContext)
     */
    public Object convertInbound(Class<?> paramType, InboundVariable data) throws ConversionException
    {
        throw new SecurityException("Contexts can not be converted");
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.Converter#convertOutbound(java.lang.Object, org.directwebremoting.OutboundContext)
     */
    public OutboundVariable convertOutbound(Object data, OutboundContext outctx) throws ConversionException
    {
        // TODO: Do we ever actually convert this? Looking at the methods, I
        // can't believe we do.
        // Context context = (Context) data;
        return new NonNestedOutboundVariable(/*context.getContextPath()*/null);
    }
}
