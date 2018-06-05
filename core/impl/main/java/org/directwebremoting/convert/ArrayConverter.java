package org.directwebremoting.convert;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.directwebremoting.ConversionException;
import org.directwebremoting.extend.ArrayOutboundVariable;
import org.directwebremoting.extend.ConvertUtil;
import org.directwebremoting.extend.Converter;
import org.directwebremoting.extend.ConverterManager;
import org.directwebremoting.extend.ErrorOutboundVariable;
import org.directwebremoting.extend.InboundContext;
import org.directwebremoting.extend.InboundVariable;
import org.directwebremoting.extend.OutboundContext;
import org.directwebremoting.extend.OutboundVariable;
import org.directwebremoting.extend.ProtocolConstants;

/**
 * An implementation of Converter for Arrays.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 * @noinspection RefusedBequest
 */
public class ArrayConverter implements Converter
{
    /* (non-Javadoc)
     * @see org.directwebremoting.convert.BaseV20Converter#setConverterManager(org.directwebremoting.ConverterManager)
     */
    public void setConverterManager(ConverterManager converterManager)
    {
        this.converterManager = converterManager;
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.Converter#convertInbound(java.lang.Class, org.directwebremoting.InboundVariable, org.directwebremoting.InboundContext)
     */
    public Object convertInbound(Class<?> paramType, InboundVariable data) throws ConversionException
    {
        if (data.isNull())
        {
            return null;
        }

        if (!paramType.isArray())
        {
            throw new ConversionException(paramType);
        }

        Class<?> componentType = paramType.getComponentType();
        //componentType = LocalUtil.getNonPrimitiveType(componentType);
        InboundContext incx = data.getContext();

        // HACK: If ArrayConverter was part of DWRP we could avoid this
        // getMembers() is there so BaseCallWrapper can support varargs by
        // creating a temporary InboundVariable so it can pass the remaining
        // parameters to be packaged into an array by the ArrayConverter
        InboundVariable[] members = data.getMembers();
        if (members != null)
        {
            Object array = Array.newInstance(componentType, members.length);
            data.getContext().addConverted(data, paramType, array);

            for (int i = 0; i < members.length; i++)
            {
                Object output = converterManager.convertInbound(componentType, members[i], data.getContext().getCurrentProperty());
                Array.set(array, i, output);
            }

            return array;
        }

        String value = data.getValue();
        if (value.startsWith(ProtocolConstants.INBOUND_ARRAY_START))
        {
            value = value.substring(1);
        }
        else
        {
            log.error("Found array end without array start: " + data.getValue());
            throw new IllegalArgumentException("Could not parse input. See logs for details.");
        }

        if (value.endsWith(ProtocolConstants.INBOUND_ARRAY_END))
        {
            value = value.substring(0, value.length() - 1);
        }
        else
        {
            log.error("Found array end without array end: " + data.getValue());
            throw new IllegalArgumentException("Could not parse input. See logs for details.");
        }

        StringTokenizer st = new StringTokenizer(value, ProtocolConstants.INBOUND_ARRAY_SEPARATOR);
        int size = st.countTokens();

        Object array = Array.newInstance(componentType, size);

        // We should put the new object into the working map in case it
        // is referenced later nested down in the conversion process.
        data.getContext().addConverted(data, paramType, array);

        for (int i = 0; i < size; i++)
        {
            String token = st.nextToken();
            String[] split = ConvertUtil.splitInbound(token);
            String splitType = split[ConvertUtil.INBOUND_INDEX_TYPE];
            String splitValue = split[ConvertUtil.INBOUND_INDEX_VALUE];

            InboundVariable nested = new InboundVariable(incx, null, splitType, splitValue);
            nested.dereference();
            Object output = converterManager.convertInbound(componentType, nested, data.getContext().getCurrentProperty());
            Array.set(array, i, output);
        }
        return array;
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.Converter#convertOutbound(java.lang.Object, org.directwebremoting.OutboundContext)
     */
    public OutboundVariable convertOutbound(Object data, OutboundContext outctx) throws ConversionException
    {
        if (!data.getClass().isArray())
        {
            throw new ConversionException(data.getClass());
        }

        ArrayOutboundVariable ov = new ArrayOutboundVariable(outctx);
        outctx.put(data, ov);

        // Convert all the data members
        int size = Array.getLength(data);
        List<OutboundVariable> ovs = new ArrayList<OutboundVariable>();
        for (int i = 0; i < size; i++)
        {
            OutboundVariable nested;
            try
            {
                nested = converterManager.convertOutbound(Array.get(data, i), outctx);
            }
            catch (Exception ex)
            {
                String errorMessage = "Conversion error for " + data.getClass().getName() + ".";
                log.warn(errorMessage, ex);

                nested = new ErrorOutboundVariable(errorMessage);
            }
            ovs.add(nested);
        }

        // Group the list of converted objects into this OutboundVariable
        ov.setChildren(ovs);

        return ov;
    }

    /**
     * The log stream
     */
    private static final Log log = LogFactory.getLog(ArrayConverter.class);

    /**
     * The converter manager to which we forward array members for conversion
     */
    private ConverterManager converterManager = null;
}
