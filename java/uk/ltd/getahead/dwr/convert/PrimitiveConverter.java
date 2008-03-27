/*
 * Copyright 2005 Joe Walker
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
package uk.ltd.getahead.dwr.convert;

import uk.ltd.getahead.dwr.ConversionException;
import uk.ltd.getahead.dwr.Converter;
import uk.ltd.getahead.dwr.ConverterManager;
import uk.ltd.getahead.dwr.InboundContext;
import uk.ltd.getahead.dwr.InboundVariable;
import uk.ltd.getahead.dwr.Messages;
import uk.ltd.getahead.dwr.OutboundContext;
import uk.ltd.getahead.dwr.compat.BaseV10Converter;
import uk.ltd.getahead.dwr.util.JavascriptUtil;
import uk.ltd.getahead.dwr.util.LocalUtil;

/**
 * Converter for all primitive types
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class PrimitiveConverter extends BaseV10Converter implements Converter
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
        String value = iv.getValue();
        try
        {
            if (paramType == Boolean.TYPE || paramType == Boolean.class)
            {
                return Boolean.valueOf(value.trim());
            }

            if (paramType == Byte.TYPE || paramType == Byte.class)
            {
                if (value.length() == 0)
                {
                    byte b = 0;
                    return new Byte(b);
                }
                return new Byte(value.trim());
            }

            if (paramType == Short.TYPE || paramType == Short.class)
            {
                if (value.length() == 0)
                {
                    short s = 0;
                    return new Short(s);
                }
                return new Short(value.trim());
            }

            if (paramType == Character.TYPE || paramType == Character.class)
            {
                String decode = LocalUtil.decode(value);
                if (decode.length() == 1)
                {
                    return new Character(decode.charAt(0));
                }
                else
                {
                    throw new ConversionException(Messages.getString("PrimitiveConverter.StringTooLong", value)); //$NON-NLS-1$
                }
            }

            if (paramType == Integer.TYPE || paramType == Integer.class)
            {
                if (value.length() == 0)
                {
                    return new Integer(0);
                }
                return new Integer(value.trim());
            }

            if (paramType == Long.TYPE || paramType == Long.class)
            {
                if (value.length() == 0)
                {
                    return new Long(0);
                }
                return new Long(value.trim());
            }

            if (paramType == Float.TYPE || paramType == Float.class)
            {
                if (value.length() == 0)
                {
                    return new Float(0);
                }
                return new Float(LocalUtil.decode(value.trim()));
            }

            if (paramType == Double.TYPE || paramType == Double.class)
            {
                if (value.length() == 0)
                {
                    return new Double(0);
                }
                return new Double(LocalUtil.decode(value.trim()));
            }

            throw new ConversionException(Messages.getString("PrimitiveConverter.TypeNotPrimitive", paramType.getName())); //$NON-NLS-1$
        }
        catch (NumberFormatException ex)
        {
            throw new ConversionException(Messages.getString("PrimitiveConverter.FormatError", value, paramType.getName()), ex); //$NON-NLS-1$
        }
    }

    /* (non-Javadoc)
     * @see uk.ltd.getahead.dwr.Converter#convertOutbound(java.lang.Object, java.lang.String, uk.ltd.getahead.dwr.OutboundContext)
     */
    public String convertOutbound(Object object, String varname, OutboundContext outctx)
    {
        Class paramType = object.getClass();

        if (object.equals(Boolean.TRUE))
        {
            return "var " + varname + "=true;"; //$NON-NLS-1$ //$NON-NLS-2$
        }
        else if (object.equals(Boolean.FALSE))
        {
            return "var " + varname + "=false;"; //$NON-NLS-1$ //$NON-NLS-2$
        }
        else if (paramType == Character.class)
        {
            // Treat characters as strings
            return "var " + varname + "=\"" + jsutil.escapeJavaScript(object.toString()) + "\";"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        }
        else
        {
            // We just use the default toString for all numbers
            return "var " + varname + '=' + object.toString() + ';'; //$NON-NLS-1$
        }
    }

    /**
     * The means by which we strip comments
     */
    private JavascriptUtil jsutil = new JavascriptUtil();
}
