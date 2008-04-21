package org.directwebremoting.convert;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.directwebremoting.extend.Converter;
import org.directwebremoting.extend.InboundContext;
import org.directwebremoting.extend.InboundVariable;
import org.directwebremoting.extend.MarshallException;
import org.directwebremoting.extend.NonNestedOutboundVariable;
import org.directwebremoting.extend.OutboundContext;
import org.directwebremoting.extend.OutboundVariable;
import org.directwebremoting.util.LocalUtil;

public class StringEnumAbstractBaseConverter extends BaseV20Converter implements Converter
{
    /* (non-Javadoc)
     * @see org.directwebremoting.extend.Converter#convertInbound(java.lang.Class, org.directwebremoting.extend.InboundVariable, org.directwebremoting.extend.InboundContext)
     */
    public Object convertInbound(Class<?> paramType, InboundVariable iv, InboundContext inctx) throws MarshallException
    {
        String value = LocalUtil.decode(iv.getValue());

        try
        {
            Method getter = paramType.getMethod("forString", String.class);
            Object bean = getter.invoke(paramType, value);

            if (bean == null)
            {
                throw new MarshallException(paramType, "unknown enum value (" + value + ")");
            }

            return bean;
        }
        catch (NoSuchMethodException e)
        {
            throw new MarshallException(paramType, e);
        }
        catch (IllegalArgumentException e)
        {
            throw new MarshallException(paramType, e);
        }
        catch (IllegalAccessException e)
        {
            throw new MarshallException(paramType, e);
        }
        catch (InvocationTargetException e)
        {
            throw new MarshallException(paramType, e);
        }
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.extend.Converter#convertOutbound(java.lang.Object, org.directwebremoting.extend.OutboundContext)
     */
    public OutboundVariable convertOutbound(Object data, OutboundContext outctx)
    {
        return new NonNestedOutboundVariable('\'' + data.toString() + '\'');
    }
}
