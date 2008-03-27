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

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
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
import uk.ltd.getahead.dwr.util.JavascriptUtil;

/**
 * An implementation of Converter for Maps.
 * @author Joe Walker [joe at eireneh dot com]
 * @version $Id: StringConverter.java,v 1.2 2004/11/04 15:54:07 joe_walker Exp $
 */
public class MapConverter extends BaseV10Converter implements Converter
{
    /* (non-Javadoc)
     * @see uk.ltd.getahead.dwr.Converter#init(uk.ltd.getahead.dwr.DefaultConfiguration)
     */
    public void setConverterManager(ConverterManager newConfig)
    {
        this.config = newConfig;
    }

    /* (non-Javadoc)
     * @see uk.ltd.getahead.dwr.Converter#convertInbound(java.lang.Class, java.util.List, uk.ltd.getahead.dwr.InboundVariable, uk.ltd.getahead.dwr.InboundContext)
     */
    public Object convertInbound(Class paramType, InboundVariable iv, InboundContext inctx) throws ConversionException
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
            Map map =  null;

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

            Method method = inctx.getCurrentMethod();
            int paramNum = inctx.getCurrentParameterNum();

            // Get type info for keys
            Class keyType = config.getExtraTypeInfo(method, paramNum, 0);
            if (keyType == null)
            {
                log.warn("Missing type info for " + method.getName() + "(), param=" + paramNum + ". Assuming this is a map with String keys. Please add to <signatures> in dwr.xml"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                keyType = String.class;
            }
            else
            {
                log.debug("Using extra type info for " + method.getName() + "(), param=" + paramNum + " of " + keyType); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            }

            // Get type info for values
            Class valType = config.getExtraTypeInfo(method, paramNum, 1);
            if (valType == null)
            {
                log.warn("Missing type info for " + method.getName() + "(), param=" + paramNum + ". Assuming this is a map with String values. Please add to <signatures> in dwr.xml"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                valType = String.class;
            }
            else
            {
                log.debug("Using extra type info for " + method.getName() + "(), param=" + paramNum + " of " + valType); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            }

            // We should put the new object into the working map in case it
            // is referenced later nested down in the conversion process.
            inctx.addConverted(iv, map);
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
                    throw new ConversionException(Messages.getString("MapConverter.MissingSeparator", ConversionConstants.INBOUND_MAP_ENTRY, token)); //$NON-NLS-1$
                }

                // Convert the value part of the token by splitting it into the
                // type and value (as passed in by Javascript)
                String valStr = token.substring(colonpos + 1).trim();
                String[] splitIv = LocalUtil.splitInbound(valStr);
                InboundVariable valIv = new InboundVariable(incx, splitIv[LocalUtil.INBOUND_INDEX_TYPE], splitIv[LocalUtil.INBOUND_INDEX_VALUE]);
                Object val = config.convertInbound(valType, valIv, inctx);

                // Keys (unlike values) do not have type info passed with them
                // TODO: bug? we could have recurrsive key?
                String keyStr = token.substring(0, colonpos).trim();
                //String[] keySplit = ExecuteQuery.splitInbound(keyStr);
                //InboundVariable keyIv = new InboundVariable(incx, splitIv[ExecuteQuery.INBOUND_INDEX_TYPE], splitIv[ExecuteQuery.INBOUND_INDEX_VALUE]);
                InboundVariable keyIv = new InboundVariable(incx, ConversionConstants.TYPE_STRING, keyStr);
                Object key = config.convertInbound(keyType, keyIv, inctx);

                map.put(key, val);
            }

            return map;
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
        Map map = (Map) data;

        StringBuffer buffer = new StringBuffer();
        buffer.append("var "); //$NON-NLS-1$
        buffer.append(varname);
        buffer.append("={};"); //$NON-NLS-1$

        for (Iterator it = map.keySet().iterator(); it.hasNext();)
        {
            Object key = it.next();
            String outkey;
            if (key instanceof String)
            {
                outkey = '\'' + jsutil.escapeJavaScript((String) key) + '\'';
            }
            else
            {
                OutboundVariable ovkey = config.convertOutbound(key, outctx);
                buffer.append(ovkey.getInitCode());
                outkey = ovkey.getAssignCode();
            }

            Object value = map.get(key);
            OutboundVariable nested = config.convertOutbound(value, outctx);

            // Make sure the nested thing is declared
            buffer.append(nested.getInitCode());

            // And now declare our stuff
            buffer.append(varname);
            buffer.append('[');
            buffer.append(outkey);
            buffer.append("]="); //$NON-NLS-1$
            buffer.append(nested.getAssignCode());
            buffer.append(';');
        }

        return buffer.toString();
    }

    /**
     * The means by which we strip comments
     */
    private JavascriptUtil jsutil = new JavascriptUtil();

    /**
     * To forward marshalling requests
     */
    private ConverterManager config = null;

    /**
     * The log stream
     */
    private static final Logger log = Logger.getLogger(MapConverter.class);
}
