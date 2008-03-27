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
package org.directwebremoting.event;

import java.util.EventObject;

import org.directwebremoting.Hub;
import org.directwebremoting.extend.ConverterManager;
import org.directwebremoting.extend.InboundContext;
import org.directwebremoting.extend.InboundVariable;
import org.directwebremoting.extend.MarshallException;
import org.directwebremoting.extend.TypeHintContext;
import org.directwebremoting.io.RawData;

/**
 * An MessageEvent is fired to a set of {@link MessageListener}s by the DWR
 * {@link Hub}.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class MessageEvent extends EventObject
{
    /**
     * Constructor for use with server-side originated messages
     * @param hub The hub used to send the data
     * @param data The data to publish
     */
    public MessageEvent(Hub hub, Object data)
    {
        super(hub);

        this.hub = hub;
        this.data = data;

        source = Source.SERVER;
    }

    /**
     * Constructor for use with client-side originated messages
     * @param hub The hub used to send the data
     * @param converterManager
     * @param rawData
     */
    public MessageEvent(Hub hub, ConverterManager converterManager, RawData rawData)
    {
        super(hub);

        this.hub = hub;
        this.converterManager = converterManager;
        this.rawData = rawData;

        source = Source.INTERNET;
    }

    /**
     * @see EventObject#getSource()
     * @return the hub that processed this message
     */
    public Hub getHub()
    {
        return hub;
    }

    /**
     * We convert the data (if the message is from the client) as late as
     * possible so the message recipient can choose what type it should be.
     * @param <T> The type that we are converting to
     * @param asType The type that we are converting to
     * @return The data coerced into the required type
     * @throws MarshallException If the data can not be coerced into required type
     */
    @SuppressWarnings("unchecked")
    public <T> T getData(Class<T> asType) throws MarshallException
    {
        if (source == Source.SERVER)
        {
            try
            {
                return (T) data;
            }
            catch (ClassCastException ex)
            {
                throw new MarshallException(asType, ex);
            }
        }
        else
        {
            InboundContext context = rawData.getInboundContext();
            InboundVariable inboundVariable = rawData.getInboundVariable();
            TypeHintContext typeHintContext = null;//new TypeHintContext(converterManager, null, 0);

            return (T) converterManager.convertInbound(asType, inboundVariable, context, typeHintContext);
        }
    }

    /**
     * WARNING: This method is for internal use only. It may well disappear at
     * some stage in the future
     * Sometimes we just want to get at whatever the data was originally without
     * any conversion.
     * @return The original data probably of type RawData
     */
    public Object getRawData()
    {
        if (source == Source.SERVER)
        {
            return data;
        }
        else
        {
            return rawData;
        }
    }

    /**
     * Did the data come from the web or from the server?
     */
    private enum Source
    {
        SERVER, INTERNET
    }

    private Hub hub;
    private Object data;
    private RawData rawData;
    private Source source;
    private ConverterManager converterManager;
}
