package uk.ltd.getahead.dwr.convert;

import java.lang.reflect.Method;

import uk.ltd.getahead.dwr.ConversionException;
import uk.ltd.getahead.dwr.Converter;
import uk.ltd.getahead.dwr.ConverterManager;
import uk.ltd.getahead.dwr.InboundContext;
import uk.ltd.getahead.dwr.InboundVariable;
import uk.ltd.getahead.dwr.Messages;
import uk.ltd.getahead.dwr.OutboundContext;
import uk.ltd.getahead.dwr.util.LocalUtil;

/**
 * Converter for all primitive types
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class EnumConverter implements Converter
{
    /* (non-Javadoc)
     * @see uk.ltd.getahead.dwr.Converter#init(uk.ltd.getahead.dwr.DefaultConfiguration)
     */
    public void setConverterManager(ConverterManager config)
    {
    }

    /* (non-Javadoc)
     * @see uk.ltd.getahead.dwr.Converter#convertInbound(java.lang.Class, java.util.List, uk.ltd.getahead.dwr.InboundVariable, uk.ltd.getahead.dwr.InboundContext)
     */
    public Object convertInbound(Class paramType, InboundVariable iv, InboundContext inctx) throws ConversionException
    {
        String value = LocalUtil.decode(iv.getValue());

        // Object[] values = paramType.getEnumConstants();
        Object[] values = null;
        try
        {
            Method getter = paramType.getMethod("getEnumConstants", null); //$NON-NLS-1$
            values = (Object[]) getter.invoke(paramType, null);
        }
        catch (NoSuchMethodException ex)
        {
            // We would like to have done: if (!paramType.isEnum())
            // But this catch block has the same effect
            throw new ConversionException(Messages.getString("EnumConverter.TypeNotEnum", paramType)); //$NON-NLS-1$
        }
        catch (Exception ex)
        {
            throw new ConversionException(Messages.getString("EnumConverter.ReflectionError", paramType), ex); //$NON-NLS-1$
        }

        for (int i = 0; i < values.length; i++)
        {
            Object en = values[i];
            if (value.equals(en.toString()))
            {
                return en;
            }
        }

        throw new ConversionException(Messages.getString("EnumConverter.NoMatch", value, paramType)); //$NON-NLS-1$
    }

    /* (non-Javadoc)
     * @see uk.ltd.getahead.dwr.Converter#convertOutbound(java.lang.Object, java.lang.String, uk.ltd.getahead.dwr.OutboundContext)
     */
    public String convertOutbound(Object object, String varname, OutboundContext outctx)
    {
        return "var " + varname + "='" + object.toString() + "';"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    }
}
