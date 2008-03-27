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

import java.lang.reflect.Array;
import java.util.StringTokenizer;

import uk.ltd.getahead.dwr.ConversionConstants;
import uk.ltd.getahead.dwr.ConversionException;
import uk.ltd.getahead.dwr.Converter;
import uk.ltd.getahead.dwr.ConverterManager;
import uk.ltd.getahead.dwr.InboundContext;
import uk.ltd.getahead.dwr.InboundVariable;
import uk.ltd.getahead.dwr.Messages;
import uk.ltd.getahead.dwr.OutboundContext;
import uk.ltd.getahead.dwr.OutboundVariable;
import uk.ltd.getahead.dwr.compat.BaseV10Converter;
import uk.ltd.getahead.dwr.util.LocalUtil;
import uk.ltd.getahead.dwr.util.Logger;

/**
 * An implementation of Converter for Arrays.
 * @author Joe Walker [joe at eireneh dot com]
 * @version $Id: StringConverter.java,v 1.2 2004/11/04 15:54:07 joe_walker Exp $
 */
public class ArrayConverter extends BaseV10Converter implements Converter
{
    /* (non-Javadoc)
     * @see uk.ltd.getahead.dwr.Converter#init(uk.ltd.getahead.dwr.DefaultConfiguration)
     */
    public void setConverterManager(ConverterManager newConfig)
    {
        this.converterManager = newConfig;
    }

    /* (non-Javadoc)
     * @see uk.ltd.getahead.dwr.Converter#convertInbound(java.lang.Class, java.util.List, uk.ltd.getahead.dwr.InboundVariable, uk.ltd.getahead.dwr.InboundContext)
     */
    public Object convertInbound(Class paramType, InboundVariable iv, InboundContext inctx) throws ConversionException
    {
        if (!paramType.isArray())
        {
            throw new ConversionException(Messages.getString("ArrayConverter.ClassIsNotAnArray", paramType.getName())); //$NON-NLS-1$
        }

        String value = iv.getValue();
        if (value.startsWith(ConversionConstants.INBOUND_ARRAY_START))
        {
            value = value.substring(1);
        }
        if (value.endsWith(ConversionConstants.INBOUND_ARRAY_END))
        {
            value = value.substring(0, value.length() - 1);
        }

        try
        {
            StringTokenizer st = new StringTokenizer(value, ConversionConstants.INBOUND_ARRAY_SEPARATOR);
            int size = st.countTokens();

            Class componentType = paramType.getComponentType();
            //componentType = LocalUtil.getNonPrimitiveType(componentType);
            Object array = Array.newInstance(componentType, size);

            // We should put the new object into the working map in case it
            // is referenced later nested down in the conversion process.
            inctx.addConverted(iv, array);
            InboundContext incx = iv.getLookup();

            for (int i = 0; i < size; i++)
            {
                String token = st.nextToken();
                String[] split = LocalUtil.splitInbound(token);

                InboundVariable nested = new InboundVariable(incx, split[LocalUtil.INBOUND_INDEX_TYPE], split[LocalUtil.INBOUND_INDEX_VALUE]);
                Object output = converterManager.convertInbound(componentType, nested, inctx, inctx.getCurrentTypeHintContext());
                Array.set(array, i, output);
            }

            return array;
        }
        catch (ConversionException ex)
        {
            throw ex;
        }
        catch (Exception ex)
        {
            throw new ConversionException(ex);
        }
    }

    /* (non-Javadoc)
     * @see uk.ltd.getahead.dwr.Converter#convertOutbound(java.lang.Object, java.lang.String, uk.ltd.getahead.dwr.OutboundContext)
     */
    public String convertOutbound(Object data, String varname, OutboundContext outctx) throws ConversionException
    {
        if (!data.getClass().isArray())
        {
            throw new ConversionException(Messages.getString("ArrayConverter.ClassIsNotAnArray", data.getClass().getName())); //$NON-NLS-1$
        }

        StringBuffer buffer = new StringBuffer();
        buffer.append("var "); //$NON-NLS-1$
        buffer.append(varname);
        buffer.append("=[];"); //$NON-NLS-1$

        int size = Array.getLength(data);
        for (int i = 0; i < size; i++)
        {
            try
            {
                OutboundVariable nested = converterManager.convertOutbound(Array.get(data, i), outctx);
    
                buffer.append(nested.getInitCode());
                buffer.append(varname);
                buffer.append('[');
                buffer.append(i);
                buffer.append("]="); //$NON-NLS-1$
                buffer.append(nested.getAssignCode());
                buffer.append(";\n"); //$NON-NLS-1$
            }
            catch (Exception ex)
            {
                buffer.append(varname);
                buffer.append('[');
                buffer.append(i);
                buffer.append("]='Conversion Error. See console log.';\n"); //$NON-NLS-1$

                log.warn("Failed to convert array member " + i + ". Conversion error for type: " + data.getClass().getName(), ex); //$NON-NLS-1$ //$NON-NLS-2$
            }
        }

        return buffer.toString();
    }

    /**
     * The log stream
     */
    private static final Logger log = Logger.getLogger(ArrayConverter.class);

    /**
     * The converter manager to which we forward array members for conversion
     */
    private ConverterManager converterManager = null;
}
