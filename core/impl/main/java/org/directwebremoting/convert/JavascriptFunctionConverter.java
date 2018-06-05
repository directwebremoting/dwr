package org.directwebremoting.convert;

import org.directwebremoting.ConversionException;
import org.directwebremoting.ScriptSession;
import org.directwebremoting.WebContextFactory;
import org.directwebremoting.extend.AbstractConverter;
import org.directwebremoting.extend.DefaultJavascriptFunction;
import org.directwebremoting.extend.InboundVariable;
import org.directwebremoting.extend.OutboundContext;
import org.directwebremoting.extend.OutboundVariable;

/**
 * A Converter for {@link org.directwebremoting.io.JavascriptFunction}s
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class JavascriptFunctionConverter extends AbstractConverter
{
    /* (non-Javadoc)
     * @see org.directwebremoting.extend.Converter#convertInbound(java.lang.Class, org.directwebremoting.extend.InboundVariable, org.directwebremoting.extend.InboundContext)
     */
    public Object convertInbound(Class<?> paramType, InboundVariable data) throws ConversionException
    {
        if (data.isNull())
        {
            return null;
        }

        ScriptSession session = WebContextFactory.get().getScriptSession();
        String id = data.getValue();

        return new DefaultJavascriptFunction(session, id);
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.extend.Converter#convertOutbound(java.lang.Object, org.directwebremoting.extend.OutboundContext)
     */
    public OutboundVariable convertOutbound(Object data, OutboundContext outctx) throws ConversionException
    {
        throw new ConversionException(data.getClass(), "JavascriptFunctions can not be passed to a browser");
    }
}
