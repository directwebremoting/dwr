package org.directwebremoting.convert;

import java.lang.reflect.Proxy;

import org.directwebremoting.ConversionException;
import org.directwebremoting.ScriptSession;
import org.directwebremoting.WebContextFactory;
import org.directwebremoting.extend.AbstractConverter;
import org.directwebremoting.extend.DefaultJavascriptObject;
import org.directwebremoting.extend.InboundVariable;
import org.directwebremoting.extend.OutboundContext;
import org.directwebremoting.extend.OutboundVariable;

/**
 * A converter that works to create objects that implement some interface and
 * proxies the method calls back to the client via a
 * {@link org.directwebremoting.io.JavascriptObject}
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class ProxyInterfaceConverter extends AbstractConverter
{
    /* (non-Javadoc)
     * @see org.directwebremoting.extend.Converter#convertInbound(java.lang.Class, org.directwebremoting.extend.InboundVariable)
     */
    public Object convertInbound(Class<?> paramType, InboundVariable data) throws ConversionException
    {
        if (data.isNull())
        {
            return null;
        }

        ScriptSession session = WebContextFactory.get().getScriptSession();
        String id = data.getValue();

        DefaultJavascriptObject object = new DefaultJavascriptObject(session, id);

        return Proxy.newProxyInstance(paramType.getClassLoader(), new Class[] { paramType }, object);
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.extend.Converter#convertOutbound(java.lang.Object, org.directwebremoting.extend.OutboundContext)
     */
    public OutboundVariable convertOutbound(Object data, OutboundContext outctx) throws ConversionException
    {
        throw new ConversionException(data.getClass(), "Interfaces can not be passed to a browser");
    }
}
