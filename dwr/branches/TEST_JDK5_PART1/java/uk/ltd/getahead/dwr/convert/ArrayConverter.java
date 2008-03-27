package uk.ltd.getahead.dwr.convert;

import java.lang.reflect.Array;
import java.util.StringTokenizer;

import uk.ltd.getahead.dwr.ConversionException;
import uk.ltd.getahead.dwr.OutboundContext;
import uk.ltd.getahead.dwr.OutboundVariable;
import uk.ltd.getahead.dwr.InboundContext;
import uk.ltd.getahead.dwr.InboundVariable;
import uk.ltd.getahead.dwr.Converter;
import uk.ltd.getahead.dwr.ConverterManager;
import uk.ltd.getahead.dwr.util.Log;

/**
 * An implementation of Converter for Arrays.
 * @author Joe Walker [joe at eireneh dot com]
 * @version $Id: StringConverter.java,v 1.2 2004/11/04 15:54:07 joe_walker Exp $
 */
public class ArrayConverter implements Converter
{
    /* (non-Javadoc)
     * @see uk.ltd.getahead.dwr.Converter#init(uk.ltd.getahead.dwr.Configuration)
     */
    public void init(ConverterManager newConfig)
    {
        this.converterManager = newConfig;
    }

    /* (non-Javadoc)
     * @see uk.ltd.getahead.dwr.Converter#convertTo(java.lang.Class, uk.ltd.getahead.dwr.InboundVariable, java.util.Map)
     */
    public Object convertInbound(Class paramType, InboundVariable data, InboundContext inctx) throws ConversionException
    {
        if (!paramType.isArray())
        {
            throw new ConversionException("Class: " + paramType + " is not an array type");
        }

        String value = data.getValue();
        if (value.startsWith("["))
        {
            value = value.substring(1);
        }
        if (value.endsWith("]"))
        {
            value = value.substring(0, value.length() - 1);
        }

        try
        {
            StringTokenizer st = new StringTokenizer(value, ",");
            int size = st.countTokens();

            Class componentType = paramType.getComponentType();
            //componentType = LocalUtil.getNonPrimitiveType(componentType);
            Object array = Array.newInstance(componentType, size);

            // We should put the new object into the working map in case it
            // is referenced later nested down in the conversion process.
            inctx.addConverted(data, array);

            for (int i = 0; i < size; i++)
            {
                String token = st.nextToken();
                final InboundContext key = data.getLookup();

                int colon = token.indexOf(":");
                String type;
                String ivalue;
                if (colon != -1)
                {
                    type = token.substring(0, colon);
                    ivalue = token.substring(colon + 1);
                }
                else
                {
                    Log.error("Missing : in conversion data");
                    type = "string";
                    ivalue = token;
                }

                InboundVariable nested = new InboundVariable(key, type, ivalue);
                Object output = converterManager.convertInbound(componentType, nested, inctx);
                Array.set(array, i, output);
            }

            return array;
        }
        catch (Exception ex)
        {
            throw new ConversionException(ex);
        }
    }

    /* (non-Javadoc)
     * @see uk.ltd.getahead.dwr.Converter#convertFrom(java.lang.Object, java.lang.String, java.util.Map)
     */
    public String convertOutbound(Object data, String varname, OutboundContext outctx) throws ConversionException
    {
        if (!data.getClass().isArray())
        {
            throw new ConversionException("Class: " + data.getClass() + " is not an array type");
        }

        StringBuffer buffer = new StringBuffer();
        buffer.append("var " + varname + " = new Array();");

        int size = Array.getLength(data);
        for (int i = 0; i < size; i++)
        {
            OutboundVariable nested = converterManager.convertOutbound(Array.get(data, i), outctx);

            buffer.append(nested.initCode);
            buffer.append(varname + "[");
            buffer.append(i);
            buffer.append("] = ");
            buffer.append(nested.assignCode);
            buffer.append(";");
        }

        return buffer.toString();
    }

    private ConverterManager converterManager;
}
