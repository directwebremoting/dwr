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
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;

import org.directwebremoting.Converter;
import org.directwebremoting.ConverterManager;
import org.directwebremoting.InboundContext;
import org.directwebremoting.InboundVariable;
import org.directwebremoting.MarshallException;
import org.directwebremoting.OutboundContext;
import org.directwebremoting.OutboundVariable;
import org.directwebremoting.TypeHintContext;
import org.directwebremoting.dwrp.ConversionConstants;
import org.directwebremoting.util.JavascriptUtil;
import org.directwebremoting.util.LocalUtil;
import org.directwebremoting.util.Logger;
import org.directwebremoting.util.Messages;

/**
 * An implementation of Converter for Maps.
 * @author Joe Walker [joe at eireneh dot com]
 * @version $Id: StringConverter.java,v 1.2 2004/11/04 15:54:07 joe_walker Exp $
 */
public class MapConverter implements Converter
{
    /* (non-Javadoc)
     * @see org.directwebremoting.Converter#setConverterManager(org.directwebremoting.ConverterManager)
     */
    public void setConverterManager(ConverterManager newConfig)
    {
        this.config = newConfig;
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.Converter#convertInbound(java.lang.Class, org.directwebremoting.InboundVariable, org.directwebremoting.InboundContext)
     */
    public Object convertInbound(Class paramType, InboundVariable iv, InboundContext inctx) throws MarshallException
    {
        String value = iv.getValue();

        // If the text is null then the whole bean is null
        if (value.trim().equals(ConversionConstants.INBOUND_NULL))
        {
            return null;
        }

        if (!value.startsWith(ConversionConstants.INBOUND_MAP_START))
        {
            throw new IllegalArgumentException(Messages.getString("MapConverter.MissingOpener", ConversionConstants.INBOUND_MAP_START)); //$NON-NLS-1$
        }

        if (!value.endsWith(ConversionConstants.INBOUND_MAP_END))
        {
            throw new IllegalArgumentException(Messages.getString("MapConverter.MissingCloser", ConversionConstants.INBOUND_MAP_START)); //$NON-NLS-1$
        }

        value = value.substring(1, value.length() - 1);

        try
        {
            // Maybe we ought to check that the paramType isn't expecting a more
            // distinct type of Map and attempt to create that?
            Map map;

            // If paramType is concrete then just use whatever we've got.
            if (!paramType.isInterface() && !Modifier.isAbstract(paramType.getModifiers()))
            {
                // If there is a problem creating the type then we have no way
                // of completing this - they asked for a specific type and we
                // can't create that type. I don't know of a way of finding
                // subclasses that might be instaniable so we accept failure.
                map = (Map) paramType.newInstance();
            }
            else
            {
                map = new HashMap();
            }

            // Get the extra type info
            TypeHintContext thc = inctx.getCurrentTypeHintContext();

            TypeHintContext keyThc = thc.createChildContext(0);
            Class keyType = keyThc.getExtraTypeInfo();

            TypeHintContext valThc = thc.createChildContext(1);
            Class valType = valThc.getExtraTypeInfo();

            // We should put the new object into the working map in case it
            // is referenced later nested down in the conversion process.
            inctx.addConverted(iv, paramType, map);
            InboundContext incx = iv.getLookup();

            // Loop through the property declarations
            StringTokenizer st = new StringTokenizer(value, ","); //$NON-NLS-1$
            int size = st.countTokens();
            for (int i = 0; i < size; i++)
            {
                String token = st.nextToken();
                if (token.trim().length() == 0)
                {
                    continue;
                }

                int colonpos = token.indexOf(ConversionConstants.INBOUND_MAP_ENTRY);
                if (colonpos == -1)
                {
                    throw new MarshallException(Messages.getString("MapConverter.MissingSeparator", ConversionConstants.INBOUND_MAP_ENTRY, token)); //$NON-NLS-1$
                }

                // Convert the value part of the token by splitting it into the
                // type and value (as passed in by Javascript)
                String valStr = token.substring(colonpos + 1).trim();
                String[] splitIv = LocalUtil.splitInbound(valStr);
                String splitIvValue = splitIv[LocalUtil.INBOUND_INDEX_VALUE];
                String splitIvType = splitIv[LocalUtil.INBOUND_INDEX_TYPE];
                InboundVariable valIv = new InboundVariable(incx, null, splitIvType, splitIvValue);
                Object val = config.convertInbound(valType, valIv, inctx, valThc);

                // Keys (unlike values) do not have type info passed with them
                // Could we have recurrsive key? - I don't think so because keys
                // must be strings in Javascript
                String keyStr = token.substring(0, colonpos).trim();
                //String[] keySplit = ExecuteQuery.splitInbound(keyStr);
                //InboundVariable keyIv = new InboundVariable(incx, splitIv[ExecuteQuery.INBOUND_INDEX_TYPE], splitIv[ExecuteQuery.INBOUND_INDEX_VALUE]);
                InboundVariable keyIv = new InboundVariable(incx, null, ConversionConstants.TYPE_STRING, keyStr);
                Object key = config.convertInbound(keyType, keyIv, inctx, keyThc);

                map.put(key, val);
            }

            return map;
        }
        catch (MarshallException ex)
        {
            throw ex;
        }
        catch (Exception ex)
        {
            throw new MarshallException(ex);
        }
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.Converter#convertOutbound(java.lang.Object, org.directwebremoting.OutboundContext)
     */
    public OutboundVariable convertOutbound(Object data, OutboundContext outctx) throws MarshallException
    {
        // First we just collect our converted children
        Map ovs = new HashMap();

        OutboundVariable ov = outctx.createOutboundVariable(data);

        // Loop through the map outputting any init code and collecting
        // converted outbound variables into the ovs map
        Map map = (Map) data;
        for (Iterator it = map.entrySet().iterator(); it.hasNext();)
        {
            Map.Entry entry = (Map.Entry) it.next();
            Object key = entry.getKey();
            Object value = entry.getValue();

            // It would be nice to check for Enums here
            if (!(key instanceof String) && !sentNonStringWarning)
            {
                log.warn("--Javascript does not support non string keys. Converting to '" + key.getClass().getName() + "' using toString()"); //$NON-NLS-1$ //$NON-NLS-2$
                sentNonStringWarning = true;
            }

            String outkey = JavascriptUtil.escapeJavaScript(key.toString());

            // OutboundVariable ovkey = config.convertOutbound(key, outctx);
            // buffer.append(ovkey.getInitCode());
            // outkey = ovkey.getAssignCode();

            OutboundVariable nested = config.convertOutbound(value, outctx);

            ovs.put(outkey, nested);
        }

        ConverterUtil.addMapInit(ov, ovs);
        return ov;
    }

    /**
     * We don't want to give the non-string warning too many times.
     */
    private static boolean sentNonStringWarning = false;

    /**
     * To forward marshalling requests
     */
    private ConverterManager config = null;

    /**
     * The log stream
     */
    private static final Logger log = Logger.getLogger(MapConverter.class);
}
