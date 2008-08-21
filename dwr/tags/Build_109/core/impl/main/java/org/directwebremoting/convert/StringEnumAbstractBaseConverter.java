package org.directwebremoting.convert;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.directwebremoting.ConversionException;
import org.directwebremoting.extend.AbstractConverter;
import org.directwebremoting.extend.Converter;
import org.directwebremoting.extend.InboundVariable;
import org.directwebremoting.extend.NonNestedOutboundVariable;
import org.directwebremoting.extend.OutboundContext;
import org.directwebremoting.extend.OutboundVariable;
import org.directwebremoting.util.LocalUtil;

public class StringEnumAbstractBaseConverter extends AbstractConverter implements Converter
{
    /* (non-Javadoc)
     * @see org.directwebremoting.extend.Converter#convertInbound(java.lang.Class, org.directwebremoting.extend.InboundVariable, org.directwebremoting.extend.InboundContext)
     */
    public Object convertInbound(Class<?> paramType, InboundVariable iv) throws ConversionException
    {
        String value = LocalUtil.decode(iv.getValue());

        try
        {
            Method getter = paramType.getMethod("forString", String.class);
            Object bean = getter.invoke(paramType, value);

            if (bean == null)
            {
                throw new ConversionException(paramType, "unknown enum value (" + value + ")");
            }

            return bean;
        }
        catch (NoSuchMethodException e)
        {
            throw new ConversionException(paramType, e);
        }
        catch (IllegalArgumentException e)
        {
            throw new ConversionException(paramType, e);
        }
        catch (IllegalAccessException e)
        {
            throw new ConversionException(paramType, e);
        }
        catch (InvocationTargetException e)
        {
            throw new ConversionException(paramType, e);
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
