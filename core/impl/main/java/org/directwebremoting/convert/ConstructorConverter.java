package org.directwebremoting.convert;

import java.lang.reflect.Constructor;

import org.directwebremoting.extend.AbstractConverter;
import org.directwebremoting.extend.InboundVariable;
import org.directwebremoting.extend.NonNestedOutboundVariable;
import org.directwebremoting.extend.OutboundContext;
import org.directwebremoting.extend.OutboundVariable;
import org.directwebremoting.util.JavascriptUtil;

/**
 * An implementation of Converter for anything with a string constructor.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class ConstructorConverter extends AbstractConverter
{
    /* (non-Javadoc)
     * @see org.directwebremoting.Converter#convertInbound(java.lang.Class, org.directwebremoting.InboundVariable, org.directwebremoting.InboundContext)
     */
    public Object convertInbound(Class<?> paramType, InboundVariable data)
    {
        if (data.isNull())
        {
            return null;
        }

        try
        {
            Constructor<?> converter = paramType.getConstructor(String.class);
            return converter.newInstance(data.getValue());
        }
        catch (Exception ex)
        {
            throw new IllegalArgumentException(ex.toString());
        }
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.Converter#convertOutbound(java.lang.Object, org.directwebremoting.OutboundContext)
     */
    public OutboundVariable convertOutbound(Object data, OutboundContext outctx)
    {
        return new NonNestedOutboundVariable('\'' + JavascriptUtil.escapeJavaScript(data.toString()) + '\'');
    }
}
