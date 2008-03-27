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

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.StringTokenizer;
import java.util.TreeSet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.directwebremoting.dwrp.ArrayJsonOutboundVariable;
import org.directwebremoting.dwrp.ArrayNonJsonOutboundVariable;
import org.directwebremoting.dwrp.CollectionOutboundVariable;
import org.directwebremoting.dwrp.ErrorOutboundVariable;
import org.directwebremoting.dwrp.ParseUtil;
import org.directwebremoting.dwrp.ProtocolConstants;
import org.directwebremoting.extend.Converter;
import org.directwebremoting.extend.ConverterManager;
import org.directwebremoting.extend.InboundContext;
import org.directwebremoting.extend.InboundVariable;
import org.directwebremoting.extend.MarshallException;
import org.directwebremoting.extend.OutboundContext;
import org.directwebremoting.extend.OutboundVariable;
import org.directwebremoting.extend.TypeHintContext;
import org.directwebremoting.util.LocalUtil;
import org.directwebremoting.util.Messages;

/**
 * An implementation of Converter for Collections of Strings.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class CollectionConverter extends BaseV20Converter implements Converter
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
    @SuppressWarnings("unchecked")
    public Object convertInbound(Class<?> paramType, InboundVariable data, InboundContext inctx) throws MarshallException
    {
        String value = data.getValue();

        // If the text is null then the whole bean is null
        if (value.trim().equals(ProtocolConstants.INBOUND_NULL))
        {
            return null;
        }

        if (!value.startsWith(ProtocolConstants.INBOUND_ARRAY_START))
        {
            throw new MarshallException(paramType, Messages.getString("CollectionConverter.FormatError", ProtocolConstants.INBOUND_ARRAY_START));
        }

        if (!value.endsWith(ProtocolConstants.INBOUND_ARRAY_END))
        {
            throw new MarshallException(paramType, Messages.getString("CollectionConverter.FormatError", ProtocolConstants.INBOUND_ARRAY_END));
        }

        value = value.substring(1, value.length() - 1);

        try
        {
            TypeHintContext icc = inctx.getCurrentTypeHintContext();

            TypeHintContext subthc = icc.createChildContext(0);
            Class<?> subtype = subthc.getExtraTypeInfo();

            // subtype.getMethod("h", null).getTypeParameters();
            Collection<Object> col;

            // If they want an iterator then just use an array list and fudge
            // at the end.
            if (Iterator.class.isAssignableFrom(paramType))
            {
                col = new ArrayList<Object>();
            }
            // If paramType is concrete then just use whatever we've got.
            else if (!paramType.isInterface() && !Modifier.isAbstract(paramType.getModifiers()))
            {
                // If there is a problem creating the type then we have no way
                // of completing this - they asked for a specific type and we
                // can't create that type. I don't know of a way of finding
                // subclasses that might be instaniable so we accept failure.
                //noinspection unchecked
                col = (Collection<Object>) paramType.newInstance();
            }
            // If they want a SortedSet then use TreeSet
            else if (SortedSet.class.isAssignableFrom(paramType))
            {
                col = new TreeSet<Object>();
            }
            // If they want a Set then use HashSet
            else if (Set.class.isAssignableFrom(paramType))
            {
                col = new HashSet<Object>();
            }
            // If they want a List then use an ArrayList
            else if (List.class.isAssignableFrom(paramType))
            {
                col = new ArrayList<Object>();
            }
            // If they just want a Collection then just use an ArrayList
            else if (Collection.class.isAssignableFrom(paramType))
            {
                col = new ArrayList<Object>();
            }
            else
            {
                throw new MarshallException(paramType);
            }

            // We should put the new object into the working map in case it
            // is referenced later nested down in the conversion process.
            inctx.addConverted(data, paramType, col);

            StringTokenizer st = new StringTokenizer(value, ProtocolConstants.INBOUND_ARRAY_SEPARATOR);
            int size = st.countTokens();
            for (int i = 0; i < size; i++)
            {
                String token = st.nextToken();

                String[] split = ParseUtil.splitInbound(token);
                String splitType = split[LocalUtil.INBOUND_INDEX_TYPE];
                String splitValue = split[LocalUtil.INBOUND_INDEX_VALUE];

                InboundVariable nested = new InboundVariable(data.getLookup(), null, splitType, splitValue);
                nested.dereference();

                Object output = converterManager.convertInbound(subtype, nested, inctx, subthc);
                col.add(output);
            }

            // If they wanted an Iterator then give them one otherwise use
            // the type we created
            if (Iterator.class.isAssignableFrom(paramType))
            {
                return col.iterator();
            }
            else
            {
                return col;
            }
        }
        catch (Exception ex)
        {
            throw new MarshallException(paramType, ex);
        }
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.Converter#convertOutbound(java.lang.Object, org.directwebremoting.OutboundContext)
     */
    @SuppressWarnings("unchecked")
    public OutboundVariable convertOutbound(Object data, OutboundContext outctx) throws MarshallException
    {
        // First we need to get ourselves the collection data
        Iterator<Object> it;
        if (data instanceof Collection)
        {
            Collection<Object> col = (Collection<Object>) data;
            it = col.iterator();
        }
        else if (data instanceof Iterator)
        {
            it = (Iterator<Object>) data;
        }
        else
        {
            throw new MarshallException(data.getClass());
        }

        // Stash this bit of data to cope with recursion
        CollectionOutboundVariable ov;
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
        List<OutboundVariable> ovs = new ArrayList<OutboundVariable>();
        while (it.hasNext())
        {
            Object member = it.next();
            OutboundVariable nested;

            try
            {
                nested = converterManager.convertOutbound(member, outctx);
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
     * For nested conversions
     */
    private ConverterManager converterManager = null;

    /**
     * The log stream
     */
    private static final Log log = LogFactory.getLog(CollectionConverter.class);
}
