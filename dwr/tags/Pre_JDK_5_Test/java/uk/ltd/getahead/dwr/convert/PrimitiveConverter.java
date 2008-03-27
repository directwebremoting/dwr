package uk.ltd.getahead.dwr.convert;

import java.util.Map;

import uk.ltd.getahead.dwr.ConversionData;
import uk.ltd.getahead.dwr.ConversionException;
import uk.ltd.getahead.dwr.Converter;
import uk.ltd.getahead.dwr.ConverterManager;
import uk.ltd.getahead.dwr.lang.StringEscapeUtils;
import uk.ltd.getahead.dwr.util.LocalUtil;

/**
 * Converter for all primitive types
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class PrimitiveConverter implements Converter
{
    /* (non-Javadoc)
     * @see uk.ltd.getahead.dwr.Converter#init(uk.ltd.getahead.dwr.Configuration)
     */
    public void init(ConverterManager config)
    {
    }
    
    /* (non-Javadoc)
     * @see uk.ltd.getahead.dwr.Converter#convertTo(java.lang.Class, uk.ltd.getahead.dwr.ConversionData, java.util.Map)
     */
    public Object convertTo(Class paramType, ConversionData data, Map working) throws ConversionException
    {
        String value = data.getValue();
        try
        {
            if (paramType == Boolean.TYPE || paramType == Boolean.class)
            {
                return Boolean.valueOf(value.trim());
            }
            else if (paramType == Byte.TYPE || paramType == Byte.class)
            {
                if (value.length() == 0)
                {
                    byte b = 0;
                    return new Byte(b);
                }
                return new Byte(value.trim());
            }
            else if (paramType == Short.TYPE || paramType == Short.class)
            {
                if (value.length() == 0)
                {
                    short s = 0;
                    return new Short(s);
                }
                return new Short(value.trim());
            }
            else if (paramType == Character.TYPE || paramType == Character.class)
            {
                String decode = LocalUtil.decode(value);
                if (decode.length() == 1)
                {
                    return new Character(decode.charAt(0));
                }
                else
                {
                    throw new ConversionException("Can't convert string to single char: '" + value + "'");
                }
            }
            else if (paramType == Integer.TYPE || paramType == Integer.class)
            {
                if (value.length() == 0)
                {
                    return new Integer(0);
                }
                return new Integer(value.trim());
            }
            else if (paramType == Long.TYPE || paramType == Long.class)
            {
                if (value.length() == 0)
                {
                    return new Long(0);
                }
                return new Long(value.trim());
            }
            else if (paramType == Float.TYPE || paramType == Float.class)
            {
                if (value.length() == 0)
                {
                    return new Float(0);
                }
                return new Float(value.trim());
            }
            else if (paramType == Double.TYPE || paramType == Double.class)
            {
                if (value.length() == 0)
                {
                    return new Double(0);
                }
                return new Double(value.trim());
            }

            throw new ConversionException("Non-primitive paramType: "+paramType.getName());
        }
        catch (NumberFormatException ex)
        {
            throw new ConversionException("Format error converting " + value + " to " + paramType.getName(), ex);
        }
    }

    /* (non-Javadoc)
     * @see uk.ltd.getahead.dwr.Converter#convertFrom(java.lang.Object, java.lang.String, java.util.Map)
     */
    public String convertFrom(Object object, String varname, Map converted)
    {
        Class paramType = object.getClass();

        if (object.equals(Boolean.TRUE))
        {
            // @PMD:REVIEWED:AvoidDuplicateLiterals: by Joe on 02/02/05 10:56
            return "var " + varname + " = true;";
        }
        else if (object.equals(Boolean.FALSE))
        {
            return "var " + varname + " = false;";
        }
        else if (paramType == Character.class)
        {
            // Treat characters as strings
            return "var " + varname + " = \"" + StringEscapeUtils.escapeJavaScript(object.toString()) + "\";";
        }
        else
        {
            // We just use the default toString for all numbers
            return "var " + varname + " = " + object.toString() + ";";
        }
    }
}
