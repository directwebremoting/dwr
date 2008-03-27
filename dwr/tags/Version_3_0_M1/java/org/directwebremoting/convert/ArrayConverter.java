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
package org.directwebremoting.convert;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.directwebremoting.dwrp.ArrayJsonOutboundVariable;
import org.directwebremoting.dwrp.ArrayNonJsonOutboundVariable;
import org.directwebremoting.dwrp.ErrorOutboundVariable;
import org.directwebremoting.dwrp.CollectionOutboundVariable;
import org.directwebremoting.dwrp.ParseUtil;
import org.directwebremoting.dwrp.ProtocolConstants;
import org.directwebremoting.extend.Converter;
import org.directwebremoting.extend.ConverterManager;
import org.directwebremoting.extend.InboundContext;
import org.directwebremoting.extend.InboundVariable;
import org.directwebremoting.extend.MarshallException;
import org.directwebremoting.extend.OutboundContext;
import org.directwebremoting.extend.OutboundVariable;
import org.directwebremoting.util.LocalUtil;

/**
 * An implementation of Converter for Arrays.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 * @noinspection RefusedBequest
 */
public class ArrayConverter extends BaseV20Converter implements Converter
{
    /* (non-Javadoc)
     * @see org.directwebremoting.convert.BaseV20Converter#setConverterManager(org.directwebremoting.ConverterManager)
     */
    @Override
    public void setConverterManager(ConverterManager converterManager)
    {
        this.converterManager = converterManager;
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.Converter#convertInbound(java.lang.Class, org.directwebremoting.InboundVariable, org.directwebremoting.InboundContext)
     */
    public Object convertInbound(Class<?> paramType, InboundVariable data, InboundContext inctx) throws MarshallException
    {
        if (!paramType.isArray())
        {
            throw new MarshallException(paramType);
        }

        String value = data.getValue();
        if (value.startsWith(ProtocolConstants.INBOUND_ARRAY_START))
        {
            value = value.substring(1);
        }
        if (value.endsWith(ProtocolConstants.INBOUND_ARRAY_END))
        {
            value = value.substring(0, value.length() - 1);
        }

        StringTokenizer st = new StringTokenizer(value, ProtocolConstants.INBOUND_ARRAY_SEPARATOR);
        int size = st.countTokens();

        Class<?> componentType = paramType.getComponentType();
        //componentType = LocalUtil.getNonPrimitiveType(componentType);
        Object array = Array.newInstance(componentType, size);

        // We should put the new object into the working map in case it
        // is referenced later nested down in the conversion process.
        inctx.addConverted(data, paramType, array);
        InboundContext incx = data.getLookup();

        for (int i = 0; i < size; i++)
        {
            String token = st.nextToken();
            String[] split = ParseUtil.splitInbound(token);
            String splitType = split[LocalUtil.INBOUND_INDEX_TYPE];
            String splitValue = split[LocalUtil.INBOUND_INDEX_VALUE];

            InboundVariable nested = new InboundVariable(incx, null, splitType, splitValue);
            nested.dereference();
            Object output = converterManager.convertInbound(componentType, nested, inctx, inctx.getCurrentTypeHintContext());
            Array.set(array, i, output);
        }

        return array;
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.Converter#convertOutbound(java.lang.Object, org.directwebremoting.OutboundContext)
     */
    public OutboundVariable convertOutbound(Object data, OutboundContext outctx) throws MarshallException
    {
        if (!data.getClass().isArray())
        {
            throw new MarshallException(data.getClass());
        }

        CollectionOutboundVariable ov;

        // Stash this bit of data to cope with recursion
        if (outctx.isJsonMode())
        {
            ov = new ArrayJsonOutboundVariable();
        }
        else
        {
            ov = new ArrayNonJsonOutboundVariable(outctx);
        }
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
